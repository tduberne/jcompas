/* *********************************************************************** *
 * project: org.jcompas.*
 * AppletRunner.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2013 by the members listed in the COPYING,        *
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

import javax.swing.JApplet;

import org.jcompas.control.Controller;
import org.jcompas.model.io.Paths;
import org.jcompas.view.ControlPaneFactory;
import org.jcompas.view.RelojPaneFactory;

/**
 * @author thibautd
 */
public class AppletRunner extends JApplet {
	private static final long serialVersionUID = 1L;

	@Override
	public void init() {
		Controller controller = new Controller( new Paths() );

		getContentPane().setLayout( new GridLayout( 1 , 2 ) );
		add( RelojPaneFactory.createRelojPane( controller ) );
		add( ControlPaneFactory.createControlPane( controller ) );
	}
}

