/*
 * OpenWorkSpaceDialog.java
 *
 */

package net.sf.jttslite.gui;

import net.sf.jttslite.common.gui.GUIUtils;
import net.sf.jttslite.common.gui.dialog.DialogNotifier;
import net.sf.jttslite.common.gui.dialog.DialogNotifierImpl;
import net.sf.jttslite.common.gui.persistence.PersistenceUtils;
import net.sf.jttslite.common.gui.persistence.PersistentComponent;
import net.sf.jttslite.ApplicationContext;
import net.sf.jttslite.core.model.WorkSpace;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.ListModel;

/**
 * Dialog di selezione del progetto da aprire.
 *
 * @author  davide
 */
public class OpenWorkSpaceDialog extends javax.swing.JDialog implements PersistentComponent, DialogNotifier {
	
	
	private final DialogNotifierImpl _dialogNotifier;
	private final ApplicationContext _context;
	
	/**
	 * 
	 * @param parent 
	 * @param modal 
	 * @param workspace 
	 */
	public OpenWorkSpaceDialog (final ApplicationContext context, java.awt.Frame parent, boolean modal){
		super (parent, modal);
		_context = context;
		initComponents ();
		
		this._dialogNotifier = new DialogNotifierImpl ();
		
		
		this.getRootPane ().setDefaultButton (okButton);
		
		cancelButton.getInputMap (JComponent.WHEN_IN_FOCUSED_WINDOW).put (KeyStroke.getKeyStroke ("ESCAPE"), "cancel");
		cancelButton.getActionMap().put("cancel", new javax.swing.AbstractAction ("cancel"){
			public void actionPerformed (ActionEvent ae){
				cancel ();
			}
		});
	
//		pack ();
		setPreferredSize (new Dimension (200, 100));
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
        workspaceMenuItem = new javax.swing.JMenuItem();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        projectsList = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        workspaceMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.ALT_MASK));
        workspaceMenuItem.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        workspaceMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/folder.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res"); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(workspaceMenuItem, bundle.getString("OpenWorkSpaceDialog/PopupMenu/WorkspacesEditor/Text")); // NOI18N
        workspaceMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                workspaceMenuItemActionPerformed(evt);
            }
        });
        jPopupMenu1.add(workspaceMenuItem);

        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("com/davidecavestro/timekeeper/gui/res"); // NOI18N
        setTitle(bundle1.getString("OpenWorkspaceDialog/Title")); // NOI18N
        setModal(true);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, bundle1.getString("OpenWorkspaceDialog/ChooseWorkspaceLabel")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 4, 5);
        getContentPane().add(jLabel6, gridBagConstraints);

        projectsList.setFont(new java.awt.Font("Dialog", 0, 12));
        projectsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        projectsList.setCellRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                final JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                final WorkSpace ws = (WorkSpace)value;
                label.setText(ws.getName ());

                final StringBuilder sb = new StringBuilder ("");
                final String descr = ws.getDescription ();
                if (descr!=null) {
                    sb.append ("descr");
                }
                final String notes = ws.getNotes ();
                if(notes!=null) {
                    if (sb.length ()>0) {
                        sb.append ("\n");
                    }
                    sb.append (notes);
                }
                label.setToolTipText (sb.toString ());

                return label;
            }
        });
        projectsList.setComponentPopupMenu(jPopupMenu1);
        projectsList.setVisibleRowCount(4);
        projectsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                projectsListMouseClicked(evt);
            }
        });
        projectsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                projectsListValueChanged(evt);
            }
        });
        projectsList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                projectsListKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(projectsList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 4, 5);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        okButton.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(okButton, bundle1.getString("OpenWorkspaceDialog/OkButton/Text")); // NOI18N
        okButton.setPreferredSize(new java.awt.Dimension(80, 25));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
        jPanel1.add(okButton, gridBagConstraints);

        cancelButton.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, bundle1.getString("OpenWorkspaceDialog/CancelButton/Text")); // NOI18N
        cancelButton.setPreferredSize(new java.awt.Dimension(80, 25));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 7);
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
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/folder.png"))); // NOI18N
        jButton1.setToolTipText(bundle.getString("OpenWorkspaceDialog/ButtonTooltip/ManageWorkspaces")); // NOI18N
        jButton1.setContentAreaFilled(false);
        jButton1.setDefaultCapable(false);
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
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 6);
        getContentPane().add(jButton1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void projectsListKeyTyped (java.awt.event.KeyEvent evt) {//GEN-FIRST:event_projectsListKeyTyped
		if (evt.getKeyCode ()==KeyEvent.VK_ENTER){
			confirm ();
		}
	}//GEN-LAST:event_projectsListKeyTyped

	private void projectsListMouseClicked (java.awt.event.MouseEvent evt) {//GEN-FIRST:event_projectsListMouseClicked
		if (evt.getClickCount ()>1){
			confirm ();
		}
	}//GEN-LAST:event_projectsListMouseClicked

	private void cancelButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
		cancel ();
	}//GEN-LAST:event_cancelButtonActionPerformed

	private void okButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
		confirm ();
	}//GEN-LAST:event_okButtonActionPerformed

	private void projectsListValueChanged (javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_projectsListValueChanged
		final WorkSpace wp = (WorkSpace)projectsList.getSelectedValue();
        projectsList.ensureIndexIsVisible(projectsList.getSelectedIndex());
		check ();
	}//GEN-LAST:event_projectsListValueChanged

