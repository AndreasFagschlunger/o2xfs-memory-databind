/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.cfg;

import at.o2xfs.memory.databind.DeserializationConfig;
import at.o2xfs.memory.databind.deser.DefaultDeserializationContext;
import at.o2xfs.memory.databind.deser.DeserializerCache;
import at.o2xfs.memory.databind.deser.DeserializerFactory;

public abstract class DeserializationContexts {

	protected final DeserializerFactory deserializerFactory;
	protected final DeserializerCache cache;

	protected DeserializationContexts() {
		this(null, null);
	}

	protected DeserializationContexts(DeserializerFactory deserializerFactory, DeserializerCache cache) {
		this.deserializerFactory = deserializerFactory;
		this.cache = cache;
	}

	protected DeserializerCache defaultCache() {
		return new DeserializerCache();
	}

	protected abstract DeserializationContexts forMapper(Object mapper, DeserializerFactory deserializerFactory,
			DeserializerCache cache);

	public abstract DefaultDeserializationContext createContext(DeserializationConfig config);

	public DeserializationContexts forMapper(Object mapper, DeserializerFactory deserializerFactory) {
		return forMapper(mapper, deserializerFactory, defaultCache());
	}

	public static class DefaultImpl extends DeserializationContexts {

		public DefaultImpl() {
			super();
		}

		public DefaultImpl(DeserializerFactory deserializerFactory, DeserializerCache cache) {
			super(deserializerFactory, cache);
		}

		@Override
		protected DeserializationContexts forMapper(Object mapper, DeserializerFactory deserializerFactory,
				DeserializerCache cache) {
			return new DefaultImpl(deserializerFactory, cache);
		}

		@Override
		public DefaultDeserializationContext createContext(DeserializationConfig config) {
			return new DefaultDeserializationContext.Impl(deserializerFactory, cache, config);
		}

	}
}
