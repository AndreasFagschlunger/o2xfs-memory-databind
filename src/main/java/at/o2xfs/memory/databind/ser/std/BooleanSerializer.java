/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser.std;

import at.o2xfs.common.Bits;
import at.o2xfs.memory.databind.MemoryGenerator;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;

public class BooleanSerializer extends MemorySerializer<Boolean> {

	private static final int FALSE = 0;
	private static final int TRUE = 1;

	@Override
	public void serialize(Boolean value, MemoryGenerator gen, SerializerProvider provider) {
		gen.write(Bits.toByteArray(value.booleanValue() ? TRUE : FALSE));
	}
}
