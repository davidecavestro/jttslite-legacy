/*
 * PieceOfWorkTemplateModelImpl.java
 *
 * Created on August 2, 2008, 5:03 PM
 *
 */

package com.davidecavestro.timekeeper.model;

import com.davidecavestro.timekeeper.persistence.PersistenceManager;
import com.davidecavestro.timekeeper.persistence.Transaction;
import com.ost.timekeeper.model.ProgressTemplate;
import com.ost.timekeeper.util.Duration;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Lista di modelli di azione. Ne viene gestita la persistenza.
 *
 * Mutuato da javax.swing.DefaultListModel
 * @see javax.swing.DefaultListModel
 *
 * @author Davide Cavestro
 */
public class PieceOfWorkTemplateModelImpl extends AbstractPieceOfWorkTemplateModel {
	
	private Vector<ProgressTemplate> delegate = new Vector<ProgressTemplate> ();
	
	/**
	 * Costruttore vuoto.
	 */
	public PieceOfWorkTemplateModelImpl () {
	}
	
	/**
	 * Costruttore con inizializzazione della lista.
	 * 
	 * @param l la lista dei modelli gi&agrave; presenti.
	 */
	public PieceOfWorkTemplateModelImpl (final List<ProgressTemplate> l) {
		init (l);
	}
	
	/**
	 * Inizializza la lista con i modelli spacificati. Si assume che ssi godano gi&agrave; della persistenza.
	 * 
	 * @param l i modelli.
	 */
	public void init (final List<ProgressTemplate> l) {
		delegate.addAll (l);
	}
	
	/**
	 * Returns the number of components in this list.
	 * <p>
	 * This method is identical to <code>size</code>, which implements the
	 * <code>List</code> interface defined in the 1.2 Collections framework.
	 * This method exists in conjunction with <code>setSize</code> so that
	 * <code>size</code> is identifiable as a JavaBean property.
	 *
	 * @return  the number of components in this list
	 * @see #size()
	 */
	public int getSize () {
		return delegate.size ();
	}
	
	/**
	 * Returns the component at the specified index.
	 * <blockquote>
	 * <b>Note:</b> Although this method is not deprecated, the preferred
	 *    method to use is <code>get(int)</code>, which implements the
	 *    <code>List</code> interface defined in the 1.2 Collections framework.
	 * </blockquote>
	 * @param      index   an index into this list
	 * @return     the component at the specified index
	 * @exception  ArrayIndexOutOfBoundsException  if the <code>index</code>
	 *             is negative or greater than the current size of this
	 *             list
	 * @see #get(int)
	 */
	public ProgressTemplate getElementAt (int index) {
		return delegate.elementAt (index);
	}
	
	/**
	 * Copies the components of this list into the specified array.
	 * The array must be big enough to hold all the objects in this list,
	 * else an <code>IndexOutOfBoundsException</code> is thrown.
	 *
	 * @param   anArray   the array into which the components get copied
	 * @see Vector#copyInto(PieceOfWorkTemplate[])
	 */
	public void copyInto (ProgressTemplate anArray[]) {
		delegate.copyInto (anArray);
	}
	
	/**
	 * Trims the capacity of this list to be the list's current size.
	 *
	 * @see Vector#trimToSize()
	 */
	public void trimToSize () {
		delegate.trimToSize ();
	}
	
	/**
	 * Increases the capacity of this list, if necessary, to ensure
	 * that it can hold at least the number of components specified by
	 * the minimum capacity argument.
	 *
	 * @param   minCapacity   the desired minimum capacity
	 * @see Vector#ensureCapacity(int)
	 */
	public void ensureCapacity (int minCapacity) {
		delegate.ensureCapacity (minCapacity);
	}
	
//	/**
//	 * Sets the size of this list.
//	 *
//	 * @param   newSize   the new size of this list
//	 * @see Vector#setSize(int)
//	 */
//	public void setSize (int newSize) {
//		int oldSize = delegate.size ();
//		delegate.setSize (newSize);
//		if (oldSize > newSize) {
//			fireIntervalRemoved (this, newSize, oldSize-1);
//		} else if (oldSize < newSize) {
//			fireIntervalAdded (this, oldSize, newSize-1);
//		}
//	}
	
