/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser.std;

import java.util.Map;

import at.o2xfs.memory.databind.MemorySerializer;

public class NumberSerializers {

	public static void addAll(Map<String, MemorySerializer<?>> allSerializers) {
		allSerializers.put(Byte.class.getName(), NumberSerializer.instance);
		allSerializers.put(Byte.TYPE.getName(), NumberSerializer.instance);
		allSerializers.put(Integer.class.getName(), NumberSerializer.instance);
		allSerializers.put(Integer.TYPE.getName(), NumberSerializer.instance);
		allSerializers.put(Long.class.getName(), NumberSerializer.instance);
		allSerializers.put(Long.TYPE.getName(), NumberSerializer.instance);
		allSerializers.put(Short.class.getName(), NumberSerializer.instance);
		allSerializers.put(Short.TYPE.getName(), NumberSerializer.instance);
	}
}
