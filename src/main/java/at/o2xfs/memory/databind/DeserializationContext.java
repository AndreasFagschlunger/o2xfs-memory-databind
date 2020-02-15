/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind;

import java.util.Objects;

import at.o2xfs.memory.databind.deser.DeserializerCache;
import at.o2xfs.memory.databind.deser.DeserializerFactory;
import at.o2xfs.memory.databind.introspect.Annotated;
import at.o2xfs.memory.databind.introspect.ClassIntrospector;
import at.o2xfs.memory.databind.type.JavaType;
import at.o2xfs.memory.databind.type.TypeFactory;

public abstract class DeserializationContext extends DatabindContext {

	private final DeserializationConfig config;
	protected final DeserializerFactory factory;
	private final DeserializerCache cache;
	protected ClassIntrospector classIntrospector;

	public DeserializationContext(DeserializerFactory factory, DeserializerCache cache, DeserializationConfig config) {
		this.factory = Objects.requireNonNull(factory);
		this.config = config;
		this.cache = cache;
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

	@Override
	protected ClassIntrospector classIntrospector() {
		if (classIntrospector == null) {
			classIntrospector = config.classIntrospectorInstance();
		}
		return classIntrospector;
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

	public final AnnotationIntrospector getAnnotationIntrospector() {
		return config.getAnnotationIntrospector();
	}

	@Override
	public DeserializationConfig getConfig() {
		return config;
	}

	@Override
	public final TypeFactory getTypeFactory() {
		return config.getTypeFactory();
	}

	public abstract MemoryDeserializer<Object> deserializerInstance(Annotated annotated, Object deserDef);

	public MemoryDeserializer<?> handlePrimaryContextualization(MemoryDeserializer<?> deser, BeanProperty prop,
			JavaType type) {
		if (deser != null) {
			deser = deser.createContextual(this, prop);
		}
		return deser;
	}

	public MemoryDeserializer<?> handleSecondaryContextualization(MemoryDeserializer<?> deser, BeanProperty prop,
			JavaType type) {
		if (deser != null) {
			deser = deser.createContextual(this, prop);
		}
		return deser;
	}

	@Override
	public BeanDescription introspectBeanDescription(JavaType type) {
		return classIntrospector().introspectForDeserialization(type);
	}

	public BeanDescription introspectBeanDescriptionForBuilder(JavaType type) {
		return classIntrospector().introspectForDeserializationWithBuilder(type);
	}
}
