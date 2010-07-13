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
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceAware;
import javax.jdo.annotations.PersistenceCapable;

/**
 * A template to ease the creation of recurrent pieces of work
 *
 * @author Davide Cavestro
 */
@PersistenceCapable(table="jtts_tasktemplate", detachable="true")
public class ProgressTemplate extends Observable implements PieceOfWorkTemplate {
	
	/**
	 * Holds value of property name.
	 */
	protected String name;

	/**
	 * Holds value of property notes.
	 */
	protected String notes;

	/**
	 * Holds value of property duration.
	 */
	@NotPersistent
	private transient DurationImpl duration;

	protected long milliseconds;

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
		duration = source.duration;
		name = source.name;
		notes = source.notes;
	}	
	
	/**
	 * Getter for property name.
	 * @return Value of property name.
	 */
	@Override
	public String getName () {
		return name;
	}

	/**
	 * Setter for property name.
	 * @param name New value of property name.
	 */
	@Override
	public void setName (final String name) {
		this.name = name;
	}

	/**
	 * Getter for property notes.
	 * @return Value of property notes.
	 */
	@Override
	public String getNotes () {
		return this.notes;
	}

	/**
	 * Setter for property notes.
	 * @param notes New value of property notes.
	 */
	@Override
	public void setNotes (final String notes) {
		this.notes = notes;
	}

	/**
	 * Getter for property duration.
	 * @return Value of property duration.
	 */
	@Override
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
	@Override
	public void setDuration (final DurationImpl duration) {
		/*
		 * caches the data
		 */
		this.duration = duration;
		if (duration !=null) {
			milliseconds = duration.getTime ();
		} else {
			milliseconds = 0;
		}
	}
	
	@Override
	public String toString () {
		final StringBuilder sb = new StringBuilder ();
		sb.append ("name: ").append (name);
		sb.append ("duration: ").append (duration);
		sb.append ("notes: ").append (notes);
		return sb.toString ();
	}


	@Override
	public PieceOfWorkTemplateBackup backup () {
		return new PieceOfWorkTemplateBackupImpl (this);
	}

	/**
	 * Implementa il backup. Classe ad esclusivo uso interno, da non rendere persistente.
	 *<P>
	 * La classe &egrave; statica per evitare l'accesso involontario alle variabili della classe che la contiene. Deve avere l'accesso solamente per estensione!
	 */
	@PersistenceAware
	private static class PieceOfWorkTemplateBackupImpl extends ProgressTemplate implements PieceOfWorkTemplateBackup {

		private final ProgressTemplate source;

		public PieceOfWorkTemplateBackupImpl (final ProgressTemplate p) {
			super (p);
			if (p.duration!=null) {
				setDuration (new DurationImpl (p.duration.getTime ()));
			}
			source = p;
		}

		@Override
		public PieceOfWorkTemplate getSource () {
			return source;
		}
		
		@Override
		public void restore () {
			
			source.milliseconds = milliseconds;
			source.name = name;
			source.notes = notes;
		}
	
	}	
	
}
