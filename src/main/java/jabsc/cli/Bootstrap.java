package jabsc.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;
import com.google.common.collect.Sets;

import io.airlift.airline.Cli;
import io.airlift.airline.Cli.CliBuilder;
import io.airlift.airline.Help;

public class Bootstrap {

  private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

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
      LOGGER.error("Compilation failed: {}",
          Throwables.getStackTraceAsString(Throwables.getRootCause(e)));
    }
  }

}
