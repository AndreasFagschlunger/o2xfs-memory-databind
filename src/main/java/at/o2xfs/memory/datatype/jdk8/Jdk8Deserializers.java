package at.o2xfs.memory.datatype.jdk8;

import java.util.Optional;

import at.o2xfs.memory.databind.BeanDescription;
import at.o2xfs.memory.databind.DeserializationConfig;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.deser.Deserializers;
import at.o2xfs.memory.databind.type.ReferenceType;

public class Jdk8Deserializers implements Deserializers {

	@Override
	public MemoryDeserializer<?> findReferenceDeserializer(ReferenceType refType, DeserializationConfig config,
			BeanDescription beanDesc) {
		MemoryDeserializer<?> result = null;
		if (refType.getRawClass() == Optional.class) {
			result = new OptionalDeserializer();
		}
		return result;
	}

}
