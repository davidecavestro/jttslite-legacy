/*
 * WIndowManager.java
 *
 * Created on 28 novembre 2005, 22.10
 */

package com.davidecavestro.timekeeper.gui;

import com.davidecavestro.common.application.ApplicationData;
import com.davidecavestro.common.gui.dialog.DialogListener;
import com.davidecavestro.timekeeper.ApplicationContext;
import com.davidecavestro.timekeeper.desktop.DesktopSupport;
import com.davidecavestro.timekeeper.model.Task;
import com.davidecavestro.timekeeper.model.WorkSpace;
import com.davidecavestro.timekeeper.tray.SystemTraySupport;
import com.ost.timekeeper.model.Progress;
import com.ost.timekeeper.model.ProgressItem;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Il gestore delle finestre.
 *
 * @author  davide
 */
public class WindowManager implements ActionListener, DialogListener {

	private ApplicationContext _context;
	
	
	/** Costruttore. */
	public WindowManager () {
	}
	
	/**
	 * Inizializza le risorse necessarie.
	 *
	 */
	public void init (final ApplicationContext context){
		this._context= context;
	}

	private Splash _splash;	
	/**
	 * Ritorna la finestra principale.
	 * @return la finestra principale.
	 * @param appData i dati dell'applicazione.
	 * Sono necessari dato che tipicamente lo Splash viene usato prima
	 * diinizializzare il contesto applicativo.
	 */
	public Splash getSplashWindow (ApplicationData appData){
		if (this._splash==null){
			this._splash = new Splash (appData);
		}
		return this._splash;
	}
	
	private MainWindow _mainWindow;	
	/**
	 * Ritorna la finestra principale.
	 * @return la finestra principale.
	 */
	public MainWindow getMainWindow (){
		synchronized (this) {
			/*
			 * Sccome all'avvio puo' essere chiamata da thread concorrenti va protetta
			 */
			if (this._mainWindow==null){
				this._mainWindow = new MainWindow (this._context);
				this._context.getUIPersisteer ().register (this._mainWindow);
				this._mainWindow.addActionListener (this);
			}
		}
		return this._mainWindow;
	}
	
	private NewPieceOfWorkDialog _mewPOWDialog;	
	/**
	 * Ritorna la dialog di inserimento nuovo avanzamento.
	 * @return la dialog di inserimento nuovo avanzamento.
	 */
	public NewPieceOfWorkDialog getNewPieceOfWorkDialog () {
		synchronized (this) {
		if (_mewPOWDialog==null){
			_mewPOWDialog = new NewPieceOfWorkDialog (_context, getMainWindow (), true);
			_context.getUIPersisteer ().register (_mewPOWDialog);
			_mewPOWDialog.addDialogListener (this);
		}
		}
		return _mewPOWDialog;
	}
	
	/**
	 * Rende visibile la dialog di inserimento nuovo avanzamento.
	 */
	public void showNewPieceOfWorkDialog (final Task parent) {
		getNewPieceOfWorkDialog ().showForTask (parent);
	}
	
