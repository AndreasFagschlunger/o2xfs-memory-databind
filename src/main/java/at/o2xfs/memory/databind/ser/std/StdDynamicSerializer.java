/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser.std;

import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.ser.impl.PropertySerializerMap;

public abstract class StdDynamicSerializer<T> extends MemorySerializer<T> {

	protected PropertySerializerMap dynamicValueSerializers = PropertySerializerMap.emptyForProperties();
}
