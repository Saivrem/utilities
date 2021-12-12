package fileutils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    public static Path resolveDestinationPath(Path root, Path relative) {
        return Paths.get(root.toString(), relative.toString());
    }

    public static Path resolveDestinationPath(Path root, String name) {
        return Paths.get(root.toString(), name);
    }

    public static void deleteFolder(Path path) {
        File dir = path.toFile();
        if (!dir.exists()) {
            return;
        }
        for (String str : dir.list()) {
            File toDelete = new File(dir.getPath(), str);
            if (toDelete.isDirectory()) {
                deleteFolder(toDelete.toPath());
            }
            toDelete.delete();
        }
        dir.delete();
    }

}
