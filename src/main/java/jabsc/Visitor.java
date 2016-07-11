package jabsc;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.print.attribute.IntegerSyntax;

import com.google.common.base.Objects;
import com.google.common.base.StandardSystemProperty;
import com.google.common.base.Supplier;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import com.pogofish.jadt.JADT;

//import abs.api.Actor;
//import abs.api.Response;
import abs.api.remote.*;
import abs.api.cwi.LocalActor;
import abs.api.cwi.Actor;
import abs.api.cwi.ABSFuture;
import abs.api.cwi.Functional;

import bnfc.abs.AbstractVisitor;
import bnfc.abs.Absyn.*;

import java.applet.*;

/**
 * The visitor for all possible nodes in the AST of an ABS program.
 */

class Visitor extends AbstractVisitor<Prog, JavaWriter> {

	/**
	 * High-level element types that an ABS program/module can have.
	 */
	static enum AbsElementType {
		/**
		 * Equivalent to Java's {@link ElementKind#INTERFACE}
		 */
		INTERFACE,

		/**
		 * Same as Java's {@link ElementKind#CLASS}
		 */
		CLASS,

		/**
		 * An abstract data type declaration.
		 */
		DATA,

		/**
		 * Equivalent of a set of Java's <code>static</code> functions.
		 */
		FUNCTION,

		/**
		 * An abstract data type declaration.
		 */
		TYPE,

		/**
		 * An exception declaration.
		 */
		EXCEPTION,

		;
	}

	// Constants

	private static final String DATA_DECL_INSTANCE_NAME = "INSTANCE";
	private static final char CHAR_UNDERSCORE = '_';
	private static final char CHAR_DOT = '.';
	private static final String VOID_WRAPPER_CLASS_NAME = "Void";
	private static final String VOID_PRIMITIVE_NAME = "void";
	private static final String LITERAL_THIS = "this";
	private static final String LITERAL_NULL = "null";
	private static final String FUNCTIONS_CLASS_NAME = "Functions";
	private static final String MAIN_CLASS_NAME = "Main";
	private static final String COMMA_SPACE = ", ";
	private static final String METHOD_GET = "geT";
	private static final String EXTERN = "JavaExternClass";
	private static final String STATIC = "JavaStaticClass";
	private static final String ACTOR_SERVER_MEMBER = "me";

	private static final String NEW_LINE = StandardSystemProperty.LINE_SEPARATOR.value();
	private static final String ABS_API_ACTOR_CLASS = LocalActor.class.getName();
	private static final String ABS_API_INTERFACE_CLASS = Actor.class.getName();

	private static final String ABS_API_ACTOR_SERVER_CLASS = ActorServer.class.getName();
	private static final Set<Modifier> DEFAULT_MODIFIERS = Collections.singleton(Modifier.PUBLIC);
	private static final String[] DEFAULT_IMPORTS = new String[] { Collection.class.getPackage().getName() + ".*",
			Function.class.getPackage().getName() + ".*", Callable.class.getPackage().getName() + ".*",
			AtomicLong.class.getPackage().getName() + ".*", Lock.class.getPackage().getName() + ".*",
			LocalActor.class.getPackage().getName() + ".*", Functional.class.getPackage().getName() + ".*",
			// CloudProvider.class.getPackage().getName() + ".*",
			// DeploymentComponent.class.getPackage().getName() + ".*",
			ABSFuture.class.getPackage().getName() + ".*" };
	// private static final String[] DEFAULT_IMPORTS_PATTERNS = new String[] {
	// "com.leacox.motif.function.*", "com.leacox.motif.matching.*",
	// "com.leacox.motif.cases.*", "com.leacox.motif.caseclass.*" };
	private static final String[] DEFAULT_STATIC_IMPORTS = new String[] {
			Functional.class.getPackage().getName() + "." + Functional.class.getSimpleName() + ".*",
			// CloudProvider.class.getPackage().getName() + "." +
			// CloudProvider.class.getSimpleName()
			// + ".*",
			// DeploymentComponent.class.getPackage().getName() + "."
			// + DeploymentComponent.class.getSimpleName() + ".*"
	};
	/*
	 * private static final String[] DEFAULT_STATIC_IMPORTS_PATTERNS = new
	 * String[] { "com.leacox.motif.Motif.*",
	 * "com.leacox.motif.cases.ListConsCases.*",
	 * "com.leacox.motif.cases.Case1Cases.*",
	 * "com.leacox.motif.cases.Case2Cases.*",
	 * "com.leacox.motif.cases.Case3Cases.*", "com.leacox.motif.MatchesAny.*",
	 * "com.leacox.motif.hamcrest.CaseThatCases.*",
	 * "com.leacox.motif.MatchesExact.eq", "org.hamcrest.CoreMatchers.*" };
	 */
	// Internal Fields

	private static final Logger LOGGER = Logger.getLogger(Visitor.class.getName());
	private static final Random RANDOM = new Random(System.currentTimeMillis());

	private final Set<String> moduleNames;
	private final Prog prog;
	private final JavaWriterSupplier javaWriterSupplier;
	private final String packageName;
	private final JavaTypeTranslator javaTypeTranslator;
	private final Path outputDirectory;

	// Internal state
	private final Multimap<String, MethodDefinition> methods = Multimaps.newSetMultimap(new HashMap<>(),
			new Supplier<Set<MethodDefinition>>() {
				@Override
				public Set<MethodDefinition> get() {
					return new HashSet<>();
				}
			});
	private final Multimap<String, VarDefinition> variables = Multimaps.newSetMultimap(new HashMap<>(),
			new Supplier<Set<VarDefinition>>() {
				@Override
				public Set<VarDefinition> get() {
					return new HashSet<>();
				}
			});
	private final Stack<Module> modules = new Stack<>();
	private final Stack<String> classes = new Stack<>();
	private final EnumMap<AbsElementType, List<AnnDecl>> elements = new EnumMap<>(AbsElementType.class);
	private final Map<String, String> classNames = new HashMap<>();
	private final Set<String> packageLevelImports = new HashSet<>();
	private final Set<String> otherImports = new HashSet<>();
	private final Map<String, String> dataDeclarations = new HashMap<>();
	private final Set<String> exceptionDeclaraions = new HashSet<>();
	private final Set<String> staticImports = new HashSet<>();

	private MethodDefinition currentMethod;

	/**
	 * Ctor.
	 * 
	 * @param packageName
	 *            the package spec of the program
	 * @param prog
	 *            the parsed {@link Prog} AST node
	 * @param javaWriterSupplier
	 *            the {@link JavaWriterSupplier} for each top-level element
	 * @param javaTypeTranslator
	 *            The ABS to Java type translator
	 * @param outputDirectory
	 */
	public Visitor(String packageName, Prog prog, JavaWriterSupplier javaWriterSupplier,
			JavaTypeTranslator javaTypeTranslator, Path outputDirectory) {
		this.packageName = packageName;
		this.prog = prog;
		this.javaWriterSupplier = javaWriterSupplier;
		this.javaTypeTranslator = javaTypeTranslator;
		this.outputDirectory = outputDirectory;
		this.moduleNames = new HashSet<>();
	}

	@Override
	public Prog visit(Prog p, JavaWriter w) {
		// WARNING: `w` should NOT be used in this method;
		// otherwise, I/O issues occur during generation.
		Prog program = (Prog) p;
		buildProgramDeclarationTypes(program);
		for (Module module : program.listmodule_) {
			moduleNames.add(getQTypeName(((Mod) module).qu_, false));
			modules.push(module);
			module.accept(this, w);
			modules.pop();
		}
		return prog;
	}

