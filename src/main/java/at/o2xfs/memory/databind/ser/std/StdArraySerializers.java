/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser.std;

import java.util.HashMap;
import java.util.Map;

import at.o2xfs.memory.databind.MemorySerializer;

public class StdArraySerializers {

	protected static Map<String, MemorySerializer<?>> arraySerializers;

	static {
		arraySerializers = new HashMap<>();
		arraySerializers.put(byte[].class.getName(), new ByteArraySerializer());
	}

	private StdArraySerializers() {

	}

	public static MemorySerializer<?> findStandardImpl(Class<?> cls) {
		return arraySerializers.get(cls.getName());
	}
}
