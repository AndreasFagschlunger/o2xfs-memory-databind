package at.o2xfs.memory.databind;

import at.o2xfs.memory.core.Address;

public interface ReadableMemory {

	byte[] read(int length);

	Address nextAddress();

	ReadableMemory nextReference();

	String nextString();

	long nextUnsignedLong();

	int nextUnsignedShort();
}
