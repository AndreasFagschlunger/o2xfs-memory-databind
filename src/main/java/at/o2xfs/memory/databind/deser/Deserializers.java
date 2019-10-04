/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser;

import at.o2xfs.memory.databind.BeanDescription;
import at.o2xfs.memory.databind.DeserializationConfig;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.type.ReferenceType;

public interface Deserializers {

	MemoryDeserializer<?> findReferenceDeserializer(ReferenceType refType, DeserializationConfig config,
			BeanDescription beanDesc);
}
