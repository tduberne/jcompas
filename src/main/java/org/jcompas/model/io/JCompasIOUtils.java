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

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author thibautd
 */
public class JCompasIOUtils {
	private static final Logger log =
		Logger.getLogger(JCompasIOUtils.class);

	public static SAXBuilder createSaxBuilder() {
		final SAXBuilder builder = new SAXBuilder( XMLReaders.DTDVALIDATING );
		builder.setEntityResolver( new ClasspathResolver( builder.getEntityResolver() ) );
		return builder;
	}

	private static class ClasspathResolver implements EntityResolver {
		final EntityResolver delegate;

		public ClasspathResolver( final EntityResolver delegate ) {
			this.delegate = delegate;
		}

		public InputSource resolveEntity(
				final String publicId,
				final String systemId)
					throws IOException, SAXException {
			if ( !systemId.matches( ".*\\.dtd" ) ) {
				return delegate.resolveEntity( publicId , systemId );
			}

			final String[] path = systemId.split( "/" );
			final String classpath = "/dtd/"+path[ path.length - 1 ];
			final InputStream stream = getClass().getResourceAsStream( classpath  );

			if ( stream != null ) {
				log.info( classpath+" found in classpath, using it." );
				return new InputSource( stream );
			}

			log.warn( "could not find "+classpath+"in classpath, reverting to default behavior!" );
			return delegate.resolveEntity( publicId , systemId );
		}
	}
}

