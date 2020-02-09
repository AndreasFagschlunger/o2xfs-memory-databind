/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind;

import java.util.Objects;

import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.introspect.Annotated;
import at.o2xfs.memory.databind.introspect.AnnotatedClass;
import at.o2xfs.memory.databind.introspect.AnnotatedMember;
import at.o2xfs.memory.databind.introspect.ClassIntrospector;
import at.o2xfs.memory.databind.jsontype.TypeSerializer;
import at.o2xfs.memory.databind.ser.SerializerCache;
import at.o2xfs.memory.databind.ser.SerializerFactory;
import at.o2xfs.memory.databind.ser.impl.ReadOnlyClassToSerializerMap;
import at.o2xfs.memory.databind.ser.impl.TypeWrappedSerializer;
import at.o2xfs.memory.databind.type.JavaType;
import at.o2xfs.memory.databind.type.TypeFactory;

public abstract class SerializerProvider extends DatabindContext {

	private final SerializationConfig config;
	private final SerializerCache serializerCache;
	private final ReadOnlyClassToSerializerMap knownSerializers;
	private final SerializerFactory serializerFactory;

	protected ClassIntrospector classIntrospector;

	protected SerializerProvider(SerializationConfig config, SerializerFactory serializerFactory,
			SerializerCache cache) {
		this.config = Objects.requireNonNull(config);
		this.serializerFactory = serializerFactory;
		serializerCache = cache;
		knownSerializers = serializerCache.getReadOnlyLookupMap();
	}

	private MemorySerializer<Object> createAndCachePropertySerializer(Class<?> rawType, JavaType fullType,
			BeanProperty prop) {
		BeanDescription beanDesc = introspectBeanDescription(fullType);
		MemorySerializer<Object> ser = serializerFactory.createSerializer(this, fullType, beanDesc);
		serializerCache.addAndResolveNonTypedSerializer(rawType, fullType, ser, this);
		return ser;

	}

	private MemorySerializer<Object> createAndCachePropertySerializer(JavaType type, BeanProperty prop) {
		BeanDescription beanDesc = introspectBeanDescription(type);
		return serializerFactory.createSerializer(this, type, beanDesc);
	}

	private MemorySerializer<Object> createAndCacheUntypedSerializer(JavaType type) {
		BeanDescription beanDesc = introspectBeanDescription(type);
		MemorySerializer<Object> ser = serializerFactory.createSerializer(this, type, beanDesc);
		serializerCache.addAndResolveNonTypedSerializer(type, ser, this);
		return ser;
	}

	private MemorySerializer<Object> createAndCacheUntypedSerializer(Class<?> rawType, JavaType fullType) {
		BeanDescription beanDesc = introspectBeanDescription(fullType);
		MemorySerializer<Object> ser = serializerFactory.createSerializer(this, fullType, beanDesc);
		serializerCache.addAndResolveNonTypedSerializer(rawType, fullType, ser, this);
		return ser;
	}

	@Override
	protected ClassIntrospector classIntrospector() {
		if (classIntrospector == null) {
			classIntrospector = config.classIntrospectorInstance();
		}
		return classIntrospector;
	}

	public final void defaultSerializeNullValue(MemoryGenerator gen) {
		gen.writeNull();
	}

	@Override
	public final SerializationConfig getConfig() {
		return config;
	}

	@Override
	public final TypeFactory getTypeFactory() {
		return config.getTypeFactory();
	}

	private MemorySerializer<Object> handleRootContextualization(MemorySerializer<Object> ser) {
		if (ser != null) {
			ser.createContextual(this, null);
		}
		return ser;
	}

	public abstract MemorySerializer<?> serializerInstance(Annotated annotated, Object serDef);

	public MemorySerializer<Object> findContentValueSerializer(JavaType valueType, BeanProperty property) {
		MemorySerializer<Object> ser = knownSerializers.untypedValueSerializer(valueType);
		if (ser == null) {
			ser = createAndCachePropertySerializer(valueType, property);
		}
		return handleSecondaryContextualization(ser, property);
	}

