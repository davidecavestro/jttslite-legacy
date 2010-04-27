/*
 * PersistentComponent.java
 *
 * Created on 16 maggio 2005, 22.48
 */
package net.sf.jttslite.prefs;

/**
 * Componente grafico con attributi persistenti.
 *
 * @author  davide
 */
public interface PersistentComponent {

   /**
	 * Ritorna la chiave usata per la persistenza degli attributi di questo componente.
	 *
	 * @return la chiave usata per la persistenza degli attributi di questo componente.
	 */	
	String getPersistenceKey ();
	
	/**
	 * Rende persistente lo statodi questo componente.
	 */
	void makePersistent ();
	
	/**
	 * Ripristina lo stato persistente, qualora esista.
	 * @return <TT>true</TT> se sono stati ripristinati i dati persistenti.
	 */
	boolean restorePersistent ();
}
