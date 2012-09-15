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
import org.jcompas.model.Tense;

/**
 * @author thibautd
 */
public class SimpleReloj extends JPanel implements Reloj {
	private static final Logger log =
		Logger.getLogger(SimpleReloj.class);

	private final int fontSize = 17;

	private final CompasInformation compas;
	private double needleAngle = 0;
	private final double tickAngleStep;

	// for debug mode: fps info
	private int frameCount = 0;
	private long lastFpsPrint = Long.MIN_VALUE;
	private String fpsText = "fps: NA";

	public SimpleReloj(final CompasInformation compas) {
		this.compas = compas;
		this.tickAngleStep = Math.PI * 2 / compas.getTensesCount();
	}

	// /////////////////////////////////////////////////////////////////////////
	// interface
	// /////////////////////////////////////////////////////////////////////////
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

	// /////////////////////////////////////////////////////////////////////////
	// Paint
	// /////////////////////////////////////////////////////////////////////////
	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent( g );

		final int width = Math.min( getWidth() , getHeight() );
		final double smallR = 0.45 * width;
		final double bigR = 0.5 * width;
		final int xCenter = (int) (0.5 * width);
		final int yCenter = xCenter;

		paintStaticPart(
				g,
				width,
				xCenter,
				yCenter,
				smallR,
				bigR);

		paintNeedle(
				g,
				xCenter,
				yCenter,
				smallR);

		if (JCompasGlobal.isDebugMode()) {
			updateFpsInfo( g , 0 , width);
		}
	}

	private void updateFpsInfo(
			final Graphics g,
			final int xText,
			final int yText) {
		if (lastFpsPrint == Long.MIN_VALUE) lastFpsPrint = System.currentTimeMillis();
		frameCount++;

		final long now = System.currentTimeMillis();
		if ( now > lastFpsPrint + 1000 ) {
			fpsText = "fps: "+ (1000d * frameCount / (now - lastFpsPrint));
			frameCount = 0;
			lastFpsPrint = now;
		}
		g.drawString( fpsText , xText , yText );
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

	private void paintStaticPart(
			final Graphics g,
			final int width,
			final int xCenter,
			final int yCenter,
			final double smallR,
			final double bigR) {
		// get defaults
		final Color c = g.getColor();
		final Font f = g.getFont();
		final Font softFont = f.deriveFont( f.getStyle() , fontSize );
		final Font strongFont = softFont.deriveFont( Font.BOLD );
		g.setFont( softFont );

		g.setColor( Color.white );
		g.fillOval( 0 , 0 , width , width );
		g.setColor( c );
		g.drawOval( 0 , 0 , width , width );

		double angle = 0;
		for (Tense tense : compas.getTenses()) {
			final double horiz = Math.sin( angle );
			final double vert = -Math.cos( angle );

			g.drawLine(
					(int) (xCenter + smallR * horiz),
					(int) (yCenter + smallR * vert),
					(int) (xCenter + bigR * horiz),
					(int) (yCenter + bigR * vert));

			if (tense.isStrong()) {
				g.setColor( Color.red );
				g.setFont( strongFont );
			}
			final Rectangle2D bounds = g.getFontMetrics().getStringBounds( tense.getName() , g );

			g.drawString(
					tense.getName(),
					(int) (xCenter + smallR * horiz - bounds.getWidth()/2d),
					(int) (yCenter + smallR * vert + bounds.getHeight()/2d));
			g.setColor( c );
			g.setFont( softFont );

			angle += tickAngleStep;
		}
		g.setFont( f );
	}
}

