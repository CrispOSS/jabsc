package bnfc.abs.Absyn; // Java Package generated by the BNF Converter.

public class ProNew extends EffExp {
  public ProNew() { }

  public <R,A> R accept(bnfc.abs.Absyn.EffExp.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof bnfc.abs.Absyn.ProNew) {
      return true;
    }
    return false;
  }

  public int hashCode() {
    return 37;
  }


}