	@Override
	public Prog visit(Mod m, JavaWriter w) {
		try {

			// m.listimport_.forEach(i -> i.accept(this, w));

			// Types
			for (AnnDecl decl : elements.get(AbsElementType.TYPE)) {
				// Does NOT use writer.
				decl.accept(this, w);
			}

			// Data
			for (AnnDecl ad : elements.get(AbsElementType.DATA)) {
				AnnDeclaration decl = (AnnDeclaration) ad;
				String name = getTopLevelDeclIdentifier(decl.decl_);
				JavaWriterSupplier jadtWriterSupplier = new DefaultJavaWriterSupplier(
						PathResolver.DEFAULT_PATH_RESOLVER, packageName, ".jadt", outputDirectory);
				StringWriter jadt = new StringWriter();
				PrintWriter pw = new PrintWriter(jadt);

				pw.printf("package %s\n", this.packageName);
				pw.println();

				pw.printf("import abs.api.cwi.Functional.*\n");
				Arrays.asList(DEFAULT_IMPORTS).forEach(i -> pw.printf("import %s\n", i));
				// Arrays.asList(DEFAULT_IMPORTS_PATTERNS).forEach(
				// i -> pw.printf("import %s\n", i));
				pw.println();

				if (decl.decl_ instanceof DData) {
					DData dd = (DData) decl.decl_;
					pw.printf("%s = ", dd.u_);
					pw.println();
					pw.print("\t");
					List<String> defs = new ArrayList<>();
					for (ConstrIdent ci : dd.listconstrident_) {
						if (ci instanceof SinglConstrIdent) {
							SinglConstrIdent sci = (SinglConstrIdent) ci;
							defs.add(sci.u_);
						} else if (ci instanceof ParamConstrIdent) {
							ParamConstrIdent pci = (ParamConstrIdent) ci;
							List<String> constrParams = new ArrayList<>();
							for (ConstrType ct : pci.listconstrtype_) {
								if (ct instanceof EmptyConstrType) {
								} else if (ct instanceof RecordConstrType) {
									RecordConstrType rct = (RecordConstrType) ct;
									constrParams.add(String.format("final %s %s", getTypeName(rct.t_), rct.l_));
								}
							}
							String pciType = String.format("%s(%s)", pci.u_, String.join(COMMA_SPACE, constrParams));
							defs.add(pciType);
						}
					}
					pw.println(String.join("\n\t| ", defs));
				} else if (decl.decl_ instanceof DDataPoly) {
					DDataPoly dpd = (DDataPoly) decl.decl_;
					pw.printf("%s<%s> = ", dpd.u_, String.join(COMMA_SPACE, dpd.listu_));
					pw.println();
					pw.print("\t");
					List<String> defs = new ArrayList<>();
					for (ConstrIdent ci : dpd.listconstrident_) {
						if (ci instanceof SinglConstrIdent) {
							SinglConstrIdent sci = (SinglConstrIdent) ci;
							defs.add(sci.u_);
						} else if (ci instanceof ParamConstrIdent) {
							ParamConstrIdent pci = (ParamConstrIdent) ci;
							List<String> constrParams = new ArrayList<>();
							for (ConstrType ct : pci.listconstrtype_) {
								if (ct instanceof EmptyConstrType) {
									String type = toString(((EmptyConstrType) ct).t_);
									String typeName = stripGenericParameterType(type);
									constrParams.add(String.format("final %s %sValue", type, typeName));
								} else if (ct instanceof RecordConstrType) {
									RecordConstrType rct = (RecordConstrType) ct;
									constrParams.add(String.format("final %s %s", getTypeName(rct.t_), rct.l_));
								}
							}
							String pciType = String.format("%s(%s)", pci.u_, String.join(COMMA_SPACE, constrParams));
							defs.add(pciType);
						}
					}
					pw.println(String.join("\n\t| ", defs));
				}
				pw.println();
				String simpleName = stripGenericParameterType(name);
				Path jadtOutputDirectory = PathResolver.DEFAULT_PATH_RESOLVER.resolveOutputDirectory("",
						this.outputDirectory);
				try (Writer jadtWriter = Files.newBufferedWriter(jadtOutputDirectory.resolve(simpleName + ".jadt"),
						StandardOpenOption.CREATE)) {
					jadtWriter.write(jadt.toString());
				}
				JADT.main(new String[] { jadtOutputDirectory.toAbsolutePath().toString(),
						this.outputDirectory.toAbsolutePath().toString() });
				this.packageLevelImports.add(simpleName);

				// JavaWriter declWriter =
				// javaWriterSupplier.apply(name);
				// declWriter.emitPackage(packageName);
				// visitImports(m.listimport_, declWriter);
				// decl.accept(this, declWriter);
				// close(declWriter, w);
			}

			// Exception
			for (AnnDecl ad : elements.get(AbsElementType.EXCEPTION)) {
				AnnDeclaration decl = (AnnDeclaration) ad;
				String name = getTopLevelDeclIdentifier(decl.decl_);
				JavaWriter declWriter = javaWriterSupplier.apply(name);
				declWriter.emitPackage(packageName);
				visitImports(m.listimport_, declWriter);
				decl.accept(this, declWriter);
				close(declWriter, w);
			}

			// Interfaces
			for (AnnDecl ad : elements.get(AbsElementType.INTERFACE)) {
				AnnDeclaration decl = (AnnDeclaration) ad;
				String name = getTopLevelDeclIdentifier(decl.decl_);
				JavaWriter declWriter = javaWriterSupplier.apply(name);
				declWriter.emitPackage(packageName);
				visitImports(m.listimport_, declWriter);
				decl.accept(this, declWriter);
				close(declWriter, w);
			}

			// Classes
			for (AnnDecl ad : elements.get(AbsElementType.CLASS)) {
				AnnDeclaration decl = (AnnDeclaration) ad;
				String name = getTopLevelDeclIdentifier(decl.decl_);
				final String refinedClassName = getRefinedClassName(name);
				JavaWriter declWriter = javaWriterSupplier.apply(refinedClassName);
				declWriter.emitPackage(packageName);
				visitImports(m.listimport_, declWriter);
				decl.accept(this, declWriter);
				close(declWriter, w);
			}

			visitFunctions(m, w);
			visitMain(m, w);

			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(AnyImport p, JavaWriter w) {
		Collection<String> types = new HashSet<>();
		if (p.isforeign_ instanceof YesForeign) {
			logNotImplemented("%s", "foreign import");
		} else {
			for (QA qa : p.listqa_) {
				types.add(getQTypeName(qa, false));
			}
			try {
				w.emitImports(types);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return prog;
	}

	@Override
	public Prog visit(StarFromImport sfi, JavaWriter w) {
		String type = getQTypeName(sfi.qu_, false);
		try {
			w.emitImports(type.toLowerCase() + ".*");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// this.staticImports.add(this.packageName + "." + type + ".*");
		return prog;
	}

	@Override
	public Prog visit(DInterf id, JavaWriter w) {
		try {
			final String identifier = id.u_;
			beginElementKind(w, ElementKind.INTERFACE, identifier, DEFAULT_MODIFIERS, null, null);
			this.classes.push(identifier);
			w.emitEmptyLine();
			id.listmethsig_.forEach(sig -> visit((MethSignature) sig, w));
			w.endType();
			this.classes.pop();
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(DExtends ed, JavaWriter w) {
		try {
			final String identifier = ed.u_;
			beginElementKind(w, ElementKind.INTERFACE, identifier, DEFAULT_MODIFIERS, null, toList(ed.listqu_, true));
			this.classes.push(identifier);
			w.emitEmptyLine();
			ed.listmethsig_.forEach(sig -> sig.accept(this, w));
			w.endType();
			this.classes.pop();
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(DClass p, JavaWriter w) {
		// Order to visit 'class':
		// - visit class body including 'methods'
		// - visit 'maybeblock'
		try {
			final String identifier = p.u_;
			final String className = getRefinedClassName(identifier);
			beginElementKind(w, ElementKind.CLASS, className, DEFAULT_MODIFIERS, ABS_API_ACTOR_CLASS, null);
			this.classes.push(className);

			w.emitEmptyLine();
			for (ClassBody cb : p.listclassbody_1) {
				cb.accept(this, w);
			}
			for (ClassBody cb : p.listclassbody_2) {
				cb.accept(this, w);
			}
			w.beginConstructor(DEFAULT_MODIFIERS, null, null);
			// emitThisActorRegistration(w);
			p.maybeblock_.accept(this, w);
			// emitDefaultRunMethodExecution(w, className);
			w.endConstructor();
			// emitToStringMethod(w);
			w.endType();
			this.classes.pop();

			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(DClassImplements ci, JavaWriter w) {
		try {
			final String identifier = ci.u_;
			String className = getRefinedClassName(identifier);
			beginElementKind(w, ElementKind.CLASS, className, DEFAULT_MODIFIERS, ABS_API_ACTOR_CLASS,
					toList(ci.listqu_, true));
			this.classes.push(className);
			w.emitEmptyLine();
			for (ClassBody cb : ci.listclassbody_1) {
				cb.accept(this, w);
			}
			for (ClassBody cb : ci.listclassbody_2) {
				cb.accept(this, w);
			}
			w.beginConstructor(DEFAULT_MODIFIERS, null, null);
			// emitThisActorRegistration(w);
			ci.maybeblock_.accept(this, w);
			// emitDefaultRunMethodExecution(w, className);
			w.endConstructor();
			// emitToStringMethod(w);
			w.endType();
			this.classes.pop();

			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(DClassPar cpd, JavaWriter w) {
		try {
			final String identifier = cpd.u_;
			String className = getRefinedClassName(identifier);
			beginElementKind(w, ElementKind.CLASS, className, DEFAULT_MODIFIERS, ABS_API_ACTOR_CLASS, null);
			this.classes.push(className);
			w.emitEmptyLine();
			List<String> parameters = new ArrayList<>();
			for (FormalPar param : cpd.listformalpar_) {
				FormalParameter p = (FormalParameter) param;
				String fieldType = getTypeName(p.t_);
				parameters.add(fieldType);
				parameters.add(p.l_);
				emitField(w, fieldType, p.l_, null, false);
			}
			w.emitEmptyLine();
			for (ClassBody cb : cpd.listclassbody_1) {
				cb.accept(this, w);
			}
			for (ClassBody cb : cpd.listclassbody_2) {
				cb.accept(this, w);
			}
			w.beginConstructor(DEFAULT_MODIFIERS, parameters, null);
			for (FormalPar param : cpd.listformalpar_) {
				FormalParameter p = (FormalParameter) param;
				w.emitStatement("this." + p.l_ + " = " + p.l_);
			}
			// emitThisActorRegistration(w);
			cpd.maybeblock_.accept(this, w);
			// emitDefaultRunMethodExecution(w, className);
			w.endConstructor();
			// emitToStringMethod(w);
			w.endType();
			this.classes.pop();
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(DClassParImplements cpi, JavaWriter w) {
		try {
			final String identifier = cpi.u_;
			String className = getRefinedClassName(identifier);
			System.out.println(className);
			beginElementKind(w, ElementKind.CLASS, className, DEFAULT_MODIFIERS, ABS_API_ACTOR_CLASS,
					toList(cpi.listqu_, true));
			this.classes.push(className);
			w.emitEmptyLine();
			List<String> parameters = new ArrayList<>();
			for (FormalPar param : cpi.listformalpar_) {
				FormalParameter p = (FormalParameter) param;
				String fieldType = getTypeName(p.t_);
				parameters.add(fieldType);
				parameters.add(p.l_);
				emitField(w, fieldType, p.l_, null, false);
			}
			w.emitEmptyLine();
			for (ClassBody cb : cpi.listclassbody_1) {
				cb.accept(this, w);
			}
			for (ClassBody cb : cpi.listclassbody_2) {
				cb.accept(this, w);
			}
			w.beginConstructor(DEFAULT_MODIFIERS, parameters, null);
			for (FormalPar param : cpi.listformalpar_) {
				FormalParameter p = (FormalParameter) param;
				w.emitStatement("this." + p.l_ + " = " + p.l_);
			}
			// emitThisActorRegistration(w);
			cpi.maybeblock_.accept(this, w);
			// emitDefaultRunMethodExecution(w, className);
			w.endConstructor();
			w.emitEmptyLine();
			// emitToStringMethod(w);
			w.endType();
			this.classes.pop();
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(AnnStatement p, JavaWriter w) {
		p.stm_.accept(this, w);
		return prog;
	}

	@Override
	public Prog visit(Annotation p, JavaWriter w) {
		p.ann__.accept(this, w);
		return prog;
	}

	@Override
	public Prog visit(AnnDeclaration p, JavaWriter w) {
		for (Ann a : p.listann_) {
			a.accept(this, w);
		}
		p.decl_.accept(this, w);
		return prog;
	}

	@Override
	public Prog visit(AnnNoType p, JavaWriter arg) {
		return prog;
	}

	@Override
	public Prog visit(AnnWithType p, JavaWriter w) {
		visitJavaAnnDecl(p);
		return prog;
	}

	public void visitStatementsBlock(ListAnnStm statements, JavaWriter w) {
		{
			StringWriter awaitsw = new StringWriter();
			JavaWriter awaitw = new JavaWriter(awaitsw);
			Boolean awaitEncountered = false;
			SAwait sa = null;
			StringWriter guardsw = new StringWriter();
			JavaWriter guardw = new JavaWriter(guardsw);

			for (AnnStm stm : statements) {
				AnnStatement as = (AnnStatement) stm;
				if ((as.stm_ instanceof SAwait) == false) {
					if (!awaitEncountered)
						stm.accept(this, w);
					else
						try {
							awaitw.beginControlFlow("");
							stm.accept(this, awaitw);
							awaitw.endControlFlow();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				} else {
					sa = (SAwait) as.stm_;
					awaitEncountered = true;
					sa.accept(this, guardw);
				}
			}
			if (awaitEncountered) {
				try {
					String continuationName = "continuation_" + awaitsw.toString().length();
					String contStatement = generateMessageStatement(continuationName, currentMethod.type(),
							awaitsw.toString());
					w.emitStatement("%s", contStatement);

					if ((sa.awaitguard_ instanceof GAnd) || (sa.awaitguard_ instanceof GExp)) {
						w.emitStatement("%s", guardsw.toString());
						w.emitStatement("%s await(%s,%s)", currentMethod.type() == null ? "" : "return",
								"supplier_" + Math.abs(sa.hashCode()), continuationName);

					} else {
						w.emitStatement("%s await(%s.f,%s)", currentMethod.type() == null ? "" : "return",
								guardsw.toString(), continuationName);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}

	}

	@Override
	public Prog visit(JustBlock jb, JavaWriter w) {
		visitStatementsBlock(jb.listannstm_, w);
		return prog;
	}

	@Override
	public Prog visit(NoBlock nb, JavaWriter w) {
		return prog;
	}

	@Override
	public Prog visit(MethSignature ms, JavaWriter w) {
		try {
			String returnType = getTypeName(ms.t_);
			String name = ms.l_.equals(METHOD_GET) ? "get" : ms.l_;
			List<String> parameters = new ArrayList<>();
			List<String> parameterTypes = new ArrayList<>();
			for (FormalPar param : ms.listformalpar_) {
				FormalParameter p = (FormalParameter) param;
				String paramType = getTypeName(p.t_);
				parameters.add(paramType);
				parameters.add(p.l_);
				parameterTypes.add(paramType);
			}
			createMethodDefinition(returnType, name, parameterTypes);
			w.beginMethod(returnType, name, DEFAULT_MODIFIERS, parameters, Collections.emptyList());
			w.endMethod();
			w.emitEmptyLine();
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(FieldClassBody p, JavaWriter w) {
		try {
			String fieldType = getTypeName(p.t_);
			String fieldName = p.l_;

			emitField(w, fieldType, fieldName, null, false);
			createVarDefinition(fieldName, fieldType);
			verifyJavaStatic(fieldType, fieldName);
			w.emitEmptyLine();
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public Prog visit(FieldAssignClassBody p, JavaWriter w) {
		try {
			String fieldType = getTypeName(p.t_);
			String fieldName = p.l_;
			StringWriter auxsw = new StringWriter();
			JavaWriter auxw = new JavaWriter(auxsw);
			p.pureexp_.accept(this, auxw);
			emitField(w, fieldType, fieldName, auxsw.toString(), false);
			createVarDefinition(fieldName, fieldType);
			w.emitEmptyLine();
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(MethClassBody mcb, JavaWriter w) {
		try {
			String returnType = getTypeName(mcb.t_);
			String name = mcb.l_.equals(METHOD_GET) ? "get" : mcb.l_;
			List<String> parameters = new ArrayList<>();
			List<String> parameterTypes = new ArrayList<>();
			for (FormalPar param : mcb.listformalpar_) {
				FormalParameter p = (FormalParameter) param;
				String paramType = getTypeName(p.t_);
				parameters.add(paramType);
				parameters.add(p.l_);
				parameterTypes.add(paramType);
				createVarDefinition(p.l_, paramType);
			}
			w.beginMethod(returnType, name, DEFAULT_MODIFIERS, parameters, Collections.emptyList());
			createMethodDefinition(returnType, name, parameterTypes);

			visitStatementsBlock(mcb.listannstm_, w);

			w.endMethod();
			w.emitEmptyLine();
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(SBlock b, JavaWriter w) {
		visitStatementsBlock(b.listannstm_, w);
		return prog;

	}

	@Override
	public Prog visit(SAwait await, JavaWriter w) {
		await.awaitguard_.accept(this, w);
		return prog;
	}

	@Override
	public Prog visit(GFutField p, JavaWriter w) {
		try {
			w.emit(LITERAL_THIS + "." + p.l_);
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
		return prog;
	}

	@Override
	public Prog visit(GExp p, JavaWriter w) {
		try {
			StringWriter suppliersw = new StringWriter();
			JavaWriter supplierw = new JavaWriter(suppliersw);

			StringBuilder supplierName = new StringBuilder();
			supplierName.append("supplier_" + Math.abs(p.hashCode()));
			p.pureexp_.accept(this, supplierw);
			w.beginControlFlow("Supplier<Boolean> %s = new Supplier<Boolean>()", supplierName);
			w.beginMethod("Boolean", "get", DEFAULT_MODIFIERS);
			w.emitStatement("return %s", suppliersw.toString());
			w.endMethod();
			w.endControlFlow();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prog;
	}

	@Override
	public Prog visit(GAnd p, JavaWriter w) {
		try {
			StringWriter suppliersw = new StringWriter();
			JavaWriter supplierw = new JavaWriter(suppliersw);

			StringWriter suppliersw2 = new StringWriter();
			JavaWriter supplierw2 = new JavaWriter(suppliersw);

			StringBuilder supplierName = new StringBuilder();
			supplierName.append("supplier_" + Math.abs(p.hashCode()));
			p.awaitguard_1.accept(this, supplierw);
			p.awaitguard_2.accept(this, supplierw2);
			w.beginControlFlow("Supplier<Boolean> %s = new Supplier<Boolean>()", supplierName);
			w.beginMethod("Boolean", "get", DEFAULT_MODIFIERS);
			w.emitStatement("return %s && %s", suppliersw.toString(), suppliersw2.toString());
			w.endMethod();
			w.endControlFlow();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prog;
	}

	@Override
	public Prog visit(GFut vg, JavaWriter w) {
		try {
			w.emit(vg.l_);
			return prog;
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	public Prog visit(SWhile sw, JavaWriter w) {
		try {
			StringWriter auxsw = new StringWriter();
			JavaWriter auxw = new JavaWriter(auxsw);
			sw.pureexp_.accept(this, auxw);
			w.beginControlFlow("while (" + auxsw.toString() + ")");
			sw.annstm_.accept(this, w);
			w.endControlFlow();
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(SIf sif, JavaWriter w) {
		try {
			StringWriter auxsw = new StringWriter();
			JavaWriter auxw = new JavaWriter(auxsw);
			sif.pureexp_.accept(this, auxw);
			w.beginControlFlow("if (" + auxsw.toString() + ")");
			sif.stm_.accept(this, w);
			w.endControlFlow();
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(SIfElse se, JavaWriter w) {
		try {
			StringWriter auxsw = new StringWriter();
			JavaWriter auxw = new JavaWriter(auxsw);
			se.pureexp_.accept(this, auxw);
			w.beginControlFlow("if (" + auxsw.toString() + ")");
			se.stm_1.accept(this, w);
			w.endControlFlow();
			w.beginControlFlow("else");
			se.stm_2.accept(this, w);
			w.endControlFlow();
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public Prog visit(SDec p, JavaWriter w) {
		try {
			String fieldType = getTypeName(p.t_);
			String fieldName = p.l_;
			emitField(w, fieldType, fieldName, null, false);
			createVarDefinition(fieldName, fieldType);
			verifyJavaStatic(fieldType, fieldName);
			w.emitEmptyLine();
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(SFieldAss fa, JavaWriter w) {
		try {
			Exp exp = fa.exp_;
			String fieldName = LITERAL_THIS + "." + fa.l_;
			String fieldType = findVariableType(fieldName);
			if (exp instanceof ExpE == false) {
				visitStatementAssignmentExp(exp, fieldName, null, w);
			} else if (exp instanceof ExpE) {
				ExpE expe = (ExpE) exp;
				EffExp effExp = expe.effexp_;
				if (effExp instanceof AsyncMethCall) {
					AsyncMethCall amc = (AsyncMethCall) effExp;
					visitAsyncMethodCall(amc, fieldType, fieldName, false, w);
				} else if (effExp instanceof SyncMethCall) {
					SyncMethCall smc = (SyncMethCall) effExp;
					visitSyncMethodCall_Sync(smc, fieldType, fieldName, true, w);
				} else if (effExp instanceof ThisSyncMethCall) {
					ThisSyncMethCall tsmc = (ThisSyncMethCall) effExp;
					SyncMethCall smc = new SyncMethCall(new ELit(new LThis()), tsmc.l_, tsmc.listpureexp_);
					visitSyncMethodCall_Sync(smc, fieldType, fieldName, true, w);
				} else {
					visitStatementAssignmentExp(exp, fieldName, fieldType, w);
				}
			}
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(SAss ss, JavaWriter w) {
		try {
			Exp exp = ss.exp_;
			String varName = ss.l_;
			String varType = findVariableType(varName);
			if (exp instanceof ExpE == false) {
				visitStatementAssignmentExp(exp, varName, null, w);
			} else if (exp instanceof ExpE) {
				ExpE expe = (ExpE) exp;
				EffExp effExp = expe.effexp_;
				if (effExp instanceof AsyncMethCall) {
					AsyncMethCall amc = (AsyncMethCall) effExp;
					visitAsyncMethodCall(amc, varType, varName, false, w);
				} else if (effExp instanceof SyncMethCall) {
					SyncMethCall smc = (SyncMethCall) effExp;
					visitSyncMethodCall_Sync(smc, varType, varName, true, w);
				} else if (effExp instanceof ThisSyncMethCall) {
					ThisSyncMethCall tsmc = (ThisSyncMethCall) effExp;
					SyncMethCall smc = new SyncMethCall(new ELit(new LThis()), tsmc.l_, tsmc.listpureexp_);
					visitSyncMethodCall_Sync(smc, varType, varName, true, w);
				} else {
					visitStatementAssignmentExp(exp, varName, null, w);
				}
			}
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(SDecAss p, JavaWriter w) {
		try {
			String varType = getTypeName(p.t_);
			varType = VOID_PRIMITIVE_NAME.equals(varType) ? Object.class.getSimpleName() : varType;
			String varName = p.l_;
			createVarDefinition(varName, varType);
			Exp exp = p.exp_;
			if (exp instanceof ExpE == false) {
				visitStatementAssignmentExp(exp, varName, varType, w);
			} else {
				ExpE expe = (ExpE) exp;
				EffExp effExp = expe.effexp_;
				if (effExp instanceof AsyncMethCall) {
					AsyncMethCall amc = (AsyncMethCall) effExp;
					visitAsyncMethodCall(amc, varType, varName, true, w);
				} else if (effExp instanceof SyncMethCall) {
					SyncMethCall smc = (SyncMethCall) effExp;
					visitSyncMethodCall_Sync(smc, varType, varName, false, w);
				} else if (effExp instanceof ThisSyncMethCall) {
					ThisSyncMethCall tsmc = (ThisSyncMethCall) effExp;
					SyncMethCall smc = new SyncMethCall(new ELit(new LThis()), tsmc.l_, tsmc.listpureexp_);
					visitSyncMethodCall_Sync(smc, varType, varName, false, w);
				} else {
					visitStatementAssignmentExp(exp, varName, varType, w);
				}
			}
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(SReturn r, JavaWriter w) {
		try {
			StringWriter auxsw = new StringWriter();
			JavaWriter auxw = new JavaWriter(auxsw);
			r.exp_.accept(this, auxw);
			w.emitStatement("return " + auxsw.toString());
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(SSuspend ss, JavaWriter w) {
		try {
			w.emitSingleLineComment("suspend;");
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(SSkip sk, JavaWriter w) {
		try {
			w.emitSingleLineComment("skip;");
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(SExp p, JavaWriter w) {
		try {
			Exp exp = p.exp_;
			StringWriter auxsw = new StringWriter();
			JavaWriter auxjw = new JavaWriter(auxsw);
			exp.accept(this, auxjw);
			w.emit(auxsw.toString(), true);
			if (exp instanceof ExpE) {
				ExpE expE = (ExpE) exp;
				EffExp effExp = expE.effexp_;
				if (effExp instanceof Get || effExp instanceof New) {
					// XXX Ideally fix the indentation
					w.emitStatementEnd();
				}
			} else if (exp instanceof ExpP) {
				if (((ExpP) exp).pureexp_ instanceof EFunCall) {
					w.emitStatementEnd();
				}
			}
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Prog visit(SThrow st, JavaWriter w) {
		try {
			StringWriter auxsw = new StringWriter();
			JavaWriter auxw = new JavaWriter(auxsw);
			st.pureexp_.accept(this, auxw);
			String newEx = auxsw.toString();
			if (LITERAL_NULL.equals(newEx)) {
				w.emitSingleLineComment("XXX jabsc: Cannot throw 'null'");
				w.emitSingleLineComment("throw %s", newEx);
			} else {
				w.emitStatement("throw " + refineFunctionalPureExpression(newEx));
			}
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(STryCatchFinally stcf, JavaWriter w) {
		try {
			w.beginControlFlow("try");
			stcf.annstm_.accept(this, w);
			w.endControlFlow();
			String exceptionVarName = "exception" + RANDOM.nextInt(100);
			w.beginControlFlow(String.format("catch(Exception %s)", exceptionVarName));
			for (SCaseBranch c : stcf.listscasebranch_) {
				SCaseB cb = (SCaseB) c;
				Pattern left = cb.pattern_;
				String exceptionTypeName = findExceptionTypeName(left);
				if (exceptionTypeName != null) {
					w.beginControlFlow("if (%s instanceof %s)", exceptionVarName, exceptionTypeName);
				} else {
					w.beginControlFlow("if (%s == null)", exceptionVarName);
				}
				cb.annstm_.accept(this, w);
				w.endControlFlow();
			}
			w.endControlFlow();
			stcf.maybefinally_.accept(this, w);
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(SPrintln p, JavaWriter w) {
		try {
			StringWriter auxsw = new StringWriter();
			JavaWriter auxw = new JavaWriter(auxsw);
			p.pureexp_.accept(this, auxw);
			w.emitStatement("System.out.println( " + auxsw.toString() + ")");
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	/*
	 * @Override public Prog visit(SCase p, JavaWriter w) { try { StringWriter
	 * auxsw = new StringWriter(); JavaWriter auxw = new JavaWriter(auxsw);
	 * p.pureexp_.accept(this, auxw); int i =0; for (CatchBranch cb :
	 * p.listcatchbranch_) { StringWriter auxswcb = new StringWriter();
	 * JavaWriter auxwcb = new JavaWriter(auxswcb); CatchBranc cs =
	 * (CatchBranc)cb; Pattern instance = cs.pattern_;
	 * 
	 * if (instance instanceof PParamConstr) { PParamConstr ppc = (PParamConstr)
	 * instance; ListPattern parameters = ppc.listpattern_; List<String> parKeys
	 * = new ArrayList<String>(); for (Pattern pattern : parameters) {
	 * StringWriter auxswp = new StringWriter(); JavaWriter auxwp = new
	 * JavaWriter(auxswp); pattern.accept(this, auxwp);
	 * javaTypeTranslator.registerAbstractType(auxswp.toString(), "((" +
	 * findVariableType(auxsw.toString())+"."+ppc.u_ + ")"+auxsw+")."+auxswp);
	 * parKeys.add(auxswp.toString()); } w.beginControlFlow((i>0)?"else ":""+
	 * "if(%s instanceof %s)", auxsw.toString(),
	 * findVariableType(auxsw.toString())+"."+ppc.u_); cs.stm_.accept(this, w);
	 * w.endControlFlow(); for (String key : parKeys) {
	 * javaTypeTranslator.deRegisterAbstractType(key); }
	 * 
	 * } if (instance instanceof PUnderscore){ w.beginControlFlow("else");
	 * cs.stm_.accept(this, w); w.endControlFlow(); }
	 * 
	 * else{ instance.accept(this, auxwcb); w.beginControlFlow((i>0)?"else":""+
	 * "if(%s instanceof %s)", auxsw.toString(),
	 * findVariableType(auxsw.toString())+"."+auxswcb.toString());
	 * cs.stm_.accept(this, w); w.endControlFlow(); } i++; }
	 * //w.endControlFlow(); return prog; } catch (IOException e) { throw new
	 * RuntimeException(e); } }
	 */

	@Override
	public Prog visit(ExpE ee, JavaWriter w) {
		ee.effexp_.accept(this, w);
		return prog;
	}

	@Override
	public Prog visit(ExpP ep, JavaWriter w) {
		ep.pureexp_.accept(this, w);
		return prog;
	}

	@Override
	public Prog visit(ELit elit, JavaWriter w) {
		elit.literal_.accept(this, w);
		return prog;
	}

	@Override
	public Prog visit(EAdd e, JavaWriter w) {
		try {
			w.beginExpressionGroup();
			e.pureexp_1.accept(this, w);
			w.emit(" + ");
			e.pureexp_2.accept(this, w);
			w.endExpressionGroup();
			return prog;
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	public Prog visit(ESub e, JavaWriter w) {
		try {
			w.beginExpressionGroup();
			e.pureexp_1.accept(this, w);
			w.emit(" - ");
			e.pureexp_2.accept(this, w);
			w.endExpressionGroup();
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
		return prog;
	}

	@Override
	public Prog visit(EMul e, JavaWriter w) {
		try {
			w.beginExpressionGroup();
			e.pureexp_1.accept(this, w);
			w.emit(" * ");
			e.pureexp_2.accept(this, w);
			w.endExpressionGroup();
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
		return prog;
	}

	@Override
	public Prog visit(EDiv e, JavaWriter w) {
		try {
			w.beginExpressionGroup();
			e.pureexp_1.accept(this, w);
			w.emit(" / ");
			e.pureexp_2.accept(this, w);
			w.endExpressionGroup();
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
		return prog;
	}

	@Override
	public Prog visit(EMod e, JavaWriter w) {
		try {
			w.beginExpressionGroup();
			e.pureexp_1.accept(this, w);
			w.emit(" %% ");
			e.pureexp_2.accept(this, w);
			w.endExpressionGroup();
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
		return prog;
	}

	@Override
	public Prog visit(EGe e, JavaWriter w) {
		try {
			w.beginExpressionGroup();
			e.pureexp_1.accept(this, w);
			w.emit(" >= ");
			e.pureexp_2.accept(this, w);
			w.endExpressionGroup();
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
		return prog;
	}

	@Override
	public Prog visit(EGt e, JavaWriter w) {
		try {
			w.beginExpressionGroup();
			e.pureexp_1.accept(this, w);
			w.emit(" > ");
			e.pureexp_2.accept(this, w);
			w.endExpressionGroup();
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
		return prog;
	}

	@Override
	public Prog visit(ELe e, JavaWriter w) {
		try {
			w.beginExpressionGroup();
			e.pureexp_1.accept(this, w);
			w.emit(" <= ");
			e.pureexp_2.accept(this, w);
			w.endExpressionGroup();
			return prog;
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	public Prog visit(ELt lt, JavaWriter w) {
		try {
			w.beginExpressionGroup();
			lt.pureexp_1.accept(this, w);
			w.emit(" < ");
			lt.pureexp_2.accept(this, w);
			w.endExpressionGroup();
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
		return prog;
	}

	@Override
	public Prog visit(EAnd e, JavaWriter w) {
		try {
			w.beginExpressionGroup();
			e.pureexp_1.accept(this, w);
			w.emit(" && ");
			e.pureexp_2.accept(this, w);
			w.endExpressionGroup();
			return prog;
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	public Prog visit(EOr e, JavaWriter w) {
		try {
			w.beginExpressionGroup();
			e.pureexp_1.accept(this, w);
			w.emit(" || ");
			e.pureexp_2.accept(this, w);
			w.endExpressionGroup();
			return prog;
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	public Prog visit(ENeq e, JavaWriter w) {
		try {
			w.beginExpressionGroup();
			StringWriter auxsw = new StringWriter();
			e.pureexp_1.accept(this, new JavaWriter(auxsw));
			String firstArg = auxsw.toString();
			auxsw = new StringWriter();
			e.pureexp_2.accept(this, new JavaWriter(auxsw));
			String secondArg = auxsw.toString();
			w.emit(String.format("!Objects.equals(%s, %s)", firstArg, secondArg));
			w.endExpressionGroup();
			return prog;
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	public Prog visit(EIntNeg in, JavaWriter w) {
		try {
			w.beginExpressionGroup();
			w.emit("-");
			in.pureexp_.accept(this, w);
			w.endExpressionGroup();
			return prog;
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	public Prog visit(ELogNeg ln, JavaWriter w) {
		try {
			w.beginExpressionGroup();
			w.emit("!");
			ln.pureexp_.accept(this, w);
			w.endExpressionGroup();
			return prog;
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	public Prog visit(EEq e, JavaWriter w) {
		try {
			w.beginExpressionGroup();
			StringWriter auxsw = new StringWriter();
			e.pureexp_1.accept(this, new JavaWriter(auxsw));
			String firstArg = auxsw.toString();
			auxsw = new StringWriter();
			e.pureexp_2.accept(this, new JavaWriter(auxsw));
			String secondArg = auxsw.toString();
			w.emit(String.format("Objects.equals(%s, %s)", firstArg, secondArg));
			w.endExpressionGroup();
			return prog;
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	public Prog visit(EVar v, JavaWriter w) {
		try {

			w.emit(translate(v.l_));
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
		return prog;

	}

	@Override
	public Prog visit(EField t, JavaWriter w) {
		try {
			w.emit("this." + t.l_);
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
		return prog;
	}

	@Override
	public Prog visit(New n, JavaWriter w) {
		try {
			List<String> parameters = new ArrayList<>();
			for (PureExp par : n.listpureexp_) {
				StringWriter parSW = new StringWriter();
				JavaWriter parameterWriter = new JavaWriter(parSW);
				par.accept(this, parameterWriter);
				parameters.add(parSW.toString());
			}
			String parametersString = String.join(COMMA_SPACE, parameters);
			String oType = getQTypeName(n.qu_, false);
			w.emit("new " + getRefinedClassName(oType) + "(" + parametersString + ")");
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(Get g, JavaWriter w) {
		try {
			StringWriter auxsw = new StringWriter();
			JavaWriter auxw = new JavaWriter(auxsw);
			g.pureexp_.accept(this, auxw);
			w.emit(auxsw.toString() + ".f.get()");
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(AsyncMethCall amc, JavaWriter w) {
		try {
			visitAsyncMethodCall(amc, null, null, false, w);
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(SyncMethCall smc, JavaWriter w) {
		try {
			visitSyncMethodCall_Sync(smc, null, null, false, w);
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(ThisSyncMethCall p, JavaWriter w) {
		try {
			SyncMethCall smc = new SyncMethCall(new ELit(new LThis()), p.l_, p.listpureexp_);
			visitSyncMethodCall_Sync(smc, null, null, false, w);
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(SCaseB cb, JavaWriter w) {
		try {
			StringWriter auxsw = new StringWriter();
			JavaWriter auxw = new JavaWriter(auxsw);
			cb.pattern_.accept(this, auxw);
			String stm = auxsw.toString();
			w.beginControlFlow("catch(%s)", refineFunctionalPureExpression(stm));
			cb.annstm_.accept(this, w);
			w.endControlFlow();
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(JustFinally jf, JavaWriter w) {
		try {
			w.beginControlFlow("finally");
			jf.annstm_.accept(this, w);
			w.endControlFlow();
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(NoFinally p, JavaWriter arg) {
		// XXX ?
		return prog;
	}

	@Override
	public Prog visit(LInt i, JavaWriter w) {
		try {
			w.emit(Long.toString(i.integer_));
			return prog;
		} catch (IOException x) {
			throw new RuntimeException(x);
		}

	}

	@Override
	public Prog visit(LNull n, JavaWriter w) {
		try {
			w.emit(LITERAL_NULL);
			return prog;
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	public Prog visit(LStr s, JavaWriter w) {
		try {

			w.emit("\"" + s.string_ + "\"");
			return prog;
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	public Prog visit(LThis t, JavaWriter w) {
		try {
			w.emit(LITERAL_THIS);
			return prog;
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
	}

	/*
	 * @Override public Prog visit(LFalse lfalse, JavaWriter w) { try {
	 * w.emit("Boolean.FALSE"); return prog; } catch (IOException e) { throw new
	 * RuntimeException(e); } }
	 * 
	 * @Override public Prog visit(LTrue ltrue, JavaWriter w) { try {
	 * w.emit("Boolean.TRUE"); return prog; } catch (IOException e) { throw new
	 * RuntimeException(e); } }
	 */
	/*
	 * @Override public Prog visit(AnyIden p, JavaWriter arg) {
	 * logNotImplemented("#visit(%s)", p); return prog; }
	 * 
	 * @Override public Prog visit(AnyTyIden p, JavaWriter arg) {
	 * logNotImplemented("#visit(%s)", p); return prog; }
	 */
	@Override
	public Prog visit(AnyExport p, JavaWriter arg) {
		logNotSupported("#visit(%s): %s", p, p.listqa_);
		return prog;
	}

	@Override
	public Prog visit(AnyFromExport p, JavaWriter arg) {
		logNotSupported("#visit(%s): %s", p, p.listqa_);
		return prog;
	}

	@Override
	public Prog visit(StarExport p, JavaWriter arg) {
		logNotSupported("#visit(%s)", p);
		return prog;
	}

	@Override
	public Prog visit(StarFromExport p, JavaWriter arg) {
		logNotSupported("#visit(%s): %s", p, p.qu_);
		return prog;
	}

	@Override
	public Prog visit(AnyFromImport p, JavaWriter arg) {
		logNotSupported("#visit(%s): %s", p, p.listqa_);
		return prog;
	}

	@Override
	public Prog visit(YesForeign fi, JavaWriter arg) {
		return prog;
	}

	@Override
	public Prog visit(NoForeign ni, JavaWriter arg) {
		return prog;
	}

	@Override
	public Prog visit(TInfer p, JavaWriter arg) {
		logNotImplemented("#visit(%s)", p);
		return prog;
	}

	@Override
	public Prog visit(TSimple t, JavaWriter w) {
		try {
			w.emit(toString(t));
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(TPoly t, JavaWriter w) {
		try {
			w.emit(toString(t));
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(DType adt, JavaWriter arg) {
		String adtName = adt.u_;
		String typeName = getTypeName(adt.t_);
		this.javaTypeTranslator.registerAbstractType(adtName, typeName);
		return prog;
	}

	@Override
	public Prog visit(DTypePoly adt, JavaWriter arg) {
		String adtName = adt.u_;
		String typeName = getTypeName(adt.t_);
		this.javaTypeTranslator.registerAbstractType(adtName, typeName);
		logNotSupported("Parametric T Declaration not supported: %s", adt.listu_);
		return prog;
	}

	@Override
	public Prog visit(DException ed, JavaWriter w) {
		try {
			w.emitEmptyLine();
			ConstrIdent ex = ed.constrident_;
			String className = getExceptionClassName(ex);
			this.exceptionDeclaraions.add(className);
			Collection<String> implementingCaseInterface = Collections.emptyList();
			beginElementKind(w, ElementKind.CLASS, className, EnumSet.of(Modifier.PUBLIC),
					RuntimeException.class.getSimpleName(), implementingCaseInterface, false);
			w.emitEmptyLine();
			emitSerialVersionUid(w);
			List<Entry<String, String>> fields = Collections.emptyList();
			if (ex instanceof SinglConstrIdent) {
				// Default super constructor works
			} else if (ex instanceof ParamConstrIdent) {
				ParamConstrIdent cstr = (ParamConstrIdent) ex;
				fields = visitConstrType(cstr.listconstrtype_, w, true);
				List<String> fieldNames = fields.stream().map(e -> e.getValue()).collect(Collectors.toList());
				emitEqualsMethod(className, fieldNames, w);
			}
			w.endType();
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(DData dd, JavaWriter w) {
		try {
			w.emitEmptyLine();
			String parentDataInterface = dd.u_;
			// Define parent 'data' holder interface
			beginElementKind(w, ElementKind.INTERFACE, parentDataInterface, EnumSet.of(Modifier.PUBLIC), null, null,
					false);
			w.emitEmptyLine();
			w.endType();
			this.dataDeclarations.put(dd.u_, parentDataInterface);
			// Each data declaration as an implementing class
			ListConstrIdent lci = dd.listconstrident_;
			visitDataDeclarationConstructors(parentDataInterface, lci, Collections.emptyList());
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(DDataPoly dpd, JavaWriter w) {
		try {
			w.emitEmptyLine();
			String parentDataInterface = dpd.u_;
			List<String> genericParams = dpd.listu_;
			// The parent data decl as an interface
			beginElementKind(w, ElementKind.INTERFACE,
					String.format("%s<%s>", parentDataInterface, String.join(COMMA_SPACE, genericParams)),
					EnumSet.of(Modifier.PUBLIC), null, null, false);
			w.emitEmptyLine();
			w.endType();
			this.dataDeclarations.put(parentDataInterface, parentDataInterface);
			visitDataDeclarationConstructors(parentDataInterface, dpd.listconstrident_, genericParams);
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(DFun f, JavaWriter w) {
		try {
			String methodName = f.l_;
			String methodType = getTypeName(f.t_);
			List<String> parameters = new ArrayList<>();
			for (FormalPar param : f.listformalpar_) {
				FormalParameter parameter = (FormalParameter) param;
				parameters.add(getTypeName(parameter.t_));
				parameters.add(parameter.l_);
			}
			Set<Modifier> modifiers = Sets.newHashSet(Modifier.PUBLIC, Modifier.STATIC);
			w.beginMethod(methodType, methodName, modifiers, parameters.toArray(new String[0]));
			FunBody fbody = f.funbody_;
			if (fbody instanceof BuiltinFunBody) {
				logNotImplemented("builtin function body: %s %s", methodType, methodName);
			} else if (fbody instanceof NormalFunBody) {
				NormalFunBody nfb = (NormalFunBody) fbody;
				PureExp pe = nfb.pureexp_;
				if (pe instanceof ECase) {
					String caseStm = visitCase((ECase) pe, methodType);
					w.emitStatement("return %s", caseStm);
				} else {
					StringWriter sw = new StringWriter();
					JavaWriter auxjw = new JavaWriter(sw);
					pe.accept(this, auxjw);
					String stm = sw.toString();
					w.emitStatement("return %s", stm);
				}
			}
			w.endMethod();
			w.emitEmptyLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return prog;
	}

	@Override
	public Prog visit(DFunPoly fpd, JavaWriter w) {
		try {
			String methodName = fpd.l_;
			String methodType = getTypeName(fpd.t_);
			List<String> parameters = new ArrayList<>();
			for (FormalPar param : fpd.listformalpar_) {
				FormalParameter parameter = (FormalParameter) param;
				parameters.add(getTypeName(parameter.t_));
				parameters.add(parameter.l_);
			}
			List<String> genericParameters = fpd.listu_.stream().collect(Collectors.toList());
			Set<Modifier> modifiers = Sets.newHashSet(Modifier.PUBLIC, Modifier.STATIC);
			w.beginMethod("<" + String.join(COMMA_SPACE, genericParameters) + "> " + methodType, methodName, modifiers,
					parameters.toArray(new String[0]));
			FunBody fbody = fpd.funbody_;
			if (fbody instanceof BuiltinFunBody) {
				logNotImplemented("builtin function body: %s %s", methodType, methodName);
			} else if (fbody instanceof NormalFunBody) {
				NormalFunBody nfb = (NormalFunBody) fbody;
				PureExp pe = nfb.pureexp_;
				if (pe instanceof ECase) {
					String caseStm = visitCase((ECase) pe, methodType);
					w.emitStatement("return %s", caseStm);
				} else {
					StringWriter sw = new StringWriter();
					JavaWriter auxjw = new JavaWriter(sw);
					pe.accept(this, auxjw);
					String stm = sw.toString();
					w.emitStatement("return %s", stm);
				}
			}
			w.endMethod();
			w.emitEmptyLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return prog;
	}

	/**
	 * @see #visit(DData, JavaWriter)
	 * @see #visit(DDataPoly, JavaWriter)
	 */
	@Override
	public Prog visit(SinglConstrIdent p, JavaWriter arg) {
		return prog;
	}

	/**
	 * @see #visit(DData, JavaWriter)
	 * @see #visit(DDataPoly, JavaWriter)
	 */
	@Override
	public Prog visit(ParamConstrIdent p, JavaWriter arg) {
		return prog;
	}

	/**
	 * @see #visit(DData, JavaWriter)
	 * @see #visit(DDataPoly, JavaWriter)
	 */
	@Override
	public Prog visit(EmptyConstrType p, JavaWriter arg) {
		return prog;
	}

	/**
	 * @see #visit(DData, JavaWriter)
	 * @see #visit(DDataPoly, JavaWriter)
	 */
	@Override
	public Prog visit(RecordConstrType p, JavaWriter arg) {
		return prog;
	}

	@Override
	public Prog visit(BuiltinFunBody p, JavaWriter arg) {
		logNotImplemented("#visit(%s)", p);
		return prog;
	}

	@Override
	public Prog visit(NormalFunBody body, JavaWriter w) {
		try {
			w.emit("return ", true);
			body.pureexp_.accept(this, w);
			w.emit(";");
			w.emitEmptyLine();
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(FormalParameter p, JavaWriter arg) {
		logNotImplemented("#visit(%s)", p);
		return prog;
	}

	@Override
	public Prog visit(SAssert p, JavaWriter arg) {
		logNotImplemented("#visit(%s)", p);
		return prog;
	}

	@Override
	public Prog visit(SPrint p, JavaWriter w) {
		try {
			StringWriter sw = new StringWriter();
			JavaWriter auxw = new JavaWriter(sw);
			p.pureexp_.accept(this, auxw);
			w.emitStatement("println(%s)", sw.toString());
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(ELet p, JavaWriter arg) {
		logNotImplemented("#visit(%s)", p);
		return prog;
	}

	@Override
	public Prog visit(EIf p, JavaWriter w) {
		try {
			StringWriter sw = new StringWriter();
			p.pureexp_1.accept(this, new JavaWriter(sw));
			String condition = sw.toString();
			sw = new StringWriter();
			p.pureexp_2.accept(this, new JavaWriter(sw));
			String left = sw.toString();
			sw = new StringWriter();
			p.pureexp_3.accept(this, new JavaWriter(sw));
			String right = sw.toString();
			w.emit(String.format("%s ? %s : %s", condition, left, right));
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(ECase p, JavaWriter w) {
		try {
			String result = visitCase(p, null);
			w.emitEmptyLine();
			w.emit(result, true);
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(EFunCall f, JavaWriter w) {
		try {
			w.emit(getQTypeName(f.ql_, false));
			w.emit("(");
			List<String> params = getParameters(f.listpureexp_);
			w.emit(String.join(COMMA_SPACE, params));
			w.emit(")");
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * @Override public Prog visit(EQualFunCall f, JavaWriter w) { try { String
	 * functionName = f.l_; ListPureExp params = f.listpureexp_; List<String>
	 * parameters = getParameters(params); w.emit(functionName); w.emit("(");
	 * w.emit(String.join(COMMA_SPACE, parameters)); w.emit(")"); return prog; }
	 * catch (IOException e) { throw new RuntimeException(e); } }
	 */

	@Override
	public Prog visit(ENaryFunCall fnc, JavaWriter w) {
		try {
			String identifier = getQTypeName(fnc.ql_, false);
			List<String> exps = new ArrayList<>();
			for (PureExp pexp : fnc.listpureexp_) {
				StringWriter auxsw = new StringWriter();
				pexp.accept(this, new JavaWriter(auxsw));
				exps.add(auxsw.toString());
			}
			w.emit(identifier + String.format("(%s)", String.join(COMMA_SPACE, exps)));
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(ESinglConstr cons, JavaWriter w) {
		try {
			String type = getQTypeName(cons.qu_, false);
			final boolean isException = this.exceptionDeclaraions.contains(type);
			String resolvedType = javaTypeTranslator.translateFunctionalType(type);
			final boolean isData = resolvedType != null && this.dataDeclarations.containsKey(type);
			if (isException) {
				w.emit("new " + type + "()");
			} else {
				String refinedType = getRefindDataDeclName(resolvedType);
				w.emit(refinedType);
				if (isData) {
					w.emit(".INSTANCE");
				}
			}
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(EParamConstr cons, JavaWriter w) {
		try {
			String functionName = getQTypeName(cons.qu_, false);
			ListPureExp params = cons.listpureexp_;
			List<String> parameters = getParameters(params);
			String result = String.format("%s(%s)", functionName, String.join(COMMA_SPACE, parameters));
			w.emit(refineFunctionalPureExpression(result));
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(ECaseB p, JavaWriter arg) {
		logNotImplemented("#visit(%s)", p);
		return prog;
	}

	@Override
	public Prog visit(PVar p, JavaWriter w) {
		try {
			w.emit(p.l_);
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(PLit p, JavaWriter w) {
		p.literal_.accept(this, w);
		return prog;
	}

	@Override
	public Prog visit(PSinglConstr p, JavaWriter w) {
		try {
			w.emit(getRefindDataDeclName(getQTypeName(p.qu_, false)));
			if (this.dataDeclarations.containsKey(p.qu_)) {
				w.emit(".INSTANCE");
			}
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(PParamConstr p, JavaWriter w) {
		try {
			List<String> parameters = new ArrayList<>();
			for (Pattern pattern : p.listpattern_) {
				StringWriter sw = new StringWriter();
				pattern.accept(this, new JavaWriter(sw));
				parameters.add(sw.toString());
			}
			String params = String.join(COMMA_SPACE, parameters);
			w.emit("new " + p.qu_ + "(" + params + ")");
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(PWildCard p, JavaWriter w) {
		try {
			w.emit("any()");
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(LThisDC p, JavaWriter arg) {
		logNotImplemented("#visit(%s)", p);
		return prog;
	}

	@Override
	public Prog visit(NewLocal p, JavaWriter w) {
		logWarn("ABS `local` is ignored.");
		New np = new New(p.qu_, p.listpureexp_);
		visit(np, w);
		return prog;
	}

	/*
	 * @Override public Prog visit(Spawns p, JavaWriter w) { return prog; }
	 * 
	 * @Override public Prog visit(SimpleAnn p, JavaWriter arg) {
	 * logNotImplemented("#visit(%s)", p); return prog; }
	 * 
	 * @Override public Prog visit(NoAnn p, JavaWriter arg) { // TODO
	 * Auto-generated method stub return prog; }
	 * 
	 * @Override public Prog visit(MappedAnn p, JavaWriter arg) { // TODO
	 * Auto-generated method stub return prog; }
	 * 
	 * @Override public Prog visit(AnnDec p, JavaWriter arg) {
	 * logNotImplemented("#visit(%s)", p); return prog; }
	 * 
	 * @Override public Prog visit(AnnTyp p, JavaWriter arg) {
	 * logNotImplemented("#visit(%s)", p); return prog; }
	 */
	protected void visitFunctions(Module m, JavaWriter w) {
		try {
			JavaWriter jw = javaWriterSupplier.apply(FUNCTIONS_CLASS_NAME);
			jw.emitPackage(packageName);
			jw.emitStaticImports(DEFAULT_STATIC_IMPORTS);
			// jw.emitStaticImports(DEFAULT_STATIC_IMPORTS_PATTERNS);
			jw.emitEmptyLine();
			jw.emitImports(DEFAULT_IMPORTS);
			// jw.emitImports(DEFAULT_IMPORTS_PATTERNS);
			emitPackageLevelImport(jw);
			jw.emitEmptyLine();
			beginElementKind(jw, ElementKind.CLASS, FUNCTIONS_CLASS_NAME, DEFAULT_MODIFIERS, null, null, false);
			jw.emitEmptyLine();
			for (AnnDecl decl : elements.get(AbsElementType.FUNCTION)) {
				((AnnDeclaration) decl).decl_.accept(this, jw);
			}
			jw.endType();
			close(jw, w);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void visitMain(Mod m, JavaWriter w) {
		try {
			JavaWriter jw = javaWriterSupplier.apply(MAIN_CLASS_NAME);
			jw.emitPackage(packageName);
			// emitDefaultImports(jw);
			visitImports(m.listimport_, jw);
			jw.emitEmptyLine();

			final String identifier = MAIN_CLASS_NAME;
			EnumSet<Modifier> mainModifiers = EnumSet.of(Modifier.PUBLIC, Modifier.STATIC);
			beginElementKind(jw, ElementKind.CLASS, identifier, DEFAULT_MODIFIERS, null, null, true);
			jw.emitEmptyLine();

			jw.beginConstructor(EnumSet.of(Modifier.PUBLIC), Arrays.asList("String[]", "args"),
					Collections.singletonList("Exception"));
			// jw.emitStatement("Context context =
			// Configuration.newConfiguration().buildContext()");
			// emitThisActorRegistration(jw);
			jw.emitEmptyLine();
			jw.emitSingleLineComment("Init section: %s", this.packageName);

			// jw.emitStatement("%s.init()",
			// DeploymentComponent.class.getName());

			m.maybeblock_.accept(this, jw);
			jw.emitEmptyLine();
			jw.emit("", true);
			jw.emitStatementEnd();
			// jw.emitStatement("context.stop()");
			jw.endConstructor();

			jw.emitEmptyLine();
			// emitToStringMethod(jw);

			jw.emitEmptyLine();
			List<String> javaMainMethodParameters = Arrays.asList("String[]", "args");
			jw.beginMethod(VOID_PRIMITIVE_NAME, "main", mainModifiers, javaMainMethodParameters,
					Collections.singletonList("Exception"));
			jw.emitStatement("new Main(args)");
			jw.endMethod();

			jw.endType();
			close(jw, w);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void visitImports(final ListImport imports, JavaWriter w) throws IOException {
		for (Import import1 : imports) {
			import1.accept(this, w);
		}
		emitDefaultImports(w);
		w.emitStaticImports(this.staticImports);
		w.emitEmptyLine();
	}

	protected void emitDefaultImports(JavaWriter w) throws IOException {
		w.emitStaticImports(DEFAULT_STATIC_IMPORTS);
		// w.emitStaticImports(DEFAULT_STATIC_IMPORTS_PATTERNS);
		w.emitStaticImports(this.packageName + "." + FUNCTIONS_CLASS_NAME + ".*");
		w.emitImports(DEFAULT_IMPORTS);
		// w.emitImports(DEFAULT_IMPORTS_PATTERNS);
	}

	protected void emitPackageLevelImport(JavaWriter w) throws IOException {
		for (String p : this.packageLevelImports) {
			w.emitImports(this.packageName + "." + p + ".*");
		}
	}

	protected void visitAsyncMethodCall(AsyncMethCall amc, String resultVarType, String resultVarName,
			final boolean isDefined, JavaWriter w) throws IOException {
		String calleeId = getCalleeId(amc);
		List<String> params = getCalleeMethodParams(amc);
		String methodName = amc.l_.equals(METHOD_GET) ? "get" : amc.l_;

		String varDefType = findVariableType(resultVarName);
		String potentialReturnType = resultVarType != null ? resultVarType
				: varDefType != null ? varDefType
						: findMethodReturnType(methodName, findVariableType(calleeId), params);
		String msgVarName = createMessageVariableName(calleeId);
		String msgStatement = generateMessageStatement(msgVarName, potentialReturnType,
				generateJavaMethodInvocation(calleeId, methodName, params));
		w.emit(msgStatement, true);
		w.emitStatementEnd();
		String responseVarName = resultVarName != null ? resultVarName : createMessageResponseVariableName(msgVarName);
		String sendStm = generateMessageInvocationStatement(calleeId, isDefined, resultVarType, msgVarName,
				responseVarName);
		w.emit(sendStm, true);
		w.emitStatementEnd();
	}

	protected void visitSyncMethodCall_Async(SyncMethCall smc, String resultVarType, String resultVarName,
			boolean isDefined, JavaWriter w) throws IOException {
		String calleeId = getCalleeId(smc);
		List<String> params = getCalleeMethodParams(smc);
		String msgVarName = createMessageVariableName(calleeId);
		String methodName = smc.l_.equals(METHOD_GET) ? "get" : smc.l_;
		String potentialReturnType = resultVarType != null ? resultVarType
				: findMethodReturnType(methodName, findVariableType(calleeId), params);
		String msgStatement = generateMessageStatement(msgVarName, potentialReturnType,
				generateJavaMethodInvocation(calleeId, methodName, params));
		w.emit(msgStatement, true);
		w.emitStatementEnd();
		String responseVarName = createMessageResponseVariableName(msgVarName);
		String sendStm = generateMessageInvocationStatement(calleeId, isDefined, resultVarType, msgVarName,
				responseVarName);
		w.emit(sendStm, true);
		w.emitStatementEnd();
		if (resultVarName != null && resultVarType != null) {
			w.emit((isDefined ? "" : resultVarType + " " + " ") + resultVarName + " = " + responseVarName
					+ ".getValue()", true);
		} else {
			w.emit(responseVarName + ".getValue()", true);
		}
		w.emitStatementEnd();
	}

	protected void visitSyncMethodCall_Sync(SyncMethCall smc, String resultVarType, String resultVarName,
			boolean isDefined, JavaWriter w) throws IOException {
		String calleeId = getCalleeId(smc);
		List<String> params = getCalleeMethodParams(smc);
		String methodName = smc.l_.equals(METHOD_GET) ? "get" : smc.l_;
		String potentialReturnType = resultVarName != null ? resultVarType
				: findMethodReturnType(methodName, findVariableType(calleeId), params);
		String javaMethodCall = generateJavaMethodInvocation(calleeId, methodName, params);
		if (resultVarName != null) {
			if (isDefined)
				w.emit(String.format("%s = %s", resultVarName, javaMethodCall), true);
			else
				w.emit(String.format("%s %s = %s", potentialReturnType, resultVarName, javaMethodCall), true);

		} else {
			w.emit(javaMethodCall, true);
		}
		w.emitStatementEnd();
	}

	protected void visitStatementAssignmentExp(Exp exp, String varName, String varType, JavaWriter w)
			throws IOException {
		if (exp instanceof ExpP && ((ExpP) exp).pureexp_ instanceof ECase) {
			String caseStm = visitCase((ECase) ((ExpP) exp).pureexp_, varType);
			w.emit(String.format("%s %s = (%s) %s", varType, varName, varType, caseStm), true);
		} else {
			/*
			 * if ((exp instanceof ExpE) && ((ExpE) exp).effexp_ instanceof
			 * Spawns) { if (((ExpE) exp).effexp_ instanceof Spawns) { try {
			 * Spawns p = (Spawns) (((ExpE) exp).effexp_); StringWriter sw = new
			 * StringWriter(); StringWriter sw2 = new StringWriter(); New np =
			 * new New(p.t_, p.listpureexp_); visit(np, new JavaWriter(sw));
			 * p.pureexp_.accept(this, new JavaWriter(sw2)); if (varType ==
			 * null) { w.emitStatement(
			 * "%s = (%s) (%s.%s.context.newActor(\"%s\", %s))", varName,
			 * getTypeName(p.t_), sw2, ACTOR_SERVER_MEMBER, varName, sw); } else
			 * { w.emitStatement(
			 * "%s %s = (%s) (%s.%s.context.newActor(\"%s\", %s))", varType,
			 * varName, getTypeName(p.t_), sw2, ACTOR_SERVER_MEMBER, varName,
			 * sw); } } catch (IOException e) { throw new RuntimeException(e); }
			 * } }
			 */

			StringWriter auxsw = new StringWriter();
			JavaWriter auxw = new JavaWriter(auxsw);
			exp.accept(this, auxw);
			if (varType == null) {
				w.emit(varName + " = " + auxsw.toString(), true);
			} else {
				w.emit(String.format("%s %s = %s", varType, varName, auxsw.toString()), true);
			}

		}
		w.emitStatementEnd();
	}

	protected String visitCase(ECase kase, String expectedCaseType) {
		StringWriter auxsw = new StringWriter();
		kase.pureexp_.accept(this, new JavaWriter(auxsw));
		String kaseVar = auxsw.toString();
		String type = expectedCaseType;
		if (type == null) {
			type = findVariableType(kaseVar);
		}
		if (type == null) {
			type = "";
		} else {
			type = "(" + type + ")";
		}
		List<Entry<Pattern, PureExp>> cases = kase.listecasebranch_.stream().map(b -> (ECaseB) b)
				.map(cb -> new SimpleEntry<Pattern, PureExp>(cb.pattern_, cb.pureexp_)).collect(Collectors.toList());
		return visitCases(kaseVar, type, cases);
	}

	protected void visitJavaAnnDecl(AnnWithType ann_) {
		/*
		 * if (ann_ instanceof MappedAnn) { final MappedAnn ma = (MappedAnn)
		 * ann_; if (ma.literal_1 instanceof LStr && ma.literal_2 instanceof
		 * LStr) { LStr s1 = (LStr) ma.literal_1; LStr s2 = (LStr) ma.literal_2;
		 * if (s1.string_.contains(EXTERN))
		 * javaTypeTranslator.registerAbstractType(u_, s2.string_); if
		 * (s1.string_.contains(STATIC)) {
		 * javaTypeTranslator.registerStaticType(u_, s2.string_); } } }
		 */
	}

	protected void verifyJavaStatic(String fieldType, String fieldName) {
		if (javaTypeTranslator.inStaticTypes(fieldType)) {
			javaTypeTranslator.registerAbstractType(fieldName, javaTypeTranslator.translateStaticType(fieldType));
		}
	}

	private String visitCases(String caseVariable, String caseVariableType, List<Entry<Pattern, PureExp>> cases) {
		StringBuilder caseStm = new StringBuilder("Matching").append(NEW_LINE);
		for (Entry<Pattern, PureExp> e : cases) {
			Pattern left = e.getKey();
			PureExp right = e.getValue();
			StringWriter auxsw = new StringWriter();
			right.accept(this, new JavaWriter(auxsw));
			String thenMatchValue = auxsw.toString();
			caseStm.append(".").append(String.format("when()")).append(NEW_LINE);
			String kaseVarLocal = "var" + RANDOM.nextInt(1000);
			if (thenMatchValue.contains(" " + caseVariable + " ")) {
				thenMatchValue = thenMatchValue.replace(caseVariable, kaseVarLocal);
			}
			if (left instanceof PVar) {
				PVar pi = (PVar) left;
				caseStm.append(".").append(String.format("isValue(%s)", pi.l_)).append(NEW_LINE).append(".")
						.append(String.format("thenApply(x -> %s)", thenMatchValue)).append(NEW_LINE);
			} else if (left instanceof PLit) {
				PLit plit = (PLit) left;
				StringWriter litsw = new StringWriter();
				plit.accept(this, new JavaWriter(litsw));
				caseStm.append(".").append(String.format("isValue(%s)", litsw.toString())).append(NEW_LINE).append(".")
						.append(String.format("thenApply(x -> %s", thenMatchValue)).append(NEW_LINE);
			} else if (left instanceof PWildCard) {
				caseStm.append(".").append(String.format("isTrue(x -> true)")).append(NEW_LINE)
						.append(String.format("thenApply(x -> %s)", thenMatchValue)).append(NEW_LINE);
			} else if (left instanceof PSinglConstr) {
				PSinglConstr psc = (PSinglConstr) left;
				String type = getQTypeName(psc.qu_, false);
				caseStm.append(".").append(String.format("isType((%s x) -> %s)", type, thenMatchValue))
						.append(NEW_LINE);
			} else if (left instanceof PParamConstr) {
				PParamConstr ppc = (PParamConstr) left;
				String type = getQTypeName(ppc.qu_, false);
				caseStm.append(".").append(String.format("isType((%s x) -> %s)", type, thenMatchValue))
						.append(NEW_LINE);
			} else {
				logNotImplemented("case pattern: %s", left);
			}
		}
		caseStm.append(String.format(".match(%s).get()", caseVariable));
		return caseStm.toString();
	}

	private String toMatchingString(Pattern p) {
		if (p instanceof PWildCard) {
			return "any()";
		}
		if (p instanceof PVar) {
			return String.format("eq(%s)", ((PVar) p).l_);
		}
		if (p instanceof PLit) {
			StringWriter sw = new StringWriter();
			JavaWriter jw = new JavaWriter(sw);
			PLit lit = (PLit) p;
			lit.accept(this, jw);
			return String.format("eq(%s)", sw.toString());
		}
		if (p instanceof PSinglConstr) {
			PSinglConstr psc = (PSinglConstr) p;
			String id = getQTypeName(psc.qu_, false);
			final boolean isExceptionClass = exceptionDeclaraions.contains(id);
			final boolean isDataInstance = dataDeclarations.containsKey(id);
			if (isDataInstance) {
				return String.format("eq(%s.INSTANCE)", getRefindDataDeclName(id));
			}
			return String.format("eq(%s)", id);
		}
		if (p instanceof PParamConstr) {
			PParamConstr ppc = (PParamConstr) p;
			String id = ppc.qu_.toString();
			final boolean isExceptionClass = exceptionDeclaraions.contains(id);
			if (isExceptionClass) {
				List<String> params = new ArrayList<>();
				boolean anyPattern = false;
				for (Pattern paramPattern : ppc.listpattern_) {
					StringWriter sw = new StringWriter();
					JavaWriter auxjw = new JavaWriter(sw);
					paramPattern.accept(this, auxjw);
					String param = sw.toString();
					if (param.contains("any()")) {
						anyPattern = true;
						break;
					}
					params.add(param);
				}
				if (anyPattern == false) {
					String paramString = String.join(COMMA_SPACE, params);
					return String.format("eq(new %s(%s))", id, paramString);
				} else {
					return String.format("caseThat(isA(%s.class))", id);
				}
			} else {
				List<String> params = new ArrayList<>();
				for (Pattern paramPattern : ppc.listpattern_) {
					params.add(toMatchingString(paramPattern));
				}
				String paramString = String.join(COMMA_SPACE, params);
				return String.format("%s(%s)", id, paramString);
			}
		}
		throw new IllegalArgumentException("Unknown pattern: " + p);
	}

	private String refineFunctionalPureExpression(String expr) {
		String result = new String(expr);
		// 1. Replace all occurrences of exceptions `E` with `new E`
		for (String ex : this.exceptionDeclaraions) {
			String p1 = ex + "(";
			String p2 = "new " + p1;
			if (expr.contains(p1)) {
				result = result.replace(p1, p2);
			}
		}
		// 2. Replace all occurrences of data `D` with `new D`
		for (String dd : this.dataDeclarations.keySet()) {
			String p1 = dd + "(";
			if (expr.contains(p1)) {
				result = result.replace(p1, "new " + getRefindDataDeclName(dd) + "(");
			}
		}
		return result.replace("new new ", "new ");
	}

	private void visitSingleConstructorDataDecl(SinglConstrIdent sci, String className, String parent,
			List<String> classGenericParams, final boolean isParentInterface) throws IOException {
		JavaWriter cw = javaWriterSupplier.apply(className);
		cw.emitPackage(this.packageName);
		emitDefaultImports(cw);
		cw.emitEmptyLine();
		String fullClassName = classGenericParams.isEmpty() ? className
				: String.format("%s<%s>", className, String.join(COMMA_SPACE, classGenericParams));
		if (parent != null && isParentInterface) {
			beginElementKind(cw, ElementKind.CLASS, fullClassName, EnumSet.of(Modifier.PUBLIC), null,
					Collections.singletonList(parent), false);
		} else if (parent != null && !isParentInterface) {
			beginElementKind(cw, ElementKind.CLASS, fullClassName, EnumSet.of(Modifier.PUBLIC), parent,
					Collections.emptyList(), false);
		}
		cw.emitEmptyLine();
		cw.emitField(className, DATA_DECL_INSTANCE_NAME, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL),
				"new " + className + "()");
		emitEqualsMethod(className, Collections.singletonList(DATA_DECL_INSTANCE_NAME), true, cw);
		cw.endType();
		cw.close();
	}

	private void visitDataDeclarationConstructors(String parentDataInterface, ListConstrIdent lci,
			List<String> classGenericParams) throws IOException {
		for (ConstrIdent ci : lci) {
			if (ci instanceof ParamConstrIdent) {
				ParamConstrIdent pci = (ParamConstrIdent) ci;
				String className = pci.u_;
				if (parentDataInterface.equals(className)) {
					className = className + "Impl";
				}
				visitParametricConstructorDataDecl(pci, className, parentDataInterface, classGenericParams, true);
				this.dataDeclarations.put(pci.u_, className);
			} else if (ci instanceof SinglConstrIdent) {
				SinglConstrIdent sci = (SinglConstrIdent) ci;
				String className = sci.u_;
				if (parentDataInterface.equals(className)) {
					className = className + "Impl";
				}
				visitSingleConstructorDataDecl(sci, className, parentDataInterface, classGenericParams, true);
				this.dataDeclarations.put(sci.u_, className);
			}
		}
	}

	private void visitParametricConstructorDataDecl(ParamConstrIdent pci, String className, String parent,
			List<String> classGenericParams, final boolean isParentInterface) throws IOException {
		JavaWriter cw = javaWriterSupplier.apply(className);
		cw.emitPackage(this.packageName);
		emitDefaultImports(cw);
		cw.emitEmptyLine();
		List<Entry<String, String>> fields = extractConstructorParameters(pci.listconstrtype_);
		List<String> actualFieldTypes = fields.stream().map(e -> e.getKey()).collect(Collectors.toList());
		String fullClassName = classGenericParams.isEmpty() ? className
				: String.format("%s<%s>", className, String.join(COMMA_SPACE, actualFieldTypes));
		if (parent != null && isParentInterface) {
			beginElementKind(cw, ElementKind.CLASS, fullClassName, EnumSet.of(Modifier.PUBLIC), null,
					Collections.singletonList(parent), false);
		} else if (parent != null && !isParentInterface) {
			beginElementKind(cw, ElementKind.CLASS, fullClassName, EnumSet.of(Modifier.PUBLIC), parent,
					Collections.emptyList(), false);
		}
		cw.emitEmptyLine();
		visitConstrType(pci.listconstrtype_, cw, false);
		List<String> fieldNames = fields.stream().map(e -> e.getValue()).collect(Collectors.toList());
		emitEqualsMethod(className, fieldNames, cw);
		cw.endType();
		cw.close();
	}

	private List<Entry<String, String>> visitConstrType(ListConstrType ctrs, JavaWriter w,
			final boolean fieldAccessorMethods) throws IOException {
		if (ctrs == null || ctrs.isEmpty()) {
			return Collections.emptyList();
		}
		List<Map.Entry<String, String>> params = extractConstructorParameters(ctrs);
		params.forEach(e -> {
			try {
				if (fieldAccessorMethods) {
					w.emitField(e.getKey(), e.getValue(), EnumSet.of(Modifier.PRIVATE));
				} else {
					w.emitField(e.getKey(), e.getValue(), EnumSet.of(Modifier.PUBLIC, Modifier.FINAL));
				}
			} catch (IOException x) {
				throw new RuntimeException(x);
			}
		});
		w.emitEmptyLine();
		List<String> tmp = new ArrayList<>();
		params.forEach(e -> {
			tmp.add(e.getKey());
			tmp.add(e.getValue());
		});
		w.beginConstructor(EnumSet.of(Modifier.PUBLIC), tmp, Collections.emptyList());
		params.forEach(e -> {
			try {
				w.emitStatement("this.%s = %s", e.getValue(), e.getValue());
			} catch (IOException x) {
				throw new RuntimeException(x);
			}
		});
		w.endConstructor();
		w.emitEmptyLine();

		if (fieldAccessorMethods) {
			params.forEach(e -> {
				try {
					w.beginMethod(e.getKey(), String.format("get%s", e.getValue()), EnumSet.of(Modifier.PUBLIC),
							new String[0]);
					w.emitStatement("return this.%s", e.getValue());
					w.endMethod();
					w.emitEmptyLine();
				} catch (IOException x) {
					throw new RuntimeException(x);
				}
			});
		}
		return params;
	}

	private List<Entry<String, String>> extractConstructorParameters(ListConstrType ctrs) {
		List<Entry<String, String>> params = new ArrayList<>();
		for (ConstrType ct : ctrs) {
			if (ct instanceof EmptyConstrType) {
				EmptyConstrType ect = (EmptyConstrType) ct;
				String type = getTypeName(ect.t_);
				String fieldName = createJavaFieldName(type);
				params.add(new SimpleEntry<String, String>(type, fieldName));
			} else if (ct instanceof RecordConstrType) {
				RecordConstrType rct = (RecordConstrType) ct;
				String type = getTypeName(rct.t_);
				String fieldName = rct.l_;
				params.add(new SimpleEntry<String, String>(type, fieldName));
			}
		}
		return params;
	}

	protected List<String> getCalleeMethodParams(AsyncMethCall amc) {
		return getParameters(amc.listpureexp_);
	}

	protected List<String> getCalleeMethodParams(SyncMethCall smc) {
		return getParameters(smc.listpureexp_);
	}

	protected List<String> getParameters(ListPureExp params) {
		List<String> parameters = new ArrayList<>();
		for (PureExp par : params) {
			StringWriter parSW = new StringWriter();
			JavaWriter parameterWriter = new JavaWriter(parSW);
			par.accept(this, parameterWriter);
			parameters.add(parSW.toString());
		}
		return parameters;
	}

	protected String getCalleeId(AsyncMethCall amc) {
		StringWriter auxsw = new StringWriter();
		JavaWriter auxw = new JavaWriter(auxsw);
		amc.pureexp_.accept(this, auxw);
		String calleeId = auxsw.toString();
		return calleeId;
	}

	protected String getCalleeId(SyncMethCall smc) {
		StringWriter auxsw = new StringWriter();
		JavaWriter auxw = new JavaWriter(auxsw);
		smc.pureexp_.accept(this, auxw);
		String calleeId = auxsw.toString();
		return calleeId;
	}

	/**
	 * Creates a Java method invocation:
	 * 
	 * <pre>
	 * myObj.myMethod(p1, p2, p3)
	 * </pre>
	 * 
	 * @param object
	 *            the callee object
	 * @param method
	 *            the method of the callee object
	 * @param parameters
	 *            the parameters of the method that can be empty string
	 * @return a string representing a Java method invocation statement
	 */
	protected String generateJavaMethodInvocation(String object, String method, List<String> parameters) {
		return String.format("%s.%s(%s)", object, method,
				parameters == null || parameters.isEmpty() ? "" : String.join(COMMA_SPACE, parameters));
	}

	/**
	 * Create an asynchronous message in the context of ABS API which is either
	 * an instance of {@link Runnable} or a {@link Callable}.
	 * 
	 * @param msgVarName
	 *            the variable name to use the created message
	 * @param returnType
	 *            the return type of the message; if <code>null</code>, it means
	 *            to use {@link Runnable}
	 * @param expression
	 *            the Java expression to use for the body of the lambda
	 *            expression
	 * @return a string in Java representing a lambda expression for a
	 *         {@link Runnable} or a {@link Callable}
	 */
	protected String generateMessageStatement(String msgVarName, String returnType, String expression) {
		String actualReturnType = resolveMessageType(returnType);
		return String.format("%s %s = () -> %s", actualReturnType, msgVarName, expression);
	}

	/**
	 * Create a Java statement when sending a message to an {@link Actor} in the
	 * ABS API.
	 * 
	 * @param target
	 *            the receiver identifier of the message
	 * @param msgReturnType
	 *            the expected return type of the message; if <code>null</code>,
	 *            the generated {@link Response} will be over {@link Void}
	 * @param msgVarName
	 *            the variable name of the message
	 * @param responseVarName
	 *            the variable of the generated {@link Response}; can be
	 *            <code>null</code>
	 * @return a Java statement string for such a call
	 */
	protected String generateMessageInvocationStatement(String target, final boolean isDefined, String msgReturnType,
			String msgVarName, String responseVarName) {
		final String method = "send";
		if (responseVarName.endsWith("_response")) {
			return String.format("%s.%s(%s)", target, method, msgVarName);
		}
		if (!isDefined) {
			return String.format("%s = %s.%s(%s)", responseVarName, target, method, msgVarName);
		}
		String returnType = msgReturnType == null || isVoid(msgReturnType) ? VOID_WRAPPER_CLASS_NAME
				: stripGenericResponseType(msgReturnType);
		return String.format("ABSFuture<%s> %s = %s.%s (%s)", returnType, responseVarName, target, method, msgVarName);
	}

	/**
	 * @param qtype
	 * @return
	 */
	protected String getQTypeName(QU qtype, boolean isTopLevel) {
		if (qtype instanceof QU_) {
			QU_ qtyp = (QU_) qtype;
			StringBuilder sb = new StringBuilder();
			sb.append(qtyp.u_);
			while (qtyp.qu_ instanceof QU_) {
				sb.append(".");
				sb.append(((QU_) qtyp).u_);
				qtyp = (QU_) qtyp.qu_;
			}
			sb.append(((U_) qtyp.qu_).u_);
			if (!isTopLevel)
				return translate(sb.toString());
			else
				return sb.toString();
		} else {
			U_ qtyp = (U_) qtype;
			StringBuilder sb = new StringBuilder();
			sb.append(qtyp.u_);
			if (!isTopLevel)
				return translate(sb.toString());
			else
				return sb.toString();
		}
	}

	/**
	 * @param qtype
	 * @return
	 */
	protected String getQTypeName(QL qtype, boolean isTopLevel) {
		if (qtype instanceof QL_) {
			QL_ qtyp = (QL_) qtype;
			StringBuilder sb = new StringBuilder();
			sb.append(qtyp.u_);
			while (qtyp.ql_ instanceof QL_) {
				sb.append(".");
				sb.append(((QL_) qtyp).u_);
				qtyp = (QL_) qtyp.ql_;
			}
			sb.append(((L_) qtyp.ql_).l_);
			if (!isTopLevel)
				return translate(sb.toString());
			else
				return sb.toString();
		} else {
			L_ qtyp = (L_) qtype;
			StringBuilder sb = new StringBuilder();
			sb.append(qtyp.l_);
			if (!isTopLevel)
				return translate(sb.toString());
			else
				return sb.toString();
		}
	}

	/**
	 * @param qtype
	 * @return
	 */
	protected String getQTypeName(QA qtype, boolean isTopLevel) {
		StringBuilder sb = new StringBuilder();
		if (qtype instanceof QA_) {
			QA_ qtyp = (QA_) qtype;
			sb.append(qtyp.u_);
			while (qtyp.qa_ instanceof QA_) {
				sb.append(".");
				sb.append(((QA_) qtyp).u_);
				qtyp = (QA_) qtyp.qa_;
			}
			if (qtyp.qa_ instanceof UA)
				sb.append(((UA) qtyp.qa_).u_);
			else
				sb.append(((LA) qtyp.qa_).l_);
			if (!isTopLevel)
				return translate(sb.toString());
			else
				return sb.toString();
		} else {
			if (qtype instanceof UA)
				sb.append(((UA) qtype).u_);
			else
				sb.append(((LA) qtype).l_);
			if (!isTopLevel)
				return translate(sb.toString());
			else
				return sb.toString();
		}
	}

	/**
	 * @param qtypes
	 * @return
	 */
	protected List<String> toList(ListQU qtypes, boolean isTopLevel) {
		return qtypes.stream().map(qtype -> getQTypeName(qtype, isTopLevel)).collect(Collectors.toList());
	}

	/**
	 * @param w
	 * @param fieldType
	 * @param fieldIdentifier
	 * @param initialValue
	 * @param isFinal
	 * @return
	 * @throws IOException
	 */
	protected JavaWriter emitField(JavaWriter w, String fieldType, String fieldIdentifier, String initialValue,
			final boolean isFinal) throws IOException {
		EnumSet<Modifier> modifiers = EnumSet.noneOf(Modifier.class);
		if (isFinal) {
			modifiers.add(Modifier.FINAL);
		}
		if (initialValue != null) {
			return w.emitField(fieldType, fieldIdentifier, modifiers, initialValue);
		} else {
			return w.emitField(fieldType, fieldIdentifier, modifiers);
		}
	}

	protected void beginElementKind(JavaWriter w, ElementKind kind, String identifier, Set<Modifier> modifiers,
			String classParentType, Collection<String> implementingInterfaces) throws IOException {
		beginElementKind(w, kind, identifier, modifiers, classParentType, implementingInterfaces, true);
	}

	/**
	 * Begin a Java type.
	 * 
	 * @param w
	 *            the Java writer
	 * @param kind
	 *            See {@link ElementKind}
	 * @param identifier
	 *            the Java identifier of the type
	 * @param modifiers
	 *            the set of {@link Modifier}s
	 * @param classParentType
	 *            the extending type that can be <code>null</code>
	 * @param implementingInterfaces
	 *            the implementing interface that can be <code>null</code>
	 * @param isActor
	 *            indicates if the class should "implement"
	 *            <code>abs.api.Actor</code>
	 * @throws IOException
	 *             Exception from {@link JavaWriter}
	 * @throws IllegalArgumentException
	 *             if kind other than "class" or "interface" is requested
	 */
	protected void beginElementKind(JavaWriter w, ElementKind kind, String identifier, Set<Modifier> modifiers,
			String classParentType, Collection<String> implementingInterfaces, final boolean isActor)
			throws IOException {
		Set<String> implementsTypes = new HashSet<>();
		if (implementingInterfaces != null && !implementingInterfaces.isEmpty()) {
			implementsTypes.addAll(implementingInterfaces);
		}
		String kindName = kind.name().toLowerCase();
		switch (kind) {
		case CLASS:
			w.beginType(identifier, kindName, modifiers, classParentType, implementsTypes.toArray(new String[0]));
			if (isActor) {
				emitSerialVersionUid(w);
			}
			return;
		case INTERFACE:
			implementsTypes.add(ABS_API_INTERFACE_CLASS);
			if (implementsTypes.isEmpty()) {
				w.beginType(identifier, kindName, modifiers, null, new String[0]);
			} else {
				w.beginType(identifier, kindName, modifiers, String.join(COMMA_SPACE, implementsTypes), new String[0]);
			}
			return;
		case ENUM:
			w.beginType(identifier, kindName, modifiers);
			return;
		default:
			throw new IllegalArgumentException("Unsupported Java element kind: " + kind);
		}
	}

	/**
	 * @param decl
	 * @param w
	 * @return
	 * @throws IOException
	 */
	protected JavaWriter createJavaWriter(AnnDecl decl, JavaWriter w) throws IOException {
		if (isTopLevel(decl)) {

			String identifier = getTopLevelDeclIdentifier(((AnnDeclaration) decl).decl_);
			if (packageName.equalsIgnoreCase(identifier) || moduleNames.contains(identifier)) {
				return w;
			}
			JavaWriter jw = javaWriterSupplier.apply(identifier);
			jw.emitPackage(packageName);
			return jw;
		} else {
			return w;
		}
	}

	/**
	 * @param childWriter
	 * @param parentWriter
	 * @throws IOException
	 */
	protected void close(JavaWriter childWriter, JavaWriter parentWriter) throws IOException {
		if (childWriter != parentWriter) {
			childWriter.close();
		}
	}

	/**
	 * @param decl
	 * @return
	 */
	protected boolean isTopLevel(AnnDecl decl) {
		return isAbsInterfaceDecl(decl) || isAbsClassDecl(decl) || isAbsFunctionDecl(decl) || isAbsDataTypeDecl(decl);
	}

	/**
	 * @param decl
	 * @return
	 */
	protected <C extends Decl> String getTopLevelDeclIdentifier(Decl decl) {
		if (decl instanceof DClass) {
			return ((DClass) decl).u_;
		}
		if (decl instanceof DClassImplements) {
			return ((DClassImplements) decl).u_;
		}
		if (decl instanceof DClassPar) {
			return ((DClassPar) decl).u_;
		}
		if (decl instanceof DClassParImplements) {
			return ((DClassParImplements) decl).u_;
		}
		if (decl instanceof DExtends) {
			return ((DExtends) decl).u_;
		}
		if (decl instanceof DInterf) {
			return ((DInterf) decl).u_;
		}
		if (decl instanceof DFun) {
			return ((DFun) decl).l_;
		}
		if (decl instanceof DData) {
			return ((DData) decl).u_;
		}
		if (decl instanceof DDataPoly) {
			return ((DDataPoly) decl).u_;
		}
		if (decl instanceof DException) {
			return getExceptionClassName(((DException) decl).constrident_);
		}
		if (decl == null) {
			return MAIN_CLASS_NAME;
		}
		throw new IllegalArgumentException("Unknown top level type: " + decl);
	}

	/**
	 * @param type
	 * @return
	 */
	protected String translate(String type) {
		if (type == null) {
			return null;
		}
		String result = javaTypeTranslator.apply(type);
		// System.out.println(String.format("T> %s -> %s", type,
		// result));
		return result;
	}

	private String getTypeName(T type) {
		if (type instanceof TSimple) {
			TSimple ts = (TSimple) type;
			QU qu_ = ts.qu_;
			return getQTypeName(qu_, false);
		}
		if (type instanceof TPoly) {
			TPoly tg = (TPoly) type;
			StringBuilder sQ = new StringBuilder(getQTypeName(tg.qu_, false));
			sQ.append("<");
			List<String> gTypes = new ArrayList<String>();
			for (T t : tg.listt_) {
				gTypes.add(getTypeName(t));
			}
			sQ.append(String.join(COMMA_SPACE, gTypes));
			sQ.append(">");
			return refineVoidType(sQ.toString());
		}
		return null;
	}

	private String findVariableType(String varName) {
		for (String fqClassName : variables.keySet()) {
			if (!fqClassName.startsWith(this.packageName)) {
				continue;
			}
			Collection<VarDefinition> vars = variables.get(fqClassName);
			for (VarDefinition vd : vars) {
				if (vd.getName().equals(varName)) {
					return vd.getType();
				}
			}
		}
		return null;
	}

	private String findMethodReturnType(String methodName, String calleeClassType, List<String> actualParamNames) {
		for (String fqClassName : methods.keySet()) {
			if (calleeClassType != null && !fqClassName.endsWith(calleeClassType)) {
				continue;
			}
			Collection<MethodDefinition> methods = this.methods.get(fqClassName);
			for (MethodDefinition md : methods) {
				if (md.matches(methodName, actualParamNames)) {
					return md.type();
				}
			}
		}
		return null;
	}

	private void createVarDefinition(String varName, String varType) {
		Module current = currentModule();
		if (current == null) {
			throw new IllegalStateException("No current module is available.");
		}
		String clazz = getQTypeName(((Mod) current).qu_, false);
		String fqClassName = this.packageName + "." + clazz;
		VarDefinition vd = new VarDefinition(varName, varType);
		variables.put(fqClassName, vd);
	}

	private void createMethodDefinition(String returnType, String name, List<String> parameters) {
		String className = currentClass();
		if (className == null) {
			throw new IllegalStateException("No current 'class' is available.");
		}
		String fqClassName = this.packageName + "." + className;
		MethodDefinition md = new MethodDefinition(fqClassName, returnType, name, parameters);
		currentMethod = md;
		methods.put(fqClassName, md);
	}

	private Module currentModule() {
		return this.modules.peek();
	}

	private String currentClass() {
		return this.classes.peek();
	}

	private void buildProgramDeclarationTypes(Prog program) {
		/*
		 * ABS allows for SAME naming of an interface and an implementing class.
		 * To be able to properly compile this, we need to eagerly identify the
		 * elements of an ABS program. Strategy of compiling:
		 * 
		 * 1. Compile interfaces to separate files
		 * 
		 * 2. Compile classes to separate files
		 */

		elements.clear();
		for (AbsElementType t : EnumSet.allOf(AbsElementType.class)) {
			elements.put(t, new LinkedList<>());
		}

		// 1. Interfaces
		for (Module module : program.listmodule_) {
			Mod m = (Mod) module;
			for (AnnDecl decl : m.listanndecl_) {
				if (isAbsInterfaceDecl(decl)) {
					elements.get(AbsElementType.INTERFACE).add(decl);
				}
			}
		}
		// 2. Classes
		for (Module module : program.listmodule_) {
			Mod m = (Mod) module;
			for (AnnDecl decl : m.listanndecl_) {
				if (isAbsClassDecl(decl)) {
					final String className = getTopLevelDeclIdentifier(((AnnDeclaration) decl).decl_);
					if (elements.get(AbsElementType.INTERFACE).stream().anyMatch(
							d -> className.equals(getTopLevelDeclIdentifier(((AnnDeclaration) decl).decl_)))) {
						// classNames.put(className, className + "Impl");
					} else {
						classNames.put(className, className);
					}
					elements.get(AbsElementType.CLASS).add(decl);
				}
			}
		}
		// 3. Functions
		for (Module module : program.listmodule_) {
			Mod m = (Mod) module;
			for (AnnDecl decl : m.listanndecl_) {
				if (isAbsFunctionDecl(decl)) {
					elements.get(AbsElementType.FUNCTION).add(decl);
				}
			}
		}
		// 4. Data
		for (Module module : program.listmodule_) {
			Mod m = (Mod) module;
			for (AnnDecl decl : m.listanndecl_) {
				if (isAbsDataTypeDecl(decl)) {
					elements.get(AbsElementType.DATA).add(decl);
				}
			}
		}
		// 5. T
		for (Module module : program.listmodule_) {
			Mod m = (Mod) module;
			for (AnnDecl decl : m.listanndecl_) {
				if (isAbsAbstractTypeDecl(decl)) {
					elements.get(AbsElementType.TYPE).add(decl);
				}
			}
		}
		// 6. Exceptions
		for (Module module : program.listmodule_) {
			Mod m = (Mod) module;
			for (AnnDecl decl : m.listanndecl_) {
				if (isAbsExceptionDecl(decl)) {
					elements.get(AbsElementType.EXCEPTION).add(decl);
				}
			}
		}
	}

	private boolean isAbsInterfaceDecl(AnnDecl decl) {
		AnnDeclaration ad = (AnnDeclaration) decl;
		return ad.decl_ instanceof DInterf || ad.decl_ instanceof DExtends;
	}

	private boolean isAbsClassDecl(AnnDecl decl) {
		AnnDeclaration ad = (AnnDeclaration) decl;

		return ad.decl_ instanceof DClass || ad.decl_ instanceof DClassImplements || ad.decl_ instanceof DClassPar
				|| ad.decl_ instanceof DClassParImplements;
	}

	private boolean isAbsFunctionDecl(AnnDecl decl) {

		AnnDeclaration ad = (AnnDeclaration) decl;
		return ad.decl_ instanceof DFun || ad.decl_ instanceof DFunPoly;
	}

	private boolean isAbsDataTypeDecl(AnnDecl decl) {
		AnnDeclaration ad = (AnnDeclaration) decl;
		return ad.decl_ instanceof DData || ad.decl_ instanceof DDataPoly;
	}

	private boolean isAbsAbstractTypeDecl(AnnDecl decl) {
		AnnDeclaration ad = (AnnDeclaration) decl;
		return ad.decl_ instanceof DType || ad.decl_ instanceof DTypePoly;
	}

	private boolean isAbsExceptionDecl(AnnDecl decl) {
		AnnDeclaration ad = (AnnDeclaration) decl;
		return ad.decl_ instanceof DException;
	}

	private String getRefinedClassName(String name) {
		return classNames.containsKey(name) ? classNames.get(name) : name;
	}

	private String getRefindDataDeclName(String name) {
		if (this.dataDeclarations.containsKey(name))
			return this.dataDeclarations.get(name);
		return name;
	}

	private void logNotImplemented(String format, Object... args) {
		String msg = "Not implemented: " + String.format(format, args);
		logWarn(msg);
	}

	private void logNotSupported(String format, Object... args) {
		String msg = "Not supported: " + String.format(format, args);
		logWarn(msg);
	}

	private void logWarn(String msg) {
		LOGGER.warning(msg);
	}

	private String stripGenericResponseType(String type) {
		String regex = "^(ABSFuture\\<)(.*)(\\>)$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(regex);
		Matcher m = p.matcher(type);
		if (m.matches()) {
			String innerType = m.group(2);
			return innerType;
		} else {
			return type;
		}
	}

	private String createMessageVariableName(String calleeId) {
		return "msg_" + Math.abs(calleeId.hashCode()) + "" + RANDOM.nextInt(1000);
	}

	private String createMessageResponseVariableName(String msgVarName) {
		return msgVarName + "_response";
	}

	private String createPropertiesVariableName(String serverID) {
		return serverID + "_response";
	}

	private boolean isVoid(String type) {
		return isLiteralPrimitiveVoid(type) || isGenericVoid(type);
	}

	private boolean isGenericVoid(String type) {
		return type.contains("<" + VOID_PRIMITIVE_NAME + ">");
	}

	private boolean isLiteralPrimitiveVoid(String type) {
		return VOID_PRIMITIVE_NAME.equalsIgnoreCase(type);
	}

	private String refineVoidType(String type) {
		return isGenericVoid(type) ? type.replaceAll("<void>", "<Void>") : type;
	}

	private String resolveMessageType(String returnType) {
		String actualReturnType = null;
		if (returnType != null) {
			String type = stripGenericResponseType(returnType);
			if (isVoid(type)) {
				actualReturnType = "Runnable";
			} else {
				actualReturnType = String.format("Callable<%s>", type);
			}
		} else {
			actualReturnType = "Runnable";
		}
		return actualReturnType;
	}

	private void emitThisActorRegistration(JavaWriter w) throws IOException {
		w.emitStatement("context().newActor(toString(), this)");
	}

	/*
	 * The reason for this method to resolve the compilation issue with Java
	 * compiler. The resolution scope hierarchy starts from java.lang.Object and
	 * that's why Functional.toString() is not resolved by default order.
	 */
	private void emitToStringMethod(JavaWriter w) throws IOException {
		w.emitEmptyLine();
		w.emitSingleLineComment("To override default Object#toString() competing");
		w.emitSingleLineComment("with static imports of Functional#toString()");
		w.beginMethod("String", "toString", EnumSet.of(Modifier.PRIVATE), "Object", "o");
		w.emitStatement("return Functional.toString(o)");
		w.endMethod();
	}

	private void emitEqualsMethod(String className, List<String> fieldNames, JavaWriter w) throws IOException {
		emitEqualsMethod(className, fieldNames, false, w);
	}

	private void emitEqualsMethod(String className, List<String> fieldNames, final boolean staticFields, JavaWriter w)
			throws IOException {
		w.emitEmptyLine();
		w.beginMethod("boolean", "equals", EnumSet.of(Modifier.PUBLIC), "Object", "o");

		w.beginControlFlow("if (o == null)");
		w.emitStatement("return false");
		w.endControlFlow();

		w.beginControlFlow("if (this == o)");
		w.emitStatement("return true");
		w.endControlFlow();

		w.beginControlFlow("if (o instanceof %s == false)", className);
		w.emitStatement("return false");
		w.endControlFlow();

		w.emitStatement("%s x = (%s) o", className, className);
		List<String> conditions = new ArrayList<>();
		String owner1 = staticFields ? className : LITERAL_THIS;
		String owner2 = "x";
		for (String f : fieldNames) {
			conditions.add(String.format("Objects.equals(%s.%s, %s.%s)", owner1, f, owner2, f));
		}
		String condition = String.join(" &&\n ", conditions);
		w.emitStatement("return %s", condition);

		w.endMethod();
	}

	private boolean isBlockStatement(Stm s) {
		return s instanceof SIf || s instanceof SIfElse || s instanceof STryCatchFinally || s instanceof SWhile;
	}

	private void emitDefaultRunMethodExecution(JavaWriter w, String className) throws IOException {
		// Check for 'run' method
		final String fqClassName = this.packageName + "." + className;
		final String methodName = "run";
		boolean definesMethod = methods.get(fqClassName).stream()
				.anyMatch(md -> md.matches(methodName, Collections.emptyList()));
		if (definesMethod) {
			w.emitEmptyLine();
			w.emitSingleLineComment("Call 'run' method");
			w.emitStatement("this.run()");
			w.emitEmptyLine();
		}
	}

	private String getExceptionClassName(ConstrIdent exConstrIdent) {
		return exConstrIdent instanceof SinglConstrIdent ? ((SinglConstrIdent) exConstrIdent).u_
				: ((ParamConstrIdent) exConstrIdent).u_;
	}

	private void emitSerialVersionUid(JavaWriter w) throws IOException {
		w.emitField("long", "serialVersionUID", EnumSet.of(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL), "1L");
		w.emitEmptyLine();
	}

	private String createJavaFieldName(String type) {
		String nonGenericType = stripGenericParameterType(type);
		// 1. Try proper Java type
		try {
			Class<?> clazz = Class.forName(nonGenericType);
			return clazz.getSimpleName() + "Value";
		} catch (ClassNotFoundException e) {
			// Ignore
		}
		// 2. Try constructing a type from the string
		String simpleType = nonGenericType;
		int dotIndex = nonGenericType.lastIndexOf(CHAR_DOT);
		if (dotIndex >= 0) {
			simpleType = nonGenericType.substring(dotIndex + 1);
		}
		simpleType = simpleType.replace(CHAR_DOT, CHAR_UNDERSCORE) + "Value";
		return simpleType;
	}

	private String stripGenericParameterType(String type) {
		String regex = "^(\\w+)\\<(.*)\\>$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(regex);
		Matcher m = p.matcher(type);
		if (m.matches()) {
			String outerType = m.group(1);
			return outerType;
		} else {
			return type;
		}
	}

	private String findExceptionTypeName(Pattern p) {
		if (p instanceof PVar) {
			return ((PVar) p).l_;
		}
		if (p instanceof PLit) {
			StringWriter sw = new StringWriter();
			JavaWriter jw = new JavaWriter(sw);
			((PLit) p).literal_.accept(this, jw);
			return sw.toString();
		}
		if (p instanceof PParamConstr) {
			PParamConstr ppc = (PParamConstr) p;
			return getQTypeName(ppc.qu_, false);
		}
		if (p instanceof PSinglConstr) {
			return getQTypeName(((PSinglConstr) p).qu_, false);
		}
		return null;
	}

	private String toString(T type) {
		if (type instanceof TSimple) {
			return getQTypeName(((TSimple) type).qu_, false);
		}
		if (type instanceof TPoly) {
			List<String> types = ((TPoly) type).listt_.stream().map(t -> toString(t)).collect(Collectors.toList());
			return String.format("%s<%s>", getQTypeName(((TPoly) type).qu_, false), String.join(COMMA_SPACE, types));
		}
		throw new IllegalArgumentException(type.toString());
	}

}
