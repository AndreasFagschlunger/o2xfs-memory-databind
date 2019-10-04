/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import java.lang.annotation.Annotation;
import java.util.Map;

import at.o2xfs.memory.databind.type.JavaType;

public abstract class AnnotatedWithParams extends AnnotatedMember {

	public AnnotatedWithParams(TypeResolutionContext ctxt, Map<Class<?>, Annotation> annotations) {
		super(ctxt, annotations);
	}

	public abstract JavaType getParameterType(int index);

	public abstract Object call() throws Exception;
}
