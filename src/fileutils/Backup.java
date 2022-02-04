package fileutils;

import general.ProgressBar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static fileutils.FileUtils.deleteFolder;
import static fileutils.FileUtils.resolveDestinationPath;
import static general.MessageUtils.message;

public class Backup {

    private final Path origin, destination;
    private Path tmpOldBackupDirectory;
    int exceptions = 0;

    public Backup(String origin, String destination) {
        this.origin = Paths.get(origin);
        this.destination = Paths.get(destination);
    }

    public void run() throws IOException {

        BackupFileVisitor visitor = new BackupFileVisitor();
        Files.walkFileTree(origin, visitor);

        ProgressBar progressBar = new ProgressBar(visitor.getCount());

        Path targetBackupDirectory = resolveDestinationPath(destination, origin.getFileName());

        if (Files.exists(targetBackupDirectory)) {
            tmpOldBackupDirectory = resolveDestinationPath(destination, "oldBackup");
            deleteFolder(tmpOldBackupDirectory);
            message("Moving old backup to tmp location");
            Files.move(targetBackupDirectory, tmpOldBackupDirectory);
            message("Moved");
        }
        Files.createDirectories(targetBackupDirectory);

        for (Path path : visitor.getFiles()) {
            Path target = resolveDestinationPath(targetBackupDirectory, origin.relativize(path));
            try {
                Files.createDirectories(target.getParent());
                progressBar.draw(1);
                Files.copy(path, target);
            } catch (IOException e) {
                exceptions++;
                e.printStackTrace();
            }
        }

        if (exceptions > 0 && tmpOldBackupDirectory.toFile().exists()) {
            message("New backup failed, old one restored");
            deleteFolder(targetBackupDirectory);
            Files.move(tmpOldBackupDirectory, targetBackupDirectory);
        } else if (tmpOldBackupDirectory != null && Files.exists(tmpOldBackupDirectory)) {
            deleteFolder(tmpOldBackupDirectory);
        }
    }
}
