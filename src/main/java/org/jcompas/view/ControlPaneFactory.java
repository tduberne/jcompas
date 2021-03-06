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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;
import org.jcompas.control.Controller;
import org.jcompas.model.datamodel.EstiloId;
import org.jcompas.model.datamodel.PaloId;
import org.jcompas.model.datamodel.PatternId;

/**
 * @author thibautd
 */
public class ControlPaneFactory {
	private static final Logger log =
		Logger.getLogger(ControlPaneFactory.class);

	private static final int BPM_MIN = 50;
	private static final int BPM_MAX = 250;
	private static final int VERT_GAP = 10;

	public static JPanel createControlPane(final Controller controller) {
		// init pane
		final JPanel pane = new JPanel();
		pane.setLayout( new BoxLayout( pane , BoxLayout.Y_AXIS ) );

		// init components and add them
		pane.add( Box.createRigidArea( new Dimension( 0 , VERT_GAP ) ) );
		final JComboBox paloBox = new JComboBox( controller.getPalos().toArray() );
		paloBox.setMaximumSize( new Dimension( Integer.MAX_VALUE , 20 ) );
		paloBox.setBorder(
				BorderFactory.createTitledBorder( "Palo" ) );
		pane.add( paloBox );

		pane.add( Box.createRigidArea( new Dimension( 0 , VERT_GAP ) ) );
		final JComboBox estiloBox = new JComboBox();
		estiloBox.setMaximumSize( new Dimension( Integer.MAX_VALUE, 20 ) );
		estiloBox.setBorder(
				BorderFactory.createTitledBorder( "Estilo" ) );
		pane.add( estiloBox );

		pane.add( Box.createRigidArea( new Dimension( 0 , VERT_GAP ) ) );
		final PatternBoxGroup patternBoxes = new PatternBoxGroup( controller );
		patternBoxes.setBorder(
				BorderFactory.createTitledBorder( "Patterns" ) );
		patternBoxes.setAlignmentX( Component.RIGHT_ALIGNMENT );
		pane.add( patternBoxes );

		final JButton startButton = new JButton( "Start" );
		startButton.setEnabled( false );
		pane.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put(
				KeyStroke.getKeyStroke( ' ' ),
				startButton );
		pane.getActionMap().put(
				startButton,
				new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent e) {
						startButton.doClick();
					}
				});
		startButton.setToolTipText( "[space]" );
		final JButton tapTempoButton = new JButton( "Tap Tempo" );
		tapTempoButton.setEnabled( false );
		pane.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put(
				KeyStroke.getKeyStroke( 't' ),
				tapTempoButton );
		pane.getActionMap().put(
				tapTempoButton,
				new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent e) {
						tapTempoButton.doClick();
					}
				});
		tapTempoButton.setToolTipText( "[t]" );

		pane.add( Box.createVerticalGlue() );
		JPanel buttons = new JPanel();
		buttons.setLayout( new BoxLayout( buttons , BoxLayout.X_AXIS ) );
		buttons.add( Box.createHorizontalGlue() );
		buttons.add( startButton );
		buttons.add( Box.createHorizontalGlue() );
		buttons.add( tapTempoButton );
		buttons.add( Box.createHorizontalGlue() );
		pane.add( buttons );
		pane.add( Box.createRigidArea( new Dimension( 0 , VERT_GAP ) ) );

		JSlider bpmSlider =
			new JSlider(
					JSlider.HORIZONTAL,
					BPM_MIN,
					BPM_MAX,
					BPM_MIN );
		bpmSlider.setMajorTickSpacing( 50 );
		bpmSlider.setMinorTickSpacing( 10 );
		bpmSlider.setPaintTicks( true );
		bpmSlider.setPaintLabels( true );

		final SliderSpinerGroup tempoPane = new SliderSpinerGroup( bpmSlider );
		tempoPane.setBorder( BorderFactory.createTitledBorder( "Tempo" ) );
		tempoPane.setEnabled( false );

		pane.add( tempoPane );
		pane.add( Box.createRigidArea( new Dimension( 0 , VERT_GAP ) ) );

		// listenners
		paloBox.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				JComboBox box = (JComboBox) e.getSource();
				controller.selectPalo( (PaloId) box.getSelectedItem() );
				log.debug( "adding estilos "+controller.getEstilos() );
				estiloBox.setModel(
					new DefaultComboBoxModel(
						controller.getEstilos().toArray() ) );
				// fire an event to update the pattern boxes
				estiloBox.setSelectedIndex( estiloBox.getSelectedIndex() );
			}
		});
		// fire an event to fill the estilo box
		paloBox.setSelectedIndex( paloBox.getSelectedIndex() );
		controller.selectPalo( (PaloId) paloBox.getSelectedItem() );

		estiloBox.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox box = (JComboBox) e.getSource();
				controller.selectEstilo( (EstiloId) box.getSelectedItem() );

				patternBoxes.setPatterns( controller.getPatterns() );

				startButton.setEnabled( true );
				tempoPane.setEnabled( true );
				tapTempoButton.setEnabled( true );
				tempoPane.setValue( controller.getBpm() );
			}
		});
		// fire an event to fill the pattern boxes
		estiloBox.setSelectedIndex( estiloBox.getSelectedIndex() );
		controller.selectEstilo( (EstiloId) estiloBox.getSelectedItem() );

		// configure buttons now, as they need to communicate with the components
		// above
		startButton.addActionListener( new ActionListener() {
			boolean isRunning = false;
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.setBpm( tempoPane.getValue() );
				boolean success = isRunning ? controller.stop() : controller.start();

				if (success) {
					isRunning = !isRunning;
					startButton.setText( isRunning ? "Stop" : "Start" );

					patternBoxes.setEnabled( !isRunning );

					paloBox.setEnabled( !isRunning );
					estiloBox.setEnabled( !isRunning );
					tempoPane.setEnabled( !isRunning );
					tapTempoButton.setEnabled( !isRunning );
				}
			}
		});

		tapTempoButton.addActionListener( new ActionListener() {
			private long lastTap = Long.MIN_VALUE;

			@Override
			public void actionPerformed(final ActionEvent e) {
				long newTap = System.currentTimeMillis();
				long bpm = 60000 / (newTap - lastTap);

				if (bpm >= BPM_MIN && bpm <= BPM_MAX) {
					tempoPane.setValue( (int) bpm );
				}

				lastTap = newTap;
			}
		});

		return pane;
	}

	private static class PatternBoxGroup extends JPanel {
		private final List<JCheckBox> boxes = new ArrayList<JCheckBox>();
		private final Listener listener = new Listener();
		private final Controller controller;

		public PatternBoxGroup(
				final Controller controller) {
			this.controller = controller;
			//setLayout( new BoxLayout( this , BoxLayout.Y_AXIS ) ); 
			setLayout( new GridLayout( 0 , 2 ) );
		}

		public void setPatterns(
				final Collection<PatternId> patterns) {
			boxes.clear();
			removeAll();
			for (PatternId p : patterns) {
				JCheckBox b = new JCheckBox( p.toString() );
				boxes.add( b );
				b.addItemListener( listener );
				add( b );
			}
			revalidate();
		}


		@Override
		public void setEnabled(final boolean bool) {
			super.setEnabled( bool );
			for (JCheckBox b : boxes) b.setEnabled( bool );
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(
					Integer.MAX_VALUE,
					getLayout().minimumLayoutSize( this ).height);
		}

		@Override
		public Dimension getMaximumSize() {
			return getPreferredSize();
		}

		@Override
		public Dimension getMinimumSize() {
			return getPreferredSize();
		}

		private class Listener implements ItemListener {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				final JCheckBox b = (JCheckBox) e.getItem();
				// XXX find a nicer way!
				final PatternId p = new PatternId( b.getLabel() );

				if (e.getStateChange() == ItemEvent.SELECTED) {
					controller.addPatternToSelection( p );
				}
				else {
					controller.removePatternFromSelection( p );
				}
			}
		}
	}

	private static class SliderSpinerGroup extends JPanel {
		private final JSlider slider;
		private final JSpinner spinner;

		public SliderSpinerGroup(final JSlider slider) {
			this.slider = slider;
			final SpinnerNumberModel spinnerModel =
					new SpinnerNumberModel(
						slider.getValue(),
						slider.getMinimum(),
						slider.getMaximum(),
						1);
			this.spinner = new JSpinner( spinnerModel );
			spinner.setMaximumSize( new Dimension( 50 , 20 ) );
			spinner.setAlignmentY( Component.BOTTOM_ALIGNMENT );

			ChangeListener listener = new ChangeListener() {
				@Override
				public void stateChanged(final ChangeEvent e) {
					Object s = e.getSource();

					if (s == slider) {
						spinner.setValue( slider.getValue() );
					}
					else if (s == spinner) {
						slider.setValue( spinnerModel.getNumber().intValue() );
					}
				}
			};

			slider.addChangeListener( listener );
			spinner.addChangeListener( listener );

			setLayout( new BoxLayout( this , BoxLayout.X_AXIS ) );
			add( slider );
			add( spinner );
		}

		public int getValue() {
			return slider.getValue();
		}

		public void setValue(final int value) {
			slider.setValue( value );
		}

		@Override
		public void setEnabled(final boolean b) {
			super.setEnabled( b );
			slider.setEnabled( b );
			spinner.setEnabled( b );
		}
	}
}


