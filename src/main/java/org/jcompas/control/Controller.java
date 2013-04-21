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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import org.jcompas.model.Estilo;
import org.jcompas.model.io.PaloReader;
import org.jcompas.model.JCompasGlobal;
import org.jcompas.model.Palo;
import org.jcompas.model.Palos;
import org.jcompas.model.sound.Pattern;
import org.jcompas.model.sound.SimpleMetronome;
import org.jcompas.view.Reloj;

/**
 * Unifies elements of the model under a unified interface,
 * for interaction with UI.
 * @author thibautd
 */
public final class Controller {
	private static final Logger log = Logger.getLogger( Controller.class );
	private static final int TIME_BEFORE_PLAY = 100;

	private final Palos palos;

	private Palo selectedPalo = null;
	private Estilo selectedEstilo = null;
	private final List<Pattern> selectedPatterns = new ArrayList<Pattern>();

	private Reloj reloj = null;
	private int bpm = -1;

	private MetronomeRunner metronomeRunner = null;
	private RelojRunner relojRunner = null;

	public Controller() {
		try {
			palos = new PaloReader().readPalos();
		}
		catch (Exception e) {
			JCompasGlobal.notifyException(
					"error while importing palos",
					e );
			throw new RuntimeException();
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	// for interface
	public void setReloj(final Reloj reloj) {
		this.reloj = reloj;
	}

	public Collection<String> getPalos() {
		return palos.getAvailablePalos();
	}

	public void selectPalo(final String name) {
		log.debug( "selecting palo "+name );
		selectedPalo = palos.createPalo( name );
		selectedEstilo = null;
		selectedPatterns.clear();
		if ( reloj != null ) reloj.setCompas( null );
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
		reloj.setCompas( selectedEstilo.getCompas() );
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

	public boolean start() {
		if (selectedPatterns.size() == 0) {
			JCompasGlobal.userWarning( "no Pattern selected!" );
			return false;
		}

		metronomeRunner =
			new MetronomeRunner( 
					new SimpleMetronome(
						selectedPatterns,
						selectedEstilo.getCompas()));
		relojRunner = new RelojRunner( reloj );

		log.debug( "start playing!" );
		long compasDur = (long)
			((60000d / bpm) * selectedEstilo.getCompas().getBeatsCount());
		log.debug( "bpm: "+bpm );
		log.debug( "compas dur.: "+compasDur+" ms." );
		long start = System.currentTimeMillis() + TIME_BEFORE_PLAY;

		metronomeRunner.start( start , compasDur );
		relojRunner.start( start , compasDur );

		return true;
	}
	
	public boolean stop() {
		if (metronomeRunner != null) {
			metronomeRunner.stop();
			relojRunner.stop();
			metronomeRunner = null;
			relojRunner = null;
			return true;
		}
		return false;
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

