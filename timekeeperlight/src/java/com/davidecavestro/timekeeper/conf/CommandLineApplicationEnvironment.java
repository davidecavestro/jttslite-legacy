/*
 * CommandLineApplicationEnvironment.java
 *
 * Created on 12 dicembre 2004, 10.14
 */

package com.davidecavestro.timekeeper.conf;

import com.davidecavestro.common.cache.Data;
import com.davidecavestro.common.cache.DisposableData;
import java.util.*;

/**
 * Ambiente di configurazione dell'applicazione impostato dai parametri di lancio.
 *
 * @author  davide
 */
public class CommandLineApplicationEnvironment implements ApplicationEnvironment{
	
	/**
	 * Nome parametro contenente il percorso di installazione dell'applicazione.
	 */
	public final static String APPLICATION_DIRECTORY = "appdir";
	
	/**
	 * Nome parametro contenente il percorso di configurazione dell'applicazione.
	 */
	public final static String APPLICATION_SETTINGS_DIRECTORY = "appconfdir";
	
	/**
	 * I parametri della linea i comando.
	 */
	private String[] _args;
	
	private final Properties _props;
	
	/**
	 *
	 * Costruttore.
	 *
	 * @param args i parametri della linea i comando.
	 */
	public CommandLineApplicationEnvironment (String[] args) {
		_args = args;
		_props = prepareProperties (args);
	}
	
	/**
	 * Entry contenente il percorso di installazione dell'applicazione.
	 */
	private Data _applicationDirPath;
	/**
	 *
	 * @return
	 */	
	public String getApplicationDirPath () {
		if (_applicationDirPath==null || !_applicationDirPath.isValid ()){
			_applicationDirPath = new DisposableData (_props.getProperty (APPLICATION_DIRECTORY));
		}
		return (String)_applicationDirPath.getData ();
	}
	
	/**
	 * Entry contenente il percorso contenente di file di configurazione dell'applicazione.
	 */
	private Data _applicationSettingsPath;
	public String getApplicationSettingsPath () {
		if (_applicationSettingsPath==null || !_applicationSettingsPath.isValid ()){
			_applicationSettingsPath = new DisposableData (_props.getProperty (APPLICATION_SETTINGS_DIRECTORY));
		}
		return (String)_applicationSettingsPath.getData ();
	}
	
	private final static char ARGUMENT_KEYVALUESEPARATOR = '=';
	private final Properties prepareProperties (String[] args){
		final Properties props = new Properties ();
		if (args!=null){
			for (int i=0;i<args.length;i++){
				final String arg = args[i];
				if (arg!=null){
					final int sepIdx = arg.indexOf (ARGUMENT_KEYVALUESEPARATOR);
					if (sepIdx>=0){
						final String key = arg.substring (0, sepIdx);
						String value = null;
						final int argLength = arg.length ();
						final int valueStartIdx = sepIdx+1;
						if (valueStartIdx < argLength) {
							value = arg.substring (valueStartIdx, argLength);
						}
						props.put (key, value);
					}
				}
			}
		}
		return props;
	}
}
