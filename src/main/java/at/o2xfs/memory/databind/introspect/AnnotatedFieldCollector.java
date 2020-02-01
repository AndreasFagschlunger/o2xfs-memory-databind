/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import at.o2xfs.memory.databind.cfg.MapperConfig;
import at.o2xfs.memory.databind.type.JavaType;
import at.o2xfs.memory.databind.util.ClassUtil;

public class AnnotatedFieldCollector extends CollectorBase {

	private final static class FieldBuilder {

		private final TypeResolutionContext typeContext;
		private final Field field;
		private AnnotationCollector annotations;

		public FieldBuilder(TypeResolutionContext typeContext, Field field) {
			this.typeContext = typeContext;
			this.field = field;
			annotations = AnnotationCollector.emptyCollector();
		}

		public AnnotatedField build() {
			return new AnnotatedField(typeContext, field, annotations.asAnnotationMap());
		}
	}

	AnnotatedFieldCollector(MapperConfig<?> config) {
		super(config);
	}

	private Map<String, FieldBuilder> findFields(TypeResolutionContext tc, JavaType type,
			Map<String, FieldBuilder> fields) {
		JavaType parent = type.getSuperClass();
		if (parent == null) {
			return fields;
		}
		final Class<?> cls = type.getRawClass();
		fields = findFields(tc, parent, fields);
		for (Field f : ClassUtil.getDeclaredFields(cls)) {
			if (!isIncludableField(f)) {
				continue;
			}
			if (fields == null) {
				fields = new LinkedHashMap<>();
			}
			FieldBuilder builder = new FieldBuilder(tc, f);
			builder.annotations = collectAnnotations(builder.annotations, f.getDeclaredAnnotations());
			fields.put(f.getName(), builder);
		}
		return fields;
	}

	private boolean isIncludableField(Field f) {
		return !(f.isSynthetic() || Modifier.isStatic(f.getModifiers()));
	}

	List<AnnotatedField> collect(TypeResolutionContext tc, JavaType type) {
		List<AnnotatedField> result = Collections.emptyList();
		Map<String, FieldBuilder> fields = findFields(tc, type, null);
		if (fields != null) {
			result = fields.values().stream().map(e -> e.build()).collect(Collectors.toList());
		}
		return result;
	}

	public static List<AnnotatedField> collectFields(MapperConfig<?> config, TypeResolutionContext tc, JavaType type) {
		return new AnnotatedFieldCollector(config).collect(tc, type);
	}
}
