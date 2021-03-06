/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser.impl;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.ReadableMemory;
import at.o2xfs.memory.databind.deser.SettableBeanProperty;
import at.o2xfs.memory.databind.introspect.AnnotatedMember;
import at.o2xfs.memory.databind.introspect.AnnotatedMethod;
import at.o2xfs.memory.databind.introspect.BeanPropertyDefinition;
import at.o2xfs.memory.databind.type.JavaType;

public class MethodProperty extends SettableBeanProperty {

	private static final Logger LOG = LogManager.getLogger(MethodProperty.class);

	private final AnnotatedMethod annotated;

	public MethodProperty(BeanPropertyDefinition propDef, JavaType type, AnnotatedMethod annotated) {
		super(propDef, type);
		this.annotated = Objects.requireNonNull(annotated);
	}

	private MethodProperty(MethodProperty src, MemoryDeserializer<?> deser) {
		super(src, deser);
		annotated = src.annotated;
	}

	@Override
	public Object deserializeSetAndReturn(ReadableMemory memory, DeserializationContext ctxt, Object instance) {
		LOG.debug("deserializeSetAndReturn: annotated={},valueDeserializer={}", annotated, valueDeserializer);
		Object value = valueDeserializer.deserialize(memory, ctxt);
		LOG.debug("deserializeSetAndReturn: value={}", value);
		try {
			annotated.getMethod().invoke(instance, value);
		} catch (Exception e) {
			throwAsIOE(e, value);
		}
		return value;
	}

	@Override
	public AnnotatedMember getMember() {
		return annotated;
	}

	@Override
	public SettableBeanProperty withValueDeserializer(MemoryDeserializer<?> deser) {
		return new MethodProperty(this, deser);
	}
}
