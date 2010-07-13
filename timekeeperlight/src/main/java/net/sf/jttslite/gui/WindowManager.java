/*
 * WIndowManager.java
 *
 * Created on 28 novembre 2005, 22.10
 */
package net.sf.jttslite.gui;

import net.sf.jttslite.common.application.ApplicationData;
import net.sf.jttslite.common.gui.dialog.DialogListener;
import net.sf.jttslite.common.util.file.CustomFileFilter;
import net.sf.jttslite.common.util.file.FileUtils;
import net.sf.jttslite.ApplicationContext;
import net.sf.jttslite.desktop.DesktopSupport;
import net.sf.jttslite.core.model.Task;
import net.sf.jttslite.core.model.WorkSpace;
import net.sf.jttslite.tray.SystemTraySupport;
import net.sf.jttslite.core.model.impl.Progress;
import net.sf.jttslite.core.model.impl.ProgressItem;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.sf.jttslite.common.gui.dialog.DialogEvent;

/**
 * Il gestore delle finestre.
 *
 * @author  davide
 */
public class WindowManager implements ActionListener, DialogListener {

	private ApplicationContext _context;
	private SystemTraySupport _systemTray;
	private Splash _splash;
	private MainWindow _mainWindow;
	private NewPieceOfWorkDialog _mewPOWDialog;
	private NewWorkspaceDialog newWorkspaceDialog;
	private StartPieceOfWorkDialog _startPOWDialog;
	private OpenWorkSpaceDialog _openWSDialog;
	private OptionsDialog _optionsDialog;
	private ReportDialog _reportDialog;
	private TaskEditDialog _taskEditDialog;
	private WorkspacesDialog _workspacesDialog;
	private DesktopSupport _desktop;
	private JFileChooser xmlFileChooser;
	private LogConsole _logConsole;
	private About _about;
        
	/** 
	 * Crea una nuova istanza.
	 */
	public WindowManager () {
	}

	/**
	 * Inizializza le risorse necessarie.
	 *
	 */
	public void init (final ApplicationContext context) {
		this._context = context;
	}

	/**
	 * Restituisce la finestra principale.
	 * @return la finestra principale.
	 * @param appData i dati dell'applicazione.
	 * Sono necessari dato che tipicamente lo Splash viene usato prima
	 * diinizializzare il contesto applicativo.
	 */
	public Splash getSplashWindow (ApplicationData appData) {
		if (_splash == null) {
			_splash = new Splash (appData);
		}
		return _splash;
	}
	
	/**
	 * Restituisce la finestra principale.
	 * @return la finestra principale.
	 */
	public MainWindow getMainWindow () {
		synchronized (this) {
			/*
			 * Sccome all'avvio puo' essere chiamata da thread concorrenti va protetta
			 */
			if (this._mainWindow == null) {
			   this._mainWindow = new MainWindow (this._context);
                            this._mainWindow.addActionListener (this);
                            _context.getPreferenceManager ().getGuiPreferences ().register (_mainWindow);
			}
		}
		return _mainWindow;
	}

	/**
	 * Restituisce la dialog di inserimento nuovo avanzamento.
	 * @return la dialog di inserimento nuovo avanzamento.
	 */
	public NewPieceOfWorkDialog getNewPieceOfWorkDialog () {
		synchronized (this) {
			if (_mewPOWDialog == null) {
				_mewPOWDialog = new NewPieceOfWorkDialog (_context, getMainWindow (), true);
				_context.getPreferenceManager ().getGuiPreferences ().register (_mewPOWDialog);
				_mewPOWDialog.addDialogListener (this);
			}
		}
		return _mewPOWDialog;
	}
	
	/**
	 * Restituisce il riferimento alla finestra di dialogo per la creazine di un nuovo workspace.
	 * @return la finestra di dialogo per la creazine di un nuovo workspace.
	 */
	public NewWorkspaceDialog getNewWorkspaceDialog () {
		synchronized (this) {
			if (newWorkspaceDialog == null) {
				newWorkspaceDialog = new NewWorkspaceDialog (_context, getMainWindow (), true);
				_context.getPreferenceManager ().getGuiPreferences ().register (newWorkspaceDialog);
				newWorkspaceDialog.addDialogListener (this);
			}
		}
		return newWorkspaceDialog;
	}

