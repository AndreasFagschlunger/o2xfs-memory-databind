package at.o2xfs.memory.datatype.jdk8;

import java.util.Optional;

import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.deser.std.ReferenceTypeDeserializer;
import at.o2xfs.memory.databind.type.JavaType;

public class OptionalDeserializer extends ReferenceTypeDeserializer<Optional<?>> {

	public OptionalDeserializer(JavaType fullType, MemoryDeserializer<?> deser) {
		super(fullType, deser);
	}

	@Override
	protected OptionalDeserializer withResolved(MemoryDeserializer<?> valueDeser) {
		return new OptionalDeserializer(fullType, valueDeser);
	}

	@Override
	public Optional<?> referenceValue(Object contents) {
		return Optional.ofNullable(contents);
	}
}
