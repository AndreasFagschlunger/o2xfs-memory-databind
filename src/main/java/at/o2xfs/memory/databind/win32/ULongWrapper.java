package at.o2xfs.memory.databind.win32;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import at.o2xfs.memory.databind.annotation.win32.ULong;

public final class ULongWrapper {

	public static class Builder {

		private long value;

		public Builder value(long value) {
			this.value = value;
			return this;
		}

		public ULongWrapper build() {
			return new ULongWrapper(this);
		}

	}

	@ULong
	private final long value;

	private ULongWrapper(Builder builder) {
		value = builder.value;
	}

	public long getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(value).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ULongWrapper) {
			ULongWrapper wrapper = (ULongWrapper) obj;
			return new EqualsBuilder().append("value", wrapper.value).isEquals();
		}
		return false;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("value", value).toString();
	}

	public static ULongWrapper build(long value) {
		return new ULongWrapper.Builder().value(value).build();
	}
}
