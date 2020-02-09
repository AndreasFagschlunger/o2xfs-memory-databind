/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import at.o2xfs.memory.databind.BeanDescription;
import at.o2xfs.memory.databind.cfg.MapperConfig;
import at.o2xfs.memory.databind.type.JavaType;

public abstract class ClassIntrospector {

	public abstract ClassIntrospector forMapper();

	public abstract ClassIntrospector forOperation(MapperConfig<?> mapperConfig);

	public abstract AnnotatedClass introspectClassAnnotations(JavaType type);

	public abstract BeanDescription introspectForDeserialization(JavaType type);

	public abstract BeanDescription introspectForDeserializationWithBuilder(JavaType type);

	public abstract BeanDescription introspectForSerialization(JavaType type);

}
