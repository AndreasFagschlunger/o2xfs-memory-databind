/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.introspect;

import java.lang.annotation.Annotation;

import at.o2xfs.memory.databind.AnnotationIntrospector;
import at.o2xfs.memory.databind.cfg.MapperConfig;
import at.o2xfs.memory.databind.util.ClassUtil;

class CollectorBase {

	protected final MapperConfig<?> config;
	protected final AnnotationIntrospector intr;

	public CollectorBase(MapperConfig<?> config) {
		this.config = config;
		intr = config == null ? null : config.getAnnotationIntrospector();
	}

	protected final AnnotationCollector collectAnnotations(Annotation[] annotations) {
		AnnotationCollector result = AnnotationCollector.emptyCollector();
		for (Annotation each : annotations) {
			result = result.addOrOverride(each);
			if (intr.isAnnotationBundle(each)) {
				collectFromBundle(result, each);
			}
		}
		return result;
	}

	protected final AnnotationCollector collectAnnotations(AnnotationCollector c, Annotation[] anns) {
		for (int i = 0, end = anns.length; i < end; ++i) {
			Annotation ann = anns[i];
			c = c.addOrOverride(ann);
			if (intr.isAnnotationBundle(ann)) {
				c = collectFromBundle(c, ann);
			}
		}
		return c;
	}

	protected final AnnotationCollector collectDefaultAnnotations(AnnotationCollector c, Annotation[] anns) {
		for (int i = 0, end = anns.length; i < end; ++i) {
			Annotation ann = anns[i];
			if (!c.isPresent(ann)) {
				c = c.addOrOverride(ann);
				if (intr.isAnnotationBundle(ann)) {
					c = collectDefaultFromBundle(c, ann);
				}
			}
		}
		return c;
	}

	protected final AnnotationCollector collectDefaultFromBundle(AnnotationCollector c, Annotation bundle) {
		Annotation[] anns = ClassUtil.findClassAnnotations(bundle.annotationType());
		for (int i = 0, end = anns.length; i < end; ++i) {
			Annotation ann = anns[i];
			if (!c.isPresent(ann)) {
				c = c.addOrOverride(ann);
				if (intr.isAnnotationBundle(ann)) {
					c = collectFromBundle(c, ann);
				}
			}
		}
		return c;
	}

	protected final AnnotationCollector collectFromBundle(AnnotationCollector c, Annotation bundle) {
		Annotation[] anns = ClassUtil.findClassAnnotations(bundle.annotationType());
		for (Annotation each : anns) {
			c = c.addOrOverride(each);
			if (intr.isAnnotationBundle(each)) {
				c = collectFromBundle(c, each);
			}
		}
		return c;
	}
}
