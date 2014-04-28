/* *********************************************************************** *
 * project: org.jcompas.*
 * ConvertPatterns.java
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
package org.jcompas.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.jcompas.model.io.Paths;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * @author thibautd
 */
public class ConvertPatterns {
	private static final Logger log =
		Logger.getLogger(ConvertPatterns.class);

	public static void main(final String[] args) throws Exception {
		final Paths paths = new Paths();
		final File[] patterns = new File(paths.getPatternsLocation().toURI())
				.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.endsWith(".xml");
					}
				});

		for (File f : patterns) {
			final Document doc = convert(f);
			write( doc, "conv/"+f.getName() );
		}
	}

	private static void write(
			final Document doc,
			final String outpath)
				throws IOException {
		log.info( "write file "+outpath );
		final XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());

		final FileWriter writer =
				new FileWriter(
					outpath );
		xmlOutput.output(
				doc,
				writer );
		writer.close();
	}

	private static Document convert(final File f) throws JDOMException,
			IOException {
		log.info( "convert file "+f.getPath() );
		final Element old = new SAXBuilder().build( f ).getRootElement();

		final Element root = new Element( "pattern" );
		root.setAttribute( "refId" , old.getAttribute( "name" ).getValue() );
		root.setAttribute( "nCompas" , old.getAttribute( "nCompas" ).getValue() );
		root.setAttribute( "nRepeat" , old.getAttribute( "nRepeat" ).getValue() );

		final Element estilos = new Element( "estilos" );
		root.addContent( estilos );

		for ( Element est : old.getChildren( "estilo" ) ) {
			final Element newEstilo = new Element( "estilo" );
			estilos.addContent( newEstilo );
			newEstilo.setAttribute(
					"refId",
					getEstiloId(
						est.getAttributeValue(
							"file" ) ) );
		}

		for ( Element m : old.getChildren( "musician" ) ) {
			for ( Element s : m.getChildren( "sound" ) ) {
				final Element line = new Element( "line" );
				root.addContent( line );

				line.setAttribute(
					"name",
					"" );

				line.setAttribute(
						"soundRefId",
						getSoundId(
							s.getAttributeValue(
								"directory" ) ) );

				line.addContent( s.getText() );
			}
		}

		return new Document(
				root,
				new DocType(
					"pattern",
					"jcompas_pattern_v1.dtd" ) );
	}

	private static String getSoundId(final String directory) {
		if ( "palmas/sonora/fuerte/".equals( directory ) ) return "palma sonora fuerte";
		if ( "palmas/sonora/baja/".equals( directory ) ) return "palma sonora baja";
		if ( "palmas/sorda/fuerte/".equals( directory) ) return "palma sorda fuerte";
		if ( "palmas/sorda/baja/".equals( directory) ) return "palma sorda baja";
		if ( "percusion/cajon/alto/fuerte/".equals( directory) ) return "cajon alto fuerte";
		if ( "percusion/mesa/sonora/".equals( directory) ) return "mesa sonora";
		if ( "percusion/mesa/sorda/".equals( directory) ) return "mesa sorda";
		if ( "percusion/foot/".equals( directory) ) return "foot";
		return null;
	}

	private static String getEstiloId(final String file) {
		if ( file.endsWith( "alegrias.xml" ) ) return "Alegrías";
		if ( file.endsWith( "bulerias-12-5.xml" ) ) return "Medio Compás 12->5";
		if ( file.endsWith( "bulerias-6-11.xml" ) ) return "Medio Compás 6-11";
		if ( file.endsWith( "bulerias-basicas.xml" ) ) return "Bulerías Clásicas";
		if ( file.endsWith( "bulerias-modernas.xml" ) ) return "Bulerías Modernas";
		if ( file.endsWith( "siguiriyas-12.xml" ) ) return "Siguiriya en 12";
		if ( file.endsWith( "siguiriyas.xml" ) ) return "Siguiriya en 5";
		if ( file.endsWith( "soleares.xml" ) ) return "Soleá";

		return null;
	}
}

