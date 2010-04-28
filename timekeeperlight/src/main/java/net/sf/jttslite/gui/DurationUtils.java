/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jttslite.gui;

import net.sf.jttslite.core.util.DurationImpl;

/**
 * Remaps to {@link com.ost.timekeeper.util.DurationUtils}.
 * 
 *
 * @author davide
 * @deprecated use {@link com.ost.timekeeper.util.DurationUtils} instead
 */
public class DurationUtils {

	public static String format (final DurationImpl d) {
		return net.sf.jttslite.core.util.DurationUtils.format (d);
	}
	
	public static String formatDuration (final long d) {
		return net.sf.jttslite.core.util.DurationUtils.formatDuration (d);
	}	
}
