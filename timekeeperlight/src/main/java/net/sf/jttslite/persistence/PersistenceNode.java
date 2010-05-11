/*
 * PersistenceNode.java
 *
 * Created on December 15, 2006, 12:03 AM
 *
 */

package net.sf.jttslite.persistence;

import net.sf.jttslite.core.model.WorkSpace;
import net.sf.jttslite.core.model.impl.ProgressTemplate;
import net.sf.jttslite.core.model.impl.Project;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;
import net.sf.jttslite.prefs.PreferencesManager;

/**
 * Gestisce l'inizializzazione e l'accesso allo storage per la persistenza dei dati tramite JDPa
 *
 * @author Davide Cavestro
 */
public class PersistenceNode {
	
	/**
	 * Le impostazioni applicative di interesse per l'accesso allo storage..
	 */
	final PreferencesManager _prefsManager;
	
	/**
	 * Il logger.
	 */
	final Logger _logger;
	
	public PersistenceNode (final PreferencesManager prefsManager, final Logger logger) {
		_prefsManager = prefsManager;
		_logger = logger;
	}
	
	/**
	 * Il gestore della persistenza dei dati.
	 */
	private PersistenceManager _pm;
	private Properties _props;
	
	/**
	 * Imposta le properties necessarie all'inizializzazione dello storage.
	 */
	private final void setDatastoreProperties () throws IOException{
		if (_props==null){
			final Properties defaults = new Properties ();
			defaults.load (getClass ().getResourceAsStream ("/net/sf/jttslite/persistence/datanucleus.properties"));
			_props = new Properties (defaults);
		}
		_props.put ("javax.jdo.PersistenceManagerFactoryClass", "org.datanucleus.jdo.JDOPersistenceManagerFactory");
		final StringBuilder connectionURL = new StringBuilder ();
		connectionURL.append ("jdbc:h2:");
		final String storageDirPath = _prefsManager.getStoragePreferences ().getJDOStorageDirPath ();
		if (storageDirPath!=null && storageDirPath.length ()>0){
			connectionURL.append (storageDirPath);
			if (!storageDirPath.endsWith ("/")){
				connectionURL.append ("/");
			}
		}
		/* 
		 * crea il percorsose necessario/possibile
		 */
		new File (_prefsManager.getStoragePreferences ().getJDOStorageDirPath ()).mkdirs ();
		
		connectionURL.append (_prefsManager.getStoragePreferences ().getJDOStorageName ()+";DB_CLOSE_ON_EXIT=TRUE");
		_props.put ("javax.jdo.option.ConnectionURL", connectionURL.toString ());
		_props.put ("javax.jdo.option.ConnectionUserName", _prefsManager.getStoragePreferences ().getJDOUserName ());
		_props.put ("javax.jdo.option.ConnectionPassword", "");

		_props.put ("javax.jdo.option.ConnectionDriverName", "org.h2.Driver");
		_props.put ("javax.jdo.option.Mapping", "h2");
	}
	
	
	private File _datastoreLock;
	/**
	 * Ritorna il file di lock del datastore.
	 * Il file viene sempre ritornato, ma potrebe anche non essere valido (non esiste il file sottostante).
	 */
	private File getDatastoreLock () {
		if (_datastoreLock==null) {
			_datastoreLock = new File (new File (_prefsManager.getStoragePreferences ().getJDOStorageDirPath ()), _prefsManager.getStoragePreferences ().getJDOStorageName ()+".lock");
		}
		return _datastoreLock;
	}
	
	
	/**
	 * Identificatore dell'istanza di applicazione al fine di accedere al db.
	 */
	private final String _dbAccessID = Double.toString (Math.random ());
	
	private final static long MAX_DELAY = 1000*90;
	/**
	 * Acquisisce il lock sul datastore.
	 */
	private void lockDatastore () throws IOException {
		final File lockFile = getDatastoreLock ();
		if (lockFile.exists () && System.currentTimeMillis ()  - lockFile.lastModified () < MAX_DELAY) {
			throw new IOException ("Cannot create a new datastore lock file. Please wait a minute or either manually delete "+lockFile.getPath ()+" (at your own risk).");
		}
		final PrintWriter fw = new PrintWriter (getDatastoreLock ());
		try {
			fw.println (_dbAccessID);
		} finally {
			fw.flush ();
			fw.close ();
		}
		getDatastoreLock ().deleteOnExit ();
		final Timer t = new Timer ("datastore-lock-refresher", true);
		t.schedule (
			new TimerTask () {
				public void run () {
					lockFile.setLastModified (System.currentTimeMillis ());
				}
			}, 1000);
	}
	
