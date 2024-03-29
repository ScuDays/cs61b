package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.Iterator;
import java.util.Set;

/**
 * Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author TODO
 */
public class Commit implements Serializable, SerializeStoreFuntion {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    /**
     * 日志消息
     */
    private String message;
    /**
     * 时间
     */
    private Date commitDate;
    /**
     * 父引用 通过名字找到父引用
     */
    private String parent;
    /**
     * Commit 序列化存储的文件夹
     */
    private static String Commit_FOLDER = "commits";
    private String MergeBranchParent;
    /**
     * 暂存区域——文件名与其对应的文件版本
     */
    private BlobsMap ActualBlobsMap;
    /**
     * 储存的文件名
     */
    private String sha1Name;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCommitDate(Date commitDate) {
        this.commitDate = commitDate;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public static void setCommit_FOLDER(String commit_FOLDER) {
        Commit_FOLDER = commit_FOLDER;
    }

    public void setActualBlobsMap(BlobsMap actualBlobsMap) {
        ActualBlobsMap = actualBlobsMap;
    }

    public void setSha1Name(String sha1Name) {
        this.sha1Name = sha1Name;
    }

    public void setInitiallyBranch(String initiallyBranch) {
        InitiallyBranch = initiallyBranch;
    }

    public void setMap(BlobsMap map) {
        Map = map;
    }

    private String InitiallyBranch;

    public String getInitiallyBranch() {
        return InitiallyBranch;
    }

    public String getMessage() {
        return message;
    }

    public String getCommit_FOLDER() {
        return Commit_FOLDER;
    }

    public static String getCommit_FOLDER_static() {
        return Commit_FOLDER;
    }

    public String getMergeBranchParent() {
        return MergeBranchParent;
    }

    /**
 * 用法： java gitlet.Main commit [message]
 * 说明：将跟踪文件的快照保存在当前提交和暂存区域中，以便以后可以还原它们，
 * 从而创建新的提交。据说提交正在跟踪保存的文件。默认情况下，
 * 每个提交的文件快照将与其父提交的文件快照完全相同;它将保持文件版本的原样，
 * 而不是更新它们。提交将仅更新它正在跟踪的文件的内容，这些文件在提交时已暂存以添加，
 * 在这种情况下，提交现在将包括暂存的文件版本，而不是从其父级获取的版本。
 * 提交将保存并开始跟踪已暂存以添加但未由其父级跟踪的任何文件。
 * 最后，在当前提交中跟踪的文件可能会在新提交中取消跟踪，因为 rm 命令（如下）会暂存以删除这些文件。
 * <p>
 * 底线：默认情况下，提交的文件内容与其父级相同。暂存添加和删除的文件是对提交的更新。
 * 当然，日期（可能还有消息）也会与父项不同。
 */

    public void setMergeBranchParent(String mergeBranchParent) {
        MergeBranchParent = mergeBranchParent;
    }

    /**
     * 在构造函数中一次性把序列化的各个过程都完成
     */


    public Commit(String message, Date commitDate) {
        this.message = message;
        this.commitDate = commitDate;
    }

    /** TODO: 2024/2/27 commit 需要存储当前版本指向的哈希名文件 每次都要更新，用什么数据结构更新速度快呢?   */
    /**
     * 使用了Treemap来实现，Treemap在StagingArea中
     */
    BlobsMap Map = new BlobsMap();


    /**
     * TODO: Commit
     * /** 包装一个Commit存储到本地的方法，同时返回文件名既文件的sha1值
     */
    @Override
    public String SerializeStore() {
        /** TODO 考虑打包成Commit的一个函数 */
        byte[] writeFileArr = Utils.serialize(this);
        String writeFileName = Utils.sha1(writeFileArr);
        File writeFile = Utils.join(InitMethod.getInit_FOLDER(), this.getCommit_FOLDER(), writeFileName);
        Utils.writeObject(writeFile, this);
        return writeFileName;
    }

    /**
     * 实现序列化读取
     */
    public static Commit SerializeRead(String ReadFileName) {
        File ReadFile = Utils.join(InitMethod.getInit_FOLDER(), Commit.Commit_FOLDER, ReadFileName);
        /** 当不存在该Sha1名字的Commit时，报错并退出 */
        if (ReadFile.exists() == false) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit readCommit = Utils.readObject(ReadFile, Commit.class);
        return readCommit;
    }

    public String getParent() {
        return parent;
    }

    public Date getCommitDate() {
        return commitDate;
    }

    /**
     * TODO Commit方法的实现
     */
    public static void CommitMethod(String message) {

        /** 判断暂存区是否无文件，若无则报错退出*/
        StagingArea sta = new StagingArea();
        BlobsMap addmap = sta.getAddMap();
        BlobsMap rmmap = sta.getRmMap();
        if ((addmap.Map.isEmpty() == true) && (rmmap.Map.isEmpty() == true)) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        /** 创建新commit */
        Commit theCommit = new Commit(message, new Date());

        /** 读取head的信息 */
        Pointer head = Pointer.ReadPointer("head");
        String CurrentBranch = head.getCurrentBranchPointer();
        String ParentSh1Name = head.getCurrentLocation();
        /** 修改commit映射 并 存储新的Commit*/
        BlobsMap BlMap = StagingArea.Combine();
        theCommit.Map = BlMap;
        theCommit.parent = ParentSh1Name;
        theCommit.InitiallyBranch = CurrentBranch;
        String Sha1Name = theCommit.SerializeStore();
        /** 设置并存储head */
        head.setCurrentLocation(Sha1Name);
        head.SerializeStore();
        /** 读取、设置并存储分支指针 */
        BranchPointer branchPointer = BranchPointer.ReadBranchPointer(CurrentBranch);
        branchPointer.add(Sha1Name);
        branchPointer.SerializeStore();

//        Set a = theCommit.Map.Map.keySet();
//        Iterator itr1 = a.iterator();
//        while(itr1.hasNext()) System.out.println(itr1.next());
    }
    /* TODO: fill in the rest of this class. */

}
