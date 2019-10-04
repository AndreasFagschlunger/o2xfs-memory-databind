/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser;

import java.util.ArrayList;
import java.util.List;

import at.o2xfs.memory.databind.BeanDescription;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializationConfig;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.cfg.SerializerFactoryConfig;
import at.o2xfs.memory.databind.introspect.AnnotatedMember;
import at.o2xfs.memory.databind.introspect.BeanPropertyDefinition;
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
		MemorySerializer<?> serializer = findSerializerFromAnnotation(prov, accessor);
		if (serializer == null) {
			serializer = findPropertyTypeSerializer(prov.getConfig(), type);
		}
		return new BeanPropertyWriter(propDef, accessor, type, serializer);
	}

	@Override
	public MemorySerializer<Object> createSerializer(SerializerProvider provider, JavaType type) {
		BeanDescription beanDesc = provider.getConfig().introspect(type);
		MemorySerializer<Object> result = createSerializer(provider, type, beanDesc);
		return result;
	}

	private MemorySerializer<Object> createSerializer(SerializerProvider prov, JavaType type,
			BeanDescription beanDesc) {
		MemorySerializer<?> result = findSerializerFromAnnotation(prov, beanDesc.getClassInfo());
		if (result == null) {
			if (type.isContainerType()) {
				result = buildContainerSerializer(type);
			} else if (type.isReferenceType()) {
				result = findReferenceSerializer(prov, (ReferenceType) type);
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

	protected List<BeanPropertyWriter> findBeanProperties(SerializerProvider prov, BeanDescription beanDesc) {
		List<BeanPropertyDefinition> properties = beanDesc.findProperties();
		List<BeanPropertyWriter> result = new ArrayList<>(properties.size());
		for (BeanPropertyDefinition each : properties) {
			result.add(constructWriter(prov, each, each.getAccessor()));
		}
		return result;
	}

	private MemorySerializer<Object> findPropertyTypeSerializer(SerializationConfig config, JavaType baseType) {
		return createTypeSerializer(config, baseType);
	}

	@Override
	public Iterable<Serializers> customSerializers() {
		return factoryConfig.serializers();
	}

	public MemorySerializer<Object> findBeanSerializer(SerializerProvider prov, JavaType type,
			BeanDescription beanDesc) {
		return constructBeanSerializer(prov, beanDesc);
	}

	public MemorySerializer<?> findReferenceSerializer(SerializerProvider prov, ReferenceType refType) {
		MemorySerializer<?> result = null;
		MemorySerializer<?> contentTypeSerializer = createTypeSerializer(prov.getConfig(), refType.getContentType());
		for (Serializers serializers : customSerializers()) {
			result = serializers.findReferenceSerializer(prov.getConfig(), refType, contentTypeSerializer);
		}
		return result;
	}

	@Override
	public SerializerFactory withConfig(SerializerFactoryConfig config) {
		return new BeanSerializerFactory(config);
	}
}
