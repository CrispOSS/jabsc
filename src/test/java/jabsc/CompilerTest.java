package jabsc;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

public class CompilerTest {

  private URL uri = getClass().getResource("abs/IPrime.abs");
  private Path outputDirectory = Paths.get("./target").resolve("generated");
  private Path tmpDirectory = Paths.get("java.io.tmpdir");

  @Test
  public void resolveOutputDirectory() throws Exception {
    Compiler compiler = new Compiler(Paths.get(uri.toURI()), outputDirectory);
    Path p1 = compiler.resolveOutputDirectory("a.b.c", tmpDirectory);
    Path p2 = tmpDirectory.resolve("a").resolve("b").resolve("c");
    Assert.assertEquals(p2.toAbsolutePath(), p1.toAbsolutePath());
  }

  @Test
  public void compileTestABSSources() throws Exception {
    Files.createDirectories(outputDirectory);
    Compiler compiler = new Compiler(Paths.get(uri.toURI()).getParent(), outputDirectory);
    compiler.compile();
    Assert.assertTrue(Files.list(outputDirectory).count() > 0);
  }

}
