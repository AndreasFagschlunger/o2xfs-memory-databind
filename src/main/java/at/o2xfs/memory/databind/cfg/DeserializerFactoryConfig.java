/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.cfg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.o2xfs.memory.databind.deser.Deserializers;

public class DeserializerFactoryConfig {

	private final List<Deserializers> additionalDeserializers;

	public DeserializerFactoryConfig() {
		this(Collections.emptyList());
	}

	protected DeserializerFactoryConfig(List<Deserializers> additionalDeserializers) {
		this.additionalDeserializers = additionalDeserializers;
	}

	public Iterable<Deserializers> deserializers() {
		return additionalDeserializers;
	}

	public DeserializerFactoryConfig withAdditionalDeserializers(Deserializers additional) {
		List<Deserializers> newList = new ArrayList<>(additionalDeserializers);
		newList.add(additional);
		return new DeserializerFactoryConfig(newList);
	}

}
