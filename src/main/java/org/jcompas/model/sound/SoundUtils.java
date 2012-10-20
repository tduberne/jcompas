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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;

import org.jcompas.model.JCompasGlobal;
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

	public static double[] convertSoundToDouble(
			final AudioFormat format,
			final byte[] soundInBytes) {
		final ByteBuffer buffer =
			ByteBuffer.wrap( soundInBytes ).order(
					format.isBigEndian() ?
						ByteOrder.BIG_ENDIAN :
						ByteOrder.LITTLE_ENDIAN );

		final List<Double> doubles = new ArrayList<Double>();
		final int sampleSize = format.getSampleSizeInBits();
		while (buffer.hasRemaining()) {
			doubles.add( getNextDouble( sampleSize , buffer ) );
		}

		double[] array = new double[doubles.size()];

		int i = 0;
		for (double d : doubles) {
			array[ i++ ] = d;
		}

		return array;
	}

	public static byte[] convertSoundToBytes(
			final AudioFormat format,
			final double[] soundInDoubles) {
		final int sampleSize = format.getSampleSizeInBits();
		ByteBuffer buffer =
				ByteBuffer.allocate( soundInDoubles.length * sampleSize / 8 ).order(
					format.isBigEndian() ?
						ByteOrder.BIG_ENDIAN :
						ByteOrder.LITTLE_ENDIAN );

		for (double d : soundInDoubles) {
			switch (sampleSize) {
				case 8:
					buffer.put( (byte) d );
					break;
				case 16:
					buffer.putShort( (short) d );
					break;
				case 32:
					buffer.putFloat( (float) d );
					break;
				case 64:
					buffer.putDouble( (double) d );
					break;
				default:
					throw new IllegalArgumentException( "unhandled sample size "+sampleSize );
			}
		}

		return buffer.array();
	}

	private static double getNextDouble(
			final int nBits,
			final ByteBuffer buffer) {
		switch (nBits) {
			case 8: return buffer.get();
			case 16: return  buffer.getShort();
			case 32: return  buffer.getFloat();
			case 64: return  buffer.getDouble();
		}

		throw new IllegalArgumentException ( "Unhandled sample size "+nBits+" bits" );
	}
}

