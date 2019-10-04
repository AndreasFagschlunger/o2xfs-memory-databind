/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import at.o2xfs.memory.databind.type.JavaType;

public abstract class AnnotatedMember extends Annotated {

	protected final TypeResolutionContext typeContext;
	private final Map<Class<?>, Annotation> annotations;

	public AnnotatedMember(TypeResolutionContext typeContext, Map<Class<?>, Annotation> annotations) {
		this.typeContext = Objects.requireNonNull(typeContext);
		this.annotations = Collections.unmodifiableMap(new HashMap<>(annotations));
	}

	public abstract Class<?> getDeclaringClass();

	public String getFullName() {
		return getDeclaringClass().getName() + "#" + getName();
	}

	public abstract String getName();

	public abstract Member getMember();

	public abstract JavaType getType();

	public abstract Class<?> getRawType();

	public Map<Class<?>, Annotation> getAnnotations() {
		return annotations;
	}

	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return annotationClass.cast(annotations.get(annotationClass));
	}

	public abstract AnnotatedMember withAnnotations(Map<Class<?>, Annotation> annotations);
}
