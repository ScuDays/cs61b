package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Pointer implements Serializable, SerializeStoreFuntion {
    /** 类介绍：该类用于 创建并存储分支指针  */

    /**
     * 存储当前指针所指向commit的哈希值
     */
    private String CurrentLocation;

    public void setCurrentBranchPointer(String currentBranchPointer) {
        CurrentBranchPointer = currentBranchPointer;
    }

    /**
     * 存储当前指针所指向的分支名字
     */
    private String CurrentBranchPointer;
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

    public String getCurrentBranchPointer() {
        return CurrentBranchPointer;
    }

    public String getCurrentLocation() {
        return CurrentLocation;
    }

    public String getPointer_FOLDER() {
        return Pointer_FOLDER;
    }
    public static String getPointer_FOLDER_static() {
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
        File writeFile = Utils.join(InitMethod.getInit_FOLDER(), this.getPointer_FOLDER(), this.Pointer_Name);
        Utils.writeObject(writeFile, this);
        return this.Pointer_Name;
    }
    public static Pointer ReadPointer(String PointerName){
        File PointerFile = Utils.join(InitMethod.getInit_FOLDER(), Pointer.Pointer_FOLDER, PointerName);
        if(PointerFile.exists() == false) System.out.println("PointerFile不存在");
       // System.out.println("测试： 这里是PointerFile地址"+PointerFile);
        return Utils.readObject(PointerFile, Pointer.class);
    }
}
