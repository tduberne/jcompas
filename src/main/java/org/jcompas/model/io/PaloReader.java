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

import org.jcompas.model.Beat;
import org.jcompas.model.CompasInformation;
import org.jcompas.model.Estilo;
import org.jcompas.model.Palo;
import org.jcompas.model.PaloFactory;
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
	private final ClapReader clapReader = new ClapReader();
	private final SAXBuilder xmlParser = new SAXBuilder();

	public PaloFactory readPalos() throws URISyntaxException, JDOMException, IOException {
		File[] files = new File( IOUtils.PATTERNS_LOCATION.toURI() ).listFiles(
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
				Pattern p = parsePattern( doc );
				String estiloFile = doc.getRootElement().getAttribute( XmlSchemaNames.PATTERN_ESTILO_ATT ).getValue();
				List<Pattern> ps = estilo2Pattern.get( estiloFile );

				if (ps == null) {
					ps = new ArrayList<Pattern>();
					estilo2Pattern.put( estiloFile , ps );
				}

				ps.add( p );
			}
		}


		Map<String, List<Estilo>> palo2Estilo = new HashMap<String, List<Estilo>>();
		for (Map.Entry<String, List<Pattern>> patterns : estilo2Pattern.entrySet()) {
			EstiloAndPalo estilo = parseEstilo( patterns.getKey() , patterns.getValue() );

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

		return new PaloFactory( palos );
	}

	private EstiloAndPalo parseEstilo(final String file, final List<Pattern> patterns) throws JDOMException, IOException {
		Document d = xmlParser.build( IOUtils.PATTERNS_LOCATION.getPath() + file );
		String name = d.getRootElement().getAttribute( XmlSchemaNames.ESTILO_NAME_ATT ).getValue();

		Element compasElement = d.getRootElement().getChild( XmlSchemaNames.COMPAS_TAG );

		TreeMap<Integer, Beat> beats = new TreeMap<Integer, Beat>();
		for (Element b : compasElement.getChildren( XmlSchemaNames.BEAT_TAG )) {
			Beat beat = new Beat(
					b.getAttribute( XmlSchemaNames.BEAT_NAME_ATT ).getValue(),
					b.getAttribute( XmlSchemaNames.BEAT_STRONG_ATT ).getBooleanValue() );
			beats.put( b.getAttribute( XmlSchemaNames.BEAT_POS_ATT ).getIntValue() , beat );
		}

		return new EstiloAndPalo(
				d.getRootElement().getAttribute( XmlSchemaNames.ESTILO_PALO_ATT ).getValue(),
				new Estilo(
					name,
					new CompasInformation(
						name,
						compasElement.getAttribute( XmlSchemaNames.COMPAS_BPM_ATT ).getIntValue(),
						new ArrayList<Beat>( beats.values() )),
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

	private Pattern parsePattern(final Document doc) throws FileNotFoundException, DataConversionException {
		Element root = doc.getRootElement();
		String patternName = root.getAttribute( XmlSchemaNames.PATTERN_NAME_ATT ).getValue();
		int nCompas = root.getAttribute( XmlSchemaNames.PATTERN_NCOMPAS_ATT ).getIntValue();
		int nRepeat = root.getAttribute( XmlSchemaNames.PATTERN_NREPEAT_ATT ).getIntValue();

		List<Musician> musicians = new ArrayList<Musician>();

		for (Element musician : root.getChildren( XmlSchemaNames.MUSICIAN_TAG )) {
			String name = musician.getAttribute( XmlSchemaNames.MUSICIAN_NAME_ATT ).getValue();
			List<Golpe> golpes = parseGolpes( musician );
			musicians.add( new Musician( name , golpes ) );
		}

		return new Pattern(
				patternName,
				musicians,
				nRepeat,
				nCompas);
	}

	private List<Golpe> parseGolpes(final Element musician) throws FileNotFoundException {
		List<Golpe> golpes = new ArrayList<Golpe>();

		for (Element e : musician.getChildren( XmlSchemaNames.SOUND_TAG )) {
			String soundDir = e.getAttribute( XmlSchemaNames.SOUND_DIR_ATT ).getValue();
			Clap clap = clapReader.createClap( soundDir );

			char[] claps = e.getTextTrim().toLowerCase().toCharArray();

			for (int i=0; i < claps.length; i++) {
				if ( claps[ i ] == 'x' ) {
					golpes.add( new Golpe( clap , (double) i / claps.length ) );
				}
			}
		}

		return golpes;
	}
}

