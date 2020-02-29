package at.o2xfs.memory.databind.deser.win32;

import java.nio.charset.StandardCharsets;

import at.o2xfs.memory.databind.BeanProperty;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.ReadableMemory;
import at.o2xfs.memory.databind.annotation.win32.CharArray;
import at.o2xfs.memory.databind.deser.std.StdDeserializer;

public class CharArrayDeserializer extends StdDeserializer<String> {

	private final BeanProperty property;

	public CharArrayDeserializer() {
		this(null);
	}

	public CharArrayDeserializer(BeanProperty property) {
		super(String.class);
		this.property = property;
	}

	@Override
	public MemoryDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
		return new CharArrayDeserializer(property);
	}

	@Override
	public String deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		CharArray charArray = property.getMember().getAnnotation(CharArray.class);
		byte[] bytes = memory.read(charArray.length());
		int length = 0;
		for (; length < bytes.length && bytes[length] != 0; length++) {
		}
		return new String(bytes, 0, length, StandardCharsets.US_ASCII);
	}

}
