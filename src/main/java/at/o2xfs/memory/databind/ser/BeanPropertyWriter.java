/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.annotation.Pointer;
import at.o2xfs.memory.databind.introspect.AnnotatedField;
import at.o2xfs.memory.databind.introspect.AnnotatedMember;
import at.o2xfs.memory.databind.introspect.AnnotatedMethod;
import at.o2xfs.memory.databind.introspect.BeanPropertyDefinition;
import at.o2xfs.memory.databind.introspect.ConcreteBeanPropertyBase;
import at.o2xfs.memory.databind.jsontype.TypeSerializer;
import at.o2xfs.memory.databind.ser.impl.PropertySerializerMap;
import at.o2xfs.memory.databind.type.JavaType;

public class BeanPropertyWriter extends ConcreteBeanPropertyBase {

	private final String name;
	private final JavaType declaredType;
	private final AnnotatedMember member;
	private Method accessorMethod;
	private Field field;
	private MemorySerializer<Object> serializer;
	private TypeSerializer typeSerializer;
	private PropertySerializerMap dynamicSerializers;
	private JavaType nonTrivialBaseType;

	public BeanPropertyWriter(BeanPropertyDefinition propDef, AnnotatedMember member, JavaType declaredType,
			MemorySerializer<?> serializer, TypeSerializer typeSer) {
		this.name = propDef.getName();
		this.declaredType = declaredType;
		this.member = member;
		if (member instanceof AnnotatedMethod) {
			accessorMethod = (Method) member.getMember();
		} else if (member instanceof AnnotatedField) {
			field = (Field) member.getMember();
		}
		this.serializer = (MemorySerializer<Object>) serializer;
		typeSerializer = typeSer;
		dynamicSerializers = (serializer == null) ? PropertySerializerMap.emptyForProperties() : null;
	}

	private MemorySerializer<Object> findAndAddDynamic(PropertySerializerMap map, Class<?> rawType,
			SerializerProvider provider) {
		JavaType t;
		if (nonTrivialBaseType != null) {
			t = provider.constructSpecializedType(nonTrivialBaseType, rawType);
		} else {
			t = provider.constructType(rawType);
		}
		PropertySerializerMap.SerializerAndMapResult result = map.findAndAddPrimarySerializer(t, provider, this);
		if (map != result.map) {
			dynamicSerializers = result.map;
		}
		return result.serializer;
	}

	public boolean hasSerializer() {
		return serializer != null;
	}

	public void assignSerializer(MemorySerializer<Object> ser) {
		serializer = ser;
	}

	public void serializeAsField(Object obj, MemoryGenerator gen, SerializerProvider prov) throws Exception {
		Object value = accessorMethod != null ? accessorMethod.invoke(obj) : field.get(obj);
		if (value == null) {
			gen.writeNull();
			return;
		}
		MemorySerializer<Object> ser = serializer;
		if (ser == null) {
			Class<?> cls = value.getClass();
			PropertySerializerMap m = dynamicSerializers;
			ser = m.serializerFor(cls);
			if (ser == null) {
				ser = findAndAddDynamic(m, cls, prov);
			}
		}
		Pointer pointer = member.getAnnotation(Pointer.class);
		if (pointer != null) {
			gen.startPointer();
			if (pointer.pointerToPointer()) {
				gen.startPointer();
			}
		}
		if (typeSerializer == null) {
			ser.serialize(value, gen, prov);
		} else {
			ser.serializeWithType(value, gen, prov, typeSerializer);
		}
		if (pointer != null) {
			if (pointer.pointerToPointer()) {
				gen.endPointer();
			}
			gen.endPointer();
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(40);
		sb.append("property '").append(getName()).append("' (");
		if (accessorMethod != null) {
			sb
					.append("via method ")
					.append(accessorMethod.getDeclaringClass().getName())
					.append("#")
					.append(accessorMethod.getName());
		} else if (field != null) {
			sb.append("field \"").append(field.getDeclaringClass().getName()).append("#").append(field.getName());
		} else {
			sb.append("virtual");
		}
		if (serializer == null) {
			sb.append(", no static serializer");
		} else {
			sb.append(", static serializer of type " + serializer.getClass().getName());
		}
		sb.append(')');
		return sb.toString();
	}
}
