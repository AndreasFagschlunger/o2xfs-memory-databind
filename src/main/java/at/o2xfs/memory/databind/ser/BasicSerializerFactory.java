/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.RandomAccess;

import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializationConfig;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.cfg.SerializerFactoryConfig;
import at.o2xfs.memory.databind.introspect.Annotated;
import at.o2xfs.memory.databind.ser.std.BooleanSerializer;
import at.o2xfs.memory.databind.ser.std.EnumSerializer;
import at.o2xfs.memory.databind.ser.std.EnumSetSerializer;
import at.o2xfs.memory.databind.ser.std.MapSerializer;
import at.o2xfs.memory.databind.ser.std.NumberSerializer;
import at.o2xfs.memory.databind.ser.std.NumberSerializers;
import at.o2xfs.memory.databind.ser.std.StringSerializer;
import at.o2xfs.memory.databind.type.CollectionType;
import at.o2xfs.memory.databind.type.JavaType;

public abstract class BasicSerializerFactory extends SerializerFactory {

	protected static final Map<String, MemorySerializer<?>> concrete;

	static {
		concrete = new HashMap<>();
		concrete.put(String.class.getName(), new StringSerializer());
		NumberSerializers.addAll(concrete);
		concrete.put(Boolean.TYPE.getName(), new BooleanSerializer());
		concrete.put(Boolean.class.getName(), new BooleanSerializer());
	}

	protected final SerializerFactoryConfig factoryConfig;

	protected BasicSerializerFactory(SerializerFactoryConfig factoryConfig) {
		this.factoryConfig = factoryConfig != null ? factoryConfig
				: new SerializerFactoryConfig(Collections.emptySet());
	}

	private MemorySerializer<?> buildCollectionSerializer(CollectionType type) {
		MemorySerializer<?> result = null;
		Class<?> raw = type.getRawClass();
		if (EnumSet.class.isAssignableFrom(raw)) {
			result = new EnumSetSerializer();
		} else {
			if (isIndexedList(raw)) {
				result = new IndexedListSerializer(type.getContentType());
			}
		}
		return result;
	}

	protected MemorySerializer<?> buildContainerSerializer(JavaType type) {
		MemorySerializer<?> result = null;
		Class<?> raw = type.getRawClass();
		if (type.isMapLikeType()) {
			if (Map.class.isAssignableFrom(raw)) {
				return buildMapSerializer(type);
			}
		}
		if (Collection.class.isAssignableFrom(raw)) {
			result = buildCollectionSerializer((CollectionType) type);
		}
		return result;
	}

	private MemorySerializer<?> buildEnumSerializer(JavaType type) {
		return EnumSerializer.build();
	}

	private MemorySerializer<?> buildMapSerializer(JavaType type) {
		return MapSerializer.build(type);
	}

	private boolean isIndexedList(Class<?> cls) {
		return RandomAccess.class.isAssignableFrom(cls);
	}

	protected MemorySerializer<?> findSerializerByLookup(JavaType type) {
		Class<?> raw = type.getRawClass();
		String clsName = raw.getName();
		return concrete.get(clsName);
	}

	@Override
	public MemorySerializer<Object> createTypeSerializer(SerializationConfig config, JavaType baseType) {
		// TODO Auto-generated method stub
		return null;
	}

	public abstract Iterable<Serializers> customSerializers();

	protected MemorySerializer<?> findSerializerByPrimaryType(JavaType type) {
		MemorySerializer<?> result = null;
		Class<?> raw = type.getRawClass();
		if (Number.class.isAssignableFrom(raw)) {
			return NumberSerializer.instance;
		}
		if (Enum.class.isAssignableFrom(raw)) {
			result = buildEnumSerializer(type);
		}
		return result;
	}

	protected MemorySerializer<?> findSerializerFromAnnotation(SerializerProvider prov, Annotated a) {
		MemorySerializer<Object> result = null;
		Object serDef = prov.getAnnotationIntrospector().findSerializer(a);
		if (serDef != null) {
			result = (MemorySerializer<Object>) prov.serializerInstance(a, serDef);
		}
		return result;
	}

	@Override
	public final SerializerFactory withAdditionalSerializers(Serializers additional) {
		return withConfig(factoryConfig.withAdditionalSerializers(additional));
	}

	public abstract SerializerFactory withConfig(SerializerFactoryConfig config);

}
