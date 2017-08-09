/*
 * MainWIndow.java
 *
 * Created on 26 novembre 2005, 14.56
 */

package net.sf.jttslite.gui;

import net.sf.jttslite.model.DuplicatedWorkSpaceException;
import net.sf.jttslite.core.util.DurationUtils;
import net.sf.jttslite.common.gui.CompositeIcon;
import net.sf.jttslite.common.gui.GUIUtils;
import net.sf.jttslite.common.gui.SwingWorker;
import net.sf.jttslite.common.gui.VTextIcon;
import net.sf.jttslite.common.gui.persistence.PersistenceUtils;
import net.sf.jttslite.common.gui.persistence.PersistentComponent;
import net.sf.jttslite.common.util.CalendarUtils;
import net.sf.jttslite.common.util.IllegalOperationException;
import net.sf.jttslite.common.util.action.ActionNotifier;
import net.sf.jttslite.common.util.action.ActionNotifierImpl;
import net.sf.jttslite.common.util.file.CustomFileFilter;
import net.sf.jttslite.common.util.file.FileUtils;
import net.sf.jttslite.ApplicationContext;
import net.sf.jttslite.backup.XMLAgent;
import net.sf.jttslite.backup.XMLImporter;
import net.sf.jttslite.gui.dnd.DataFlavors;
import net.sf.jttslite.gui.dnd.ProgressItemTransferHandler;
import net.sf.jttslite.gui.dnd.ProgressTransferHandler;
import net.sf.jttslite.gui.dnd.TransferAction;
import net.sf.jttslite.gui.dnd.TransferActionListener;
import net.sf.jttslite.gui.dnd.TransferData;
import net.sf.jttslite.gui.tray.TrayMessageNotifier;
import net.sf.jttslite.help.HelpResources;
import net.sf.jttslite.model.TaskTreeModelImpl;
import net.sf.jttslite.core.model.PieceOfWork;
import net.sf.jttslite.model.PieceOfWorkTemplateModelImpl;
import net.sf.jttslite.core.model.Task;
import net.sf.jttslite.core.model.TaskTreePath;
import net.sf.jttslite.core.model.WorkSpace;
import net.sf.jttslite.core.model.event.TaskTreeModelEvent;
import net.sf.jttslite.core.model.event.TaskTreeModelListener;
import net.sf.jttslite.core.model.event.WorkAdvanceModelEvent;
import net.sf.jttslite.core.model.event.WorkAdvanceModelListener;
import net.sf.jttslite.tray.SystemTraySupport;
import net.sf.jttslite.core.model.impl.Progress;
import net.sf.jttslite.core.model.impl.ProgressItem;
import net.sf.jttslite.core.model.impl.ProgressTemplate;
import net.sf.jttslite.core.util.Duration;
import net.sf.jttslite.core.util.LocalizedPeriod;
import net.sf.jttslite.core.util.LocalizedPeriodImpl;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import javax.help.CSH;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.color.ColorUtil;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.decorator.SortController;
import org.jdesktop.swingx.decorator.SortOrder;
import org.jdesktop.swingx.decorator.Sorter;
import org.jdesktop.swingx.treetable.TreeTableCellEditor;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * La finestra principale dell'applicazione.
 *
 * @author  davide
 */
public class MainWindow extends javax.swing.JFrame implements PersistentComponent, ActionNotifier {
	
	private final ActionNotifierImpl _actionNotifier;
	
	private final ApplicationContext _context;
	private final WindowManager _wm;
	
	private final AdvancingTicPump _ticPump;
	
	private final TransferActionListener tal = new TransferActionListener ();
	
	/**
	 * Lista dei percorsi in fase di avanzamento
	 */
	private final List<TaskTreePath> _progressingPaths = new ArrayList<TaskTreePath> ();
	
	private final static ImageIcon DEFAULT_PROGRESSES_TABLE_BACKGROUND = new ImageIcon ();
	
	/*
	 * progress Table Backgroud Image
	 */
	ImageIcon ptbImage = new ImageIcon ();
	private int ptbImageWidth;
	private int ptbImageHeight;
        
	/**
	 * Costruttore.
	 */
	public MainWindow (final ApplicationContext context){
		this._actionNotifier = new ActionNotifierImpl ();
		this._context = context;
		this._wm = context.getWindowManager ();
		
		
		/*
		 * va istanziato prima dell'inizializzazione dei componenti, che vi si registrano
		 */
		_ticPump = new AdvancingTicPump (_context.getModel (), 1000);
		/*
		 * garantisce che alla chiusura della finestra la ticpump smetta di notificare eventi riempiendo la coda AWT 
		 * (altrimenti la JVM non si chiude in funzione della frequenza del timer!)
		 */
		addWindowListener (new WindowListener () {
			public void windowActivated (WindowEvent e) {
			}
			public void windowClosed (WindowEvent e) {
			}
			public void windowClosing (WindowEvent e) {
				_ticPump.release ();
			}
			public void windowDeactivated (WindowEvent e) {
			}
			public void windowDeiconified (WindowEvent e) {
			}
			public void windowIconified (WindowEvent e) {
			}
			public void windowOpened (WindowEvent e) {
			}
		});
		_context.getModel ().addWorkAdvanceModelListener (_ticPump);
		
		createActionTable (new JTextArea ());
		
		initComponents ();

		/*
		 * registrazione modello albero come listener sui modelli  
		 * (viene fatta prima dell'albero dato che le notifiche avvengono in ordine inverso, e la tabella dipende dall'albero. in questo modo si evita che notifiche all'albero scatenino ulteriori notifiche alla tabella prima che essa sia ricaricata a fronte della notifica da parte delmodello centrale)
		 */
		_context.getModel ().addTaskTreeModelListener ((TaskJTreeModel)taskTree.getTreeTableModel ());
		_ticPump.addAdvancingTicListener ((TaskJTreeModel)taskTree.getTreeTableModel ());
		_context.getModel ().addWorkAdvanceModelListener ((TaskJTreeModel)taskTree.getTreeTableModel ());
		
		/*
		 * registrazione modello tabella come listener sui modelli 
		 */
		_context.getModel ().addTaskTreeModelListener ((ProgressesJTableModel)progressesTable.getModel ());
		_ticPump.addAdvancingTicListener ((ProgressesJTableModel)progressesTable.getModel ());
		_context.getModel ().addWorkAdvanceModelListener ((ProgressesJTableModel)progressesTable.getModel ());

		/*
		 * registra l'albero e la tabella per la gestione del cut&paste dal menu e toolbar
		 */
		tal.register (taskTree);
		tal.register (progressesTable);

		
		initTableEditors();
		
		final ColorHighlighter rollover = new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, null, Color.BLUE);
		progressesTable.setHighlighters(HighlighterFactory.createAlternateStriping (javax.swing.UIManager.getDefaults().getColor("Table.background"), new Color(255, 252, 180)), rollover);		
		
		/*
		 * Imposta l'ordinamento crescente come default per la colonna di inizio nella tabella degli avanzamenti
		 */
		progressesTable.setSortOrder (progressesTable.convertColumnIndexToView (START_COL_INDEX), SortOrder.ASCENDING);
		
		progressesTable.getColumnExt (progressesTable.convertColumnIndexToView (DURATION_COL_INDEX)).setComparator (new Comparator () {
			public int compare (Object o1, Object o2) {
				return Double.compare ((double)((Progress)o1).getDuration ().getTime (), (double)((Progress)o2).getDuration ().getTime ());
			}
			public boolean equals (Object obj) {
				return super.equals (obj);
			}
		});
		
		
		
		progressesTable.setAutoStartEditOnKeyStroke (false);
		progressesTable.setTerminateEditOnFocusLost (false);
		
		
		taskTree.setAutoStartEditOnKeyStroke (false);
		taskTree.setTerminateEditOnFocusLost (true);
		
		
		this._context.getModel ().addPropertyChangeListener (new PropertyChangeListener (){
			public void propertyChange (PropertyChangeEvent e){
				String pName = e.getPropertyName ();
				if (pName.equals ("name")){
					setTitle (prepareTitle (_context.getModel ()));
				}
			}
		}
		);
		
		
		
		_context.addPropertyChangeListener ("isProcessing", new PropertyChangeListener (){
			public void propertyChange (PropertyChangeEvent evt){
				boolean isProcessing = ((Boolean)evt.getNewValue ()).booleanValue ();
				
				progressBar.setIndeterminate (isProcessing);
				progressBar.setValue (isProcessing ?99:0);
//				progressBar.setVisible (isProcessing);
			}
		});
		
		//ascolta se stesso per la status bar (usa variabili interne)
		progressesTable.getModel ().addTableModelListener ((ProgressesJTableModel)progressesTable.getModel ());
		
		//ascolta l'albero per variare il contenuto della tabella'
		taskTree.addTreeSelectionListener ((ProgressesJTableModel)progressesTable.getModel ());
		
		/*
		 * @workaround
		 * garantisce che la variazione di selezione porti il focus sull'albero, altrimenti serve un ulteriore click perche' il focus arrivi
		 * e siano quindi abilitati il Dnd e cut&paste
		 */
		taskTree.addTreeSelectionListener (new TreeSelectionListener () {
			public void valueChanged (TreeSelectionEvent e) {
				if (e.getPath ()!=null) {
					taskTree.requestFocusInWindow ();
				}
			}
		});
		
		
		
		
		taskTree.addTreeSelectionListener (new TreeSelectionListener () {
			public void valueChanged (TreeSelectionEvent e) {
				final TreePath path = e.getPath ();
				if (path==null) {
					tbp.updateProgressTableBackground (null);
				} else {
					/*
					 * Chiamata asincrona per evitare problemi in caso di rimozione dei task (accesso a campi nulli)
					 */
					SwingUtilities.invokeLater (new Runnable () {
						public void run () {
							final int r = taskTree.getSelectedRow ();
							if (r>=0) {
								tbp.updateProgressTableBackground ((ProgressItem)taskTree.getModel ().getValueAt (taskTree.convertRowIndexToModel (r), taskTree.convertColumnIndexToModel (TaskJTreeModel.TREE_COLUMN_INDEX)));
							}
						}
					});
				}
			}
		});
		
		
		setTitle (prepareTitle (this._context.getModel ()));
		
		setLocationRelativeTo (null);
		
		/*
		 * Rollover su elementi toolbar
		 */
		GUIUtils.addBorderRollover (mainToolbar.getComponents ());
		GUIUtils.addBorderRollover (mainToolbar1.getComponents ());
		GUIUtils.addBorderRollover (mainToolbar2.getComponents ());
		
		//attach transfer handler
		taskTree.setTransferHandler (new TaskTreeTransferHandler ());
		
		progressesTable.setTransferHandler (new ProgressTableTransferHandler ());
		
		_context.getModel ().addWorkAdvanceModelListener (new WorkAdvanceModelListener () {
			public void elementsInserted (WorkAdvanceModelEvent e) {
				adjustStatusLabel ();
			}
			public void elementsRemoved (WorkAdvanceModelEvent e) {
				adjustStatusLabel ();
			}
			
			private void adjustStatusLabel () {
				if (_context.getModel ().getAdvancing ().isEmpty ()) {
					appStatusLabel.setText (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("MainWindow/StatusBar/StatusLabel/IDLE"));
				} else {
					appStatusLabel.setText (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("MainWindow/StatusBar/StatusLabel/RUNNING"));
				}
			}
		});
		
		taskTree.setDefaultRenderer (TaskTreeDuration.class, new DefaultTableCellRenderer () {
			private Font _originalFont;
			private Font _boldFont;
			
			public Component getTableCellRendererComponent (final JTable table, final Object value, boolean isSelected, boolean hasFocus, final int row, final int column) {
				
				final JLabel res = (JLabel)super.getTableCellRendererComponent ( table, value, isSelected, hasFocus, row, column);
				final TaskTreeDuration tduration = (TaskTreeDuration)value;
				final Duration duration = tduration.getDuration ();
				final Task t = tduration.getTask ();
				
				
				if (null==_originalFont) {
					_originalFont = res.getFont ();
				}
				if (null==_boldFont) {
					_boldFont = _originalFont.deriveFont (Font.BOLD);
				}
				
				boolean progressing = false;
				
				for (final TaskTreePath p : _progressingPaths) {
					if (p.contains (t)) {
						progressing = true;
						break;
					}
				}
				if (progressing) {
					res.setFont (_boldFont);
				} else {
					res.setFont (_originalFont);
				}
				
				if (duration.getTime ()==0){
					res.setText ("");
				} else {
					res.setText (DurationUtils.format (duration));
				}
				
				setHorizontalAlignment (TRAILING);
				return res;
			}
			
		});

		/*
		 * toggle sort
		 * 1 asc, 2 desc, 3 null
		 */
		progressesTable.setFilters (new FilterPipeline () {
			protected SortController createDefaultSortController () {
				return new SorterBasedSortController () {
					
					public void toggleSortOrder (int column, Comparator comparator) {
						Sorter currentSorter = getSorter ();
						if ((currentSorter != null)
						&& (currentSorter.getColumnIndex () == column)
						&& !currentSorter.isAscending ()) {
							setSorter (null);
						} else {
							super.toggleSortOrder (column, comparator);
						}
					}
					
				};
			}
		});
		
		progressesTable.getModel ().addTableModelListener (new TableModelListener () {
			public void tableChanged (TableModelEvent e) {
				if (e.getColumn ()==TableModelEvent.ALL_COLUMNS && e.getLastRow ()==Integer.MAX_VALUE) {
					/* denota un ricaricamento totale */
					/*
					 * @workaround commentato il packall, altrimenti provoca un accesso al modello non valido (in caso di eliminazione chiede righe non piu' esistenti, anche se il modello sembra coerente. Cache interna?)
					 */
//					progressesTable.packAll ();
				}
			}
		});
		
		
		/*
		 * forza il focus sull'albero in caso di variazione del workspace
		 */
		this._context.getModel ().addPropertyChangeListener (new PropertyChangeListener (){
			public void propertyChange (PropertyChangeEvent e){
				final String pName = e.getPropertyName ();
				if (pName.equals ("name")){
					try {
						taskTree.requestFocusInWindow ();
						taskTree.getSelectionModel ().setSelectionInterval (0, 0);
					} catch (Exception ex) {
						/*
						 * Se non dovesse funzionare, non deve bloccare il thread AWT
						 */
						ex.printStackTrace (System.err);
					}
				}
			}
		}
		);
		
		/*
		 * Aggiunge azioni mappate con acceleratori all'albero ed alla tabella degli avanzamenti
		 */
		final ActionMap taskTreeActionMap = new ActionMap ();
		taskTreeActionMap.setParent (taskTree.getActionMap ());
		taskTreeActionMap.put ("delete", new DeleteTasksAction ());
		taskTreeActionMap.put ("startProgress", new StartProgressAction ());
		taskTreeActionMap.put ("stopProgress", new StopProgressAction ());
		final Action renameTaskAction = new RenameTaskAction ();
		taskTreeActionMap.put (renameTaskAction.getValue (Action.NAME), renameTaskAction);
		taskTree.setActionMap (taskTreeActionMap);
//		/*
//		 * @workaround
//		 */
//		//((JXTree)taskTree.getCellRenderer (0, 0)).getActionMap ().put ("renameTask", new RenameTaskAction ());
//		//taskTree.getTreeCellRenderer ().taskTreeActionMap);
//		final InputMap taskTreeInputMap = new InputMap ();
//		taskTreeInputMap.setParent (taskTree.getInputMap ());
//		taskTreeInputMap.put ((KeyStroke) renameTaskAction.getValue (Action.ACCELERATOR_KEY),renameTaskAction.getValue (Action.NAME));
		
		final ActionMap progressesTableActionMap = new ActionMap ();
		progressesTableActionMap.setParent (progressesTable.getActionMap ());
		progressesTableActionMap.put ("delete", new DeletePiecesOfWorkAction ());
		progressesTableActionMap.put ("startProgress", new StartProgressAction ());
		progressesTableActionMap.put ("stopProgress", new StopProgressAction ());
		progressesTable.setActionMap (progressesTableActionMap);
		
		taskTree.getTreeTableModel ().addTreeModelListener (new TreeModelListener () {
			public void treeNodesChanged (TreeModelEvent e) {
			}
			public void treeNodesInserted (TreeModelEvent e) {
			}
			public void treeNodesRemoved (TreeModelEvent e) {
				/*
				 * @workaround
				 * forza il repaint per evitare la mancata rimozione (a video) dell'ultima foglia dell'albero
				 */
				taskTree.repaint ();
			}
			public void treeStructureChanged (TreeModelEvent e) {
			}
		});
		
//		_context.getModel ().addTaskTreeModelListener (new TaskTreeModelListener () {
//			public void treeNodesChanged (TaskTreeModelEvent e) {
//				invokePackAll (progressesTable);
//			}
//			public void treeNodesInserted (TaskTreeModelEvent e) {
//				invokePackAll (progressesTable);
//			}
//			public void treeNodesRemoved (TaskTreeModelEvent e) {
//				invokePackAll (progressesTable);
//			}
//			public void treeStructureChanged (TaskTreeModelEvent e) {
//				invokePackAll (progressesTable);
//			}
//			public void workSpaceChanged (WorkSpace oldWS, WorkSpace newWS) {
//				invokePackAll (progressesTable);
//			}
//			
//		});
		invokePackAll (taskTree);
		
		
		/**
		 * consente di disegnare in grassetto il percorso in progress
		 */
		_context.getModel ().addWorkAdvanceModelListener (new WorkAdvanceModelListener () {
			
			private void setPaths () {
				synchronized (_progressingPaths) {
					_progressingPaths.clear ();
					for (final PieceOfWork pow : _context.getModel ().getAdvancing ()) {
						_progressingPaths.add (new TaskTreePath (_context.getModel ().getWorkSpace (), pow.getTask ()));
					}
				}
			}
			public void elementsInserted (WorkAdvanceModelEvent e) {
				setPaths ();
				taskTree.repaint ();
			}
			public void elementsRemoved (WorkAdvanceModelEvent e) {
				setPaths ();
				taskTree.repaint ();
			}
		});
		
		
		mainContentsTabbedPane.setIconAt (0, 
			new CompositeIcon (
				new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/view_text.png")),
				new VTextIcon(mainContentsTabbedPane, java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("MainWindow/MainContentsTabbedPane/TabTitle/Actions"), VTextIcon.ROTATE_DEFAULT)
				)
			);
		
		mainContentsTabbedPane.setIconAt (1, 
			new CompositeIcon (
				new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/colors.png")),
				new VTextIcon(mainContentsTabbedPane, java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("MainWindow/MainContentsTabbedPane/TabTitle/Charts"), VTextIcon.ROTATE_DEFAULT)
				)
			);
			
		
		((RingChartPanel)chartPanel).addRingChartRootChangeListener (new PropertyChangeListener () {
			public void propertyChange (PropertyChangeEvent evt) {
				final Task t = (Task)evt.getNewValue ();
				final TreePath p = taskTree.getTreeSelectionModel ().getSelectionPath ();
				final TaskTreePath ttp = new TaskTreePath (_context.getModel ().getWorkSpace (), t);
				final TreePath p1 = toTreePath (ttp);
				if (!p1.equals (p)) {
					/*
					 * varia selezione albero
					 */
					taskTree.expandPath (p1);
					taskTree.getTreeSelectionModel ().setSelectionPath (p1);
				}
			}
		});

		
		final SystemTraySupport tray = context.getWindowManager ().getSystemTraySupport ();

		tray.setMenu (trayMenu);

		context.getModel ().addWorkAdvanceModelListener (new WorkAdvanceModelListener () {
			public void elementsInserted (WorkAdvanceModelEvent e) {
				processEvent (e);
			}
			public void elementsRemoved (WorkAdvanceModelEvent e) {
				processEvent (e);
			}

			private void processEvent (final WorkAdvanceModelEvent e) {
				/*
				 * Notifica la variazione di avanzamenti in corso
				 */
				PieceOfWork pow = null;

				final Set<PieceOfWork> advancing = _context.getModel ().getAdvancing ();
				if (!advancing.isEmpty ()) {
					/*
					 * Usa il primo avanzamento
					 */
					pow = advancing.iterator ().next ();
				}

				tray.setWorking (pow);
			}
		});

		/*
		 * Mostra la finestra principale con un doppio clicksulla tray icon
		 */
		tray.addMouseListener (new MouseListener () {
			public void mouseClicked (MouseEvent e) {
				if (e.getClickCount ()==1) {
//					tray.showMenu ();
				} else {
					bringToFront ();
				}
			}
			public void mouseEntered (MouseEvent e) {
			}
			public void mouseExited (MouseEvent e) {
			}
			public void mousePressed (MouseEvent e) {
			}
			public void mouseReleased (MouseEvent e) {
			}
		});


		_ticPump.addAdvancingTicListener (
			new AdvancingTicListener () {
				public void advanceTic (AdvancingTicEvent e) {
					/*
					 * Notifica la variazione di avanzamenti in corso
					 */
					PieceOfWork pow = null;

					final Set<PieceOfWork> advancing = _context.getModel ().getAdvancing ();
					if (!advancing.isEmpty ()) {
						/*
						 * Usa il primo avanzamento
						 */
						pow = advancing.iterator ().next ();
					}

					tray.setWorking (pow);

				}
			}
		);
		
		
		
		/*
		 * notifica 15 minuti di inattivita'
		 */
		context.getModel ().addWorkAdvanceModelListener (new WorkAdvanceModelListener () {
			
			private final TrayMessageNotifier _notifier = new TrayMessageNotifier (context, 1000*60*15) {
				public void notifyTray (final SystemTraySupport traySupport) {
					traySupport.displayInfo (
						java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("SystemTray/InfoMesage/Title/Idle_15_minutes"), 
						java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("SystemTray/InfoMesage/Message/Idle_15_minutes"));
				}
			};
			
			{
				testActivation ();
			}
			
			public void elementsInserted (WorkAdvanceModelEvent e) {
				_notifier.deactivate ();
			}
			public void elementsRemoved (WorkAdvanceModelEvent e) {
				testActivation ();
			}
			
			private void testActivation () {
				if (context.getModel ().getAdvancing ().isEmpty ()) {
					_notifier.activate ();
				}
			}
		});
		
		
		/*
		 * notifica la variazione di selezione sull'alberoalla tray icon
		 */
		taskTree.addTreeSelectionListener (new TreeSelectionListener () {
			public void valueChanged (TreeSelectionEvent e) {
				final List<Task> selected = getSelectedTasks ();
				if (!selected.isEmpty ()) {
					context.getWindowManager ().getSystemTraySupport ().setSelected (selected.get (0));
				}
			}
		});
		
//		taskTree.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//
//			int lastSelectedRow=-2;
//			public void valueChanged(ListSelectionEvent e) {
//				if (e.getValueIsAdjusting ()) {
//					/* evento spurio */
//					return;
//				}
//				/*
//				 * @workaround salta event spurio
//				 */
//				if (taskTree.getSelectedRow()>=0) {
//					taskTreeSelectionChanged = taskTree.getSelectedRow()!=lastSelectedRow;
//					lastSelectedRow = taskTree.getSelectedRow();
//				}
//			}
//		});
		
