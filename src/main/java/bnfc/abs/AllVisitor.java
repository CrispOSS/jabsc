package bnfc.abs;

import bnfc.abs.Absyn.*;

/** BNFC-Generated All Visitor */
public interface AllVisitor<R,A> extends
  bnfc.abs.Absyn.Literal.Visitor<R,A>,
  bnfc.abs.Absyn.QU.Visitor<R,A>,
  bnfc.abs.Absyn.QL.Visitor<R,A>,
  bnfc.abs.Absyn.QA.Visitor<R,A>,
  bnfc.abs.Absyn.T.Visitor<R,A>,
  bnfc.abs.Absyn.FormalPar.Visitor<R,A>,
  bnfc.abs.Absyn.Program.Visitor<R,A>,
  bnfc.abs.Absyn.Module.Visitor<R,A>,
  bnfc.abs.Absyn.Export.Visitor<R,A>,
  bnfc.abs.Absyn.Import.Visitor<R,A>,
  bnfc.abs.Absyn.IsForeign.Visitor<R,A>,
  bnfc.abs.Absyn.Decl.Visitor<R,A>,
  bnfc.abs.Absyn.ConstrIdent.Visitor<R,A>,
  bnfc.abs.Absyn.ConstrType.Visitor<R,A>,
  bnfc.abs.Absyn.FunBody.Visitor<R,A>,
  bnfc.abs.Absyn.MethSig.Visitor<R,A>,
  bnfc.abs.Absyn.ClassBody.Visitor<R,A>,
  bnfc.abs.Absyn.Stm.Visitor<R,A>,
  bnfc.abs.Absyn.SCaseBranch.Visitor<R,A>,
  bnfc.abs.Absyn.AwaitGuard.Visitor<R,A>,
  bnfc.abs.Absyn.Exp.Visitor<R,A>,
  bnfc.abs.Absyn.PureExp.Visitor<R,A>,
  bnfc.abs.Absyn.ECaseBranch.Visitor<R,A>,
  bnfc.abs.Absyn.Pattern.Visitor<R,A>,
  bnfc.abs.Absyn.EffExp.Visitor<R,A>,
  bnfc.abs.Absyn.Ann.Visitor<R,A>,
  bnfc.abs.Absyn.Ann_.Visitor<R,A>,
  bnfc.abs.Absyn.AnnStm.Visitor<R,A>,
  bnfc.abs.Absyn.AnnDecl.Visitor<R,A>,
  bnfc.abs.Absyn.MaybeFinally.Visitor<R,A>,
  bnfc.abs.Absyn.MaybeBlock.Visitor<R,A>
{}
