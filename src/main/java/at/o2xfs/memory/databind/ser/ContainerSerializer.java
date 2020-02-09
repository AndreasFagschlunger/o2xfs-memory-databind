/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser;

import at.o2xfs.memory.databind.BeanProperty;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.ser.impl.PropertySerializerMap;
import at.o2xfs.memory.databind.ser.std.StdSerializer;
import at.o2xfs.memory.databind.type.JavaType;

public abstract class ContainerSerializer<T> extends StdSerializer<T> {

	protected final BeanProperty property;

	protected PropertySerializerMap dynamicValueSerializers;

	public ContainerSerializer(Class<?> t) {
		super(t);
		property = null;
		dynamicValueSerializers = PropertySerializerMap.emptyForProperties();
	}

	protected MemorySerializer<Object> findAndAddDynamic(SerializerProvider ctxt, Class<?> type) {
		PropertySerializerMap map = dynamicValueSerializers;
		PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, ctxt, property);
		if (map != result.map) {
			dynamicValueSerializers = result.map;
		}
		return result.serializer;
	}

	protected MemorySerializer<Object> findAndAddDynamic(SerializerProvider ctxt, JavaType type) {
		PropertySerializerMap map = dynamicValueSerializers;
		PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, ctxt, property);
		if (map != result.map) {
			dynamicValueSerializers = result.map;
		}
		return result.serializer;
	}

}
