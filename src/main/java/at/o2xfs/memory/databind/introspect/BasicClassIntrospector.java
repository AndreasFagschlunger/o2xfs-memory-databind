/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import at.o2xfs.memory.databind.BeanDescription;
import at.o2xfs.memory.databind.cfg.MapperConfig;
import at.o2xfs.memory.databind.type.JavaType;
import at.o2xfs.memory.databind.util.ClassUtil;

public class BasicClassIntrospector extends ClassIntrospector {

	private final static Class<?> CLS_OBJECT = Object.class;
	private final static Class<?> CLS_STRING = String.class;
	private final static Class<?> CLS_NUMBER = Number.class;

	private final static AnnotatedClass OBJECT_AC = new AnnotatedClass(CLS_OBJECT);
	private final static AnnotatedClass STRING_AC = new AnnotatedClass(CLS_STRING);

	private final static AnnotatedClass BOOLEAN_AC = new AnnotatedClass(Boolean.TYPE);
	private final static AnnotatedClass INT_AC = new AnnotatedClass(Integer.TYPE);
	private final static AnnotatedClass LONG_AC = new AnnotatedClass(Long.TYPE);

	private final static AnnotatedClass NUMBER_AC = new AnnotatedClass(CLS_NUMBER);

	private final MapperConfig<?> config;

	private Map<JavaType, AnnotatedClass> resolvedFullAnnotations;

	private Map<JavaType, BasicBeanDescription> resolvedSerBeanDescs;
	private Map<JavaType, BasicBeanDescription> resolvedDeserBeanDescs;

	public BasicClassIntrospector() {
		config = null;
	}

	public BasicClassIntrospector(MapperConfig<?> config) {
		this.config = Objects.requireNonNull(config);
	}

	protected POJOPropertiesCollector collectProperties(JavaType type, AnnotatedClass ac, boolean forSerialization,
			String mutatorPrefix) {
		return constructPropertyCollector(type, ac, forSerialization, mutatorPrefix);
	}

	protected POJOPropertiesCollector collectPropertiesWithBuilder(JavaType type, AnnotatedClass ac,
			boolean forSerialization) {
		return constructPropertyCollector(type, ac, forSerialization, "");
	}

	private POJOPropertiesCollector constructPropertyCollector(JavaType type, AnnotatedClass ac,
			boolean forSerialization, String mutatorPrefix) {
		return new POJOPropertiesCollector(config, type, ac, mutatorPrefix);
	}

	private BasicBeanDescription findStdJdkCollectionDesc(JavaType type) {
		if (isStdJDKCollection(type)) {
			return BasicBeanDescription.forOtherUse(config, type, introspectClassAnnotations(type));
		}
		return null;
	}

	private AnnotatedClass findStdTypeDef(JavaType type) {
		Class<?> rawType = type.getRawClass();
		if (rawType.isPrimitive()) {
			if (rawType == Integer.TYPE) {
				return INT_AC;
			}
			if (rawType == Long.TYPE) {
				return LONG_AC;
			}
			if (rawType == Boolean.TYPE) {
				return BOOLEAN_AC;
			}
		} else if (ClassUtil.isJDKClass(rawType)) {
			if (rawType == CLS_STRING) {
				return STRING_AC;
			}
			// Should be ok to just pass "primitive" info
			if (rawType == Integer.class) {
				return INT_AC;
			}
			if (rawType == Long.class) {
				return LONG_AC;
			}
			if (rawType == Boolean.class) {
				return BOOLEAN_AC;
			}

			if (rawType == CLS_OBJECT) {
				return OBJECT_AC;
			}
			// This mostly matters for "untyped" deserialization
			if (rawType == CLS_NUMBER) {
				return NUMBER_AC;
			}
		}
		return null;
	}

	private BasicBeanDescription findStdTypeDesc(JavaType type) {
		AnnotatedClass ac = findStdTypeDef(type);
		return ac == null ? null : BasicBeanDescription.forOtherUse(config, type, ac);
	}

	private boolean isStdJDKCollection(JavaType type) {
		if (!type.isContainerType() || !type.isArrayType()) {
			return false;
		}
		Class<?> raw = type.getRawClass();
		if (ClassUtil.isJDKClass(raw)) {
			if (Collection.class.isAssignableFrom(raw) || Map.class.isAssignableFrom(raw)) {
				return true;
			}
		}
		return false;
	}

	private AnnotatedClass resolveAnnotatedClass(JavaType type) {
		return AnnotatedClassResolver.resolve(config, type);
	}

	@Override
	public ClassIntrospector forMapper() {
		return this;
	}

	@Override
	public ClassIntrospector forOperation(MapperConfig<?> config) {
		return new BasicClassIntrospector(config);
	}

	@Override
	public AnnotatedClass introspectClassAnnotations(JavaType type) {
		AnnotatedClass ac = findStdTypeDef(type);
		if (ac != null) {
			return ac;
		}
		if (resolvedFullAnnotations == null) {
			resolvedFullAnnotations = new HashMap<>();
		} else {
			ac = resolvedFullAnnotations.get(type);
			if (ac != null) {
				return ac;
			}
		}
		ac = resolveAnnotatedClass(type);
		resolvedFullAnnotations.put(type, ac);
		return ac;
	}

	@Override
	public BeanDescription introspectForDeserialization(JavaType type) {
		BasicBeanDescription desc = findStdTypeDesc(type);
		if (desc == null) {
			desc = findStdJdkCollectionDesc(type);
			if (desc == null) {
				if (resolvedDeserBeanDescs == null) {
					resolvedDeserBeanDescs = new HashMap<>();
				} else {
					desc = resolvedDeserBeanDescs.get(type);
					if (desc != null) {
						return desc;
					}
				}
				desc = BasicBeanDescription
						.forDeserialization(collectProperties(type, introspectClassAnnotations(type), false, "set"));
				resolvedDeserBeanDescs.put(type, desc);
			}
		}
		return desc;
	}

	@Override
	public BeanDescription introspectForDeserializationWithBuilder(JavaType type) {
		return BasicBeanDescription
				.forDeserialization(collectPropertiesWithBuilder(type, introspectClassAnnotations(type), false));
	}

	@Override
	public BasicBeanDescription introspectForSerialization(JavaType type) {
		BasicBeanDescription desc = findStdTypeDesc(type);
		if (desc == null) {
			desc = findStdJdkCollectionDesc(type);
			if (desc == null) {
				if (resolvedSerBeanDescs == null) {
					resolvedSerBeanDescs = new HashMap<>();
				} else {
					desc = resolvedSerBeanDescs.get(type);
					if (desc != null) {
						return desc;
					}
				}
				desc = BasicBeanDescription
						.forSerialization(collectProperties(type, introspectClassAnnotations(type), true, "set"));
				resolvedSerBeanDescs.put(type, desc);
			}
		}
		return desc;
	}

}
