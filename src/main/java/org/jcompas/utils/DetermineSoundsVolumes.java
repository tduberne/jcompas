/* *********************************************************************** *
 * project: org.jcompas.*
 * DetermineSoundsVolumes.java
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
package org.jcompas.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FilenameFilter;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.apache.log4j.Logger;

import org.jcompas.model.io.IOUtils;
import org.jcompas.model.sound.SoundUtils;

/**
 * Prints the maximum amplitude for all clap file,
 * as well as recomended attenuation settings.
 * @author thibautd
 */
public class DetermineSoundsVolumes {
	private static final Logger log =
		Logger.getLogger(DetermineSoundsVolumes.class);

	public static void main(final String[] args) {
		recurse( new File( IOUtils.SOUNDS_LOCATION.getPath() ) );
	}

	private static void recurse(final File directory) {
		File[] directories = directory.listFiles(
				new FileFilter() {
					@Override
					public boolean accept(final File f) {
						return f.isDirectory();
					}});

		for (File dir : directories) {
			log.info( "analysing "+dir.getPath() );

			File[] fs = dir.listFiles(
					new FilenameFilter() {
						@Override
						public boolean accept(
							final File dir,
							final String name) {
							return name.endsWith( ".wav" );
						}
					});

			String[] names = new String[ fs.length ];
			double[] volumes = new double[ fs.length  ];
			double minVolume =  Double.POSITIVE_INFINITY;
			for ( int i=0 ; i < fs.length ; i++ ) {
				final File f = fs[ i ];
				names[ i ] = f.getPath();
				volumes[ i ] = maxAmplitude( f );
	
				log.info( "max amplitude for file "+names[ i ]+": "+volumes[ i ] );

				minVolume = Math.min( minVolume , volumes[ i ] );
			}

			for ( int i=0 ; i < fs.length ; i++ ) {
				log.info( "recommended attenuation for "+names[ i ]+": "+
						(100d * minVolume / volumes[ i ])+"%" );
			}

			recurse( dir );
		}
	}

	private static double maxAmplitude(final File soundFile) {
		double max = Double.NEGATIVE_INFINITY;
		double[] samples = getSamples( soundFile );

		for (double d : samples) {
			max = Math.max( max , d < 0 ? -d : d );
		}
		
		return max;
	}

	private static double[] getSamples(final File soundFile) {
		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream(
					new FileInputStream( soundFile ) );
			ByteArrayOutputStream array = new ByteArrayOutputStream();

			byte[] buffer = new byte[20000];
			int nRead = audio.read( buffer );
			while ( nRead > 0 ) {
				array.write( buffer , 0 , nRead );
				nRead = audio.read( buffer );
			}

			return SoundUtils.convertSoundToDouble(
						audio.getFormat(),
						array.toByteArray());
		}
		catch (Exception e) {
			throw new RuntimeException( e );
		}
	}
}

