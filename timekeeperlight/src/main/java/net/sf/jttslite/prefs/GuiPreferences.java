package net.sf.jttslite.prefs;

import java.awt.Component;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * Gestisce le preferenze relative all'interfaccia grafica. Si occupa di rendere
 * persistenti le informazioni di larghezza delle colonne e proporzioni di rettangoli.
 *
 * @author capitanfuturo
 */
public class GuiPreferences {

   /** Identificativo della sezione di impostazioni sulle dimensioni e posizioni delle finestre */
   public static final String SECTION_GUI = "GUI";
   /** Identificativo del parametro: posizione x del componente */
   private static final String PARAM_X_POSITION = "-x-pos";
   /** Identificativo del parametro: posizione y del componente */
   private static final String PARAM_Y_POSITION = "-y-pos";
   /** Identificativo del parametro: larghezza assoluta del componente */
   private static final String PARAM_ABSOLUTE_WIDTH = "-width";
   /** Identificativo del parametro: altezza assoluta del componente */
   private static final String PARAM_ABSOLUTE_HEIGHT = "-height";

   /** lista dei componenti registrati nel gestore delle preferenze grafiche */
   private final List<PersistentComponent> registeredComponents =
		   new ArrayList<PersistentComponent> ();
   private PreferencesManager manager;
   private Preferences preferences;

   /**
	* Costruttore.
    * @param manager
    */
   public GuiPreferences (PreferencesManager manager) {
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
   public boolean register (final PersistentComponent component,
		   final boolean init) {
	  if (!registeredComponents.contains (component)) {
		 registeredComponents.add (component);
	  }
	  if (init) {
		 return component.restorePersistent ();
	  }
	  return false;
   }

   /**
    * Rimuove il componente specificato dal registro.
    *
    * @param component il componente da rimuovere dal registro.
    */
   public void unregister (final PersistentComponent component) {
	  registeredComponents.remove (component);
   }

   /**
    * Salva le impostazioni di tuttii componenti registrati.
    */
   public void makePersistentAll () {
	  for (final Iterator<PersistentComponent> it = registeredComponents.
			  iterator (); it.hasNext ();) {
		 it.next ().makePersistent ();
	  }
   }

   /**
    * Imposta le informazioni sulle dimensioni assolute dell'area occupata dal 
    * rettangolo r
    * 
    * @param r rettangolo di cui salvare le dimensioni
    * @param name identificativo del rettangolo
    */
   public void setAbsoluteRectangleDimension (final Rectangle r,
		   final String name) {
	  preferences.putDouble (name + PARAM_X_POSITION, r.getX ());
	  preferences.putDouble (name + PARAM_Y_POSITION, r.getY ());
	  preferences.putDouble (name + PARAM_ABSOLUTE_WIDTH, r.getWidth ());
	  preferences.putDouble (name + PARAM_ABSOLUTE_HEIGHT, r.getHeight ());
   }

   /**
    * Restituisce le informazioni sulle dimensioni assolute dell'area occupata dal
    * rettangolo di nome name
	*
    * @param name identificativo del rettangolo
    * @return  un rettangolo con le le informazioni sulle dimensioni assolute dell'area occupata dal
    * rettangolo di nome name
    */
   public Rectangle getAbsoluteRectangleDimension (final String name) {
	  final double xPos = preferences.getDouble (name + PARAM_X_POSITION, 0.0);
	  final double yPos = preferences.getDouble (name + PARAM_Y_POSITION, 0.0);
	  final double width = preferences.getDouble (name + PARAM_ABSOLUTE_WIDTH,
			  0.0);
	  final double height = preferences.getDouble (name + PARAM_ABSOLUTE_HEIGHT,
			  0.0);
	  return new Rectangle ((int) xPos, (int) yPos, (int) width, (int) height);
   }

   /**
    * Rende persistenti i dati relativi alle dimensioni e poisizione del componente
    * specificato.
    *
    * @param key la chiave per la persistenza.
    * @param component il componente.
    */
   public final void makeBoundsPersistent (final String key,
		   final Component component) {
	  setAbsoluteRectangleDimension (component.getBounds (), key);
   }

   /**
    * Ripristina le dimensioni persistenti per il componente specificato,
	* identificato dalla chiave specificata.
    *
    * @param key la chiave.
    * @param component il componente.
    * @return <TT>true</TT> se sono stati recuperati e ripristinati i dati persistenti.
    */
   public final boolean restoreAbsolutePersistentBounds (String key,
		   final Component component) {
	  final Rectangle bounds = getAbsoluteRectangleDimension (key);
	  if (bounds != null) {
		 //component.setPreferresdSize (bounds.getSize ());
              component.setBounds(bounds);
		 component.setLocation ((int) bounds.getX (), (int) bounds.getY ());
		 return true;
	  }
	  return false;
   }

    public final void makeColumnWidthsPersistent(final String key,
            final JTable table) {
        final TableColumnModel columns = table.getColumnModel();
        for (int i = 0; i < columns.getColumnCount(); i++) {
            preferences.putInt(key + "-col-width" + i, columns.getColumn(i).getWidth());
        }
    }

    public final void restorePersistentColumnWidths(final String key,
            final JTable table) {
        final TableColumnModel columns = table.getColumnModel();
        for (int i = 0; i < columns.getColumnCount(); i++) {
            final int width = preferences.getInt(key + "-col-width" + i, columns.getColumn(i).getWidth());
            columns.getColumn(i).setPreferredWidth(width);
        }
    }

   public final Preferences getPreferences () {
	  return preferences;
   }
}
