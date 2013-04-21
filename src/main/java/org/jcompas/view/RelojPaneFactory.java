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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.jcompas.control.Controller;

/**
 * @author thibautd
 */
public final class RelojPaneFactory {
	private static final int VERT_GAP = 10;
	private static final int RELOJ_PANE_BORDER = 20;
	private RelojPaneFactory() {}

	public static JPanel createRelojPane(final Controller controller) {
		final JPanel pane = new JPanel();

		pane.setLayout( new BoxLayout( pane , BoxLayout.Y_AXIS ) );

		pane.add( Box.createRigidArea( new Dimension( 0 , VERT_GAP ) ) );
		final JComboBox selector =
			new JComboBox( new Reloj[]{ new SimpleReloj() } );
		selector.setMaximumSize( new Dimension( Integer.MAX_VALUE , 20 ) );
		selector.setBorder(
				BorderFactory.createTitledBorder( "View Type" ) );
		pane.add( selector );

		pane.add( Box.createRigidArea( new Dimension( 0 , VERT_GAP ) ) );

		final JPanel relojPane = new JPanel();
		relojPane.setLayout( new GridLayout( 1 , 1 ) );
		pane.add( relojPane );
		relojPane.add( ((Reloj) selector.getSelectedItem()).getView() );
		controller.setReloj( (Reloj) selector.getSelectedItem() );

		relojPane.setBorder(
				BorderFactory.createEmptyBorder(
					RELOJ_PANE_BORDER,
					RELOJ_PANE_BORDER,
					RELOJ_PANE_BORDER,
					RELOJ_PANE_BORDER) );

		selector.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(final ActionEvent e) {
						final Reloj r = (Reloj) selector.getSelectedItem();
						controller.setReloj( r );
						relojPane.removeAll();
						relojPane.add( r.getView() );
					}
				});

		return pane;
	}
}

