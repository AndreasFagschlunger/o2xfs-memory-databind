/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.type;

public class CollectionType extends CollectionLikeType {

	private CollectionType(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces,
			JavaType elementType) {
		super(rawType, bindings, superClass, superInterfaces, elementType);
	}

	@Override
	public JavaType getContentType() {
		return elementType;
	}

	@Override
	public StringBuilder getGenericSignature(StringBuilder sb) {
		classSignature(rawClass, sb, false);
		sb.append('<');
		elementType.getGenericSignature(sb);
		sb.append(">;");
		return sb;
	}

	@Override
	public JavaType refine(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces) {
		return new CollectionType(rawType, bindings, superClass, superInterfaces, elementType);
	}

	public static CollectionType construct(Class<?> rawType, TypeBindings bindings, JavaType superClass,
			JavaType[] superInterfaces, JavaType elementType) {
		return new CollectionType(rawType, bindings, superClass, superInterfaces, elementType);
	}
}
