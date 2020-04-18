package at.o2xfs.memory.databind;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import at.o2xfs.memory.core.Address;
import at.o2xfs.memory.core.util.ByteArrayMemoryGenerator;
import at.o2xfs.memory.core.util.ByteArrayMemorySystem;

public class BaseMemoryMapperTest {

	protected MemoryMapper memoryMapper;
	protected ByteArrayMemorySystem memorySystem;

	public BaseMemoryMapperTest() {
		memoryMapper = new MemoryMapper();
		memorySystem = new ByteArrayMemorySystem();
	}

	protected void shouldBeEqualAfterDeserialization(Object expected) throws IOException {
		Address address;
		try (ByteArrayMemoryGenerator gen = memorySystem.createGenerator()) {
			memoryMapper.write(gen, expected);
			address = gen.allocate();
		}
		Object actual = memoryMapper.read(memorySystem.dereference(address), expected.getClass());
		assertEquals(expected, actual);
	}
}
