/*
 * ApplicationOptions.java
 *
 * Created on 4 dicembre 2004, 14.12
 */

package net.sf.jttslite.conf;

/**
 * Opzioni di configurazione dell'applicazione. E' possibile implementare una catena
 * di responsabilita', innestando diversi oggetti di questo tipo.
 *
 * @author  davide
 */
public final class ApplicationOptions {
	
	/**
	 * Le impostazioni.
	 */
	private ApplicationSettings _settings;
	
	/**
	 * L'anello successore nella catena di responsabilita''.
	 */
	private ApplicationOptions _successor;
	
	/**
	 *
	 * Costruttore privato, evita istanzazione dall'esterno.
	 *
	 * @param settings le impostazioni.
	 * @param successor l'anello successore nella catena di responsabilita''.
	 */
	public ApplicationOptions (ApplicationSettings settings, ApplicationOptions successor) {
		_settings = settings;
		_successor = successor;
	}
	
	
	public String getLogDirPath (){
		final String returnValue = _settings.getLogDirPath ();
		if (returnValue!=null){
				/*
				 * Risposta locale.
				 */
			return returnValue;
		} else {
			if (_successor!=null){
					/*
					 * Delega successore.
					 */
				return _successor.getLogDirPath ();
			} else {
					/*
					 * Informazione non disponibile.
					 */
				return null;
			}
		}
	}
	

	/**
	 * Ritorna la dimensione del buffer per il logger di testo semplice.
	 *
	 * @return la dimensione del buffer per il logger di testo semplice.
	 */
	public int getPlainTextLogBufferSize (){
		final Integer returnValue = _settings.getPlainTextLogBufferSize ();
		if (returnValue!=null){
			/*
			 * Risposta locale.
			 */
			return returnValue.intValue ();
		} else {
			if (_successor!=null){
				/*
				 * Delega successore.
				 */
				return _successor.getPlainTextLogBufferSize ();
			} else {
				/*
				 * Informazione non disponibile.
				 * Funzionalit� disabilita'ta.
				 */
				return 8192;
			}
		}
	}
	
	/**
	 * Ritorna il percorso per il database.
	 *
	 * @return il percorso per il database.
	 */	
	public String getJDOStorageDirPath (){
		final String returnValue = _settings.getJDOStorageDirPath ();
		if (returnValue!=null){
				/*
				 * Risposta locale.
				 */
			return returnValue;
		} else {
			if (_successor!=null){
					/*
					 * Delega successore.
					 */
				return _successor.getJDOStorageDirPath ();
			} else {
				/*
				 * Informazione non disponibile
				 */
				return null;
			}
		}
	}

	/**
	 * Ritorna il nome del db.
	 *
	 * @return il nome del db.
	 */	
	public String getJDOStorageName (){
		final String returnValue = _settings.getJDOStorageName ();
		if (returnValue!=null){
				/*
				 * Risposta locale.
				 */
			return returnValue;
		} else {
			if (_successor!=null){
					/*
					 * Delega successore.
					 */
				return _successor.getJDOStorageName ();
			} else {
				/*
				 * Informazione non disponibile
				 */
				return null;
			}
		}
	}
	
	/**
	 * Ritorna il nome del db.
	 *
	 * @return il nome del db.
	 */	
	public String getJDOUserName (){
		final String returnValue = _settings.getJDOUserName ();
		if (returnValue!=null){
				/*
				 * Risposta locale.
				 */
			return returnValue;
		} else {
			if (_successor!=null){
					/*
					 * Delega successore.
					 */
				return _successor.getJDOUserName ();
			} else {
				/*
				 * Informazione non disponibile
				 */
				return null;
			}
		}
	}
	
	/**
	 * Ritorna il nome dell'ultimo progettoutilizzato.
	 *
	 * @return il nome dell'ultimo progettoutilizzato.
	 */	
	public String getLastProjectName (){
		final String returnValue = _settings.getLastProjectName ();
		if (returnValue!=null){
				/*
				 * Risposta locale.
				 */
			return returnValue;
		} else {
			if (_successor!=null){
					/*
					 * Delega successore.
					 */
				return _successor.getLastProjectName ();
			} else {
				/*
				 * Informazione non disponibile
				 */
				return null;
			}
		}
	}
	
