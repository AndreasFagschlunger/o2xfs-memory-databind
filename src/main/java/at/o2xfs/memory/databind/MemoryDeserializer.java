/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind;

public abstract class MemoryDeserializer<T> {

	public abstract static class None extends MemoryDeserializer<Object> {

	}

	public abstract T deserialize(ReadableMemory memory, DeserializationContext ctxt);

	public boolean isCachable() {
		return false;
	}

	public void resolve(DeserializationContext ctxt) {

	}

	public MemoryDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
		return this;
	}
}
