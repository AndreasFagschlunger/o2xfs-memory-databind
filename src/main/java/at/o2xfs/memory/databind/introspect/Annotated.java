/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import java.lang.annotation.Annotation;

import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class Annotated {

	public abstract <A extends Annotation> A getAnnotation(Class<A> cls);

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
