/*
 * NewTaskDialog.java
 *
 * Created on 10 dicembre 2005, 10.38
 */

package com.davidecavestro.timekeeper.gui;

import com.davidecavestro.common.gui.dialog.DialogEvent;
import com.davidecavestro.common.gui.dialog.DialogNotifier;
import com.davidecavestro.common.gui.dialog.DialogNotifierImpl;
import com.davidecavestro.common.gui.persistence.PersistenceUtils;
import com.davidecavestro.common.gui.persistence.PersistentComponent;
import com.davidecavestro.timekeeper.ApplicationContext;
import com.davidecavestro.timekeeper.model.Task;
import com.ost.timekeeper.util.Duration;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.text.DateFormatter;

/**
 *
 * @author  davide
 */
public class NewPieceOfWorkDialog extends javax.swing.JDialog implements PersistentComponent, DialogNotifier {
	
	private final DialogNotifierImpl _dialogNotifier;
	
	private final ApplicationContext _context;
	/**
	 * Creates new form NewTaskDialog
	 */
	public NewPieceOfWorkDialog (final ApplicationContext context, java.awt.Frame parent, boolean modal){
		super (parent, modal);
		_context = context;
		initComponents ();
		this._dialogNotifier = new DialogNotifierImpl ();
		this.getRootPane ().setDefaultButton (okButton);
		
		cancelButton.getInputMap (JComponent.WHEN_IN_FOCUSED_WINDOW).put (KeyStroke.getKeyStroke ("ESCAPE"), "cancel");
		cancelButton.getActionMap().put("cancel", new javax.swing.AbstractAction ("cancel"){
			public void actionPerformed (ActionEvent ae){
				close();
			}
		});
		
//		pack ();
		setLocationRelativeTo (null);
		
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        descriptionPane = new javax.swing.JScrollPane();
        descriptionField = new javax.swing.JTextArea();
        fromField = new JFormattedTextField (new DateFormatter (new SimpleDateFormat (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("from_to_format_long"))));
        toField = new JFormattedTextField (new DateFormatter (new SimpleDateFormat (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("from_to_format_long"))));
        durationField = new DurationTextField ();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        helpButton = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("New_progress"));
        setModal(true);
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, "&From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(jLabel1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, "&To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(jLabel2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, "&Notes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(jLabel3, gridBagConstraints);

        descriptionField.setColumns(20);
        descriptionField.setRows(5);
        descriptionPane.setViewportView(descriptionField);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
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
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
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
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(durationField, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, "&Duration");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
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
        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Cancel"));
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
        org.openide.awt.Mnemonics.setLocalizedText(okButton, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Ok"));
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

        _context.getHelpManager ().initialize (helpButton);

        helpButton.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(helpButton, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("&Help"));
        helpButton.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Help"));
        helpButton.setActionCommand("help");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 3, 6);
        jPanel1.add(helpButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jPanel1, gridBagConstraints);

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
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextArea descriptionField;
    private javax.swing.JScrollPane descriptionPane;
    private javax.swing.JFormattedTextField durationField;
    private javax.swing.JFormattedTextField fromField;
    private javax.swing.JButton helpButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton okButton;
    private javax.swing.JFormattedTextField toField;
    // End of variables declaration//GEN-END:variables
	
	private void confirm (){
		this._dialogNotifier.fireDialogPerformed (new DialogEvent (this, JOptionPane.OK_OPTION));
		this.hide ();
	}
	
	private Task _task;
	public void showForTask (Task t) {
		this._task = t;
        setTitle(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("New_progress")+ " - ["+t.getName ()+"]");
		show ();
	}
	
	public Task getTask (){
		return this._task;
	}
	
	public void show () {
		reset ();
		super.show ();
	}
	
	private void reset (){
		this.fromField.setValue (new Date ());
		this.toField.setValue (new Date ());
		syncDuration ();
		this.descriptionField.setText ("");
		check ();
	}
	
	
	private void check (){
		final Date from = (Date)fromField.getValue ();
		final Date to = (Date)toField.getValue ();
		okButton.setEnabled (_task!=null && from!=null && to!=null && from.before (to));
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
	
	public Date getFromDate (){
		return (Date)fromField.getValue ();
	}
	
	public Date getToDate (){
		return (Date)toField.getValue ();
	}
	
	public String getDescriptionText (){
		return this.descriptionField.getText ();
	}
	
	public void addDialogListener (com.davidecavestro.common.gui.dialog.DialogListener l) {
		this._dialogNotifier.addDialogListener (l);
	}	
	
	public void removeDialogListener (com.davidecavestro.common.gui.dialog.DialogListener l) {
		this._dialogNotifier.removeDialogListener (l);
	}
	
	private void close (){
		hide ();
	}
	
	private void syncDuration (){
		if (getFromDate ()!=null && getToDate ()!=null) {
			durationField.setValue (new Duration (getFromDate (), getToDate ()));
		}
	}
	
	private void syncToDate (){
		if (getFromDate ()!=null && durationField.getValue ()!=null) {
			toField.setValue (new Date (getFromDate ().getTime ()+ ((Duration)durationField.getValue ()).getTime ()));
		}
	}
}
