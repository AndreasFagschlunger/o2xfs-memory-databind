/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.type;

public abstract class TypeBase extends JavaType {

	private final JavaType superClass;
	private final JavaType[] superInterfaces;
	protected final TypeBindings bindings;

	protected TypeBase(Class<?> rawClass, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces) {
		super(rawClass);
		this.bindings = bindings;
		this.superClass = superClass;
		this.superInterfaces = superInterfaces;
	}

	protected TypeBase(TypeBase base) {
		super(base);
		this.bindings = base.bindings;
		this.superClass = base.superClass;
		this.superInterfaces = base.superInterfaces;
	}

	protected String buildCanonicalName() {
		return rawClass.getName();
	}

	@Override
	public JavaType containedType(int index) {
		return bindings.getBoundType(index);
	}

	@Override
	public JavaType findSuperType(Class<?> rawTarget) {
		if (rawClass == rawTarget) {
			return this;
		}
		if (rawTarget.isInterface() && superInterfaces != null) {
			for (int i = 0; i < superInterfaces.length; i++) {
				JavaType type = superInterfaces[i].findSuperType(rawTarget);
				if (type != null) {
					return type;
				}
			}
		}
		if (superClass != null) {
			JavaType type = superClass.findSuperType(rawTarget);
			if (type != null) {
				return type;
			}
		}
		return null;

	}

	@Override
	public TypeBindings getBindings() {
		return bindings;
	}

	@Override
	public JavaType getSuperClass() {
		return superClass;
	}

	@Override
	public String toCanonical() {
		return buildCanonicalName();
	}

	protected static StringBuilder classSignature(Class<?> cls, StringBuilder sb, boolean trailingSemicolon) {
		if (cls.isPrimitive()) {
			if (cls == Boolean.TYPE) {
				sb.append('Z');
			} else if (cls == Byte.TYPE) {
				sb.append('B');
			} else if (cls == Short.TYPE) {
				sb.append('S');
			} else if (cls == Character.TYPE) {
				sb.append('C');
			} else if (cls == Integer.TYPE) {
				sb.append('I');
			} else if (cls == Long.TYPE) {
				sb.append('J');
			} else if (cls == Float.TYPE) {
				sb.append('F');
			} else if (cls == Double.TYPE) {
				sb.append('D');
			} else if (cls == Void.TYPE) {
				sb.append('V');
			} else {
				throw new IllegalStateException("Unrecognized primitive type: " + cls.getName());
			}
		} else {
			sb.append('L');
			String name = cls.getName();
			for (int i = 0, len = name.length(); i < len; ++i) {
				char c = name.charAt(i);
				if (c == '.')
					c = '/';
				sb.append(c);
			}
			if (trailingSemicolon) {
				sb.append(';');
			}
		}
		return sb;
	}
}
