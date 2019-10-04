/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser.std;

import java.util.Map;

import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.ReadableMemory;
import at.o2xfs.memory.databind.type.JavaType;

public class MapDeserializer extends MemoryDeserializer<Map<Object, Object>> {

	private final JavaType mapType;

	public MapDeserializer(JavaType mapType) {
		this.mapType = mapType;
	}

	@Override
	public Map<Object, Object> deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		return null;
	}
}
