/*
 * AbsolutePeriodImpl.java
 *
 * Created on 20 marzo 2005, 8.48
 */

package net.sf.jttslite.core.util;

import net.sf.jttslite.common.util.CalendarUtils;
import java.util.Date;
import java.util.Observable;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Sequence;
import javax.jdo.annotations.SequenceStrategy;

/**
 * Implementazione di periodo assoluto.
 *
 * @author  davide
 */
@PersistenceCapable(table="jtts_action", detachable="true")
@Sequence(name="seq", datastoreSequence="PERIODSEQ", strategy=SequenceStrategy.CONTIGUOUS )
public class AbsolutePeriodImpl extends Observable implements AbsolutePeriod {
	
	@Persistent(valueStrategy=IdGeneratorStrategy.SEQUENCE, sequence="seq")
	private long id;

	/**
	 * La data di inizio periodo.
	 */
	protected Date from;
	
	/**
	 * La data di fine periodo.
	 */
	protected Date to;
	
//	/**
//	 * Durata calcolata (valida).
//	 */
//	private transient boolean isDurationComputed = false;

	@NotPersistent
	private transient Date now = new Date ();

	/**
	 * La durata effettiva attuale (se <TT>isDurationComputed</TT> value <TT>tre</TT>.
	 */
	@NotPersistent
	private transient DurationImpl computedDuration = new DurationImpl (0);

	@NotPersistent
	private transient AbsolutePeriodImpl intersectionSupport;

	/**
	 * Costruttore vuoto.
	 */
	public AbsolutePeriodImpl () {
//		LogManager.getLogManager ().getLogger ("net.sf.jtts").warning ("Creating AbsolutePeriodImpl");
	}
	
	/**
	 * Costruttore con data di inizio e fine.
	 *
	 * @param from la data di inizio.
	 * @param to la data di fine.
	 */
	public AbsolutePeriodImpl (final Date from, final Date to) {
		this.from = cloneDate (from);
		this.to = cloneDate (to);
//		LogManager.getLogManager ().getLogger ("net.sf.jtts").warning ("Creating AbsolutePeriodImpl");
	}
	
	/**
	 *
	 * Costruttore copia.
	 *
	 * @param source la sorgente della copia.
	 */
	public AbsolutePeriodImpl (final AbsolutePeriod source) {
		from = cloneDate (source.getFrom ());
		to = cloneDate (source.getTo ());
//		LogManager.getLogManager ().getLogger ("net.sf.jtts").warning ("Creating AbsolutePeriodImpl");
	}
	
	/**
	 * Ritorna la data di inizio di questo periodo.
	 *
	 * @return la data d'inizio.
	 */
	@Override
	public Date getFrom () {
		return cloneDate (from);
	}
	
	/**
	 * Imposta la data d'inizio per questo periodo.
	 *
	 * @param from la nuova data d'inizio.
	 */
	public synchronized void setFrom (final Date from) {
		if (!CalendarUtils.equals (this.from,from)){
			this.from = cloneDate (from);
//			isDurationComputed = false;
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
		return cloneDate (to);
	}
	
	/**
	 * Imposta la data di fine per questo periodo.
	 *
	 * @param to la nuova data di fine del periodo.
	 */
	public synchronized void setTo (final Date to) {
		if (!CalendarUtils.equals (this.to,to)){
			this.to = cloneDate (to);
//			isDurationComputed = false;
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
		return ! (from.after (period.getTo ())
		|| to.before (period.getFrom ()));
	}
	
	/**
	 * Calcola l'intersezione tra due periodi.
	 *
	 * @param period il periodo da testare.
	 * @return il periodo di intersezione.
	 */
	@Override
	public synchronized AbsolutePeriod intersection (final AbsolutePeriod period){

		final long maxFrom = Math.max (from.getTime (), period.getFrom ().getTime ());
		final long minTo = Math.min (to.getTime (), period.getTo ().getTime ());
		
		if (maxFrom<=minTo){
			final Date fromDate = new Date (maxFrom);
			final Date toDate = new Date (minTo);
			if (intersectionSupport==null) {
				intersectionSupport = new AbsolutePeriodImpl (fromDate, toDate);
			} else {
				intersectionSupport.setFrom (fromDate);
				intersectionSupport.setTo (toDate);
			}

			return intersectionSupport;
//			return new AbsolutePeriodImpl (new Date (maxFrom), new Date (minTo));
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
		return from!=null
		&& to!=null
		&& !from.after (to);
	}
	
	/**
	 * Verifica se questo periodo non � terminato.
	 *
	 * @return <code>true</code> se questo � un periodo temporale non terminato;
	 * <code>false</code> altrimenti.
	 */
	@Override
	public synchronized boolean isEndOpened () {
		return from!=null
		&& to==null;
	}
	
	/**
	 * Ritorna la durata di questo periodo.
	 *
	 * @return la durata.
	 */
	@Override
	public synchronized DurationImpl getDuration (){
		if (isEndOpened ()){
			now.setTime (System.currentTimeMillis ());
			computedDuration.setDates (from, now);
//			return new DurationImpl (from, new Date ());
		} else {
			computedDuration.setDates (from, to);
//			if (!isDurationComputed){
//				computedDuration = new DurationImpl (from, to);
//				isDurationComputed = true;
//			}
//			return computedDuration;
		}
		return computedDuration;
	}

	protected Date cloneDate (final Date d) {
		return d!=null?(Date)d.clone():d;
	}
	
	/**
	 * Ritorna la rappresentazione di formato stringa di questo periodo.
	 *
	 * @return una stringa che rappresenta questo periodo.
	 */
	@Override
	public String toString (){
		final StringBuilder sb = new StringBuilder ();
		sb.append ("from: ").append (CalendarUtils.toTSString (from))
		.append (" to: ").append (CalendarUtils.toTSString (to));
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
	public int compareToStart (final AbsolutePeriod compare){
		return from.compareTo (compare.getFrom ());
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
	public int compareToFinish (final AbsolutePeriod compare){
		return to.compareTo (compare.getTo ());
	}

	@Override
	public long getId () {
		return id;
	}
	
	/**
	 *
	 * @param object
	 * @return
	 */	
	@Override
	public boolean equals (final Object object){
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
			return test.getFrom ().equals (from) && test.getTo ().equals (to);
		}
		return false;
	}
	
	@Override
	public int hashCode (){
		final StringBuilder sb = new StringBuilder ();
		sb.append (getClass ().getName ()).append ("@@@");
		sb.append (from);
		sb.append ("@@@");
		sb.append (to);
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
		return System.currentTimeMillis () - from.getTime ();
	}
}

