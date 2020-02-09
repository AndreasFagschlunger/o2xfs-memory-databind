package at.o2xfs.memory.databind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import at.o2xfs.memory.databind.MemoryDeserializer;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface MemoryDeserialize {

	@SuppressWarnings("rawtypes")
	public Class<? extends MemoryDeserializer> using() default MemoryDeserializer.None.class;
}
