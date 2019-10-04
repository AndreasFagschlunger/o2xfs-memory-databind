/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser.std;

import java.util.Objects;

import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.deser.ValueInstantiator;
import at.o2xfs.memory.databind.introspect.AnnotatedWithParams;
import at.o2xfs.memory.databind.type.JavaType;

public class StdValueInstantiator extends ValueInstantiator {

	protected final Class<?> valueClass;
	private AnnotatedWithParams defaultCreator;

	public StdValueInstantiator(JavaType valueType) {
		valueClass = Objects.requireNonNull(valueType.getRawClass());
	}

	public void configureFromObjectSettings(AnnotatedWithParams defaultCreator) {
		this.defaultCreator = defaultCreator;
	}

	@Override
	public Object createUsingDefault(DeserializationContext ctxt) {
		try {
			return defaultCreator.call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
