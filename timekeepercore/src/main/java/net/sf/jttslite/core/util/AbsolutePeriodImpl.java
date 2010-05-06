/*
 * AbsolutePeriodImpl.java
 *
 * Created on 20 marzo 2005, 8.48
 */

package net.sf.jttslite.core.util;

import net.sf.jttslite.common.util.CalendarUtils;
import java.util.Date;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementazione di periodo assoluto.
 *
 * @author  davide
 */
public class AbsolutePeriodImpl extends Observable implements AbsolutePeriod {
	
	/**
	 * La data di inizio periodo.
	 */
	private Date _from;
	
	/**
	 * La data di fine periodo.
	 */
	private Date _to;
	
	/**
	 * Durata calcolata (valida).
	 */
	private transient boolean isDurationComputed = false;
	
	/**
	 * La durata effettiva attuale (se <TT>isDurationComputed</TT> value <TT>tre</TT>.
	 */
	private transient DurationImpl computedDuration;
	
	/**
	 * Costruttore vuoto.
	 */
	public AbsolutePeriodImpl () {
		Logger.getAnonymousLogger ().warning ("Creating AbsolutePeriodImpl");
	}
	
	/**
	 * Costruttore con data di inizio e fine.
	 *
	 * @param from la data di inizio.
	 * @param to la data di fine.
	 */
	public AbsolutePeriodImpl (final Date from, final Date to) {
		this._from = from;
		this._to = to;
		Logger.getAnonymousLogger ().warning ("Creating AbsolutePeriodImpl");
	}
	
	/**
	 *
	 * Costruttore copia.
	 *
	 * @param source la sorgente della copia.
	 */
	public AbsolutePeriodImpl (final AbsolutePeriod source) {
		_from = source.getFrom ();
		_to = source.getTo ();
		Logger.getAnonymousLogger ().warning ("Creating AbsolutePeriodImpl");
	}
	
	/**
	 * Ritorna la data di inizio di questo periodo.
	 *
	 * @return la data d'inizio.
	 */
	@Override
	public Date getFrom () {
		return _from;
	}
	
	/**
	 * Imposta la data d'inizio per questo periodo.
	 *
	 * @param from la nuova data d'inizio.
	 */
	public synchronized void setFrom (Date from) {
		if (!CalendarUtils.equals (_from,from)){
			_from = from;
			isDurationComputed = false;
			setChanged ();
			notifyObservers ();
		}
	}
	
	/**
	 * Ritorna la data di fine di questo periodo.
	 *
	 * @return la data di fine.
	 */
	@Override
	public Date getTo () {
		return _to;
	}
	
	/**
	 * Imposta la data di fine per questo periodo.
	 *
	 * @param to la nuova data di fine del periodo.
	 */
	public synchronized void setTo (Date to) {
		if (!CalendarUtils.equals (this._to,to)){
			_to = to;
			isDurationComputed = false;
			setChanged ();
			notifyObservers ();
		}
	}
	
	/**
	 * Verifica l'intersezione non vuota tra due periodi.
	 *
	 * @param period il periodo da testare.
	 * @return <code>true</code> se questo periodo temporale interseca <code>period</code>;
	 * <code>false</code> altrimenti.
	 */
	@Override
	public synchronized boolean intersects (final AbsolutePeriod period){

		if (!isValid ()){
			return false;
		}
		if (!period.isValid ()){
			return false;
		}
		return ! (getFrom ().after (period.getTo ())
		|| getTo ().before (period.getFrom ()));
	}
	
	/**
	 * Calcola l'intersezione tra due periodi.
	 *
	 * @param period il periodo da testare.
	 * @return il periodo di intersezione.
	 */
	@Override
	public synchronized AbsolutePeriod intersection (final AbsolutePeriod period){

		final long maxFrom = Math.max (getFrom ().getTime (), period.getFrom ().getTime ());
		final long minTo = Math.min (getTo ().getTime (), period.getTo ().getTime ());
		
		if (maxFrom<=minTo){
			return new AbsolutePeriodImpl (new Date (maxFrom), new Date (minTo));
		}
		return null;
	}
	
