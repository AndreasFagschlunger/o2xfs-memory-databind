/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import java.lang.reflect.Method;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public final class MemberKey {

	private final String name;
	private final Class<?>[] parameterTypes;

	public MemberKey(Method m) {
		this(m.getName(), m.getParameterTypes());
	}

	public MemberKey(String name, Class<?>[] parameterTypes) {
		this.name = name;
		this.parameterTypes = parameterTypes;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof MemberKey) {
			MemberKey memberKey = (MemberKey) obj;
			result = new EqualsBuilder()
					.append(name, memberKey.name)
					.append(parameterTypes, memberKey.parameterTypes)
					.isEquals();
		}
		return result;

	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(name).append(parameterTypes).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).append("parameterTypes", parameterTypes).toString();
	}
}
