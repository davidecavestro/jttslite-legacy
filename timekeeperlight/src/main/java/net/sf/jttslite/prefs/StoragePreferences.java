/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jttslite.prefs;

import java.util.prefs.Preferences;

/**
 * Preferenze relative allo strato di persistenza dell'applicazione
 * @author g-caliendo
 */
public class StoragePreferences {

   /** Identificativo della sezione di configurazione dello storage */
   protected  static final String SECTION_STORAGE = "JDO";
   /** Parametro che definisce il nome dell'utente da usare per l'0accesso allo storage JDO. */
   protected final static String PROPNAME_JDOSTORAGEUSER_NAME = "jdousername";
   /** Parametro che definisce il nome dello storage JDO per la persistenza. */
   protected final static String PROPNAME_JDOSTORAGE_NAME = "jdostoragename";
   /** Parametro che definisce il percorso della directory contenente lo storage JDO per la persistenza. */
   protected final static String PROPNAME_JDOSTORAGEDIR_PATH = "jdostoragedirpath";
   /** Manager globale delle preferenze */
   private PreferencesManager manager;
   /** Oggetto che wrappa le preferenze relative allo strato di persistenza */
   private Preferences preferences;

   protected StoragePreferences(PreferencesManager manager){
	  this.manager = manager;
	  this.preferences = manager.getMainPreferences ().node (SECTION_STORAGE);
   }

   	public String getJDOUserName () {
	   return preferences.get (PROPNAME_JDOSTORAGEUSER_NAME, PreferencesManager.getUserAccount ());
	}

	public void setJDOUserName (final String s) {
	   preferences.put (PROPNAME_JDOSTORAGEUSER_NAME, s);
	}

	public String getJDOStorageName () {
	   return preferences.get (PROPNAME_JDOSTORAGE_NAME, "database");
	}

	public void setJDOStorageName (final String s) {
	   preferences.put (PROPNAME_JDOSTORAGE_NAME, s);
	}

	public String getJDOStorageDirPath () {
	   return preferences.get(PROPNAME_JDOSTORAGEDIR_PATH, manager.getUserApplicationDataDirPath ());
	}

	public void setJDOStorageDirPath (final String s) {
	   preferences.put (PROPNAME_JDOSTORAGEDIR_PATH, s);
	}

}
