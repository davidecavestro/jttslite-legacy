/*
 * Application.java
 *
 * Created on 26 novembre 2005, 14.55
 */

package net.sf.jttslite;

import net.sf.jttslite.common.application.ApplicationData;
import net.sf.jttslite.common.gui.HungAwtExit;
import net.sf.jttslite.common.gui.persistence.PersistenceStorage;
import net.sf.jttslite.common.gui.persistence.UIPersister;
import net.sf.jttslite.common.help.HelpManager;
import net.sf.jttslite.common.help.HelpResourcesResolver;
import net.sf.jttslite.common.undo.RBUndoManager;
import net.sf.jttslite.common.util.*;
import net.sf.jttslite.conf.ApplicationEnvironment;
import net.sf.jttslite.conf.CommandLineApplicationEnvironment;
import net.sf.jttslite.conf.UserResources;
import net.sf.jttslite.conf.UserSettings;
import net.sf.jttslite.gui.WindowManager;
import net.sf.jttslite.actions.ActionManager;
import net.sf.jttslite.conf.ApplicationOptions;
import net.sf.jttslite.conf.DefaultSettings;
import net.sf.jttslite.gui.Splash;
import net.sf.jttslite.model.DuplicatedWorkSpaceException;
import net.sf.jttslite.model.PersistentPieceOfWorkTemplateModel;
import net.sf.jttslite.model.PersistentTaskTreeModel;
import net.sf.jttslite.model.PersistentWorkSpaceModel;
import net.sf.jttslite.model.TaskTreeModelExceptionHandler;
import net.sf.jttslite.core.model.WorkSpace;
import net.sf.jttslite.model.WorkSpaceModel;
import net.sf.jttslite.model.WorkSpaceModelListener;
import net.sf.jttslite.model.WorkSpaceRemovalControllerImpl;
import net.sf.jttslite.core.model.event.TaskTreeModelEvent;
import net.sf.jttslite.core.model.event.TaskTreeModelListener;
import net.sf.jttslite.model.event.WorkSpaceModelEvent;
import net.sf.jttslite.persistence.PersistenceNode;
import net.sf.jttslite.persistence.PersistenceNodeException;
import net.sf.jttslite.core.model.impl.ProgressItem;
import net.sf.jttslite.core.model.impl.Project;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultStyledDocument;
import net.sf.jttslite.common.log.SwingHandler;

/**
 * Il gestore centrale dell'intera applicazione.
 *
 * @author  davide
 */
public class Application {
	private Logger _logger;

	private final ApplicationEnvironment _env;
	private final ApplicationContext _context;
	private final PersistenceNode _persistenceNode;
	