	/**
	 * Returns the current capacity of this list.
	 *
	 * @return  the current capacity
	 * @see Vector#capacity()
	 */
	public int capacity () {
		return delegate.capacity ();
	}
	
	/**
	 * Returns the number of components in this list.
	 *
	 * @return  the number of components in this list
	 * @see Vector#size()
	 */
	public int size () {
		return delegate.size ();
	}
	
	/**
	 * Tests whether this list has any components.
	 *
	 * @return  <code>true</code> if and only if this list has
	 *          no components, that is, its size is zero;
	 *          <code>false</code> otherwise
	 * @see Vector#isEmpty()
	 */
	public boolean isEmpty () {
		return delegate.isEmpty ();
	}
	
	/**
	 * Returns an enumeration of the components of this list.
	 *
	 * @return  an enumeration of the components of this list
	 * @see Vector#elements()
	 */
	public Enumeration<ProgressTemplate> elements () {
		return delegate.elements ();
	}
	
	/**
	 *
	 * @see Vector#iterator()
	 */
	public Iterator<ProgressTemplate> iterator () {
		return delegate.iterator ();
	}
	
	/**
	 * Tests whether the specified object is a component in this list.
	 *
	 * @param   elem   an object
	 * @return  <code>true</code> if the specified object
	 *          is the same as a component in this list
	 * @see Vector#contains(PieceOfWorkTemplate)
	 */
	public boolean contains (ProgressTemplate elem) {
		return delegate.contains (elem);
	}
	
	/**
	 * Searches for the first occurrence of <code>elem</code>.
	 *
	 * @param   elem   an object
	 * @return  the index of the first occurrence of the argument in this
	 *          list; returns <code>-1</code> if the object is not found
	 * @see Vector#indexOf(PieceOfWorkTemplate)
	 */
	public int indexOf (ProgressTemplate elem) {
		return delegate.indexOf (elem);
	}
	
	/**
	 * Returns the component at the specified index.
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index
	 * is negative or not less than the size of the list.
	 * <blockquote>
	 * <b>Note:</b> Although this method is not deprecated, the preferred
	 *    method to use is <code>get(int)</code>, which implements the
	 *    <code>List</code> interface defined in the 1.2 Collections framework.
	 * </blockquote>
	 *
	 * @param      index   an index into this list
	 * @return     the component at the specified index
	 * @see #get(int)
	 * @see Vector#elementAt(int)
	 */
	public ProgressTemplate elementAt (int index) {
		return delegate.elementAt (index);
	}
	
	/**
	 * Returns the first component of this list.
	 * Throws a <code>NoSuchElementException</code> if this
	 * vector has no components.
	 * @return     the first component of this list
	 * @see Vector#firstElement()
	 */
	public ProgressTemplate firstElement () {
		return delegate.firstElement ();
	}
	
	/**
	 * Returns the last component of the list.
	 * Throws a <code>NoSuchElementException</code> if this vector
	 * has no components.
	 *
	 * @return  the last component of the list
	 * @see Vector#lastElement()
	 */
	public ProgressTemplate lastElement () {
		return delegate.lastElement ();
	}
	
	/**
	 * Sets the component at the specified <code>index</code> of this
	 * list to be the specified object. The previous component at that
	 * position is discarded.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index
	 * is invalid.
	 * <blockquote>
	 * <b>Note:</b> Although this method is not deprecated, the preferred
	 *    method to use is <code>set(int,PieceOfWorkTemplate)</code>, which implements the
	 *    <code>List</code> interface defined in the 1.2 Collections framework.
	 * </blockquote>
	 *
	 * @param      obj     what the component is to be set to
	 * @param      index   the specified index
	 * @see #set(int,PieceOfWorkTemplate)
	 * @see Vector#setElementAt(PieceOfWorkTemplate,int)
	 */
	public void setElementAt (ProgressTemplate obj, int index) {
		
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			deletePersistent (getPersistenceManager (), delegate.elementAt (index));
			makePersistent (getPersistenceManager (), obj);
			
			delegate.setElementAt (obj, index);
			
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new RuntimeException (e);
		}
		
