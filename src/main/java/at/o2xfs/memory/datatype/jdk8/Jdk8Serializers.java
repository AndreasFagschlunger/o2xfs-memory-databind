/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.datatype.jdk8;

import java.util.Optional;

import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializationConfig;
import at.o2xfs.memory.databind.ser.Serializers;
import at.o2xfs.memory.databind.type.ReferenceType;

public class Jdk8Serializers implements Serializers {

	@Override
	public MemorySerializer<?> findReferenceSerializer(SerializationConfig config, ReferenceType refType,
			MemorySerializer<?> contentTypeSerializer) {
		MemorySerializer<?> result = null;
		if (refType.getRawClass().isAssignableFrom(Optional.class)) {
			result = new OptionalSerializer(refType);
		}
		return result;
	}

}
