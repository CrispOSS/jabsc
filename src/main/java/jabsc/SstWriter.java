package jabsc;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;

public class SstWriter implements Closeable {

	static final String MO = "Mo";
	static final String SH = "Sh";
	static final String IN = "In";
	protected final Writer out;
	static final String BASE = "Base";

	public SstWriter(Writer out) {
		this.out = out;
	}

	@Override
	public void close() throws IOException {
		out.close();
	}

	public SstWriter emitAssignBaseTag(int offset, int tag, int arity) throws IOException {
		out.write(String.format("Assign(Pair(%s, %i), Tag(%i, %i))", BASE, offset, tag, arity));
		return this;
	}

	public SstWriter emitAssignRefTag(int ref, int offset, int tag, int arity) throws IOException {
		out.write(String.format("Assign(Pair(Real(%i), %i), Tag(%i, %i))", ref, offset, tag, arity));
		return this;
	}

	public SstWriter emitAssignBaseRBO(int offset1, int offset2) throws IOException {
		out.write(String.format("Assign(Pair(%s, %i), R(Pair(%s, %i))", BASE, offset1, BASE, offset2));
		return this;
	}

	public SstWriter emitAssignBaseRRO(int offset1, int ref, int offset2) throws IOException {
		out.write(String.format("Assign(Pair(%s, %i), R(Pair(Real(%i), %i))", BASE, offset1, ref, offset2));
		return this;
	}

	public SstWriter emitAssignRefRBO(int ref, int offset1, int offset2) throws IOException {
		out.write(String.format("Assign(Pair(Real(%i), %i), R(Pair(%s, %i))", ref, offset1, BASE, offset2));
		return this;
	}

	public SstWriter emitAssignRefRRO(int ref1, int offset1, int ref2, int offset2) throws IOException {
		out.write(String.format("Assign(Pair(Real(%i), %i), R(Pair(Real(%i), %i))", ref1, offset1, ref2, offset2));
		return this;
	}

	public SstWriter emitAllocBaseAr(int offset, int arity) throws IOException {
		out.write(String.format("Alloc(Pair(%s, %i), %i)", BASE, offset, arity));
		return this;
	}

	public SstWriter emitAllocRefAr(int ref, int offset, int arity) throws IOException {
		out.write(String.format("Alloc(Pair(Real(%s), %i), %i)", ref, offset, arity));
		return this;
	}

	public SstWriter emitCommitBase(int offset) throws IOException {
		out.write(String.format("Commit(Pair(%s, %i), %i)", BASE, offset));
		return this;
	}

	public SstWriter emitCommitRef(int ref, int offset) throws IOException {
		out.write(String.format("Commit(Pair(Real(%i), %i), %i)", ref, offset));
		return this;
	}

	public void emitPackage(String packageName) throws IOException {
		out.write("module " + packageName + ";\n\n");
	}
	
	public void emitTypeDecl(String type, String name, String value) throws IOException{
		out.write(String.format("%s %s= %s", type, name, value));
	}

}
