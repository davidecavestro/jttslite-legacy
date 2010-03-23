/*
 * BeforeDateFIlter.java
 *
 * Created on 31 gennaio 2005, 21.21
 */

package com.davidecavestro.timekeeper.report.filter.flavors.date;

import com.davidecavestro.common.util.CalendarUtils;
import com.davidecavestro.timekeeper.report.filter.AbstractFilter;
import java.util.*;

/**
 * Implementa il filtro 'data anteriore a'.
 *
 * @author  davide
 */
public final class BeforeDateFilter extends AbstractFilter{
	
	/**
	 * La data del filtro.
	 */
	private Date _date;
	
	/**
	 * Costruttore.
	 *
	 * @param date la data di test.
	 */
	public BeforeDateFilter (final Date date) {
		this._date = date;
	}
	
	/**
	 * Ritorna la data di test del filtro.
	 *
	 * @return la data di test del filtro.
	 */	
	public Date getDate (){
		return this._date;
	}
	
	/**
	 * Ritorna <TT>true</TT> se questo filtro � uguale al filtro specificato.
	 *
	 * @param obj ilfiltro da testare.
	 * @return <TT>true</TT> se questo filtro � uguale al filtro specificato.
	 */	
	public boolean equals (Object obj){
		if (this==obj){
			/* identita' */
			return true;
		}
		if (obj instanceof BeforeDateFilter){
			final BeforeDateFilter filter = (BeforeDateFilter)obj;
			return this._date==filter._date || this._date.equals (filter._date);
		}
		/* tipo incompatibile */
		return false;
	}
	
	/**
	 * Ritorna <TT>true</TT> se l'oggetto specificato soddisfa questo filtro.
	 *
	 * @param obj l'oggetto da filtrare-
	 * @return <TT>true</TT> se l'oggetto specificato soddisfa questo filtro.
	 */	
	public boolean matches (Object obj) {
		return ((Date)obj).before (this._date);
	}

	/**
	 * Ritorna una rappresentazione in formato stringa di questo filtro.
	 *
	 * @return una stringa che rappresenta questo filtro.
	 */	
	public String toString (){
		final StringBuilder sb = new StringBuilder ();
		sb.append ("[BEFORE: ");
		sb.append (CalendarUtils.toTSString (this._date));
		sb.append ("] ");
		return sb.toString ();
	}
}
