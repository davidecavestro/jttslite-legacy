/*
 * DurationUtils.java
 *
 * Created on November 28, 2006, 12:33 AM
 *
 */

package net.sf.jttslite.core.util;

import java.text.DecimalFormat;

/**
 * Utilit&agrave; per la formattazione delle durate.
 *
 * @author Davide Cavestro
 */
public class DurationUtils {
	
	private DurationUtils (){}
	
	private final static DurationNumberFormatter durationNumberFormatter = new DurationNumberFormatter ();

	public static class DurationNumberFormatter extends DecimalFormat {
		public DurationNumberFormatter (){
			setMinimumIntegerDigits (2);
		}
	}
	
	public static String format (final Duration d) {
		final StringBuilder sb = new StringBuilder ();
					
		sb.append (durationNumberFormatter.format (d.getTotalHours ()))
		.append (":")
		.append (durationNumberFormatter.format (d.getMinutes ()))
		.append (":")
		.append (durationNumberFormatter.format (d.getSeconds ()));
		return sb.toString ();		
	}
	
	public static String formatDuration (final long d) {
		final StringBuilder sb = new StringBuilder ();
		
		final long[] f = DurationImpl.getDurationFields (d);
		
		sb.append (durationNumberFormatter.format (f[DurationImpl.HOURS_SLOT]))
		.append (":")
		.append (durationNumberFormatter.format (f[DurationImpl.MINUTES_SLOT]))
		.append (":")
		.append (durationNumberFormatter.format (f[DurationImpl.SECONDS_SLOT]));
		return sb.toString ();
	}
	

	
}
