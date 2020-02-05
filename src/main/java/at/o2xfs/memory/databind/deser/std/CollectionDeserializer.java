/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser.std;

import java.util.ArrayList;
import java.util.Collection;

import at.o2xfs.memory.databind.BeanProperty;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.ReadableMemory;
import at.o2xfs.memory.databind.type.JavaType;

public class CollectionDeserializer extends ContainerDeserializerBase<Collection<Object>> {

	protected final MemoryDeserializer<Object> valueDeserializer;

	public CollectionDeserializer(JavaType collectionType, MemoryDeserializer<Object> valueDeser) {
		super(collectionType);
		this.valueDeserializer = valueDeser;
	}

	protected CollectionDeserializer withResolved(MemoryDeserializer<?> valueDeser) {
		return new CollectionDeserializer(containerType, (MemoryDeserializer<Object>) valueDeser);
	}

	@Override
	public CollectionDeserializer createContextual(DeserializationContext ctxt, BeanProperty property) {
		JavaType vt = containerType.getContentType();
		MemoryDeserializer<?> valueDeser = valueDeserializer;
		if (valueDeser == null) {
			valueDeser = ctxt.findContextualValueDeserializer(vt, property);
		}
		if (valueDeser != null) {
			return withResolved(valueDeser);
		}
		return this;
	}

	@Override
	public Collection<Object> deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		int length = memory.nextUnsignedShort();
		return deserialize(length, memory.nextReference(), ctxt, new ArrayList<Object>());
	}

	public Collection<Object> deserialize(int length, ReadableMemory memory, DeserializationContext ctxt,
			Collection<Object> result) {
		for (int i = 0; i < length; i++) {
			Object value = valueDeserializer.deserialize(memory.nextReference(), ctxt);
			result.add(value);
		}
		return result;
	}

}
