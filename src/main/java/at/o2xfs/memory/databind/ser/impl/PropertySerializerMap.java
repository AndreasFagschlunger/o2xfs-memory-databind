/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser.impl;

import at.o2xfs.memory.databind.BeanProperty;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.type.JavaType;

public abstract class PropertySerializerMap {

	public final SerializerAndMapResult findAndAddPrimarySerializer(JavaType type, SerializerProvider provider,
			BeanProperty property) {
		MemorySerializer<Object> serializer = provider.findPrimaryPropertySerializer(type, property);
		return new SerializerAndMapResult(serializer, newWith(type.getRawClass(), serializer));
	}

	public final SerializerAndMapResult findAndAddSecondarySerializer(Class<?> type, SerializerProvider provider,
			BeanProperty property) {
		MemorySerializer<Object> serializer = provider.findContentValueSerializer(type, property);
		return new SerializerAndMapResult(serializer, newWith(type, serializer));
	}

	public final SerializerAndMapResult findAndAddSecondarySerializer(JavaType type, SerializerProvider provider,
			BeanProperty property) {
		MemorySerializer<Object> serializer = provider.findContentValueSerializer(type, property);
		return new SerializerAndMapResult(serializer, newWith(type.getRawClass(), serializer));
	}

	public abstract PropertySerializerMap newWith(Class<?> type, MemorySerializer<Object> serializer);

	public abstract MemorySerializer<Object> serializerFor(Class<?> cls);

	public static PropertySerializerMap emptyForProperties() {
		return Empty.FOR_PROPERTIES;
	}

	private static final class Empty extends PropertySerializerMap {

		public final static Empty FOR_PROPERTIES = new Empty();

		@Override
		public PropertySerializerMap newWith(Class<?> type, MemorySerializer<Object> serializer) {
			return null;
		}

		@Override
		public MemorySerializer<Object> serializerFor(Class<?> cls) {
			return null;
		}
	}

	public static final class SerializerAndMapResult {

		public final MemorySerializer<Object> serializer;
		public final PropertySerializerMap map;

		public SerializerAndMapResult(MemorySerializer<Object> serializer, PropertySerializerMap map) {
			this.serializer = serializer;
			this.map = map;
		}
	}
}
