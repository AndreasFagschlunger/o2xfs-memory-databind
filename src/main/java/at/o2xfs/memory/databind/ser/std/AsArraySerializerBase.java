/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser.std;

import at.o2xfs.memory.databind.AnnotationIntrospector;
import at.o2xfs.memory.databind.BeanProperty;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.introspect.AnnotatedMember;
import at.o2xfs.memory.databind.ser.ContainerSerializer;
import at.o2xfs.memory.databind.type.JavaType;

public abstract class AsArraySerializerBase<T> extends ContainerSerializer<T> {

	protected final JavaType elementType;
	protected final MemorySerializer<Object> elementSerializer;

	protected AsArraySerializerBase(Class<?> cls, JavaType et, MemorySerializer<?> elementSerializer) {
		super(cls);
		elementType = et;
		this.elementSerializer = (MemorySerializer<Object>) elementSerializer;
	}

	@Override
	public MemorySerializer<?> createContextual(SerializerProvider ctxt, BeanProperty property) {
		MemorySerializer<?> ser = null;
		if (property != null) {
			AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
			AnnotatedMember m = property.getMember();
			if (m != null) {
				ser = ctxt.serializerInstance(m, intr.findContentSerializer(ctxt.getConfig(), m));
			}
		}
		return this;
	}
}
