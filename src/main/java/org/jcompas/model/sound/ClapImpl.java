/* *********************************************************************** *
 * project: org.jcompas.*
 * MonoSoundClap.java
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.jcompas.model.JCompasGlobal;

/**
 * @author thibautd
 */
public class ClapImpl implements Clap {
	private final byte[] sound;
	private final AudioFormat format;
	private final String name;


	public ClapImpl(
			final String name,
			final File data) throws FileNotFoundException {
		this( name , new FileInputStream( data ) );
	}

	public ClapImpl(
			final String name,
			final InputStream data) {
		this.name = name;
		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream( data );
			ByteArrayOutputStream array = new ByteArrayOutputStream();
			this.format = audio.getFormat();

			byte[] buffer = new byte[20000];
			int nRead = audio.read( buffer );
			while ( nRead > 0 ) {
				array.write( buffer , 0 , nRead );
				nRead = audio.read( buffer );
			}

			this.sound = array.toByteArray();
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
		return sound;
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

