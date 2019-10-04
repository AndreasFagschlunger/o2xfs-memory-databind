/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.cfg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.o2xfs.memory.databind.AnnotationIntrospector;
import at.o2xfs.memory.databind.deser.Deserializers;
import at.o2xfs.memory.databind.introspect.ClassIntrospector;
import at.o2xfs.memory.databind.type.TypeFactory;

public class DeserializerFactoryConfig extends MapperConfig {

	private final List<Deserializers> additionalDeserializers;

	public DeserializerFactoryConfig() {
		this(null, null, null, Collections.emptyList());
	}

	public DeserializerFactoryConfig(TypeFactory typeFactory, ClassIntrospector classIntrospector,
			AnnotationIntrospector annotationIntrospector, List<Deserializers> additionalDeserializers) {
		super(typeFactory, classIntrospector, annotationIntrospector);
		this.additionalDeserializers = additionalDeserializers;
	}

	public Iterable<Deserializers> deserializers() {
		return additionalDeserializers;
	}

	public DeserializerFactoryConfig withAdditionalDeserializers(Deserializers additional) {
		List<Deserializers> newList = new ArrayList<>(additionalDeserializers);
		newList.add(additional);
		return new DeserializerFactoryConfig(typeFactory, classIntrospector, annotationIntrospector, newList);
	}

}
