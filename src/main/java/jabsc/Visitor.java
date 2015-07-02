package jabsc;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;

import abs.api.Actor;
import bnfc.abs.AbstractVisitor;
import bnfc.abs.Absyn.AnyImport;
import bnfc.abs.Absyn.AsyncMethCall;
import bnfc.abs.Absyn.Bloc;
import bnfc.abs.Absyn.CatchBranc;
import bnfc.abs.Absyn.CatchBranch;
import bnfc.abs.Absyn.ClassBody;
import bnfc.abs.Absyn.ClassDecl;
import bnfc.abs.Absyn.ClassImplements;
import bnfc.abs.Absyn.ClassParamDecl;
import bnfc.abs.Absyn.ClassParamImplements;
import bnfc.abs.Absyn.Decl;
import bnfc.abs.Absyn.EAdd;
import bnfc.abs.Absyn.EAnd;
import bnfc.abs.Absyn.EDiv;
import bnfc.abs.Absyn.EEq;
import bnfc.abs.Absyn.EGe;
import bnfc.abs.Absyn.EGt;
import bnfc.abs.Absyn.EIntNeg;
import bnfc.abs.Absyn.ELe;
import bnfc.abs.Absyn.ELit;
import bnfc.abs.Absyn.ELogNeg;
import bnfc.abs.Absyn.ELt;
import bnfc.abs.Absyn.EMod;
import bnfc.abs.Absyn.EMul;
import bnfc.abs.Absyn.ENeq;
import bnfc.abs.Absyn.EOr;
import bnfc.abs.Absyn.ESub;
import bnfc.abs.Absyn.EThis;
import bnfc.abs.Absyn.EVar;
import bnfc.abs.Absyn.ExpE;
import bnfc.abs.Absyn.ExpP;
import bnfc.abs.Absyn.ExtendsDecl;
import bnfc.abs.Absyn.FieldAssignClassBody;
import bnfc.abs.Absyn.FieldClassBody;
import bnfc.abs.Absyn.ForeignImport;
import bnfc.abs.Absyn.Get;
import bnfc.abs.Absyn.Import;
import bnfc.abs.Absyn.ImportType;
import bnfc.abs.Absyn.InterfDecl;
import bnfc.abs.Absyn.JustBlock;
import bnfc.abs.Absyn.JustFinally;
import bnfc.abs.Absyn.LInt;
import bnfc.abs.Absyn.LNull;
import bnfc.abs.Absyn.LStr;
import bnfc.abs.Absyn.LThis;
import bnfc.abs.Absyn.ListQType;
import bnfc.abs.Absyn.MethClassBody;
import bnfc.abs.Absyn.MethSig;
import bnfc.abs.Absyn.Modul;
import bnfc.abs.Absyn.Module;
import bnfc.abs.Absyn.New;
import bnfc.abs.Absyn.NoBlock;
import bnfc.abs.Absyn.NoFinally;
import bnfc.abs.Absyn.Par;
import bnfc.abs.Absyn.Param;
import bnfc.abs.Absyn.Prog;
import bnfc.abs.Absyn.PureExp;
import bnfc.abs.Absyn.QTyp;
import bnfc.abs.Absyn.QType;
import bnfc.abs.Absyn.QTypeSegmen;
import bnfc.abs.Absyn.QTypeSegment;
import bnfc.abs.Absyn.SAss;
import bnfc.abs.Absyn.SAwait;
import bnfc.abs.Absyn.SBlock;
import bnfc.abs.Absyn.SDec;
import bnfc.abs.Absyn.SDecAss;
import bnfc.abs.Absyn.SExp;
import bnfc.abs.Absyn.SFieldAss;
import bnfc.abs.Absyn.SIf;
import bnfc.abs.Absyn.SIfElse;
import bnfc.abs.Absyn.SReturn;
import bnfc.abs.Absyn.SSkip;
import bnfc.abs.Absyn.SSuspend;
import bnfc.abs.Absyn.SThrow;
import bnfc.abs.Absyn.STryCatchFinally;
import bnfc.abs.Absyn.SWhile;
import bnfc.abs.Absyn.Stm;
import bnfc.abs.Absyn.TGen;
import bnfc.abs.Absyn.TSimple;
import bnfc.abs.Absyn.TTyp;
import bnfc.abs.Absyn.TTypeSegmen;
import bnfc.abs.Absyn.TTypeSegment;
import bnfc.abs.Absyn.Type;
import bnfc.abs.Absyn.VarGuard;

