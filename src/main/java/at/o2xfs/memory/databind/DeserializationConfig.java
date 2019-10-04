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

public class DeserializationConfig extends MapperConfig {

	public DeserializationConfig(TypeFactory typeFactory, ClassIntrospector classIntrospector,
			AnnotationIntrospector annotationIntrospector) {
		super(typeFactory, classIntrospector, annotationIntrospector);
	}

	public <T extends BeanDescription> T introspect(JavaType type) {
		return (T) getClassIntrospector().forDeserialization(this, type);
	}

	public <T extends BeanDescription> T introspectForBuilder(JavaType type) {
		return (T) getClassIntrospector().forDeserializationWithBuilder(this, type);
	}
}
