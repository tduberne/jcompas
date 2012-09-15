/* *********************************************************************** *
 * project: org.jcompas.*
 * CompasFactory.java
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author thibautd
 */
public final class CompasFactory {
	private CompasFactory() {};

	public static CompasInformation createBuleriasCompas() {
		List<Tense> tenses = new ArrayList<Tense>();

		tenses.add ( new Tense( 12 , true ) );
		tenses.add ( new Tense( 1 , false ) );
		tenses.add ( new Tense( 2 , false ) );
		tenses.add ( new Tense( 3 , true ) );
		tenses.add ( new Tense( 4 , false ) );
		tenses.add ( new Tense( 5 , false ) );
		tenses.add ( new Tense( 6 , true ) );
		tenses.add ( new Tense( 7 , false ) );
		tenses.add ( new Tense( 8 , true ) );
		tenses.add ( new Tense( 9 , false ) );
		tenses.add ( new Tense( 10 , true ) );
		tenses.add ( new Tense( 11 , false ) );
		
		return new CompasInformation( 190 , tenses );
	}
}

