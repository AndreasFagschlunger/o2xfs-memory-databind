/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser;

import at.o2xfs.memory.databind.BeanDescription;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.type.JavaType;

public abstract class SerializerFactory {

	public abstract MemorySerializer<Object> createSerializer(SerializerProvider ctxt, JavaType baseType,
			BeanDescription beanDesc);

	public abstract SerializerFactory withAdditionalSerializers(Serializers s);
}
