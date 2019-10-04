package at.o2xfs.memory.databind.impl;

import java.util.HashMap;
import java.util.Map;

import at.o2xfs.common.Bits;

public class ByteArrayMemorySystem {

	private final Map<Address, ByteArrayMemory> allocatedMemory;

	private int nextAddress = 1;

	public ByteArrayMemorySystem() {
		allocatedMemory = new HashMap<>();
	}

	ByteArrayMemory allocate(byte[] bytes) {
		Address address = Address.build(Bits.toByteArray(nextAddress++));
		ByteArrayMemory result = new ByteArrayMemory(this, address, bytes);
		allocatedMemory.put(address, result);
		return result;
	}

	ByteArrayMemory dereference(Address address) {
		return allocatedMemory.get(address);
	}

	public ByteArrayMemoryGenerator createGenerator() {
		return new ByteArrayMemoryGenerator(this);
	}
}
