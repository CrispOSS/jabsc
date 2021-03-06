package bnfc.abs.Absyn; // Java Package generated by the BNF Converter.

public abstract class Exp implements java.io.Serializable {
  public abstract <R,A> R accept(Exp.Visitor<R,A> v, A arg);
  public interface Visitor <R,A> {
    public R visit(bnfc.abs.Absyn.ExpP p, A arg);
    public R visit(bnfc.abs.Absyn.ExpE p, A arg);

  }

}
