/*
 * Project.java
 *
 * Created on 18 aprile 2004, 14.25
 */

package net.sf.jttslite.core.model.impl;

import net.sf.jttslite.core.model.Task;
import net.sf.jttslite.core.model.WorkSpace;
import net.sf.jttslite.core.model.WorkSpaceBackup;

/**
 * Implementa l'entit� di tipo PROGETTO. 
 * I principali attributi di un progetto sono:
 *	<UL>
 *		<LI>il nome;
 *		<LI>una gerarchia di nodi sui quali � possibile effettuare avanzamenti.
 *	</UL>
 *
 * Anche se per implementare l'interfaccia <TT>WorkSpace</TT>, utilizza <TT>Task</TT>, questa classe DEVE ricevere oggetti di tipo effettivo <TT>ProgressItem</TT> per funzionare correttamente.
 *<BR>
 *Questo comportamento anomalo &egrave; dovuto all'utilizzo di codice JDO legacy.
 *
 * @author  davide
 */
public class Project implements WorkSpace {
	
	/**
	 * La radice della gerarchia di avanzamenti.
	 */
	protected ProgressItem root;
	
	/**
	 * Il nome.
	 */
	protected String name;
	
	/** 
	 * La descrizione. 
	 */
	protected String description;
	
	/**
	 * Le annotazioni.
	 */
	protected String notes;
	
	/** 
	 * Costruttore vuoto.
	 */
	public Project() {
	}
	

	/**
	 * Costruttore copia.
	 */
	public Project (final Project source) {
        this.description = source.description;
        this.name = source.name;
        this.notes = source.notes;
	}

	/**
	 * Costruttore con nome e gerarchia nodi.
	 * @param name
	 * @param root
	 */
	public Project(String name, ProgressItem root) {
		this.name = name;
		this.root = root;
	}
	
	/**
	 * Ritorna il nome di questo progetto.
	 *
	 * @return il nome.
	 */	
	public String getName (){
		return this.name;
	}
	
	/**
	 * Imposta il nome di questo progetto.
	 *
	 * @param name il nome da impostare.
	 */	
	public void setName (String name){
		this.name = name;
	}
	
	/**
	 * Ritorna la radice della gerarchia degli avanzamenti di questo progetto.
	 *
	 * @return la radice della gerarchia dei nodi di avanzamento.
	 */	
	public Task getRoot (){
		return this.root;
	}
	
	/**
	 * Ritorna la rappresentazione in formato stringa di questo progetto.
	 *
	 * @return una stringa che rappresenta questo progetto.
	 */	
	public String toString (){
		final StringBuilder sb = new StringBuilder ();
		sb.append ("name: ").append (this.name)
		.append (" root: ").append (this.root);
		return sb.toString();
	}
	
	/** 
	 * Imposta il nodo radice della gerarchia di avanzamenti.
	 *
	 * @param root la radice della gerarchia di nodi.
	 */
	public void setRoot(Task root) {
		this.root=(ProgressItem)root;
	}
	
	/** 
	 * Ritorna la descrizione di questo progetto.
	 *
	 * @return la descrizione.
	 */
	public String getDescription() {
		return this.description;
	}
	
	/** 
	 * Imposta la descrizione di questo progetto.
	 *
	 * @param description la descrizione.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/** 
	 * Ritorna le annotazioni relative a questo progetto.
	 *
	 * @return le note.
	 */
	public String getNotes() {
		return this.notes;
	}
	
	/** 
	 * Imposta le annotazioni relative a questo progetto.
	 *
	 * @param notes le note.
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	public WorkSpaceBackup backup () {
		return new WorkSpaceBackupImpl (this);
	}

	/**
	 * Implementa il backup. Classe ad esclusivo uso interno, da non rendere persistente.
	 *<P>
	 * La classe &egrave; statica per evitare l'accesso involontario alle variabili della classe che la contiene. Deve avere l'accesso solamente per estensione!
	 */
	private static class WorkSpaceBackupImpl extends Project implements WorkSpaceBackup {
		private final Project _source;
		public WorkSpaceBackupImpl (final Project p) {
			super (p);
			_source = p;
		}
		public WorkSpace getSource () {
			return _source;
		}

		public void restore () {
			_source.description = description;
			_source.name = name;
			_source.notes = notes;

            _source.root = root;
		}

	}
}
