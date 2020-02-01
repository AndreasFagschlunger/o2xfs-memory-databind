package at.o2xfs.memory.core.util;

import java.nio.charset.StandardCharsets;

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
}
