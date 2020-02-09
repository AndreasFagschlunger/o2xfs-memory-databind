/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser.impl;

import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.type.JavaType;

public final class ReadOnlyClassToSerializerMap {

	public MemorySerializer<Object> untypedValueSerializer(JavaType valueType) {
		return null;
	}

	public MemorySerializer<Object> untypedValueSerializer(Class<?> rawType) {
		return null;
	}

	public MemorySerializer<Object> typedValueSerializer(Class<?> rawType) {
		return null;
	}

}
