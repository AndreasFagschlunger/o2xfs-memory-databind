/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind;

public abstract class MemorySerializer<T> {

	public abstract static class None extends MemorySerializer<Object> {

	}

	public abstract void serialize(T value, MemoryGenerator gen, SerializerProvider provider);
}
