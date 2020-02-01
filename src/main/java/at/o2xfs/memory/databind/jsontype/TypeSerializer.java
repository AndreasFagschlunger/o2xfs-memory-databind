/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.jsontype;

import at.o2xfs.memory.databind.BeanProperty;
import at.o2xfs.memory.databind.SerializerProvider;

public abstract class TypeSerializer {

	public abstract TypeSerializer forProperty(SerializerProvider ctxt, BeanProperty prop);

}