		/*
		 * Tabpane tooltips
		 */
		mainContentsTabbedPane.setToolTipTextAt (0, java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("MainWindow/ProgressesPane/ProgressesTableTab/Tooltip"));
		mainContentsTabbedPane.setToolTipTextAt (1, java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("MainWindow/ProgressesPane/ChartsTab/Tooltip"));
				
		
		
	}

	private void initTableEditors () {
		
		progressesTable.setDefaultEditor (Date.class, new SmartCellEditor (new DateCellEditor ()));
		progressesTable.setDefaultEditor (PieceOfWork.class, new SmartCellEditor (new DurationCellEditor ()));
		progressesTable.setDefaultEditor (Duration.class, new SmartCellEditor (new DurationCellEditor ()));
		progressesTable.setDefaultEditor (String.class, new SmartCellEditor (new DefaultCellEditor (new JTextField ()) {
			
			
			/**
			 * Editazione parte, in caso di evento del mouse, solo dopo doppio click
			 */
			@Override
			public boolean isCellEditable (EventObject eo) {
				if (eo instanceof MouseEvent) {
					return ((MouseEvent)eo).getClickCount ()>1;
				} else {
					return super.isCellEditable (eo);
				}
			}
		}));
	}
	
	
	private String prepareTitle (TaskTreeModelImpl model){
		final StringBuilder sb = new StringBuilder ();
		sb.append (_context.getApplicationData ().getApplicationExternalName ()).append (" - Workspace ").append (model.getRoot ().getName ());
		return sb.toString ();
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        borderLayout1 = new java.awt.BorderLayout();
        treePopupMenu = new javax.swing.JPopupMenu();
        newTaskPopupItem = new javax.swing.JMenuItem();
        newProgressPopupItem = new javax.swing.JMenuItem();
        startProgressPopupItem = new javax.swing.JMenuItem();
        jSeparator12 = new javax.swing.JSeparator();
        expandPopupItem = new javax.swing.JMenuItem();
        collapsePopupItem = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JSeparator();
        cutTasksMenuItem = new javax.swing.JMenuItem();
        copyTasksMenuItem = new javax.swing.JMenuItem();
        pasteTasksMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        renameTaskPopupItem = new javax.swing.JMenuItem();
        deleteTaskPopupItem = new javax.swing.JMenuItem();
        editTaskPopupItem = new javax.swing.JMenuItem();
        tablePopupMenu = new javax.swing.JPopupMenu();
        newProgressPopupItem1 = new javax.swing.JMenuItem();
        startProgressPopupItem1 = new javax.swing.JMenuItem();
        startProgressClonePopupItem = new javax.swing.JMenuItem();
        continueProgressPopupItem = new javax.swing.JMenuItem();
        stopProgressMenuItem1 = new javax.swing.JMenuItem();
        addToTemplatesMenuItem = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JSeparator();
        cutProgressesMenuItem1 = new javax.swing.JMenuItem();
        copyProgressesMenuItem1 = new javax.swing.JMenuItem();
        pasteProgressesMenuItem1 = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JSeparator();
        deleteProgressPopupItem = new javax.swing.JMenuItem();
        openFileChooser = new javax.swing.JFileChooser();
        saveFileChooser = new javax.swing.JFileChooser();
        TabShowingChoiceButtonGroup = new javax.swing.ButtonGroup();
        trayMenu = new java.awt.PopupMenu();
        trayMenuShowMenuItem = new java.awt.MenuItem();
        trayMenuStartProgressMenuItem = new java.awt.MenuItem();
        trayMenuStopProgressMenuItem = new java.awt.MenuItem();
        trayMenuExitMenuItem = new java.awt.MenuItem();
        mainPanel = new javax.swing.JPanel();
        tree_table_splitPane = new javax.swing.JSplitPane();
        taskTreePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taskTree = new org.jdesktop.swingx.JXTreeTable();
        jPanel3 = new javax.swing.JPanel();
        mainContentsTabbedPane = new javax.swing.JTabbedPane();
        progressesTablePanel = new javax.swing.JPanel();
        progressesTableScrollPane = new javax.swing.JScrollPane();
        progressesTable = new SmartJXTable (){

            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer( renderer, row, column);
                // We want renderer component to be transparent
                // so background image is visible
                if( c instanceof JComponent ) {
                    //((JComponent)c).setOpaque(false);
                    final Color oldColor = c.getBackground ();
                    if (oldColor!=null) {
                        c.setBackground (ColorUtil.setAlpha (oldColor, 150));
                    }
                }
                return c;
            }

            public void paint( Graphics g ) {
                // First draw the background image - tiled
                final Dimension d = getSize();

                g.drawImage (ptbImage.getImage(), 0, jPanel4.getHeight () - getTableHeader ().getHeight (), null, null);

                /*
                * immagine centrata, non va bene perchè si vuole l'esatta posizione del tab dedicata al grafico
                */
                //g.drawImage( ptbImage.getImage(), (d.width - ptbImageWidth)/2, (d.height - ptbImageHeight)/2, null, null );
                /*
                * Trova le dimensioni minime tra l'immagine e la tabella
                */
                final int h = d.height < ptbImageHeight ? d.height : ptbImageHeight;
                final int w = d.width < ptbImageWidth ? d.width : ptbImageWidth;

                /*
                * Dato che il pannello contenente lo slider è pià alto dello header della tabella
                * si posiziona con un'offset
                */
                //g.drawImage (ptbImage.getImage(), 0, d.height - ptbImageHeight, w, h, null, null);

                super.paint(g);
            }
        };
        ;
        jPanel1 = new javax.swing.JPanel();
        chartPanel = new RingChartPanel ("");
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        chartDepthSlider = new javax.swing.JSlider();
        jPanel5 = new javax.swing.JPanel();
        mainToolbar = new javax.swing.JToolBar();
        jButton5 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jButton4 = new javax.swing.JButton();
        jSeparator17 = new javax.swing.JSeparator();
        jButton3 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jSeparator14 = new javax.swing.JSeparator();
        mainToolbar1 = new javax.swing.JToolBar();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        undoButton = new javax.swing.JButton();
        redoButton = new javax.swing.JButton();
        jSeparator15 = new javax.swing.JSeparator();
        mainToolbar2 = new javax.swing.JToolBar();
        printButton = new javax.swing.JButton();
        workspacesButton = new javax.swing.JButton();
        actionTemplatesButton = new javax.swing.JButton();
        helpButton = new javax.swing.JButton();
        statusPanel = new javax.swing.JPanel();
        appStatusLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tableTaskNameLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tableProgressCountLabel = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newWorkSpaceMenuItem = new javax.swing.JMenuItem();
        newTaskMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        openWorkSpaceMenuItem = new javax.swing.JMenuItem();
        exportWorkSpaceMenuItem = new javax.swing.JMenuItem();
        importWorkSpaceMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        printMenuItem = new javax.swing.JMenuItem();
        jSeparator11 = new javax.swing.JSeparator();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        undoMenuItem = new javax.swing.JMenuItem();
        redoMenuItem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        getActionByName(DefaultEditorKit.cutAction).putValue (Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        cutMenuItem = new javax.swing.JMenuItem();
        getActionByName(DefaultEditorKit.copyAction).putValue (Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        copyMenuItem = new javax.swing.JMenuItem();
        getActionByName(DefaultEditorKit.pasteAction).putValue (Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        pasteMenuItem = new javax.swing.JMenuItem();
        getActionByName(DefaultEditorKit.deleteNextCharAction).putValue (Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_DELETE, 0));
        deleteMenuItem = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        showActionsMenuItem = new javax.swing.JRadioButtonMenuItem();
        showChartsMenuItem = new javax.swing.JRadioButtonMenuItem();
        jSeparator16 = new javax.swing.JSeparator();
        linkTableBackgroundMenuItem = new javax.swing.JCheckBoxMenuItem();
        tasksMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        actionstMenu = new javax.swing.JMenu();
        newProgressMenuItem = new javax.swing.JMenuItem();
        startProgressMenuItem = new javax.swing.JMenuItem();
        startProgressCloneMenuItem = new javax.swing.JMenuItem();
        continueProgressMenuItem = new javax.swing.JMenuItem();
        jSeparator13 = new javax.swing.JSeparator();
        stopProgressMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        logConsoleMenuItem = new javax.swing.JMenuItem();
        workspaceMenuItem = new javax.swing.JMenuItem();
        templateMenuItem = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        optionsMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        contextHelpMenuItem = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JSeparator();
        aboutMenuItem = new javax.swing.JMenuItem();

        treePopupMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        treePopupMenu.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                treePopupMenuPopupMenuWillBecomeVisible(evt);
            }
        });

        newTaskPopupItem.setAction(new NewTaskAction ());
        newTaskPopupItem.setFont(new java.awt.Font("Dialog", 0, 12));
        newTaskPopupItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/file-new.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res"); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(newTaskPopupItem, bundle.getString("New_task")); // NOI18N
        newTaskPopupItem.setActionCommand("newTask");
        newTaskPopupItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newTaskPopupItemActionPerformed(evt);
            }
        });
        treePopupMenu.add(newTaskPopupItem);

        newProgressPopupItem.setAction(new NewPieceOfWorkAction ());
        newProgressPopupItem.setFont(new java.awt.Font("Dialog", 0, 12));
        newProgressPopupItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(newProgressPopupItem, bundle.getString("New_action")); // NOI18N
        newProgressPopupItem.setActionCommand("newProgress");
        newProgressPopupItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newProgressPopupItemActionPerformed(evt);
            }
        });
        treePopupMenu.add(newProgressPopupItem);

        startProgressPopupItem.setAction(new StartProgressAction ());
        startProgressPopupItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        startProgressPopupItem.setFont(new java.awt.Font("Dialog", 0, 12));
        startProgressPopupItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/media-record.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(startProgressPopupItem, bundle.getString("Start_action")); // NOI18N
        startProgressPopupItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startProgressPopupItemActionPerformed(evt);
            }
        });
        treePopupMenu.add(startProgressPopupItem);
        treePopupMenu.add(jSeparator12);

        expandPopupItem.setAction(new ExpandTreeAction ());
        expandPopupItem.setFont(new java.awt.Font("Dialog", 0, 12));
        expandPopupItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/transparent.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(expandPopupItem, bundle.getString("Expand")); // NOI18N
        expandPopupItem.setToolTipText(bundle.getString("Expands_subtree")); // NOI18N
        treePopupMenu.add(expandPopupItem);

        collapsePopupItem.setAction(new CollapseTreeAction ());
        collapsePopupItem.setFont(new java.awt.Font("Dialog", 0, 12));
        collapsePopupItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/transparent.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(collapsePopupItem, bundle.getString("Collapse")); // NOI18N
        treePopupMenu.add(collapsePopupItem);
        treePopupMenu.add(jSeparator6);

        cutTasksMenuItem.setAction(new TransferAction (TransferAction.Type.CUT, tal));
        cutTasksMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        cutTasksMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
        cutTasksMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/edit-cut.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(cutTasksMenuItem, bundle.getString("Cut")); // NOI18N
        treePopupMenu.add(cutTasksMenuItem);

        copyTasksMenuItem.setAction(new TransferAction (TransferAction.Type.COPY, tal));
        copyTasksMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        copyTasksMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
        copyTasksMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/edit-copy.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(copyTasksMenuItem, bundle.getString("Copy")); // NOI18N
        treePopupMenu.add(copyTasksMenuItem);

        pasteTasksMenuItem.setAction(new TransferAction (TransferAction.Type.PASTE, tal));
        pasteTasksMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        pasteTasksMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
        pasteTasksMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/edit-paste.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(pasteTasksMenuItem, bundle.getString("Paste")); // NOI18N
        treePopupMenu.add(pasteTasksMenuItem);
        treePopupMenu.add(jSeparator1);

        renameTaskPopupItem.setAction(new RenameTaskAction ());
        renameTaskPopupItem.setFont(new java.awt.Font("Dialog", 0, 12));
        renameTaskPopupItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/transparent.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(renameTaskPopupItem, bundle.getString("Rename")); // NOI18N
        renameTaskPopupItem.setToolTipText(bundle.getString("Rename_selected_task")); // NOI18N
        renameTaskPopupItem.setActionCommand("renameTask");
        renameTaskPopupItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renameTaskPopupItemActionPerformed(evt);
            }
        });
        treePopupMenu.add(renameTaskPopupItem);

        deleteTaskPopupItem.setAction(new DeleteTasksAction ());
        deleteTaskPopupItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        deleteTaskPopupItem.setFont(new java.awt.Font("Dialog", 0, 12));
        deleteTaskPopupItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/edit-delete.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(deleteTaskPopupItem, bundle.getString("Delete")); // NOI18N
        deleteTaskPopupItem.setActionCommand("deleteTask");
        treePopupMenu.add(deleteTaskPopupItem);

        editTaskPopupItem.setAction(new StartTaskEditAction ());
        editTaskPopupItem.setFont(new java.awt.Font("Dialog", 0, 12));
        editTaskPopupItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/transparent.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(editTaskPopupItem, bundle.getString("MainWIndow/TaskTreePopupMenu/TaskProperties")); // NOI18N
        treePopupMenu.add(editTaskPopupItem);

        tablePopupMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        tablePopupMenu.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                tablePopupMenuPopupMenuWillBecomeVisible(evt);
            }
        });

        newProgressPopupItem1.setAction(new NewPieceOfWorkAction ());
        newProgressPopupItem1.setFont(new java.awt.Font("Dialog", 0, 12));
        newProgressPopupItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(newProgressPopupItem1, bundle.getString("New_action")); // NOI18N
        newProgressPopupItem1.setToolTipText(bundle.getString("Creates_a_new_action")); // NOI18N
        newProgressPopupItem1.setActionCommand("newProgress");
        newProgressPopupItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newProgressPopupItem1ActionPerformed(evt);
            }
        });
        tablePopupMenu.add(newProgressPopupItem1);

        startProgressPopupItem1.setAction(new StartProgressAction ());
        startProgressPopupItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        startProgressPopupItem1.setFont(new java.awt.Font("Dialog", 0, 12));
        startProgressPopupItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/media-record.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(startProgressPopupItem1, bundle.getString("Start_action")); // NOI18N
        startProgressPopupItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startProgressPopupItem1ActionPerformed(evt);
            }
        });
        tablePopupMenu.add(startProgressPopupItem1);

        startProgressClonePopupItem.setAction(new StartProgressCloneAction ());
        startProgressClonePopupItem.setFont(new java.awt.Font("Dialog", 0, 12));
        startProgressClonePopupItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/transparent.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(startProgressClonePopupItem, bundle.getString("Start_action_clone")); // NOI18N
        startProgressClonePopupItem.setToolTipText(bundle.getString("Start_a_new_action_having_the_same_description_of_the_selected_one")); // NOI18N
        startProgressClonePopupItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startProgressClonePopupItemActionPerformed(evt);
            }
        });
        tablePopupMenu.add(startProgressClonePopupItem);

        continueProgressPopupItem.setAction(new ContinueProgressAction ());
        continueProgressPopupItem.setFont(new java.awt.Font("Dialog", 0, 12));
        continueProgressPopupItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/transparent.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(continueProgressPopupItem, bundle.getString("Continue_action")); // NOI18N
        continueProgressPopupItem.setToolTipText(bundle.getString("Continue_previously_stopped_action")); // NOI18N
        continueProgressPopupItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                continueProgressPopupItemActionPerformed(evt);
            }
        });
        tablePopupMenu.add(continueProgressPopupItem);

        stopProgressMenuItem1.setAction(new StopProgressAction ());
        stopProgressMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        stopProgressMenuItem1.setFont(new java.awt.Font("Dialog", 0, 12));
        stopProgressMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/media-stop.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(stopProgressMenuItem1, bundle.getString("Stop")); // NOI18N
        stopProgressMenuItem1.setToolTipText(bundle.getString("Stop_current_action")); // NOI18N
        stopProgressMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopProgressMenuItem1ActionPerformed(evt);
            }
        });
        tablePopupMenu.add(stopProgressMenuItem1);

        addToTemplatesMenuItem.setAction(new AddProgressToTemplatesAction ());
        addToTemplatesMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
        addToTemplatesMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/transparent.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addToTemplatesMenuItem, bundle.getString("MainWIndow/ProgressesTableMenu/AddProgressToTemplates")); // NOI18N
        addToTemplatesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addToTemplatesMenuItemActionPerformed(evt);
            }
        });
        tablePopupMenu.add(addToTemplatesMenuItem);
        tablePopupMenu.add(jSeparator9);

        cutProgressesMenuItem1.setAction(new TransferAction (TransferAction.Type.CUT, tal));
        cutProgressesMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        cutProgressesMenuItem1.setFont(new java.awt.Font("Dialog", 0, 12));
        cutProgressesMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/edit-cut.png"))); // NOI18N
        cutProgressesMenuItem1.setText(bundle.getString("Cut")); // NOI18N
        tablePopupMenu.add(cutProgressesMenuItem1);

        copyProgressesMenuItem1.setAction(new TransferAction (TransferAction.Type.COPY, tal));
        copyProgressesMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        copyProgressesMenuItem1.setFont(new java.awt.Font("Dialog", 0, 12));
        copyProgressesMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/edit-copy.png"))); // NOI18N
        copyProgressesMenuItem1.setText(bundle.getString("Copy")); // NOI18N
        tablePopupMenu.add(copyProgressesMenuItem1);

        pasteProgressesMenuItem1.setAction(new TransferAction (TransferAction.Type.PASTE, tal));
        pasteProgressesMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        pasteProgressesMenuItem1.setFont(new java.awt.Font("Dialog", 0, 12));
        pasteProgressesMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/edit-paste.png"))); // NOI18N
        pasteProgressesMenuItem1.setText(bundle.getString("Paste")); // NOI18N
        tablePopupMenu.add(pasteProgressesMenuItem1);
        tablePopupMenu.add(jSeparator10);

        deleteProgressPopupItem.setAction(new DeletePiecesOfWorkAction ());
        deleteProgressPopupItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        deleteProgressPopupItem.setFont(new java.awt.Font("Dialog", 0, 12));
        deleteProgressPopupItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/edit-delete.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(deleteProgressPopupItem, bundle.getString("Delete")); // NOI18N
        deleteProgressPopupItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteProgressPopupItemActionPerformed(evt);
            }
        });
        tablePopupMenu.add(deleteProgressPopupItem);

        openFileChooser.setCurrentDirectory(null);
        openFileChooser.setFileFilter(new CustomFileFilter (
            new String []{FileUtils.properties},
            new String []{"Properties files"}
        ));
        openFileChooser.setFont(new java.awt.Font("Dialog", 0, 12));

        saveFileChooser.setCurrentDirectory(null);
        saveFileChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        saveFileChooser.setFileFilter(new CustomFileFilter (
            new String []{FileUtils.properties},
            new String []{"Properties files"}
        ));
        saveFileChooser.setFont(new java.awt.Font("Dialog", 0, 12));
        saveFileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveFileChooserActionPerformed(evt);
            }
        });

        trayMenu.setLabel(_context.getApplicationData ().getApplicationExternalName ());

        trayMenuShowMenuItem.setLabel(bundle.getString("TrayIcon/MenuItem/Label/Show")); // NOI18N
        trayMenuShowMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                traMenuShowMenuItemActionPerformed(evt);
            }
        });
        trayMenu.add(trayMenuShowMenuItem);
        trayMenu.addSeparator();
        trayMenuStartProgressMenuItem.setLabel(bundle.getString("TrayIcon/MenuItem/Label/Start_action")); // NOI18N
        bind (trayMenuStartProgressMenuItem, new StartProgressAction ());
        trayMenu.add(trayMenuStartProgressMenuItem);

        trayMenuStopProgressMenuItem.setLabel(bundle.getString("TrayIcon/MenuItem/Label/Stop_action")); // NOI18N
        bind (trayMenuStopProgressMenuItem, new StopProgressAction ());
        trayMenu.add(trayMenuStopProgressMenuItem);
        trayMenu.addSeparator();
        trayMenuExitMenuItem.setLabel(bundle.getString("TrayIcon/MenuItem/Label/Exit")); // NOI18N
        trayMenuExitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trayMenuExitActionPerformed(evt);
            }
        });
        trayMenu.add(trayMenuExitMenuItem);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconImage(new javax.swing.ImageIcon (MainWindow.class.getResource ("/net/sf/jttslite/gui/images/clock.png")).getImage ());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                formWindowDeactivated(evt);
            }
        });

        mainPanel.setLayout(new java.awt.BorderLayout());

        tree_table_splitPane.setMaximumSize(null);
        tree_table_splitPane.setOneTouchExpandable(true);

        taskTreePanel.setAutoscrolls(true);
        taskTreePanel.setPreferredSize(new java.awt.Dimension(160, 354));
        taskTreePanel.setLayout(new java.awt.BorderLayout());

        taskTree.setBackground(new java.awt.Color(254, 254, 254));
        taskTree.setColumnControlVisible(true);
        taskTree.setComponentPopupMenu(treePopupMenu);
        taskTree.setDragEnabled(true);
        taskTree.setHorizontalScrollEnabled(true);
        taskTree.setMaximumSize(null);
        taskTree.setMinimumSize(null);
        taskTree.setRootVisible(true);
        taskTree.setTreeTableModel(new TaskJTreeModel (_context.getModel ()));
        javax.help.CSH.setHelpIDString (taskTree, _context.getHelpManager ().getResolver ().resolveHelpID (HelpResources.TASK_TREE ));
        taskTree.setTreeCellRenderer (new TaskTreeCellRenderer ());
        taskTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                taskTreeMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                taskTreeMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(taskTree);

        taskTreePanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        tree_table_splitPane.setLeftComponent(taskTreePanel);

        jPanel3.setLayout(new java.awt.BorderLayout());

        mainContentsTabbedPane.setTabPlacement(javax.swing.JTabbedPane.RIGHT);
        mainContentsTabbedPane.setFont(new java.awt.Font("Dialog", 0, 12));
        mainContentsTabbedPane.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                mainContentsTabbedPaneFocusGained(evt);
            }
        });

        progressesTablePanel.setBackground(java.awt.Color.white);
        progressesTablePanel.setOpaque(false);
        progressesTablePanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                progressesTablePanelComponentShown(evt);
            }
        });
        progressesTablePanel.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                progressesTablePanelFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                progressesTablePanelFocusLost(evt);
            }
        });
        progressesTablePanel.setLayout(new java.awt.BorderLayout());

        progressesTableScrollPane.setBackground(java.awt.Color.white);
        progressesTableScrollPane.setMaximumSize(null);
        progressesTableScrollPane.setOpaque(false);
        //jScrollPane3.getViewport ().setBackground (javax.swing.UIManager.getDefaults().getColor("Table.background"));
        progressesTableScrollPane.getViewport ().setBackground (Color.WHITE);

        progressesTable.setModel(new ProgressesJTableModel (_context.getModel ()));
        progressesTable.setColumnControlVisible(true);
        progressesTable.setComponentPopupMenu(tablePopupMenu);
        progressesTable.setDragEnabled(true);
        progressesTable.setHorizontalScrollEnabled(true);
        progressesTable.setOpaque(false);
        progressesTable.setPreferredScrollableViewportSize(null);
        progressesTable.setPreferredSize(null);
        progressesTable.setSurrendersFocusOnKeystroke(true);
        //progressesTable.setDefaultEditor (String.class, new ValueCellEditor ());

        /*
        * editor (shortcut per cancellazione
            */
            progressesTable.addKeyListener (new KeyAdapter (){
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode ()==KeyEvent.VK_DELETE){
                    }
                }
            });

            javax.help.CSH.setHelpIDString (progressesTable, _context.getHelpManager ().getResolver ().resolveHelpID (HelpResources.PROGRESSES_TABLE ));

            progressesTable.setDefaultRenderer (java.util.Date.class, new DefaultTableCellRenderer (){

                public Component getTableCellRendererComponent (final JTable table, final Object value, boolean isSelected, boolean hasFocus, final int row, final int column) {
                    final Component c = super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);

                    setHorizontalAlignment (TRAILING);

                    return c;
                }
                public void setValue (Object value) {
                    setText (
                        net.sf.jttslite.common.util.CalendarUtils.getTimestamp (
                            (Date)value,
                            CustomizableFormat.LONG_DATE.getValue (_context)
                        )
                    );
                }
            });

            progressesTable.setDefaultRenderer (Duration.class,
                new DefaultTableCellRenderer () {
                    private Font _originalFont;
                    private Font _boldFont;
                    public Component getTableCellRendererComponent (final JTable table, final Object value, boolean isSelected, boolean hasFocus, final int row, final int column) {

                        final JLabel res = (JLabel)super.getTableCellRendererComponent ( table, value, isSelected, hasFocus, row, column);
                        final PieceOfWork progress = (PieceOfWork)value;

                        final boolean progressing = progress.getTo ()==null;

                        if (null==_originalFont) {
                            _originalFont = res.getFont ();
                        }
                        if (null==_boldFont) {
                            _boldFont = _originalFont.deriveFont (Font.BOLD);
                        }

                        if (progressing){
                            res.setFont (_boldFont);
                        } else {
                            res.setFont (_originalFont);
                        }

                        setHorizontalAlignment (TRAILING);

                        return res;
                    }
                    public void setValue (Object value) {
                        final PieceOfWork progress = (PieceOfWork)value;
                        setText (DurationUtils.formatDuration (progress.getTime ()));
                    }
                }
            );
            progressesTable.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    progressesTableMousePressed(evt);
                }
            });
            progressesTableScrollPane.setViewportView(progressesTable);

            progressesTablePanel.add(progressesTableScrollPane, java.awt.BorderLayout.CENTER);

            mainContentsTabbedPane.addTab(null, progressesTablePanel);

            jPanel1.setToolTipText(bundle.getString("MainWindow/ProgressesPane/ChartsTab/Tooltip")); // NOI18N
            jPanel1.addComponentListener(new java.awt.event.ComponentAdapter() {
                public void componentShown(java.awt.event.ComponentEvent evt) {
                    jPanel1ComponentShown(evt);
                }
            });
            jPanel1.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    jPanel1FocusGained(evt);
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    jPanel1FocusLost(evt);
                }
            });
            jPanel1.setLayout(new java.awt.GridBagLayout());

            chartPanel.setBackground(java.awt.Color.white);
            chartPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
                public void componentResized(java.awt.event.ComponentEvent evt) {
                    chartPanelComponentResized(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            jPanel1.add(chartPanel, gridBagConstraints);

            jPanel4.setPreferredSize(new java.awt.Dimension(210, 30));
            jPanel4.setLayout(new java.awt.GridBagLayout());

            jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
            jLabel3.setText(bundle.getString("MainWindow/ControlLabel/Visible_chart_levels")); // NOI18N
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
            gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
            jPanel4.add(jLabel3, gridBagConstraints);

            chartDepthSlider.setMaximum(10);
            chartDepthSlider.setMinimum(1);
            chartDepthSlider.setValue(5);
            {
                final int v = _context.getApplicationOptions ().getChartDepth ();
                chartDepthSlider.setValue (v);

                ((RingChartPanel)chartPanel).setDepth (v);
            }
            chartDepthSlider.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    chartDepthSliderFocusLost(evt);
                }
            });
            chartDepthSlider.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    chartDepthSliderStateChanged(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
            jPanel4.add(chartDepthSlider, gridBagConstraints);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            jPanel1.add(jPanel4, gridBagConstraints);

            mainContentsTabbedPane.addTab(null, jPanel1);

            jPanel3.add(mainContentsTabbedPane, java.awt.BorderLayout.CENTER);

            tree_table_splitPane.setRightComponent(jPanel3);

            mainPanel.add(tree_table_splitPane, java.awt.BorderLayout.CENTER);

            jPanel5.setMinimumSize(new java.awt.Dimension(400, 28));
            jPanel5.setLayout(new java.awt.GridBagLayout());

            mainToolbar.setFloatable(false);
            mainToolbar.setRollover(true);
            javax.help.CSH.setHelpIDString (mainToolbar, _context.getHelpManager ().getResolver ().resolveHelpID (HelpResources.MAIN_TOOLBAR ));

            jButton5.setAction(new NewWorkSpaceAction ());
            jButton5.setFont(new java.awt.Font("Dialog", 0, 12));
            jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/toolbar_package-add.png"))); // NOI18N
            jButton5.setToolTipText(bundle.getString("New_workspace")); // NOI18N
            jButton5.setBorderPainted(false);
            jButton5.setMargin(null);
            mainToolbar.add(jButton5);

            jButton1.setAction(new NewTaskAction ());
            jButton1.setFont(new java.awt.Font("Dialog", 0, 12));
            jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/toolbar_file-new.png"))); // NOI18N
            jButton1.setToolTipText(bundle.getString("New_task_(Ctrl+N)")); // NOI18N
            jButton1.setBorderPainted(false);
            jButton1.setMargin(null);
            mainToolbar.add(jButton1);

            jButton2.setAction(new OpenWorkSpaceAction ());
            jButton2.setFont(new java.awt.Font("Dialog", 0, 12));
            jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/toolbar_folder-open.png"))); // NOI18N
            jButton2.setToolTipText(bundle.getString("Open_workspace_(Ctrl+O)")); // NOI18N
            jButton2.setBorderPainted(false);
            jButton2.setMargin(null);
            mainToolbar.add(jButton2);

            jSeparator5.setMaximumSize(null);
            jSeparator5.setMinimumSize(null);
            jSeparator5.setPreferredSize(null);
            mainToolbar.add(jSeparator5);

            jButton4.setAction(new NewPieceOfWorkAction ());
            jButton4.setFont(new java.awt.Font("Dialog", 0, 12));
            jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/toolbar_add.png"))); // NOI18N
            jButton4.setToolTipText(bundle.getString("New_action")); // NOI18N
            jButton4.setBorderPainted(false);
            jButton4.setMargin(null);
            mainToolbar.add(jButton4);

            jSeparator17.setMaximumSize(null);
            jSeparator17.setMinimumSize(null);
            jSeparator17.setPreferredSize(null);
            mainToolbar.add(jSeparator17);

            jButton3.setAction(new StartProgressAction ());
            jButton3.setFont(new java.awt.Font("Dialog", 0, 12));
            jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/toolbar_media-record.png"))); // NOI18N
            jButton3.setToolTipText(bundle.getString("<HTML>_Start_action_(Ctrl+S)_<BR>__<I>hold_SHIFT_key_pressed_for_options_dialog</I>_</HTML>")); // NOI18N
            jButton3.setBorderPainted(false);
            jButton3.setMargin(null);
            mainToolbar.add(jButton3);

            jButton10.setAction(new StopProgressAction ());
            jButton10.setFont(new java.awt.Font("Dialog", 0, 12));
            jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/toolbar_media-stop.png"))); // NOI18N
            jButton10.setToolTipText(bundle.getString("Stop_action_(Ctrl+T)")); // NOI18N
            jButton10.setBorderPainted(false);
            jButton10.setMargin(null);
            mainToolbar.add(jButton10);

            jSeparator14.setMaximumSize(null);
            jSeparator14.setMinimumSize(null);
            jSeparator14.setPreferredSize(null);
            mainToolbar.add(jSeparator14);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
            jPanel5.add(mainToolbar, gridBagConstraints);

            mainToolbar1.setFloatable(false);
            mainToolbar1.setRollover(true);
            javax.help.CSH.setHelpIDString (mainToolbar1, _context.getHelpManager ().getResolver ().resolveHelpID (HelpResources.MAIN_TOOLBAR ));

            jButton6.setAction(new TransferAction (TransferAction.Type.CUT, tal));
            jButton6.setFont(new java.awt.Font("Dialog", 0, 12));
            jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/toolbar_edit-cut.png"))); // NOI18N
            jButton6.setToolTipText(bundle.getString("Cut_(Ctrl+X)")); // NOI18N
            jButton6.setBorderPainted(false);
            jButton6.setMargin(null);
            jButton6.setText (null);
            mainToolbar1.add(jButton6);

            jButton7.setAction(new TransferAction (TransferAction.Type.COPY, tal));
            jButton7.setFont(new java.awt.Font("Dialog", 0, 12));
            jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/toolbar_edit-copy.png"))); // NOI18N
            jButton7.setToolTipText(bundle.getString("Copy_(Ctrl+C)")); // NOI18N
            jButton7.setBorderPainted(false);
            jButton7.setMargin(null);
            jButton7.setText (null);
            mainToolbar1.add(jButton7);

            jButton8.setAction(new TransferAction (TransferAction.Type.PASTE, tal));
            jButton8.setFont(new java.awt.Font("Dialog", 0, 12));
            jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/toolbar_edit-paste.png"))); // NOI18N
            jButton8.setToolTipText(bundle.getString("Paste_(Ctrl+V)")); // NOI18N
            jButton8.setBorderPainted(false);
            jButton8.setMargin(null);
            jButton8.setText (null);
            mainToolbar1.add(jButton8);

            undoButton.setAction(_context.getUndoManager ().getUndoAction());
            undoButton.setFont(new java.awt.Font("Dialog", 0, 12));
            undoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/toolbar_edit-undo.png"))); // NOI18N
            undoButton.setToolTipText(bundle.getString("Undo+SHORTCUT")); // NOI18N
            undoButton.setBorderPainted(false);
            undoButton.setMargin(null);
            /* mantiene nascosto il testo  dell'action */
            undoButton.setText (null);
            undoButton.putClientProperty ("hideActionText", Boolean.TRUE);
            mainToolbar1.add(undoButton);

            redoButton.setAction(_context.getUndoManager ().getRedoAction());
            redoButton.setFont(new java.awt.Font("Dialog", 0, 12));
            redoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/toolbar_edit-redo.png"))); // NOI18N
            redoButton.setToolTipText(bundle.getString("Redo+SHORTCUT")); // NOI18N
            redoButton.setBorderPainted(false);
            redoButton.setMargin(null);
            /* mantiene nascosto il testo  dell'action */
            redoButton.setText (null);
            redoButton.putClientProperty ("hideActionText", Boolean.TRUE);
            mainToolbar1.add(redoButton);

            jSeparator15.setMinimumSize(null);
            jSeparator15.setPreferredSize(null);
            mainToolbar1.add(jSeparator15);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
            jPanel5.add(mainToolbar1, gridBagConstraints);

            mainToolbar2.setFloatable(false);
            mainToolbar2.setRollover(true);
            //javax.help.CSH.setHelpIDString (mainToolbar2, _context.getHelpManager ().getResolver ().resolveHelpID (HelpResources.MAIN_TOOLBAR ));

            _context.getHelpManager ().initialize (helpButton);
            printButton.setAction(new StartReportAction ());
            printButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/toolbar_document-print.png"))); // NOI18N
            printButton.setToolTipText(bundle.getString("Print_(Ctrl+P)")); // NOI18N
            printButton.setBorderPainted(false);
            /* mantiene nascosto il testo  dell'action */
            redoButton.setText (null);
            redoButton.putClientProperty ("hideActionText", Boolean.TRUE);
            mainToolbar2.add(printButton);

            _context.getHelpManager ().initialize (helpButton);
            workspacesButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/toolbar_package.png"))); // NOI18N
            workspacesButton.setToolTipText(bundle.getString("MainWindow/Toolbar/ButtonTooltip/Workspaces")); // NOI18N
            workspacesButton.setBorderPainted(false);
            workspacesButton.setFocusable(false);
            workspacesButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            workspacesButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
            /* mantiene nascosto il testo  dell'action */
            redoButton.setText (null);
            redoButton.putClientProperty ("hideActionText", Boolean.TRUE);
            workspacesButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    workspacesButtontemplateMenuItemActionPerformed(evt);
                }
            });
            mainToolbar2.add(workspacesButton);

            _context.getHelpManager ().initialize (helpButton);
            actionTemplatesButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/toolbar_template.png"))); // NOI18N
            actionTemplatesButton.setToolTipText(bundle.getString("MainWIndow/Toolbar/ButtonTooltip/Templates")); // NOI18N
            actionTemplatesButton.setBorderPainted(false);
            actionTemplatesButton.setFocusable(false);
            actionTemplatesButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            actionTemplatesButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
            /* mantiene nascosto il testo  dell'action */
            redoButton.setText (null);
            redoButton.putClientProperty ("hideActionText", Boolean.TRUE);
            actionTemplatesButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    templateMenuItemActionPerformed(evt);
                }
            });
            mainToolbar2.add(actionTemplatesButton);

            _context.getHelpManager ().initialize (helpButton);
            helpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/toolbar_help-browser.png"))); // NOI18N
            helpButton.setToolTipText(bundle.getString("Show_help")); // NOI18N
            helpButton.setBorderPainted(false);
            /* mantiene nascosto il testo  dell'action */
            redoButton.setText (null);
            redoButton.putClientProperty ("hideActionText", Boolean.TRUE);
            mainToolbar2.add(helpButton);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
            jPanel5.add(mainToolbar2, gridBagConstraints);

            mainPanel.add(jPanel5, java.awt.BorderLayout.NORTH);

            statusPanel.setMinimumSize(new java.awt.Dimension(320, 20));
            statusPanel.setPreferredSize(new java.awt.Dimension(436, 20));
            javax.help.CSH.setHelpIDString (statusPanel, _context.getHelpManager ().getResolver ().resolveHelpID (HelpResources.STATUS_BAR));
            statusPanel.setLayout(new java.awt.GridBagLayout());

            appStatusLabel.setFont(new java.awt.Font("Dialog", 0, 12));
            org.openide.awt.Mnemonics.setLocalizedText(appStatusLabel, bundle.getString("IDLE")); // NOI18N
            appStatusLabel.setToolTipText("Running status");
            appStatusLabel.setMinimumSize(new java.awt.Dimension(60, 19));
            appStatusLabel.setPreferredSize(new java.awt.Dimension(50, 19));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.weightx = 0.2;
            gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
            statusPanel.add(appStatusLabel, gridBagConstraints);

            progressBar.setBorder(null);
            progressBar.setBorderPainted(false);
            progressBar.setFocusable(false);
            progressBar.setMinimumSize(new java.awt.Dimension(10, 6));
            progressBar.setPreferredSize(new java.awt.Dimension(50, 14));
            progressBar.setString("");
            progressBar.setVerifyInputWhenFocusTarget(false);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0;
            statusPanel.add(progressBar, gridBagConstraints);

            jPanel2.setMinimumSize(new java.awt.Dimension(300, 20));
            jPanel2.setPreferredSize(new java.awt.Dimension(300, 22));
            jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

            jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
            jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabel1.setText(bundle.getString("Task:")); // NOI18N
            jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 70, 20));

            tableTaskNameLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
            jPanel2.add(tableTaskNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 150, 20));

            jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
            jLabel2.setText(bundle.getString("Actions:")); // NOI18N
            jLabel2.setToolTipText(bundle.getString("Progress_count")); // NOI18N
            jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 0, -1, 20));

            tableProgressCountLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
            jPanel2.add(tableProgressCountLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 0, 20, 20));

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
            statusPanel.add(jPanel2, gridBagConstraints);

            mainPanel.add(statusPanel, java.awt.BorderLayout.SOUTH);

            getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

            menuBar.setFont(new java.awt.Font("Dialog", 0, 12));

            org.openide.awt.Mnemonics.setLocalizedText(fileMenu, bundle.getString("MainMenu/FileMenu/Text")); // NOI18N
            fileMenu.setFont(new java.awt.Font("Dialog", 0, 12));

            newWorkSpaceMenuItem.setAction(new NewWorkSpaceAction ());
            newWorkSpaceMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            newWorkSpaceMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/folder-new.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(newWorkSpaceMenuItem, bundle.getString("MainMenu/FileMenu/NewWorkspaceMenuItem/Text")); // NOI18N
            newWorkSpaceMenuItem.setToolTipText(bundle.getString("MainMenu/FileMenu/NewWorkspaceMenuItem/TooltipText")); // NOI18N
            newWorkSpaceMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    newWorkSpaceMenuItemActionPerformed(evt);
                }
            });
            fileMenu.add(newWorkSpaceMenuItem);

            newTaskMenuItem.setAction(new NewTaskAction ());
            newTaskMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            newTaskMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/file-new.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(newTaskMenuItem, bundle.getString("MainMenu/FileMenu/NewTaskMenuItem/Text")); // NOI18N
            newTaskMenuItem.setToolTipText(bundle.getString("MainMenu/FileMenu/NewTaskMenuItem/TooltipText")); // NOI18N
            newTaskMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    newTaskMenuItemActionPerformed(evt);
                }
            });
            fileMenu.add(newTaskMenuItem);
            fileMenu.add(jSeparator2);

            openWorkSpaceMenuItem.setAction(new OpenWorkSpaceAction ());
            openWorkSpaceMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
            openWorkSpaceMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            openWorkSpaceMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/folder-open.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(openWorkSpaceMenuItem, bundle.getString("MainMenu/FileMenu/OpenWorkspaceMenuItem/Text")); // NOI18N
            openWorkSpaceMenuItem.setToolTipText(bundle.getString("MainMenu/FileMenu/OpenWorkspaceMenuItem/TooltipText")); // NOI18N
            openWorkSpaceMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    openWorkSpaceMenuItemActionPerformed(evt);
                }
            });
            fileMenu.add(openWorkSpaceMenuItem);

            exportWorkSpaceMenuItem.setAction(new ExportWorkSpaceAction (_context));
            exportWorkSpaceMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            exportWorkSpaceMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/fileexport.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(exportWorkSpaceMenuItem, bundle.getString("MainMenu/FileMenu/ExportWorkspaceMenuItem/Text")); // NOI18N
            exportWorkSpaceMenuItem.setToolTipText(bundle.getString("MainMenu/FileMenu/ExportWorkspaceMenuItem/TooltipText")); // NOI18N
            exportWorkSpaceMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    exportWorkSpaceMenuItemActionPerformed(evt);
                }
            });
            fileMenu.add(exportWorkSpaceMenuItem);

            importWorkSpaceMenuItem.setAction(new ImportWorkSpaceAction (_context));
            importWorkSpaceMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            importWorkSpaceMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/fileimport.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(importWorkSpaceMenuItem, bundle.getString("MainMenu/FileMenu/ImportWorkspaceMenuItem/Text")); // NOI18N
            importWorkSpaceMenuItem.setToolTipText(bundle.getString("MainMenu/FileMenu/ImportWorkspaceMenuItem/TooltipText")); // NOI18N
            importWorkSpaceMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    importWorkSpaceMenuItemActionPerformed(evt);
                }
            });
            fileMenu.add(importWorkSpaceMenuItem);
            fileMenu.add(jSeparator3);

            printMenuItem.setAction(new StartReportAction ());
            printMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
            printMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            printMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/document-print.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(printMenuItem, bundle.getString("MainMenu/FileMenu/ReportingMenuItem/Text")); // NOI18N
            printMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    printMenuItemActionPerformed(evt);
                }
            });
            fileMenu.add(printMenuItem);
            fileMenu.add(jSeparator11);

            exitMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            exitMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/application-exit.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(exitMenuItem, bundle.getString("MainMenu/FileMenu/ExitMenuItem/Text")); // NOI18N
            exitMenuItem.setToolTipText(bundle.getString("MainMenu/FileMenu/ExitMenuItem/TooltipText")); // NOI18N
            exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    exitMenuItemActionPerformed(evt);
                }
            });
            fileMenu.add(exitMenuItem);

            menuBar.add(fileMenu);

            org.openide.awt.Mnemonics.setLocalizedText(editMenu, bundle.getString("MainMenu/EditMenu/Text")); // NOI18N
            editMenu.setFont(new java.awt.Font("Dialog", 0, 12));

            undoMenuItem.setAction(_context.getUndoManager ().getUndoAction());
            undoMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            undoMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/edit-undo.png"))); // NOI18N
            undoMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    undoMenuItemActionPerformed(evt);
                }
            });
            editMenu.add(undoMenuItem);

            redoMenuItem.setAction(_context.getUndoManager ().getRedoAction());
            redoMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            redoMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/edit-redo.png"))); // NOI18N
            editMenu.add(redoMenuItem);
            editMenu.add(jSeparator4);

            cutMenuItem.setAction(new TransferAction (TransferAction.Type.CUT, tal));
            cutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
            cutMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            cutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/edit-cut.png"))); // NOI18N
            cutMenuItem.setMnemonic('X');
            org.openide.awt.Mnemonics.setLocalizedText(cutMenuItem, bundle.getString("MainMenu/EditMenu/CutMenuItem/Text")); // NOI18N
            editMenu.add(cutMenuItem);

            copyMenuItem.setAction(new TransferAction (TransferAction.Type.COPY, tal));
            copyMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
            copyMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            copyMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/edit-copy.png"))); // NOI18N
            copyMenuItem.setMnemonic('C');
            org.openide.awt.Mnemonics.setLocalizedText(copyMenuItem, bundle.getString("MainMenu/EditMenu/CopyMenuItem/Text")); // NOI18N
            editMenu.add(copyMenuItem);

            pasteMenuItem.setAction(new TransferAction (TransferAction.Type.PASTE, tal));
            pasteMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
            pasteMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            pasteMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/edit-paste.png"))); // NOI18N
            pasteMenuItem.setMnemonic('V');
            org.openide.awt.Mnemonics.setLocalizedText(pasteMenuItem, bundle.getString("MainMenu/EditMenu/PasteMenuItem/Text")); // NOI18N
            editMenu.add(pasteMenuItem);

            deleteMenuItem.setAction(new TransferAction ("delete", tal));
            deleteMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
            deleteMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            deleteMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/edit-delete.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(deleteMenuItem, bundle.getString("MainMenu/EditMenu/DeleteMenuItem/Text")); // NOI18N
            editMenu.add(deleteMenuItem);

            menuBar.add(editMenu);

            org.openide.awt.Mnemonics.setLocalizedText(viewMenu, bundle.getString("MainMenu/ViewMenu/Text")); // NOI18N
            viewMenu.setFont(new java.awt.Font("Dialog", 0, 12));

            TabShowingChoiceButtonGroup.add(showActionsMenuItem);
            showActionsMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            showActionsMenuItem.setSelected(true);
            org.openide.awt.Mnemonics.setLocalizedText(showActionsMenuItem, bundle.getString("MainMenu/ViewMenu/ViewActionListMenuItem/Text")); // NOI18N
            showActionsMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    showActionsMenuItemActionPerformed(evt);
                }
            });
            viewMenu.add(showActionsMenuItem);

            TabShowingChoiceButtonGroup.add(showChartsMenuItem);
            showChartsMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            org.openide.awt.Mnemonics.setLocalizedText(showChartsMenuItem, bundle.getString("MainMenu/ViewMenu/ViewChartMenuItem/Text")); // NOI18N
            showChartsMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    showChartsMenuItemActionPerformed(evt);
                }
            });
            viewMenu.add(showChartsMenuItem);
            viewMenu.add(jSeparator16);

            linkTableBackgroundMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            org.openide.awt.Mnemonics.setLocalizedText(linkTableBackgroundMenuItem, bundle.getString("MainMenu/ViewMenu/ShowChartShadowMenuItem/Text")); // NOI18N
            linkTableBackgroundMenuItem.setSelected (_context.getApplicationOptions ().isChartGhostEnabled ());
            linkTableBackgroundMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    linkTableBackgroundMenuItemActionPerformed(evt);
                }
            });
            viewMenu.add(linkTableBackgroundMenuItem);

            menuBar.add(viewMenu);

            org.openide.awt.Mnemonics.setLocalizedText(tasksMenu, bundle.getString("MainMenu/TasksMenu/Text")); // NOI18N
            tasksMenu.setFont(new java.awt.Font("Dialog", 0, 12));

            jMenuItem1.setAction(new RenameTaskAction ());
            jMenuItem1.setFont(new java.awt.Font("Dialog", 0, 12));
            jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/transparent.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(jMenuItem1, bundle.getString("MainMenu/TasksMenu/RenameTaskMenuItem/Text")); // NOI18N
            jMenuItem1.setToolTipText(bundle.getString("Rename_selected_task")); // NOI18N
            tasksMenu.add(jMenuItem1);

            jMenuItem2.setAction(new StartTaskEditAction ());
            jMenuItem2.setFont(new java.awt.Font("Dialog", 0, 12));
            jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/transparent.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(jMenuItem2, bundle.getString("MainMenu/TasksMenu/TaskPropertiesMenuItem/Text")); // NOI18N
            tasksMenu.add(jMenuItem2);

            menuBar.add(tasksMenu);

            org.openide.awt.Mnemonics.setLocalizedText(actionstMenu, bundle.getString("MainMenu/ActionsMenu/Text")); // NOI18N
            actionstMenu.setFont(new java.awt.Font("Dialog", 0, 12));

            newProgressMenuItem.setAction(new NewPieceOfWorkAction ());
            newProgressMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            newProgressMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/add.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(newProgressMenuItem, bundle.getString("MainMenu/ActionsMenu/AddActionMenuItem/Text")); // NOI18N
            newProgressMenuItem.setToolTipText(bundle.getString("MainMenu/ActionsMenu/AddActionMenuItem/TooltipText")); // NOI18N
            newProgressMenuItem.setActionCommand("newProgress");
            newProgressMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    newProgressMenuItemActionPerformed(evt);
                }
            });
            actionstMenu.add(newProgressMenuItem);

            startProgressMenuItem.setAction(new StartProgressAction ());
            startProgressMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
            startProgressMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            startProgressMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/media-record.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(startProgressMenuItem, bundle.getString("MainMenu/ActionsMenu/StartActionMenuItem/Text")); // NOI18N
            startProgressMenuItem.setToolTipText(bundle.getString("MainMenu/ActionsMenu/StartActionMenuItem/TooltipText")); // NOI18N
            startProgressMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    startProgressMenuItemActionPerformed(evt);
                }
            });
            actionstMenu.add(startProgressMenuItem);

            startProgressCloneMenuItem.setAction(new StartProgressCloneAction ());
            startProgressCloneMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            startProgressCloneMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/transparent.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(startProgressCloneMenuItem, bundle.getString("MainMenu/ActionsMenu/StartActionCloneMenuItem/Text")); // NOI18N
            startProgressCloneMenuItem.setToolTipText(bundle.getString("MainMenu/ActionsMenu/StartActionCloneMenuItem/TooltipText")); // NOI18N
            startProgressCloneMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    startProgressCloneMenuItemActionPerformed(evt);
                }
            });
            actionstMenu.add(startProgressCloneMenuItem);

            continueProgressMenuItem.setAction(new ContinueProgressAction ());
            continueProgressMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            continueProgressMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/transparent.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(continueProgressMenuItem, bundle.getString("MainMenu/ActionsMenu/ContinueActionMenuItem/Text")); // NOI18N
            continueProgressMenuItem.setToolTipText(bundle.getString("MainMenu/ActionsMenu/ContinueActionMenuItem/TooltipText")); // NOI18N
            continueProgressMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    continueProgressMenuItemActionPerformed(evt);
                }
            });
            actionstMenu.add(continueProgressMenuItem);
            actionstMenu.add(jSeparator13);

            stopProgressMenuItem.setAction(new StopProgressAction ());
            stopProgressMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
            stopProgressMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            stopProgressMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/media-stop.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(stopProgressMenuItem, bundle.getString("MainMenu/ActionsMenu/StopActionMenuItem/Text")); // NOI18N
            stopProgressMenuItem.setToolTipText(bundle.getString("MainMenu/ActionsMenu/StopActionMenuItem/TooltipText")); // NOI18N
            stopProgressMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    stopProgressMenuItemActionPerformed(evt);
                }
            });
            actionstMenu.add(stopProgressMenuItem);

            menuBar.add(actionstMenu);

            org.openide.awt.Mnemonics.setLocalizedText(toolsMenu, bundle.getString("MainMenu/ToolsMenu/Text")); // NOI18N
            toolsMenu.setFont(new java.awt.Font("Dialog", 0, 12));

            logConsoleMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.ALT_MASK));
            logConsoleMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            logConsoleMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/console.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(logConsoleMenuItem, bundle.getString("MainMenu/ToolsMenu/LogMenuItem/Text")); // NOI18N
            logConsoleMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    logConsoleMenuItemActionPerformed(evt);
                }
            });
            toolsMenu.add(logConsoleMenuItem);

            workspaceMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.ALT_MASK));
            workspaceMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            workspaceMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/folder.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(workspaceMenuItem, bundle.getString("MainMenu/ToolsMenu/WorkspacesMenuItem/Text")); // NOI18N
            workspaceMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    workspaceMenuItemActionPerformed(evt);
                }
            });
            toolsMenu.add(workspaceMenuItem);

            templateMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.ALT_MASK));
            templateMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            templateMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/tag_blue.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(templateMenuItem, bundle.getString("MainMenu/ToolsMenu/TemplatesMenuItem/Text")); // NOI18N
            templateMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    templateMenuItemActionPerformed(evt);
                }
            });
            toolsMenu.add(templateMenuItem);
            toolsMenu.add(jSeparator8);

            optionsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.ALT_MASK));
            optionsMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            optionsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/cog.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(optionsMenuItem, bundle.getString("MainMenu/ToolsMenu/OptionsMenuItem/Text")); // NOI18N
            optionsMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    optionsMenuItemActionPerformed(evt);
                }
            });
            toolsMenu.add(optionsMenuItem);

            menuBar.add(toolsMenu);

            org.openide.awt.Mnemonics.setLocalizedText(helpMenu, bundle.getString("MainMenu/HelpMenu/Text")); // NOI18N
            helpMenu.setFont(new java.awt.Font("Dialog", 0, 12));

            _context.getHelpManager ().initialize (contentsMenuItem);
            contentsMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            contentsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/help-browser.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(contentsMenuItem, bundle.getString("MainMenu/HelpMenu/ContentsMenuItem/Text")); // NOI18N
            contentsMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    contentsMenuItemActionPerformed(evt);
                }
            });
            helpMenu.add(contentsMenuItem);

            contextHelpMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            contextHelpMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/transparent.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(contextHelpMenuItem, bundle.getString("MainMenu/HelpMenu/ContextualHelpMenuItem/Text")); // NOI18N
            contextHelpMenuItem.addActionListener (new CSH.DisplayHelpAfterTracking (_context.getHelpManager ().getMainHelpBroker ()));
            helpMenu.add(contextHelpMenuItem);
            helpMenu.add(jSeparator7);

            aboutMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            aboutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/dialog-information.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(aboutMenuItem, bundle.getString("MainMenu/HelpMenu/AboutMenuItem/Text")); // NOI18N
            aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    aboutMenuItemActionPerformed(evt);
                }
            });
            helpMenu.add(aboutMenuItem);

            menuBar.add(helpMenu);

            setJMenuBar(menuBar);

            java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            setBounds((screenSize.width-802)/2, (screenSize.height-600)/2, 802, 600);
        }// </editor-fold>//GEN-END:initComponents

	private void traMenuShowMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_traMenuShowMenuItemActionPerformed
		bringToFront ();
	}//GEN-LAST:event_traMenuShowMenuItemActionPerformed

	private void trayMenuExitActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trayMenuExitActionPerformed
		/*
		 * Come voce menu Esci
		 */
		exitMenuItemActionPerformed(evt);
	}//GEN-LAST:event_trayMenuExitActionPerformed

	private void chartDepthSliderFocusLost (java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chartDepthSliderFocusLost
		tbp.repaintProgressTableBackground ();
	}//GEN-LAST:event_chartDepthSliderFocusLost

	private void progressesTablePanelComponentShown (java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_progressesTablePanelComponentShown
		showActionsMenuItem.doClick ();
	}//GEN-LAST:event_progressesTablePanelComponentShown

	private void jPanel1ComponentShown (java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel1ComponentShown
		showChartsMenuItem.doClick ();
		tbp.revalidateIfNeeded ();
	}//GEN-LAST:event_jPanel1ComponentShown

	private void jPanel1FocusLost (java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel1FocusLost
		
	}//GEN-LAST:event_jPanel1FocusLost

	private void progressesTablePanelFocusLost (java.awt.event.FocusEvent evt) {//GEN-FIRST:event_progressesTablePanelFocusLost
		
	}//GEN-LAST:event_progressesTablePanelFocusLost

	private void progressesTablePanelFocusGained (java.awt.event.FocusEvent evt) {//GEN-FIRST:event_progressesTablePanelFocusGained

	}//GEN-LAST:event_progressesTablePanelFocusGained

	private void jPanel1FocusGained (java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel1FocusGained

	}//GEN-LAST:event_jPanel1FocusGained

	private void mainContentsTabbedPaneFocusGained (java.awt.event.FocusEvent evt) {//GEN-FIRST:event_mainContentsTabbedPaneFocusGained

	}//GEN-LAST:event_mainContentsTabbedPaneFocusGained

	private void linkTableBackgroundMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkTableBackgroundMenuItemActionPerformed
		_context.getUserSettings ().setChartGhostEnabled (linkTableBackgroundMenuItem.isSelected ());
		setTableBackoundImage (DEFAULT_PROGRESSES_TABLE_BACKGROUND);
		/*
		 * @workaround chiamata necessaria per ridisegnare lo sfondo della tabella anche quandoil thread dighost non gira (disabilitato)
		 */
		progressesTable.repaint ();
		tbp.repaintProgressTableBackground ();
	}//GEN-LAST:event_linkTableBackgroundMenuItemActionPerformed

	private void showChartsMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showChartsMenuItemActionPerformed
		mainContentsTabbedPane.setSelectedIndex (1);
	}//GEN-LAST:event_showChartsMenuItemActionPerformed

	private void showActionsMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showActionsMenuItemActionPerformed
		mainContentsTabbedPane.setSelectedIndex (0);
	}//GEN-LAST:event_showActionsMenuItemActionPerformed

	private void chartPanelComponentResized (java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_chartPanelComponentResized
		tbp.repaintProgressTableBackground ();
	}//GEN-LAST:event_chartPanelComponentResized

	private void chartDepthSliderStateChanged (javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chartDepthSliderStateChanged
		final int v = chartDepthSlider.getValue ();
		((RingChartPanel)chartPanel).setDepth (v);
		_context.getUserSettings ().setCharDepth (Integer.valueOf (v));
	}//GEN-LAST:event_chartDepthSliderStateChanged

	private void renameTaskPopupItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renameTaskPopupItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_renameTaskPopupItemActionPerformed

	private void stopProgressMenuItem1ActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopProgressMenuItem1ActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_stopProgressMenuItem1ActionPerformed

	private void stopProgressMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopProgressMenuItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_stopProgressMenuItemActionPerformed

	private void newProgressMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newProgressMenuItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_newProgressMenuItemActionPerformed

	private void continueProgressMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_continueProgressMenuItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_continueProgressMenuItemActionPerformed

	private void startProgressMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startProgressMenuItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_startProgressMenuItemActionPerformed

	private void startProgressCloneMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startProgressCloneMenuItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_startProgressCloneMenuItemActionPerformed

	private void continueProgressPopupItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_continueProgressPopupItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_continueProgressPopupItemActionPerformed

	private void startProgressClonePopupItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startProgressClonePopupItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_startProgressClonePopupItemActionPerformed

	private void printMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printMenuItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_printMenuItemActionPerformed
	
	private void startProgressPopupItem1ActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startProgressPopupItem1ActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_startProgressPopupItem1ActionPerformed
	
	private void newProgressPopupItem1ActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newProgressPopupItem1ActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_newProgressPopupItem1ActionPerformed
	
	private void startProgressPopupItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startProgressPopupItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_startProgressPopupItemActionPerformed
	
	private void taskTreeMousePressed (java.awt.event.MouseEvent evt) {//GEN-FIRST:event_taskTreeMousePressed
		/*
		 * Gestione click tasto destro
		 */
		if (evt.getButton ()!=MouseEvent.BUTTON3) {
			return;
		}
		final int clickedRow = taskTree.getRowForPath (taskTree.getPathForLocation (evt.getX (), evt.getY ()));
		if (clickedRow >=0) {
			final int[] selectedRows = taskTree.getSelectedRows ();
			Arrays.sort (selectedRows);
			if (Arrays.binarySearch (selectedRows, clickedRow)<0) {
				//la riga cliccata non e' attualmente selezionata: seleziona solo quella
				taskTree.getSelectionModel ().clearSelection ();
				taskTree.getSelectionModel ().addSelectionInterval (clickedRow, clickedRow);
			}
		}
		treePopupMenu.show (taskTree, evt.getX (), evt.getY ());
	}//GEN-LAST:event_taskTreeMousePressed
	
	private void exportWorkSpaceMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportWorkSpaceMenuItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_exportWorkSpaceMenuItemActionPerformed
	
	private void importWorkSpaceMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importWorkSpaceMenuItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_importWorkSpaceMenuItemActionPerformed
	
	private void openWorkSpaceMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openWorkSpaceMenuItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_openWorkSpaceMenuItemActionPerformed
	
	private void progressesTableMousePressed (java.awt.event.MouseEvent evt) {//GEN-FIRST:event_progressesTableMousePressed
//		prepareTablePopupMenu (evt);
	}//GEN-LAST:event_progressesTableMousePressed
	
	private void tablePopupMenuPopupMenuWillBecomeVisible (javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_tablePopupMenuPopupMenuWillBecomeVisible
//		prepareTablePopupMenu (evt);
	}//GEN-LAST:event_tablePopupMenuPopupMenuWillBecomeVisible
	
	private void treePopupMenuPopupMenuWillBecomeVisible (javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_treePopupMenuPopupMenuWillBecomeVisible
		prepareTreePopupMenu (evt);
	}//GEN-LAST:event_treePopupMenuPopupMenuWillBecomeVisible
	
	private void newTaskMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newTaskMenuItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_newTaskMenuItemActionPerformed
	
	private void optionsMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionsMenuItemActionPerformed
		_context.getWindowManager ().getOptionsDialog ().show ();
	}//GEN-LAST:event_optionsMenuItemActionPerformed
	
    private void logConsoleMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logConsoleMenuItemActionPerformed
		_context.getWindowManager ().getLogConsole ().show ();
    }//GEN-LAST:event_logConsoleMenuItemActionPerformed
	
	private void contentsMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contentsMenuItemActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_contentsMenuItemActionPerformed
	
	private void deleteProgressPopupItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteProgressPopupItemActionPerformed
		
		
	}//GEN-LAST:event_deleteProgressPopupItemActionPerformed
	
	private void undoMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoMenuItemActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_undoMenuItemActionPerformed
	
	private void formWindowDeactivated (java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
							}//GEN-LAST:event_formWindowDeactivated
	
	private void aboutMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
		_wm.getAbout ().show ();
	}//GEN-LAST:event_aboutMenuItemActionPerformed
		
	private void saveFileChooserActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveFileChooserActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_saveFileChooserActionPerformed
	
	private void newTaskPopupItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newTaskPopupItemActionPerformed
		this._actionNotifier.fireActionPerformed (new ActionEvent (this, -1, "showNewTaskDialog"));
	}//GEN-LAST:event_newTaskPopupItemActionPerformed
	
	private void newWorkSpaceMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newWorkSpaceMenuItemActionPerformed
			}//GEN-LAST:event_newWorkSpaceMenuItemActionPerformed
	
	private void newProgressPopupItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newProgressPopupItemActionPerformed
			}//GEN-LAST:event_newProgressPopupItemActionPerformed
	
	private void exitMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
		/*
		 * Questo evento notifica la richiesta di chiusura della finestra.
		 * Il listener registrato da Application dovrebbe provvedere alla chisuura effettiva dell'applicazione.
		 */
		dispatchEvent (new WindowEvent (this, WindowEvent.WINDOW_CLOSING));
	}//GEN-LAST:event_exitMenuItemActionPerformed

