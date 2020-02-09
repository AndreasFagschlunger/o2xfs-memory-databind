/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser.std;

import java.util.EnumSet;

import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;

public class EnumSetSerializer extends MemorySerializer<EnumSet<? extends Enum<?>>> {

	@Override
	public void serialize(EnumSet<? extends Enum<?>> value, MemoryGenerator gen, SerializerProvider provider) {
	}
}
