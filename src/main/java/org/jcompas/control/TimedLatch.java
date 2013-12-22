/* *********************************************************************** *
 * project: org.jcompas.*
 * TimedLatch.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2013 by the members listed in the COPYING,        *
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

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * @author thibautd
 */
public class TimedLatch {
	private final CyclicBarrier barrier;
	private final CountDownLatch releaseTimeSetLatch = new CountDownLatch(1);
	private long releaseTime = Long.MIN_VALUE;

	public TimedLatch(final int nParties) {
		this.barrier = new CyclicBarrier(nParties);
	}

	public long await() throws InterruptedException, BrokenBarrierException {
		// first wait for everybody to be ready
		barrier.await();
		// then wait for release time to be set
		releaseTimeSetLatch.await();
		return releaseTime;
	}

	public synchronized void release() throws InterruptedException,
			BrokenBarrierException {
		if ( releaseTime != Long.MIN_VALUE ) throw new IllegalStateException();
		barrier.await();
		releaseTime = System.currentTimeMillis();
		releaseTimeSetLatch.countDown();
	}

	public long getReleaseTime() {
		return releaseTime;
	}
}

