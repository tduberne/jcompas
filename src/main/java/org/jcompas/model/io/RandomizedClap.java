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
package org.jcompas.model.io;

import java.util.Random;

import javax.sound.sampled.AudioFormat;
import org.jcompas.model.datamodel.ClapId;
import org.jcompas.model.sound.Clap;

/**
 * @author thibautd
 */
class RandomizedClap implements Clap {
	private final ClapId id;
	private final byte[][] sounds;
	private final AudioFormat format;
	private final Random random = new Random();

	RandomizedClap(
			final ClapId id,
			final byte[][] sounds,
			final AudioFormat format) {
		this.id = id;
		this.sounds = sounds;
		this.format = format;
	}

	@Override
	public byte[] getSoundData() {
		return sounds[random.nextInt(sounds.length)];
	}

	@Override
	public AudioFormat getAudioFormat() {
		return format;
	}

	@Override
	public ClapId getId() {
		return id;
	}

}

