package jabsc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import abs.api.cwi.ABSFuture;

/**
 * Some ABS types have an equivalent proper Java type. This is
 * function to be able to translate those.
 */
class JavaTypeTranslator implements Function<String, String> {

  private final Map<String, String> absTypes = new HashMap<>();
  private final Map<String, String> abstractTypes = new HashMap<>();
  private final Map<String, String> functionalTypes = new HashMap<>();
  private final Map<String, String> staticTypes = new HashMap<>();
  private final Map<String, String> remoteNames = new HashMap<>();
  
  
  

  public JavaTypeTranslator() {
    fillABSTypes(absTypes);
    fillFunctionalTypes(functionalTypes);
  }

  @Override
  public String apply(String absType) {
    String javaType = translateJava(absType);
    if (javaType != null) {
      //System.out.println(String.format("1> %s => %s", absType, javaType));
      return javaType;
    }
    String type = translateAbstract(absType);
    if (type == null) {
      //System.out.println(String.format("2> %s => %s", absType, type));
      return absType;
    }
    javaType = translateJava(type);
    //System.out.println(String.format("3> %s => %s => %s", absType, type, javaType));
    return javaType == null ? type : javaType;
  }

  protected String getRemoteName(String variable){
    return remoteNames.get(variable);
  }
  
  protected String translateFunctionalType(String type) {
    if (this.functionalTypes.containsKey(type)) {
      return this.functionalTypes.get(type);
    }
    return type;
  }
  
  protected String translateStaticType(String type){
    return staticTypes.get(type);
  }

  private String translateJava(String absType) {
    return absTypes.get(absType);
  }

  private String translateAbstract(String absType) {
    return abstractTypes.get(absType);
  }
  
  
  

  protected void fillABSTypes(final Map<String, String> types) {
    types.put("Int", Integer.class.getName());
    types.put("Rat", Double.class.getName());
    types.put("Bool", Boolean.class.getName());
    types.put("ABS.StdLib.Map", Map.class.getName());
    types.put("Unit", "void");
    types.put("Fut", ABSFuture.class.getSimpleName());
    types.put("Maybe", Optional.class.getSimpleName());
    
  }

  protected void fillFunctionalTypes(final Map<String, String> types) {
    types.put("Nil", "Nil()");
    types.put("EmptyList", "EmptyList()");
    types.put("EmptySet", "EmptySet()");
    types.put("EmptyMap", "EmptyMap()");
    types.put("True", "true");
    types.put("False", "false");
    
  }

  protected void registerAbstractType(String absType, String defType) {
    this.abstractTypes.put(absType, defType);
    //System.out.println(this.abstractTypes);
  }
  
  protected void deRegisterAbstractType(String absType) {
	    this.abstractTypes.remove(absType);
	    //System.out.println(this.abstractTypes);
	  }
  
  protected void registerStaticType(String stType, String defType) {
    this.staticTypes.put(stType, defType);
  }
  
  protected boolean inStaticTypes(String stType) {
    return this.staticTypes.containsKey(stType);
  }
  
  protected void registerRemoteName(String stName, String remoteName) {
    this.remoteNames.put(stName, remoteName);
  }
  
  
  

}
