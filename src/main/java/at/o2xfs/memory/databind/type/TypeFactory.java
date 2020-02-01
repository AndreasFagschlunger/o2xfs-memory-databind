/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.type;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import at.o2xfs.memory.datatype.jdk8.Jdk8TypeModifier;

public final class TypeFactory {

	private static final Class<?> CLS_COMPARABLE = Comparable.class;
	private static final Class<?> CLS_ENUM = Enum.class;

	private static final SimpleType BOOLEAN_TYPE = new SimpleType(Boolean.TYPE);
	private static final SimpleType INT_TYPE = new SimpleType(Integer.TYPE);
	private static final SimpleType LONG_TYPE = new SimpleType(Long.TYPE);
	private static final SimpleType STRING_TYPE = new SimpleType(String.class);
	private static final SimpleType OBJECT_TYPE = new SimpleType(Object.class);
	private static final SimpleType CORE_TYPE_COMPARABLE = new SimpleType(CLS_COMPARABLE);
	private static final SimpleType CORE_TYPE_ENUM = new SimpleType(CLS_ENUM);

	private static final TypeFactory instance = new TypeFactory();

	private final TypeModifier[] modifiers;

	private TypeFactory() {
		this.modifiers = new TypeModifier[] { new Jdk8TypeModifier() };
	}

	private TypeBindings bindingsForSubtype(JavaType baseType, int typeParamCount, Class<?> subclass) {
		PlaceholderForType[] placeholders = new PlaceholderForType[typeParamCount];
		for (int i = 0; i < placeholders.length; i++) {
			placeholders[i] = new PlaceholderForType(i);
		}
		TypeBindings b = TypeBindings.create(subclass, placeholders);
		JavaType tmpSub = fromClass(subclass, b);
		JavaType baseWithPlaceholders = tmpSub.findSuperType(baseType.getRawClass());
		resolveTypePlaceholders(baseType, baseWithPlaceholders);
		JavaType[] typeParams = new JavaType[typeParamCount];
		for (int i = 0; i < typeParams.length; i++) {
			typeParams[i] = placeholders[i].actualType();
		}
		return TypeBindings.create(subclass, typeParams);
	}

	private JavaType collectionType(Class<?> rawType, TypeBindings bindings, JavaType superClass,
			JavaType[] superInterfaces) {
		List<JavaType> typeParams = bindings.getTypeParameters();
		JavaType elementType = typeParams.get(0);
		return CollectionType.construct(rawType, bindings, superClass, superInterfaces, elementType);
	}

	private JavaType fromArrayType(GenericArrayType type, TypeBindings bindings) {
		JavaType elementType = fromAny(type.getGenericComponentType(), bindings);
		return ArrayType.construct(elementType, bindings);
	}

