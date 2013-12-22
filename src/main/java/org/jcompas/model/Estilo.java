/* *********************************************************************** *
 * project: org.jcompas.*
 * Estilo.java
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

import org.jcompas.model.sound.Pattern;

/**
 * Represents an estilo, that is a special form of a palo
 * (Bulerias por 12 or por 6, sevillanas rocieras or boleras...).
 * Technically, it can be seen as a collection of patterns associated with
 * one particular compas.
 *
 * @author thibautd
 */
public final class Estilo {
	private final String name;
	private final CompasInformation compas;
	private final Map<String, Pattern> patterns;

	public Estilo(
			final String name,
			final CompasInformation compas,
			final List<Pattern> patterns) {
		this.name = name;
		this.compas = compas;
		Map<String, Pattern> map = new HashMap<String, Pattern>();
		for (Pattern p : patterns) {
			map.put( p.getName() , p );
		}

		this.patterns = Collections.unmodifiableMap( map );
	}

	public String getName() {
		return name;
	}

	public CompasInformation getCompas() {
		return compas;
	}

	public Set<String> getPatterns() {
		return patterns.keySet();
	}

	public Pattern getPattern(final String patternName) {
		return patterns.get( patternName );
	}
}

