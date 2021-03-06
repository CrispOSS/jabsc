package bnfc.abs.Absyn; // Java Package generated by the BNF Converter.

public class EVar extends PureExp {
  public final String lident_;
  public EVar(String p1) { lident_ = p1; }

  public <R,A> R accept(bnfc.abs.Absyn.PureExp.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof bnfc.abs.Absyn.EVar) {
      bnfc.abs.Absyn.EVar x = (bnfc.abs.Absyn.EVar)o;
      return this.lident_.equals(x.lident_);
    }
    return false;
  }

  public int hashCode() {
    return this.lident_.hashCode();
  }


}
