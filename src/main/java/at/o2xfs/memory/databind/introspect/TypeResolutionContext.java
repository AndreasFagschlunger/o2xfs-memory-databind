/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import java.lang.reflect.Type;
import java.util.Objects;

import at.o2xfs.memory.databind.type.JavaType;
import at.o2xfs.memory.databind.type.TypeBindings;
import at.o2xfs.memory.databind.type.TypeFactory;

public interface TypeResolutionContext {

	public static class Basic implements TypeResolutionContext {

		private final TypeFactory typeFactory;
		private final TypeBindings bindings;

		public Basic(TypeFactory typeFactory, TypeBindings bindings) {
			this.typeFactory = Objects.requireNonNull(typeFactory);
			this.bindings = Objects.requireNonNull(bindings);
		}

		@Override
		public JavaType resolveType(Type t) {
			return typeFactory.constructType(t, bindings);
		}
	}

	public JavaType resolveType(Type t);
}