		fireContentsChanged (this, index, index);
	}
	
	/**
	 * Deletes the component at the specified index.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index
	 * is invalid.
	 * <blockquote>
	 * <b>Note:</b> Although this method is not deprecated, the preferred
	 *    method to use is <code>remove(int)</code>, which implements the
	 *    <code>List</code> interface defined in the 1.2 Collections framework.
	 * </blockquote>
	 *
	 * @param      index   the index of the object to remove
	 * @see #remove(int)
	 * @see Vector#removeElementAt(int)
	 */
	public void removeElementAt (int index) {
		
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			deletePersistent (getPersistenceManager (), delegate.elementAt (index));
			
			delegate.removeElementAt (index);
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new RuntimeException (e);
		}
		
		fireIntervalRemoved (this, index, index);
	}
	
	/**
	 * Inserts the specified object as a component in this list at the
	 * specified <code>index</code>.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index
	 * is invalid.
	 * <blockquote>
	 * <b>Note:</b> Although this method is not deprecated, the preferred
	 *    method to use is <code>add(int,PieceOfWorkTemplate)</code>, which implements the
	 *    <code>List</code> interface defined in the 1.2 Collections framework.
	 * </blockquote>
	 *
	 * @param      obj     the component to insert
	 * @param      index   where to insert the new component
	 * @exception  ArrayIndexOutOfBoundsException  if the index was invalid
	 * @see #add(int,PieceOfWorkTemplate)
	 * @see Vector#insertElementAt(PieceOfWorkTemplate,int)
	 */
	public void insertElementAt (ProgressTemplate obj, int index) {
		
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			delegate.insertElementAt (obj, index);
			
			makePersistent (getPersistenceManager (), obj);
			
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new RuntimeException (e);
		}
		
		fireIntervalAdded (this, index, index);
	}
	
	/**
	 * Adds the specified component to the end of this list.
	 *
	 * @param   obj   the component to be added
	 * @see Vector#addElement(PieceOfWorkTemplate)
	 */
	public void addElement (ProgressTemplate obj) {
		int index = delegate.size ();
		
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			delegate.addElement (obj);
			
			makePersistent (getPersistenceManager (), obj);
			
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new RuntimeException (e);
		}
		
		fireIntervalAdded (this, index, index);
	}
	
	/**
	 * Removes the first (lowest-indexed) occurrence of the argument
	 * from this list.
	 *
	 * @param   obj   the component to be removed
	 * @return  <code>true</code> if the argument was a component of this
	 *          list; <code>false</code> otherwise
	 * @see Vector#removeElement(PieceOfWorkTemplate)
	 */
	public boolean removeElement (ProgressTemplate obj) {
		int index = indexOf (obj);
		boolean rv = false;
		
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			deletePersistent (getPersistenceManager (), obj);
			
			rv = delegate.removeElement (obj);
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new RuntimeException (e);
		}
		
		if (index >= 0) {
			fireIntervalRemoved (this, index, index);
		}
		return rv;
	}
	
	
	/**
	 * Removes all components from this list and sets its size to zero.
	 * <blockquote>
	 * <b>Note:</b> Although this method is not deprecated, the preferred
	 *    method to use is <code>clear</code>, which implements the
	 *    <code>List</code> interface defined in the 1.2 Collections framework.
	 * </blockquote>
	 *
	 * @see #clear()
	 * @see Vector#removeAllElements()
	 */
	public void removeAllElements () {
		int index1 = delegate.size ()-1;
		
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			
			deletePersistent (getPersistenceManager (), delegate);
			
			delegate.removeAllElements ();
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new RuntimeException (e);
		}
		
		if (index1 >= 0) {
			fireIntervalRemoved (this, 0, index1);
		}
	}
	
	
	/**
	 * Returns a string that displays and identifies this
	 * object's properties.
	 *
	 * @return a String representation of this object
	 */
	@Override
	public String toString () {
		return delegate.toString ();
	}
	
	
	/* The remaining methods are included for compatibility with the
	 * Java 2 platform Vector class.
	 */
	
	/**
	 * Returns an array containing all of the elements in this list in the
	 * correct order.
	 *
	 * @return an array containing the elements of the list
	 * @see Vector#toArray()
	 */
	public ProgressTemplate[] toArray () {
		ProgressTemplate[] rv = new ProgressTemplate[delegate.size ()];
		delegate.copyInto (rv);
		return rv;
	}
	
	/**
	 * Returns the element at the specified position in this list.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code>
	 * if the index is out of range
	 * (<code>index &lt; 0 || index &gt;= size()</code>).
	 *
	 * @param index index of element to return
	 */
	public ProgressTemplate get (int index) {
		return delegate.elementAt (index);
	}
	
	/**
	 * Replaces the element at the specified position in this list with the
	 * specified element.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code>
	 * if the index is out of range
	 * (<code>index &lt; 0 || index &gt;= size()</code>).
	 *
	 * @param index index of element to replace
	 * @param element element to be stored at the specified position
	 * @return the element previously at the specified position
	 */
	public ProgressTemplate set (int index, ProgressTemplate element) {
		ProgressTemplate rv = null;
		
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			rv = delegate.elementAt (index);
			
			deletePersistent (getPersistenceManager (), rv);
			makePersistent (getPersistenceManager (), element);
			
			delegate.setElementAt (element, index);
			
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new RuntimeException (e);
		}
		
		fireContentsChanged (this, index, index);
		return rv;
	}
	
	/**
	 * Inserts the specified element at the specified position in this list.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the
	 * index is out of range
	 * (<code>index &lt; 0 || index &gt; size()</code>).
	 *
	 * @param index index at which the specified element is to be inserted
	 * @param element element to be inserted
	 */
	public void add (int index, ProgressTemplate element) {
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			delegate.insertElementAt (element, index);
			makePersistent (getPersistenceManager (), element);
			
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new RuntimeException (e);
		}
		fireIntervalAdded (this, index, index);
	}
	
	/**
	 * Removes the element at the specified position in this list.
	 * Returns the element that was removed from the list.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code>
	 * if the index is out of range
	 * (<code>index &lt; 0 || index &gt;= size()</code>).
	 *
	 * @param index the index of the element to removed
	 */
	public ProgressTemplate remove (int index) {
		ProgressTemplate rv = delegate.elementAt (index);
		
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			deletePersistent (getPersistenceManager (), rv);
			
			delegate.removeElementAt (index);
			
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new RuntimeException (e);
		}
		
		fireIntervalRemoved (this, index, index);
		return rv;
	}
	
	/**
	 * Removes all of the elements from this list.  The list will
	 * be empty after this call returns (unless it throws an exception).
	 */
	public void clear () {
		int index1 = delegate.size ()-1;
		
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			deletePersistent (getPersistenceManager (), delegate);
			
			delegate.removeAllElements ();
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new RuntimeException (e);
		}
		
		if (index1 >= 0) {
			fireIntervalRemoved (this, 0, index1);
		}
	}
	
