/* *********************************************************************** *
 * project: org.jcompas.*
 * IOUtils.java
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
package org.jcompas.model.io;

import java.net.URL;

/**
 * @author thibautd
 */
public class Paths {
	private static final URL DEFAULT_SOUNDS_LOCATION = ClassLoader.getSystemResource( "sounds/" );
	private static final URL DEFAULT_PATTERNS_LOCATION = ClassLoader.getSystemResource( "patterns/" );
	private static final URL DEFAULT_SOUND_CONFIG_LOCATION = ClassLoader.getSystemResource( "sounds/params.xml" );

	private final URL soundsLocation;
	private final URL patternsLocation;
	private final URL soundConfigLocation;

	public Paths() {
		this( DEFAULT_SOUNDS_LOCATION,
				DEFAULT_PATTERNS_LOCATION,
				DEFAULT_SOUND_CONFIG_LOCATION );
	}

	public Paths(
			final URL soundsLocation,
			final URL patternsLocation,
			final URL soundConfigLocation) {
		this.soundsLocation = soundsLocation;
		this.patternsLocation = patternsLocation;
		this.soundConfigLocation = soundConfigLocation;
	}
	
	public URL getSoundsLocation() {
		return soundsLocation;
	}

	public URL getPatternsLocation() {
		return patternsLocation;
	}

	public URL getSoundConfigLocation() {
		return soundConfigLocation;
	}
}

