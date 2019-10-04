/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind;

import at.o2xfs.memory.databind.introspect.AnnotatedMember;
import at.o2xfs.memory.databind.type.JavaType;

public interface BeanProperty {

	String getName();

	JavaType getType();

	AnnotatedMember getMember();
}
