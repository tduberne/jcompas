/* *********************************************************************** *
 * project: org.jcompas.*
 * SimpleReloj.java
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

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import org.jcompas.model.CompasInformation;
import org.jcompas.model.JCompasGlobal;
import org.jcompas.model.Beat;

/**
 * @author thibautd
 */
public class SimpleReloj extends JPanel implements Reloj {
	private static final Logger log =
		Logger.getLogger(SimpleReloj.class);

	private static final Color BEAT_COLOR = new Color( 235 , 235 , 235 );
	private static final Color BEAT_STRONG_COLOR = new Color( 255 , 150 , 150 );
	private final int fontSize = 17;

	private CompasInformation compas = null;
	private double tickAngleStep = -1;

	private double needleAngle = 0;

	// /////////////////////////////////////////////////////////////////////////
	// interface
	// /////////////////////////////////////////////////////////////////////////
	@Override
	public void setCompas(final CompasInformation compas) {
		this.compas = compas;
		if ( compas != null ) {
			this.tickAngleStep = Math.PI * 2 / compas.getBeatsCount();
		}
		repaint();
	}

	@Override
	public JPanel getView() {
		return this;
	}

	@Override
	public void notifyCompasFraction(final double fraction) {
		needleAngle = fraction * 2 * Math.PI;
		if (log.isTraceEnabled()) {
			log.trace( "needleAngle: "+needleAngle+" for fraction "+fraction );
		}
		repaint();
	}

	@Override
	public String toString() {
		return "Reloj";
	}

	// /////////////////////////////////////////////////////////////////////////
	// Paint
	// /////////////////////////////////////////////////////////////////////////
	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent( g );
		if ( compas == null ) return;

		final int width = Math.min( getWidth() , getHeight() );
		final double smallR = 0.45 * width;
		final double bigR = 0.5 * width;
		final int xCenter = (int) (0.5 * width);
		final int yCenter = xCenter;

		paintBackground(
				g,
				width);

		paintBeat(
				g,
				width);

		paintNeedle(
				g,
				xCenter,
				yCenter,
				smallR);

		paintForeground(
				g,
				xCenter,
				yCenter,
				smallR,
				bigR);
	}

	private void paintNeedle(
			final Graphics g,
			final int xCenter,
			final int yCenter,
			final double length) {
		g.drawLine(
				xCenter,
				yCenter,
				(int) (xCenter + length * Math.sin( needleAngle )),
				(int) (yCenter - length * Math.cos( needleAngle )));
	}

	private void paintBeat(
			final Graphics g,
			final int w) {
		final double stepInDegrees = tickAngleStep * 180 / Math.PI;
		final int currBeat = (int) (needleAngle / tickAngleStep);
		final double startAngle = 90 - currBeat * stepInDegrees;
		final Color c = g.getColor();

		if ( compas.getBeats().get( currBeat ).isStrong() ) {
			g.setColor( BEAT_STRONG_COLOR );
		}
		else {
			g.setColor( BEAT_COLOR );
		}

		g.fillArc(
				0,
				0,
				w,
				w,
				(int) Math.round( startAngle ),
				(int) -Math.round( stepInDegrees ));
		g.setColor( c );
	}

	private void paintBackground(
			final Graphics g,
			final int width) {
		// get defaults
		final Color c = g.getColor();

		g.setColor( Color.white );
		g.fillOval( 0 , 0 , width , width );
		g.setColor( c );
		g.drawOval( 0 , 0 , width , width );
		g.setColor( c );
	}

	private void paintForeground(
			final Graphics g,
			final int xCenter,
			final int yCenter,
			final double smallR,
			final double bigR) {
		final Color c = g.getColor();
		final Font f = g.getFont();
		final Font softFont = f.deriveFont( f.getStyle() , fontSize );
		final Font strongFont = softFont.deriveFont( Font.BOLD );
		g.setFont( softFont );
		double angle = 0;
		for (Beat beat : compas.getBeats()) {
			final double horiz = Math.sin( angle );
			final double vert = -Math.cos( angle );

			g.drawLine(
					(int) (xCenter + smallR * horiz),
					(int) (yCenter + smallR * vert),
					(int) (xCenter + bigR * horiz),
					(int) (yCenter + bigR * vert));

			if (beat.isStrong()) {
				g.setColor( Color.red );
				g.setFont( strongFont );
			}
			final Rectangle2D bounds = g.getFontMetrics().getStringBounds( beat.getName() , g );

			final double horizName = Math.sin( angle  + (tickAngleStep / 2));
			final double vertName = -Math.cos( angle  + (tickAngleStep / 2));

			g.drawString(
					beat.getName(),
					(int) (xCenter + smallR * horizName - bounds.getWidth()/2d),
					(int) (yCenter + smallR * vertName + bounds.getHeight()/2d));
			g.setColor( c );
			g.setFont( softFont );

			angle += tickAngleStep;
		}
		g.setFont( f );
	}
}

