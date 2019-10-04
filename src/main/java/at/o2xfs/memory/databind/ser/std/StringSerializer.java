/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser.std;

import java.nio.charset.StandardCharsets;

import at.o2xfs.memory.databind.MemoryGenerator;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;

public class StringSerializer extends MemorySerializer<String> {

	private static final byte[] NUL = new byte[1];

	public static final StringSerializer instance = new StringSerializer();

	@Override
	public void serialize(String value, MemoryGenerator gen, SerializerProvider provider) {
		gen.write(value.getBytes(StandardCharsets.US_ASCII));
		gen.write(NUL);
	}
}
