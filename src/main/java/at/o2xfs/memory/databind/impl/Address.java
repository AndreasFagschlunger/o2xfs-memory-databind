package at.o2xfs.memory.databind.impl;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import at.o2xfs.common.Bytes;

final class Address {

	private final byte[] value;

	private Address(byte[] value) {
		if (value.length == 0) {
			throw new IllegalArgumentException("value must not be empty");
		}
		this.value = Bytes.copy(value);
	}

	public byte[] getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(value).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Address) {
			Address address = (Address) obj;
			return new EqualsBuilder().append(value, address.value).isEquals();
		}
		return false;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("value", value).toString();
	}

	public static final Address build(byte[] value) {
		return new Address(value);
	}
}