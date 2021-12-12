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
    private Path targetBackupDirectory, tmpOldBackupDirectory;
    int exceptions = 0;

    public Backup(String origin, String destination) {
        this.origin = Paths.get(origin);
        this.destination = Paths.get(destination);
    }

    public void run() throws IOException {

        BackupFileVisitor visitor = new BackupFileVisitor();
        Files.walkFileTree(origin, visitor);

        ProgressBar progressBar = new ProgressBar(visitor.getCount());

        if (!Files.exists(destination)) {
            Files.createDirectories(destination);
        }

        targetBackupDirectory = resolveDestinationPath(destination, origin.getFileName());

        if (Files.exists(targetBackupDirectory)) {
            tmpOldBackupDirectory = resolveDestinationPath(destination, "oldBackup");
            deleteFolder(tmpOldBackupDirectory);
            message("Moving old backup to tmp location");
            Files.move(targetBackupDirectory, tmpOldBackupDirectory);
            message("Moved");
        }
        Files.createDirectories(targetBackupDirectory);

        Files.walk(origin).forEach(source -> {
            Path target = resolveDestinationPath(targetBackupDirectory, origin.relativize(source));
            try {
                if (Files.isDirectory(source)) {
                    Files.createDirectories(target);
                } else {
                    progressBar.draw(1);
                    Files.copy(source, target);
                }
            } catch (IOException e) {
                exceptions++;
                e.printStackTrace();
            }
        });

        if (exceptions > 0 && tmpOldBackupDirectory.toFile().exists()) {
            message("New backup failed, old one restored");
            deleteFolder(targetBackupDirectory);
            Files.move(tmpOldBackupDirectory, targetBackupDirectory);
        } else if (Files.exists(tmpOldBackupDirectory)) {
            deleteFolder(tmpOldBackupDirectory);
        }
    }
}
