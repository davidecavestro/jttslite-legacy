/*
 * AdvancingTicListener.java
 *
 * Created on November 30, 2006, 12:15 AM
 *
 */

package net.sf.jttslite.gui;

import java.util.EventListener;

/**
 * Interfaccia usata dagli oggetti che intendono essere temporizzati con l'avanzamento in progress.
 *
 * @author Davide Cavestro
 */
public interface AdvancingTicListener extends EventListener {
	/**
	 * Notifica l'evento di temporizzazione dell'avanzamento.
	 * 
	 * 
	 * @param e l'evento
	 */
	void advanceTic (AdvancingTicEvent e);
}
