/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser;

import java.util.Objects;

import at.o2xfs.memory.databind.BeanDescription;
import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.ReadableMemory;
import at.o2xfs.memory.databind.annotation.Pointer;
import at.o2xfs.memory.databind.deser.impl.BeanPropertyMap;
import at.o2xfs.memory.databind.introspect.AnnotatedMethod;

public class BuilderBasedDeserializer extends BeanDeserializerBase {

	private final AnnotatedMethod buildMethod;

	public BuilderBasedDeserializer(BeanDeserializerBuilder builder, BeanDescription beanDesc,
			BeanPropertyMap beanProperties) {
		super(builder, beanDesc, beanProperties);
		buildMethod = Objects.requireNonNull(builder.getBuildMethod());
	}

	private Object finishBuild(Object builder) {
		try {
			return buildMethod.getMember().invoke(builder);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private final Object vanillaDeserialize(ReadableMemory memory, DeserializationContext ctxt) {
		Object result = valueInstantiator.createUsingDefault(ctxt);
		for (SettableBeanProperty prop : beanProperties) {
			Pointer pointer = prop.getMember().getAnnotation(Pointer.class);
			if (pointer != null) {
				ReadableMemory reference = memory.nextReference();
				if (pointer.pointerToPointer()) {
					reference = reference.nextReference();
				}
				prop.deserializeSetAndReturn(reference, ctxt, result);
			} else {
				prop.deserializeSetAndReturn(memory, ctxt, result);
			}
		}
		return result;
	}

	@Override
	public Object deserialize(ReadableMemory memory, DeserializationContext ctxt) {
		return finishBuild(vanillaDeserialize(memory, ctxt));
	}

}
