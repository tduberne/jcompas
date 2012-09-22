/* *********************************************************************** *
 * project: org.jcompas.*
 * Tense.java
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
package org.jcompas.model;

/**
 * @author thibautd
 */
public final class Beat {
	private final String name;
	private final boolean isStrong;

	public Beat(final int name, final boolean isStrong) {
		this ( ""+name , isStrong );
	}

	public Beat(final String name, final boolean isStrong) {
		this.name = name;
		this.isStrong = isStrong;
	}

	public String getName() {
		return name;
	}

	public boolean isStrong() {
		return isStrong;
	}

	public String toString() {
		return "["+getClass().getSimpleName()+": "+name+(isStrong ? "s" : "w")+"]";
	}
}

