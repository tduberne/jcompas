/* *********************************************************************** *
 * project: org.jcompas.*
 * Reloj.java
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
package org.jcompas.view;

import javax.swing.JPanel;

/**
 * Interface to be implemented by visual components indicating
 * the position in the compas.
 * A Reloj object should be as "dead" as possible: the movement is
 * handled by the RelojRunner.
 * @author thibautd
 */
public interface Reloj {
	/**
	 * Returns the component to add in the UI
	 * @return the component
	 */
	public JPanel getView();

	/**
	 * Fires the update of the view if necessary.
	 *
	 * @param fraction a number between 0 and 1 indicating
	 * the position in the compas.
	 */
	public void notifyCompasFraction(final double fraction);
}

