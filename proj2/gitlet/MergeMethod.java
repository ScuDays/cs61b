package gitlet;

import javax.imageio.ImageTranscoder;
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

        /** 获取三个commit的map，根据split commit 进行处理，*/
        // SplitCommit
        Commit SplitCommit = Commit.SerializeRead(SplitCommitName);
        BlobsMap SplitMap = SplitCommit.Map;
        //当前分支Commit
        Commit MasterCommit = Commit.SerializeRead(head.getCurrentLocation());
        BlobsMap MasterMap = MasterCommit.Map;
        //指定分支Commit
        BranchPointer other = BranchPointer.ReadBranchPointer(theBranchName);
        Commit OtherCommit = Commit.SerializeRead(other.getCurrentLocation());
        BlobsMap OtherMap = OtherCommit.Map;

        // SplitCommit 的 KeySet ， 通过遍历KeySet来处理完SplitCommit中的内容
        Set SplitMapKeySet = SplitMap.Map.keySet();
        // 看Master和Other分支是否有变化
        Boolean MasterChange = false;
        Boolean OtherChange = false;

        BlobsMap FinalMap = new BlobsMap();
        //遍历 SplitCommit
        Iterator itr = SplitMapKeySet.iterator();
        while(itr.hasNext()){
            String FileName = (String) itr.next();
            String SplitSha1Name = SplitMap.Map.get(FileName);
            /** 判断Master中是否有变化，若与Split中不同，则MasterChange设为true*/
            if(! SplitSha1Name.equals(MasterMap.Map.get(FileName)))MasterChange = true;
            if(! SplitSha1Name.equals(OtherMap.Map.get(FileName)))OtherChange= true;
            /** 开始判断*/
            /** 若两者都没变*/
            if(MasterChange == OtherChange && MasterChange == false && OtherChange == false)
                FinalMap.Map.put(FileName, SplitSha1Name);
            /** 若Master变了而Other没变，则内容根据Master改变，同时主要Master中的改变是删除了还是改变了*/
            if( MasterChange == true && OtherChange == false) {
                if(MasterMap.Map.get(FileName) != null)FinalMap.Map.put(FileName, MasterMap.Map.get(FileName));
            }
            /** 若Other变了Master没变同理*/
            if( OtherChange == true && MasterChange == false) {
                if(OtherMap.Map.get(FileName) != null)FinalMap.Map.put(FileName, OtherMap.Map.get(FileName));
            }
            /** 若Master 和 Other都变了
             *  1：看是否变化相同
             *  2：若变化不同则出问题*/
            if( OtherChange == true && MasterChange == true) {
                if(MasterMap.Map.get(FileName) == OtherMap.Map.get(FileName) && MasterMap.Map.get(FileName) == null)continue;
                if(MasterMap.Map.get(FileName) == null && OtherMap.Map.get(FileName) != null)SetConflictFileAndReportError(SplitSha1Name);
                if(OtherMap.Map.get(FileName) == null && MasterMap.Map.get(FileName) != null)SetConflictFileAndReportError(SplitSha1Name);
                if(OtherMap.Map.get(FileName).equals(MasterMap.Map.get(FileName)))FinalMap.Map.put(FileName, MasterMap.Map.get(FileName));
            }

            MasterMap.Map.remove(FileName);
            OtherMap.Map.remove(FileName);
        }
        /** 遍历处理完SplitCommit中的，现在要处理MasterCommit中和OtherCommit中不同的*/
        Set MasterKeySet = MasterMap.Map.keySet();
        Iterator MasterItr = MasterKeySet.iterator();
        while(MasterItr.hasNext()){
            String NewFileName = (String) MasterItr.next();
            /** Master中新增的文件，而OtherMap中没有*/
            if (OtherMap.Map.get(NewFileName) == null)
                FinalMap.Map.put(NewFileName, MasterMap.Map.get(NewFileName));
            /** Master中新增的文件，而OtherMap也新增该文件，但内容不同*/
            if (OtherMap.Map.get(NewFileName) != null &&  MasterMap.Map.get(NewFileName).equals(OtherMap.Map.get(NewFileName)) == false)
                SetConflictFileAndReportError(NewFileName);
            /** Master中新增的文件，而OtherMap也新增该文件，且内容不同*/
            if(OtherMap.Map.get(NewFileName) != null && MasterMap.Map.get(NewFileName).equals(OtherMap.Map.get(NewFileName)) == true)
                FinalMap.Map.put(NewFileName, MasterMap.Map.get(NewFileName));
            OtherMap.Map.remove(NewFileName);
        }
        /** 遍历处理完SplitCommit中的，再处理完MasterCommit中和OtherCommit中不同的，处理OtherCommit独有的*/
        Set OtherKeySet = OtherMap.Map.keySet();
        Iterator OtherItr = OtherKeySet.iterator();
        while (OtherItr.hasNext()){
            String OtherNewFile = (String) OtherItr.next();
            FinalMap.Map.put(OtherNewFile, OtherMap.Map.get(OtherNewFile));
        }

        /** 三次处理后，FinalMap中的就是最后内容*/



    }
    public static void SetConflictFileAndReportError(String FileSha1Name){}
}
