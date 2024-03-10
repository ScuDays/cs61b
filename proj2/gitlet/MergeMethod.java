package gitlet;

import java.util.*;

public class MergeMethod {
    public static void Merge(String theBranchName) {
        /** 首先得找到最近的共同分歧Commit*/
        /** 建立一个Map，把要merge的分支加到的所有Commit加到Map里面，然后从当前分支开始一个一个往前，和Map对比，看是否在里面*/
        Set<String> BranchCommitMap = new TreeSet<>();
        BranchPointer theBranch = BranchPointer.ReadBranchPointer(theBranchName);
        /** 当前添加的分支*/
        String addBranchName = theBranchName;
        String divideCommitName = theBranch.getCurrentLocation();
        while (true) {

            BranchPointer addBranch = BranchPointer.ReadBranchPointer(addBranchName);
            ArrayList<BranchPointer.Node> AddNodeList = addBranch.NodeList;
            int i = 0;
            while (true) {
                BranchCommitMap.add(AddNodeList.get(i).CommitSha1Name);
                if (AddNodeList.get(i).CommitSha1Name.equals(divideCommitName)) {
                    addBranchName = addBranch.parentBranch;
                    divideCommitName = AddNodeList.get(0).CommitSha1Name;
                    break;
                }
                i++;
            }
            if (addBranchName.equals("master")) {
                break;
            }
        }

        /** 把master部分添加进去*/
        BranchPointer addBranch = BranchPointer.ReadBranchPointer(theBranchName);
        ArrayList<BranchPointer.Node> AddNodeList = addBranch.NodeList;

        for (int i = 0; i < AddNodeList.size(); i++) {
            BranchCommitMap.add(AddNodeList.get(i).CommitSha1Name);
            if (AddNodeList.get(i).CommitSha1Name.equals(divideCommitName)) {
                addBranchName = addBranch.parentBranch;
                divideCommitName = AddNodeList.get(0).CommitSha1Name;
                break;
            }
        }
        /** 从当前分支进行比较，找到splitCommit */
        Pointer head = Pointer.ReadPointer("head");
        String CurrentBranchPointerName = head.getCurrentBranchPointer();
        String CurrentCommit = head.getCurrentLocation();
        BranchPointer CurrentTraverseBranch = BranchPointer.ReadBranchPointer(CurrentBranchPointerName);
        ArrayList<BranchPointer.Node> CurrentTraverseList = CurrentTraverseBranch.NodeList;

        String SplitCommitName = null;
        int j = 0;
        while (true) {
            boolean ok = false;
            Iterator itr = CurrentTraverseList.iterator();
            while (itr.hasNext()) {
                BranchPointer.Node a = (BranchPointer.Node) itr.next();
                if (a.CommitSha1Name.equals(CurrentCommit)) break;
                j++;
            }
            for (int k = j; k >= 0; k--) {
                BranchPointer.Node b = CurrentTraverseList.get(k);
                String str = b.CommitSha1Name;
                if (BranchCommitMap.contains(str) == true) {
                    SplitCommitName = str;
                    ok = true;
                    break;
                }
            }
            if (ok == true) break;
        }

        /** 找到splitCommit 先进行错误判断*/

        /** 情况一：other分支的最新commit就是SplitCommit ——> 说明other分支就在当前分支上，只不过落后于当前分支
         *  相当于已经merge了，输出Given branch is an ancestor of the current branch.即可*/
        if (SplitCommitName.equals(theBranch.getCurrentLocation())) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }
        /** 情况二：当前分支的最新commit就是SplitCommit ——>说明当前分支落后于other分支，other分支就是从当前最新commit分支出去的
         *  将head设为other分支的最新commit，同时打印 Given branch is an ancestor of the current branch.即可*/
        if(head.getCurrentLocation().equals(SplitCommitName)){
            System.out.println("Current branch fast-forwarded.");
            head.setCurrentLocation(theBranch.getCurrentLocation());
            head.setPointer_Name(theBranchName);
            head.SerializeStore();
        }
    }
}
