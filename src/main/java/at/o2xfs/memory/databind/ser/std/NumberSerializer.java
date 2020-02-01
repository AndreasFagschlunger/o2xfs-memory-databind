/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser.std;

import at.o2xfs.common.Bits;
import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;

public class NumberSerializer extends MemorySerializer<Number> {

	public static final NumberSerializer instance = new NumberSerializer();

	@Override
	public void serialize(Number value, MemoryGenerator gen, SerializerProvider provider) {
		if (value instanceof Long) {
			gen.write(Bits.toByteArray(value.longValue()));
		} else if (value instanceof Integer) {
			gen.write(Bits.toByteArray(value.intValue()));
		} else if (value instanceof Short) {
			gen.write(Bits.toByteArray(value.shortValue()));
		} else if (value instanceof Byte) {
			gen.write(Bits.toByteArray(value.byteValue()));
		}
	}

}