private void templateMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_templateMenuItemActionPerformed
	_context.getWindowManager ().getActionTemplatesDialog ().show ();
}//GEN-LAST:event_templateMenuItemActionPerformed

private void addToTemplatesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addToTemplatesMenuItemActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_addToTemplatesMenuItemActionPerformed

private void taskTreeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_taskTreeMouseClicked
		/*
		 * Gestione click tasto sinistro
		 */
//		if (evt.getButton ()==MouseEvent.BUTTON1) {
//			if (evt.getClickCount()==1) {
//
//				final int row = taskTree.getRowForPath (taskTree.getPathForLocation (evt.getX (), evt.getY ()));
//////				if (row >=0 && taskTree.getSelectedRow()==row && !taskTreeSelectionChanged) {
//					evt.consume();
//					SwingUtilities.invokeLater(new Runnable() {
//
//						public void run() {
//							/*
//							 * avvia editazione nome task
//							 */
//							taskTree.editCellAt (row, TaskJTreeModel.TREE_COLUMN_INDEX);
//							if (!taskTree.isEditing() && taskTree.getCellEditor () instanceof TreeTableCellEditor) {
//								final JTextField editor = (JTextField)taskTree.getCellEditor ().getTableCellEditorComponent (taskTree, taskTree.getModel ().getValueAt (row, TaskJTreeModel.TREE_COLUMN_INDEX), true, row, TaskJTreeModel.TREE_COLUMN_INDEX);
//								editor.selectAll ();
//								editor.requestFocusInWindow ();
//							}
//						}
//					});
//					return;
//				}
//
//			}
//		}

}//GEN-LAST:event_taskTreeMouseClicked

