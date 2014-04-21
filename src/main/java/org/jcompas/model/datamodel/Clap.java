/* *********************************************************************** *
 * project: org.jcompas.*
 * Clap.java
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
package org.jcompas.model.datamodel;

import javax.sound.sampled.AudioFormat;

/**
 * Abstraction for a metronome "clap"
 * @author thibautd
 */
public interface Clap {
	/**
	 * Gives access to the sound raw data.
	 * It does not have to be the same sound returned over and over
	 * (ie some randomness can be added to improve realism).
	 * @return the byte sequence, as specified by the format.
	 */
	public byte[] getSoundData();

	/**
	 * Gives a unique identifier to the clap
	 */
	public ClapId getId();

	/**
	 * Gives access to the format
	 * @return the format.
	 */
	public AudioFormat getAudioFormat();
}

