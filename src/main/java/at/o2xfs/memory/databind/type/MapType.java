/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.type;

public class MapType extends MapLikeType {

	protected MapType(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces,
			JavaType keyType, JavaType valueType) {
		super(rawType, bindings, superClass, superInterfaces, keyType, valueType);
	}

	@Override
	public JavaType refine(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces) {
		return new MapType(rawType, bindings, superClass, superInterfaces, keyType, valueType);
	}

	public static MapType construct(Class<?> rawType, TypeBindings bindings, JavaType superClass,
			JavaType[] superInterfaces, JavaType keyType, JavaType valueType) {
		return new MapType(rawType, bindings, superClass, superInterfaces, keyType, valueType);
	}
}
