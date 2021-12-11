package general;

import java.nio.file.Path;

public class MessageUtils {

    public static final String COPIED = "Copied %s\n";
    public static final String DELETED = "Deleted %s\n";
    public final static String WRONG_ARGS = "Wrong number of arguments";
    public final static String NO_MATCHES = "No matched functions for your input";
    public final static String FAILED = "Task failed";
    public final static String SUCCESS = "Task succeeded";
    public static final String MANUAL = ""
            + "****************************************************************************\n"
            + "**      This is a simple utility program, functions available so far:     **\n"
            + "****************************************************************************\n"
            + "**      -b                                                                **\n"
            + "**      --backup                                                          **\n"
            + "**          backups all contents of folder A to folder B                  **\n"
            + "**          example:                                                      **\n"
            + "**              -b path/to/folderA path/to/folderB                        **\n"
            + "****************************************************************************\n"
            + "**      -h                                                                **\n"
            + "**      --help                                                            **\n"
            + "**          call this manual                                              **\n"
            + "****************************************************************************\n";

    public static void message(String pattern, String message) {
        System.out.printf(pattern, message);
    }

    public static void message(String message) {
        System.out.println(message);
    }

    public static void copiedMessage(Path path) {
        message(COPIED, stringify(path));
    }

    public static void deletedMessage(Path path) {
        message(DELETED, stringify(path));
    }

    private static String stringify(Path path) {
        return path.toAbsolutePath().toString();
    }
}
