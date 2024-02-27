package gitlet;

import java.util.TreeMap;

public class StagingArea {
    /** 描述： 该类用于暂存文件副本-暂存区域 */
    /** TODO  该暂存区域也需要序列化，直到Commit之后，暂存区域与最新一个Commit的暂存区域同步。 */
    public TreeMap<String, String> StoreArea = new TreeMap<>();
}
