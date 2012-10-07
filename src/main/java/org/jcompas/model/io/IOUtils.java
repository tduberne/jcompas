/* *********************************************************************** *
 * project: org.jcompas.*
 * IOUtils.java
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
package org.jcompas.model.io;

import java.net.URL;

/**
 * @author thibautd
 */
public class IOUtils {
	public static final URL SOUNDS_LOCATION = ClassLoader.getSystemResource( "sounds/" );
	public static final URL PATTERNS_LOCATION = ClassLoader.getSystemResource( "patterns/" );
	public static final URL SOUND_CONFIG_LOCATION = ClassLoader.getSystemResource( "sounds/params.xml" );
}

