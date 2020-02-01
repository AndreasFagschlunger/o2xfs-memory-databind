package at.o2xfs.memory.databind.ser.win32;

import at.o2xfs.memory.databind.BeanProperty;
import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.ser.std.ArraySerializerBase;

public class UShortArraySerializer extends ArraySerializerBase<int[]> {

	public UShortArraySerializer() {
		super(int[].class);
	}

	@Override
	public void serialize(int[] value, MemoryGenerator gen, SerializerProvider provider) {
		System.out.println("VALUE=" + value);
	}

	@Override
	public MemorySerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
		System.out.println("property=" + property);
		return this;
	}

}
