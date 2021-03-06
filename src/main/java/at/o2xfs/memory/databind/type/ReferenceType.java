/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.type;

public class ReferenceType extends SimpleType {

	private JavaType referencedType;

	protected ReferenceType(Class<?> cls, TypeBindings bindings, JavaType superClass, JavaType[] superInts,
			JavaType refType) {
		super(cls, bindings, superClass, superInts);
		referencedType = refType;
	}

	protected ReferenceType(TypeBase base, JavaType referencedType) {
		super(base);
		this.referencedType = referencedType;
	}

	@Override
	public JavaType getContentType() {
		return referencedType;
	}

	@Override
	public JavaType getReferencedType() {
		return referencedType;
	}

	public static ReferenceType upgradeFrom(JavaType baseType, JavaType referencedType) {
		return new ReferenceType((TypeBase) baseType, referencedType);
	}

	public static JavaType construct(Class<?> cls, TypeBindings bindings, JavaType superClass, JavaType[] superInts,
			JavaType refType) {
		return new ReferenceType(cls, bindings, superClass, superInts, refType);
	}
}
