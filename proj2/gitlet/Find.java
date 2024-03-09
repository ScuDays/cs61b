package gitlet;

import java.util.Iterator;
import java.util.List;

public class Find {

    /**
     * 描述：打印出具有给定提交消息的所有提交的 ID，每行一个。
     * 如果有多个这样的提交，它会在单独的行上打印 id。
     * 提交消息是单个操作数；要指示多字消息，请将操作数放在引号中，如下面的 commit 命令。
     * 提示：此命令的提示与 global-log 的提示相同。
     */

    public static void find(String message) {
        List<String> Sha1NameList = Utils.plainFilenamesIn(Utils.join(InitMethod.getInit_FOLDER(), Commit.getCommit_FOLDER_static()));
        Iterator itr = Sha1NameList.listIterator();
        Boolean exist = false;
        while (itr.hasNext()) {
            String Sha1Name = (String) itr.next();
            Commit theCommit = Commit.SerializeRead(Sha1Name);
            if (theCommit.getMessage().equals(message)) {
                System.out.println(Sha1Name);
                exist = true;
            }
        }
        if (exist == false) System.out.println("Found no commit with that message.");
    }
}
