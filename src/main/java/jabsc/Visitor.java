package jabsc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;

import com.squareup.javawriter.JavaWriter;

import bnfc.abs.AbstractVisitor;
import bnfc.abs.Absyn.AnyImport;
import bnfc.abs.Absyn.ClassBody;
import bnfc.abs.Absyn.ClassDecl;
import bnfc.abs.Absyn.ClassImplements;
import bnfc.abs.Absyn.Decl;
import bnfc.abs.Absyn.ExtendsDecl;
import bnfc.abs.Absyn.FieldAssignClassBody;
import bnfc.abs.Absyn.FieldClassBody;
import bnfc.abs.Absyn.Import;
import bnfc.abs.Absyn.InterfDecl;
import bnfc.abs.Absyn.ListQType;
import bnfc.abs.Absyn.MethClassBody;
import bnfc.abs.Absyn.MethSig;
import bnfc.abs.Absyn.Modul;
import bnfc.abs.Absyn.Module;
import bnfc.abs.Absyn.Par;
import bnfc.abs.Absyn.Param;
import bnfc.abs.Absyn.Prog;
import bnfc.abs.Absyn.QTyp;
import bnfc.abs.Absyn.QType;
import bnfc.abs.Absyn.QTypeSegmen;
import bnfc.abs.Absyn.QTypeSegment;
import bnfc.abs.Absyn.TSimple;
import bnfc.abs.Absyn.Type;

/**
 * The visitor for all possible nodes in the AST of an ABS
 * program.
 */
class Visitor extends AbstractVisitor<Prog, JavaWriter> {

  private static final Set<Modifier> DEFAULT_MODIFIERS = Collections.singleton(Modifier.PUBLIC);

  private final Prog prog;

  public Visitor(Prog prog) {
    this.prog = prog;
  }

  @Override
  public Prog visit(Prog p, JavaWriter w) {
    try {
      for (Module module : ((Prog) p).listmodule_) {
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
      String moduleName = getQTypeName(m.qtype_);
      w.emitPackage(moduleName);
      for (Import imprt : m.listimport_) {
        imprt.accept(this, w);
      }
      for (Decl decl : m.listdecl_) {
        decl.accept(this, w);
      }
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(AnyImport p, JavaWriter w) {
    return prog;
  }

  @Override
  public Prog visit(InterfDecl id, JavaWriter w) {
    try {
      w.beginType(id.uident_, "interface", DEFAULT_MODIFIERS, "abs.api.Actor");
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
      
      /*for (QType impl : ed.listqtype_) {
        impl.accept(this, w);
      }
      */
      
      w.beginType(ed.uident_, "interface", DEFAULT_MODIFIERS, "abs.api.Actor, "+getQTypeList(ed.listqtype_));
      w.emitEmptyLine();
      ed.listmethsignat_.forEach(sig -> visit((MethSig) sig, w));
      w.endType();
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }   
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
  public Prog visit(ClassDecl p, JavaWriter w) {
    try {
      w.beginType(p.uident_, "class", DEFAULT_MODIFIERS, "abs.api.Actor");
      w.emitEmptyLine();

      for (ClassBody cb : p.listclassbody_1) {
        cb.accept(this, w);
      }
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
  public Prog visit(ClassImplements p, JavaWriter w) {
    try {
      
      /*
      for (QType impl : p.listqtype_) {
        impl.accept(this, w);
      }
      */
      
      w.beginType(p.uident_, "class", DEFAULT_MODIFIERS, getQTypeList(p.listqtype_));
      w.emitEmptyLine();

      for (ClassBody cb : p.listclassbody_1) {
        cb.accept(this, w);
      }
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
  public Prog visit(FieldClassBody p, JavaWriter w) {
    try {
      String fType = getTypeName(p.type_);
      w.emitField(fType, p.lident_);
      w.emitEmptyLine();
      return prog;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      throw new RuntimeException(e);
    }

  }

  @Override
  public Prog visit(FieldAssignClassBody p, JavaWriter w) {
    try {
      String fType = getTypeName(p.type_);
      p.pureexp_.accept(this, w);
      w.emitField(fType, p.lident_, DEFAULT_MODIFIERS, "TODO:visit PureExpresion");
      w.emitEmptyLine();
      return prog;
    } catch (IOException e) {
      // TODO Auto-generated catch block
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

  


  private String getTypeName(Type type) {
    if (type instanceof TSimple) {
      TSimple ts = (TSimple) type;
      QType qtype_ = ts.qtype_;
      return getQTypeName(qtype_);
    }
    return null;
  }

  protected String getQTypeName(QType qtype) {
    if (qtype instanceof QTyp) {
      QTyp qtyp = (QTyp) qtype;
      QTypeSegment qtypesegment_ = qtyp.listqtypesegment_.iterator().next();
      if (qtypesegment_ instanceof QTypeSegmen) {
        QTypeSegmen qTypeSegmen = (QTypeSegmen) qtypesegment_;
        return qTypeSegmen.uident_;
      }
    }
    return null;
  }
  
  protected String getQTypeList(ListQType lqtype) {
    StringBuilder s= new StringBuilder("");
    
    for (QType qtype : lqtype) {
      s.append(getQTypeName(qtype)+", ");      
    }
    s.deleteCharAt(s.length()-1);
    return s.toString();
  }
  

}
