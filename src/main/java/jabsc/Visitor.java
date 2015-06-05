package jabsc;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.element.Modifier;

import com.squareup.javawriter.JavaWriter;

import bnfc.abs.AbstractVisitor;
import bnfc.abs.Absyn.InterfDecl;
import bnfc.abs.Absyn.MethSig;
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
public class Visitor extends AbstractVisitor<Prog, JavaWriter> {

  private static final Set<Modifier> DEFAULT_MODIFIERS = Collections.singleton(Modifier.PUBLIC);

  private final Prog prog;

  public Visitor(Prog prog) {
    this.prog = prog;
  }

  @Override
  public Prog visit(InterfDecl id, JavaWriter w) {
    try {
      w.beginType(id.uident_, "interface", DEFAULT_MODIFIERS);
      id.listmethsignat_.forEach(sig -> visit((MethSig) sig, w));
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Prog visit(MethSig ms, JavaWriter w) {
    try {
      String returnType = getSimpleTypeName(ms.type_);
      String name = ms.lident_;
      List<String> parameters =
          ms.listparam_.stream().map(this::getParameter).collect(Collectors.toList());
      w.beginMethod(returnType, name, DEFAULT_MODIFIERS, parameters, Collections.emptyList());
      return prog;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private String getSimpleTypeName(Type type) {
    if (type instanceof TSimple) {
      TSimple ts = (TSimple) type;
      QType qtype_ = ts.qtype_;
      if (qtype_ instanceof QTyp) {
        QTyp qtyp = (QTyp) qtype_;
        QTypeSegment qtypesegment_ = qtyp.listqtypesegment_.iterator().next();
        if (qtypesegment_ instanceof QTypeSegmen) {
          QTypeSegmen qTypeSegmen = (QTypeSegmen) qtypesegment_;
          return qTypeSegmen.uident_;
        }
      }
    }
    return null;
  }

  private String getParameter(Param p) {
    Par par = (Par) p;
    return getSimpleTypeName(par.type_) + " " + par.lident_;
  }

}
