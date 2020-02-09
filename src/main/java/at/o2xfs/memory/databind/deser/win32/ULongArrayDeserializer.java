package at.o2xfs.memory.databind.deser.win32;

import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.ReadableMemory;
import at.o2xfs.memory.databind.deser.std.StdDeserializer;

public class ULongArrayDeserializer extends StdDeserializer<long[]> {

	public ULongArrayDeserializer() {
		super(long[].class);
	}

	private long[] deserialize(ReadableMemory memory, int length) {
		long[] result = new long[length];
		for (int i = 0; i < result.length; i++) {
			result[i] = memory.nextUnsignedLong();
		}
		return result;
	}

	@Override
	public long[] deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		int length = memory.nextUnsignedShort();
		return deserialize(memory.nextReference(), length);
	}

}
