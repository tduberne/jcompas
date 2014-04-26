/* *********************************************************************** *
 * project: org.jcompas.*
 * JCompasIOUtils.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2014 by the members listed in the COPYING,        *
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

import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;

/**
 * @author thibautd
 */
public class JCompasIOUtils {
	public static SAXBuilder createSaxBuilder() {
		return new SAXBuilder( XMLReaders.DTDVALIDATING );
	}
}