	public MemorySerializer<Object> findContentValueSerializer(Class<?> rawType, BeanProperty property) {
		MemorySerializer<Object> ser = knownSerializers.untypedValueSerializer(rawType);
		if (ser == null) {
			JavaType fullType = config.constructType(rawType);
			ser = serializerCache.untypedValueSerializer(fullType);
			if (ser == null) {
				ser = createAndCachePropertySerializer(rawType, fullType, property);
			}
		}
		return handleSecondaryContextualization(ser, property);
	}

	public MemorySerializer<Object> findPrimaryPropertySerializer(JavaType valueType, BeanProperty property) {
		MemorySerializer<Object> result = createAndCachePropertySerializer(valueType, property);
		return handlePrimaryContextualization(result, property);
	}

	public MemorySerializer<Object> findPrimaryPropertySerializer(Class<?> rawType, BeanProperty property) {
		MemorySerializer<Object> ser = knownSerializers.untypedValueSerializer(rawType);
		if (ser == null) {
			JavaType fullType = config.constructType(rawType);
			ser = serializerCache.untypedValueSerializer(fullType);
			if (ser == null) {
				ser = createAndCachePropertySerializer(rawType, fullType, property);
			}
		}
		return handlePrimaryContextualization(ser, property);
	}

	public MemorySerializer<Object> findPrimaryPropertySerializer(Class<?> rawType, JavaType fullType,
			BeanProperty prop) {
		BeanDescription beanDesc = introspectBeanDescription(fullType);
		MemorySerializer<Object> ser = serializerFactory.createSerializer(this, fullType, beanDesc);
		serializerCache.addAndResolveNonTypedSerializer(rawType, fullType, ser, this);
		return ser;
	}

	public TypeSerializer findPropertyTypeSerializer(JavaType baseType, AnnotatedMember accessor) {
		return config.getTypeResolverProvider().findPropertyTypeSerializer(this, accessor, baseType);
	}

	public MemorySerializer<Object> findValueSerializer(Class<?> rawType) {
		MemorySerializer<Object> ser = knownSerializers.untypedValueSerializer(rawType);
		if (ser == null) {
			JavaType fullType = config.constructType(rawType);
			ser = serializerCache.untypedValueSerializer(fullType);
			if (ser == null) {
				ser = createAndCacheUntypedSerializer(rawType, fullType);
			}
		}
		return ser;
	}

	public MemorySerializer<Object> findValueSerializer(JavaType valueType) {
		MemorySerializer<Object> ser = knownSerializers.untypedValueSerializer(valueType);
		if (ser == null) {
			ser = createAndCacheUntypedSerializer(valueType);
		}
		return ser;
	}

	public MemorySerializer<Object> handlePrimaryContextualization(MemorySerializer<?> ser, BeanProperty property) {
		if (ser != null) {
			ser = ser.createContextual(this, property);
		}
		return (MemorySerializer<Object>) ser;
	}

	public MemorySerializer<Object> handleSecondaryContextualization(MemorySerializer<?> ser, BeanProperty property) {
		if (ser != null) {
			ser = ser.createContextual(this, property);
		}
		return (MemorySerializer<Object>) ser;
	}

	@Override
	public BeanDescription introspectBeanDescription(JavaType type) {
		return classIntrospector().introspectForSerialization(type);
	}

	public final AnnotationIntrospector getAnnotationIntrospector() {
		return config.getAnnotationIntrospector();
	}

	public TypeSerializer findTypeSerializer(JavaType baseType) {
		return findTypeSerializer(baseType, introspectClassAnnotations(baseType));
	}

	public TypeSerializer findTypeSerializer(JavaType baseType, AnnotatedClass classAnnotations) {
		return getConfig().getTypeResolverProvider().findTypeSerializer(this, baseType, classAnnotations);
	}

	public MemorySerializer<Object> findTypedValueSerializer(Class<?> rawType, boolean cache) {
		MemorySerializer<Object> ser = knownSerializers.typedValueSerializer(rawType);
		if (ser != null) {
			return ser;
		}
		JavaType fullType = config.constructType(rawType);
		ser = handleRootContextualization(findValueSerializer(fullType));
		TypeSerializer typeSer = findTypeSerializer(fullType);
		if (typeSer != null) {
			typeSer = typeSer.forProperty(this, null);
			ser = new TypeWrappedSerializer(typeSer, ser);
		}
		if (cache) {
			serializerCache.addTypedSerializer(rawType, ser);
		}
		return ser;
	}
}
