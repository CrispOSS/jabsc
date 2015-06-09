package jabsc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.squareup.javawriter.JavaWriter;

import bnfc.abs.Yylex;
import bnfc.abs.parser;
import bnfc.abs.Absyn.Modul;
import bnfc.abs.Absyn.Module;
import bnfc.abs.Absyn.Prog;
import bnfc.abs.Absyn.Program;

/**
 * Entry point to ABS to Java compiler.
 */
public class Compiler implements Runnable {

  /**
   * .java
   */
  private static final String JAVA_FILE_EXTENSION = ".java";

  private final List<Path> sources;
  private final Path outputDirectory;

  public Compiler(Path source, Path outputDirectory) {
    this(Collections.singletonList(source), outputDirectory);
  }

  public Compiler(List<Path> sources, Path outputDirectory) {
    if (sources == null || sources.isEmpty()) {
      throw new IllegalArgumentException("No source provided to compile.");
    }
    this.sources = sources;
    this.outputDirectory = outputDirectory;
  }

  /**
   * @return
   * @throws Exception
   */
  public List<Path> compile() throws Exception {
    final List<Path> compilations = new ArrayList<>();
    for (Path source : sources) {
      Path compilation = compile(source, outputDirectory);
      compilations.add(compilation);
    }
    return compilations;
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
   * @return
   * @throws Exception
   */
  protected Path compile(Path source, Path outputDirectory) throws Exception {
    try (final BufferedReader reader = Files.newBufferedReader(source)) {
      final Program program = parseSource(reader);
      return generateSource(program, source, outputDirectory);
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
    final Prog prog = (Prog) program;
    final String packageName = getPackageName(prog);
    final Path sourcePath = createSourcePath(packageName, source, outputDirectory);
    final Visitor visitor = new Visitor(prog);
    Files.createDirectories(sourcePath.getParent());
    try (final Writer writer = Files.newBufferedWriter(sourcePath, StandardOpenOption.CREATE)) {
      JavaWriter jw = new JavaWriter(writer);
      prog.accept(visitor, jw);
      return sourcePath;
    }
  }

  /**
   * @param packageName
   * @param source
   * @param outputDirectory
   * @return
   */
  protected Path createSourcePath(String packageName, Path source, Path outputDirectory) {
    final String fullFileName = source.getFileName().toString();
    int dotIndex = fullFileName.lastIndexOf('.');
    final String fileName = dotIndex == -1 ? fullFileName : fullFileName.substring(0, dotIndex);
    outputDirectory = resolveOutputDirectory(packageName, outputDirectory);
    return outputDirectory.resolve(fileName + JAVA_FILE_EXTENSION);
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

  /**
   * @param prog
   * @return
   */
  protected String getPackageName(final Prog prog) {
    Module module = prog.listmodule_.iterator().next();
    Visitor v = new Visitor(prog);
    return v.getQTypeName(((Modul) module).qtype_);
  }

  /**
   * @param packageName
   * @param outputDirectory
   * @return
   */
  protected Path resolveOutputDirectory(String packageName, Path outputDirectory) {
    if (packageName == null || packageName.isEmpty()) {
      return outputDirectory;
    }
    String[] parts = packageName.split("\\.");
    for (String packagePart : parts) {
      outputDirectory = outputDirectory.resolve(packagePart);
    }
    return outputDirectory;
  }

}