/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import at.o2xfs.memory.databind.PropertyName;

public abstract class BeanPropertyDefinition {

	public AnnotatedMember getAccessor() {
		AnnotatedMember result = getGetter();
		if (result == null) {
			result = getField();
		}
		return result;
	}

	public abstract String getName();

	public abstract PropertyName getFullName();

	public abstract AnnotatedMethod getGetter();

	public abstract AnnotatedMethod getSetter();

	public abstract AnnotatedField getField();

	public abstract boolean hasSetter();

	public AnnotatedMember getNonConstructorMutator() {
		AnnotatedMember result = getSetter();
		if (result == null) {
			result = getField();
		}
		return result;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
