/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.type;

import java.lang.reflect.Modifier;

import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class JavaType {

	protected final Class<?> rawClass;
	protected final Object typeHandler;

	protected JavaType(Class<?> rawClass) {
		this.rawClass = rawClass;
		this.typeHandler = null;
	}

	protected JavaType(JavaType base) {
		this.rawClass = base.rawClass;
		this.typeHandler = null;
	}

	public abstract JavaType containedType(int index);

	public abstract int containedTypeCount();

	public JavaType containedTypeOrUnknown(int index) {
		JavaType t = containedType(index);
		return (t == null) ? TypeFactory.unknownType() : t;
	}

	public abstract JavaType findSuperType(Class<?> rawTarget);

	public Class<?> getRawClass() {
		return rawClass;
	}

	public JavaType getContentType() {
		return null;
	}

	public String getGenericSignature() {
		StringBuilder builder = new StringBuilder();
		getGenericSignature(new StringBuilder());
		return builder.toString();
	}

	public abstract StringBuilder getGenericSignature(StringBuilder sb);

	public JavaType getKeyType() {
		return null;
	}

	public abstract TypeBindings getBindings();

	public JavaType getReferencedType() {
		return null;
	}

	public abstract JavaType getSuperClass();

	public boolean hasGenericTypes() {
		return containedTypeCount() > 0;
	}

	public boolean isCollectionLikeType() {
		return false;
	}

	public final boolean isFinal() {
		return Modifier.isFinal(rawClass.getModifiers());
	}

	public boolean isMapLikeType() {
		return false;
	}

	public abstract boolean isContainerType();

	public final boolean isReferenceType() {
		return getReferencedType() != null;
	}

	public abstract JavaType refine(Class<?> rawType, TypeBindings bindings, JavaType superClass,
			JavaType[] superInterfaces);

	public abstract String toCanonical();

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public <T> T getTypeHandler() {
		return (T) typeHandler;
	}

	public final boolean isEnumType() {
		return rawClass.isEnum();
	}

}
