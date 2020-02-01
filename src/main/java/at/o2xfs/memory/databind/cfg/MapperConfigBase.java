/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.cfg;

import at.o2xfs.memory.databind.introspect.ClassIntrospector;
import at.o2xfs.memory.databind.jsontype.TypeResolverProvider;
import at.o2xfs.memory.databind.type.JavaType;
import at.o2xfs.memory.databind.type.TypeFactory;

public abstract class MapperConfigBase<T extends MapperConfigBase<T>> extends MapperConfig<T> {

	protected final TypeFactory typeFactory;
	protected final ClassIntrospector classIntrospector;
	protected final TypeResolverProvider typeResolverProvider;

	public MapperConfigBase(MapperBuilder b, TypeFactory tf, ClassIntrospector classIntr) {
		super(b.baseSettings());
		typeFactory = tf;
		classIntrospector = classIntr;
		typeResolverProvider = b.typeResolverProvider();
	}

	@Override
	public final JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
		return typeFactory.constructSpecializedType(baseType, subclass);
	}

	@Override
	public final JavaType constructType(Class<?> cls) {
		return typeFactory.constructType(cls);
	}

	public TypeResolverProvider getTypeResolverProvider() {
		return typeResolverProvider;
	}

	@Override
	public ClassIntrospector classIntrospectorInstance() {
		return classIntrospector.forOperation(this);
	}

	@Override
	public TypeFactory getTypeFactory() {
		return typeFactory;
	}
}
