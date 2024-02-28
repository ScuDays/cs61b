package gitlet;

import jdk.jshell.execution.Util;

import java.io.File;
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

    // TODO: 2024/2/26  
    /**
     * 失败情况：如果当前目录中已经有 Gitlet 版本控制系统，它应该中止。
     * 它不应该用新系统覆盖现有系统。应打印错误消息 ：
     * A Gitlet version-control system already exists in the current directory.
     */
    private static File Init_FOLDER;
    static {
        Init_FOLDER = new File(System.getProperty("user.dir"));
        Init_FOLDER = Utils.join(Init_FOLDER, ".gitlet");
    }
    static void Init() {
        /** 检查是否已经存在存储库 */
        if(Init_FOLDER.exists()){
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        /** 创建文件目录 */
        InitFileFolder();
        /** Date构造函数接收一个参数，该参数是从 1970 年 1 月 1 日起的毫秒数。按要求所以填0 */
            Commit firstCommit = new Commit("initial commit", new Date(0));
        /** init时候产生的头指针 */
            Pointer master  = new Pointer();

        //TODO 应该先把Commit储存完了再获取
        /** 设定指针指向第一个Commit的文件名 */
       Pointer a = new Pointer();
        /** 将所要存的文件 序列化 转换为字节数组 才能SHA1获得哈希值 */

        /** TODO 考虑打包成Commit的一个函数 *//*
        byte[] writeFileArr = Utils.serialize(firstCommit);
        String writeFileName = Utils.sha1(writeFileArr);
        File writeFile = Utils.join(Init_FOLDER,firstCommit.getCommit_FOLDER(), writeFileName);
        Utils.writeObject(writeFile, byte[].class);*/
        /** 存储firstCommit */
        firstCommit.SerializeStore();
        }

        /** 初始化文件目录 */
    static void InitFileFolder(){
        File commits_Folder = Utils.join(InitMethod.Init_FOLDER, "Commits");
        File blobs_Folder = Utils.join(InitMethod.Init_FOLDER, "blobs");
        File pointers_Folder = Utils.join(InitMethod.Init_FOLDER, "pointers");
        Init_FOLDER.mkdir();
        commits_Folder.mkdir();
        blobs_Folder.mkdir();
        pointers_Folder.mkdir();
    }
}