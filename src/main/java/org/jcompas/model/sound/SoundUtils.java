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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.apache.log4j.Logger;
import org.jcompas.model.datamodel.Claps;
import org.jcompas.model.datamodel.Pattern;

/**
 * @author thibautd
 */
public final class SoundUtils {
	private static Logger log = Logger.getLogger( SoundUtils.class );
	private static Map<String, SourceDataLine> activeLines = new HashMap<String, SourceDataLine>();

	private SoundUtils() {};

	public static AudioFormat identifyAudioFormat(
			final Claps claps,
			final Pattern p ) {
		return identifyAudioFormat( claps , p.getMusicians().get( 0 ) );
	}

	public static AudioFormat identifyAudioFormat(
			final Claps claps,
			final Pattern.ClapLine m ) {
		return claps.getClap( m.getClapId() ).getAudioFormat();
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
					buffer.putDouble( d );
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

	public static SourceDataLine acquireLine(final AudioFormat format) throws LineUnavailableException {
		SourceDataLine line = activeLines.get( format.toString() );
		
		if (line == null) {
			line = (SourceDataLine) AudioSystem.getLine(
						new DataLine.Info(
							SourceDataLine.class,
							format));
			line.open( format );

			log.debug( "opened line: "+line );
			line.addLineListener( new LineListener() {
				@Override
				public void update(final LineEvent event) {
					log.debug( "got event: "+event );
					log.debug( "Controls:" );
					for (Control c : event.getLine().getControls()) {
						log.debug( c );
					}
				}
			});

			activeLines.put( format.toString() , line );
		}

		return line;
	}

	public static void releaseLine(final AudioFormat format) {
		SourceDataLine l = activeLines.remove( format );
		if (l != null) l.close();
	}
}

