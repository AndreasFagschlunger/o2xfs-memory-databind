/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.ser;

import java.util.List;

import at.o2xfs.memory.core.MemoryGenerator;
import at.o2xfs.memory.databind.MemorySerializer;
import at.o2xfs.memory.databind.SerializerProvider;
import at.o2xfs.memory.databind.type.JavaType;

public abstract class BeanSerializerBase extends MemorySerializer<Object> {

	protected final List<BeanPropertyWriter> properties;

	protected BeanSerializerBase(List<BeanPropertyWriter> properties) {
		this.properties = properties;
	}

	protected void serializeFields(Object obj, MemoryGenerator gen, SerializerProvider prov) {
		for (BeanPropertyWriter property : properties) {
			try {
				property.serializeAsField(obj, gen, prov);
			} catch (Exception e) {
				throw new RuntimeException("Error serializing Field: property=" + property + ",obj=" + obj, e);
			}
		}
	}

	@Override
	public void resolve(SerializerProvider provider) {
		for (BeanPropertyWriter prop : properties) {
			if (prop.hasSerializer()) {
				continue;
			}
			JavaType type = prop.getType();
			if (!type.isFinal()) {
				if (type.isContainerType() || type.hasGenericTypes()) {
					prop.setNonTrivialBaseType(type);
				}
				continue;
			}
			MemorySerializer<Object> ser = provider.findPrimaryPropertySerializer(type, prop);
			prop.assignSerializer(ser);
		}
	}
}
