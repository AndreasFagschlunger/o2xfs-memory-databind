/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.o2xfs.memory.databind.AnnotationIntrospector;
import at.o2xfs.memory.databind.PropertyName;
import at.o2xfs.memory.databind.cfg.MapperConfig;
import at.o2xfs.memory.databind.type.JavaType;
import at.o2xfs.memory.databind.util.BeanUtil;

public class POJOPropertiesCollector {

	private static final Logger LOG = LogManager.getLogger(POJOPropertiesCollector.class);

	private final MapperConfig<?> config;
	private final JavaType type;
	private final AnnotatedClass classDef;
	private final AnnotationIntrospector annotationIntrospector;
	private final String mutatorPrefix;
	private Map<String, POJOPropertyBuilder> properties;

	public POJOPropertiesCollector(MapperConfig<?> config, JavaType type, AnnotatedClass classDef,
			String mutatorPrefix) {
		this.config = Objects.requireNonNull(config);
		this.type = type;
		this.classDef = classDef;
		this.mutatorPrefix = Objects.requireNonNull(mutatorPrefix);
		annotationIntrospector = config.getAnnotationIntrospector();
	}

	private void collectAll() {
		properties = new LinkedHashMap<>();
		addFields(properties);
		addMethods(properties);
		for (POJOPropertyBuilder each : properties.values()) {
			each.mergeAnnotations();
		}
		sortProperties(properties);
	}

	private void addGetterMethod(Map<String, POJOPropertyBuilder> props, AnnotatedMethod m) {
		if (m.hasReturnType()) {
			String implName = BeanUtil.okNameForGetter(m);
			if (implName == null) {
				implName = BeanUtil.okNameForIsGetter(m);
			}
			if (implName != null) {
				property(props, implName).setGetter(m);
			}
		}
	}

	private void addSetterMethod(Map<String, POJOPropertyBuilder> props, AnnotatedMethod m) {
		String implName = BeanUtil.okNameForMutator(m, mutatorPrefix);
		if (implName != null) {
			property(props, implName).addSetter(m);
		}
	}

	private POJOPropertyBuilder property(Map<String, POJOPropertyBuilder> props, String implName) {
		POJOPropertyBuilder result = props.get(implName);
		if (result == null) {
			result = new POJOPropertyBuilder(new PropertyName(implName));
			props.put(result.getName(), result);
		}
		return result;
	}

	private void sortProperties(Map<String, POJOPropertyBuilder> props) {
		List<String> propertyOrder = annotationIntrospector.findSerializationPropertyOrder(config, classDef);
		if (propertyOrder.isEmpty()) {
			return;
		}
		Map<String, POJOPropertyBuilder> ordered = new LinkedHashMap<>(propertyOrder.size());
		for (String each : propertyOrder) {
			ordered.put(each, props.remove(each));
		}
		props.clear();
		props.putAll(ordered);
	}

	public MapperConfig<?> getConfig() {
		return config;
	}

	public JavaType getType() {
		return type;
	}

	public AnnotatedClass getClassDef() {
		return classDef;
	}

	public List<BeanPropertyDefinition> getProperties() {
		if (properties == null) {
			collectAll();
		}
		return new ArrayList<>(properties.values());
	}

	private void addFields(Map<String, POJOPropertyBuilder> props) {
		for (AnnotatedField field : classDef.fields()) {
			LOG.debug("addFields: {}", field);
			property(props, field.getName()).setField(field);
		}
	}

	private void addMethods(Map<String, POJOPropertyBuilder> props) {
		for (AnnotatedMethod method : classDef.memberMethods()) {
			LOG.debug("addMethods: {}", method);
			int parameterCount = method.getParameterCount();
			if (parameterCount == 0) {
				addGetterMethod(props, method);
			} else if (parameterCount == 1) {
				addSetterMethod(props, method);
			}
		}
	}
}
