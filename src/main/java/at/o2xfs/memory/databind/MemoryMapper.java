/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind;

import java.io.IOException;
import java.util.Objects;

import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.cfg.DeserializationContexts;
import at.o2xfs.memory.databind.cfg.MapperBuilder;
import at.o2xfs.memory.databind.cfg.SerializationContexts;
import at.o2xfs.memory.databind.deser.DefaultDeserializationContext;
import at.o2xfs.memory.databind.introspect.ClassIntrospector;
import at.o2xfs.memory.databind.ser.DefaultSerializerProvider;
import at.o2xfs.memory.databind.ser.SerializerFactory;
import at.o2xfs.memory.databind.type.JavaType;
import at.o2xfs.memory.databind.type.TypeFactory;

public class MemoryMapper {

	private final TypeFactory typeFactory;
	private final SerializationContexts serializationContexts;
	private final SerializationConfig serializationConfig;

	private final DeserializationConfig deserializationConfig;

	private DeserializationContexts deserializationContexts;

	private SerializerFactory serializerFactory;

	public MemoryMapper() {
		this(new PrivateBuilder());
	}

	public MemoryMapper(MapperBuilder b) {
		typeFactory = b.typeFactory();
		serializationContexts = b.serializationContexts().forMapper(this, b.serializerFactory());
		deserializationContexts = b.deserializationContexts().forMapper(this, b.deserializerFactory());

		ClassIntrospector classIntr = b.classIntrospector().forMapper();
		deserializationConfig = b.buildDeserializationConfig(typeFactory, classIntr);
		serializationConfig = b.buildSerializationConfig(typeFactory, classIntr);
	}

	private DefaultDeserializationContext deserializationContext() {
		return deserializationContexts.createContext(deserializationConfig());
	}

	private MemoryDeserializer<Object> findRootDeserializer(DeserializationContext ctxt, JavaType valueType) {
		MemoryDeserializer<Object> result = ctxt.findRootValueDeserializer(valueType);
		return result;
	}

	private Object readMapAndClose(ReadableMemory memory, JavaType valueType) {
		DeserializationContext ctxt = deserializationContext();
		MemoryDeserializer<Object> deser = findRootDeserializer(ctxt, valueType);
		Object result = deser.deserialize(memory, ctxt);
		return result;
	}

	private DefaultSerializerProvider serializerProvider() {
		return serializationContexts.createContext(serializationConfig());
	}

	public DeserializationConfig deserializationConfig() {
		return deserializationConfig;
	}

	public SerializationConfig getSerializationConfig() {
		return serializationConfig;
	}

	public SerializationConfig serializationConfig() {
		return serializationConfig;
	}

	public void write(MemoryGenerator gen, Object value) throws IOException {
		serializerProvider().serializeValue(gen, value);
	}

	public <T> T read(ReadableMemory memory, Class<T> valueType) {
		Objects.requireNonNull(memory, "memory must not be null");
		Objects.requireNonNull(valueType, "valueType must not be null");
		return (T) readMapAndClose(memory, typeFactory.constructType(valueType));
	}

	private static final class PrivateBuilder extends MapperBuilder {

	}
}
