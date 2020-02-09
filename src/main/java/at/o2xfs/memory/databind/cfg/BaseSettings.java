/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.cfg;

import at.o2xfs.memory.databind.AnnotationIntrospector;

public final class BaseSettings {

	protected final AnnotationIntrospector annotationIntrospector;

	public BaseSettings(AnnotationIntrospector annotationIntrospector) {
		this.annotationIntrospector = annotationIntrospector;
	}

	public AnnotationIntrospector getAnnotationIntrospector() {
		return annotationIntrospector;
	}
}
