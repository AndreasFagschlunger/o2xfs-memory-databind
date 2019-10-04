/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.type;

import java.util.Collection;

public abstract class CollectionLikeType extends TypeBase {

	protected final JavaType elementType;

	protected CollectionLikeType(Class<?> rawClass, TypeBindings bindings, JavaType superClass,
			JavaType[] superInterfaces, JavaType elementType) {
		super(rawClass, bindings, superClass, superInterfaces);
		this.elementType = elementType;
	}

	@Override
	protected String buildCanonicalName() {
		StringBuilder sb = new StringBuilder();
		sb.append(rawClass.getName());
		if (elementType != null) {
			sb.append('<');
			sb.append(elementType.toCanonical());
			sb.append('>');
		}
		return sb.toString();
	}

	@Override
	public boolean isCollectionLikeType() {
		return true;
	}

	@Override
	public boolean isContainerType() {
		return true;
	}

	public boolean isTrueCollectionType() {
		return Collection.class.isAssignableFrom(rawClass);
	}

}
