/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;

import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.ser.std.AsArraySerializerBase;
import at.o2xfs.memory.databind.ser.win32.UShortSerializer;
import at.o2xfs.memory.databind.type.JavaType;

public class IndexedListSerializer extends AsArraySerializerBase<Object> {

	private final int indexBytes;

	public IndexedListSerializer(JavaType elementType, MemorySerializer<?> valueSerializer) {
		super(List.class, elementType, valueSerializer);
		indexBytes = 0;
	}

	private void serializeSize(int size, MemoryGenerator gen, SerializerProvider provider) {
		switch (indexBytes) {
		case 2:
			UShortSerializer.instance.serialize(Integer.valueOf(size), gen, provider);
			break;
		case 4:
			UShortSerializer.instance.serialize(Long.valueOf(size), gen, provider);
			break;
		}
	}

	@Override
	public void serialize(Object aValue, MemoryGenerator gen, SerializerProvider ctxt) throws IOException {
		List<?> value = (List<?>) aValue;
		if (indexBytes > 0) {
			serializeSize(value.size(), gen, ctxt);
		}
		if (elementSerializer != null) {
			serializeContentsUsing(value, gen, ctxt, elementSerializer);
			return;
		}
		System.out.println("value=" + value);
		for (int i = 0; i < value.size(); i++) {
			Object elem = value.get(i);
			if (elem == null) {

			} else {
				Class<?> cc = elem.getClass();
				MemorySerializer<Object> serializer = dynamicValueSerializers.serializerFor(cc);
				if (serializer == null) {
					if (elementType.hasGenericTypes()) {
						serializer = findAndAddDynamic(ctxt,
								ctxt.constructSpecializedType(elementType, elem.getClass()));
					} else {
						serializer = findAndAddDynamic(ctxt, cc);
					}
				}
				System.out.println("serializer=" + serializer);
				serializer.serialize(elem, gen, ctxt);
			}
		}
	}

	private void serializeContentsUsing(List<?> value, MemoryGenerator gen, SerializerProvider provider,
			MemorySerializer<Object> ser) throws IOException {
		boolean arrayOfPointers = !ClassUtils.isPrimitiveOrWrapper(elementType.getRawClass());
		for (Object each : value) {
			if (arrayOfPointers) {
				gen.startPointer();
			}
			ser.serialize(each, gen, provider);
			if (arrayOfPointers) {
				gen.endPointer();
			}
		}
	}
}
