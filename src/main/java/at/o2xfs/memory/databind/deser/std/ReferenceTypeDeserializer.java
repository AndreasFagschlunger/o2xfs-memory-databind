/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser.std;

import at.o2xfs.memory.databind.BeanProperty;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.ReadableMemory;
import at.o2xfs.memory.databind.type.JavaType;

public abstract class ReferenceTypeDeserializer<T> extends StdDeserializer<T> {

	protected final JavaType fullType;
	protected final MemoryDeserializer<?> valueDeserializer;

	public ReferenceTypeDeserializer(JavaType fullType, MemoryDeserializer<?> deser) {
		super(fullType);
		this.fullType = fullType;
		valueDeserializer = deser;
	}

	protected abstract ReferenceTypeDeserializer<T> withResolved(MemoryDeserializer<?> valueDeser);

	@Override
	public T deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		Object contents = valueDeserializer.deserialize(memory.nextReference(), ctxt);
		return referenceValue(contents);
	}

	@Override
	public MemoryDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
		MemoryDeserializer<?> deser = valueDeserializer;
		if (deser == null) {
			deser = ctxt.findContextualValueDeserializer(fullType.getReferencedType(), property);
		}
		return withResolved(deser);
	}

	public abstract T referenceValue(Object contents);
}
