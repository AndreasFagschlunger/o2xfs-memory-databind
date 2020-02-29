package at.o2xfs.memory.databind.ser.win32;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.BeanProperty;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.annotation.win32.CharArray;
import at.o2xfs.memory.databind.ser.std.StdSerializer;

public class CharArraySerializer extends StdSerializer<String> {

	private final BeanProperty property;

	public CharArraySerializer() {
		this(null);
	}

	public CharArraySerializer(BeanProperty property) {
		super(String.class);
		this.property = property;
	}

	@Override
	public MemorySerializer<?> createContextual(SerializerProvider provider, BeanProperty prop) {
		return new CharArraySerializer(prop);
	}

	@Override
	public void serialize(String value, MemoryGenerator gen, SerializerProvider provider) throws IOException {
		CharArray charArray = property.getMember().getAnnotation(CharArray.class);
		byte[] src = value.getBytes(StandardCharsets.US_ASCII);
		byte[] buf = new byte[charArray.length()];
		System.arraycopy(src, 0, buf, 0, Math.min(src.length, buf.length));
		gen.write(buf);
	}
}
