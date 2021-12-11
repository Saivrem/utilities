package fileutils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static general.MessageUtils.copiedMessage;

public class BackupFileVisitor extends SimpleFileVisitor<Path> {

    private final Path origin;
    private final Path destination;
    private Path root;

    public BackupFileVisitor(Path origin, Path destination) {
        this.destination = destination;
        this.origin = origin;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {

        Path relative = origin.relativize(dir);

        if (relative.toString().isEmpty()) {
            setRoot(dir);
        } else {
            Files.createDirectory(destinationRelative(relative));
        }
        copiedMessage(dir);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        Path relative = origin.relativize(file);

        Files.copy(file, destinationRelative(relative));
        copiedMessage(file);
        return FileVisitResult.CONTINUE;
    }

    private Path destinationRelative(Path relative) {
        return Paths.get(root.toString(), relative.toString());
    }

    private void setRoot(Path dir) throws IOException {
        root = Paths.get(destination.toString() + File.separator + dir.getFileName().toString());
        Files.createDirectory(root);
    }
}
