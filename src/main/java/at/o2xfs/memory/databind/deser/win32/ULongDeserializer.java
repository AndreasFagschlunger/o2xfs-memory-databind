package at.o2xfs.memory.databind.deser.win32;

import at.o2xfs.common.Bits;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.ReadableMemory;

public class ULongDeserializer extends MemoryDeserializer<Long> {

	private static final long MAX_VALUE = 4294967295L;

	public static final ULongDeserializer instance = new ULongDeserializer();

	@Override
	public Long deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		return Long.valueOf(Bits.getInt(memory.read(Integer.BYTES)) & MAX_VALUE);
	}

}
