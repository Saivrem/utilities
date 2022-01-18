package fileutils;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class BackupFileVisitor extends SimpleFileVisitor<Path> {

    private int count = 0;
    private final List<Path> files = new ArrayList<>();

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        count++;
        if (Files.isRegularFile(file)) {
            files.add(file);
        }
        return FileVisitResult.CONTINUE;
    }

    public int getCount() {
        return count;
    }
    public List<Path> getFiles() {return files;}
}
