package at.o2xfs.memory.databind.win32;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import at.o2xfs.memory.databind.BaseMemoryMapperTest;

class SystemtimeTest extends BaseMemoryMapperTest {

	@Test
	final void testNow() throws IOException {
		Systemtime expected = Systemtime.valueOf(LocalDateTime.now());
		shouldBeEqualAfterDeserialization(expected);
	}

	@Test
	final void testEmpty() throws IOException {
		shouldBeEqualAfterDeserialization(Systemtime.empty());
	}

	@Test
	void testEmptyToString() {
		String expected = "0000-00-00T00:00:00.0";
		String actual = Systemtime.empty().toString();
		assertEquals(expected, actual);
	}
}