private void workspaceMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_workspaceMenuItemActionPerformed
	_context.getWindowManager ().getWorkspacesDialog ().show ();
}//GEN-LAST:event_workspaceMenuItemActionPerformed

private void workspacesButtontemplateMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_workspacesButtontemplateMenuItemActionPerformed
	_context.getWindowManager ().getWorkspacesDialog ().show ();
}//GEN-LAST:event_workspacesButtontemplateMenuItemActionPerformed
	
	public String getPersistenceKey () {
		return "mainwindow";
	}
	
	public void makePersistent (net.sf.jttslite.common.gui.persistence.PersistenceStorage props) {
		PersistenceUtils.makeBoundsPersistent (props, this.getPersistenceKey (), this);
	}
	
	public boolean restorePersistent (net.sf.jttslite.common.gui.persistence.PersistenceStorage props) {
		return PersistenceUtils.restorePersistentBounds (props, this.getPersistenceKey (), this);
	}
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup TabShowingChoiceButtonGroup;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JButton actionTemplatesButton;
    private javax.swing.JMenu actionstMenu;
    private javax.swing.JMenuItem addToTemplatesMenuItem;
    private javax.swing.JLabel appStatusLabel;
    private java.awt.BorderLayout borderLayout1;
    private javax.swing.JSlider chartDepthSlider;
    private javax.swing.JPanel chartPanel;
    private javax.swing.JMenuItem collapsePopupItem;
    private javax.swing.JMenuItem contentsMenuItem;
    private javax.swing.JMenuItem contextHelpMenuItem;
    private javax.swing.JMenuItem continueProgressMenuItem;
    private javax.swing.JMenuItem continueProgressPopupItem;
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JMenuItem copyProgressesMenuItem1;
    private javax.swing.JMenuItem copyTasksMenuItem;
    private javax.swing.JMenuItem cutMenuItem;
    private javax.swing.JMenuItem cutProgressesMenuItem1;
    private javax.swing.JMenuItem cutTasksMenuItem;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JMenuItem deleteProgressPopupItem;
    private javax.swing.JMenuItem deleteTaskPopupItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem editTaskPopupItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenuItem expandPopupItem;
    private javax.swing.JMenuItem exportWorkSpaceMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JButton helpButton;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem importWorkSpaceMenuItem;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JCheckBoxMenuItem linkTableBackgroundMenuItem;
    private javax.swing.JMenuItem logConsoleMenuItem;
    private javax.swing.JTabbedPane mainContentsTabbedPane;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JToolBar mainToolbar;
    private javax.swing.JToolBar mainToolbar1;
    private javax.swing.JToolBar mainToolbar2;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem newProgressMenuItem;
    private javax.swing.JMenuItem newProgressPopupItem;
    private javax.swing.JMenuItem newProgressPopupItem1;
    private javax.swing.JMenuItem newTaskMenuItem;
    private javax.swing.JMenuItem newTaskPopupItem;
    private javax.swing.JMenuItem newWorkSpaceMenuItem;
    private javax.swing.JFileChooser openFileChooser;
    private javax.swing.JMenuItem openWorkSpaceMenuItem;
    private javax.swing.JMenuItem optionsMenuItem;
    private javax.swing.JMenuItem pasteMenuItem;
    private javax.swing.JMenuItem pasteProgressesMenuItem1;
    private javax.swing.JMenuItem pasteTasksMenuItem;
    private javax.swing.JButton printButton;
    private javax.swing.JMenuItem printMenuItem;
    private javax.swing.JProgressBar progressBar;
    private org.jdesktop.swingx.JXTable progressesTable;
    private javax.swing.JPanel progressesTablePanel;
    private javax.swing.JScrollPane progressesTableScrollPane;
    private javax.swing.JButton redoButton;
    private javax.swing.JMenuItem redoMenuItem;
    private javax.swing.JMenuItem renameTaskPopupItem;
    private javax.swing.JFileChooser saveFileChooser;
    private javax.swing.JRadioButtonMenuItem showActionsMenuItem;
    private javax.swing.JRadioButtonMenuItem showChartsMenuItem;
    private javax.swing.JMenuItem startProgressCloneMenuItem;
    private javax.swing.JMenuItem startProgressClonePopupItem;
    private javax.swing.JMenuItem startProgressMenuItem;
    private javax.swing.JMenuItem startProgressPopupItem;
    private javax.swing.JMenuItem startProgressPopupItem1;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JMenuItem stopProgressMenuItem;
    private javax.swing.JMenuItem stopProgressMenuItem1;
    private javax.swing.JPopupMenu tablePopupMenu;
    private javax.swing.JLabel tableProgressCountLabel;
    private javax.swing.JLabel tableTaskNameLabel;
    private org.jdesktop.swingx.JXTreeTable taskTree;
    private javax.swing.JPanel taskTreePanel;
    private javax.swing.JMenu tasksMenu;
    private javax.swing.JMenuItem templateMenuItem;
    private javax.swing.JMenu toolsMenu;
    private java.awt.PopupMenu trayMenu;
    private java.awt.MenuItem trayMenuExitMenuItem;
    private java.awt.MenuItem trayMenuShowMenuItem;
    private java.awt.MenuItem trayMenuStartProgressMenuItem;
    private java.awt.MenuItem trayMenuStopProgressMenuItem;
    private javax.swing.JPopupMenu treePopupMenu;
    private javax.swing.JSplitPane tree_table_splitPane;
    private javax.swing.JButton undoButton;
    private javax.swing.JMenuItem undoMenuItem;
    private javax.swing.JMenu viewMenu;
    private javax.swing.JMenuItem workspaceMenuItem;
    private javax.swing.JButton workspacesButton;
    // End of variables declaration//GEN-END:variables
	
	
	private class PersistenceTreeAdapter implements PersistentComponent	{
		
		private final JComponent _tree;
		public PersistenceTreeAdapter (JComponent tree){
			this._tree = tree;
		}
		public String getPersistenceKey () {
			return "tasktree";
		}
		
		public void makePersistent (net.sf.jttslite.common.gui.persistence.PersistenceStorage props) {
			PersistenceUtils.makeBoundsPersistent (props, this.getPersistenceKey (), this._tree);
		}
		
		public boolean restorePersistent (net.sf.jttslite.common.gui.persistence.PersistenceStorage props) {
			return PersistenceUtils.restorePersistentBoundsToPreferredSize (props, this.getPersistenceKey (), this._tree);
		}
		
	}
	
	/**
	 * Implementa la persistenza delle dimensioni per un pannello.
	 */
	private class PersistencePanelAdapter implements PersistentComponent	{
		
		private final JComponent _panel;
		private final String _key;
		public PersistencePanelAdapter (JPanel panel, String key){
			this._panel = panel;
			this._key = key;
		}
		public String getPersistenceKey () {
			return _key;
		}
		
		public void makePersistent (net.sf.jttslite.common.gui.persistence.PersistenceStorage props) {
			PersistenceUtils.makeBoundsPersistent (props, this.getPersistenceKey (), this._panel);
		}
		
		public boolean restorePersistent (net.sf.jttslite.common.gui.persistence.PersistenceStorage props) {
			return PersistenceUtils.restorePersistentBoundsToPreferredSize (props, this.getPersistenceKey (), this._panel);
		}
		
	}
	
	private final static String[] voidStringArray = new String[0];
	
	
	
	
	
	
	
	
	
	/**********************************************
	 * INIZIO SEZIONE DEDICATA AI MODELLI
	 **********************************************/
	
	
	
	/**
	 * Indice della colonna di durata.
	 * <BR>
	 *ATTENZIONE: per questa colonna il modello ritorna il valore del PieceOfWork.
	 */
	public final static int DURATION_COL_INDEX = 0;
	
	/**
	 * Indice della colonna di inizio.
	 */
	public final static int START_COL_INDEX = 1;
	
	/**
	 * Indice della colonna di fine.
	 */
	public final static int END_COL_INDEX = 2;
	
	/**
	 * Indice della colonna di descrizione.
	 */
	public final int DESCRIPTION_COL_INDEX = 3;
	
	/**
	 * Il modello della tabella degli avanzamenti.
	 */
	private class ProgressesJTableModel extends AbstractTableModel implements TaskTreeModelListener, TreeSelectionListener, TableModelListener, AdvancingTicListener, WorkAdvanceModelListener {
		
		private final TaskTreeModelImpl _taskTreeModel;
		
		private Task _master;
		private WorkSpace _workspace;
		private TaskTreePath _masterPath;
		private List<PieceOfWork> _progresses;
		
		
		public ProgressesJTableModel (final TaskTreeModelImpl ttm){
			this._taskTreeModel = ttm;
			reload (null, null);
			fireTableStructureChanged ();
//			ttm.addTaskTreeModelListener (this);
//			ttm.addWorkAdvanceModelListener (this);
//			_ticPump.addAdvancingTicListener (this);
		}
		
		public int getColumnCount () {
			return 4;
		}
		
		public int getRowCount () {
			if (null==_progresses) {
				return 0;
			}
			return _progresses.size ();
		}
		
		private PieceOfWork getPieceOfWork (int rowIndex) {
			return _progresses.get (rowIndex);
		}
		public Object getValueAt (int rowIndex, int columnIndex) {
			switch (columnIndex) {
				case DURATION_COL_INDEX:
					return getPieceOfWork (rowIndex);
//				case 1:
//					return _progresses.get (rowIndex).getTask ().getName ();
				case START_COL_INDEX:
					return getPieceOfWork (rowIndex).getFrom ();
				case END_COL_INDEX:
					return getPieceOfWork (rowIndex).getTo ();
				case DESCRIPTION_COL_INDEX:
					return getPieceOfWork (rowIndex).getDescription ();
			}
			return null;
		}
		
		
		private void reload (WorkSpace workspace, Task newMaster){
			_workspace = workspace;
			_master = newMaster;
			cache ();
			fireTableDataChanged ();
		}
		
		private void cache (){
			if (_master == null){
				/*
				 * master non impostato, modello vuoto.
				 */
				_progresses = new ArrayList ();
				_masterPath = null;
				return;
			} else {
				_progresses = _master.getPiecesOfWork ();
				_masterPath = new TaskTreePath (_workspace, _master);
			}
			cacheAdvancingProgress ();
		}
		
		private final String[] _columnNames = new String[] {
			java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("table_header/Duration")
			, /*"Task", */
			java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("table_header/From"),
			java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("table_header/To"),
			java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("table_header/Notes")
		};
		
		@Override
		public String getColumnName (int columnIndex) {
			return _columnNames[columnIndex];
		}
		
		
		@Override
		public boolean isCellEditable (int rowIndex, int columnIndex) {
			return
				//action terminata
				!getPieceOfWork (rowIndex).isEndOpened ()
				//oppure in progress, e allora esclude data fine e durata
				|| (columnIndex!=DURATION_COL_INDEX && columnIndex!=END_COL_INDEX);
		}
		
		private final Class[] _columnClasses = new Class[] {
			Duration.class,
			Date.class,
			Date.class,
			String.class
		};
		@Override
		public Class getColumnClass (int col) {
			return _columnClasses[col];
		}
		
		/**
		 * Imposta il valore nel modello della tabella, propagandolo al modello applicativo.
		 */
		@Override
		public void setValueAt (Object aValue, int rowIndex, int columnIndex) {
			switch (columnIndex) {
				case DURATION_COL_INDEX:
				{
					/*
					 * @warning
					 * asimmetria: riceve una Duration, anche se getValue ritorna una PieceOfWork
					 * Cio' e' dovuto al fatto che l'editazione non imposta date di inizio e fine, ma solo una durata
					 */
					
					final PieceOfWork pow = getPieceOfWork (rowIndex);
					
					
					_context.getModel ().updatePieceOfWork (pow, pow.getFrom (), new Date (pow.getFrom ().getTime ()+((Duration)aValue).getTime ()), pow.getDescription ());
					break;
				}
				case START_COL_INDEX:
				{
					final PieceOfWork pow = getPieceOfWork (rowIndex);
					_context.getModel ().updatePieceOfWork (pow, (Date)aValue, pow.getTo (), pow.getDescription ());
					break;
				}
				case END_COL_INDEX:
				{
					final PieceOfWork pow = getPieceOfWork (rowIndex);
					_context.getModel ().updatePieceOfWork (pow, pow.getFrom (), (Date)aValue, pow.getDescription ());
					break;
				}
				case DESCRIPTION_COL_INDEX:
				{
					final PieceOfWork pow = getPieceOfWork (rowIndex);
					_context.getModel ().updatePieceOfWork (pow, pow.getFrom (), pow.getTo (), (String)aValue);
					break;
				}
				default:
					return;
			}
		}
		
		/**
		 * Gestore delle modifiche ai nodi dell'albero di modello principale.
		 */
		private TaskTreeModelEvent.Inspector _treeNodesChangedInspector = new TaskTreeModelEvent.Inspector (){
			public Object inspectTASK (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, Task[] children){
				//nessun effetto in caso di modifica ai task
				return null;
			}
			public Object inspectPROGRESS (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, PieceOfWork[] progresses){
				if (_masterPath==null || !path.equals (_masterPath)) {
					/*
					 * Evento esterno al percorso di interesse.
					 */
					return null;
				}
				cache ();
				if (childIndices.length==0) {
					/*
					 * Modifica estesa agli avanzamenti del percorso
					 */
					fireTableDataChanged ();
				} else {
					/*
					 * Modifiche puntuali agli avanzamenti del percorso
					 */
					for (int i=0;i<childIndices.length;i++){
						fireTableRowsUpdated (childIndices[i], childIndices[i]);
					}
				}
				return null;
			}
		};
		public void treeNodesChanged (TaskTreeModelEvent e) {
			checkForReload (e);
			e.allow (_treeNodesChangedInspector);
		}
		
		/**
		 * Gestore degli inserimenti nell'albero di modello principale.
		 */
		private TaskTreeModelEvent.Inspector _treeNodesInsertedInspector = new TaskTreeModelEvent.Inspector (){
			public Object inspectTASK (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, Task[] children){
				//nessun effetto in caso di inserimento task
				return null;
			}
			public Object inspectPROGRESS (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, PieceOfWork[] progresses){
				if (!path.equals (_masterPath)) {
					/*
					 * Evento esterno al percorso di interesse.
					 */
					return null;
				}
				cache ();
				if (childIndices.length==0) {
					/*
					 * Modifica estesa agli avanzamenti del percorso
					 */
					fireTableDataChanged ();
				} else {
					/*
					 * Inserimenti puntuali di avanzamenti nel percorso
					 */
					for (int i=0;i<childIndices.length;i++){
						fireTableRowsInserted (childIndices[i], childIndices[i]);
					}
				}
				return null;
			}
		};
		public void treeNodesInserted (TaskTreeModelEvent e) {
			e.allow (_treeNodesInsertedInspector);
		}
		
		/**
		 * Gestore delle rimozioni nell'albero di modello principale.
		 */
		private TaskTreeModelEvent.Inspector _treeNodesRemovedInspector = new TaskTreeModelEvent.Inspector (){
			public Object inspectTASK (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, Task[] children){
				if (null==_masterPath) {
					/*
					 * modello in stato inattivo
					 */
					return null;
				}
				if (!_masterPath.contains (path)) {
					/*
					 * Evento esterno al percorso di interesse.
					 */
					return null;
				}
				for (int i=0;i<children.length;i++){
					if (_masterPath.contains (children[i])){
						/*
						 * Cambia master
						 */
						reload (null, null);
						break;
					}
				}
				return null;
			}
			public Object inspectPROGRESS (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, PieceOfWork[] progresses){
				if (!path.equals (_masterPath)) {
					/*
					 * Evento esterno al percorso di interesse.
					 */
					return null;
				}
				cache ();
				if (childIndices.length==0) {
					/*
					 * Modifica estesa agli avanzamenti del percorso
					 */
					fireTableDataChanged ();
				} else {
					/*
					 * Inserimenti puntuali di avanzamenti nel percorso
					 */
//					for (int i=0;i<childIndices.length;i++){
//						fireTableRowsDeleted (childIndices[i], childIndices[i]);
//					}
					/*
					 * @todo ottimizzare con codice riportato sopra
					 * al momento tale codice comporta problemi dato che esso e' pensato per rimozioni contigue
					 */
					fireTableDataChanged ();
				}
				return null;
			}
		};
		
		public void treeNodesRemoved (TaskTreeModelEvent e) {
			e.allow (_treeNodesRemovedInspector);
		}
		
		public void treeStructureChanged (TaskTreeModelEvent e) {
//			System.out.println ("table treeStructureChanged");
			checkForReload (e);
			if (_masterPath==null) {
				return;
			}
//			if (e.getWorkSpace ()!=)
			if (e.getPath ().contains (_masterPath)) {
				cache ();
				fireTableDataChanged ();
			}
		}
		
		private void checkForReload (TaskTreeModelEvent e) {
			/*
			 * l'evento da un workspace diversoviene consideratocome un comando di reinizializzazione su tale workspace
			 */
			if (e.getWorkSpace ()!=_workspace) {
				final WorkSpace ws = e.getWorkSpace ();
				if (ws!=null) {
					reload (ws, ws.getRoot ());
//					System.out.println ("reloading table for "+ws);
				} else {
					reload (null, null);
//					System.out.println ("reloading table for null");
				}
			}
		}
		
		public void valueChanged (TreeSelectionEvent e) {
			final int r = taskTree.getSelectedRow ();
			if (r<0) {
				reload (null, null);
			} else {
				/*
				 * invocazione asincrona per evitare problemi in cancellazione (l0evento di rimozione su pià task 
				 * provoca un evento di modifica selezione che implica ancora i nodi rimossi-> nullpointer
				 */
				SwingUtilities.invokeLater (new Runnable () {

					public void run () {
						final int r = taskTree.getSelectedRow ();
						reload (_context.getModel ().getWorkSpace (), (Task)taskTree.getModel ().getValueAt (taskTree.convertRowIndexToModel (r), taskTree.convertColumnIndexToModel (TaskJTreeModel.TREE_COLUMN_INDEX)));
					}
				});
			}
			
//			final TreePath path = e.getPath ();
//			if (path==null) {
//				return;
//			}
//			final Object o = path.getLastPathComponent ();
//			if (!e.isAddedPath ()) {
//				reload (null, null);
//			} else if (o instanceof Task){
//				reload (_context.getModel ().getWorkSpace (), (Task)o);
//			}
		}
		
		public void tableChanged (TableModelEvent e) {
			tableTaskNameLabel.setText (_master==null?"":_master.getName ());
			tableTaskNameLabel.setToolTipText (_master==null?"":_master.getName ());
			tableProgressCountLabel.setText (Integer.toString (progressesTable.getModel ().getRowCount ()));
		}
		public void advanceTic (AdvancingTicEvent e) {
			final int l = advancingIdx.length;
			for (int i = 0; i < l; i++) {
				final int idx = advancingIdx[i];
				SwingUtilities.invokeLater (new Runnable () {
					public void run () {
						/*
						 * @workaround invoca in modo asincrono la notifica della variazione nella tabella, sullo stesso thread degli eventi AWT.
						 * questo dovrebbe evitare i problemi di accesso concorrente in JDORI, finc&egrave; l'accesso a JDO avviene sul thread AWT!!!
						 */
						fireTableCellUpdated (idx, DURATION_COL_INDEX);
					}
				});
			}
		}
		
		public void elementsInserted (WorkAdvanceModelEvent e) {
			cacheAdvancingProgress ();
		}
		
		public void elementsRemoved (WorkAdvanceModelEvent e) {
			cacheAdvancingProgress ();
		}
		
		/**
		 * array contenente gli indici delle righe che sono in avanzamento
		 */
		private int[] advancingIdx;
		
		/**
		 * aggiorna l'array contenente gli indici delle righe che sono in avanzamento
		 */
		private void cacheAdvancingProgress () {
			final Set advancing = _context.getModel ().getAdvancing ();
			int[] tmp = new int[0];
			int counter = 0;
			int i = 0;
			for (final PieceOfWork p : _progresses) {
				if (advancing.contains (p)) {
					final int[] tmp1 = tmp;
					tmp = new int[counter+1];
					
					System.arraycopy (tmp1, 0, tmp, 0, tmp1.length);
					
					tmp[counter] = i;
					counter++;
				}
				i++;
			}
			advancingIdx = new int[tmp.length];
			System.arraycopy (tmp, 0, advancingIdx, 0, tmp.length);
		}

		public void workSpaceChanged (WorkSpace oldWS, WorkSpace newWS) {
		}
	}
	
	
	/**
	 * Modello dell'albero degli avanzamenti.
	 */
	private class TaskJTreeModel extends BasicTreeTableModel implements /*TreeModel,*/ TaskTreeModelListener, PropertyChangeListener, WorkAdvanceModelListener, AdvancingTicListener {
		
		private final TaskTreeModelImpl _model;
		
		/**
		 * Indice della colonna contenente l'albero.
		 */
		public final static int TREE_COLUMN_INDEX = 0;
		/**
		 * Indice della colonna contenente la durata giornaliera.
		 */
		public final static int TODAY_COLUMN_INDEX = 1;
		/**
		 * Indice della colonna contenente la durata globale.
		 */
		public final static int GLOBAL_COLUMN_INDEX = 2;
		
		public TaskJTreeModel (final TaskTreeModelImpl model) {
			super (model.getRoot ());
			_model = model;
		}
		
		public boolean isCellEditable (Object node, int column) {
			//colonna dell'albero editabile'
			return column==0;
		}
		
		public int getIndexOfChild (Object parent, Object child) {
			return ((Task)parent).childIndex ((Task)child);
		}
		
		public Object getChild (Object parent, int index) {
			return ((Task)parent).childAt (index);
		}
		
		public int getChildCount (Object parent) {
			return((Task)parent).childCount ();
		}
		
		public int getColumnCount () {
			return 3;
		}
		
		private final Class[] _columnClasses = new Class[] {
			Object.class,
			TaskTreeDuration.class,
			TaskTreeDuration.class
		};
		
		public Class getColumnClass (int col) {
			if (col==0) {
				return super.getColumnClass (col);
			}
			return _columnClasses[col];
		}
		
		private String[] columnNames = new String[] {
			java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("Task"), 
			java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("Today"), 
			java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("Total")
		};
		public String getColumnName (int column) {
			return columnNames[column];
		}
		
		
		public void propertyChange (PropertyChangeEvent evt){
		}
		
		
    /*
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     *
     * @param source the node where the tree model has changed
     * @param path the path to the root node
     * @see EventListenerList
     */
    private void fireTreeStructureChanged(Object source, TreePath path) {
        // Guaranteed to return a non-null array
        TreeModelListener[] listeners = getTreeModelListeners();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 1; i >= 0; i--) {
			// Lazily create the event:
			if (e == null) {
				e = new TreeModelEvent (source, path);
			}
			listeners[i].treeStructureChanged (e);
        }
    }
	
	
		
		private void checkForReload (TaskTreeModelEvent e) {
			final WorkSpace ws = e.getWorkSpace ();
			
			
			if (ws!=null && getRoot ()!=ws.getRoot ()) {
				/*
				 *workspace cambiato
				 */
				Task oldRoot = (Task)getRoot ();
				Task newRoot = (Task)ws.getRoot ();
				this.root = newRoot;
				if (newRoot == null && oldRoot != null) {
					fireTreeStructureChanged (this, null);
				} else {
					final TaskTreePath path = new TaskTreePath (ws, newRoot);
					try {
					fireTreeStructureChanged (this, toTreePath (path));
					} catch (final NullPointerException npe) {
						/*
						 * @workaround saltuariamente (la frequenza varia da sistema a sistema) in fase di avvio 
						 * viene sollevata un'accezione al momento inspiegabile che va silenziata.
						 * Appena possibile bisogna rimuovere questo catch che potrebbe fermare altre eccezioni. NullPointerException e' moooolto generica.
						 * L'eccezione sembra dovuta ad una parziale inizializzazione del TreeUI
						 *
						 *
Exception in thread "main" java.lang.NullPointerException
        at
javax.swing.plaf.basic.BasicTreeUI.getLeadSelectionPath(BasicTreeUI.java:2358)
        at
javax.swing.plaf.basic.BasicTreeUI.updateLeadRow(BasicTreeUI.java:2362)
        at
javax.swing.plaf.basic.BasicTreeUI.access$1400(BasicTreeUI.java:46)
        at
javax.swing.plaf.basic.BasicTreeUI$Handler.treeStructureChanged(BasicTreeUI.java:3723)
        at
org.jdesktop.swingx.treetable.AbstractTreeTableModel.fireTreeStructureChanged(AbstractTreeTableModel.java:315)
        at
net.sf.jttslite.gui.MainWindow$TaskJTreeModel.checkForReload(MainWindow.java:2559)

						 */
						System.err.println ("Exception detected on reloading workspace");
						npe.printStackTrace (System.err);
					}
				}
			}
		}
		/**
		 * Gestore delle modifiche nell'albero di modello principale.
		 */
		private TaskTreeModelEvent.Inspector _treeNodesChangedInspector = new TaskTreeModelEvent.Inspector (){
			public Object inspectTASK (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, Task[] children){
				fireTreeNodesChanged (source, toObjectArray (path), childIndices, children);
				return null;
			}
			public Object inspectPROGRESS (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, PieceOfWork[] progresses){
				final AbstractTableModel m = (AbstractTableModel)taskTree.getModel ();
				/*
				 * Evento modifica colonne durata
				 */
				m.fireTableChanged (new TableModelEvent (m, 0, m.getRowCount (), TODAY_COLUMN_INDEX));
				m.fireTableChanged (new TableModelEvent (m, 0, m.getRowCount (), GLOBAL_COLUMN_INDEX));
				return null;
			}
		};
		
		public void treeNodesChanged (TaskTreeModelEvent e) {
			e.allow (_treeNodesChangedInspector);
		}
		
		/**
		 * Gestore degli inserimenti nell'albero di modello principale.
		 */
		private TaskTreeModelEvent.Inspector _treeNodesInsertedInspector = new TaskTreeModelEvent.Inspector (){
			public Object inspectTASK (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, Task[] children){
				fireTreeNodesInserted (source, toObjectArray (path), childIndices, children);
				return null;
			}
			public Object inspectPROGRESS (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, PieceOfWork[] progresses){
				final AbstractTableModel m = (AbstractTableModel)taskTree.getModel ();
				/*
				 * Evento modifica colonne durata
				 */
				m.fireTableChanged (new TableModelEvent (m, 0, m.getRowCount (), TODAY_COLUMN_INDEX));
				m.fireTableChanged (new TableModelEvent (m, 0, m.getRowCount (), GLOBAL_COLUMN_INDEX));
				SwingUtilities.invokeLater (new Runnable () {
					public void run () {
				/*
				 * @workaround per evitare problemi in casodi passaggio difont da plain a bold (puntini)
				 */
						taskTree.updateUI ();
					}
				});
				
				return null;
			}
		};
		
		public void treeNodesInserted (TaskTreeModelEvent e) {
			e.allow (_treeNodesInsertedInspector);
		}
		
		/**
		 * Gestore delle rimozioni nell'albero di modello principale.
		 */
		private TaskTreeModelEvent.Inspector _treeNodesRemovedInspector = new TaskTreeModelEvent.Inspector (){
			public Object inspectTASK (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, Task[] children){
				fireTreeNodesRemoved (source, toObjectArray (path), childIndices, children);
				return null;
			}
			public Object inspectPROGRESS (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, PieceOfWork[] progresses){
				final AbstractTableModel m = (AbstractTableModel)taskTree.getModel ();
				/*
				 * Evento modifica colonne durata
				 */
				m.fireTableChanged (new TableModelEvent (m, 0, m.getRowCount (), TODAY_COLUMN_INDEX));
				m.fireTableChanged (new TableModelEvent (m, 0, m.getRowCount (), GLOBAL_COLUMN_INDEX));
				SwingUtilities.invokeLater (new Runnable () {
					public void run () {
				/*
				 * @workaround per evitare problemi in casodi passaggio difont da plain a bold (puntini)
				 */
						taskTree.updateUI ();
					}
				});
				return null;
			}
		};
		
		public void treeNodesRemoved (TaskTreeModelEvent e) {
			e.allow (_treeNodesRemovedInspector);
		}
		
		/**
		 * Gestore delle modifiche alla struttura nell'albero di modello principale.
		 */
		private TaskTreeModelEvent.Inspector _treeStructureChangedInspector = new TaskTreeModelEvent.Inspector (){
			
			public Object inspectTASK (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, Task[] children){
				if (path.getParentPath ()!=null) {
					Object[] ar = null;
					if (children!=null) {
						ar = new Object[children.length];
						System.arraycopy (children, 0, ar, 0, children.length);
					}
					
					fireTreeStructureChanged (source, toObjectArray (path), childIndices, ar);
				} else {
					//radice
					TaskJTreeModel.super.root = path.getLastPathComponent ();
					fireTreeStructureChanged (source, toObjectArray (path), null, null);
				}
				return null;
			}
			
			public Object inspectPROGRESS (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, PieceOfWork[] progresses){
				final AbstractTableModel m = (AbstractTableModel)taskTree.getModel ();
				/*
				 * Evento modifica colonne durata
				 */
				m.fireTableChanged (new TableModelEvent (m, 0, m.getRowCount (), TODAY_COLUMN_INDEX));
				m.fireTableChanged (new TableModelEvent (m, 0, m.getRowCount (), GLOBAL_COLUMN_INDEX));
				return null;
			}
		};
		
		public void treeStructureChanged (TaskTreeModelEvent e) {
//			System.out.println ("tree treeStructureChanged");
			checkForReload (e);
			e.allow (_treeStructureChangedInspector);
		}
		
		final TaskTreeDuration _rubberStampDuration = new TaskTreeDuration ();
		
		public Object getValueAt (Object object, int i) {
			switch (i) {
				//colonna  albero
				case TREE_COLUMN_INDEX: {
					/*
					 * Ritorna direttamente il task
					 */
					return object;
				}
				//colonna durata giornaliera
				case TODAY_COLUMN_INDEX: /*@todo ottimizzare!*/ {
					_todayPeriod.reset ();
					final Task t = (Task) object;
					final List progresses = t.getSubtreeProgresses ();
					for (final Iterator it = progresses.iterator ();it.hasNext ();){
						final Progress p = (Progress)it.next ();
						_todayPeriod.computeProgress (p);
					}
					_rubberStampDuration.setTask (t);
					_rubberStampDuration.setDuration (_todayPeriod.getTodayAmount ());
					return _rubberStampDuration;
				}
				
				//colonna durata totale
				case GLOBAL_COLUMN_INDEX: {
					long totalDuration = 0;
					final Task t = (Task)object;
					for (final Iterator it = t.getSubtreeProgresses ().iterator ();it.hasNext ();){
						final Progress p = (Progress)it.next ();
						totalDuration += p.getDuration ().getTime ();
					}
					
					_rubberStampDuration.setTask (t);
					_rubberStampDuration.setDuration (new Duration (totalDuration));
					return _rubberStampDuration;
				}
				
			}
			return null;
		}
		
		public void setValueAt (Object object, Object object0, int i) {
			switch (i) {
				//colonna  albero
				case TREE_COLUMN_INDEX:
					final ProgressItem t = (ProgressItem)object0;
					_model.updateTask (t, (String)object, t.getDescription ());
				default:
					return;
			}
		}
		
		private LocalizedPeriod _today = null;
		
		private final LocalizedPeriod getToday (){
			if (this._today==null){
				final Calendar now = new GregorianCalendar ();
				
				now.set (Calendar.HOUR_OF_DAY, 0);
				now.set (Calendar.MINUTE, 0);
				now.set (Calendar.SECOND, 0);
				now.set (Calendar.MILLISECOND, 0);
				//		now.roll (Calendar.DATE, 1);
				final Date periodStartDate = new Date (now.getTime ().getTime ());
				
				now.add (Calendar.DAY_OF_YEAR, 1);
				final Date periodFinishDate = new Date (now.getTime ().getTime ());
				this._today = new LocalizedPeriodImpl (periodStartDate, periodFinishDate);
			}
			
			return this._today;
			
		}
		
		public void elementsInserted (WorkAdvanceModelEvent e) {
		}
		
		public void elementsRemoved (WorkAdvanceModelEvent e) {
		}
		
		public void advanceTic (AdvancingTicEvent e) {
//			for (final int idx : advancingIdx) {
//				fireTableCellUpdated (idx, DURATION_COL_INDEX);
//			}
			final AbstractTableModel m = (AbstractTableModel)taskTree.getModel ();
				/*
				 * Evento modifica colonne durata
				 */
			m.fireTableChanged (new TableModelEvent (m, 0, m.getRowCount (), TODAY_COLUMN_INDEX));
			m.fireTableChanged (new TableModelEvent (m, 0, m.getRowCount (), GLOBAL_COLUMN_INDEX));
		}

		public void workSpaceChanged (WorkSpace oldWS, WorkSpace newWS) {
		}
		
		
		/**
		 * Un periodo.
		 */
		private final class TodayPeriod extends LocalizedPeriodImpl implements Comparable {
			/*
			 * durata calcolata in millisecondi
			 */
			private long _millisecs = 0;
			
			public TodayPeriod (){
				super (getToday ());
				
			}
			
			public void reset (){
				setFrom (getToday ().getFrom ());
				setTo (getToday ().getTo ());
				this._millisecs = 0;
			}
			
			public int compareTo (Object o) {
				return compareToStart ((LocalizedPeriod)o);
			}
			
			/**
			 * Calcola la quota di lavoro appartenente al giorno odierno per l'avanzamento specificato.
			 */
			public void computeProgress ( Progress progress ){
				LocalizedPeriod toIntersect = null;
				if (!progress.isEndOpened ()){
					if (!this.intersects (progress)){
						return;
					}
					toIntersect = progress;
				} else {
					//avanzamento in corso
					toIntersect = new LocalizedPeriodImpl (progress.getFrom (), new Date ());
				}
				
				final LocalizedPeriod intersection = this.intersection (toIntersect);
				if (intersection!=null) {
					this._millisecs += intersection.getDuration ().getTime ();
				}
			}
			
			/**
			 * Ritorna la durata risultante del lavoro giornaliero
			 *@returns la durata risultante del lavoro giornaliero
			 */
			public Duration getTodayAmount (){
				return new Duration (this._millisecs);
			}
		}
		
		private final TodayPeriod _todayPeriod = new TodayPeriod ();
		
	}
	
	/**********************************************
	 * FINE SEZIONE DEDICATA AI MODELLI
	 **********************************************/
	
	
	
	
	
	
	/**********************************************
	 * INIZIO SEZIONE DEDICATA AI METODI DI SERVIZIO
	 **********************************************/
	
	
	/**
	 * Ritorna il modello dell'albero delle attivita'.
	 */
	TreeTableModel getTaskTreeTableModel () {
		return taskTree.getTreeTableModel ();
	}

	private void forceUpperExpansion (final TreePath tp) {
		if (!taskTree.isExpanded (tp)) {
			final TreePath parentPath = tp.getParentPath ();
			if (parentPath!=null) {
				/*
				 * Ricorsione su percorso superire.
				 */
				forceUpperExpansion (parentPath);
			}
			taskTree.expandPath (tp);
		}
	}
	
	private void deepExpand (final WorkSpace ws, final Task t) {
		deepExpand (ws, t, true);
	}
	private void deepExpand (final WorkSpace ws, final Task t, final boolean forceUpperExpansion) {
		final TreePath tp = toTreePath (new TaskTreePath (ws, t));
		if (forceUpperExpansion) {
			forceUpperExpansion (tp);
		}
		taskTree.expandPath (tp);
		for (final Iterator i = t.getChildren ().iterator (); i.hasNext ();) {
			final Task c = (Task) i.next ();
			deepExpand (ws, c, false);
		}
	}
	
	
	
