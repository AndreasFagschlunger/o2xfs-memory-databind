/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser.std;

import at.o2xfs.memory.databind.MemorySerializer;

public abstract class StdSerializer<T> extends MemorySerializer<T> {

	protected final Class<?> handledType;

	protected StdSerializer(Class<?> handledType) {
		this.handledType = handledType;
	}

}
