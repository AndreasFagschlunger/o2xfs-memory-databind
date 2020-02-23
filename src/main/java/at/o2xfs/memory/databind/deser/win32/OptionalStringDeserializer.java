package at.o2xfs.memory.databind.deser.win32;

import java.util.Optional;

import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.ReadableMemory;
import at.o2xfs.memory.databind.deser.std.StdDeserializer;

public class OptionalStringDeserializer extends StdDeserializer<Optional<String>> {

	public OptionalStringDeserializer() {
		super(Optional.class);
	}

	@Override
	public Optional<String> deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		Optional<String> result = Optional.empty();
		ReadableMemory next = memory.nextReference();
		if (next != null) {
			result = Optional.of(next.nextString());
		}
		return result;
	}
}
