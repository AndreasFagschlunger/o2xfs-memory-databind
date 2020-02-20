package at.o2xfs.memory.databind.ser.win32;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.ser.std.StdSerializer;

public class UnicodeSerializer extends StdSerializer<String> {

	protected static final byte[] NUL = new byte[2];

	public UnicodeSerializer() {
		super(String.class);
	}

	@Override
	public void serialize(String value, MemoryGenerator gen, SerializerProvider provider) throws IOException {
		gen.startPointer();
		gen.write(value.getBytes(StandardCharsets.UTF_16));
		gen.write(NUL);
		gen.endPointer();
	}

}
