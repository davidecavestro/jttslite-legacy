/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jttslite.gui.dnd;

import javax.swing.tree.TreePath;


/**
 * Quest'interfaccia contiene dei metodi per controllare il 
 * comportamento del Drag&Drop implementato dal componente DDJXTreeTable.
 * Tale interfaccia deve essere implementata dalla classe che utilizza
 * il componente DDJXTreeTable.
 * 
 * @author Stefano Gallina
 *
 */
public interface IDragAndDrop {

	/**
	 * Metodo che gestisce l'azione dell'eliminazione dell'oggetto in stato di "DRAG" 
	 * 
	 * @param percorso sorgente
	 */
	public boolean removeDraggedElement(Object pathSource); 
	
	
	/**
	 * Metodo che gestisce l'inserimento dell'oggetto in stato di "DRAG" nella 
	 * nuova posizione scelta
	 * 
	 * 
	 * @param pathTarget percorso di destinazione
	 * @param pathSource percorso sorgente
	 */
	public boolean insertDroppedElement(Object pathTarget,Object pathSource);

	/**
	 * Metodo che permette di gestire la selezione degli elementi
	 * della JXTreeTable in fase di DRAG
	 * 
	 * @param selectPath il path dell'elemento selezionato
	 * @param table il riferimento al componente DDJXTreeTable
	 * @param row l'indice della riga selezionata
	 */
	public void selectTargetElement(TreePath selectPath, DDJXTreeTable table,int row);
	
	
	/**
	 * Metodo che controlla se un elemento pu� essere "DRAGGATO"
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	
	public boolean canDragElement(TreePath source,TreePath target);
	
	
	/**
	 * Metodo che controlla se un elemento pu� essere "DROPPATO"
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public boolean canDropElement(TreePath source,TreePath target);
	
	
	/**
	 * E' richiamato per gestire la necessit� di eseguire un refresh dei dati
	 *
	 */
	public void refresh(boolean response);
	
	
	/**
	 * E' richiamato quando si inizia la funzione di Drag
	 */
	public void startDrag();
	
	/**
	 * E' richiamato quando si inizia la funzione di Drop
	 */
	public void startDrop();
	
}

