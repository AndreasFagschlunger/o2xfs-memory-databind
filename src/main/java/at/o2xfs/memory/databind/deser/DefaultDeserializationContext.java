/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser;

import at.o2xfs.memory.databind.DeserializationConfig;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.introspect.Annotated;
import at.o2xfs.memory.databind.util.ClassUtil;

public abstract class DefaultDeserializationContext extends DeserializationContext {

	public DefaultDeserializationContext(DeserializerFactory factory, DeserializerCache cache,
			DeserializationConfig config) {
		super(factory, cache, config);
	}

	private DefaultDeserializationContext(DefaultDeserializationContext src, DeserializationConfig config) {
		super(src, config);
	}

	private DefaultDeserializationContext(DefaultDeserializationContext src, DeserializerFactory factory) {
		super(src, factory);
	}

	public abstract DeserializationContext createInstance(DeserializationConfig cfg);

	@Override
	public MemoryDeserializer<Object> deserializerInstance(Annotated annotated, Object deserDef) {
		MemoryDeserializer<?> result = null;
		Class<?> deserClass = (Class<?>) deserDef;
		if (deserClass != MemoryDeserializer.None.class) {
			result = (MemoryDeserializer<?>) ClassUtil.createInstance(deserClass);
		}
		return (MemoryDeserializer<Object>) result;
	}

	public abstract DefaultDeserializationContext with(DeserializerFactory factory);

	public static final class Impl extends DefaultDeserializationContext {

		public Impl(DeserializerFactory factory, DeserializerCache cache, DeserializationConfig config) {
			super(factory, cache, config);
		}

		private Impl(Impl src, DeserializationConfig cfg) {
			super(src, cfg);
		}

		private Impl(Impl src, DeserializerFactory factory) {
			super(src, factory);
		}

		@Override
		public DeserializationContext createInstance(DeserializationConfig cfg) {
			return new Impl(this, cfg);
		}

		@Override
		public DefaultDeserializationContext with(DeserializerFactory factory) {
			return new Impl(this, factory);
		}

	}

}
