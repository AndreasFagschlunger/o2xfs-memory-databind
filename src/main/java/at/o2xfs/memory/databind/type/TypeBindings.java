/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.type;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;

public class TypeBindings {

	private static final TypeBindings EMPTY = new TypeBindings(new String[0], new JavaType[0]);

	private final String[] names;
	private final JavaType[] types;

	private TypeBindings(String[] names, JavaType[] types) {
		this.names = names;
		this.types = types;
	}

	protected JavaType[] typeParameterArray() {
		return types;
	}

	public JavaType getBoundType(int index) {
		return types[index];
	}

	public List<JavaType> getTypeParameters() {
		return Arrays.asList(types);
	}

	public static TypeBindings emptyBindings() {
		return EMPTY;
	}

	public int size() {
		return types.length;
	}

	@Override
	public String toString() {
		if (types.length == 0) {
			return "<>";
		}
		StringBuilder builder = new StringBuilder().append('<');
		for (int i = 0, len = types.length; i < len; ++i) {
			if (i > 0) {
				builder.append(',');
			}
			String sig = types[i].getGenericSignature();
			builder.append(sig);
		}
		return builder.append('>').toString();
	}

	public static TypeBindings create(Class<?> erasedType, JavaType[] types) {
		String[] names = new String[types.length];
		TypeVariable<?> vars[] = erasedType.getTypeParameters();
		for (int i = 0; i < vars.length; i++) {
			names[i] = vars[i].getName();
		}
		return new TypeBindings(names, types);
	}

	public static TypeBindings createIfNeeded(Class<?> erasedType, JavaType typeArg1) {
		TypeVariable<?>[] vars = erasedType.getTypeParameters();
		int varLen = (vars == null) ? 0 : vars.length;
		if (varLen == 0) {
			return EMPTY;
		}
		if (varLen != 1) {
			throw new IllegalArgumentException("Cannot create TypeBindings for class " + erasedType.getName()
					+ " with 1 type parameter: class expects " + varLen);
		}
		return new TypeBindings(new String[] { vars[0].getName() }, new JavaType[] { typeArg1 });
	}

	public JavaType findBoundType(String name) {
		JavaType result = null;
		for (int i = 0; i < names.length; i++) {
			if (names[i].equals(name)) {
				result = types[i];
				break;
			}
		}
		return result;
	}
}
