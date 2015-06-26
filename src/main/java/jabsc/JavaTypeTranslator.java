package jabsc;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Some ABS types have an equivalent proper Java type. This is
 * function to be able to translate those.
 */
class JavaTypeTranslator implements Function<String, String> {

  private final Map<String, String> abs2java = new HashMap<>();

  public JavaTypeTranslator() {
    fill(abs2java);
  }

  @Override
  public String apply(String absType) {
    String javaType = abs2java.get(absType);
    if (javaType != null) {
      return javaType;
    }
    // TODO To be completed.
    return absType;
  }

  protected void fill(Map<String, String> types) {
    types.put("Int", Integer.class.getName());
    types.put("Unit", "void");
    types.put("Fut", "Future");
  }

}
