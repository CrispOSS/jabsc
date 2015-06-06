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
import bnfc.abs.Absyn.Decl;
import bnfc.abs.Absyn.Import;
import bnfc.abs.Absyn.InterfDecl;
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
      w.beginType(id.uident_, "interface", DEFAULT_MODIFIERS);
      w.emitEmptyLine();
      id.listmethsignat_.forEach(sig -> visit((MethSig) sig, w));
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

}
