package gitlet;

import java.io.File;
import java.io.IOException;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author TODO
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) throws IOException {
        // TODO: what if args is empty?
        String firstArg = args[0];

        /**无输入参数时，报错并退出。*/
        if (firstArg == null) {
            System.out.print("Please enter a command.");
            System.exit(0);
        }
        // TODO: 命令的操作数编号或格式错误怎么处理？
        // TODO: 输入的命令时需要在存储库中，但实际不在，怎么办？
        switch (firstArg) {
            case "init":
                // TODO: handle the `init` command
                InitMethod.Init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                StagingArea.Add(args[1]);
                break;
            case "commit":
                // TODO: handle the `commit` command
                String secondArg = args[1];
                Commit.CommitMethod(secondArg);
                break;
            case "rm":
                // TODO: handle the `rm` command
                StagingArea.rm(args[1]);
                break;
            case "log":
                // TODO: handle the `log` command
                break;
            case "global-log":
                // TODO: handle the `global-log` command
                break;
            case "find":
                // TODO: handle the `find` command
                break;
            case "status":
                // TODO: handle the `status` command
                break;
            case "checkout":
                // TODO: handle the `checkout` command
                break;
            case "branch":
                // TODO: handle the `branch` command
                break;
            case "rm-branch":
                // TODO: handle the `rm-branch` command
                break;
            case "reset":
                // TODO: handle the `reset` command
                break;
            case "merge":
                // TODO: handle the `merge` command
                break;

        }
    }
}
