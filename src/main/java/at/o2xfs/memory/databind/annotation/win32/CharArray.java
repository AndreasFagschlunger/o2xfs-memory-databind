package at.o2xfs.memory.databind.annotation.win32;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import at.o2xfs.memory.databind.annotation.MemoryAnnotationsInside;
import at.o2xfs.memory.databind.annotation.MemoryDeserialize;
import at.o2xfs.memory.databind.annotation.MemorySerialize;
import at.o2xfs.memory.databind.deser.win32.CharArrayDeserializer;
import at.o2xfs.memory.databind.ser.win32.CharArraySerializer;

@Retention(RetentionPolicy.RUNTIME)
@MemoryAnnotationsInside
@MemorySerialize(using = CharArraySerializer.class)
@MemoryDeserialize(using = CharArrayDeserializer.class)
public @interface CharArray {

	int length();
}
