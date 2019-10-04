package at.o2xfs.memory.databind;

import java.util.Optional;

import at.o2xfs.common.Bytes;

public final class MemoryNode {

	public static final MemoryNode NULL = new MemoryNode(Optional.empty(), Optional.empty());

	private final Optional<byte[]> value;

	private final Optional<MemoryNode> child;

	private MemoryNode(Optional<byte[]> value, Optional<MemoryNode> child) {
		this.value = value;
		this.child = child;
	}

	public static MemoryNode of(byte[] src) {
		return new MemoryNode(Optional.of(Bytes.copy(src)), Optional.empty());
	}

	public static MemoryNode of(MemoryNode child) {
		return new MemoryNode(Optional.empty(), Optional.of(child));
	}
}
