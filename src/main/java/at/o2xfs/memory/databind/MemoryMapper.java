/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind;

import java.util.Objects;

import at.o2xfs.memory.databind.Module.SetupContext;
import at.o2xfs.memory.databind.deser.BeanDeserializerFactory;
import at.o2xfs.memory.databind.deser.DefaultDeserializationContext;
import at.o2xfs.memory.databind.deser.DeserializerFactory;
import at.o2xfs.memory.databind.deser.Deserializers;
import at.o2xfs.memory.databind.introspect.BasicClassIntrospector;
import at.o2xfs.memory.databind.introspect.ClassIntrospector;
import at.o2xfs.memory.databind.ser.BeanSerializerFactory;
import at.o2xfs.memory.databind.ser.DefaultSerializerProvider;
import at.o2xfs.memory.databind.ser.SerializerFactory;
import at.o2xfs.memory.databind.ser.Serializers;
import at.o2xfs.memory.databind.type.JavaType;
import at.o2xfs.memory.databind.type.TypeFactory;

public class MemoryMapper {

	private final static AnnotationIntrospector DEFAULT_ANNOTATION_INTROSPECTOR = new AnnotationIntrospector();

	private final TypeFactory typeFactory;

	private final DeserializationConfig deserializationConfig;

	private DefaultDeserializationContext deserializationContext;

	private final SerializationConfig serializationConfig;
	private SerializerFactory serializerFactory;
	private final DefaultSerializerProvider serializerProvider;

	public MemoryMapper() {
		typeFactory = TypeFactory.defaultInstance();
		ClassIntrospector classIntrospector = new BasicClassIntrospector();
		deserializationConfig = new DeserializationConfig(typeFactory, classIntrospector,
				DEFAULT_ANNOTATION_INTROSPECTOR);
		deserializationContext = new DefaultDeserializationContext.Impl(BeanDeserializerFactory.INSTANCE);
		serializationConfig = new SerializationConfig(typeFactory, classIntrospector, DEFAULT_ANNOTATION_INTROSPECTOR);
		serializerProvider = new DefaultSerializerProvider.Impl();
		serializerFactory = BeanSerializerFactory.instance;
	}

	private DeserializationContext createDeserializationContext(DeserializationConfig cfg) {
		return deserializationContext.createInstance(cfg);
	}

	private MemoryDeserializer<Object> findRootDeserializer(DeserializationContext ctxt, JavaType valueType) {
		MemoryDeserializer<Object> result = ctxt.findRootValueDeserializer(valueType);
		return result;
	}

	private Object readMapAndClose(ReadableMemory memory, JavaType valueType) {
		DeserializationContext ctxt = createDeserializationContext(deserializationConfig);
		MemoryDeserializer<Object> deser = findRootDeserializer(ctxt, valueType);
		Object result = deser.deserialize(memory, ctxt);
		return result;
	}

	private SerializerProvider serializerProvider(SerializationConfig config) {
		return serializerProvider.createInstance(config, serializerFactory);
	}

	public SerializationConfig getSerializationConfig() {
		return serializationConfig;
	}

	public MemoryMapper registerModule(Module module) {
		module.setupModule(new SetupContext() {

			@Override
			public void addSerializers(Serializers s) {
				serializerFactory = serializerFactory.withAdditionalSerializers(s);
			}

			@Override
			public void addDeserializers(Deserializers d) {
				DeserializerFactory df = deserializationContext.factory.withAdditionalDeserializers(d);
				deserializationContext = deserializationContext.with(df);

			}
		});
		return this;
	}

	public void write(MemoryGenerator memory, Object value) {
		SerializationConfig cfg = getSerializationConfig();
		serializerProvider(cfg).serializeValue(memory, value);
	}

	public <T> T read(ReadableMemory memory, Class<T> valueType) {
		Objects.requireNonNull(memory, "memory must not be null");
		Objects.requireNonNull(valueType, "valueType must not be null");
		return (T) readMapAndClose(memory, typeFactory.constructType(valueType));
	}
}
