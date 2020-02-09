/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser;

import java.io.IOException;
import java.util.List;

import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.BeanProperty;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.annotation.NullTerminated;
import at.o2xfs.memory.databind.introspect.AnnotatedMember;
import at.o2xfs.memory.databind.ser.std.AsArraySerializerBase;
import at.o2xfs.memory.databind.type.JavaType;

public class IndexedListSerializer extends AsArraySerializerBase<Object> {

	private final boolean nullTerminated;

	public IndexedListSerializer(JavaType elementType, MemorySerializer<?> valueSerializer) {
		this(elementType, valueSerializer, false);
	}

	private IndexedListSerializer(JavaType elementType, MemorySerializer<?> valueSerializer, boolean nullTerminated) {
		super(List.class, elementType, valueSerializer);
		this.nullTerminated = nullTerminated;
	}

	private void serializeContents(List<?> value, MemoryGenerator gen, SerializerProvider ctxt) throws IOException {
		for (int i = 0; i < value.size(); i++) {
			Object elem = value.get(i);
			if (elem == null) {
				gen.writeNull();
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
				gen.startPointer();
				serializer.serialize(elem, gen, ctxt);
				gen.endPointer();
			}
		}
	}

	@Override
	public MemorySerializer<?> createContextual(SerializerProvider ctxt, BeanProperty property) {
		MemorySerializer<?> result = this;
		AnnotatedMember member = property.getMember();
		NullTerminated nullTerminated = member.getAnnotation(NullTerminated.class);
		if (nullTerminated != null) {
			result = new IndexedListSerializer(elementType, elementSerializer, true);
		}
		return result;
	}

	@Override
	public void serialize(Object aValue, MemoryGenerator gen, SerializerProvider ctxt) throws IOException {
		List<?> value = (List<?>) aValue;
		if (!nullTerminated) {
			gen.writeUnsignedShort(value.size());
		}
		if (value.isEmpty()) {
			gen.writeNull();
			return;
		}
		gen.startPointer();
		serializeContents(value, gen, ctxt);
		if (nullTerminated) {
			gen.writeNull();
		}

		gen.endPointer();
	}
}
