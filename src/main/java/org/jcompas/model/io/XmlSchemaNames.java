/* *********************************************************************** *
 * project: org.jcompas.*
 * XmlSchemaNames.java
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
package org.jcompas.model.io;

/**
 * @author thibautd
 */
public class XmlSchemaNames {
	private XmlSchemaNames() {}

	public static final String PATTERN_TAG = "pattern";
	public static final String PATTERN_NAME_ATT = "name";
	public static final String PATTERN_NCOMPAS_ATT = "nCompas";
	public static final String PATTERN_NREPEAT_ATT = "nRepeat";

	public static final String PATTERN_ESTILO_TAG = "estilo";
	public static final String PATTERN_ESTILO_ATT = "file";

	public static final String MUSICIAN_TAG = "musician";
	public static final String MUSICIAN_NAME_ATT = "name";

	public static final String SOUND_TAG = "sound";
	public static final String SOUND_DIR_ATT = "directory";

	public static final String ESTILO_TAG = "estilo";
	public static final String ESTILO_NAME_ATT = "name";
	public static final String ESTILO_PALO_ATT = "palo";

	public static final String COMPAS_TAG = "compas";
	public static final String COMPAS_BPM_ATT = "bpm";
	public static final String BEAT_TAG = "beat";
	public static final String BEAT_NAME_ATT = "name";
	public static final String BEAT_POS_ATT = "pos";
	public static final String BEAT_STRONG_ATT = "strong";
}

