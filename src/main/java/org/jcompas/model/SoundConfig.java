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
	private final Map<String, Attenuations> attenuationsMap = new HashMap<String, Attenuations>();
	public static final String VOLUMES_TAG = "volumes";
	public static final String VOLUME_TAG = "volume";
	public static final String VOLUME_DIR_ATT = "dir";
	public static final String VOLUME_ATT = "volume";
	public static final String ATTENUATION_TAG = "attenuation";
	public static final String ATTENUATION_FILE_ATT = "file";
	public static final String ATTENUATION_ATT_ATT = "att";

	public SoundConfig(final URL configFile) {
		try {
			Document document = new SAXBuilder().build( configFile );

			Element volumes = document.getRootElement().getChild( VOLUMES_TAG );
			for (Element e : volumes.getChildren( VOLUME_TAG )) {
				String dir = e.getAttribute( VOLUME_DIR_ATT ).getValue();
				double vol = e.getAttribute( VOLUME_ATT ).getDoubleValue() / 100;
				log.debug( "defined volume for "+dir+"= "+vol );
				volumesMap.put(
						dir,
						vol );
				Attenuations atts = new Attenuations();
				attenuationsMap.put( dir , atts );
				for (Element attElem : e.getChildren( ATTENUATION_TAG )) {
					String file = attElem.getAttribute( ATTENUATION_FILE_ATT ).getValue();
					double att = attElem.getAttribute( ATTENUATION_ATT_ATT ).getDoubleValue() / 100d;
					log.debug( "defined attenuation for "+file+"= "+att );
					atts.atts.put( file , att );
				}
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

	public Attenuations getAttenuations(final String directory) {
		Attenuations a = attenuationsMap.get( directory );
		return a != null ? a : new Attenuations();
	}

	public static class Attenuations {
		private final Map<String, Double> atts = new HashMap<String, Double>();

		public double getAttenuation(final String file) {
			Double vol = atts.get( file );
			return vol != null ? vol : 1;
		}
	}
}

