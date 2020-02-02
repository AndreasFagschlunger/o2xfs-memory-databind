package at.o2xfs.memory.databind.ext.jdk8;

import java.util.Optional;

import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.deser.std.ReferenceTypeDeserializer;
import at.o2xfs.memory.databind.type.JavaType;

public class Jdk8OptionalDeserializer extends ReferenceTypeDeserializer<Optional<?>> {

	public Jdk8OptionalDeserializer(JavaType fullType, MemoryDeserializer<?> deser) {
		super(fullType, deser);
	}

	@Override
	protected Jdk8OptionalDeserializer withResolved(MemoryDeserializer<?> valueDeser) {
		return new Jdk8OptionalDeserializer(fullType, valueDeser);
	}

	@Override
	public Optional<?> referenceValue(Object contents) {
		return Optional.ofNullable(contents);
	}
}
