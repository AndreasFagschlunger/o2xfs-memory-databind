package at.o2xfs.memory.databind.ser.win32;

import at.o2xfs.common.Bits;
import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;

public class ULongSerializer extends MemorySerializer<Long> {

	public static final ULongSerializer instance = new ULongSerializer();

	@Override
	public void serialize(Long value, MemoryGenerator gen, SerializerProvider provider) {
		gen.write(Bits.toByteArray(value.intValue()));
	}
}
