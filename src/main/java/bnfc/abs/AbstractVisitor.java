package bnfc.abs;
import bnfc.abs.Absyn.*;
/** BNFC-Generated Abstract Visitor */
public class AbstractVisitor<R,A> implements AllVisitor<R,A> {
/* Literal */
    public R visit(bnfc.abs.Absyn.LNull p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.LThis p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.LStr p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.LInt p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.LFloat p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.LThisDC p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.Literal p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* QU */
    public R visit(bnfc.abs.Absyn.U_ p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.QU_ p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.QU p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* QL */
    public R visit(bnfc.abs.Absyn.L_ p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.QL_ p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.QL p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* QA */
    public R visit(bnfc.abs.Absyn.LA p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.UA p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.QA_ p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.QA p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* T */
    public R visit(bnfc.abs.Absyn.TSimple p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.TPoly p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.TInfer p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.T p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* FormalPar */
    public R visit(bnfc.abs.Absyn.FormalParameter p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.FormalPar p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Program */
    public R visit(bnfc.abs.Absyn.Prog p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.Program p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Module */
    public R visit(bnfc.abs.Absyn.Mod p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.Module p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Export */
    public R visit(bnfc.abs.Absyn.StarExport p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.StarFromExport p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.AnyExport p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.AnyFromExport p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.Export p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Import */
    public R visit(bnfc.abs.Absyn.StarFromImport p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.AnyImport p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.AnyFromImport p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.Import p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* IsForeign */
    public R visit(bnfc.abs.Absyn.NoForeign p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.YesForeign p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.IsForeign p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Decl */
    public R visit(bnfc.abs.Absyn.DType p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.DTypePoly p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.DData p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.DDataPoly p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.DFun p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.DFunPoly p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.DInterf p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.DExtends p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.DClass p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.DClassPar p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.DClassImplements p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.DClassParImplements p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.DException p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.Decl p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* ConstrIdent */
    public R visit(bnfc.abs.Absyn.SinglConstrIdent p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ParamConstrIdent p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.ConstrIdent p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* ConstrType */
    public R visit(bnfc.abs.Absyn.EmptyConstrType p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.RecordConstrType p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.ConstrType p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* FunBody */
    public R visit(bnfc.abs.Absyn.BuiltinFunBody p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.NormalFunBody p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.FunBody p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* MethSig */
    public R visit(bnfc.abs.Absyn.MethSignature p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.MethSig p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* ClassBody */
    public R visit(bnfc.abs.Absyn.FieldClassBody p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.FieldAssignClassBody p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.MethClassBody p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.ClassBody p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Stm */
    public R visit(bnfc.abs.Absyn.SSkip p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SSuspend p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SReturn p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SAssert p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SAwait p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SAss p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SFieldAss p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SDec p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SDecAss p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SWhile p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SIf p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SIfElse p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SCase p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SBlock p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SExp p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SPrint p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SPrintln p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SThrow p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.STryCatchFinally p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SGive p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SDuration p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.Stm p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* SCaseBranch */
    public R visit(bnfc.abs.Absyn.SCaseB p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.SCaseBranch p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* AwaitGuard */
    public R visit(bnfc.abs.Absyn.GFut p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.GFutField p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.GExp p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.GAnd p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.GDuration p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.AwaitGuard p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Exp */
    public R visit(bnfc.abs.Absyn.ExpP p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ExpE p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.Exp p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* PureExp */
    public R visit(bnfc.abs.Absyn.EOr p, A arg) { return visitDefault(p, arg); }

    public R visit(bnfc.abs.Absyn.ELet p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.EIf p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ECase p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.EAnd p, A arg) { return visitDefault(p, arg); }

    public R visit(bnfc.abs.Absyn.EEq p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ENeq p, A arg) { return visitDefault(p, arg); }

    public R visit(bnfc.abs.Absyn.ELt p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ELe p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.EGt p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.EGe p, A arg) { return visitDefault(p, arg); }

    public R visit(bnfc.abs.Absyn.EAdd p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ESub p, A arg) { return visitDefault(p, arg); }

    public R visit(bnfc.abs.Absyn.EMul p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.EDiv p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.EMod p, A arg) { return visitDefault(p, arg); }

    public R visit(bnfc.abs.Absyn.ELogNeg p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.EIntNeg p, A arg) { return visitDefault(p, arg); }

    public R visit(bnfc.abs.Absyn.EFunCall p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ENaryFunCall p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.EVar p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.EField p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ESinglConstr p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.EParamConstr p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ELit p, A arg) { return visitDefault(p, arg); }

    public R visitDefault(bnfc.abs.Absyn.PureExp p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* ECaseBranch */
    public R visit(bnfc.abs.Absyn.ECaseB p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.ECaseBranch p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Pattern */
    public R visit(bnfc.abs.Absyn.PLit p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.PVar p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.PSinglConstr p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.PParamConstr p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.PWildCard p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.Pattern p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* EffExp */
    public R visit(bnfc.abs.Absyn.New p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.NewLocal p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SyncMethCall p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ThisSyncMethCall p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.AsyncMethCall p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.AwaitMethCall p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.Get p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.Readln p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ProNew p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ProTry p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.Now p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.EffExp p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Ann */
    public R visit(bnfc.abs.Absyn.Annotation p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.Ann p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Ann_ */
    public R visit(bnfc.abs.Absyn.AnnNoType p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.AnnWithType p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.Ann_ p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* AnnStm */
    public R visit(bnfc.abs.Absyn.AnnStatement p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.AnnStm p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* AnnDecl */
    public R visit(bnfc.abs.Absyn.AnnDeclaration p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.AnnDecl p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* MaybeFinally */
    public R visit(bnfc.abs.Absyn.JustFinally p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.NoFinally p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.MaybeFinally p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* MaybeBlock */
    public R visit(bnfc.abs.Absyn.JustBlock p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.NoBlock p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.MaybeBlock p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }

}
