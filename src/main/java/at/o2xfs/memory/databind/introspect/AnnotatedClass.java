/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import at.o2xfs.memory.databind.AnnotationIntrospector;
import at.o2xfs.memory.databind.type.JavaType;
import at.o2xfs.memory.databind.type.TypeBindings;
import at.o2xfs.memory.databind.type.TypeFactory;

public class AnnotatedClass extends Annotated implements TypeResolutionContext {

	public static final class Creators {

		public final AnnotatedConstructor defaultConstructor;

		public Creators(AnnotatedConstructor defaultConstructor) {
			this.defaultConstructor = defaultConstructor;
		}
	}

	private final JavaType type;
	private final Class<?> rawType;
	private final TypeBindings bindings;
	private final List<JavaType> superTypes;
	private final AnnotationIntrospector annotationIntrospector;
	private final TypeFactory typeFactory;
	private final Map<Class<?>, Annotation> classAnnotations;
	private Creators creators;
	private AnnotatedMethodMap memberMethods;
	private List<AnnotatedField> fields;

	public AnnotatedClass(JavaType type, Class<?> rawType, List<JavaType> superTypes,
			Map<Class<?>, Annotation> classAnnotations, TypeBindings bindings,
			AnnotationIntrospector annotationIntrospector, TypeFactory typeFactory) {
		this.type = Objects.requireNonNull(type);
		this.rawType = rawType;
		this.superTypes = superTypes;
		this.classAnnotations = classAnnotations;
		this.bindings = bindings;
		this.annotationIntrospector = annotationIntrospector;
		this.typeFactory = Objects.requireNonNull(typeFactory);
	}

	private Creators creators() {
		if (creators == null) {
			creators = AnnotatedCreatorCollector.collectCreators(this, type);
		}
		return creators;
	}

	private AnnotatedMethodMap methods() {
		if (memberMethods == null) {
			memberMethods = AnnotatedMethodCollector
					.collectMethods(annotationIntrospector, this, typeFactory, type, superTypes);
		}
		return memberMethods;
	}

	public List<AnnotatedField> fields() {
		if (fields == null) {
			fields = AnnotatedFieldCollector.collectFields(annotationIntrospector, this, type);
		}
		return fields;
	}

	public AnnotatedMethod findMethod(String name) {
		return methods().find(name);
	}

	public AnnotatedConstructor getDefaultConstructor() {
		return creators().defaultConstructor;
	}

	@Override
	public <A extends Annotation> A getAnnotation(Class<A> cls) {
		return cls.cast(classAnnotations.get(cls));
	}

	public JavaType getType() {
		return type;
	}

	public Class<?> getRawType() {
		return rawType;
	}

	public Iterable<AnnotatedMethod> memberMethods() {
		return methods();
	}

	@Override
	public JavaType resolveType(Type t) {
		return typeFactory.constructType(t, bindings);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
