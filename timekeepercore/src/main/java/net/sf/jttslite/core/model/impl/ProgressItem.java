/*
 * ProgressItem.java
 *
 * Created on 4 aprile 2004, 11.42
 */

package net.sf.jttslite.core.model.impl;

import net.sf.jttslite.core.model.PieceOfWork;
import net.sf.jttslite.core.model.Task;
import net.sf.jttslite.core.model.TaskBackup;
import java.util.*;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceAware;
import javax.jdo.annotations.PersistenceCapable;
import net.sf.jttslite.core.model.impl.Progress.PieceOfWorkBackupImpl;

/**
 * Generico nodo della gerarchia di avanzamenti (nodo di avanzamento).
 * Un nodo pu&ograve; avere figli, nonch� avanzamenti associati.
 * Un nodo pu&ograve; avere un padre, se non lo ha esso funge da radice della gerarchia.
 * Un nodo &egrave; associato ad un progetto (lo stesso dei figli e del padre).
 *
 *<P>
 * Pur implementando l'interfaccia <TT>Task</TT>, questa classe DEVE ricevere oggetti di tipo effettivo <TT>ProgressItem</TT> per funzionare correttamente.
 *<BR>
 *Questo comportamento anomalo &egrave; dovuto all'utilizzo di codice JDO legacy.
 *
 * @author  davide
 */
@PersistenceCapable(table="jtts_task", detachable="true")
public class ProgressItem extends Observable implements Task {
	
	/**
	 * Il codice di questo nodo.
	 */
	protected String code;
	
	/**
	 * Il nome di questo nodo.
	 */
	protected String name;
	
	/**
	 * La descrizione di qeusto nodo.
	 */
	protected String description;
	
	/**
	 * Le annotazioni relative a questo nodo.
	 */
	protected String notes;
	
	/**
	 * Il padre.
	 */
	protected ProgressItem parent;
	
	/**
	 * I figli di questo nodo.
	 */
	@Element(types=net.sf.jttslite.core.model.impl.ProgressItem.class, mappedBy="parent", dependent="true")
	protected final List<ProgressItem> children = new ArrayList<ProgressItem> ();
	
	/**
	 * Gli avanzamenti effettuati su questo nodo.
	 */
	@Element(types=net.sf.jttslite.core.model.impl.Progress.class, mappedBy="progressItem", dependent="true")
	protected final List<Progress> progresses = new ArrayList<Progress> ();
	
	/**
	 * Il progetto di appartenenza.
	 * @warning non utilizzato, per retrocompatibilit&agrave; con JTTS v1 (BUG)
	 */
	
	//Project project;


	/**
	 * I listener registrati per questo nodo.
	 */
	@NotPersistent
	private final List<ProgressListener> progressListeners = new ArrayList<ProgressListener> ();
	
	/**
	 * Costruttore vuoto.
	 */
	public ProgressItem () {
	}
	
	/**
	 * Crea una nuova istanza inizializzandone solo il nome.
	 *
	 * @param name il nome.
	 */
	public ProgressItem (final String name) {
		this.name=name;
	}
	
	/**
	 * Crea una nuova istanza inizianizzandola.
	 *
	 *
	 * @param code
	 * @param name
	 * @param description
	 * @param notes
	 */
	public ProgressItem (final String code, final String name, final String description, final String notes) {
		this.code=code;
		this.description = description;
		this.name = name;
		this.notes = notes;
	}
	
	/**
	 * Costruttore copia.
	 *
	 * @param source il nodo sorgente.
	 */
	public ProgressItem (final ProgressItem source) {
		code=source.code;
		description = source.description;
		name = source.name;
		notes = source.notes;
	}
	
	/**
	 * Ritorna il codice di questo nodo.
	 *
	 * @return il codice.
	 */
	@Override
	public String getCode (){return code;}
	
