package gitlet;

public class Pointer {
    /** 类介绍：该类用于 创建并存储分支指针  */

    /** 存储当前指针所指向commit的哈希值 */
    private String CurrentLocation;
    public Pointer(){
    }
    public Pointer(String CurrentLocation){
        this.CurrentLocation = CurrentLocation;
    }
}
