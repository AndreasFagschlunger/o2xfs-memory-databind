package at.o2xfs.memory.databind.deser.win32;

import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.ReadableMemory;
import at.o2xfs.memory.databind.deser.std.StdDeserializer;

public class UShortArrayDeserializer extends StdDeserializer<int[]> {

	public UShortArrayDeserializer() {
		super(int[].class);
	}

	@Override
	public int[] deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		int length = memory.nextUnsignedShort();
		return deserialize(memory.nextReference(), length);
	}

	private int[] deserialize(ReadableMemory memory, int length) {
		int[] result = new int[length];
		for (int i = 0; i < result.length; i++) {
			result[i] = memory.nextUnsignedShort();
		}
		return result;
	}
}
