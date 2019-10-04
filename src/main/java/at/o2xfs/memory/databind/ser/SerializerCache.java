/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser;

import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.type.JavaType;

public class SerializerCache {

	public void addAndResolveNonTypedSerializer(Class<?> rawType, JavaType fullType, MemorySerializer ser,
			SerializerProvider provider) {
		if (ser instanceof ResolvableSerializer) {
			((ResolvableSerializer) ser).resolve(provider);
		}
	}
}
