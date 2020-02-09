/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ext.jdk8;

import java.util.Optional;

import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.ReadableMemory;
import at.o2xfs.memory.databind.deser.std.ReferenceTypeDeserializer;
import at.o2xfs.memory.databind.type.JavaType;

public class Jdk8OptionalDeserializer extends ReferenceTypeDeserializer<Optional<?>> {

	public Jdk8OptionalDeserializer(JavaType fullType, MemoryDeserializer<?> deser) {
		super(fullType, deser);
	}

	@Override
	public Optional<?> deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		ReadableMemory next = memory.nextReference();
		Object contents = null;
		if (next != null) {
			contents = valueDeserializer.deserialize(next, ctxt);
		}
		return referenceValue(contents);

	}

	@Override
	public Object getNullValue(DeserializationContext ctxt) {
		return Optional.empty();
	}

	@Override
	protected Jdk8OptionalDeserializer withResolved(MemoryDeserializer<?> valueDeser) {
		return new Jdk8OptionalDeserializer(fullType, valueDeser);
	}

	@Override
	public Optional<?> referenceValue(Object contents) {
		return Optional.ofNullable(contents);
	}
}
