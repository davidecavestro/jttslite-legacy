/*
 * DurationImpl.java
 *
 * Created on 8 maggio 2004, 11.17
 */

package net.sf.jttslite.core.util;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Durata immutabile.
 * 
 * <p>
 * La durata può essere definita come intervallo di tempo che intercorre tra una
 * data di inizio e una di fine, oppure semplicemente come un numero fisso di
 * millisecondi.
 * </p>
 * 
 *
 * @author  davide
 */
public final class DurationImpl implements Duration {

	/**
	 * Posizione dello slot contenente il valore delle ore, nel risultato del metodo <CODE>getDurationFields</CODE>.
	 */
	static int HOURS_SLOT = 2;
	/**
	 * Posizione dello slot contenente il valore dei minuti, nel risultato del metodo <CODE>getDurationFields</CODE>.
	 */
	static int MINUTES_SLOT = 1;
	/**
	 * Posizione dello slot contenente il valore dei secondi, nel risultato del metodo <CODE>getDurationFields</CODE>.
	 */
	static int SECONDS_SLOT = 0;

	private Date from;
	private Date to;



	private boolean computedTotalMilliseconds;

	private boolean computedMilliseconds = false;
	private long totalMilliseconds = 0;
	private long modMilliseconds = 0;

	private boolean computedSeconds = false;
	private long modSeconds = 0;

	private boolean computedMinutes = false;
	private long modMinutes = 0;

	private boolean computedHours = false;
	private long totalHours = 0;
	private long modHours = 0;

	private boolean computedDays = false;
	private long totalDays = 0;

	/**
	 * Durata nulla.
	 */
	public final static DurationImpl ZERODURATION = new DurationImpl (0);
	
	/**
	 * Costruttore con data di inizio e fine periodo.
	 *
	 * @param from data d'inizio.
	 * @param to data di fine del periodo.
	 */
	public DurationImpl(Date from, Date to) {
		this.from = from;
		this.to = to;
		computeFields ();
		Logger.getLogger ("net.sf.jtts").log (Level.WARNING, "Creating DurationImpl");
	}
	
	/**
	 * Costruttore con durata in millisecondi.
	 *
	 * @param milliseconds il numero di millisecondi.
	 */	
	public DurationImpl (final long milliseconds) {
		totalMilliseconds = milliseconds;
		computedTotalMilliseconds = true;
		computeFields ();
		Logger.getLogger ("net.sf.jtts").log (Level.WARNING, "Creating DurationImpl");
	}
	
	/**
	 * Costruttore con durata in ore minuti secondi e millisecondi.
	 *
	 * @param hours ore
	 * @param minutes minuti
	 * @param seconds secondi
	 * @param milliseconds millisecondi.
	 */	
	public DurationImpl (final int hours, final int minutes, final int seconds, final int milliseconds) {
		totalMilliseconds = milliseconds + MILLISECONDS_PER_SECOND*seconds+MILLISECONDS_PER_MINUTE*minutes+MILLISECONDS_PER_HOUR*hours;
		computedTotalMilliseconds = true;
		computeFields ();
		Logger.getLogger ("net.sf.jtts").log (Level.WARNING, "Creating DurationImpl");
	}
	
	private final void computeFields (){
		if (!computedTotalMilliseconds){
			computeTotalMilliseconds ();
		}
		if (!computedMilliseconds){
			computeMilliseconds ();
		}
		if (!computedSeconds){
			computeSeconds ();
		}
		if (!computedMinutes){
			computeMinutes ();
		}
		if (!computedHours){
			computeHours ();
		}
		if (!computedDays){
			computeDays ();
		}
	}

	
	private final void computeTotalMilliseconds (){
		totalMilliseconds = to.getTime() - from.getTime();
		computedTotalMilliseconds = true;
	}

	private final void computeMilliseconds (){
		modMilliseconds = totalMilliseconds % MILLISECONDS_PER_SECOND;
		computedMilliseconds = true;
	}
	

	private final void computeSeconds (){
		final long m = computeSeconds (totalMilliseconds);
//		totalSeconds = m[TOTAL_SLOT];
		modSeconds = m;
		computedSeconds = true;
	}
	