	/**
	 * Ritorna il nome di questo nodo.
	 *
	 * @return il nome.
	 */
	@Override
	public String getName (){return name;}
	
	/**
	 * Ritorna il padre di questo nodo.
	 *
	 * @return il padre
	 */
	@Override
	public ProgressItem getParent () {
		return parent;
	}
	
	/**
	 * Inserisce un nuovo elemento figlio alla posizione desiderata.
	 * L'inserimento del figlio viene notificata ai listener registrati su
	 * questo nodo e sul nuovo figlio.
	 *
	 * @param child il nuovo figlio.
	 * @param pos la posizione del figlio. Usare unvalore negativoper accodare.
	 */
	@Override
	public int insert (final Task child, final int pos){
		int retValue;
		final ProgressItem realChild = (ProgressItem)child;
		if (pos<0) {
			children.add (realChild);
			retValue = children.indexOf (child);
		} else {
			children.add (pos, realChild);
			retValue = pos;
		}
		realChild.parent=this;
		
		setChanged ();
		realChild.setChanged ();
		notifyObservers ();
		realChild.notifyObservers ();
		
		return retValue;
	}
	
	/**
	 * Inserisce un nuovo elemento figlio in coda agli altri.
	 * L'inserimento del figlio viene notificata ai listener registrati su
	 * questo nodo e sul nuovo figlio.
	 *
	 * @param child il nuovo figlio.
	 * @return la posizione di inserimento del nuovo nodo.
	 */
	public int insert (final Task child){
		int position;
		synchronized (children) {
			position = children.size ();
			insert (child, position);
		}
		return position;
	}
	
	/**
	 * Rimuove da questo nodo il figlio alla posizione <TT>pos</TT>.
	 * La rimozione del figlio viene notificata ai listener registrati su
	 * questo nodo e sul figlio rimosso.
	 *
	 * @param pos la posizione del figlio da rimuovere.
	 */
	@Override
	public void remove (final int pos) {
		final ProgressItem child = children.remove (pos);
		child.parent=null;
		
		setChanged ();
		child.setChanged ();
		notifyObservers ();
		child.notifyObservers ();
	}
	
	/**
	 * Rimuove il figlio da questo nodo.
	 * La rimozione del figlio viene notificata ai listener registrati su
	 * questo nodo e sul figlio rimosso.
	 *
	 * @param child il figlio da rimuovere.
	 */
	public void remove (final Task child) {
		int ix = childIndex (child);
		this.remove (ix);
	}
	
	/**
	 * Ritorna l'indice relativo alla posizione del figlio tra tutti i figli di
	 * questo nodo.
	 *
	 * @param child il figlio.
	 * @return l'indice relativo alla posizione del figlio.
	 */
	@Override
	public int childIndex (final Task child){
		for (int i=0;i<children.size ();i++){
			final Task childAt = children.get (i);
			if (child.equals (childAt)){
				return i;
			}
		}
		return -1;
	}

	/**
	 * Aggiunge un avanzamento a questo nodo.
	 *
	 * Questa azione viene notificata ai listener registrati su
	 * questo nodo.
	 *
	 * @param progress
	 * @throws ClassCastException se progress non è un'istanza di Progress.
	 */
	public synchronized void addProgress (final PieceOfWork progress) {
		progresses.add ((Progress)progress);
		
		setChanged ();
		notifyObservers ();
	}
	
	/**
	 * Accetta il visitor.
	 *
	 * @param recurse specificare se applicare la visita ricorsivamente sul sottoalbero.
	 * @param visitor il visitor.
	 */
	public void accept (final ProgressItemVisitor visitor, final boolean recurse){
		for (final Iterator<ProgressItem> it=children.iterator ();it.hasNext ();){
			it.next ().accept (visitor, recurse);
		}
		visitor.visit (this);
	}
	
	/**
	 * Registra un nuovo listener.
	 *
	 * @param l il nuovo listener.
	 */
	public void addProgressListener (final ProgressListener l) {
		progressListeners.add (l);
	}
	
