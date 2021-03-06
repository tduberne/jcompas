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
import org.jcompas.model.JCompasGlobal;
import org.jcompas.model.datamodel.DataModel;
import org.jcompas.model.datamodel.Estilo;
import org.jcompas.model.datamodel.EstiloId;
import org.jcompas.model.datamodel.Palo;
import org.jcompas.model.datamodel.PaloId;
import org.jcompas.model.datamodel.Pattern;
import org.jcompas.model.datamodel.PatternId;
import org.jcompas.model.io.ModelReader;
import org.jcompas.model.io.Paths;
import org.jcompas.model.sound.SimpleMetronome;
import org.jcompas.view.Reloj;

/**
 * Unifies elements of the model under a unified interface,
 * for interaction with UI.
 * @author thibautd
 */
public final class Controller {
	private static final Logger log = Logger.getLogger(Controller.class);
	private final DataModel model;

	private Palo selectedPalo = null;
	private Estilo selectedEstilo = null;
	private final List<Pattern> selectedPatterns = new ArrayList<Pattern>();

	private Reloj reloj = null;
	private int bpm = -1;

	private MetronomeRunner metronomeRunner = null;
	private RelojRunner relojRunner = null;

	public Controller( final Paths paths ) {
		this( new DataModel() );
		new ModelReader( this.model ).read( paths );
	}
	
	public Controller(final DataModel model) {
		this.model = model;
	}

	// /////////////////////////////////////////////////////////////////////////
	// for interface
	public void setReloj(final Reloj reloj) {
		this.reloj = reloj;
		if (selectedEstilo != null) {
			reloj.setCompas(selectedEstilo.getCompas());
		}
	}

	public Collection<PaloId> getPalos() {
		return model.getPalos().getAvailablePalos();
	}

	public void selectPalo(final PaloId name) {
		log.debug("selecting palo " + name);
		selectedPalo = model.getPalos().getPalo(name);
		selectedEstilo = null;
		selectedPatterns.clear();
		if (reloj != null)
			reloj.setCompas(null);
	}

	public String getSelectedPalo() {
		return selectedPalo == null ? null : selectedPalo.getName();
	}

	public Collection<EstiloId> getEstilos() {
		return selectedPalo == null ? null : selectedPalo.getEstilos();
	}

	public void selectEstilo(final EstiloId name) {
		selectedEstilo = model.getEstilos().getEstilo( name );
		selectedPatterns.clear();
		bpm = selectedEstilo.getCompas().getTypicalBpm();

		log.debug("selecting estilo " + name);
		log.debug("compas is: " + selectedEstilo.getCompas());
		reloj.setCompas(selectedEstilo.getCompas());
	}

	public String getSelectedEstilo() {
		return selectedEstilo == null ? null : selectedEstilo.getName();
	}

	public Collection<PatternId> getPatterns() {
		return selectedEstilo == null ? null : selectedEstilo.getPatterns();
	}

	public List<PatternId> getSelectedPatterns() {
		return toStrings(selectedPatterns);
	}

	public List<PatternId> addPatternToSelection(final PatternId name) {
		selectedPatterns.add( model.getPatterns().getPattern(name) );
		return getSelectedPatterns();
	}

	public List<PatternId> removePatternFromSelection(final PatternId name) {
		selectedPatterns.remove( model.getPatterns().getPattern(name) );
		return getSelectedPatterns();
	}

	public boolean start() {
		if (selectedPatterns.size() == 0) {
			JCompasGlobal.userWarning("no Pattern selected!");
			return false;
		}

		metronomeRunner =
				new MetronomeRunner(
						model,
						new SimpleMetronome(
								selectedPatterns,
								selectedEstilo.getCompas()));
		relojRunner = new RelojRunner(reloj);

		log.debug("start playing!");
		long compasDur = (long) ((60000d / bpm) * selectedEstilo.getCompas()
				.getBeatsCount());
		log.debug("bpm: " + bpm);
		log.debug("compas dur.: " + compasDur + " ms.");

		final TimedLatch latch = new TimedLatch(3);
		metronomeRunner.start(latch, compasDur);
		relojRunner.start(latch, compasDur);
		try {
			latch.release();
		}
		catch (Exception e) {
			return false;
		}

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

	private static List<PatternId> toStrings(final List<Pattern> ps) {
		final List<PatternId> strings = new ArrayList<PatternId>();

		for ( Pattern p : ps ) {
			strings.add( p.getId() );
		}

		return strings;
	}
}

