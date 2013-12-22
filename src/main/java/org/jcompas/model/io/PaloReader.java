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
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import org.jcompas.model.Beat;
import org.jcompas.model.CompasInformation;
import org.jcompas.model.Estilo;
import org.jcompas.model.Palo;
import org.jcompas.model.Palos;
import org.jcompas.model.sound.Clap;
import org.jcompas.model.sound.Pattern;
import org.jcompas.model.sound.Pattern.Golpe;
import org.jcompas.model.sound.Pattern.Musician;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * @author thibautd
 */
public class PaloReader {
	private static final Logger log =
		Logger.getLogger(PaloReader.class);

	private final SAXBuilder xmlParser = new SAXBuilder();

	public Palos readPalos( final Paths paths ) {
		final ClapReader clapReader = new ClapReader( paths );
		try {
			log.debug( "getting palos from "+paths.getPatternsLocation().toURI() );
			File[] files = new File( paths.getPatternsLocation().toURI() ).listFiles(
					new FilenameFilter() {
						@Override
						public boolean accept(File dir, String name) {
							return name.endsWith( ".xml" );
						}
					});

			Map<String, List<Pattern>> estilo2Pattern = new HashMap<String, List<Pattern>>();
			for (File f : files) {
				Document doc = xmlParser.build( f );
				if (doc.getRootElement().getName().equals( XmlSchemaNames.PATTERN_TAG )) {
					Pattern p = parsePattern( clapReader , doc );
					for (String estiloFile : extractEstilos( doc )) {
						List<Pattern> ps = estilo2Pattern.get( estiloFile );

						if (ps == null) {
							ps = new ArrayList<Pattern>();
							estilo2Pattern.put( estiloFile , ps );
						}

						ps.add( p );
					}
				}
			}


			Map<String, List<Estilo>> palo2Estilo = new HashMap<String, List<Estilo>>();
			for (Map.Entry<String, List<Pattern>> patterns : estilo2Pattern.entrySet()) {
				EstiloAndPalo estilo = parseEstilo( paths , patterns.getKey() , patterns.getValue() );

				List<Estilo> es = palo2Estilo.get( estilo.palo );
				if (es == null) {
					es = new ArrayList<Estilo>();
					palo2Estilo.put( estilo.palo , es);
				}
				es.add( estilo.estilo );
			}

			List<Palo> palos = new ArrayList<Palo>();

			for (Map.Entry<String, List<Estilo>> p : palo2Estilo.entrySet()) {
				palos.add( new Palo( p.getKey() , p.getValue() ) );
			}

			return new Palos( palos );
		}
		catch (Exception e) {
			throw new JCompasImportException(
					"could not import palos",
					e);
		}
	}

	private static List<String> extractEstilos(final Document patternDoc) {
		List<String> files = new ArrayList<String>();

		for (Element estilo :
				patternDoc.getRootElement().getChildren(
					XmlSchemaNames.PATTERN_ESTILO_TAG )) {
			files.add( estilo.getAttribute( XmlSchemaNames.PATTERN_ESTILO_ATT ).getValue() );
		}

		return files;
	}

	private EstiloAndPalo parseEstilo(
			final Paths paths,
			final String file,
			final List<Pattern> patterns) throws JDOMException, IOException {
		Document d = xmlParser.build( paths.getPatternsLocation().getPath() + file );
		String name = d.getRootElement().getAttribute( XmlSchemaNames.ESTILO_NAME_ATT ).getValue();

		Element compasElement = d.getRootElement().getChild( XmlSchemaNames.COMPAS_TAG );

		List<Beat> beats = new ArrayList<Beat>();
		for (Element b : compasElement.getChildren( XmlSchemaNames.BEAT_TAG )) {
			Beat beat = new Beat(
					b.getAttribute( XmlSchemaNames.BEAT_NAME_ATT ).getValue(),
					b.getAttribute( XmlSchemaNames.BEAT_STRONG_ATT ).getBooleanValue() );
			beats.add( beat );
		}

		return new EstiloAndPalo(
				d.getRootElement().getAttribute( XmlSchemaNames.ESTILO_PALO_ATT ).getValue(),
				new Estilo(
					name,
					new CompasInformation(
						name,
						compasElement.getAttribute( XmlSchemaNames.COMPAS_BPM_ATT ).getIntValue(),
						beats),
					patterns));
	}

	private static class EstiloAndPalo {
		public final String palo;
		public final Estilo estilo;
		public EstiloAndPalo(
				final String palo,
				final Estilo estilo) {
			this.palo = palo;
			this.estilo = estilo;
		}
	}

	private Pattern parsePattern(final ClapReader clapReader, final Document doc) throws DataConversionException {
		Element root = doc.getRootElement();
		String patternName = root.getAttribute( XmlSchemaNames.PATTERN_NAME_ATT ).getValue();
		int nCompas = root.getAttribute( XmlSchemaNames.PATTERN_NCOMPAS_ATT ).getIntValue();
		int nRepeat = root.getAttribute( XmlSchemaNames.PATTERN_NREPEAT_ATT ).getIntValue();

		List<Musician> musicians = new ArrayList<Musician>();

		for (Element musician : root.getChildren( XmlSchemaNames.MUSICIAN_TAG )) {
			String name = musician.getAttribute( XmlSchemaNames.MUSICIAN_NAME_ATT ).getValue();
			List<Golpe> golpes = parseGolpes( clapReader , musician );
			musicians.add( new Musician( name , golpes ) );
		}

		return new Pattern(
				patternName,
				musicians,
				nRepeat,
				nCompas);
	}

	private List<Golpe> parseGolpes(
			final ClapReader clapReader,
			final Element musician) {
		List<Golpe> golpes = new ArrayList<Golpe>();

		for (Element e : musician.getChildren( XmlSchemaNames.SOUND_TAG )) {
			String soundDir = e.getAttribute( XmlSchemaNames.SOUND_DIR_ATT ).getValue();
			Clap clap = clapReader.createClap( soundDir );

			for ( double pos : getTimePositionOfClaps( e.getTextTrim() ) ) {
				golpes.add( new Golpe( clap , pos ) );
			}
		}

		return golpes;
	}

	private List<Double> getTimePositionOfClaps(final String description) {
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
}

