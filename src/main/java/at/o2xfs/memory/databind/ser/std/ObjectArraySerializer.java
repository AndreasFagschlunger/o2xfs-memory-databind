/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser.std;

import java.io.IOException;

import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.type.JavaType;

public class ObjectArraySerializer extends ArraySerializerBase<Object[]> {

	protected MemorySerializer<Object> elementSerializer;

	public ObjectArraySerializer(JavaType elemType, MemorySerializer<Object> elementSerializer) {
		super(Object[].class);
		this.elementSerializer = elementSerializer;
	}

	@Override
	public void serialize(Object[] value, MemoryGenerator gen, SerializerProvider ctxt) throws IOException {
		gen.writeUnsignedShort(value.length);
		if (value.length == 0) {
			gen.writeNull();
		} else {
			gen.startPointer();
			serializeContents(value, gen, ctxt);
			gen.endPointer();
		}
	}

	public void serializeContents(Object[] value, MemoryGenerator gen, SerializerProvider ctxt) throws IOException {
		for (int i = 0; i < value.length; i++) {
			Object elem = value[i];
			if (elem == null) {
				ctxt.defaultSerializeNullValue(gen);
				continue;
			}
			Class<?> cc = elem.getClass();
			MemorySerializer<Object> serializer = dynamicValueSerializers.serializerFor(cc);
			if (serializer == null) {
				serializer = findAndAddDynamic(ctxt, cc);
			}
			serializer.serialize(elem, gen, ctxt);
		}
	}

}
