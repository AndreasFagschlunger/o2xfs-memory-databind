/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind;

import java.util.Objects;

import at.o2xfs.memory.databind.deser.ContextualDeserializer;
import at.o2xfs.memory.databind.deser.DeserializerCache;
import at.o2xfs.memory.databind.deser.DeserializerFactory;
import at.o2xfs.memory.databind.introspect.Annotated;
import at.o2xfs.memory.databind.type.JavaType;

public abstract class DeserializationContext {

	private final DeserializationConfig config;
	protected final DeserializerFactory factory;
	private final DeserializerCache cache;

	public DeserializationContext(DeserializerFactory factory, DeserializationConfig config) {
		this.factory = Objects.requireNonNull(factory);
		this.config = config;
		cache = new DeserializerCache();
	}

	protected DeserializationContext(DeserializationContext src, DeserializerFactory factory) {
		config = src.config;
		cache = src.cache;
		this.factory = factory;

	}

	protected DeserializationContext(DeserializationContext src, DeserializationConfig config) {
		this.config = config;
		factory = src.factory;
		cache = src.cache;
	}

	public final JavaType constructType(Class<?> cls) {
		return config.constructType(cls);
	}

	public MemoryDeserializer<?> findContextualValueDeserializer(JavaType type, BeanProperty property) {
		MemoryDeserializer<Object> deser = cache.findValueDeserializer(this, factory, type);
		if (deser == null) {
			throw new AssertionError();
		}
		return deser;
	}

	public MemoryDeserializer<?> findNonContextualValueDeserializer(JavaType type) {
		return cache.findValueDeserializer(this, factory, type);
	}

	public final MemoryDeserializer<Object> findRootValueDeserializer(JavaType type) {
		MemoryDeserializer<Object> result = cache.findValueDeserializer(this, factory, type);
		return result;
	}

	public DeserializationConfig getConfig() {
		return config;
	}

	public final AnnotationIntrospector getAnnotationIntrospector() {
		return config.getAnnotationIntrospector();
	}

	public abstract MemoryDeserializer<Object> deserializerInstance(Annotated annotated, Object deserDef);

	public MemoryDeserializer<?> handlePrimaryContextualization(MemoryDeserializer<?> deser, BeanProperty prop,
			JavaType type) {
		if (deser instanceof ContextualDeserializer) {
			deser = ((ContextualDeserializer) deser).createContextual(this, prop);
		}
		return deser;
	}

}
