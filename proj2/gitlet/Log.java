package gitlet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class Log {
    /**
     * TODO 处理Merge合并得到的Commit
     */
    public static void log() {
        Pointer head = Pointer.ReadPointer("head");
        String CurrentBranchPointerName = head.getCurrentBranchPointer();
        BranchPointer CurrentBranchPointer = (BranchPointer) Pointer.ReadPointer(CurrentBranchPointerName);
        String Sha1Name = CurrentBranchPointer.getCurrentLocation();
        while (true) {
            Commit theCommit = Commit.SerializeRead(Sha1Name);
            TimeZone tz = TimeZone.getTimeZone("GMT-8"); // 对于 -0800 时区
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z", Locale.US);
            sdf.setTimeZone(tz); // 设置时区
            String formattedDate = sdf.format(theCommit.getCommitDate());
            System.out.println("===");
            System.out.println("commit " + Sha1Name);
            if(theCommit.getMergeBranchParent() != null){
                System.out.print("Merge: ");
                System.out.print(theCommit.getParent().substring(0, 7));
                System.out.print(" ");
                System.out.println(theCommit.getMergeBranchParent().substring(0, 7));
            }
            System.out.printf("Date: ");
            System.out.println(formattedDate);
            System.out.println(theCommit.getMessage());
            System.out.println();
            if (theCommit.getMessage().equals("initial commit")) break;
            else Sha1Name = theCommit.getParent();
        }

//        /** TODO 测试
//         *
//         */
//        Commit theCommit  = Commit.SerializeRead(head.getCurrentLocation());
//        Set theset = theCommit.Map.Map.keySet();
//        Iterator itr1 = theset.iterator();
//        System.out.println(CurrentBranchPointer.getCurrentLocation());
//        System.out.println("----------------");
//        while(itr1.hasNext()){
//            String a = (String) itr1.next();
//            System.out.println(a);
//        }
//
//        StagingArea sta = new StagingArea();
//        Set setRm = sta.getRmMap().Map.keySet();
//        Iterator itrRm = setRm.iterator();
//        while(itrRm.hasNext()) System.out.println(itrRm.next());

    }

    public static void global_log() {
        File CommitFileFolder = Utils.join(InitMethod.getInit_FOLDER(), Commit.getCommit_FOLDER_static());
        List<String> CommitList = Utils.plainFilenamesIn(CommitFileFolder);
        Iterator itr = CommitList.iterator();
        while (itr.hasNext()) {
            String Sha1Name = (String) itr.next();
            Commit theCommit = Commit.SerializeRead(Sha1Name);

            TimeZone tz = TimeZone.getTimeZone("GMT-8"); // 对于 -0800 时区
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z", Locale.US);
            sdf.setTimeZone(tz); // 设置时区
            String formattedDate = sdf.format(theCommit.getCommitDate());
            System.out.println("===");
            System.out.println("commit " + Sha1Name);
            System.out.printf("Date: ");
            System.out.println(formattedDate);
            System.out.println(theCommit.getMessage());
            System.out.println();
        }
    }

}