	public void showNewWorkspaceDialog () {
		getNewWorkspaceDialog ().setVisible (true);
	}
	
	/**
	 * Restituisce la dialog di partenza nuovo avanzamento.
	 * @return la dialog di partenza nuovo avanzamento.
	 */
	public StartPieceOfWorkDialog getStartPieceOfWorkDialog () {
		if (_startPOWDialog == null) {
			_startPOWDialog = new StartPieceOfWorkDialog (_context, getMainWindow (), true);
			_context.getPreferenceManager ().getGuiPreferences ().register (_startPOWDialog);
			_startPOWDialog.addDialogListener (this);
		}
		return _startPOWDialog;
	}

	/**
	 * Rende visibile la dialog di partenza nuovo avanzamento.
	 */
	public void showStartPieceOfWorkDialog (final Task parent) {
		getStartPieceOfWorkDialog ().showForTask (parent);
	}

	/**
	 * Restituisce la dialog di selezione progetto.
	 * @return la dialog di selezione progetto.
	 */
	public OpenWorkSpaceDialog getOpenWorkSpaceDialog () {
		if (_openWSDialog == null) {
			_openWSDialog = new OpenWorkSpaceDialog (_context, getMainWindow (), true);
			_context.getPreferenceManager ().getGuiPreferences ().register (_openWSDialog);
			_openWSDialog.addDialogListener (this);
		}
		return _openWSDialog;
	}

	public void showOpenWorkSpaceDialog (final WorkSpace initialValue) {
		getOpenWorkSpaceDialog ().showWithSelection (initialValue);
	}
	
	/**
	 * Restituisce la dialog di stampa.
	 * @return la dialog di stampa.
	 */
	public ReportDialog getReportDialog () {
		if (_reportDialog == null) {
			_reportDialog = new ReportDialog (getMainWindow (), false, _context);
			_context.getPreferenceManager ().getGuiPreferences ().register (_reportDialog);
			_reportDialog.addDialogListener (this);
		}
		return _reportDialog;
	}

	public void showReportDialog (final Task reportRoot) {
		getReportDialog ().showForRoot (reportRoot);
	}
	
	/**
	 * Restituisce la console dialog.
	 * @return la console dialog.
	 */
	public LogConsole getLogConsole () {
		if (_logConsole == null) {
			_logConsole = new LogConsole (_context);
			_context.getPreferenceManager ().getGuiPreferences ().register (this._logConsole);
		}
		return _logConsole;
	}

	/**
	 * Restituisce la dialog di impostazione delle opzioni.
	 * 
	 * @return la dialog di impostazione delle opzioni.
	 */
	public OptionsDialog getOptionsDialog () {
		if (_optionsDialog == null) {
			_optionsDialog = new OptionsDialog (getMainWindow (), true, _context);
		}
		return _optionsDialog;
	}
	
	/**
	 * Restituisce la dialog di modifica task.
	 * 
	 * @return la dialog di modifica task.
	 */
	public TaskEditDialog showTaskEditDialog (final Task t) {
		if (_taskEditDialog == null) {
			_taskEditDialog = new TaskEditDialog (_context);
		}
		_taskEditDialog.show (t);
		return _taskEditDialog;
	}

	private ActionTemplatesDialog _actionTemplatesDialog;
	/**
	 * Restituisce la dialog di gestione modelli di azione.
	 * 
	 * @return la dialog di gestione modelli di azione.
	 */
	public ActionTemplatesDialog getActionTemplatesDialog () {
		if (_actionTemplatesDialog == null) {
			_actionTemplatesDialog = new ActionTemplatesDialog (_context, getMainWindow (), true);
			_context.getPreferenceManager ().getGuiPreferences ().register (_actionTemplatesDialog);
		}
		return _actionTemplatesDialog;
	}
	
	/**
	 * Restituisce la dialog di gestione dei progetti.
	 * 
	 * @return la dialog di gestione dei progetti.
	 */
	public WorkspacesDialog getWorkspacesDialog () {
		if (_workspacesDialog == null) {
			_workspacesDialog = new WorkspacesDialog (_context, getMainWindow (), true);
			_context.getPreferenceManager ().getGuiPreferences ().register (_workspacesDialog);
		}
		return _workspacesDialog;
	}

