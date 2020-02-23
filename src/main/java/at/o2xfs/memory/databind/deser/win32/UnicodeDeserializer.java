package at.o2xfs.memory.databind.deser.win32;

import java.nio.charset.StandardCharsets;

import at.o2xfs.common.ByteArrayBuffer;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.ReadableMemory;
import at.o2xfs.memory.databind.deser.std.StdDeserializer;

public class UnicodeDeserializer extends StdDeserializer<String> {

	public UnicodeDeserializer() {
		super(String.class);
	}

	@Override
	public String deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		ReadableMemory next = memory.nextReference();
		if (next == null) {
			return null;
		}
		ByteArrayBuffer buffer = new ByteArrayBuffer(32);
		do {
			byte[] b = next.read(2);
			buffer.append(b);
		} while (buffer.byteAt(buffer.length() - 2) != 0 || buffer.byteAt(buffer.length() - 1) != 0);
		return new String(buffer.buffer(), 0, buffer.length() - 2, StandardCharsets.UTF_16LE);
	}

}