//	/**
//	 * Elimina un avanzamento dal modello.
//	 */
//	private void deleteProgresses (List<PieceOfWork> p){
//		if (p.isEmpty ()) {
//			return;
//		}
//		this._context.getModel ().removePiecesOfWork (p.get (0).getTask (), p);
//	}
	
//	/**
//	 * Elimina un insieme di task dal modello.
//	 */
//	private void deleteTasks (List<Task> t){
//		if (t.isEmpty ()) {
//			return;
//		}
//		if (JOptionPane.showConfirmDialog (
//			this,
//			StringUtils.toStringArray (
//			"Removing a task you remove all sub-tasks and actions. Please confirm if you want to continue."
//			)
//			)!=JOptionPane.OK_OPTION){
//			return;
//		}
//		this._context.getModel ().removeTasks (t.get (0).getParent (), t);
//	}
	
	public void addActionListener (ActionListener l) {
		this._actionNotifier.addActionListener (l);
	}
	
	public ActionListener[] getActionListeners () {
		return this._actionNotifier.getActionListeners ();
	}
	
	public void removeActionListener (ActionListener l) {
		this._actionNotifier.removeActionListener (l);
	}
	
	
	
	/**
	 * Stato di operazione in corso.
	 */
	private class Processing {
		private boolean p;
		
		public void setValue (boolean processing){
			p = processing;
		}
		public boolean booleanValue (){
			return p;
		}
	}
	
	
	
	
	boolean postInitialized = false;
	/**
	 * Mostra la finestra, eventualmente completando la fase di inizializzazione.
	 * @see #postInit
	 */
	@Override
	public void show (){
		if (!postInitialized){
			postInit ();
		}
		super.show ();
	}
	
	/**
	 * Fase di post-inizializzazione.
	 * Consente di inizializzare cio' che prima avrebbe prodotto problemi di dipendenze.
	 */
	private void postInit (){
		/*
		 * Ritarda il più possibile la gestione delle colonne visibili, in quanto
		 * con l'ultima versione di swingx non si riesce a nascondere le finestre (durante l'init).
		 */
		_context.getUIPersister ().register (new MainWindow.PersistencePanelAdapter (taskTreePanel, "taskTreePanel"));
		_context.getUIPersister ().register (new TablePersistenceExt (taskTree) {
			public String getPersistenceKey () {
				return "task.tree";
			}

		});
		
		
		_context.getUIPersister ().register (new TablePersistenceExt (progressesTable) {
			public String getPersistenceKey () {
				return "progresses.table";
			}
		});
		
		postInitialized = true;
	}
	
	
	/**
	 * Notifica l'applicazione l'esecuzione di lavori lunghi, mostrando la barra di progressione.
	 * Solo se il task dura + di 0.2 secs
	 */
	private abstract class VisibleWorker extends SwingWorker {
		private final String initialMessage;
		
		public VisibleWorker (String initialMessage){
			this.initialMessage = initialMessage;
		}
		
		public abstract void work ();
		public Object construct () {
			final Processing processing = new Processing ();
			processing.setValue (true);
			
			final java.util.Timer timer = new java.util.Timer ("processNotificationTimer", true);
			timer.schedule (new TimerTask (){
				public void run (){
					if (processing.booleanValue ()) {
						_context.setProcessing (true);
						progressBar.setString (initialMessage);
					}
				}},
				/* 2 decimi di secondo */
				200);
				
				
				try{
					work ();
				} finally {
					processing.setValue (false);
					_context.setProcessing (false);
					progressBar.setString ("");
				}
				
				return null;
		}
	}
	
	private void invokePackAll (final JXTable t) {
		SwingUtilities.invokeLater (new Runnable () {
			public void run () {
				/*
				 * @workaround invoca in modo asincrono il packall.
				 * questo dovrebbe evitare i problemi di accesso concorrente in JDORI, finc&egrave; l'accesso a JDO avviene sul thread AWT!!!
				 */
				t.packAll ();
			}
		});
	}
	
	/**********************************************
	 * FINE SEZIONE DEDICATA AI METODI DI SERVIZIO
	 **********************************************/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**********************************************
	 * INIZIO SEZIONE DEDICATA AGLI EDITOR
	 **********************************************/
	
	/**
	 * Editor per le date.
	 */
	private class DateCellEditor extends DefaultCellEditor implements TableCellEditor {

		public DateCellEditor () {
			super (new JFormattedTextField (new DateFormatter (new SimpleDateFormat (CustomizableFormat.LONG_DATE.getValue (_context)))));
			final SimpleDateFormat sdf = (SimpleDateFormat)((DateFormatter)getComponent ().getFormatter ()).getFormat ();
			CustomizableFormat.LONG_DATE.addPropertyChangeListener (new PropertyChangeListener () {

				public void propertyChange (PropertyChangeEvent evt) {
					sdf.applyPattern (CustomizableFormat.LONG_DATE.getValue (_context));
				}
			});
			getComponent ().setHorizontalAlignment (JTextField.TRAILING);
//			getComponent ().addFocusListener (new FocusListener () {
//				public void focusGained (FocusEvent e) {
//					getComponent ().requestFocusInWindow ();
//					getComponent ().selectAll ();
//				}
//				public void focusLost (FocusEvent e) {
//				}
//			});

		}

		@Override
		public JFormattedTextField getComponent () {
			return (JFormattedTextField)super.getComponent ();
		}

		
		/**
		 * Editazione parte, in caso di evento del mouse, solo dopo doppio click
		 */
		@Override
			public boolean isCellEditable (EventObject eo) {
			if (eo instanceof MouseEvent) {
				return ((MouseEvent)eo).getClickCount ()>1;
			} else {
				return super.isCellEditable (eo);
			}
		}
		
		public Object getCellEditorValue () {
			return (Date)getComponent ().getValue ();
		}
		
		public Component getTableCellEditorComponent (JTable table, Object value, boolean isSelected, int row, int column) {
			getComponent ().setValue ((Date)value);
//			getComponent ().selectAll ();
			getComponent ().setCaretPosition (0);
			return getComponent ();
		}
	}
	
	/**
	 * Editor per le durate.
	 * Funziona usando PieceOfWork, e non Duration
	 */
	private class DurationCellEditor extends DefaultCellEditor implements TableCellEditor {
		public DurationCellEditor () {
			super (new DurationTextField ());
			getComponent ().setHorizontalAlignment (JTextField.TRAILING);
//			getComponent ().addFocusListener (new FocusListener () {
//				public void focusGained (FocusEvent e) {
//					getComponent ().requestFocusInWindow ();
//					getComponent ().selectAll ();
//				}
//				public void focusLost (FocusEvent e) {
//				}
//			});
		}
		
		@Override
		public JFormattedTextField getComponent () {
			return (JFormattedTextField)super.getComponent ();
		}

		/**
		 * Editazione parte, in caso di evento del mouse, solo dopo doppio click
		 */
		@Override
			public boolean isCellEditable (EventObject eo) {
			if (eo instanceof MouseEvent) {
				return ((MouseEvent)eo).getClickCount ()>1;
			} else {
				return super.isCellEditable (eo);
			}
		}
		
		public Object getCellEditorValue () {
			/*
			 * ritorna una durata
			 */
			return (Duration)getComponent ().getValue ();
		}
		
		public Component getTableCellEditorComponent (JTable table, Object value, boolean isSelected, int row, int column) {
			/*
			 * riceve un avanzamento
			 */
			getComponent ().setValue (((PieceOfWork)value).getDuration ());
			getComponent ().setText (DurationUtils.format (((PieceOfWork)value).getDuration ()));
//			getComponent ().selectAll ();
			getComponent ().setCaretPosition (0);
			return getComponent ();
		}
	}
	
	
	/**********************************************
	 * FINE SEZIONE DEDICATA AGLI EDITOR
	 **********************************************/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**********************************************
	 * INIZIO SEZIONE DEDICATA AI POPUP
	 **********************************************/
	
	
