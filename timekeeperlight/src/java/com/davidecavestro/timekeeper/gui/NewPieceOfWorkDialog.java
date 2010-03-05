/*
 * NewTaskDialog.java
 *
 * Created on 10 dicembre 2005, 10.38
 */
package com.davidecavestro.timekeeper.gui;

import com.davidecavestro.common.gui.GUIUtils;
import com.ost.timekeeper.util.DurationUtils;
import com.davidecavestro.common.gui.dialog.DialogEvent;
import com.davidecavestro.common.gui.dialog.DialogNotifier;
import com.davidecavestro.common.gui.dialog.DialogNotifierImpl;
import com.davidecavestro.common.gui.persistence.PersistenceUtils;
import com.davidecavestro.common.gui.persistence.PersistentComponent;
import com.davidecavestro.timekeeper.ApplicationContext;
import com.davidecavestro.timekeeper.model.PieceOfWorkTemplateModelListener;
import com.davidecavestro.timekeeper.model.Task;
import com.davidecavestro.timekeeper.model.event.PieceOfWorkTemplateModelEvent;
import com.ost.timekeeper.model.ProgressTemplate;
import com.ost.timekeeper.util.Duration;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.text.DateFormatter;

/**
 * Dialog for action creation.
 *
 * @author  davide
 */
public class NewPieceOfWorkDialog extends javax.swing.JDialog implements PersistentComponent, DialogNotifier, PropertyChangeListener {

	private final DialogNotifierImpl _dialogNotifier;
	private final ApplicationContext _context;

	/**
	 * Creates new form NewTaskDialog
	 */
	public NewPieceOfWorkDialog (final ApplicationContext context, java.awt.Frame parent, boolean modal) {
		super (parent, modal);
		_context = context;
		initComponents ();
		this._dialogNotifier = new DialogNotifierImpl ();
		this.getRootPane ().setDefaultButton (okButton);

		cancelButton.getInputMap (JComponent.WHEN_IN_FOCUSED_WINDOW).put (KeyStroke.getKeyStroke ("ESCAPE"), "cancel");
		cancelButton.getActionMap ().put ("cancel", new javax.swing.AbstractAction ("cancel") {

			public void actionPerformed (ActionEvent ae) {
				close ();
			}
		});

		initDateFormat ();

		templateCombo.setRenderer (new DefaultListCellRenderer () {

			@Override
			public Component getListCellRendererComponent (JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent (list, value, index, isSelected, cellHasFocus);

				if (value == null || !(value instanceof ProgressTemplate)) {
					//la stringa vuota inibisce la visualizzazione della riga
					setText (" ");
				} else {
					final ProgressTemplate template = (ProgressTemplate) value;
					setText ( "[" + DurationUtils.format (template.getDuration ()) + "] " + template.getName ());
				}

				return this;
			}
		});

		pack ();
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        descriptionPane = new javax.swing.JScrollPane();
        descriptionField = new javax.swing.JTextArea();
        fromField = new javax.swing.JFormattedTextField();
        toField = new javax.swing.JFormattedTextField();
        durationField = new DurationTextField ();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        templateCombo = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res"); // NOI18N
        setTitle(bundle.getString("New_progress")); // NOI18N
        setModal(true);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setLabelFor(fromField);
        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("com/davidecavestro/timekeeper/gui/res"); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, bundle1.getString("NewPieceOfWorkDialog/From/Label")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(jLabel1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setLabelFor(toField);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, bundle1.getString("NewPieceOfWorkDialog/To/Label")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(jLabel2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel3.setLabelFor(descriptionField);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, bundle1.getString("NewPieceOfWorkDialog/Notes/Label")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(jLabel3, gridBagConstraints);

        descriptionField.setColumns(20);
        descriptionField.setRows(5);
        descriptionPane.setViewportView(descriptionField);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(descriptionPane, gridBagConstraints);

        fromField.setText("jFormattedTextField1");
        fromField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fromFieldActionPerformed(evt);
            }
        });
        fromField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fromFieldPropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(fromField, gridBagConstraints);

