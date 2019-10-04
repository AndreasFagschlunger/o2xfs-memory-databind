/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import at.o2xfs.memory.databind.type.JavaType;

public class AnnotatedMethod extends AnnotatedWithParams {

	private final Method method;

	public AnnotatedMethod(TypeResolutionContext ctxt, Method method, Map<Class<?>, Annotation> annotations) {
		super(ctxt, annotations);
		this.method = Objects.requireNonNull(method);
	}

	@Override
	public Class<?> getDeclaringClass() {
		return method.getDeclaringClass();
	}

	@Override
	public String getFullName() {
		return String.format("%s(%d params)", super.getFullName(), getParameterCount());
	}

	@Override
	public String getName() {
		return method.getName();
	}

	@Override
	public Method getMember() {
		return method;
	}

	public int getParameterCount() {
		return method.getParameterCount();
	}

	@Override
	public Class<?> getRawType() {
		return method.getReturnType();
	}

	@Override
	public JavaType getType() {
		return typeContext.resolveType(method.getGenericReturnType());
	}

	public Method getMethod() {
		return method;
	}

	public boolean hasReturnType() {
		Class<?> returnType = method.getReturnType();
		return returnType != Void.TYPE && returnType != Void.class;
	}

	@Override
	public AnnotatedMethod withAnnotations(Map<Class<?>, Annotation> annotations) {
		return new AnnotatedMethod(typeContext, method, annotations);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", getName()).toString();
	}

	@Override
	public JavaType getParameterType(int index) {
		Type[] types = method.getGenericParameterTypes();
		if (index >= types.length) {
			return null;
		}
		return typeContext.resolveType(types[index]);
	}

	@Override
	public final Object call() throws Exception {
		return method.invoke(null);
	}
}
