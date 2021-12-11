package general;

import fileutils.Backup;

import java.io.IOException;

import static general.MessageUtils.FAILED;
import static general.MessageUtils.MANUAL;
import static general.MessageUtils.NO_MATCHES;
import static general.MessageUtils.SUCCESS;
import static general.MessageUtils.WRONG_ARGS;
import static general.MessageUtils.message;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            message(WRONG_ARGS);
            message(MANUAL);
            exitErr();
        }

        switch (args[0]) {
            case "--backup":
            case "-b":
                doBackup(args);
                break;
            case "-h":
            case "--help":
                message(MANUAL);
                exitOk();
            default:
                message(NO_MATCHES);
                break;
        }

        exitOk();
    }

    private static void doBackup(String[] args) {
        try {
            String origin = args[1];
            String destination = args[2];
            Backup backup = new Backup(origin, destination);
            backup.start();
        } catch (ArrayIndexOutOfBoundsException e) {
            message(WRONG_ARGS);
            exitErr();
        } catch (IOException e) {
            e.printStackTrace();
            message("Backup " + FAILED);
            exitErr();
        }
        message("Backup " + SUCCESS);
    }

    private static void exitOk() {
        System.exit(0);
    }

    private static void exitErr() {
        System.exit(1);
    }
}
