/* *********************************************************************** *
 * project: org.jcompas.*
 * Palo.java
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a "Palo", such as Bulerias,
 * soleares, seguiryias, sevillanas...
 * @author thibautd
 */
public final class Palo {
	private final String name;
	private final Map<String, Estilo> estilos;

	public Palo(
			final String name,
			final List<Estilo> estilos) {
		this.name = name;

		Map<String, Estilo> map = new HashMap<String, Estilo>();
		for (Estilo e : estilos) {
			map.put( e.getName() , e );
		}
		
		this.estilos = Collections.unmodifiableMap( map );
	}

	public String getName() {
		return name;
	}

	public Set<String> getEstilos() {
		return estilos.keySet();
	}

	public Estilo getEstilo(final String name) {
		return estilos.get( name );
	}

	public String toString() {
		return "["+getClass().getSimpleName()+": "+name+", "+estilos+"]";
	}
}

