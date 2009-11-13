/*
 * AbstractPieceOfWorkTemplateModel.java
 *
 * Created on August 2, 2008, 5:10 PM
 *
 */

package com.davidecavestro.timekeeper.model;

import com.davidecavestro.timekeeper.model.event.PieceOfWorkTemplateModelEvent;
import java.util.EventListener;
import javax.swing.event.EventListenerList;

/**
 * Implementazione parziale del modello di lista dei modelli di azione.
 * Mutuato da javax.swing.AbstractListModel
 *
 * @see javax.swing.AbstractListModel
 *
 * @author Davide Cavestro
 */
public abstract class AbstractPieceOfWorkTemplateModel implements PieceOfWorkTemplateModel {
	
	protected EventListenerList listenerList = new EventListenerList ();
	
	
	/**
	 * Adds a listener to the list that's notified each time a change
	 * to the data model occurs.
	 *
	 * @param l the <code>PieceOfWorkTemplateModelListener</code> to be added
	 */
	public void addPieceOfWorkTemplateModelListener (final PieceOfWorkTemplateModelListener l) {
		listenerList.add (PieceOfWorkTemplateModelListener.class, l);
	}
	
	
	/**
	 * Removes a listener from the list that's notified each time a
	 * change to the data model occurs.
	 *
	 * @param l the <code>PieceOfWorkTemplateModelListener</code> to be removed
	 */
	public void removePieceOfWorkTemplateModelListener (final PieceOfWorkTemplateModelListener l) {
		listenerList.remove (PieceOfWorkTemplateModelListener.class, l);
	}
	
	
	/**
	 * Returns an array of all the list data listeners
	 * registered on this <code>AbstractListModel</code>.
	 *
	 * @return all of this model's <code>PieceOfWorkTemplateModelListener</code>s,
	 *         or an empty array if no list data listeners
	 *         are currently registered
	 *
	 * @see #addPieceOfWorkTemplateModelListener
	 * @see #removePieceOfWorkTemplateModelListener
	 *
	 * @since 1.4
	 */
	public PieceOfWorkTemplateModelListener[] getPieceOfWorkTemplateModelListeners () {
		return (PieceOfWorkTemplateModelListener[])listenerList.getListeners (
			PieceOfWorkTemplateModelListener.class);
	}
	
	
	/**
	 * <code>AbstractListModel</code> subclasses must call this method
	 * <b>after</b>
	 * one or more elements of the list change.  The changed elements
	 * are specified by the closed interval index0, index1 -- the endpoints
	 * are included.  Note that
	 * index0 need not be less than or equal to index1.
	 *
	 * @param source the <code>ListModel</code> that changed, typically "this"
	 * @param index0 one end of the new interval
	 * @param index1 the other end of the new interval
	 * @see EventListenerList
	 * @see DefaultListModel
	 */
	protected void fireContentsChanged (final Object source, final int index0, final int index1) {
		Object[] listeners = listenerList.getListenerList ();
		PieceOfWorkTemplateModelEvent e = null;
		
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == PieceOfWorkTemplateModelListener.class) {
				if (e == null) {
					e = new PieceOfWorkTemplateModelEvent (source, PieceOfWorkTemplateModelEvent.CONTENTS_CHANGED, index0, index1);
				}
				((PieceOfWorkTemplateModelListener)listeners[i+1]).contentsChanged (e);
			}
		}
	}
	
	
	/**
	 * <code>AbstractListModel</code> subclasses must call this method
	 * <b>after</b>
	 * one or more elements are added to the model.  The new elements
	 * are specified by a closed interval index0, index1 -- the enpoints
	 * are included.  Note that
	 * index0 need not be less than or equal to index1.
	 *
	 * @param source the <code>ListModel</code> that changed, typically "this"
	 * @param index0 one end of the new interval
	 * @param index1 the other end of the new interval
	 * @see EventListenerList
	 * @see DefaultListModel
	 */
	protected void fireIntervalAdded (final Object source, final int index0, final int index1) {
		Object[] listeners = listenerList.getListenerList ();
		PieceOfWorkTemplateModelEvent e = null;
		
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == PieceOfWorkTemplateModelListener.class) {
				if (e == null) {
					e = new PieceOfWorkTemplateModelEvent (source, PieceOfWorkTemplateModelEvent.INTERVAL_ADDED, index0, index1);
				}
				((PieceOfWorkTemplateModelListener)listeners[i+1]).intervalAdded (e);
			}
		}
	}
	
	
	/**
	 * <code>AbstractListModel</code> subclasses must call this method
	 * <b>after</b> one or more elements are removed from the model.
	 * <code>index0</code> and <code>index1</code> are the end points
	 * of the interval that's been removed.  Note that <code>index0</code>
	 * need not be less than or equal to <code>index1</code>.
	 *
	 * @param source the <code>ListModel</code> that changed, typically "this"
	 * @param index0 one end of the removed interval,
	 *               including <code>index0</code>
	 * @param index1 the other end of the removed interval,
	 *               including <code>index1</code>
	 * @see EventListenerList
	 * @see DefaultListModel
	 */
	protected void fireIntervalRemoved (final Object source, final int index0, final int index1) {
		Object[] listeners = listenerList.getListenerList ();
		PieceOfWorkTemplateModelEvent e = null;
		
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == PieceOfWorkTemplateModelListener.class) {
				if (e == null) {
					e = new PieceOfWorkTemplateModelEvent (source, PieceOfWorkTemplateModelEvent.INTERVAL_REMOVED, index0, index1);
				}
				((PieceOfWorkTemplateModelListener)listeners[i+1]).intervalRemoved (e);
			}
		}
	}
	
	/**
	 * Returns an array of all the objects currently registered as
	 * <code><em>Foo</em>Listener</code>s
	 * upon this model.
	 * <code><em>Foo</em>Listener</code>s
	 * are registered using the <code>add<em>Foo</em>Listener</code> method.
	 * <p>
	 * You can specify the <code>listenerType</code> argument
	 * with a class literal, such as <code><em>Foo</em>Listener.class</code>.
	 * For example, you can query a list model
	 * <code>m</code>
	 * for its list data listeners
	 * with the following code:
	 *
	 * <pre>PieceOfWorkTemplateModelListener[] ldls = (PieceOfWorkTemplateModelListener[])(m.getListeners(PieceOfWorkTemplateModelListener.class));</pre>
	 *
	 * If no such listeners exist,
	 * this method returns an empty array.
	 *
	 * @param listenerType  the type of listeners requested;
	 *          this parameter should specify an interface
	 *          that descends from <code>java.util.EventListener</code>
	 * @return an array of all objects registered as
	 *          <code><em>Foo</em>Listener</code>s
	 *          on this model,
	 *          or an empty array if no such
	 *          listeners have been added
	 * @exception ClassCastException if <code>listenerType</code> doesn't
	 *          specify a class or interface that implements
	 *          <code>java.util.EventListener</code>
	 *
	 * @see #getPieceOfWorkTemplateModelListeners
	 *
	 * @since 1.3
	 */
	public <T extends EventListener> T[] getListeners (final Class<T> listenerType) {
		return listenerList.getListeners (listenerType);
	}
}