	/**
	 * Ritorna <CODE>true</CODE> se &egrave; abilitatoil ghost del grafico sulla tabella degli avanzamenti.
	 *
	 * @return <CODE>true</CODE> se &egrave; abilitatoil ghost del grafico.
	 */	
	public boolean isChartGhostEnabled () {
		final Boolean returnValue = _settings.getChartGhostEnabled ();
		if (returnValue!=null){
				/*
				 * Risposta locale.
				 */
			return returnValue.booleanValue ();
		} else {
			if (_successor!=null){
					/*
					 * Delega successore.
					 */
				return _successor.isChartGhostEnabled ();
			} else {
				/*
				 * Default diSistema
				 */
				return DefaultSettings.isChartGhostEnabled ();
			}
		}
	}
	
	/**
	 * Ritorna la profondit&agrave; da usare per il grafico ad anello.
	 *
	 * @return la profondit&agrave; da usare per il grafico ad anello.
	 */
	public int getChartDepth (){
		final Integer returnValue = _settings.getChartDepth ();
		if (returnValue!=null){
			/*
			 * Risposta locale.
			 */
			return returnValue.intValue ();
		} else {
			if (_successor!=null){
				/*
				 * Delega successore.
				 */
				return _successor.getChartDepth ();
			} else {
				/*
				 * Informazione non disponibile.
				 * Funzionalit� disabilita'ta.
				 */
				return DefaultSettings.defaultChartDepth ();
			}
		}
	}

	/**
	 * Indica se l'integrazione con il desktop per la visualizzazione della trayicon è abilitata.
	 * @return <tt>true/tt> se l'integrazione con il desktop per la visualizzazione della trayicon è abilitata.
	 */
	public boolean isTrayIconEnabled () {
		final Boolean returnValue = _settings.getTrayIconEnabled ();
		if (returnValue!=null){
				/*
				 * Risposta locale.
				 */
			return returnValue.booleanValue ();
		} else {
			if (_successor!=null){
					/*
					 * Delega successore.
					 */
				return _successor.isTrayIconEnabled ();
			} else {
				/*
				 * Default diSistema
				 */
				return DefaultSettings.isTrayIconEnabled ();
			}
		}
	}
	
	/**
	 * Indica se l'integrazione con il desktop per l'utilizzo delle helper application (per aprire i file) è abilitata.
	 * @return <tt>true/tt> se l'integrazione con il desktop per l'utilizzo delle helper application (per aprire i file) è abilitata.
	 */
	public boolean isHelperApplicationIntegrationEnabled () {
		final Boolean returnValue = _settings.getHelperApplicationsEnabled ();
		if (returnValue!=null){
				/*
				 * Risposta locale.
				 */
			return returnValue.booleanValue ();
		} else {
			if (_successor!=null){
					/*
					 * Delega successore.
					 */
				return _successor.isHelperApplicationIntegrationEnabled ();
			} else {
				/*
				 * Default diSistema
				 */
				return DefaultSettings.isHelperApplicationIntegrationEnabled ();
			}
		}
	}

        	/**
	 * Indica se il L&F di sistema è abilitato.
	 * @return <tt>true/tt> se il L&F di sistema è abilitato.
	 */
	public boolean isSystemLookAndFeelEnabled () {
		final Boolean returnValue = _settings.getSystemLookAndFeelEnabled();
		if (returnValue!=null){
				/*
				 * Risposta locale.
				 */
			return returnValue.booleanValue ();
		} else {
			if (_successor!=null){
					/*
					 * Delega successore.
					 */
				return _successor.isSystemLookAndFeelEnabled();
			} else {
				/*
				 * Default diSistema
				 */
				return DefaultSettings.isSystemLookAndFeelEnabled();
			}
		}
	}
}