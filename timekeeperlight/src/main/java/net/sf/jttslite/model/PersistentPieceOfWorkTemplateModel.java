/*
 * PersistentPieceOfWorkTemplateModel.java
 *
 * Created on August 2, 2008, 5:00 PM
 *
 */

package net.sf.jttslite.model;

import net.sf.jttslite.conf.ApplicationOptions;
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
public class PersistentPieceOfWorkTemplateModel extends UndoablePieceOfWorkTemplateModel {
	
	
	private final PersistenceNode _persistenceNode;
	private final Logger _logger;
	
	
	/**
	 * Costruttore.
	 *
	 * @param persistenceNode the persistence delegate
	 * @param applicationOptions le opzioni di configurazione.
	 * @logger the logger
	 */
	public PersistentPieceOfWorkTemplateModel (final PersistenceNode persistenceNode, final ApplicationOptions applicationOptions, final Logger logger) {
		super ();
		_persistenceNode = persistenceNode;
		_logger = logger;
	}
	
	/**
	 * Initialize this model.
	 */
	public void init () {
		init (_persistenceNode.getAvailableTemplates ());
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