	public void dialogChanged (final DialogEvent e) {
		if (e.getSource () == _mewPOWDialog) {
			if (e.getType () == JOptionPane.OK_OPTION) {
				final ProgressItem t = (ProgressItem) _mewPOWDialog.getTask ();
				final Progress p = new Progress (
					_mewPOWDialog.getFromDate (),
					_mewPOWDialog.getToDate (),
					t);
				p.setDescription (_mewPOWDialog.getDescriptionText ());
				this._context.getModel ().insertPieceOfWorkInto (
					p,
					t,
					-1);
			}

		} else if (e.getSource () == _startPOWDialog) {
			if (e.getType () == JOptionPane.OK_OPTION) {
				getMainWindow ().stopAdvancing ();
				_context.getLogger ().log (Level.INFO,java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("Starting_new_action..."));
				final ProgressItem t = (ProgressItem) _startPOWDialog.getTask ();
				final Progress p = new Progress (
					_startPOWDialog.getFromDate (),
					null,
					t);
				p.setDescription (_startPOWDialog.getDescriptionText ());
				_context.getModel ().insertPieceOfWorkInto (
					p,
					t,
					-1);
				_context.getLogger ().log (Level.INFO,java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("New_action_started"));
			}
		} else if (e.getSource () == _reportDialog) {
			if (e.getType () == JOptionPane.OK_OPTION) {
				_context.getLogger ().log (Level.INFO,java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("Generating_report..."));
				_reportDialog.launchReport ();
				_context.getLogger ().log (Level.INFO,java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("Report_successfully_generated"));
			}
		}
	}

	/**
	 * Gestione dell'evento su azioni su cui questo manager &egrave; registrato come listener.
	 * <P>
	 *Comandi di azione gestiti:
	 *<UL>
	 *</UL>
	 */
	public void actionPerformed (java.awt.event.ActionEvent e) {
	}

	/**
	 * Espone il contesto applicativo, ad uso degli oggetti che hanno solamente un riferimento a questo manager.
	 *
	 */
	public ApplicationContext getApplicationContext () {
		return this._context;
	}

	
	/**
	 * Ritorna la dialog di inserimento nuova entry, con una inizializzazione lazy.
	 *
	 * @return la dialog di inserimento nuova entry.
	 */
	public About getAbout () {
		if (this._about == null) {
			this._about = new About (getMainWindow (), true, _context);
		}
		return this._about;
	}

	
    /**
     * Ritorna il supporto per la tray icon, con una inizializzazione lazy.
     *
     * @return il supporto per la tray icon.
     */
    public SystemTraySupport getSystemTraySupport() {
        if (_systemTray == null) {
            _systemTray = new SystemTraySupport(_context);
            _systemTray.register(null, null);
        }
        return _systemTray;
    }

	/**
	 * Ritorna il supporto per il desktop support, con una inizializzazione lazy.
	 *
	 * @return il supporto per il desktop support.
	 */
	public DesktopSupport getDesktopSupport () {
		if (_desktop == null) {
			_desktop = new DesktopSupport ();
		}
		return _desktop;
	}
	
	/**
	 * Restituisce la dialog per la selezione di fle XML.
	 *
	 * @return la dialog per la selezione di fle XML.
	 */
	public JFileChooser getXMLFileChooser () {
		if (xmlFileChooser == null) {
			xmlFileChooser = new JFileChooser (new File (System.getProperty ("user.dir")));

			xmlFileChooser.setFileHidingEnabled (false);
			xmlFileChooser.addChoosableFileFilter (new CustomFileFilter (
				new String[]{FileUtils.xml},
				new String[]{"XML files"}));
		}
		return xmlFileChooser;
	}

	/**
	 * Chiude e rilascia tutte le finestre.
	 */
	public void disposeAllFrames () {
		/*
		 * Tutti i figli di MainWindow vengono chiusi in automatico
		 */
		getMainWindow ().setVisible (false);
		getMainWindow ().dispose ();
		getLogConsole ().setVisible (false);
		getLogConsole ().dispose ();
		getSystemTraySupport ().release ();
	}
}
