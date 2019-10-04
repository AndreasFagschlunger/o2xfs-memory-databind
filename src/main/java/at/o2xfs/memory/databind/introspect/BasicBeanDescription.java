/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import at.o2xfs.memory.databind.AnnotationIntrospector;
import at.o2xfs.memory.databind.BeanDescription;
import at.o2xfs.memory.databind.cfg.MapperConfig;

public class BasicBeanDescription extends BeanDescription {

	private final POJOPropertiesCollector coll;
	private final MapperConfig config;
	private final AnnotationIntrospector annotationIntrospector;
	private final AnnotatedClass classInfo;
	private List<BeanPropertyDefinition> properties;

	private BasicBeanDescription(POJOPropertiesCollector coll) {
		super(coll.getType());
		this.coll = Objects.requireNonNull(coll);
		config = coll.getConfig();
		annotationIntrospector = config.getAnnotationIntrospector();
		classInfo = coll.getClassDef();
	}

	public static BasicBeanDescription forDeserialization(POJOPropertiesCollector coll) {
		return new BasicBeanDescription(coll);
	}

	@Override
	public AnnotatedConstructor findDefaultConstructor() {
		return classInfo.getDefaultConstructor();
	}

	@Override
	public AnnotatedMethod findMethod(String name) {
		return classInfo.findMethod(name);
	}

	@Override
	public List<BeanPropertyDefinition> findProperties() {
		if (properties == null) {
			properties = coll.getProperties();
		}
		return properties;
	}

	@Override
	public Class<?> findPOJOBuilder() {
		Class<?> result = annotationIntrospector.findPOJOBuilder();
		if (result == null) {
			Class<?>[] classes = classInfo.getRawType().getDeclaredClasses();
			for (Class<?> each : classes) {
				if ("Builder".equals(each.getSimpleName())) {
					result = each;
					break;
				}
			}
		}
		return result;
	}

	@Override
	public AnnotatedClass getClassInfo() {
		return classInfo;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
