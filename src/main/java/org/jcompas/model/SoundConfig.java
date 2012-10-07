/* *********************************************************************** *
 * project: org.jcompas.*
 * SoundConfig.java
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

import java.net.URL;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

/**
 * @author thibautd
 */
public class SoundConfig {
	private static final Logger log =
		Logger.getLogger(SoundConfig.class);

	private final Map<String, Double> volumesMap = new HashMap<String, Double>();
	public static final String VOLUMES_TAG = "volumes";
	public static final String VOLUME_TAG = "volume";
	public static final String VOLUME_DIR_ATT = "dir";
	public static final String VOLUME_ATT = "volume";

	public SoundConfig(final URL configFile) {
		try {
			Document document = new SAXBuilder().build( configFile );

			Element volumes = document.getRootElement().getChild( VOLUMES_TAG );
			for (Element e : volumes.getChildren( VOLUME_TAG )) {
				volumesMap.put(
						e.getAttribute( VOLUME_DIR_ATT ).getValue(),
						e.getAttribute( VOLUME_ATT ).getDoubleValue() / 100 );
			}
		}
		catch (Exception e) {
			log.warn( "got error", e );
			log.warn( "config may be left uninitialized" );
		}
	}

	/**
	 * @return the volume, as a double, 0 being nothing and 1 no modification
	 */
	public double getVolume(final String directory) {
		Double vol = volumesMap.get( directory );
		return vol != null ? vol : 1;
	}
}

