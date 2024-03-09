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
    public static void main(String[] args) {
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
                Args_isValid(args.length, 1);
                // TODO: handle the `init` command
                try {
                    InitMethod.Init();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                Args_isValid(args.length, 2);
                try {
                    StagingArea.Add(args[1]);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "commit":
                // TODO: handle the `commit` command
                if (args.length == 1) {
                    System.out.print("Please enter a commit message.");
                    System.exit(0);
                }
                String secondArg = args[1];
                Commit.CommitMethod(secondArg);
                break;
            case "rm":
                // TODO: handle the `rm` command
                Args_isValid(args.length, 2);
                StagingArea.rm(args[1]);
                break;
            case "log":
                // TODO: handle the `log` command
                Args_isValid(args.length, 1);
                Log.log();
                break;
            case "global-log":
                // TODO: handle the `global-log` command
                Log.global_log();
                break;
            case "find":
                // TODO: handle the `find` command
                Args_isValid(args.length, 2);
                Find.find(args[1]);
                break;
            case "status":
                // TODO: handle the `status` command
                Args_isValid(args.length, 1);
                Status.status();
                break;
            case "checkout":
                // TODO: handle the `checkout` command
                if (args.length == 2) {
                    try {
                        Checkout.checkoutBranch(args[1]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                } else if (args.length == 3) {

                    try {
                        Checkout.checkoutFileName(args[2]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (args.length == 4) {
                    if (args[2].equals("--") == false) {
                        System.out.print("Incorrect operands.");
                        System.exit(0);
                    }
                    try {
                        Checkout.checkoutCommitFileName(args[1], args[3]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else System.exit(0);
                break;
            case "branch":
                // TODO: handle the `branch` command
                Args_isValid(args.length, 2);
                BranchPointer.Branch(args[1]);
                break;
            case "rm-branch":
                // TODO: handle the `rm-branch` command
                Args_isValid(args.length, 2);
                BranchPointer.RmBranch(args[1]);
                break;
            case "reset":
                // TODO: handle the `reset` command
                break;
            case "merge":
                // TODO: handle the `merge` command
                break;
        }
    }

    public static void Args_isValid(int ActualNum, int ExpectNum) {
        if (ActualNum != ExpectNum) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }
}
