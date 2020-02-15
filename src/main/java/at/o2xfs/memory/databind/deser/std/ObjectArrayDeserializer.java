/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser.std;

import java.lang.reflect.Array;

import at.o2xfs.memory.databind.BeanProperty;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.ReadableMemory;
import at.o2xfs.memory.databind.type.JavaType;

public class ObjectArrayDeserializer extends ContainerDeserializerBase<Object[]> {

	protected final Class<?> elementClass;

	protected MemoryDeserializer<Object> elementDeserializer;

	protected ObjectArrayDeserializer(ObjectArrayDeserializer base, MemoryDeserializer<Object> elemDeser) {
		super(base);
		elementClass = base.elementClass;
		elementDeserializer = elemDeser;
	}

	public ObjectArrayDeserializer(JavaType arrayType, MemoryDeserializer<Object> elemDeser) {
		super(arrayType);
		elementClass = arrayType.getContentType().getRawClass();
		elementDeserializer = elemDeser;
	}

	@Override
	public MemoryDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
		MemoryDeserializer<?> valueDeser = elementDeserializer;
		JavaType vt = containerType.getContentType();
		if (valueDeser == null) {
			valueDeser = ctxt.findContextualValueDeserializer(vt, property);
		} else {
			valueDeser = ctxt.handleSecondaryContextualization(valueDeser, property, vt);
		}
		return withResolved(valueDeser);
	}

	@Override
	public Object[] deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		int length = memory.nextUnsignedShort();
		Object[] result = (Object[]) Array.newInstance(elementClass, length);
		for (int i = 0; i < result.length; i++) {
			ReadableMemory next = memory.nextReference();
			if (next == null) {
				continue;
			}
			Object value = elementDeserializer.deserialize(next, ctxt);
			result[i] = value;
		}
		return result;
	}

	public MemoryDeserializer<?> withResolved(MemoryDeserializer<?> elemDeser) {
		if (elementDeserializer == elemDeser) {
			return this;
		}
		return new ObjectArrayDeserializer(this, (MemoryDeserializer<Object>) elemDeser);
	}

}
