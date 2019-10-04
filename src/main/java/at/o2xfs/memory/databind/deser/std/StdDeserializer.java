/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser.std;

import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.type.JavaType;

public abstract class StdDeserializer<T> extends MemoryDeserializer<T> {

	protected final Class<?> valueClass;

	public StdDeserializer(Class<?> valueClass) {
		this.valueClass = valueClass;
	}

	public StdDeserializer(JavaType valueType) {
		this(valueType.getRawClass());
	}
}
