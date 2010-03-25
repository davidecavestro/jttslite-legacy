/*
 * WorkSpaceBackup.java
 *
 * Created on April 25, 2009, 7:48 PM
 *
 */

package net.sf.jttslite.core.model;

/**
 * Copia di backup del modello di progetto.
 *
 * @workaround dovuto a problemi di implementazione dell'undo/redo (annullamento campi interni con JDO)
 *
 * @author Davide Cavestro
 */
public interface WorkSpaceBackup extends WorkSpace {

	/**
	 * Ritorna il riferimento al modello originale da cui è stata estratta questa copia.
	 * @return il modello  originale.
	 */
	WorkSpace getSource ();
	/**
	 * Ripristina lo stato interno salvato in questo backup.
	 */
	void restore ();
}
