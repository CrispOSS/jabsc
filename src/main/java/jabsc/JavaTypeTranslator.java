package jabsc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import abs.api.Response;

/**
 * Some ABS types have an equivalent proper Java type. This is
 * function to be able to translate those.
 */
class JavaTypeTranslator implements Function<String, String> {

  private final Map<String, String> absTypes = new HashMap<>();
  private final Map<String, String> abstractTypes = new HashMap<>();
  private final Map<String, String> functionalTypes = new HashMap<>();

  public JavaTypeTranslator() {
    fillABSTypes(absTypes);
    fillFunctionalTypes(functionalTypes);
  }

  @Override
  public String apply(String absType) {
    String javaType = translateJava(absType);
    if (javaType != null) {
//      System.out.println(String.format("1> %s => %s", absType, javaType));
      return javaType;
    }
    String type = translateAbstract(absType);
    if (type == null) {
//      System.out.println(String.format("2> %s => %s", absType, type));
      return absType;
    }
    javaType = translateJava(type);
//    System.out.println(String.format("3> %s => %s => %s", absType, type, javaType));
    return javaType == null ? type : javaType;
  }

  protected String translateFunctionalType(String type) {
    if (this.functionalTypes.containsKey(type)) {
      return this.functionalTypes.get(type);
    }
    return type;
  }

  private String translateJava(String absType) {
    return absTypes.get(absType);
  }

  private String translateAbstract(String absType) {
    return abstractTypes.get(absType);
  }

  protected void fillABSTypes(final Map<String, String> types) {
    types.put("Int", Long.class.getName());
    types.put("Rat", Double.class.getName());
    types.put("Bool", Boolean.class.getName());
    types.put("ABS.StdLib.Map", Map.class.getName());
    types.put("Unit", "void");
    types.put("Fut", Response.class.getSimpleName());
    types.put("Maybe", Optional.class.getSimpleName());
  }

  protected void fillFunctionalTypes(final Map<String, String> types) {
    types.put("Nil", "Nil()");
    types.put("EmptyList", "EmptyList()");
    types.put("EmptySet", "EmptySet()");
    types.put("EmptyMap", "EmptyMap()");
  }

  protected void registerAbstractType(String absType, String defType) {
    this.abstractTypes.put(absType, defType);
  }

}
