package at.o2xfs.memory.databind.deser.win32;

import at.o2xfs.common.Bits;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.ReadableMemory;

public class UShortDeserializer extends MemoryDeserializer<Integer> {

	public static final UShortDeserializer instance = new UShortDeserializer();

	@Override
	public Integer deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		return Integer.valueOf(Bits.getShort(memory.read(Short.BYTES)) & 0xffff);
	}

}
