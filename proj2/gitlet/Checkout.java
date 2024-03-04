package gitlet;

import java.io.File;
import java.io.IOException;

public class Checkout {
    /** 三种用途 */

    /** TODO 1: java gitlet.Main checkout -- [file name]
     * 获取头提交中存在的文件版本并将其放入工作目录中，覆盖已存在的文件版本（如果存在）。该文件的新版本未暂存。*/

    /** TODO 2:java gitlet.Main checkout [commit id] -- [file name]
     * 获取具有给定 id 的提交中存在的文件版本，并将其放入工作目录中，覆盖已存在的文件版本（如果存在）。该文件的新版本未暂存。*/

    /** TODO 3:java gitlet.Main checkout [branch name]
     * 获取给定分支头部提交中的所有文件，并将它们放入工作目录中，覆盖已存在的文件的版本（如果存在）。
     * 此外，在此命令结束时，给定分支现在将被视为当前分支 (HEAD)。当前分支中跟踪但不存在于签出分支中的任何文件都将被删除。
     * 暂存区域将被清除，除非签出的分支是当前分支（请参阅下面的失败案例）*/

    /**
     * 失败案例：
     * 1：如果上次提交时该文件不存在，则中止，打印错误消息 File does not exist in that commit. 不要更改CWD。
     * 2：如果不存在给定 id 的提交，则打印 No commit with that id exists.
     * 否则，如果给定提交中不存在该文件，则打印与失败情况 1 相同的消息。不要更改 CWD。
     * 3：如果不存在具有该名称的分支，则打印 No such branch exists.
     * 如果该分支是当前分支，则打印 No need to checkout the current branch.
     * 如果工作文件在当前分支中未跟踪并且将被签出覆盖，
     * 打印 There is an untracked file in the way; delete it, or add and commit it first. 并退出；
     * 在执行其他操作之前执行此检查。不要更改 CWD。
     */

    public static void checkoutFileName(String FileName) throws IOException {
        Pointer head = Pointer.ReadPointer("head");
        String Sha1Name = head.getCurrentLocation();

        Checkout.checkoutCommitFileName(Sha1Name, FileName);
    }
    public static void checkoutCommitFileName(String CommitSha1Name, String FileName) throws IOException {
        /** 如果读取失败，自动报错 */
        Commit theCommit = Commit.SerializeRead(CommitSha1Name);
//        System.out.println(CommitSha1Name);
//        System.out.println(FileName);

        BlobsMap theMap = theCommit.Map;
        /** 如果对应提交中不存在该文件，报错 */
        if (theMap.Map.containsKey(FileName) == false) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }

        String SourceBlobSha1Name = theMap.Map.get(FileName);
        /** 读取源文件的内容*/
        File SourceBlob = Utils.join(InitMethod.getInit_FOLDER(), "blobs", SourceBlobSha1Name );
        byte [] SourceFile = Utils.readContents(SourceBlob);
        /** 本地工作目录待改文件 */
        File changeFile = Utils.join(System.getProperty("user.dir"), FileName);
        /** 若当地文件不存在，则创建新文件 */
        if(changeFile.exists() == false)changeFile.createNewFile();
        /** 把指定的文件写到本地工作目录中 */
        Utils.writeContents(changeFile, SourceFile);
    }


}