//	/**
//	 * Riferimento all'avanzamento interesato dalle azioni del popup menu per la tabella degli avanzamenti.
//	 *<P>
//	 *La presenza di questo riferimento sirende necessaria dato che non &egrave; possibile determinare
//	 *la riga interessata dal menu di popup in sede di gestione degli eventi di tipo PopupMenuEvent, che
//	 *non danno alcuna coordinata.
//	 */
//	private PieceOfWork _tablePopupMenuSubject;
//	private void prepareTablePopupMenu (MouseEvent evt) {
//		if (evt.getButton ()==MouseEvent.BUTTON3){
////			if (evt.getSource ()==progressesTable){
//			_tablePopupMenuSubject = getPieceOfWorkForEvent (evt);
////			}
//		}
//		deleteProgressPopupItem.setEnabled (_tablePopupMenuSubject!=null);
//	}
//	
//	private void prepareTablePopupMenu (PopupMenuEvent evt) {
//		deleteProgressPopupItem.setEnabled (_tablePopupMenuSubject!=null);
//	}
	
	/**
	 * Ritorna l'avanzamento della tabella candidato come soggetto di esecuzione delle azioni riferite alle coordinate dell'evento del mosue.
	 * <P>
	 * Viene usato dal popup menu.
	 * @param evt
	 * @return
	 */
	private PieceOfWork getPieceOfWorkForEvent (MouseEvent evt) {
		final int x = evt.getX ();
		final int y = evt.getY ();
		final Point p = new Point (x, y);
		final int r = progressesTable.rowAtPoint (p);
		if (r<0) {
			return null;
		}
		return (PieceOfWork) progressesTable.getModel ().getValueAt (progressesTable.convertRowIndexToModel (r), progressesTable.convertColumnIndexToModel (DURATION_COL_INDEX));
	}
	
	private void prepareTreePopupMenu (PopupMenuEvent evt) {
//					if (evt.getSource ()==this.bundleTree){
//				final int x = evt.getX ();
//				final int y = evt.getY ();
//				treePopupPath = this.bundleTree.getPathForLocation (x, y);
//				if (treePopupPath!=null){
//					final Object node = treePopupPath.getLastPathComponent ();
//					treePopupNode = node;
//					if (node instanceof ResourceBundleModel){
//						bundlePopupMenu.show ((Component)evt.getSource (), x, y);
		
	}
	
	
	/**********************************************
	 * FINE SEZIONE DEDICATA AI POPUP
	 **********************************************/
	
	
	
	
	
	
	
	
	
	
	/**********************************************
	 * INIZIO SEZIONE DEDICATA ALLE ACTION
	 **********************************************/
	
	
	
	
	/**
	 * Array delle azioni.
	 */
	private final Map actions = new HashMap ();
	
	/**
	 * Crea l'array delle azioni registrate su di un componente testuale.
	 */
	private void createActionTable (JTextComponent textComponent) {
		Action[] actionsArray = textComponent.getActions ();
		for (int i = 0; i < actionsArray.length; i++) {
			Action a = actionsArray[i];
			actions.put (a.getValue (Action.NAME), a);
		}
	}
	
	private Action getActionByName (String name) {
		return (Action)(actions.get (name));
	}
	
	
	
	/**
	 * Crea un nuovo task.
	 *
	 */
	private class NewTaskAction extends AbstractAction implements TreeSelectionListener {
		
		
		/**
		 * Costruttore.
		 */
		public NewTaskAction () {
			this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
			taskTree.addTreeSelectionListener (this);
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			final TaskTreeModelImpl m = _context.getModel ();
			_context.getLogger ().debug ("Creating new task...");
			final Task t = new ProgressItem (_candidateParent.getName ()+"."+(_candidateParent.childCount ()+1));
			m.insertNodeInto (t, _candidateParent, _candidateParent.childCount ());
			SwingUtilities.invokeLater (new Runnable () {
				public void run () {
					/*
					 * @workaround invoca in modo asincrono
					 */
					final WorkSpace ws = _context.getModel ().getWorkSpace ();
					deepExpand (ws, t);
					final TreePath path = toTreePath (new TaskTreePath (ws, t));
					taskTree.scrollPathToVisible (path);
					taskTree.getTreeSelectionModel ().setSelectionPath (path);
					final int row = taskTree.getSelectedRow ();
					taskTree.editCellAt (row, TaskJTreeModel.TREE_COLUMN_INDEX);
					if (taskTree.getCellEditor () instanceof TreeTableCellEditor) {
						final JTextField editor = (JTextField)taskTree.getCellEditor ().getTableCellEditorComponent (taskTree, t.getName (), true, row, TaskJTreeModel.TREE_COLUMN_INDEX);
						editor.requestFocusInWindow ();
						editor.selectAll ();
					}
				}
			});
			
			_context.getLogger ().debug ("New task created");
		}
		
		Task _candidateParent;
		private boolean isEnabled (final Task candidateParent) {
			return candidateParent!=null;
		}
		
		public void valueChanged (TreeSelectionEvent e) {
			final Task oldCandidateParent = _candidateParent;
			final TreePath path = taskTree.getPathForRow (taskTree.getSelectedRow ());
			if (path==null) {
				_candidateParent = null;
			} else {
				final Object o = path.getLastPathComponent ();
				if (o instanceof Task){
					_candidateParent = (Task)o;
				} else {
					_candidateParent = null;
				}
			}
			if (oldCandidateParent!=_candidateParent){
				setEnabled (isEnabled (_candidateParent));
			}
		}
		
	}
	
	/**
	 * Elimina i task selezionati.
	 *
	 */
	private class DeleteTasksAction extends AbstractAction implements TreeSelectionListener {
		
		
		/**
		 * Costruttore.
		 */
		public DeleteTasksAction () {
			this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_DELETE, 0));
			taskTree.addTreeSelectionListener (this);
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			/*
			 * @workaround forza il cambio di selezione, datoche la modifica dell'albero non modifica la selezione, comportandfonullpinter nella tabella, che usa datinon piu' validi
			 */
