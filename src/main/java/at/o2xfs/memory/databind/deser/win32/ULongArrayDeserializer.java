package at.o2xfs.memory.databind.deser.win32;

import at.o2xfs.memory.databind.BeanProperty;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.ReadableMemory;
import at.o2xfs.memory.databind.deser.std.StdDeserializer;

public class ULongArrayDeserializer extends StdDeserializer<long[]> {

	private final int length;

	public ULongArrayDeserializer() {
		this(0);
	}

	public ULongArrayDeserializer(int length) {
		super(long[].class);
		this.length = length;
	}

	@Override
	public MemoryDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
		return new ULongArrayDeserializer(0);
	}

	@Override
	public long[] deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		long[] result = new long[length];
		for (int i = 0; i < result.length; i++) {
			result[i] = memory.nextUnsignedLong();
		}
		return result;
	}

}