/**
 * The visitor for all possible nodes in the AST of an ABS
 * program.
 */
class Visitor extends AbstractVisitor<Prog, JavaWriter> {

  private static final String LITERAL_THIS = "this";
  private static final String LITERAL_NULL = "null";
  private static final String MAIN_CLASS_NAME = "Main";
  private static final String COMMA_SPACE = ", ";
  private static final String ABS_API_ACTOR_CLASS = Actor.class.getName();
  private static final Set<Modifier> DEFAULT_MODIFIERS = Collections.singleton(Modifier.PUBLIC);


  private final Set<String> moduleNames;
  private final Prog prog;
  private final JavaWriterSupplier javaWriterSupplier;
  private final String packageName;
  private final JavaTypeTranslator javaTypeTranslator;

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
      for (Module module : ((Prog) p).listmodule_) {
        moduleNames.add(getQTypeName(((Modul) module).qtype_));
        module.accept(this, w);
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
      w.emitPackage(packageName);
      for (Import imprt : m.listimport_) {
        imprt.accept(this, w);
      }
      for (Decl decl : m.listdecl_) {
        JavaWriter jw = createJavaWriter(decl, w);
        decl.accept(this, jw);
        jw.emitEmptyLine();
        close(jw, w);
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
      beginElementKind(w, ElementKind.CLASS, identifier, DEFAULT_MODIFIERS, null, null);
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
      beginElementKind(w, ElementKind.CLASS, identifier, DEFAULT_MODIFIERS, null,
          toList(ci.listqtype_));
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
      beginElementKind(w, ElementKind.CLASS, identifier, DEFAULT_MODIFIERS, null, null);
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
      beginElementKind(w, ElementKind.CLASS, identifier, DEFAULT_MODIFIERS, null,
          toList(cpi.listqtype_));
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
      for (Param param : ms.listparam_) {
        Par p = (Par) param;
        parameters.add(getTypeName(p.type_));
        parameters.add(p.lident_);
      }
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
      emitField(w, fieldType, p.lident_, null, false);
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
      StringWriter auxsw = new StringWriter();
      JavaWriter auxw = new JavaWriter(auxsw);
      p.pureexp_.accept(this, auxw);
      emitField(w, fieldType, p.lident_, auxsw.toString(), false);
      w.emitEmptyLine();
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(MethClassBody p, JavaWriter w) {
    try {
      String returnType = getTypeName(p.type_);
      String name = p.lident_;
      List<String> parameters = new ArrayList<>();
      for (Param param : p.listparam_) {
        Par p1 = (Par) param;
        parameters.add(getTypeName(p1.type_));
        parameters.add(p1.lident_);
      }
      w.beginMethod(returnType, name, DEFAULT_MODIFIERS, parameters, Collections.emptyList());
      p.block_.accept(this, w);
      w.endMethod();
      w.emitEmptyLine();
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(Bloc b, JavaWriter w) {
    try {
      for (Stm stm : b.liststm_) {
        if (stm instanceof SAwait) {

          SAwait sa = (SAwait) stm;
          StringWriter auxsw = new StringWriter();
          JavaWriter auxw = new JavaWriter(auxsw);
          sa.guard_.accept(this, auxw);
          w.beginControlFlow("if (%s.isDone)", auxsw.toString());
          for (Stm aStm : b.liststm_) {
            if (b.liststm_.indexOf(aStm) > b.liststm_.indexOf(stm))
              aStm.accept(this, w);
          }
          w.endControlFlow();
          w.beginControlFlow("else");
          w.beginControlFlow("Runnable msg = () -> ");
          for (Stm aStm : b.liststm_) {
            if (b.liststm_.indexOf(aStm) > b.liststm_.indexOf(stm))
              aStm.accept(this, w);
          }
          w.endControlFlow("");
          w.emitStatement("send(%s, msg)", "this");
          w.endControlFlow();
          break;
        } else {
          stm.accept(this, w);
        }
      }
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public Prog visit(SBlock b, JavaWriter w) {
    try {
      for (Stm stm : b.liststm_) {
        if (stm instanceof SAwait) {

          SAwait sa = (SAwait) stm;
          StringWriter auxsw = new StringWriter();
          JavaWriter auxw = new JavaWriter(auxsw);
          sa.guard_.accept(this, auxw);
          w.beginControlFlow("if (%s.isDone)", auxsw.toString());
          for (Stm aStm : b.liststm_) {
            if (b.liststm_.indexOf(aStm) > b.liststm_.indexOf(stm))
              aStm.accept(this, w);
          }
          w.endControlFlow();
          w.beginControlFlow("else");
          w.beginControlFlow("Runnable msg = () -> ");
          for (Stm aStm : b.liststm_) {
            if (b.liststm_.indexOf(aStm) > b.liststm_.indexOf(stm))
              aStm.accept(this, w);
          }
          w.endControlFlow("");
          w.emitStatement("send(%s, msg)", "this");
          w.endControlFlow();
          break;
        } else {
          stm.accept(this, w);
        }
      }
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
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
      emitField(w, fieldType, p.lident_, null, false);
      w.emitEmptyLine();
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(SDecAss p, JavaWriter w) {
    try {
      String fieldType = getTypeName(p.type_);
      StringWriter auxsw = new StringWriter();
      JavaWriter auxw = new JavaWriter(auxsw);
      p.exp_.accept(this, auxw);
      emitField(w, fieldType, p.lident_, auxsw.toString(), false);
      w.emitEmptyLine();
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(SFieldAss fa, JavaWriter w) {
    try {
      StringWriter auxsw = new StringWriter();
      JavaWriter auxw = new JavaWriter(auxsw);
      fa.exp_.accept(this, auxw);
      w.emitStatement("%s = %s", fa.lident_, auxsw.toString());
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(SAss ss, JavaWriter w) {
    try {
      StringWriter auxsw = new StringWriter();
      JavaWriter auxw = new JavaWriter(auxsw);
      ss.exp_.accept(this, auxw);
      w.emitStatement(ss.lident_ + " = " + auxsw.toString());
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
    // TODO Auto-generated method stub
    return prog;
  }

  @Override
  public Prog visit(SExp p, JavaWriter w) {
    p.exp_.accept(this, w);
    return prog;
  }
  
  public Prog visit(SThrow st, JavaWriter w){
    try {
      StringWriter auxsw = new StringWriter();
      JavaWriter auxw = new JavaWriter(auxsw);
      st.pureexp_.accept(this, auxw);
      w.emitStatement("throw "+auxsw.toString());
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
        w.emit("this."+t.lident_);
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
      StringWriter auxsw = new StringWriter();
      JavaWriter auxw = new JavaWriter(auxsw);
      amc.pureexp_.accept(this, auxw);
      List<String> parameters = new ArrayList<>();
      for (PureExp par : amc.listpureexp_) {
        StringWriter parSW = new StringWriter();
        JavaWriter parameterWriter = new JavaWriter(parSW);
        par.accept(this, parameterWriter);
        parameters.add(parSW.toString());
      }
      String parametersString = String.join(COMMA_SPACE, parameters);
      String receiverId = auxsw.toString();
      // w.emitStatement("Runnable msg = () -> %s.%s(%s)",
      // receiverId, amc.lident_, parametersString);
      // w.emitStatement("send(%s, () -> %s.%s(%s))",
      // receiverId,amc.lident_, parametersString);
      w.emit("send(" + receiverId + ", () -> " + receiverId + "." + amc.lident_ + "("
          + parametersString + "))");
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
  public Prog visit(VarGuard vg, JavaWriter w) {
    try {
      w.emit(vg.lident_);
      return prog;
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
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

}
