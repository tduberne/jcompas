/* *********************************************************************** *
 * project: org.jcompas.*
 * CounterReloj.java
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

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.jcompas.model.Beat;
import org.jcompas.model.CompasInformation;

/**
 * @author thibautd
 */
public class CounterReloj implements Reloj {
	private final CounterRelojView view = new CounterRelojView();
	private final List<Beat> beats = new ArrayList<Beat>();

	@Override
	public void setCompas(final CompasInformation compas) {
		beats.clear();

		view.setName( "X" );
		view.setEmphasize( false );
		view.repaint();

		if ( compas == null ) return;

		String lastName = "";
		for ( Beat b : compas.getBeats() ) {
			if ( !b.getName().isEmpty() ) {
				beats.add( b );
				lastName = b.getName();
			}
			else {
				beats.add(
						new Beat( 
							lastName,
							b.isStrong() ) );
			}
		}
	}

	@Override
	public JPanel getView() {
		return view;
	}

	@Override
	public void notifyCompasFraction(final double fraction) {
		final Beat b = beats.get( (int) (fraction * beats.size()) );
		view.setBeatName( b.getName() );
		view.setEmphasize( b.isStrong() );
		view.repaint();
	}

	@Override
	public String toString() {
		return "Counter";
	}
}

class CounterRelojView extends JPanel {
	private static final long serialVersionUID = 1L;

	private String beatName = "X";
	private boolean emphasize = false;

	public void setBeatName(final String beatName) {
		this.beatName = beatName;
	}

	public void setEmphasize(final boolean b) {
		this.emphasize = b;
	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent( g );

		final int width = Math.min( getWidth() , getHeight() );
		final int xCenter = (int) (0.5 * width);
		final int yCenter = xCenter;

		paintBackground( g , width );
		paintName( g , width , xCenter , yCenter );
	}

	private void paintBackground(
			final Graphics g,
			final int width) {
		// get defaults
		final Color c = g.getColor();

		g.setColor( emphasize ? Color.red : Color.white );
		g.fillRect( 0 , 0 , width , width );
		g.setColor( c );
		g.drawRect( 0 , 0 , width , width );
		g.setColor( c );
	}

	private void paintName(
			final Graphics g,
			final int width,
			final int xCenter,
			final int yCenter) {
		final FontMetrics initialFm = g.getFontMetrics();
		final Font font = g.getFont();
		g.setFont(
				font.deriveFont( (float)
					((font.getSize() * width / 2.0) /
					 (initialFm.getAscent() + initialFm.getDescent()) ) ) );

		final FontMetrics fm = g.getFontMetrics();
		int x = xCenter - (fm.stringWidth( beatName ) / 2);
		int y = fm.getAscent() + yCenter - ((fm.getAscent() + fm.getDescent()) / 2);
		g.drawString(
				beatName,
				x,
				y );
		g.setFont( font );
	}
}
