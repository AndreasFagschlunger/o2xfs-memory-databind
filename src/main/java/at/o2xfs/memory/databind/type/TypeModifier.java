/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.type;

import java.lang.reflect.Type;

public abstract class TypeModifier {

	public abstract JavaType modifyType(JavaType type, Type jdkType, TypeBindings context, TypeFactory typeFactory);
}
