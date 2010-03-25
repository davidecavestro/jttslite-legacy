/*
 * PredictiveTransferhandler.java
 *
 * Created on December 13, 2006, 11:48 PM
 *
 */

package net.sf.jttslite.gui.dnd;

/**
 * Interfaccia che definisce l'abilitazione delle azioni legate al transfer handler, ovvero al trasferimento dati tramite copia incolla, dnd, etc...
 *
 * @author Davide Cavestro
 */
public interface PredictiveTransferhandler {
	boolean canCopy ();
	boolean canCut ();
	boolean canPaste ();
}
