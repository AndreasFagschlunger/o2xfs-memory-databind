/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser.std;

import java.io.IOException;
import java.util.Collection;

import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.type.JavaType;

public class CollectionSerializer extends AsArraySerializerBase<Collection<?>> {

	public CollectionSerializer(JavaType elemType, MemorySerializer<?> elementSerializer) {
		super(Collection.class, elemType, elementSerializer);
	}

	@Override
	public void serialize(Collection<?> value, MemoryGenerator gen, SerializerProvider provider) throws IOException {
	}
}
