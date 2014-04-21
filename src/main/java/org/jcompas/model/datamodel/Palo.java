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
package org.jcompas.model.datamodel;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents a "Palo", such as Bulerias,
 * soleares, seguiryias, sevillanas...
 * @author thibautd
 */
public final class Palo {
	private final PaloId id;
	private final Set<EstiloId> estilos = new LinkedHashSet<EstiloId>();

	public Palo(
			final PaloId id ) {
		this.id = id;
	}

	void addEstilo( final EstiloId estilo ) {
		estilos.add( estilo );
	}

	public PaloId getId() {
		return id;
	}

	public String getName() {
		return id.toString();
	}

	public Set<EstiloId> getEstilos() {
		return Collections.unmodifiableSet( estilos );
	}

	public String toString() {
		return "["+getClass().getSimpleName()+": "+getName()+", "+estilos+"]";
	}
}

