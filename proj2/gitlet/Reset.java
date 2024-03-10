package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class Reset {
    /** java gitlet.Main reset [commit id] */
    /** 描述
     作用：该命令用于检出（Checkout）给定提交（Commit）跟踪的所有文件。这意味着它会将你的工作目录中的文件更新为在那次提交中的状态。
     删除未跟踪文件：如果当前跟踪的文件在那次提交中不存在，这些文件将被移除。
     移动分支头：此外，该命令还会移动当前分支的头指针到该提交节点。这是通过改变当前分支头部的指向来实现的，以便指向指定的提交。
     示例：在介绍中可能有关于使用reset后头指针（head pointer）发生了什么变化的例子。
     提交ID简写：提交ID（[commit id]）可以像在checkout命令中一样被简写。
     清空暂存区：执行此命令时，暂存区（Staging Area）会被清空。
     本质：这个命令本质上是对任意提交的检出操作，同时也会改变当前分支头部的指向。 */
    public static void resetMethod(String theCommitName) throws IOException {

//        /** 如果当前分支不存在该Commit，自动报错*/
//        Pointer head = Pointer.ReadPointer("head");
//        String CurrentBranchName = head.getCurrentBranchPointer();
//        //System.out.println(CurrentBranchName);
//        BranchPointer CurrentBranch = BranchPointer.ReadBranchPointer(CurrentBranchName);
//        Iterator itr = CurrentBranch.NodeList.iterator();
//        boolean exist = false;
//        while(itr.hasNext()){
//            BranchPointer.Node a = (BranchPointer.Node) itr.next();
//            if (a.CommitSha1Name.equals(theCommitName)){
//                exist = true;
//                break;
//            }
//        }
//        if(exist == false){
//            System.out.println("No commit with that id exists.");
//
//            System.exit(0);
//        }
//        CurrentBranch.setCurrentLocation(theCommitName);
//        CurrentBranch.SerializeStore();
//        Checkout.checkoutBranch(CurrentBranchName);

        /** 如果不存在该Commit，自动报错*/
        Pointer head = Pointer.ReadPointer("head");
        File Commit_Folder = Utils.join(InitMethod.getInit_FOLDER(), Commit.getCommit_FOLDER_static());
        List<String> CommitList = Utils.plainFilenamesIn(Commit_Folder);
        Iterator itr = CommitList.iterator();
        boolean exist = false;
        while(itr.hasNext()){
            String a = (String)itr.next();
            if (a.equals(theCommitName)){
                exist = true;
                break;
            }
        }
        if(exist == false){
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit theCommit = Commit.SerializeRead(theCommitName);
        /** 获得该Commit所在的分支*/
        String theBranchName = theCommit.getInitiallyBranch();
        BranchPointer theBranch = BranchPointer.ReadBranchPointer(theBranchName);

        theBranch.setCurrentLocation(theCommitName);
        theBranch.SerializeStore();

        /**checkoutBranch中会自动更改head到指定的branch，所以不用自己更改 */
//        head.setCurrentLocation(theCommitName);
//        head.setCurrentBranchPointer(theBranchName);
//        head.SerializeStore();

        Checkout.checkoutBranch(theBranchName);

//        CurrentBranch.setCurrentLocation(theCommitName);
//        CurrentBranch.SerializeStore();
//        Checkout.checkoutBranch(CurrentBranchName);
    }
}