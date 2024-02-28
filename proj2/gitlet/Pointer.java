package gitlet;

import java.io.File;
import java.io.Serializable;

public class Pointer implements Serializable, SerializeStoreFuntion {
    /** 类介绍：该类用于 创建并存储分支指针  */

    /**
     * 存储当前指针所指向commit的哈希值
     */
    private String CurrentLocation;
    /**
     * Pointer所存储的文件夹
     */
    private static String Pointer_FOLDER = "pointers";

    /**
     * 指针名字
     */
    private String Pointer_Name;

    public Pointer(String CurrentLocation, String Pointer_Name) {
        this.CurrentLocation = CurrentLocation;
        this.Pointer_Name = Pointer_Name;
    }

    public String getPointer_Name() {
        return Pointer_Name;
    }

    public void setPointer_Name(String pointer_Name) {
        Pointer_Name = pointer_Name;
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

    /**
     * 序列化存储Pointer 并返回文件名
     */
    @Override
    public String SerializeStore() {
        File writeFile = Utils.join(System.getProperty("user.dir"), ".gitlet", this.getPointer_FOLDER(), this.Pointer_Name);
        Utils.writeObject(writeFile, String.class);
        return this.Pointer_Name;
    }
}
