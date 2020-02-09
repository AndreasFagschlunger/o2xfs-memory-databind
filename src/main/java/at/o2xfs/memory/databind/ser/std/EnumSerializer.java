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

public class EnumSerializer extends MemorySerializer<Enum<?>> {

	private EnumSerializer() {
	}

	@Override
	public void serialize(Enum<?> value, MemoryGenerator gen, SerializerProvider provider) {
		gen.write(Bits.toByteArray(value.ordinal()));
	}

	public static EnumSerializer build() {
		return new EnumSerializer();
	}
}
