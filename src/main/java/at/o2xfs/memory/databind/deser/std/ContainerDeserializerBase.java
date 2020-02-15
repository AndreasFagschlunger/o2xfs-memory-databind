/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser.std;

import at.o2xfs.memory.databind.type.JavaType;

public abstract class ContainerDeserializerBase<T> extends StdDeserializer<T> {

	protected final JavaType containerType;

	protected ContainerDeserializerBase(ContainerDeserializerBase<?> base) {
		super(base.containerType);
		containerType = base.containerType;
	}

	public ContainerDeserializerBase(JavaType valueType) {
		super(valueType);
		containerType = valueType;
	}
}
