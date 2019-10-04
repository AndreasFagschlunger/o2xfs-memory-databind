/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.datatype.jdk8;

import java.lang.reflect.Type;
import java.util.Optional;

import at.o2xfs.memory.databind.type.JavaType;
import at.o2xfs.memory.databind.type.ReferenceType;
import at.o2xfs.memory.databind.type.TypeBindings;
import at.o2xfs.memory.databind.type.TypeFactory;
import at.o2xfs.memory.databind.type.TypeModifier;

public class Jdk8TypeModifier extends TypeModifier {

	@Override
	public JavaType modifyType(JavaType type, Type jdkType, TypeBindings context, TypeFactory typeFactory) {
		final Class<?> raw = type.getRawClass();
		JavaType refType;
		if (raw == Optional.class) {
			refType = type.containedTypeOrUnknown(0);
		} else {
			return type;
		}
		return ReferenceType.upgradeFrom(type, refType);
	}

}
