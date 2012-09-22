/* *********************************************************************** *
 * project: org.jcompas.*
 * ClapReader.java
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

import java.io.File;
import java.io.FileNotFoundException;

import java.util.HashMap;
import java.util.Map;

import org.jcompas.model.sound.Clap;
import org.jcompas.model.sound.MonoSoundClap;

/**
 * @author thibautd
 */
public class ClapReader {
	private final Map<String, Clap> cache = new HashMap<String, Clap>();

	public Clap createClap(final String directory) throws FileNotFoundException {
		Clap clap = cache.get( directory );

		if (clap == null) {
			File f = new File( IOUtils.SOUNDS_LOCATION.getPath() + "/"+directory );
			clap = new MonoSoundClap( directory , f.listFiles()[0] );
			cache.put( directory , clap );
		}

		return clap;
	}
}

