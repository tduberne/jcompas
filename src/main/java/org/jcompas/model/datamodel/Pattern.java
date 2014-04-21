/* *********************************************************************** *
 * project: org.jcompas.*
 * Pattern.java
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Abstraction for a "pattern".
 * @author thibautd
 */
public final class Pattern {
	private final List<ClapLine> clapLines;
	private final PatternId id;
	private final Set<EstiloId> estilos;
	private final int typicalNRepeats;
	private final int durationInCompas;

	// /////////////////////////////////////////////////////////////////////////
	// constructor
	// /////////////////////////////////////////////////////////////////////////
	public Pattern(
			final PatternId id,
			final Set<EstiloId> estilos,
			final List<ClapLine> clapLines,
			final int typicalNRepeats,
			final int durationInCompas) {
		this.id = id;
		this.estilos = estilos;
		this.clapLines = Collections.unmodifiableList( clapLines );
		this.typicalNRepeats = typicalNRepeats;
		this.durationInCompas = durationInCompas;
	}

	// /////////////////////////////////////////////////////////////////////////
	// getters
	// /////////////////////////////////////////////////////////////////////////
	public List<ClapLine> getMusicians() {
		return clapLines;
	}

	public PatternId getId() {
		return id;
	}

	public String getName() {
		return id.toString();
	}

	public Set<EstiloId> getEstilos() {
		return estilos;
	}

	public int getTypicalNumberOfRepetitions() {
		return typicalNRepeats;
	}

	public int getDurationInCompases() {
		return durationInCompas;
	}

	// /////////////////////////////////////////////////////////////////////////
	// classes
	// /////////////////////////////////////////////////////////////////////////
	public static final class ClapLine {
		private final String name;
		private final ClapId clap;
		private final List<Golpe> golpes;

		public ClapLine(
				final String name,
				final ClapId clap,
				final List<Golpe> golpes) {
			this.name = name;
			this.clap = clap;
			List<Golpe> sorted = new ArrayList<Golpe>( golpes );
			Collections.sort(
					golpes,
					new Comparator<Golpe> () {
						@Override
						public int compare(
							final Golpe o1,
							final Golpe o2) {
							return Double.compare(
								o1.getPositionInCompas(),
								o2.getPositionInCompas() );
						}
					});
			this.golpes = sorted;
		}

		public String getName() {
			return name;
		}

		public List<Golpe> getGolpes() {
			return golpes;
		}

		public ClapId getClapId() {
			return clap;
		}
	}

	public static final class Golpe {
		private final double position;

		public Golpe(
				final double positionInCompas) {
			this.position = positionInCompas;
		}

		public double getPositionInCompas() {
			return position;
		}
	}
}

