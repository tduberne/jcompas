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
package org.jcompas.control;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;

import org.apache.log4j.Logger;
import org.jcompas.model.JCompasGlobal;
import org.jcompas.model.datamodel.Pattern;
import org.jcompas.model.sound.MetronomeData;
import org.jcompas.model.sound.SoundUtils;

/**
 * Basically a pattern mixer.
 * @author thibautd
 */
public final class MetronomeRunner implements InfinitePlayer {
	private static final Logger log = Logger.getLogger(MetronomeRunner.class);
	private final MetronomeData metronome;
	private final AudioFormat format;
	private final SourceDataLine line;
	private Feeder feeder = null;

	public MetronomeRunner(final MetronomeData m) {
		this.metronome = m;
		// Oops, this pattern just vanishes afterwards...
		// Try to find a CLEAN way to play it (or another CLEAN way
		// to identify the AudioFormat)
		Pattern pattern = metronome.getNextPattern();
		format = SoundUtils.identifyAudioFormat(pattern);
		try {
			line = SoundUtils.acquireLine(format);
		} catch (Exception e) {
			JCompasGlobal.notifyException(
					"exception while initializing metronome", e);
			throw new RuntimeException(e);
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	// interface
	// /////////////////////////////////////////////////////////////////////////
	@Override
	public void start(final TimedLatch startLatch, final long compasLengthMilli) {
		line.start();
		feeder = new Feeder(startLatch, line, metronome, format,
				compasLengthMilli);
		new Thread(feeder).start();
	}

	@Override
	public void stop() {
		// order is important!
		// stopping the line stops the blocking behavior of
		// write(), and we do not want the feeder to loop again
		// and refeed the line!
		log.debug("stopping feeder");
		if (feeder != null)
			feeder.run = false;
		if (line != null) {
			log.debug("stopping line");
			line.stop();
			log.debug("flushing line");
			line.flush();
		}
		feeder = null;
	}

	// /////////////////////////////////////////////////////////////////////////
	// processing
	// /////////////////////////////////////////////////////////////////////////
	private static class Feeder implements Runnable {
		// contrary to what the documentation says, the line does not stop
		// in the middle of a buffer (at least on linux).
		// This is a compromise between latency and allowing the thread to sleep.
		// Half a second seems to be a good value.
		private static final int MAX_BUFFER_DUR = 500;
		// TODO: pass to config file
		private static final int MAX_IMPRECISION_MILLI = 10;
		private final Random random = new Random();
		private final int maxBytesInBuffer;

		private boolean run = true;
		private final MetronomeData metronome;
		private final AudioFormat format;
		private final int nBytesPerCompas;
		private final int nFramesPerCompas;
		private final int nSamplesPerCompas;
		private final int frameSize;
		private final SourceDataLine line;
		private final TimedLatch startLatch;
		private final double frameDurationMilli;

		public Feeder(final TimedLatch startLatch, final SourceDataLine line,
				final MetronomeData metronome, final AudioFormat format,
				final long compasLengthMillisec) {
			this.format = format;
			this.startLatch = startLatch;
			this.line = line;
			this.metronome = metronome;

			frameDurationMilli = 1000 / format.getFrameRate();
			log.debug("frame duration in ms: " + frameDurationMilli);
			double dnFramesPerCompas = compasLengthMillisec
					/ frameDurationMilli;
			log.debug(dnFramesPerCompas + " frames per compas.");
			nFramesPerCompas = (int) dnFramesPerCompas;
			frameSize = format.getFrameSize();
			nBytesPerCompas = nFramesPerCompas * frameSize;
			nSamplesPerCompas = nBytesPerCompas
					/ (format.getSampleSizeInBits() / 8);
			if (nBytesPerCompas == Integer.MAX_VALUE) {
				throw new RuntimeException("overflow!");
			}
			maxBytesInBuffer = (int) (MAX_BUFFER_DUR / frameDurationMilli)
					* frameSize;
			log.debug("max buffer size: " + maxBytesInBuffer);
		}

		@Override
		public void run() {
			byte[] buffer = mixPattern(metronome.getNextPattern());

			try {
				startLatch.await();
			}
			catch (BrokenBarrierException e) {
				throw new RuntimeException( e );
			}
			catch (InterruptedException e) {
				throw new RuntimeException( e );
			}
			while (run) {
				int start = 0;
				int length = buffer.length;
				
				while ( run && (length - start > 0)) {
					start += line.write(
							buffer,
							start,
							Math.min(length - start, maxBytesInBuffer));
				}
				buffer = mixPattern( metronome.getNextPattern() );
			}
		}

		private byte[] mixPattern(final Pattern pattern) {
			double[] ds = new double[ nSamplesPerCompas ];

			for (int i=0; i<ds.length; i++) {
				ds[ i ] = 0;
			}

			for (Pattern.ClapLine m : pattern.getMusicians()) {
				double[] mDs = SoundUtils.convertSoundToDouble(
						format,
						catMusician( m ));

				for (int i=0; i < ds.length; i++) {
					ds[ i ] += mDs[ i ];
				}
			}

			return SoundUtils.convertSoundToBytes( format , ds );
		}

		private byte[] catMusician(final Pattern.ClapLine clapLine) {
			byte[] bytes = new byte[ nBytesPerCompas ];

			for (int i=0; i<bytes.length; i++) {
				bytes[ i ] = 0;
			}

			for (Pattern.Golpe golpe : clapLine.getGolpes()) {
				byte[] sound = golpe.getClap().getSoundData();
				int frameNr = (int) (golpe.getPositionInCompas() * nFramesPerCompas);
				// "swing": randomize a little
				// the aim is more attractive sound and avoiding clipping when the same
				// sound is played by several musicians simultaneously
				int frameShift = (int) (random.nextDouble() * MAX_IMPRECISION_MILLI / frameDurationMilli);
				if (random.nextBoolean()) frameShift = -frameShift;
				frameNr += frameShift;
				if (frameNr < 0) frameNr = 0;
				final int byteNr = frameNr * frameSize;

				for (int i=0; i < sound.length; i++) {
					if (byteNr + i < bytes.length) {
						bytes[ byteNr + i ] = sound[ i ];
					}
				}
			}

			return bytes;
		}
	}
}

