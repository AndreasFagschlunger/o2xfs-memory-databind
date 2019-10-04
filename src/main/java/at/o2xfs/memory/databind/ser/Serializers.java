/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser;

import at.o2xfs.memory.databind.SerializationConfig;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.type.ReferenceType;

public interface Serializers {

	MemorySerializer<?> findReferenceSerializer(SerializationConfig config, ReferenceType refType,
			MemorySerializer<?> contentTypeSerializer);
}
