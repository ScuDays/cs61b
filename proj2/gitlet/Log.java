package gitlet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.Locale;

public class Log {
    /** TODO 处理Merge合并得到的Commit */
    public static void log(){
        Pointer head = Pointer.ReadPointer("head");
        String CurrentBranchPointerName = head.getCurrentBranchPointer();
        BranchPointer CurrentBranchPointer = (BranchPointer) Pointer.ReadPointer(CurrentBranchPointerName);
        String Sha1Name = CurrentBranchPointer.getCurrentLocation();
       while(true){
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
           if (theCommit.getMessage().equals("initial commit"))break;
           else Sha1Name = theCommit.getParent();

       }
    }
    public static void global_log(){
        File CommitFileFolder = Utils.join(InitMethod.getInit_FOLDER(), Commit.getCommit_FOLDER_static());
        List<String> CommitList = Utils.plainFilenamesIn(CommitFileFolder);
        Iterator itr = CommitList.iterator();
        while(itr.hasNext()) {
            String Sha1Name  = (String) itr.next();
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
