/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser.std;

import at.o2xfs.memory.databind.ser.ContainerSerializer;

public abstract class ArraySerializerBase<T> extends ContainerSerializer<T> {

	public ArraySerializerBase(Class<?> cls) {
		super(cls);
	}

}
