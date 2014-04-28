/* *********************************************************************** *
 * project: org.jcompas.*
 * Claps.java
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
public class Claps {
	private static final Logger log =
		Logger.getLogger(Claps.class);

	private Map<ClapId, Clap> claps = new LinkedHashMap<ClapId, Clap>();

	public Clap getClap(final ClapId id) {
		final Clap clap = claps.get( id );
		if ( clap == null ) {
			log.error( "no clap "+id );
			log.error( "valid values: "+claps.keySet() );
			throw new NoSuchElementException( "no clap "+id ); 
		}
		return clap;
	}

	// package: modifications should be done via the data model
	void addClap(final Clap clap) {
		final Clap old = claps.put( clap.getId() , clap );
		if ( old != null ) throw new IllegalStateException( "already a clap "+clap.getId() );
	}
}

