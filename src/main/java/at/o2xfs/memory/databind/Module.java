/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind;

import at.o2xfs.memory.databind.deser.Deserializers;
import at.o2xfs.memory.databind.ser.Serializers;

public abstract class Module {

	public static interface SetupContext {
		void addDeserializers(Deserializers d);

		void addSerializers(Serializers s);
	}

	public abstract void setupModule(SetupContext context);
}
