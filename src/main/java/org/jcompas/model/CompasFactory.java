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
		List<Beat> beats = new ArrayList<Beat>();

		beats.add ( new Beat( 12 , true ) );
		beats.add ( new Beat( 1 , false ) );
		beats.add ( new Beat( 2 , false ) );
		beats.add ( new Beat( 3 , true ) );
		beats.add ( new Beat( 4 , false ) );
		beats.add ( new Beat( 5 , false ) );
		beats.add ( new Beat( 6 , true ) );
		beats.add ( new Beat( 7 , false ) );
		beats.add ( new Beat( 8 , true ) );
		beats.add ( new Beat( 9 , false ) );
		beats.add ( new Beat( 10 , true ) );
		beats.add ( new Beat( 11 , false ) );
		
		return new CompasInformation( "Bulerias" , 190 , beats );
	}
}

