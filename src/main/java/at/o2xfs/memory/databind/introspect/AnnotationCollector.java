/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AnnotationCollector {

	static class EmptyCollector extends AnnotationCollector {

		public static final EmptyCollector instance = new EmptyCollector();

		@Override
		public Map<Class<?>, Annotation> asAnnotationMap() {
			return Collections.emptyMap();
		}

		@Override
		public boolean isPresent(Annotation ann) {
			return false;
		}

		@Override
		public AnnotationCollector addOrOverride(Annotation ann) {
			return new OneCollector(ann.annotationType(), ann);
		}

	}

	static class OneCollector extends AnnotationCollector {

		private Class<?> type;
		private Annotation value;

		public OneCollector(Class<?> type, Annotation value) {
			this.type = type;
			this.value = value;
		}

		@Override
		public Map<Class<?>, Annotation> asAnnotationMap() {
			return Collections.singletonMap(type, value);
		}

		@Override
		public boolean isPresent(Annotation ann) {
			return type == ann.annotationType();
		}

		@Override
		public AnnotationCollector addOrOverride(Annotation ann) {
			if (type == ann.annotationType()) {
				value = ann;
				return this;
			}
			return new NCollector(type, value, ann.annotationType(), ann);
		}
	}

	static class NCollector extends AnnotationCollector {

		private final Map<Class<?>, Annotation> annotations;

		public NCollector(Class<?> type1, Annotation value1, Class<?> type2, Annotation value2) {
			annotations = new HashMap<>();
			annotations.put(type1, value1);
			annotations.put(type2, value2);
		}

		@Override
		public Map<Class<?>, Annotation> asAnnotationMap() {
			return annotations;
		}

		@Override
		public boolean isPresent(Annotation ann) {
			return annotations.containsKey(ann.annotationType());
		}

		@Override
		public AnnotationCollector addOrOverride(Annotation ann) {
			annotations.put(ann.annotationType(), ann);
			return this;
		}
	}

	public abstract Map<Class<?>, Annotation> asAnnotationMap();

	public abstract boolean isPresent(Annotation ann);

	public abstract AnnotationCollector addOrOverride(Annotation ann);

	public static AnnotationCollector emptyCollector() {
		return EmptyCollector.instance;
	}
}