	private StartPieceOfWorkDialog _startPOWDialog;	
	/**
	 * Ritorna la dialog di partenza nuovo avanzamento.
	 * @return la dialog di partenza nuovo avanzamento.
	 */
	public StartPieceOfWorkDialog getStartPieceOfWorkDialog () {
		if (_startPOWDialog==null){
			_startPOWDialog = new StartPieceOfWorkDialog (_context, getMainWindow (), true);
			_context.getUIPersisteer ().register (_startPOWDialog);
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
	

	
	private OpenWorkSpaceDialog _openWSDialog;	
	/**
	 * Ritorna la dialog di selezione progetto.
	 * @return la dialog di selezione progetto.
	 */
	public OpenWorkSpaceDialog getOpenWorkSpaceDialog () {
		if (_openWSDialog==null){
			_openWSDialog = new OpenWorkSpaceDialog (_context, getMainWindow (), true);
			_context.getUIPersisteer ().register (_openWSDialog);
			_openWSDialog.addDialogListener (this);
		}
		return _openWSDialog;
	}
	
	public void showOpenWorkSpaceDialog (final WorkSpace initialValue) {
		getOpenWorkSpaceDialog ().showWithSelection (initialValue);
	}
	
	private ReportDialog _reportDialog;	
	/**
	 * Ritorna la dialog di stampa.
	 * @return la dialog di stampa.
	 */
	public ReportDialog getReportDialog () {
		if (_reportDialog==null){
			_reportDialog = new ReportDialog (getMainWindow (), false, _context);
			_context.getUIPersisteer ().register (_reportDialog);
			_reportDialog.addDialogListener (this);
		}
		return _reportDialog;
	}
	
	public void showReportDialog (final Task reportRoot) {
		getReportDialog ().showForRoot (reportRoot);
	}
	
	
	private LogConsole _logConsole;	
	/**
	 * Ritorna la console dilog.
	 * @return la console dilog.
	 */
	public LogConsole getLogConsole (){
		if (this._logConsole==null){
			this._logConsole = new LogConsole (_context);
			this._context.getUIPersisteer ().register (this._logConsole);
		}
		return this._logConsole;
	}
	
	private OptionsDialog _optionsDialog;	
	/**
	 * Ritorna la dialog di impostazione delle opzioni.
	 * 
	 * @return la dialog di impostazione delle opzioni.
	 */
	public OptionsDialog getOptionsDialog (){
		if (this._optionsDialog==null){
			this._optionsDialog = new OptionsDialog (getMainWindow (), true, _context);
		}
		return this._optionsDialog;
	}
	
	private TaskEditDialog _taskEditDialog;	
	/**
	 * Ritorna la dialog di modifica task.
	 * 
	 * @return la dialog di modifica task.
	 */
	public TaskEditDialog showTaskEditDialog (final Task t){
		if (_taskEditDialog==null){
			_taskEditDialog = new TaskEditDialog (_context);
		}
		_taskEditDialog.show (t);
		return _taskEditDialog;
	}
	
	
	private ActionTemplatesDialog _actionTemplatesDialog;	
	/**
	 * Ritorna la dialog di gestione modelli di azione.
	 * 
	 * @return la dialog di gestionemodelli di azione.
	 */
	public ActionTemplatesDialog getActionTemplatesDialog (){
		if (_actionTemplatesDialog==null){
			_actionTemplatesDialog = new ActionTemplatesDialog (_context,getMainWindow (), true);
			_context.getUIPersisteer ().register (_actionTemplatesDialog);
		}
		return _actionTemplatesDialog;
	}
	
	
	
	public void dialogChanged (com.davidecavestro.common.gui.dialog.DialogEvent e) {
		if (e.getSource ()==_mewPOWDialog){
			if (e.getType ()==JOptionPane.OK_OPTION){
				final ProgressItem t = (ProgressItem)_mewPOWDialog.getTask ();
				final Progress p = new Progress (
						_mewPOWDialog.getFromDate (), 
						_mewPOWDialog.getToDate (), 
						t
					);
				p.setDescription (_mewPOWDialog.getDescriptionText ());
				this._context.getModel ().insertPieceOfWorkInto (
					p,
					t, 
					-1
					);
			}
			
		} else if (e.getSource ()==_startPOWDialog){
			if (e.getType ()==JOptionPane.OK_OPTION){
				getMainWindow ().stopAdvancing ();
				_context.getLogger ().debug (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Starting_new_action..."));
				final ProgressItem t = (ProgressItem)_startPOWDialog.getTask ();
				final Progress p = new Progress (
						_startPOWDialog.getFromDate (), 
						null, 
						t
					);
				p.setDescription (_startPOWDialog.getDescriptionText ());
				this._context.getModel ().insertPieceOfWorkInto (
					p,
					t, 
					-1
					);
				_context.getLogger ().debug (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("New_action_started"));
			}
		} else if (e.getSource ()==_openWSDialog){
			if (e.getType ()==JOptionPane.OK_OPTION){
				_context.getLogger ().debug (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Opening_workspace..."));
				
				final WorkSpace ws = (WorkSpace)e.getValue ();
				_context.getModel ().setWorkSpace (ws);
				_context.getUserSettings ().setLastProjectName (ws.getName ());
				
				_context.getLogger ().debug (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Workspace_successfully_opened"));
			}
		} else if (e.getSource ()==_reportDialog){
			if (e.getType ()==JOptionPane.OK_OPTION){
				_context.getLogger ().debug (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Generating_report..."));
				_reportDialog.launchReport ();
				_context.getLogger ().debug (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Report_successfully_generated"));
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
	public ApplicationContext getApplicationContext (){
		return this._context;
	}
	
	private About _about;	
	/**
	 * Ritorna la dialog di inserimento nuova entry, con una inizializzazione lazy.
	 *
	 * @return la dialog di inserimento nuova entry.
	 */
	public About getAbout (){
		if (this._about==null){
			this._about = new About (getMainWindow (), true, _context);
		}
		return this._about;
	}
	
	
	private SystemTraySupport _systemTray;
	
	/**
	 * Ritorna il supporto per la tray icon, con una inizializzazione lazy.
	 *
	 * @return il supporto per la tray icon.
	 */
	public SystemTraySupport getSystemTraySupport () {
		if (_systemTray==null) {
			_systemTray = new SystemTraySupport (_context);
			try {
				_systemTray.register (null, null);
			} catch (final NoClassDefFoundError er) {
				/*
				 * Eccezione silenziata in quanto è previsto che possa accadere, usando una versione di Java anteriore alla 6
				 */
				//er.printStackTrace();
			} catch (final ClassNotFoundException ex) {
				/*
				 * Eccezione silenziata in quanto è previsto che possa accadere, usando una versione di Java anteriore alla 6
				 */
				//ex.printStackTrace();
			}
			
		}
		return _systemTray;
	}
	
	private DesktopSupport _desktop;
	
	/**
	 * Ritorna il supporto per il desktop support, con una inizializzazione lazy.
	 *
	 * @return il supporto per il desktop support.
	 */
	public DesktopSupport getDesktopSupport () {
		if (_desktop==null) {
			_desktop = new DesktopSupport ();
		}
		return _desktop;
	}
	
	
	/**
	 * Varia il look and feel.
	 * 
	 * 
	 * @param propagateToExistingWindows indica se propafare le modifiche alle finestre esistenti.
	 * @param laf il nuovo look and feel.
	 */
	public void setLookAndFeel (final String laf, final boolean propagateToExistingWindows) {
		SwingUtilities.invokeLater (new Runnable () {
				public void run () {
					/*
					 * @workaround invoca in modo asincrono, per evitare dead lock all'avvio (sulla finestra di about)
					 */
					try {
						UIManager.setLookAndFeel (laf);
						if (propagateToExistingWindows) {
							SwingUtilities.updateComponentTreeUI (getAbout ());
							SwingUtilities.updateComponentTreeUI (getMainWindow ());
							SwingUtilities.updateComponentTreeUI (getLogConsole ());
							SwingUtilities.updateComponentTreeUI (getNewPieceOfWorkDialog ());
							SwingUtilities.updateComponentTreeUI (getOpenWorkSpaceDialog ());
							SwingUtilities.updateComponentTreeUI (getOptionsDialog ());
							SwingUtilities.updateComponentTreeUI (getReportDialog ());
						}
					} catch (final Exception e) {
						try {
						_context.getLogger ().error (e, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Cannot_change_look_and_feel_"));
						} catch (Exception ee) {
							/*
							 * Durante l'avvio dell'applicazione il logger potrebbe anche non esistere.
							 */
							e.printStackTrace (System.err);
						}
					}
				}
		});
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
