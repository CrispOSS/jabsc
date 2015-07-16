package jabsc.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;

import io.airlift.airline.Command;
import io.airlift.airline.Option;
import jabsc.Compiler;

@Command(name = "compile", description = "Compile ABS source to Java source")
public class CompilerCommand implements Runnable {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Option(name = {"-s", "--source"}, title = "ABS-SOURCE",
      description = "The path to ABS source(s) to compile. Can be a directory.", required = true)
  public String source;

  @Option(name = {"-d", "-o", "--output", "--directory"}, title = "JAVA-DIRECTORY",
      description = "The path of the target directory for compiled Java source. "
          + "Default value is a sibling directory to source directory with name:" + " '"
          + Compiler.DEFAULT_OUTPUT_DIRECTORY_NAME + "'.",
      required = false)
  public String outputDirectory;

  @Override
  public void run() {
    try {
      new Compiler(source, outputDirectory).compile();
      return;
    } catch (Exception e) {
      logger.error("Compliation failed: {}",
          Throwables.getStackTraceAsString(Throwables.getRootCause(e)));
      return;
    }
  }

}
