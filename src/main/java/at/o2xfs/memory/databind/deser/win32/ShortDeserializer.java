package at.o2xfs.memory.databind.deser.win32;

import at.o2xfs.common.Bits;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.ReadableMemory;

public class ShortDeserializer extends MemoryDeserializer<Integer> {

	public static final ShortDeserializer instance = new ShortDeserializer();

	@Override
	public Integer deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		return Integer.valueOf(Bits.getShort(memory.read(Short.BYTES)));
	}

}
