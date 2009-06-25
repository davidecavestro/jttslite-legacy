/*
 * WorkspacesDialog.java
 *
 * Created on March 29, 2009, 9:07 AM
 */
package com.davidecavestro.timekeeper.gui;

import com.davidecavestro.common.gui.persistence.PersistenceUtils;
import com.davidecavestro.common.gui.persistence.PersistentComponent;
import com.davidecavestro.timekeeper.model.CannotRemoveWorkSpaceException;
import com.davidecavestro.timekeeper.model.event.WorkSpaceModelEvent;
import com.davidecavestro.timekeeper.ApplicationContext;
import com.davidecavestro.timekeeper.model.DuplicatedWorkSpaceException;
import com.davidecavestro.timekeeper.model.WorkSpace;
import com.davidecavestro.timekeeper.model.WorkSpaceModelListener;
import com.ost.timekeeper.model.ProgressItem;
import com.ost.timekeeper.model.Project;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;
import java.util.Comparator;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.SortController;
import org.jdesktop.swingx.decorator.SortOrder;
import org.jdesktop.swingx.decorator.Sorter;

/**
 * Dialog for workspaces CRUD.
 *
 * @author  Davide Cavestro
 */
public class WorkspacesDialog extends javax.swing.JDialog implements PersistentComponent {

	private final ApplicationContext _context;
	