	/**
	 * Rimuove un listener precedentemente registrato.
	 *
	 * @param l il listener da rimuovere.
	 */
	public void removeProgressListener (final ProgressListener l) {
		progressListeners.remove (l);
	}
	
	/**
	 * Ritorna una rappresentazione in formato stringa di questo nodo.
	 *
	 * @return una stringa che rappresenta questo nodo.
	 */
	@Override
	public String toString (){
		return name;
	}
	
	/**
	 * Ritorna la lista dei figli di questo nodo. Da usare solo in lettura.
	 * La lista non &egrave; modificabile.
	 *
	 * @return la lista dei figli.
	 */
	@Override
	public List<ProgressItem> getChildren (){
		return Collections.unmodifiableList (children);
	}
	
	/**
	 * Ritorna il figlio che occupa una determinata posizione tra i tutti i figli
	 * di questo nodo.
	 *
	 * @param pos la posizione del figlio da cercare.
	 * @return il figlio di questo nodo avente posizione <TT>pos</TT>.
	 */
	@Override
	public Task childAt (final int pos){
		return children.get (pos);
	}
	
	/**
	 * Ritorna il numero di figli di questo nodo.
	 *
	 * @return il numero dei figli.
	 */
	@Override
	public int childCount (){
		return children.size ();
	}
	
	/**
	 * Ritorna la lista di avanzamenti appartnenti a queto nodo. Non dovrebbe
	 * essere usata per apportare modifiche agli avanzamenti.
	 *
	 * @return la lista di avanzamenti appartnenti a queto nodo.
	 */
	@Override
	public List<Progress> getPiecesOfWork (){
		return Collections.unmodifiableList (progresses);
	}
	
//	private transient List _safeProgressesAccessor;
//	/**
//	 * Protegge l'accesso al campo interno da eventuali eccezioni dovutea corruzione dati. Siccome tali dati sonoconsideratiirrecuperabili
//	 * allora tanto vale proseguire.
//	 */
//	protected List safeProgressesAccessor (){
//		try {
//			return this.progresses;
//		} catch (final Exception e) {
//			/*
//			 * @workaround per mitigare problemi di corruzione dati
//			 */
//			if (_safeProgressesAccessor==null) {
//				_safeProgressesAccessor = new ArrayList ();
//				/*
//				 * mostra messaggio solo la prima volta
//				 */
//				System.out.println ("WARNING: Potential corruption detected. Erasing unavailable object reference.");
//				System.out.println ("Root cause:" + ExceptionUtils.getStackTrace (e));
//			}
//			return _safeProgressesAccessor;
//		}
//	}
//
//	private transient List _safeChildrenAccessor;
//	/**
//	 * Protegge l'accesso al campo interno da eventuali eccezioni dovutea corruzione dati. Siccome tali dati sonoconsideratiirrecuperabili
//	 * allora tanto vale proseguire.
//	 */
//	protected List safeChildrenAccessor (){
//		try {
//			return this.children;
//		} catch (final Exception e) {
//			/*
//			 * @workaround per mitigare problmi di corruzione dati
//			 */
//			if (_safeChildrenAccessor==null) {
//				_safeChildrenAccessor = new ArrayList ();
//				/*
//				 * mostra messaggio solo la prima volta
//				 */
//				System.out.println ("WARNING: Potential corruption detected. Erasing unavailable object reference.");
//				System.out.println ("Root cause:" + ExceptionUtils.getStackTrace (e));
//			}
//			return _safeChildrenAccessor;
//		}
//	}
	/**
	 * Ritorna gli avanzamenti apparteneneti al sottoalbero avente questo nodo
	 * come radice.
	 * Una lista di {@link PieceOfWork}.
	 *
	 * @return gli avanzamenti apparteneneti al sottoalbero.
	 */
	@Override
	public List<Progress> getSubtreeProgresses (){
		final List<Progress> subProgresses = new ArrayList<Progress> (progresses);
		for (final Iterator<ProgressItem> it = children.iterator (); it.hasNext ();){
			subProgresses.addAll (it.next ().getSubtreeProgresses ());
		}
		return Collections.unmodifiableList (subProgresses);
	}
	/**
	 * Ritorna gli elementi del sottoalbero.
	 * Una lista di {@link Task}
	 * @return gli elementi del sottoalbero avente questo item
	 * come radice.
	 */
	public List<ProgressItem> getDescendants (){
		final List<ProgressItem> retValue = new ArrayList<ProgressItem> (getChildren ());
		for (final Iterator<ProgressItem> it = getChildren ().iterator (); it.hasNext ();){
			retValue.addAll (it.next ().getDescendants ());
		}
		return Collections.unmodifiableList (retValue);
	}
	
