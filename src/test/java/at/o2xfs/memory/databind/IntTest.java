package at.o2xfs.memory.databind;

import java.io.IOException;
import java.util.Random;

import org.junit.jupiter.api.Test;

import at.o2xfs.memory.databind.dummies.IntDummy;

public class IntTest extends BaseMemoryMapperTest {

	@Test
	public void testZero() throws IOException {
		shouldBeEqualAfterDeserialization(new IntDummy.Builder().build());
	}

	@Test
	public void testMin() throws IOException {
		shouldBeEqualAfterDeserialization(new IntDummy.Builder().value(Integer.MIN_VALUE).build());
	}

	@Test
	public void testMax() throws IOException {
		shouldBeEqualAfterDeserialization(new IntDummy.Builder().value(Integer.MAX_VALUE).build());
	}

	@Test
	public void testRandom() throws IOException {
		shouldBeEqualAfterDeserialization(new IntDummy.Builder().value(new Random().nextInt()).build());
	}
}
