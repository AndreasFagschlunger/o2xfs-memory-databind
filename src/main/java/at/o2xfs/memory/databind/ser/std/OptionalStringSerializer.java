package at.o2xfs.memory.databind.ser.std;

import java.io.IOException;
import java.util.Optional;

import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.SerializerProvider;

public class OptionalStringSerializer extends StdSerializer<Optional<String>> {

	public OptionalStringSerializer() {
		super(Optional.class);
	}

	@Override
	public void serialize(Optional<String> value, MemoryGenerator gen, SerializerProvider provider) throws IOException {
		if (!value.isPresent()) {
			gen.writeNull();
		} else {
			gen.writeString(value.get());
		}
	}
}