        toField.setText("jFormattedTextField1");
        toField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toFieldActionPerformed(evt);
            }
        });
        toField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                toFieldPropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(toField, gridBagConstraints);

        durationField.setText("jFormattedTextField1");
        durationField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                durationFieldActionPerformed(evt);
            }
        });
        durationField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                durationFieldPropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(durationField, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel4.setLabelFor(durationField);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, bundle1.getString("NewPieceOfWorkDialog/Duration/Label")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(jLabel4, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jPanel2, gridBagConstraints);

        cancelButton.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, bundle.getString("Cancel")); // NOI18N
        cancelButton.setPreferredSize(new java.awt.Dimension(80, 25));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        jPanel1.add(cancelButton, gridBagConstraints);

        okButton.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(okButton, bundle.getString("Ok")); // NOI18N
        okButton.setPreferredSize(new java.awt.Dimension(80, 25));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        jPanel1.add(okButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jPanel1, gridBagConstraints);

        templateCombo.setFont(new java.awt.Font("Dialog", 0, 12));
        templateCombo.setModel(new TemplateModel ());
        templateCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                templateComboActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(templateCombo, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel5.setLabelFor(templateCombo);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, bundle1.getString("NewPieceOfWorkDialog/TemplateCombo/Label")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(jLabel5, gridBagConstraints);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/tag_blue_edit.png"))); // NOI18N
        jButton1.setToolTipText(bundle.getString("NewPieceOfWorkDialog//ButtonTooltip/ManageTemplates")); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setMaximumSize(new java.awt.Dimension(26, 26));
        jButton1.setMinimumSize(new java.awt.Dimension(26, 26));
        jButton1.setPreferredSize(new java.awt.Dimension(26, 26));
        GUIUtils.addBorderRollover (new Component[] {jButton1});
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 6);
        getContentPane().add(jButton1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void durationFieldPropertyChange (java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_durationFieldPropertyChange
		syncToDate ();
		check ();
	}//GEN-LAST:event_durationFieldPropertyChange

	private void toFieldPropertyChange (java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_toFieldPropertyChange
		syncDuration ();
		check ();
	}//GEN-LAST:event_toFieldPropertyChange

	private void fromFieldPropertyChange (java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fromFieldPropertyChange
		syncDuration ();
		check ();
	}//GEN-LAST:event_fromFieldPropertyChange

	private void durationFieldActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_durationFieldActionPerformed
		syncToDate ();
		check ();
	}//GEN-LAST:event_durationFieldActionPerformed

	private void toFieldActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toFieldActionPerformed
		syncDuration ();
		check ();
	}//GEN-LAST:event_toFieldActionPerformed

	private void fromFieldActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fromFieldActionPerformed
		syncDuration ();
		check ();
	}//GEN-LAST:event_fromFieldActionPerformed

	private void cancelButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
		close ();
	}//GEN-LAST:event_cancelButtonActionPerformed

	private void okButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
		confirm ();
	}//GEN-LAST:event_okButtonActionPerformed

