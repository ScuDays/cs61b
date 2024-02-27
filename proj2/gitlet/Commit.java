package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class

/**
 * Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author TODO
 */
public class Commit implements Serializable {
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
    /** 父引用 通过名字找到父引用 */
    private String parent;
    /** Commit 序列化存储的文件夹 */
    private String Commit_FOLDER;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(Date commitDate) {
        this.commitDate = commitDate;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getCommit_FOLDER() {
        return Commit_FOLDER;
    }

    public void setCommit_FOLDER(String commit_FOLDER) {
        Commit_FOLDER = commit_FOLDER;
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

    /** 在构造函数中一次性把序列化的各个过程都完成 */
    public Commit(String message, Date commitDate) {
        this.message = message;
        this.commitDate = commitDate;
    }
    public static void CommitWrite(){

    }
    /** TODO: 2024/2/27 commit 需要存储当前版本指向的哈希名文件 每次都要更新，用什么数据结构更新速度快呢?   */

    /** TODO: Commit

    /* TODO: fill in the rest of this class. */
}
