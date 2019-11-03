package at.o2xfs.memory.databind.impl;

import java.util.Objects;
import java.util.Optional;

import at.o2xfs.common.ByteArrayBuffer;
import at.o2xfs.memory.databind.MemoryGenerator;

public class ByteArrayMemoryGenerator implements MemoryGenerator {

	private final ByteArrayBuffer buffer;
	private final ByteArrayMemorySystem memorySystem;
	private final Optional<ByteArrayMemoryGenerator> parent;
	private ByteArrayMemory memory;

	private ByteArrayMemoryGenerator(ByteArrayMemorySystem memorySystem, Optional<ByteArrayMemoryGenerator> parent) {
		this.memorySystem = Objects.requireNonNull(memorySystem, "memorySystem must not be null");
		this.parent = parent;
		buffer = new ByteArrayBuffer(128);
	}

	ByteArrayMemoryGenerator(ByteArrayMemorySystem memorySystem) {
		this(memorySystem, Optional.empty());
	}

	ByteArrayMemoryGenerator(ByteArrayMemoryGenerator parent) {
		this(parent.memorySystem, Optional.of(parent));
	}

	@Override
	public void close() {
		if (memory == null) {
			memory = memorySystem.allocate(buffer.toByteArray());
			if (parent.isPresent()) {
				parent.get().write(memory.getAddress().getValue());
			}
		}
	}

	public ByteArrayMemory getMemory() {
		close();
		return memory;
	}

	@Override
	public void write(byte[] src) {
		buffer.append(src);
	}

	@Override
	public void writeNull() {
		write(new byte[Integer.BYTES]);
	}

	@Override
	public MemoryGenerator writePointer() {
		return new ByteArrayMemoryGenerator(this);
	}
}
