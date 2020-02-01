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

public abstract class MapperConfig<T extends MapperConfig<T>> {

	protected final BaseSettings base;

	protected MapperConfig(BaseSettings base) {
		this.base = base;
	}

	public abstract ClassIntrospector classIntrospectorInstance();

	public abstract JavaType constructType(Class<?> cls);

	public abstract JavaType constructSpecializedType(JavaType baseType, Class<?> subclass);

	public AnnotationIntrospector getAnnotationIntrospector() {
		return base.getAnnotationIntrospector();
	}

	public abstract TypeFactory getTypeFactory();

}