//			final Map<Task, List<Task>> candidatesBackup = new HashMap<Task, List<Task>> (_removalCandidates);
//			int sel = taskTree.getSelectionModel ().getMinSelectionIndex ();
//			if (sel>0) {
//				sel--;
//			} else {
//				sel = 0;
//			}
//			taskTree.getSelectionModel ().setSelectionInterval (sel,sel);
//			final TaskTreeModelImpl m = _context.getModel ();
//			for (final Task parent : candidatesBackup.keySet ()) {
//				m.removeTasks (parent, candidatesBackup.get (parent));
//			}
			
			
			if (
				JOptionPane.showConfirmDialog (MainWindow.this, 
					java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("task.removal.confirm.message"), 
					java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("task.removal.confirm.dialogtitle"), 
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.OK_OPTION) {
				return;
			}
			
			_context.getLogger ().debug ("Removing tasks...");
			final TaskTreeModelImpl m = _context.getModel ();
			for (final Task parent : _removalCandidates.keySet ()) {
				m.removeTasks (parent, _removalCandidates.get (parent));
			}
			_context.getLogger ().debug ("Tasks removed");
		}
		
		Map<Task, List<Task>> _removalCandidates;
		private boolean isEnabled (final Map<Task, List<Task>> removalCandidates) {
			return !removalCandidates.isEmpty ();
		}
		
		public void valueChanged (TreeSelectionEvent e) {
			final Map<Task, List<Task>> removalCandidates = new HashMap ();
			for (final int r : taskTree.getSelectedRows ()){
				final Task candidate = (Task) taskTree.getModel ().getValueAt (taskTree.convertRowIndexToModel (r), taskTree.convertColumnIndexToModel (TaskJTreeModel.TREE_COLUMN_INDEX));
				final Task parent = candidate.getParent ();
				/*
				 * e' un Task
				 */
				if (parent!=null) {
					
					/*
					 * ha un padre: non e' il nodo radice
					 */
					if (removalCandidates.containsKey (parent)) {
						/*
						 * gia' un fratello schedulato per la cancellazione.
						 */
						final List<Task> l = removalCandidates.get (parent);
						l.add (candidate);
					} else {
						/*
						 * nessun fratello gia' in lista.
						 */
						final List<Task> l = new ArrayList ();
						l.add (candidate);
						removalCandidates.put (parent, l);
					}
				}
			}
			
			_removalCandidates = removalCandidates;
			setEnabled (isEnabled (removalCandidates));
		}
		
	}
	
	/**
	 * Fa partire un nuovo avanzamento.
	 *
	 */
	private class StartProgressAction extends AbstractAction implements TreeSelectionListener, WorkAdvanceModelListener {
		
		private StopProgressAction _stopAction = new StopProgressAction ();
		/**
		 * Costruttore.
		 */
		public StartProgressAction () {
			this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
			taskTree.addTreeSelectionListener (this);
			_context.getModel ().addWorkAdvanceModelListener (this);
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			if (0!=(e.getModifiers() & ActionEvent.SHIFT_MASK)){
				_context.getWindowManager ().showStartPieceOfWorkDialog (_candidateParent);
			} else {
				if (_stopAction.isEnabled ()) {
					/*
					 * ferma l'avanzamento in corso
					 */
					_stopAction.actionPerformed (e);
				}
				_context.getLogger ().debug ("Starting new action...");
				final TaskTreeModelImpl m = _context.getModel ();
				m.insertPieceOfWorkInto (new Progress (new Date (), null, (ProgressItem)_candidateParent), _candidateParent, _candidateParent.pieceOfWorkCount ());
				_context.getLogger ().debug ("Action started");
			}
		}
		
		private boolean _isEnabled () {
			return _candidateParent!=null;
			/*
			 * condizione disabilitata, per consentire l'inizio di  una nuova azione che termina quella corrente.
			 *
			 && _context.getModel ().getAdvancing ().isEmpty ()
			 */
		}
		
		Task _candidateParent;
		public void valueChanged (TreeSelectionEvent e) {
			final Task oldCandidateParent = _candidateParent;
			final TreePath path = taskTree.getPathForRow (taskTree.getSelectedRow ());
			if (path==null) {
				_candidateParent = null;
			} else {
				final Object o = path.getLastPathComponent ();
				if (o instanceof Task){
					_candidateParent = (Task)o;
				} else {
					_candidateParent = null;
				}
			}
			if (oldCandidateParent!=_candidateParent){
				setEnabled (_isEnabled ());
			}
		}
		
		public void elementsInserted (WorkAdvanceModelEvent e) {
			setEnabled (_isEnabled ());
		}
		
		public void elementsRemoved (WorkAdvanceModelEvent e) {
			setEnabled (_isEnabled ());
		}
	}
	
	
	/**
	 * Fa partire un nuovo avanzamento clone.
	 *
	 */
	private class StartProgressCloneAction extends AbstractAction implements ListSelectionListener, WorkAdvanceModelListener {
		
		private StopProgressAction _stopAction = new StopProgressAction ();
		
		/**
		 * Costruttore.
		 */
		public StartProgressCloneAction () {
			_context.getModel ().addWorkAdvanceModelListener (this);
			progressesTable.getSelectionModel ().addListSelectionListener (this);
			progressesTable.getColumnModel ().getSelectionModel ().addListSelectionListener (this);
			
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			if (_stopAction.isEnabled ()) {
				/*
				 * ferma l'avanzamento in corso
				 */
				_stopAction.actionPerformed (e);
			}
			_context.getLogger ().debug ("Starting progress clone...");
			final TaskTreeModelImpl m = _context.getModel ();
			final ProgressItem parent = (ProgressItem)_candidateSource.getTask ();
			final Progress clone = new Progress (new Date (), null, parent);
			clone.setDescription (_candidateSource.getDescription ());
			clone.setNotes (_candidateSource.getNotes ());
			m.insertPieceOfWorkInto (clone, parent, parent.pieceOfWorkCount ());
			_context.getLogger ().debug ("Clone progress started");
		}
		
		private Progress _candidateSource;
		
		private boolean _isEnabled () {
			return _candidateSource!=null;
			/*
			 * condizione disabilitata, per consentire l'inizio di  una nuova azione che termina quella corrente.
				
				&& _context.getModel ().getAdvancing ().isEmpty ();
			 * 
			 */
		}
		
		private void resetEnabled () {
			_candidateSource = null;
			setEnabled ();
		}
		
		private void setEnabled () {
			setEnabled (_isEnabled ());
		}

		
		public void elementsInserted (WorkAdvanceModelEvent e) {
			setEnabled (_isEnabled ());
		}
		
		public void elementsRemoved (WorkAdvanceModelEvent e) {
			setEnabled (_isEnabled ());
		}
		
		public void valueChanged (ListSelectionEvent e) {
			if (e.getValueIsAdjusting ()){
				/* evento spurio */
				return;
			}
			if (progressesTable.getSelectedRows ().length>1) {
				resetEnabled ();
				return;
			}
			int r = progressesTable.getSelectedRow ();
			if (r<0) {
				resetEnabled ();
				return;
			}
			
			r = progressesTable.convertRowIndexToModel (r);
			if (r<0 || r>=progressesTable.getModel ().getRowCount ()) {
				resetEnabled ();
				return;
			} 
			_candidateSource = (Progress)progressesTable.getModel ().getValueAt (r, progressesTable.convertColumnIndexToModel (DURATION_COL_INDEX));
			setEnabled ();
		}
		
	}
		
	/**
	 * Continua un avanzamento come se non fosse mai terminato.
	 *
	 */
	private class ContinueProgressAction extends AbstractAction implements ListSelectionListener, WorkAdvanceModelListener {
		
		
		/**
		 * Costruttore.
		 */
		public ContinueProgressAction () {
			_context.getModel ().addWorkAdvanceModelListener (this);
			progressesTable.getSelectionModel ().addListSelectionListener (this);
			progressesTable.getColumnModel ().getSelectionModel ().addListSelectionListener (this);
			
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			_context.getLogger ().debug ("Continuing a previously closed action...");
			final TaskTreeModelImpl m = _context.getModel ();
			m.updatePieceOfWork (_candidate, _candidate.getFrom (), null, _candidate.getDescription ());
			_context.getLogger ().debug ("Action (re)started");
		}
		
		private Progress _candidate;
		
		private boolean _isEnabled () {
			return _candidate!=null && _context.getModel ().getAdvancing ().isEmpty ();
		}
		
		private void resetEnabled () {
			_candidate = null;
			setEnabled ();
		}
		
		private void setEnabled () {
			setEnabled (_isEnabled ());
		}

		
		public void elementsInserted (WorkAdvanceModelEvent e) {
			setEnabled (_isEnabled ());
		}
		
		public void elementsRemoved (WorkAdvanceModelEvent e) {
			setEnabled (_isEnabled ());
		}
		
		public void valueChanged (ListSelectionEvent e) {
			if (e.getValueIsAdjusting ()){
				/* evento spurio */
				return;
			}
			if (progressesTable.getSelectedRows ().length>1) {
				resetEnabled ();
				return;
			}
			int r = progressesTable.getSelectedRow ();
			if (r<0) {
				resetEnabled ();
				return;
			}
			
			r = progressesTable.convertRowIndexToModel (r);
			if (r<0 || r>=progressesTable.getModel ().getRowCount ()) {
				resetEnabled ();
				return;
			} 
			_candidate = (Progress)progressesTable.getModel ().getValueAt (r, progressesTable.convertColumnIndexToModel (DURATION_COL_INDEX));
			setEnabled ();
		}
		
	}
	
	/**
	 *Stop every running action.
	 */
	void stopAdvancing () {
		final TaskTreeModelImpl m = _context.getModel ();
		
		for (final PieceOfWork pow : _context.getModel ().getAdvancing ()) {
			_context.getModel ().updatePieceOfWork (pow, pow.getFrom (), new Date (), pow.getDescription ());
			m.pieceOfWorkChanged (pow);
		}
	}
	
	/**
	 * Ferma un avanzamento in progress.
	 *
	 */
	class StopProgressAction extends AbstractAction implements WorkAdvanceModelListener {
		
		
		/**
		 * Costruttore.
		 */
		public StopProgressAction () {
			this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
			_context.getModel ().addWorkAdvanceModelListener (this);
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			_context.getLogger ().debug ("Stopping current action...");
			stopAdvancing ();
			_context.getLogger ().debug ("Current action stopped");
		}
		
		private void setEnabled () {
			setEnabled (!_context.getModel ().getAdvancing ().isEmpty ());
		}
		
		public void elementsInserted (WorkAdvanceModelEvent e) {
			setEnabled ();
		}
		
		public void elementsRemoved (WorkAdvanceModelEvent e) {
			setEnabled ();
		}
		
	}
	
	/**
	 * Aggiunge un avanzamento all'elenco dei template.
	 *
	 */
	private class AddProgressToTemplatesAction extends AbstractAction implements ListSelectionListener, WorkAdvanceModelListener {
		
		
		/**
		 * Costruttore.
		 */
		public AddProgressToTemplatesAction () {
			_context.getModel ().addWorkAdvanceModelListener (this);
			progressesTable.getSelectionModel ().addListSelectionListener (this);
			progressesTable.getColumnModel ().getSelectionModel ().addListSelectionListener (this);
			
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			_context.getLogger ().debug ("Adding action to templates...");
			final PieceOfWorkTemplateModelImpl m = _context.getTemplateModel ();
			for (final Progress progress : _candidates ) {
				final ProgressTemplate template = new ProgressTemplate ();
				template.setName (MessageFormat.format (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("new.template.automatic.name"), progress.getTask ().getName (), ""));
				template.setDuration (progress.getDuration ());
				template.setNotes (progress.getDescription ());
				m.addElement (template);
			}
			_context.getLogger ().debug ("Template added");
		}
		
		private final List<Progress> _candidates = new ArrayList<Progress> ();
		
		private boolean _isEnabled () {
			return !_candidates.isEmpty ();
		}
		
		private void resetEnabled () {
			_candidates.clear ();
			setEnabled ();
		}
		
		private void setEnabled () {
			setEnabled (_isEnabled ());
		}

		
		public void elementsInserted (WorkAdvanceModelEvent e) {
			setEnabled (_isEnabled ());
		}
		
		public void elementsRemoved (WorkAdvanceModelEvent e) {
			setEnabled (_isEnabled ());
		}
		
		public void valueChanged (ListSelectionEvent e) {
			if (e.getValueIsAdjusting ()){
				/* evento spurio */
				return;
			}

			int[] rows = progressesTable.getSelectedRows ();
			for (final int row : rows) {
				if (row<0) {
					resetEnabled ();
					return;
				}
				
				final int r = progressesTable.convertRowIndexToModel (row);
				
				if (r<0 || r>=progressesTable.getModel ().getRowCount ()) {
					resetEnabled ();
					return;
				} 
				_candidates.clear ();
				_candidates.add ((Progress)progressesTable.getModel ().getValueAt (r, progressesTable.convertColumnIndexToModel (DURATION_COL_INDEX)));
			}
			
			setEnabled ();
		}
		
	}
	
	
	/**
	 * Crea un nuovo avanzamento.
	 *
	 */
	private class NewPieceOfWorkAction extends AbstractAction implements TreeSelectionListener {
		
		
		/**
		 * Costruttore.
		 */
		public NewPieceOfWorkAction () {
			taskTree.addTreeSelectionListener (this);
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			_context.getLogger ().debug ("Creating new action...");
//			final TaskTreeModelImpl m = _context.getModel ();
			_context.getWindowManager ().getNewPieceOfWorkDialog ().showForTask (_candidateParent);
//			m.insertPieceOfWorkInto (new Progress (new Date (), null, (ProgressItem)_candidateParent), _candidateParent, _candidateParent.pieceOfWorkCount ());
			_context.getLogger ().debug ("New action created");
		}
		
		private boolean _isEnabled () {
			return _candidateParent!=null;
		}
		
		Task _candidateParent;
		public void valueChanged (TreeSelectionEvent e) {
			final Task oldCandidateParent = _candidateParent;
			final TreePath path = taskTree.getPathForRow (taskTree.getSelectedRow ());
			if (path==null) {
				_candidateParent = null;
			} else {
				final Object o = path.getLastPathComponent ();
				if (o instanceof Task){
					_candidateParent = (Task)o;
				} else {
					_candidateParent = null;
				}
			}
			if (oldCandidateParent!=_candidateParent){
				setEnabled (_isEnabled ());
			}
		}
		
		
	}
	
	/**
	 * Elimina gli avanzamenti selezionati.
	 *
	 */
	private class DeletePiecesOfWorkAction extends AbstractAction implements ListSelectionListener, WorkAdvanceModelListener, TaskTreeModelListener {
		
		/**
		 * Costruttore.
		 */
		public DeletePiecesOfWorkAction () {
			this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_DELETE, 0));
			
			progressesTable.getSelectionModel ().addListSelectionListener (this);
			progressesTable.getColumnModel ().getSelectionModel ().addListSelectionListener (this);
			
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			
			
			if (
				JOptionPane.showConfirmDialog (MainWindow.this, 
					java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("action.removal.confirm.message"), 
					java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("action.removal.confirm.dialogtitle"), 
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.OK_OPTION) {
				return;
			}
			
			
			
			_context.getLogger ().debug ("Deleting actions...");
			final TaskTreeModelImpl m = _context.getModel ();
			for (final Task t : _removalCandidates.keySet ()) {
				m.removePiecesOfWork (t, _removalCandidates.get (t));
			}
			_context.getLogger ().debug ("Actions deleted");
		}
		
		private final Map<Task, List<PieceOfWork>> _removalCandidates = new HashMap<Task, List<PieceOfWork>>  ();
		private boolean isEnabled (final Map<Task, List<PieceOfWork>> removalCandidates) {
			final boolean b = !removalCandidates.isEmpty ();
//			System.out.println ("isEnabled "+b);
//			System.out.println ("Enabling progress removalaction: "+b);
			return b;
		}
		
		/*
		 * @todo ottimizzare le chiamate a questo metodo
		 */
		private void setEnabled () {
			_removalCandidates.clear ();
			final int[] selectedRows = progressesTable.getSelectedRows ();
			/*
			 * @workarond rowCount prende dal modello e non dalla tabella, dato che la tabella risponde ancora con il
			 * numero di righe totali anche incaso di eliminazione in corso.
			 * @warning purtroppo sembra che questo comportamento anomalo sia time-dependant
			 */
			//final int rowCount = progressesTable.getRowCount ();
			final int rowCount = progressesTable.getModel ().getRowCount ();
			
			for (int r : selectedRows){
				r = progressesTable.convertRowIndexToModel (r);
				/*
				 * @workaround in questo momento la selezione Puo' ancora essere su righe ormai rimosse e fuori tabella
				 */
				if (r<0 || r>=rowCount) {
//					System.out.println ("Jumping, as per selected row "+r+" of "+rowCount);
					continue;
//				} else {
//					System.out.println ("OK, as per selected row "+r+" of "+rowCount);
//					System.out.println ("Selected rows: ["+Arrays.toString (selectedRows)+"]");
				}
				final PieceOfWork candidate = (PieceOfWork) progressesTable.getModel ().getValueAt (r, progressesTable.convertColumnIndexToModel (DURATION_COL_INDEX));
				if (!candidate.isEndOpened ()) {
					/*
					 * non sta avanzando
					 */
					final Task t = candidate.getTask ();
					
					if (_removalCandidates.containsKey (t)) {
						/*
						 * gia' un fratello schedulato per la cancellazione.
						 */
						final List<PieceOfWork> l = _removalCandidates.get (t);
						l.add (candidate);
					} else {
						/*
						 * nessun fratello gia' in lista.
						 */
						final List<PieceOfWork> l = new ArrayList<PieceOfWork> ();
						l.add (candidate);
						_removalCandidates.put (t, l);
					}
				}
			}
			
			setEnabled (isEnabled (_removalCandidates));
//			System.out.println ("setEnabled "+ isEnabled ()+"for action "+hashCode ());
		}
		
		public void valueChanged (ListSelectionEvent e) {
			if (e.getValueIsAdjusting ()){
				/* evento spurio */
				return;
			}
			
			setEnabled ();
		}
		
		public void elementsInserted (WorkAdvanceModelEvent e) {
			setEnabled ();
		}
		
		public void elementsRemoved (WorkAdvanceModelEvent e) {
			setEnabled ();
		}
		
		public void treeNodesChanged (TaskTreeModelEvent e) {
			setEnabled ();
		}
		
		public void treeNodesInserted (TaskTreeModelEvent e) {
			setEnabled ();
		}
		
		public void treeNodesRemoved (TaskTreeModelEvent e) {
			setEnabled ();
		}
		
		public void treeStructureChanged (TaskTreeModelEvent e) {
			setEnabled ();
		}

		public void workSpaceChanged (WorkSpace oldWS, WorkSpace newWS) {
		}
		
		
	}
	
	
	/**
	 * Crea ed imposta nell'applicazione un nuovo progetto.
	 */
	private class NewWorkSpaceAction extends AbstractAction {
		public NewWorkSpaceAction () {
		}
		
		public void actionPerformed (ActionEvent e) {
			create ();
		}
		
		private void create () {
			try {
				WorkspacesDialog.createWorkspace (_context, new WorkspacesDialog.BasicWorkspaceDataCallback (_context));

			} catch (final RequestAbortedException ex) {
				/*
				 * richiesta interrotta dall'utente
				 */
			} catch (final DuplicatedWorkSpaceException ex) {
				JOptionPane.showMessageDialog (MainWindow.this, java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("New_workspace_conflicting_name_message"));
				//ci riprova
				create ();
			}
			
		}
	}
	
	private class OpenWorkSpaceAction extends AbstractAction {
		public void actionPerformed (ActionEvent e) {
			this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
			_context.getWindowManager ().showOpenWorkSpaceDialog (_context.getModel ().getWorkSpace ());
		}
	}
	

	
	private class ImportSubtreeAction extends AbstractAction {
		public void actionPerformed (ActionEvent e) {
		}
	}
	
	private class DeleteAction extends TransferAction {
		public DeleteAction (final TransferActionListener tal) {
			super ("delete", tal);
		}
		
	}
	
	private class StartReportAction extends AbstractAction {
		public void actionPerformed (ActionEvent e) {
			final int r = taskTree.getSelectedRow ();
			Task reportRoot;
			if (r<0) {
				reportRoot = _context.getModel ().getRoot ();
			} else {
				reportRoot = (Task) taskTree.getModel ().getValueAt (taskTree.convertRowIndexToModel (r), taskTree.convertColumnIndexToModel (TaskJTreeModel.TREE_COLUMN_INDEX));
			}
			
			_context.getWindowManager ().showReportDialog (reportRoot);
		}
	}
	
	
		
	/**
	 * Espande l'albero.
	 *
	 */
	private class ExpandTreeAction extends AbstractAction implements TreeSelectionListener, TaskTreeModelListener  {
		
		
		/**
		 * Costruttore.
		 */
		public ExpandTreeAction () {
			taskTree.addTreeSelectionListener (this);
			_context.getModel ().addTaskTreeModelListener (this);
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			deepExpand (_context.getModel ().getWorkSpace (), _candidate);
		}
		
		public void setEnabled () {
			setEnabled (_isEnabled ());
		}
		
		private boolean _isEnabled () {
			return _candidate!=null /*&& _candidate.childCount ()!=0*/;
		}
		
		Task _candidate;
		public void valueChanged (TreeSelectionEvent e) {
			final TreePath path = e.getPath ();
			Task candidate;
			if (path==null) {
				candidate = null;
			} else {
				final Object o = path.getLastPathComponent ();
				if (o instanceof Task){
					candidate = (Task)o;
				} else {
					candidate = null;
				}
			}
			setCandidate (candidate);
		}
		
		private void setCandidate (Task candidate) {
			final Task oldCandidate = _candidate;
			_candidate = candidate;
			if (oldCandidate !=_candidate){
				setEnabled ();
			}
		}

		public void treeNodesChanged (TaskTreeModelEvent e) {
		}

		public void treeNodesInserted (TaskTreeModelEvent e) {
			dataChanged ();
		}

		protected void dataChanged () {
			final TreePath p = taskTree.getTreeSelectionModel ().getSelectionPath ();
			if (p!=null) {
				setCandidate ((Task)p.getLastPathComponent ());
			}
		}
		
		public void treeNodesRemoved (TaskTreeModelEvent e) {
			dataChanged ();
		}

		public void treeStructureChanged (TaskTreeModelEvent e) {
			dataChanged ();
		}

		public void workSpaceChanged (WorkSpace oldWS, WorkSpace newWS) {
		}

	}
	
	/**
	 * Collassa l'albero.
	 *
	 */
	private class CollapseTreeAction extends AbstractAction implements TreeSelectionListener, TaskTreeModelListener  {
		
		
		/**
		 * Costruttore.
		 */
		public CollapseTreeAction () {
			taskTree.addTreeSelectionListener (this);
			_context.getModel ().addTaskTreeModelListener (this);
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			taskTree.collapsePath (toTreePath (new TaskTreePath (_context.getModel ().getWorkSpace (), _candidate)));
		}
		
		public void setEnabled () {
			setEnabled (_isEnabled ());
		}
		
		private boolean _isEnabled () {
			return _candidate!=null /*&& taskTree.isExpanded (toTreePath (new TaskTreePath (_context.getModel ().getWorkSpace (), _candidate)))*/;
		}
		
		Task _candidate;
		public void valueChanged (TreeSelectionEvent e) {
			final TreePath path = e.getPath ();
			Task candidate;
			if (path==null) {
				candidate = null;
			} else {
				final Object o = path.getLastPathComponent ();
				if (o instanceof Task){
					candidate = (Task)o;
				} else {
					candidate = null;
				}
			}
			setCandidate (candidate);
		}
		
		private void setCandidate (Task candidate) {
			final Task oldCandidate = _candidate;
			_candidate = candidate;
			if (oldCandidate !=_candidate){
				setEnabled ();
			}
		}

		public void treeNodesChanged (TaskTreeModelEvent e) {
		}

		public void treeNodesInserted (TaskTreeModelEvent e) {
			dataChanged ();
		}

		
		protected void dataChanged () {
			final TreePath p = taskTree.getTreeSelectionModel ().getSelectionPath ();
			if (p!=null) {
				setCandidate ((Task)p.getLastPathComponent ());
			}
		}
		

		public void treeNodesRemoved (TaskTreeModelEvent e) {
			dataChanged ();
		}

		public void treeStructureChanged (TaskTreeModelEvent e) {
			dataChanged ();
		}

		public void workSpaceChanged (WorkSpace oldWS, WorkSpace newWS) {
		}

	}
	
	
	private class StartTaskEditAction extends AbstractAction implements TreeSelectionListener {
		
		public StartTaskEditAction () {
			taskTree.addTreeSelectionListener (this);
		}
		
		public void actionPerformed (ActionEvent e) {
			
			final List<Task> selected = getSelectedTasks ();
			if (!selected.isEmpty ()) {
				SwingUtilities.invokeLater (new Runnable () {

					/*
					 * invocato in modo asincrono per evitare problemi con il focus in caso di chiamata da menu contestuale
					 */
					public void run () {
						_context.getWindowManager ().showTaskEditDialog (selected.get(0));
					}
				});
			}
		}
		
		public void setEnabled () {
			setEnabled (!getSelectedTasks ().isEmpty ());
		}

		public void valueChanged (TreeSelectionEvent e) {
			setEnabled ();
		}
	}
	
	/**
	 * Rinomina l'azione selezionata
	 */
	public class RenameTaskAction extends AbstractAction implements TreeSelectionListener {
		
		public RenameTaskAction () {
			super ("renameTask");
			putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
			taskTree.addTreeSelectionListener (this);
		}
		
		public void actionPerformed (final ActionEvent e) {
			final int row = taskTree.getSelectedRow ();
			taskTree.editCellAt (row, TaskJTreeModel.TREE_COLUMN_INDEX);
			if (taskTree.getCellEditor () instanceof TreeTableCellEditor) {
				final JTextField editor = (JTextField)taskTree.getCellEditor ().getTableCellEditorComponent (taskTree, taskTree.getModel ().getValueAt (row, TaskJTreeModel.TREE_COLUMN_INDEX), true, row, TaskJTreeModel.TREE_COLUMN_INDEX);
				editor.requestFocusInWindow ();
				editor.selectAll ();
			}
		}
		
		public void setEnabled () {
			setEnabled (!getSelectedTasks ().isEmpty ());
		}

		public void valueChanged (TreeSelectionEvent e) {
			setEnabled ();
		}
	}
	
	
	
	
