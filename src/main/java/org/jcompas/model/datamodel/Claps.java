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

/**
 * @author thibautd
 */
public class Claps {
	private Map<ClapId, Clap> claps = new LinkedHashMap<ClapId, Clap>();

	public Clap getClap(final ClapId id) {
		return claps.get( id );
	}

	// package: modifications should be done via the data model
	void addClap(final Clap clap) {
		final Clap old = claps.put( clap.getId() , clap );
		if ( old != null ) throw new IllegalStateException( "already a clap "+clap.getId() );
	}
}

