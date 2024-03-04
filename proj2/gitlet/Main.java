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
    public static void main(String[] args){
        // TODO: what if args is empty?
        /**无输入参数时，报错并退出。*/
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        // TODO: 命令的操作数编号或格式错误怎么处理？
        // TODO: 输入的命令时需要在存储库中，但实际不在，怎么办？
        switch (firstArg) {
            case "init":
                // TODO: handle the `init` command
                try {
                    InitMethod.Init();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                try {
                    StagingArea.Add(args[1]);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
                Log.log();
                break;
            case "global-log":
                // TODO: handle the `global-log` command
                break;
            case "find":
                // TODO: handle the `find` command
                Find.find(args[1]);
                break;
            case "status":
                // TODO: handle the `status` command
                break;
            case "checkout":
                // TODO: handle the `checkout` command
                if(args.length <= 2) {
                    try {
                        Checkout.checkoutFileName(args[1]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
                else {
                    try {
                        Checkout.checkoutCommitFileName(args[1], args[2]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
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
