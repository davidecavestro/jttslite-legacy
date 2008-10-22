/*
 * OptionsDialog.java
 *
 * Created on March 7, 2006, 11:45 PM
 */

package com.davidecavestro.timekeeper.gui;

import com.davidecavestro.timekeeper.ApplicationContext;
import com.davidecavestro.timekeeper.conf.ApplicationOptions;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.SortController;
import org.jdesktop.swingx.decorator.Sorter;

/**
 * Dialog di impostazione delle opzioni.
 *
 * @author  davide
 */
public class OptionsDialog extends javax.swing.JDialog {

	private final ApplicationContext _context;
	
	/**
	 * Costruttore
	 */
	public OptionsDialog (final java.awt.Frame parent, final boolean modal, final ApplicationContext context) {
		super (parent, modal);
		this._context = context;
		initComponents ();
		
//		cancelButton.getInputMap (JComponent.WHEN_IN_FOCUSED_WINDOW).put (KeyStroke.getKeyStroke ("ESCAPE"), "cancel");
//		cancelButton.getActionMap().put("cancel", new javax.swing.AbstractAction ("cancel"){
//			public void actionPerformed (ActionEvent ae){
//				cancel ();
//			}
//		});
		
		getDateFormatTable ().setColumnControlVisible (true);
		
				/*
		 * toggle sort
		 * 1 asc, 2 desc, 3 null
		 */
		getDateFormatTable ().setFilters (new FilterPipeline () {
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
		
		
		okButton.getInputMap (JComponent.WHEN_IN_FOCUSED_WINDOW).put (KeyStroke.getKeyStroke ("ESCAPE"), "cancel");
		okButton.getActionMap ().put ("cancel", new javax.swing.AbstractAction ("cancel") {

			public void actionPerformed (ActionEvent ae) {
				okButton.doClick ();
			}
		});
		
		getDateFormatTable ().setAutoStartEditOnKeyStroke (false);
		getDateFormatTable ().setTerminateEditOnFocusLost (true);

		ToolTipManager.sharedInstance ().registerComponent (getDateFormatTable ());

		final SampleUpdater su = new SampleUpdater ();
		final ListSelectionListener lsl =
			new ListSelectionListener () {

				public void valueChanged (ListSelectionEvent e) {
					if (e.getValueIsAdjusting ()) {
						return;
					}
					su.update ();
				}
			};
		getDateFormatTable ().getSelectionModel ().addListSelectionListener (lsl);
		getDateFormatTable ().getColumnModel ().getSelectionModel ().addListSelectionListener (lsl);

		
		this.getRootPane ().setDefaultButton (okButton);
		
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        okButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new JXTable ();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        sampleTextField = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res"); // NOI18N
        setTitle(bundle.getString("Options")); // NOI18N
        setModal(true);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        okButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        okButton.setText(bundle.getString("OptionsDialog/CloseButton/Text")); // NOI18N
        okButton.setMinimumSize(new java.awt.Dimension(80, 25));
        okButton.setPreferredSize(new java.awt.Dimension(80, 25));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        getContentPane().add(okButton, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jTabbedPane1.setFont(new java.awt.Font("Dialog", 0, 12));

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jTable1.setModel(new CustomFormattingTableModel ());
        jTable1.setCellSelectionEnabled(true);
        jTable1.setDefaultRenderer (Object.class, new DefaultTableCellRenderer () {

            //		private final Font valuesFont = new Font("monospaced", Font.PLAIN, 12);

            //		private Font defaultFont;

            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

                final CustomizableFormat fmt = CustomizableFormat.values ()[getDateFormatTable ().convertRowIndexToModel (row)];

                final JLabel label = (JLabel)super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
                //			if (defaultFont==null) {
                    //				defaultFont = label.getFont ();
                    //			}
                if (0 != column) {
                    /* colonna valori */
                    //				label.setFont (valuesFont);
                    label.setToolTipText (null);
                } else {
                    //				label.setFont (defaultFont);
                    label.setToolTipText (fmt.getDescription ());
                }

                return label;
            }

        });
        jScrollPane2.setViewportView(jTable1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(jScrollPane2, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setText(bundle.getString("OptionsDialog/DateFormat/Sample")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(jLabel1, gridBagConstraints);

        sampleTextField.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        sampleTextField.setEditable(false);
        sampleTextField.setPreferredSize(new java.awt.Dimension(280, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 9);
        jPanel1.add(sampleTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel4.add(jPanel1, gridBagConstraints);

        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("com/davidecavestro/timekeeper/gui/res"); // NOI18N
        jTabbedPane1.addTab(bundle1.getString("OptionsDialog/DateFormatTab/Title"), jPanel4); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jTabbedPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 2);
        getContentPane().add(jPanel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel5, gridBagConstraints);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-605)/2, (screenSize.height-249)/2, 605, 249);
    }// </editor-fold>//GEN-END:initComponents

	private void formComponentShown (java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
		init (_context.getApplicationOptions ());		

	}//GEN-LAST:event_formComponentShown

	private void okButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
		confirm ();
	}//GEN-LAST:event_okButtonActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField sampleTextField;
    // End of variables declaration//GEN-END:variables
	
	private final void confirm (){
//		final UserSettings us = _context.getUserSettings ();
		
//		us.setBackupOnSave (Boolean.valueOf (createBackupFilesCheckBox.getModel ().isSelected ()));
//		us.setKeyEditing (Boolean.valueOf (enableKeyEditingCheckBox.getModel ().isSelected ()));
		apply ();
		hide ();
	}
	
	private LookAndFeelChoice _initialChoice;
	
	private void init (ApplicationOptions ao) {
	}
	
	private void cancel (){
		apply ();
		hide ();
	}
	
	private enum LookAndFeelChoice {
		SYSTEM {
			public String getClassName () {
				return UIManager.getSystemLookAndFeelClassName ();
			}
			public String toString () {
				return "System";
			}
		},
		CROSS_PLATFORM {
			public String getClassName () {
				return UIManager.getCrossPlatformLookAndFeelClassName ();
			}
			public String toString () {
				return "Cross Platform";
			}
		},
		WINDOWS {
			public String getClassName () {
				return "com.jgoodies.looks.windows.WindowsLookAndFeel";
			}
			public String toString () {
				return "Windows";
			}
		},
		PLASTIC {
			public String getClassName () {
				return "com.jgoodies.looks.plastic.PlasticLookAndFeel";
			}
			public String toString () {
				return "Plastic";
			}
		},
		PLASTIC2D {
			public String getClassName () {
				return "com.jgoodies.looks.plastic.Plastic3DLookAndFeel";
			}
			public String toString () {
				return "Plastic 3D";
			}
		},
		PLASTICXP {
			public String getClassName () {
				return "com.jgoodies.looks.plastic.PlasticXPLookAndFeel";
			}
			public String toString () {
				return "Plastic XP";
			}
		},
		TINYLAF {
			public String getClassName () {
				return "de.muntjak.tinylookandfeel.TinyLookAndFeel";
			}
			public String toString () {
				return "TinyLAF";
			}
		};
		
		
		public abstract String getClassName ();
		@Override
		public abstract String toString ();
		
	}

	
	private void apply () {
	}
	
	private class CustomFormattingTableModel extends AbstractTableModel {
		
		private final static int COL_NAME = 0;
		private final static int COL_DEFAULT = 1;
		private final static int COL_CUSTOM = 2;
		
		public int getRowCount () {
			return CustomizableFormat.values ().length;
		}

		public int getColumnCount () {
			return 3;
		}

		@Override
		public String getColumnName (int column) {
			switch (column) {
				case COL_NAME: 
					return java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("OptionsDialog/DateFormatTable/NameColumn/Title");
				case COL_CUSTOM: 
					return java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("OptionsDialog/DateFormatTable/CustomColumn/Title");
				case COL_DEFAULT: 
					return java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("OptionsDialog/DateFormatTable/DefaultColumn/Title");
					
				default:
					return null;
			}
		}

		
		
		public Object getValueAt (int rowIndex, int columnIndex) {
			final CustomizableFormat fmt = CustomizableFormat.values ()[rowIndex];
			switch (columnIndex) {
				case COL_NAME: 
					return fmt.getName ();
					
				case COL_CUSTOM: 
					return fmt.getCustomValue (_context);
					
				case COL_DEFAULT: 
					return fmt.getDefaultValue ();
					
				default:
					return null;
			}
		}
		
		
		@Override
		public void setValueAt (Object aValue, int rowIndex, int columnIndex) {
			final CustomizableFormat fmt = CustomizableFormat.values ()[rowIndex];
			switch (columnIndex) {
				case COL_NAME: 
					return;
					
				case COL_CUSTOM: 
					try {
						final SimpleDateFormat df = new SimpleDateFormat ((String)aValue);
					} catch (final IllegalArgumentException iae) {
						JOptionPane.showMessageDialog (OptionsDialog.this, java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("OptionsDialog/DateFOrmatTab/UserInsertedInvalidFormat"));
						return;
					}
					fmt.setCustomValue (_context, (String)aValue);
	                fireTableCellUpdated(rowIndex, columnIndex);
					return;
					
				case COL_DEFAULT: 
					return;
					
				default:
					return;
			}
		}

		@Override
		public boolean isCellEditable (int rowIndex, int columnIndex) {
			return columnIndex == COL_CUSTOM;
		}

	}

	private JXTable getDateFormatTable () {
		return (JXTable)jTable1;
	}
	
	private class SampleUpdater {

		final Date date = new Date ();

		public void update () {
			if (getDateFormatTable ().getSelectedRowCount () == 0 || getDateFormatTable ().getSelectedColumnCount () == 0) {
				sampleTextField.setText ("");
				return;
			}
			final int row = getDateFormatTable ().convertRowIndexToModel (getDateFormatTable ().getSelectedRow ());
			final int col = getDateFormatTable ().convertColumnIndexToModel (getDateFormatTable ().getSelectedColumn ());
			if (col == 0) {
				sampleTextField.setText ("");
				return;
			}
			sampleTextField.setText (new SimpleDateFormat (String.valueOf (getDateFormatTable ().getModel ().getValueAt (row, col))).format (date));

		}
	}
	
}
