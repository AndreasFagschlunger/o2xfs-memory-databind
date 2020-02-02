/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ext.jdk8;

import java.util.Optional;

import at.o2xfs.memory.databind.BeanProperty;
import at.o2xfs.memory.databind.ser.std.ReferenceTypeSerializer;
import at.o2xfs.memory.databind.type.ReferenceType;

public class Jdk8OptionalSerializer extends ReferenceTypeSerializer<Optional<?>> {

	public Jdk8OptionalSerializer(ReferenceType fullType) {
		super(fullType);
	}

	protected Jdk8OptionalSerializer(Jdk8OptionalSerializer base, BeanProperty property) {
		super(base, property);
	}

	@Override
	protected Object getReferencedIfPresent(Optional<?> value) {
		return value.isPresent() ? value.get() : null;
	}

	@Override
	protected ReferenceTypeSerializer<Optional<?>> withResolved(BeanProperty property) {
		return new Jdk8OptionalSerializer(this, property);
	}
}
