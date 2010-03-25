/*
 * ProgressItemVisitor.java
 *
 * Created on 4 aprile 2004, 12.50
 */

package net.sf.jttslite.core.model.impl;

/**
 * Permette di variare variare dinamicamente il comportamento dei nodi di avanzamento.
 *
 * @author  davide
 */
public interface ProgressItemVisitor {
	
	/**
	 * Visita il nodo specificato.
	 *
	 * @param item il nodo da visitare.
	 */	
	public void visit (final ProgressItem item);
	
}
