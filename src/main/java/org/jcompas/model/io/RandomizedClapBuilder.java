/* *********************************************************************** *
 * project: org.jcompas.*
 * RandomizedClapBuilder.java
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
package org.jcompas.model.io;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.apache.log4j.Logger;
import org.jcompas.model.JCompasGlobal;
import org.jcompas.model.datamodel.ClapId;
import org.jcompas.model.sound.Clap;
import org.jcompas.model.sound.SoundUtils;

/**
 * @author thibautd
 */
public class RandomizedClapBuilder {
	private static final Logger log =
		Logger.getLogger(RandomizedClapBuilder.class);

	private final ClapId id;
	private final List<String> paths = new ArrayList<String>();
	private final List<Double> volumes = new ArrayList<Double>();

	public RandomizedClapBuilder(final ClapId id) {
		this.id = id;
	}

	public void withSound(final String path, final double volume) {
		if ( volume < 0 ) throw new IllegalArgumentException( "volume "+volume+" is negative" );
		if ( volume < 1 ) log.warn( "volume "+volume+" between 0 and 1. Be aware that no attenuation is 100, not 1!" );

		paths.add( path );
		volumes.add( volume );
	}

	public Clap build() {
		final byte[][] sounds = new byte[paths.size()][];
		try {
			AudioFormat currentFormat = null;

			for ( int i = 0; i < paths.size(); i++ ) {
				AudioInputStream audio =
					AudioSystem.getAudioInputStream(
							new FileInputStream(
								paths.get( i ) ));
				ByteArrayOutputStream array = new ByteArrayOutputStream();

				if (currentFormat == null) {
					currentFormat = audio.getFormat();
					log.debug("format: " + currentFormat);
				}
				// TODO: else check compatibility

				byte[] buffer = new byte[20000];
				int nRead = audio.read(buffer);
				while (nRead > 0) {
					array.write(buffer, 0, nRead);
					nRead = audio.read(buffer);
				}

				double[] soundAsDouble = SoundUtils.convertSoundToDouble(
						currentFormat, array.toByteArray());

				double fileVolume = volumes.get( i );
				for (int j = 0; j < soundAsDouble.length; j++) {
					soundAsDouble[j] *= fileVolume;
				}

				sounds[i] = SoundUtils.convertSoundToBytes(currentFormat,
						soundAsDouble);
			}

			return new RandomizedClap( id , sounds , currentFormat );
		} catch (Exception e) {
			JCompasGlobal.notifyException("", e);
			// global should already exit,
			// but without that the code does not
			// compile
			throw new RuntimeException();
		}
	}
}

