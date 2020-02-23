/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser.std;

import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.ReadableMemory;

public class StringDeserializer extends StdDeserializer<String> {

	public static final StringDeserializer instance = new StringDeserializer();

	public StringDeserializer() {
		super(String.class);
	}

	@Override
	public String deserialize(ReadableMemory aMemory, DeserializationContext ctxt) {
		ReadableMemory memory = aMemory.nextReference();
		if (memory == null) {
			return null;
		}
		return memory.nextString();
	}
}
