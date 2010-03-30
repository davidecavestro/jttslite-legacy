/*
 * PieceOfWork.java
 *
 * Created on November 11, 2006, 12:50 PM
 *
 */

package net.sf.jttslite.core.model;

import net.sf.jttslite.core.util.LocalizedPeriod;
import java.util.Date;

/**
 * Interfaccia per la registrazione di un periodo di lavoro.
 *
 * @author Davide Cavestro
 */
public interface PieceOfWork extends LocalizedPeriod {
	
	/**
	 * Ritorna il nodo di appartnenenza del periodo di lavoro.
	 *
	 * @return il nodo di appartnenenza del periodo di lavoro.
	 */	
	Task getTask ();
	/**
	 * Imposta il nodo di appartnenenza del periodo di lavoro.
	 *
	 * @param task il nodo di appartnenenza del periodo di lavoro.
	 */	
	void setTask (Task task);
	
	/** 
	 * Imposta la data di inizio per questo periodo di lavoro.
	 *
	 * @param from to la nuova data di inizio del periodo di lavoro.
	 */
	void setFrom (Date from);
		
	/** 
	 * Imposta la data di fine per questo periodo di lavoro.
	 *
	 * @param to la nuova data di fine del periodo di lavoro.
	 */
	void setTo(Date to);
	
	/** 
	 * Ritorna la descrizione di questo periodo di lavoro.
	 *
	 * @return la descrizione di periodo di lavoro.
	 */
	String getDescription();
	/** 
	 * Imposta la descrizione di questo periodo di lavoro.
	 *
	 * @param d la descrizione di periodo di lavoro.
	 */
	void setDescription(String d);
	
	/**
	 * Ritorna un clone di questo avanzamento, a solo scopo di backup.
	 *
	 * @return un clone di questo avanzamento, a solo scopo di backup.
	 */
	PieceOfWorkBackup backup ();

   /**
    * Restituisce le note dell'avanzamento
    * @return le note dell'avanzamento
    */
   String getNotes();
}
