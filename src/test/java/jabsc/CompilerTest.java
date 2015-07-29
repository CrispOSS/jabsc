package jabsc;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

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
    Path directory = Paths.get(uri.toURI()).getParent();
    List<Path> sources =
        Lists.newArrayList(directory.resolve("IPrime.abs"), directory.resolve("TestInterface.abs"));
    Compiler compiler = new Compiler(sources, outputDirectory).enableDebugMode();
    compiler.compile();
    Assert.assertTrue(Files.list(outputDirectory).count() > 0);
  }

}
