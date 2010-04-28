/*
 * AbsolutePeriod.java
 *
 * Created on 19 marzo 2005, 19.56
 */

package net.sf.jttslite.core.util;

import java.util.Date;

/**
 * Un periodo temporale assoluto.
 * <p>
 * Qui per <I>assoluto</I> si intende <I>individuabile sull'asse temporale</I>.
 * </p>
 * <p>
 * E' caratterizzato dalle date di inizio e di fine, che determinano quindi una durata.
 * </p>
 * 
 * @author  davide
 */
public interface AbsolutePeriod {

	/**
	 * La data di inizio di questo periodo.
	 * @return la data di inizio di questo periodo.
	 */
	Date getFrom ();
	/**
	 * La data di fine di questo periodo.
	 * @return la data di fine di questo periodo.
	 */
	Date getTo ();
	
	/**
	 * Restituisce <code>true</code> se l'intersezione tra questo periodo e quello specificato non è vuota.
	 *
	 * @param period il periodo da confrontare.
	 * @return <code>true</code> se questo periodo temporale interseca <code>period</code>;
	 * <code>false</code> altrimenti.
	 */
	boolean intersects (final AbsolutePeriod period);
	
	/**
	 * Ritorna l'intersezione tra questo periodo e quello specificato.
	 *
	 * @param period il periodo da intersecare.
	 * @return l'intersezione tra questo periodo e quello specificato.
	 */
	AbsolutePeriod intersection (final AbsolutePeriod period);
	
	/**
	 * Verifica se questo periodo � valido.
	 *
	 * @return <code>true</code> se questo � un periodo temporale valido;
	 * <code>false</code> altrimenti.
	 */
	boolean isValid ();
	
	/**
	 * Verifica se questo periodo non � terminato.
	 *
	 * @return <code>true</code> se questo � un periodo temporale non terminato;
	 * <code>false</code> altrimenti.
	 */
	boolean isEndOpened ();
	
	/**
	 * Ritorna la durata di questo periodo.
	 *
	 * @return la durata.
	 */
	DurationImpl getDuration ();
	
	/**
	 * Ritorna la durata di questo periodo, in millisecondi.
	 * 
	 * @return la durata di questo periodo, in millisecondi.
	 */
	long getTime ();
}
