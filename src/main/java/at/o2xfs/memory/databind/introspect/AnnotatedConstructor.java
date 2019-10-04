/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

import at.o2xfs.memory.databind.type.JavaType;

public class AnnotatedConstructor extends AnnotatedWithParams {

	private final Constructor<?> constructor;

	public AnnotatedConstructor(TypeResolutionContext ctxt, Map<Class<?>, Annotation> annotations,
			Constructor<?> constructor) {
		super(ctxt, annotations);
		this.constructor = Objects.requireNonNull(constructor);
	}

	@Override
	public Object call() throws Exception {
		return constructor.newInstance();
	}

	@Override
	public Class<?> getDeclaringClass() {
		return constructor.getDeclaringClass();
	}

	@Override
	public String getName() {
		return constructor.getName();
	}

	@Override
	public Member getMember() {
		return constructor;
	}

	@Override
	public JavaType getParameterType(int index) {
		Type[] types = constructor.getGenericParameterTypes();
		if (index >= types.length) {
			return null;
		}
		return typeContext.resolveType(types[index]);
	}

	@Override
	public JavaType getType() {
		return typeContext.resolveType(getRawType());
	}

	@Override
	public Class<?> getRawType() {
		return constructor.getDeclaringClass();
	}

	@Override
	public AnnotatedMember withAnnotations(Map<Class<?>, Annotation> annotations) {
		return new AnnotatedConstructor(typeContext, annotations, constructor);
	}
}
