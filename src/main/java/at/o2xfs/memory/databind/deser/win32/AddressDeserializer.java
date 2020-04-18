package at.o2xfs.memory.databind.deser.win32;

import at.o2xfs.memory.core.Address;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.ReadableMemory;
import at.o2xfs.memory.databind.deser.std.StdDeserializer;

public class AddressDeserializer extends StdDeserializer<Address> {

	public AddressDeserializer() {
		super(Address.class);
	}

	@Override
	public Address deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		return memory.nextAddress();
	}
}
