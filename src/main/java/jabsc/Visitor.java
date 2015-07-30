package jabsc;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Supplier;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import abs.api.Actor;
import abs.api.Functional;
import abs.api.Response;
import bnfc.abs.AbstractVisitor;
import bnfc.abs.Absyn.*;

/**
 * The visitor for all possible nodes in the AST of an ABS
 * program.
 */
class Visitor extends AbstractVisitor<Prog, JavaWriter> {

  // Constants
  private static final String LITERAL_THIS = "this";
  private static final String LITERAL_NULL = "null";
  private static final String MAIN_CLASS_NAME = "Main";
  private static final String COMMA_SPACE = ", ";
  private static final String ABS_API_ACTOR_CLASS = Actor.class.getName();
  private static final Set<Modifier> DEFAULT_MODIFIERS = Collections.singleton(Modifier.PUBLIC);
  private static final String[] DEFAULT_IMPORTS =
      new String[] {Collection.class.getPackage().getName() + ".*",
          Callable.class.getPackage().getName() + ".*", Actor.class.getPackage().getName() + ".*"};
  private static final String[] DEFAULT_STATIC_IMPORTS = new String[] {
      Functional.class.getPackage().getName() + "." + Functional.class.getSimpleName() + ".*"};

  // Internal Fields

  private static final Logger LOGGER = LoggerFactory.getLogger(Visitor.class);

  private final Set<String> moduleNames;
  private final Prog prog;
  private final JavaWriterSupplier javaWriterSupplier;
  private final String packageName;
  private final JavaTypeTranslator javaTypeTranslator;

  // Internal state
  private static final Multimap<String, MethodDefinition> METHODS =
      Multimaps.newSetMultimap(new HashMap<>(), new Supplier<Set<MethodDefinition>>() {
        @Override
        public Set<MethodDefinition> get() {
          return new HashSet<>();
        }
      });
  private static final Multimap<String, VarDefinition> VARIABLES =
      Multimaps.newSetMultimap(new HashMap<>(), new Supplier<Set<VarDefinition>>() {
        @Override
        public Set<VarDefinition> get() {
          return new HashSet<>();
        }
      });
  private final Stack<Module> modules = new Stack<>();
  private final EnumMap<ElementKind, Set<Decl>> elements = new EnumMap<>(ElementKind.class);
  private final Map<String, String> classNames = new HashMap<>();

  /**
   * Ctor.
   * 
   * @param packageName the package spec of the program
   * @param prog the parsed {@link Prog} AST node
   * @param javaWriterSupplier the {@link JavaWriterSupplier}
   *        for each top-level element
   * @param javaTypeTranslator The ABS to Java type translator
   */
  public Visitor(String packageName, Prog prog, JavaWriterSupplier javaWriterSupplier,
      JavaTypeTranslator javaTypeTranslator) {
    this.packageName = packageName;
    this.prog = prog;
    this.javaWriterSupplier = javaWriterSupplier;
    this.javaTypeTranslator = javaTypeTranslator;
    this.moduleNames = new HashSet<>();
  }