//	public class SensibleCutACtion extends TransferAction implements TreeSelectionListener, ListSelectionListener, FocusListener {
//		 public SensibleCutACtion () {
//			 super (TransferAction.Type.CUT, tal);
//			 taskTree.addTreeSelectionListener (this);
//			progressesTable.getSelectionModel ().addListSelectionListener (this);
////			progressesTable.getColumnModel ().getSelectionModel ().addListSelectionListener (this);
//						 
//		 }
//
////		 private boolean isEnabled () {
////			 if (taskTree.hasFocus ()) {
////				 
////			 }
////			 return 
////		 }
//		public void valueChanged (final TreeSelectionEvent e) {
////			setEnabled ()
//			throw new UnsupportedOperationException ("Not supported yet.");
//		}
//
//		public void valueChanged (final ListSelectionEvent e) {
//			throw new UnsupportedOperationException ("Not supported yet.");
//		}
//
//		public void focusGained (FocusEvent e) {
//			throw new UnsupportedOperationException ("Not supported yet.");
//		}
//
//		public void focusLost (FocusEvent e) {
//			throw new UnsupportedOperationException ("Not supported yet.");
//		}
//	}
	/**********************************************
	 * FINE SEZIONE DEDICATA ALLE ACTION
	 **********************************************/
	
	
	
	
	
	
	
	
	
	
	
	/**********************************************
	 * INIZIO SEZIONE DEDICATA AI TRANSFER HANDLER
	 **********************************************/
	
	/**
	 * Trasferimento dati (DnD, cut&paste) per l'albero
	 */
	private final class TaskTreeTransferHandler extends ProgressItemTransferHandler{
		
		private final DataFlavor progressItemFlavor = DataFlavors.progressItemFlavor;
		
		public TaskTreeTransferHandler () {
			super (_context);
		}
		
		
		/**
		 * Overridden to include a check for a ProgressItem or Progress flavor.
		 */
		@Override
			public boolean canImport (JComponent c, DataFlavor[] flavors) {
			return hasProgressItemFlavor (flavors) || hasProgressFlavor (flavors);
		}
		
		/*
		 * Bundle up the selected items in the list
		 * as a single string, for export.
		 */
		@Override
			protected Task[] exportProgressItems (JComponent c) {
			if (c!=taskTree){return null;}
			
			final TreePath[] p = taskTree.getTreeSelectionModel ().getSelectionPaths ();
			if (p==null) {
				return new Task[0];
			}
			final Task[] t = new Task[p.length];
			for (int i = 0; i < p.length; i++) {
				t[i] = (Task)p[i].getLastPathComponent ();
			}
			return t;
		}
		
		
		
		/**
		 * Importa i task.
		 *
		 * @param c il componente.
		 * @param progressItems gli avanzamentida importare.
		 */
		@Override
			protected void importProgressItems (JComponent c, Task[] progressItems, boolean removeFromSource) {
			
			final ProgressItem releaseTarget = (ProgressItem)taskTree.getTreeSelectionModel ().getSelectionPath ().getLastPathComponent ();
			final boolean rootTarget = releaseTarget.getParent ()==null;
			
			final Object[] optionsArray = new Object[] {PasteMode.BEFORE, PasteMode.AFTER, PasteMode.AS_LAST_CHILD};
			final StringBuilder sourceNames = new StringBuilder ();
			
			for (int i = 0; i< progressItems.length; i++) {
				if (progressItems[i]==releaseTarget) {
					/*
					 * uno dei task da incollare e' la destinazione stessa.
					 * Non si fa niente!!!
					 */
					return;
				}
			}
			
			if (progressItems.length>0) {
				sourceNames.append (progressItems[0]);
				for (int i = 1; i< progressItems.length; i++) {
					sourceNames.append (", ").append (progressItems[i]);
				}
			}
			
			final int selectedOption = rootTarget?
				//sul nodo radice si incolla solo come figlio
				Arrays.binarySearch (optionsArray, PasteMode.AS_LAST_CHILD):
				JOptionPane.showOptionDialog (
				taskTree,
				MessageFormat.format (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("PasteOptionsDialog/MainMessage"), releaseTarget.getName (), sourceNames.toString ()),
				java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("PasteOptionsDialog/Title"),
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				optionsArray,
				PasteMode.AS_LAST_CHILD
				);
			
			if (selectedOption == JOptionPane.CLOSED_OPTION) {
				/*
				 * paste cancellato
				 */
				return;
			}
			
			final PasteMode pasteMode = (PasteMode)optionsArray[selectedOption];
			
			Task target;
			if (pasteMode==PasteMode.AS_LAST_CHILD) {
				target = releaseTarget;
			} else if (pasteMode==PasteMode.BEFORE) {
				target = releaseTarget.getParent ();
			} else {
				//after
				target = releaseTarget.getParent ();
			}
			
			if (c!=taskTree){return;}
			if (progressItems.length == 0) {
				return;
			}
			try {
				final Map<Task, List<Task>> m = new HashMap<Task, List<Task>> ();
				for (final Task t : progressItems) {
					final Task p = t.getParent ();
					if (m.containsKey (p)) {
						m.get (p).add (t);
					} else {
						final List<Task> l = new ArrayList<Task> ();
						l.add (t);
						m.put (p, l);
					}
				}
				if (removeFromSource) {
					for (final Task p : m.keySet ()) {
						final List<Task> l = m.get (p);
						_context.getModel ().moveTasksTo (p, l, target, getPastePosition (pasteMode, releaseTarget, l, false));
						expandPastedTask (pasteMode, l);
					}
				} else {
					for (final Task p : m.keySet ()) {
						final List<Task> l = m.get (p);
						copySubtree (l, target, getPastePosition (pasteMode, releaseTarget, l, false));
						expandPastedTask (pasteMode, l);
					}
				}
			} catch (final IllegalOperationException iae){
				JOptionPane.showMessageDialog (taskTree, iae.getMessage ());
			}
		}
		
		private void expandPastedTask (final PasteMode pasteMode, final List<Task> l) {
			if (pasteMode==PasteMode.AS_LAST_CHILD && !l.isEmpty ()) {
				taskTree.expandPath (toTreePath (new TaskTreePath (_context.getModel ().getWorkSpace (), l.get (0))));
			}
		}
		
		/**
		 * Ritorna la posizione da usare per il paste della lista di task fratelli, con la modalit&agrave; ed il target specificati.
		 */
		private int getPastePosition (final PasteMode pasteMode, final Task releaseTarget, final List<Task> sources, final boolean copying) {
			if (pasteMode==PasteMode.AS_LAST_CHILD) {
				return -1;
			} else if (pasteMode==PasteMode.BEFORE) {
				return releaseTarget.getParent ().childIndex (releaseTarget);
			} else {
				if (copying) {
					return releaseTarget.getParent ().childIndex (releaseTarget)+1;
				} else {
					//after
					final Task releaseParent = releaseTarget.getParent ();
					final int releasePos = releaseParent.childIndex (releaseTarget);
					
					int diff = 0;
					for (final Task source : sources) {
						final Task sourceParent = source.getParent ();
						
						if (sourceParent!=null && sourceParent.equals (releaseTarget.getParent ())) {
							//paste su task "fratello"
							final int sourcePos = sourceParent.childIndex (source);
							if (sourcePos<releasePos) {
								/*
								 * aumenta il contatore degli elementi da sottrarre
								 */
								diff++;
							}
						}
					}
					return releasePos - diff + 1;
				}
			}
		}
		
		
		private void copySubtree (final List<Task> sources, final Task target, final int pos) {
			copySubtree (sources, target, target, target.getChildren (), pos);
		}
		
		
		private void copySubtree (final List<Task> sources, final Task target, final Task start, final List<Task> startChildren, final int pos) {
			final List<Task> copies = new ArrayList<Task> ();
			for (final Task t : sources) {
				final Task copy = new ProgressItem ((ProgressItem)t);
				copies.add (copy);
			}
			_context.getModel ().insertTasksInto (copies, target, pos);
			final int size = copies.size ();
			for (int i = 0; i < size; i++) {
				final Task s = sources.get (i);
				final Task c = copies.get (i);
				
				if (sources.get (i) == start) {
					/*
					 * sta copiando il target, bisogna prendere gli elementi originari, senza eventuali elementi introdotti dalla copia
					 */
					copySubtree (startChildren, c, start, startChildren, -1);
				} else {
					copySubtree (s.getChildren (), c, start, startChildren, -1);
				}
				_importProgresses ((PieceOfWork[])s.getPiecesOfWork ().toArray (new PieceOfWork[0]), (ProgressItem)c, false);
			}
		}
		
		
		private final DataFlavor progressFlavor = DataFlavors.progressFlavor;
		
		
		
		/**
		 * Does the flavor list have a Progress flavor?
		 */
		protected boolean hasProgressFlavor (DataFlavor[] flavors) {
			if (progressFlavor == null) {
				return false;
			}
			
			for (int i = 0; i < flavors.length; i++) {
				if (progressFlavor.equals (flavors[i])) {
					return true;
				}
			}
			return false;
		}
		
		
		
		
		/**
		 * Non esporta nulla. Caso non previsto, questo albero non gestisce gli avanzamenti.
		 */
		protected net.sf.jttslite.core.model.impl.Progress exportProgress (JComponent c) {
			return null;
		}
		
		/**
		 *
		 * ATTENZIONE: gli avanzamenti devono appartenere allo stesso task.
		 *
		 * @param c
		 * @param progresses
		 */
		protected void importProgresses (JComponent c, PieceOfWork[] progresses, boolean removeFromSource) {
			if (c!=taskTree){return;}
			if (progresses.length==0) {
				return;
			}
			final ProgressItem target = (ProgressItem)taskTree.getTreeSelectionModel ().getSelectionPath ().getLastPathComponent ();
			_importProgresses (progresses, target, removeFromSource);
		}
		
		@Override
			public boolean importData (JComponent c, Transferable t) {
			if (!canImport (c, t.getTransferDataFlavors ())) {
				return false;
			}
			
			try {
				if (hasProgressItemFlavor (t.getTransferDataFlavors ())){
					final TransferData<Task> td = (TransferData<Task>)t.getTransferData (progressItemFlavor);
					final Task[] progressItems = (Task[])td.getData ();
					importProgressItems (c, progressItems, td.getAction ()!=TransferHandler.COPY);
				} else if (hasProgressFlavor (t.getTransferDataFlavors ())){
					final TransferData<PieceOfWork> td = (TransferData<PieceOfWork>)t.getTransferData (progressFlavor);
					final PieceOfWork[] progresses = td.getData ();
					importProgresses (c, progresses, td.getAction ()!=TransferHandler.COPY);
				}
			} catch (UnsupportedFlavorException ufe) {
				_context.getLogger ().warning (ufe, "Error transferring UI data.");
				return false;
			} catch (IOException ioe) {
				_context.getLogger ().warning (ioe, "Error transferring UI data.");
				return false;
			}
			return true;
		}
		
		protected void cleanup (JComponent c, Transferable data, int action) {
		}
		
		public boolean canCopy () {
			final Task[] t = exportProgressItems (taskTree);
			return t.length>0;
		}
		
		public boolean canCut () {
			final Task[] sel = exportProgressItems (taskTree);
			if (sel.length==0) {
				return false;
			}
			for (final Task t : sel) {
				if (t.getParent ()==null) {
					//nodo radice selezionato
					return false;
				}
			}
			return true;
		}
		
		public boolean canPaste () {
			return true;
		}
		
	}
	
	
	
	
	/**
	 * Trasferimento dati (DnD, cut&paste) per la tabella
	 */
	private final class ProgressTableTransferHandler extends ProgressTransferHandler {
		public ProgressTableTransferHandler () {
			super (_context);
		}
		
		protected PieceOfWork[] exportProgresses (JComponent c) {
			if (c!=progressesTable){
				return null;
			}
			final int[] selectedRows = progressesTable.getSelectedRows ();
			final int selectedRowsCount = selectedRows.length;
			final PieceOfWork[] exportedProgresses = new PieceOfWork[selectedRowsCount];
			for (int i=0;i<selectedRowsCount;i++){
				exportedProgresses[i] = (PieceOfWork)((ProgressesJTableModel)progressesTable.getModel ()).getValueAt (progressesTable.convertRowIndexToModel (selectedRows[i]), progressesTable.convertColumnIndexToModel (DURATION_COL_INDEX));
			}
//			System.out.println ("Exported progresses: "+selectedRowsCount);
			return exportedProgresses;
		}
		
		/**
		 * ATTENZIONE: si assume che gli avanzamenti appartengano almedesimo task.
		 */
		@Override
			protected void importProgresses (JComponent c, PieceOfWork[] progresses, boolean removeFromSource) {
			if (c!=progressesTable){
				return;
			}
			if (progresses.length==0) {
				return;
			}
			
			final ProgressItem target = (ProgressItem)taskTree.getTreeSelectionModel ().getSelectionPath ().getLastPathComponent ();
			_importProgresses (progresses, target, removeFromSource);
		}
		
		protected void cleanup (JComponent c, Transferable data, int action) {
		}
		
		public boolean canCopy () {
			final PieceOfWork[] pow = exportProgresses (progressesTable);
			return pow.length>0;
		}
		
		public boolean canCut () {
			final PieceOfWork[] sel = exportProgresses (progressesTable);
			if (sel.length==0) {
				return false;
			}
			return true;
		}
		
		public boolean canPaste () {
			return true;
		}
	}
	
	/**
	 * Effettua il trasferimento effettivo dei dati relativo agliavanzamenti, in caso di Dnd, Cut&Paste, etc
	 */
	private void _importProgresses (final PieceOfWork[] progresses, final ProgressItem target, final boolean removeFromSource) {
		if (removeFromSource) {
			/*
			 * sposta
			 */
			_context.getModel ().movePiecesOfWorkTo (progresses[0].getTask (), Arrays.asList (progresses), target, -1);
		} else {
			/*
			 * copia
			 */
			final List<PieceOfWork> l = new ArrayList<PieceOfWork> ();
			for (final PieceOfWork pow : Arrays.asList (progresses)) {
				if (pow.isEndOpened ()) {
					/*
					 * non copia gli avanzamenti in corso
					 */
					continue;
				}
				final PieceOfWork copy = new Progress ((Progress)pow);
				copy.setTask (target);
				copy.setFrom (pow.getFrom ());
				copy.setTo (pow.getTo ());
				copy.setDescription (pow.getDescription ());
				l.add (copy);
			}
			_context.getModel ().insertPiecesOfWorkInto (l, target, -1);
		}
		
		
	}
	
	private static enum PasteMode {
		BEFORE {
			public String toString () {
				return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("GeneralChoice/PasteMode/before");
			}
		},
		AFTER {
			public String toString () {
				return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("GeneralChoice/PasteMode/after");
			}
		},
		AS_LAST_CHILD {
			public String toString () {
				return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("GeneralChoice/PasteMode/as_last_child");
			}
		}
		
	}
	
	/**********************************************
	 * FINE SEZIONE DEDICATA AI TRANSFER HANDLER
	 **********************************************/
	
	private static Object[] toObjectArray (final TaskTreePath p){
		final List l = new ArrayList ();
		populateList (l, p);
		return l.toArray ();
	}
	
	private static void populateList (final List l, final TaskTreePath p){
		final Task t = p.getLastPathComponent ();
		if (t!=null) {
			l.add (0, t);
		}
		final TaskTreePath parentPath = p.getParentPath ();
		if (parentPath!=null) {
			populateList (l, parentPath);
		}
	}
	
	private static TreePath toTreePath (final TaskTreePath p){
		return new TreePath (toObjectArray (p));
	}

	
	/**
	 * Imposta l'immagine di background per la tabella degli avanzamenti
	 */
	private void setTableBackoundImage (final ImageIcon image) {
		ptbImage = image;
		ptbImageWidth = ptbImage.getIconWidth();
		ptbImageHeight = ptbImage.getIconHeight();
	}
	
	/**
	 * consente di disegnare in grassetto il percorsoinprogress
	 */
	private class TaskTreeCellRenderer extends DefaultTreeCellRenderer {
		private Font _originalFont;
		private Font _boldFont;
		
		private final TooltipHTML tooltip = new TooltipHTML ();
		
		public Component getTreeCellRendererComponent (JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			final JLabel res = (JLabel)super.getTreeCellRendererComponent ( tree, value, selected, expanded, leaf, row, hasFocus);
			final Task t = (Task)value;

			if (null==_originalFont) {
				_originalFont = res.getFont ();
			}
			if (null==_boldFont) {
				_boldFont = _originalFont.deriveFont (Font.BOLD);
			}

			boolean progressing = false;
			
			for (final TaskTreePath p : _progressingPaths) {
				if (p.contains (t)) {
					res.setFont (_boldFont);
					return res;
				}
			}
			res.setFont (_originalFont);

			if (t.getDescription ()==null || 
				t.getDescription ().trim ().length ()==0 || 
				/*
				 * non mostra tooltip se la descrizione � uguale al nome
				 * (workaround per ovviare al BUG# 1939177)
				 */
				t.getDescription ().equals (t.getName ())) {
				res.setToolTipText (null);
			} else {
				tooltip.setName (t.getName ());
				tooltip.setDescription (t.getDescription ());
				res.setToolTipText (tooltip.getHTML ());
			}
			return res;
		}
		
		private class TooltipHTML {
			
			private String _name;
			private String _descr;
			
			private final StringBuilder sb1 = new StringBuilder ()
			.append ("<HTML>")
			.append ("<BODY>")
			.append ("<TABLE>")
			.append ("<THEAD>")
			.append ("<TR>")
			.append ("<TD><PRE>");

			private final StringBuilder sb2 = new StringBuilder ()
			.append ("</PRE></TD>")
			.append ("</TR>")
			.append ("</THEAD>")
			.append ("<TBODY>")
			.append ("<TR>")
			.append ("<TD><PRE>");

			private final StringBuilder sb3 = new StringBuilder ()
			.append ("</PRE></TD>")
			.append ("</TR>")
			.append ("</TBODY>")
			.append ("</TABLE>")
			.append ("</BODY>")
			.append ("</HTML>");
				
			public void setName (final String name) {
				_name = name;
			}
			
			public void setDescription (final String descr) {
				_descr = descr;
			}

			public String getHTML () {
				return new StringBuffer ()
				.append (sb1)
				.append (_name)
				.append (sb2)
				.append (_descr)
				.append (sb3).toString ();
			}
		}
	}
	
	private class TaskTreeDuration  {
		private Duration _d;
		private Task _t;
		
		public TaskTreeDuration () {
		}
		
		public TaskTreeDuration (final Duration d, final Task t) {
			_d = d;
			_t = t;
		}
		public Duration getDuration () {
			return _d;
		}
		public Task getTask () {
			return _t;
		}
		public void setDuration (Duration d) {
			_d = d;
		}
		public void setTask (Task t) {
			_t = t;
		}
	}
	
	
	
	private final static AlphaComposite ac = AlphaComposite.getInstance (AlphaComposite.SRC_OVER, 0.5f);
		
	private final RingChartPainter tbp = new RingChartPainter ();
	
	private class RingChartPainter {
		
		private final Semaphore semaphore = new Semaphore (0);
		
		private ProgressItem _chartRoot = null;
		
		/**
		 * Ritorna la radice del grafico.
		 */
		public ProgressItem getChartRoot () {
			return _chartRoot;
		}
		
		private boolean _needRevalidation;
		/**
		 * Indica se &egrave; un ricaricamento deidati del grafico.
		 * E' necessario quando, a fronte di un evento di variazione della radice, essonon viene 
		 * utilizzato per ricaricare il grafico, in quanto ilgrafico non &egrave; attualmente visibile, e non &egrave; nemmeno
		 * abilitato il ghost nella tabella.
		 */
		public boolean needsRevalidation () {
			return _needRevalidation;
		}
		
		/**
		 * Provoca il ricaricamento del grafico se necessario.
		 */
		public void revalidateIfNeeded () {
			if (_needRevalidation) {
				((RingChartPanel)chartPanel).reloadChart (getChartRoot ());				
			}
		}
		
		RingChartPainter (){
			final Thread t = new Thread (new Runnable () {
				public void run () {
					while (true) {
						semaphore.acquireUninterruptibly ();
						ProgressItem chartRoot = null;
//						synchronized (_chartRoot) {
						chartRoot = _chartRoot;
//						}
						if (chartRoot!=null) {
							if (chartPanel.isVisible () || _context.getApplicationOptions ().isChartGhostEnabled ()) {
								
								_needRevalidation = false;
								try {
									SwingUtilities.invokeAndWait (new Runnable () {

										public void run () {
											((RingChartPanel) chartPanel).reloadChart (_chartRoot);
										}
									});
								} catch (final InterruptedException ex) {
									continue;
								} catch (final InvocationTargetException ex) {
									ex.printStackTrace (System.err);
								}
								
								if (_context.getApplicationOptions ().isChartGhostEnabled ()) {
									/*
									 * Abilita il thread per il ridisegno dello sfondo solo se abilitato
									 */
//									final int w =progressesTable.getWidth ();
//									final int h = progressesTable.getHeight ();
									final int w = chartPanel.getWidth ();
									final int h = chartPanel.getHeight ();
									if (w == 0 || h == 0 ) {
										/*
										 * @workaround necessario per evitare problemi in fase di inizializzazione della tabella
										 */
										continue;
									}
									final BufferedImage i = (BufferedImage)chartPanel.createImage (w, h);
									if (i==null) {
										return;
									}
									final Graphics2D g = i.createGraphics ();
									g.setComposite (ac);
									g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
									g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
									g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
									g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
									g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
									g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
									g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
//							        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
									/*
									 * Invoca il repaint 
									 */
									SwingUtilities.invokeLater (new Runnable () {
										public void run () {
											chartPanel.paint (g);
											setTableBackoundImage (new ImageIcon (i));
											progressesTable.repaint ();
										}
									});
								}
							} else {
								_needRevalidation = true;
							}
						}
						
					}
				}
			}, "table-background-chart-painter");
			t.setPriority (Thread.MIN_PRIORITY);
			t.setDaemon (true);
			t.start ();
		}
		
		public void updateProgressTableBackground (final ProgressItem chartRoot) {
//			synchronized (_chartRoot) {
				if (_chartRoot == chartRoot) {
					return;
				}
				if (chartRoot==null) {
					return;
				}
				_chartRoot = chartRoot;
//			}
			repaintProgressTableBackground ();
		}
		
		public void repaintProgressTableBackground () {
//			System.out.println ("repaintProgressTableBackground ()");
			semaphore.release ();
		}
	}

	/**
	 * Associa una voce del menu ad un'azione (come in swing)
	 */
	private static void bind (final MenuItem mi, final AbstractAction action) {
		mi.setEnabled (action.isEnabled ());
		action.addPropertyChangeListener (new PropertyChangeListener () {
			public void propertyChange (PropertyChangeEvent evt) {
				if (evt.getPropertyName ().equals ("enabled")) {
					final Boolean b = (Boolean)evt.getNewValue ();
					if (b!=null) {
						mi.setEnabled (b.booleanValue ());
					}
				}
			}
		});
		mi.addActionListener (action);
		
	}

	/**
	 * Porta in primo piano questa finestra.
	 */
	public void bringToFront () {
		/*
		 * Ripristina la finestra
		 */
//		dispatchEvent (new WindowEvent (this, WindowEvent.WINDOW_DEICONIFIED));

		getToolkit ().getSystemEventQueue ().postEvent(new WindowEvent (this, WindowEvent.WINDOW_DEICONIFIED));

		
//		setState(Frame.ICONIFIED);
		setState (java.awt.Frame.NORMAL);		
		
		try {
//			setAlwaysOnTop (true);
		} catch (final SecurityException se) {
			se.printStackTrace();
		}
		toFront ();
		try {
//			setAlwaysOnTop (false);
		} catch (final SecurityException se) {
			se.printStackTrace();
		}
		requestFocus ();
	}
	
	private List<Task> getSelectedTasks () {
		final List<Task> l= new ArrayList<Task> ();
		for (final int r : taskTree.getSelectedRows ()){
			l.add ((Task) taskTree.getModel ().getValueAt (taskTree.convertRowIndexToModel (r), taskTree.convertColumnIndexToModel (TaskJTreeModel.TREE_COLUMN_INDEX)));
		}		
		return l;
	}

	public boolean canExit () {
		if (!_context.getModel ().getAdvancing ().isEmpty ()) {
			/*
			 * Azione in corso.
			 */
			final int choice = JOptionPane.showOptionDialog (_context.getWindowManager ().getMainWindow (), 
				java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("ApplicationExit/ActiveActionConfirm/AskUserChoice"), 
				java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("ApplicationExit/ActiveActionConfirm/Title"), 
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, ActiveActionExitChoice.values (), ActiveActionExitChoice.LEAVE);
			
			if (choice<0) {
				return false;
			} else {
				return ActiveActionExitChoice.values ()[choice].process (this);
			}
		}
		return true;
	}
	
	
	
	
		
	/**
	 *Scelta chiusura applicazione in presenza di azione in corso.
	 */
	private enum ActiveActionExitChoice {
		LEAVE {
			public String toString () {
				return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("ConfirmDialog/ApplicationExitWIthActiveAction/Leave");
			}
			
			public boolean process (final MainWindow mw) {
				return true;
			}
		},
		STOP {
			public String toString () {
				return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("ConfirmDialog/ApplicationExitWIthActiveAction/Stop");
			}
			
			public boolean process (final MainWindow mw) {
				mw.stopAdvancing ();
				return true;
			}
		}/*,
		CANCEL {
			public String toString () {
				return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("ConfirmDialog/ApplicationExitWIthActiveAction/Cancel");
			}
			
			public boolean process (final MainWindow mw) {
				return false;
			}
		}*/;
		
		
		/**
		 * Processa la scelta, e ritorna <tt>true</tt> se ilprocessodi chiusura dell'applicazione può continuare.
		 *
		 */
		public abstract boolean process  (MainWindow mw);
	}
}

/**
 * Supporto per la definizione di azioni swing che si basano sull'{@link ApplicationContext}
 *
 * @author davide
 */
abstract class ContextAwareAction extends AbstractAction {
    private final ApplicationContext context;

    public ContextAwareAction (final ApplicationContext context) {
        this.context = context;
    }

    ApplicationContext getContext () {
        return context;
    }
}

class ImportWorkSpaceAction extends ContextAwareAction {

    public ImportWorkSpaceAction (final ApplicationContext context) {
        super (context);
    }

    public void actionPerformed (ActionEvent e) {
        final MainWindow mainWindow = getContext ().getWindowManager ().getMainWindow ();
        getContext ().getLogger ().debug ("Restoring workspace backup...");

        final JFileChooser xmlFileChooser = getContext ().getWindowManager ().getXMLFileChooser ();

        xmlFileChooser.setDialogTitle (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Import_workspace_from"));
        final int returnVal = xmlFileChooser.showOpenDialog (mainWindow);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }

        final SAXBuilder builder = new SAXBuilder ();
        try {
            final org.jdom.Document d = builder.build (new FileInputStream (xmlFileChooser.getSelectedFile ()));

            if (new XMLAgent (getContext ()).restore (d, new XMLImporter.ImportConflictsResolver () {
                public XMLImporter.ConflictResolution chooseResolution () {
                    final XMLImporter.ConflictResolution[] options = new XMLImporter.ConflictResolution[] {XMLImporter.ConflictResolution.OVERWRITE, XMLImporter.ConflictResolution.RENAME, XMLImporter.ConflictResolution.CANCEL};
                    final XMLImporter.ConflictResolution defaultValue = XMLImporter.ConflictResolution.OVERWRITE;
                    final int choice = JOptionPane.showOptionDialog (mainWindow,
                        java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Workspace_import_conflict_resolution_message"),
                        java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Import_conflict"),
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, defaultValue);
                    if (choice<0) {
                        return XMLImporter.ConflictResolution.CANCEL;
                    }
                    return options[choice];
                }

                public String getNewName () {
                    return JOptionPane.showInputDialog (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Rename_importing_workspace_to"));
                }


                public boolean continueOverwritingCurrentWorkspace () {
                    return JOptionPane.YES_OPTION==JOptionPane.showConfirmDialog (mainWindow,
                        java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Current_workspace_overwrite_confirm_equest"),
                        java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Overwriting_current_workspace"),
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                }

                @Override
                public void showErrorMessage (String msg) {
                    JOptionPane.showMessageDialog (mainWindow, msg, java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("MainWindow/GenericErrorDialog/Title"), JOptionPane.ERROR_MESSAGE);
                }
            })) {
                JOptionPane.showMessageDialog (mainWindow, java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Import_completed"));
            }
        } catch (final FileNotFoundException fnfe) {
            throw new RuntimeException (fnfe);
        } catch (final IOException ioe) {
            throw new RuntimeException (ioe);
        } catch (final JDOMException jde) {
            throw new RuntimeException (jde);
        }
        getContext ().getLogger ().debug ("Workspace backup restored");
    }

}

class ExportWorkSpaceAction extends ContextAwareAction {

    public ExportWorkSpaceAction (final ApplicationContext context) {
        super (context);
    }

    public void actionPerformed (ActionEvent e) {
        final MainWindow mainWindow = getContext ().getWindowManager ().getMainWindow ();

        getContext ().getLogger ().debug ("Generating workspace backup...");

        final JFileChooser xmlFileChooser = getContext ().getWindowManager ().getXMLFileChooser ();

        int returnVal;
        boolean isNullFile;
        boolean fileExists;
        do {
            xmlFileChooser.setSelectedFile (new File (getContext ().getModel ().getWorkSpace ().getName ()+"_"+CalendarUtils.getTimestamp (new Date (), "yyyyMMdd")+".xml"));
            xmlFileChooser.setDialogTitle (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Export_workspace_to"));
            returnVal = xmlFileChooser.showSaveDialog(mainWindow);
            if (returnVal != JFileChooser.APPROVE_OPTION) {
                return;
            }
            isNullFile = xmlFileChooser.getSelectedFile ()==null;
            final boolean overwriting = !isNullFile && xmlFileChooser.getSelectedFile ().exists ();
            if (overwriting){
                final int canOverwrite = JOptionPane.showConfirmDialog (mainWindow, "The selected file already exixts. Do you really want to overwrite it?");
                if (canOverwrite==JOptionPane.CANCEL_OPTION){
                    return;
                }
                fileExists = canOverwrite!=JOptionPane.OK_OPTION;
            } else {
                fileExists = false;
            }
        } while (isNullFile || fileExists);

        final org.jdom.Document d = new org.jdom.Document ();
        if (new XMLAgent (getContext ()).backup (getContext ().getModel ().getWorkSpace (), d)) {
            try {
                new XMLOutputter ().output (d, new FileOutputStream (xmlFileChooser.getSelectedFile ()));
                JOptionPane.showMessageDialog (mainWindow, java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Export_completed"));
            } catch (FileNotFoundException ex) {
                throw new RuntimeException (ex);
            } catch (IOException ex) {
                throw new RuntimeException (ex);
            }
        }
        getContext ().getLogger ().debug ("Workspace backup generated");
    }
}