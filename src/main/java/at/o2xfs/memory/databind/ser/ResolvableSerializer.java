/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser;

import at.o2xfs.memory.databind.SerializerProvider;

public interface ResolvableSerializer {

	void resolve(SerializerProvider provider);
}
