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

import org.jcompas.model.CompasInformation;

/**
 * @author thibautd
 */
public final class SimpleMetronome implements MetronomeData {
	private final Pattern pattern;
	private final CompasInformation compasInformation;

	public SimpleMetronome(
			final Pattern pattern,
			final CompasInformation compasInformation) {
		this.pattern = pattern;
		this.compasInformation = compasInformation;
	}

	@Override
	public Pattern getNextPattern() {
		return pattern;
	}

	@Override
	public CompasInformation getCompasInfo() {
		return compasInformation;
	}
}

