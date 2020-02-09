package at.o2xfs.memory.databind.ser.win32;

import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.ser.std.ArraySerializerBase;

public class UShortArraySerializer extends ArraySerializerBase<int[]> {

	public UShortArraySerializer() {
		super(int[].class);
	}

	@Override
	public void serialize(int[] value, MemoryGenerator gen, SerializerProvider provider) {
		gen.writeUnsignedShort(value.length);
		if (value.length == 0) {
			gen.writeNull();
		} else {
			gen.startPointer();
			for (int i : value) {
				gen.writeUnsignedShort(i);
			}
			gen.endPointer();
		}
	}
}
