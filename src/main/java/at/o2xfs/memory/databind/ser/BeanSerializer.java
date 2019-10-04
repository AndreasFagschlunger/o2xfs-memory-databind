/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser;

import java.util.List;

import at.o2xfs.memory.databind.MemoryGenerator;
import at.o2xfs.memory.databind.SerializerProvider;

public class BeanSerializer extends BeanSerializerBase {

	public BeanSerializer(List<BeanPropertyWriter> properties) {
		super(properties);
	}

	@Override
	public void serialize(Object obj, MemoryGenerator gen, SerializerProvider prov) {
		serializeFields(obj, gen, prov);
	}

}
