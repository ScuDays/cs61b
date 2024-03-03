package gitlet;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Locale;

public class Log {

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
           System.out.println("====");
           System.out.println("commit " + Sha1Name);
           System.out.println(formattedDate);
           System.out.println(theCommit.getMessage());
           if (theCommit.getMessage().equals("initial commit"))break;
           else Sha1Name = theCommit.getParent();
       }
    }
}
