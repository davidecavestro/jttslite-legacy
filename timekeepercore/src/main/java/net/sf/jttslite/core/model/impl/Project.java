/*
 * Project.java
 *
 * Created on 18 aprile 2004, 14.25
 */

package net.sf.jttslite.core.model.impl;

import javax.jdo.annotations.PersistenceAware;
import javax.jdo.annotations.PersistenceCapable;
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
@PersistenceCapable(table="jtts_workspace", detachable="true")
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
	 *
	 * @param source il progetto da copiare
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
	@Override
	public String getName (){
		return name;
	}
	
	/**
	 * Imposta il nome di questo progetto.
	 *
	 * @param name il nome da impostare.
	 */	
	@Override
	public void setName (final String name){
		this.name = name;
	}
	
	/**
	 * Ritorna la radice della gerarchia degli avanzamenti di questo progetto.
	 *
	 * @return la radice della gerarchia dei nodi di avanzamento.
	 */	
	@Override
	public ProgressItem getRoot (){
		return root;
	}
	
	/**
	 * Ritorna la rappresentazione in formato stringa di questo progetto.
	 *
	 * @return una stringa che rappresenta questo progetto.
	 */
	@Override
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
	@Override
	public void setRoot(final Task root) {
		this.root=(ProgressItem)root;
	}
	
	/** 
	 * Ritorna la descrizione di questo progetto.
	 *
	 * @return la descrizione.
	 */
	@Override
	public String getDescription() {
		return description;
	}
	
	/** 
	 * Imposta la descrizione di questo progetto.
	 *
	 * @param description la descrizione.
	 */
	@Override
	public void setDescription(final String description) {
		this.description = description;
	}
	
	/** 
	 * Ritorna le annotazioni relative a questo progetto.
	 *
	 * @return le note.
	 */
	@Override
	public String getNotes() {
		return notes;
	}
	
	/** 
	 * Imposta le annotazioni relative a questo progetto.
	 *
	 * @param notes le note.
	 */
	@Override
	public void setNotes(final String notes) {
		this.notes = notes;
	}

	@Override
	public WorkSpaceBackup backup () {
		return new WorkSpaceBackupImpl (this);
	}

	/**
	 * Implementa il backup. Classe ad esclusivo uso interno, da non rendere persistente.
	 *<P>
	 * La classe &egrave; statica per evitare l'accesso involontario alle variabili della classe che la contiene. Deve avere l'accesso solamente per estensione!
	 */
	@PersistenceAware
	private static class WorkSpaceBackupImpl extends Project implements WorkSpaceBackup {
		
		private final Project source;

		public WorkSpaceBackupImpl (final Project p) {
			super (p);
			source = p;
		}
		@Override
		public WorkSpace getSource () {
			return source;
		}

		@Override
		public void restore () {
			source.description = description;
			source.name = name;
			source.notes = notes;

            source.root = root;
		}

	}
}
