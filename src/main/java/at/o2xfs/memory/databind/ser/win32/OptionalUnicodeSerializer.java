package at.o2xfs.memory.databind.ser.win32;

import java.io.IOException;
import java.util.Optional;

import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.ser.std.StdSerializer;

public class OptionalUnicodeSerializer extends StdSerializer<Optional<String>> {

	public OptionalUnicodeSerializer() {
		super(Optional.class);
	}

	@Override
	public void serialize(Optional<String> value, MemoryGenerator gen, SerializerProvider provider) throws IOException {
		if (value.isPresent() && !value.get().isEmpty()) {
			new UnicodeSerializer().serialize(value.get(), gen, provider);
		} else {
			provider.defaultSerializeNullValue(gen);
		}
	}
}
