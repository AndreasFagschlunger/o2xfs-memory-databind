/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import java.util.HashMap;
import java.util.Map;

import at.o2xfs.memory.databind.BeanDescription;
import at.o2xfs.memory.databind.DeserializationConfig;
import at.o2xfs.memory.databind.SerializationConfig;
import at.o2xfs.memory.databind.cfg.MapperConfig;
import at.o2xfs.memory.databind.type.JavaType;

public class BasicClassIntrospector extends ClassIntrospector {

	private final Map<JavaType, BeanDescription> cache;

	public BasicClassIntrospector() {
		cache = new HashMap<>();
	}

	private POJOPropertiesCollector collectProperties(MapperConfig config, JavaType type, boolean forSerialization,
			String mutatorPrefix) {
		return constructPropertyCollector(config, resolveAnnotatedClass(config, type), type, forSerialization,
				mutatorPrefix);
	}

	private POJOPropertiesCollector collectPropertiesWithBuilder(MapperConfig config, JavaType type,
			boolean forSerialization) {
		AnnotatedClass ac = resolveAnnotatedClass(config, type);
		return constructPropertyCollector(config, ac, type, forSerialization, "");
	}

	private POJOPropertiesCollector constructPropertyCollector(MapperConfig config, AnnotatedClass ac, JavaType type,
			boolean forSerialization, String mutatorPrefix) {
		return new POJOPropertiesCollector(config, type, ac, mutatorPrefix);
	}

	private AnnotatedClass resolveAnnotatedClass(MapperConfig config, JavaType type) {
		return AnnotatedClassResolver.resolve(config, type);
	}

	@Override
	public BeanDescription forDeserialization(DeserializationConfig cfg, JavaType type) {
		BeanDescription result = BasicBeanDescription.forDeserialization(collectProperties(cfg, type, false, "set"));
		cache.put(type, result);
		return result;
	}

	@Override
	public BeanDescription forDeserializationWithBuilder(DeserializationConfig cfg, JavaType type) {
		BeanDescription result = BasicBeanDescription
				.forDeserialization(collectPropertiesWithBuilder(cfg, type, false));
		cache.put(type, result);
		return result;
	}

	@Override
	public BeanDescription forSerialization(SerializationConfig cfg, JavaType type) {
		BeanDescription result = BasicBeanDescription.forDeserialization(collectProperties(cfg, type, true, "set"));
		cache.put(type, result);
		return result;
	}
}
