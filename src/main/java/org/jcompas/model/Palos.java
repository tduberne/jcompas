/* *********************************************************************** *
 * project: org.jcompas.*
 * PaloFactory.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2012 by the members listed in the COPYING,        *
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
package org.jcompas.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Gives access to the available palos.
 * @author thibautd
 */
public final class Palos {
	private final Map<String, Palo> palos = new HashMap<String, Palo>();

	public Palos(final Collection<Palo> palos) {
		for (Palo p : palos) {
			this.palos.put( p.getName() , p );
		}
	}

	public Collection<String> getAvailablePalos() {
		return palos.keySet();
	}

	public Palo createPalo( final String name ) {
		return palos.get( name );
	}
}

