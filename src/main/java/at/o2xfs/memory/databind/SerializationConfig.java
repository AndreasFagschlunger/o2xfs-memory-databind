/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind;

import at.o2xfs.memory.databind.cfg.MapperBuilder;
import at.o2xfs.memory.databind.cfg.MapperConfigBase;
import at.o2xfs.memory.databind.introspect.ClassIntrospector;
import at.o2xfs.memory.databind.type.TypeFactory;

public class SerializationConfig extends MapperConfigBase<SerializationConfig> {

	public SerializationConfig(MapperBuilder b, TypeFactory typeFactory, ClassIntrospector classIntrospector) {
		super(b, typeFactory, classIntrospector);
	}
}
