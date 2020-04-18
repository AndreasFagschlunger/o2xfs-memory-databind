/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind;

import java.util.List;
import java.util.Objects;

import at.o2xfs.memory.databind.introspect.AnnotatedClass;
import at.o2xfs.memory.databind.introspect.AnnotatedConstructor;
import at.o2xfs.memory.databind.introspect.AnnotatedMethod;
import at.o2xfs.memory.databind.introspect.BeanPropertyDefinition;
import at.o2xfs.memory.databind.type.JavaType;

public abstract class BeanDescription {

	protected final JavaType type;

	protected BeanDescription(JavaType type) {
		this.type = Objects.requireNonNull(type);
	}

	public abstract AnnotatedConstructor findDefaultConstructor();

	public abstract AnnotatedMethod findMethod(String name, Class<?>[] parameterTypes);

	public abstract Class<?> findPOJOBuilder();

	public abstract List<BeanPropertyDefinition> findProperties();

	public abstract AnnotatedClass getClassInfo();

	public JavaType getType() {
		return type;
	}

}