	public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
		JavaType result = null;
		final Class<?> rawBase = baseType.getRawClass();
		if (rawBase == subclass) {
			return baseType;
		}
		do {
			if (baseType.isContainerType()) {
				if (baseType.isCollectionLikeType()) {
					if (subclass == ArrayList.class) {
						result = fromClass(subclass,
								TypeBindings.create(subclass, new JavaType[] { baseType.getContentType() }));
						break;
					}
				}
			}
			TypeBindings tb = bindingsForSubtype(baseType, subclass.getTypeParameters().length, subclass);
			result = fromClass(subclass, tb);
		} while (false);
		return result;
	}

	public JavaType constructType(Type type) {
		return fromAny(type, TypeBindings.emptyBindings());
	}

	public JavaType constructType(Type type, TypeBindings bindings) {
		return fromAny(type, bindings);
	}

	private JavaType findWellKnownSimple(Class<?> cls) {
		JavaType result = null;
		if (cls.isPrimitive()) {
			if (cls == Boolean.TYPE) {
				result = BOOLEAN_TYPE;
			} else if (cls == Integer.TYPE) {
				result = INT_TYPE;
			} else if (cls == Long.TYPE) {
				result = LONG_TYPE;
			}
		} else if (cls == String.class) {
			result = STRING_TYPE;
		} else if (cls == Object.class) {
			result = OBJECT_TYPE;
		}
		return result;
	}

	private JavaType fromAny(Type type, TypeBindings bindings) {
		JavaType result;
		if (type instanceof Class<?>) {
			result = fromClass((Class<?>) type, bindings);
		} else if (type instanceof ParameterizedType) {
			result = fromParamType((ParameterizedType) type, bindings);
		} else if (type instanceof GenericArrayType) {
			result = fromArrayType((GenericArrayType) type, bindings);
		} else if (type instanceof TypeVariable) {
			result = fromVariable((TypeVariable<?>) type, bindings);
		} else if (type instanceof WildcardType) {
			result = fromWildcard((WildcardType) type, bindings);
		} else {
			throw new IllegalArgumentException(type.toString());
		}
		for (TypeModifier each : modifiers) {
			result = each.modifyType(result, type, result.getBindings(), this);
		}
		return result;
	}

	private JavaType fromParamType(ParameterizedType pType, TypeBindings parentBindings) {
		Class<?> rawType = (Class<?>) pType.getRawType();
		if (rawType == CLS_ENUM) {
			return CORE_TYPE_ENUM;
		} else if (rawType == CLS_COMPARABLE) {
			return CORE_TYPE_COMPARABLE;
		}
		TypeBindings newBindings;
		Type[] args = pType.getActualTypeArguments();
		if (args.length == 0) {
			newBindings = TypeBindings.emptyBindings();
		} else {
			JavaType[] types = new JavaType[args.length];
			for (int i = 0; i < types.length; i++) {
				types[i] = fromAny(args[i], parentBindings);
			}
			newBindings = TypeBindings.create((Class<?>) pType.getRawType(), types);
		}
		return fromClass((Class<?>) pType.getRawType(), newBindings);
	}

	private JavaType fromVariable(TypeVariable<?> var, TypeBindings bindings) {
		String name = var.getName();
		JavaType type = bindings.findBoundType(name);
		if (type != null) {
			return type;
		}
		return fromAny(var.getBounds()[0], bindings);
	}

	private JavaType fromWildcard(WildcardType type, TypeBindings bindings) {
		return fromAny(type.getUpperBounds()[0], bindings);
	}

	private JavaType fromClass(Class<?> rawType, TypeBindings bindings) {
		JavaType result = findWellKnownSimple(rawType);
		if (result == null) {
			if (rawType.isArray()) {
				result = ArrayType.construct(fromAny(rawType.getComponentType(), bindings), bindings);
			} else {
				JavaType superClass;
				JavaType[] superInterfaces = resolveSuperInterfaces(rawType, bindings);
				if (rawType.isInterface()) {
					superClass = null;
				} else {
					superClass = resolveSuperClass(rawType, bindings);
				}
				if (superClass != null) {
					result = superClass.refine(rawType, bindings, superClass, superInterfaces);
				}
				if (result == null) {
					result = fromWellKnownClass(rawType, bindings, superClass, superInterfaces);
					if (result == null) {
						result = fromWellKnownInterface(rawType, bindings, superClass, superInterfaces);
						if (result == null) {
							result = new SimpleType(rawType, bindings, superClass, superInterfaces);
						}
					}
				}
			}
		}
		return result;
	}

	private JavaType fromWellKnownClass(Class<?> rawType, TypeBindings bindings, JavaType superClass,
			JavaType[] superInterfaces) {
		JavaType result = null;
		if (rawType == Map.class) {
			result = mapType(rawType, bindings, superClass, superInterfaces);
		} else if (rawType == Collection.class) {
			result = collectionType(rawType, bindings, superClass, superInterfaces);
		}
		return result;
	}

	private JavaType fromWellKnownInterface(Class<?> rawType, TypeBindings bindings, JavaType superClass,
			JavaType[] superInterfaces) {
		JavaType result = null;
		for (JavaType each : superInterfaces) {
			result = each.refine(rawType, bindings, superClass, superInterfaces);
			if (result != null) {
				break;
			}
		}
		return result;
	}

	private JavaType mapType(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces) {
		List<JavaType> typeParameters = bindings.getTypeParameters();
		JavaType keyType = typeParameters.get(0);
		JavaType valueType = typeParameters.get(1);
		return MapType.construct(rawType, bindings, superClass, superInterfaces, keyType, valueType);
	}

	private JavaType resolveSuperClass(Class<?> rawType, TypeBindings bindings) {
		JavaType result = null;
		Type parent = rawType.getGenericSuperclass();
		if (parent != null) {
			result = fromAny(parent, bindings);
		}
		return result;
	}

	private JavaType[] resolveSuperInterfaces(Class<?> rawType, TypeBindings bindings) {
		Type[] types = rawType.getGenericInterfaces();
		JavaType[] result = new JavaType[types.length];
		for (int i = 0; i < types.length; i++) {
			result[i] = fromAny(types[i], bindings);
		}
		return result;
	}

	private void resolveTypePlaceholders(JavaType sourceType, JavaType actualType) {
		List<JavaType> expectedTypes = sourceType.getBindings().getTypeParameters();
		List<JavaType> actualTypes = actualType.getBindings().getTypeParameters();
		for (int i = 0; i < expectedTypes.size(); i++) {
			JavaType expeted = expectedTypes.get(i);
			JavaType actual = actualTypes.get(i);
			if (!verifyAndResolvePlaceholders(expeted, actual)) {

			}
		}
	}

	private boolean verifyAndResolvePlaceholders(JavaType expeted, JavaType actual) {
		if (actual instanceof PlaceholderForType) {
			((PlaceholderForType) actual).actualType(expeted);
			return true;
		}
		return false;
	}

	public static JavaType unknownType() {
		return new SimpleType(Object.class);
	}

	public static TypeFactory defaultInstance() {
		return instance;
	}
}
