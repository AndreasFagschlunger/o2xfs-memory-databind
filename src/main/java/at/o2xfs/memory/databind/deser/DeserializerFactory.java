/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser;

import at.o2xfs.memory.databind.BeanDescription;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.type.ArrayType;
import at.o2xfs.memory.databind.type.CollectionType;
import at.o2xfs.memory.databind.type.JavaType;
import at.o2xfs.memory.databind.type.MapType;
import at.o2xfs.memory.databind.type.ReferenceType;

public abstract class DeserializerFactory {

	public abstract MemoryDeserializer<?> createArrayDeserializer(DeserializationContext ctxt, ArrayType type,
			BeanDescription beanDesc);

	public abstract MemoryDeserializer<?> createBeanDeserializer(DeserializationContext ctxt, JavaType type,
			BeanDescription beanDesc);

	public abstract MemoryDeserializer<Object> createBuilderBasedDeserializer(DeserializationContext ctxt,
			JavaType valueType, BeanDescription beanDesc, Class<?> builderClass);

	public abstract MemoryDeserializer<?> createEnumDeserializer(DeserializationContext ctxt, JavaType type,
			BeanDescription beanDesc);

	public abstract MemoryDeserializer<?> createCollectionDeserializer(DeserializationContext ctxt, CollectionType clt,
			BeanDescription beanDesc);

	public abstract MemoryDeserializer<?> createMapDeserializer(DeserializationContext ctxt, MapType mlt,
			BeanDescription beanDesc);

	public abstract MemoryDeserializer<?> createReferenceDeserializer(DeserializationContext ctxt, ReferenceType type,
			BeanDescription beanDesc);

	public abstract DeserializerFactory withAdditionalDeserializers(Deserializers d);

}
