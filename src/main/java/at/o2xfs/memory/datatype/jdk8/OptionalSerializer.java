/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.datatype.jdk8;

import java.util.Optional;

import at.o2xfs.memory.databind.MemoryGenerator;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;

public class OptionalSerializer extends MemorySerializer<Optional<?>> {

	@Override
	public void serialize(Optional<?> value, MemoryGenerator gen, SerializerProvider provider) {
	}
}
