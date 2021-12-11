package fileutils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static general.MessageUtils.deletedMessage;
import static general.MessageUtils.message;

public class Backup {

    private final Path origin, destination;
    private Path existingBackup, tmpOldBackup;

    public Backup(String origin, String destination) {
        this.origin = Paths.get(origin);
        this.destination = Paths.get(destination);
    }

    public void start() throws IOException {

        if (!Files.exists(destination)) {
            Files.createDirectories(destination);
        }

        if (destinationContainsSource()) {
            existingBackup = Paths.get(destination.toString(), origin.getFileName().toString());
            tmpOldBackup = Paths.get(destination.toString(), "oldBackup");
            deleteIfExists(tmpOldBackup);
            message("Moving old backup to tmp location");
            Files.move(existingBackup, tmpOldBackup);
        }

        BackupFileVisitor backupFileVisitor = new BackupFileVisitor(origin, destination);

        try {
            Files.walkFileTree(origin, backupFileVisitor);
        } catch (IOException e) {
            deleteIfExists(existingBackup);
            Files.move(tmpOldBackup, existingBackup);
            message("New backup failed, old one restored");
            e.printStackTrace();
        }
        deleteIfExists(tmpOldBackup);
    }

    private boolean destinationContainsSource() throws IOException {
        return Files.list(destination).anyMatch(element -> element.getFileName().equals(origin.getFileName()));
    }

    private void deleteIfExists(Path path) {
        File dir = path.toFile();
        if (!dir.exists()) {
            return;
        }
        for (String str : dir.list()) {
            File toDelete = new File(dir.getPath(), str);
            if (toDelete.isDirectory()) {
                deleteIfExists(toDelete.toPath());
            }
            toDelete.delete();
            deletedMessage(toDelete.toPath());
        }
        dir.delete();
        deletedMessage(dir.toPath());
    }
}
