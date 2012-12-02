/* *********************************************************************** *
 * project: org.jcompas.*
 * JCompasGlobal.java
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

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

/**
 * Groups compile-time options and general interest static methods.
 * @author thibautd
 */
public class JCompasGlobal {
	private static final Logger log =
		Logger.getLogger(JCompasGlobal.class);

	private static final String versionId = "0.0.1-SNAPSHOT";

	private JCompasGlobal() {}

	public static String getVersionId() {
		return versionId;
	}

	public static void notifyException(
			final String message,
			final Exception e) {
		log.error( message , e );
		System.exit( 1 );
	}

	public static void userWarning(
			final String message) {
		JOptionPane.showMessageDialog(
				null,
				message,
				"Warning",
				JOptionPane.WARNING_MESSAGE);
	}
}

