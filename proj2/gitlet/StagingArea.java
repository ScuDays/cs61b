package gitlet;

import java.io.File;
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


    public void Add(String BlobFileName, String BlobFileSha1Name){
        /** 错误处理，当文件不存在的时候，打印错误信息并退出 */
        File FileName = Utils.join(InitMethod.getInit_FOLDER(), "blobs", BlobFileSha1Name);
        if (!FileName.exists()){
            System.out.println("File does not exist.");
            System.exit(0);
        }
        /** 如果文件的当前工作版本与当前提交中的版本相同，请不要暂存要添加的版本 */
        if(FatherMap.Map.get(BlobFileName).equals(BlobFileName)) System.exit(0);
        /** 最后，添加进入 */
        AddMap.Map.put(BlobFileName, BlobFileSha1Name);
    }

    public void rm(String BlobFileName, String BlobFileSha1Name){
        /** 若暂存，则删除暂存*/
        if(AddMap.Map.containsKey(BlobFileName))AddMap.Map.remove(BlobFileName);
        /** 若未暂存，则删除存储的文件 */
        if(!FatherMap.Map.containsKey(BlobFileName)){
            System.out.println("No reason to remove the file.");
        }
        else FatherMap.Map.remove(BlobFileName);
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
