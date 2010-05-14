package net.sf.jttslite.prefs;

import java.io.IOException;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.spi.IniBuilder;

/**
 * Inizializzatore del file di configurazione applicativo. Si occupa di popolare
 * il file con i valori di default
 *
 * @author capitanfuturo
 */
public class IniInitializator {

   private final PreferencesManager prefsManager;

   /**
    * Costruttore dell'inizializzatore del file di configurazione applicativo.
    * Si occupa di popolareil file con i valori di default. Dopo l'istanzazione
    * va utilizzato il metodo {@link IniInitializator#init() }
    *
	* @param prefsManager 
    */
   public IniInitializator(PreferencesManager prefsManager) {
	  this.prefsManager = prefsManager;
   }

   /**
    * Inizializza il file di configurazione su filesistem secondo la sintassi
    * ini
    *
    * @return l'oggetto Ini che descrive la configurazione
    * @throws InvalidFileFormatException
    * @throws IOException
    */
   public Ini init() throws InvalidFileFormatException, IOException {
	  Ini ini = new Ini ();
	  IniBuilder builder = IniBuilder.newInstance (ini);
	  // inizio del file di configurazione
	  builder.startIni ();
	  // impostazioni di JDO
	  builder.startSection (StoragePreferences.SECTION_STORAGE);
	  builder.handleOption (StoragePreferences.PROPNAME_JDOSTORAGEUSER_NAME, PreferencesManager.getUserAccount ());
	  builder.handleOption (StoragePreferences.PROPNAME_JDOSTORAGEDIR_PATH, prefsManager.getUserApplicationSettingsDirPath ());
	  builder.handleOption (StoragePreferences.PROPNAME_JDOSTORAGE_NAME, "database");
	  builder.endSection ();
	  // impostazioni sulle dimensioni e posizioni delle finestre
	  builder.startSection (GuiPreferences.SECTION_GUI);
	  builder.handleComment ("Action template dialog");
	  builder.handleOption ("actionTemplatesDialog-x-pos", "353.0");
	  builder.handleOption ("actionTemplatesDialog-y-pos", "25.0");
	  builder.handleOption ("actionTemplatesDialog-width", "391.0");
	  builder.handleOption ("actionTemplatesDialog-height", "202.0");
	  builder.handleComment ("Log console window");
	  builder.handleOption ("logconsole-x-pos", "415.0");
	  builder.handleOption ("logconsole-y-pos", "295.0");
	  builder.handleOption ("logconsole-width", "443.0");
	  builder.handleOption ("logconsole-height", "322.0");
	  builder.handleComment ("Main window");
	  builder.handleOption ("mainwindow-x-pos", "239.0");
	  builder.handleOption ("mainwindow-y-pos", "212.0");
	  builder.handleOption ("mainwindow-width", "802.0");
	  builder.handleOption ("mainwindow-height", "600.0");
	  builder.handleComment ("New task dialog");
	  builder.handleOption ("newtaskdialog-x-pos", "50.0");
	  builder.handleOption ("newtaskdialog-y-pos", "50.0");
	  builder.handleOption ("newtaskdialog-width", "200.0");
	  builder.handleOption ("newtaskdialog-height", "300.0");
	  builder.handleComment ("New work space dialog");
	  builder.handleOption ("newworkspacedialog-x-pos", "428.0");
	  builder.handleOption ("newworkspacedialog-y-pos", "397.0");
	  builder.handleOption ("newworkspacedialog-width", "365.0");
	  builder.handleOption ("newworkspacedialog-height", "153.0");
	  builder.handleComment ("Open project dialog");
	  builder.handleOption ("open-project-dialog-x-pos", "503.0");
	  builder.handleOption ("open-project-dialog-y-pos", "432.0");
	  builder.handleOption ("open-project-dialog-width", "275.0");
	  builder.handleOption ("open-project-dialog-height", "161.0");
	  builder.handleComment ("Report dialog");
	  builder.handleOption ("report-dialog-x-pos", "370.0");
	  builder.handleOption ("report-dialog-y-pos", "222.0");
	  builder.handleOption ("report-dialog-width", "536.0");
	  builder.handleOption ("report-dialog-height", "576.0");
	  builder.handleComment ("Task tree panel");
	  builder.handleOption ("taskTreePanel-x-pos", "0.0");
	  builder.handleOption ("taskTreePanel-y-pos", "0.0");
	  builder.handleOption ("taskTreePanel-width", "342.0");
	  builder.handleOption ("taskTreePanel-height", "481.0");
	  builder.handleComment ("Work space dialog");
	  builder.handleOption ("workspacesDialog-x-pos", "341.0");
	  builder.handleOption ("workspacesDialog-y-pos", "328.0");
	  builder.handleOption ("workspacesDialog-width", "563.0");
	  builder.handleOption ("workspacesDialog-height", "275.0");
	  builder.handleComment ("Table preferences");
	  builder.handleOption ("actiontemplates.table.visible.columns", "0,1,2");
	  builder.handleOption ("actiontemplates.table.sorted.columns", "1");
	  builder.handleOption ("progresses.table.sorted.columns", "2");
	  builder.handleOption ("progresses.table.visible.columns", "0,1,2");
	  builder.handleOption ("task.tree.sorted.columns", "1");
	  builder.handleOption ("task.tree.visible.columns", "0,1,2");
	  builder.handleOption ("workspaces.table.sorted.columns", "1");
	  builder.handleOption ("workspaces.table.visible.columns", "0,1");
	  builder.endSection ();
	  // impostazioni utente
	  builder.startSection (UserPreferences.SECTION_OPTIONS);
	  builder.handleOption ("chartdepth", "5");
	  builder.handleOption (UserPreferences.PROPNAME_HELPERAPPSENABLED, "true");
	  builder.handleOption (UserPreferences.PROPNAME_LOOKANDFEELENABLED, "false");
	  builder.handleOption (UserPreferences.PROPNAME_TRAYICONENABLED, "true");
	  builder.handleOption (UserPreferences.PROPNAME_LOGDIRPATH, prefsManager.getUserApplicationDataDirPath () + UserPreferences.DEF_LOG_DIR);
	  builder.handleOption (UserPreferences.PROPNAME_LASTPROJECTNAME, null);
	  builder.handleOption (UserPreferences.PROPNAME_CHARTGHOSTENABLED, "false");
	  builder.endSection ();
	  // chiusura del file di configurazione
	  builder.endIni ();
	  return ini;
   }
}
