/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser;

import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.ser.impl.ReadOnlyClassToSerializerMap;
import at.o2xfs.memory.databind.type.JavaType;

public class SerializerCache {

	private final ReadOnlyClassToSerializerMap readOnlyClassToSerializerMap;

	public SerializerCache() {
		readOnlyClassToSerializerMap = new ReadOnlyClassToSerializerMap();
	}

	public void addAndResolveNonTypedSerializer(Class<?> rawType, JavaType fullType, MemorySerializer<Object> ser,
			SerializerProvider provider) {
		ser.resolve(provider);
	}

	public void addAndResolveNonTypedSerializer(JavaType type, MemorySerializer<Object> ser,
			SerializerProvider provider) {
		ser.resolve(provider);
	}

	public MemorySerializer<Object> untypedValueSerializer(JavaType type) {
		return null;
	}

	public ReadOnlyClassToSerializerMap getReadOnlyLookupMap() {
		return readOnlyClassToSerializerMap;
	}

	public void addTypedSerializer(Class<?> rawType, MemorySerializer<Object> ser) {

	}
}
