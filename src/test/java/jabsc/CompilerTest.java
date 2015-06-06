package jabsc;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

public class CompilerTest {

  @Test
  public void compileInterface() throws Exception {
    URI uri = getClass().getResource("abs/TestInterface.abs").toURI();
    Path outputDirectory = Paths.get("./target").resolve("generated");
    Files.createDirectories(outputDirectory);
    Compiler compiler = new Compiler(Collections.singletonList(Paths.get(uri)), outputDirectory);
    compiler.compile();
    Assert.assertTrue(Files.list(outputDirectory).count() > 0);
  }

}
