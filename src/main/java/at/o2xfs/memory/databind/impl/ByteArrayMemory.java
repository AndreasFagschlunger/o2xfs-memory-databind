package at.o2xfs.memory.databind.impl;

import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import at.o2xfs.common.Hex;
import at.o2xfs.memory.databind.ReadableMemory;

public final class ByteArrayMemory implements ReadableMemory {

	private final ByteArrayMemorySystem memorySystem;
	private final Address address;
	private final byte[] value;
	private int offset = 0;

	ByteArrayMemory(ByteArrayMemorySystem memorySystem, Address address, byte[] value) {
		this.memorySystem = Objects.requireNonNull(memorySystem);
		this.address = Objects.requireNonNull(address);
		this.value = Objects.requireNonNull(value);
	}

	public void free() {

	}

	public Address getAddress() {
		return address;
	}

	@Override
	public byte[] read(int length) {
		byte[] result = new byte[length];
		System.arraycopy(value, offset, result, 0, result.length);
		offset += length;
		return result;
	}

	@Override
	public ByteArrayMemory dereference() {
		return memorySystem.dereference(Address.build(read(address.getValue().length)));
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("address", address).append("value", Hex.encode(value)).toString();
	}
}