private void templateComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_templateComboActionPerformed
	setTemplate ();
}//GEN-LAST:event_templateComboActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
_context.getWindowManager ().getActionTemplatesDialog ().show ();
}//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextArea descriptionField;
    private javax.swing.JScrollPane descriptionPane;
    private javax.swing.JFormattedTextField durationField;
    private javax.swing.JFormattedTextField fromField;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton okButton;
    private javax.swing.JComboBox templateCombo;
    private javax.swing.JFormattedTextField toField;
    // End of variables declaration//GEN-END:variables

	private void confirm () {
		this._dialogNotifier.fireDialogPerformed (new DialogEvent (this, JOptionPane.OK_OPTION));
		this.setVisible (false);
	}
	private Task _task;

	public void showForTask (Task t) {
		this._task = t;
		setTitle (java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("New_progress") + " - [" + t.getName () + "]");
		show ();
	}

	public Task getTask () {
		return this._task;
	}

	@Override
	public void show () {
		reset ();
		super.show ();
	}

	private void reset () {
		fromField.setValue (new Date ());
		toField.setValue (new Date ());
		syncDuration ();
		descriptionField.setText ("");
		templateCombo.setSelectedIndex (-1);
		
		fromField.selectAll ();
		fromField.requestFocusInWindow ();
		check ();
	}

	private void check () {
		final Date from = (Date) fromField.getValue ();
		final Date to = (Date) toField.getValue ();
		okButton.setEnabled (_task != null && from != null && to != null && from.before (to));
	}

	public String getPersistenceKey () {
		return "newtaskdialog";
	}

	public void makePersistent (com.davidecavestro.common.gui.persistence.PersistenceStorage props) {
		PersistenceUtils.makeBoundsPersistent (props, this.getPersistenceKey (), this);

	}

	public boolean restorePersistent (com.davidecavestro.common.gui.persistence.PersistenceStorage props) {
		return PersistenceUtils.restorePersistentBounds (props, this.getPersistenceKey (), this);
	}

	public Date getFromDate () {
		return (Date) fromField.getValue ();
	}

	public Date getToDate () {
		return (Date) toField.getValue ();
	}

	public String getDescriptionText () {
		return this.descriptionField.getText ();
	}

	public void addDialogListener (com.davidecavestro.common.gui.dialog.DialogListener l) {
		this._dialogNotifier.addDialogListener (l);
	}

	public void removeDialogListener (com.davidecavestro.common.gui.dialog.DialogListener l) {
		this._dialogNotifier.removeDialogListener (l);
	}

	private void close () {
		setVisible (false);
	}
	private boolean sync = false;

	private void syncDuration () {
		if (sync) {
			return;
		}
		sync = true;
		try {
			if (getFromDate () != null && getToDate () != null) {
				durationField.setValue (new Duration (getFromDate (), getToDate ()));
			}
		} finally {
			sync = false;
		}
	}

	private void syncToDate () {
		if (sync) {
			return;
		}
		sync = true;
		try {
			if (getFromDate () != null && durationField.getValue () != null) {
				toField.setValue (new Date (getFromDate ().getTime () + ((Duration) durationField.getValue ()).getTime ()));
			}
		} finally {
			sync = false;
		}
	}

	private void initDateFormat () {
		CustomizableFormat.LONG_DATE.addPropertyChangeListener (this);
		setDateFormat ();
	}

	public void propertyChange (final PropertyChangeEvent evt) {
		setDateFormat ();
	}

	private void setDateFormat () {
		if (fromField.getFormatter () != null) {
			fromField.getFormatter ().uninstall ();
		}
		if (toField.getFormatter () != null) {
			toField.getFormatter ().uninstall ();
		}

		fromField.setFormatterFactory (new JFormattedTextField.AbstractFormatterFactory () {

			public JFormattedTextField.AbstractFormatter getFormatter (final JFormattedTextField tf) {
				return new DateFormatter (new SimpleDateFormat (CustomizableFormat.LONG_DATE.getValue (_context)));
			}
		});

		toField.setFormatterFactory (new JFormattedTextField.AbstractFormatterFactory () {

			public JFormattedTextField.AbstractFormatter getFormatter (final JFormattedTextField tf) {
				return new DateFormatter (new SimpleDateFormat (CustomizableFormat.LONG_DATE.getValue (_context)));
			}
		});
	}

	private void setTemplate () {
		final ProgressTemplate template = (ProgressTemplate)templateCombo.getSelectedItem ();
		if (template==null) {
			durationField.setValue (new Duration (0));
			descriptionField.setText ("");
		} else {
			durationField.setValue (template.getDuration ());
			descriptionField.setText (template.getNotes ());
		}
	}
	
	private class TemplateModel extends AbstractListModel implements ComboBoxModel, PieceOfWorkTemplateModelListener {
		private Object selectedObject;
		
		public TemplateModel () {
			_context.getTemplateModel ().addPieceOfWorkTemplateModelListener (this);
		}
		
		public int getSize () {
			return _context.getTemplateModel ().getSize () + 1;
		}

		public Object getElementAt (int index) {
			switch (index) {
				case 0:
					return null;
				default:
					return _context.getTemplateModel ().get (index - 1);
			}
		}

		// implements javax.swing.ComboBoxModel
		/**
		 * Set the value of the selected item. The selected item may be null.
		 * <p>
		 * @param anObject The combo box value or null for no selection.
		 */
		public void setSelectedItem(Object anObject) {
			if ((selectedObject != null && !selectedObject.equals( anObject )) ||
				selectedObject == null && anObject != null) {
				selectedObject = anObject;
				fireContentsChanged(this, -1, -1);
			}
		}

		// implements javax.swing.ComboBoxModel
		public Object getSelectedItem() {
			return selectedObject;
		}

		public void intervalAdded (PieceOfWorkTemplateModelEvent e) {
			fireIntervalAdded (this, e.getIndex0 ()+1, e.getIndex1 ()+1);
		}

		public void intervalRemoved (PieceOfWorkTemplateModelEvent e) {
			fireIntervalRemoved (this, e.getIndex0 ()+1, e.getIndex1 ()+1);
		}

		public void contentsChanged (PieceOfWorkTemplateModelEvent e) {
			fireContentsChanged (this, e.getIndex0 ()+1, e.getIndex1 ()+1);
		}
	}
}
