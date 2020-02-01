package at.o2xfs.memory.core.util;

import java.util.HashMap;
import java.util.Map;

import at.o2xfs.common.Bits;
import at.o2xfs.common.Hex;

public class ByteArrayMemorySystem {

	private final Map<Address, ByteArrayMemory> allocatedMemory;

	private int nextAddress = 1;

	public ByteArrayMemorySystem() {
		allocatedMemory = new HashMap<>();
	}

	public ByteArrayMemory allocate(byte[] bytes) {
		Address address = Address.build(Bits.toByteArray(nextAddress++));
		System.out.println("<< " + address.toString() + " " + Hex.encode(bytes));
		ByteArrayMemory result = new ByteArrayMemory(this, address, bytes);
		allocatedMemory.put(address, result);
		return result;
	}

	public ByteArrayMemory dereference(Address address) {
		return allocatedMemory.get(address);
	}

	public ByteArrayMemoryGenerator createGenerator() {
		return new ByteArrayMemoryGenerator(this);
	}
}
