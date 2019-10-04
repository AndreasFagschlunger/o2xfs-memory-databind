/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind;

import at.o2xfs.memory.databind.cfg.MapperConfig;
import at.o2xfs.memory.databind.introspect.ClassIntrospector;
import at.o2xfs.memory.databind.type.JavaType;
import at.o2xfs.memory.databind.type.TypeFactory;

public class SerializationConfig extends MapperConfig {

	public SerializationConfig(TypeFactory typeFactory, ClassIntrospector classIntrospector,
			AnnotationIntrospector annotationIntrospector) {
		super(typeFactory, classIntrospector, annotationIntrospector);
	}

	public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
		return getTypeFactory().constructSpecializedType(baseType, subclass);
	}

	public BeanDescription introspect(JavaType type) {
		return getClassIntrospector().forSerialization(this, type);
	}
}
