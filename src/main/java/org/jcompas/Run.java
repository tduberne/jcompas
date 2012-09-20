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

import javax.swing.JFrame;

import org.apache.log4j.BasicConfigurator;

import org.jcompas.control.Controller;
import org.jcompas.model.JCompasGlobal;
import org.jcompas.view.ControlPaneFactory;

/**
 * @author thibautd
 */
public class Run {
	public static void main(final String[] args) {
		BasicConfigurator.configure();
		try {
			Controller controller = new Controller();

			JFrame window = new JFrame( "jCompas "+JCompasGlobal.getVersionId() );
			window.getContentPane().setLayout( new GridLayout( 1 , 2 ) );
			window.add( controller.getRelojPane() );
			window.add( ControlPaneFactory.createControlPane( controller ) );
			window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
			window.setVisible( true );
		}
		catch (Exception e) {
			JCompasGlobal.notifyException( "got an uncatched exception!", e );
		}
	}
}

