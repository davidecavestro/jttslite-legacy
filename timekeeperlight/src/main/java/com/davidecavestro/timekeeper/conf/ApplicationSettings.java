/*
 * ApplicationSettings.java
 *
 * Created on 4 dicembre 2004, 14.15
 */

package com.davidecavestro.timekeeper.conf;

/**
 * Impostazioni configurazbili.
 *
 * @author  davide
 */
public interface ApplicationSettings {
	/**
	 * Ritorna il percorso della directory contenente i file di log.
	 *
	 * @return il percorso della directory contenente i file di log.
	 */
	public String getLogDirPath ();
	
	/**
	 * Ritorna la dimensione del buffer per il logger di testo semplice.
	 *
	 * @return la dimensione del buffer per il logger di testo semplice.
	 */
	public Integer getPlainTextLogBufferSize ();

	/**
	 * Indica se la trayicon è abilitata.
	 * 
	 * @return <tt>true</tt> se la tray iconè abilitata.
	 */
	public Boolean getTrayIconEnabled ();
	

	/**
	 * Indica se l'integrazione con il desktop per l'us odelle helper applications è abilitata.
	 * @return <tt>true/tt> se l'integrazione con il desktop per l'uso delle helper applications è abilitata.
	 */
	public Boolean getHelperApplicationsEnabled ();

        /**
	 * Indica se il L&F di sistema è abilitato
	 * @return <tt>true/tt> se il L&F di sistema è abilitato
	 */
	public Boolean getSystemLookAndFeelEnabled ();

	
	/**
	 * Ritorna il nome dell'ultimo progetto.
	 * @return il nome dell'ultimo progetto.
	 */
	String getLastProjectName ();
	
	/**
	 * Ritorna il nome utente da utilizare per accedere allo storage JDo.
	 * @return il nome utente da utilizare per accedere allo storage JDo.
	 */
	String getJDOUserName ();

	/**
	 * Ritorna il nome dello storage JDo.
	 * 
	 * @return il nome dello storage JDo.
	 */
	String getJDOStorageName ();

	/**
	 * Ritorna il percorso della directory da usare per lo storage JDO.
	 * 
	 * @return il percorso della directory da usare per lo storage JDO.
	 */
	String getJDOStorageDirPath ();

	/**
	 * Ritorna lo stato di abilitazione del ghost per il grafico ad anellosulla tabella degli avanzamenti.
	 *
	 * @return lo stato di abilitazione del ghost per il grafico ad anellosulla tabella degli avanzamenti.
	 */
	Boolean getChartGhostEnabled ();

	/**
	 * Ritorna la profondit&agrave; da usare per il grafico ad anello.
	 *
	 * @return la profondit&agrave; da usare per il grafico ad anello.
	 */
	Integer getChartDepth ();
}
