/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser;

import at.o2xfs.memory.databind.DeserializationContext;

public abstract class ValueInstantiator {

	public abstract Object createUsingDefault(DeserializationContext ctxt);
}
