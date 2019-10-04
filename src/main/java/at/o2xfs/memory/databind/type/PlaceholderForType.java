/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.type;

public class PlaceholderForType extends TypeBase {

	private final int ordinal;

	private JavaType actualType;

	public PlaceholderForType(int ordinal) {
		super(Object.class, TypeBindings.emptyBindings(), TypeFactory.unknownType(), null);
		this.ordinal = ordinal;
	}

	public void actualType(JavaType actualType) {
		this.actualType = actualType;
	}

	public JavaType actualType() {
		return actualType;
	}

	@Override
	public StringBuilder getGenericSignature(StringBuilder sb) {
		sb.append('$').append(ordinal + 1);
		return sb;
	}

	@Override
	public boolean isContainerType() {
		return false;
	}

	@Override
	public JavaType refine(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces) {
		throw new UnsupportedOperationException();
	}
}
