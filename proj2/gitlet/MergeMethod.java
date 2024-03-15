package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MergeMethod {
    public static void Merge(String theBranchName) throws IOException {

        /** 错误处理*/
        Pointer head1 = Pointer.ReadPointer("head");
        /** 1：给定分支是自身*/
        if (theBranchName.equals(head1.getCurrentBranchPointer())) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        /** 2：存在已暂存但未commit文件*/
        StagingArea sta = new StagingArea();
        if(!sta.getRmMap().Map.isEmpty()){
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        if(!sta.getAddMap().Map.isEmpty()){
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        /*if(BranchPointer.ReadBranchPointer())*/
        /** 3：不存在给定分支，报错，笨方法*/
        File Point_Folder = Utils.join(InitMethod.getInit_FOLDER(), BranchPointer.getPointer_FOLDER_static());
        List PointList = Utils.plainFilenamesIn(Point_Folder);
        if(PointList.contains(theBranchName) == false){
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
//        // TODO
        /** 4：存在未跟踪文件，且文件会被merge之后覆盖或者删除*/
        File File_Folder1 = Utils.join(InitMethod.getUser_FOLDER());
        List FileList1 = Utils.plainFilenamesIn(File_Folder1);
        Iterator FileItr = FileList1.iterator();
        Commit CurrentCommit1 = Commit.SerializeRead(head1.getCurrentLocation());
         BlobsMap CurrentCommit1Map = CurrentCommit1.Map;

//        System.out.println(theBranchName);
        BranchPointer otherBranch = BranchPointer.ReadBranchPointer(theBranchName);
        Commit otherCommit = Commit.SerializeRead(otherBranch.getCurrentLocation());
        BlobsMap otherCommitMap = otherCommit.Map;
        while(FileItr.hasNext()){
            String fileName = (String) FileItr.next();
            File theFile = Utils.join(InitMethod.getUser_FOLDER(), fileName);
            byte [] arr = Utils.readContents(theFile);
            String theFileSHA1Name = Utils.sha1(arr);
            if(CurrentCommit1Map.Map.containsKey(fileName)){
                if(theFileSHA1Name.equals(CurrentCommit1Map.Map.get(fileName)))
                    continue;
            }
            if(otherCommitMap.Map.containsKey(fileName)){
                if(theFileSHA1Name.equals(otherCommitMap.Map.get(fileName)))
                    continue;
            }
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            System.exit(0);
        }


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
        if (head.getCurrentLocation().equals(SplitCommitName)) {
            System.out.println("Current branch fast-forwarded.");
            head.setCurrentLocation(theBranch.getCurrentLocation());
            head.setPointer_Name(theBranchName);
            head.SerializeStore();
            Checkout.checkoutBranch(theBranchName);
        }

        /** 获取三个commit的map，根据split commit 进行处理，*/
        // System.out.println("SPlitCommit 是" + SplitCommitName);
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


        BlobsMap FinalMap = new BlobsMap();
        Set ConflictSet = new TreeSet();
        //遍历 SplitCommit
        Iterator itr = SplitMapKeySet.iterator();
        boolean ifConflict = false;
        while (itr.hasNext()) {
            Boolean MasterChange = false;
            Boolean OtherChange = false;
            String FileName = (String) itr.next();
            String SplitSha1Name = SplitMap.Map.get(FileName);
            /** 判断Master中是否有变化，若与Split中不同，则MasterChange设为true*/
            if ((SplitSha1Name.equals(MasterMap.Map.get(FileName))) == false) {
                MasterChange = true;
            }
            if ((SplitSha1Name.equals(OtherMap.Map.get(FileName))) == false) {
                OtherChange = true;
            }

//            System.out.println(FileName);
//            System.out.println("SplitSha1Name:" +SplitSha1Name + "   MasterMap:"+MasterMap.Map.get(FileName));
//            System.out.println("SplitSha1Name:" +SplitSha1Name + "   OtherMap:"+OtherMap.Map.get(FileName));
//            System.out.println("-----------------------------");
//            if(MasterChange == true && OtherChange == true) System.out.println(FileName + "都改变了");
            /** 开始判断*/
            /** 若两者都没变*/
            if (MasterChange == OtherChange && MasterChange == false && OtherChange == false)
                FinalMap.Map.put(FileName, SplitSha1Name);


            /** 变了其中一个*/

            /** 若Master变了而Other没变，则内容根据Master改变。 两种情况，Master删除了，Master没删除*/
            if (MasterChange == true && OtherChange == false) {
                if (MasterMap.Map.get(FileName) != null) FinalMap.Map.put(FileName, MasterMap.Map.get(FileName));
//                else {
//                    RmSet.add(FileName);
//                }
            }
            /** 若Other变了而Master没变，则内容根据Other改变。 两种情况，Other删除了，Other没删除*/
            if (OtherChange == true && MasterChange == false) {
                if (OtherMap.Map.get(FileName) != null) FinalMap.Map.put(FileName, OtherMap.Map.get(FileName));
//                else {
//                    RmSet.add(FileName);
//                }
            }

            /** 两个都变了*/
            /** 若Master 和 Other都变了
             *  1：看是否变化相同
             *  2：若变化不同则出问题*/
            if (OtherChange == true && MasterChange == true) {
                /**  两个都变化后，两个相同*/
                if (OtherMap.Map.get(FileName) == (MasterMap.Map.get(FileName))) {
                    FinalMap.Map.put(FileName, MasterMap.Map.get(FileName));
                }
                /** 两个都变化后，两个相同且都删除了*/
//                if (MasterMap.Map.get(FileName) == OtherMap.Map.get(FileName) && MasterMap.Map.get(FileName) == null) {
//                    RmSet.add(FileName);
//                }

                /** 两个都变化后，一个删除一个没删除*/
                if (MasterMap.Map.get(FileName) == null && OtherMap.Map.get(FileName) != null) {
                    ifConflict = true;
                    ConflictSet.add(FileName);
                    SetConflictFileAndReportError(MasterMap.Map.get(FileName), OtherMap.Map.get(FileName), FileName);
                }
                /** 两个都变化后，一个删除一个没删除*/
                if (OtherMap.Map.get(FileName) == null && MasterMap.Map.get(FileName) != null) {
                    ifConflict = true;
                    ConflictSet.add(FileName);
                    SetConflictFileAndReportError(MasterMap.Map.get(FileName), OtherMap.Map.get(FileName), FileName);
                }
                //FinalMap.Map.put(FileName, MasterMap.Map.get(FileName));

                /**  两个都变化后，两个都不相同*/
                if (OtherMap.Map.get(FileName) != MasterMap.Map.get(FileName)) {
                    ifConflict = true;
                    ConflictSet.add(FileName);
                    SetConflictFileAndReportError(MasterMap.Map.get(FileName), OtherMap.Map.get(FileName), FileName);
                }
            }
            MasterMap.Map.remove(FileName);
            OtherMap.Map.remove(FileName);
        }
        /** 遍历处理完SplitCommit中的，现在要处理MasterCommit中和OtherCommit中不同的*/
        Set MasterKeySet = MasterMap.Map.keySet();
        Iterator MasterItr = MasterKeySet.iterator();
        while (MasterItr.hasNext()) {
            String NewFileName = (String) MasterItr.next();
            /** Master中新增的文件，而OtherMap中没有*/
            if (OtherMap.Map.get(NewFileName) == null)
                FinalMap.Map.put(NewFileName, MasterMap.Map.get(NewFileName));
            /** Master中新增的文件，而OtherMap也新增该文件，但内容不同*/
            if (OtherMap.Map.get(NewFileName) != null && MasterMap.Map.get(NewFileName).equals(OtherMap.Map.get(NewFileName)) == false) {
                ifConflict = true;
                ConflictSet.add(NewFileName);
                SetConflictFileAndReportError(MasterMap.Map.get(NewFileName), OtherMap.Map.get(NewFileName), NewFileName);
            }

            /** Master中新增的文件，而OtherMap也新增该文件，且内容相同*/
            if (OtherMap.Map.get(NewFileName) != null && MasterMap.Map.get(NewFileName).equals(OtherMap.Map.get(NewFileName)) == true)
                FinalMap.Map.put(NewFileName, MasterMap.Map.get(NewFileName));

            OtherMap.Map.remove(NewFileName);
        }
        /** 遍历处理完SplitCommit中的，再处理完MasterCommit中和OtherCommit中不同的，处理OtherCommit独有的*/
        Set OtherKeySet = OtherMap.Map.keySet();
        Iterator OtherItr = OtherKeySet.iterator();
        while (OtherItr.hasNext()) {
            String OtherNewFile = (String) OtherItr.next();
            FinalMap.Map.put(OtherNewFile, OtherMap.Map.get(OtherNewFile));
        }

        /** 三次处理后，FinalMap中的就是最后内容*/
        File File_Folder = Utils.join(InitMethod.getUser_FOLDER());
        List FileList = Utils.plainFilenamesIn(File_Folder);
        Iterator FileListItr = FileList.listIterator();
        while (FileListItr.hasNext()) {
            String theFileName = (String) FileListItr.next();
            File theFile = Utils.join(InitMethod.getUser_FOLDER(), theFileName);
            if (FinalMap.Map.containsKey(theFileName) == true || ConflictSet.contains(theFileName) == true) {
                continue;
            } else theFile.delete();
        }

        Set FinalSet = FinalMap.Map.keySet();
        Iterator FinalItr = FinalSet.iterator();
        while (FinalItr.hasNext()) {
            String theFileName = (String) FinalItr.next();
            //System.out.println(theFileName);
            if(FinalMap.Map.get(theFileName) == null)continue;
            File SourceFile = Utils.join(InitMethod.getInit_FOLDER(), "blobs", FinalMap.Map.get(theFileName));
            File theFile = Utils.join(InitMethod.getUser_FOLDER(), theFileName);
            byte[] SourceArr = Utils.readContents(SourceFile);
            Utils.writeContents(theFile, SourceArr);
        }
        if (ifConflict == true) System.out.println("Encountered a merge conflict.");

        String message = "Merged " + theBranchName + " into " + CurrentBranchPointerName + ".";
        /** 创建新commit */
        Commit theCommit = new Commit(message, new Date());
        /** 读取head的信息 */
        String CurrentBranch = head.getCurrentBranchPointer();
        String ParentSh1Name = head.getCurrentLocation();
        /** 修改commit映射 并 存储新的Commit*/
        BlobsMap BlMap = StagingArea.Combine();
        theCommit.Map = BlMap;
        theCommit.setParent(ParentSh1Name);
        theCommit.setInitiallyBranch(CurrentBranch);
        theCommit.setMergeBranchParent(theBranch.getCurrentLocation());

        String Sha1Name = theCommit.SerializeStore();
        /** 设置并存储head */
        head.setCurrentLocation(Sha1Name);
        head.SerializeStore();
        /** 读取、设置并存储分支指针 */
        BranchPointer branchPointer = BranchPointer.ReadBranchPointer(CurrentBranch);
        branchPointer.add(Sha1Name);
        branchPointer.SerializeStore();

    }

    public static void SetConflictFileAndReportError(String CurrentBranchFileSha1Name, String givenBranchFileSha1Name, String FileName) throws IOException {
        String CurrentBranchContent = new String();
        String givenBranchContent = new String();
        // String FinalFileName = new String();
        if (CurrentBranchFileSha1Name == null && givenBranchFileSha1Name != null) {
            File givenBranchFile = Utils.join(InitMethod.getInit_FOLDER(), "blobs", givenBranchFileSha1Name);
            givenBranchContent = Utils.readContentsAsString(givenBranchFile);
            //FinalFileName = givenBranchFileSha1Name;
        }
        if (givenBranchFileSha1Name == null && CurrentBranchFileSha1Name != null) {
            File CurrentBranchFile = Utils.join(InitMethod.getInit_FOLDER(), "blobs", CurrentBranchFileSha1Name);
            ;
            CurrentBranchContent = Utils.readContentsAsString(CurrentBranchFile);
            //FinalFileName = CurrentBranchFileSha1Name;
        }
        if (givenBranchFileSha1Name != null && CurrentBranchFileSha1Name != null) {
            File CurrentBranchFile = Utils.join(InitMethod.getInit_FOLDER(), "blobs", CurrentBranchFileSha1Name);
            ;
            File givenBranchFile = Utils.join(InitMethod.getInit_FOLDER(), "blobs", givenBranchFileSha1Name);
            CurrentBranchContent = Utils.readContentsAsString(CurrentBranchFile);
            givenBranchContent = Utils.readContentsAsString(givenBranchFile);
            //FinalFileName = CurrentBranchFileSha1Name;
        }

        String ConflictFileContent = "<<<<<<< HEAD\n" +
                CurrentBranchContent +
                "=======\n" +
                givenBranchContent +
                ">>>>>>>\n";


        File FinalFile = Utils.join(FileName);
        if (FinalFile.exists() == false) FinalFile.createNewFile();
        Utils.writeContents(FinalFile, ConflictFileContent);
//        System.out.println(FinalFile + "这里是Conflict处理");
//
//        System.out.println("Encountered a merge conflict.");

    }
}
