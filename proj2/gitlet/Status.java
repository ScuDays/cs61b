package gitlet;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Status {
    /**
     * 描述：显示当前存在哪些分支，并用 * 标记当前分支。还显示哪些文件已暂存以供添加或删除。它应遵循的确切格式的示例如下。
     */
    public static void status() {
        Pointer head = Pointer.ReadPointer("head");
        String CurrentBranch = head.getCurrentBranchPointer();
        File Pointer_Folder = Utils.join(InitMethod.getInit_FOLDER(), Pointer.getPointer_FOLDER_static());
        List<String> PointerList = Utils.plainFilenamesIn(Pointer_Folder);
        Iterator pointerItr = PointerList.iterator();

        System.out.println("=== Branches ===");
        while (pointerItr.hasNext()) {
            String pointerName = (String) pointerItr.next();
            if (pointerName.equals("head")) continue;
            if (pointerName.equals(CurrentBranch)) System.out.printf("*");
            System.out.println(pointerName);
        }
        System.out.println();

        StagingArea sta = new StagingArea();
        System.out.println("=== Staged Files ===");
        Iterator<Map.Entry<String, String>> itr1 = sta.getAddMap().Map.entrySet().iterator();
        while (itr1.hasNext()) {
            Map.Entry<String, String> map = itr1.next();
            System.out.println(map.getKey());
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        Iterator<Map.Entry<String, String>> itr2 = sta.getRmMap().Map.entrySet().iterator();
        while (itr2.hasNext()) {
            Map.Entry<String, String> map = itr2.next();
            System.out.println(map.getKey());
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

}
