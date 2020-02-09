/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser.std;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import at.o2xfs.memory.databind.BeanProperty;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.ReadableMemory;
import at.o2xfs.memory.databind.annotation.NullTerminated;
import at.o2xfs.memory.databind.introspect.AnnotatedMember;
import at.o2xfs.memory.databind.type.JavaType;

public class CollectionDeserializer extends ContainerDeserializerBase<Collection<Object>> {

	protected final MemoryDeserializer<Object> valueDeserializer;

	protected final boolean nullTerminated;

	public CollectionDeserializer(JavaType collectionType, MemoryDeserializer<Object> valueDeser) {
		this(collectionType, valueDeser, false);
	}

	public CollectionDeserializer(JavaType collectionType, MemoryDeserializer<Object> valueDeser,
			boolean nullTerminated) {
		super(collectionType);
		this.valueDeserializer = valueDeser;
		this.nullTerminated = nullTerminated;
	}

	public Collection<Object> deserializeNullTerminated(ReadableMemory memory, DeserializationContext ctxt) {
		if (memory == null) {
			return Collections.emptyList();
		}
		List<Object> result = new ArrayList<>();
		ReadableMemory next;
		while ((next = memory.nextReference()) != null) {
			result.add(valueDeserializer.deserialize(next, ctxt));
		}
		return result;
	}

	protected CollectionDeserializer withResolved(MemoryDeserializer<?> valueDeser, boolean nullTerminated) {
		return new CollectionDeserializer(containerType, (MemoryDeserializer<Object>) valueDeser, nullTerminated);
	}

	@Override
	public CollectionDeserializer createContextual(DeserializationContext ctxt, BeanProperty property) {
		JavaType vt = containerType.getContentType();
		MemoryDeserializer<?> valueDeser = valueDeserializer;
		AnnotatedMember member = property.getMember();
		NullTerminated nullTerminated = member.getAnnotation(NullTerminated.class);
		if (valueDeser == null) {
			valueDeser = ctxt.findContextualValueDeserializer(vt, property);
		}
		if (valueDeser != null || nullTerminated != null) {
			return withResolved(valueDeser, nullTerminated != null);
		}
		return this;
	}

	@Override
	public Collection<Object> deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		if (nullTerminated) {
			return deserializeNullTerminated(memory.nextReference(), ctxt);
		}
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
