package at.o2xfs.memory.databind.deser.win32;

import at.o2xfs.memory.databind.BeanProperty;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.ReadableMemory;
import at.o2xfs.memory.databind.deser.std.StdDeserializer;

public class UShortArrayDeserializer extends StdDeserializer<int[]> {

	private final int length;

	public UShortArrayDeserializer() {
		this(0);
	}

	public UShortArrayDeserializer(int length) {
		super(int[].class);
		this.length = length;
	}

	@Override
	public MemoryDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
		return this;
	}

	@Override
	public int[] deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		int[] result = new int[length];
		for (int i = 0; i < result.length; i++) {
			result[i] = memory.nextUnsignedShort();
		}
		return result;
	}

}
