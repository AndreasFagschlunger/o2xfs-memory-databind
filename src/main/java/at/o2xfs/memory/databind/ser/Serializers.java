/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser;

import at.o2xfs.memory.databind.BeanDescription;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializationConfig;
import at.o2xfs.memory.databind.jsontype.TypeSerializer;
import at.o2xfs.memory.databind.type.ReferenceType;

public interface Serializers {

	MemorySerializer<?> findReferenceSerializer(SerializationConfig config, ReferenceType type,
			BeanDescription beanDesc, TypeSerializer contentTypeSerializer);
}
