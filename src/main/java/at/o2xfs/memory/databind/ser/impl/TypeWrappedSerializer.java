/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser.impl;

import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.jsontype.TypeSerializer;

public final class TypeWrappedSerializer extends MemorySerializer<Object> {

	private final TypeSerializer typeSerializer;
	private final MemorySerializer<Object> serializer;

	public TypeWrappedSerializer(TypeSerializer typeSer, MemorySerializer<?> ser) {
		typeSerializer = typeSer;
		serializer = (MemorySerializer<Object>) ser;
	}

	@Override
	public void serialize(Object value, MemoryGenerator gen, SerializerProvider provider) {
		serializer.serializeWithType(value, gen, provider, typeSerializer);
	}

}
