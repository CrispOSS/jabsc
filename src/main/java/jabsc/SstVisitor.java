package jabsc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;

import bnfc.abs.AbstractVisitor;
import bnfc.abs.Absyn.*;

public class SstVisitor extends AbstractVisitor<Prog, SstWriter> {
	private String packageName;
	private Path outputDirectory;
	private final PathResolver pathResolver;

	private final static String EXTENSION = ".abs";
	private static final String MAIN_CLASS_NAME = "Main";
	

	/*
	 * TB = Dummy variable for task base address, used for substitution OB =
	 * Dummy variable object base address, used for substitution
	 */
	public SstWriter createNewFileWriter(String typeName) {
		try {
			Path fqdnOutputDirectory = pathResolver.resolveOutputDirectory(packageName, outputDirectory);
			return new SstWriter(Files.newBufferedWriter(fqdnOutputDirectory.resolve(typeName + EXTENSION),
					StandardOpenOption.CREATE, StandardOpenOption.WRITE));
		} catch (IOException e) {
			throw new IllegalArgumentException("Cannot create Java writer for " + typeName, e);
		}
	}

	public SstVisitor(String packageName, Prog prog, Path outputDirectory, PathResolver pathResolver) {
		this.pathResolver = pathResolver;
		this.packageName = packageName;
		this.prog = prog;
		this.outputDirectory = outputDirectory;
	}

