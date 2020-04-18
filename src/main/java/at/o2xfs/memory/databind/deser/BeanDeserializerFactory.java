/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser;

import at.o2xfs.memory.databind.BeanDescription;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.cfg.DeserializerFactoryConfig;
import at.o2xfs.memory.databind.deser.impl.MethodProperty;
import at.o2xfs.memory.databind.introspect.AnnotatedMember;
import at.o2xfs.memory.databind.introspect.AnnotatedMethod;
import at.o2xfs.memory.databind.introspect.BeanPropertyDefinition;
import at.o2xfs.memory.databind.type.JavaType;

public class BeanDeserializerFactory extends BasicDeserializerFactory {

	private static final String DEFAULT_BUILD_METHOD = "build";

	public static final BeanDeserializerFactory instance = new BeanDeserializerFactory(new DeserializerFactoryConfig());

	public BeanDeserializerFactory(DeserializerFactoryConfig factoryConfig) {
		super(factoryConfig);
	}

	private void addBeanProps(DeserializationContext ctxt, BeanDescription beanDesc, BeanDescription builderDesc,
			BeanDeserializerBuilder builder) {
		for (BeanPropertyDefinition beanPropDef : beanDesc.findProperties()) {
			SettableBeanProperty prop = null;
			BeanPropertyDefinition propDef = null;
			for (BeanPropertyDefinition each : builderDesc.findProperties()) {
				if (beanPropDef.getName().equals(each.getName())) {
					propDef = each;
					break;
				}
			}
			if (propDef == null) {
				continue;
			}
			if (propDef.hasSetter()) {
				JavaType propertyType = propDef.getSetter().getParameterType(0);
				prop = constructSettableProperty(ctxt, beanPropDef, propDef, propertyType);
			}
			builder.addProperty(prop);
		}
	}

	private BeanDeserializerBuilder constructBeanDeserializerBuilder(DeserializationContext ctxt,
			BeanDescription beanDesc) {
		return new BeanDeserializerBuilder(ctxt, beanDesc);
	}

	private SettableBeanProperty constructSettableProperty(DeserializationContext ctxt,
			BeanPropertyDefinition beanPropDef, BeanPropertyDefinition propDef, JavaType propType) {
		AnnotatedMember mutator = propDef.getNonConstructorMutator();
		if (beanPropDef != null) {
			mutator = mutator.withAnnotations(beanPropDef.getAccessor().getAnnotations());
		}
		JavaType type = resolveMemberAndTypeAnnotations(ctxt, mutator, propType);
		SettableBeanProperty result = new MethodProperty(propDef, type, (AnnotatedMethod) mutator);
		MemoryDeserializer<?> deser = findDeserializerFromAnnotation(ctxt, mutator);
		if (deser != null) {
			deser = ctxt.handlePrimaryContextualization(deser, result, type);
			result = result.withValueDeserializer(deser);
		}
		return result;
	}

	private MemoryDeserializer<Object> buildBuilderBasedDeserializer(DeserializationContext ctxt, JavaType valueType,
			BeanDescription beanDesc, BeanDescription builderDesc) {
		ValueInstantiator valueInstantiator = findValueInstantiator(ctxt, builderDesc);
		BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(ctxt, builderDesc);
		builder.setValueInstantiator(valueInstantiator);
		addBeanProps(ctxt, beanDesc, builderDesc, builder);

		AnnotatedMethod buildMethod = builderDesc.findMethod(DEFAULT_BUILD_METHOD, new Class<?>[0]);
		builder.setPOJOBuilder(buildMethod);
		return (MemoryDeserializer<Object>) builder.buildBuilderBased();
	}

	@Override
	public MemoryDeserializer<Object> createBeanDeserializer(DeserializationContext ctxt, JavaType type,
			BeanDescription beanDesc) {
		MemoryDeserializer<Object> result = (MemoryDeserializer<Object>) findStdDeserializer(ctxt, type, beanDesc);
		return result;
	}

	public MemoryDeserializer<?> findStdDeserializer(DeserializationContext ctxt, JavaType type,
			BeanDescription beanDesc) {
		MemoryDeserializer<?> result = findDefaultDeserializer(ctxt, type, beanDesc);
		return result;
	}

	@Override
	public MemoryDeserializer<Object> createBuilderBasedDeserializer(DeserializationContext ctxt, JavaType valueType,
			BeanDescription beanDesc, Class<?> builderClass) {
		JavaType builderType = ctxt.constructType(builderClass);
		BeanDescription builderDesc = ctxt.introspectBeanDescriptionForBuilder(builderType);
		return buildBuilderBasedDeserializer(ctxt, valueType, beanDesc, builderDesc);
	}

	@Override
	public DeserializerFactory withConfig(DeserializerFactoryConfig config) {
		return new BeanDeserializerFactory(config);
	}
}
