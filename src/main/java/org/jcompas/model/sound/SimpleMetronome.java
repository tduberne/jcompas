/* *********************************************************************** *
 * project: org.jcompas.*
 * SimpleMetronome.java
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

import java.util.List;
import java.util.Random;

import org.jcompas.model.CompasInformation;

/**
 * @author thibautd
 */
public final class SimpleMetronome implements MetronomeData {
	private final Random random = new Random();
	private final List<Pattern> patterns;
	private final CompasInformation compasInformation;

	private Pattern currentPattern = null;
	private int remainingPlays = 0;

	public SimpleMetronome(
			final List<Pattern> patterns,
			final CompasInformation compasInformation) {
		this.patterns = patterns;
		this.compasInformation = compasInformation;
	}

	@Override
	public Pattern getNextPattern() {
		if (remainingPlays <= 0) {
			currentPattern = patterns.get( random.nextInt( patterns.size() ) );
			remainingPlays = currentPattern.getTypicalNumberOfRepetitions();
		}
		remainingPlays--;
		return currentPattern;
	}

	@Override
	public CompasInformation getCompasInfo() {
		return compasInformation;
	}
}

