package bnfc.abs.Absyn; // Java Package generated by the BNF Converter.

public class SCase extends Stm {
  public final PureExp pureexp_;
  public final ListCatchBranch listcatchbranch_;
  public SCase(PureExp p1, ListCatchBranch p2) { pureexp_ = p1; listcatchbranch_ = p2; }

  public <R,A> R accept(bnfc.abs.Absyn.Stm.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof bnfc.abs.Absyn.SCase) {
      bnfc.abs.Absyn.SCase x = (bnfc.abs.Absyn.SCase)o;
      return this.pureexp_.equals(x.pureexp_) && this.listcatchbranch_.equals(x.listcatchbranch_);
    }
    return false;
  }

  public int hashCode() {
    return 37*(this.pureexp_.hashCode())+this.listcatchbranch_.hashCode();
  }


}