	Prog prog;
	static final String BASE = "Base";

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.LNull,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(LNull p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.LThis,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(LThis p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.LStr,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(LStr p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.LInt,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(LInt p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.LFloat,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(LFloat p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.LThisDC,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(LThisDC p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.U_, java.lang.Object)
	 */
	@Override
	public Prog visit(U_ p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.QU_, java.lang.Object)
	 */
	@Override
	public Prog visit(QU_ p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.L_, java.lang.Object)
	 */
	@Override
	public Prog visit(L_ p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.QL_, java.lang.Object)
	 */
	@Override
	public Prog visit(QL_ p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.LA, java.lang.Object)
	 */
	@Override
	public Prog visit(LA p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.UA, java.lang.Object)
	 */
	@Override
	public Prog visit(UA p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.QA_, java.lang.Object)
	 */
	@Override
	public Prog visit(QA_ p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.TSimple,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(TSimple p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.TPoly,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(TPoly p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.TInfer,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(TInfer p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.FormalParameter,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(FormalParameter p, SstWriter arg) {
		
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.Prog,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(Prog p, SstWriter arg) {
		for (Module m : p.listmodule_) {
			m.accept(this, arg);
		}
		
		
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.Mod, java.lang.Object)
	 */
	@Override
	public Prog visit(Mod m, SstWriter arg) {
		for (AnnDecl ad : m.listanndecl_) {
			ad.accept(this, arg);
		}
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.StarExport,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(StarExport p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.StarFromExport,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(StarFromExport p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.AnyExport,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(AnyExport p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.AnyFromExport,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(AnyFromExport p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.StarFromImport,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(StarFromImport p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.AnyImport,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(AnyImport p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.AnyFromImport,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(AnyFromImport p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.NoForeign,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(NoForeign p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.YesForeign,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(YesForeign p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.DType,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(DType p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.DTypePoly,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(DTypePoly p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.DData,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(DData p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.DDataPoly,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(DDataPoly p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.DFun,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(DFun p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.DFunPoly,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(DFunPoly p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.DInterf,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(DInterf p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.DExtends,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(DExtends p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.DClass,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(DClass p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.DClassPar,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(DClassPar p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.DClassImplements,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(DClassImplements p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.DClassParImplements,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(DClassParImplements p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.DException,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(DException p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.SinglConstrIdent,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(SinglConstrIdent p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.ParamConstrIdent,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(ParamConstrIdent p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.EmptyConstrType,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(EmptyConstrType p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.RecordConstrType,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(RecordConstrType p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.BuiltinFunBody,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(BuiltinFunBody p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.NormalFunBody,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(NormalFunBody p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.MethSignature,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(MethSignature p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.FieldClassBody,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(FieldClassBody p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.FieldAssignClassBody,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(FieldAssignClassBody p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.MethClassBody,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(MethClassBody p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.SSkip,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(SSkip p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.SSuspend,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(SSuspend p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.SReturn,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(SReturn p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.SAssert,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(SAssert p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.SAwait,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(SAwait p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.SFieldAss,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(SFieldAss p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	public Prog visit(bnfc.abs.Absyn.SAss p, SstWriter arg) {
		/*--before
		 * Alloc(RefInt, 1)
		 * Alloc + Assgin 14 times
		 * t= Pair<0,-14>
		 * x= Pair<0,-3>
		 * Spawn (Ref(fields), Ref(formalPars), SstList (x=3)) -- SstList is visited.
		 * --now
		 * Alloc(t,1);
		 * Assign(t, tag<0,0>)
		 * Assign(x,t)
		 */
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.SDec,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(SDec p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.SDecAss,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(SDecAss p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.SWhile,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(SWhile p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.SIf, java.lang.Object)
	 */
	@Override
	public Prog visit(SIf p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.SIfElse,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(SIfElse p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.SCase,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(SCase p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.SBlock,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(SBlock p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.SExp,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(SExp p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.SPrint,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(SPrint p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.SPrintln,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(SPrintln p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.SThrow,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(SThrow p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.STryCatchFinally,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(STryCatchFinally p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.SGive,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(SGive p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.SDuration,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(SDuration p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.SCaseB,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(SCaseB p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.GFut,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(GFut p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.GFutField,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(GFutField p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.GExp,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(GExp p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.GAnd,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(GAnd p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.GDuration,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(GDuration p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.ExpP,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(ExpP p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.ExpE,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(ExpE p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.EOr, java.lang.Object)
	 */
	@Override
	public Prog visit(EOr p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.ELet,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(ELet p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.EIf, java.lang.Object)
	 */
	@Override
	public Prog visit(EIf p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.ECase,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(ECase p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.EAnd,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(EAnd p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.EEq, java.lang.Object)
	 */
	@Override
	public Prog visit(EEq p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.ENeq,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(ENeq p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.ELt, java.lang.Object)
	 */
	@Override
	public Prog visit(ELt p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.ELe, java.lang.Object)
	 */
	@Override
	public Prog visit(ELe p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.EGt, java.lang.Object)
	 */
	@Override
	public Prog visit(EGt p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.EGe, java.lang.Object)
	 */
	@Override
	public Prog visit(EGe p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.EAdd,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(EAdd p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.ESub,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(ESub p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.EMul,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(EMul p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.EDiv,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(EDiv p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.EMod,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(EMod p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.ELogNeg,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(ELogNeg p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.EIntNeg,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(EIntNeg p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.EFunCall,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(EFunCall p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.ENaryFunCall,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(ENaryFunCall p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.EVar,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(EVar p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.EField,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(EField p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.ESinglConstr,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(ESinglConstr p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.EParamConstr,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(EParamConstr p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.ELit,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(ELit p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.ECaseB,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(ECaseB p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.PLit,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(PLit p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.PVar,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(PVar p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.PSinglConstr,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(PSinglConstr p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.PParamConstr,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(PParamConstr p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.PWildCard,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(PWildCard p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.New, java.lang.Object)
	 */
	@Override
	public Prog visit(New p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.NewLocal,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(NewLocal p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.SyncMethCall,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(SyncMethCall p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.ThisSyncMethCall,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(ThisSyncMethCall p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.AsyncMethCall,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(AsyncMethCall p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.AwaitMethCall,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(AwaitMethCall p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.Get, java.lang.Object)
	 */
	@Override
	public Prog visit(Get p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.Readln,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(Readln p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.ProNew,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(ProNew p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.ProTry,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(ProTry p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.Now, java.lang.Object)
	 */
	@Override
	public Prog visit(Now p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.Annotation,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(Annotation p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.AnnNoType,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(AnnNoType p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.AnnWithType,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(AnnWithType p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.AnnStatement,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(AnnStatement p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.AnnDeclaration,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(AnnDeclaration p, SstWriter arg) {
		for (Ann a : p.listann_) {
			a.accept(this, arg);
		}
		p.decl_.accept(this, arg);
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.JustFinally,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(JustFinally p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.NoFinally,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(NoFinally p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.JustBlock,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(JustBlock p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bnfc.abs.AbstractVisitor#visit(bnfc.abs.Absyn.NoBlock,
	 * java.lang.Object)
	 */
	@Override
	public Prog visit(NoBlock p, SstWriter arg) {
		// TODO Auto-generated method stub
		return prog;
	}
	
	protected void visitMain(Mod m, ScalaWriter w) {
		try {
			SstWriter sw = createNewFileWriter(MAIN_CLASS_NAME);
			sw.emitPackage(packageName);
			// emitDefaultImports(jw);
			sw.emitTypeDecl("Memory", "mainMemory", "EmptyMap");
			sw.close();
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
