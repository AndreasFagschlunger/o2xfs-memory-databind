/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import at.o2xfs.memory.databind.cfg.MapperConfig;
import at.o2xfs.memory.databind.type.JavaType;
import at.o2xfs.memory.databind.type.TypeBindings;

public class AnnotatedClass extends Annotated implements TypeResolutionContext {

	public static final class Creators {

		public final AnnotatedConstructor defaultConstructor;

		public Creators(AnnotatedConstructor defaultConstructor) {
			this.defaultConstructor = defaultConstructor;
		}
	}

	private final MapperConfig<?> config;

	private final JavaType type;
	private final Class<?> rawType;
	private final TypeBindings bindings;
	private final List<JavaType> superTypes;
	private final Map<Class<?>, Annotation> classAnnotations;
	private Creators creators;
	private AnnotatedMethodMap memberMethods;
	private List<AnnotatedField> fields;

	AnnotatedClass(MapperConfig<?> config, JavaType type, Class<?> rawType, List<JavaType> superTypes,
			Map<Class<?>, Annotation> classAnnotations, TypeBindings bindings) {
		this.config = config;
		this.type = Objects.requireNonNull(type);
		this.rawType = rawType;
		this.superTypes = superTypes;
		this.classAnnotations = classAnnotations;
		this.bindings = bindings;
	}

	AnnotatedClass(Class<?> rawType) {
		config = null;
		type = null;
		this.rawType = rawType;
		superTypes = Collections.emptyList();
		classAnnotations = Collections.emptyMap();
		bindings = TypeBindings.emptyBindings();
	}

	private Creators creators() {
		if (creators == null) {
			creators = AnnotatedCreatorCollector.collectCreators(this, type);
		}
		return creators;
	}

	private AnnotatedMethodMap methods() {
		if (memberMethods == null) {
			memberMethods = AnnotatedMethodCollector.collectMethods(config, this, type, superTypes);
		}
		return memberMethods;
	}

	public List<AnnotatedField> fields() {
		if (fields == null) {
			fields = AnnotatedFieldCollector.collectFields(config, this, type);
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
		return config.getTypeFactory().constructType(t, bindings);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
