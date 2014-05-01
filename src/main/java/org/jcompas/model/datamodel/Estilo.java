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
package org.jcompas.model.datamodel;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents an estilo, that is a special form of a palo
 * (Bulerias por 12 or por 6, sevillanas rocieras or boleras...).
 * Technically, it can be seen as a collection of patterns associated with
 * one particular compas.
 *
 * @author thibautd
 */
public final class Estilo {
	private final EstiloId id;
	private final PaloId palo;
	private final CompasInformation compas;
	private final Set<PatternId> patterns = new LinkedHashSet<PatternId>();

	public Estilo(
			final EstiloId id,
			final PaloId palo,
			final CompasInformation compas ) {
		this.id = id;
		this.palo = palo;
		this.compas = compas;
	}

	public EstiloId getId() {
		return id;
	}

	public String getName() {
		return id.toString();
	}

	public PaloId getPalo() {
		return palo;
	}

	public CompasInformation getCompas() {
		return compas;
	}

	public Set<PatternId> getPatterns() {
		return Collections.unmodifiableSet( patterns );
	}

	void addPattern( final PatternId patternId ) {
		patterns.add( patternId );
	}
}

