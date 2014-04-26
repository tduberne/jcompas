/* *********************************************************************** *
 * project: org.jcompas.*
 * EstiloReader.java
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
import java.util.ArrayList;
import java.util.List;

import org.jcompas.model.datamodel.Beat;
import org.jcompas.model.datamodel.CompasInformation;
import org.jcompas.model.datamodel.DataModel;
import org.jcompas.model.datamodel.Estilo;
import org.jcompas.model.datamodel.EstiloId;
import org.jcompas.model.datamodel.PaloId;
import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * @author thibautd
 */
public class EstiloReader {
	private final DataModel model;

	public EstiloReader(final DataModel model) {
		this.model = model;
	}

	public void readFile(final String estiloFile) {
		try {
			final Document document = new SAXBuilder().build( estiloFile );
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
		final Element root = document.getRootElement();
		final EstiloId estiloId = new EstiloId(root.getAttribute("refId").getValue());
		final PaloId paloId = new PaloId(root.getAttribute("paloRefId").getValue());
		final CompasInformation compas = parseCompas(root.getChild("compas"));

		model.addEstilo( new Estilo(estiloId, paloId, compas) );
	}

	private static CompasInformation parseCompas(final Element compasElement)
			throws DataConversionException {
		final int bpm = compasElement.getAttribute( "bpm" ).getIntValue();

		final List<Beat> beats = new ArrayList<Beat>();
		for (Element b : compasElement.getChildren( "beat" )) {
			Beat beat = new Beat(
					b.getAttribute( "name" ).getValue(),
					b.getAttribute( "strong" ).getBooleanValue() );
			beats.add( beat );
		}

		return new CompasInformation( bpm , beats );
	}
}

