/* *********************************************************************** *
 * project: org.jcompas.*
 * DataModel.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2014 by the members listed in the COPYING,        *
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


/**
 * This class structures and stores all the data.
 * The basic data model is the following:
 * <ul>
 * <li> Top-level classification is the {@link Palo}, which is just a name.
 * <li> A palo consists of one or more styles ({@link Estilo}), which define
 * things like beat name and typical tempo.
 * <li> What is played are {@link Pattern}s, which are linked to one or more
 * {@link Estilo}s. They define lines, which define at which beats a given sound
 * is played.
 * </ul>
 * @author thibautd
 */
public class DataModel {
	private final Palos palos = new Palos();
	private final Estilos estilos = new Estilos();
	private final Patterns patterns = new Patterns();

	public Palos getPalos() {
		return palos;
	}

	public Estilos getEstilos() {
		return estilos;
	}

	public Patterns getPatterns() {
		return patterns;
	}

	public void addEstilo( final Estilo estilo ) {
		estilos.addEstilo( estilo );
		palos.getOrCreatePalo( estilo.getPalo() ).addEstilo( estilo.getId() );
	}

	public void addPattern( final Pattern pattern ) {
		patterns.addPattern( pattern );
		for ( EstiloId eId : pattern.getEstilos() ) {
			final Estilo estilo = estilos.getEstilo( eId );
			estilo.addPattern( pattern.getId() );
		}
	}
}

