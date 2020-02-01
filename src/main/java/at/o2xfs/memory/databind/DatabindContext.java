/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind;

import java.lang.reflect.Type;

import at.o2xfs.memory.databind.cfg.MapperConfig;
import at.o2xfs.memory.databind.introspect.AnnotatedClass;
import at.o2xfs.memory.databind.introspect.ClassIntrospector;
import at.o2xfs.memory.databind.type.JavaType;
import at.o2xfs.memory.databind.type.TypeFactory;

public abstract class DatabindContext {

	protected abstract ClassIntrospector classIntrospector();

	public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
		if (baseType.getRawClass() == subclass) {
			return baseType;
		}
		return getConfig().constructSpecializedType(baseType, subclass);
	}

	public JavaType constructType(Type type) {
		if (type == null) {
			return null;
		}
		return getTypeFactory().constructType(type);
	}

	public abstract MapperConfig<?> getConfig();

	public abstract TypeFactory getTypeFactory();

	public AnnotatedClass introspectClassAnnotations(JavaType type) {
		return classIntrospector().introspectClassAnnotations(type);
	}

	public abstract BeanDescription introspectBeanDescription(JavaType type);

	public void reportBadDefinition(Class<?> type, String msg) {

	}
}
