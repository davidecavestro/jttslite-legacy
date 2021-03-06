/*
 * PieceOfWorkTemplate.java
 *
 * Created on July 20, 2008, 7:48 AM
 *
 */

package com.davidecavestro.timekeeper.model;

import com.ost.timekeeper.util.Duration;

/**
 * A template to ease the creation of recurrent pieces of work
 *
 * @author Davide Cavestro
 */
public interface PieceOfWorkTemplate {

	/**
	 * Getter for property name.
	 * @return Value of property name.
	 */
	String getName ();

	/**
	 * Setter for property name.
	 * @param name New value of property name.
	 */
	void setName (String name);

	/**
	 * Getter for property notes.
	 * @return Value of property notes.
	 */
	String getNotes ();

	/**
	 * Setter for property notes.
	 * @param notes New value of property notes.
	 */
	void setNotes (String notes);


	/**
	 * Getter for property duration.
	 * @return Value of property duration.
	 */
	Duration getDuration ();

	/**
	 * Setter for property duration.
	 * @param duration New value of property duration.
	 */
	void setDuration (Duration duration);
	
	/**
	 * Ritorna un clone di questo avanzamento, a solo scopo di backup.
	 */
	PieceOfWorkTemplateBackup backup ();

}
