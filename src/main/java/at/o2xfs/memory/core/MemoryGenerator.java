package at.o2xfs.memory.core;

import java.io.Closeable;

public interface MemoryGenerator extends Closeable {

	boolean isClosed();

	void write(byte[] src);

	void writeNull();

	void writeString(String value);

	void writeUnsignedLong(long value);

	void writeUnsignedShort(int value);

	void startPointer();

	void endPointer();

}
