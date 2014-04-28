/* *********************************************************************** *
 * project: org.jcompas.*
 * PatternReader.java
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

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jcompas.model.datamodel.ClapId;
import org.jcompas.model.datamodel.DataModel;
import org.jcompas.model.datamodel.EstiloId;
import org.jcompas.model.datamodel.Pattern;
import org.jcompas.model.datamodel.Pattern.ClapLine;
import org.jcompas.model.datamodel.Pattern.Golpe;
import org.jcompas.model.datamodel.PatternId;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

/**
 * @author thibautd
 */
public class PatternReader {
	private final DataModel model;

	public PatternReader(
			final DataModel model) {
		this.model = model;
	}

	public void readFile(final File patternFile) {
		try {
			final Document document = JCompasIOUtils.createSaxBuilder().build( patternFile );
			parseDocument(document);
		}
		catch (Exception e) {
			throw new RuntimeException( "error while reading file "+patternFile, e);
		}
	}

	private void parseDocument(final Document document) {
		final PatternId id = parseId(document);
		final String name = parseName( document );
		final int nRepeats = parseNRepeats(document);
		final int duration = parseDuration(document);
		final Set<EstiloId> estilos = parseEstilos(document);
		final List<ClapLine> lines = parseLines(document);

		model.addPattern(
				new Pattern(
					name,
					id,
					estilos,
					lines,
					nRepeats,
					duration));
	}

	private static List<ClapLine> parseLines(final Document document) {
		final List<ClapLine> lines = new ArrayList<ClapLine>();

		for ( Element line : document.getRootElement().getChildren( "line" ) ) {
			lines.add( parseLine(line) );
		}

		return lines;
	}

	private static ClapLine parseLine(final Element line) {
		final ClapId sound = new ClapId( line.getAttribute( "soundRefId" ).getValue() );
		final String name = line.getAttribute( "name" ).getValue();

		final List<Golpe> golpes = new ArrayList<Golpe>();
		for ( Double pos : getTimePositionOfClaps( line.getTextTrim() ) ) {
			golpes.add( new Golpe( pos ) );
		}

		return new ClapLine( name , sound , golpes );
	}

	private static List<Double> getTimePositionOfClaps(final String description) {
		final char[] chars = description.toLowerCase().toCharArray();
		
		final List<Integer> posOfClaps = new ArrayList<Integer>();
		int countNonWhitespace = 0;
		for (char c : chars) {
			if ( Character.isWhitespace( c ) ) continue;
			if ( c == 'x' ) posOfClaps.add( countNonWhitespace );
			countNonWhitespace++;
		}

		final List<Double> positions = new ArrayList<Double>();
		for ( Integer p : posOfClaps ) {
			positions.add( ((double) p) / countNonWhitespace );
		}
		return positions;
	}

	private static Set<EstiloId> parseEstilos(final Document document) {
		final Set<EstiloId> estilos = new LinkedHashSet<EstiloId>();

		final Element estilosElem =
			document.getRootElement().getChild( "estilos" );

		for ( Element elem : estilosElem.getChildren( "estilo" ) ) {
			final String id = elem.getAttributeValue( "refId" );
			estilos.add( new EstiloId( id ) );
		}

		return estilos;
	}

	private static int parseDuration(final Document document) {
		final String d = document.getRootElement().getAttribute( "nCompas" ).getValue();
		return Integer.parseInt( d );
	}

	private static int parseNRepeats(final Document document) {
		final String n = document.getRootElement().getAttribute( "nRepeat" ).getValue();
		return Integer.parseInt( n );
	}

	private static PatternId parseId(final Document document) {
		final String id = document.getRootElement().getAttribute( "refId" ).getValue();
		return new PatternId( id );
	}

	private static String parseName(final Document document) {
		final Attribute att = document.getRootElement().getAttribute( "name" );
		return att == null ? null : att.getValue();
	}
}
