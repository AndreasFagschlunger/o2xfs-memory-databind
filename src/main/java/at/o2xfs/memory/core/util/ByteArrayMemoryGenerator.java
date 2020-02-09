package at.o2xfs.memory.core.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import at.o2xfs.common.Bits;
import at.o2xfs.common.ByteArrayBuffer;

public class ByteArrayMemoryGenerator extends BaseMemoryGenerator {

	private static final byte[] NULL = new byte[4];

	private final ByteArrayMemorySystem memorySystem;
	private final List<ByteArrayBuffer> buffers;
	private Address address;

	ByteArrayMemoryGenerator(ByteArrayMemorySystem memorySystem) {
		this.memorySystem = memorySystem;
		buffers = new ArrayList<>();
		buffers.add(new ByteArrayBuffer(128));
	}

	private ByteArrayBuffer getBuffer() {
		return buffers.get(buffers.size() - 1);
	}

	@Override
	public void close() throws IOException {
		if (isClosed()) {
			return;
		} else if (buffers.size() != 1) {
			throw new IOException("");
		}
		ByteArrayBuffer buffer = buffers.remove(buffers.size() - 1);
		address = memorySystem.allocate(buffer.toByteArray()).getAddress();
	}

	@Override
	public boolean isClosed() {
		return buffers.isEmpty();
	}

	@Override
	public void write(byte[] src) {
		getBuffer().append(src);
	}

	@Override
	public void writeNull() {
		write(NULL);
	}

	@Override
	public void writeUnsignedLong(long value) {
		getBuffer().append(Bits.toByteArray((int) value));
	}

	@Override
	public void writeUnsignedShort(int value) {
		getBuffer().append(Bits.toByteArray((short) value));
	}

	@Override
	public void startPointer() {
		buffers.add(new ByteArrayBuffer(128));
	}

	@Override
	public void endPointer() {
		ByteArrayBuffer buffer = buffers.remove(buffers.size() - 1);
		if (buffer.length() == 0) {
			writeNull();
		} else {
			ByteArrayMemory memory = memorySystem.allocate(buffer.toByteArray());
			write(memory.getAddress().getValue());
		}
	}

	public Address allocate() throws IOException {
		close();
		return address;
	}
}
