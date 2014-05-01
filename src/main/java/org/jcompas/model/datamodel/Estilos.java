/* *********************************************************************** *
 * project: org.jcompas.*
 * Estilos.java
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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

/**
 * @author thibautd
 */
public class Estilos {
	private static final Logger log =
		Logger.getLogger(Estilos.class);

	private Map<EstiloId, Estilo> estilos = new LinkedHashMap<EstiloId, Estilo>();

	public Estilo getEstilo(final EstiloId id) {
		final Estilo estilo = estilos.get( id );
		if ( estilo == null ) {
			log.error( "no estilo "+id );
			log.error( "valid values: "+estilos.keySet() );
			throw new NoSuchElementException( "no estilo "+id ); 
		}
		return estilo;
	}

	// package: modifications should be done via the data model
	void addEstilo(final Estilo estilo) {
		final Estilo old = estilos.put( estilo.getId() , estilo );
		if ( old != null ) throw new IllegalStateException( "already an estilo "+estilo.getName() );
	}
}

