package fileutils;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class BackupFileVisitor extends SimpleFileVisitor<Path> {

    int count = 0;

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        count++;
        return FileVisitResult.CONTINUE;
    }

    public int getCount() {
        return count;
    }
}
