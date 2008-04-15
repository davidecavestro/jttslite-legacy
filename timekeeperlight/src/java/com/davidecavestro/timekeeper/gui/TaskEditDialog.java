/*
 * TaskEditDialog.java
 *
 * Created on March 25, 2008, 8:14 AM
 */

package com.davidecavestro.timekeeper.gui;

import com.davidecavestro.timekeeper.ApplicationContext;
import com.davidecavestro.timekeeper.model.Task;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Dialog di editazione delle propriet&agrave; di un task.
 *
 * @author  Davide Cavestro
 */
public class TaskEditDialog extends javax.swing.JDialog {
	
	private final ApplicationContext _applicationContext;
	private Task _task;
	
	
	/** Costruttore. */
	public TaskEditDialog (final ApplicationContext ac) {
		_applicationContext = ac;
		initComponents ();
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
        nameTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        descriptionTextArea = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        confirmButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("TaskEditDialog/Title"));
        setModal(true);
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("TaskEditDialog/TaskName/Label"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel1, gridBagConstraints);

        nameTextField.setText("jTextField1");
        nameTextField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                nameTextFieldPropertyChange(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        getContentPane().add(nameTextField, gridBagConstraints);

        descriptionTextArea.setColumns(20);
        descriptionTextArea.setRows(5);
        jScrollPane1.setViewportView(descriptionTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("TaskEditDialog/TaskDescription/Label"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel2, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        confirmButton.setAction(new ConfirmAction ());
        confirmButton.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(confirmButton, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("TaskEditDialog/ConfirmButton/Label"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        jPanel1.add(confirmButton, gridBagConstraints);

        cancelButton.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("TaskEditDialog/CancelButton/Label"));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        jPanel1.add(cancelButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void nameTextFieldPropertyChange (java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_nameTextFieldPropertyChange
// TODO add your handling code here:
	}//GEN-LAST:event_nameTextFieldPropertyChange

	private void cancelButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
		cancel ();
	}//GEN-LAST:event_cancelButtonActionPerformed
	


	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton confirmButton;
    private javax.swing.JTextArea descriptionTextArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nameTextField;
    // End of variables declaration//GEN-END:variables
	
	/**
	 * Mostra la dialog di editazione epr il task specificato.
	 * 
	 * @param task il task da modificare.
	 */
	public void show (final Task task) {
		_task = task;
		initData ();
		show ();
	}
	
	private void initData () {
		nameTextField.setText (_task.getName ());
		descriptionTextArea.setText (_task.getDescription ());
	}

	private void cancel () {
		hide ();
	}
	
	private final class ConfirmAction extends AbstractAction {
		
		public ConfirmAction () {
			/*
			 * Varia abilitazione in funzione dei dati
			 */
			nameTextField.getDocument ().addDocumentListener (new DocumentListener () {
				public void changedUpdate (DocumentEvent e) {
					setEnabled ();
				}
				public void insertUpdate (DocumentEvent e) {
					setEnabled ();
				}
				public void removeUpdate (DocumentEvent e) {
					setEnabled ();
				}
			});
		}
		
		public void actionPerformed (final ActionEvent e) {
			_applicationContext.getModel ().updateTask (_task, nameTextField.getText (), descriptionTextArea.getText ());
			hide ();
		}
		
		public void setEnabled () {
			setEnabled (nameTextField.getText ()!=null && nameTextField.getText ().trim ().length ()>0);
		}
	}
}