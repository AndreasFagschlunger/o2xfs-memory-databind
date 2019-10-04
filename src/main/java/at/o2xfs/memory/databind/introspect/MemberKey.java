/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public final class MemberKey {

	private final String name;

	public MemberKey(String name) {
		this.name = Objects.requireNonNull(name);
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof MemberKey) {
			MemberKey memberKey = (MemberKey) obj;
			result = new EqualsBuilder().append(name, memberKey.name).isEquals();
		}
		return result;

	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(name).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).toString();
	}
}
