/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jttslite.prefs;

import java.awt.Component;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author g-caliendo
 */
public class GuiPreferences {

   /** Identificativo della sezione di impostazioni sulle dimensioni e posizioni delle finestre */
   public static final String SECTION_GUI = "GUI";
   	private final List<PersistentComponent> registeredComponents = new ArrayList<PersistentComponent> ();
	private PreferencesManager manager;
	private Preferences preferences;

	/** Costruttore.
	 * @param manager 
	 */
	public GuiPreferences(PreferencesManager manager){
	   this.manager = manager;
	   preferences = manager.getMainPreferences ().node (SECTION_GUI);
	}

	/**
	 * Registra il componente specificato per la persistenza e lo inizializza.
	 *
	 * @param component il componente.
	 * @return <TT>true</TT> se il <TT>component</TT> e' stato inizializzato con i dati persistenti.
	 */
	public boolean register (final PersistentComponent component) {
		return this.register (component, true);
	}
	/**
	 * Registra il componente specificato per la persistenza.
	 *
	 * @param component il componente.
	 * @param init <TT>true</TT> per inizializzare il componente con i dati precedentemente salvati.
	 * @return <TT>true</TT> se il <TT>component</TT> e' stato inizializzato con i dati persistenti.
	 */
	public boolean register (final PersistentComponent component, final boolean init) {
		if (!registeredComponents.contains (component)){
			registeredComponents.add (component);
		}
		if (init){
			return component.restorePersistent ();
		}
		return false;
	}

	/**
	 * Rimuove il componente specificato dal registro.
	 *
	 * @param component il componente da rimuovere dal registro.
	 */
	public void unregister (final PersistentComponent component){
		registeredComponents.remove (component);
	}

	/**
	 * Salva le impostazioni di tuttii componenti registrati.
	 */
	public void makePersistentAll (){
		for (final Iterator<PersistentComponent> it = registeredComponents.iterator ();it.hasNext ();){
			it.next ().makePersistent ();
		}
	}

	public Rectangle getRectangle (final String xPosName, final String yPosName, final String widthName, final String heightName){
		final double xPos = preferences.getDouble (xPosName, 0.0);
		final double yPos = preferences.getDouble (yPosName, 0.0);
		final double width = preferences.getDouble (widthName, 0.0);
		final double height = preferences.getDouble (heightName, 0.0);
		return new Rectangle ((int) xPos, (int) yPos, (int) width, (int) height);
	}


	public void setRectangle (final Rectangle r, final String xPosName, final String yPosName, final String widthName, final String heightName){
		preferences.putDouble (xPosName, r.getX ());
		preferences.putDouble (yPosName, r.getY ());
		preferences.putDouble (widthName, r.getWidth ());
		preferences.putDouble (heightName, r.getHeight ());
	}

	/**
	 * Rende persistenti i dati relativi alle dimensioni e poisizione del componente specificato.
	 *
	 * @param key la chiave per la persistenza.
	 * @param component il componente.
	 */
	public final void makeBoundsPersistent (final String key, final Component component ){
		setRectangle (
			component.getBounds (),
			key+"-x-pos",
			key+"-y-pos",
			key+"-width",
			key+"-height");
	}

	/**
	 * Ripristina le dimensioni persistenti per il componente specificato, identificato dalla chiave specificata.
	 *
	 * @param key la chiave.
	 * @param component il componente.
	 * @return <TT>true</TT> se sono stati recuperati e ripristinati i dati persistenti.
	 */
	public final boolean restorePersistentBounds (String key, final Component component ){
		final Rectangle bounds = getRectangle (
			key+"-x-pos",
			key+"-y-pos",
			key+"-width",
			key+"-height");
		if (bounds!=null){
			component.setBounds (bounds);
			return true;
		}
		return false;
	}

	/**
	 * Imposta la PreferredSize per il componente specificato alle dimensioni persistenti, identificato dalla chiave specificata.
	 *
	 * @param key la chiave.
	 * @param component il componente.
	 * @return <TT>true</TT> se sono stati recuperati e ripristinati i dati persistenti.
	 */
	public final boolean restorePersistentBoundsToPreferredSize (final String key, final JComponent component ){
		final Rectangle bounds =  getRectangle (
			key+"-x-pos",
			key+"-y-pos",
			key+"-width",
			key+"-height");
		if (bounds!=null){
			component.setPreferredSize (bounds.getSize ());
			return true;
		}
		return false;
	}

	public final void makeColumnWidthsPersistent (final String key, final JTable table ){
		final TableColumnModel columns = table.getColumnModel ();
		int i=0;
		for (final Enumeration<TableColumn> en = columns.getColumns ();en.hasMoreElements ();){
			final TableColumn column = en.nextElement ();
			preferences.putInt (key+"-col-width"+i++, column.getWidth ());
		}
	}

	public final void restorePersistentColumnWidths (final String key, final JTable table ){
		final TableColumnModel columns = table.getColumnModel ();
		int i=0;
		for (final Enumeration<TableColumn> en = columns.getColumns ();en.hasMoreElements ();){
			final TableColumn column = en.nextElement ();
			final int width = preferences.getInt (key+"-col-width"+i++,10);
			column.setPreferredWidth (width);
		}
	}

	public final Preferences getPreferences(){
	   return preferences;
	}
}