//	/**
//	 * Deletes the components at the specified range of indexes.
//	 * The removal is inclusive, so specifying a range of (1,5)
//	 * removes the component at index 1 and the component at index 5,
//	 * as well as all components in between.
//	 * <p>
//	 * Throws an <code>ArrayIndexOutOfBoundsException</code>
//	 * if the index was invalid.
//	 * Throws an <code>IllegalArgumentException</code> if
//	 * <code>fromIndex &gt; toIndex</code>.
//	 *
//	 * @param      fromIndex the index of the lower end of the range
//	 * @param      toIndex   the index of the upper end of the range
//	 * @see	   #remove(int)
//	 */
//	public void removeRange (int fromIndex, int toIndex) {
//		if (fromIndex > toIndex) {
//			throw new IllegalArgumentException ("fromIndex must be <= toIndex");
//		}
//		for(int i = toIndex; i >= fromIndex; i--) {
//			delegate.removeElementAt (i);
//		}
//		fireIntervalRemoved (this, fromIndex, toIndex);
//	}
	
	
	
	
	public void updateElement (final ProgressTemplate t, final String name, final String notes, final Duration d) {
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			t.setName (name);
			t.setNotes (notes);
			t.setDuration (d);
			
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new RuntimeException (e);
		}
		
		nodeChanged (t);
	}
	
	public void nodeChanged (final ProgressTemplate t) {
		final int index = indexOf (t);
		fireContentsChanged (this, index, index);
	}
	
	
	
	
	
	
	
	
	/*-------------------------------------------------------------
	 * INIZIO sezione contenente i metodi di supporto alla persistenza
	 --------------------------------------------------------------*/
	
	
	/**
	 * Riferimento alla transazione fake, creata lazy.
	 */
	private Transaction _tx;
	/**
	 * Ritorna una transazione difacciata, con una implementazione vuota.
	 * <P>
	 * Scavalcare questo metodo per fornire una adeguata implementazione di Transactionse necessario (ad esempio una transazione JDO).
	 */
	protected Transaction getTransaction () {
		if (null == _tx) {
			_tx = new Transaction () {
				public void begin () {
				}
				public void commit () {
				}
				public void rollback () {
				}
			};
		}
		return _tx;
	}
	
	/**
	 * RIferimentoalla transazione fake, creata lazy.
	 */
	private PersistenceManager _pm;
	/**
	 * Ritorna un persistence manager difacciata, con una implementazione vuota.
	 * <P>
	 * Scavalcare questo metodo per fornire una adeguata implementazione di PersistenceManager se necessario (ad esempio un PersistenceManager JDO).
	 */
	protected PersistenceManager getPersistenceManager () {
		if (null == _pm) {
			_pm = new PersistenceManager () {
				public void deletePersistent (Object o) {
				}
				public void makePersistent (Object o) {
				}
				public void deletePersistentAll (Collection c) {
				}
				public void makePersistentAll (Collection c) {
				}
			};
		}
		return _pm;
	}
	
	

	/**
	 * Rimuove il modello.
	 *
	 * @param toDelete il modello da rimuovere.
	 */
	private void deletePersistent (final PersistenceManager pm, final ProgressTemplate toDelete){
		pm.deletePersistent (toDelete);
	}
	
	/**
	 * Rimuove i modelli.
	 *
	 * @param toDelete i modelli da rimuovere.
	 */
	private void deletePersistent (final PersistenceManager pm, final List<ProgressTemplate> toDelete){
		for (final ProgressTemplate ws : toDelete) {
			deletePersistent (pm, ws);
		}
	}
	
	
	


	
	/**
	 * Rende persistente il modello.
	 *
	 * @param toPersist il modello da rendere persistente.
	 */
	private void makePersistent (final PersistenceManager pm, final ProgressTemplate toPersist){
		pm.makePersistent (toPersist);
	}
	
	/**
	 * Rende persistenti i modelli.
	 *
	 * @param toPersist i modelli da rendere persistenti.
	 */
	private void makePersistent (final PersistenceManager pm, final List<ProgressTemplate> toPersist){
		for (final ProgressTemplate ws : toPersist) {
			makePersistent (pm, ws);
		}
	}
		
	/*-------------------------------------------------------------
	 * FINE sezione contenente i metodi di supporto alla persistenza
	 --------------------------------------------------------------*/
	
	
}