private void workspaceMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_workspaceMenuItemActionPerformed
	showWorkspaceEditor ();
}//GEN-LAST:event_workspaceMenuItemActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
	showWorkspaceEditor ();
}//GEN-LAST:event_jButton1ActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton okButton;
    private javax.swing.JList projectsList;
    private javax.swing.JMenuItem workspaceMenuItem;
    // End of variables declaration//GEN-END:variables

	/**
	 * Mostra la dialog con un progetto selezionato.
	 */
	public void showWithSelection (final WorkSpace ws) {
		reset ();
		projectsList.setModel (prepareProjects ());
		projectsList.setSelectedValue (ws, true);
		super.show ();
	}
	
	@Override
	public void show () {
		reset ();
		projectsList.setModel (prepareProjects ());
		super.show ();
	}
	
	/**
	 * Reimposta lo stato iniziale.
	 */
	private void reset (){
		check ();
	}
	
	/**
	 * Controlli di abilitazione dei pulsanti
	 */
	private void check (){
		okButton.setEnabled (projectsList.getSelectedValue ()!=null);
	}
	
	public String getPersistenceKey () {
		return "open-project-dialog";
	}
	
	public void makePersistent (net.sf.jttslite.common.gui.persistence.PersistenceStorage props) {
		PersistenceUtils.makeBoundsPersistent (props, this.getPersistenceKey (), this);
	}
	
	public boolean restorePersistent (net.sf.jttslite.common.gui.persistence.PersistenceStorage props) {
		return PersistenceUtils.restorePersistentBounds (props, this.getPersistenceKey (), this);
	}
	

	/**
	 * COnsente l'ordinamento per nome dei progetti.
	 */
	class WorkSpaceComparator implements Comparator {
		
		public int compare (Object o1, Object o2) {
			return ((WorkSpace)o1).getName ().compareTo (((WorkSpace)o2).getName ());
		}
		
	}
	
	
	public void addDialogListener (net.sf.jttslite.common.gui.dialog.DialogListener l) {
		this._dialogNotifier.addDialogListener (l);
	}	
	
	public void removeDialogListener (net.sf.jttslite.common.gui.dialog.DialogListener l) {
		this._dialogNotifier.removeDialogListener (l);
	}
	
	public WorkSpace getSelectedWorkSpace (){
		return (WorkSpace)projectsList.getSelectedValue ();
	}
	
	
	/**
	 * Azione di CONFERMA sulla dialog
	 */
	private void confirm (){
		if (openWorkspace (_context, (WorkSpace)projectsList.getSelectedValue ())) {
			setVisible (false);
		}
	}
	
	/**
	 * Azione di ANNULLA sulla dialog
	 */
	private void cancel (){
		hide ();
	}
	
	/**
	 * Fornisce il modello aggiornato per la lista contenente i progetti
	 */
	private ListModel prepareProjects () {
		final List<WorkSpace> data = Arrays.asList (_context.getWorkSpaceModel ().toArray ());

		Collections.sort (data, new WorkSpaceComparator ());

		return new AbstractListModel () {
			public Object getElementAt (int index) {
				return data.get (index);
			}
			public int getSize () {
				return data.size ();
			}
		};
		
	}
	
	public static boolean openWorkspace (final ApplicationContext context, final WorkSpace ws) {
		if (!context.getModel ().getAdvancing ().isEmpty ()) {
			/*
			 * ci sono avanzamenti in corso nel progetto corrente
			 */
			if (JOptionPane.showConfirmDialog (context.getWindowManager ().getMainWindow (), 
				java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("OpenWorkSpaceDialog/ConfirmDialog/StopCurrentProgress/Text"), 
				java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("OpenWorkSpaceDialog/ConfirmDialog/StopCurrentProgress/Title"), 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
				
				return false;
			}
		}
		context.getLogger ().debug (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Opening_workspace..."));

		context.getModel ().setWorkSpace (ws);
		context.getUserSettings ().setLastProjectName (ws.getName ());

		context.getLogger ().debug (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Workspace_successfully_opened"));
		return true;
	}
	
	
	/**
	 * Visualizza la finestra di gestione progetti.
	 */
	private void showWorkspaceEditor () {
		setVisible (false);
		_context.getWindowManager ().getWorkspacesDialog ().setVisible (true);
	}

	@Override
	public void setVisible (boolean b) {
		if (b) {
			/*
			 * ripristina il focus sulla lista
			 */
			projectsList.requestFocusInWindow ();
		}
		super.setVisible (b);
	}

	
}