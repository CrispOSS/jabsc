package bnfc.abs;
import bnfc.abs.Absyn.*;
/** BNFC-Generated Composition Visitor
*/

public class ComposVisitor<A> implements
  bnfc.abs.Absyn.AnyIdent.Visitor<bnfc.abs.Absyn.AnyIdent,A>,
  bnfc.abs.Absyn.Program.Visitor<bnfc.abs.Absyn.Program,A>,
  bnfc.abs.Absyn.Module.Visitor<bnfc.abs.Absyn.Module,A>,
  bnfc.abs.Absyn.Export.Visitor<bnfc.abs.Absyn.Export,A>,
  bnfc.abs.Absyn.Import.Visitor<bnfc.abs.Absyn.Import,A>,
  bnfc.abs.Absyn.ImportType.Visitor<bnfc.abs.Absyn.ImportType,A>,
  bnfc.abs.Absyn.Type.Visitor<bnfc.abs.Absyn.Type,A>,
  bnfc.abs.Absyn.QType.Visitor<bnfc.abs.Absyn.QType,A>,
  bnfc.abs.Absyn.QTypeSegment.Visitor<bnfc.abs.Absyn.QTypeSegment,A>,
  bnfc.abs.Absyn.TType.Visitor<bnfc.abs.Absyn.TType,A>,
  bnfc.abs.Absyn.TTypeSegment.Visitor<bnfc.abs.Absyn.TTypeSegment,A>,
  bnfc.abs.Absyn.Decl.Visitor<bnfc.abs.Absyn.Decl,A>,
  bnfc.abs.Absyn.ConstrIdent.Visitor<bnfc.abs.Absyn.ConstrIdent,A>,
  bnfc.abs.Absyn.ConstrType.Visitor<bnfc.abs.Absyn.ConstrType,A>,
  bnfc.abs.Absyn.FunBody.Visitor<bnfc.abs.Absyn.FunBody,A>,
  bnfc.abs.Absyn.MethSignat.Visitor<bnfc.abs.Absyn.MethSignat,A>,
  bnfc.abs.Absyn.ClassBody.Visitor<bnfc.abs.Absyn.ClassBody,A>,
  bnfc.abs.Absyn.Block.Visitor<bnfc.abs.Absyn.Block,A>,
  bnfc.abs.Absyn.MaybeBlock.Visitor<bnfc.abs.Absyn.MaybeBlock,A>,
  bnfc.abs.Absyn.Param.Visitor<bnfc.abs.Absyn.Param,A>,
  bnfc.abs.Absyn.Stm.Visitor<bnfc.abs.Absyn.Stm,A>,
  bnfc.abs.Absyn.CatchBranch.Visitor<bnfc.abs.Absyn.CatchBranch,A>,
  bnfc.abs.Absyn.MaybeFinally.Visitor<bnfc.abs.Absyn.MaybeFinally,A>,
  bnfc.abs.Absyn.Guard.Visitor<bnfc.abs.Absyn.Guard,A>,
  bnfc.abs.Absyn.Exp.Visitor<bnfc.abs.Absyn.Exp,A>,
  bnfc.abs.Absyn.PureExp.Visitor<bnfc.abs.Absyn.PureExp,A>,
  bnfc.abs.Absyn.CaseBranch.Visitor<bnfc.abs.Absyn.CaseBranch,A>,
  bnfc.abs.Absyn.Pattern.Visitor<bnfc.abs.Absyn.Pattern,A>,
  bnfc.abs.Absyn.Literal.Visitor<bnfc.abs.Absyn.Literal,A>,
  bnfc.abs.Absyn.EffExp.Visitor<bnfc.abs.Absyn.EffExp,A>,
  bnfc.abs.Absyn.Ann.Visitor<bnfc.abs.Absyn.Ann,A>,
  bnfc.abs.Absyn.AnnDecl.Visitor<bnfc.abs.Absyn.AnnDecl,A>,
  bnfc.abs.Absyn.AnnType.Visitor<bnfc.abs.Absyn.AnnType,A>
{
/* AnyIdent */
    public AnyIdent visit(bnfc.abs.Absyn.AnyIden p, A arg)
    {
      String lident_ = p.lident_;
      return new bnfc.abs.Absyn.AnyIden(lident_);
    }    public AnyIdent visit(bnfc.abs.Absyn.AnyTyIden p, A arg)
    {
      String uident_ = p.uident_;
      return new bnfc.abs.Absyn.AnyTyIden(uident_);
    }
/* Program */
    public Program visit(bnfc.abs.Absyn.Prog p, A arg)
    {
      ListModule listmodule_ = new ListModule();
      for (Module x : p.listmodule_)
      {
        listmodule_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.Prog(listmodule_);
    }
/* Module */
    public Module visit(bnfc.abs.Absyn.Modul p, A arg)
    {
      QType qtype_ = p.qtype_.accept(this, arg);
      ListExport listexport_ = new ListExport();
      for (Export x : p.listexport_)
      {
        listexport_.add(x.accept(this,arg));
      }
      ListImport listimport_ = new ListImport();
      for (Import x : p.listimport_)
      {
        listimport_.add(x.accept(this,arg));
      }
      ListDecl listdecl_ = new ListDecl();
      for (Decl x : p.listdecl_)
      {
        listdecl_.add(x.accept(this,arg));
      }
      MaybeBlock maybeblock_ = p.maybeblock_.accept(this, arg);
      return new bnfc.abs.Absyn.Modul(qtype_, listexport_, listimport_, listdecl_, maybeblock_);
    }
/* Export */
    public Export visit(bnfc.abs.Absyn.AnyExport p, A arg)
    {
      ListAnyIdent listanyident_ = new ListAnyIdent();
      for (AnyIdent x : p.listanyident_)
      {
        listanyident_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.AnyExport(listanyident_);
    }    public Export visit(bnfc.abs.Absyn.AnyFromExport p, A arg)
    {
      ListAnyIdent listanyident_ = new ListAnyIdent();
      for (AnyIdent x : p.listanyident_)
      {
        listanyident_.add(x.accept(this,arg));
      }
      QType qtype_ = p.qtype_.accept(this, arg);
      return new bnfc.abs.Absyn.AnyFromExport(listanyident_, qtype_);
    }    public Export visit(bnfc.abs.Absyn.StarExport p, A arg)
    {
      return new bnfc.abs.Absyn.StarExport();
    }    public Export visit(bnfc.abs.Absyn.StarFromExport p, A arg)
    {
      QType qtype_ = p.qtype_.accept(this, arg);
      return new bnfc.abs.Absyn.StarFromExport(qtype_);
    }
/* Import */
    public Import visit(bnfc.abs.Absyn.AnyImport p, A arg)
    {
      ImportType importtype_ = p.importtype_.accept(this, arg);
      TType ttype_ = p.ttype_.accept(this, arg);
      AnyIdent anyident_ = p.anyident_.accept(this, arg);
      return new bnfc.abs.Absyn.AnyImport(importtype_, ttype_, anyident_);
    }    public Import visit(bnfc.abs.Absyn.AnyFromImport p, A arg)
    {
      ImportType importtype_ = p.importtype_.accept(this, arg);
      ListAnyIdent listanyident_ = new ListAnyIdent();
      for (AnyIdent x : p.listanyident_)
      {
        listanyident_.add(x.accept(this,arg));
      }
      QType qtype_ = p.qtype_.accept(this, arg);
      return new bnfc.abs.Absyn.AnyFromImport(importtype_, listanyident_, qtype_);
    }    public Import visit(bnfc.abs.Absyn.StarFromImport p, A arg)
    {
      ImportType importtype_ = p.importtype_.accept(this, arg);
      QType qtype_ = p.qtype_.accept(this, arg);
      return new bnfc.abs.Absyn.StarFromImport(importtype_, qtype_);
    }
/* ImportType */
    public ImportType visit(bnfc.abs.Absyn.ForeignImport p, A arg)
    {
      return new bnfc.abs.Absyn.ForeignImport();
    }    public ImportType visit(bnfc.abs.Absyn.NormalImport p, A arg)
    {
      return new bnfc.abs.Absyn.NormalImport();
    }
/* Type */
    public Type visit(bnfc.abs.Absyn.TUnderscore p, A arg)
    {
      return new bnfc.abs.Absyn.TUnderscore();
    }    public Type visit(bnfc.abs.Absyn.TSimple p, A arg)
    {
      QType qtype_ = p.qtype_.accept(this, arg);
      return new bnfc.abs.Absyn.TSimple(qtype_);
    }    public Type visit(bnfc.abs.Absyn.TGen p, A arg)
    {
      QType qtype_ = p.qtype_.accept(this, arg);
      ListType listtype_ = new ListType();
      for (Type x : p.listtype_)
      {
        listtype_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.TGen(qtype_, listtype_);
    }
/* QType */
    public QType visit(bnfc.abs.Absyn.QTyp p, A arg)
    {
      ListQTypeSegment listqtypesegment_ = new ListQTypeSegment();
      for (QTypeSegment x : p.listqtypesegment_)
      {
        listqtypesegment_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.QTyp(listqtypesegment_);
    }
/* QTypeSegment */
    public QTypeSegment visit(bnfc.abs.Absyn.QTypeSegmen p, A arg)
    {
      String uident_ = p.uident_;
      return new bnfc.abs.Absyn.QTypeSegmen(uident_);
    }
/* TType */
    public TType visit(bnfc.abs.Absyn.TTyp p, A arg)
    {
      ListTTypeSegment listttypesegment_ = new ListTTypeSegment();
      for (TTypeSegment x : p.listttypesegment_)
      {
        listttypesegment_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.TTyp(listttypesegment_);
    }
/* TTypeSegment */
    public TTypeSegment visit(bnfc.abs.Absyn.TTypeSegmen p, A arg)
    {
      String uident_ = p.uident_;
      return new bnfc.abs.Absyn.TTypeSegmen(uident_);
    }
/* Decl */
    public Decl visit(bnfc.abs.Absyn.TypeDecl p, A arg)
    {
      String uident_ = p.uident_;
      Type type_ = p.type_.accept(this, arg);
      return new bnfc.abs.Absyn.TypeDecl(uident_, type_);
    }    public Decl visit(bnfc.abs.Absyn.TypeParDecl p, A arg)
    {
      String uident_ = p.uident_;
      ListUIdent listuident_ = p.listuident_;
      Type type_ = p.type_.accept(this, arg);
      return new bnfc.abs.Absyn.TypeParDecl(uident_, listuident_, type_);
    }    public Decl visit(bnfc.abs.Absyn.ExceptionDecl p, A arg)
    {
      ConstrIdent constrident_ = p.constrident_.accept(this, arg);
      return new bnfc.abs.Absyn.ExceptionDecl(constrident_);
    }    public Decl visit(bnfc.abs.Absyn.DataDecl p, A arg)
    {
      String uident_ = p.uident_;
      ListConstrIdent listconstrident_ = new ListConstrIdent();
      for (ConstrIdent x : p.listconstrident_)
      {
        listconstrident_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.DataDecl(uident_, listconstrident_);
    }    public Decl visit(bnfc.abs.Absyn.DataParDecl p, A arg)
    {
      String uident_ = p.uident_;
      ListUIdent listuident_ = p.listuident_;
      ListConstrIdent listconstrident_ = new ListConstrIdent();
      for (ConstrIdent x : p.listconstrident_)
      {
        listconstrident_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.DataParDecl(uident_, listuident_, listconstrident_);
    }    public Decl visit(bnfc.abs.Absyn.FunDecl p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      String lident_ = p.lident_;
      ListParam listparam_ = new ListParam();
      for (Param x : p.listparam_)
      {
        listparam_.add(x.accept(this,arg));
      }
      FunBody funbody_ = p.funbody_.accept(this, arg);
      return new bnfc.abs.Absyn.FunDecl(type_, lident_, listparam_, funbody_);
    }    public Decl visit(bnfc.abs.Absyn.FunParDecl p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      String lident_ = p.lident_;
      ListUIdent listuident_ = p.listuident_;
      ListParam listparam_ = new ListParam();
      for (Param x : p.listparam_)
      {
        listparam_.add(x.accept(this,arg));
      }
      FunBody funbody_ = p.funbody_.accept(this, arg);
      return new bnfc.abs.Absyn.FunParDecl(type_, lident_, listuident_, listparam_, funbody_);
    }    public Decl visit(bnfc.abs.Absyn.InterfDecl p, A arg)
    {
      String uident_ = p.uident_;
      ListMethSignat listmethsignat_ = new ListMethSignat();
      for (MethSignat x : p.listmethsignat_)
      {
        listmethsignat_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.InterfDecl(uident_, listmethsignat_);
    }    public Decl visit(bnfc.abs.Absyn.ExtendsDecl p, A arg)
    {
      String uident_ = p.uident_;
      ListQType listqtype_ = new ListQType();
      for (QType x : p.listqtype_)
      {
        listqtype_.add(x.accept(this,arg));
      }
      ListMethSignat listmethsignat_ = new ListMethSignat();
      for (MethSignat x : p.listmethsignat_)
      {
        listmethsignat_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.ExtendsDecl(uident_, listqtype_, listmethsignat_);
    }    public Decl visit(bnfc.abs.Absyn.ClassDecl p, A arg)
    {
      String uident_ = p.uident_;
      ListClassBody listclassbody_1 = new ListClassBody();
      for (ClassBody x : p.listclassbody_1)
      {
        listclassbody_1.add(x.accept(this,arg));
      }
      MaybeBlock maybeblock_ = p.maybeblock_.accept(this, arg);
      ListClassBody listclassbody_2 = new ListClassBody();
      for (ClassBody x : p.listclassbody_2)
      {
        listclassbody_2.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.ClassDecl(uident_, listclassbody_1, maybeblock_, listclassbody_2);
    }    public Decl visit(bnfc.abs.Absyn.ClassParamDecl p, A arg)
    {
      String uident_ = p.uident_;
      ListParam listparam_ = new ListParam();
      for (Param x : p.listparam_)
      {
        listparam_.add(x.accept(this,arg));
      }
      ListClassBody listclassbody_1 = new ListClassBody();
      for (ClassBody x : p.listclassbody_1)
      {
        listclassbody_1.add(x.accept(this,arg));
      }
      MaybeBlock maybeblock_ = p.maybeblock_.accept(this, arg);
      ListClassBody listclassbody_2 = new ListClassBody();
      for (ClassBody x : p.listclassbody_2)
      {
        listclassbody_2.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.ClassParamDecl(uident_, listparam_, listclassbody_1, maybeblock_, listclassbody_2);
    }    public Decl visit(bnfc.abs.Absyn.ClassImplements p, A arg)
    {
      String uident_ = p.uident_;
      ListQType listqtype_ = new ListQType();
      for (QType x : p.listqtype_)
      {
        listqtype_.add(x.accept(this,arg));
      }
      ListClassBody listclassbody_1 = new ListClassBody();
      for (ClassBody x : p.listclassbody_1)
      {
        listclassbody_1.add(x.accept(this,arg));
      }
      MaybeBlock maybeblock_ = p.maybeblock_.accept(this, arg);
      ListClassBody listclassbody_2 = new ListClassBody();
      for (ClassBody x : p.listclassbody_2)
      {
        listclassbody_2.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.ClassImplements(uident_, listqtype_, listclassbody_1, maybeblock_, listclassbody_2);
    }    public Decl visit(bnfc.abs.Absyn.ClassParamImplements p, A arg)
    {
      String uident_ = p.uident_;
      ListParam listparam_ = new ListParam();
      for (Param x : p.listparam_)
      {
        listparam_.add(x.accept(this,arg));
      }
      ListQType listqtype_ = new ListQType();
      for (QType x : p.listqtype_)
      {
        listqtype_.add(x.accept(this,arg));
      }
      ListClassBody listclassbody_1 = new ListClassBody();
      for (ClassBody x : p.listclassbody_1)
      {
        listclassbody_1.add(x.accept(this,arg));
      }
      MaybeBlock maybeblock_ = p.maybeblock_.accept(this, arg);
      ListClassBody listclassbody_2 = new ListClassBody();
      for (ClassBody x : p.listclassbody_2)
      {
        listclassbody_2.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.ClassParamImplements(uident_, listparam_, listqtype_, listclassbody_1, maybeblock_, listclassbody_2);
    }
/* ConstrIdent */
    public ConstrIdent visit(bnfc.abs.Absyn.SinglConstrIdent p, A arg)
    {
      String uident_ = p.uident_;
      return new bnfc.abs.Absyn.SinglConstrIdent(uident_);
    }    public ConstrIdent visit(bnfc.abs.Absyn.ParamConstrIdent p, A arg)
    {
      String uident_ = p.uident_;
      ListConstrType listconstrtype_ = new ListConstrType();
      for (ConstrType x : p.listconstrtype_)
      {
        listconstrtype_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.ParamConstrIdent(uident_, listconstrtype_);
    }
/* ConstrType */
    public ConstrType visit(bnfc.abs.Absyn.EmptyConstrType p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      return new bnfc.abs.Absyn.EmptyConstrType(type_);
    }    public ConstrType visit(bnfc.abs.Absyn.RecordConstrType p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      String lident_ = p.lident_;
      return new bnfc.abs.Absyn.RecordConstrType(type_, lident_);
    }
/* FunBody */
    public FunBody visit(bnfc.abs.Absyn.BuiltinFunBody p, A arg)
    {
      return new bnfc.abs.Absyn.BuiltinFunBody();
    }    public FunBody visit(bnfc.abs.Absyn.NormalFunBody p, A arg)
    {
      PureExp pureexp_ = p.pureexp_.accept(this, arg);
      return new bnfc.abs.Absyn.NormalFunBody(pureexp_);
    }
/* MethSignat */
    public MethSignat visit(bnfc.abs.Absyn.MethSig p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      String lident_ = p.lident_;
      ListParam listparam_ = new ListParam();
      for (Param x : p.listparam_)
      {
        listparam_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.MethSig(type_, lident_, listparam_);
    }
/* ClassBody */
    public ClassBody visit(bnfc.abs.Absyn.FieldClassBody p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      String lident_ = p.lident_;
      return new bnfc.abs.Absyn.FieldClassBody(type_, lident_);
    }    public ClassBody visit(bnfc.abs.Absyn.FieldAssignClassBody p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      String lident_ = p.lident_;
      PureExp pureexp_ = p.pureexp_.accept(this, arg);
      return new bnfc.abs.Absyn.FieldAssignClassBody(type_, lident_, pureexp_);
    }    public ClassBody visit(bnfc.abs.Absyn.MethClassBody p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      String lident_ = p.lident_;
      ListParam listparam_ = new ListParam();
      for (Param x : p.listparam_)
      {
        listparam_.add(x.accept(this,arg));
      }
      Block block_ = p.block_.accept(this, arg);
      return new bnfc.abs.Absyn.MethClassBody(type_, lident_, listparam_, block_);
    }
/* Block */
    public Block visit(bnfc.abs.Absyn.Bloc p, A arg)
    {
      ListStm liststm_ = new ListStm();
      for (Stm x : p.liststm_)
      {
        liststm_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.Bloc(liststm_);
    }
/* MaybeBlock */
    public MaybeBlock visit(bnfc.abs.Absyn.JustBlock p, A arg)
    {
      Block block_ = p.block_.accept(this, arg);
      return new bnfc.abs.Absyn.JustBlock(block_);
    }    public MaybeBlock visit(bnfc.abs.Absyn.NoBlock p, A arg)
    {
      return new bnfc.abs.Absyn.NoBlock();
    }
/* Param */
    public Param visit(bnfc.abs.Absyn.Par p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      String lident_ = p.lident_;
      return new bnfc.abs.Absyn.Par(type_, lident_);
    }
/* Stm */
    public Stm visit(bnfc.abs.Absyn.SExp p, A arg)
    {
      Exp exp_ = p.exp_.accept(this, arg);
      return new bnfc.abs.Absyn.SExp(exp_);
    }    public Stm visit(bnfc.abs.Absyn.SBlock p, A arg)
    {
      ListStm liststm_ = new ListStm();
      for (Stm x : p.liststm_)
      {
        liststm_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.SBlock(liststm_);
    }    public Stm visit(bnfc.abs.Absyn.SWhile p, A arg)
    {
      PureExp pureexp_ = p.pureexp_.accept(this, arg);
      Stm stm_ = p.stm_.accept(this, arg);
      return new bnfc.abs.Absyn.SWhile(pureexp_, stm_);
    }    public Stm visit(bnfc.abs.Absyn.SReturn p, A arg)
    {
      Exp exp_ = p.exp_.accept(this, arg);
      return new bnfc.abs.Absyn.SReturn(exp_);
    }    public Stm visit(bnfc.abs.Absyn.SAss p, A arg)
    {
      String lident_ = p.lident_;
      Exp exp_ = p.exp_.accept(this, arg);
      return new bnfc.abs.Absyn.SAss(lident_, exp_);
    }    public Stm visit(bnfc.abs.Absyn.SFieldAss p, A arg)
    {
      String lident_ = p.lident_;
      Exp exp_ = p.exp_.accept(this, arg);
      return new bnfc.abs.Absyn.SFieldAss(lident_, exp_);
    }    public Stm visit(bnfc.abs.Absyn.SDec p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      String lident_ = p.lident_;
      return new bnfc.abs.Absyn.SDec(type_, lident_);
    }    public Stm visit(bnfc.abs.Absyn.SDecAss p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      String lident_ = p.lident_;
      Exp exp_ = p.exp_.accept(this, arg);
      return new bnfc.abs.Absyn.SDecAss(type_, lident_, exp_);
    }    public Stm visit(bnfc.abs.Absyn.SIf p, A arg)
    {
      PureExp pureexp_ = p.pureexp_.accept(this, arg);
      Stm stm_ = p.stm_.accept(this, arg);
      return new bnfc.abs.Absyn.SIf(pureexp_, stm_);
    }    public Stm visit(bnfc.abs.Absyn.SIfElse p, A arg)
    {
      PureExp pureexp_ = p.pureexp_.accept(this, arg);
      Stm stm_1 = p.stm_1.accept(this, arg);
      Stm stm_2 = p.stm_2.accept(this, arg);
      return new bnfc.abs.Absyn.SIfElse(pureexp_, stm_1, stm_2);
    }    public Stm visit(bnfc.abs.Absyn.SSuspend p, A arg)
    {
      return new bnfc.abs.Absyn.SSuspend();
    }    public Stm visit(bnfc.abs.Absyn.SSkip p, A arg)
    {
      return new bnfc.abs.Absyn.SSkip();
    }    public Stm visit(bnfc.abs.Absyn.SAssert p, A arg)
    {
      PureExp pureexp_ = p.pureexp_.accept(this, arg);
      return new bnfc.abs.Absyn.SAssert(pureexp_);
    }    public Stm visit(bnfc.abs.Absyn.SAwait p, A arg)
    {
      Guard guard_ = p.guard_.accept(this, arg);
      return new bnfc.abs.Absyn.SAwait(guard_);
    }    public Stm visit(bnfc.abs.Absyn.SThrow p, A arg)
    {
      PureExp pureexp_ = p.pureexp_.accept(this, arg);
      return new bnfc.abs.Absyn.SThrow(pureexp_);
    }    public Stm visit(bnfc.abs.Absyn.STryCatchFinally p, A arg)
    {
      Stm stm_ = p.stm_.accept(this, arg);
      ListCatchBranch listcatchbranch_ = new ListCatchBranch();
      for (CatchBranch x : p.listcatchbranch_)
      {
        listcatchbranch_.add(x.accept(this,arg));
      }
      MaybeFinally maybefinally_ = p.maybefinally_.accept(this, arg);
      return new bnfc.abs.Absyn.STryCatchFinally(stm_, listcatchbranch_, maybefinally_);
    }    public Stm visit(bnfc.abs.Absyn.SPrint p, A arg)
    {
      PureExp pureexp_ = p.pureexp_.accept(this, arg);
      return new bnfc.abs.Absyn.SPrint(pureexp_);
    }
/* CatchBranch */
    public CatchBranch visit(bnfc.abs.Absyn.CatchBranc p, A arg)
    {
      Pattern pattern_ = p.pattern_.accept(this, arg);
      Stm stm_ = p.stm_.accept(this, arg);
      return new bnfc.abs.Absyn.CatchBranc(pattern_, stm_);
    }
/* MaybeFinally */
    public MaybeFinally visit(bnfc.abs.Absyn.JustFinally p, A arg)
    {
      Stm stm_ = p.stm_.accept(this, arg);
      return new bnfc.abs.Absyn.JustFinally(stm_);
    }    public MaybeFinally visit(bnfc.abs.Absyn.NoFinally p, A arg)
    {
      return new bnfc.abs.Absyn.NoFinally();
    }
/* Guard */
    public Guard visit(bnfc.abs.Absyn.VarGuard p, A arg)
    {
      String lident_ = p.lident_;
      return new bnfc.abs.Absyn.VarGuard(lident_);
    }    public Guard visit(bnfc.abs.Absyn.FieldGuard p, A arg)
    {
      String lident_ = p.lident_;
      return new bnfc.abs.Absyn.FieldGuard(lident_);
    }    public Guard visit(bnfc.abs.Absyn.ExpGuard p, A arg)
    {
      PureExp pureexp_ = p.pureexp_.accept(this, arg);
      return new bnfc.abs.Absyn.ExpGuard(pureexp_);
    }    public Guard visit(bnfc.abs.Absyn.AndGuard p, A arg)
    {
      Guard guard_1 = p.guard_1.accept(this, arg);
      Guard guard_2 = p.guard_2.accept(this, arg);
      return new bnfc.abs.Absyn.AndGuard(guard_1, guard_2);
    }
/* Exp */
    public Exp visit(bnfc.abs.Absyn.ExpP p, A arg)
    {
      PureExp pureexp_ = p.pureexp_.accept(this, arg);
      return new bnfc.abs.Absyn.ExpP(pureexp_);
    }    public Exp visit(bnfc.abs.Absyn.ExpE p, A arg)
    {
      EffExp effexp_ = p.effexp_.accept(this, arg);
      return new bnfc.abs.Absyn.ExpE(effexp_);
    }
/* PureExp */
    public PureExp visit(bnfc.abs.Absyn.EOr p, A arg)
    {
      PureExp pureexp_1 = p.pureexp_1.accept(this, arg);
      PureExp pureexp_2 = p.pureexp_2.accept(this, arg);
      return new bnfc.abs.Absyn.EOr(pureexp_1, pureexp_2);
    }    public PureExp visit(bnfc.abs.Absyn.Let p, A arg)
    {
      Param param_ = p.param_.accept(this, arg);
      PureExp pureexp_1 = p.pureexp_1.accept(this, arg);
      PureExp pureexp_2 = p.pureexp_2.accept(this, arg);
      return new bnfc.abs.Absyn.Let(param_, pureexp_1, pureexp_2);
    }    public PureExp visit(bnfc.abs.Absyn.If p, A arg)
    {
      PureExp pureexp_1 = p.pureexp_1.accept(this, arg);
      PureExp pureexp_2 = p.pureexp_2.accept(this, arg);
      PureExp pureexp_3 = p.pureexp_3.accept(this, arg);
      return new bnfc.abs.Absyn.If(pureexp_1, pureexp_2, pureexp_3);
    }    public PureExp visit(bnfc.abs.Absyn.Case p, A arg)
    {
      PureExp pureexp_ = p.pureexp_.accept(this, arg);
      ListCaseBranch listcasebranch_ = new ListCaseBranch();
      for (CaseBranch x : p.listcasebranch_)
      {
        listcasebranch_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.Case(pureexp_, listcasebranch_);
    }    public PureExp visit(bnfc.abs.Absyn.EAnd p, A arg)
    {
      PureExp pureexp_1 = p.pureexp_1.accept(this, arg);
      PureExp pureexp_2 = p.pureexp_2.accept(this, arg);
      return new bnfc.abs.Absyn.EAnd(pureexp_1, pureexp_2);
    }    public PureExp visit(bnfc.abs.Absyn.EEq p, A arg)
    {
      PureExp pureexp_1 = p.pureexp_1.accept(this, arg);
      PureExp pureexp_2 = p.pureexp_2.accept(this, arg);
      return new bnfc.abs.Absyn.EEq(pureexp_1, pureexp_2);
    }    public PureExp visit(bnfc.abs.Absyn.ENeq p, A arg)
    {
      PureExp pureexp_1 = p.pureexp_1.accept(this, arg);
      PureExp pureexp_2 = p.pureexp_2.accept(this, arg);
      return new bnfc.abs.Absyn.ENeq(pureexp_1, pureexp_2);
    }    public PureExp visit(bnfc.abs.Absyn.ELt p, A arg)
    {
      PureExp pureexp_1 = p.pureexp_1.accept(this, arg);
      PureExp pureexp_2 = p.pureexp_2.accept(this, arg);
      return new bnfc.abs.Absyn.ELt(pureexp_1, pureexp_2);
    }    public PureExp visit(bnfc.abs.Absyn.ELe p, A arg)
    {
      PureExp pureexp_1 = p.pureexp_1.accept(this, arg);
      PureExp pureexp_2 = p.pureexp_2.accept(this, arg);
      return new bnfc.abs.Absyn.ELe(pureexp_1, pureexp_2);
    }    public PureExp visit(bnfc.abs.Absyn.EGt p, A arg)
    {
      PureExp pureexp_1 = p.pureexp_1.accept(this, arg);
      PureExp pureexp_2 = p.pureexp_2.accept(this, arg);
      return new bnfc.abs.Absyn.EGt(pureexp_1, pureexp_2);
    }    public PureExp visit(bnfc.abs.Absyn.EGe p, A arg)
    {
      PureExp pureexp_1 = p.pureexp_1.accept(this, arg);
      PureExp pureexp_2 = p.pureexp_2.accept(this, arg);
      return new bnfc.abs.Absyn.EGe(pureexp_1, pureexp_2);
    }    public PureExp visit(bnfc.abs.Absyn.EAdd p, A arg)
    {
      PureExp pureexp_1 = p.pureexp_1.accept(this, arg);
      PureExp pureexp_2 = p.pureexp_2.accept(this, arg);
      return new bnfc.abs.Absyn.EAdd(pureexp_1, pureexp_2);
    }    public PureExp visit(bnfc.abs.Absyn.ESub p, A arg)
    {
      PureExp pureexp_1 = p.pureexp_1.accept(this, arg);
      PureExp pureexp_2 = p.pureexp_2.accept(this, arg);
      return new bnfc.abs.Absyn.ESub(pureexp_1, pureexp_2);
    }    public PureExp visit(bnfc.abs.Absyn.EMul p, A arg)
    {
      PureExp pureexp_1 = p.pureexp_1.accept(this, arg);
      PureExp pureexp_2 = p.pureexp_2.accept(this, arg);
      return new bnfc.abs.Absyn.EMul(pureexp_1, pureexp_2);
    }    public PureExp visit(bnfc.abs.Absyn.EDiv p, A arg)
    {
      PureExp pureexp_1 = p.pureexp_1.accept(this, arg);
      PureExp pureexp_2 = p.pureexp_2.accept(this, arg);
      return new bnfc.abs.Absyn.EDiv(pureexp_1, pureexp_2);
    }    public PureExp visit(bnfc.abs.Absyn.EMod p, A arg)
    {
      PureExp pureexp_1 = p.pureexp_1.accept(this, arg);
      PureExp pureexp_2 = p.pureexp_2.accept(this, arg);
      return new bnfc.abs.Absyn.EMod(pureexp_1, pureexp_2);
    }    public PureExp visit(bnfc.abs.Absyn.ELogNeg p, A arg)
    {
      PureExp pureexp_ = p.pureexp_.accept(this, arg);
      return new bnfc.abs.Absyn.ELogNeg(pureexp_);
    }    public PureExp visit(bnfc.abs.Absyn.EIntNeg p, A arg)
    {
      PureExp pureexp_ = p.pureexp_.accept(this, arg);
      return new bnfc.abs.Absyn.EIntNeg(pureexp_);
    }    public PureExp visit(bnfc.abs.Absyn.EFunCall p, A arg)
    {
      String lident_ = p.lident_;
      ListPureExp listpureexp_ = new ListPureExp();
      for (PureExp x : p.listpureexp_)
      {
        listpureexp_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.EFunCall(lident_, listpureexp_);
    }    public PureExp visit(bnfc.abs.Absyn.EQualFunCall p, A arg)
    {
      TType ttype_ = p.ttype_.accept(this, arg);
      String lident_ = p.lident_;
      ListPureExp listpureexp_ = new ListPureExp();
      for (PureExp x : p.listpureexp_)
      {
        listpureexp_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.EQualFunCall(ttype_, lident_, listpureexp_);
    }    public PureExp visit(bnfc.abs.Absyn.ENaryFunCall p, A arg)
    {
      String lident_ = p.lident_;
      ListPureExp listpureexp_ = new ListPureExp();
      for (PureExp x : p.listpureexp_)
      {
        listpureexp_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.ENaryFunCall(lident_, listpureexp_);
    }    public PureExp visit(bnfc.abs.Absyn.ENaryQualFunCall p, A arg)
    {
      TType ttype_ = p.ttype_.accept(this, arg);
      String lident_ = p.lident_;
      ListPureExp listpureexp_ = new ListPureExp();
      for (PureExp x : p.listpureexp_)
      {
        listpureexp_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.ENaryQualFunCall(ttype_, lident_, listpureexp_);
    }    public PureExp visit(bnfc.abs.Absyn.EVar p, A arg)
    {
      String lident_ = p.lident_;
      return new bnfc.abs.Absyn.EVar(lident_);
    }    public PureExp visit(bnfc.abs.Absyn.EThis p, A arg)
    {
      String lident_ = p.lident_;
      return new bnfc.abs.Absyn.EThis(lident_);
    }    public PureExp visit(bnfc.abs.Absyn.EQualVar p, A arg)
    {
      TType ttype_ = p.ttype_.accept(this, arg);
      String lident_ = p.lident_;
      return new bnfc.abs.Absyn.EQualVar(ttype_, lident_);
    }    public PureExp visit(bnfc.abs.Absyn.ESinglConstr p, A arg)
    {
      QType qtype_ = p.qtype_.accept(this, arg);
      return new bnfc.abs.Absyn.ESinglConstr(qtype_);
    }    public PureExp visit(bnfc.abs.Absyn.EParamConstr p, A arg)
    {
      QType qtype_ = p.qtype_.accept(this, arg);
      ListPureExp listpureexp_ = new ListPureExp();
      for (PureExp x : p.listpureexp_)
      {
        listpureexp_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.EParamConstr(qtype_, listpureexp_);
    }    public PureExp visit(bnfc.abs.Absyn.ELit p, A arg)
    {
      Literal literal_ = p.literal_.accept(this, arg);
      return new bnfc.abs.Absyn.ELit(literal_);
    }
/* CaseBranch */
    public CaseBranch visit(bnfc.abs.Absyn.CaseBranc p, A arg)
    {
      Pattern pattern_ = p.pattern_.accept(this, arg);
      PureExp pureexp_ = p.pureexp_.accept(this, arg);
      return new bnfc.abs.Absyn.CaseBranc(pattern_, pureexp_);
    }
/* Pattern */
    public Pattern visit(bnfc.abs.Absyn.PIdent p, A arg)
    {
      String lident_ = p.lident_;
      return new bnfc.abs.Absyn.PIdent(lident_);
    }    public Pattern visit(bnfc.abs.Absyn.PLit p, A arg)
    {
      Literal literal_ = p.literal_.accept(this, arg);
      return new bnfc.abs.Absyn.PLit(literal_);
    }    public Pattern visit(bnfc.abs.Absyn.PSinglConstr p, A arg)
    {
      String uident_ = p.uident_;
      return new bnfc.abs.Absyn.PSinglConstr(uident_);
    }    public Pattern visit(bnfc.abs.Absyn.PParamConstr p, A arg)
    {
      String uident_ = p.uident_;
      ListPattern listpattern_ = new ListPattern();
      for (Pattern x : p.listpattern_)
      {
        listpattern_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.PParamConstr(uident_, listpattern_);
    }    public Pattern visit(bnfc.abs.Absyn.PUnderscore p, A arg)
    {
      return new bnfc.abs.Absyn.PUnderscore();
    }
/* Literal */
    public Literal visit(bnfc.abs.Absyn.LNull p, A arg)
    {
      return new bnfc.abs.Absyn.LNull();
    }    public Literal visit(bnfc.abs.Absyn.LThis p, A arg)
    {
      return new bnfc.abs.Absyn.LThis();
    }    public Literal visit(bnfc.abs.Absyn.LThisDC p, A arg)
    {
      return new bnfc.abs.Absyn.LThisDC();
    }    public Literal visit(bnfc.abs.Absyn.LStr p, A arg)
    {
      String string_ = p.string_;
      return new bnfc.abs.Absyn.LStr(string_);
    }    public Literal visit(bnfc.abs.Absyn.LInt p, A arg)
    {
      Integer integer_ = p.integer_;
      return new bnfc.abs.Absyn.LInt(integer_);
    }
/* EffExp */
    public EffExp visit(bnfc.abs.Absyn.New p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      ListPureExp listpureexp_ = new ListPureExp();
      for (PureExp x : p.listpureexp_)
      {
        listpureexp_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.New(type_, listpureexp_);
    }    public EffExp visit(bnfc.abs.Absyn.NewLocal p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      ListPureExp listpureexp_ = new ListPureExp();
      for (PureExp x : p.listpureexp_)
      {
        listpureexp_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.NewLocal(type_, listpureexp_);
    }    public EffExp visit(bnfc.abs.Absyn.SyncMethCall p, A arg)
    {
      PureExp pureexp_ = p.pureexp_.accept(this, arg);
      String lident_ = p.lident_;
      ListPureExp listpureexp_ = new ListPureExp();
      for (PureExp x : p.listpureexp_)
      {
        listpureexp_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.SyncMethCall(pureexp_, lident_, listpureexp_);
    }    public EffExp visit(bnfc.abs.Absyn.ThisSyncMethCall p, A arg)
    {
      String lident_ = p.lident_;
      ListPureExp listpureexp_ = new ListPureExp();
      for (PureExp x : p.listpureexp_)
      {
        listpureexp_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.ThisSyncMethCall(lident_, listpureexp_);
    }    public EffExp visit(bnfc.abs.Absyn.AsyncMethCall p, A arg)
    {
      PureExp pureexp_ = p.pureexp_.accept(this, arg);
      String lident_ = p.lident_;
      ListPureExp listpureexp_ = new ListPureExp();
      for (PureExp x : p.listpureexp_)
      {
        listpureexp_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.AsyncMethCall(pureexp_, lident_, listpureexp_);
    }    public EffExp visit(bnfc.abs.Absyn.ThisAsyncMethCall p, A arg)
    {
      String lident_ = p.lident_;
      ListPureExp listpureexp_ = new ListPureExp();
      for (PureExp x : p.listpureexp_)
      {
        listpureexp_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.ThisAsyncMethCall(lident_, listpureexp_);
    }    public EffExp visit(bnfc.abs.Absyn.Get p, A arg)
    {
      PureExp pureexp_ = p.pureexp_.accept(this, arg);
      return new bnfc.abs.Absyn.Get(pureexp_);
    }    public EffExp visit(bnfc.abs.Absyn.Spawns p, A arg)
    {
      PureExp pureexp_ = p.pureexp_.accept(this, arg);
      Type type_ = p.type_.accept(this, arg);
      ListPureExp listpureexp_ = new ListPureExp();
      for (PureExp x : p.listpureexp_)
      {
        listpureexp_.add(x.accept(this,arg));
      }
      return new bnfc.abs.Absyn.Spawns(pureexp_, type_, listpureexp_);
    }
/* Ann */
    public Ann visit(bnfc.abs.Absyn.SimpleAnn p, A arg)
    {
      PureExp pureexp_ = p.pureexp_.accept(this, arg);
      return new bnfc.abs.Absyn.SimpleAnn(pureexp_);
    }
/* AnnDecl */
    public AnnDecl visit(bnfc.abs.Absyn.AnnDec p, A arg)
    {
      ListAnn listann_ = new ListAnn();
      for (Ann x : p.listann_)
      {
        listann_.add(x.accept(this,arg));
      }
      Decl decl_ = p.decl_.accept(this, arg);
      return new bnfc.abs.Absyn.AnnDec(listann_, decl_);
    }
/* AnnType */
    public AnnType visit(bnfc.abs.Absyn.AnnTyp p, A arg)
    {
      ListAnn listann_ = new ListAnn();
      for (Ann x : p.listann_)
      {
        listann_.add(x.accept(this,arg));
      }
      Type type_ = p.type_.accept(this, arg);
      return new bnfc.abs.Absyn.AnnTyp(listann_, type_);
    }
}