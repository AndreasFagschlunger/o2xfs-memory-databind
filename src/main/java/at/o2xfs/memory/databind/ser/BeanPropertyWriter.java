/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import at.o2xfs.memory.databind.MemoryGenerator;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.annotation.Pointer;
import at.o2xfs.memory.databind.introspect.AnnotatedField;
import at.o2xfs.memory.databind.introspect.AnnotatedMember;
import at.o2xfs.memory.databind.introspect.AnnotatedMethod;
import at.o2xfs.memory.databind.introspect.BeanPropertyDefinition;
import at.o2xfs.memory.databind.introspect.ConcreteBeanPropertyBase;
import at.o2xfs.memory.databind.type.JavaType;

public class BeanPropertyWriter extends ConcreteBeanPropertyBase {

	private final String name;
	private final JavaType declaredType;
	private final AnnotatedMember member;
	private Method accessorMethod;
	private Field field;
	private MemorySerializer<Object> serializer;
	private JavaType nonTrivialBaseType;

	public BeanPropertyWriter(BeanPropertyDefinition propDef, AnnotatedMember member, JavaType declaredType,
			MemorySerializer<?> serializer) {
		this.name = propDef.getName();
		this.declaredType = declaredType;
		this.member = member;
		if (member instanceof AnnotatedMethod) {
			accessorMethod = (Method) member.getMember();
		} else if (member instanceof AnnotatedField) {
			field = (Field) member.getMember();
		}
		this.serializer = (MemorySerializer<Object>) serializer;
	}

	private MemorySerializer<Object> findAndAddDynamic(Class<?> type, SerializerProvider prov) {
		MemorySerializer<Object> result = null;
		if (nonTrivialBaseType != null) {
			JavaType t = prov.constructSpecializedType(nonTrivialBaseType, type);
			result = prov.findPrimaryPropertySerializer(t, this);
		}
		return result;
	}

	public boolean hasSerializer() {
		return serializer != null;
	}

	public void assignSerializer(MemorySerializer<Object> ser) {
		serializer = ser;
	}

	public void serializeAsField(Object obj, MemoryGenerator gen, SerializerProvider prov) {
		try {
			Object value = accessorMethod != null ? accessorMethod.invoke(obj) : field.get(obj);
			if (value != null && serializer == null) {
				serializer = findAndAddDynamic(value.getClass(), prov);
			}
			if (serializer != null) {
				if (member.getAnnotation(Pointer.class) != null) {
					try (MemoryGenerator reference = gen.writePointer()) {
						serializer.serialize(value, reference, prov);
					}
				} else {
					serializer.serialize(value, gen, prov);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public AnnotatedMember getMember() {
		return member;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public JavaType getType() {
		return declaredType;
	}

	public MemorySerializer<Object> getSerializer() {
		return serializer;
	}

	public JavaType getNonTrivialBaseType() {
		return nonTrivialBaseType;
	}

	public void setNonTrivialBaseType(JavaType nonTrivialBaseType) {
		this.nonTrivialBaseType = nonTrivialBaseType;
	}

}
