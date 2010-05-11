/*
 * PersistentWorkSpaceModel.java
 *
 * Created on January 3, 2007, 10:28 AM
 *
 */

package net.sf.jttslite.model;

import net.sf.jttslite.persistence.PersistenceManager;
import net.sf.jttslite.persistence.PersistenceNode;
import net.sf.jttslite.persistence.Transaction;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * Fornisce l'implementazione della persistenza.
 *
 * @author Davide Cavestro
 */
public class PersistentWorkSpaceModel extends WorkSpaceModelImpl /*UndoableWorkSpaceModel*/ {
	
	
	private final PersistenceNode _persistenceNode;
	
	/**
	 * Costruttore.
	 *
	 * @param persistenceNode
	 * @param peh
	 * @param workSpace
	 * @param applicationOptions le opzioni di configurazione.
	 */
	public PersistentWorkSpaceModel (final PersistenceNode persistenceNode) {
		super ();
		_persistenceNode = persistenceNode;
	}
	
	public void init () {
		init (_persistenceNode.getAvailableWorkSpaces ());
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
	@Override
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
