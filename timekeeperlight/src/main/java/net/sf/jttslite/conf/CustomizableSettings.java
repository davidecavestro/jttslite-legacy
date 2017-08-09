/*
 * CustomizableSettings.java
 *
 * Created on 11 dicembre 2004, 15.02
 */

package net.sf.jttslite.conf;

import java.util.*;

/**
 * Impostazione applicative personalizzabili.
 *
 * @author  davide
 */
public interface CustomizableSettings extends ApplicationSettings {
	
	
	/** Dimensione del buffer per il log di testo semplice. */
	public final static String PLAINTEXTLOG_BUFFERSIZE = "palintextlogbuffersize";
	
	/**
	 * Percorso della directory contenente i file di log.
	 */
	public final static String PROPNAME_LOGDIRPATH = "logdirpath";
	
	/**
	 * Abilitazione del look and feel di sistema.
	 */
	public final static String PROPNAME_LOOKANDFEELENABLED = "systemlookandfeelenabled";
		
	
	/**
	 * Parametro che definisce il nome dell'utente da usare per l'0accesso allo storage JDO.
	 */
	public final static String PROPNAME_JDOSTORAGEUSER_NAME = "jdousername";

	/**
	 * Parametro che definisce il nome dello storage JDO per la persistenza.
	 */
	public final static String PROPNAME_JDOSTORAGE_NAME = "jdostoragename";

	/**
	 * Parametro che definisce il percorso della directory contenente lo storage JDO per la persistenza.
	 */
	public final static String PROPNAME_JDOSTORAGEDIR_PATH = "jdostoragedirpath";
	
	/**
	 * Parametro che definisce il nome dell'ultimo progetto utilizzato.
	 */
	public final static String PROPNAME_LASTPROJECTNAME = "lastprojectname";
	
	/**
	 * Parametro che definisce l'abilitazione del ghost per il grafico ad anello nella tabella degli avanzamenti.
	 */
	public final static String PROPNAME_CHARTGHOSTENABLED = "chartghostenabled";
	
	/**
	 * Parametro che definisce la profondit&agrave; da usare per il grafico ad anello.
	 */
	public final static String PROPNAME_CHARTDEPTH = "chartdepth";
	

	/**
	 * Nome del parametro relativo all'abilitazione della tray icon.
	 */
	public final static String PROPNAME_TRAYICONENABLED = "trayiconenabled";
	/**
	 * Nome del parametro per l'abilitazione delle helper applications.
	 */
	public final static String PROPNAME_HELPERAPPSENABLED = "helperappsenabled";
		
	/**
	 * Ritorna il nome del file di preferenze associato a queste impostazioni.
	 * @return il nome del file di preferenze associato a queste impostazioni.
	 */	
	public String getPropertiesFileName ();
	
	/**
	 * Carica e ritorna le properties a partire dal nome del file associato a queste impostazioni.
	 *
	 * @throws RuntimeException in caso di errori nellapertura del file di risorse.
	 * @return le properties caricate.
	 */	
	public Properties loadProperties () throws RuntimeException;
	
	/**
	 * Salva le properties a partire dal nome del file associato a queste impostazioni.
	 *
	 * @throws RuntimeException in caso di errori nell'apertura del file di risorse.
	 */	
	public void storeProperties () throws RuntimeException;
	
	/**
	 * Ritorna lo header del file di properties di queste impostazioni.
	 * @return lo header del file di properties di queste impostazioni.
	 */	
	public String getPropertiesHeader ();
	
	/**
	 * Ritorna il file contenente le impostazioni applicative persistenti.
	 *
	 * @return il file contenente le impostazioni applicative persistenti.
	 */	
	public Properties getProperties ();
	
	
	/**
	 * Impostala dimensione del buffer per il logger di testo semplice.
	 */
	public void setPlainTextLogBufferSize (final Integer size);
	
	/**
	 * Imposta il LookAndFeel.
	 */
	void setSystemLookAndFeelEnabled (Boolean b);
	
	/**
	 * Imposta il nome utente da utilizare per accedere allo storage JDo.
	 *
	 * @param s il nuovo valore.
	 */
	void setJDOUserName (String s);

	/**
	 * Imposta il nome dello storage JDo.
	 *
	 * @param s il nuovo valore.
	 */
	void setJDOStorageName (String s);

	/**
	 * Imposta il percorso della directory da usare per lo storage JDO.
	 * 
	 * @param s il nuovo valore.
	 */
	void setJDOStorageDirPath (String s);	

	/**
	 * Imposta il nome dell'ultimo project caricato.
	 * 
	 * @param s il nome dell'ultimo project caricato.
	 */
	void setLastProjectName (String s);	
	
	/**
	 * Imposta lo stato di abilitazione del ghost per il grafico ad anellosulla tabella degli avanzamenti.
	 *
	 * @b il valore da impostare.
	 */
	void setChartGhostEnabled (Boolean b);
	
	/**
	 * Imposta la profondit&agrave; da usare per il grafico ad anello.
	 * @param i il valore da impostare.
	 */
	void setCharDepth (Integer i);
	
	/**
	 * Imposta la proprietà di abilitazione della tray icon.
	 * @param b l'abilitazione della tray icon.
	 */
	void setTrayIconEnabled (Boolean b);
	
	/**
	 * Imposta la proprietà di abilitazione delle helper application.
	 * @param b l'abilitazione delle helper application.
	 */
	void setHelperApplicationsEnabled (Boolean b);
	
}
