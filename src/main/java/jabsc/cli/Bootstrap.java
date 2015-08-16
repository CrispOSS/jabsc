package jabsc.cli;

import java.util.logging.Logger;

import com.google.common.base.Throwables;
import com.google.common.collect.Sets;

import io.airlift.airline.Cli;
import io.airlift.airline.Cli.CliBuilder;
import io.airlift.airline.Help;

public class Bootstrap {

  private static final Logger LOGGER = Logger.getLogger(Bootstrap.class.getName());

  public static void main(String[] args) {
    try {
      CliBuilder<Runnable> cliBuilder =
          Cli.<Runnable>builder("jabsc").withDescription("ABS to Java Compiler")
              .withCommands(Sets.newHashSet(Help.class, CompilerCommand.class))
              .withDefaultCommand(Help.class);
      Cli<Runnable> cli = cliBuilder.build();
      Runnable command = cli.parse(args);
      command.run();
    } catch (Exception e) {
      LOGGER.severe(
          "Compilation failed: " + Throwables.getStackTraceAsString(Throwables.getRootCause(e)));
      System.exit(1);
    }
  }

}