	/** 
	 * Creates a new dialog WorkspacesDialog.
	 */
	public WorkspacesDialog (final ApplicationContext context, java.awt.Frame parent, boolean modal) {
		super (parent, modal);
		
		_context = context;		
		
		initComponents ();

		getTable ().setColumnControlVisible (true);
		
		/*
		 * toggle sort
		 * 1 asc, 2 desc, 3 null
		 */
		getTable ().setFilters (new FilterPipeline () {
			@Override
			protected SortController createDefaultSortController () {
				return new SorterBasedSortController () {
					
					@Override
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

		
		getTable ().setSortOrder (getTable ().convertColumnIndexToView (NAME_COL_INDEX), SortOrder.ASCENDING);
		
		_context.getUIPersister ().register (new TablePersistenceExt (getTable ()) {
			public String getPersistenceKey () {
				return "workspaces.table";
			}
		});		
		
		/*
		 * Shortcut per cancellazione da tabella
		 */
		getTable ().addKeyListener (
			new KeyAdapter () {
			@Override
				public void keyPressed (KeyEvent e) {
					if (e.getKeyCode () == KeyEvent.VK_DELETE) {
						int rowIdx = getTable ().getSelectedRow ();
						if (rowIdx >= 0) {
							deleteButton.doClick ();
							e.consume ();
						}
					}
				}
			});
		
//		GUIUtils.addBorderRollover (new Component[] {addButton});
//		GUIUtils.addBorderRollover (new Component[] {deleteButton});
//		
		
		okButton.getInputMap (JComponent.WHEN_IN_FOCUSED_WINDOW).put (KeyStroke.getKeyStroke ("ESCAPE"), "cancel");
		okButton.getActionMap ().put ("cancel", new javax.swing.AbstractAction ("cancel") {

			public void actionPerformed (ActionEvent ae) {
				okButton.doClick ();
			}
		});
		
		getTable ().setAutoStartEditOnKeyStroke (false);
		getTable ().setTerminateEditOnFocusLost (true);
		
		

		/*
		 * shortcut undo redo
		 */
		{
			final Action action = new AbstractAction () {
				final Action _action = _context.getWorkspacesUndoManager ().getUndoAction ();

				@Override
				public Object getValue (String key) {
					return _action.getValue (key);
				}

				@Override
				public void putValue (String key, Object value) {
					_action.putValue (key, value);
				}

				@Override
				public void setEnabled (boolean b) {
					_action.setEnabled (b);
				}

				@Override
				public boolean isEnabled () {
					return _action.isEnabled ();
				}


				public void actionPerformed (ActionEvent e) {
					/*
					 * Scarta l'evento in caso di editazione in corso
					 */
					if (getTable ().isEditing ()) {
						return;
					}
					_action.actionPerformed (e);
				}
			};
			final Object command = "workspaces.undo.called";
			getRootPane ().getInputMap (JComponent.WHEN_IN_FOCUSED_WINDOW).put ((KeyStroke) action.getValue (Action.ACCELERATOR_KEY), command);
			getRootPane ().getActionMap ().put (command, action);
		}
		
		{
			final Action action = new AbstractAction () {
				final Action _action = _context.getWorkspacesUndoManager ().getRedoAction ();

				@Override
				public Object getValue (String key) {
					return _action.getValue (key);
				}

				@Override
				public void putValue (String key, Object value) {
					_action.putValue (key, value);
				}

				@Override
				public void setEnabled (boolean b) {
					_action.setEnabled (b);
				}

				@Override
				public boolean isEnabled () {
					return _action.isEnabled ();
				}


				public void actionPerformed (ActionEvent e) {
					/*
					 * Scarta l'evento in caso di editazione in corso
					 */
					if (getTable ().isEditing ()) {
						return;
					}
					_action.actionPerformed (e);
				}
			};
			final Object command = "workspaces.redo.called";
			getRootPane ().getInputMap (JComponent.WHEN_IN_FOCUSED_WINDOW).put ((KeyStroke) action.getValue (Action.ACCELERATOR_KEY), command);
			getRootPane ().getActionMap ().put (command, action);
		}
		
//		pack ();
		setLocationRelativeTo (null);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        undoMenuItem = new javax.swing.JMenuItem();
        redoMenuItem = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        workspaceTable = new SmartJXTable (new WorkspaceTableModel ());
        jPanel4 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        openButton = new javax.swing.JButton();
        exportButton = new javax.swing.JButton();
        importButton = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();

        jMenuItem1.setAction(new AddAction ());
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/add-workspace.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res"); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jMenuItem1, bundle.getString("WorkspacesDialog/Button/Add")); // NOI18N
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setAction(new DeleteAction ());
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/delete-workspace.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jMenuItem2, bundle.getString("WorkspacesDialog/Button/Delete")); // NOI18N
        jPopupMenu1.add(jMenuItem2);
        jPopupMenu1.add(jSeparator1);

        undoMenuItem.setAction(_context.getWorkspacesUndoManager ().getUndoAction ());
        undoMenuItem.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        undoMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/edit-undo.png"))); // NOI18N
        undoMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoMenuItemActionPerformed(evt);
            }
        });
        jPopupMenu1.add(undoMenuItem);

        redoMenuItem.setAction(_context.getWorkspacesUndoManager ().getRedoAction ());
        redoMenuItem.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        redoMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/edit-redo.png"))); // NOI18N
        jPopupMenu1.add(redoMenuItem);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(bundle.getString("WorkspacesDialog/Title")); // NOI18N
        setModal(true);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setPreferredSize(new java.awt.Dimension(453, 203));

        workspaceTable.setModel(new WorkspaceTableModel ());
        workspaceTable.setComponentPopupMenu(jPopupMenu1);
        workspaceTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(workspaceTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(jScrollPane1, gridBagConstraints);

        jPanel4.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jPanel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        getContentPane().add(jPanel1, gridBagConstraints);

        okButton.setAction(new ConfirmAction ());
        okButton.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(okButton, bundle.getString("WorkspacesDialog/Button/Confirm")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 4, 6, 8);
        getContentPane().add(okButton, gridBagConstraints);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        addButton.setAction(new AddAction ());
        addButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        addButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/add-workspace.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addButton, bundle.getString("WorkspacesDialog/Button/Add")); // NOI18N
        addButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 4, 2, 8);
        jPanel5.add(addButton, gridBagConstraints);

