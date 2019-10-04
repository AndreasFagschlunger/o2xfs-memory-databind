package at.o2xfs.memory.databind;

import java.io.Closeable;

public interface MemoryGenerator extends Closeable {

	void write(byte[] src);

	void writeNull();

	MemoryGenerator writePointer();

}
