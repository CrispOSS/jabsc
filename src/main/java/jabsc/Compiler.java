package jabsc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import com.squareup.javawriter.JavaWriter;

import bnfc.abs.Yylex;
import bnfc.abs.parser;
import bnfc.abs.Absyn.Prog;
import bnfc.abs.Absyn.Program;

/**
 * Entry point to ABS to Java compiler.
 */
public class Compiler implements Runnable {

  private final List<Path> sources;
  private final Path outputDirectory;

  public Compiler(List<Path> sources, Path outputDirectory) {
    if (sources == null || sources.isEmpty()) {
      throw new IllegalArgumentException("No source provided to compile.");
    }
    this.sources = sources;
    this.outputDirectory = outputDirectory;
  }

  /**
   * @throws Exception
   */
  public void compile() throws Exception {
    for (Path source : sources) {
      compile(source, outputDirectory);
    }
  }

  @Override
  public void run() {
    try {
      compile();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * @param source
   * @param outputDirectory
   * @throws Exception
   */
  protected void compile(Path source, Path outputDirectory) throws Exception {
    try (final BufferedReader reader = Files.newBufferedReader(source)) {
      final Program program = parseSource(reader);
      generateSource(program, source, outputDirectory);
    }
  }

  /**
   * @param program
   * @param source
   * @param outputDirectory
   * @return
   * @throws IOException
   */
  protected Path generateSource(Program program, Path source, Path outputDirectory)
      throws IOException {
    final Path sourcePath = createSourcePath(source, outputDirectory);
    try (final Writer writer = Files.newBufferedWriter(sourcePath, StandardOpenOption.CREATE_NEW)) {
      JavaWriter jw = new JavaWriter(writer);
      Prog prog = (Prog) program;
      Visitor visitor = new Visitor(prog);
      prog.accept(visitor, jw);
      return sourcePath;
    }
  }

  /**
   * @param source
   * @param outputDirectory
   * @return
   */
  protected Path createSourcePath(Path source, Path outputDirectory) {
    final String fullFileName = source.getFileName().toString();
    int dotIndex = fullFileName.lastIndexOf('.');
    final String fileName = dotIndex == -1 ? fullFileName : fullFileName.substring(0, dotIndex);
    return outputDirectory.resolve(fileName + ".java");
  }

  /**
   * @param reader
   * @return
   * @throws Exception
   */
  protected Program parseSource(final BufferedReader reader) throws Exception {
    final Yylex lexer = new Yylex(reader);
    final parser parser = new parser(lexer);
    return parser.pProgram();
  }

}
