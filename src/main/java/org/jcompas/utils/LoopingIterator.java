/* *********************************************************************** *
 * project: org.jcompas.*
 * LoopingIterator.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2013 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           :                                                       *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */
package org.jcompas.utils;

import java.util.Iterator;

/**
 * @author thibautd
 */
public class LoopingIterator<T> implements Iterator<T> {
	private final Iterable<T> iterable;
	private Iterator<T> delegate;

	public LoopingIterator(
			final Iterable<T> iterable) {
		this.iterable = iterable;
	}

	@Override
	public boolean hasNext() {
		if ( !delegate.hasNext() ) delegate = iterable.iterator();
		return delegate.hasNext();
	}

	@Override
	public T next() {
		if ( !delegate.hasNext() ) delegate = iterable.iterator();
		return delegate.next();
	}

	@Override
	public void remove() {
		delegate.remove();
	}
}

