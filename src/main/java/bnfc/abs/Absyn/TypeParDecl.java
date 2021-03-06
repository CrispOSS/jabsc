package bnfc.abs.Absyn; // Java Package generated by the BNF Converter.

public class TypeParDecl extends Decl {
  public final String uident_;
  public final ListUIdent listuident_;
  public final Type type_;
  public TypeParDecl(String p1, ListUIdent p2, Type p3) { uident_ = p1; listuident_ = p2; type_ = p3; }

  public <R,A> R accept(bnfc.abs.Absyn.Decl.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof bnfc.abs.Absyn.TypeParDecl) {
      bnfc.abs.Absyn.TypeParDecl x = (bnfc.abs.Absyn.TypeParDecl)o;
      return this.uident_.equals(x.uident_) && this.listuident_.equals(x.listuident_) && this.type_.equals(x.type_);
    }
    return false;
  }

  public int hashCode() {
    return 37*(37*(this.uident_.hashCode())+this.listuident_.hashCode())+this.type_.hashCode();
  }


}
