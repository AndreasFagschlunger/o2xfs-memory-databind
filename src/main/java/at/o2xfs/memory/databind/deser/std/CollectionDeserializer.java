/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser.std;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import at.o2xfs.memory.databind.BeanProperty;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.ReadableMemory;
import at.o2xfs.memory.databind.annotation.MemoryList;
import at.o2xfs.memory.databind.deser.ContextualDeserializer;
import at.o2xfs.memory.databind.deser.win32.UShortDeserializer;
import at.o2xfs.memory.databind.type.JavaType;

public class CollectionDeserializer extends ContainerDeserializerBase<Collection<Object>>
		implements ContextualDeserializer {

	protected final MemoryDeserializer<Object> valueDeserializer;

	private final int indexBytes;

	public CollectionDeserializer(JavaType collectionType, MemoryDeserializer<Object> valueDeser) {
		this(collectionType, valueDeser, 0);
	}

	private CollectionDeserializer(JavaType collectionType, MemoryDeserializer<Object> valueDeser, int indexBytes) {
		super(collectionType);
		this.valueDeserializer = valueDeser;
		this.indexBytes = indexBytes;
	}

	protected CollectionDeserializer withResolved(MemoryDeserializer<?> valueDeser, int indexBytes) {
		return new CollectionDeserializer(containerType, (MemoryDeserializer<Object>) valueDeser, indexBytes);
	}

	@Override
	public CollectionDeserializer createContextual(DeserializationContext ctxt, BeanProperty property) {
		JavaType vt = containerType.getContentType();
		MemoryList nativeList = property.getMember().getAnnotation(MemoryList.class);
		MemoryDeserializer<?> valueDeser = ctxt.findContextualValueDeserializer(vt, property);
		if (valueDeser != null) {
			return withResolved(valueDeser, nativeList == null ? 0 : nativeList.indexBytes());
		}
		return null;
	}

	@Override
	public Collection<Object> deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		return deserialize(memory, ctxt, new ArrayList<Object>());
	}

	public Collection<Object> deserialize(ReadableMemory memory, DeserializationContext ctxt,
			Collection<Object> result) {
		Objects.requireNonNull(memory);
		int size = 0;
		switch (indexBytes) {
		case 2:
			size = UShortDeserializer.instance.deserialize(memory, ctxt);
			break;
		}
		for (int i = 0; i < size; i++) {
			Object value = valueDeserializer.deserialize(memory.dereference(), ctxt);
			result.add(value);
		}
		return result;
	}

}