  @Override
  public Prog visit(Prog p, JavaWriter w) {
    try {
      Prog program = (Prog) p;
      determineProgramDeclarationTypes(program);
      for (Module module : program.listmodule_) {
        moduleNames.add(getQTypeName(((Modul) module).qtype_));
        modules.push(module);
        module.accept(this, w);
        modules.pop();
        w.emitEmptyLine();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return prog;
  }

  @Override
  public Prog visit(Modul m, JavaWriter w) {
    try {

      // Interfaces
      for (Decl decl : elements.get(ElementKind.INTERFACE)) {
        String name = getTopLevelDeclIdentifier(decl);
        JavaWriter declWriter = javaWriterSupplier.apply(name);
        declWriter.emitPackage(packageName);
        visitImports(m.listimport_, declWriter);
        decl.accept(this, declWriter);
        close(declWriter, w);
      }

      // Classes
      for (Decl decl : elements.get(ElementKind.CLASS)) {
        String name = getTopLevelDeclIdentifier(decl);
        final String refinedClassName = getRefinedClassName(name);
        JavaWriter declWriter = javaWriterSupplier.apply(refinedClassName);
        declWriter.emitPackage(packageName);
        visitImports(m.listimport_, declWriter);
        decl.accept(this, declWriter);
        close(declWriter, w);
      }

      visitMain(m, w);

      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(AnyImport p, JavaWriter w) {
    ImportType importType = p.importtype_;
    if (importType instanceof ForeignImport) {
      throw new UnsupportedOperationException("ABS Foreign Import not supported.");
    }
    Collection<String> types = new HashSet<>();
    for (TTypeSegment segment : ((TTyp) p.ttype_).listttypesegment_) {
      TTypeSegmen segmen = (TTypeSegmen) segment;
      types.add(segmen.uident_);
    }
    try {
      w.emitImports(types);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return prog;
  }


  @Override
  public Prog visit(InterfDecl id, JavaWriter w) {
    try {
      final String identifier = id.uident_;
      beginElementKind(w, ElementKind.INTERFACE, identifier, DEFAULT_MODIFIERS, null, null);
      w.emitEmptyLine();
      id.listmethsignat_.forEach(sig -> visit((MethSig) sig, w));
      w.endType();
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(ExtendsDecl ed, JavaWriter w) {
    try {
      final String identifier = ed.uident_;
      beginElementKind(w, ElementKind.INTERFACE, identifier, DEFAULT_MODIFIERS, null,
          toList(ed.listqtype_));
      w.emitEmptyLine();
      ed.listmethsignat_.forEach(sig -> sig.accept(this, w));
      w.endType();
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(ClassDecl p, JavaWriter w) {
    try {
      final String identifier = p.uident_;
      final String className = getRefinedClassName(identifier);
      beginElementKind(w, ElementKind.CLASS, className, DEFAULT_MODIFIERS, null, null);
      w.emitEmptyLine();
      for (ClassBody cb : p.listclassbody_1) {
        cb.accept(this, w);
      }

      w.beginConstructor(DEFAULT_MODIFIERS, null, null);

      p.maybeblock_.accept(this, w);

      w.endConstructor();

      for (ClassBody cb : p.listclassbody_2) {
        cb.accept(this, w);
      }
      w.endType();
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(ClassImplements ci, JavaWriter w) {
    try {
      final String identifier = ci.uident_;
      beginElementKind(w, ElementKind.CLASS, getRefinedClassName(identifier), DEFAULT_MODIFIERS,
          null, toList(ci.listqtype_));
      w.emitEmptyLine();
      for (ClassBody cb : ci.listclassbody_1) {
        cb.accept(this, w);
      }

      w.beginConstructor(DEFAULT_MODIFIERS, null, null);

      ci.maybeblock_.accept(this, w);

      w.endConstructor();

      for (ClassBody cb : ci.listclassbody_2) {
        cb.accept(this, w);
      }
      w.endType();
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(ClassParamDecl cpd, JavaWriter w) {
    try {
      final String identifier = cpd.uident_;
      beginElementKind(w, ElementKind.CLASS, getRefinedClassName(identifier), DEFAULT_MODIFIERS,
          null, null);
      w.emitEmptyLine();
      List<String> parameters = new ArrayList<>();
      for (Param param : cpd.listparam_) {
        Par p = (Par) param;
        String fieldType = getTypeName(p.type_);
        parameters.add(fieldType);
        parameters.add(p.lident_);
        emitField(w, fieldType, p.lident_, null, true);
      }
      w.emitEmptyLine();

      for (ClassBody cb : cpd.listclassbody_1) {
        cb.accept(this, w);
      }
      w.beginConstructor(DEFAULT_MODIFIERS, parameters, null);
      for (Param param : cpd.listparam_) {
        Par p = (Par) param;
        w.emitStatement("this." + p.lident_ + " = " + p.lident_);
      }

      cpd.maybeblock_.accept(this, w);

      w.endConstructor();
      w.emitEmptyLine();

      for (ClassBody cb : cpd.listclassbody_2) {
        cb.accept(this, w);
      }
      w.endType();
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(ClassParamImplements cpi, JavaWriter w) {
    try {
      final String identifier = cpi.uident_;
      beginElementKind(w, ElementKind.CLASS, getRefinedClassName(identifier), DEFAULT_MODIFIERS,
          null, toList(cpi.listqtype_));
      w.emitEmptyLine();
      List<String> parameters = new ArrayList<>();
      for (Param param : cpi.listparam_) {
        Par p = (Par) param;
        String fieldType = getTypeName(p.type_);
        parameters.add(fieldType);
        parameters.add(p.lident_);
        emitField(w, fieldType, p.lident_, null, true);
      }

      w.emitEmptyLine();

      for (ClassBody cb : cpi.listclassbody_1) {
        cb.accept(this, w);
      }


      w.beginConstructor(DEFAULT_MODIFIERS, parameters, null);
      for (Param param : cpi.listparam_) {
        Par p = (Par) param;
        w.emitStatement("this." + p.lident_ + " = " + p.lident_);
      }
      cpi.maybeblock_.accept(this, w);
      w.endConstructor();
      w.emitEmptyLine();
      for (ClassBody cb : cpi.listclassbody_2) {
        cb.accept(this, w);
      }
      w.endType();
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(JustBlock jb, JavaWriter w) {
    jb.block_.accept(this, w);
    return prog;
  }

  @Override
  public Prog visit(NoBlock nb, JavaWriter w) {
    return prog;
  }


  @Override
  public Prog visit(MethSig ms, JavaWriter w) {
    try {
      String returnType = getTypeName(ms.type_);
      String name = ms.lident_;
      List<String> parameters = new ArrayList<>();
      List<String> parameterTypes = new ArrayList<>();
      for (Param param : ms.listparam_) {
        Par p = (Par) param;
        String paramType = getTypeName(p.type_);
        parameters.add(paramType);
        parameters.add(p.lident_);
        parameterTypes.add(paramType);
      }
      createMethodDefinition(returnType, name, parameterTypes);
      w.beginMethod(returnType, name, DEFAULT_MODIFIERS, parameters, Collections.emptyList());
      w.endMethod();
      w.emitEmptyLine();
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(FieldClassBody p, JavaWriter w) {
    try {
      String fieldType = getTypeName(p.type_);
      String fieldName = p.lident_;
      emitField(w, fieldType, fieldName, null, false);
      createVarDefinition(fieldName, fieldType);
      w.emitEmptyLine();
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public Prog visit(FieldAssignClassBody p, JavaWriter w) {
    try {
      String fieldType = getTypeName(p.type_);
      String fieldName = p.lident_;
      StringWriter auxsw = new StringWriter();
      JavaWriter auxw = new JavaWriter(auxsw);
      p.pureexp_.accept(this, auxw);
      emitField(w, fieldType, fieldName, auxsw.toString(), false);
      createVarDefinition(fieldName, fieldType);
      w.emitEmptyLine();
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(MethClassBody mcb, JavaWriter w) {
    try {
      String returnType = getTypeName(mcb.type_);
      String name = mcb.lident_;
      List<String> parameters = new ArrayList<>();
      List<String> parameterTypes = new ArrayList<>();
      for (Param param : mcb.listparam_) {
        Par p = (Par) param;
        String paramType = getTypeName(p.type_);
        parameters.add(paramType);
        parameters.add(p.lident_);
        parameterTypes.add(paramType);
      }
      w.beginMethod(returnType, name, DEFAULT_MODIFIERS, parameters, Collections.emptyList());
      createMethodDefinition(returnType, name, parameterTypes);
      mcb.block_.accept(this, w);
      w.endMethod();
      w.emitEmptyLine();
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(Bloc b, JavaWriter w) {
    for (Stm stm : b.liststm_) {
      stm.accept(this, w);
    }
    return prog;
  }

  @Override
  public Prog visit(SBlock b, JavaWriter w) {
    for (Stm stm : b.liststm_) {
      stm.accept(this, w);
    }
    return prog;
  }

  @Override
  public Prog visit(SAwait p, JavaWriter w) {
    try {
      StringWriter auxsw = new StringWriter();
      JavaWriter auxjw = new JavaWriter(auxsw);
      p.guard_.accept(this, auxjw);
      w.emitStatement("%s.getValue()", auxsw.toString());

      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(FieldGuard p, JavaWriter w) {
    try {
      w.emit(p.lident_);
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
    return prog;
  }

  @Override
  public Prog visit(ExpGuard p, JavaWriter w) {
    p.pureexp_.accept(this, w);
    return prog;
  }

  @Override
  public Prog visit(VarGuard vg, JavaWriter w) {
    try {
      w.emit(vg.lident_);
      return prog;
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
  }

  @Override
  public Prog visit(SWhile sw, JavaWriter w) {
    try {
      StringWriter auxsw = new StringWriter();
      JavaWriter auxw = new JavaWriter(auxsw);
      sw.pureexp_.accept(this, auxw);
      w.beginControlFlow("while (" + auxsw.toString() + ")");
      sw.stm_.accept(this, w);
      w.endControlFlow();
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(SIf sif, JavaWriter w) {
    try {
      StringWriter auxsw = new StringWriter();
      JavaWriter auxw = new JavaWriter(auxsw);
      sif.pureexp_.accept(this, auxw);
      w.beginControlFlow("if (" + auxsw.toString() + ")");
      sif.stm_.accept(this, w);
      w.endControlFlow();
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(SIfElse se, JavaWriter w) {
    try {
      StringWriter auxsw = new StringWriter();
      JavaWriter auxw = new JavaWriter(auxsw);
      se.pureexp_.accept(this, auxw);
      w.beginControlFlow("if (" + auxsw.toString() + ")");
      se.stm_1.accept(this, w);
      w.endControlFlow();
      w.beginControlFlow("else");
      se.stm_2.accept(this, w);
      w.endControlFlow();
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public Prog visit(SDec p, JavaWriter w) {
    try {
      String fieldType = getTypeName(p.type_);
      String fieldName = p.lident_;
      emitField(w, fieldType, fieldName, null, false);
      createVarDefinition(fieldName, fieldType);
      w.emitEmptyLine();
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(SFieldAss fa, JavaWriter w) {
    try {
      Exp exp = fa.exp_;
      if (exp instanceof ExpE == false) {
        visitStatementAssignmentExp(exp, fa.lident_, null, w);
      } else if (exp instanceof ExpE) {
        ExpE expe = (ExpE) exp;
        EffExp effExp = expe.effexp_;
        if (effExp instanceof AsyncMethCall == false) {
          visitStatementAssignmentExp(exp, fa.lident_, null, w);
        } else if (effExp instanceof AsyncMethCall) {
          AsyncMethCall amc = (AsyncMethCall) effExp;
          visitAsyncMethodCall(amc, fa.lident_, w);
        }
      }
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(SAss ss, JavaWriter w) {
    try {
      Exp exp = ss.exp_;
      if (exp instanceof ExpE == false) {
        visitStatementAssignmentExp(exp, ss.lident_, null, w);
      } else if (exp instanceof ExpE) {
        ExpE expe = (ExpE) exp;
        EffExp effExp = expe.effexp_;
        if (effExp instanceof AsyncMethCall == false) {
          visitStatementAssignmentExp(exp, ss.lident_, null, w);
        } else if (effExp instanceof AsyncMethCall) {
          AsyncMethCall amc = (AsyncMethCall) effExp;
          visitAsyncMethodCall(amc, ss.lident_, w);
        }
      }
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(SDecAss p, JavaWriter w) {
    try {
      String varType = getTypeName(p.type_);
      Exp exp = p.exp_;
      if (exp instanceof ExpE == false) {
        visitStatementAssignmentExp(exp, p.lident_, varType, w);
      } else if (exp instanceof ExpE) {
        ExpE expe = (ExpE) exp;
        EffExp effExp = expe.effexp_;
        if (effExp instanceof AsyncMethCall == false) {
          visitStatementAssignmentExp(exp, p.lident_, varType, w);
        } else if (effExp instanceof AsyncMethCall) {
          AsyncMethCall amc = (AsyncMethCall) effExp;
          visitAsyncMethodCall(amc, p.lident_, w);
        }
      }
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  @Override
  public Prog visit(SReturn r, JavaWriter w) {
    try {
      StringWriter auxsw = new StringWriter();
      JavaWriter auxw = new JavaWriter(auxsw);
      r.exp_.accept(this, auxw);
      w.emitStatement("return " + auxsw.toString());
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }


  }

  @Override
  public Prog visit(SSuspend ss, JavaWriter w) {
    try {
      w.emitStatement("Thread.yield()");
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(SSkip sk, JavaWriter w) {
    LOGGER.warn("Not implemented: #visit({})", sk);
    return prog;
  }

  @Override
  public Prog visit(SExp p, JavaWriter w) {
    p.exp_.accept(this, w);
    return prog;
  }

  public Prog visit(SThrow st, JavaWriter w) {
    try {
      StringWriter auxsw = new StringWriter();
      JavaWriter auxw = new JavaWriter(auxsw);
      st.pureexp_.accept(this, auxw);
      w.emitStatement("throw " + auxsw.toString());
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(STryCatchFinally stcf, JavaWriter w) {
    try {
      w.beginControlFlow("try { ");
      stcf.stm_.accept(this, w);
      w.endControlFlow();
      for (CatchBranch cb : stcf.listcatchbranch_) {
        cb.accept(this, w);
      }
      stcf.maybefinally_.accept(this, w);
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(ExpE ee, JavaWriter w) {
    ee.effexp_.accept(this, w);
    return prog;
  }

  @Override
  public Prog visit(ExpP ep, JavaWriter w) {
    ep.pureexp_.accept(this, w);
    return prog;
  }


  @Override
  public Prog visit(ELit elit, JavaWriter w) {
    elit.literal_.accept(this, w);
    return prog;
  }

  @Override
  public Prog visit(EAdd e, JavaWriter w) {
    try {
      w.beginExpressionGroup();
      e.pureexp_1.accept(this, w);
      w.emit(" + ");
      e.pureexp_2.accept(this, w);
      w.endExpressionGroup();
      return prog;
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
  }

  @Override
  public Prog visit(ESub e, JavaWriter w) {
    try {
      w.beginExpressionGroup();
      e.pureexp_1.accept(this, w);
      w.emit(" - ");
      e.pureexp_2.accept(this, w);
      w.endExpressionGroup();
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
    return prog;
  }

  @Override
  public Prog visit(EMul e, JavaWriter w) {
    try {
      w.beginExpressionGroup();
      e.pureexp_1.accept(this, w);
      w.emit(" * ");
      e.pureexp_2.accept(this, w);
      w.endExpressionGroup();
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
    return prog;
  }

  @Override
  public Prog visit(EDiv e, JavaWriter w) {
    try {
      w.beginExpressionGroup();
      e.pureexp_1.accept(this, w);
      w.emit(" / ");
      e.pureexp_2.accept(this, w);
      w.endExpressionGroup();
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
    return prog;
  }

  @Override
  public Prog visit(EMod e, JavaWriter w) {
    try {
      w.beginExpressionGroup();
      e.pureexp_1.accept(this, w);
      w.emit(" %% ");
      e.pureexp_2.accept(this, w);
      w.endExpressionGroup();
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
    return prog;
  }

  @Override
  public Prog visit(EGe e, JavaWriter w) {
    try {
      w.beginExpressionGroup();
      e.pureexp_1.accept(this, w);
      w.emit(" >= ");
      e.pureexp_2.accept(this, w);
      w.endExpressionGroup();
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
    return prog;
  }

  @Override
  public Prog visit(EGt e, JavaWriter w) {
    try {
      w.beginExpressionGroup();
      e.pureexp_1.accept(this, w);
      w.emit(" > ");
      e.pureexp_2.accept(this, w);
      w.endExpressionGroup();
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
    return prog;
  }

  @Override
  public Prog visit(ELe e, JavaWriter w) {
    try {
      w.beginExpressionGroup();
      e.pureexp_1.accept(this, w);
      w.emit(" <= ");
      e.pureexp_2.accept(this, w);
      w.endExpressionGroup();
      return prog;
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
  }

  @Override
  public Prog visit(ELt lt, JavaWriter w) {
    try {
      w.beginExpressionGroup();
      lt.pureexp_1.accept(this, w);
      w.emit(" < ");
      lt.pureexp_2.accept(this, w);
      w.endExpressionGroup();
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
    return prog;
  }

  @Override
  public Prog visit(EAnd e, JavaWriter w) {
    try {
      w.beginExpressionGroup();
      e.pureexp_1.accept(this, w);
      w.emit(" && ");
      e.pureexp_2.accept(this, w);
      w.endExpressionGroup();
      return prog;
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
  }

  @Override
  public Prog visit(EOr e, JavaWriter w) {
    try {
      w.beginExpressionGroup();
      e.pureexp_1.accept(this, w);
      w.emit(" || ");
      e.pureexp_2.accept(this, w);
      w.endExpressionGroup();
      return prog;
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
  }

  @Override
  public Prog visit(ENeq e, JavaWriter w) {
    try {
      w.beginExpressionGroup();
      e.pureexp_1.accept(this, w);
      w.emit(" != ");
      e.pureexp_2.accept(this, w);
      w.endExpressionGroup();
      return prog;
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
  }

  @Override
  public Prog visit(EIntNeg in, JavaWriter w) {
    try {
      w.beginExpressionGroup();
      w.emit(" - ");
      in.pureexp_.accept(this, w);
      w.endExpressionGroup();
      return prog;
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
  }

  @Override
  public Prog visit(ELogNeg ln, JavaWriter w) {
    try {
      w.beginExpressionGroup();
      w.emit(" ! ");
      ln.pureexp_.accept(this, w);
      w.endExpressionGroup();
      return prog;
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
  }

  @Override
  public Prog visit(EEq e, JavaWriter w) {
    try {
      w.beginExpressionGroup();
      e.pureexp_1.accept(this, w);
      w.emit(" == ");
      e.pureexp_2.accept(this, w);
      w.endExpressionGroup();
      return prog;
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
  }

  @Override
  public Prog visit(EVar v, JavaWriter w) {
    try {
      w.emit(v.lident_);
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
    return prog;

  }

  @Override
  public Prog visit(EThis t, JavaWriter w) {
    try {
      w.emit("this." + t.lident_);
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
    return prog;
  }

  @Override
  public Prog visit(New n, JavaWriter w) {
    try {
      List<String> parameters = new ArrayList<>();
      for (PureExp par : n.listpureexp_) {
        StringWriter parSW = new StringWriter();
        JavaWriter parameterWriter = new JavaWriter(parSW);
        par.accept(this, parameterWriter);
        parameters.add(parSW.toString());
      }
      String parametersString = String.join(COMMA_SPACE, parameters);
      String oType = getTypeName(n.type_);
      w.emit("new " + oType + "(" + parametersString + ")");
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(Get g, JavaWriter w) {
    try {
      StringWriter auxsw = new StringWriter();
      JavaWriter auxw = new JavaWriter(auxsw);
      g.pureexp_.accept(this, auxw);
      w.emit(auxsw.toString() + ".get()");
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(AsyncMethCall amc, JavaWriter w) {
    try {
      visitAsyncMethodCall(amc, null, w);
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(SyncMethCall smc, JavaWriter w) {
    try {
      visitSyncMethodCall(smc, smc.lident_ + "_res", w);
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(ThisAsyncMethCall p, JavaWriter w) {
    try {
      ELit lhs = new ELit(new LThis());
      AsyncMethCall amc = new AsyncMethCall(lhs, p.lident_, p.listpureexp_);
      visitAsyncMethodCall(amc, null, w);
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(ThisSyncMethCall p, JavaWriter w) {
    try {
      ELit lhs = new ELit(new LThis());
      SyncMethCall smc = new SyncMethCall(lhs, p.lident_, p.listpureexp_);
      visitSyncMethodCall(smc, p.lident_ + "_res", w);
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(CatchBranc cb, JavaWriter w) {
    try {
      StringWriter auxsw = new StringWriter();
      JavaWriter auxw = new JavaWriter(auxsw);
      cb.pattern_.accept(this, auxw);
      w.beginControlFlow("catch(%s)", auxsw.toString());
      cb.stm_.accept(this, w);
      w.endControlFlow();
      w.emit(auxsw.toString() + ".get()");
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(JustFinally jf, JavaWriter w) {
    try {

      w.beginControlFlow("finally");
      jf.stm_.accept(this, w);
      w.endControlFlow();
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(NoFinally p, JavaWriter arg) {
    // XXX ?
    return prog;
  }

  @Override
  public Prog visit(LInt i, JavaWriter w) {
    try {
      w.emit(Integer.toString(i.integer_));
      return prog;
    } catch (IOException x) {
      throw new RuntimeException(x);
    }

  }

  @Override
  public Prog visit(LNull n, JavaWriter w) {
    try {
      w.emit(LITERAL_NULL);
      return prog;
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
  }

  @Override
  public Prog visit(LStr s, JavaWriter w) {
    try {
      w.emit("\"" + s.string_ + "\"");
      return prog;
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
  }

  @Override
  public Prog visit(LThis t, JavaWriter w) {
    try {
      w.emit(LITERAL_THIS);
      return prog;
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
  }

  @Override
  public Prog visit(AnyIden p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(AnyTyIden p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(AnyExport p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(AnyFromExport p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(StarExport p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(StarFromExport p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(AnyFromImport p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(StarFromImport p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(ForeignImport p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(NormalImport p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(TUnderscore p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(TSimple p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(TGen p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(QTyp p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(QTypeSegmen p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(TTyp p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(TTypeSegmen p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(TypeDecl p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(TypeParDecl p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(ExceptionDecl p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(DataDecl p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(DataParDecl p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(FunDecl p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(FunParDecl p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(SinglConstrIdent p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(ParamConstrIdent p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(EmptyConstrType p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(RecordConstrType p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(BuiltinFunBody p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(NormalFunBody p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(Par p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(SAssert p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(SPrint p, JavaWriter w) {
    try {
      StringWriter sw = new StringWriter();
      JavaWriter auxw = new JavaWriter(sw);
      p.pureexp_.accept(this, auxw);
      w.emitStatement("println(%s)", sw.toString());
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(AndGuard p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(Let p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(If p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(Case p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(EFunCall p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(EQualFunCall p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(ENaryFunCall p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(ENaryQualFunCall p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(EQualVar p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(ESinglConstr p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(EParamConstr p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(CaseBranc p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(PIdent p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(PLit p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(PSinglConstr p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(PParamConstr p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(PUnderscore p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(LThisDC p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(NewLocal p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(Spawns p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(SimpleAnn p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(AnnDec p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  @Override
  public Prog visit(AnnTyp p, JavaWriter arg) {
    LOGGER.warn("Not implemented: #visit({})", p);
    return prog;
  }

  protected void visitMain(Modul m, JavaWriter w) {
    try {
      JavaWriter jw = createJavaWriter(null, w);
      final String identifier = MAIN_CLASS_NAME;
      EnumSet<Modifier> mainModifiers = EnumSet.of(Modifier.PUBLIC, Modifier.STATIC);
      beginElementKind(jw, ElementKind.CLASS, identifier, DEFAULT_MODIFIERS, null, null);
      jw.emitEmptyLine();
      List<String> javaMainMethodParameters = Arrays.asList("String[]", "args");
      jw.beginMethod("void", "main", mainModifiers, javaMainMethodParameters,
          Collections.emptyList());
      m.maybeblock_.accept(this, jw);
      jw.emit(";");
      jw.emitEmptyLine();
      jw.endMethod();
      jw.endType();
      close(jw, w);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected void visitImports(final ListImport imports, JavaWriter w) throws IOException {
    w.emitStaticImports(DEFAULT_STATIC_IMPORTS);
    w.emitImports(DEFAULT_IMPORTS);
    for (Import imprt : imports) {
      imprt.accept(this, w);
    }
    w.emitEmptyLine();
  }

  protected void visitAsyncMethodCall(AsyncMethCall amc, String responseVarName, JavaWriter w)
      throws IOException {
    String calleeId = getCalleeId(amc);
    List<String> params = getCalleeMethodParams(amc);
    String methodName = amc.lident_;
    String potentialReturnType =
        findMethodReturnType(methodName, findVariableType(calleeId), params);
    String msgVarName = "msg_" + calleeId.hashCode();
    String msgStatement = generateMessageStatement(msgVarName, potentialReturnType,
        generateJavaMethodInvocation(calleeId, methodName, params));
    w.emit(msgStatement, true);
    w.emitStatementEnd();
    String sendStm = generateMessageInvocationStatement(calleeId, false, potentialReturnType,
        msgVarName, responseVarName);
    w.emit(sendStm, true);
    w.emitStatementEnd();
  }

  protected void visitSyncMethodCall(SyncMethCall smc, String responseVarName, JavaWriter w)
      throws IOException {
    String calleeId = getCalleeId(smc);
    List<String> params = getCalleeMethodParams(smc);
    String msgVarName = "msg_" + calleeId.hashCode();
    String methodName = smc.lident_;
    String potentialReturnType =
        findMethodReturnType(methodName, findVariableType(calleeId), params);
    String msgStatement = generateMessageStatement(msgVarName, potentialReturnType,
        generateJavaMethodInvocation(calleeId, methodName, params));
    w.emit(msgStatement, true);
    w.emitStatementEnd();
    String sendStm = generateMessageInvocationStatement(calleeId, false, potentialReturnType,
        msgVarName, responseVarName);
    w.emit(sendStm, true);
    w.emitStatementEnd();
    w.emit(responseVarName + ".getValue()", true);
    w.emitStatementEnd();
  }

  protected void visitStatementAssignmentExp(Exp exp, String varName, String varType, JavaWriter w)
      throws IOException {
    StringWriter auxsw = new StringWriter();
    JavaWriter auxw = new JavaWriter(auxsw);
    exp.accept(this, auxw);
    if (varType == null) {
      w.emitStatement("%s = %s", varName, auxsw.toString());
    } else {
      w.emitStatement("%s %s = %s", varType, varName, auxsw.toString());
      createVarDefinition(varName, varType);
    }
  }

  protected List<String> getCalleeMethodParams(AsyncMethCall amc) {
    List<String> parameters = new ArrayList<>();
    for (PureExp par : amc.listpureexp_) {
      StringWriter parSW = new StringWriter();
      JavaWriter parameterWriter = new JavaWriter(parSW);
      par.accept(this, parameterWriter);
      parameters.add(parSW.toString());
    }
    return parameters;
  }

  protected List<String> getCalleeMethodParams(SyncMethCall smc) {
    List<String> parameters = new ArrayList<>();
    for (PureExp par : smc.listpureexp_) {
      StringWriter parSW = new StringWriter();
      JavaWriter parameterWriter = new JavaWriter(parSW);
      par.accept(this, parameterWriter);
      parameters.add(parSW.toString());
    }
    return parameters;
  }

  protected String getCalleeId(AsyncMethCall amc) {
    StringWriter auxsw = new StringWriter();
    JavaWriter auxw = new JavaWriter(auxsw);
    amc.pureexp_.accept(this, auxw);
    String calleeId = auxsw.toString();
    return calleeId;
  }

  protected String getCalleeId(SyncMethCall smc) {
    StringWriter auxsw = new StringWriter();
    JavaWriter auxw = new JavaWriter(auxsw);
    smc.pureexp_.accept(this, auxw);
    String calleeId = auxsw.toString();
    return calleeId;
  }

  /**
   * Creates a Java method invocation:
   * 
   * <pre>
   * myObj.myMethod(p1, p2, p3)
   * </pre>
   * 
   * @param object the callee object
   * @param method the method of the callee object
   * @param parameters the parameters of the method that can be
   *        empty string
   * @return a string representing a Java method invocation
   *         statement
   */
  protected String generateJavaMethodInvocation(String object, String method,
      List<String> parameters) {
    return String.format("%s.%s(%s)", object, method,
        parameters == null || parameters.isEmpty() ? "" : String.join(COMMA_SPACE, parameters));
  }

  /**
   * Create an asynchronous message in the context of ABS API
   * which is either an instance of {@link Runnable} or a
   * {@link Callable}.
   * 
   * @param msgVarName the variable name to use the created
   *        message
   * @param returnType the return type of the message; if
   *        <code>null</code>, it means to use {@link Runnable}
   * @param expression the Java expression to use for the body
   *        of the lambda expression
   * @return a string in Java representing a lambda expression
   *         for a {@link Runnable} or a {@link Callable}
   */
  protected String generateMessageStatement(String msgVarName, String returnType,
      String expression) {
    final String returnTypeVar = returnType == null ? "Runnable" : "Callable<" + returnType + ">";
    return String.format("%s %s = () -> %s", returnTypeVar, msgVarName, expression);
  }

  /**
   * Create a Java statement when sending a message to an
   * {@link Actor} in the ABS API.
   * 
   * @param target the receiver identifier of the message
   * @param await if this is an await statement; If
   *        <code>true</code>,
   *        {@link Actor#await(Object, Object)} is generated
   *        otherwise {@link Actor#send(Object, Object)}
   * @param msgReturnType the expected return type of the
   *        message; if <code>null</code>, the generated
   *        {@link Response} will be over {@link Void}
   * @param msgVarName the variable name of the message
   * @param responseVarName the variable of the generated
   *        {@link Response}; can be <code>null</code>
   * @return a Java statement string for such a call
   */
  protected String generateMessageInvocationStatement(String target, final boolean await,
      String msgReturnType, String msgVarName, String responseVarName) {
    final String method = await ? "await" : "send";
    final String returnType = msgReturnType == null ? "Void" : msgReturnType;
    return responseVarName == null ? String.format("%s(%s, %s)", method, target, msgVarName)
        : String.format("Response<%s> %s = %s(%s, %s)", returnType, responseVarName, method, target,
            msgVarName);
  }

  /**
   * @param qtype
   * @return
   */
  protected String getQTypeName(QType qtype) {
    if (qtype instanceof QTyp) {
      QTyp qtyp = (QTyp) qtype;
      QTypeSegment qtypesegment_ = qtyp.listqtypesegment_.iterator().next();
      if (qtypesegment_ instanceof QTypeSegmen) {
        QTypeSegmen qTypeSegmen = (QTypeSegmen) qtypesegment_;
        return translate(qTypeSegmen.uident_);
      }
    }
    return null;
  }

  /**
   * @param qtypes
   * @return
   */
  protected List<String> toList(ListQType qtypes) {
    return qtypes.stream().map(qtype -> getQTypeName(qtype)).collect(Collectors.toList());
  }

  /**
   * @param w
   * @param fieldType
   * @param fieldIdentifier
   * @param initialValue
   * @param isFinal
   * @return
   * @throws IOException
   */
  protected JavaWriter emitField(JavaWriter w, String fieldType, String fieldIdentifier,
      String initialValue, final boolean isFinal) throws IOException {
    EnumSet<Modifier> modifiers = EnumSet.noneOf(Modifier.class);
    if (isFinal) {
      modifiers.add(Modifier.FINAL);
    }
    if (initialValue != null) {
      return w.emitField(fieldType, fieldIdentifier, modifiers, initialValue);
    } else {
      return w.emitField(fieldType, fieldIdentifier, modifiers);
    }
  }

  /**
   * Begin a Java type.
   * 
   * @param w the Java writer
   * @param kind See {@link ElementKind}
   * @param identifier the Java identifier of the type
   * @param modifiers the set of {@link Modifier}s
   * @param classParentType the extending type that can be
   *        <code>null</code>
   * @param implementingInterfaces the implementing interface
   *        that can be <code>null</code>
   * @throws IOException Exception from {@link JavaWriter}
   * @throws IllegalArgumentException if kind other than "class"
   *         or "interface" is requested
   */
  protected void beginElementKind(JavaWriter w, ElementKind kind, String identifier,
      Set<Modifier> modifiers, String classParentType, Collection<String> implementingInterfaces)
          throws IOException {
    switch (kind) {
      case CLASS:
        Set<String> implementsTypes = new HashSet<>();
        implementsTypes.add(ABS_API_ACTOR_CLASS);
        if (implementingInterfaces != null && !implementingInterfaces.isEmpty()) {
          implementsTypes.addAll(implementingInterfaces);
        }
        w.beginType(identifier, kind.name().toLowerCase(), modifiers, classParentType,
            implementsTypes.toArray(new String[0]));
        return;
      case INTERFACE:
        w.beginType(identifier, kind.name().toLowerCase(), modifiers, ABS_API_ACTOR_CLASS,
            new String[0]);
        return;
      default:
        throw new IllegalArgumentException("Unsupported Java element kind: " + kind);
    }
  }

  /**
   * @param decl
   * @param w
   * @return
   * @throws IOException
   */
  protected JavaWriter createJavaWriter(Decl decl, JavaWriter w) throws IOException {
    if (isTopLevel(decl)) {
      String identifier = getTopLevelDeclIdentifier(decl);
      if (packageName.equalsIgnoreCase(identifier) || moduleNames.contains(identifier)) {
        return w;
      }
      JavaWriter jw = javaWriterSupplier.apply(identifier);
      jw.emitPackage(packageName);
      return jw;
    } else {
      return w;
    }
  }

  /**
   * @param childWriter
   * @param parentWriter
   * @throws IOException
   */
  protected void close(JavaWriter childWriter, JavaWriter parentWriter) throws IOException {
    if (childWriter != parentWriter) {
      childWriter.close();
    }
  }

  /**
   * @param decl
   * @return
   */
  protected boolean isTopLevel(Decl decl) {
    return decl instanceof ClassDecl || decl instanceof ClassImplements
        || decl instanceof ClassParamDecl || decl instanceof ClassParamImplements
        || decl instanceof ExtendsDecl || decl instanceof InterfDecl || decl == null;
  }

  /**
   * @param decl
   * @return
   */
  protected <C extends Decl> String getTopLevelDeclIdentifier(Decl decl) {
    if (decl instanceof ClassDecl) {
      return ((ClassDecl) decl).uident_;
    }
    if (decl instanceof ClassImplements) {
      return ((ClassImplements) decl).uident_;
    }
    if (decl instanceof ClassParamDecl) {
      return ((ClassParamDecl) decl).uident_;
    }
    if (decl instanceof ClassParamImplements) {
      return ((ClassParamImplements) decl).uident_;
    }
    if (decl instanceof ExtendsDecl) {
      return ((ExtendsDecl) decl).uident_;
    }
    if (decl instanceof InterfDecl) {
      return ((InterfDecl) decl).uident_;
    }
    if (decl instanceof FunDecl) {
      return ((FunDecl) decl).lident_;
    }
    if (decl == null) {
      return MAIN_CLASS_NAME;
    }
    throw new IllegalArgumentException("Unknown top level type: " + decl);
  }

  /**
   * @param type
   * @return
   */
  protected String translate(String type) {
    if (type == null) {
      return null;
    }
    return javaTypeTranslator.apply(type);
  }

  private String getTypeName(Type type) {
    if (type instanceof TSimple) {
      TSimple ts = (TSimple) type;
      QType qtype_ = ts.qtype_;
      return getQTypeName(qtype_);
    }
    if (type instanceof TGen) {
      TGen tg = (TGen) type;
      StringBuilder sQ = new StringBuilder(getQTypeName(tg.qtype_));
      sQ.append("<");
      List<String> gTypes = new ArrayList<String>();
      for (Type t : tg.listtype_) {
        gTypes.add(getTypeName(t));
      }
      sQ.append(String.join(COMMA_SPACE, gTypes));
      sQ.append(">");
      return sQ.toString();
    }
    return null;
  }

  private String findVariableType(String varName) {
    for (String fqClassName : VARIABLES.keySet()) {
      if (!fqClassName.startsWith(this.packageName)) {
        continue;
      }
      Collection<VarDefinition> variables = VARIABLES.get(fqClassName);
      for (VarDefinition vd : variables) {
        if (vd.getName().equals(varName)) {
          return vd.getType();
        }
      }
    }
    return null;
  }

  private String findMethodReturnType(String methodName, String calleeClassType,
      List<String> actualParamNames) {
    for (String fqClassName : METHODS.keySet()) {
      if (calleeClassType != null && !fqClassName.endsWith(calleeClassType)) {
        continue;
      }
      Collection<MethodDefinition> methods = METHODS.get(fqClassName);
      for (MethodDefinition md : methods) {
        if (md.matches(methodName, actualParamNames)) {
          return md.type();
        }
      }
    }
    return null;
  }

  private void createVarDefinition(String varName, String varType) {
    Module current = currentModule();
    if (current == null) {
      throw new IllegalStateException("No current module is available.");
    }
    String clazz = getQTypeName(((Modul) current).qtype_);
    String fqClassName = this.packageName + "." + clazz;
    VarDefinition vd = new VarDefinition(varName, varType);
    VARIABLES.put(fqClassName, vd);
  }

  private void createMethodDefinition(String returnType, String name, List<String> parameters) {
    Module current = currentModule();
    if (current == null) {
      throw new IllegalStateException("No current module is available.");
    }
    String clazz = getQTypeName(((Modul) current).qtype_);
    String fqClassName = this.packageName + "." + clazz;
    MethodDefinition md = new MethodDefinition(fqClassName, returnType, name, parameters);
    METHODS.put(fqClassName, md);
  }

  private Module currentModule() {
    return this.modules.peek();
  }

  private void determineProgramDeclarationTypes(Prog program) {
    /*
     * ABS allows for SAME naming of an interface and an
     * implementing class. To be able to properly compile this,
     * we need to eagerly identify the elements of an ABS
     * program. Strategy of compiling:
     * 
     * 1. Compile interfaces to separate files
     * 
     * 2. Compile classes to separate files
     */

    elements.clear();

    for (ElementKind k : EnumSet.of(ElementKind.INTERFACE, ElementKind.CLASS)) {
      elements.put(k, new HashSet<>());
    }
    for (Module module : program.listmodule_) {
      Modul m = (Modul) module;
      for (Decl decl : m.listdecl_) {
        if (decl instanceof InterfDecl || decl instanceof ExtendsDecl) {
          elements.get(ElementKind.INTERFACE).add(decl);
        }
      }
    }
    for (Module module : program.listmodule_) {
      Modul m = (Modul) module;
      for (Decl decl : m.listdecl_) {
        if (decl instanceof InterfDecl || decl instanceof ExtendsDecl) {
          continue;
        }
        final String className = getTopLevelDeclIdentifier(decl);
        if (elements.get(ElementKind.INTERFACE).stream()
            .anyMatch(d -> className.equals(getTopLevelDeclIdentifier(d)))) {
          classNames.put(className, className + "Impl");
        } else {
          classNames.put(className, className);
        }
        elements.get(ElementKind.CLASS).add(decl);
      }
    }
  }

  private String getRefinedClassName(String name) {
    return classNames.get(name);
  }

}
