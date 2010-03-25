/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.jttslite.gui;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

/**
 * An <tt>AbstractTreeTableModel</tt> with some additional utility metods
 *
 * @author davide
 */
abstract class BasicTreeTableModel extends AbstractTreeTableModel {

	
    /**
     * Constructs an <code>AbstractTreeTableModel</code> with the specified node
     * as the root node.
     *
     * @param root root node
     */
    public BasicTreeTableModel (Object root) {
        super (root);
    }
	
	/*
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 * @see EventListenerList
	 */
	protected void fireTreeNodesChanged(Object source, Object[] path,
			int[] childIndices, Object[] children) {
		// Guaranteed to return a non-null array
		final TreeModelListener[] listeners = getTreeModelListeners();
		TreeModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 1; i >= 0; i--) {
			// Lazily create the event:
			if (e == null) {
				e = new TreeModelEvent(source, path,
						childIndices, children);
			}
			((TreeModelListener) listeners[i]).treeNodesChanged(e);
		}
	}

	/*
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 * @see EventListenerList
	 */
	protected void fireTreeNodesInserted(Object source, Object[] path,
			int[] childIndices, Object[] children) {
		// Guaranteed to return a non-null array
		final TreeModelListener[] listeners = getTreeModelListeners();
		TreeModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 1; i >= 0; i--) {
			// Lazily create the event:
			if (e == null) {
				e = new TreeModelEvent(source, path,
						childIndices, children);
			}
			listeners[i].treeNodesInserted(e);
		}
	}

	/*
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 * @see EventListenerList
	 */
	protected void fireTreeNodesRemoved(Object source, Object[] path,
			int[] childIndices, Object[] children) {
		// Guaranteed to return a non-null array
		final TreeModelListener[] listeners = getTreeModelListeners();
		TreeModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 1; i >= 0; i--) {
			// Lazily create the event:
			if (e == null) {
				e = new TreeModelEvent(source, path,
						childIndices, children);
			}
			listeners[i].treeNodesRemoved(e);
		}
	}

	/*
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 * @see EventListenerList
	 */
	protected void fireTreeStructureChanged(Object source, Object[] path,
			int[] childIndices,
			Object[] children) {
		// Guaranteed to return a non-null array
		final TreeModelListener[] listeners = getTreeModelListeners();
		TreeModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 1; i >= 0; i--) {
			// Lazily create the event:
			if (e == null) {
				e = new TreeModelEvent(source, path,
						childIndices, children);
			}
			listeners[i].treeStructureChanged(e);
		}
	}
}

