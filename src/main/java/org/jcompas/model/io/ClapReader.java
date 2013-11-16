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
import java.io.FilenameFilter;
import java.io.FileNotFoundException;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import org.jcompas.model.sound.Clap;
import org.jcompas.model.sound.RandomizedClap;
import org.jcompas.model.SoundConfig;

/**
 * @author thibautd
 */
public class ClapReader {
	private static final Logger log =
		Logger.getLogger(ClapReader.class);

	private final Map<String, Clap> cache = new HashMap<String, Clap>();
	private final SoundConfig config;

	public ClapReader() {
		this( new SoundConfig( Paths.getSoundConfigLocation() ) );
	}

	public ClapReader(final SoundConfig config) {
		this.config = config;
	}

	public Clap createClap(final String directory) {
		Clap clap = cache.get( directory );

		if (clap == null) {
			File f = new File( Paths.getSoundsLocation().getPath() + "/"+directory );
			log.debug( "reading sound directory "+f );
			log.debug( "volume is "+(config.getVolume( directory )*100)+"%" );
			clap = new RandomizedClap(
					directory,
					f.listFiles(
						new FilenameFilter() {
							@Override
							public boolean accept(File dir, String name) {
								return name.endsWith( ".wav" );
							}
						}),
					config.getVolume( directory ),
					config.getAttenuations( directory ));
			cache.put( directory , clap );
		}

		return clap;
	}
}

