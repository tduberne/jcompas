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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author thibautd
 */
public class Paths {
	private static final URL DEFAULT_SOUNDS_LOCATION = ClassLoader.getSystemResource("sounds/");
	private static final URL DEFAULT_PATTERNS_LOCATION = ClassLoader.getSystemResource("patterns/");
	private static final URL DEFAULT_ESTILOS_LOCATION = ClassLoader.getSystemResource("patterns/estilos/");
	private static final URL DEFAULT_SOUND_CONFIG_LOCATION = ClassLoader.getSystemResource("sounds/params.xml");

	private final URL soundsLocation;
	private final URL patternsLocation;
	private final URL estilosLocation;
	private final URL soundConfigLocation;

	public Paths() {
		this(DEFAULT_SOUNDS_LOCATION,
				DEFAULT_PATTERNS_LOCATION,
				DEFAULT_ESTILOS_LOCATION,
				DEFAULT_SOUND_CONFIG_LOCATION);
	}

	public Paths(final URL soundsLocation,
			final URL patternsLocation,
			final URL estilosLocation,
			final URL soundConfigLocation) {
		this.soundsLocation = soundsLocation;
		this.patternsLocation = patternsLocation;
		this.estilosLocation = estilosLocation;
		this.soundConfigLocation = soundConfigLocation;
	}

	public Paths(final File rootDirectory) {
		this(
				getChildURL(rootDirectory, "sounds/"),
				getChildURL(rootDirectory, "patterns/"),
				getChildURL(rootDirectory, "patterns/estilos/"),
				getChildURL(rootDirectory, "sounds/params.xml"));
	}

	private static URL getChildURL(
			final File rootDirectory,
			final String child) {
		if (!rootDirectory.isDirectory()) {
			throw new RuntimeException(rootDirectory + " is not a directory!");
		}

		final File childFile = new File(rootDirectory.getAbsolutePath() + "/"
				+ child);

		if (!childFile.exists()) {
			throw new RuntimeException(childFile + " does not exist!");
		}

		try {
			return childFile.toURI().toURL();
		} catch (MalformedURLException e) {
			throw new RuntimeException( e );
		}
	}

	public URL getSoundsLocation() {
		return soundsLocation;
	}

	public URL getPatternsLocation() {
		return patternsLocation;
	}

	public URL getEstilosLocation() {
		return estilosLocation;
	}

	public URL getSoundConfigLocation() {
		return soundConfigLocation;
	}
}

