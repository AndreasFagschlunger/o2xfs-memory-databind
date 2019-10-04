/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class AnnotatedMethodMap implements Iterable<AnnotatedMethod> {

	private final Map<MemberKey, AnnotatedMethod> methods;

	public AnnotatedMethodMap(Map<MemberKey, AnnotatedMethod> methods) {
		this.methods = Objects.requireNonNull(methods);
	}

	public AnnotatedMethod find(String name) {
		return methods.get(new MemberKey(name));
	}

	@Override
	public Iterator<AnnotatedMethod> iterator() {
		return methods.values().iterator();
	}
}
