/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import at.o2xfs.memory.databind.type.JavaType;

public class AnnotatedField extends AnnotatedMember {

	private final Field field;

	public AnnotatedField(TypeResolutionContext ctxt, Field field, Map<Class<?>, Annotation> annotations) {
		super(ctxt, annotations);
		this.field = Objects.requireNonNull(field);

	}

	@Override
	public Class<?> getDeclaringClass() {
		return field.getDeclaringClass();
	}

	@Override
	public String getName() {
		return field.getName();
	}

	@Override
	public Field getMember() {
		return field;
	}

	@Override
	public JavaType getType() {
		return typeContext.resolveType(field.getGenericType());
	}

	@Override
	public Class<?> getRawType() {
		return field.getType();
	}

	@Override
	public AnnotatedField withAnnotations(Map<Class<?>, Annotation> annotations) {
		return new AnnotatedField(typeContext, field, annotations);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("field", field).toString();
	}
}
