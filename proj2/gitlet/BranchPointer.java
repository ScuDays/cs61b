package gitlet;

import java.awt.*;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class BranchPointer extends Pointer implements Serializable {
    ArrayList<Node> NodeList;
    public String parentBranch = "master";
    //private int branchNum;

    public BranchPointer(String CurrentLocation, String Pointer_Name) {
        super(CurrentLocation, Pointer_Name);
        this.NodeList = new ArrayList<>();
        this.NodeList.add(new Node(CurrentLocation));
    }

    public void add(String CommitSha1Name) {
        this.NodeList.add(new Node(CommitSha1Name));
        this.setCurrentLocation(CommitSha1Name);
    }

    public static void Branch(String BranchName) {
        /** 检查是否存在该分支，若已经存在该分支，则报错并退出 */
        File Pointer_Folder = Utils.join(InitMethod.getInit_FOLDER(), Pointer.getPointer_FOLDER_static());
        List<String> PointerList = Utils.plainFilenamesIn(Pointer_Folder);
        Iterator pointerItr = PointerList.iterator();
        boolean isExist = false;
        while (pointerItr.hasNext()) {
            String pointerName = (String) pointerItr.next();
            if (pointerName.equals(BranchName)) {
                System.out.printf("A branch with that name already exists.");
                isExist = true;
                break;
            }
        }
        if (isExist == true) System.exit(0);
        /** 读取head指针来获取当前分支指针*/
        Pointer head = Pointer.ReadPointer("head");
        /** 读取当前分支指针*/
        String CurrentBranchName = head.getCurrentBranchPointer();
        BranchPointer CurrentBranch = BranchPointer.ReadBranchPointer(CurrentBranchName);
        /** 在当前分支指针处添加新的分支 */
        CurrentBranch.newBranch(BranchName);
        /** 把当前分支指针存回去*/
        CurrentBranch.SerializeStore();
    }

    public void newBranch(String BranchName) {
//        /** 找到当前指向Commit在NodeList中的位置 */
//        int index = NodeList.indexOf(this.getCurrentLocation());
        /** 找到该所要添加Branch位置的Node */
        int index = this.NodeList.size();
        index--;
        Node currentNode = this.NodeList.get(index);
        BranchPointer addBranch = new BranchPointer(this.getCurrentLocation(), BranchName);
        /** 在该位置添加分支 */
        currentNode.sonBranch.add(addBranch.getPointer_Name());
        /** 设置该分支的父分支*/
        addBranch.parentBranch = this.getPointer_Name();
        /** 把该分支单独存储出来 */
        addBranch.SerializeStore();
//
    }

    public static BranchPointer ReadBranchPointer(String BranchPointerName) {
        File BranchPointerFile = Utils.join(InitMethod.getInit_FOLDER(), Pointer.getPointer_FOLDER_static(), BranchPointerName);
        if (BranchPointerFile.exists() == false) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        return Utils.readObject(BranchPointerFile, BranchPointer.class);
    }

    public static void RmBranch(String BranchPointerName) {
        File BranchPointerFile = Utils.join(InitMethod.getInit_FOLDER(), Pointer.getPointer_FOLDER_static(), BranchPointerName);
        /** 若该分支不存在，退出 */
        if (BranchPointerFile.exists() == false) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        /** 若该分支为当前分支，则中止 */
        Pointer head = Pointer.ReadPointer("head");
        // System.out.println(head.getCurrentBranchPointer());
        if (head.getCurrentBranchPointer().equals(BranchPointerName)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        /** 删除该分支，并且要把该分支在父分支中删除 */
        BranchPointer RmBranch = BranchPointer.ReadBranchPointer(BranchPointerName);
        Node FirstNode = RmBranch.NodeList.get(0);
        String FirstCommitSha1Name = FirstNode.CommitSha1Name;
        String parentBranchName = RmBranch.parentBranch;
        BranchPointer parentBranch = BranchPointer.ReadBranchPointer(parentBranchName);
        /** 先找到是在父分支的哪一个节点，再找是这个节点中哪一个子分支*/
        for (int i = 0; i < parentBranch.NodeList.size(); i++) {
            Node theNode = parentBranch.NodeList.get(i);
            if (theNode.CommitSha1Name == FirstCommitSha1Name) {
                for (int j = 0; j < theNode.sonBranch.size(); j++) {
                    if (theNode.sonBranch.get(i) == RmBranch.getPointer_Name()) {
                        theNode.sonBranch.remove(i);
                        break;
                    }
                }
                break;
            }
        }
        if (BranchPointerFile.exists()) BranchPointerFile.delete();
    }

    public class Node implements Serializable {
        public String CommitSha1Name;
        ArrayList<String> sonBranch;

        public Node(String CurrentLocation) {
            this.CommitSha1Name = CurrentLocation;
            this.sonBranch = new ArrayList<>();
        }
    }

}
