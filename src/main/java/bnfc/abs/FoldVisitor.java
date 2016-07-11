package bnfc.abs;

import bnfc.abs.Absyn.*;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/** BNFC-Generated Fold Visitor */
public abstract class FoldVisitor<R,A> implements AllVisitor<R,A> {
    public abstract R leaf(A arg);
    public abstract R combine(R x, R y, A arg);

/* Literal */
    public R visit(bnfc.abs.Absyn.LNull p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.LThis p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.LStr p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.LInt p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.LFloat p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.LThisDC p, A arg) {
      R r = leaf(arg);
      return r;
    }

/* QU */
    public R visit(bnfc.abs.Absyn.U_ p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.QU_ p, A arg) {
      R r = leaf(arg);
      r = combine(p.qu_.accept(this, arg), r, arg);
      return r;
    }

/* QL */
    public R visit(bnfc.abs.Absyn.L_ p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.QL_ p, A arg) {
      R r = leaf(arg);
      r = combine(p.ql_.accept(this, arg), r, arg);
      return r;
    }

/* QA */
    public R visit(bnfc.abs.Absyn.LA p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.UA p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.QA_ p, A arg) {
      R r = leaf(arg);
      r = combine(p.qa_.accept(this, arg), r, arg);
      return r;
    }

/* T */
    public R visit(bnfc.abs.Absyn.TSimple p, A arg) {
      R r = leaf(arg);
      r = combine(p.qu_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.TPoly p, A arg) {
      R r = leaf(arg);
      r = combine(p.qu_.accept(this, arg), r, arg);
      for (T x : p.listt_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.TInfer p, A arg) {
      R r = leaf(arg);
      return r;
    }

/* FormalPar */
    public R visit(bnfc.abs.Absyn.FormalParameter p, A arg) {
      R r = leaf(arg);
      r = combine(p.t_.accept(this, arg), r, arg);
      return r;
    }

/* Program */
    public R visit(bnfc.abs.Absyn.Prog p, A arg) {
      R r = leaf(arg);
      for (Module x : p.listmodule_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }

/* Module */
    public R visit(bnfc.abs.Absyn.Mod p, A arg) {
      R r = leaf(arg);
      r = combine(p.qu_.accept(this, arg), r, arg);
      for (Export x : p.listexport_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      for (Import x : p.listimport_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      for (AnnDecl x : p.listanndecl_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.maybeblock_.accept(this, arg), r, arg);
      return r;
    }

/* Export */
    public R visit(bnfc.abs.Absyn.StarExport p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.StarFromExport p, A arg) {
      R r = leaf(arg);
      r = combine(p.qu_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.AnyExport p, A arg) {
      R r = leaf(arg);
      for (QA x : p.listqa_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.AnyFromExport p, A arg) {
      R r = leaf(arg);
      for (QA x : p.listqa_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.qu_.accept(this, arg), r, arg);
      return r;
    }

/* Import */
    public R visit(bnfc.abs.Absyn.StarFromImport p, A arg) {
      R r = leaf(arg);
      r = combine(p.isforeign_.accept(this, arg), r, arg);
      r = combine(p.qu_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.AnyImport p, A arg) {
      R r = leaf(arg);
      r = combine(p.isforeign_.accept(this, arg), r, arg);
      for (QA x : p.listqa_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.AnyFromImport p, A arg) {
      R r = leaf(arg);
      r = combine(p.isforeign_.accept(this, arg), r, arg);
      for (QA x : p.listqa_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.qu_.accept(this, arg), r, arg);
      return r;
    }

/* IsForeign */
    public R visit(bnfc.abs.Absyn.NoForeign p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.YesForeign p, A arg) {
      R r = leaf(arg);
      return r;
    }

/* Decl */
    public R visit(bnfc.abs.Absyn.DType p, A arg) {
      R r = leaf(arg);
      r = combine(p.t_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.DTypePoly p, A arg) {
      R r = leaf(arg);
      r = combine(p.t_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.DData p, A arg) {
      R r = leaf(arg);
      for (ConstrIdent x : p.listconstrident_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.DDataPoly p, A arg) {
      R r = leaf(arg);
      for (ConstrIdent x : p.listconstrident_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.DFun p, A arg) {
      R r = leaf(arg);
      r = combine(p.t_.accept(this, arg), r, arg);
      for (FormalPar x : p.listformalpar_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.funbody_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.DFunPoly p, A arg) {
      R r = leaf(arg);
      r = combine(p.t_.accept(this, arg), r, arg);
      for (FormalPar x : p.listformalpar_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.funbody_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.DInterf p, A arg) {
      R r = leaf(arg);
      for (MethSig x : p.listmethsig_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.DExtends p, A arg) {
      R r = leaf(arg);
      for (QU x : p.listqu_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      for (MethSig x : p.listmethsig_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.DClass p, A arg) {
      R r = leaf(arg);
      for (ClassBody x : p.listclassbody_1)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.maybeblock_.accept(this, arg), r, arg);
      for (ClassBody x : p.listclassbody_2)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.DClassPar p, A arg) {
      R r = leaf(arg);
      for (FormalPar x : p.listformalpar_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      for (ClassBody x : p.listclassbody_1)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.maybeblock_.accept(this, arg), r, arg);
      for (ClassBody x : p.listclassbody_2)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.DClassImplements p, A arg) {
      R r = leaf(arg);
      for (QU x : p.listqu_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      for (ClassBody x : p.listclassbody_1)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.maybeblock_.accept(this, arg), r, arg);
      for (ClassBody x : p.listclassbody_2)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.DClassParImplements p, A arg) {
      R r = leaf(arg);
      for (FormalPar x : p.listformalpar_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      for (QU x : p.listqu_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      for (ClassBody x : p.listclassbody_1)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.maybeblock_.accept(this, arg), r, arg);
      for (ClassBody x : p.listclassbody_2)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.DException p, A arg) {
      R r = leaf(arg);
      r = combine(p.constrident_.accept(this, arg), r, arg);
      return r;
    }

/* ConstrIdent */
    public R visit(bnfc.abs.Absyn.SinglConstrIdent p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.ParamConstrIdent p, A arg) {
      R r = leaf(arg);
      for (ConstrType x : p.listconstrtype_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }

/* ConstrType */
    public R visit(bnfc.abs.Absyn.EmptyConstrType p, A arg) {
      R r = leaf(arg);
      r = combine(p.t_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.RecordConstrType p, A arg) {
      R r = leaf(arg);
      r = combine(p.t_.accept(this, arg), r, arg);
      return r;
    }

/* FunBody */
    public R visit(bnfc.abs.Absyn.BuiltinFunBody p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.NormalFunBody p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      return r;
    }

/* MethSig */
    public R visit(bnfc.abs.Absyn.MethSignature p, A arg) {
      R r = leaf(arg);
      for (Ann x : p.listann_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.t_.accept(this, arg), r, arg);
      for (FormalPar x : p.listformalpar_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }

/* ClassBody */
    public R visit(bnfc.abs.Absyn.FieldClassBody p, A arg) {
      R r = leaf(arg);
      r = combine(p.t_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.FieldAssignClassBody p, A arg) {
      R r = leaf(arg);
      r = combine(p.t_.accept(this, arg), r, arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.MethClassBody p, A arg) {
      R r = leaf(arg);
      r = combine(p.t_.accept(this, arg), r, arg);
      for (FormalPar x : p.listformalpar_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      for (AnnStm x : p.listannstm_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }

/* Stm */
    public R visit(bnfc.abs.Absyn.SSkip p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SSuspend p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SReturn p, A arg) {
      R r = leaf(arg);
      r = combine(p.exp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SAssert p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SAwait p, A arg) {
      R r = leaf(arg);
      r = combine(p.awaitguard_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SAss p, A arg) {
      R r = leaf(arg);
      r = combine(p.exp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SFieldAss p, A arg) {
      R r = leaf(arg);
      r = combine(p.exp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SDec p, A arg) {
      R r = leaf(arg);
      r = combine(p.t_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SDecAss p, A arg) {
      R r = leaf(arg);
      r = combine(p.t_.accept(this, arg), r, arg);
      r = combine(p.exp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SWhile p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      r = combine(p.annstm_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SIf p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      r = combine(p.stm_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SIfElse p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      r = combine(p.stm_1.accept(this, arg), r, arg);
      r = combine(p.stm_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SCase p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      for (SCaseBranch x : p.listscasebranch_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.SBlock p, A arg) {
      R r = leaf(arg);
      for (AnnStm x : p.listannstm_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.SExp p, A arg) {
      R r = leaf(arg);
      r = combine(p.exp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SPrint p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SPrintln p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SThrow p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.STryCatchFinally p, A arg) {
      R r = leaf(arg);
      r = combine(p.annstm_.accept(this, arg), r, arg);
      for (SCaseBranch x : p.listscasebranch_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.maybefinally_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SGive p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_1.accept(this, arg), r, arg);
      r = combine(p.pureexp_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SDuration p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_1.accept(this, arg), r, arg);
      r = combine(p.pureexp_2.accept(this, arg), r, arg);
      return r;
    }

/* SCaseBranch */
    public R visit(bnfc.abs.Absyn.SCaseB p, A arg) {
      R r = leaf(arg);
      r = combine(p.pattern_.accept(this, arg), r, arg);
      r = combine(p.annstm_.accept(this, arg), r, arg);
      return r;
    }

/* AwaitGuard */
    public R visit(bnfc.abs.Absyn.GFut p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.GFutField p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.GExp p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.GAnd p, A arg) {
      R r = leaf(arg);
      r = combine(p.awaitguard_1.accept(this, arg), r, arg);
      r = combine(p.awaitguard_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.GDuration p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_1.accept(this, arg), r, arg);
      r = combine(p.pureexp_2.accept(this, arg), r, arg);
      return r;
    }

/* Exp */
    public R visit(bnfc.abs.Absyn.ExpP p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.ExpE p, A arg) {
      R r = leaf(arg);
      r = combine(p.effexp_.accept(this, arg), r, arg);
      return r;
    }

/* PureExp */
    public R visit(bnfc.abs.Absyn.EOr p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_1.accept(this, arg), r, arg);
      r = combine(p.pureexp_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.ELet p, A arg) {
      R r = leaf(arg);
      r = combine(p.formalpar_.accept(this, arg), r, arg);
      r = combine(p.pureexp_1.accept(this, arg), r, arg);
      r = combine(p.pureexp_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.EIf p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_1.accept(this, arg), r, arg);
      r = combine(p.pureexp_2.accept(this, arg), r, arg);
      r = combine(p.pureexp_3.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.ECase p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      for (ECaseBranch x : p.listecasebranch_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.EAnd p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_1.accept(this, arg), r, arg);
      r = combine(p.pureexp_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.EEq p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_1.accept(this, arg), r, arg);
      r = combine(p.pureexp_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.ENeq p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_1.accept(this, arg), r, arg);
      r = combine(p.pureexp_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.ELt p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_1.accept(this, arg), r, arg);
      r = combine(p.pureexp_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.ELe p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_1.accept(this, arg), r, arg);
      r = combine(p.pureexp_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.EGt p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_1.accept(this, arg), r, arg);
      r = combine(p.pureexp_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.EGe p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_1.accept(this, arg), r, arg);
      r = combine(p.pureexp_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.EAdd p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_1.accept(this, arg), r, arg);
      r = combine(p.pureexp_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.ESub p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_1.accept(this, arg), r, arg);
      r = combine(p.pureexp_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.EMul p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_1.accept(this, arg), r, arg);
      r = combine(p.pureexp_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.EDiv p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_1.accept(this, arg), r, arg);
      r = combine(p.pureexp_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.EMod p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_1.accept(this, arg), r, arg);
      r = combine(p.pureexp_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.ELogNeg p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.EIntNeg p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.EFunCall p, A arg) {
      R r = leaf(arg);
      r = combine(p.ql_.accept(this, arg), r, arg);
      for (PureExp x : p.listpureexp_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.ENaryFunCall p, A arg) {
      R r = leaf(arg);
      r = combine(p.ql_.accept(this, arg), r, arg);
      for (PureExp x : p.listpureexp_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.EVar p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.EField p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.ESinglConstr p, A arg) {
      R r = leaf(arg);
      r = combine(p.qu_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.EParamConstr p, A arg) {
      R r = leaf(arg);
      r = combine(p.qu_.accept(this, arg), r, arg);
      for (PureExp x : p.listpureexp_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.ELit p, A arg) {
      R r = leaf(arg);
      r = combine(p.literal_.accept(this, arg), r, arg);
      return r;
    }

/* ECaseBranch */
    public R visit(bnfc.abs.Absyn.ECaseB p, A arg) {
      R r = leaf(arg);
      r = combine(p.pattern_.accept(this, arg), r, arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      return r;
    }

/* Pattern */
    public R visit(bnfc.abs.Absyn.PLit p, A arg) {
      R r = leaf(arg);
      r = combine(p.literal_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.PVar p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.PSinglConstr p, A arg) {
      R r = leaf(arg);
      r = combine(p.qu_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.PParamConstr p, A arg) {
      R r = leaf(arg);
      r = combine(p.qu_.accept(this, arg), r, arg);
      for (Pattern x : p.listpattern_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.PWildCard p, A arg) {
      R r = leaf(arg);
      return r;
    }

/* EffExp */
    public R visit(bnfc.abs.Absyn.New p, A arg) {
      R r = leaf(arg);
      r = combine(p.qu_.accept(this, arg), r, arg);
      for (PureExp x : p.listpureexp_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.NewLocal p, A arg) {
      R r = leaf(arg);
      r = combine(p.qu_.accept(this, arg), r, arg);
      for (PureExp x : p.listpureexp_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.SyncMethCall p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      for (PureExp x : p.listpureexp_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.ThisSyncMethCall p, A arg) {
      R r = leaf(arg);
      for (PureExp x : p.listpureexp_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.AsyncMethCall p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      for (PureExp x : p.listpureexp_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.AwaitMethCall p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      for (PureExp x : p.listpureexp_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.Get p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.Readln p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.ProNew p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.ProTry p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.Now p, A arg) {
      R r = leaf(arg);
      return r;
    }

/* Ann */
    public R visit(bnfc.abs.Absyn.Annotation p, A arg) {
      R r = leaf(arg);
      r = combine(p.ann__.accept(this, arg), r, arg);
      return r;
    }

/* Ann_ */
    public R visit(bnfc.abs.Absyn.AnnNoType p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.AnnWithType p, A arg) {
      R r = leaf(arg);
      r = combine(p.t_.accept(this, arg), r, arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      return r;
    }

/* AnnStm */
    public R visit(bnfc.abs.Absyn.AnnStatement p, A arg) {
      R r = leaf(arg);
      for (Ann x : p.listann_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.stm_.accept(this, arg), r, arg);
      return r;
    }

/* AnnDecl */
    public R visit(bnfc.abs.Absyn.AnnDeclaration p, A arg) {
      R r = leaf(arg);
      for (Ann x : p.listann_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.decl_.accept(this, arg), r, arg);
      return r;
    }

/* MaybeFinally */
    public R visit(bnfc.abs.Absyn.JustFinally p, A arg) {
      R r = leaf(arg);
      r = combine(p.annstm_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.NoFinally p, A arg) {
      R r = leaf(arg);
      return r;
    }

/* MaybeBlock */
    public R visit(bnfc.abs.Absyn.JustBlock p, A arg) {
      R r = leaf(arg);
      for (AnnStm x : p.listannstm_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.NoBlock p, A arg) {
      R r = leaf(arg);
      return r;
    }


}
