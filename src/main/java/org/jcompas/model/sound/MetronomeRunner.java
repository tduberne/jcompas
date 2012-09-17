/* *********************************************************************** *
 * project: org.jcompas.*
 * MetronomeRunner.java
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
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import org.apache.log4j.Logger;

import org.jcompas.model.InfinitePlayer;
import org.jcompas.model.JCompasGlobal;
import org.jcompas.model.sound.Pattern;

/**
 * Basically a pattern mixer.
 * @author thibautd
 */
public final class MetronomeRunner implements InfinitePlayer {
	private static final Logger log = Logger.getLogger( MetronomeRunner.class );
	private final MetronomeData metronome;
	private SourceDataLine line = null;
	private Feeder feeder = null;

	public MetronomeRunner(final MetronomeData m) {
		this.metronome = m;
	}

	// /////////////////////////////////////////////////////////////////////////
	// interface
	// /////////////////////////////////////////////////////////////////////////
	@Override
	public void start(
			final long startTime,
			final int compasLengthMilli) {
		try {
			// Oops, this pattern just vanishes afterwards...
			// Try to find a CLEAN way to play it (or another CLEAN way
			// to identify the AudioFormat)
			Pattern pattern = metronome.getNextPattern();
			
			AudioFormat format = SoundUtils.identifyAudioFormat( pattern );
			line = (SourceDataLine) AudioSystem.getLine(
						new DataLine.Info(
							SourceDataLine.class,
							format));
			line.open( format );
			line.start();
			feeder = new Feeder(
					startTime,
					line,
					metronome,
					format,
					compasLengthMilli);
			new Thread( feeder ).start();
		}
		catch (Exception e) {
			JCompasGlobal.notifyException(
					"exception while playing sound",
					e);
		}
	}

	@Override
	public void stop() {
		// order is important!
		// stopping the line stops the blocking behavior of
		// write(), and we do not want the feeder to loop again
		// and refeed the line!
		if ( feeder != null ) feeder.run = false;
		if ( line != null ) line.stop();
		line = null;
		feeder = null;
	}

	// /////////////////////////////////////////////////////////////////////////
	// processing
	// /////////////////////////////////////////////////////////////////////////
	private static class Feeder implements Runnable {
		private boolean run = true;
		private final MetronomeData metronome;
		private final int nBytesPerCompas;
		private final int nFramesPerCompas;
		private final int frameSize;
		private final SourceDataLine line;
		private final long startTime;

		public Feeder(
				final long startTime,
				final SourceDataLine line,
				final MetronomeData metronome,
				final AudioFormat format,
				final long compasLengthMillisec) {
			this.startTime = startTime;
			this.line = line;
			this.metronome = metronome;

			double frameDurationMilli = 1000 / format.getFrameRate();
			log.debug( "frame duration in ms: "+frameDurationMilli );
			double dnFramesPerCompas = compasLengthMillisec / frameDurationMilli;
			log.debug( dnFramesPerCompas+" frames per compas." );
			nFramesPerCompas = (int) dnFramesPerCompas;
			frameSize = format.getFrameSize();
			nBytesPerCompas = nFramesPerCompas * frameSize;
			if (nBytesPerCompas == Integer.MAX_VALUE) {
				throw new RuntimeException( "overflow!" );
			}
		}

		@Override
		public void run() {
			byte[] buffer = mixPattern( metronome.getNextPattern() );
			try {
				Thread.sleep( startTime - System.currentTimeMillis() );
			} catch (InterruptedException e) {
				JCompasGlobal.notifyException(
						"could not sleep",
						e );
			}
			while (run) {
				int start = 0;
				int length = buffer.length;
				
				while (length - start > 0) {
					start += line.write(
							buffer,
							start,
							length - start);
				}
				buffer = mixPattern( metronome.getNextPattern() );
			}
		}

		private byte[] mixPattern(final Pattern pattern) {
			byte[] bytes = new byte[ nBytesPerCompas ];


			for (int i=0; i<bytes.length; i++) {
				bytes[ i ] = 0;
			}

			for (Pattern.Musician m : pattern.getMusicians()) {
				byte[] mBytes = catMusician( m );

				for (int i=0; i < bytes.length; i++) {
					// will not be nice with several musicians...
					bytes[ i ] += mBytes[ i ];
				}
			}

			return bytes;
		}

		private byte[] catMusician(final Pattern.Musician musician) {
			byte[] bytes = new byte[ nBytesPerCompas ];

			for (int i=0; i<bytes.length; i++) {
				bytes[ i ] = 0;
			}

			for (Pattern.Golpe golpe : musician.getGolpes()) {
				byte[] sound = golpe.getClap().getSoundData();
				final int frameNr = (int) (golpe.getPositionInCompas() * nFramesPerCompas);
				final int byteNr = frameNr * frameSize;

				for (int i=0; i < sound.length; i++) {
					bytes[ byteNr + i ] = sound[ i ];
				}
			}

			return bytes;
		}
	}
}
