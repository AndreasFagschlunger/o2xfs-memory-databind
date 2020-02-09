package at.o2xfs.memory.databind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import at.o2xfs.memory.databind.MemorySerializer;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface MemorySerialize {

	public Class<? extends MemorySerializer> using() default MemorySerializer.None.class;

	public Class<? extends MemorySerializer> nullsUsing() default MemorySerializer.None.class;
}
