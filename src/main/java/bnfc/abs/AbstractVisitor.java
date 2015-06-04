package bnfc.abs;
import bnfc.abs.Absyn.*;
/** BNFC-Generated Abstract Visitor */
public class AbstractVisitor<R,A> implements AllVisitor<R,A> {
/* AnyIdent */
    public R visit(bnfc.abs.Absyn.AnyIden p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.AnyTyIden p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.AnyIdent p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Program */
    public R visit(bnfc.abs.Absyn.Prog p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.Program p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Module */
    public R visit(bnfc.abs.Absyn.Modul p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.Module p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Export */
    public R visit(bnfc.abs.Absyn.AnyExport p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.AnyFromExport p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.StarExport p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.StarFromExport p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.Export p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Import */
    public R visit(bnfc.abs.Absyn.AnyImport p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.AnyFromImport p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.StarFromImport p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.Import p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* ImportType */
    public R visit(bnfc.abs.Absyn.ForeignImport p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.NormalImport p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.ImportType p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Type */
    public R visit(bnfc.abs.Absyn.TUnderscore p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.TSimple p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.TGen p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.Type p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* QType */
    public R visit(bnfc.abs.Absyn.QTyp p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.QType p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* QTypeSegment */
    public R visit(bnfc.abs.Absyn.QTypeSegmen p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.QTypeSegment p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* TType */
    public R visit(bnfc.abs.Absyn.TTyp p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.TType p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* TTypeSegment */
    public R visit(bnfc.abs.Absyn.TTypeSegmen p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.TTypeSegment p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Decl */
    public R visit(bnfc.abs.Absyn.TypeDecl p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.TypeParDecl p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ExceptionDecl p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.DataDecl p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.DataParDecl p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.FunDecl p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.FunParDecl p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.InterfDecl p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ExtendsDecl p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ClassDecl p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ClassParamDecl p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ClassImplements p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ClassParamImplements p, A arg) { return visitDefault(p, arg); }
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
/* MethSignat */
    public R visit(bnfc.abs.Absyn.MethSig p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.MethSignat p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* ClassBody */
    public R visit(bnfc.abs.Absyn.FieldClassBody p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.FieldAssignClassBody p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.MethClassBody p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.ClassBody p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Block */
    public R visit(bnfc.abs.Absyn.Bloc p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.Block p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* MaybeBlock */
    public R visit(bnfc.abs.Absyn.JustBlock p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.NoBlock p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.MaybeBlock p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Param */
    public R visit(bnfc.abs.Absyn.Par p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.Param p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Stm */
    public R visit(bnfc.abs.Absyn.SExp p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SBlock p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SWhile p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SReturn p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SAss p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SFieldAss p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SDec p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SDecAss p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SIf p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SIfElse p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SSuspend p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SSkip p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SAssert p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SAwait p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SThrow p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.STryCatchFinally p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SPrint p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.Stm p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* CatchBranch */
    public R visit(bnfc.abs.Absyn.CatchBranc p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.CatchBranch p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* MaybeFinally */
    public R visit(bnfc.abs.Absyn.JustFinally p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.NoFinally p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.MaybeFinally p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Guard */
    public R visit(bnfc.abs.Absyn.VarGuard p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.FieldGuard p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ExpGuard p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.AndGuard p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.Guard p, A arg) {
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

    public R visit(bnfc.abs.Absyn.Let p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.If p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.Case p, A arg) { return visitDefault(p, arg); }
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
    public R visit(bnfc.abs.Absyn.EQualFunCall p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ENaryFunCall p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ENaryQualFunCall p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.EVar p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.EThis p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.EQualVar p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ESinglConstr p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.EParamConstr p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ELit p, A arg) { return visitDefault(p, arg); }

    public R visitDefault(bnfc.abs.Absyn.PureExp p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* CaseBranch */
    public R visit(bnfc.abs.Absyn.CaseBranc p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.CaseBranch p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Pattern */
    public R visit(bnfc.abs.Absyn.PIdent p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.PLit p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.PSinglConstr p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.PParamConstr p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.PUnderscore p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.Pattern p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Literal */
    public R visit(bnfc.abs.Absyn.LNull p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.LThis p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.LThisDC p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.LStr p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.LInt p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.Literal p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* EffExp */
    public R visit(bnfc.abs.Absyn.New p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.NewLocal p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.SyncMethCall p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ThisSyncMethCall p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.AsyncMethCall p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.ThisAsyncMethCall p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.Get p, A arg) { return visitDefault(p, arg); }
    public R visit(bnfc.abs.Absyn.Spawns p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.EffExp p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Ann */
    public R visit(bnfc.abs.Absyn.SimpleAnn p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.Ann p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* AnnDecl */
    public R visit(bnfc.abs.Absyn.AnnDec p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.AnnDecl p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* AnnType */
    public R visit(bnfc.abs.Absyn.AnnTyp p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(bnfc.abs.Absyn.AnnType p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }

}
