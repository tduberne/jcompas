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
package org.jcompas.model.datamodel;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

/**
 * Gives access to the available palos.
 * @author thibautd
 */
public final class Palos {
	private static final Logger log = Logger.getLogger( Palos.class );
	private final Map<PaloId, Palo> palos = new LinkedHashMap<PaloId, Palo>();

	public Palos() {}

	public Collection<PaloId> getAvailablePalos() {
		return palos.keySet();
	}

	public Palo getPalo( final PaloId id ) {
		final Palo palo = palos.get( id );
		if ( palo == null ) {
			log.error( "no palo "+id );
			log.error( "valid values: "+palos.keySet() );
			throw new NoSuchElementException( "no palo "+id ); 
		}
		return palo;
	}

	Palo getOrCreatePalo( final PaloId name ) {
		Palo p = palos.get( name );
		if ( p == null ) {
			p = new Palo( name );
			palos.put( name , p );
		}
		return p;
	}
}

