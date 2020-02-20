/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser.std;

import java.io.IOException;

import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.SerializerProvider;

public class ByteArraySerializer extends StdSerializer<byte[]> {

	public ByteArraySerializer() {
		super(byte[].class);
	}

	@Override
	public void serialize(byte[] value, MemoryGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeUnsignedLong(value.length);
		if (value.length == 0) {
			gen.writeNull();
		} else {
			gen.startPointer();
			gen.write(value);
			gen.endPointer();
		}
	}
}
