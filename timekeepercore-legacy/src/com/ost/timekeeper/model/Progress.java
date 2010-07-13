/*
 * Progress.java
 *
 * Created on 6 marzo 2005, 12.23
 */

package com.ost.timekeeper.model;

import java.util.Date;
import net.sf.jttslite.core.model.PieceOfWork;
import net.sf.jttslite.core.model.PieceOfWorkBackup;
import net.sf.jttslite.core.model.Task;

/**
 * Un avanzamento.
 * <P>
 * Anche se per implementare l'interfaccia <TT>PieceOfWork</TT>, utilizza <TT>Task</TT>, questa classe DEVE ricevere oggetti di tipo effettivo <TT>ProgressItem</TT> per funzionare correttamente.
 *<BR>
 *Questo comportamento anomalo &egrave; dovuto all'utilizzo di codice JDO legacy.
 *
 * @author  davide
 */
public class Progress extends Period implements PieceOfWork {
	
	protected ProgressItem progressItem;
	
	/** Costruttore vuoto. */
	private Progress () {
	}
	
	/** 
	 * Costruttore con data di inizio, fine, enodo di appartenenza.
	 *
	 * @param from la data di inizio.
	 * @param to la data di fine.
	 * @param progressItem il nodo di appartenenza dell'avanzamento.
	 */
	public Progress (final Date from, final Date to, final ProgressItem progressItem) {
		super(from, to);
		this.progressItem = progressItem;
	}
	
	/**
	 *
	 * Costruttore copia.
	 *
	 * @param source la sorgente della copia.
	 */
	public Progress (final Progress source) {
		super(source);
		this.progressItem=source.progressItem;
	}
	/**
	 * Ritorna il nodo di appartnenenza dell'avanzamento.
	 *
	 * @return il nodo di appartnenenza dell'avanzamento.
	 */	
	public Task getTask (){return this.progressItem;}
	/**
	 * Imposta il nodo di appartnenenza dell'avanzamento.
	 *
	 * @param progressItem il nodo di appartnenenza dell'avanzamento.
	 */	
	public void setTask (final Task progressItem){this.progressItem=(ProgressItem)progressItem;}
	
	public PieceOfWorkBackup backup () {
		return new PieceOfWorkBackupImpl (this);
	}

	public long getId () {
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	/**
	 * Implementa il backup. Classe ad esclusivo uso interno, da non rendere persistente.
	 *<P>
	 * La classe &egrave; statica per evitare l'accesso involontario alle variabili della classe che la contiene. Deve avere l'accesso solamente per estensione!
	 */
	private static class PieceOfWorkBackupImpl extends Progress implements PieceOfWorkBackup {
		private final Progress _source;
		public PieceOfWorkBackupImpl (Progress p) {
			super (p);
			_source = p;
		}
		public PieceOfWork getSource () {
			return _source;
		}
		
		public void restore () {
			
			_source.from = safeFromAccessor ();
			_source.to = safeToAccessor ();
			_source.description = description;
			_source.notes = notes;
			_source.progressItem = progressItem;
		}
	
	}
}
