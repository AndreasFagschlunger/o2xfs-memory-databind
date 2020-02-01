/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser;

import java.util.ArrayList;
import java.util.List;

import at.o2xfs.memory.databind.BeanDescription;
import at.o2xfs.memory.databind.BeanProperty;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.cfg.SerializerFactoryConfig;
import at.o2xfs.memory.databind.introspect.AnnotatedMember;
import at.o2xfs.memory.databind.introspect.BeanPropertyDefinition;
import at.o2xfs.memory.databind.jsontype.TypeSerializer;
import at.o2xfs.memory.databind.type.JavaType;
import at.o2xfs.memory.databind.type.ReferenceType;

public class BeanSerializerFactory extends BasicSerializerFactory {

	public static final BeanSerializerFactory instance = new BeanSerializerFactory(null);

	public BeanSerializerFactory(SerializerFactoryConfig factoryConfig) {
		super(factoryConfig);
	}

	private MemorySerializer<Object> constructBeanSerializer(SerializerProvider prov, BeanDescription beanDesc) {
		List<BeanPropertyWriter> properties = findBeanProperties(prov, beanDesc);
		return new BeanSerializer(properties);
	}

	private BeanPropertyWriter constructWriter(SerializerProvider prov, BeanPropertyDefinition propDef,
			AnnotatedMember accessor) {
		JavaType type = accessor.getType();
		BeanProperty property = new BeanProperty.Std(propDef.getFullName(), type, accessor);
		MemorySerializer<?> annotatedSerializer = findSerializerFromAnnotation(prov, accessor);
		if (annotatedSerializer != null) {
			annotatedSerializer = prov.handlePrimaryContextualization(annotatedSerializer, property);
		}
		TypeSerializer typeSer = prov.findPropertyTypeSerializer(type, accessor);
		return new BeanPropertyWriter(propDef, accessor, type, annotatedSerializer, typeSer);
	}

	protected List<BeanPropertyWriter> findBeanProperties(SerializerProvider prov, BeanDescription beanDesc) {
		List<BeanPropertyDefinition> properties = beanDesc.findProperties();
		List<BeanPropertyWriter> result = new ArrayList<>(properties.size());
		for (BeanPropertyDefinition each : properties) {
			result.add(constructWriter(prov, each, each.getAccessor()));
		}
		return result;
	}

	@Override
	public MemorySerializer<Object> createSerializer(SerializerProvider prov, JavaType type, BeanDescription beanDesc) {
		MemorySerializer<?> result = findSerializerFromAnnotation(prov, beanDesc.getClassInfo());
		if (result == null) {
			if (type.isContainerType()) {
				result = buildContainerSerializer(prov, type, beanDesc);
			} else if (type.isReferenceType()) {
				result = findReferenceSerializer(prov, (ReferenceType) type, beanDesc);
			} else {
				result = findSerializerByLookup(type);
				if (result == null) {
					result = findSerializerByPrimaryType(type);
					if (result == null) {
						result = findBeanSerializer(prov, type, beanDesc);
					}
				}
			}
		}
		return (MemorySerializer<Object>) result;
	}

	@Override
	public Iterable<Serializers> customSerializers() {
		return factoryConfig.serializers();
	}

	public MemorySerializer<Object> findBeanSerializer(SerializerProvider prov, JavaType type,
			BeanDescription beanDesc) {
		return constructBeanSerializer(prov, beanDesc);
	}

	@Override
	public SerializerFactory withConfig(SerializerFactoryConfig config) {
		return new BeanSerializerFactory(config);
	}
}
