/*
 * WorkSpaceModelImpl.java
 *
 * Created on January 3, 2007, 9:46 AM
 *
 */

package net.sf.jttslite.model;

import net.sf.jttslite.core.model.WorkSpace;
import net.sf.jttslite.core.model.Task;
import net.sf.jttslite.persistence.PersistenceManager;
import net.sf.jttslite.persistence.Transaction;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Lista di workspace. Ne viene gestita la persistenza.
 *
 * Mutuato da javax.swing.DefaultListModel
 * @see javax.swing.DefaultListModel
 *
 * @author Davide Cavestro
 */
public class WorkSpaceModelImpl extends AbstractWorkSpaceModel {
	
	private Vector<WorkSpace> delegate = new Vector<WorkSpace> ();
	
	/**
	 * Costruttore vuoto.
	 */
	public WorkSpaceModelImpl () {
	}
	
	/**
	 * Costruttore con inizializzazione della lista.
	 * 
	 * @param l la lista degli workspace gi&agrave; presenti.
	 */
	public WorkSpaceModelImpl (final List<WorkSpace> l) {
		init (l);
	}
	
	/**
	 * Inizializza la lista con gli workspace spacificati. Si assume che ssi godano gi&agrave; della persistenza.
	 * 
	 * @param l igli workspace.
	 */
	public void init (final List<WorkSpace> l) {
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
	public WorkSpace getElementAt (int index) {
		return delegate.elementAt (index);
	}
	
	/**
	 * Copies the components of this list into the specified array.
	 * The array must be big enough to hold all the objects in this list,
	 * else an <code>IndexOutOfBoundsException</code> is thrown.
	 *
	 * @param   anArray   the array into which the components get copied
	 * @see Vector#copyInto(WorkSpace[])
	 */
	public void copyInto (WorkSpace anArray[]) {
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
	public Enumeration<WorkSpace> elements () {
		return delegate.elements ();
	}
	
	/**
	 *
	 * @see Vector#iterator()
	 */
	public Iterator<WorkSpace> iterator () {
		return delegate.iterator ();
	}
	
	/**
	 * Tests whether the specified object is a component in this list.
	 *
	 * @param   elem   an object
	 * @return  <code>true</code> if the specified object
	 *          is the same as a component in this list
	 * @see Vector#contains(WorkSpace)
	 */
	public boolean contains (WorkSpace elem) {
		return delegate.contains (elem);
	}
	
	/**
	 * Searches for the first occurrence of <code>elem</code>.
	 *
	 * @param   elem   an object
	 * @return  the index of the first occurrence of the argument in this
	 *          list; returns <code>-1</code> if the object is not found
	 * @see Vector#indexOf(WorkSpace)
	 */
	public int indexOf (WorkSpace elem) {
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
	public WorkSpace elementAt (int index) {
		return delegate.elementAt (index);
	}
	
	/**
	 * Returns the first component of this list.
	 * Throws a <code>NoSuchElementException</code> if this
	 * vector has no components.
	 * @return     the first component of this list
	 * @see Vector#firstElement()
	 */
	public WorkSpace firstElement () {
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
	public WorkSpace lastElement () {
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
	 *    method to use is <code>set(int,WorkSpace)</code>, which implements the
	 *    <code>List</code> interface defined in the 1.2 Collections framework.
	 * </blockquote>
	 *
	 * @param      obj     what the component is to be set to
	 * @param      index   the specified index
	 * @see #set(int,WorkSpace)
	 * @see Vector#setElementAt(WorkSpace,int)
	 */
	public void setElementAt (WorkSpace obj, int index) {
		
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
     *
     * @throws CannotRemoveWorkSpaceException if a workspace cannot be removed.
	 */
	public void removeElementAt (int index) throws CannotRemoveWorkSpaceException {
		
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
	 *    method to use is <code>add(int,WorkSpace)</code>, which implements the
	 *    <code>List</code> interface defined in the 1.2 Collections framework.
	 * </blockquote>
	 *
	 * @param      obj     the component to insert
	 * @param      index   where to insert the new component
	 * @exception  ArrayIndexOutOfBoundsException  if the index was invalid
	 * @see #add(int,WorkSpace)
	 * @see Vector#insertElementAt(WorkSpace,int)
	 */
	public void insertElementAt (WorkSpace obj, int index) {
		
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
	 * @see Vector#addElement(WorkSpace)
	 * @throws DuplicatedWorkSpaceException 
	 */
	public void addElement (final WorkSpace obj) throws DuplicatedWorkSpaceException {
		for (final WorkSpace ws : delegate) {
			if (obj.getName ().equals (ws.getName ())) {
				throw new DuplicatedWorkSpaceException ();
			}
		}		
		
		final int index = delegate.size ();
		
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
	 * @see Vector#removeElement(WorkSpace)
     *
     * @throws CannotRemoveWorkSpaceException if a workspace cannot be removed.
	 */
	public boolean removeElement (WorkSpace obj) throws CannotRemoveWorkSpaceException {
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
     *
     * @throws CannotRemoveWorkSpaceException if a workspace cannot be removed.
	 */
	public void removeAllElements () throws CannotRemoveWorkSpaceException {
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
	public WorkSpace[] toArray () {
		WorkSpace[] rv = new WorkSpace[delegate.size ()];
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
	 * @return the element at the specified position in this list
	 */
	public WorkSpace get (int index) {
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
     *
     * @throws CannotRemoveWorkSpaceException if a workspace cannot be removed.
	 */
	public WorkSpace set (int index, WorkSpace element) throws CannotRemoveWorkSpaceException {
		WorkSpace rv = null;
		
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
	public void add (int index, WorkSpace element) {
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
     *
	 * @return the element that was removed from the list
	 * @throws CannotRemoveWorkSpaceException if a workspace cannot be removed.
	 */
	public WorkSpace remove (int index) throws CannotRemoveWorkSpaceException {
		WorkSpace rv = delegate.elementAt (index);
		
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
	
	
	public void updateElement (final WorkSpace ws, final String name, final String descr, final String notes) {
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			ws.setName (name);
			ws.setDescription (descr);
			ws.setNotes (notes);
			
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new RuntimeException (e);
		}
		
		nodeChanged (ws);
	}
	
	public void nodeChanged (final WorkSpace ws) {
		final int index = indexOf (ws);
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
	 * Ritorna una transazione di facciata, con una implementazione vuota.
	 * <P>
	 * Scavalcare questo metodo per fornire una adeguata implementazione di Transactionse necessario (ad esempio una transazione JDO).
	 *
	 * @return una transazione di facciata, con una implementazione vuota.
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
	 * Riferimento alla transazione fake, creata lazy.
	 */
	private PersistenceManager _pm;
	/**
	 * Ritorna un persistence manager di facciata, con una implementazione vuota.
	 * <P>
	 * Scavalcare questo metodo per fornire una adeguata implementazione di PersistenceManager se necessario (ad esempio un PersistenceManager JDO).
	 *
	 * @return un persistence manager di facciata, con una implementazione vuota
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
	 * Rimuove il sottoalbero, avanzamenti compresi.
	 *
	 * @param toDelete la radice del sottoalbero da rimuovere.
	 */
	private void deleteSubtreePersistent (final PersistenceManager pm, final Task toDelete){
	   for(Task task : toDelete.getChildren ()){
		   deleteSubtreePersistent (pm, task);
		}
	   pm.deletePersistentAll (toDelete.getPiecesOfWork ());
	   pm.deletePersistent (toDelete);
	}
	
	/**
	 * Rimuove il workspace.
	 *
	 * @param toDelete il workspace da rimuovere.
	 */
	private void deletePersistent (final PersistenceManager pm, final WorkSpace toDelete){
		deleteSubtreePersistent (pm, toDelete.getRoot ());
		pm.deletePersistent (toDelete);
	}
	
	/**
	 * Rimuove i workspace.
	 *
	 * @param toDelete i workspace da rimuovere.
	 */
	private void deletePersistent (final PersistenceManager pm, final List<WorkSpace> toDelete){
		for (final WorkSpace ws : toDelete) {
			deletePersistent (pm, ws);
		}
	}
	
	/**
	 * Rende persistente il sottoalbero, avanzamenti compresi.
	 *
	 * @param toPersist la radice del sottoalbero da rendere persistente.
	 */
	private void makeSubtreePersistent (final PersistenceManager pm, final Task toPersist){
		for (Task task : toPersist.getChildren ()){
			makeSubtreePersistent (pm, task);
		}
		pm.makePersistentAll (toPersist.getPiecesOfWork ());
		pm.makePersistent (toPersist);
	}
	
	/**
	 * Rende persistente il workspace.
	 *
	 * @param toPersist il workspace da rendere persistente.
	 */
	private void makePersistent (final PersistenceManager pm, final WorkSpace toPersist){
		makeSubtreePersistent (pm, toPersist.getRoot ());
		pm.makePersistent (toPersist);
	}
		
	/*-------------------------------------------------------------
	 * FINE sezione contenente i metodi di supporto alla persistenza
	 --------------------------------------------------------------*/
	
	
}
