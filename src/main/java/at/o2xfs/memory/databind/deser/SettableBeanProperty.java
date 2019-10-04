/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser;

import java.util.Objects;

import at.o2xfs.memory.databind.DeserializationContext;
import at.o2xfs.memory.databind.MemoryDeserializer;
import at.o2xfs.memory.databind.PropertyName;
import at.o2xfs.memory.databind.ReadableMemory;
import at.o2xfs.memory.databind.introspect.BeanPropertyDefinition;
import at.o2xfs.memory.databind.introspect.ConcreteBeanPropertyBase;
import at.o2xfs.memory.databind.type.JavaType;
import at.o2xfs.memory.databind.util.ClassUtil;

public abstract class SettableBeanProperty extends ConcreteBeanPropertyBase {

	private final PropertyName propName;
	private final JavaType type;
	protected final MemoryDeserializer<Object> valueDeserializer;

	protected SettableBeanProperty(BeanPropertyDefinition propDef, JavaType type) {
		this(propDef.getFullName(), type, null);
	}

	protected SettableBeanProperty(SettableBeanProperty src, MemoryDeserializer<?> deser) {
		this(src.propName, src.type, deser);
	}

	private SettableBeanProperty(PropertyName propName, JavaType type, MemoryDeserializer<?> valueDeserializer) {
		this.propName = Objects.requireNonNull(propName);
		this.type = Objects.requireNonNull(type);
		this.valueDeserializer = (MemoryDeserializer<Object>) valueDeserializer;

	}

	protected void throwAsIOE(Exception e, Object value) {
		if (e instanceof IllegalArgumentException) {
			String actType = ClassUtil.classNameOf(value);
			StringBuilder msg = new StringBuilder("Problem deserializing property '")
					.append(getName())
					.append("' (expected type: ")
					.append(getType())
					.append("; actual type: ")
					.append(actType)
					.append(")");
			String origMsg = e.getMessage();
			if (origMsg != null) {
				msg.append(", problem: ").append(origMsg);
			} else {
				msg.append(" (no error message provided)");
			}
			throw new RuntimeException(msg.toString(), e);
		}
	}

	@Override
	public final String getName() {
		return propName.getSimpleName();
	}

	@Override
	public JavaType getType() {
		return type;
	}

	public abstract Object deserializeSetAndReturn(ReadableMemory memory, DeserializationContext ctxt, Object instance);

	public MemoryDeserializer<?> getValueDeserializer() {
		return valueDeserializer;
	}

	public abstract SettableBeanProperty withValueDeserializer(MemoryDeserializer<?> deser);

	public boolean hasValueDeserializer() {
		return valueDeserializer != null;
	}

	@Override
	public String toString() {
		return "[property '" + getName() + "']";
	}
}
