package bnfc.abs.Absyn; // Java Package generated by the BNF Converter.

public abstract class Decl implements java.io.Serializable {
  public abstract <R,A> R accept(Decl.Visitor<R,A> v, A arg);
  public interface Visitor <R,A> {
    public R visit(bnfc.abs.Absyn.TypeDecl p, A arg);
    public R visit(bnfc.abs.Absyn.TypeParDecl p, A arg);
    public R visit(bnfc.abs.Absyn.ExceptionDecl p, A arg);
    public R visit(bnfc.abs.Absyn.DataDecl p, A arg);
    public R visit(bnfc.abs.Absyn.DataParDecl p, A arg);
    public R visit(bnfc.abs.Absyn.FunDecl p, A arg);
    public R visit(bnfc.abs.Absyn.FunParDecl p, A arg);
    public R visit(bnfc.abs.Absyn.InterfDecl p, A arg);
    public R visit(bnfc.abs.Absyn.ExtendsDecl p, A arg);
    public R visit(bnfc.abs.Absyn.ClassDecl p, A arg);
    public R visit(bnfc.abs.Absyn.ClassParamDecl p, A arg);
    public R visit(bnfc.abs.Absyn.ClassImplements p, A arg);
    public R visit(bnfc.abs.Absyn.ClassParamImplements p, A arg);

  }

}
