package at.o2xfs.memory.core;

import java.nio.charset.StandardCharsets;

import at.o2xfs.common.Bits;
import at.o2xfs.common.ByteArrayBuffer;
import at.o2xfs.memory.databind.ReadableMemory;

public abstract class BaseReadableMemory implements ReadableMemory {

	@Override
	public String nextString() {
		ByteArrayBuffer buffer = new ByteArrayBuffer(32);
		do {
			buffer.append(read(1));
		} while (buffer.byteAt(buffer.length() - 1) != 0);
		return new String(buffer.buffer(), 0, buffer.length() - 1, StandardCharsets.US_ASCII);
	}

	@Override
	public long nextUnsignedLong() {
		return Bits.getInt(read(Integer.BYTES)) & 0xffffffff;
	}

	@Override
	public int nextUnsignedShort() {
		return Bits.getShort(read(Short.BYTES)) & 0xffff;
	}
}
