/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind;

import at.o2xfs.memory.databind.deser.NullValueProvider;

public abstract class MemoryDeserializer<T> implements NullValueProvider {

	public abstract static class None extends MemoryDeserializer<Object> {

	}

	public abstract T deserialize(ReadableMemory memory, DeserializationContext ctxt);

	@Override
	public Object getNullValue(DeserializationContext ctxt) {
		return null;
	}

	public boolean isCachable() {
		return false;
	}

	public void resolve(DeserializationContext ctxt) {

	}

	public MemoryDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
		return this;
	}
}
