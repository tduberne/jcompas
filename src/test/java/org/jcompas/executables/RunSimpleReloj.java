/* *********************************************************************** *
 * project: org.jcompas.*
 * RunSimpleReloj.java
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
package org.jcompas.executables;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import org.jcompas.model.CompasFactory;
import org.jcompas.model.CompasInformation;
import org.jcompas.model.RelojRunner;
import org.jcompas.view.Reloj;
import org.jcompas.view.SimpleReloj;

/**
 * @author thibautd
 */
public class RunSimpleReloj {
	public static void main(final String[] args) {
		BasicConfigurator.configure();
		// Logger.getRootLogger().setLevel( Level.TRACE );

		CompasInformation compas = CompasFactory.createBuleriasCompas();
		Reloj reloj = new SimpleReloj( compas );

		JFrame window = new JFrame();
		window.getContentPane().add( reloj.getView() );
		window.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
		window.setVisible( true );

		RelojRunner runner = new RelojRunner( reloj );
		runner.start(
				System.currentTimeMillis() + 1000,
				3000);
	}
}

