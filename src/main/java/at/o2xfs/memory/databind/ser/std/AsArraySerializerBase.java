/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser.std;

import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.ser.ContainerSerializer;
import at.o2xfs.memory.databind.type.JavaType;

public abstract class AsArraySerializerBase<T> extends ContainerSerializer<T> {

	protected final JavaType elementType;
	protected final MemorySerializer<Object> elementSerializer;

	protected AsArraySerializerBase(Class<?> cls, JavaType et, MemorySerializer<?> elementSerializer) {
		super(cls);
		elementType = et;
		this.elementSerializer = (MemorySerializer<Object>) elementSerializer;
	}
}
