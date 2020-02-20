/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.type;

import java.lang.reflect.Array;

public final class ArrayType extends TypeBase {

	protected final JavaType componentType;
	protected final Object emptyArray;

	protected ArrayType(JavaType componentType, TypeBindings bindings, Object emptyArray) {
		super(emptyArray.getClass(), bindings, null, null);
		this.componentType = componentType;
		this.emptyArray = emptyArray;
	}

	@Override
	public JavaType getContentType() {
		return componentType;
	}

	@Override
	public StringBuilder getGenericSignature(StringBuilder sb) {
		sb.append('[');
		componentType.getGenericSignature(sb);
		return sb;
	}

	@Override
	public boolean isArrayType() {
		return true;
	}

	@Override
	public boolean isContainerType() {
		return true;
	}

	@Override
	public JavaType refine(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces) {
		return null;
	}

	public static ArrayType construct(JavaType componentType, TypeBindings bindings) {
		Object emptyArray = Array.newInstance(componentType.getRawClass(), 0);
		return new ArrayType(componentType, bindings, emptyArray);
	}

}
