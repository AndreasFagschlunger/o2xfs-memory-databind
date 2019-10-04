/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import at.o2xfs.memory.databind.PropertyName;

public class POJOPropertyBuilder extends BeanPropertyDefinition {

	private final PropertyName name;

	private AnnotatedField field;
	private AnnotatedMethod getter;
	private List<AnnotatedMethod> setters;

	public POJOPropertyBuilder(PropertyName name) {
		this.name = Objects.requireNonNull(name);
		this.field = null;
		this.getter = null;
		setters = new ArrayList<AnnotatedMethod>();
	}

	public void addSetter(AnnotatedMethod m) {
		setters.add(m);
	}

	public void mergeAnnotations() {
		Map<Class<?>, Annotation> annotations = merge(field, getter);
		if (getter != null) {
			getter = getter.withAnnotations(annotations);
		} else if (field != null) {
			field = field.withAnnotations(annotations);
		}
	}

	private Map<Class<?>, Annotation> merge(AnnotatedMember... members) {
		Map<Class<?>, Annotation> result = new HashMap<Class<?>, Annotation>();
		for (AnnotatedMember each : members) {
			if (each != null) {
				result.putAll(each.getAnnotations());
			}
		}
		return result;
	}

	protected int setterPriority(AnnotatedMethod m) {
		final String name = m.getName();
		if (name.startsWith("set") && name.length() > 3) {
			// should we check capitalization?
			return 1;
		}
		return 2;
	}

	@Override
	public PropertyName getFullName() {
		return name;
	}

	@Override
	public boolean hasSetter() {
		return !setters.isEmpty();
	}

	@Override
	public AnnotatedMethod getSetter() {
		Iterator<AnnotatedMethod> it = setters.iterator();
		AnnotatedMethod curr = it.next();
		if (!it.hasNext()) {
			return curr;
		}

		for (; it.hasNext();) {
			AnnotatedMethod next = it.next();
			Class<?> currClass = curr.getDeclaringClass();
			Class<?> nextClass = next.getDeclaringClass();
			if (currClass != nextClass) {
				if (currClass.isAssignableFrom(nextClass)) {
					curr = next;
					continue;
				}
				if (nextClass.isAssignableFrom(currClass)) {
					continue;
				}
			}
			AnnotatedMethod nextM = next;
			AnnotatedMethod currM = curr;

			int priNext = setterPriority(nextM);
			int priCurr = setterPriority(currM);

			if (priNext != priCurr) {
				if (priNext < priCurr) {
					curr = next;
				}
				continue;
			}
			throw new IllegalArgumentException(String
					.format("Conflicting setter definitions for property \"%s\": %s vs %s", getName(),
							curr.getFullName(), next.getFullName()));
		}
		setters = Collections.singletonList(curr);
		return curr;
	}

	@Override
	public String getName() {
		return name.getSimpleName();
	}

	@Override
	public AnnotatedMethod getGetter() {
		return getter;
	}

	@Override
	public AnnotatedField getField() {
		return field;
	}

	public void setField(AnnotatedField field) {
		this.field = field;
	}

	public void setGetter(AnnotatedMethod getter) {
		this.getter = getter;
	}
}
