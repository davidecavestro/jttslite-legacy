package net.sf.jttslite.dbmigrator;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import org.ini4j.Ini;
import org.ini4j.IniPreferences;

/**
 * Classe dedicata ad occuparsi della detection del precedente sistema di gestione delle
 * preferenze via file di properties e migrazione nel nuovo sistema di file Ini
 * @author g-caliendo
 */
public class PreferencesMigrator {

   /** Identificativo della sezione di configurazione dello storage */
   private static final String SECTION_STORAGE = "JDO";
   /** Parametro che definisce il nome dell'utente da usare per l'accesso allo storage JDO. */
   private final static String PROPNAME_JDOSTORAGEUSER_NAME = "jdousername";
   /** Parametro che definisce il nome dello storage JDO per la persistenza. */
   private final static String PROPNAME_JDOSTORAGE_NAME = "jdostoragename";
   /** Parametro che definisce il percorso della directory contenente lo storage JDO per la persistenza. */
   private final static String PROPNAME_JDOSTORAGEDIR_PATH = "jdostoragedirpath";
   private Ini iniPreferences;
   private Properties properties;
   private Logger logger;
   private String jdoStorageUserName;
   private String jdoStorageName;
   private String jdoStorageDirPath;

   public PreferencesMigrator(Logger logger, File oldProperties, Ini iniPreferences) {
	  this.iniPreferences = iniPreferences;
	  this.logger = logger;
	  /* Recupero le precedenti le properties */
	  try {
		 loadOldProperties (oldProperties);
	  } catch (Exception e) {
		 logger.log (Level.WARNING, "Error retireving old properties ", e);
	  }
	  jdoStorageUserName = properties.getProperty (PROPNAME_JDOSTORAGEUSER_NAME);
	  jdoStorageName = properties.getProperty (PROPNAME_JDOSTORAGE_NAME);
	  jdoStorageDirPath = properties.getProperty (PROPNAME_JDOSTORAGEDIR_PATH);
	  /* Aggiorno il nuovo file di ini */
	  try {
		 writeIni ();
	  } catch (MigrationException e) {
		 logger.log (Level.WARNING, "Error retrieving section " + SECTION_STORAGE, e);
	  }
   }

   /**
    * Cerca di reperire i dati dal precedente file di property
    * @param oldProperties
    * @throws MigrationException
    */
   public void loadOldProperties(File oldProperties) throws MigrationException {
	  try {
		 FileInputStream fis = new FileInputStream (oldProperties);
		 this.properties = new Properties ();
		 properties.load (fis);
		 logger.info ("Old properties loaded");
	  } catch (Exception e) {
		 throw new MigrationException (e);
	  }
   }

   /**
    * Dato il file ini di destinazione il metodo si occupa di migrare i dati
    * reperiti nei passi precedenti
    *
    * @throws MigrationException
    */
   public void writeIni() throws MigrationException {
	  Preferences preferences = new IniPreferences (iniPreferences);
	  try {
		 if (preferences.nodeExists (SECTION_STORAGE)) {
			Preferences storagePreferences = preferences.node (SECTION_STORAGE);
			if (jdoStorageUserName != null) {
			   storagePreferences.put (PROPNAME_JDOSTORAGEUSER_NAME, jdoStorageUserName);
			   logger.info ("Property " + PROPNAME_JDOSTORAGEUSER_NAME + " migrated");
			} else {
			   logger.info ("Property " + PROPNAME_JDOSTORAGEUSER_NAME + " not migrated migrated");
			}
			if (jdoStorageName != null) {
			   storagePreferences.put (PROPNAME_JDOSTORAGE_NAME, jdoStorageName);
			   logger.info ("Property " + PROPNAME_JDOSTORAGE_NAME + " migrated");
			} else {
			   logger.info ("Property " + PROPNAME_JDOSTORAGE_NAME + " not migrated migrated");
			}
			if (jdoStorageDirPath != null) {
			   storagePreferences.put (PROPNAME_JDOSTORAGEDIR_PATH, jdoStorageDirPath);
			   logger.info ("Property " + PROPNAME_JDOSTORAGEDIR_PATH + " migrated");
			} else {
			   logger.info ("Property " + PROPNAME_JDOSTORAGEDIR_PATH + " not migrated migrated");
			}
		 } else {
			throw new MigrationException ("Ini preferences not found ");
		 }
	  } catch (Exception e) {
		 throw new MigrationException (e);
	  }
   }
}
