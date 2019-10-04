package at.o2xfs.memory.databind.annotation.win32;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import at.o2xfs.memory.databind.annotation.MemoryAnnotationsInside;
import at.o2xfs.memory.databind.annotation.MemoryDeserialize;
import at.o2xfs.memory.databind.annotation.MemorySerialize;
import at.o2xfs.memory.databind.deser.win32.ULongDeserializer;
import at.o2xfs.memory.databind.ser.win32.ULongSerializer;

@Retention(RetentionPolicy.RUNTIME)
@MemoryAnnotationsInside
@MemorySerialize(using = ULongSerializer.class)
@MemoryDeserialize(using = ULongDeserializer.class)
public @interface ULong {

}
