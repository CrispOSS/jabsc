package bnfc.abs.Absyn; // Java Package generated by the BNF Converter.

public abstract class MaybeFinally implements java.io.Serializable {
  public abstract <R,A> R accept(MaybeFinally.Visitor<R,A> v, A arg);
  public interface Visitor <R,A> {
    public R visit(bnfc.abs.Absyn.JustFinally p, A arg);
    public R visit(bnfc.abs.Absyn.NoFinally p, A arg);

  }

}
