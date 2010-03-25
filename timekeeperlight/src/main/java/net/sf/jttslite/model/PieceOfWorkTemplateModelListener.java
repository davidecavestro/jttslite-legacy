/*
 * PieceOfWorkTemplateModelListener.java
 *
 * Created on August 2, 2008, 5:02 PM
 *
 */

package net.sf.jttslite.model;

import net.sf.jttslite.model.event.PieceOfWorkTemplateModelEvent;
import java.util.EventListener;

/**
 * Mutuato da ListDataListener.
 * @see javax.swing.ListDataListener
 *
 * @author Davide Cavestro
 */
public interface PieceOfWorkTemplateModelListener extends EventListener {
	/**
	 * Sent after the indices in the index0,index1
	 * interval have been inserted in the data model.
	 * The new interval includes both index0 and index1.
	 *
	 * @param e  a <code>PieceOfWorkTemplateModelEvent</code> encapsulating the
	 *    event information
	 */
	void intervalAdded (PieceOfWorkTemplateModelEvent e);
	
	
	/**
	 * Sent after the indices in the index0,index1 interval
	 * have been removed from the data model.  The interval
	 * includes both index0 and index1.
	 *
	 * @param e  a <code>PieceOfWorkTemplateModelEvent</code> encapsulating the
	 *    event information
	 */
	void intervalRemoved (PieceOfWorkTemplateModelEvent e);
	
	
	/**
	 * Sent when the contents of the list has changed in a way
	 * that's too complex to characterize with the previous
	 * methods. For example, this is sent when an item has been
	 * replaced. Index0 and index1 bracket the change.
	 *
	 * @param e  a <code>PieceOfWorkTemplateModelEvent</code> encapsulating the
	 *    event information
	 */
	void contentsChanged (PieceOfWorkTemplateModelEvent e);
}
