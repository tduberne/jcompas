/* *********************************************************************** *
 * project: org.jcompas.*
 * CompasInformation.java
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

import java.util.Collections;
import java.util.List;

/**
 * A data structure giving access to compas information.
 * @author thibautd
 */
public final class CompasInformation {
	private final String name;
	private final int typicalBpm;
	private final List<Beat> beats;

	public CompasInformation(
			final String name,
			final int bpm,
			final List<Beat> beats) {
		this.name = name;
		this.typicalBpm = bpm;
		this.beats = Collections.unmodifiableList( beats );
	}

	public String getName() {
		return name;
	}

	public int getTypicalBpm() {
		return typicalBpm;
	}

	public List<Beat> getBeats() {
		return beats;
	}

	public int getBeatsCount() {
		return beats.size();
	}

	public String toString() {
		return "["+getClass().getSimpleName()+": "+name+", "+typicalBpm+"bpm, "+beats+"]";
	}
}

