package at.o2xfs.memory.databind;

public interface ReadableMemory {

	ReadableMemory dereference();

	byte[] read(int length);
}
