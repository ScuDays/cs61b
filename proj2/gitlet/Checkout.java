package gitlet;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Checkout {
    /** 三种用途 */

    /** TODO 1: java gitlet.Main checkout -- [filmake name]
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

        BlobsMap theMap = theCommit.Map;
        /** 如果对应提交中不存在该文件，报错 */
        if (theMap.Map.containsKey(FileName) == false) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }

        String SourceBlobSha1Name = theMap.Map.get(FileName);
        /** 读取源文件的内容*/
        File SourceBlob = Utils.join(InitMethod.getInit_FOLDER(), "blobs", SourceBlobSha1Name);
        byte[] SourceFile = Utils.readContents(SourceBlob);
        /** 本地工作目录待改文件 */
        File changeFile = Utils.join(System.getProperty("user.dir"), FileName);
        /** 若当地文件不存在，则创建新文件 */
        if (changeFile.exists() == false) changeFile.createNewFile();
        /** 把指定的文件写到本地工作目录中 */
        Utils.writeContents(changeFile, SourceFile);
    }

    /**
     * java gitlet.Main checkout [branch name]
     */
    public static void checkoutBranch(String theBranchName) throws IOException {
        /** 如果该分支是当前分支，则报错*/
        Pointer head = Pointer.ReadPointer("head");
        /** 当前分支名字*/
        String CurrentBranchName = head.getCurrentBranchPointer();
        BranchPointer CurrentBranch = BranchPointer.ReadBranchPointer(CurrentBranchName);
        if (CurrentBranchName.equals(theBranchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        /** 如果不存在具有该名称的分支，则自动打印 No such branch exists.*/
        BranchPointer theBranch = BranchPointer.ReadBranchPointer(theBranchName);


        /** 如果工作文件在当前分支中未跟踪并且将被签出覆盖，
         * 打印 There is an untracked file in the way; delete it, or add and commit it first.
         * 并退出
         文件名既被Commit3B追踪的文件，也被Commit3A追踪，那么对于相同文件名但blobID不同（也就是内容不同），
         * 则用Commit3B种的文件来替代原来的文件；相同文件名并且blobID相同，不进行任何操作。

         文件名不被Commit3B追踪的文件，而仅被Commit3A追踪，那么直接删除这些文件。

         文件名仅被Commit3B追踪的文件，而不被Commit3A追踪，那么直接将这些文件写入到工作目录。

         这里有个例外，即对于第三种情况，将要直接写入的时候如果有同名文件（例如1.txt）已经在工作目录中了，
         说明工作目录中在执行checkout前增加了新的1.txt文件而没有commit，
         这时候gitlet不知道是应该保存用户新添加进来的1.txt还是把Commit3B中的1.txt拿过来overwrite掉，
         为了避免出现信息丢失，gitlet就会报错，
         输出There is an untracked file in the way; delete it, or add and commit it first.*/

        // TODO
        /** 获取目的Commit和当前Commit*/
        String theCommitName = theBranch.getCurrentLocation();
        String CurrentCommitName = head.getCurrentLocation();
        //String CurrentCommitName = CurrentBranch.getCurrentLocation();

        Commit theCommit = Commit.SerializeRead(theCommitName);
        Commit CurrentCommit = Commit.SerializeRead(CurrentCommitName);
        /** 获取目的映射和当前映射*/
        BlobsMap theMap = theCommit.Map;
        BlobsMap CurrentMap = CurrentCommit.Map;

        File Repository_Folder = new File(InitMethod.getUser_FOLDER());
        /** 存储库文件集合 */
        List<String> FileList = Utils.plainFilenamesIn(Repository_Folder);

        /** 看存储库是否存在未跟踪文件 */
        Iterator itr = FileList.iterator();
        while (itr.hasNext()) {
            String fileName = (String) itr.next();
            File theFile = Utils.join(InitMethod.getUser_FOLDER(), fileName);
            byte[] byteArr = Utils.readContents(theFile);
            String theFileSha1 = Utils.sha1(byteArr);

            /** 存在未跟踪文件 */
            if (CurrentMap.Map.containsKey(theFile.getName()) == false) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
            String Sha1 = CurrentMap.Map.get(fileName);

            /** 存在跟踪文件但更改未Commit */
            if (!Sha1.equals(theFileSha1)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
            /** 若已跟踪，当不是指定Commit中的文件，则删除 */
            if(theMap.Map.containsKey(fileName) == false)theFile.delete();
        }

        /** 把指定commit的内容全部写到工作目录 */
        Set<String> theCommitKeyset = theCommit.Map.Map.keySet();
        for (String key : theCommitKeyset) {
            File theFile = Utils.join(InitMethod.getUser_FOLDER(), key);
            /** 如果文件存在，且就是目的Commit中文件 不用更改*/
            if (theFile.exists()) {
                byte[] byteArr = Utils.readContents(theFile);
                String SHA1 = Utils.sha1(byteArr);
                if (SHA1.equals(theCommit.Map.Map.get(key))) continue;
            }
            /** 若文件不存在，则创建 */
            if (theFile.exists() == false) theFile.createNewFile();
            /** 读取源文件的内容*/
            File SourceBlob = Utils.join(InitMethod.getInit_FOLDER(), "blobs", theCommit.Map.Map.get(key));
            byte[] SourceFile = Utils.readContents(SourceBlob);
            /** 本地工作目录待改文件 */
            File changeFile = Utils.join(InitMethod.getUser_FOLDER(), key);
            /** 若当地文件不存在，则创建新文件 */
            if (changeFile.exists() == false) changeFile.createNewFile();
            /** 把指定的文件写到本地工作目录中 */
            Utils.writeContents(changeFile, SourceFile);
        }
        head.setCurrentBranchPointer(theBranchName);
        head.setCurrentLocation(theCommitName);
        head.SerializeStore();

        StagingArea sta = new StagingArea();
        sta.setFatherMap(theMap);
        sta.setAddMap(new BlobsMap());
        sta.setRmMap(new BlobsMap());
        /** 把暂存区域存回去 */
        File stafile = Utils.join(InitMethod.getInit_FOLDER(), "stagingArea");
        Utils.writeObject(stafile, sta);
    }

}




