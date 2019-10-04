/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser.std;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import at.o2xfs.common.Bits;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.ReadableMemory;

public final class NumberDeserializers {

	private static final Set<String> classNames = new HashSet<>();
	static {
		Class<?>[] numberTypes = new Class<?>[] { Boolean.class, Byte.class, Short.class, Character.class,
				Integer.class, Long.class, Float.class, Double.class,
				// and more generic ones
				Number.class, BigDecimal.class, BigInteger.class };
		for (Class<?> cls : numberTypes) {
			classNames.add(cls.getName());
		}
	}

	private static final class BooleanDeserializer extends MemoryDeserializer<Boolean> {

		final static MemoryDeserializer<Boolean> instance = new BooleanDeserializer();

		@Override
		public Boolean deserialize(ReadableMemory memory, DeserializationContext ctxt) {
			return Boolean.valueOf(IntegerDeserializer.instance.deserialize(memory, ctxt).intValue() == 1);
		}

	}

	private static final class IntegerDeserializer extends MemoryDeserializer<Integer> {

		final static MemoryDeserializer<Integer> instance = new IntegerDeserializer();

		@Override
		public Integer deserialize(ReadableMemory memory, DeserializationContext ctxt) {
			return Bits.getInt(memory.read(4));
		}

	}

	private static final class LongDeserializer extends MemoryDeserializer<Long> {

		final static MemoryDeserializer<Long> instance = new LongDeserializer();

		@Override
		public Long deserialize(ReadableMemory memory, DeserializationContext ctxt) {
			return Bits.getLong(memory.read(8));
		}

	}

	public static MemoryDeserializer<?> find(Class<?> rawType, String clsName) {
		MemoryDeserializer<?> result = null;
		if (rawType.isPrimitive()) {
			if (rawType == Boolean.TYPE) {
				return BooleanDeserializer.instance;
			} else if (rawType == Integer.TYPE) {
				return IntegerDeserializer.instance;
			} else if (rawType == Long.TYPE) {
				return LongDeserializer.instance;
			}
		} else if (classNames.contains(clsName)) {
			if (rawType == Integer.class) {
				return IntegerDeserializer.instance;
			}
		}
		return result;
	}
}
