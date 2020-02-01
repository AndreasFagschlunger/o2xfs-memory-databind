package at.o2xfs.memory.databind;

public interface ReadableMemory {

	byte[] read(int length);

	ReadableMemory nextReference();

	String nextString();

	long nextUnsignedLong();

	int nextUnsignedShort();
}
