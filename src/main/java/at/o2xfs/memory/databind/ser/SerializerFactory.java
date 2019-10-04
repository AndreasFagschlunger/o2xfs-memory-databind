/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser;

import at.o2xfs.memory.databind.SerializationConfig;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.type.JavaType;

public abstract class SerializerFactory {

	public abstract MemorySerializer<Object> createSerializer(SerializerProvider provider, JavaType type);

	public abstract MemorySerializer<Object> createTypeSerializer(SerializationConfig config, JavaType baseType);

	public abstract SerializerFactory withAdditionalSerializers(Serializers s);
}
