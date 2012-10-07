/* *********************************************************************** *
 * project: org.jcompas.*
 * RandomizedClap.java
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
package org.jcompas.model.sound;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.util.Random;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.jcompas.model.JCompasGlobal;

/**
 * @author thibautd
 */
public class RandomizedClap implements Clap {
	private final String name;
	private final byte[][] sounds;
	private final AudioFormat format;
	private final Random random = new Random();

	public RandomizedClap(
			final String name,
			final File[] data,
			final double volume) {
		this.name = name;
		sounds = new byte[data.length][];
		try {
			AudioFormat currentFormat = null;

			for (int i=0; i < data.length; i++) {
				AudioInputStream audio = AudioSystem.getAudioInputStream(
						new FileInputStream( data[ i ] ) );
				ByteArrayOutputStream array = new ByteArrayOutputStream();

				if (currentFormat == null) {
					currentFormat = audio.getFormat();
				}
				// TODO: else check compatibility

				byte[] buffer = new byte[20000];
				int nRead = audio.read( buffer );
				while ( nRead > 0 ) {
					array.write( buffer , 0 , nRead );
					nRead = audio.read( buffer );
				}
				this.sounds[i] = array.toByteArray();

				for (int j=0; j < sounds[i].length; j++) {
					sounds[i][j] *= volume;
				}
			}

			this.format = currentFormat;
		} catch (Exception e) {
			JCompasGlobal.notifyException( "" , e );
			// global should already exit,
			// but without that the code does not
			// compile
			throw new RuntimeException();
		}
	}

	@Override
	public byte[] getSoundData() {
		return sounds[ random.nextInt( sounds.length ) ];
	}

	@Override
	public String getSoundName() {
		return name;
	}

	@Override
	public AudioFormat getAudioFormat() {
		return format;
	}

}

