package jabsc;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

class MethodDefinition {

	private static final String VOID = "void";
	private final String clazz;
	private final String type;
	private final String name;
	private final List<String> paramTypes;

	public MethodDefinition(String clazz, String type, String method) {
		this(clazz, type, method, Collections.emptyList());
	}

	public MethodDefinition(String clazz, String type, String method, List<String> paramTypes) {
		this.clazz = clazz;
		this.type = type == null ? VOID : type;
		this.name = method;
		this.paramTypes = paramTypes;
	}

	public boolean matches(String methodName, List<String> params) {
		if (params.size() != paramTypes.size()) {
			return false;
		}
		return this.name.equals(methodName);
	}

	public String type() {
		return isVoid() ? null : this.type;
	}

	protected boolean isVoid() {
		return this.type.equals(VOID);
	}

	protected boolean hasParameters() {
		return !this.paramTypes.isEmpty();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o == this) {
			return true;
		}
		if (o instanceof MethodDefinition == false) {
			return false;
		}
		MethodDefinition md = (MethodDefinition) o;
		return Objects.equals(this.clazz, md.clazz) && Objects.equals(this.type, md.type)
				&& Objects.equals(this.name, md.name);
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return clazz + ": " + type + " " + name + "(" + String.join(",", this.paramTypes) + ")";
	}

}
