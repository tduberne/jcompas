/* *********************************************************************** *
 * project: org.jcompas.*
 * PaloReader.java
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
import java.io.FilenameFilter;

import org.apache.log4j.Logger;
import org.jcompas.model.datamodel.DataModel;

/**
 * @author thibautd
 */
public class ModelReader {
	private static final Logger log =
		Logger.getLogger(ModelReader.class);

	private static final FilenameFilter xmlFilter =
		new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith( ".xml" );
			}
		};

	private final DataModel model;

	public ModelReader( final DataModel model ) {
		this.model = model;
	}

	public void read( final Paths paths ) {
		try {
			log.debug( "getting sounds from "+paths.getSoundConfigLocation() );
			new ClapReader( paths , model ).readFile(
					new File( paths.getSoundConfigLocation().toURI() ) );

			log.debug( "getting estilos from "+paths.getPatternsLocation() );
			final File[] estilos = new File( paths.getEstilosLocation().toURI() ).listFiles( xmlFilter );
			final EstiloReader estiloReader = new EstiloReader( model );

			for (File f : estilos) {
				estiloReader.readFile( f );
			}

			log.debug( "getting patterns from "+paths.getPatternsLocation().toURI() );
			final File[] patterns = new File( paths.getPatternsLocation().toURI() ).listFiles( xmlFilter );
			final PatternReader patternReader = new PatternReader( model );

			for (File f : patterns) {
				patternReader.readFile( f );
			}
		}
		catch (Exception e) {
			throw new JCompasImportException(
					"could not import palos",
					e);
		}
	}
}