	/**
	 * Notifica i listener registrati su questo nodo delle modifiche avvenute.
	 */
	public void itemChanged () {
		this.setChanged ();
		this.notifyObservers ();
	}
	
	/**
	 * Imposta la lista dei figli di questo nodo.
	 * <BR>
	 * N.B.: questo metodo non modifica i riferimenti dei figli verso il nuovo padre.
	 * @see ProgressItem#insert .
	 *
	 * @param children la nuova lista dei figli di questo nodo.
	 */
	public void setChildren (final List<ProgressItem> children) {
		this.children.clear ();
		this.children.addAll (children);
	}
	
	/**
	 * Imposta il codice di questo nodo.
	 *
	 * @param code il nuovo codice.
	 */
	public void setCode (final String code) {
		this.code=code;
	}
	
	/**
	 * Imposta il nome di questo nodo.
	 *
	 * @param name il nuovo nome.
	 */
	@Override
	public void setName (final String name) {
		this.name=name;
	}
	
	/**
	 * Imposta il padre di questo nodo.
	 * <BR>
	 * N.B.: questo metodo non modifica i riferimenti del nuovo e del vechio padre verso questo oggetto.
	 * @see Task#insert .
	 *
	 * @param parent il nuovo padre.
	 *@throws ClassCastException se il parametro passato non &egrave; di tipo ProgressItem
	 */
	public void setParent (final Task parent) {this.parent=(ProgressItem)parent;}
	
	/**
	 * Imposta gli avanzamenti relativi a questo nodo.
	 * ESCLUSIVAMENTE AD USO DEL PERSISTENT MANAGER.
	 *
	 * @param progresses Gli avanzamenti.
	 */
	public synchronized void setProgresses (final List<Progress> progresses) {
		this.progresses.clear ();
		this.progresses.addAll (progresses);
	}
	
	/**
	 * Ritorna la descrizione di questo nodo.
	 *
	 * @return la descrizione.
	 */
	@Override
	public String getDescription () {
		return this.description;
	}
	
	/**
	 * Imposta la descrizione di questo nodo.
	 *
	 * @param description la descrizione.
	 */
	@Override
	public void setDescription (final String description) {
		this.description = description;
	}
	
	/**
	 * Ritorna le annotazioni relative a questo nodo.
	 *
	 * @return le note.
	 */
	@Override
	public String getNotes () {
		return this.notes;
	}
	
	/**
	 * Imposta le annotazioni per questo nodo.
	 *
	 * @param notes le nuove note.
	 */
	public void setNotes (final String notes) {
		this.notes = notes;
	}
	
	/**
	 * Ritorna <TT>true</TT> se questo nodo � la radice della gerarchia dei 
	 * nodi di avanzamento del progetto.
	 *
	 * @return <TT>true</TT> se questo nodo � la radice della gerarchia dei 
	 * nodi di avanzamento del progetto.
	 */	
	public boolean isRoot (){
		return parent==null;
	}
	
