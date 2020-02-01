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

	public static class Std implements BeanProperty {

		private final PropertyName name;
		private final JavaType type;
		private final AnnotatedMember member;

		public Std(PropertyName name, JavaType type, AnnotatedMember member) {
			this.name = name;
			this.type = type;
			this.member = member;
		}

		@Override
		public String getName() {
			return name.getSimpleName();
		}

		@Override
		public JavaType getType() {
			return type;
		}

		@Override
		public AnnotatedMember getMember() {
			return member;
		}

	}
}
