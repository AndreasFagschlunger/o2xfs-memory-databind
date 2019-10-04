/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.util;

import at.o2xfs.memory.databind.introspect.AnnotatedMethod;

public final class BeanUtil {

	private static String legacyManglePropertyName(String basename, int offset) {
		char[] cbuf = new char[basename.length() - offset];
		System.arraycopy(basename.toCharArray(), offset, cbuf, 0, cbuf.length);
		cbuf[0] = Character.toLowerCase(cbuf[0]);
		return new String(cbuf);
	}

	public static String okNameForGetter(AnnotatedMethod m) {
		String result = null;
		if (m.getName().startsWith("get")) {
			result = propertyName(m.getName(), 3);
		}
		return result;
	}

	public static String okNameForIsGetter(AnnotatedMethod m) {
		String result = null;
		if (m.getName().startsWith("is")) {
			if (m.getRawType() == Boolean.TYPE || m.getRawType() == Boolean.class) {
				result = propertyName(m.getName(), 2);
			}
		}
		return result;
	}

	public static String okNameForMutator(AnnotatedMethod am, String prefix) {
		String result = null;
		String name = am.getName();
		if (name.startsWith(prefix)) {
			result = legacyManglePropertyName(name, prefix.length());
		}
		return result;
	}

	private static final String propertyName(String name, int offset) {
		String result = null;
		if (name.length() > offset) {
			char[] cbuf = new char[name.length() - offset];
			System.arraycopy(name.toCharArray(), offset, cbuf, 0, cbuf.length);
			cbuf[0] = Character.toLowerCase(cbuf[0]);
			result = new String(cbuf);
		}
		return result;
	}

}
