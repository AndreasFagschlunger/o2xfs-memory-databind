/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser.std;

import java.io.IOException;

import at.o2xfs.memory.databind.BeanProperty;
import at.o2xfs.memory.databind.MemoryGenerator;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.ser.ContextualSerializer;
import at.o2xfs.memory.databind.type.JavaType;
import at.o2xfs.memory.databind.type.ReferenceType;

public abstract class ReferenceTypeSerializer<T> extends MemorySerializer<T> implements ContextualSerializer {

	protected final JavaType referredType;
	protected final BeanProperty property;

	public ReferenceTypeSerializer(ReferenceType fullType) {
		this.referredType = fullType.getReferencedType();
		this.property = null;
	}

	protected ReferenceTypeSerializer(ReferenceTypeSerializer<?> base, BeanProperty property) {
		referredType = base.referredType;
		this.property = property;
	}

	private final MemorySerializer<Object> findCachedSerializer(SerializerProvider provider, Class<?> rawType) {
		MemorySerializer<Object> ser = null;
		if (referredType.hasGenericTypes()) {
			JavaType fullType = provider.constructSpecializedType(referredType, rawType);
			ser = provider.findValueSerializer(fullType, property);
		} else {
			ser = provider.findValueSerializer(rawType, property);
		}
		return ser;
	}

	protected abstract Object getReferencedIfPresent(T value);

	protected abstract ReferenceTypeSerializer<T> withResolved(BeanProperty property);

	@Override
	public MemorySerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
		ReferenceTypeSerializer<?> refSer = withResolved(property);
		return refSer;
	}

	@Override
	public void serialize(T ref, MemoryGenerator gen, SerializerProvider provider) {
		Object value = getReferencedIfPresent(ref);
		MemorySerializer<Object> ser = findCachedSerializer(provider, value.getClass());
		try (MemoryGenerator reference = gen.writePointer()) {
			ser.serialize(value, reference, provider);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
