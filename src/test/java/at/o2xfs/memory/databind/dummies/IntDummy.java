package at.o2xfs.memory.databind.dummies;

public class IntDummy extends BaseDummy {

	public static class Builder {

		private int value;

		public Builder value(int value) {
			this.value = value;
			return this;
		}

		public IntDummy build() {
			return new IntDummy(this);
		}
	}

	private final int value;

	public IntDummy(Builder builder) {
		value = builder.value;
	}

	public int getValue() {
		return value;
	}
}