	/**
	 * Ritorna <CODE>true</CODE> se il datastore &egrave; acquisito 
	 * dall'istanza corrente dell'applicazione.
	 */
	private boolean isDatastoreAvailable () {
		final BufferedReader br;
		try {
			br = new BufferedReader (new FileReader (getDatastoreLock()));
			try {
				return _dbAccessID.equals (br.readLine ());
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				try {
					br.close ();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			return false;
		} catch (FileNotFoundException ex) {
			return false;
		}
	}
	
	/**
	 * Libera il lock acquisito sul datastore-
	 */
	private void unlockDatastore () {
		getDatastoreLock ().delete ();
	}
	
	/**
	 * Inizializza la gestione della persistenza dei dati.
	 * Imposta il campo interno <CODE>_pm</CODE>.
	 */
	private void initPersistence () throws IOException{
		setDatastoreProperties ();
		final PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory (_props);
		
		_pm = pmf.getPersistenceManager ();
		
		if (_pm.currentTransaction ().isActive ()){
			_pm.currentTransaction ().commit ();
		}
		
		_pm.currentTransaction ().setOptimistic (false);
		_pm.setMultithreaded (true);
	}
	
	/**
	 * Rilascia le risorse per la gestione della persistenza dei dati.
	 */
	private void closePersistence (){
		if (_pm==null){
			return;
		}
		if (_pm.currentTransaction ().isActive ()){
			_pm.currentTransaction ().commit ();
		}
		if (!_pm.isClosed ()){
			_pm.close ();
		}
	}
	
	/**
	 * Inizializza il repository dei dati persistenti con le impostazioni correnti.
	 * Imposta il campo interno <CODE>_pm</CODE>.
	 */
	private void createDataStore () throws IOException{
		if (_pm!=null){
			closePersistence ();
		}
		setDatastoreProperties ();
		_pm = createDataStore (_props);
	}
	
	/**
	 * Ritorna il gestore della persistenza dei dati.
	 * Se non &egrave; stato ancora inizializzato ritorna <CODE>null</CODE>.
	 * 
	 *
	 * @return il gestore della persistenza dei dati, oppure <CODE>null</CODE>. se non &egrave; disponibile.
	 * @see #init()
	 */
	public final PersistenceManager getPersistenceManager (){
		return _pm;
	}
	
	/**
	 * Forza la persistenza delle modifiche.
	 */
	public void flushData (){
		if (_pm==null) {
			return;
		}
		final Transaction tx = _pm.currentTransaction ();
		try {
			if (_pm.currentTransaction ().isActive ()){
				_pm.currentTransaction ().commit ();
			}
		} catch (Exception e){
			e.printStackTrace (System.err);
			tx.rollback ();
		}
	}
	
	/**
	 * Crea un nuovo repository di dati persistenti.
	 * @todo siccome l'INFAME che ha fatto la classe JDOHelper chiama direttamente i metodi
	 * di Hashtable, non posso usare le properties di default.
	 *
	 * @param props l'ambiente di configurazione del data store.
	 * @return il gestore della persistenza per il datastore creato.
	 */
	public static PersistenceManager createDataStore (final Properties props) {
		final Properties properties = (Properties)props.clone ();
		properties.put ("com.sun.jdori.option.ConnectionCreate", "true");
		final PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory (properties);
		final PersistenceManager pm = pmf.getPersistenceManager ();
		final Transaction tx = pm.currentTransaction ();
		
		tx.begin ();
		tx.commit ();
		
		return pm;
	}
	
	public List<WorkSpace> getAvailableWorkSpaces () {
		final List<WorkSpace> data = new ArrayList<WorkSpace> ();
		final PersistenceManager pm = getPersistenceManager ();
		for (final Iterator it = pm.getExtent(Project.class, true).iterator();it.hasNext ();){
			data.add ((Project)it.next());
		}
		return data;
	}
	
	public List<ProgressTemplate> getAvailableTemplates () {
		final List<ProgressTemplate> data = new ArrayList<ProgressTemplate> ();
		final PersistenceManager pm = getPersistenceManager ();
		
		try {
			/*
			 * Evita problemi di class loading
			 */
			Class.forName ("net.sf.jttslite.core.model.impl.ProgressTemplate", true, pm.getClass ().getClassLoader ());
		} catch (final ClassNotFoundException ex) {
			throw new RuntimeException (ex);
		}
		for (final Iterator it = pm.getExtent(ProgressTemplate.class, true).iterator();it.hasNext ();){
			data.add ((ProgressTemplate)it.next());
		}
		return data;
	}
	
	/**
	 * Inizializza il gestore della persistenza dei dati.
	 * Nel caso in cui il sistema non sia configurato correttamente &egrave; possibile che il PersistenceManager non possa essere istanziato.
	 * Se il campo interno <CODE>_pm</CODE> ha un valore diverso da <CODE>null</CODE>, il metodo ritorna subito.
	 */
	public void init () throws PersistenceNodeException {
		if (_pm==null) {
			try {
				try {
					/*
					 * prova ad usare il datastore correntemente configurato.
					 */
					initPersistence ();
					_pm.currentTransaction ().begin ();
					_pm.currentTransaction ().rollback ();
				} catch (final Exception e) {
					/*
					 * prova a creare il datastore alvolo, con le impostazioni correnti.
					 */
					createDataStore ();
				}
			
				lockDatastore ();
				if (!isDatastoreAvailable ()) {
					throw new PersistenceNodeException (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Cannot_initialize_data_storage."));
				}
				
			} catch (final Exception ex) {
				throw new PersistenceNodeException (ex);
			}
		}
	}

}
