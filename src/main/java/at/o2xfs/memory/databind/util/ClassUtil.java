/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import at.o2xfs.memory.databind.type.JavaType;

public final class ClassUtil {

	private ClassUtil() {
		throw new AssertionError();
	}

	private static void addSuperTypes(JavaType type, Collection<JavaType> result) {
		if (type == null) {
			return;
		}
		final Class<?> cls = type.getRawClass();
		if (cls != Object.class) {
			result.add(type);
		}
		addSuperTypes(type.getSuperClass(), result);
	}

	public static String classNameOf(Object inst) {
		if (inst == null) {
			return "[null]";
		}
		return nameOf(inst.getClass());
	}

	public static <T> Constructor<T> findConstructor(Class<T> cls) throws IllegalArgumentException {
		try {
			Constructor<T> ctor = cls.getDeclaredConstructor();
			if (!Modifier.isPublic(ctor.getModifiers())) {
				throw new IllegalArgumentException("Default constructor for " + cls.getName()
						+ " is not accessible (non-public?): not allowed to try modify access via Reflection: cannot instantiate type");
			}
			return ctor;
		} catch (NoSuchMethodException e) {
			;
		} catch (Exception e) {
			ClassUtil
					.unwrapAndThrowAsIAE(e, "Failed to find default constructor of class " + cls.getName()
							+ ", problem: " + e.getMessage());
		}
		return null;
	}

	public static Field[] getDeclaredFields(Class<?> cls) {
		return cls.getDeclaredFields();
	}

	public static String nameOf(Class<?> cls) {
		if (cls == null) {
			return "[null]";
		}
		int index = 0;
		while (cls.isArray()) {
			++index;
			cls = cls.getComponentType();
		}
		String base = cls.isPrimitive() ? cls.getSimpleName() : cls.getName();
		if (index > 0) {
			StringBuilder sb = new StringBuilder(base);
			do {
				sb.append("[]");
			} while (--index > 0);
			base = sb.toString();
		}
		return base;
	}

	public static <T> T createInstance(Class<T> cls) {
		try {
			Constructor<T> ctor = findConstructor(cls);
			if (ctor == null) {
				throw new IllegalArgumentException("Class " + cls.getName() + " has no default (no arg) constructor");
			}
			return ctor.newInstance();
		} catch (ReflectiveOperationException e) {
			ClassUtil
					.unwrapAndThrowAsIAE(e,
							"Failed to instantiate class " + cls.getName() + ", problem: " + e.getMessage());
			return null;
		}
	}

	public static List<JavaType> findSuperTypes(JavaType type) {
		List<JavaType> result = new ArrayList<>();
		addSuperTypes(type.getSuperClass(), result);
		return result;
	}

	public static Annotation[] findClassAnnotations(Class<?> rawClass) {
		return rawClass.getDeclaredAnnotations();
	}

	public static Method[] getClassMethods(Class<?> cls) {
		return cls.getDeclaredMethods();
	}

	public static Throwable getRootCause(Throwable t) {
		Throwable result = t;
		while (result.getCause() != null) {
			result = result.getCause();
		}
		return result;
	}

	public static boolean isJDKClass(Class<?> rawType) {
		return rawType.getName().startsWith("java.");
	}

	public static void throwAsIAE(Throwable t, String msg) {
		throw new IllegalArgumentException(msg, t);
	}

	public static void unwrapAndThrowAsIAE(Throwable t, String msg) {
		throwAsIAE(getRootCause(t), msg);
	}

}
