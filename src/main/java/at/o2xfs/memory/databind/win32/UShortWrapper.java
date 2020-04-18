package at.o2xfs.memory.databind.win32;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import at.o2xfs.memory.databind.annotation.win32.UShort;

public final class UShortWrapper {

	public static class Builder {

		private int value;

		public Builder value(int value) {
			this.value = value;
			return this;
		}

		public UShortWrapper build() {
			return new UShortWrapper(this);
		}

	}

	@UShort
	private final int value;

	private UShortWrapper(Builder builder) {
		value = builder.value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(value).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UShortWrapper) {
			UShortWrapper wrapper = (UShortWrapper) obj;
			return new EqualsBuilder().append("value", wrapper.value).isEquals();
		}
		return false;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("value", value).toString();
	}

	public static UShortWrapper build(int value) {
		return new UShortWrapper.Builder().value(value).build();
	}
}
