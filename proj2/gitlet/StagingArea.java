package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
    /**TODO 存在一个问题，在Commit中所存储的文件路径都是只有文件名，
     * TODO 无法反应一个真实的文件树，但好像在测试用例中无影响，用例中没有复杂结构，只存在一个平面文件结构。
     * TODO 如果后续出现问题，需要更改一下，格式化一下文件路径 */
public class StagingArea implements Serializable {
    public BlobsMap getFatherMap() {
        return FatherMap;
    }

    public BlobsMap getAddMap() {
        return AddMap;
    }

    public BlobsMap getRmMap() {
        return RmMap;
    }

    private BlobsMap FatherMap;
    private BlobsMap AddMap;
    private BlobsMap RmMap;

    /** 读取暂存区信息 */
    public StagingArea(){
        File FileName = Utils.join(InitMethod.getInit_FOLDER(), "stagingArea");
        this.FatherMap =  (Utils.readObject(FileName, StagingArea.class)).FatherMap;
        this.AddMap =  (Utils.readObject(FileName, StagingArea.class)).AddMap;
        this.RmMap =  (Utils.readObject(FileName, StagingArea.class)).RmMap;
    }
    public StagingArea(BlobsMap FatherMap, BlobsMap AddMap, BlobsMap RmMap){
        this.FatherMap = FatherMap;
        this.AddMap = AddMap;
        this.RmMap = RmMap;
    }

        public void setFatherMap(BlobsMap fatherMap) {
            FatherMap = fatherMap;
        }

        public void setAddMap(BlobsMap addMap) {
            AddMap = addMap;
        }

        public void setRmMap(BlobsMap rmMap) {
            RmMap = rmMap;
        }

        public static void Add(String BlobAbsoluteFileName) throws IOException {
        /** 路径名怎么处理 */
        /** 很奇怪 输入为.\gitlet\ddd.txt ，然后合成变成D:\cs61B\proj2\.\gitlet\ddd.txt，
         * 这样的目录可以正常的读取文件
         *
         * 解决： .指向的当前目录，所以进入D:\cs61B\proj2\后再接\.，其实还是在D:\cs61B\proj2\
         * 无影响
         */
        //System.out.println(BlobAbsoluteFileName);
        /** 读取暂存区文件 */
        StagingArea sta = new StagingArea();

        File addFile = Utils.join(System.getProperty("user.dir"),BlobAbsoluteFileName);
        /** 错误处理，当所要暂存文件不存在的时候，打印错误信息并退出 */
        if (!addFile.exists()){
            System.out.println("File does not exist.");
            System.exit(0);
        }
        /** 获取要存储文件的SHA1值 */
       /** 读取文件内容 */
        byte [] readFile = Utils.readContents(addFile);
        String BlobFileSha1Name = Utils.sha1(readFile);

        /** 获取要存储文件的名字 */
        String BlobAbstractFileName = addFile.getName();

        /** TODO 之前是同一个文件，之前rm，还没commmit，直接add，取消rm */
        if(sta.RmMap.Map.containsKey(BlobAbstractFileName)){
            String theSha1 = sta.RmMap.Map.get(BlobAbstractFileName);
            if(theSha1 == null){
                sta.RmMap.Map.remove(BlobAbstractFileName);
                /** 把暂存区域存回去 */
                File stafile = Utils.join(InitMethod.getInit_FOLDER(), "stagingArea");
                Utils.writeObject(stafile, sta);
            }
        }
        /** 如果文件的当前工作版本与当前提交中的版本相同，请不要暂存要添加的版本 */
        if(sta.FatherMap.Map.get(BlobAbstractFileName) != null) {

            if (sta.FatherMap.Map.get(BlobAbstractFileName).equals(BlobFileSha1Name)) {
                System.exit(0);
            }
        }
        /** 最后，添加进入 */
        sta.AddMap.Map.put(BlobAbstractFileName, BlobFileSha1Name);

        /** 读取要add文件的内容，写入到新生成的blob中 */
        File newBlob = Utils.join(InitMethod.getInit_FOLDER(), "blobs", BlobFileSha1Name);
        newBlob.createNewFile();
        Utils.writeContents(newBlob, readFile);

        /** 把暂存区域存回去 */
        File stafile = Utils.join(InitMethod.getInit_FOLDER(), "stagingArea");
        Utils.writeObject(stafile, sta);
    }

    public static void rm(String BlobAbsoluteFileName){

        /** 读取暂存区文件 */
        StagingArea sta = new StagingArea();
        /** addFile为工作目录里的文件 */
        File workingFile = Utils.join(System.getProperty("user.dir"), BlobAbsoluteFileName);
        /** 获取要存储文件的名字 */
        String BlobAbstractFileName = workingFile.getName();

        /** 若暂存，则删除暂存 ，
         * TODO 同时删除文件暂存的blob*/
        if(sta.AddMap.Map.containsKey(BlobAbstractFileName)){
            String BlobFileSha1Name = sta.AddMap.Map.get(BlobAbstractFileName);
            sta.AddMap.Map.remove(BlobAbstractFileName);
            File newBlob = Utils.join(InitMethod.getInit_FOLDER(), "blobs", BlobFileSha1Name);
            if(newBlob.exists()) {
                newBlob.delete();
            }
        }
        /** 若未暂存但先前已跟踪，则删除工作目录里面的文件 */
        else if(sta.FatherMap.Map.containsKey(BlobAbstractFileName)){

            if(workingFile.exists())workingFile.delete();
            String BlobFileSha1Name = sta.AddMap.Map.get(BlobAbstractFileName);
            sta.RmMap.Map.put(BlobAbstractFileName, BlobFileSha1Name);
        }
        /** 若未暂存且先前未跟踪，则打印错误消息 */
        else if(!sta.FatherMap.Map.containsKey(BlobAbstractFileName)){
            System.out.println("No reason to remove the file.");
        }
        /** 把暂存区域存回去 */
        File stafile = Utils.join(InitMethod.getInit_FOLDER(), "stagingArea");
        Utils.writeObject(stafile, sta);
    }

    /** 合并三个Map，并返回处理过之后的映射，Commit时要用 */
    public static BlobsMap Combine(){
        /** 读取暂存区文件 */
        StagingArea sta = new StagingArea();

        Iterator<Map.Entry<String, String>> itr1 = sta.AddMap.Map.entrySet().iterator();
        while (itr1.hasNext()){
            Map.Entry<String, String> map = itr1.next();
            sta.FatherMap.Map.put(map.getKey(), map.getValue());
        }
        sta.AddMap = new BlobsMap();

        Iterator<Map.Entry<String, String>> itr2 = sta.RmMap.Map.entrySet().iterator();
        while (itr2.hasNext()){
            Map.Entry<String, String> map = itr2.next();
            sta.FatherMap.Map.remove(map.getKey(), map.getValue());
        }
        sta.RmMap = new BlobsMap();

        /** 把暂存区域存回去 */
        File stafile = Utils.join(InitMethod.getInit_FOLDER(), "stagingArea");
        Utils.writeObject(stafile, sta);
        return  sta.FatherMap;

    }
}
