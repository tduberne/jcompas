/* *********************************************************************** *
 * project: org.jcompas.*
 * Patterns.java
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
public class Patterns {
	private static final Logger log =
		Logger.getLogger(Patterns.class);

	private Map<PatternId, Pattern> patterns = new LinkedHashMap<PatternId, Pattern>();

	public Pattern getPattern(final PatternId id) {
		final Pattern pattern = patterns.get( id );
		if ( pattern == null ) {
			log.error( "no pattern "+id );
			log.error( "valid values: "+patterns.keySet() );
			throw new NoSuchElementException( "no pattern "+id ); 
		}
		return pattern;
	}

	// package: modifications should be done via the data model
	void addPattern(final Pattern pattern) {
		final Pattern old = patterns.put( pattern.getId() , pattern );
		if ( old != null ) throw new IllegalStateException( "already a pattern "+pattern.getName() );
	}
}

