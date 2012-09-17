/* *********************************************************************** *
 * project: org.jcompas.*
 * SoundUtils.java
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

import javax.sound.sampled.AudioFormat;

import org.jcompas.model.sound.Pattern;

/**
 * @author thibautd
 */
public final class SoundUtils {
	private SoundUtils() {};

	public static AudioFormat identifyAudioFormat( final Pattern p ) {
		return identifyAudioFormat( p.getMusicians().get( 0 ) );
	}

	public static AudioFormat identifyAudioFormat( final Pattern.Musician m ) {
		return identifyAudioFormat( m.getGolpes().get( 0 ) );
	}

	public static AudioFormat identifyAudioFormat( final Pattern.Golpe p ) {
		return p.getClap().getAudioFormat();
	}
}

