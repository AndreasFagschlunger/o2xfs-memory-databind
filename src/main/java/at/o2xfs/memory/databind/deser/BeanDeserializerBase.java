/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser;

import java.util.Objects;

import at.o2xfs.memory.databind.BeanDescription;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.deser.impl.BeanPropertyMap;
import at.o2xfs.memory.databind.deser.std.StdDeserializer;
import at.o2xfs.memory.databind.type.JavaType;

public abstract class BeanDeserializerBase extends StdDeserializer<Object> implements ResolvableDeserializer {

	protected final JavaType beanType;
	protected final ValueInstantiator valueInstantiator;
	protected final BeanPropertyMap beanProperties;

	public BeanDeserializerBase(BeanDeserializerBuilder builder, BeanDescription beanDesc,
			BeanPropertyMap beanProperties) {
		super(beanDesc.getType());
		this.beanType = Objects.requireNonNull(beanDesc.getType());
		this.valueInstantiator = Objects.requireNonNull(builder.getValueInstantiator());
		this.beanProperties = Objects.requireNonNull(beanProperties);
	}

	private void replaceProperty(SettableBeanProperty origProp, SettableBeanProperty newProp) {
		beanProperties.replace(origProp, newProp);
	}

	@Override
	public void resolve(DeserializationContext ctxt) {
		for (SettableBeanProperty prop : beanProperties) {
			if (!prop.hasValueDeserializer()) {
				MemoryDeserializer<?> deser = ctxt.findNonContextualValueDeserializer(prop.getType());
				if (deser != null) {
					SettableBeanProperty newProp = prop.withValueDeserializer(deser);
					replaceProperty(prop, newProp);
				}
			}
		}
		for (SettableBeanProperty origProp : beanProperties) {
			SettableBeanProperty prop = origProp;
			MemoryDeserializer<?> deser = prop.getValueDeserializer();
			deser = ctxt.handlePrimaryContextualization(deser, prop, prop.getType());
			prop = prop.withValueDeserializer(deser);
			if (prop != origProp) {
				replaceProperty(origProp, prop);
			}
		}
	}
}
