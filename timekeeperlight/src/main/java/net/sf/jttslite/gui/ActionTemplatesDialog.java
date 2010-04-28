/*
 * ActionTemplatesDialog.java
 *
 * Created on July 12, 2008, 2:15 PM
 */
package net.sf.jttslite.gui;

import net.sf.jttslite.common.gui.persistence.PersistenceUtils;
import net.sf.jttslite.common.gui.persistence.PersistentComponent;
import net.sf.jttslite.core.util.DurationUtils;
import net.sf.jttslite.ApplicationContext;
import net.sf.jttslite.model.PieceOfWorkTemplateModelListener;
import net.sf.jttslite.model.event.PieceOfWorkTemplateModelEvent;
import net.sf.jttslite.core.model.impl.ProgressTemplate;
import net.sf.jttslite.core.util.DurationImpl;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Comparator;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.SortController;
import org.jdesktop.swingx.decorator.SortOrder;
import org.jdesktop.swingx.decorator.Sorter;

/**
 * Dialog for action templates CRUD.
 *
 * @author  Davide Cavestro
 */
public class ActionTemplatesDialog extends javax.swing.JDialog implements PersistentComponent {

	private final ApplicationContext _context;
	
	/** 
	 * Creates new form ActionTemplatesDialog. 
	 */
	public ActionTemplatesDialog (final ApplicationContext context, java.awt.Frame parent, boolean modal) {
		super (parent, modal);
		
		_context = context;		
		
		initComponents ();
//
//		templateTable.getSelectionModel ().addListSelectionListener (new ListSelectionListener () {
//
//			public void valueChanged (final ListSelectionEvent e) {
//				if (e.getValueIsAdjusting ()) {
//					/* spurios event*/
//					return;
//				}
//				if (templateTable.getSelectedRows ().length > 1) {
//					/*
//					 * too much selected rows
//					 */
//					resetDetails ();
//					return;
//				}
//				int r = templateTable.getSelectedRow ();
//				if (r < 0) {
//					resetDetails ();
//					return;
//				}
//
//				r = ((JXTable) templateTable).convertRowIndexToModel (r);
//				if (r < 0 || r >= templateTable.getModel ().getRowCount ()) {
//					resetDetails ();
//					return;
//				}
//				
//				setDetails ((ProgressTemplate) _context.getTemplateModel ().getElementAt (getTable ().convertRowIndexToModel (r)));
//			}
//
//		});
//		
//		
//		getTable ().getSelectionModel ().addListSelectionListener (new ListSelectionListener () {
//
//			public void valueChanged (ListSelectionEvent e) {
//				final int r = getTable ().convertRowIndexToModel (getTable ().getSelectedRow ());
//				if (r>=0) {
//					setDetails (_context.getTemplateModel ().getElementAt (r));
//				}
//			}
//		});
		
		getTable ().setColumnControlVisible (true);
		
		
		
		/*
		 * Imposta il comparator che sappia ordinare sulal colonan delle durate.
		 */
		getTable ().getColumnExt (getTable ().convertColumnIndexToView (DURATION_COL_INDEX)).setComparator (new Comparator () {
			public int compare (final Object o1, final Object o2) {
				return Double.compare ((double)((DurationImpl)o1).getTime (), (double)((DurationImpl)o2).getTime ());
			}
			public boolean equals (final Object obj) {
				return super.equals (obj);
			}
		});
		
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
		getTable ().setDefaultEditor (DurationImpl.class, new SmartCellEditor (new DurationTableCellEditor ()));

		
		getTable ().setSortOrder (getTable ().convertColumnIndexToView (NAME_COL_INDEX), SortOrder.ASCENDING);
		
		_context.getUIPersister ().register (new TablePersistenceExt (getTable ()) {
			public String getPersistenceKey () {
				return "actiontemplates.table";
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
				final Action _action = _context.getActionTemplatesUndoManager ().getUndoAction ();

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
			final Object command = "actiontemplates.undo.called";
			getRootPane ().getInputMap (JComponent.WHEN_IN_FOCUSED_WINDOW).put ((KeyStroke) action.getValue (Action.ACCELERATOR_KEY), command);
			getRootPane ().getActionMap ().put (command, action);
		}
		
		{
			final Action action = new AbstractAction () {
				final Action _action = _context.getActionTemplatesUndoManager ().getRedoAction ();

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
			final Object command = "actiontemplates.redo.called";
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
        templateTable = new SmartJXTable (new TemplateTableModel ());
        jPanel4 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();

        jMenuItem1.setAction(new AddAction ());
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/add-template.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res"); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jMenuItem1, bundle.getString("ActionTemplatesDialog/Button/Add")); // NOI18N
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setAction(new DeleteAction ());
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/delete-template.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jMenuItem2, bundle.getString("ActionTemplatesDialog/Button/Delete")); // NOI18N
        jPopupMenu1.add(jMenuItem2);
        jPopupMenu1.add(jSeparator1);

        undoMenuItem.setAction(_context.getActionTemplatesUndoManager ().getUndoAction());
        undoMenuItem.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        undoMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/edit-undo.png"))); // NOI18N
        undoMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoMenuItemActionPerformed(evt);
            }
        });
        jPopupMenu1.add(undoMenuItem);

        redoMenuItem.setAction(_context.getActionTemplatesUndoManager ().getRedoAction());
        redoMenuItem.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        redoMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/edit-redo.png"))); // NOI18N
        jPopupMenu1.add(redoMenuItem);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(bundle.getString("ActionTemplatesDialog/Title")); // NOI18N
        setModal(true);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setPreferredSize(new java.awt.Dimension(453, 203));
        getTable ().setDefaultRenderer (DurationImpl.class, new DefaultTableCellRenderer () {

            public Component getTableCellRendererComponent (final JTable table, final Object value, boolean isSelected, boolean hasFocus, final int row, final int column) {

                final JLabel res = (JLabel)super.getTableCellRendererComponent ( table, value, isSelected, hasFocus, row, column);
                final DurationImpl duration = (DurationImpl)value;

                if (duration ==null || duration.getTime ()==0){
                    res.setText ("");
                } else {
                    res.setText (DurationUtils.format (duration));
                }

                setHorizontalAlignment (TRAILING);
                return res;
            }

        });

        templateTable.setModel(new TemplateTableModel ());
        templateTable.setComponentPopupMenu(jPopupMenu1);
        templateTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(templateTable);

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
        org.openide.awt.Mnemonics.setLocalizedText(okButton, bundle.getString("ActionTemplatesDialog/Button/Confirm")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 4, 6, 8);
        getContentPane().add(okButton, gridBagConstraints);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        addButton.setAction(new AddAction ());
        addButton.setFont(new java.awt.Font("Dialog", 0, 12));
        addButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/add-template.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addButton, bundle.getString("ActionTemplatesDialog/Button/Add")); // NOI18N
        addButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 4, 2, 8);
        jPanel5.add(addButton, gridBagConstraints);

        deleteButton.setAction(new DeleteAction ());
        deleteButton.setFont(new java.awt.Font("Dialog", 0, 12));
        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jttslite/gui/images/delete-template.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(deleteButton, bundle.getString("ActionTemplatesDialog/Button/Delete")); // NOI18N
        deleteButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 2, 8);
        jPanel5.add(deleteButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
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
    private javax.swing.JMenuItem redoMenuItem;
    private javax.swing.JTable templateTable;
    private javax.swing.JMenuItem undoMenuItem;
    // End of variables declaration//GEN-END:variables

	/**
	 * Indice della colonna di tabella contenente il nome.
	 */
	private final static int NAME_COL_INDEX = 0;
	
	/**
	 * Indice della colonna di tabella contenente la durata.
	 */
	private final static int DURATION_COL_INDEX = 1;
	
	/**
	 * Indice della colonna di tabella contenente le annotazioni.
	 */
	private final static int NOTES_COL_INDEX = 2;
	
	private class TemplateTableModel extends AbstractTableModel implements PieceOfWorkTemplateModelListener {

		public TemplateTableModel () {
			_context.getTemplateModel ().addPieceOfWorkTemplateModelListener (this);
		}

		
		private final String[] _columnNames = new String[] {
			java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("ActionTemplatesDialog/TemplatesTable/ColumnHeader/Name"),
			java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("ActionTemplatesDialog/TemplatesTable/ColumnHeader/Duration"),
			java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("ActionTemplatesDialog/TemplatesTable/ColumnHeader/Notes")
		};
		
		private final Class[] _columnClasses = new Class[] {
			String.class,
			DurationImpl.class,
			String.class
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
			return _context.getTemplateModel ().size ();
		}

		public int getColumnCount () {
			return _columnNames.length;
		}

		public Object getValueAt (final int r, final int c) {
			final int rowIndex = r;//getTable ().convertRowIndexToModel (r);
			final int columnIndex = c;//getTable ().convertColumnIndexToModel (c);
			switch (columnIndex) {
				case NAME_COL_INDEX:
					return _context.getTemplateModel ().get (rowIndex).getName ();
				case DURATION_COL_INDEX:
					return _context.getTemplateModel ().get (rowIndex).getDuration ();
				case NOTES_COL_INDEX:
					return _context.getTemplateModel ().get (rowIndex).getNotes ();
				default:
					throw new RuntimeException ("Invalid column");
			}
		}
		
		/**
		 * Imposta il valore nel modello della tabella, propagandolo al modello applicativo.
		 */
		@Override
		public void setValueAt (final Object aValue, final int rowIndex, final int columnIndex) {
			final ProgressTemplate template = _context.getTemplateModel ().get (rowIndex);
			
			switch (columnIndex) {
				case DURATION_COL_INDEX:
				{
					_context.getTemplateModel ().updateElement (template, template.getName (), template.getNotes (), (DurationImpl)aValue);
					break;
				}
				case NAME_COL_INDEX:
				{
					_context.getTemplateModel ().updateElement (template, (String)aValue, template.getNotes (), template.getDuration ());
					break;
				}
				case NOTES_COL_INDEX:
				{
					_context.getTemplateModel ().updateElement (template, template.getName (), (String)aValue, template.getDuration ());
					break;
				}
				default:
					throw new RuntimeException ("Invalid column");
			}
		}		

		@Override
		public boolean isCellEditable (int rowIndex, int columnIndex) {
			return true;
		}
		public void intervalAdded (PieceOfWorkTemplateModelEvent e) {
			fireTableRowsInserted (e.getIndex0 (), e.getIndex1 ());
		}

		public void intervalRemoved (PieceOfWorkTemplateModelEvent e) {
			fireTableRowsDeleted (e.getIndex0 (), e.getIndex1 ());
		}

		public void contentsChanged (PieceOfWorkTemplateModelEvent e) {
			fireTableRowsUpdated (e.getIndex0 (), e.getIndex1 ());
		}

		
	}
	
	private JXTable getTable () {
		return (JXTable)templateTable;
	}
	
	
	private class AddAction extends AbstractAction {

		public void actionPerformed (ActionEvent e) {
			
			final ProgressTemplate template = new ProgressTemplate ();
			template.setName ("");
			template.setDuration (new DurationImpl (0, 10, 0, 0));
			_context.getTemplateModel ().addElement (template);
			
			final int newSelection = getTable ().convertRowIndexToView (_context.getTemplateModel ().indexOf (template));
			getTable ().getSelectionModel ().setSelectionInterval (newSelection, newSelection);
			//final int cellIndex = getTable ().convertColumnIndexToView (NAME_COL_INDEX);
			//getTable ().requestFocusInWindow ();
			//getTable ().editCellAt (newSelection, cellIndex);
		}
	}
	
	private class DeleteAction extends AbstractAction implements ListSelectionListener {
		public DeleteAction () {
			setEnabled (false);
			this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_DELETE, 0));
			getTable ().getSelectionModel ().addListSelectionListener (this);
		}

		public void actionPerformed (ActionEvent e) {
			final int r = getTable ().getSelectedRow ();
			_context.getTemplateModel ().removeElementAt (getTable ().convertRowIndexToModel (r));
			
			int newSelection = r;
			if (newSelection>=getTable ().getRowCount ()) {
				newSelection = getTable ().getRowCount ()-1;
			}
			if (newSelection>=0 && newSelection < getTable ().getRowCount ()) {
				getTable ().getSelectionModel ().setSelectionInterval (newSelection, newSelection);
			}
		}

		public void valueChanged (ListSelectionEvent e) {
			setEnabled (getTable ().getSelectedRowCount ()>0);
		}
		
	}
	

	
	private class ConfirmAction extends AbstractAction {

		public void actionPerformed (ActionEvent e) {
			setVisible (false);
		}
	}

	public String getPersistenceKey () {
		return "actionTemplatesDialog";
	}
	
	public void makePersistent (net.sf.jttslite.common.gui.persistence.PersistenceStorage props) {
		PersistenceUtils.makeBoundsPersistent (props, this.getPersistenceKey (), this);
	}
	
	public boolean restorePersistent (net.sf.jttslite.common.gui.persistence.PersistenceStorage props) {
		return PersistenceUtils.restorePersistentBounds (props, this.getPersistenceKey (), this);
	}
	
}
