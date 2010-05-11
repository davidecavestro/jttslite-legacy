package net.sf.jttslite.prefs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import net.sf.jttslite.common.application.ApplicationData;
import org.ini4j.Ini;
import org.ini4j.IniPreferences;

/**
 * Manager dedicato alla gestione delle preferenze applicative. Nel caso non sia
 * definito nulla si preoccupa di gestire anche i valori di default
 *
 * @author g-caliendo
 */
public class PreferencesManager {

   /** Nome directory dati utente. */
   public final static String USER_DATADIR_NAME = "var";
   /** Nome proprieta' di sistema contenente il percorso della home directory utente. */
   public final static String USER_HOMEDIR_PATH = "user.home";
   /** Nome proprieta' di sistema contenente il percorso della directory di lavoro dell'utente utente. */
   public final static String USER_WORKINGDIR_PATH = "user.dir";
   /** Nome account utente corrente. */
   public final static String USER_ACCOUNT = "user.name";
   /** Nome directory impostazioni utente. */
   public final static String USER_SETTINGSDIR_NAME = "etc";
   private Ini ini;
   private Preferences preferences;
   private UserPreferences userPreferences;
   private GuiPreferences guiPreferences;
   private StoragePreferences storagePreferences;
   private ApplicationData applicationData;
   private File prefsFile;

   public PreferencesManager(ApplicationData applicationData) {
	  try {
		 this.applicationData = applicationData;
		 //recupero il file delle preferenze
		 File prefsDir = new File (getUserApplicationSettingsDirPath ());
		 if (!prefsDir.exists ()) {
			prefsDir.mkdirs ();
			Logger.getLogger (PreferencesManager.class.getName ()).info ("Created configuration directory in: " + getUserApplicationSettingsDirPath ());
		 }
		 prefsFile = new File (getUserApplicationSettingsDirPath (), "JTTS.ini");
		 //se non esiste lo creo e gli imposto i valori di default
		 if (!prefsFile.exists ()) {
			prefsFile.createNewFile ();
			Logger.getLogger (PreferencesManager.class.getName ()).info ("Created configuration file: " + prefsFile.getName ());
			IniInitializator initializator = new IniInitializator (this);
			ini = initializator.init ();
		 }
		 //altrimenti carico i dati
		 if (ini == null) {
			FileInputStream fip = new FileInputStream (prefsFile);
			ini = new Ini (fip);
		 }
		 preferences = new IniPreferences (ini);
	  } catch (IOException ex) {
		 Logger.getLogger (PreferencesManager.class.getName ()).log (Level.SEVERE, "Error while retrieving configuration", ex);
	  }
   }

   public void storePrefs() {
	  try {
		 FileOutputStream fop = new FileOutputStream (prefsFile);
		 getGuiPreferences ().makePersistentAll ();
		 preferences.flush ();
		 ini.store (fop);
	  } catch (Exception ex) {
		 Logger.getLogger (PreferencesManager.class.getName ()).log (Level.SEVERE, "Error while storing preferences", ex);
	  }
   }

   protected Preferences getMainPreferences() {
	  if (preferences == null) {
		 preferences = new IniPreferences (ini);
	  }
	  return preferences;
   }

   public UserPreferences getUserPreferences() {
	  if (userPreferences == null) {
		 userPreferences = new UserPreferences (this);
	  }
	  return userPreferences;
   }

   public GuiPreferences getGuiPreferences() {
	  if (guiPreferences == null) {
		 guiPreferences = new GuiPreferences (this);
	  }
	  return guiPreferences;
   }

   public StoragePreferences getStoragePreferences() {
	  if (storagePreferences == null) {
		 storagePreferences = new StoragePreferences (this);
	  }
	  return storagePreferences;
   }

   /**
    * Ritorna il percorso della HOME directory dell'utente.
    *
    * @return il percorso della HOME directory dell'utente.
    */
   public static String getUserHomeDirPath() {
	  return System.getProperty (USER_HOMEDIR_PATH);
   }

   /**
    * Ritorna il nome dell'acount dell'utente.
    *
    * @return il nome dell'acount dell'utente.
    */
   public static String getUserAccount() {
	  return System.getProperty (USER_ACCOUNT);
   }

   /**
    * Ritorna il percorso della directory privata dell'utente contenente i dati dell'applicazione .
    *
    * @return il percorso della directory privata dell'utente contenente i dati dell'applicazione .
    */
   public String getUserApplicationDataDirPath() {
	  return new File (getUserApplicationDirPath (), USER_DATADIR_NAME).getPath ();
   }

   /**
    * Ritorna il nome directory utente di supporto all'applicazione.
    * @return il nome directory utente di supporto all'applicazione.
    */
   protected String getUserApplicationRepositoryDirName() {
	  return "." + applicationData.getApplicationInternalName ();
   }

   /**
    * Ritorna il percorso della directory privata dell'utente di supporto all'applicazione .
    *
    * @return il percorso della directory privata dell'utente di supporto all'applicazione .
    */
   public String getUserApplicationDirPath() {
	  return new File (getUserHomeDirPath (), getUserApplicationRepositoryDirName ()).getPath ();
   }

   /**
    * Ritorna il percorso della directory privata dell'utente contenente la configurazione dell'applicazione .
    *
    * @return il percorso della directory privata dell'utente contenente la configurazione dell'applicazione .
    */
   public String getUserApplicationSettingsDirPath() {
	  return new File (getUserApplicationDirPath (), USER_SETTINGSDIR_NAME).getPath ();
   }
}
