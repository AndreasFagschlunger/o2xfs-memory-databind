/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser;

import java.util.List;

import at.o2xfs.memory.databind.BeanProperty;
import at.o2xfs.memory.databind.MemoryGenerator;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.annotation.MemoryList;
import at.o2xfs.memory.databind.introspect.AnnotatedMember;
import at.o2xfs.memory.databind.ser.win32.ULongSerializer;
import at.o2xfs.memory.databind.ser.win32.UShortSerializer;
import at.o2xfs.memory.databind.type.JavaType;

public class IndexedListSerializer extends MemorySerializer<List<?>> implements ContextualSerializer {

	private final JavaType elementType;

	private final int indexBytes;

	public IndexedListSerializer(JavaType elementType) {
		this(elementType, 0);
	}

	protected IndexedListSerializer(JavaType elementType, int indexBytes) {
		this.elementType = elementType;
		this.indexBytes = indexBytes;
	}

	private void serializeSize(int size, MemoryGenerator gen, SerializerProvider provider) {
		switch (indexBytes) {
		case 2:
			new UShortSerializer().serialize(Integer.valueOf(size), gen, provider);
			break;
		case 4:
			new ULongSerializer().serialize(Long.valueOf(size), gen, provider);
			break;
		}
	}

	@Override
	public MemorySerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
		MemorySerializer<?> result = this;
		AnnotatedMember member = property.getMember();
		MemoryList nativeList = member.getAnnotation(MemoryList.class);
		if (nativeList != null) {
			if (nativeList.indexBytes() > 0) {
				result = new IndexedListSerializer(elementType, nativeList.indexBytes());
			}
		}
		return result;
	}

	@Override
	public void serialize(List<?> value, MemoryGenerator gen, SerializerProvider provider) {
		if (indexBytes > 0) {
			serializeSize(value.size(), gen, provider);
		}
		for (Object each : value) {
			MemorySerializer<Object> serializer = provider.findValueSerializer(each.getClass(), null);
			serializer.serialize(each, gen.writePointer(), provider);
		}
	}
}
