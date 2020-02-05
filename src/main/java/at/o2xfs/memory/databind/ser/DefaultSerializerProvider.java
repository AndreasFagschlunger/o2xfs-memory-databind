/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser;

import java.io.IOException;

import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializationConfig;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.introspect.Annotated;
import at.o2xfs.memory.databind.util.ClassUtil;

public abstract class DefaultSerializerProvider extends SerializerProvider {

	protected DefaultSerializerProvider(SerializationConfig config, SerializerFactory f, SerializerCache cache) {
		super(config, f, cache);
	}

	@Override
	public MemorySerializer<?> serializerInstance(Annotated annotated, Object serDef) {
		if (serDef == null) {
			return null;
		}
		return (MemorySerializer<?>) ClassUtil.createInstance((Class<?>) serDef);
	}

	public void serializeValue(MemoryGenerator gen, Object value) throws IOException {
		MemorySerializer<Object> serializer = findTypedValueSerializer(value.getClass(), true);
		serializer.serialize(value, gen, this);
	}

	public static final class Impl extends DefaultSerializerProvider {

		public Impl(SerializationConfig config, SerializerFactory f, SerializerCache cache) {
			super(config, f, cache);
		}
	}

}
