/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import at.o2xfs.memory.databind.AnnotationIntrospector;
import at.o2xfs.memory.databind.BeanDescription;
import at.o2xfs.memory.databind.DeserializationConfig;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.cfg.DeserializerFactoryConfig;
import at.o2xfs.memory.databind.deser.impl.CreatorCollector;
import at.o2xfs.memory.databind.deser.std.CollectionDeserializer;
import at.o2xfs.memory.databind.deser.std.EnumDeserializer;
import at.o2xfs.memory.databind.deser.std.MapDeserializer;
import at.o2xfs.memory.databind.deser.std.NumberDeserializers;
import at.o2xfs.memory.databind.deser.std.ObjectArrayDeserializer;
import at.o2xfs.memory.databind.deser.std.StringDeserializer;
import at.o2xfs.memory.databind.ext.jdk8.Jdk8OptionalDeserializer;
import at.o2xfs.memory.databind.introspect.Annotated;
import at.o2xfs.memory.databind.introspect.AnnotatedConstructor;
import at.o2xfs.memory.databind.introspect.AnnotatedMember;
import at.o2xfs.memory.databind.type.ArrayType;
import at.o2xfs.memory.databind.type.CollectionType;
import at.o2xfs.memory.databind.type.JavaType;
import at.o2xfs.memory.databind.type.MapType;
import at.o2xfs.memory.databind.type.ReferenceType;
import at.o2xfs.memory.databind.type.TypeFactory;

public abstract class BasicDeserializerFactory extends DeserializerFactory {

	private final static Class<?> CLASS_STRING = String.class;
	private final static Class<?> CLASS_ITERABLE = Iterable.class;

	private final DeserializerFactoryConfig factoryConfig;

	public BasicDeserializerFactory(DeserializerFactoryConfig factoryConfig) {
		this.factoryConfig = Objects.requireNonNull(factoryConfig);
	}

	private void addDeserializerConstructors(DeserializationContext ctxt, BeanDescription beanDesc,
			CreatorCollector creators) {
		AnnotatedConstructor defaultCtor = beanDesc.findDefaultConstructor();
		if (defaultCtor != null) {
			creators.setDefaultCreator(defaultCtor);
		}
	}

	private ValueInstantiator constructDefaultValueInstantiator(DeserializationContext ctxt, BeanDescription beanDesc) {
		CreatorCollector creators = new CreatorCollector(beanDesc);
		addDeserializerConstructors(ctxt, beanDesc, creators);
		return creators.constructValueInstantiator(ctxt);
	}

	private MemoryDeserializer<?> findCustomReferenceDeserializer(ReferenceType refType, DeserializationConfig config,
			BeanDescription beanDesc) {
		MemoryDeserializer<?> result = null;
		for (Deserializers each : factoryConfig.deserializers()) {
			result = each.findReferenceDeserializer(refType, config, beanDesc);
			if (result != null) {
				break;
			}
		}
		return result;
	}

	protected MemoryDeserializer<Object> findDeserializerFromAnnotation(DeserializationContext ctxt, Annotated ann) {
		MemoryDeserializer<Object> result = null;
		AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
		if (intr != null) {
			Object deserDef = intr.findDeserializer(ann);
			if (deserDef != null) {
				result = ctxt.deserializerInstance(ann, deserDef);
			}
		}
		return result;
	}

	@Override
	public MemoryDeserializer<?> createArrayDeserializer(DeserializationContext ctxt, ArrayType type,
			BeanDescription beanDesc) {
		return new ObjectArrayDeserializer(type, null);
	}

	@Override
	public MemoryDeserializer<?> createCollectionDeserializer(DeserializationContext ctxt, CollectionType type,
			BeanDescription beanDesc) {
		return new CollectionDeserializer(type, null);
	}

	@Override
	public MemoryDeserializer<?> createEnumDeserializer(DeserializationContext ctxt, JavaType type,
			BeanDescription beanDesc) {
		return new EnumDeserializer(type);
	}

	@Override
	public MemoryDeserializer<?> createMapDeserializer(DeserializationContext ctxt, MapType mlt,
			BeanDescription beanDesc) {
		return new MapDeserializer(mlt);
	}

	public MemoryDeserializer<?> findDefaultDeserializer(DeserializationContext ctxt, JavaType type,
			BeanDescription beanDesc) {
		Class<?> rawType = type.getRawClass();
		String clsName = rawType.getName();
		MemoryDeserializer<?> result = null;
		if (rawType == CLASS_STRING) {
			return StringDeserializer.instance;
		}
		if (rawType == CLASS_ITERABLE) {
			TypeFactory tf = ctxt.getTypeFactory();
			JavaType[] tps = tf.findTypeParameters(type, CLASS_ITERABLE);
			JavaType elemType = (tps == null || tps.length != 1) ? TypeFactory.unknownType() : tps[0];
			CollectionType ct = tf.constructCollectionType(Collection.class, elemType);
			return createCollectionDeserializer(ctxt, ct, beanDesc);
		}
		if (rawType.isPrimitive() || clsName.startsWith("java.")) {
			result = NumberDeserializers.find(rawType, clsName);
		}
		return result;
	}

	protected ValueInstantiator findValueInstantiator(DeserializationContext ctxt, BeanDescription beanDesc) {
		return constructDefaultValueInstantiator(ctxt, beanDesc);
	}

	protected JavaType resolveMemberAndTypeAnnotations(DeserializationContext ctxt, AnnotatedMember member,
			JavaType type) {
		return type;

	}

	public abstract DeserializerFactory withConfig(DeserializerFactoryConfig config);

	@Override
	public final DeserializerFactory withAdditionalDeserializers(Deserializers additional) {
		return withConfig(factoryConfig.withAdditionalDeserializers(additional));
	}

	@Override
	public MemoryDeserializer<?> createReferenceDeserializer(DeserializationContext ctxt, ReferenceType type,
			BeanDescription beanDesc) {
		JavaType contentType = type.getContentType();
		MemoryDeserializer<Object> contentTypeDeser = contentType.getTypeHandler();
		MemoryDeserializer<?> deser = findCustomReferenceDeserializer(type, ctxt.getConfig(), beanDesc);
		if (deser == null) {
			if (type.isTypeOrSubTypeOf(Optional.class)) {
				return new Jdk8OptionalDeserializer(type, contentTypeDeser);
			}
		}
		return deser;
	}
}