	/**
	 * Verifica se questo periodo � valido.
	 *
	 * @return <code>true</code> se questo � un periodo temporale valido;
	 * <code>false</code> altrimenti.
	 */
	@Override
	public boolean isValid () {
		return _from!=null
		&& _to!=null
		&& !_from.after (_to);
	}
	
	/**
	 * Verifica se questo periodo non � terminato.
	 *
	 * @return <code>true</code> se questo � un periodo temporale non terminato;
	 * <code>false</code> altrimenti.
	 */
	@Override
	public synchronized boolean isEndOpened () {
		return _from!=null
		&& _to==null;
	}
	
	/**
	 * Ritorna la durata di questo periodo.
	 *
	 * @return la durata.
	 */
	@Override
	public synchronized DurationImpl getDuration (){
		if (isEndOpened ()){
			return new DurationImpl (_from, new Date ());
		} else {
			if (!isDurationComputed){
				computedDuration = new DurationImpl (_from, _to);
				isDurationComputed = true;
			}
			return computedDuration;
		}
	}
	
	/**
	 * Ritorna la rappresentazione di formato stringa di questo periodo.
	 *
	 * @return una stringa che rappresenta questo periodo.
	 */
	@Override
	public String toString (){
		final StringBuilder sb = new StringBuilder ();
		sb.append ("from: ").append (CalendarUtils.toTSString (_from))
		.append (" to: ").append (CalendarUtils.toTSString (_to));
		return sb.toString ();
	}
	
	/**
	 * Compara dueperiodi per l'ordinamento secondo la data d'inizio.
	 *
	 * @param compare il periodo da comparare.
	 * @return il valore <TT>0</TT> se le date di inizio sono uguali;
	 * un valore negativo se la data d'inizio del periodo specificato � 
	 * antecedente all'inizio di questo periodo;
	 * e un valore positivo se la data d'inizio del periodo specificato � 
	 * posteriore all'inizio di questo periodo;
	 * @see java.util.Date#compareTo
	 */	
	public int compareToStart (AbsolutePeriod compare){
		return _from.compareTo (compare.getFrom ());
	}
	
	/**
	 * Compara due periodi per l'ordinamento secondo la data di fine.
	 *
	 * @param compare il periodo da comparare.
	 * @return il valore <TT>0</TT> se le date di fine sono uguali;
	 * un valore negativo se la data di fine del periodo specificato � 
	 * antecedente alla fine di questo periodo;
	 * e un valore positivo se la data di fine del periodo specificato � 
	 * posteriore alla fine di questo periodo;
	 * @see java.util.Date#compareTo
	 */	
	public int compareToFinish (AbsolutePeriod compare){
		return _to.compareTo (compare.getTo ());
	}
	
	/**
	 *
	 * @param object
	 * @return
	 */	
	@Override
	public boolean equals (Object object){
		if (this == object){
			return true;
		}
		if (null == object){
			return false;
		}
		if (object instanceof AbsolutePeriod){
			final AbsolutePeriod test = (AbsolutePeriod)object;
			if (!test.isValid ()){
				return false;
			}
			if (!isValid ()){
				return false;
			}
			return test.getFrom ().equals (_from) && test.getTo ().equals (_to);
		}
		return false;
	}
	
	@Override
	public int hashCode (){
		final StringBuilder sb = new StringBuilder ();
		sb.append (getClass ().getName ()).append ("@@@");
		sb.append (_from);
		sb.append ("@@@");
		sb.append (_to);
		return sb.toString ().hashCode ();
	}

	@Override
	public long getTime () {
		if (isEndOpened ()){
			return toNow ();
		} else {
			return getDuration ().getTime ();
		}		
	}

	private long toNow () {
		return System.currentTimeMillis () - _from.getTime ();
	}
}

