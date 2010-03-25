/*
 * PieceOfWorkBackup.java
 *
 * Created on December 26, 2006, 8:43 PM
 *
 */

package net.sf.jttslite.core.model;

/**
 * Copia di backup del modello di azione.
 * @workaround dovuto a problemi di implementazione dell'undo/redo (annullamento campi interni con JDO)
 *
 * @author Davide Cavestro
 */
public interface PieceOfWorkTemplateBackup extends PieceOfWorkTemplate {
	/**
	 * Ritorna il riferimento al modello originale da cui &egrave; stata estratta questa copia.
	 * @return il modello  originale.
	 */
	PieceOfWorkTemplate getSource ();
	/**
	 * Ripristina lo stato interno salvato in questo backup.
	 */
	void restore ();
}
