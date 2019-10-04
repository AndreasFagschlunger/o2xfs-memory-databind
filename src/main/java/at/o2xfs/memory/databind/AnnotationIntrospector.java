/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind;

import java.lang.annotation.Annotation;

import at.o2xfs.memory.databind.annotation.MemoryAnnotationsInside;
import at.o2xfs.memory.databind.annotation.MemoryDeserialize;
import at.o2xfs.memory.databind.annotation.MemorySerialize;
import at.o2xfs.memory.databind.introspect.Annotated;

public class AnnotationIntrospector {

	private <A extends Annotation> A findAnnotation(Annotated annotated, Class<A> cls) {
		return annotated.getAnnotation(cls);
	}

	public Class<?> findPOJOBuilder() {
		return null;
	}

	public Object findDeserializer(Annotated a) {
		MemoryDeserialize ann = findAnnotation(a, MemoryDeserialize.class);
		Object result = null;
		if (ann != null) {
			Class<? extends MemoryDeserializer> deserClass = ann.using();
			if (deserClass != MemoryDeserializer.None.class) {
				result = deserClass;
			}
		}
		return result;
	}

	public Object findSerializer(Annotated a) {
		MemorySerialize ann = findAnnotation(a, MemorySerialize.class);
		if (ann != null && MemorySerializer.None.class != ann.using()) {
			return ann.using();
		}
		return null;
	}

	public boolean isAnnotationBundle(Annotation ann) {
		Class<?> type = ann.annotationType();
		return type.getAnnotation(MemoryAnnotationsInside.class) != null;
	}
}
