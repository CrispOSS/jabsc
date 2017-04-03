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
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;

import com.google.common.base.StandardSystemProperty;
import com.google.common.base.Supplier;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import com.pogofish.jadt.JADT;

import abs.api.cwi.ABSFutureTask;
import abs.api.cwi.Actor;
import abs.api.cwi.Functional;
import abs.api.cwi.LocalActor;
import bnfc.abs.AbstractVisitor;

import bnfc.abs.Absyn.*;
import jabsc.Visitor.AbsElementType;

class ScalaVisitor extends AbstractVisitor<Prog, ScalaWriter> {
	static int x = 0;

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
	private static final String ALL_IMPORTS = "._";

	private static final String NEW_LINE = StandardSystemProperty.LINE_SEPARATOR.value();
	private static final String ABS_API_ACTOR_CLASS = "LocalActor";
	private static final String ABS_API_INTERFACE_CLASS = "Actor";
	private static final String ORDERED_INTERFACE_CLASS = "Ordered";

	private static final String CASE = "_case";

	// private static final String ABS_API_ACTOR_SERVER_CLASS =
	// ActorServer.class.getName();
	private static final Set<Modifier> DEFAULT_MODIFIERS = new HashSet<>();
	private static final String[] DEFAULT_IMPORTS = new String[] {
			ABSFutureTask.class.getPackage().getName() + ALL_IMPORTS,
			Function.class.getPackage().getName() + ALL_IMPORTS, Callable.class.getPackage().getName() + ALL_IMPORTS,
			AtomicLong.class.getPackage().getName() + ALL_IMPORTS, Lock.class.getPackage().getName() + ALL_IMPORTS,
			// LocalActor.class.getPackage().getName() + ALL_IMPORTS,
			// "absstdlib.Functions" + ALL_IMPORTS, "absstdlib" + ALL_IMPORTS,
			// "scala.collection.mutable.Set",
			// "scala.collection.mutable.TreeSet",
			// "scala.collection.mutable.Map",
			// "scala.collection.mutable.HashMap",
			Objects.class.getName(),
			// List.class.getName(),
			// LinkedList.class.getName(),
			// CloudProvider.class.getPackage().getName() + ".*",
			// DeploymentComponent.class.getPackage().getName() + ".*",
	};
	// private static String LIBRARY_IMPORT = "";
	// private static final String[] DEFAULT_IMPORTS_PATTERNS = new String[] {
	// "com.leacox.motif.function.*", "com.leacox.motif.matching.*",
	// "com.leacox.motif.cases.*", "com.leacox.motif.caseclass.*" };
	private static final String[] DEFAULT_STATIC_IMPORTS = new String[] {
			// Functional.class.getPackage().getName() + "." +
			// Functional.class.getSimpleName() + ALL_IMPORTS,
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
	private final ScalaTypeTranslator javaTypeTranslator;
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

	private final LinkedList<TreeSet<VarDefinition>> variablesInScope = new LinkedList<>();
	private final HashSet<VarDefinition> variablesBeforeBlock = new HashSet<>();

	private final HashMap<String, MethodDefinition> programMethods = new HashMap<>();
	private final HashMap<String, List<String>> paramConstructs = new HashMap<>();
	private final HashMap<String, List<String>> caseTypeConstructs = new HashMap<>();

	private String patternParamType = "";
	private String caseKey = "";
	private boolean fromCase = false;

	private final Stack<Module> modules = new Stack<>();
	private final Stack<String> classes = new Stack<>();
	private final EnumMap<AbsElementType, List<AnnDecl>> elements = new EnumMap<>(AbsElementType.class);
	private final Map<String, String> classNames = new HashMap<>();
	private final Set<String> packageLevelImports = new HashSet<>();
	private final Map<String, String> dataDeclarations = new HashMap<>();

	private final Set<String> exceptionDeclaraions = new HashSet<>();
	private final Set<String> staticImports = new HashSet<>();
	private final Set<String> adtInstances = new HashSet<>();

	private final Map<String, LinkedList<StringWriter>> labelMap = new HashMap<>();

	private final List<StringWriter> currentMethodLabels = new LinkedList<>();

	private final Map<String, String> caseMap = new HashMap<>();

	private MethodDefinition currentMethod;

	private boolean awaitsDetected = false;

	private int awaitCounter = 0;
	private int syncPCounter = 0;
	private int asyncPCounter = 0;
	private ScalaWriter functionsWriter;

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
	public ScalaVisitor(String packageName, Prog prog, JavaWriterSupplier javaWriterSupplier,
			ScalaTypeTranslator javaTypeTranslator, Path outputDirectory) {
		this.packageName = packageName;
		this.prog = prog;
		this.javaWriterSupplier = javaWriterSupplier;
		this.javaTypeTranslator = javaTypeTranslator;
		this.outputDirectory = outputDirectory;
		this.moduleNames = new HashSet<>();

		List<String> justT = new ArrayList<>();
		justT.add("A");
		paramConstructs.put("Just", justT);
		List<String> consT = new ArrayList<>(justT);
		consT.add("List[A]");
		paramConstructs.put("Cons", consT);
		List<String> insT = new ArrayList<>(justT);
		insT.add("Set[A]");
		paramConstructs.put("Insert", insT);

		List<String> leftT = new ArrayList<>(justT);
		leftT.add("B");
		paramConstructs.put("Left", leftT);
		List<String> rightT = new ArrayList<>(leftT);
		paramConstructs.put("Right", rightT);
		List<String> pairT = new ArrayList<>(leftT);
		paramConstructs.put("APair", pairT);
		List<String> iaT = new ArrayList<>();
		iaT.add("Pair[A,B]");
		iaT.add("Map[A,B]");
		paramConstructs.put("InsertAsscoc", iaT);

		List<String> tripleT = new ArrayList<>(leftT);
		tripleT.add("C");
		paramConstructs.put("Triple", tripleT);

		List<String> maybe = new ArrayList<>();
		maybe.add("Just");
		caseTypeConstructs.put("Maybe", maybe);

		List<String> cons = new ArrayList<>();
		cons.add("Cons");
		caseTypeConstructs.put("List", cons);
		caseTypeConstructs.put("LinkedList", cons);

		List<String> ts = new ArrayList<>();
		ts.add("Insert");
		caseTypeConstructs.put("TreeSet", ts);
		caseTypeConstructs.put("Set", ts);

		List<String> ia = new ArrayList<>();
		ia.add("InsertAssoc");
		caseTypeConstructs.put("HashMap", ia);
		caseTypeConstructs.put("Map", ia);

		List<String> pair = new ArrayList<>();
		pair.add("APair");
		caseTypeConstructs.put("Pair", pair);

		List<String> triple = new ArrayList<>();
		triple.add("ATriple");
		caseTypeConstructs.put("Triple", triple);

		List<String> either = new ArrayList<>();
		either.add("Left");
		either.add("Right");
		caseTypeConstructs.put("Either", either);

		System.out.println(caseTypeConstructs);
		System.out.println(paramConstructs);

	}

	@Override
	public Prog visit(Prog p, ScalaWriter w) {
		// WARNING: `w` should NOT be used in this method;
		// otherwise, I/O issues occur during generation.
		Prog program = (Prog) p;
		buildProgramDeclarationTypes(program);
		do {
			awaitsDetected = false;
			ScalaWriter notNeeded = new ScalaWriter(true, new StringWriter(), true);
			for (Module module : program.listmodule_) {
				moduleNames.add(getQTypeName(((Mod) module).qu_, false));
				modules.push(module);
				module.accept(this, notNeeded);
				modules.pop();
			}
			// System.out.println(programMethods);
		} while (awaitsDetected);
		for (Module module : program.listmodule_) {
			moduleNames.add(getQTypeName(((Mod) module).qu_, false));
			modules.push(module);
			module.accept(this, w);
			modules.pop();
		}
		return prog;
	}

	@Override
	public Prog visit(Mod m, ScalaWriter w) {
		try {

			// m.listimport_.forEach(i -> i.accept(this, w));

			// Types
			for (AnnDecl decl : elements.get(AbsElementType.TYPE)) {
				// Does NOT use writer.
				decl.accept(this, w);
			}

			functionsWriter = (ScalaWriter) javaWriterSupplier.apply(FUNCTIONS_CLASS_NAME);
			functionsWriter.emitPackage(packageName);
			// jw.emitStaticImports(DEFAULT_STATIC_IMPORTS_PATTERNS);
			functionsWriter.emitEmptyLine();
			//functionsWriter.emitImports(DEFAULT_IMPORTS);
			visitImports(m.listimport_, functionsWriter);
			// jw.emitImports(DEFAULT_IMPORTS_PATTERNS);
			emitPackageLevelImport(functionsWriter);
			functionsWriter.emitEmptyLine();

			beginElementKind(functionsWriter, ElementKind.CONSTRUCTOR, FUNCTIONS_CLASS_NAME, DEFAULT_MODIFIERS, null,
					null, null, false);
			functionsWriter.emitEmptyLine();

			// Data
			for (AnnDecl ad : elements.get(AbsElementType.DATA)) {
				AnnDeclaration decl = (AnnDeclaration) ad;
				String name = getTopLevelDeclIdentifier(decl.decl_);
				ScalaWriter declWriter = (ScalaWriter) javaWriterSupplier.apply(name);
				declWriter.emitPackage(packageName);
				visitImports(m.listimport_, declWriter);
				emitPackageLevelImport(declWriter);
				decl.decl_.accept(this, declWriter);
				close(declWriter, w);

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
				ScalaWriter declWriter = (ScalaWriter) javaWriterSupplier.apply(name);
				declWriter.emitPackage(packageName);
				visitImports(m.listimport_, declWriter);
				decl.accept(this, declWriter);
				close(declWriter, w);
			}

			// Interfaces
			for (AnnDecl ad : elements.get(AbsElementType.INTERFACE)) {
				if (w.checkAwaits) {
					AnnDeclaration decl = (AnnDeclaration) ad;
					decl.accept(this, w);

				} else {
					AnnDeclaration decl = (AnnDeclaration) ad;
					String name = getTopLevelDeclIdentifier(decl.decl_);
					ScalaWriter declWriter = (ScalaWriter) javaWriterSupplier.apply(name);
					declWriter.emitPackage(packageName);
					visitImports(m.listimport_, declWriter);
					emitPackageLevelImport(declWriter);
					decl.accept(this, declWriter);
					close(declWriter, w);
				}
			}

			// Classes
			for (AnnDecl ad : elements.get(AbsElementType.CLASS)) {
				if (w.checkAwaits) {
					AnnDeclaration decl = (AnnDeclaration) ad;
					decl.accept(this, w);
					// System.out.println(programMethods);

				} else {
					AnnDeclaration decl = (AnnDeclaration) ad;
					String name = getTopLevelDeclIdentifier(decl.decl_);
					final String refinedClassName = getRefinedClassName(name);
					ScalaWriter declWriter = (ScalaWriter) javaWriterSupplier.apply(refinedClassName);
					declWriter.emitPackage(packageName);
					visitImports(m.listimport_, declWriter);
					emitPackageLevelImport(declWriter);
					decl.accept(this, declWriter);
					close(declWriter, w);

				}
			}

			visitFunctions(m, w);

			close(functionsWriter, w);
			visitMain(m, w);

			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(AnyImport p, ScalaWriter w) {
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
	public Prog visit(StarFromImport sfi, ScalaWriter w) {
		
		String type = getQTypeName(sfi.qu_, false);
		StringBuilder lctype = new StringBuilder();
	    lctype.append(type.substring(0, 1).toLowerCase());
	    lctype.append(type.substring(1));
		try {
			w.emitImports(lctype + ALL_IMPORTS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// this.staticImports.add(this.packageName + "." + type + ".*");
		return prog;
	}

	@Override
	public Prog visit(DInterf id, ScalaWriter w) {
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
	public Prog visit(DExtends ed, ScalaWriter w) {
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
	public Prog visit(DClass p, ScalaWriter w) {
		// Order to visit 'class':
		// - visit class body including 'methods'
		// - visit 'maybeblock'
		try {
			final String identifier = p.u_;
			final String className = getRefinedClassName(identifier);
			beginElementKind(w, ElementKind.CLASS, className, DEFAULT_MODIFIERS, ABS_API_ACTOR_CLASS, null);
			this.classes.push(className);
			labelMap.put(className, new LinkedList<>());
			w.emitEmptyLine();

			emitImplicitConversions(w);

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
			for (StringWriter continuation : labelMap.get(className)) {
				w.emit(continuation.toString());
			}

			// emitToStringMethod(w);
			w.endType();
			this.classes.pop();
			labelMap.remove(className);

			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(DClassImplements ci, ScalaWriter w) {
		try {
			final String identifier = ci.u_;
			String className = getRefinedClassName(identifier);
			beginElementKind(w, ElementKind.CLASS, className, DEFAULT_MODIFIERS, ABS_API_ACTOR_CLASS,
					toList(ci.listqu_, true));
			this.classes.push(className);
			labelMap.put(className, new LinkedList<>());

			w.emitEmptyLine();

			emitImplicitConversions(w);

			for (ClassBody cb : ci.listclassbody_1) {
				cb.accept(this, w);
			}
			for (ClassBody cb : ci.listclassbody_2) {
				cb.accept(this, w);
			}
			w.beginConstructor(DEFAULT_MODIFIERS, null, null);
			// emitThisActorRegistration(w);
			// emitDefaultRunMethodExecution(w, className);
			ci.maybeblock_.accept(this, w);
			w.endConstructor();
			// emitToStringMethod(w);
			for (StringWriter continuation : labelMap.get(className)) {
				w.emit(continuation.toString());
			}

			w.endType();
			this.classes.pop();

			labelMap.remove(className);
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(DClassPar cpd, ScalaWriter w) {
		try {
			final String identifier = cpd.u_;
			String className = getRefinedClassName(identifier);

			List<String> parameters = new ArrayList<>();
			for (FormalPar param : cpd.listformalpar_) {
				FormalParameter p = (FormalParameter) param;
				String fieldType = getTypeName(p.t_);
				parameters.add(fieldType);
				parameters.add(p.l_);
			}

			beginElementKind(w, ElementKind.CLASS, className, DEFAULT_MODIFIERS, ABS_API_ACTOR_CLASS, null, parameters,
					true);
			this.classes.push(className);
			labelMap.put(className, new LinkedList<>());

			w.emitEmptyLine();

			emitImplicitConversions(w);
			for (ClassBody cb : cpd.listclassbody_1) {
				cb.accept(this, w);
			}
			for (ClassBody cb : cpd.listclassbody_2) {
				cb.accept(this, w);
			}
			// emitToStringMethod(w);
			for (StringWriter continuation : labelMap.get(className)) {
				w.emit(continuation.toString());
			}

			w.beginConstructor(DEFAULT_MODIFIERS, null, null);

			cpd.maybeblock_.accept(this, w);
			w.endConstructor();
			w.emitEmptyLine();

			w.endType();
			this.classes.pop();
			labelMap.remove(className);
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public Prog visit(DClassParImplements cpi, ScalaWriter w) {
		try {
			final String identifier = cpi.u_;
			String className = getRefinedClassName(identifier);
			List<String> parameters = new ArrayList<>();
			for (FormalPar param : cpi.listformalpar_) {
				FormalParameter p = (FormalParameter) param;
				String fieldType = getTypeName(p.t_);
				parameters.add(fieldType);
				parameters.add(p.l_);
			}

			beginElementKind(w, ElementKind.CLASS, className, DEFAULT_MODIFIERS, ABS_API_ACTOR_CLASS,
					toList(cpi.listqu_, true), parameters, true);
			this.classes.push(className);
			labelMap.put(className, new LinkedList<>());

			w.emitEmptyLine();

			emitImplicitConversions(w);

			for (ClassBody cb : cpi.listclassbody_1) {
				cb.accept(this, w);
			}
			for (ClassBody cb : cpi.listclassbody_2) {
				cb.accept(this, w);
			}
			w.emitEmptyLine();
			// emitToStringMethod(w);
			for (StringWriter continuation : labelMap.get(className)) {
				w.emit(continuation.toString());
			}
			w.beginConstructor(DEFAULT_MODIFIERS, null, null);

			cpi.maybeblock_.accept(this, w);
			w.endConstructor();

			w.emitEmptyLine();
			w.endType();
			this.classes.pop();
			labelMap.remove(className);
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(AnnStatement p, ScalaWriter w) {
		p.stm_.accept(this, w);
		return prog;
	}

	@Override
	public Prog visit(Annotation p, ScalaWriter w) {
		p.ann__.accept(this, w);
		return prog;
	}

	@Override
	public Prog visit(AnnDeclaration p, ScalaWriter w) {
		for (Ann a : p.listann_) {
			a.accept(this, w);
		}
		p.decl_.accept(this, w);
		return prog;
	}

	@Override
	public Prog visit(AnnNoType p, ScalaWriter arg) {
		return prog;
	}

	@Override
	public Prog visit(AnnWithType p, ScalaWriter w) {
		visitJavaAnnDecl(p);
		return prog;
	}

	public List<ScalaWriter> continuation(ListAnnStm statements, List<ScalaWriter> currentMethodWriters,
			boolean isInMethod) {

		// System.out.println("Entering continuation creation with awaitCounter
		// " + awaitCounter + "for method "
		// + currentMethod.getName());

		TreeSet<VarDefinition> methodScope = new TreeSet<>();
		variablesInScope.push(methodScope);

		StringWriter scopesw = new StringWriter();
		ScalaWriter scopew = new ScalaWriter(scopesw);
		scopew.isScope = true;

		for (AnnStm stm : statements) {

			AnnStatement as = (AnnStatement) stm;

			int tmp = awaitCounter;
			int tmpS = syncPCounter;
			int tmpA = asyncPCounter;

			as.accept(this, scopew);

			awaitCounter = tmp;
			syncPCounter = tmpS;
			asyncPCounter = tmpA;

			for (ScalaWriter javaWriter : currentMethodWriters) {
				tmp = awaitCounter;
				tmpS = syncPCounter;
				tmpA = asyncPCounter;

				as.accept(this, javaWriter);

				awaitCounter = tmp;
				syncPCounter = tmpS;
				asyncPCounter = tmpA;
			}

			if (as.stm_ instanceof SWhile) {
				SWhile sw = (SWhile) as.stm_;
				SBlock whileBlock = (SBlock) ((AnnStatement) sw.annstm_).stm_;

				List<ScalaWriter> innerAwaits = continuation(whileBlock.listannstm_, new LinkedList<>(), false);

				int tmp2 = awaitCounter;
				int tmpS2 = syncPCounter;
				int tmpA2 = asyncPCounter;

				awaitCounter = tmp;
				syncPCounter = tmpS;
				asyncPCounter = tmpA;

				for (ScalaWriter javaWriter : innerAwaits) {

					tmp = awaitCounter;
					tmpS = syncPCounter;
					tmpA = asyncPCounter;
					as.accept(this, javaWriter);
					awaitCounter = tmp;
					syncPCounter = tmpS;
					asyncPCounter = tmpA;

				}

				currentMethodWriters.addAll(innerAwaits);

				awaitCounter = tmp2;
				syncPCounter = tmpS2;
				asyncPCounter = tmpA2;

				/*
				 * for (JavaWriter javaWriter : currentMethodWriters) {
				 * visitLabelWhile(sw, javaWriter); }
				 */
				// System.out.println("after while check " + awaitCounter);

			}

			// System.out.println(awaitCounter);

			if (as.stm_ instanceof SIfElse) {
				SIfElse sie = (SIfElse) as.stm_;
				SBlock ifBlock = (SBlock) ((Stm) sie.stm_1);
				SBlock elseBlock = (SBlock) ((Stm) sie.stm_2);

				List<ScalaWriter> innerAwaits1 = continuation(ifBlock.listannstm_, new LinkedList<>(), false);
				List<ScalaWriter> innerAwaits2 = continuation(elseBlock.listannstm_, new LinkedList<>(), false);

				currentMethodWriters.addAll(innerAwaits1);
				currentMethodWriters.addAll(innerAwaits2);
				/*
				 * for (JavaWriter javaWriter : currentMethodWriters) {
				 * visitLabelWhile(sw, javaWriter); }
				 */
				// System.out.println("after while check " + awaitCounter);

			}

			if (as.stm_ instanceof SIf) {
				// System.out.println("before while check " + awaitCounter);
				SIf si = (SIf) as.stm_;
				SBlock ifBlock = (SBlock) ((Stm) si.stm_);

				List<ScalaWriter> innerAwaits1 = continuation(ifBlock.listannstm_, new LinkedList<>(), false);

				currentMethodWriters.addAll(innerAwaits1);
				/*
				 * for (JavaWriter javaWriter : currentMethodWriters) {
				 * visitLabelWhile(sw, javaWriter); }
				 */
				// System.out.println("after while check " + awaitCounter);

			}

			if (as.stm_ instanceof SCase) {
				// System.out.println("before while check " + awaitCounter);
				SCase sc = (SCase) as.stm_;

				StringWriter auxsw = new StringWriter();
				ScalaWriter auxw = new ScalaWriter(auxsw);

				fromCase = true;
				sc.pureexp_.accept(this, auxw);

				// caseKey = findVariableTypeInScope(auxsw.toString());

				for (SCaseBranch lcb : sc.listscasebranch_) {
					SCaseB branch = (SCaseB) lcb;
					SBlock caseBlock = (SBlock) ((AnnStatement) ((branch).annstm_)).stm_;

					branch.pattern_.accept(this, scopew);

					TreeSet<VarDefinition> blockScope = new TreeSet<>();
					variablesInScope.push(blockScope);

					if (!variablesBeforeBlock.isEmpty()) {
						for (VarDefinition vd : variablesBeforeBlock) {
							if (!variablesInScope.isEmpty()) {
								variablesInScope.peek().add(vd);
							}
						}
						variablesBeforeBlock.clear();
					}

					List<ScalaWriter> innerAwaits1 = continuation(caseBlock.listannstm_, new LinkedList<>(), false);

					variablesInScope.pop();

					currentMethodWriters.addAll(innerAwaits1);

				}
				fromCase = false;
				caseKey = "";

				/*
				 * for (JavaWriter javaWriter : currentMethodWriters) {
				 * visitLabelWhile(sw, javaWriter); }
				 */
				// System.out.println("after while check " + awaitCounter);

			}

			if (as.stm_ instanceof SAwait) {

				StringBuilder label = new StringBuilder(classes.peek());
				label.append(currentMethod.getName());
				label.append("Await" + (awaitCounter++));
				StringWriter auxsw = new StringWriter();
				ScalaWriter auxw = new ScalaWriter(auxsw);
				auxw.continuationLevel = -1;

				try {
					String returnType = currentMethod.type() != null ? currentMethod.type() : "void";
					List<String> parameters = new ArrayList<>();
					for (TreeSet<VarDefinition> defs : variablesInScope) {
						for (VarDefinition varDefinition : defs) {
							parameters.add(varDefinition.getType());

							parameters.add(getDuplicate(varDefinition.getName(), auxw));
						}
					}
					startContinuation(label, auxw, returnType, parameters);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				currentMethodLabels.add(auxsw);
				currentMethodWriters.add(auxw);

			}

			if (as.stm_ instanceof SExp) {
				SExp exp = (SExp) as.stm_;
				if (exp.exp_ instanceof ExpE) {
					ExpE ee = (ExpE) exp.exp_;
					if (ee.effexp_ instanceof SyncMethCall) {
						SyncMethCall smc = (SyncMethCall) ee.effexp_;
						String methodName = smc.l_.equals(METHOD_GET) ? "get" : smc.l_;
						if (callHasAwait(methodName)) {
							StringBuilder label = new StringBuilder(classes.peek());
							label.append(currentMethod.getName());
							label.append("Await" + (awaitCounter++));

							StringWriter auxsw = new StringWriter();
							ScalaWriter auxw = new ScalaWriter(auxsw);
							auxw.continuationLevel = -1;
							try {
								String returnType = currentMethod.type() != null ? currentMethod.type() : "void";
								List<String> parameters = new ArrayList<>();
								for (TreeSet<VarDefinition> defs : variablesInScope) {
									for (VarDefinition varDefinition : defs) {
										parameters.add(varDefinition.getType());
										parameters.add(getDuplicate(varDefinition.getName(), auxw));
									}
								}

								String smcReturnType = findMethodReturnType(methodName, currentClass(),
										getParameters(smc.listpureexp_, auxw));

								if (smcReturnType != null) {
									parameters.add("ABSFutureTask<?>");
									parameters.add("f_par");
								}

								startContinuation(label, auxw, returnType, parameters);

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							currentMethodLabels.add(auxsw);
							currentMethodWriters.add(auxw);
						}
					}
					if (ee.effexp_ instanceof ThisSyncMethCall) {
						ThisSyncMethCall smc = (ThisSyncMethCall) ee.effexp_;
						String methodName = smc.l_.equals(METHOD_GET) ? "get" : smc.l_;
						if (callHasAwait(methodName)) {
							StringBuilder label = new StringBuilder(classes.peek());
							label.append(currentMethod.getName());
							label.append("Await" + (awaitCounter++));

							StringWriter auxsw = new StringWriter();
							ScalaWriter auxw = new ScalaWriter(auxsw);
							auxw.continuationLevel = -1;
							try {
								String returnType = currentMethod.type() != null ? currentMethod.type() : "void";
								List<String> parameters = new ArrayList<>();
								for (TreeSet<VarDefinition> defs : variablesInScope) {
									for (VarDefinition varDefinition : defs) {
										parameters.add(varDefinition.getType());
										parameters.add(getDuplicate(varDefinition.getName(), auxw));
									}
								}

								String smcReturnType = findMethodReturnType(methodName, currentClass(),
										getParameters(smc.listpureexp_, auxw));

								if (smcReturnType != null) {
									parameters.add("ABSFutureTask<?>");
									parameters.add("f_par");
								}

								startContinuation(label, auxw, returnType, parameters);

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							currentMethodLabels.add(auxsw);
							currentMethodWriters.add(auxw);
						}
					}
				}
			}

			// TODO assignment SDecAss, SFieldAss, Sass
			if (as.stm_ instanceof SAss) {
				SAss sas = (SAss) as.stm_;
				if (sas.exp_ instanceof ExpE) {
					ExpE ee = (ExpE) sas.exp_;
					String methodName = null;
					if (ee.effexp_ instanceof SyncMethCall) {
						SyncMethCall smc = (SyncMethCall) ee.effexp_;
						methodName = smc.l_.equals(METHOD_GET) ? "get" : smc.l_;
					}
					if (ee.effexp_ instanceof ThisSyncMethCall) {
						ThisSyncMethCall smc = (ThisSyncMethCall) ee.effexp_;
						methodName = smc.l_.equals(METHOD_GET) ? "get" : smc.l_;
					}
					if (callHasAwait(methodName)) {
						StringBuilder label = new StringBuilder(classes.peek());
						label.append(currentMethod.getName());
						label.append("Await" + (awaitCounter++));

						StringBuilder varName = new StringBuilder("");

						StringWriter auxsw = new StringWriter();
						ScalaWriter auxw = new ScalaWriter(auxsw);

						varName.append(getDuplicate(sas.l_, auxw));
						StringBuilder futureName = new StringBuilder();
						futureName.append("future_");
						futureName.append(varName.toString());
						auxw.continuationLevel = -1;

						try {
							String returnType = currentMethod.type() != null ? currentMethod.type() : "void";
							List<String> parameters = new ArrayList<>();
							for (TreeSet<VarDefinition> defs : variablesInScope) {
								for (VarDefinition varDefinition : defs) {
									parameters.add(varDefinition.getType());
									parameters.add(getDuplicate(varDefinition.getName(), auxw));
								}
							}
							parameters.add(String.format("ABSFutureTask[%s]", findVariableTypeInScope(sas.l_)));
							parameters.add(futureName.toString());

							startContinuation(label, auxw, returnType, parameters);

							auxw.emitStatement("%s = %s.get()", varName, futureName);

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						currentMethodLabels.add(auxsw);
						currentMethodWriters.add(auxw);
					}

				}
			}

			if (as.stm_ instanceof SDecAss) {
				SDecAss das = (SDecAss) as.stm_;
				if (das.exp_ instanceof ExpE) {
					ExpE ee = (ExpE) das.exp_;
					String methodName = null;
					if (ee.effexp_ instanceof SyncMethCall) {
						SyncMethCall smc = (SyncMethCall) ee.effexp_;
						methodName = smc.l_.equals(METHOD_GET) ? "get" : smc.l_;
					}
					if (ee.effexp_ instanceof ThisSyncMethCall) {
						ThisSyncMethCall smc = (ThisSyncMethCall) ee.effexp_;
						methodName = smc.l_.equals(METHOD_GET) ? "get" : smc.l_;
					}
					if (callHasAwait(methodName)) {

						StringBuilder label = new StringBuilder(classes.peek());
						label.append(currentMethod.getName());
						label.append("Await" + (awaitCounter++));

						StringBuilder varName = new StringBuilder("");

						StringWriter auxsw = new StringWriter();
						ScalaWriter auxw = new ScalaWriter(auxsw);
						auxw.continuationLevel = -1;

						varName.append(getDuplicate(das.l_, auxw));

						StringBuilder futureName = new StringBuilder();
						futureName.append("future_");
						futureName.append(varName.toString());

						try {
							String returnType = currentMethod.type() != null ? currentMethod.type() : "void";
							List<String> parameters = new ArrayList<>();
							for (TreeSet<VarDefinition> defs : variablesInScope) {
								for (VarDefinition varDefinition : defs) {
									if (!varDefinition.getName().equals(das.l_)) {
										parameters.add(varDefinition.getType());
										parameters.add(getDuplicate(varDefinition.getName(), auxw));
									}
								}
							}
							parameters.add(String.format("ABSFutureTask[%s]", getTypeName(das.t_)));
							parameters.add(futureName.toString());

							startContinuation(label, auxw, returnType, parameters);
							auxw.emitStatement("var %s : %s = %s.get()", varName, getTypeName(das.t_), futureName);

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						currentMethodLabels.add(auxsw);
						currentMethodWriters.add(auxw);
					}

				}
			}

			if (as.stm_ instanceof SFieldAss) {
				SFieldAss fas = (SFieldAss) as.stm_;
				if (fas.exp_ instanceof ExpE) {
					ExpE ee = (ExpE) fas.exp_;
					String methodName = null;
					if (ee.effexp_ instanceof SyncMethCall) {
						SyncMethCall smc = (SyncMethCall) ee.effexp_;
						methodName = smc.l_.equals(METHOD_GET) ? "get" : smc.l_;
					}
					if (ee.effexp_ instanceof ThisSyncMethCall) {
						ThisSyncMethCall smc = (ThisSyncMethCall) ee.effexp_;
						methodName = smc.l_.equals(METHOD_GET) ? "get" : smc.l_;
					}
					if (callHasAwait(methodName)) {
						StringBuilder label = new StringBuilder(classes.peek());
						label.append(currentMethod.getName());
						label.append("Await" + (awaitCounter++));

						StringBuilder futureName = new StringBuilder();
						futureName.append("future_");
						futureName.append(fas.l_);

						StringWriter auxsw = new StringWriter();
						ScalaWriter auxw = new ScalaWriter(auxsw);
						auxw.continuationLevel = -1;
						try {
							String returnType = currentMethod.type() != null ? currentMethod.type() : "void";
							List<String> parameters = new ArrayList<>();
							for (TreeSet<VarDefinition> defs : variablesInScope) {
								for (VarDefinition varDefinition : defs) {
									parameters.add(varDefinition.getType());
									parameters.add(getDuplicate(varDefinition.getName(), auxw));
								}
							}
							parameters.add(String.format("ABSFutureTask[%s]", findVariableTypeInScope(fas.l_)));
							parameters.add(futureName.toString());

							startContinuation(label, auxw, returnType, parameters);
							auxw.emitStatement("this.%s = %s.get()", fas.l_, futureName);

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						currentMethodLabels.add(auxsw);
						currentMethodWriters.add(auxw);
					}

				}
			}

		}
		variablesInScope.pop();

		if (isInMethod)

		{
			for (ScalaWriter javaWriter : currentMethodWriters) {
				try {
					javaWriter.endMethod();
					javaWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			for (ScalaWriter javaWriter : currentMethodWriters) {
				javaWriter.continuationLevel = variablesInScope.size();
			}
		}
		return currentMethodWriters;

	}

	public void visitStatementsBlock(ListAnnStm statements, ScalaWriter w) {
		{
			StringWriter awaitsw = new StringWriter();
			ScalaWriter awaitw = new ScalaWriter(awaitsw);
			Boolean awaitEncountered = false;
			SAwait sa = null;
			StringWriter guardsw = new StringWriter();
			ScalaWriter guardw = new ScalaWriter(guardsw);

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
						w.emitStatement("%s await(%s,%s)", currentMethod.type() == null ? "" : "return",
								guardsw.toString(), continuationName);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}

	}

	@Override
	public Prog visit(JustBlock jb, ScalaWriter w) {
		for (AnnStm stm : jb.listannstm_) {
			stm.accept(this, w);
		}
		return prog;
	}

	@Override
	public Prog visit(NoBlock nb, ScalaWriter w) {
		return prog;
	}

	@Override
	public Prog visit(MethSignature ms, ScalaWriter w) {
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
	public Prog visit(FieldClassBody p, ScalaWriter w) {
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
	public Prog visit(FieldAssignClassBody p, ScalaWriter w) {
		try {
			String fieldType = getTypeName(p.t_);
			String fieldName = p.l_;
			StringWriter auxsw = new StringWriter();
			ScalaWriter auxw = new ScalaWriter(auxsw);
			auxw.continuationLevel = w.continuationLevel;
			auxw.duplicateReplacements = w.duplicateReplacements;
			p.pureexp_.accept(this, auxw);
			emitField(w, fieldType, fieldName, auxsw.toString(), false);
			createVarDefinition(fieldName, fieldType);

			w.emitEmptyLine();
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void visitMethodBody(MethClassBody mcb, ScalaWriter notNeeded) {
		for (AnnStm annStm : mcb.listannstm_) {
			annStm.accept(this, notNeeded);
		}
	}

	@Override
	public Prog visit(MethClassBody mcb, ScalaWriter w) {
		try {

			if (w.checkAwaits) {
				String returnType = getTypeName(mcb.t_);
				String name = mcb.l_.equals(METHOD_GET) ? "get" : mcb.l_;
				List<String> parameterTypes = new ArrayList<>();
				for (FormalPar param : mcb.listformalpar_) {
					FormalParameter p = (FormalParameter) param;
					String paramType = getTypeName(p.t_);
					parameterTypes.add(paramType);
					// createVarDefinition(p.l_, paramType);
				}
				createMethodDefinition(returnType, name, parameterTypes);
				visitMethodBody(mcb, w);
			} else {

				variablesInScope.clear();
				TreeSet<VarDefinition> methodScope = new TreeSet<>();
				variablesInScope.push(methodScope);
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
					VarDefinition vd = createVarDefinition(p.l_, paramType);
					if (!variablesInScope.isEmpty()) {
						variablesInScope.peek().add(vd);
					}
				}
				// System.out.println(variablesInScope);

				w.beginMethod(returnType, name, DEFAULT_MODIFIERS, parameters, Collections.emptyList());
				createMethodDefinition(returnType, name, parameterTypes);

				ListAnnStm copyOfMcb = (ListAnnStm) mcb.listannstm_.clone();

				TreeSet<VarDefinition> blockScope = new TreeSet<>();
				variablesInScope.push(blockScope);

				awaitCounter = 0;
				asyncPCounter = 0;
				syncPCounter = 0;

				System.out.println("Building method for " + name);
				for (AnnStm annStm : mcb.listannstm_) {
					annStm.accept(this, w);
				}
				System.out.println("Done method for " + name);

				variablesInScope.pop();

				awaitCounter = 0;
				asyncPCounter = 0;
				syncPCounter = 0;

				System.out.println("Building continuations for " + name);
				continuation(copyOfMcb, new LinkedList<>(), true);
				// visitStatementsBlock(mcb.listannstm_, w);
				variablesInScope.pop();
				System.out.println("Done continuations for " + name);
				variablesInScope.clear();

				w.endMethod();

				for (StringWriter stringWriter : currentMethodLabels) {
					labelMap.get(this.classes.peek()).add(stringWriter);

				}
				currentMethodLabels.clear();

				w.emitEmptyLine();
			}
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(SBlock b, ScalaWriter w) {

		TreeSet<VarDefinition> blockScope = new TreeSet<>();
		variablesInScope.push(blockScope);
		if (w.continuationLevel != -5)
			w.continuationLevel++;

		HashMap<String, String> duplicateScope = new HashMap<>();
		w.duplicateReplacements.push(duplicateScope);

		if (!variablesBeforeBlock.isEmpty()) {
			for (VarDefinition vd : variablesBeforeBlock) {
				if ((w.continuationLevel > -1 || w.avoidDuplicates)) {
					w.duplicateReplacements.peek().put(vd.getName(), "w_" + awaitCounter + "$" + vd.getName());
				}

				if (!variablesInScope.isEmpty()) {
					variablesInScope.peek().add(vd);
				}
			}
			variablesBeforeBlock.clear();
		}

		// System.out.println("new block " + w.duplicateReplacements);
		for (AnnStm stm : b.listannstm_) {
			stm.accept(this, w);
		}
		// System.out.println(variablesInScope);
		w.duplicateReplacements.pop();
		variablesInScope.pop();
		if (w.continuationLevel > -5)
			w.continuationLevel--;
		return prog;

	}

	@Override
	public Prog visit(SAwait await, ScalaWriter w) {
		if (w.checkAwaits && !currentMethod.containsAwait()) {
			currentMethod.setContainsAwait(true);
			awaitsDetected = true;
		}
		StringWriter auxsw = new StringWriter();
		ScalaWriter auxw = new ScalaWriter(auxsw);
		auxw.continuationLevel = w.continuationLevel;
		auxw.duplicateReplacements = w.duplicateReplacements;
		await.awaitguard_.accept(this, auxw);
		StringBuilder label = new StringBuilder(classes.peek());
		label.append(currentMethod.getName());

		if (!w.isScope && !w.checkAwaits) {
			label.append("Await" + (awaitCounter++));
		}
		List<String> parameters = new ArrayList<>();
		for (TreeSet<VarDefinition> defs : variablesInScope) {
			for (VarDefinition varDefinition : defs) {
				parameters.add(varDefinition.getName());
			}
		}
		String methodCall = generateJavaMethodInvocation("this", label.toString(), parameters, w, 'w', awaitCounter);
		try {
			if (currentMethod.isVoid())
				w.emitStatement("await(Guard.convert(%s),%s)", auxsw.toString(), "()=>" + methodCall);
			else
				w.emitStatement("await(%s, Guard.convert(%s))", "()=>" + methodCall, auxsw.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prog;

	}

	@Override
	public Prog visit(GFutField p, ScalaWriter w) {
		try {
			w.emit(LITERAL_THIS + "." + p.l_);
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
		return prog;
	}

	@Override
	public Prog visit(GExp p, ScalaWriter w) {
		try {
			StringWriter suppliersw = new StringWriter();
			ScalaWriter supplierw = new ScalaWriter(suppliersw);
			supplierw.continuationLevel = w.continuationLevel;
			supplierw.duplicateReplacements = w.duplicateReplacements;
			p.pureexp_.accept(this, supplierw);
			w.beginControlFlow("new Supplier[Boolean]");
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
	public Prog visit(GAnd p, ScalaWriter w) {
		try {
			StringWriter suppliersw = new StringWriter();
			ScalaWriter supplierw = new ScalaWriter(suppliersw);

			supplierw.continuationLevel = w.continuationLevel;
			supplierw.duplicateReplacements = w.duplicateReplacements;

			StringWriter suppliersw2 = new StringWriter();
			ScalaWriter supplierw2 = new ScalaWriter(suppliersw);
			supplierw2.continuationLevel = w.continuationLevel;
			supplierw2.duplicateReplacements = w.duplicateReplacements;

			StringBuilder supplierName = new StringBuilder();
			supplierName.append("supplier_" + Math.abs(p.hashCode()));
			p.awaitguard_1.accept(this, supplierw);
			p.awaitguard_2.accept(this, supplierw2);
			w.emit("new ConjunctionGuard(Guard.convert(" + suppliersw.toString() + "),Guard.convert("
					+ suppliersw2.toString() + "))");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prog;
	}

	@Override
	public Prog visit(GFut vg, ScalaWriter w) {
		try {

			w.emit(getDuplicate(vg.l_, w));
			return prog;
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	public Prog visit(SWhile sw, ScalaWriter w) {
		// TODO Continue preprocessing from here with the three while functions.
		try {
			StringWriter auxsw = new StringWriter();
			ScalaWriter auxw = new ScalaWriter(auxsw);
			auxw.continuationLevel = w.continuationLevel;
			auxw.duplicateReplacements = w.duplicateReplacements;

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
	public Prog visit(SIf sif, ScalaWriter w) {
		try {
			StringWriter auxsw = new StringWriter();
			ScalaWriter auxw = new ScalaWriter(auxsw);
			auxw.continuationLevel = w.continuationLevel;
			auxw.duplicateReplacements = w.duplicateReplacements;
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
	public Prog visit(SIfElse se, ScalaWriter w) {
		try {
			StringWriter auxsw = new StringWriter();
			ScalaWriter auxw = new ScalaWriter(auxsw);
			auxw.continuationLevel = w.continuationLevel;
			auxw.duplicateReplacements = w.duplicateReplacements;
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
	public Prog visit(SCase p, ScalaWriter w) {
		try {
			StringWriter auxsw = new StringWriter();
			ScalaWriter auxw = new ScalaWriter(auxsw);
			auxw.continuationLevel = w.continuationLevel;
			auxw.duplicateReplacements = w.duplicateReplacements;

			if (!w.checkAwaits)
				fromCase = true;
			p.pureexp_.accept(this, auxw);
			w.beginControlFlow("%s match ", auxsw);

			// if (!w.checkAwaits) {
			// caseKey = findVariableTypeInScope(auxsw.toString());
			// System.out.println("#############" + caseKey + " " +
			// auxsw.toString() + "#################");
			// }
			for (SCaseBranch scb : p.listscasebranch_) {
				scb.accept(this, w);
			}
			caseKey = "";
			fromCase = false;
			w.endControlFlow();

			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(SCaseB cb, ScalaWriter w) {
		try {

			StringWriter patsw = new StringWriter();
			ScalaWriter patw = new ScalaWriter(patsw);
			patw.continuationLevel = w.continuationLevel;
			patw.duplicateReplacements = w.duplicateReplacements;
			cb.pattern_.accept(this, patw);

			StringWriter stmsw = new StringWriter();
			ScalaWriter stmw = new ScalaWriter(stmsw);
			stmw.continuationLevel = w.continuationLevel;
			stmw.duplicateReplacements = w.duplicateReplacements;
			stmw.checkAwaits = w.checkAwaits;

			cb.annstm_.accept(this, stmw);
			// TODO1: Add declarations inside block

			w.emitStatement("case %s => %s", patsw.toString(), stmsw.toString());
			// variablesBeforeBlock.clear();
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(SDec p, ScalaWriter w) {
		try {
			String fieldType = getTypeName(p.t_);
			String fieldName = p.l_;

			VarDefinition vd = createVarDefinition(fieldName, fieldType);
			if ((w.continuationLevel > -1 || w.avoidDuplicates)) {
				w.duplicateReplacements.peek().put(fieldName, "w_" + awaitCounter + "$" + fieldName);
			}

			emitField(w, fieldType, fieldName, null, false);

			if (!variablesInScope.isEmpty()) {
				variablesInScope.peek().add(vd);
			}
			verifyJavaStatic(fieldType, fieldName);
			w.emitEmptyLine();
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(SFieldAss fa, ScalaWriter w) {
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
	public Prog visit(SAss ss, ScalaWriter w) {
		try {
			Exp exp = ss.exp_;
			String varName = ss.l_;

			String type = "";

			/*
			 * if (w.continuationLevel >= -1 || w.avoidDuplicates) {
			 * if(w.methodParameters.containsKey(varName)){
			 * duplicateReplacements.peek().put(varName, "w_" + awaitCounter +
			 * "$" + varName); type=w.methodParameters.get(varName); } }
			 */

			String varType = findVariableType(varName);
			if (exp instanceof ExpE == false) {
				visitStatementAssignmentExp(exp, varName, type.length() == 0 ? null : type, w);
			} else if (exp instanceof ExpE) {
				ExpE expe = (ExpE) exp;
				EffExp effExp = expe.effexp_;
				if (effExp instanceof AsyncMethCall) {
					AsyncMethCall amc = (AsyncMethCall) effExp;
					visitAsyncMethodCall(amc, type.length() == 0 ? varType : type, varName, type.length() > 0, w);
				} else if (effExp instanceof SyncMethCall) {
					SyncMethCall smc = (SyncMethCall) effExp;
					visitSyncMethodCall_Sync(smc, type.length() == 0 ? varType : type, varName, true, w);
				} else if (effExp instanceof ThisSyncMethCall) {
					ThisSyncMethCall tsmc = (ThisSyncMethCall) effExp;
					SyncMethCall smc = new SyncMethCall(new ELit(new LThis()), tsmc.l_, tsmc.listpureexp_);
					visitSyncMethodCall_Sync(smc, type.length() == 0 ? varType : type, varName, true, w);
				} else {
					visitStatementAssignmentExp(exp, varName, type.length() == 0 ? null : type, w);
				}
			}
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(SDecAss p, ScalaWriter w) {
		try {

			String varType = getTypeName(p.t_);
			varType = VOID_PRIMITIVE_NAME.equals(varType) ? Object.class.getSimpleName() : varType;
			String varName = p.l_;
			VarDefinition vd = createVarDefinition(varName, varType);
			if ((w.continuationLevel > -1 || w.avoidDuplicates)) {
				w.duplicateReplacements.peek().put(varName, "w_" + awaitCounter + "$" + varName);
			}

			if (!variablesInScope.isEmpty()) {
				variablesInScope.peek().add(vd);
			}

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
	public Prog visit(SReturn r, ScalaWriter w) {
		try {
			StringWriter auxsw = new StringWriter();
			ScalaWriter auxw = new ScalaWriter(auxsw);
			auxw.continuationLevel = w.continuationLevel;
			auxw.duplicateReplacements = w.duplicateReplacements;
			r.exp_.accept(this, auxw);
			w.emitStatement("return " + auxsw.toString());
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(SSuspend ss, ScalaWriter w) {
		try {
			w.emitSingleLineComment("suspend;");
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(SSkip sk, ScalaWriter w) {
		try {
			w.emitSingleLineComment("skip;");
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(SExp p, ScalaWriter w) {
		try {
			Exp exp = p.exp_;
			StringWriter auxsw = new StringWriter();
			ScalaWriter auxjw = new ScalaWriter(auxsw);
			auxjw.duplicateReplacements = w.duplicateReplacements;
			auxjw.continuationLevel = w.continuationLevel;
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

	public Prog visit(SThrow st, ScalaWriter w) {
		try {
			StringWriter auxsw = new StringWriter();
			ScalaWriter auxw = new ScalaWriter(auxsw);
			auxw.continuationLevel = w.continuationLevel;
			auxw.duplicateReplacements = w.duplicateReplacements;
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
	public Prog visit(STryCatchFinally stcf, ScalaWriter w) {
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
	public Prog visit(SPrintln p, ScalaWriter w) {
		try {
			StringWriter auxsw = new StringWriter();
			ScalaWriter auxw = new ScalaWriter(auxsw);
			auxw.continuationLevel = w.continuationLevel;
			auxw.duplicateReplacements = w.duplicateReplacements;
			p.pureexp_.accept(this, auxw);

			w.emitStatement("println( " + auxsw.toString() + ")");
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
	public Prog visit(ExpE ee, ScalaWriter w) {
		ee.effexp_.accept(this, w);
		return prog;
	}

	@Override
	public Prog visit(ExpP ep, ScalaWriter w) {
		ep.pureexp_.accept(this, w);
		return prog;
	}

	@Override
	public Prog visit(ELit elit, ScalaWriter w) {
		elit.literal_.accept(this, w);
		return prog;
	}

	@Override
	public Prog visit(EAdd e, ScalaWriter w) {
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
	public Prog visit(ESub e, ScalaWriter w) {
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
	public Prog visit(EMul e, ScalaWriter w) {
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
	public Prog visit(EDiv e, ScalaWriter w) {
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
	public Prog visit(EMod e, ScalaWriter w) {
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
	public Prog visit(EGe e, ScalaWriter w) {
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
	public Prog visit(EGt e, ScalaWriter w) {
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
	public Prog visit(ELe e, ScalaWriter w) {
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
	public Prog visit(ELt lt, ScalaWriter w) {
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
	public Prog visit(EAnd e, ScalaWriter w) {
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
	public Prog visit(EOr e, ScalaWriter w) {
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
	public Prog visit(ENeq e, ScalaWriter w) {
		try {
			w.beginExpressionGroup();
			StringWriter auxsw = new StringWriter();
			ScalaWriter auxw = new ScalaWriter(auxsw);
			auxw.continuationLevel = w.continuationLevel;
			auxw.duplicateReplacements = w.duplicateReplacements;

			e.pureexp_1.accept(this, auxw);
			String firstArg = auxsw.toString();
			StringWriter auxsw2 = new StringWriter();
			ScalaWriter auxw2 = new ScalaWriter(auxsw2);
			auxw2.continuationLevel = w.continuationLevel;
			auxw2.duplicateReplacements = w.duplicateReplacements;

			e.pureexp_2.accept(this, auxw2);
			String secondArg = auxsw2.toString();
			w.emit(String.format("!Objects.equals(%s, %s)", firstArg, secondArg));
			w.endExpressionGroup();
			return prog;
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	public Prog visit(EIntNeg in, ScalaWriter w) {
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
	public Prog visit(ELogNeg ln, ScalaWriter w) {
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
	public Prog visit(EEq e, ScalaWriter w) {
		try {
			w.beginExpressionGroup();
			StringWriter auxsw = new StringWriter();
			ScalaWriter auxw = new ScalaWriter(auxsw);
			auxw.continuationLevel = w.continuationLevel;
			auxw.duplicateReplacements = w.duplicateReplacements;

			e.pureexp_1.accept(this, auxw);
			String firstArg = auxsw.toString();
			StringWriter auxsw2 = new StringWriter();
			ScalaWriter auxw2 = new ScalaWriter(auxsw2);
			auxw2.continuationLevel = w.continuationLevel;
			auxw2.duplicateReplacements = w.duplicateReplacements;

			e.pureexp_2.accept(this, auxw2);
			String secondArg = auxsw2.toString();
			w.emit(String.format("Objects.equals(%s, %s)", firstArg, secondArg));
			w.endExpressionGroup();
			return prog;
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	public Prog visit(EVar v, ScalaWriter w) {
		try {
			if (fromCase)
				caseKey = findVariableTypeInScope(v.l_);
			w.emit(translate(getDuplicate(v.l_, w)));
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
		return prog;

	}

	@Override
	public Prog visit(EField t, ScalaWriter w) {
		try {
			if (fromCase)
				caseKey = findVariableTypeInScope(t.l_);
			w.emit("this." + t.l_);
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
		return prog;
	}

	@Override
	public Prog visit(New n, ScalaWriter w) {
		try {
			List<String> parameters = new ArrayList<>();
			for (PureExp par : n.listpureexp_) {
				StringWriter parSW = new StringWriter();
				ScalaWriter parameterWriter = new ScalaWriter(parSW);
				parameterWriter.continuationLevel = w.continuationLevel;
				parameterWriter.duplicateReplacements = w.duplicateReplacements;
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
	public Prog visit(Get g, ScalaWriter w) {
		try {
			StringWriter auxsw = new StringWriter();
			ScalaWriter auxw = new ScalaWriter(auxsw);
			auxw.continuationLevel = w.continuationLevel;
			auxw.duplicateReplacements = w.duplicateReplacements;
			g.pureexp_.accept(this, auxw);
			w.emit(auxsw.toString() + ".get()");
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(AsyncMethCall amc, ScalaWriter w) {
		try {
			visitAsyncMethodCall(amc, null, null, false, w);
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(SyncMethCall smc, ScalaWriter w) {
		try {
			visitSyncMethodCall_Sync(smc, null, null, false, w);
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(ThisSyncMethCall p, ScalaWriter w) {
		try {
			SyncMethCall smc = new SyncMethCall(new ELit(new LThis()), p.l_, p.listpureexp_);
			visitSyncMethodCall_Sync(smc, null, null, false, w);
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(JustFinally jf, ScalaWriter w) {
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
	public Prog visit(NoFinally p, ScalaWriter arg) {
		// XXX ?
		return prog;
	}

	@Override
	public Prog visit(LFloat p, ScalaWriter w) {
		try {
			w.emit(Double.toString(p.double_));
			return prog;
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	public Prog visit(LInt i, ScalaWriter w) {
		try {
			w.emit(Long.toString(i.integer_));
			return prog;
		} catch (IOException x) {
			throw new RuntimeException(x);
		}

	}

	@Override
	public Prog visit(LNull n, ScalaWriter w) {
		try {
			w.emit(LITERAL_NULL);
			return prog;
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	public Prog visit(LStr s, ScalaWriter w) {
		try {

			w.emit("\"" + s.string_ + "\"");
			return prog;
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	public Prog visit(LThis t, ScalaWriter w) {
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
	public Prog visit(AnyExport p, ScalaWriter arg) {
		logNotSupported("#visit(%s): %s", p, p.listqa_);
		return prog;
	}

	@Override
	public Prog visit(AnyFromExport p, ScalaWriter arg) {
		logNotSupported("#visit(%s): %s", p, p.listqa_);
		return prog;
	}

	@Override
	public Prog visit(StarExport p, ScalaWriter arg) {
		logNotSupported("#visit(%s)", p);
		return prog;
	}

	@Override
	public Prog visit(StarFromExport p, ScalaWriter arg) {
		logNotSupported("#visit(%s): %s", p, p.qu_);
		return prog;
	}

	@Override
	public Prog visit(AnyFromImport p, ScalaWriter arg) {
		logNotSupported("#visit(%s): %s", p, p.listqa_);
		return prog;
	}

	@Override
	public Prog visit(YesForeign fi, ScalaWriter arg) {
		return prog;
	}

	@Override
	public Prog visit(NoForeign ni, ScalaWriter arg) {
		return prog;
	}

	@Override
	public Prog visit(TInfer p, ScalaWriter arg) {
		logNotImplemented("#visit(%s)", p);
		return prog;
	}

	@Override
	public Prog visit(TSimple t, ScalaWriter w) {
		try {
			w.emit(toString(t));
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(TPoly t, ScalaWriter w) {
		try {

			w.emit(toString(t));
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(DType adt, ScalaWriter arg) {
		String adtName = adt.u_;
		String typeName = getTypeName(adt.t_);
		this.javaTypeTranslator.registerAbstractType(adtName, typeName);
		return prog;
	}

	@Override
	public Prog visit(DTypePoly adt, ScalaWriter arg) {
		String adtName = adt.u_;
		String typeName = getTypeName(adt.t_);
		this.javaTypeTranslator.registerAbstractType(adtName, typeName);
		logNotSupported("Parametric T Declaration not supported: %s", adt.listu_);
		return prog;
	}

	@Override
	public Prog visit(DException ed, ScalaWriter w) {
		try {
			w.emitEmptyLine();
			ConstrIdent ex = ed.constrident_;
			String className = getExceptionClassName(ex);
			this.exceptionDeclaraions.add(className);
			Collection<String> implementingCaseInterface = Collections.emptyList();
			beginElementKind(w, ElementKind.CLASS, className, EnumSet.of(Modifier.PUBLIC),
					RuntimeException.class.getSimpleName(), implementingCaseInterface, null, false);
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
	public Prog visit(DData dd, ScalaWriter w) {
		try {

			w.emitEmptyLine();
			Set<Modifier> mods = new HashSet<>(DEFAULT_MODIFIERS);
			mods.add(Modifier.ABSTRACT);
//			String parentOrder = String.format("%s[%s]", ORDERED_INTERFACE_CLASS, dd.u_);
			beginElementKind(w, ElementKind.CLASS, dd.u_, mods, null, null);
			w.endType();

			String parentDataInterface = dd.u_;
			// Define parent 'data' holder interface
			this.dataDeclarations.put(dd.u_, parentDataInterface);
			// Each data declaration as an implementing class
			ListConstrIdent lci = dd.listconstrident_;
			for (ConstrIdent constrIdent : lci) {
				if (constrIdent instanceof SinglConstrIdent) {
					SinglConstrIdent sci = (SinglConstrIdent) constrIdent;
					beginElementKind(w, ElementKind.OTHER, sci.u_, DEFAULT_MODIFIERS, parentDataInterface, null,
							new ArrayList<>(), false);
					w.endType();
				} else if (constrIdent instanceof ParamConstrIdent) {
					ParamConstrIdent pci = (ParamConstrIdent) constrIdent;
					List<String> parameters = new ArrayList<>();
					List<String> types = new ArrayList<>();
					int currentSpot = 0;
					for (ConstrType ct : pci.listconstrtype_) {

						StringWriter auxsw = new StringWriter();
						ScalaWriter auxw = new ScalaWriter(auxsw, true);

						ct.accept(this, auxw);
						String[] pars = auxsw.toString().split("#");

						parameters.add(pars[0]);
						types.add(pars[0]);

						if (pars.length == 2) {
							parameters.add(pars[1]);
							functionsWriter.beginMethod(pars[0], pars[1], DEFAULT_MODIFIERS, dd.u_, dd.u_.toLowerCase() + "par");
							functionsWriter.beginControlFlow("%s match", dd.u_.toLowerCase() + "par");
							StringBuilder matchList = new StringBuilder();
							for (int i = 0; i < pci.listconstrtype_.size(); i++) {
								if (i == currentSpot) {
									matchList.append(pars[1] + "p");
								} else
									matchList.append('_');
								if (i < pci.listconstrtype_.size() - 1)
									matchList.append(',');
							}
							functionsWriter.emitStatement("case %s(%s) => %s", pci.u_, matchList, pars[1] + "p");
							functionsWriter.endControlFlow();
							functionsWriter.endMethod();
						}

						else {
							if (pars[0].contains("["))
								parameters.add(pars[0].substring(0, pars[0].indexOf('[')).toLowerCase());
							else
								parameters.add(pars[0].toLowerCase());
						}
						currentSpot++;

					}
					paramConstructs.put(pci.u_, types);

					beginElementKind(w, ElementKind.OTHER, pci.u_, DEFAULT_MODIFIERS, parentDataInterface, null,
							parameters, false);
					w.endType();

				}
			}

			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(DDataPoly dpd, ScalaWriter w) {
		try {

			Set<Modifier> mods = new HashSet<>(DEFAULT_MODIFIERS);
			mods.add(Modifier.ABSTRACT);
			beginElementKind(w, ElementKind.CLASS, String.format("%s%s", dpd.u_, dpd.listu_), mods,
					null, null);
			//ORDERED_INTERFACE_CLASS + "[" + dpd.u_ + dpd.listu_ + "]"
			w.endType();

			caseTypeConstructs.put(dpd.u_, new ArrayList<>());

			String parentDataInterface = dpd.u_;
			// Define parent 'data' holder interface
			this.dataDeclarations.put(dpd.u_, parentDataInterface);
			// Each data declaration as an implementing class
			ListConstrIdent lci = dpd.listconstrident_;
			for (ConstrIdent constrIdent : lci) {
				if (constrIdent instanceof SinglConstrIdent) {
					SinglConstrIdent sci = (SinglConstrIdent) constrIdent;
					beginElementKind(w, ElementKind.OTHER, String.format("%s%s", sci.u_, dpd.listu_), DEFAULT_MODIFIERS,
							String.format("%s%s", dpd.u_, dpd.listu_), null, new ArrayList<>(), false);
					w.endType();
				} else if (constrIdent instanceof ParamConstrIdent) {
					ParamConstrIdent pci = (ParamConstrIdent) constrIdent;
					List<String> parameters = new ArrayList<>();
					List<String> types = new ArrayList<>();
					int currentSpot = 0;
					for (ConstrType ct : pci.listconstrtype_) {
						StringWriter auxsw = new StringWriter();
						ScalaWriter auxw = new ScalaWriter(auxsw, true);

						ct.accept(this, auxw);
						String[] pars = auxsw.toString().split("#");

						parameters.add(pars[0]);
						types.add(pars[0]);

						if (pars.length == 2) {
							parameters.add(pars[1]);
							String fieldPar = dpd.u_.toLowerCase() + "par";
							System.out.println(fieldPar);
							functionsWriter.beginMethod(pars[0], pars[1] + dpd.listu_, DEFAULT_MODIFIERS,
									dpd.u_ + dpd.listu_, fieldPar);
							functionsWriter.beginControlFlow("%s match", fieldPar);
							StringBuilder matchList = new StringBuilder();
							for (int i = 0; i < pci.listconstrtype_.size(); i++) {
								if (i == currentSpot) {
									matchList.append(pars[1] + "p");
								} else
									matchList.append('_');
								if (i < pci.listconstrtype_.size() - 1)
									matchList.append(',');
							}
							functionsWriter.emitStatement("case %s(%s) => %s", pci.u_, matchList, pars[1] + "p");
							functionsWriter.endControlFlow();
							functionsWriter.endMethod();
						}

						else {
							if (pars[0].contains("["))
								parameters.add(pars[0].substring(0, pars[0].indexOf('[')).toLowerCase());
							else
								parameters.add(pars[0].toLowerCase());

						}
						currentSpot++;

					}

					paramConstructs.put(pci.u_, types);
					caseTypeConstructs.get(dpd.u_).add(pci.u_);

					beginElementKind(w, ElementKind.OTHER, String.format("%s%s", pci.u_, dpd.listu_), DEFAULT_MODIFIERS,
							String.format("%s%s", dpd.u_, dpd.listu_), null, parameters, false);
					w.endType();

				}
			}

			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(DFun f, ScalaWriter w) {
		try {
			this.classes.push("Functions");
			String methodName = f.l_;
			String methodType = getTypeName(f.t_);
			List<String> parameters = new ArrayList<>();
			for (FormalPar param : f.listformalpar_) {
				FormalParameter parameter = (FormalParameter) param;
				String paramType = getTypeName(parameter.t_);
				parameters.add(paramType);
				parameters.add(parameter.l_);
				/*
				 * VarDefinition vd = createVarDefinition(parameter.l_,
				 * paramType); if (!variablesInScope.isEmpty()) {
				 * variablesInScope.peek().add(vd); }
				 */
			}
			Set<Modifier> modifiers = Sets.newHashSet(Modifier.PUBLIC, Modifier.STATIC);

			w.beginMethod(methodType, methodName, modifiers, parameters.toArray(new String[0]));
			createMethodDefinition(methodType, methodName, parameters);
			FunBody fbody = f.funbody_;
			if (fbody instanceof BuiltinFunBody) {
				logNotImplemented("builtin function body: %s %s", methodType, methodName);
			} else if (fbody instanceof NormalFunBody) {
				NormalFunBody nfb = (NormalFunBody) fbody;
				PureExp pe = nfb.pureexp_;

				StringWriter sw = new StringWriter();
				ScalaWriter auxjw = new ScalaWriter(sw);

				auxjw.continuationLevel = w.continuationLevel;
				auxjw.duplicateReplacements = w.duplicateReplacements;

				pe.accept(this, auxjw);
				String stm = sw.toString();
				w.emitStatement("return %s", stm);

			}
			w.endMethod();
			w.emitEmptyLine();

			this.classes.push("Functions");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return prog;
	}

	@Override
	public Prog visit(DFunPoly fpd, ScalaWriter w) {
		try {
			this.classes.push("Functions");
			String methodName = fpd.l_;
			String methodType = getTypeName(fpd.t_);
			List<String> parameters = new ArrayList<>();
			for (FormalPar param : fpd.listformalpar_) {
				FormalParameter parameter = (FormalParameter) param;
				String paramType = getTypeName(parameter.t_);
				parameters.add(paramType);
				parameters.add(parameter.l_);
				/*
				 * VarDefinition vd = createVarDefinition(parameter.l_,
				 * paramType); if (!variablesInScope.isEmpty()) {
				 * variablesInScope.peek().add(vd); }
				 */
			}
			Set<Modifier> modifiers = Sets.newHashSet(Modifier.PUBLIC, Modifier.STATIC);
			/*List<String> generics = new LinkedList<>();
			for (String modifier : fpd.listu_) {
				generics.add(modifier + "<:" + ORDERED_INTERFACE_CLASS + "[" + modifier + "]");
			}*/
			w.beginMethod(methodType, methodName + fpd.listu_, modifiers, parameters.toArray(new String[0]));
			FunBody fbody = fpd.funbody_;
			if (fbody instanceof BuiltinFunBody) {
				logNotImplemented("builtin function body: %s %s", methodType, methodName);
			} else if (fbody instanceof NormalFunBody) {
				NormalFunBody nfb = (NormalFunBody) fbody;
				PureExp pe = nfb.pureexp_;

				StringWriter sw = new StringWriter();
				ScalaWriter auxjw = new ScalaWriter(sw);

				auxjw.continuationLevel = w.continuationLevel;
				auxjw.duplicateReplacements = w.duplicateReplacements;

				pe.accept(this, auxjw);
				String stm = sw.toString();
				w.emitStatement("return %s", stm);

			}
			w.endMethod();
			w.emitEmptyLine();
			this.classes.pop();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return prog;
	}

	/**
	 * @see #visit(DData, ScalaWriter)
	 * @see #visit(DDataPoly, ScalaWriter)
	 */
	@Override
	public Prog visit(SinglConstrIdent p, ScalaWriter w) {

		return prog;
	}

	/**
	 * @see #visit(DData, ScalaWriter)
	 * @see #visit(DDataPoly, ScalaWriter)
	 */
	@Override
	public Prog visit(ParamConstrIdent p, ScalaWriter arg) {
		return prog;
	}

	/**
	 * @see #visit(DData, ScalaWriter)
	 * @see #visit(DDataPoly, ScalaWriter)
	 */
	@Override
	public Prog visit(EmptyConstrType p, ScalaWriter arg) {
		try {
			arg.emit(getTypeName(p.t_));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prog;
	}

	/**
	 * @see #visit(DData, ScalaWriter)
	 * @see #visit(DDataPoly, ScalaWriter)
	 */
	@Override
	public Prog visit(RecordConstrType p, ScalaWriter arg) {
		try {
			arg.emit(getTypeName(p.t_) + "#" + p.l_);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prog;
	}

	@Override
	public Prog visit(BuiltinFunBody p, ScalaWriter arg) {
		logNotImplemented("#visit(%s)", p);
		return prog;
	}

	@Override
	public Prog visit(NormalFunBody body, ScalaWriter w) {
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
	public Prog visit(FormalParameter p, ScalaWriter arg) {
		logNotImplemented("#visit(%s)", p);
		return prog;
	}

	@Override
	public Prog visit(SAssert p, ScalaWriter arg) {
		logNotImplemented("#visit(%s)", p);
		return prog;
	}

	@Override
	public Prog visit(SPrint p, ScalaWriter w) {
		try {
			StringWriter sw = new StringWriter();
			ScalaWriter auxw = new ScalaWriter(sw);
			auxw.continuationLevel = w.continuationLevel;
			auxw.duplicateReplacements = w.duplicateReplacements;
			p.pureexp_.accept(this, auxw);
			w.emitStatement("println(%s)", sw.toString());
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(ELet p, ScalaWriter arg) {
		logNotImplemented("#visit(%s)", p);
		return prog;
	}

	@Override
	public Prog visit(EIf p, ScalaWriter w) {
		try {
			StringWriter sw = new StringWriter();
			ScalaWriter w1 = new ScalaWriter(sw);
			w1.continuationLevel = w.continuationLevel;
			w1.duplicateReplacements = w.duplicateReplacements;
			p.pureexp_1.accept(this, w1);
			String condition = sw.toString();

			sw = new StringWriter();
			ScalaWriter w2 = new ScalaWriter(sw);
			w2.continuationLevel = w.continuationLevel;
			w2.duplicateReplacements = w.duplicateReplacements;
			p.pureexp_2.accept(this, w2);
			String left = sw.toString();

			sw = new StringWriter();
			ScalaWriter w3 = new ScalaWriter(sw);
			w3.continuationLevel = w.continuationLevel;
			w3.duplicateReplacements = w.duplicateReplacements;
			p.pureexp_3.accept(this, w3);
			String right = sw.toString();

			w.emit(String.format("if (%s)  %s else %s", condition, left, right));

			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(ECase p, ScalaWriter w) {
		try {
			StringWriter auxsw = new StringWriter();
			ScalaWriter auxw = new ScalaWriter(auxsw);
			auxw.continuationLevel = w.continuationLevel;
			auxw.duplicateReplacements = w.duplicateReplacements;

			boolean oldFromCase = fromCase;
			fromCase = false;
			p.pureexp_.accept(this, auxw);
			w.beginControlFlow("%s match ", auxsw);
			for (ECaseBranch scb : p.listecasebranch_) {
				StringWriter pattersw = new StringWriter();
				ScalaWriter patternw = new ScalaWriter(pattersw);
				patternw.continuationLevel = w.continuationLevel;
				patternw.duplicateReplacements = w.duplicateReplacements;

				scb.accept(this, patternw);

				w.emit(pattersw.toString());

			}
			fromCase = oldFromCase;
			w.endControlFlow();

			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(EFunCall f, ScalaWriter w) {
		try {
			List<String> params = getParameters(f.listpureexp_, w);
			String name = getQTypeName(f.ql_, false);
			if (fromCase)
				caseKey = findMethodReturnType(name, "Functions", params);
			w.emit(name);
			w.emit("(");
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
	public Prog visit(ENaryFunCall fnc, ScalaWriter w) {
		try {
			List<String> exps = new ArrayList<>();
			for (PureExp pexp : fnc.listpureexp_) {
				StringWriter auxsw = new StringWriter();
				ScalaWriter auxw = new ScalaWriter(auxsw);
				auxw.continuationLevel = w.continuationLevel;
				auxw.duplicateReplacements = w.duplicateReplacements;

				pexp.accept(this, auxw);
				exps.add(auxsw.toString());
			}
			String identifier = getQTypeName(fnc.ql_, false);
			if (fromCase)
				caseKey = findMethodReturnType(identifier, "Functions", exps);

			w.emit(identifier + String.format("(%s)", String.join(COMMA_SPACE, exps)));
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(ESinglConstr cons, ScalaWriter w) {
		try {
			String type = getQTypeName(cons.qu_, false);
			final boolean isException = this.exceptionDeclaraions.contains(type);
			String resolvedType = javaTypeTranslator.translateFunctionalType(type);
			final boolean isData = resolvedType != null && this.dataDeclarations.containsKey(type);
			if (isException) {
				w.emit("new " + type + "()");
			} else {
				String refinedType = getRefindDataDeclName(resolvedType);
				w.emit(refinedType + (refinedType.equals("false") || refinedType.equals("true") ? "" : "()"));
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
	public Prog visit(EParamConstr cons, ScalaWriter w) {
		try {
			String functionName = getQTypeName(cons.qu_, false);
			ListPureExp params = cons.listpureexp_;
			List<String> parameters = getParameters(params, w);
			String result = String.format("%s(%s)", functionName, String.join(COMMA_SPACE, parameters));
			w.emit(result);
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(ECaseB p, ScalaWriter w) {
		try {
			StringWriter patsw = new StringWriter();
			ScalaWriter patw = new ScalaWriter(patsw);
			patw.continuationLevel = w.continuationLevel;
			patw.duplicateReplacements = w.duplicateReplacements;
			p.pattern_.accept(this, patw);

			StringWriter stmsw = new StringWriter();
			ScalaWriter stmw = new ScalaWriter(stmsw);
			stmw.continuationLevel = w.continuationLevel;
			stmw.duplicateReplacements = w.duplicateReplacements;

			p.pureexp_.accept(this, stmw);

			w.emitStatement("case %s => %s", patsw.toString(), stmsw.toString());
			variablesBeforeBlock.clear();
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(PVar p, ScalaWriter w) {
		try {
			VarDefinition vd = createVarDefinition(p.l_, patternParamType);
			variablesBeforeBlock.add(vd);
			if ((w.continuationLevel > -1 || w.avoidDuplicates)) {
				w.emit("w_" + awaitCounter + "$" + p.l_);
			} else
				w.emit(getDuplicate(p.l_, w));
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(PLit p, ScalaWriter w) {
		p.literal_.accept(this, w);
		return prog;
	}

	@Override
	public Prog visit(PSinglConstr p, ScalaWriter w) {
		try {
			String resolvedType = javaTypeTranslator.translateFunctionalType(getQTypeName(p.qu_, false));
			String t = getRefindDataDeclName(resolvedType);
			w.emit(t + (t.equals("false") || t.equals("true") ? "" : "()"));
			if (this.dataDeclarations.containsKey(p.qu_)) {
				w.emit(".INSTANCE");
			}
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(PParamConstr p, ScalaWriter w) {
		try {
			String name = getRefindDataDeclName(getQTypeName(p.qu_, false));
			Map<String, List<String>> typeDic = paramConstructs;
			if (fromCase && caseKey.contains("[")) {
				System.out.println(caseKey);
				typeDic = createTypeDictionary(caseKey);
			}

			// System.out.println(typeDic);
			// if (!w.checkAwaits && (caseKey.length() > 0))
			/// System.out.println("============" + caseKey + " " + name +
			// "=================");
			String oldCaseKey = caseKey;
			List<String> parameters = new ArrayList<>();
			int i = 0;
			for (Pattern pattern : p.listpattern_) {
				StringWriter sw = new StringWriter();
				ScalaWriter auxw = new ScalaWriter(sw);
				auxw.continuationLevel = w.continuationLevel;
				auxw.duplicateReplacements = w.duplicateReplacements;
				if (!w.checkAwaits && fromCase && caseKey.length() > 0) {
					patternParamType = typeDic.get(name).get(i);
					if (pattern instanceof PParamConstr)
						caseKey = patternParamType;
					// System.out.println(paramType);
				}
				pattern.accept(this, auxw);
				parameters.add(sw.toString());
				patternParamType = "";
				i++;
			}
			caseKey = oldCaseKey;
			// if (!w.checkAwaits && (caseKey.length() > 0))
			// System.out.println("=============");
			String params = String.join(COMMA_SPACE, parameters);
			w.emit(name + "(" + params + ")");
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(PWildCard p, ScalaWriter w) {
		try {
			w.emit("_");
			return prog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Prog visit(LThisDC p, ScalaWriter arg) {
		logNotImplemented("#visit(%s)", p);
		return prog;
	}

	@Override
	public Prog visit(NewLocal p, ScalaWriter w) {
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
	protected void visitFunctions(Module m, ScalaWriter w) {
		try {
			for (AnnDecl decl : elements.get(AbsElementType.FUNCTION)) {
				((AnnDeclaration) decl).decl_.accept(this, functionsWriter);
			}
			functionsWriter.endType();

			/*
			 * List<String> pairPar = new ArrayList<>(); pairPar.add("F");
			 * pairPar.add("first"); pairPar.add("S"); pairPar.add("second");
			 * 
			 * beginElementKind(jw, ElementKind.CLASS, "Pair[F,S]",
			 * DEFAULT_MODIFIERS, null, null, pairPar, false); jw.endType();
			 * 
			 * Set<Modifier> mods = new HashSet<>(DEFAULT_MODIFIERS);
			 * mods.add(Modifier.ABSTRACT);
			 * 
			 * beginElementKind(jw, ElementKind.CLASS, "Maybe[T]", mods, null,
			 * null); jw.endType();
			 * 
			 * beginElementKind(jw, ElementKind.OTHER, "Nothing[T]",
			 * DEFAULT_MODIFIERS, "Maybe[T]", null, new ArrayList<>(), false);
			 * 
			 * jw.endType();
			 * 
			 * pairPar.clear(); pairPar.add("T"); pairPar.add("member");
			 * 
			 * beginElementKind(jw, ElementKind.OTHER, "Just[T]",
			 * DEFAULT_MODIFIERS, "Maybe[T]", null, pairPar, false);
			 * 
			 * jw.endType();
			 */

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void visitMain(Mod m, ScalaWriter w) {
		try {
			ScalaWriter jw = (ScalaWriter) javaWriterSupplier.apply(MAIN_CLASS_NAME);
			jw.emitPackage(packageName);
			// emitDefaultImports(jw);

			visitImports(m.listimport_, jw);
			jw.emitEmptyLine();

			final String identifier = MAIN_CLASS_NAME;
			EnumSet<Modifier> mainModifiers = EnumSet.of(Modifier.PUBLIC, Modifier.STATIC);
			beginElementKind(jw, ElementKind.CONSTRUCTOR, identifier, DEFAULT_MODIFIERS, null, null, null, true);
			classes.push("Main");
			jw.emitEmptyLine();

			emitImplicitConversions(jw);
			List<String> javaMainMethodParameters = Arrays.asList("Array[String]", "args");

			jw.beginMethod("Unit", "main", mainModifiers, javaMainMethodParameters, null);

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

			jw.emitEmptyLine();
			// emitToStringMethod(jw);

			jw.emitEmptyLine();
			jw.endMethod();

			jw.endType();
			classes.pop();
			close(jw, w);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void visitImports(final ListImport imports, ScalaWriter w) throws IOException {
		emitDefaultImports(w);
		w.emitStaticImports(this.staticImports);
		for (Import import1 : imports) {
			import1.accept(this, w);
		}
		w.emitEmptyLine();
	}

	protected void emitDefaultImports(ScalaWriter w) throws IOException {
		// w.emitStaticImports(DEFAULT_STATIC_IMPORTS_PATTERNS);
		w.emitImports(DEFAULT_IMPORTS);
		w.emitStaticImports(DEFAULT_STATIC_IMPORTS);
		w.emitStaticImports(this.packageName + "." + FUNCTIONS_CLASS_NAME + "._");

		// w.emitImports(DEFAULT_IMPORTS_PATTERNS);
	}

	protected void emitPackageLevelImport(ScalaWriter w) throws IOException {
		for (String p : this.packageLevelImports) {
			w.emitImports(this.packageName + "." + p + "._");
		}
	}

	protected void visitAsyncMethodCall(AsyncMethCall amc, String resultVarType, String resultVarName,
			final boolean isDefined, ScalaWriter w) throws IOException {
		String calleeId = getCalleeId(amc, w);
		List<String> params = getCalleeMethodParams(amc, w);
		String methodName = amc.l_.equals(METHOD_GET) ? "get" : amc.l_;

		String varDefType = findVariableType(resultVarName);
		String potentialReturnType = resultVarType != null ? resultVarType
				: varDefType != null ? varDefType
						: findMethodReturnType(methodName, findVariableType(calleeId), params);
		String msgVarName = createMessageVariableName(calleeId);
		String msgStatement = generateMessageStatement(msgVarName, potentialReturnType,
				generateJavaMethodInvocation(calleeId, methodName, params, w, 'a', asyncPCounter));
		w.emit(msgStatement, true);
		w.emitStatementEnd();
		String responseVarName = resultVarName != null ? resultVarName : createMessageResponseVariableName(msgVarName);

		String sendStm = generateMessageInvocationStatement(calleeId, isDefined, resultVarType, msgVarName,
				responseVarName, w);
		w.emit(sendStm, true);
		w.emitStatementEnd();
		if (!w.isScope && !w.checkAwaits)
			asyncPCounter++;
	}

	protected void visitSyncMethodCall_Async(SyncMethCall smc, String resultVarType, String resultVarName,
			boolean isDefined, ScalaWriter w) throws IOException {

		// TODO : complete sync->async
		String calleeId = getCalleeId(smc, w);
		List<String> params = getCalleeMethodParams(smc, w);
		String msgVarName = createMessageVariableName(calleeId);
		String methodName = smc.l_.equals(METHOD_GET) ? "get" : smc.l_;
		String potentialReturnType = resultVarType != null ? resultVarType
				: findMethodReturnType(methodName, findVariableType(calleeId), params);

		StringBuilder label = new StringBuilder(classes.peek());
		label.append(currentMethod.getName());

		if (!w.isScope && !w.checkAwaits) {
			label.append("Await" + (awaitCounter++));
		}
		List<String> parameters = new ArrayList<>();
		for (TreeSet<VarDefinition> defs : variablesInScope) {
			for (VarDefinition varDefinition : defs) {
				if (!varDefinition.getName().equals(resultVarName))
					parameters.add(varDefinition.getName());
			}
		}

		StringBuilder futureName = new StringBuilder();

		if (!(resultVarName == null)) {
			futureName.append("future_");
			futureName.append(resultVarName);
		}

		StringBuilder extraP = new StringBuilder();

		if (potentialReturnType != null) {
			parameters.add(futureName.toString() + "_par");
			extraP.append(futureName.toString() + "_par: ABSFutureTask[" + (resultVarType == null ? "_" : resultVarType)
					+ "]");
		}

		if ((w.continuationLevel > -1 || w.avoidDuplicates)) {
			w.duplicateReplacements.peek().put(futureName.toString(),
					"w_" + awaitCounter + "$" + futureName.toString());

		}

		String syncCall = generateJavaMethodInvocation(calleeId, methodName, params, w, 's', syncPCounter);

		String awaitCall = generateJavaMethodInvocation("this", label.toString(), parameters, w, 'w', awaitCounter);

		// System.out.println(awaitCall);

		String msgStatement = generateMessageStatement(msgVarName, potentialReturnType, syncCall);
		w.emit(msgStatement, true);
		w.emitStatementEnd();

		String sendStm;
		if (!currentMethod.isVoid() && extraP.length() > 0) {
			sendStm = generateMessageSyncInvocationStatement(calleeId, isDefined, resultVarType,
					"(" + extraP + ")=>" + awaitCall, msgVarName, ((w.avoidDuplicates || w.continuationLevel > -1)
							? w.duplicateReplacements.peek().get(futureName.toString()) : futureName.toString()));
		} else {

			sendStm = generateMessageSyncInvocationStatement(calleeId, isDefined, resultVarType, msgVarName,
					"(" + extraP + ")=>" + awaitCall, ((w.avoidDuplicates || w.continuationLevel > -1)
							? w.duplicateReplacements.peek().get(futureName.toString()) : futureName.toString()));
		}

		w.emit(sendStm, true);
		w.emitStatementEnd();

		if (resultVarName != null && resultVarType != null) {
			w.emit("var "
					+ ((w.avoidDuplicates || w.continuationLevel > -1)
							? w.duplicateReplacements.peek().get(resultVarName) : resultVarName)
					+ ": " + (isDefined ? "" : resultVarType + " ") + " " + " = "
					+ ((w.avoidDuplicates || w.continuationLevel > -1)
							? w.duplicateReplacements.peek().get(futureName.toString()) : futureName.toString())
					+ ".get()", true);
		} else {
			if (futureName.equals(""))
				w.emit(futureName + ".get()", true);
		}
		w.emitStatementEnd();
		if (!w.isScope && !w.checkAwaits)
			syncPCounter++;

	}

	protected void visitSyncMethodCall_Sync(SyncMethCall smc, String resultVarType, String resultVarName,
			boolean isDefined, ScalaWriter w) throws IOException {
		String calleeId = getCalleeId(smc, w);
		List<String> params = getCalleeMethodParams(smc, w);
		String methodName = smc.l_.equals(METHOD_GET) ? "get" : smc.l_;
		String potentialReturnType = resultVarName != null ? resultVarType
				: findMethodReturnType(methodName, findVariableType(calleeId), params);
		if (w.checkAwaits) {
			if (callHasAwait(methodName) && !currentMethod.containsAwait()) {
				currentMethod.setContainsAwait(true);
				awaitsDetected = true;
			}
		} else if (callHasAwait(methodName)) {
			visitSyncMethodCall_Async(smc, resultVarType, resultVarName, isDefined, w);
		}

		else {
			String javaMethodCall = String.format("%s.%s(%s)", calleeId, methodName, String.join(COMMA_SPACE, params));
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
	}

	protected void visitStatementAssignmentExp(Exp exp, String varName, String varType, ScalaWriter w)
			throws IOException {

		/*
		 * if ((exp instanceof ExpE) && ((ExpE) exp).effexp_ instanceof Spawns)
		 * { if (((ExpE) exp).effexp_ instanceof Spawns) { try { Spawns p =
		 * (Spawns) (((ExpE) exp).effexp_); StringWriter sw = new
		 * StringWriter(); StringWriter sw2 = new StringWriter(); New np = new
		 * New(p.t_, p.listpureexp_); visit(np, new JavaWriter(sw));
		 * p.pureexp_.accept(this, new JavaWriter(sw2)); if (varType == null) {
		 * w.emitStatement( "%s = (%s) (%s.%s.context.newActor(\"%s\", %s))",
		 * varName, getTypeName(p.t_), sw2, ACTOR_SERVER_MEMBER, varName, sw); }
		 * else { w.emitStatement(
		 * "%s %s = (%s) (%s.%s.context.newActor(\"%s\", %s))", varType,
		 * varName, getTypeName(p.t_), sw2, ACTOR_SERVER_MEMBER, varName, sw); }
		 * } catch (IOException e) { throw new RuntimeException(e); } } }
		 */

		StringWriter auxsw = new StringWriter();
		ScalaWriter auxw = new ScalaWriter(auxsw);
		auxw.continuationLevel = w.continuationLevel;
		auxw.duplicateReplacements = w.duplicateReplacements;
		exp.accept(this, auxw);
		varName = getDuplicate(varName, w);
		if (varType == null) {
			w.emit(varName + " = " + auxsw.toString(), true);
		} else {
			w.emitField(varType, varName, new HashSet<>(), auxsw.toString());
		}

		w.emitStatementEnd();
	}

	protected void visitECase(ECase kase, String expectedCaseType, ScalaWriter w) {
		StringWriter auxsw = new StringWriter();
		kase.pureexp_.accept(this, new ScalaWriter(auxsw));
		String kaseVar = auxsw.toString();
		String type = expectedCaseType;
		String caseVarType = findVariableTypeInScope(kaseVar);
		/*
		 * if (type == null) { type = findVariableType(kaseVar); }
		 */
		if (type == null) {
			type = currentMethod.type() == null ? "void" : currentMethod.type();
			/*
			 * } else { type = "(" + type + ")";
			 */}

		List<Entry<Pattern, PureExp>> cases = kase.listecasebranch_.stream().map(b -> (ECaseB) b)
				.map(cb -> new SimpleEntry<Pattern, PureExp>(cb.pattern_, cb.pureexp_)).collect(Collectors.toList());
		if (caseVarType.startsWith("Optional"))
			visitECasesJM(kaseVar, type, cases, w);
		else
			visitECases(kaseVar, type, cases, w);
	}

	protected void visitSCase(SCase kase, String expectedCaseType, ScalaWriter w) {
		StringWriter auxsw = new StringWriter();
		kase.pureexp_.accept(this, new ScalaWriter(auxsw));
		String kaseVar = auxsw.toString();
		String type = expectedCaseType;
		String caseVarType = findVariableTypeInScope(kaseVar);
		/*
		 * if (type == null) { type = findVariableType(kaseVar); }
		 */
		if (type == null) {
			type = currentMethod.type() == null ? "Void" : currentMethod.type();
			/*
			 * } else { type = "(" + type + ")";
			 */}

		List<Entry<Pattern, AnnStm>> cases = kase.listscasebranch_.stream().map(b -> (SCaseB) b)
				.map(cb -> new SimpleEntry<Pattern, AnnStm>(cb.pattern_, cb.annstm_)).collect(Collectors.toList());

		if (caseVarType.startsWith("Optional"))
			visitSCasesJM(kaseVar, type, cases, w);
		else
			visitSCases(kaseVar, type, cases, w);
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

	private void visitECasesJM(String caseVariable, String caseVariableType, List<Entry<Pattern, PureExp>> cases,
			ScalaWriter w) {
		try {
			w.emit(String.format("(%s.get()!=null)? ", caseVariable), true);
			String optional = findVariableTypeInScope(caseVariable);
			String optionalType = optional.substring(optional.indexOf('<') + 1, optional.indexOf('>'));
			Entry<Pattern, PureExp> e = cases.get(0);
			Pattern left = e.getKey();
			StringWriter leftauxsw = new StringWriter();
			left.accept(this, new ScalaWriter(leftauxsw));
			PureExp right = e.getValue();
			String patt = leftauxsw.toString();
			int index = patt.indexOf('(');
			String[] vars = null;
			String value = null;
			vars = patt.substring(index + 1, patt.indexOf(')')).split(",");
			value = patt.substring(0, index);
			for (String string : vars) {
				if (optionalType.startsWith("Pair")) {
					caseMap.put(string.trim(), String.format("%s(%s.get())", string.trim(), caseVariable));
				} else if (!classNames.containsKey(optionalType)) {
					caseMap.put(string.trim(), String.format("%s.get()", caseVariable));
				} else
					caseMap.put(string.trim(), String.format("%s.get().%s", caseVariable, string.trim()));
			}
			StringWriter rightauxsw = new StringWriter();
			right.accept(this, new ScalaWriter(rightauxsw));

			w.emit(String.format("%s:", rightauxsw.toString()), true);
			if (vars != null) {
				for (String string : vars) {
					caseMap.remove(string.trim());
				}
			}
			Entry<Pattern, PureExp> nothing = cases.get(1);
			PureExp rightNothing = nothing.getValue();
			StringWriter rightNothingSw = new StringWriter();
			rightNothing.accept(this, new ScalaWriter(rightNothingSw));

			w.emit(String.format("%s;", rightNothingSw.toString()), true);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private void visitSCasesJM(String caseVariable, String caseVariableType, List<Entry<Pattern, AnnStm>> cases,
			ScalaWriter w) {

		try {
			w.beginControlFlow("if(%s.get()!=null) ", caseVariable);
			String optional = findVariableTypeInScope(caseVariable);
			String optionalType = optional.substring(optional.indexOf('<') + 1, optional.lastIndexOf('>'));
			Entry<Pattern, AnnStm> e = cases.get(0);
			Pattern left = e.getKey();
			StringWriter leftauxsw = new StringWriter();
			left.accept(this, new ScalaWriter(leftauxsw));
			AnnStm right = e.getValue();
			String patt = leftauxsw.toString();
			int index = patt.indexOf('(');
			patt = patt.substring(index + 1);
			index = patt.indexOf('(');
			String[] vars = null;
			String value = null;
			vars = patt.substring(index + 1, patt.indexOf(')')).split(",");
			value = patt.substring(0, index);
			w.emitStatement("%s %s = %s.get()", optionalType, value.toLowerCase(), caseVariable);
			for (String string : vars) {
				if (optionalType.startsWith("Pair")) {
					caseMap.put(string.trim(), String.format("%s(%s)", string.trim(), value.toLowerCase()));
				} else if (!classNames.containsKey(optionalType)) {
					caseMap.put(string.trim(), String.format("%s.get()", caseVariable));
				} else
					caseMap.put(string.trim(), String.format("%s.get().%s", caseVariable, string.trim()));
			}
			StringWriter rightauxsw = new StringWriter();
			right.accept(this, new ScalaWriter(rightauxsw));

			w.emitStatement("%s", rightauxsw.toString());
			if (vars != null) {
				for (String string : vars) {
					caseMap.remove(string.trim());
				}
			}
			w.endControlFlow();

			w.beginControlFlow("else", caseVariable);
			Entry<Pattern, AnnStm> nothing = cases.get(1);
			AnnStm rightNothing = nothing.getValue();
			StringWriter rightNothingSw = new StringWriter();
			rightNothing.accept(this, new ScalaWriter(rightNothingSw));
			w.emitStatement("%s", rightNothingSw.toString());

			w.endControlFlow();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void visitECases(String caseVariable, String caseVariableType, List<Entry<Pattern, PureExp>> cases,
			ScalaWriter w) {
		/*
		 * StringBuilder caseStm = new
		 * StringBuilder("Matching").append(NEW_LINE); for (Entry<Pattern,
		 * PureExp> e : cases) { Pattern left = e.getKey(); PureExp right =
		 * e.getValue(); StringWriter auxsw = new StringWriter();
		 * right.accept(this, new JavaWriter(auxsw)); String thenMatchValue =
		 * auxsw.toString();
		 * caseStm.append(".").append(String.format("when()")).append(NEW_LINE);
		 * String kaseVarLocal = "var" + RANDOM.nextInt(1000); if
		 * (thenMatchValue.contains(" " + caseVariable + " ")) { thenMatchValue
		 * = thenMatchValue.replace(caseVariable, kaseVarLocal); } if (left
		 * instanceof PVar) { PVar pi = (PVar) left;
		 * caseStm.append(".").append(String.format("isValue(%s)",
		 * pi.l_)).append(NEW_LINE).append(".") .append(String.format(
		 * "thenApply(x -> %s)", thenMatchValue)).append(NEW_LINE); } else if
		 * (left instanceof PLit) { PLit plit = (PLit) left; StringWriter litsw
		 * = new StringWriter(); plit.accept(this, new JavaWriter(litsw));
		 * caseStm.append(".").append(String.format("isValue(%s)",
		 * litsw.toString())).append(NEW_LINE).append(".")
		 * .append(String.format("thenApply(x -> %s",
		 * thenMatchValue)).append(NEW_LINE); } else if (left instanceof
		 * PWildCard) { caseStm.append(".").append(String.format(
		 * "isTrue(x -> true)")).append(NEW_LINE) .append(String.format(
		 * "thenApply(x -> %s)", thenMatchValue)).append(NEW_LINE); } else if
		 * (left instanceof PSinglConstr) { PSinglConstr psc = (PSinglConstr)
		 * left; String type = getQTypeName(psc.qu_, false);
		 * caseStm.append(".").append(String.format("isType((%s x) -> %s)",
		 * type, thenMatchValue)) .append(NEW_LINE); } else if (left instanceof
		 * PParamConstr) { PParamConstr ppc = (PParamConstr) left; String type =
		 * getQTypeName(ppc.qu_, false);
		 * caseStm.append(".").append(String.format("isType((%s x) -> %s)",
		 * type, thenMatchValue)) .append(NEW_LINE); } else { logNotImplemented(
		 * "case pattern: %s", left); } }
		 * caseStm.append(String.format(".match(%s).get()", caseVariable));
		 * return caseStm.toString();
		 */

		try {
			w.beginControlFlow("%s.match(new %s.MatchBlock<%s>() ", caseVariable, findVariableTypeInScope(caseVariable),
					caseVariableType);
			for (Entry<Pattern, PureExp> e : cases) {
				Pattern left = e.getKey();
				StringWriter leftauxsw = new StringWriter();
				left.accept(this, new ScalaWriter(leftauxsw));
				PureExp right = e.getValue();
				String patt = leftauxsw.toString();
				int index = patt.indexOf('(');
				boolean isMultiCons = index != -1;
				String[] vars = null;
				String value = null;
				if (isMultiCons) {
					vars = patt.substring(index + 1, patt.indexOf(')')).split(",");
					value = patt.substring(0, index);
					for (String string : vars) {
						if (value.startsWith("Pair")) {
							caseMap.put(string.trim(), String.format("%s(%s)", string.trim(), value.toLowerCase()));
						} else
							caseMap.put(string.trim(), value.toLowerCase() + "." + string.trim());
					}
				}
				w.beginMethod(caseVariableType, CASE, DEFAULT_MODIFIERS, isMultiCons ? value : patt,
						isMultiCons ? value.toLowerCase() : patt.toLowerCase());
				StringWriter rightauxsw = new StringWriter();
				right.accept(this, new ScalaWriter(rightauxsw));

				w.emitStatement("return %s", rightauxsw.toString());
				w.endMethod();
				if (vars != null) {
					for (String string : vars) {
						caseMap.remove(string.trim());
					}
				}

			}

			w.endControlFlow();
			w.emit(")");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private void visitSCases(String caseVariable, String caseVariableType, List<Entry<Pattern, AnnStm>> cases,
			ScalaWriter w) {
		/*
		 * StringBuilder caseStm = new
		 * StringBuilder("Matching").append(NEW_LINE); for (Entry<Pattern,
		 * AnnStm> e : cases) { Pattern left = e.getKey(); AnnStm right =
		 * e.getValue(); StringWriter auxsw = new StringWriter();
		 * right.accept(this, new JavaWriter(auxsw)); String thenMatchValue =
		 * auxsw.toString();
		 * caseStm.append(".").append(String.format("when()")).append(NEW_LINE);
		 * String kaseVarLocal = "var" + RANDOM.nextInt(1000); if
		 * (thenMatchValue.contains(" " + caseVariable + " ")) { thenMatchValue
		 * = thenMatchValue.replace(caseVariable, kaseVarLocal); } if (left
		 * instanceof PVar) { PVar pi = (PVar) left;
		 * caseStm.append(".").append(String.format("isValue(%s)",
		 * pi.l_)).append(NEW_LINE).append(".") .append(String.format(
		 * "thenApply(x -> %s)", thenMatchValue)).append(NEW_LINE); } else if
		 * (left instanceof PLit) { PLit plit = (PLit) left; StringWriter litsw
		 * = new StringWriter(); plit.accept(this, new JavaWriter(litsw));
		 * caseStm.append(".").append(String.format("isValue(%s)",
		 * litsw.toString())).append(NEW_LINE).append(".")
		 * .append(String.format("thenApply(x -> %s",
		 * thenMatchValue)).append(NEW_LINE); } else if (left instanceof
		 * PWildCard) { caseStm.append(".").append(String.format(
		 * "isTrue(x -> true)")).append(NEW_LINE) .append(String.format(
		 * "thenApply(x -> %s)", thenMatchValue)).append(NEW_LINE); } else if
		 * (left instanceof PSinglConstr) { PSinglConstr psc = (PSinglConstr)
		 * left; String type = getQTypeName(psc.qu_, false);
		 * caseStm.append(".").append(String.format("isType((%s x) -> %s)",
		 * type, thenMatchValue)) .append(NEW_LINE); } else if (left instanceof
		 * PParamConstr) { PParamConstr ppc = (PParamConstr) left; String type =
		 * getQTypeName(ppc.qu_, false);
		 * caseStm.append(".").append(String.format("isType((%s x) -> %s)",
		 * type, thenMatchValue)) .append(NEW_LINE); } else { logNotImplemented(
		 * "case pattern: %s", left); } }
		 * caseStm.append(String.format(".match(%s).get()", caseVariable));
		 * return caseStm.toString();
		 */

		try {
			w.beginControlFlow("%s.match(new %s.MatchBlock<%s>() ", caseVariable, findVariableTypeInScope(caseVariable),
					caseVariableType);
			for (Entry<Pattern, AnnStm> e : cases) {
				Pattern left = e.getKey();
				StringWriter leftauxsw = new StringWriter();
				left.accept(this, new ScalaWriter(leftauxsw));
				AnnStm right = e.getValue();
				String patt = leftauxsw.toString();
				int index = patt.indexOf('(');
				boolean isMultiCons = index != -1;
				String[] vars = null;
				String value = null;
				if (isMultiCons) {
					vars = patt.substring(index + 1, patt.indexOf(')')).split(",");
					value = patt.substring(0, index);
					for (String string : vars) {
						if (value.startsWith("Pair")) {
							caseMap.put(string.trim(), String.format("%s(%s)", string.trim(), value.toLowerCase()));
						} else
							caseMap.put(string.trim(), value.toLowerCase() + "." + string.trim());
					}
				}
				w.beginMethod(caseVariableType, CASE, DEFAULT_MODIFIERS, isMultiCons ? value : patt,
						isMultiCons ? value.toLowerCase() : patt.toLowerCase());
				StringWriter rightauxsw = new StringWriter();
				right.accept(this, new ScalaWriter(rightauxsw));

				w.emitStatement("%s", rightauxsw.toString());
				w.endMethod();
				if (vars != null) {
					for (String string : vars) {
						caseMap.remove(string.trim());
					}
				}

			}

			w.endControlFlow();
			w.emitStatement(")");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
			ScalaWriter jw = new ScalaWriter(sw);
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
					ScalaWriter auxjw = new ScalaWriter(sw);
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
		ScalaWriter cw = (ScalaWriter) javaWriterSupplier.apply(className);
		cw.emitPackage(this.packageName);
		emitDefaultImports(cw);
		cw.emitEmptyLine();
		String fullClassName = classGenericParams.isEmpty() ? className
				: String.format("%s<%s>", className, String.join(COMMA_SPACE, classGenericParams));
		if (parent != null && isParentInterface) {
			beginElementKind(cw, ElementKind.CLASS, fullClassName, EnumSet.of(Modifier.PUBLIC), null,
					Collections.singletonList(parent), null, false);
		} else if (parent != null && !isParentInterface) {
			beginElementKind(cw, ElementKind.CLASS, fullClassName, EnumSet.of(Modifier.PUBLIC), parent,
					Collections.emptyList(), null, false);
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
		ScalaWriter cw = (ScalaWriter) javaWriterSupplier.apply(className);
		cw.emitPackage(this.packageName);
		emitDefaultImports(cw);
		cw.emitEmptyLine();
		List<Entry<String, String>> fields = extractConstructorParameters(pci.listconstrtype_);
		List<String> actualFieldTypes = fields.stream().map(e -> e.getKey()).collect(Collectors.toList());
		String fullClassName = classGenericParams.isEmpty() ? className
				: String.format("%s<%s>", className, String.join(COMMA_SPACE, actualFieldTypes));
		if (parent != null && isParentInterface) {
			beginElementKind(cw, ElementKind.CLASS, fullClassName, EnumSet.of(Modifier.PUBLIC), null,
					Collections.singletonList(parent), null, false);
		} else if (parent != null && !isParentInterface) {
			beginElementKind(cw, ElementKind.CLASS, fullClassName, EnumSet.of(Modifier.PUBLIC), parent,
					Collections.emptyList(), null, false);
		}
		cw.emitEmptyLine();
		visitConstrType(pci.listconstrtype_, cw, false);
		List<String> fieldNames = fields.stream().map(e -> e.getValue()).collect(Collectors.toList());
		emitEqualsMethod(className, fieldNames, cw);
		cw.endType();
		cw.close();
	}

	private List<Entry<String, String>> visitConstrType(ListConstrType ctrs, ScalaWriter w,
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

	protected List<String> getCalleeMethodParams(AsyncMethCall amc, ScalaWriter w) {
		return getParameters(amc.listpureexp_, w);
	}

	protected List<String> getCalleeMethodParams(SyncMethCall smc, ScalaWriter w) {
		return getParameters(smc.listpureexp_, w);
	}

	protected List<String> getParameters(ListPureExp params, ScalaWriter w) {
		List<String> parameters = new ArrayList<>();
		for (PureExp par : params) {
			StringWriter parSW = new StringWriter();
			ScalaWriter parameterWriter = new ScalaWriter(parSW);
			parameterWriter.continuationLevel = w.continuationLevel;
			parameterWriter.duplicateReplacements = w.duplicateReplacements;

			par.accept(this, parameterWriter);
			parameters.add(parSW.toString());
		}
		return parameters;
	}

	protected String getCalleeId(AsyncMethCall amc, ScalaWriter w) {
		StringWriter auxsw = new StringWriter();
		ScalaWriter auxw = new ScalaWriter(auxsw);
		auxw.continuationLevel = w.continuationLevel;
		auxw.duplicateReplacements = w.duplicateReplacements;
		// System.out.println("amc calee writer "+ auxw.duplicateReplacements);
		amc.pureexp_.accept(this, auxw);
		String calleeId = auxsw.toString();
		return calleeId;
	}

	protected String getCalleeId(SyncMethCall smc, ScalaWriter w) {
		StringWriter auxsw = new StringWriter();
		ScalaWriter auxw = new ScalaWriter(auxsw);
		auxw.continuationLevel = w.continuationLevel;
		auxw.duplicateReplacements = w.duplicateReplacements;
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

	protected String generateJavaMethodInvocation(String object, String method, List<String> parameters, ScalaWriter w,
			char c, int counter) {
		object = getDuplicate(object, w);

		List<String> duplicateParameters = new LinkedList<>();
		for (String string : parameters) {
			boolean found = false;

			for (HashMap<String, String> hashMap1 : w.duplicateReplacements) {
				if (hashMap1.containsKey(string)) {
					final String stringName = "f_" + c + counter + hashMap1.get(string);
					duplicateParameters.add(stringName);
					try {
						w.emitStatement("var %s : %s = %s", stringName, findVariableTypeInScope(string),
								hashMap1.get(string));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					found = true;
					break;

				}
			}
			if (!found)
				if (findVariableTypeInScope(string) != null) {
					final String stringName = "f_" + c + counter + string;
					duplicateParameters.add(stringName);
					try {
						w.emitStatement("var %s : %s = %s", stringName, findVariableTypeInScope(string), string);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (string.contains("$")) {
					final String stringName = "f_" + c + counter + string;
					duplicateParameters.add(stringName);
					try {
						w.emitStatement("var %s : %s = %s", stringName,
								findVariableTypeInScope(string.substring(string.indexOf("$") + 1)), string);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else
					duplicateParameters.add(string);

		}
		return String.format("%s.%s(%s)", object, method, duplicateParameters == null || duplicateParameters.isEmpty()
				? "" : String.join(COMMA_SPACE, duplicateParameters));
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
		return String.format("var %s : %s = () => %s", msgVarName, actualReturnType, expression);
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
			String msgVarName, String responseVarName, ScalaWriter w) {

		responseVarName = getDuplicate(responseVarName, w);

		final String method = "send";
		if (responseVarName.endsWith("_response")) {
			return String.format("%s.%s(%s)", target, method, msgVarName);
		}
		if (!isDefined) {
			return String.format("%s = %s.%s(%s)", responseVarName, target, method, msgVarName);
		}
		String returnType = msgReturnType == null || isVoid(msgReturnType) ? VOID_WRAPPER_CLASS_NAME
				: stripGenericResponseType(msgReturnType);
		return String.format("var %s : ABSFutureTask[%s] = %s.%s (%s)", responseVarName, returnType, target, method,
				msgVarName);
	}

	/**
	 * @param qtype
	 * @return
	 */
	protected String generateMessageSyncInvocationStatement(String target, final boolean isDefined,
			String msgReturnType, String msgVarName, String contVarName, String responseVarName) {
		final String method = "sendSync";
		if (responseVarName.equals("")) {
			return String.format("%s.%s(%s, %s)", target, method, msgVarName, contVarName);
		}
		// if (!isDefined) {
		// return String.format("%s = %s.%s(%s, %s)", responseVarName, target,
		// method, msgVarName, contVarName);
		// }
		String returnType = msgReturnType == null || isVoid(msgReturnType) ? VOID_WRAPPER_CLASS_NAME
				: stripGenericResponseType(msgReturnType);
		return String.format("var %s: ABSFutureTask[%s]  = %s.%s (%s, %s)", responseVarName, returnType, target, method,
				msgVarName, contVarName);
	}

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
			sb.append(".");
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
	protected ScalaWriter emitField(ScalaWriter w, String fieldType, String fieldIdentifier, String initialValue,
			final boolean isFinal) throws IOException {
		EnumSet<Modifier> modifiers = EnumSet.noneOf(Modifier.class);

		fieldIdentifier = getDuplicate(fieldIdentifier, w);

		if (isFinal) {
			modifiers.add(Modifier.FINAL);
		}
		if (initialValue != null) {
			return w.emitField(fieldType, fieldIdentifier, modifiers, initialValue);
		} else {
			return w.emitField(fieldType, fieldIdentifier, modifiers);
		}
	}

	protected void beginElementKind(ScalaWriter w, ElementKind kind, String identifier, Set<Modifier> modifiers,
			String classParentType, Collection<String> implementingInterfaces) throws IOException {
		beginElementKind(w, kind, identifier, modifiers, classParentType, implementingInterfaces, null, true);
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
	 *             Exception from {@link ScalaWriter}
	 * @throws IllegalArgumentException
	 *             if kind other than "class" or "interface" is requested
	 */
	protected void beginElementKind(ScalaWriter w, ElementKind kind, String identifier, Set<Modifier> modifiers,
			String classParentType, Collection<String> implementingInterfaces, List<String> constructorParameters,
			final boolean isActor) throws IOException {
		Set<String> implementsTypes = new HashSet<>();
		if (implementingInterfaces != null && !implementingInterfaces.isEmpty()) {
			implementsTypes.addAll(implementingInterfaces);
		}
		String kindName = kind.name().toLowerCase();
		switch (kind) {
		case CLASS:
			w.beginType(identifier, kindName, modifiers, classParentType, constructorParameters,
					implementsTypes.toArray(new String[0]));
			if (isActor) {
				emitSerialVersionUid(w);
			}
			return;
		case INTERFACE:
			implementsTypes.add(ABS_API_INTERFACE_CLASS);
			w.beginType(identifier, kindName, modifiers, null, implementsTypes.toArray(new String[0]));
			return;
		case ENUM:
			w.beginType(identifier, kindName, modifiers);
			return;
		case CONSTRUCTOR:
			w.beginType(identifier, "object", modifiers, classParentType, implementsTypes.toArray(new String[0]));
			return;
		case OTHER:
			w.beginType(identifier, "case class", modifiers, classParentType, constructorParameters,
					implementsTypes.toArray(new String[0]));
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
	protected ScalaWriter createJavaWriter(AnnDecl decl, ScalaWriter w) throws IOException {
		if (isTopLevel(decl)) {

			String identifier = getTopLevelDeclIdentifier(((AnnDeclaration) decl).decl_);
			if (packageName.equalsIgnoreCase(identifier) || moduleNames.contains(identifier)) {
				return w;
			}
			ScalaWriter jw = (ScalaWriter) javaWriterSupplier.apply(identifier);
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
	protected void close(ScalaWriter childWriter, ScalaWriter parentWriter) throws IOException {
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

	protected String getDuplicate(String name, ScalaWriter w) {

		for (HashMap<String, String> hashMap : w.duplicateReplacements) {
			if (hashMap.containsKey(name)) {
				return hashMap.get(name);
			}
		}
		return name;
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
			sQ.append("[");
			List<String> gTypes = new ArrayList<String>();
			for (T t : tg.listt_) {
				gTypes.add(getTypeName(t));
			}
			sQ.append(String.join(COMMA_SPACE, gTypes));
			sQ.append("]");
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

	private String findVariableTypeInScope(String varName) {

		for (TreeSet<VarDefinition> scope : variablesInScope) {
			for (VarDefinition varDefinition : scope) {
				if (varDefinition.getName().equals(varName))

					return varDefinition.getType();
			}
		}

		return findVariableType(varName);
	}

	private Map<String, String> fillGenericTypes(String givenTypes) {
		char key = 'A';
		Map<String, String> genMapping = new HashMap<>();
		LinkedList<Character> paranthesis = new LinkedList<>();
		int i = 0;
		paranthesis.push(givenTypes.charAt(i++));
		StringBuilder newPar = new StringBuilder();
		while (i < givenTypes.length()) {
			char c = givenTypes.charAt(i);
			if (c == ',') {
				if (paranthesis.size() == 1) {
					genMapping.put(String.valueOf(key), newPar.toString());
					newPar.setLength(0);
					key++;
					i++;
					while (givenTypes.charAt(i) == ' ')
						i++;
				} else {
					newPar.append(c);
					i++;
				}
			} else {
				if (c == '[') {
					paranthesis.push(c);
				}
				if (c == ']') {
					paranthesis.pop();
					if (paranthesis.size() == 0)
						break;
				}

				newPar.append(c);
				i++;
			}

		}
		genMapping.put(String.valueOf(key), newPar.toString());

		return genMapping;
	}

	private String replaceGenType(String genP, Map<String, String> genMap) {
		if (!genP.contains("["))
			return genMap.containsKey(genP) ? genMap.get(genP) : genP;
		StringBuilder replacedString = new StringBuilder(genP.substring(0, genP.indexOf('[')));
		String[] toTraverse = genP.substring(genP.indexOf('[') + 1, genP.lastIndexOf(']')).split(", ");

		replacedString.append("[");
		for (String string : toTraverse) {
			if (string.length() > 0) {
				replacedString.append(replaceGenType(string, genMap));
				replacedString.append(',');
			}
		}
		replacedString.deleteCharAt(replacedString.length() - 1);
		replacedString.append(']');

		return replacedString.toString();
	}

	private Map<String, List<String>> createTypeDictionary(String actualType) {
		int index = actualType.indexOf('[');
		if (index < 0)
			System.err.println("Not given a generic type");
		String genericType = actualType.substring(index);
		String caseType = actualType.substring(0, index);
		Map<String, List<String>> typeDict = new HashMap<>();
		Map<String, String> genMap = fillGenericTypes(genericType);
		for (String keyType : caseTypeConstructs.get(caseType)) {
			List<String> params = paramConstructs.get(keyType);
			List<String> newParams = new ArrayList<>();
			for (String p : params) {
				if (genMap.containsKey(p))
					newParams.add(genMap.get(p));
				else
					newParams.add(replaceGenType(p, genMap));
			}
			typeDict.put(keyType, newParams);
		}

		return typeDict;

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

	private boolean callHasAwait(String methodName) {
		String key = this.packageName + "." + currentClass() + "." + methodName;
		if (programMethods.containsKey(key))
			return programMethods.get(key).containsAwait();
		return false;
	}

	private VarDefinition createVarDefinition(String varName, String varType) {
		Module current = currentModule();
		if (current == null) {
			throw new IllegalStateException("No current module is available.");
		}
		String clazz = getQTypeName(((Mod) current).qu_, false);
		String fqClassName = this.packageName + "." + clazz;
		VarDefinition vd = new VarDefinition(varName, varType);
		variables.put(fqClassName, vd);
		return vd;

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

		if (!programMethods.containsKey(fqClassName + "." + name))
			programMethods.put(fqClassName + "." + name, md);
		else
			currentMethod = programMethods.get(fqClassName + "." + name);
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
		String regex = "^(ABSFutureTask\\[)(.*)(\\])$";
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
				actualReturnType = String.format("Callable[%s]", type);
			}
		} else {
			actualReturnType = "Runnable";
		}
		return actualReturnType;
	}

	private void startContinuation(StringBuilder label, ScalaWriter auxw, String returnType, List<String> parameters)
			throws IOException {
		// System.out.println(label);
		auxw.beginMethod(returnType, label.toString(), DEFAULT_MODIFIERS, parameters, null);

		for (String string : auxw.methodParameters.keySet()) {

			String newName = "w_" + awaitCounter + "$" + string;
			auxw.duplicateReplacements.peekLast().put(string, newName);
			emitField(auxw, auxw.methodParameters.get(string), newName, string, false);
		}
	}

	private void emitImplicitConversions(ScalaWriter w) throws IOException {
		w.emit("implicit def fun2Call[R](f: () => R) = new Callable[R] { def call : R = f() }\n");
		w.emit("implicit def funcToRunnable( func : () => Unit ) = new Runnable(){ def run() = func() }\n");
		w.emit("implicit def funcToCallablewFut[R,V]( f : (ABSFutureTask[V]) => R ) = new CallablewFut[R,V] { def run(v: ABSFutureTask[V]) : R = f(v) }\n");
		w.emit("implicit def funcToRunnablewFut[V]( f : (ABSFutureTask[V]) => Unit ) = new RunnablewFut[V] { def run(v: ABSFutureTask[V]) : Unit = f(v) }\n");
	}

	private void emitThisActorRegistration(ScalaWriter w) throws IOException {
		w.emitStatement("context().newActor(toString(), this)");
	}

	/*
	 * The reason for this method to resolve the compilation issue with Java
	 * compiler. The resolution scope hierarchy starts from java.lang.Object and
	 * that's why Functional.toString() is not resolved by default order.
	 */
	private void emitToStringMethod(ScalaWriter w) throws IOException {
		w.emitEmptyLine();
		w.emitSingleLineComment("To override default Object#toString() competing");
		w.emitSingleLineComment("with static imports of Functional#toString()");
		w.beginMethod("String", "toString", EnumSet.of(Modifier.PRIVATE), "Object", "o");
		w.emitStatement("return Functional.toString(o)");
		w.endMethod();
	}

	private void emitEqualsMethod(String className, List<String> fieldNames, ScalaWriter w) throws IOException {
		emitEqualsMethod(className, fieldNames, false, w);
	}

	private void emitEqualsMethod(String className, List<String> fieldNames, final boolean staticFields, ScalaWriter w)
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

	private void emitDefaultRunMethodExecution(ScalaWriter w, String className) throws IOException {
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

	private void emitSerialVersionUid(ScalaWriter w) throws IOException {
		w.emitField("java.lang.Long", "serialVersionUID", EnumSet.of(Modifier.PRIVATE, Modifier.FINAL), "1L");
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
			ScalaWriter jw = new ScalaWriter(sw);
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
			return String.format("%s[%s]", getQTypeName(((TPoly) type).qu_, false), String.join(COMMA_SPACE, types));
		}
		throw new IllegalArgumentException(type.toString());
	}

}
