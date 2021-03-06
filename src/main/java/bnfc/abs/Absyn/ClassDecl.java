package bnfc.abs.Absyn; // Java Package generated by the BNF Converter.

public class ClassDecl extends Decl {
  public final String uident_;
  public final ListClassBody listclassbody_1, listclassbody_2;
  public final MaybeBlock maybeblock_;
  public ClassDecl(String p1, ListClassBody p2, MaybeBlock p3, ListClassBody p4) { uident_ = p1; listclassbody_1 = p2; maybeblock_ = p3; listclassbody_2 = p4; }

  public <R,A> R accept(bnfc.abs.Absyn.Decl.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof bnfc.abs.Absyn.ClassDecl) {
      bnfc.abs.Absyn.ClassDecl x = (bnfc.abs.Absyn.ClassDecl)o;
      return this.uident_.equals(x.uident_) && this.listclassbody_1.equals(x.listclassbody_1) && this.maybeblock_.equals(x.maybeblock_) && this.listclassbody_2.equals(x.listclassbody_2);
    }
    return false;
  }

  public int hashCode() {
    return 37*(37*(37*(this.uident_.hashCode())+this.listclassbody_1.hashCode())+this.maybeblock_.hashCode())+this.listclassbody_2.hashCode();
  }


}
