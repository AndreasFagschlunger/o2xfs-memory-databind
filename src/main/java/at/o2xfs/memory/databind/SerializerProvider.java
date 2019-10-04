/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind;

import java.util.Objects;

import at.o2xfs.memory.databind.introspect.Annotated;
import at.o2xfs.memory.databind.ser.ContextualSerializer;
import at.o2xfs.memory.databind.ser.SerializerCache;
import at.o2xfs.memory.databind.ser.SerializerFactory;
import at.o2xfs.memory.databind.type.JavaType;

public abstract class SerializerProvider {

	private final SerializationConfig config;
	private final SerializerCache serializerCache;
	private final SerializerFactory serializerFactory;

	public SerializerProvider() {
		config = null;
		serializerCache = new SerializerCache();
		serializerFactory = null;
	}

	protected SerializerProvider(SerializerProvider src, SerializationConfig config,
			SerializerFactory serializerFactory) {
		serializerCache = src.serializerCache;
		this.config = Objects.requireNonNull(config);
		this.serializerFactory = serializerFactory;
	}

	public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
		if (baseType.getRawClass() == subclass) {
			return baseType;
		}
		return getConfig().constructSpecializedType(baseType, subclass);
	}

	public SerializationConfig getConfig() {
		return config;
	}

	public MemorySerializer<?> handlePrimaryContextualization(MemorySerializer<?> ser, BeanProperty property) {
		MemorySerializer<?> result = ser;
		if (result instanceof ContextualSerializer) {
			result = ((ContextualSerializer) result).createContextual(this, property);
		}
		return result;
	}

	public abstract MemorySerializer<?> serializerInstance(Annotated annotated, Object serDef);

	public void serializeValue(MemoryGenerator gen, Object value) {
		MemorySerializer<Object> serializer = findValueSerializer(value.getClass(), null);
		serializer.serialize(value, gen, this);
	}

	public MemorySerializer<Object> findPrimaryPropertySerializer(JavaType valueType, BeanProperty property) {
		MemorySerializer<Object> result = createAndCacheUntypedSerializer(valueType);
		return (MemorySerializer<Object>) handlePrimaryContextualization(result, property);
	}

	public MemorySerializer<Object> findValueSerializer(Class<?> valueType, BeanProperty property) {
		MemorySerializer<Object> result = createAndCacheUntypedSerializer(valueType);
		return result;
	}

	public MemorySerializer<Object> findValueSerializer(JavaType valueType, BeanProperty property) {
		MemorySerializer<Object> result = createAndCacheUntypedSerializer(valueType);
		return result;
	}

	private MemorySerializer<Object> createAndCacheUntypedSerializer(Class<?> rawType) {
		JavaType fullType = config.constructType(rawType);
		MemorySerializer<Object> result = createUntypedSerializer(fullType);
		serializerCache.addAndResolveNonTypedSerializer(rawType, fullType, result, this);
		return result;
	}

	private MemorySerializer<Object> createAndCacheUntypedSerializer(JavaType type) {
		MemorySerializer<Object> result = createUntypedSerializer(type);
		return result;
	}

	private MemorySerializer<Object> createUntypedSerializer(JavaType type) {
		return serializerFactory.createSerializer(this, type);
	}

	public final AnnotationIntrospector getAnnotationIntrospector() {
		return config.getAnnotationIntrospector();
	}
}
