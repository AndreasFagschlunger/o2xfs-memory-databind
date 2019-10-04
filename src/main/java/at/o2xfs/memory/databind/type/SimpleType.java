/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.type;

public class SimpleType extends TypeBase {

	protected SimpleType(Class<?> cls) {
		super(cls, TypeBindings.emptyBindings(), null, null);
	}

	public SimpleType(Class<?> rawClass, TypeBindings bindings, JavaType superClass, JavaType[] superInts) {
		super(rawClass, bindings, superClass, superInts);
	}

	protected SimpleType(TypeBase base) {
		super(base);
	}

	@Override
	protected String buildCanonicalName() {
		StringBuilder sb = new StringBuilder();
		sb.append(rawClass.getName());

		final int count = bindings.size();
		if (count > 0) {
			sb.append('<');
			for (int i = 0; i < count; ++i) {
				JavaType t = containedType(i);
				if (i > 0) {
					sb.append(',');
				}
				sb.append(t.toCanonical());
			}
			sb.append('>');
		}
		return sb.toString();
	}

	@Override
	public StringBuilder getGenericSignature(StringBuilder sb) {
		classSignature(rawClass, sb, false);
		final int count = bindings.size();
		if (count > 0) {
			sb.append('<');
			for (int i = 0; i < count; ++i) {
				sb = containedType(i).getGenericSignature(sb);
			}
			sb.append('>');
		}
		sb.append(';');
		return sb;
	}

	@Override
	public boolean isContainerType() {
		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(40);
		sb.append("[simple type, class ").append(buildCanonicalName()).append(']');
		return sb.toString();
	}

	@Override
	public JavaType refine(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces) {
		return null;
	}
}
