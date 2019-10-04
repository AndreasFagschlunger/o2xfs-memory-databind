/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.type;

import java.util.Map;

public class MapLikeType extends TypeBase {

	protected final JavaType keyType;
	protected final JavaType valueType;

	protected MapLikeType(TypeBase base, JavaType keyType, JavaType valueType) {
		super(base);
		this.keyType = keyType;
		this.valueType = valueType;
	}

	protected MapLikeType(Class<?> rawClass, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces,
			JavaType keyType, JavaType valueType) {
		super(rawClass, bindings, superClass, superInterfaces);
		this.keyType = keyType;
		this.valueType = valueType;
	}

	@Override
	public JavaType getContentType() {
		return valueType;
	}

	@Override
	public StringBuilder getGenericSignature(StringBuilder sb) {
		classSignature(rawClass, sb, false);
		sb.append('<');
		keyType.getGenericSignature(sb);
		valueType.getGenericSignature(sb);
		sb.append(">;");
		return sb;
	}

	@Override
	public JavaType getKeyType() {
		return keyType;
	}

	@Override
	public boolean isContainerType() {
		return true;
	}

	@Override
	public boolean isMapLikeType() {
		return true;
	}

	public boolean isTrueMapType() {
		return Map.class.isAssignableFrom(rawClass);
	}

	@Override
	public JavaType refine(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces) {
		return new MapLikeType(rawType, bindings, superClass, superInterfaces, keyType, valueType);
	}
}
