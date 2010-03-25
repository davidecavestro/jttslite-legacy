/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.davidecavestro.timekeeper.gui;

import com.ost.timekeeper.util.Duration;

/**
 * Remaps to {@link com.ost.timekeeper.util.DurationUtils}.
 * 
 *
 * @author davide
 * @deprecated use {@link com.ost.timekeeper.util.DurationUtils} instead
 */
public class DurationUtils {

	public static String format (final Duration d) {
		return com.ost.timekeeper.util.DurationUtils.format (d);
	}
	
	public static String formatDuration (final long d) {
		return com.ost.timekeeper.util.DurationUtils.formatDuration (d);
	}	
}
