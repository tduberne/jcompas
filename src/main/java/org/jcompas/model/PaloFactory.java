/* *********************************************************************** *
 * project: org.jcompas.*
 * PaloFactory.java
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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jcompas.model.sound.Clap;
import org.jcompas.model.sound.MonoSoundClap;
import org.jcompas.model.sound.Pattern;
import org.jcompas.model.sound.Pattern.Musician;
import org.jcompas.model.sound.Pattern.Golpe;

/**
 * Gives access to the available palos.
 * @author thibautd
 */
public final class PaloFactory {
	private final Map<String, Palo> palos = new HashMap<String, Palo>();

	public PaloFactory() {
		CompasInformation compas = CompasFactory.createBuleriasCompas();

		Clap fuerte = new MonoSoundClap(
				"fuerte",
				getClass().getResourceAsStream(
					"/sounds/palmas/sonora-fuerte-1.wav" ));
		Clap bajo = new MonoSoundClap(
				"bajo",
				getClass().getResourceAsStream(
					"/sounds/palmas/sonora-baja-1.wav" ));

	
		palos.put(
				"Buleria",
				new Palo(
					"Buleria",
					Arrays.asList(
						new Estilo(
							"Basica",
							compas,
							Arrays.asList(
								new Pattern(
									"Basico",
									Arrays.asList(
										new Musician(
											"Manuel",
											Arrays.asList(
												 new Golpe( fuerte , 0 ),
												 new Golpe( bajo , 1 / 12d ),
												 new Golpe( bajo , 2 / 12d ),
												 new Golpe( fuerte , 3 / 12d ),
												 new Golpe( bajo , 4 / 12d ),
												 new Golpe( bajo , 5 / 12d ),
												 new Golpe( fuerte , 6 / 12d ),
												 new Golpe( bajo , 7 / 12d ),
												 new Golpe( fuerte , 8 / 12d ),
												 new Golpe( bajo , 9 / 12d ),
												 new Golpe( fuerte , 10 / 12d ),
												 new Golpe( bajo , 11 / 12d )
												 )
											)),
									1,
									1))))));
	}

	public Collection<String> getAvailablePalos() {
		return palos.keySet();
	}

	public Palo createPalo( final String name ) {
		return palos.get( name );
	}
}

