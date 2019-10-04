/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser.std;

import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.ReadableMemory;
import at.o2xfs.memory.databind.type.JavaType;

public class EnumDeserializer extends StdDeserializer<Object> {

	public EnumDeserializer(JavaType valueType) {
		super(valueType);
	}

	@Override
	public Object deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		// TODO Auto-generated method stub
		return null;
	}

}
