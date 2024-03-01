package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

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

    /** 读取储存的暂存区信息 */
    public StagingArea(){
        File FileName = Utils.join(InitMethod.getInit_FOLDER(), "StagingArea");
        this.FatherMap =  (Utils.readObject(FileName, StagingArea.class)).FatherMap;
        this.AddMap =  (Utils.readObject(FileName, StagingArea.class)).AddMap;
        this.RmMap =  (Utils.readObject(FileName, StagingArea.class)).RmMap;
    }
    public StagingArea(BlobsMap FatherMap, BlobsMap AddMap, BlobsMap RmMap){
        this.FatherMap = FatherMap;
        this.AddMap = AddMap;
        this.RmMap = RmMap;
    }


    public static void Add(String BlobAbsoluteFileName) throws IOException {
        /** 路径名怎么处理 */
        /** 很奇怪 输入为.\gitlet\ddd.txt ，然后合成变成D:\cs61B\proj2\.\gitlet\ddd.txt，
         * 这样的目录可以正常的读取文件
         *
         * 解决： .指向的当前目录，所以进入D:\cs61B\proj2\后再接\.，其实还是在D:\cs61B\proj2\
         * 无影响
         */
        File addFile = Utils.join(System.getProperty("user.dir"),BlobAbsoluteFileName);
        /** 读取暂存区文件 */
        StagingArea sta = new StagingArea();
        /** 错误处理，当文件不存在的时候，打印错误信息并退出 */
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


        /** 如果文件的当前工作版本与当前提交中的版本相同，请不要暂存要添加的版本 */
        if(sta.FatherMap.Map.get(BlobAbstractFileName) != null) {
            if (sta.FatherMap.Map.get(BlobAbstractFileName).equals(BlobFileSha1Name)) System.exit(0);
        }
        /** 最后，添加进入 */
        sta.AddMap.Map.put(BlobAbstractFileName, BlobFileSha1Name);

        /** 读取要add文件的内容，写入到新生成的blob中 */
        File newBlob = Utils.join(InitMethod.getInit_FOLDER(), "blobs", BlobFileSha1Name);
        newBlob.createNewFile();
        Utils.writeContents(newBlob, readFile);

        File stafile = Utils.join(InitMethod.getInit_FOLDER(), "StagingArea");
        Utils.writeObject(stafile, sta);
    }

    public static void rm(String BlobAbsoluteFileName){

        /** 读取暂存区文件 */
        StagingArea sta = new StagingArea();

        File addFile = Utils.join(System.getProperty("user.dir"),BlobAbsoluteFileName);
        /** 获取要存储文件的名字 */
        String BlobAbstractFileName = addFile.getName();
        /** 获取存储在.gitlet里面的文件 */
        byte [] readFile = Utils.readContents(addFile);
        String BlobFileSha1Name = Utils.sha1(readFile);
        File newBlob = Utils.join(InitMethod.getInit_FOLDER(), "blobs", BlobFileSha1Name);
        /** 若暂存，则删除暂存 ，
         * TODO 同时删除文件*/
        if(sta.AddMap.Map.containsKey(BlobAbstractFileName)){
            sta.AddMap.Map.remove(BlobAbstractFileName);
            if(newBlob.exists()) {
                newBlob.delete();
                System.out.println("该文件已暂存，删除暂存区中文件");
            }
        }
        /** 若未暂存，又没有跟踪，则报错 */
        else if(!sta.FatherMap.Map.containsKey(BlobAbstractFileName)){
            System.out.println("No reason to remove the file.");
        }
        /** 若未暂存，则删除存储的文件 */
        else {
            sta.FatherMap.Map.remove(BlobAbstractFileName);
            addFile.delete();
            System.out.println("该文件未暂存，删除当前工作目录下的该文件");
        }

        File stafile = Utils.join(InitMethod.getInit_FOLDER(), "StagingArea");
        Utils.writeObject(stafile, sta);
    }

    /** 合并三个Map，Commit时要用 */
    public void Combine(){
        Iterator<Map.Entry<String, String>> itr1 = AddMap.Map.entrySet().iterator();
        while (itr1.hasNext()){
            Map.Entry<String, String> map = itr1.next();
            this.FatherMap.Map.put(map.getKey(), map.getValue());
        }
        Iterator<Map.Entry<String, String>> itr2 = RmMap.Map.entrySet().iterator();
        while (itr2.hasNext()){
            Map.Entry<String, String> map = itr2.next();
            this.FatherMap.Map.remove(map.getKey(), map.getValue());
        }
    }
}
