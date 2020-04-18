package at.o2xfs.memory.databind;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import at.o2xfs.memory.core.util.ByteArrayMemory;
import at.o2xfs.memory.core.util.ByteArrayMemoryGenerator;
import at.o2xfs.memory.core.util.ByteArrayMemorySystem;

class MemoryMapperTest {

	@Test
	final void test() throws IOException {
		MemoryMapper mapper = new MemoryMapper();
		ByteArrayMemorySystem memorySystem = new ByteArrayMemorySystem();
		ByteArrayMemory memory;
		try (ByteArrayMemoryGenerator gen = memorySystem.createGenerator()) {
			mapper.write(gen, "Hello World!");
			memory = memorySystem.dereference(gen.allocate());
		}
	}

}
