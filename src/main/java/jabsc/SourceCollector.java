package jabsc;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;

import com.google.common.io.Files;

/**
 * A {@link FileVisitor} that collects files in a hierarchy with
 * specific file extension.
 */
class SourceCollector extends SimpleFileVisitor<Path>implements Supplier<Set<Path>> {

  private final String extension;
  private final Set<Path> paths;

  public SourceCollector(String extension) {
    this.extension = extension;
    this.paths = new TreeSet<>();
  }

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    super.visitFile(file, attrs);
    String fileExtension = Files.getFileExtension(file.getFileName().toString());
    if (this.extension.equalsIgnoreCase(fileExtension)) {
      this.paths.add(file);
    }
    return FileVisitResult.CONTINUE;
  }

  @Override
  public Set<Path> get() {
    return Collections.unmodifiableSet(this.paths);
  }

}
