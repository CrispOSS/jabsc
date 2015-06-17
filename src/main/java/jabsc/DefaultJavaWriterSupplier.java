package jabsc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * An implementation of {@link JavaWriterSupplier} using the
 * package spec and the current output directory to generate
 * Java files.
 */
class DefaultJavaWriterSupplier implements JavaWriterSupplier {

  private final Compiler compiler;
  private final String packageName;
  private final Path outputDirectory;

  public DefaultJavaWriterSupplier(Compiler compiler, String packageName, Path outputDirectory) {
    this.compiler = compiler;
    this.packageName = packageName;
    this.outputDirectory = outputDirectory;
  }

  @Override
  public JavaWriter apply(String typeName) {
    try {
      Path fqdnOutputDirectory = compiler.resolveOutputDirectory(packageName, outputDirectory);
      return new JavaWriter(Files.newBufferedWriter(fqdnOutputDirectory.resolve(typeName + ".java"),
          StandardOpenOption.CREATE, StandardOpenOption.WRITE));
    } catch (IOException e) {
      throw new IllegalArgumentException("Cannot create Java writer for " + typeName, e);
    }
  }

}
