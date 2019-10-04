/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser;

import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializationConfig;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.introspect.Annotated;
import at.o2xfs.memory.databind.util.ClassUtil;

public abstract class DefaultSerializerProvider extends SerializerProvider {

	protected DefaultSerializerProvider() {
		super();
	}

	protected DefaultSerializerProvider(SerializerProvider src, SerializationConfig config, SerializerFactory f) {
		super(src, config, f);
	}

	public abstract DefaultSerializerProvider createInstance(SerializationConfig config, SerializerFactory jsf);

	@Override
	public MemorySerializer<?> serializerInstance(Annotated annotated, Object serDef) {
		return (MemorySerializer<?>) ClassUtil.createInstance((Class<?>) serDef);
	}

	public static final class Impl extends DefaultSerializerProvider {

		public Impl() {
			super();
		}

		private Impl(SerializerProvider src, SerializationConfig config, SerializerFactory f) {
			super(src, config, f);
		}

		@Override
		public DefaultSerializerProvider createInstance(SerializationConfig config, SerializerFactory f) {
			return new Impl(this, config, f);
		}

	}

}