        deleteButton.setAction(new DeleteAction ());
        deleteButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/delete-workspace.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(deleteButton, bundle.getString("WorkspacesDialog/Button/Delete")); // NOI18N
        deleteButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 2, 8);
        jPanel5.add(deleteButton, gridBagConstraints);

        openButton.setAction(new OpenAction ());
        openButton.setFont(new java.awt.Font("Dialog", 0, 12));
        openButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/folder-open.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(openButton, bundle.getString("WorkspacesDialog/Button/Open")); // NOI18N
        openButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 4, 2, 8);
        jPanel5.add(openButton, gridBagConstraints);

        exportButton.setAction(new ExportWorkSpaceAction (_context));
        exportButton.setFont(new java.awt.Font("Dialog", 0, 12));
        exportButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/fileexport.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(exportButton, bundle.getString("WorkspacesDialog/Button/Export")); // NOI18N
        exportButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 4, 2, 8);
        jPanel5.add(exportButton, gridBagConstraints);

        importButton.setAction(new ImportWorkSpaceAction (_context));
        importButton.setFont(new java.awt.Font("Dialog", 0, 12));
        importButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/fileimport.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(importButton, bundle.getString("WorkspacesDialog/Button/Import")); // NOI18N
        importButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 2, 8);
        jPanel5.add(importButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel5.add(jPanel6, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jPanel5, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void undoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoMenuItemActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_undoMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton exportButton;
    private javax.swing.JButton importButton;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton okButton;
    private javax.swing.JButton openButton;
    private javax.swing.JMenuItem redoMenuItem;
    private javax.swing.JMenuItem undoMenuItem;
    private javax.swing.JTable workspaceTable;
    // End of variables declaration//GEN-END:variables

	/**
	 * Indice della colonna di tabella contenente il nome.
	 */
	private final static int NAME_COL_INDEX = 0;
	
	/**
	 * Indice della colonna di tabella contenente la descrizione.
	 */
	private final static int DESCRIPTION_COL_INDEX = 1;
	
//	/**
//	 * Indice della colonna di tabella contenente le annotazioni.
//	 */
//	private final static int NOTES_COL_INDEX = 2;
	
	private class WorkspaceTableModel extends AbstractTableModel implements WorkSpaceModelListener {

		public WorkspaceTableModel () {
			_context.getWorkSpaceModel ().addWorkSpaceModelListener (this);
		}

		
		private final String[] _columnNames = new String[] {
			java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("WorkspacesDialog/WorkspaceTable/ColumnHeader/Name"),
			java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("WorkspacesDialog/WorkspaceTable/ColumnHeader/Description")/*,
			java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("WorkspacesDialog/WorkspaceTable/ColumnHeader/Notes")*/
		};
		
		private final Class[] _columnClasses = new Class[] {
			String.class,
			String.class/*,
			String.class*/
		};

		@Override
		public String getColumnName (int columnIndex) {
			return _columnNames[columnIndex];
		}		
		
		@Override
		public Class<?> getColumnClass (int columnIndex) {
			return _columnClasses[columnIndex];
		}
		
		public int getRowCount () {
			return _context.getWorkSpaceModel ().size ();
		}

		public int getColumnCount () {
			return _columnNames.length;
		}

		public Object getValueAt (final int r, final int c) {
			final int rowIndex = r;//getTable ().convertRowIndexToModel (r);
			final int columnIndex = c;//getTable ().convertColumnIndexToModel (c);
			switch (columnIndex) {
				case NAME_COL_INDEX:
					return _context.getWorkSpaceModel ().get (rowIndex).getName ();
				case DESCRIPTION_COL_INDEX:
					return _context.getWorkSpaceModel ().get (rowIndex).getDescription ();
//				case NOTES_COL_INDEX:
//					return _context.getWorkSpaceModel ().get (rowIndex).getNotes ();
				default:
					throw new RuntimeException ("Invalid column");
			}
		}
		
		/**
		 * Imposta il valore nel modello della tabella, propagandolo al modello applicativo.
		 */
		@Override
		public void setValueAt (final Object aValue, final int rowIndex, final int columnIndex) {
			final WorkSpace workspace = _context.getWorkSpaceModel ().get (rowIndex);
			
			switch (columnIndex) {
				case NAME_COL_INDEX:
				{
					_context.getWorkSpaceModel ().updateElement (workspace, (String)aValue, workspace.getDescription (), workspace.getNotes ());
					break;
				}
				case DESCRIPTION_COL_INDEX:
				{
					_context.getWorkSpaceModel ().updateElement (workspace, workspace.getName (), (String)aValue, workspace.getNotes ());
					break;
				}
//				case NOTES_COL_INDEX:
//				{
//					_context.getWorkSpaceModel ().updateElement (workspace, workspace.getName (), workspace.getDescription (), (String)aValue);
//					break;
//				}
				default:
					throw new RuntimeException ("Invalid column");
			}
		}		

		@Override
		public boolean isCellEditable (final int rowIndex, final int columnIndex) {
			return true;
		}

		public void intervalAdded (final WorkSpaceModelEvent e) {
			fireTableRowsInserted (e.getIndex0 (), e.getIndex1 ());
		}

		public void intervalRemoved (final WorkSpaceModelEvent e) {
			fireTableRowsDeleted (e.getIndex0 (), e.getIndex1 ());
		}

		public void contentsChanged (final WorkSpaceModelEvent e) {
			fireTableRowsUpdated (e.getIndex0 (), e.getIndex1 ());
		}

		
	}
	
	private JXTable getTable () {
		return (JXTable)workspaceTable;
	}
	
	
	private class AddAction extends AbstractAction {

		public void actionPerformed (ActionEvent e) {
			try {
				final WorkSpace workspace = WorkspacesDialog.createWorkspace (_context, new WorkspacesDialog.WorkspaceDataCallback () {

					/*
					 * impostata a stringa vuota per consentire di discriminare il valore null ricevuto in caso di cancel
					 */
					private String projectName="";

					public String getName () throws RequestAbortedException {
						askData ();
						return projectName;
					}

					public ProgressItem getRoot () throws RequestAbortedException {
						askData ();
						return new ProgressItem (projectName);
					}

					private void askData () throws RequestAbortedException {
						/*
						 * cicla finchè non viene dato un valore valido oppure null
						 */
						while (projectName != null && projectName.trim ().length () == 0) {
							projectName = (String) JOptionPane.showInputDialog (WorkspacesDialog.this,
								java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Please_insert_new_workspace_name"),
								java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("New_workspace"),
								JOptionPane.QUESTION_MESSAGE,
								null, null, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("New_workspace"));
							if (projectName==null) {
								/*
								 * l'utente ha premuto cancel
								 */
								throw new RequestAbortedException ();
							}
						}
					}
				});
				
				final int newSelection = getTable ().convertRowIndexToView (_context.getWorkSpaceModel ().indexOf (workspace));
				getTable ().getSelectionModel ().setSelectionInterval (newSelection, newSelection);
			} catch (final RequestAbortedException ex) {
				/*
				 * richiesta interrotta dall'utente
				 */
			} catch (final DuplicatedWorkSpaceException ex) {
				JOptionPane.showMessageDialog (WorkspacesDialog.this, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("New_workspace_conflicting_name_message"));
			}
			
		}
	}
	
	private class DeleteAction extends AbstractAction implements ListSelectionListener {
		public DeleteAction () {
			setEnabled (false);
			this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_DELETE, 0));
			getTable ().getSelectionModel ().addListSelectionListener (this);
		}

		public void actionPerformed (final ActionEvent e) {
			final int r = getTable ().getSelectedRow ();

            if (JOptionPane.showConfirmDialog (
                WorkspacesDialog.this,
                MessageFormat.format (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("WorkspacesDialog/WarningMessage/Removing_workspace"), getSelectedWorkspace ().getName ()),
                java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("WorkspacesDialog/WarningMessageTitle/Removing_workspace"),
                JOptionPane.YES_NO_OPTION)
                !=JOptionPane.YES_OPTION) {
                return;
            }

            try {
                _context.getWorkSpaceModel ().removeElementAt (getTable ().convertRowIndexToModel (r));
            } catch (final CannotRemoveWorkSpaceException ex) {
                /*
                 * questo caso non si dovrebbe verificare, fintantochè l'azione
                 * viene abilitata solo per i progetti non in uso
                 */
                JOptionPane.showMessageDialog (WorkspacesDialog.this, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("WorkspacesDialog/ErrorMessage/CannotRemoveWorkSpaceException"));
                return;
            }

            /*
             * reimposta una selezione adeguata sulla tabella
             */
			int newSelection = r;
			if (newSelection>=getTable ().getRowCount ()) {
				newSelection = getTable ().getRowCount ()-1;
			}
			if (newSelection>=0 && newSelection < getTable ().getRowCount ()) {
				getTable ().getSelectionModel ().setSelectionInterval (newSelection, newSelection);
			}
		}

		public void valueChanged (ListSelectionEvent e) {
			if (e.getValueIsAdjusting ()) {
				/*
				 * scarta eventi spuri (non definitivi).
				 */
				return;
			}
			checkEnabled ();
		}
		
		private void checkEnabled () {
			/*
			 * abilitata solo se cè una selezione
			 */
			if (getTable ().getSelectedRowCount ()==0) {
				setEnabled (false);
				return;
			}
			final WorkSpace ws = getSelectedWorkspace ();
			
			/*
			 * e se il progetto selezionato non è aperto
			 */
			setEnabled (ws!=null && !ws.equals (_context.getModel ().getWorkSpace ()));
		}

		
	}
	

	private class OpenAction extends AbstractAction implements ListSelectionListener {

		public OpenAction () {
			setEnabled (false);
			getTable ().getSelectionModel ().addListSelectionListener (this);
		}
		
		public void actionPerformed (ActionEvent e) {
			final int r = getTable ().getSelectedRow ();
			final WorkSpace ws = _context.getWorkSpaceModel ().getElementAt (getTable ().convertRowIndexToModel (r));
			OpenWorkSpaceDialog.openWorkspace (_context, ws);
		}

		public void valueChanged (ListSelectionEvent e) {
			if (e.getValueIsAdjusting ()) {
				/*
				 * scarta eventi spuri (non definitivi).
				 */
				return;
			}
			/*
			 * abilitata solo se cè una selezione
			 */
			setEnabled (getTable ().getSelectedRowCount ()>0);
		}
		
	}
	
	
	private class ConfirmAction extends AbstractAction {

		public void actionPerformed (ActionEvent e) {
			setVisible (false);
		}
	}

	public String getPersistenceKey () {
		return "workspacesDialog";
	}
	
	public void makePersistent (com.davidecavestro.common.gui.persistence.PersistenceStorage props) {
		PersistenceUtils.makeBoundsPersistent (props, this.getPersistenceKey (), this);
	}
	
	public boolean restorePersistent (com.davidecavestro.common.gui.persistence.PersistenceStorage props) {
		return PersistenceUtils.restorePersistentBounds (props, this.getPersistenceKey (), this);
	}
	
	private ImageIcon getIcon () {
		return new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/folder.png"));
	}
	
	private WorkSpace getSelectedWorkspace () {

		final int r = getTable ().convertRowIndexToModel (getTable ().getSelectedRow ());
        if (r<0) {
            return null;
        }
		return _context.getWorkSpaceModel ().getElementAt (r);
	}
	
	public static WorkSpace createWorkspace (final ApplicationContext context, final WorkspaceDataCallback callback) throws DuplicatedWorkSpaceException, RequestAbortedException {

			context.getLogger ().debug ("Creating new workspace");
			
			final WorkSpace prj = new Project (callback.getName (), callback.getRoot ());
			context.getWorkSpaceModel ().addElement (prj);
			if (!OpenWorkSpaceDialog.openWorkspace (context, prj)) {
				/*
				 * Notifica all'utente che il nuovo workspace non è stato aperto, ma è comunque disponibile
				 */
				JOptionPane.showMessageDialog (context.getWindowManager ().getMainWindow (), 
				MessageFormat.format (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("WorkSpacesDialog/CreationMessageDialog/Text"), prj.getName ()), 
				java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("WorkSpacesDialog/CreationMessageDialog/Title"), 
				JOptionPane.INFORMATION_MESSAGE);
			}
			
			context.getLogger ().debug ("New workspace created");
			
			return prj;
	}
	
	public static interface WorkspaceDataCallback {
		String getName () throws RequestAbortedException;
		ProgressItem getRoot () throws RequestAbortedException;
	}
	

}
