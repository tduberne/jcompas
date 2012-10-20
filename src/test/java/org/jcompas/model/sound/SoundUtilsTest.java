/* *********************************************************************** *
 * project: org.jcompas.*
 * SoundUtilsTest.java
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
package org.jcompas.model.sound;

import java.util.Random;

import javax.sound.sampled.AudioFormat;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author thibautd
 */
public class SoundUtilsTest {
	@Test
	public void testConversion() throws Exception {
		byte[] testBytes = new byte[ 16 * 100 ];
		new Random( 123 ).nextBytes( testBytes );
		AudioFormat f = new AudioFormat( 10 , 16 , 2 , true , false );

		double[] ds = SoundUtils.convertSoundToDouble( f , testBytes );

		byte[] newBytes = SoundUtils.convertSoundToBytes( f , ds );

		Assert.assertEquals(
				"unexpected length after reconversion",
				testBytes.length,
				newBytes.length);

		for (int i=0; i<testBytes.length; i++) {
			Assert.assertEquals(
					"values before and after reconversion do not match",
					testBytes[i],
					newBytes[i]);
		}
	}
}

