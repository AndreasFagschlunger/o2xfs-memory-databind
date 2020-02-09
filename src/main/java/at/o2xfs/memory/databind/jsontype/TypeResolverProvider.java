/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.jsontype;

import java.util.Collection;

import at.o2xfs.memory.databind.SerializationConfig;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.cfg.MapperConfig;
import at.o2xfs.memory.databind.introspect.Annotated;
import at.o2xfs.memory.databind.introspect.AnnotatedClass;
import at.o2xfs.memory.databind.introspect.AnnotatedMember;
import at.o2xfs.memory.databind.type.JavaType;

public class TypeResolverProvider {

	private TypeResolverBuilder<?> findTypeResolver(MapperConfig config, Annotated ann, JavaType baseType) {
		return null;
	}

	public TypeSerializer findTypeSerializer(SerializerProvider ctxt, JavaType baseType, AnnotatedClass classInfo) {
		SerializationConfig config = ctxt.getConfig();
		TypeResolverBuilder<?> b = findTypeResolver(config, classInfo, baseType);
		Collection<NamedType> subtypes = null;
		if (b == null) {
			return null;
		}
		return b.buildTypeSerializer(ctxt, baseType, subtypes);

	}

	public TypeSerializer findPropertyTypeSerializer(SerializerProvider ctxt, AnnotatedMember accessor,
			JavaType baseType) {
		return findTypeSerializer(ctxt, baseType, ctxt.introspectClassAnnotations(baseType));
	}
}