	/**
	 * Ritorna un array a due elementi, contenente il numero totale di secondi corrispondenti al numero di millisecondi specificato,
	 * ed il numero di minuti rimanenti, modulo {@link #SECONDS_PER_MINUTE}.
	 * 
	 * @param millis il numero di millisecondi.
	 */
	private final static long computeSeconds (final long millis) {
		final long total = millis / MILLISECONDS_PER_SECOND;
		return total % SECONDS_PER_MINUTE;
//		return new long [] {total, mod};
	}
	

	private final void computeMinutes (){
		final long m = computeMinutes (totalMilliseconds);
//		totalMinutes = m[TOTAL_SLOT];
		modMinutes = m;
		computedMinutes = true;
	}
	
	/**
	 * Ritorna un array a due elementi, contenente il numero totale di minuti 
	 * corrispondenti al numero di millisecondi specificato,
	 * ed il numero di minuti rimanenti, modulo {@link #MINUTES_PER_HOUR}.
	 * 
	 * @param millis il numero di millisecondi.
	 */
	private final static long computeMinutes (final long millis) {
		final long total = millis / MILLISECONDS_PER_MINUTE;
		return total % MINUTES_PER_HOUR;
//		return new long [] {total, mod};
	}
	
	private final void computeHours (){
		totalHours = computeTotalHours (totalMilliseconds);
		modHours = totalHours % HOURS_PER_DAY;
		computedHours = true;
	}
	
	private final static long computeTotalHours (final long millis){
		return  millis / MILLISECONDS_PER_HOUR;
	}
	
	private final void computeDays (){
		totalDays = totalMilliseconds / MILLISECONDS_PER_DAY;
		computedDays = true;
	}
	
	/**
	 * Ritorna i millisecondi di questa durata.
	 *
	 * @return i millisecondi di questa durata.
	 */	
	@Override
	public long getMilliseconds (){
		return modMilliseconds;
	}
	
	/**
	 * Ritorna i secondi di questa durata.
	 *
	 * @return i secondi di questa durata.
	 */	
	@Override
	public long getSeconds (){
		return modSeconds;
	}
	
	/**
	 * Ritorna i minuti di questa durata.
	 *
	 * @return i minuti di questa durata.
	 */	
	@Override
	public long getMinutes (){
		return modMinutes;
	}
	
	/**
	 * Ritorna le ore di questa durata.
	 *
	 * @return le ore di questa durata.
	 */	
	@Override
	public long getHours (){
		return modHours;
	}
	
	/**
	 * Ritorna i giorni di questa durata.
	 *
	 * @return i giorni di questa durata.
	 */	
	@Override
	public long getDays (){
		return totalDays;
	}
	
	/**
	 * Ritorna il numero totale di ore di questa durata.
	 *
	 * @return il numero totale di ore di questa durata.
	 */	
	@Override
	public long getTotalHours (){
		return totalHours;
	}
	
	/**
	 * Ritorna questa durata in millisecondi.
	 *
	 * @return questa durata in millisecondi.
	 */	
	@Override
	public long getTime (){
		return totalMilliseconds;
	}

	/**
	 * Reimposta queesta durata.
	 * <p>
	 * Impone il ricalcolo d itutti i campi interni.
	 * <p>
	 * @param time il nuovo valore di  questa durata.
	 */
	public void setTime (final long time) {
		totalMilliseconds = time;
		computedTotalMilliseconds = true;
		resetFields ();
	}

	/**
	 * Ritorna un array contenente il valore, dei campi <PRE>[secondi, minuti, ore]</PRE> relatici alla durata specificata.
	 * <BR>
	 * Tali valori corrispondono ai valori che ritornerebbero le chiamate ai metodi <PRE>[getSeconds (), getMinutes (), getTtalHours ()] </PRE>di una DurationImpl creta a partire dal numero di millisecondi specificato.
	 * 
	 * @param millis il numerodi millisecondi
	 * @return un array contenente il valore, dei campi <PRE>[secondi, minuti, ore]</PRE> relatici alla durata specificata.
	 */
	public final static long[] getDurationFields (final long millis) {
		return new  long[] {computeSeconds (millis), computeMinutes (millis), computeTotalHours (millis)};
	}

	private void resetFields () {
		computedMilliseconds= false;
		computedSeconds = false;
		computedMinutes = false;
		computedHours = false;
		computedDays = false;

		computeFields ();
	}
}
