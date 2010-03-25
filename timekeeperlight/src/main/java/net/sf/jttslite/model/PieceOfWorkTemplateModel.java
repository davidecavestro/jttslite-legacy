/*
 * PieceOfWorkTemplateModel.java
 *
 * Created on August 2, 2008, 5:00 PM
 *
 */

package net.sf.jttslite.model;

import net.sf.jttslite.core.model.impl.ProgressTemplate;

/**
 * Model for action templates.
 *
 * @author Davide Cavestro
 */
public interface PieceOfWorkTemplateModel {
	/**
	 * Returns the length of the list.
	 * @return the length of the list
	 */
	int getSize ();
	
	/**
	 * Returns the value at the specified index.
	 * @param index the requested index
	 * @return the value at <code>index</code>
	 */
	ProgressTemplate getElementAt (int index);
	
	/**
	 * Adds a listener to the list that's notified each time a change
	 * to the data model occurs.
	 * @param l the <code>PieceOfWorkTemplateModelListener</code> to be added
	 */
	void addPieceOfWorkTemplateModelListener (PieceOfWorkTemplateModelListener l);
	
	/**
	 * Removes a listener from the list that's notified each time a
	 * change to the data model occurs.
	 * @param l the <code>PieceOfWorkTemplateModelListener</code> to be removed
	 */
	void removePieceOfWorkTemplateModelListener (PieceOfWorkTemplateModelListener l);
	
}
