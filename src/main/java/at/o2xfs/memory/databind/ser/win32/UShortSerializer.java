package at.o2xfs.memory.databind.ser.win32;

import at.o2xfs.common.Bits;
import at.o2xfs.memory.databind.MemoryGenerator;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;

public class UShortSerializer extends MemorySerializer<Number> {

	@Override
	public void serialize(Number value, MemoryGenerator gen, SerializerProvider provider) {
		gen.write(Bits.toByteArray(value.shortValue()));
	}
}
