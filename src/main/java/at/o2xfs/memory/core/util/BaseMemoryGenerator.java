package at.o2xfs.memory.core.util;

import java.nio.charset.StandardCharsets;

import at.o2xfs.common.Bits;
import at.o2xfs.memory.core.MemoryGenerator;

public abstract class BaseMemoryGenerator implements MemoryGenerator {

	private static final byte[] NUL = new byte[1];

	@Override
	public void writeString(String value) {
		startPointer();
		write(value.getBytes(StandardCharsets.US_ASCII));
		write(NUL);
		endPointer();
	}

	@Override
	public void writeUnsignedLong(long value) {
		write(Bits.toByteArray((int) value));
	}

	@Override
	public void writeUnsignedShort(int value) {
		write(Bits.toByteArray((short) value));
	}
}
