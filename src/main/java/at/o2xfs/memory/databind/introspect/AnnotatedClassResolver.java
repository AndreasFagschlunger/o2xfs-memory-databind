/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import at.o2xfs.memory.databind.cfg.MapperConfig;
import at.o2xfs.memory.databind.type.JavaType;
import at.o2xfs.memory.databind.util.ClassUtil;

public final class AnnotatedClassResolver {

	private final MapperConfig<?> config;
	private final JavaType type;
	private final Class<?> rawType;

	private AnnotatedClassResolver(MapperConfig<?> config, JavaType type) {
		this.config = Objects.requireNonNull(config);
		this.type = Objects.requireNonNull(type);
		this.rawType = type.getRawClass();
	}

	private Map<Class<?>, Annotation> resolveClassAnnotations(List<JavaType> superTypes) {
		Map<Class<?>, Annotation> result = new HashMap<>();
		for (Annotation each : ClassUtil.findClassAnnotations(rawType)) {
			result.put(each.annotationType(), each);
		}
		for (JavaType each : superTypes) {
			Annotation[] annotations = ClassUtil.findClassAnnotations(each.getRawClass());
			for (Annotation a : annotations) {
				result.put(a.annotationType(), a);
			}
		}
		return result;
	}

	private AnnotatedClass resolveFully() {
		List<JavaType> superTypes = ClassUtil.findSuperTypes(type);
		return new AnnotatedClass(config, type, rawType, superTypes, resolveClassAnnotations(superTypes),
				type.getBindings());

	}

	public static AnnotatedClass resolve(MapperConfig<?> config, JavaType forType) {
		return new AnnotatedClassResolver(config, forType).resolveFully();
	}
}