	/**
	 * Rimuove il periodo di avanzamento specificato da questo nodo.
	 * @param progress l'avanzamento da rimuovere.
	 */
	@Override
	public synchronized void removePieceOfWork (final PieceOfWork progress){
		final int size = progresses.size ();
		for (int i=0;i<size;i++){
			final PieceOfWork candidate = progresses.get (i);
			if (candidate==progress){
				progresses.remove (i);
				break;
			}
		}
		setChanged ();
		notifyObservers ();
	}
	
	/**
	 * Aggiunge il periodo di avanzamento specificato a questo nodo.
	 * @param progress l'avanzamento da aggiungere.
	 * @return la posizione del nuovo avanzamento.
	 */
	public synchronized int insert (final PieceOfWork progress){
		progress.setTask (this);
		final int position = progresses.size ();
		progresses.add ((Progress)progress);
		setChanged ();
		notifyObservers ();
		return position;
	}
	
	/**
	 * Aggiunge il periodo di avanzamento specificato a questo nodo.
	 * 
	 * @param position la posizionedelnuovoavanzamento. Usare unvalore negativoper accodare.
	 * @param progress l'avanzamento da aggiungere.
	 */
	@Override
	public synchronized int insert (final PieceOfWork progress, final int position){
		progress.setTask (this);
		final int retValue;
		if (position<0) {
			progresses.add ((Progress)progress);
			retValue = progresses.indexOf (progress);
		} else {
			progresses.add (position, (Progress)progress);
			retValue = position;
		}
		setChanged ();
		notifyObservers ();
		
		return retValue;
	}

	@Override
	public int pieceOfWorkIndex (final PieceOfWork p) {
		return progresses.indexOf (p);
	}

	@Override
	public PieceOfWork pieceOfWorkAt (final int pos) {
		return progresses.get (pos);
	}

	@Override
	public int pieceOfWorkCount () {
		return progresses.size ();
	}

	@Override
	public TaskBackupImpl backup () {
		return new TaskBackupImpl (this);
	}

	/**
	 * Implementa il backup. Classe ad esclusivo uso interno, da non rendere persistente.
	 *<P>
	 * La classe &egrave; statica per evitare l'accesso involontarioalle variabili della classe che la contiene. Deve avere l'accesso solamente per estensione!
	 */
	@PersistenceAware
	public static class TaskBackupImpl extends ProgressItem implements TaskBackup {

		private final ProgressItem source;

		public TaskBackupImpl (final ProgressItem pi) {
			super (pi);
			source = pi;
			
			/*
			 * Usa le liste interne di riferimenti per salvare i backup.
			 */
//			children = new ArrayList<ProgressItem> ();
			for (final Iterator<ProgressItem> it = pi.children.iterator (); it.hasNext ();) {
				final ProgressItem t = it.next ();
				children.add (t.backup ());
			}
//			progresses = new ArrayList ();
			for (final Iterator<Progress> it = pi.progresses.iterator (); it.hasNext ();) {
				final Progress pow = it.next ();
				progresses.add (pow.backup ());
			}
			
		}
		@Override
		public ProgressItem getSource () {
			return source;
		}
		
		@Override
		public void restore () {

			source.code = code;
			source.description = description;
			source.name = name;
			source.notes = notes;


//			_source.children = new ArrayList<ProgressItem> ();
			source.children.clear ();
			for (final Iterator<ProgressItem> it = children.iterator (); it.hasNext ();) {
				final TaskBackupImpl tb = (TaskBackupImpl)it.next ();
				tb.restore ();
				source.children.add (tb.getSource ());
			}
//			_source.progresses = new ArrayList ();
			source.progresses.clear ();
			for (final Iterator<Progress> it = progresses.iterator (); it.hasNext ();) {
				final PieceOfWorkBackupImpl powb = (PieceOfWorkBackupImpl)it.next ();
				powb.restore ();
				source.progresses.add (powb.getSource ());
			}
		}
	}

}
