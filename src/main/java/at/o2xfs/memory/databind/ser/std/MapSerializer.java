/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser.std;

import java.util.Map;

import at.o2xfs.memory.databind.MemoryGenerator;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.type.JavaType;

public class MapSerializer extends MemorySerializer<Map<?, ?>> {

	private final JavaType keyType;
	private final JavaType valueType;

	public MapSerializer(JavaType keyType, JavaType valueType) {
		this.keyType = keyType;
		this.valueType = valueType;
	}

	@Override
	public void serialize(Map<?, ?> value, MemoryGenerator gen, SerializerProvider provider) {

	}

	public static MapSerializer build(JavaType mapType) {
		return new MapSerializer(mapType.getKeyType(), mapType.getContentType());
	}
}
