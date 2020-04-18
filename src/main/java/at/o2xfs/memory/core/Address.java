package at.o2xfs.memory.core;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import at.o2xfs.common.Hex;
import at.o2xfs.memory.databind.annotation.MemoryDeserialize;
import at.o2xfs.memory.databind.annotation.MemorySerialize;
import at.o2xfs.memory.databind.deser.win32.AddressDeserializer;
import at.o2xfs.memory.databind.ser.win32.AddressSerializer;

@MemorySerialize(using = AddressSerializer.class)
@MemoryDeserialize(using = AddressDeserializer.class)
public class Address {

	private final byte[] value;

	protected Address(byte[] value) {
		this.value = value.clone();
	}

	public final byte[] getValue() {
		return value.clone();
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
		return new ToStringBuilder(this).append("value", Hex.encode(value)).toString();
	}

	public static Address build(byte[] value) {
		return new Address(value);
	}
}
