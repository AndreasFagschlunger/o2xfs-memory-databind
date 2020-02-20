package at.o2xfs.memory.databind.deser.win32;

import java.util.Optional;

import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.ReadableMemory;
import at.o2xfs.memory.databind.deser.std.StdDeserializer;

public class OptionalUnicodeDeserializer extends StdDeserializer<Optional<String>> {

	public OptionalUnicodeDeserializer() {
		super(Optional.class);
	}

	@Override
	public Optional<String> deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		String value = new UnicodeDeserializer().deserialize(memory, ctxt);
		if (value == null) {
			return Optional.empty();
		}
		return Optional.of(value);
	}
}
