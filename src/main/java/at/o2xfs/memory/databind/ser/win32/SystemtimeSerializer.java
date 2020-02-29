package at.o2xfs.memory.databind.ser.win32;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.ser.std.StdSerializer;

public class SystemtimeSerializer extends StdSerializer<LocalDateTime> {

	public SystemtimeSerializer() {
		super(LocalDateTime.class);
	}

	@Override
	public void serialize(LocalDateTime value, MemoryGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeUnsignedShort(value.getYear());
		gen.writeUnsignedShort(value.getMonthValue());
		gen.writeUnsignedShort(value.getDayOfWeek() == DayOfWeek.SUNDAY ? 0 : value.getDayOfWeek().getValue());
		gen.writeUnsignedShort(value.getDayOfMonth());
		gen.writeUnsignedShort(value.getHour());
		gen.writeUnsignedShort(value.getMinute());
		gen.writeUnsignedShort(value.getSecond());
		gen.writeUnsignedShort(value.get(ChronoField.MILLI_OF_SECOND));
	}
}
