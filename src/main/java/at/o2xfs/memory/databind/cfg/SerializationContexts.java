/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.cfg;

import at.o2xfs.memory.databind.SerializationConfig;
import at.o2xfs.memory.databind.ser.DefaultSerializerProvider;
import at.o2xfs.memory.databind.ser.SerializerCache;
import at.o2xfs.memory.databind.ser.SerializerFactory;

public abstract class SerializationContexts {

	protected final SerializerFactory serializerFactory;
	protected final SerializerCache cache;

	protected SerializationContexts() {
		this(null, null);
	}

	protected SerializationContexts(SerializerFactory serializerFactory, SerializerCache cache) {
		this.serializerFactory = serializerFactory;
		this.cache = cache;
	}

	protected SerializerCache defaultCache() {
		return new SerializerCache();
	}

	protected abstract SerializationContexts forMapper(Object mapper, SerializerFactory serializerFactory,
			SerializerCache defaultCache);

	public abstract DefaultSerializerProvider createContext(SerializationConfig serializationConfig);

	public SerializationContexts forMapper(Object mapper, SerializerFactory serializerFactory) {
		return forMapper(mapper, serializerFactory, defaultCache());
	}

	public static class DefaultImpl extends SerializationContexts {

		public DefaultImpl() {
			super(null, null);
		}

		public DefaultImpl(SerializerFactory serializerFactory, SerializerCache cache) {
			super(serializerFactory, cache);
		}

		@Override
		public DefaultSerializerProvider createContext(SerializationConfig serializationConfig) {
			return new DefaultSerializerProvider.Impl(serializationConfig, serializerFactory, cache);
		}

		@Override
		protected SerializationContexts forMapper(Object mapper, SerializerFactory serializerFactory,
				SerializerCache cache) {
			return new DefaultImpl(serializerFactory, cache);
		}

	}

}
