package at.o2xfs.memory.databind.ser.win32;

import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;

public class ULongArraySerializer extends MemorySerializer<long[]> {

	@Override
	public void serialize(long[] value, MemoryGenerator gen, SerializerProvider provider) {
		gen.writeUnsignedShort(value.length);
		if (value.length == 0) {
			gen.writeNull();
		} else {
			gen.startPointer();
			for (long each : value) {
				gen.writeUnsignedLong(each);
			}
			gen.endPointer();
		}
	}
}
