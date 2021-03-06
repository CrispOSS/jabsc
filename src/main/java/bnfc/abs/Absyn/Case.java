package bnfc.abs.Absyn; // Java Package generated by the BNF Converter.

public class Case extends PureExp {
  public final PureExp pureexp_;
  public final ListCaseBranch listcasebranch_;
  public Case(PureExp p1, ListCaseBranch p2) { pureexp_ = p1; listcasebranch_ = p2; }

  public <R,A> R accept(bnfc.abs.Absyn.PureExp.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof bnfc.abs.Absyn.Case) {
      bnfc.abs.Absyn.Case x = (bnfc.abs.Absyn.Case)o;
      return this.pureexp_.equals(x.pureexp_) && this.listcasebranch_.equals(x.listcasebranch_);
    }
    return false;
  }

  public int hashCode() {
    return 37*(this.pureexp_.hashCode())+this.listcasebranch_.hashCode();
  }


}
