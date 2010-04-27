/*
 * PersistentTaskTreeModel.java
 *
 * Created on December 18, 2006, 11:55 PM
 *
 */

package net.sf.jttslite.model;

import net.sf.jttslite.core.model.WorkSpace;
import net.sf.jttslite.persistence.PersistenceManager;
import net.sf.jttslite.persistence.PersistenceNode;
import net.sf.jttslite.persistence.Transaction;
import java.util.Collection;

/**
 * Estensione al modello con supporto alla persistenza.
 *
 * @author Davide Cavestro
 */
public class PersistentTaskTreeModel extends UndoableTaskTreeModel {
	
	private final PersistenceNode _persistenceNode;
	
	/**
	 * Costruttore.
	 * 
	 * @param persistenceNode
	 * @param workSpace 
	 */
	public PersistentTaskTreeModel (final PersistenceNode persistenceNode, final WorkSpace workSpace) {
		super (workSpace);
		_persistenceNode = persistenceNode;
	}
	
	/**
	 * RIferimentoalla transazione fake, creata lazy.
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
					_persistenceNode.getPersistenceManager ().currentTransaction ().begin ();
				}
				public void commit () {
					_persistenceNode.getPersistenceManager ().currentTransaction ().commit ();
				}
				public void rollback () {
					_persistenceNode.getPersistenceManager ().currentTransaction ().rollback ();
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
	 * Ritorna il PersistenceManager JDO).
	 */
	@Override
	protected PersistenceManager getPersistenceManager () {
		if (null == _pm) {
			_pm = new PersistenceManager () {
				public void deletePersistent (Object o) {
					_persistenceNode.getPersistenceManager ().deletePersistent (o);
				}
				public void makePersistent (Object o) {
					_persistenceNode.getPersistenceManager ().makePersistent (o);
				}
				public void deletePersistentAll (Collection c) {
					_persistenceNode.getPersistenceManager ().deletePersistentAll (c);
				}
				public void makePersistentAll (Collection c) {
					_persistenceNode.getPersistenceManager ().makePersistentAll (c);
				}
			};
		}
		return _pm;
	}
		
}
