/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind;

import java.io.IOException;

import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.jsontype.TypeSerializer;

public abstract class MemorySerializer<T> {

	public abstract static class None extends MemorySerializer<Object> {

	}

	public MemorySerializer<?> createContextual(SerializerProvider provider, BeanProperty prop) {
		return this;
	}

	public Class<?> handledType() {
		return Object.class;
	}

	public void resolve(SerializerProvider provider) {

	}

	public abstract void serialize(T value, MemoryGenerator gen, SerializerProvider provider) throws IOException;

	public void serializeWithType(T value, MemoryGenerator gen, SerializerProvider serializers,
			TypeSerializer typeSer) {
		Class<?> clz = handledType();
		if (clz == null) {
			clz = value.getClass();
		}
		serializers
				.reportBadDefinition(clz,
						String
								.format("Type id handling not implemented for type %s (by serializer of type %s)",
										clz.getName(), getClass().getName()));
	}
}
