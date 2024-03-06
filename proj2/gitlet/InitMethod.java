package gitlet;

import jdk.jshell.execution.Util;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class InitMethod {
    // TODO: 2024/2/26  
    /** 用法： java gitlet.Main init */
    /** 说明：在当前目录中创建新的 Gitlet 版本控制系统。
     * 这个系统将自动从一个提交开始：一个不包含文件并有提交消息：initial commit 的提交（就像这样，没有标点符号）。
     * 它将有一个分支： master ，它最初指向此初始提交，并且 master 将是当前分支。
     * 此初始提交的时间戳将是 100:00:00 UTC, Thursday, 1 January 1970
     * 采用您选择的任何日期格式（这称为“（Unix） 纪元”，内部用时间 0 表示。
     * 由于 Gitlet 创建的所有仓库中的初始提交将具有完全相同的内容，因此所有仓库将自动共享此提交（它们都将具有相同的 UID），
     * 并且所有存储库中的所有提交都将追溯到它。*/

    /**
     * 失败情况：如果当前目录中已经有 Gitlet 版本控制系统，它应该中止。
     * 它不应该用新系统覆盖现有系统。应打印错误消息 ：
     * A Gitlet version-control system already exists in the current directory.
     */


    /**
     * 初始化的文件夹
     */
    private static File Init_FOLDER;

    public static File getInit_FOLDER() {
        return Init_FOLDER;
    }

    static {
        Init_FOLDER = Utils.join(System.getProperty("user.dir"), ".gitlet");
    }

    static void Init() throws IOException {
        Init_FOLDER = Utils.join(System.getProperty("user.dir"), ".gitlet");
        /** 检查是否已经存在存储库 */
        if (Init_FOLDER.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        /** 创建文件目录 */
        InitFileFolder();
        /** Date构造函数接收一个参数，该参数是从 1970 年 1 月 1 日起的毫秒数。按要求所以填0 */
        Commit firstCommit = new Commit("initial commit", new Date(0));

        /** TODO 考虑打包成Commit的一个函数 */
        /** 存储firstCommit */
        String location = firstCommit.SerializeStore();

        /** 设定Head指针指向第一个Commit的文件名 */
        /** init时候产生的Head指针 */
        Pointer Head = new Pointer(location, "head");
        Head.setCurrentBranchPointer("master");
        Head.setCurrentLocation(location);
        Head.SerializeStore();
        /** 初始化master分支，并把初始Commit加入到master分支 */
        BranchPointer master = new BranchPointer(location, "master");
        //master.add(location);
        master.SerializeStore();


        /** 初始化暂存区域并存储 */
        File stagingAreaFile = Utils.join(InitMethod.Init_FOLDER, "stagingArea");
        stagingAreaFile.createNewFile();
        StagingArea stagingArea = new StagingArea(new BlobsMap(), new BlobsMap(), new BlobsMap());
        Utils.writeObject(stagingAreaFile, stagingArea);

        /** 测试是否能够正确读取回来
        Commit readCommit = Commit.SerializeRead(location);
        System.out.println(readCommit.getMessage());
            测试成功
         */
    }


    /**
     * 初始化文件目录
     */
    static void InitFileFolder() throws IOException {
        File commits_Folder = Utils.join(InitMethod.Init_FOLDER, "commits");
        File blobs_Folder = Utils.join(InitMethod.Init_FOLDER, "blobs");
        File pointers_Folder = Utils.join(InitMethod.Init_FOLDER, "pointers");
        Init_FOLDER.mkdir();
        commits_Folder.mkdir();
        blobs_Folder.mkdir();
        pointers_Folder.mkdir();
    }
}
