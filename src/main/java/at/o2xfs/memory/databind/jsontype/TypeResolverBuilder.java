/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.jsontype;

import java.util.Collection;

import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.type.JavaType;

public interface TypeResolverBuilder<T extends TypeResolverBuilder<T>> {

	TypeSerializer buildTypeSerializer(SerializerProvider ctxt, JavaType baseType, Collection<NamedType> subtypes);
}
