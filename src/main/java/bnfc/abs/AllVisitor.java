package bnfc.abs;

import bnfc.abs.Absyn.*;

/** BNFC-Generated All Visitor */
public interface AllVisitor<R,A> extends
  bnfc.abs.Absyn.AnyIdent.Visitor<R,A>,
  bnfc.abs.Absyn.Program.Visitor<R,A>,
  bnfc.abs.Absyn.Module.Visitor<R,A>,
  bnfc.abs.Absyn.Export.Visitor<R,A>,
  bnfc.abs.Absyn.Import.Visitor<R,A>,
  bnfc.abs.Absyn.ImportType.Visitor<R,A>,
  bnfc.abs.Absyn.Type.Visitor<R,A>,
  bnfc.abs.Absyn.QType.Visitor<R,A>,
  bnfc.abs.Absyn.QTypeSegment.Visitor<R,A>,
  bnfc.abs.Absyn.TType.Visitor<R,A>,
  bnfc.abs.Absyn.TTypeSegment.Visitor<R,A>,
  bnfc.abs.Absyn.Decl.Visitor<R,A>,
  bnfc.abs.Absyn.ConstrIdent.Visitor<R,A>,
  bnfc.abs.Absyn.ConstrType.Visitor<R,A>,
  bnfc.abs.Absyn.FunBody.Visitor<R,A>,
  bnfc.abs.Absyn.MethSignat.Visitor<R,A>,
  bnfc.abs.Absyn.ClassBody.Visitor<R,A>,
  bnfc.abs.Absyn.Block.Visitor<R,A>,
  bnfc.abs.Absyn.MaybeBlock.Visitor<R,A>,
  bnfc.abs.Absyn.Param.Visitor<R,A>,
  bnfc.abs.Absyn.Stm.Visitor<R,A>,
  bnfc.abs.Absyn.CatchBranch.Visitor<R,A>,
  bnfc.abs.Absyn.MaybeFinally.Visitor<R,A>,
  bnfc.abs.Absyn.Guard.Visitor<R,A>,
  bnfc.abs.Absyn.Exp.Visitor<R,A>,
  bnfc.abs.Absyn.PureExp.Visitor<R,A>,
  bnfc.abs.Absyn.CaseBranch.Visitor<R,A>,
  bnfc.abs.Absyn.Pattern.Visitor<R,A>,
  bnfc.abs.Absyn.Literal.Visitor<R,A>,
  bnfc.abs.Absyn.EffExp.Visitor<R,A>,
  bnfc.abs.Absyn.Ann.Visitor<R,A>,
  bnfc.abs.Absyn.AnnDecl.Visitor<R,A>,
  bnfc.abs.Absyn.AnnType.Visitor<R,A>
{}
