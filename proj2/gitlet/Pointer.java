package gitlet;

import java.io.File;
import java.io.Serializable;

public class Pointer implements Serializable, SerializeStoreFuntion {
    /** 类介绍：该类用于 创建并存储分支指针  */

    /** 存储当前指针所指向commit的哈希值 */
    private String CurrentLocation;
    /** Pointer存储文件夹 */
    private String Pointer_FOLDER;
    public Pointer(){
    }
    public Pointer(String CurrentLocation){
        this.CurrentLocation = CurrentLocation;
    }

    public String getCurrentLocation() {
        return CurrentLocation;
    }

    public String getPointer_FOLDER() {
        return Pointer_FOLDER;
    }

    public void setCurrentLocation(String currentLocation) {
        CurrentLocation = currentLocation;
    }

    /** 序列化存储Pointer */
    @Override
    public void SerializeStore() {
        byte[] writeFileArr = Utils.serialize(this);
        String writeFileName = Utils.sha1(writeFileArr);
        File writeFile = Utils.join(System.getProperty("user.dir"),".gitlet", this.getPointer_FOLDER(), writeFileName);
        Utils.writeObject(writeFile, byte[].class);
    }
}
