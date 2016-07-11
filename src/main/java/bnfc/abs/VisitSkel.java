package bnfc.abs;
import bnfc.abs.Absyn.*;
/*** BNFC-Generated Visitor Design Pattern Skeleton. ***/
/* This implements the common visitor design pattern.
   Tests show it to be slightly less efficient than the
   instanceof method, but easier to use. 
   Replace the R and A parameters with the desired return
   and context types.*/

public class VisitSkel
{
  public class LiteralVisitor<R,A> implements Literal.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.LNull p, A arg)
    { /* Code For LNull Goes Here */
      return null;
    }    public R visit(bnfc.abs.Absyn.LThis p, A arg)
    { /* Code For LThis Goes Here */
      return null;
    }    public R visit(bnfc.abs.Absyn.LStr p, A arg)
    { /* Code For LStr Goes Here */
      //p.string_;
      return null;
    }    public R visit(bnfc.abs.Absyn.LInt p, A arg)
    { /* Code For LInt Goes Here */
      //p.integer_;
      return null;
    }    public R visit(bnfc.abs.Absyn.LFloat p, A arg)
    { /* Code For LFloat Goes Here */
      //p.double_;
      return null;
    }    public R visit(bnfc.abs.Absyn.LThisDC p, A arg)
    { /* Code For LThisDC Goes Here */
      return null;
    }
  }
  public class QUVisitor<R,A> implements QU.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.U_ p, A arg)
    { /* Code For U_ Goes Here */
      //p.u_;
      return null;
    }    public R visit(bnfc.abs.Absyn.QU_ p, A arg)
    { /* Code For QU_ Goes Here */
      //p.u_;
      p.qu_.accept(new QUVisitor<R,A>(), arg);
      return null;
    }
  }
  public class QLVisitor<R,A> implements QL.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.L_ p, A arg)
    { /* Code For L_ Goes Here */
      //p.l_;
      return null;
    }    public R visit(bnfc.abs.Absyn.QL_ p, A arg)
    { /* Code For QL_ Goes Here */
      //p.u_;
      p.ql_.accept(new QLVisitor<R,A>(), arg);
      return null;
    }
  }
  public class QAVisitor<R,A> implements QA.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.LA p, A arg)
    { /* Code For LA Goes Here */
      //p.l_;
      return null;
    }    public R visit(bnfc.abs.Absyn.UA p, A arg)
    { /* Code For UA Goes Here */
      //p.u_;
      return null;
    }    public R visit(bnfc.abs.Absyn.QA_ p, A arg)
    { /* Code For QA_ Goes Here */
      //p.u_;
      p.qa_.accept(new QAVisitor<R,A>(), arg);
      return null;
    }
  }
  public class TVisitor<R,A> implements T.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.TSimple p, A arg)
    { /* Code For TSimple Goes Here */
      p.qu_.accept(new QUVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.TPoly p, A arg)
    { /* Code For TPoly Goes Here */
      p.qu_.accept(new QUVisitor<R,A>(), arg);
      for (T x: p.listt_)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.TInfer p, A arg)
    { /* Code For TInfer Goes Here */
      return null;
    }
  }
  public class FormalParVisitor<R,A> implements FormalPar.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.FormalParameter p, A arg)
    { /* Code For FormalParameter Goes Here */
      p.t_.accept(new TVisitor<R,A>(), arg);
      //p.l_;
      return null;
    }
  }
  public class ProgramVisitor<R,A> implements Program.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.Prog p, A arg)
    { /* Code For Prog Goes Here */
      for (Module x: p.listmodule_)
      { /* ... */ }
      return null;
    }
  }
  public class ModuleVisitor<R,A> implements Module.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.Mod p, A arg)
    { /* Code For Mod Goes Here */
      p.qu_.accept(new QUVisitor<R,A>(), arg);
      for (Export x: p.listexport_)
      { /* ... */ }
      for (Import x: p.listimport_)
      { /* ... */ }
      for (AnnDecl x: p.listanndecl_)
      { /* ... */ }
      p.maybeblock_.accept(new MaybeBlockVisitor<R,A>(), arg);
      return null;
    }
  }
  public class ExportVisitor<R,A> implements Export.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.StarExport p, A arg)
    { /* Code For StarExport Goes Here */
      return null;
    }    public R visit(bnfc.abs.Absyn.StarFromExport p, A arg)
    { /* Code For StarFromExport Goes Here */
      p.qu_.accept(new QUVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.AnyExport p, A arg)
    { /* Code For AnyExport Goes Here */
      for (QA x: p.listqa_)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.AnyFromExport p, A arg)
    { /* Code For AnyFromExport Goes Here */
      for (QA x: p.listqa_)
      { /* ... */ }
      p.qu_.accept(new QUVisitor<R,A>(), arg);
      return null;
    }
  }
  public class ImportVisitor<R,A> implements Import.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.StarFromImport p, A arg)
    { /* Code For StarFromImport Goes Here */
      p.isforeign_.accept(new IsForeignVisitor<R,A>(), arg);
      p.qu_.accept(new QUVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.AnyImport p, A arg)
    { /* Code For AnyImport Goes Here */
      p.isforeign_.accept(new IsForeignVisitor<R,A>(), arg);
      for (QA x: p.listqa_)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.AnyFromImport p, A arg)
    { /* Code For AnyFromImport Goes Here */
      p.isforeign_.accept(new IsForeignVisitor<R,A>(), arg);
      for (QA x: p.listqa_)
      { /* ... */ }
      p.qu_.accept(new QUVisitor<R,A>(), arg);
      return null;
    }
  }
  public class IsForeignVisitor<R,A> implements IsForeign.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.NoForeign p, A arg)
    { /* Code For NoForeign Goes Here */
      return null;
    }    public R visit(bnfc.abs.Absyn.YesForeign p, A arg)
    { /* Code For YesForeign Goes Here */
      return null;
    }
  }
  public class DeclVisitor<R,A> implements Decl.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.DType p, A arg)
    { /* Code For DType Goes Here */
      //p.u_;
      p.t_.accept(new TVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.DTypePoly p, A arg)
    { /* Code For DTypePoly Goes Here */
      //p.u_;
      for (String x: p.listu_)
      { /* ... */ }
      p.t_.accept(new TVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.DData p, A arg)
    { /* Code For DData Goes Here */
      //p.u_;
      for (ConstrIdent x: p.listconstrident_)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.DDataPoly p, A arg)
    { /* Code For DDataPoly Goes Here */
      //p.u_;
      for (String x: p.listu_)
      { /* ... */ }
      for (ConstrIdent x: p.listconstrident_)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.DFun p, A arg)
    { /* Code For DFun Goes Here */
      p.t_.accept(new TVisitor<R,A>(), arg);
      //p.l_;
      for (FormalPar x: p.listformalpar_)
      { /* ... */ }
      p.funbody_.accept(new FunBodyVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.DFunPoly p, A arg)
    { /* Code For DFunPoly Goes Here */
      p.t_.accept(new TVisitor<R,A>(), arg);
      //p.l_;
      for (String x: p.listu_)
      { /* ... */ }
      for (FormalPar x: p.listformalpar_)
      { /* ... */ }
      p.funbody_.accept(new FunBodyVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.DInterf p, A arg)
    { /* Code For DInterf Goes Here */
      //p.u_;
      for (MethSig x: p.listmethsig_)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.DExtends p, A arg)
    { /* Code For DExtends Goes Here */
      //p.u_;
      for (QU x: p.listqu_)
      { /* ... */ }
      for (MethSig x: p.listmethsig_)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.DClass p, A arg)
    { /* Code For DClass Goes Here */
      //p.u_;
      for (ClassBody x: p.listclassbody_1)
      { /* ... */ }
      p.maybeblock_.accept(new MaybeBlockVisitor<R,A>(), arg);
      for (ClassBody x: p.listclassbody_2)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.DClassPar p, A arg)
    { /* Code For DClassPar Goes Here */
      //p.u_;
      for (FormalPar x: p.listformalpar_)
      { /* ... */ }
      for (ClassBody x: p.listclassbody_1)
      { /* ... */ }
      p.maybeblock_.accept(new MaybeBlockVisitor<R,A>(), arg);
      for (ClassBody x: p.listclassbody_2)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.DClassImplements p, A arg)
    { /* Code For DClassImplements Goes Here */
      //p.u_;
      for (QU x: p.listqu_)
      { /* ... */ }
      for (ClassBody x: p.listclassbody_1)
      { /* ... */ }
      p.maybeblock_.accept(new MaybeBlockVisitor<R,A>(), arg);
      for (ClassBody x: p.listclassbody_2)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.DClassParImplements p, A arg)
    { /* Code For DClassParImplements Goes Here */
      //p.u_;
      for (FormalPar x: p.listformalpar_)
      { /* ... */ }
      for (QU x: p.listqu_)
      { /* ... */ }
      for (ClassBody x: p.listclassbody_1)
      { /* ... */ }
      p.maybeblock_.accept(new MaybeBlockVisitor<R,A>(), arg);
      for (ClassBody x: p.listclassbody_2)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.DException p, A arg)
    { /* Code For DException Goes Here */
      p.constrident_.accept(new ConstrIdentVisitor<R,A>(), arg);
      return null;
    }
  }
  public class ConstrIdentVisitor<R,A> implements ConstrIdent.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.SinglConstrIdent p, A arg)
    { /* Code For SinglConstrIdent Goes Here */
      //p.u_;
      return null;
    }    public R visit(bnfc.abs.Absyn.ParamConstrIdent p, A arg)
    { /* Code For ParamConstrIdent Goes Here */
      //p.u_;
      for (ConstrType x: p.listconstrtype_)
      { /* ... */ }
      return null;
    }
  }
  public class ConstrTypeVisitor<R,A> implements ConstrType.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.EmptyConstrType p, A arg)
    { /* Code For EmptyConstrType Goes Here */
      p.t_.accept(new TVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.RecordConstrType p, A arg)
    { /* Code For RecordConstrType Goes Here */
      p.t_.accept(new TVisitor<R,A>(), arg);
      //p.l_;
      return null;
    }
  }
  public class FunBodyVisitor<R,A> implements FunBody.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.BuiltinFunBody p, A arg)
    { /* Code For BuiltinFunBody Goes Here */
      return null;
    }    public R visit(bnfc.abs.Absyn.NormalFunBody p, A arg)
    { /* Code For NormalFunBody Goes Here */
      p.pureexp_.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }
  }
  public class MethSigVisitor<R,A> implements MethSig.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.MethSignature p, A arg)
    { /* Code For MethSignature Goes Here */
      for (Ann x: p.listann_)
      { /* ... */ }
      p.t_.accept(new TVisitor<R,A>(), arg);
      //p.l_;
      for (FormalPar x: p.listformalpar_)
      { /* ... */ }
      return null;
    }
  }
  public class ClassBodyVisitor<R,A> implements ClassBody.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.FieldClassBody p, A arg)
    { /* Code For FieldClassBody Goes Here */
      p.t_.accept(new TVisitor<R,A>(), arg);
      //p.l_;
      return null;
    }    public R visit(bnfc.abs.Absyn.FieldAssignClassBody p, A arg)
    { /* Code For FieldAssignClassBody Goes Here */
      p.t_.accept(new TVisitor<R,A>(), arg);
      //p.l_;
      p.pureexp_.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.MethClassBody p, A arg)
    { /* Code For MethClassBody Goes Here */
      p.t_.accept(new TVisitor<R,A>(), arg);
      //p.l_;
      for (FormalPar x: p.listformalpar_)
      { /* ... */ }
      for (AnnStm x: p.listannstm_)
      { /* ... */ }
      return null;
    }
  }
  public class StmVisitor<R,A> implements Stm.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.SSkip p, A arg)
    { /* Code For SSkip Goes Here */
      return null;
    }    public R visit(bnfc.abs.Absyn.SSuspend p, A arg)
    { /* Code For SSuspend Goes Here */
      return null;
    }    public R visit(bnfc.abs.Absyn.SReturn p, A arg)
    { /* Code For SReturn Goes Here */
      p.exp_.accept(new ExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.SAssert p, A arg)
    { /* Code For SAssert Goes Here */
      p.pureexp_.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.SAwait p, A arg)
    { /* Code For SAwait Goes Here */
      p.awaitguard_.accept(new AwaitGuardVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.SAss p, A arg)
    { /* Code For SAss Goes Here */
      //p.l_;
      p.exp_.accept(new ExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.SFieldAss p, A arg)
    { /* Code For SFieldAss Goes Here */
      //p.l_;
      p.exp_.accept(new ExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.SDec p, A arg)
    { /* Code For SDec Goes Here */
      p.t_.accept(new TVisitor<R,A>(), arg);
      //p.l_;
      return null;
    }    public R visit(bnfc.abs.Absyn.SDecAss p, A arg)
    { /* Code For SDecAss Goes Here */
      p.t_.accept(new TVisitor<R,A>(), arg);
      //p.l_;
      p.exp_.accept(new ExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.SWhile p, A arg)
    { /* Code For SWhile Goes Here */
      p.pureexp_.accept(new PureExpVisitor<R,A>(), arg);
      p.annstm_.accept(new AnnStmVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.SIf p, A arg)
    { /* Code For SIf Goes Here */
      p.pureexp_.accept(new PureExpVisitor<R,A>(), arg);
      p.stm_.accept(new StmVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.SIfElse p, A arg)
    { /* Code For SIfElse Goes Here */
      p.pureexp_.accept(new PureExpVisitor<R,A>(), arg);
      p.stm_1.accept(new StmVisitor<R,A>(), arg);
      p.stm_2.accept(new StmVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.SCase p, A arg)
    { /* Code For SCase Goes Here */
      p.pureexp_.accept(new PureExpVisitor<R,A>(), arg);
      for (SCaseBranch x: p.listscasebranch_)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.SBlock p, A arg)
    { /* Code For SBlock Goes Here */
      for (AnnStm x: p.listannstm_)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.SExp p, A arg)
    { /* Code For SExp Goes Here */
      p.exp_.accept(new ExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.SPrint p, A arg)
    { /* Code For SPrint Goes Here */
      p.pureexp_.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.SPrintln p, A arg)
    { /* Code For SPrintln Goes Here */
      p.pureexp_.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.SThrow p, A arg)
    { /* Code For SThrow Goes Here */
      p.pureexp_.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.STryCatchFinally p, A arg)
    { /* Code For STryCatchFinally Goes Here */
      p.annstm_.accept(new AnnStmVisitor<R,A>(), arg);
      for (SCaseBranch x: p.listscasebranch_)
      { /* ... */ }
      p.maybefinally_.accept(new MaybeFinallyVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.SGive p, A arg)
    { /* Code For SGive Goes Here */
      p.pureexp_1.accept(new PureExpVisitor<R,A>(), arg);
      p.pureexp_2.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.SDuration p, A arg)
    { /* Code For SDuration Goes Here */
      p.pureexp_1.accept(new PureExpVisitor<R,A>(), arg);
      p.pureexp_2.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }
  }
  public class SCaseBranchVisitor<R,A> implements SCaseBranch.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.SCaseB p, A arg)
    { /* Code For SCaseB Goes Here */
      p.pattern_.accept(new PatternVisitor<R,A>(), arg);
      p.annstm_.accept(new AnnStmVisitor<R,A>(), arg);
      return null;
    }
  }
  public class AwaitGuardVisitor<R,A> implements AwaitGuard.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.GFut p, A arg)
    { /* Code For GFut Goes Here */
      //p.l_;
      return null;
    }    public R visit(bnfc.abs.Absyn.GFutField p, A arg)
    { /* Code For GFutField Goes Here */
      //p.l_;
      return null;
    }    public R visit(bnfc.abs.Absyn.GExp p, A arg)
    { /* Code For GExp Goes Here */
      p.pureexp_.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.GAnd p, A arg)
    { /* Code For GAnd Goes Here */
      p.awaitguard_1.accept(new AwaitGuardVisitor<R,A>(), arg);
      p.awaitguard_2.accept(new AwaitGuardVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.GDuration p, A arg)
    { /* Code For GDuration Goes Here */
      p.pureexp_1.accept(new PureExpVisitor<R,A>(), arg);
      p.pureexp_2.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }
  }
  public class ExpVisitor<R,A> implements Exp.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.ExpP p, A arg)
    { /* Code For ExpP Goes Here */
      p.pureexp_.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.ExpE p, A arg)
    { /* Code For ExpE Goes Here */
      p.effexp_.accept(new EffExpVisitor<R,A>(), arg);
      return null;
    }
  }
  public class PureExpVisitor<R,A> implements PureExp.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.EOr p, A arg)
    { /* Code For EOr Goes Here */
      p.pureexp_1.accept(new PureExpVisitor<R,A>(), arg);
      p.pureexp_2.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }        public R visit(bnfc.abs.Absyn.ELet p, A arg)
    { /* Code For ELet Goes Here */
      p.formalpar_.accept(new FormalParVisitor<R,A>(), arg);
      p.pureexp_1.accept(new PureExpVisitor<R,A>(), arg);
      p.pureexp_2.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.EIf p, A arg)
    { /* Code For EIf Goes Here */
      p.pureexp_1.accept(new PureExpVisitor<R,A>(), arg);
      p.pureexp_2.accept(new PureExpVisitor<R,A>(), arg);
      p.pureexp_3.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.ECase p, A arg)
    { /* Code For ECase Goes Here */
      p.pureexp_.accept(new PureExpVisitor<R,A>(), arg);
      for (ECaseBranch x: p.listecasebranch_)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.EAnd p, A arg)
    { /* Code For EAnd Goes Here */
      p.pureexp_1.accept(new PureExpVisitor<R,A>(), arg);
      p.pureexp_2.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }        public R visit(bnfc.abs.Absyn.EEq p, A arg)
    { /* Code For EEq Goes Here */
      p.pureexp_1.accept(new PureExpVisitor<R,A>(), arg);
      p.pureexp_2.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.ENeq p, A arg)
    { /* Code For ENeq Goes Here */
      p.pureexp_1.accept(new PureExpVisitor<R,A>(), arg);
      p.pureexp_2.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }        public R visit(bnfc.abs.Absyn.ELt p, A arg)
    { /* Code For ELt Goes Here */
      p.pureexp_1.accept(new PureExpVisitor<R,A>(), arg);
      p.pureexp_2.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.ELe p, A arg)
    { /* Code For ELe Goes Here */
      p.pureexp_1.accept(new PureExpVisitor<R,A>(), arg);
      p.pureexp_2.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.EGt p, A arg)
    { /* Code For EGt Goes Here */
      p.pureexp_1.accept(new PureExpVisitor<R,A>(), arg);
      p.pureexp_2.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.EGe p, A arg)
    { /* Code For EGe Goes Here */
      p.pureexp_1.accept(new PureExpVisitor<R,A>(), arg);
      p.pureexp_2.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }        public R visit(bnfc.abs.Absyn.EAdd p, A arg)
    { /* Code For EAdd Goes Here */
      p.pureexp_1.accept(new PureExpVisitor<R,A>(), arg);
      p.pureexp_2.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.ESub p, A arg)
    { /* Code For ESub Goes Here */
      p.pureexp_1.accept(new PureExpVisitor<R,A>(), arg);
      p.pureexp_2.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }        public R visit(bnfc.abs.Absyn.EMul p, A arg)
    { /* Code For EMul Goes Here */
      p.pureexp_1.accept(new PureExpVisitor<R,A>(), arg);
      p.pureexp_2.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.EDiv p, A arg)
    { /* Code For EDiv Goes Here */
      p.pureexp_1.accept(new PureExpVisitor<R,A>(), arg);
      p.pureexp_2.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.EMod p, A arg)
    { /* Code For EMod Goes Here */
      p.pureexp_1.accept(new PureExpVisitor<R,A>(), arg);
      p.pureexp_2.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }        public R visit(bnfc.abs.Absyn.ELogNeg p, A arg)
    { /* Code For ELogNeg Goes Here */
      p.pureexp_.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.EIntNeg p, A arg)
    { /* Code For EIntNeg Goes Here */
      p.pureexp_.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }        public R visit(bnfc.abs.Absyn.EFunCall p, A arg)
    { /* Code For EFunCall Goes Here */
      p.ql_.accept(new QLVisitor<R,A>(), arg);
      for (PureExp x: p.listpureexp_)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.ENaryFunCall p, A arg)
    { /* Code For ENaryFunCall Goes Here */
      p.ql_.accept(new QLVisitor<R,A>(), arg);
      for (PureExp x: p.listpureexp_)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.EVar p, A arg)
    { /* Code For EVar Goes Here */
      //p.l_;
      return null;
    }    public R visit(bnfc.abs.Absyn.EField p, A arg)
    { /* Code For EField Goes Here */
      //p.l_;
      return null;
    }    public R visit(bnfc.abs.Absyn.ESinglConstr p, A arg)
    { /* Code For ESinglConstr Goes Here */
      p.qu_.accept(new QUVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.EParamConstr p, A arg)
    { /* Code For EParamConstr Goes Here */
      p.qu_.accept(new QUVisitor<R,A>(), arg);
      for (PureExp x: p.listpureexp_)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.ELit p, A arg)
    { /* Code For ELit Goes Here */
      p.literal_.accept(new LiteralVisitor<R,A>(), arg);
      return null;
    }    
  }
  public class ECaseBranchVisitor<R,A> implements ECaseBranch.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.ECaseB p, A arg)
    { /* Code For ECaseB Goes Here */
      p.pattern_.accept(new PatternVisitor<R,A>(), arg);
      p.pureexp_.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }
  }
  public class PatternVisitor<R,A> implements Pattern.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.PLit p, A arg)
    { /* Code For PLit Goes Here */
      p.literal_.accept(new LiteralVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.PVar p, A arg)
    { /* Code For PVar Goes Here */
      //p.l_;
      return null;
    }    public R visit(bnfc.abs.Absyn.PSinglConstr p, A arg)
    { /* Code For PSinglConstr Goes Here */
      p.qu_.accept(new QUVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.PParamConstr p, A arg)
    { /* Code For PParamConstr Goes Here */
      p.qu_.accept(new QUVisitor<R,A>(), arg);
      for (Pattern x: p.listpattern_)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.PWildCard p, A arg)
    { /* Code For PWildCard Goes Here */
      return null;
    }
  }
  public class EffExpVisitor<R,A> implements EffExp.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.New p, A arg)
    { /* Code For New Goes Here */
      p.qu_.accept(new QUVisitor<R,A>(), arg);
      for (PureExp x: p.listpureexp_)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.NewLocal p, A arg)
    { /* Code For NewLocal Goes Here */
      p.qu_.accept(new QUVisitor<R,A>(), arg);
      for (PureExp x: p.listpureexp_)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.SyncMethCall p, A arg)
    { /* Code For SyncMethCall Goes Here */
      p.pureexp_.accept(new PureExpVisitor<R,A>(), arg);
      //p.l_;
      for (PureExp x: p.listpureexp_)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.ThisSyncMethCall p, A arg)
    { /* Code For ThisSyncMethCall Goes Here */
      //p.l_;
      for (PureExp x: p.listpureexp_)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.AsyncMethCall p, A arg)
    { /* Code For AsyncMethCall Goes Here */
      p.pureexp_.accept(new PureExpVisitor<R,A>(), arg);
      //p.l_;
      for (PureExp x: p.listpureexp_)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.AwaitMethCall p, A arg)
    { /* Code For AwaitMethCall Goes Here */
      p.pureexp_.accept(new PureExpVisitor<R,A>(), arg);
      //p.l_;
      for (PureExp x: p.listpureexp_)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.Get p, A arg)
    { /* Code For Get Goes Here */
      p.pureexp_.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.Readln p, A arg)
    { /* Code For Readln Goes Here */
      return null;
    }    public R visit(bnfc.abs.Absyn.ProNew p, A arg)
    { /* Code For ProNew Goes Here */
      return null;
    }    public R visit(bnfc.abs.Absyn.ProTry p, A arg)
    { /* Code For ProTry Goes Here */
      p.pureexp_.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.Now p, A arg)
    { /* Code For Now Goes Here */
      return null;
    }
  }
  public class AnnVisitor<R,A> implements Ann.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.Annotation p, A arg)
    { /* Code For Annotation Goes Here */
      p.ann__.accept(new Ann_Visitor<R,A>(), arg);
      return null;
    }
  }
  public class Ann_Visitor<R,A> implements Ann_.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.AnnNoType p, A arg)
    { /* Code For AnnNoType Goes Here */
      p.pureexp_.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.AnnWithType p, A arg)
    { /* Code For AnnWithType Goes Here */
      p.t_.accept(new TVisitor<R,A>(), arg);
      p.pureexp_.accept(new PureExpVisitor<R,A>(), arg);
      return null;
    }
  }
  public class AnnStmVisitor<R,A> implements AnnStm.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.AnnStatement p, A arg)
    { /* Code For AnnStatement Goes Here */
      for (Ann x: p.listann_)
      { /* ... */ }
      p.stm_.accept(new StmVisitor<R,A>(), arg);
      return null;
    }
  }
  public class AnnDeclVisitor<R,A> implements AnnDecl.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.AnnDeclaration p, A arg)
    { /* Code For AnnDeclaration Goes Here */
      for (Ann x: p.listann_)
      { /* ... */ }
      p.decl_.accept(new DeclVisitor<R,A>(), arg);
      return null;
    }
  }
  public class MaybeFinallyVisitor<R,A> implements MaybeFinally.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.JustFinally p, A arg)
    { /* Code For JustFinally Goes Here */
      p.annstm_.accept(new AnnStmVisitor<R,A>(), arg);
      return null;
    }    public R visit(bnfc.abs.Absyn.NoFinally p, A arg)
    { /* Code For NoFinally Goes Here */
      return null;
    }
  }
  public class MaybeBlockVisitor<R,A> implements MaybeBlock.Visitor<R,A>
  {
    public R visit(bnfc.abs.Absyn.JustBlock p, A arg)
    { /* Code For JustBlock Goes Here */
      for (AnnStm x: p.listannstm_)
      { /* ... */ }
      return null;
    }    public R visit(bnfc.abs.Absyn.NoBlock p, A arg)
    { /* Code For NoBlock Goes Here */
      return null;
    }
  }
}