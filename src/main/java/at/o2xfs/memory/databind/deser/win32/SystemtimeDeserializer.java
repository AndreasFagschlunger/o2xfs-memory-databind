package at.o2xfs.memory.databind.deser.win32;

import java.time.LocalDateTime;

import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.ReadableMemory;
import at.o2xfs.memory.databind.deser.std.StdDeserializer;

public class SystemtimeDeserializer extends StdDeserializer<LocalDateTime> {

	public SystemtimeDeserializer() {
		super(LocalDateTime.class);
	}

	@Override
	public LocalDateTime deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		int year = memory.nextUnsignedShort();
		int month = memory.nextUnsignedShort();
		memory.nextUnsignedShort(); // dayOfWeek
		int dayOfMonth = memory.nextUnsignedShort();
		int hour = memory.nextUnsignedShort();
		int minute = memory.nextUnsignedShort();
		int second = memory.nextUnsignedShort();
		int milliseconds = memory.nextUnsignedShort();
		return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, milliseconds);
	}

}
