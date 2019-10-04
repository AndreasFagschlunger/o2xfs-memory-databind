/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import at.o2xfs.memory.databind.AnnotationIntrospector;
import at.o2xfs.memory.databind.type.JavaType;
import at.o2xfs.memory.databind.type.TypeFactory;
import at.o2xfs.memory.databind.util.ClassUtil;

public class AnnotatedMethodCollector extends CollectorBase {

	private static final class MethodBuilder {

		private final TypeResolutionContext typeContext;
		private Method method;
		private AnnotationCollector annotations;

		public MethodBuilder(TypeResolutionContext typeContext, Method method, AnnotationCollector annotations) {
			this.typeContext = Objects.requireNonNull(typeContext);
			this.method = Objects.requireNonNull(method);
			this.annotations = Objects.requireNonNull(annotations);
		}

		public AnnotatedMethod build() {
			return new AnnotatedMethod(typeContext, method, annotations.asAnnotationMap());
		}
	}

	public AnnotatedMethodCollector(AnnotationIntrospector intr) {
		super(intr);
	}

	private void addMemberMethods(TypeResolutionContext tc, Class<?> cls, Map<MemberKey, MethodBuilder> methods) {
		for (Method each : ClassUtil.getClassMethods(cls)) {
			if (!isIncludableMemberMethod(each)) {
				continue;
			}
			MemberKey key = new MemberKey(each.getName());
			MethodBuilder builder = methods.get(key);
			if (builder == null) {
				methods.put(key, new MethodBuilder(tc, each, collectAnnotations(each.getAnnotations())));
			} else {
				builder.annotations = collectDefaultAnnotations(builder.annotations, each.getDeclaredAnnotations());
			}
		}
	}

	private AnnotatedMethodMap collect(TypeFactory typeFactory, TypeResolutionContext tc, JavaType mainType,
			List<JavaType> superTypes) {
		Map<MemberKey, MethodBuilder> methods = new LinkedHashMap<>();
		addMemberMethods(tc, mainType.getRawClass(), methods);
		for (JavaType type : superTypes) {
			addMemberMethods(new TypeResolutionContext.Basic(typeFactory, type.getBindings()), type.getRawClass(),
					methods);
		}

		Map<MemberKey, AnnotatedMethod> actual = new LinkedHashMap<>();
		for (Map.Entry<MemberKey, MethodBuilder> entry : methods.entrySet()) {
			actual.put(entry.getKey(), entry.getValue().build());
		}
		return new AnnotatedMethodMap(actual);
	}

	private boolean isIncludableMemberMethod(Method m) {
		boolean result = true;
		if (Modifier.isStatic(m.getModifiers()) || m.isSynthetic() || m.isBridge()) {
			result = false;
		}
		return result;
	}

	public static AnnotatedMethodMap collectMethods(AnnotationIntrospector intr, TypeResolutionContext tc,
			TypeFactory typeFactory, JavaType type, List<JavaType> superTypes) {
		return new AnnotatedMethodCollector(intr).collect(typeFactory, tc, type, superTypes);
	}

}
