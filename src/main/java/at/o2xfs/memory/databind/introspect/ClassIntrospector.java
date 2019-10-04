/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import at.o2xfs.memory.databind.BeanDescription;
import at.o2xfs.memory.databind.DeserializationConfig;
import at.o2xfs.memory.databind.SerializationConfig;
import at.o2xfs.memory.databind.type.JavaType;

public abstract class ClassIntrospector {

	public abstract BeanDescription forDeserialization(DeserializationConfig cfg, JavaType type);

	public abstract BeanDescription forDeserializationWithBuilder(DeserializationConfig cfg, JavaType type);

	public abstract BeanDescription forSerialization(SerializationConfig cfg, JavaType type);
}
