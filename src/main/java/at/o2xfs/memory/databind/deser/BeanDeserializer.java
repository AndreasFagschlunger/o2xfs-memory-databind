/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser;

import at.o2xfs.memory.databind.BeanDescription;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.ReadableMemory;
import at.o2xfs.memory.databind.deser.impl.BeanPropertyMap;

public class BeanDeserializer extends BeanDeserializerBase {

	public BeanDeserializer(BeanDeserializerBuilder builder, BeanDescription beanDesc, BeanPropertyMap beanProperties) {
		super(builder, beanDesc, beanProperties);
	}

	private final Object vanillaDeserialize(DeserializationContext ctxt) {
		Object result = valueInstantiator.createUsingDefault(ctxt);
		return result;
	}

	@Override
	public Object deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		return vanillaDeserialize(ctxt);
	}
}
