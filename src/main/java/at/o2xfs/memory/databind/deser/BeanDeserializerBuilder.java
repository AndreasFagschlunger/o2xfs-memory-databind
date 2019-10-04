/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import at.o2xfs.memory.databind.BeanDescription;
import at.o2xfs.memory.databind.DeserializationConfig;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.deser.impl.BeanPropertyMap;
import at.o2xfs.memory.databind.introspect.AnnotatedMethod;

public class BeanDeserializerBuilder {

	private final DeserializationContext context;
	private final DeserializationConfig config;
	private final BeanDescription beanDesc;
	private final Map<String, SettableBeanProperty> properties = new LinkedHashMap<>();

	private ValueInstantiator valueInstantiator;
	private AnnotatedMethod buildMethod;

	public BeanDeserializerBuilder(DeserializationContext context, BeanDescription beanDesc) {
		this.context = Objects.requireNonNull(context);
		this.config = Objects.requireNonNull(context.getConfig());
		this.beanDesc = Objects.requireNonNull(beanDesc);
	}

	public ValueInstantiator getValueInstantiator() {
		return valueInstantiator;
	}

	public void setPOJOBuilder(AnnotatedMethod buildMethod) {
		this.buildMethod = Objects.requireNonNull(buildMethod);
	}

	public void setValueInstantiator(ValueInstantiator valueInstantiator) {
		this.valueInstantiator = valueInstantiator;
	}

	public MemoryDeserializer<?> buildBuilderBased() {
		BeanPropertyMap propertyMap = BeanPropertyMap.construct(properties.values());
		return new BuilderBasedDeserializer(this, beanDesc, propertyMap);
	}

	public AnnotatedMethod getBuildMethod() {
		return buildMethod;
	}

	public void addProperty(SettableBeanProperty prop) {
		SettableBeanProperty old = properties.put(prop.getName(), prop);
		if (old != null && old != prop) {
			throw new IllegalArgumentException("Duplicate property '" + prop.getName() + "' for " + beanDesc.getType());
		}
	}
}
