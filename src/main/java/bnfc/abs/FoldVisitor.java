package bnfc.abs;

import bnfc.abs.Absyn.*;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/** BNFC-Generated Fold Visitor */
public abstract class FoldVisitor<R,A> implements AllVisitor<R,A> {
    public abstract R leaf(A arg);
    public abstract R combine(R x, R y, A arg);

/* AnyIdent */
    public R visit(bnfc.abs.Absyn.AnyIden p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.AnyTyIden p, A arg) {
      R r = leaf(arg);
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
    public R visit(bnfc.abs.Absyn.Modul p, A arg) {
      R r = leaf(arg);
      r = combine(p.qtype_.accept(this, arg), r, arg);
      for (Export x : p.listexport_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      for (Import x : p.listimport_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      for (Decl x : p.listdecl_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.maybeblock_.accept(this, arg), r, arg);
      return r;
    }

/* Export */
    public R visit(bnfc.abs.Absyn.AnyExport p, A arg) {
      R r = leaf(arg);
      for (AnyIdent x : p.listanyident_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.AnyFromExport p, A arg) {
      R r = leaf(arg);
      for (AnyIdent x : p.listanyident_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.qtype_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.StarExport p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.StarFromExport p, A arg) {
      R r = leaf(arg);
      r = combine(p.qtype_.accept(this, arg), r, arg);
      return r;
    }

/* Import */
    public R visit(bnfc.abs.Absyn.AnyImport p, A arg) {
      R r = leaf(arg);
      r = combine(p.importtype_.accept(this, arg), r, arg);
      r = combine(p.ttype_.accept(this, arg), r, arg);
      r = combine(p.anyident_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.AnyFromImport p, A arg) {
      R r = leaf(arg);
      r = combine(p.importtype_.accept(this, arg), r, arg);
      for (AnyIdent x : p.listanyident_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.qtype_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.StarFromImport p, A arg) {
      R r = leaf(arg);
      r = combine(p.importtype_.accept(this, arg), r, arg);
      r = combine(p.qtype_.accept(this, arg), r, arg);
      return r;
    }

/* ImportType */
    public R visit(bnfc.abs.Absyn.ForeignImport p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.NormalImport p, A arg) {
      R r = leaf(arg);
      return r;
    }

/* Type */
    public R visit(bnfc.abs.Absyn.TUnderscore p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.TSimple p, A arg) {
      R r = leaf(arg);
      r = combine(p.qtype_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.TGen p, A arg) {
      R r = leaf(arg);
      r = combine(p.qtype_.accept(this, arg), r, arg);
      for (Type x : p.listtype_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }

/* QType */
    public R visit(bnfc.abs.Absyn.QTyp p, A arg) {
      R r = leaf(arg);
      for (QTypeSegment x : p.listqtypesegment_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }

/* QTypeSegment */
    public R visit(bnfc.abs.Absyn.QTypeSegmen p, A arg) {
      R r = leaf(arg);
      return r;
    }

/* TType */
    public R visit(bnfc.abs.Absyn.TTyp p, A arg) {
      R r = leaf(arg);
      for (TTypeSegment x : p.listttypesegment_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }

/* TTypeSegment */
    public R visit(bnfc.abs.Absyn.TTypeSegmen p, A arg) {
      R r = leaf(arg);
      return r;
    }

/* Decl */
    public R visit(bnfc.abs.Absyn.TypeDecl p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.TypeParDecl p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.ExceptionDecl p, A arg) {
      R r = leaf(arg);
      r = combine(p.constrident_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.DataDecl p, A arg) {
      R r = leaf(arg);
      for (ConstrIdent x : p.listconstrident_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.DataParDecl p, A arg) {
      R r = leaf(arg);
      for (ConstrIdent x : p.listconstrident_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.FunDecl p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      for (Param x : p.listparam_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.funbody_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.FunParDecl p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      for (Param x : p.listparam_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.funbody_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.InterfDecl p, A arg) {
      R r = leaf(arg);
      for (MethSignat x : p.listmethsignat_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.ExtendsDecl p, A arg) {
      R r = leaf(arg);
      for (QType x : p.listqtype_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      for (MethSignat x : p.listmethsignat_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.ClassDecl p, A arg) {
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
    public R visit(bnfc.abs.Absyn.ClassParamDecl p, A arg) {
      R r = leaf(arg);
      for (Param x : p.listparam_)
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
    public R visit(bnfc.abs.Absyn.ClassImplements p, A arg) {
      R r = leaf(arg);
      for (QType x : p.listqtype_)
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
    public R visit(bnfc.abs.Absyn.ClassParamImplements p, A arg) {
      R r = leaf(arg);
      for (Param x : p.listparam_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      for (QType x : p.listqtype_)
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
      r = combine(p.type_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.RecordConstrType p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
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

/* MethSignat */
    public R visit(bnfc.abs.Absyn.MethSig p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      for (Param x : p.listparam_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }

/* ClassBody */
    public R visit(bnfc.abs.Absyn.FieldClassBody p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.FieldAssignClassBody p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.MethClassBody p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      for (Param x : p.listparam_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.block_.accept(this, arg), r, arg);
      return r;
    }

/* Block */
    public R visit(bnfc.abs.Absyn.Bloc p, A arg) {
      R r = leaf(arg);
      for (Stm x : p.liststm_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }

/* MaybeBlock */
    public R visit(bnfc.abs.Absyn.JustBlock p, A arg) {
      R r = leaf(arg);
      r = combine(p.block_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.NoBlock p, A arg) {
      R r = leaf(arg);
      return r;
    }

/* Param */
    public R visit(bnfc.abs.Absyn.Par p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      return r;
    }

/* Stm */
    public R visit(bnfc.abs.Absyn.SExp p, A arg) {
      R r = leaf(arg);
      r = combine(p.exp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SBlock p, A arg) {
      R r = leaf(arg);
      for (Stm x : p.liststm_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.SWhile p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      r = combine(p.stm_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SReturn p, A arg) {
      R r = leaf(arg);
      r = combine(p.exp_.accept(this, arg), r, arg);
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
      r = combine(p.type_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SDecAss p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      r = combine(p.exp_.accept(this, arg), r, arg);
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
    public R visit(bnfc.abs.Absyn.SSuspend p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SSkip p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SAssert p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SAwait p, A arg) {
      R r = leaf(arg);
      r = combine(p.guard_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SThrow p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.STryCatchFinally p, A arg) {
      R r = leaf(arg);
      r = combine(p.stm_.accept(this, arg), r, arg);
      for (CatchBranch x : p.listcatchbranch_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.maybefinally_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.SPrint p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      return r;
    }

/* CatchBranch */
    public R visit(bnfc.abs.Absyn.CatchBranc p, A arg) {
      R r = leaf(arg);
      r = combine(p.pattern_.accept(this, arg), r, arg);
      r = combine(p.stm_.accept(this, arg), r, arg);
      return r;
    }

/* MaybeFinally */
    public R visit(bnfc.abs.Absyn.JustFinally p, A arg) {
      R r = leaf(arg);
      r = combine(p.stm_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.NoFinally p, A arg) {
      R r = leaf(arg);
      return r;
    }

/* Guard */
    public R visit(bnfc.abs.Absyn.VarGuard p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.FieldGuard p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.ExpGuard p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.AndGuard p, A arg) {
      R r = leaf(arg);
      r = combine(p.guard_1.accept(this, arg), r, arg);
      r = combine(p.guard_2.accept(this, arg), r, arg);
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
    public R visit(bnfc.abs.Absyn.Let p, A arg) {
      R r = leaf(arg);
      r = combine(p.param_.accept(this, arg), r, arg);
      r = combine(p.pureexp_1.accept(this, arg), r, arg);
      r = combine(p.pureexp_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.If p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_1.accept(this, arg), r, arg);
      r = combine(p.pureexp_2.accept(this, arg), r, arg);
      r = combine(p.pureexp_3.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.Case p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      for (CaseBranch x : p.listcasebranch_)
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
      for (PureExp x : p.listpureexp_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.EQualFunCall p, A arg) {
      R r = leaf(arg);
      r = combine(p.ttype_.accept(this, arg), r, arg);
      for (PureExp x : p.listpureexp_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.ENaryFunCall p, A arg) {
      R r = leaf(arg);
      for (PureExp x : p.listpureexp_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.ENaryQualFunCall p, A arg) {
      R r = leaf(arg);
      r = combine(p.ttype_.accept(this, arg), r, arg);
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
    public R visit(bnfc.abs.Absyn.EThis p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.EQualVar p, A arg) {
      R r = leaf(arg);
      r = combine(p.ttype_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.ESinglConstr p, A arg) {
      R r = leaf(arg);
      r = combine(p.qtype_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.EParamConstr p, A arg) {
      R r = leaf(arg);
      r = combine(p.qtype_.accept(this, arg), r, arg);
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

/* CaseBranch */
    public R visit(bnfc.abs.Absyn.CaseBranc p, A arg) {
      R r = leaf(arg);
      r = combine(p.pattern_.accept(this, arg), r, arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      return r;
    }

/* Pattern */
    public R visit(bnfc.abs.Absyn.PIdent p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.PLit p, A arg) {
      R r = leaf(arg);
      r = combine(p.literal_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.PSinglConstr p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.PParamConstr p, A arg) {
      R r = leaf(arg);
      for (Pattern x : p.listpattern_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.PUnderscore p, A arg) {
      R r = leaf(arg);
      return r;
    }

/* Literal */
    public R visit(bnfc.abs.Absyn.LNull p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.LThis p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(bnfc.abs.Absyn.LThisDC p, A arg) {
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

/* EffExp */
    public R visit(bnfc.abs.Absyn.New p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      for (PureExp x : p.listpureexp_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(bnfc.abs.Absyn.NewLocal p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
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
    public R visit(bnfc.abs.Absyn.ThisAsyncMethCall p, A arg) {
      R r = leaf(arg);
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
    public R visit(bnfc.abs.Absyn.Spawns p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      for (PureExp x : p.listpureexp_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }

/* Ann */
    public R visit(bnfc.abs.Absyn.SimpleAnn p, A arg) {
      R r = leaf(arg);
      r = combine(p.pureexp_.accept(this, arg), r, arg);
      return r;
    }

/* AnnDecl */
    public R visit(bnfc.abs.Absyn.AnnDec p, A arg) {
      R r = leaf(arg);
      for (Ann x : p.listann_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.decl_.accept(this, arg), r, arg);
      return r;
    }

/* AnnType */
    public R visit(bnfc.abs.Absyn.AnnTyp p, A arg) {
      R r = leaf(arg);
      for (Ann x : p.listann_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.type_.accept(this, arg), r, arg);
      return r;
    }


}
