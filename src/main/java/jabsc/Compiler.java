package jabsc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import com.google.common.collect.Sets;

import bnfc.abs.Yylex;
import bnfc.abs.parser;
import bnfc.abs.Absyn.Mod;
import bnfc.abs.Absyn.Module;
import bnfc.abs.Absyn.Prog;
import bnfc.abs.Absyn.Program;




/**
 * Entry point to ABS to Java compiler.
 */
public class Compiler implements Runnable {

  /**
   * Java keywords
   */
  public static final Set<String> JAVA_KEYWORDS =
      Sets.newHashSet("abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
          "class", "const", "continue", "default", "do", "double", "else", "extends", "false",
          "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof",
          "int", "interface", "long", "native", "new", "null", "package", "private", "protected",
          "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized",
          "this", "throw", "throws", "transient", "true", "try", "void", "volatile", "while");
  /**
   * The default relative directory to generate Java files.
   */
  public static final String DEFAULT_OUTPUT_DIRECTORY_NAME = "generated-sources/jabsc";
  /**
   * .abs
   */
  private static final String ABS_FILE_EXTENSION = "abs";
  /**
   * .java
   */
  private static final String JAVA_FILE_EXTENSION = "java";

  /**
   * The version of JABSC Compiler.
   */
  private static final String VERSION;

  static {
    // Set j.u.logging format: [LEVEL] MSG [THROWABLE]
    System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$s] %5$s%n%6$s");
    VERSION = Optional.ofNullable(Compiler.class.getPackage().getImplementationVersion())
        .orElse("1.x-SNAPSHOT");
  }

  private final Logger logger = Logger.getLogger(getClass().getName());

  private final List<Path> sources;
  private final Path outputDirectory;
  private boolean debug = false;

  public Compiler(String source, String outputDirectory) {
    this(createPath(source), createOutputDirectoryPath(outputDirectory, createPath(source)));
  }

  public Compiler(Path source, Path outputDirectory) {
    this(Collections.singletonList(source), outputDirectory);
  }

  public Compiler(List<Path> sources, Path outputDirectory) {
    this.sources = createSources(sources);
    this.outputDirectory = outputDirectory.normalize().toAbsolutePath();
    validate(this.sources, outputDirectory);
    logger.info("jabsc " + VERSION);
  }

  /**
   * @return
   * @throws Exception
   */
  public List<Path> compile() throws Exception {
    logger.info("Compiling from " + sources + " to " + outputDirectory);
    final List<Path> compilations = new ArrayList<>();
    for (Path source : sources) {
      if (Files.isDirectory(source) && Files.list(source).count() == 0) {
        logger.info("Source directory " + source + " is empty. Skipping.");
        continue;
      }
      Path compilation = compile(source, outputDirectory);
      compilations.add(compilation);
    }
    logger.info("Compiled " + compilations.size() + " Java sources to: " + outputDirectory);
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

  public Compiler enableDebugMode() {
    this.debug = true;
    return this;
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
    final Visitor visitor = new Visitor(packageName, prog,
        new DefaultJavaWriterSupplier(PathResolver.DEFAULT_PATH_RESOLVER, packageName,
            outputDirectory),
        new JavaTypeTranslator(), outputDirectory);
    Files.createDirectories(sourcePath.getParent());
    try (final Writer writer = createWriter(sourcePath)) {
      JavaWriter jw = new JavaWriter(writer,false);
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
    final int dotIndex = fullFileName.lastIndexOf('.');
    final String fileName = dotIndex == -1 ? fullFileName : fullFileName.substring(0, dotIndex);
    outputDirectory = resolveOutputDirectory(packageName, outputDirectory);
    return outputDirectory.resolve(fileName + "." + JAVA_FILE_EXTENSION);
  }

  /**
   * @param sourcePath
   * @return
   * @throws IOException
   */
  protected BufferedWriter createWriter(final Path sourcePath) throws IOException {
    return Files.newBufferedWriter(sourcePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
  }

  /**
   * @param reader
   * @return
   * @throws Exception
   */
  protected Program parseSource(final BufferedReader reader) throws Exception {
    final Yylex lexer = new Yylex(reader);
    final parser parser = new parser(lexer);
    try {
      return debug ? (Program) parser.debug_parse().value : (Program) parser.parse().value;
    } catch (Exception e) {
      if (debug) {
        parser.dump_stack();
      }
      logger.severe(
          "Compile error found at line " + lexer.line_num() + " near:\n\t{}" + lexer.buff());
      throw e;
    }
  }

  /**
   * @param prog
   * @return
   */
  protected String getPackageName(final Prog prog) {
    Module module = prog.listmodule_.iterator().next();
    Visitor v = new Visitor(null, prog, null, new JavaTypeTranslator(), null);
    String pakkage = v.getQTypeName(((Mod) module).qu_,false).toLowerCase();
    if (isJavaKeyword(pakkage)) {
      String newPakkage = pakkage + "_";
      logger.warning(
          String.format("Java keyword is improperly used: %s. Renamed: %s", pakkage, newPakkage));
      return newPakkage;
    }
    return pakkage;
  }

  /**
   * @param packageName
   * @param outputDirectory
   * @return
   */
  protected Path resolveOutputDirectory(String packageName, Path outputDirectory) {
    return PathResolver.DEFAULT_PATH_RESOLVER.resolveOutputDirectory(packageName, outputDirectory);
  }

  /**
   * Traverses the source paths and collects all possible ABS
   * source files.
   * 
   * @param sources
   * @return
   */
  protected List<Path> createSources(List<Path> sources) {
    Set<Path> result = new TreeSet<>();
    if (sources == null || sources.isEmpty()) {
      return new ArrayList<>();
    }
    for (Path source : sources) {
      if (Files.isDirectory(source)) {
        SourceCollector collector = new SourceCollector(ABS_FILE_EXTENSION);
        try {
          Files.walkFileTree(source, collector);
          result.addAll(collector.get());
        } catch (IOException e) {
          throw new IllegalArgumentException("Source directory is not valid: " + source);
        }
      } else if (Files.isRegularFile(source)) {
        result.add(source.toAbsolutePath());
      }
    }
    return new ArrayList<>(result);
  }

  protected void validate(List<Path> sources, Path outputDirectory) {
    if (!Files.exists(outputDirectory)) {
      try {
        Files.createDirectories(outputDirectory);
      } catch (IOException e) {
        throw new IllegalArgumentException(e);
      }
    }
    if (!Files.isWritable(outputDirectory)) {
      throw new IllegalArgumentException("Cannot write to output directory: " + outputDirectory);
    }
    for (Path p : sources) {
      if (!Files.isReadable(p)) {
        throw new IllegalArgumentException("Cannot read from source directory: " + p);
      }
    }
  }

  protected static Path createOutputDirectoryPath(String outputDirectory, Path source) {
    final boolean isSourceDirectory = Files.isDirectory(source) && Files.isReadable(source);
    if (outputDirectory != null) {
      return createPath(outputDirectory);
    } else {
      return isSourceDirectory
          ? source.getParent().resolve(DEFAULT_OUTPUT_DIRECTORY_NAME).toAbsolutePath()
          : source.getParent().getParent().resolve(DEFAULT_OUTPUT_DIRECTORY_NAME).toAbsolutePath();
    }
  }

  protected static boolean isJavaKeyword(String s) {
    return s != null && !s.trim().isEmpty() && JAVA_KEYWORDS.contains(s);
  }

  private static Path createPath(String source) {
    return Paths.get(source).toAbsolutePath().normalize();
  }

}
