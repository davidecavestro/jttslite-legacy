/*
 * ReportPreferences.java
 *
 * Created on 30 gennaio 2005, 20.59
 */

package net.sf.jttslite.report;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Preferenze relative alla generazione del report.
 *
 * @author  davide
 */
public final class ReportPreferences {
	/**
	 * Il file contenente il report.
	 */
	private File _output;
	
	/**
	 * La mappa dei parametri da passare al report.
	 */
	private final Map<String, Object> _params = new HashMap<String, Object> ();
	
	/**
	 * Costruttore.
	 *
	 * @param output il report generato.
	 */
	public ReportPreferences (final File output) {
		this._output = output;
	}
	
	/**
	 * COstruttore con parametri dilancio del report.
	 * 
	 * @param output il report da generare.
	 * @param params i parametri da utilizzare per il lancio.
	 */
	public ReportPreferences (final File output, final Map<String, Object> params) {
		this._output = output;
		_params.putAll (params);
	}
	
	/**
	 * Ritorna il file contenente il report.
	 *
	 * @return il risultato del report.
	 */	
	public File getOutput (){
		return this._output;
	}
	
	/**
	 * Ritorna i parametri da utilizzare per il lancio del report.
	 * @return i parametri da utilizzare per il lancio del report.
	 */
	public Map<String, Object> getParams () {
		return _params;
	}
	
	/**
	 * Imposta il parametro specificato.
	 * @param name nome del parametro.
	 * @param value valore del parametro.
	 */
	public void addParameter (final String name, final Object value) {
		_params.put (name, value);
	}
	
	/**
	 * Ritorna una rappresentazione in formato stringa di queste preferenze.
	 *
	 * @return una stringa che rappresenta queste preferenze.
	 */	
	public String toString (){
		final StringBuilder sb = new StringBuilder ();
		sb.append (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("_output:_")).append (this._output);
		return sb.toString ();
	}
}
