/* *********************************************************************** *
 * project: org.jcompas.*
 * Controller.java
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
package org.jcompas.control;

import java.awt.GridLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import org.jcompas.model.Estilo;
import org.jcompas.model.Palo;
import org.jcompas.model.PaloFactory;
import org.jcompas.model.sound.Pattern;
import org.jcompas.model.sound.SimpleMetronome;
import org.jcompas.view.Reloj;
import org.jcompas.view.SimpleReloj;

/**
 * Unifies elements of the model under a unified interface,
 * for interaction with UI.
 * @author thibautd
 */
public final class Controller {
	private static final Logger log = Logger.getLogger( Controller.class );
	private static final int TIME_BEFORE_PLAY = 100;

	private final PaloFactory paloFactory = new PaloFactory();

	private Palo selectedPalo = null;
	private Estilo selectedEstilo = null;
	private final List<Pattern> selectedPatterns = new ArrayList<Pattern>();

	private JPanel relojPanel = new JPanel();
	private Reloj reloj = null;
	private int bpm = -1;

	private MetronomeRunner metronomeRunner = null;
	private RelojRunner relojRunner = null;

	public Controller() {
		relojPanel.setLayout( new GridLayout( 1 , 1 ) );
	}

	// /////////////////////////////////////////////////////////////////////////
	// for interface
	public Collection<String> getPalos() {
		return paloFactory.getAvailablePalos();
	}

	public void selectPalo(final String name) {
		log.debug( "selecting palo "+name );
		selectedPalo = paloFactory.createPalo( name );
		selectedEstilo = null;
		selectedPatterns.clear();
		relojPanel.removeAll();
	}

	public String getSelectedPalo() {
		return selectedPalo == null ? null : selectedPalo.getName();
	}

	public Collection<String> getEstilos() {
		return selectedPalo == null ? null : selectedPalo.getEstilos();
	}

	public void selectEstilo(final String name) {
		selectedEstilo = selectedPalo.getEstilo( name );
		selectedPatterns.clear();
		bpm = selectedEstilo.getCompas().getTypicalBpm();

		log.debug( "selecting estilo "+name );
		log.debug( "compas is: "+selectedEstilo.getCompas() );
		relojPanel.removeAll();
		reloj = new SimpleReloj( selectedEstilo.getCompas() );
		log.debug( "adding Reloj "+reloj );
		relojPanel.add( reloj.getView() );
		relojPanel.revalidate();
	}

	public String getSelectedEstilo() {
		return selectedEstilo == null ? null : selectedEstilo.getName();
	}

	public Collection<String> getPatterns() {
		return selectedEstilo == null ? null : selectedEstilo.getPatterns();
	}

	public List<String> getSelectedPatterns() {
		return toStrings( selectedPatterns );
	}


	public List<String> addPatternToSelection( final String name ) {
		selectedPatterns.add( selectedEstilo.getPattern( name ) );
		return getSelectedPatterns();
	}

	public List<String> removePatternFromSelection( final String name ) {
		selectedPatterns.remove( selectedEstilo.getPattern( name ) );
		return getSelectedPatterns();
	}

	public JComponent getRelojPane() {
		return relojPanel;
	}

	public boolean start() {
		metronomeRunner =
			new MetronomeRunner( 
					new SimpleMetronome(
						selectedPatterns,
						selectedEstilo.getCompas()));
		relojRunner = new RelojRunner( reloj );

		log.debug( "start playing!" );
		long compasDur = (long)
			((60000d / bpm) * selectedEstilo.getCompas().getTensesCount());
		log.debug( "bpm: "+bpm );
		log.debug( "compas dur.: "+compasDur+" ms." );
		long start = System.currentTimeMillis() + TIME_BEFORE_PLAY;

		metronomeRunner.start( start , compasDur );
		relojRunner.start( start , compasDur );

		return true;
	}
	
	public boolean stop() {
		metronomeRunner.stop();
		relojRunner.stop();
		metronomeRunner = null;
		relojRunner = null;
		return true;
	}

	public void setBpm(final int bpm) {
		this.bpm = bpm;
	}

	public int getBpm() {
		return bpm;
	}

	private static List<String> toStrings(final List<Pattern> ps) {
		List<String> strings = new ArrayList<String>();

		for ( Pattern p : ps ) {
			strings.add( p.getName() );
		}

		return strings;
	}
}