   /**
    * Costruttore.
    *
    * @param args Ambiente di configurazione dell'applicazione impostato dai parametri di lancio
    */
	public Application (final CommandLineApplicationEnvironment args) {
		_env = args;
				
		final RBUndoManager undoManager = new RBUndoManager ();
		final RBUndoManager atUndoManager = new RBUndoManager ();
		final RBUndoManager wsUndoManager = new RBUndoManager ();

		final Properties releaseProps = new Properties ();
		try {
			/*
			 * carica dati di configurazione.
			 */
			releaseProps.load (getClass ().getResourceAsStream ("release.properties"));
		} catch (final Exception e) {
			System.err.println (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Cannot_load_release_properties"));
		}
		
		final ApplicationData applicationData = new ApplicationData (releaseProps);
		final UserResources userResources = new UserResources (applicationData);
		final UserSettings userSettings = new UserSettings (this, userResources);
		
		final ApplicationOptions applicationOptions = new ApplicationOptions (userSettings, new ApplicationOptions (new DefaultSettings (args, userResources), null));

            /**
             * Imposto il look and feel scelto da preferenze
             */
            try {
                if (userSettings.getSystemLookAndFeelEnabled() != null &&
                        userSettings.getSystemLookAndFeelEnabled().booleanValue()) {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } else {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                }
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
		/**
		 * Percorso del file di configuraizone/mappatura, relativo alla directory di
		 * installazione dell'applicazione.
		 */
		final Properties p = new Properties ();
		try {
			p.load (new FileInputStream (new File (_env.getApplicationDirPath (), "helpmap.properties")));
		} catch (final IOException ioe){
		 System.err.println (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Missing_help_resources_mapping_file"));
		}

		/*
		 * Inizializzazione del sistema di log
		 */
		_logger = Logger.getLogger ("net.sf.jtts");
		try {
		   final File plainTextLogFile = new File (applicationOptions.getLogDirPath (), CalendarUtils.getTimestamp (Calendar.getInstance ().getTime (), CalendarUtils.FILENAME_TIMESTAMP_FORMAT)+".log");
		   if(!plainTextLogFile.exists ()){
			  plainTextLogFile.createNewFile ();
			  _logger.log (Level.INFO, "Created log file");
		   }
		   final FileHandler fh = new FileHandler (plainTextLogFile.getAbsolutePath (),true);
		   fh.setFormatter (new SimpleFormatter ());
			_logger.addHandler (fh);
		} catch (IOException ioe){
		   _logger.log (Level.WARNING, "Connot initializate file log handler", ioe);
		}
		
		final TaskTreeModelExceptionHandler peh = new TaskTreeModelExceptionHandler () {};
		_persistenceNode = new PersistenceNode (applicationOptions, _logger);
		/*
		 * salva le informazioni relative al database nelle impostazioni personali
		 */
		userSettings.setJDOStorageDirPath (applicationOptions.getJDOStorageDirPath ());
		userSettings.setJDOStorageName (applicationOptions.getJDOStorageName ());
		userSettings.setJDOUserName (applicationOptions.getJDOUserName ());
		
		final ProgressItem pi = new ProgressItem (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("New_workspace"));
		final Project prj = new Project (pi.getName (), pi);
		final PersistentTaskTreeModel model = new PersistentTaskTreeModel (_persistenceNode, applicationOptions, _logger, peh, prj);
		model.addTaskTreeModelListener (new TaskTreeModelListener () {
			public void treeNodesChanged (TaskTreeModelEvent e) {
			}
			public void treeNodesInserted (TaskTreeModelEvent e) {
			}
			public void treeNodesRemoved (TaskTreeModelEvent e) {
			}
			public void treeStructureChanged (TaskTreeModelEvent e) {
			}
			public void workSpaceChanged (WorkSpace oldWS, WorkSpace newWS) {
				/*
				 * La variazione del progetto fa rimuovere la coda di undo
				 */
				undoManager.discardAllEdits ();
			}
		});
        final WorkSpaceRemovalControllerImpl wsRemovalController = new WorkSpaceRemovalControllerImpl ();
		final PersistentWorkSpaceModel wsModel = new PersistentWorkSpaceModel (_persistenceNode, applicationOptions, _logger, wsRemovalController);
		final PersistentPieceOfWorkTemplateModel templateModel = new PersistentPieceOfWorkTemplateModel (_persistenceNode, applicationOptions, _logger);
		
		try {
			_persistenceNode.init ();
		} catch (final PersistenceNodeException pne) {
			
			
			final String[] message = {
				"Cannot initialize the persistence subsystem. ",
				"Please check your write permissions to "+applicationOptions.getJDOStorageDirPath ()+".",
				"If you have the correct permissions, please wait a couple of minutes, so that the current lock becomes obsolete."
				};
			JOptionPane.showMessageDialog (null, message, "", JOptionPane.ERROR_MESSAGE);
			exit ();
			/*
			 * Exit dovrebbe terminare la JVM, se non fosse, comunque l'applicazione non deve partire
			 */
			throw new RuntimeException (pne);
		}
		wsModel.init () ;
		
		templateModel.init ();
		
		_context = new ApplicationContext (
			_env,
			applicationOptions,
			new WindowManager (),
			new UIPersister (new UserUIStorage (userSettings)),
			_logger,
			userSettings,
			applicationData,
			model,
			wsModel,
			templateModel,
			undoManager,
			atUndoManager,
			wsUndoManager,
			new ActionManager (),
			new HelpManager (new HelpResourcesResolver (p), "help-contents/JTTSlite.hs"),
			peh,
			_persistenceNode
			);
		
		model.addUndoableEditListener (undoManager);
		templateModel.addUndoableEditListener (atUndoManager);
		/*
		 * Assicura la persistenza delle informazioni di configurazione del DB
		 */
		userSettings.setJDOStorageDirPath (applicationOptions.getJDOStorageDirPath ());
		userSettings.setJDOStorageName (applicationOptions.getJDOStorageName ());
		userSettings.setJDOUserName (applicationOptions.getJDOUserName ());
        
        wsRemovalController.setContext (_context);
	}
	
	
	/**
	 * Fa partire l'applicazione.
	 * @throws java.awt.HeadlessException se l'ambiente grafico non è supportato.
	 */
	public void start () throws HeadlessException {
		_context.getLogger ().info (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("starting_UI"));
		final WindowManager wm = _context.getWindowManager ();
		
		wm.init (_context);
		
		final Splash splash = wm.getSplashWindow (_context.getApplicationData ());
		splash.setVisible (true);
		try {
			splash.showInfo (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Initializing_context..."));
			splash.showInfo (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Initializing_log_console..."));
			final SwingHandler cl = new SwingHandler (new DefaultStyledDocument ());
			_context.getWindowManager ().getLogConsole ().init (cl.getDocument ());
			_logger.addHandler (cl);
			splash.showInfo (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Preparing_main_window..."));
			wm.getMainWindow ().addWindowListener (
				new java.awt.event.WindowAdapter () {
				@Override
				public void windowClosing (final java.awt.event.WindowEvent evt) {
					processExitRequest (evt);
				}
			});
			
			_context.getModel ().setWorkSpace (prepareWorkSpace ());

            /*
             * crea al volo un progetto qualora vengano rimossi tutti
             */
            _context.getWorkSpaceModel ().addWorkSpaceModelListener (new WorkSpaceModelListener () {

                public void intervalAdded (WorkSpaceModelEvent e) {

                }

                public void intervalRemoved (WorkSpaceModelEvent e) {
                    if (_context.getWorkSpaceModel ().isEmpty ()){
                        createNewWorkSpace ();
                    }
                }

                public void contentsChanged (WorkSpaceModelEvent e) {

                }
            });


		
		} finally {
			splash.setVisible (false);
			splash.dispose ();
		}
		
		wm.getMainWindow ().show ();
		_context.getLogger ().info (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("UI_successfully_started"));
	}
	
	public Logger getLogger (){
		return _logger;
	}
	
	/**
	 * Operazioni da effettuare prima dell'uscita dall'applicazione, tra le quali:
	 *	<UL>
	 *		<LI>Salvataggio impostazioni utente.
	 *	</UL>
	 */
	public void beforeExit (){
		if (_persistenceNode!=null) {
			_persistenceNode.flushData ();
		}
		
		if (_context!=null) {
			_context.getUIPersister ().makePersistentAll ();
		}
		
		/* Salva le preferenze utente */
		if (_context!=null) {
			_context.getUserSettings ().storeProperties ();
		}
	}
	
	private WorkSpace prepareWorkSpace () {
		
		
		
		final WorkSpace lastWorkSpace = findWorkSpace (_context.getApplicationOptions ().getLastProjectName ());
		if (lastWorkSpace!=null) {
			/*
			 * ritorna l'ultimo usato
			 */
			return lastWorkSpace;
		}
		
		final WorkSpaceModel wsm = _context.getWorkSpaceModel ();
		if (wsm.getSize ()>0) {
			/*
			 * ritorna il primo workspace disponibile
			 */
			return wsm.getElementAt (0);
		}
		
		/*
		 * Crea un nuovo workspace
		 */
		final WorkSpace newWS = createNewWorkSpace ();
		
		return newWS;
	}
	
	private WorkSpace createNewWorkSpace () {
		_context.getLogger ().info (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Creating_a_new_empty_workspace..."));
		/*
		 * Crea nuovo progetto.
		 */
		final ProgressItem pi = new ProgressItem (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("New_workspace"));
		pi.insert (new ProgressItem (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("New_task")));
		final WorkSpace ws = new Project (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("New_workspace"), pi);
		try {
			_context.getWorkSpaceModel ().addElement (ws);
		} catch (final DuplicatedWorkSpaceException ex) {
			throw new RuntimeException (ex);
		}
		
		_context.getLogger ().info (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Empty_workspace_created."));
		return ws;
	}
	
	private WorkSpace findWorkSpace (String name) {
		if (name!=null) {
			for (final Iterator<WorkSpace> it =_context.getWorkSpaceModel ().iterator (); it.hasNext ();) {
				final WorkSpace p = it.next ();
				if (name.equals (p.getName ())) {
					return p;
				}
			}
		}
		return null;
	}
	
	/**
	 * Termina l'applicazione.
	 *
	 * @param evt 
	 */
	public final void processExitRequest (java.awt.event.WindowEvent evt){
		if (!_context.getWindowManager ().getMainWindow ().canExit ()) {
			return;
		}
		exit ();
	}
	private void exit () {
		beforeExit ();
		_context.getWindowManager ().disposeAllFrames ();
		HungAwtExit.forceOtherFramesDispose (_context.getWindowManager ().getMainWindow ());
	}
	
	private final class UserUIStorage implements PersistenceStorage {
		private final UserSettings _userSettings;
		public UserUIStorage (final UserSettings userSettings){
			_userSettings = userSettings;
		}
		
		public java.util.Properties getRegistry () {
			return _userSettings.getProperties ();
		}
		
	}
	
	
	/**
	 * Porta la finestra principale dell'applicazione inprimo piano.
	 */
	public void bringToFront () {
		_context.getWindowManager ().getMainWindow ().bringToFront ();		
	}

	
}
