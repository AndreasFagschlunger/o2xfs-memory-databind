/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser.impl;

import java.util.Objects;

import at.o2xfs.memory.databind.BeanDescription;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.deser.ValueInstantiator;
import at.o2xfs.memory.databind.deser.std.StdValueInstantiator;
import at.o2xfs.memory.databind.introspect.AnnotatedWithParams;

public class CreatorCollector {

	protected final static int C_DEFAULT = 0;

	private final BeanDescription beanDesc;
	private final AnnotatedWithParams[] creators;

	public CreatorCollector(BeanDescription beanDesc) {
		this.beanDesc = Objects.requireNonNull(beanDesc);
		creators = new AnnotatedWithParams[9];
	}

	public ValueInstantiator constructValueInstantiator(DeserializationContext ctxt) {
		StdValueInstantiator result = new StdValueInstantiator(beanDesc.getType());
		result.configureFromObjectSettings(creators[C_DEFAULT]);
		return result;
	}

	public void setDefaultCreator(AnnotatedWithParams creator) {
		creators[C_DEFAULT] = creator;
	}
}
