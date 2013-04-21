/* *********************************************************************** *
 * project: org.jcompas.*
 * Run.java
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
package org.jcompas;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.jcompas.control.Controller;
import org.jcompas.model.JCompasGlobal;
import org.jcompas.view.ControlPaneFactory;
import org.jcompas.view.RelojPaneFactory;

/**
 * @author thibautd
 */
public class Run {
	private static final Logger log =
		Logger.getLogger(Run.class);

	public static void main(final String[] args) {
		PropertyConfigurator.configure( ClassLoader.getSystemResource( "log4j.properties" ) );
		log.info( "################################################################################" );
		log.info( "Starting jCompas "+JCompasGlobal.getVersionId() );

		Runtime.getRuntime().addShutdownHook( new Thread() {
			@Override
			public void run() {
				log.info( "Ending jCompas "+JCompasGlobal.getVersionId() );
				log.info( "################################################################################" );
			}
		});
		try {
			Controller controller = new Controller();

			JFrame window = new JFrame( "jCompas "+JCompasGlobal.getVersionId() );
			window.getContentPane().setLayout( new GridLayout( 1 , 2 ) );
			window.add( RelojPaneFactory.createRelojPane( controller ) );
			window.add( ControlPaneFactory.createControlPane( controller ) );
			window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
			window.setVisible( true );
		}
		catch (Exception e) {
			JCompasGlobal.notifyException( "got an uncatched exception!", e );
		}
	}
}

