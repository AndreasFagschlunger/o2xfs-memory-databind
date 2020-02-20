/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser.std;

import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.ReadableMemory;

public abstract class PrimitiveArrayDeserializers<T> extends StdDeserializer<T> {

	private PrimitiveArrayDeserializers(Class<?> cls) {
		super(cls);
	}

	public static MemoryDeserializer<?> forType(Class<?> rawType) {
		if (rawType == Byte.TYPE) {
			return new ByteDeser();
		}
		return null;
	}

	final static class ByteDeser extends PrimitiveArrayDeserializers<byte[]> {

		public ByteDeser() {
			super(byte[].class);
		}

		@Override
		public byte[] deserialize(ReadableMemory memory, DeserializationContext ctxt) {
			long length = memory.nextUnsignedLong();
			ReadableMemory next = memory.nextReference();
			if (length == 0) {
				return new byte[0];
			}
			return next.read((int) length);
		}
	}
}
