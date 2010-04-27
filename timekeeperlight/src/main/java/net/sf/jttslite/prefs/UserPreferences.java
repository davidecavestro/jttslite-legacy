/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jttslite.prefs;

import java.util.prefs.Preferences;

/**
 *
 * @author g-caliendo
 */
public class UserPreferences {

   /** Identificativo della sezione di impostazioni utente */
   public static final String SECTION_OPTIONS = "OPTIONS";
   /** Abilitazione del look and feel di sistema. */
   public final static String PROPNAME_LOOKANDFEELENABLED = "systemlookandfeelenabled";
   /** Parametro che definisce l'abilitazione del ghost per il grafico ad anello nella tabella degli avanzamenti. */
   public final static String PROPNAME_CHARTGHOSTENABLED = "chartghostenabled";
   /** Parametro che definisce la profondit&agrave; da usare per il grafico ad anello. */
   public final static String PROPNAME_CHARTDEPTH = "chartdepth";
   /** Nome del parametro relativo all'abilitazione della tray icon. */
   public final static String PROPNAME_TRAYICONENABLED = "trayiconenabled";
   /** Nome del parametro per l'abilitazione delle helper applications. */
   public final static String PROPNAME_HELPERAPPSENABLED = "helperappsenabled";
   /** Percorso della directory contenente i file di log. */
   public final static String PROPNAME_LOGDIRPATH = "logdirpath";
   /** Nome directory contenente i log dell'applicazione */
   public final static String DEF_LOG_DIR = "/logs";
   /** Parametro che definisce il nome dell'ultimo progetto utilizzato. */
   public final static String PROPNAME_LASTPROJECTNAME = "lastprojectname";

   private PreferencesManager manager;
   private Preferences preferences;

   public UserPreferences(PreferencesManager manager){
	  this.manager = manager;
	  preferences = manager.getMainPreferences ().node (SECTION_OPTIONS);
   }

   	public void setChartGhostEnabled (final boolean b) {
	   preferences.putBoolean (PROPNAME_CHARTGHOSTENABLED, b);
	}

	public boolean getChartGhostEnabled () {
	   return preferences.getBoolean (PROPNAME_CHARTGHOSTENABLED, false);
	}

	public int getChartDepth () {
	   return preferences.getInt (PROPNAME_CHARTDEPTH, 5);
	}

	public void setCharDepth (final int i) {
	   preferences.putInt (PROPNAME_CHARTDEPTH, i);
	}

	public boolean getTrayIconEnabled () {
	  return preferences.getBoolean (PROPNAME_TRAYICONENABLED, true);
	}

	public void setTrayIconEnabled (final boolean b) {
		preferences.putBoolean (PROPNAME_TRAYICONENABLED, b);
	}

	public boolean getHelperApplicationsEnabled () {
	  return  preferences.getBoolean (PROPNAME_HELPERAPPSENABLED, true);
	}

	public void setHelperApplicationsEnabled (final boolean b) {
		preferences.putBoolean (PROPNAME_HELPERAPPSENABLED, b);
	}

	public boolean getSystemLookAndFeelEnabled () {
	   return preferences.getBoolean (PROPNAME_LOOKANDFEELENABLED, false);
	}

	public void setSystemLookAndFeelEnabled (final boolean b) {
	   preferences.putBoolean (PROPNAME_LOOKANDFEELENABLED, b);
	}

   public String getLogDirPath (){
	  return preferences.get (PROPNAME_LOGDIRPATH, manager.getUserApplicationSettingsDirPath () + DEF_LOG_DIR);
	}

	public void setLastProjectName (final String s) {
	   preferences.put (PROPNAME_LASTPROJECTNAME, s);
	}

	public String getLastProjectName(){
	   return preferences.get (PROPNAME_LASTPROJECTNAME, null);
	}
}
