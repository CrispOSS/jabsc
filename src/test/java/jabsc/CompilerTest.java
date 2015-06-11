package jabsc;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

public class CompilerTest {

  private URL uri = getClass().getResource("abs/TestInterface.abs");
  private Path outputDirectory = Paths.get("./target").resolve("generated");

  @Test
  public void resolveOutputDirectory() throws Exception {
    Compiler compiler = new Compiler(Paths.get(uri.toURI()), outputDirectory);
    Path result = compiler.resolveOutputDirectory("a.b.c", Paths.get("\\tmp"));
    Assert.assertEquals("D:\\tmp\\a\\b\\c", result.toAbsolutePath().toString());
  }

  @Test
  public void compileInterface() throws Exception {
    Files.createDirectories(outputDirectory);
    Compiler compiler = new Compiler(Paths.get(uri.toURI()), outputDirectory);
    compiler.compile();
    Assert.assertTrue(Files.list(outputDirectory).count() > 0);
  }

}
