/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import at.o2xfs.memory.databind.BeanDescription;
import at.o2xfs.memory.databind.DeserializationConfig;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.introspect.Annotated;
import at.o2xfs.memory.databind.type.CollectionLikeType;
import at.o2xfs.memory.databind.type.CollectionType;
import at.o2xfs.memory.databind.type.JavaType;
import at.o2xfs.memory.databind.type.MapLikeType;
import at.o2xfs.memory.databind.type.MapType;
import at.o2xfs.memory.databind.type.ReferenceType;

public class DeserializerCache {

	private final Map<JavaType, MemoryDeserializer<Object>> cachedDeserializers = new ConcurrentHashMap<>();

	private MemoryDeserializer<Object> createAndCacheValueDeserializer(DeserializationContext ctxt,
			DeserializerFactory factory, JavaType type) {
		MemoryDeserializer<Object> result = createAndCache(ctxt, factory, type);
		return result;
	}

	private MemoryDeserializer<Object> createAndCache(DeserializationContext ctxt, DeserializerFactory factory,
			JavaType type) {
		MemoryDeserializer<Object> deser = createDeserializer(ctxt, factory, type);
		if (deser == null) {
			return null;
		}
		boolean addToCache = deser.isCachable();
		deser.resolve(ctxt);
		if (addToCache) {
			cachedDeserializers.put(type, deser);
		}
		return deser;
	}

	private MemoryDeserializer<Object> createDeserializer(DeserializationContext ctxt, DeserializerFactory factory,
			JavaType type) {
		DeserializationConfig config = ctxt.getConfig();
		BeanDescription beanDesc = ctxt.introspectBeanDescription(type);

		MemoryDeserializer<Object> result = findDeserializerFromAnnotation(ctxt, beanDesc.getClassInfo());
		if (result == null) {
			Class<?> builder = beanDesc.findPOJOBuilder();
			if (builder != null) {
				result = factory.createBuilderBasedDeserializer(ctxt, type, beanDesc, builder);
			} else {
				result = (MemoryDeserializer<Object>) createDeserializer2(ctxt, factory, type, beanDesc);
			}
		}
		return result;
	}

	private MemoryDeserializer<?> createDeserializer2(DeserializationContext ctxt, DeserializerFactory factory,
			JavaType type, BeanDescription beanDesc) {
		if (type.isEnumType()) {
			return factory.createEnumDeserializer(ctxt, type, beanDesc);
		}
		if (type.isContainerType()) {
			if (type.isMapLikeType()) {
				MapLikeType mlt = (MapLikeType) type;
				if (mlt.isTrueMapType()) {
					return factory.createMapDeserializer(ctxt, (MapType) mlt, beanDesc);
				}
			}
			if (type.isCollectionLikeType()) {
				CollectionLikeType clt = (CollectionLikeType) type;
				if (clt.isTrueCollectionType()) {
					return factory.createCollectionDeserializer(ctxt, (CollectionType) clt, beanDesc);
				}
			}
		}
		if (type.isReferenceType()) {
			return factory.createReferenceDeserializer(ctxt, (ReferenceType) type, beanDesc);
		}
		return factory.createBeanDeserializer(ctxt, type, beanDesc);
	}

	private MemoryDeserializer<Object> findCachedDeserializer(JavaType type) {
		return cachedDeserializers.get(type);
	}

	private MemoryDeserializer<Object> findDeserializerFromAnnotation(DeserializationContext ctxt, Annotated ann) {
		MemoryDeserializer<Object> result = null;
		Object deserDef = ctxt.getAnnotationIntrospector().findDeserializer(ann);
		if (deserDef != null) {
			result = ctxt.deserializerInstance(ann, deserDef);
		}
		return result;
	}

	public MemoryDeserializer<Object> findValueDeserializer(DeserializationContext ctxt, DeserializerFactory factory,
			JavaType propertyType) {
		MemoryDeserializer<Object> result = findCachedDeserializer(propertyType);
		if (result == null) {
			result = createAndCacheValueDeserializer(ctxt, factory, propertyType);
		}
		return result;
	}
}
