/* *********************************************************************** *
 * project: org.jcompas.*
 * ClapReader.java
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

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.jcompas.model.datamodel.ClapId;
import org.jcompas.model.datamodel.DataModel;
import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

/**
 * @author thibautd
 */
public class ClapReader {
	private static final Logger log = Logger.getLogger(ClapReader.class);

	private final Paths paths;
	private final DataModel model;

	public ClapReader(
			final Paths paths,
			final DataModel model) {
		this.paths = paths;
		this.model = model;
	}

	public void readFile(final File configFile) {
		try {
			final Document document = JCompasIOUtils.createSaxBuilder().build(configFile);
			parseDocument(document);
		}
		catch (JDOMException e) {
			throw new RuntimeException(e);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void parseDocument(final Document document)
			throws DataConversionException {
		for (Element e : document.getRootElement().getChildren( "sound" )) {
			parseSound( e );
		}
	}

	private void parseSound(final Element e)
			throws DataConversionException {
		final String id = e.getAttribute( "refId" ).getValue();

		final RandomizedClapBuilder builder =
			new RandomizedClapBuilder(
					new ClapId( id ) );

		for (Element attElem : e.getChildren( "soundFile" )) {
			final String file = attElem.getAttribute( "path" ).getValue();
			final double att = attElem.getAttribute( "volume" ).getDoubleValue() / 100d;
			log.debug( "defined attenuation for "+file+"= "+att );
			builder.withSound(
					paths.getSoundsLocation().getPath() + file,
					att );
		}

		model.addClap( builder.build() );
	}
}

