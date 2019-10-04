/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 * Modifications Copyright (c) 2019 Andreas Fagschlunger
 */
package at.o2xfs.memory.databind.deser.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import at.o2xfs.memory.databind.deser.SettableBeanProperty;

public final class BeanPropertyMap implements Iterable<SettableBeanProperty> {

	private final SettableBeanProperty[] propsInOrder;

	private BeanPropertyMap(Collection<SettableBeanProperty> props) {
		propsInOrder = props.toArray(new SettableBeanProperty[props.size()]);
	}

	private int findFromOrdered(SettableBeanProperty prop) {
		for (int i = 0, end = propsInOrder.length; i < end; ++i) {
			if (propsInOrder[i] == prop) {
				return i;
			}
		}
		throw new IllegalStateException("Illegal state: property '" + prop.getName() + "' missing from _propsInOrder");
	}

	@Override
	public Iterator<SettableBeanProperty> iterator() {
		return Arrays.asList(propsInOrder).iterator();
	}

	public static BeanPropertyMap construct(Collection<SettableBeanProperty> props) {
		return new BeanPropertyMap(props);
	}

	public void replace(SettableBeanProperty origProp, SettableBeanProperty newProp) {
		propsInOrder[findFromOrdered(origProp)] = newProp;
	}
}
