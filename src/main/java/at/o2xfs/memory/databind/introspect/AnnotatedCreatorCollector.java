/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import at.o2xfs.memory.databind.introspect.AnnotatedClass.Creators;
import at.o2xfs.memory.databind.type.JavaType;

public class AnnotatedCreatorCollector {

	private final TypeResolutionContext typeContext;

	private AnnotatedConstructor defaultConstructor;

	public AnnotatedCreatorCollector(TypeResolutionContext typeContext) {
		this.typeContext = Objects.requireNonNull(typeContext);
	}

	private AnnotatedConstructor constructDefaultConstructor(Constructor<?> ctor) {
		return new AnnotatedConstructor(typeContext, Collections.emptyMap(), ctor);
	}

	private List<AnnotatedConstructor> findPotentialConstructors(JavaType type) {
		Constructor<?>[] declaredCtors = type.getRawClass().getConstructors();
		Constructor<?> defaultCtor = null;
		for (Constructor<?> each : declaredCtors) {
			if (each.getParameterCount() == 0) {
				defaultCtor = each;
			}
		}
		if (defaultCtor != null) {
			defaultConstructor = constructDefaultConstructor(defaultCtor);
		}
		return null;
	}

	private Creators collect(JavaType type) {
		findPotentialConstructors(type);
		return new Creators(defaultConstructor);
	}

	public static Creators collectCreators(TypeResolutionContext tc, JavaType type) {
		return new AnnotatedCreatorCollector(tc).collect(type);
	}
}
