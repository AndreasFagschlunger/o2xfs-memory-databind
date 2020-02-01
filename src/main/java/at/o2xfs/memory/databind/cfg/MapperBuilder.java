/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.cfg;

import at.o2xfs.memory.databind.AnnotationIntrospector;
import at.o2xfs.memory.databind.DeserializationConfig;
import at.o2xfs.memory.databind.SerializationConfig;
import at.o2xfs.memory.databind.deser.BeanDeserializerFactory;
import at.o2xfs.memory.databind.deser.DeserializerFactory;
import at.o2xfs.memory.databind.introspect.BasicClassIntrospector;
import at.o2xfs.memory.databind.introspect.ClassIntrospector;
import at.o2xfs.memory.databind.introspect.MemoryAnnotationIntrospector;
import at.o2xfs.memory.databind.jsontype.TypeResolverProvider;
import at.o2xfs.memory.databind.ser.BeanSerializerFactory;
import at.o2xfs.memory.databind.ser.SerializerFactory;
import at.o2xfs.memory.databind.type.TypeFactory;

public class MapperBuilder {

	protected final static AnnotationIntrospector DEFAULT_ANNOTATION_INTROSPECTOR = new MemoryAnnotationIntrospector();
	protected final static BaseSettings DEFAULT_BASE_SETTINGS = new BaseSettings(DEFAULT_ANNOTATION_INTROSPECTOR);

	private BaseSettings baseSettings;

	private TypeFactory typeFactory;
	private ClassIntrospector classIntrospector;
	private TypeResolverProvider typeResolverProvider;

	private SerializationContexts serializationContexts;
	private SerializerFactory serializerFactory;

	private DeserializationContexts deserializationContexts;
	private DeserializerFactory deserializerFactory;

	protected MapperBuilder() {
		baseSettings = DEFAULT_BASE_SETTINGS;
	}

	protected ClassIntrospector defaultClassIntrospector() {
		return new BasicClassIntrospector();
	}

	protected DeserializationContexts defaultDeserializationContexts() {
		return new DeserializationContexts.DefaultImpl();
	}

	protected DeserializerFactory defaultDeserializerFactory() {
		return BeanDeserializerFactory.instance;
	}

	protected SerializationContexts defaultSerializationContexts() {
		return new SerializationContexts.DefaultImpl();
	}

	protected SerializerFactory defaultSerializerFactory() {
		return BeanSerializerFactory.instance;
	}

	protected TypeFactory defaultTypeFactory() {
		return TypeFactory.defaultInstance();
	}

	protected TypeResolverProvider defaultTypeResolverProvider() {
		return new TypeResolverProvider();
	}

	public BaseSettings baseSettings() {
		return baseSettings;
	}

	public DeserializationConfig buildDeserializationConfig(TypeFactory tf, ClassIntrospector classIntr) {
		return new DeserializationConfig(this, tf, classIntr);
	}

	public SerializationConfig buildSerializationConfig(TypeFactory tf, ClassIntrospector classIntr) {
		return new SerializationConfig(this, tf, classIntr);
	}

	public ClassIntrospector classIntrospector() {
		if (classIntrospector == null) {
			classIntrospector = defaultClassIntrospector();
		}
		return classIntrospector;
	}

	public DeserializationContexts deserializationContexts() {
		if (deserializationContexts == null) {
			deserializationContexts = defaultDeserializationContexts();
		}
		return deserializationContexts;
	}

	public DeserializerFactory deserializerFactory() {
		if (deserializerFactory == null) {
			deserializerFactory = defaultDeserializerFactory();
		}
		return deserializerFactory;
	}

	public SerializationContexts serializationContexts() {
		if (serializationContexts == null) {
			serializationContexts = defaultSerializationContexts();
		}
		return serializationContexts;
	}

	public SerializerFactory serializerFactory() {
		if (serializerFactory == null) {
			serializerFactory = defaultSerializerFactory();
		}
		return serializerFactory;
	}

	public TypeResolverProvider typeResolverProvider() {
		if (typeResolverProvider == null) {
			typeResolverProvider = defaultTypeResolverProvider();
		}
		return typeResolverProvider;
	}

	public TypeFactory typeFactory() {
		if (typeFactory == null) {
			typeFactory = defaultTypeFactory();
		}
		return typeFactory;
	}

}
