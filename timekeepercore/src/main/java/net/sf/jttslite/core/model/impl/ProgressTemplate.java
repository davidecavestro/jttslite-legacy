/*
 * PieceOfWorkTemplate.java
 *
 * Created on July 20, 2008, 7:48 AM
 *
 */

package net.sf.jttslite.core.model.impl;

import net.sf.jttslite.core.model.PieceOfWorkTemplate;
import net.sf.jttslite.core.model.PieceOfWorkTemplateBackup;
import net.sf.jttslite.core.util.DurationImpl;
import java.util.Observable;

/**
 * A template to ease the creation of recurrent pieces of work
 *
 * @author Davide Cavestro
 */
public class ProgressTemplate extends Observable implements PieceOfWorkTemplate {
	
	/**
	 * Costruttore.
	 */
	public ProgressTemplate () {
	}

	/**
	 *
	 * Costruttore copia.
	 *
	 * @param source la sorgente della copia.
	 */
	public ProgressTemplate (final ProgressTemplate source) {
		this.duration = source.duration;
		this.name = source.name;
		this.notes = source.notes;
	}	
	
	/**
	 * Holds value of property name.
	 */
	protected String name;

	/**
	 * Getter for property name.
	 * @return Value of property name.
	 */
	public String getName () {
		return this.name;
	}

	/**
	 * Setter for property name.
	 * @param name New value of property name.
	 */
	public void setName (String name) {
		this.name = name;
	}

	/**
	 * Holds value of property notes.
	 */
	protected String notes;

	/**
	 * Getter for property notes.
	 * @return Value of property notes.
	 */
	public String getNotes () {
		return this.notes;
	}

	/**
	 * Setter for property notes.
	 * @param notes New value of property notes.
	 */
	public void setNotes (String notes) {
		this.notes = notes;
	}

	/**
	 * Holds value of property duration.
	 */
	private transient DurationImpl duration;
	protected long milliseconds;

	/**
	 * Getter for property duration.
	 * @return Value of property duration.
	 */
	public DurationImpl getDuration () {
		/*
		 * Since only hthe milliseconds internal field is persisted,
		 * we should checkif the DurationImpl field is aligned with it.
		 */
		if (milliseconds < 0) {
			/*
			 * the duration should be null
			 */
			return null;
		}
		if (duration!=null ) {
			/*
			 * the internal field is not null
			 */
			if (duration.getTime ()==milliseconds) {
				/*
				 * the internal field is still valid
				 */
				return duration;
			}
		}
		duration = new DurationImpl (milliseconds);
		
		return duration;
	}

	/**
	 * Setter for property duration.
	 * @param duration New value of property duration.
	 */
	public void setDuration (final DurationImpl duration) {
		/*
		 * caches the data
		 */
		this.duration = duration;
		if (duration !=null) {
			this.milliseconds = duration.getTime ();
		} else {
			this.milliseconds = 0;
		}
	}
	
	public String toString () {
		final StringBuilder sb = new StringBuilder ();
		sb.append ("name: ").append (name);
		sb.append ("duration: ").append (duration);
		sb.append ("notes: ").append (notes);
		return sb.toString ();
	}


	public PieceOfWorkTemplateBackup backup () {
		return new PieceOfWorkTemplateBackupImpl (this);
	}

	/**
	 * Implementa il backup. Classe ad esclusivo uso interno, da non rendere persistente.
	 *<P>
	 * La classe &egrave; statica per evitare l'accesso involontario alle variabili della classe che la contiene. Deve avere l'accesso solamente per estensione!
	 */
	private static class PieceOfWorkTemplateBackupImpl extends ProgressTemplate implements PieceOfWorkTemplateBackup {
		private final ProgressTemplate _source;
		public PieceOfWorkTemplateBackupImpl (final ProgressTemplate p) {
			super (p);
			if (p.duration!=null) {
				setDuration (new DurationImpl (p.duration.getTime ()));
			}
			_source = p;
		}
		public PieceOfWorkTemplate getSource () {
			return _source;
		}
		
		public void restore () {
			
			_source.milliseconds = milliseconds;
			_source.name = name;
			_source.notes = notes;
		}
	
	}	
	
}
