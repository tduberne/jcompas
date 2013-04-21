/* *********************************************************************** *
 * project: org.jcompas.*
 * RelojPaneFactory.java
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
package org.jcompas.view;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.jcompas.control.Controller;

/**
 * @author thibautd
 */
public final class RelojPaneFactory {
	private static final int RELOJ_PANE_BORDER = 20;
	private RelojPaneFactory() {}

	public static JPanel createRelojPane(final Controller controller) {
		final JPanel pane = new JPanel();

		pane.setLayout( new GridLayout( 1 , 1 ) );
		final Reloj reloj = new SimpleReloj();
		pane.add( reloj.getView() );
		controller.setReloj( reloj );

		pane.setBorder(
				BorderFactory.createEmptyBorder(
					RELOJ_PANE_BORDER,
					RELOJ_PANE_BORDER,
					RELOJ_PANE_BORDER,
					RELOJ_PANE_BORDER) );

		return pane;
	}
}

