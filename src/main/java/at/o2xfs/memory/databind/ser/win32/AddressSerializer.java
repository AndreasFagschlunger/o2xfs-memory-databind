package at.o2xfs.memory.databind.ser.win32;

import java.io.IOException;

import at.o2xfs.memory.core.Address;
import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.ser.std.StdSerializer;

public class AddressSerializer extends StdSerializer<Address> {

	public AddressSerializer() {
		super(Address.class);
	}

	@Override
	public void serialize(Address value, MemoryGenerator gen, SerializerProvider provider) throws IOException {
		gen.write(value.getValue());
	}
}
