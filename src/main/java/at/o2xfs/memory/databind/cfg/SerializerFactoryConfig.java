/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.cfg;

import java.util.HashSet;
import java.util.Set;

import at.o2xfs.memory.databind.ser.Serializers;

public class SerializerFactoryConfig {

	private final Set<Serializers> serializers;

	public SerializerFactoryConfig(Set<Serializers> serializers) {
		this.serializers = serializers;
	}

	public Set<Serializers> serializers() {
		return serializers;
	}

	public SerializerFactoryConfig withAdditionalSerializers(Serializers additional) {
		Set<Serializers> newSet = new HashSet<>(serializers);
		newSet.add(additional);
		return new SerializerFactoryConfig(newSet);
	}
}
