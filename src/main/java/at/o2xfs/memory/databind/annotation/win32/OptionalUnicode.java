package at.o2xfs.memory.databind.annotation.win32;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import at.o2xfs.memory.databind.annotation.MemoryAnnotationsInside;
import at.o2xfs.memory.databind.annotation.MemoryDeserialize;
import at.o2xfs.memory.databind.annotation.MemorySerialize;
import at.o2xfs.memory.databind.deser.win32.OptionalUnicodeDeserializer;
import at.o2xfs.memory.databind.ser.win32.OptionalUnicodeSerializer;

@Retention(RetentionPolicy.RUNTIME)
@MemoryAnnotationsInside
@MemorySerialize(using = OptionalUnicodeSerializer.class)
@MemoryDeserialize(using = OptionalUnicodeDeserializer.class)
public @interface OptionalUnicode {

}
