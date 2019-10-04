/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.cfg;

import at.o2xfs.memory.databind.AnnotationIntrospector;
import at.o2xfs.memory.databind.introspect.ClassIntrospector;
import at.o2xfs.memory.databind.type.JavaType;
import at.o2xfs.memory.databind.type.TypeFactory;

public class MapperConfig {

	protected final TypeFactory typeFactory;
	protected final ClassIntrospector classIntrospector;
	protected final AnnotationIntrospector annotationIntrospector;

	public MapperConfig(TypeFactory typeFactory, ClassIntrospector classIntrospector,
			AnnotationIntrospector annotationIntrospector) {
		this.typeFactory = typeFactory;
		this.classIntrospector = classIntrospector;
		this.annotationIntrospector = annotationIntrospector;
	}

	public final JavaType constructType(Class<?> cls) {
		return getTypeFactory().constructType(cls);
	}

	public AnnotationIntrospector getAnnotationIntrospector() {
		return annotationIntrospector;
	}

	public ClassIntrospector getClassIntrospector() {
		return classIntrospector;
	}

	public final TypeFactory getTypeFactory() {
		return typeFactory;
	}
}
