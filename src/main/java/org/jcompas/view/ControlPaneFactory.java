/* *********************************************************************** *
 * project: org.jcompas.*
 * ControlPane.java
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
package org.jcompas.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;

import org.apache.log4j.Logger;

import org.jcompas.control.Controller;

/**
 * @author thibautd
 */
public class ControlPaneFactory {
	private static final Logger log =
		Logger.getLogger(ControlPaneFactory.class);

	private static final int BPM_MIN = 50;
	private static final int BPM_MAX = 250;
	private static final int VERT_GAP = 20;

	private static final String START_ACTION = "cocorico";
	private static final String STOP_ACTION = "turlututu";

	public static JPanel createControlPane(final Controller controller) {
		// init pane
		final JPanel pane = new JPanel();
		pane.setLayout( new BoxLayout( pane , BoxLayout.Y_AXIS ) );

		// init components and add them
		pane.add( Box.createRigidArea( new Dimension( 0 , VERT_GAP ) ) );
		final JComboBox paloBox = new JComboBox( controller.getPalos().toArray() );
		paloBox.setMaximumSize( new Dimension( 200, 20 ) );
		pane.add( paloBox );

		pane.add( Box.createRigidArea( new Dimension( 0 , VERT_GAP ) ) );
		final JComboBox estiloBox = new JComboBox();
		estiloBox.setMaximumSize( new Dimension( 200, 20 ) );
		pane.add( estiloBox );

		pane.add( Box.createRigidArea( new Dimension( 0 , VERT_GAP ) ) );
		final PatternBoxGroup patternBoxes = new PatternBoxGroup( controller );
		pane.add( patternBoxes );

		final JButton startButton = new JButton( "Start" );
		startButton.setEnabled( false );
		startButton.setActionCommand( START_ACTION );
		final JButton stopButton = new JButton( "Stop" );
		stopButton.setEnabled( false );
		stopButton.setActionCommand( STOP_ACTION );

		pane.add( Box.createVerticalGlue() );
		JPanel buttons = new JPanel();
		buttons.setLayout( new BoxLayout( buttons , BoxLayout.X_AXIS ) );
		buttons.add( Box.createHorizontalGlue() );
		buttons.add( startButton );
		buttons.add( Box.createHorizontalGlue() );
		buttons.add( stopButton );
		buttons.add( Box.createHorizontalGlue() );
		pane.add( buttons );
		pane.add( Box.createRigidArea( new Dimension( 0 , VERT_GAP ) ) );

		final JSlider bpmSlider =
			new JSlider(
					JSlider.HORIZONTAL,
					BPM_MIN,
					BPM_MAX,
					BPM_MIN );
		bpmSlider.setMajorTickSpacing( 50 );
		bpmSlider.setMinorTickSpacing( 10 );
		bpmSlider.setPaintTicks( true );
		bpmSlider.setPaintLabels( true );
		bpmSlider.setBorder( BorderFactory.createTitledBorder( "Tempo" ) );
		bpmSlider.setEnabled( false );

		pane.add( bpmSlider );
		pane.add( Box.createRigidArea( new Dimension( 0 , VERT_GAP ) ) );

		// listenners
		paloBox.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				JComboBox box = (JComboBox) e.getSource();
				controller.selectPalo( (String) box.getSelectedItem() );
				log.debug( "adding estilos "+controller.getEstilos() );
				estiloBox.setModel(
					new DefaultComboBoxModel(
						controller.getEstilos().toArray() ) );
			}
		});

		estiloBox.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox box = (JComboBox) e.getSource();
				controller.selectEstilo( (String) box.getSelectedItem() );

				patternBoxes.setPatterns( controller.getPatterns() );

				startButton.setEnabled( true );
				bpmSlider.setEnabled( true );
				bpmSlider.setValue( controller.getBpm() );
			}
		});

		ActionListener buttonListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.setBpm( bpmSlider.getValue() );
				boolean start = e.getActionCommand().equals( START_ACTION );
				boolean success = start ? controller.start() : controller.stop();

				if (success) {
					startButton.setEnabled( !start );
					stopButton.setEnabled( start );

					patternBoxes.setEnabled( !start );

					paloBox.setEnabled( !start );
					estiloBox.setEnabled( !start );
					bpmSlider.setEnabled( !start );
				}
			}
		};
		startButton.addActionListener( buttonListener );
		stopButton.addActionListener( buttonListener );

		return pane;
	}

	private static class PatternBoxGroup extends JPanel {
		private final List<JCheckBox> boxes = new ArrayList<JCheckBox>();
		private final Listener listener = new Listener();
		private final Controller controller;

		public PatternBoxGroup(
				final Controller controller) {
			this.controller = controller;
			setLayout( new BoxLayout( this , BoxLayout.Y_AXIS ) ); 
		}

		public void setPatterns(
				final Collection<String> patterns) {
			boxes.clear();
			removeAll();
			for (String p : patterns) {
				JCheckBox b = new JCheckBox( p );
				boxes.add( b );
				b.addItemListener( listener );
				add( b );
			}
			revalidate();
		}

		public void setEnabled(final boolean bool) {
			super.setEnabled( bool );
			for (JCheckBox b : boxes) b.setEnabled( bool );
		}

		private class Listener implements ItemListener {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				JCheckBox b = (JCheckBox) e.getItem();
				String p = b.getLabel();

				if (e.getStateChange() == ItemEvent.SELECTED) {
					controller.addPatternToSelection( p );
				}
				else {
					controller.removePatternFromSelection( p );
				}
			}
		}


	}
}


