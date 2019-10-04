package at.o2xfs.memory.datatype.jdk8;

import java.util.Optional;

import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.ReadableMemory;

public class OptionalDeserializer extends MemoryDeserializer<Optional<?>> {

	@Override
	public Optional<?> deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		return Optional.empty();
	}

}
