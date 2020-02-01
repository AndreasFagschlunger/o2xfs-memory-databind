package at.o2xfs.memory.databind.ser.win32;

import java.util.List;

import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;

public class ULongArraySerializer extends MemorySerializer<List<Long>> {

	@Override
	public void serialize(List<Long> value, MemoryGenerator gen, SerializerProvider provider) {
		for (Long each : value) {
			ULongSerializer.instance.serialize(each, gen, provider);
		}
	}
}
