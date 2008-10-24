/*
 * ReportDialog.java
 *
 * Created on January 10, 2007, 11:15 PM
 */

package com.davidecavestro.timekeeper.gui;

import com.davidecavestro.common.gui.dialog.DialogEvent;
import com.davidecavestro.common.gui.dialog.DialogListener;
import com.davidecavestro.common.gui.dialog.DialogNotifier;
import com.davidecavestro.common.gui.dialog.DialogNotifierImpl;
import com.davidecavestro.common.gui.persistence.PersistenceStorage;
import com.davidecavestro.common.gui.persistence.PersistenceUtils;
import com.davidecavestro.common.gui.persistence.PersistentComponent;
import com.davidecavestro.common.util.CalendarUtils;
import com.davidecavestro.timekeeper.ApplicationContext;
import com.davidecavestro.timekeeper.model.Task;
import com.davidecavestro.timekeeper.report.DataExtractor;
import com.davidecavestro.timekeeper.report.JRBindings;
import com.davidecavestro.timekeeper.report.ReportDataGenerator;
import com.davidecavestro.timekeeper.report.ReportExportAction;
import com.davidecavestro.timekeeper.report.ReportLaunchAction;
import com.davidecavestro.timekeeper.report.ReportPreferences;
import com.davidecavestro.timekeeper.report.ReportViewer;
import com.davidecavestro.timekeeper.report.filter.TargetedFilterContainer;
import com.davidecavestro.timekeeper.report.flavors.CumulateLocalProgresses;
import com.davidecavestro.timekeeper.report.flavors.TaskListExtractor;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.AbstractListModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Dialog di preparazione del report.
 *
 * @author  Davide Cavestro
 */
public class ReportDialog extends javax.swing.JDialog implements PersistentComponent, DialogNotifier, PropertyChangeListener {
	
	private final DialogNotifierImpl _dialogNotifier = new DialogNotifierImpl ();
	
	private final ApplicationContext _context;
	
	private final static TargetedFilterContainer[] targetedFilterArray = new TargetedFilterContainer [0];
	
	/** Costruttore. */
	public ReportDialog (java.awt.Frame parent, boolean modal, final ApplicationContext context) {
		super (parent, modal);
		_context = context;
		initComponents ();
		
		initFormats ();
		presetsRadio.getModel ().setSelected (true);
		/*
		 * elimina colonne non necessarie
		 */
		jXTreeTable1.getColumnModel ().removeColumn (jXTreeTable1.getColumnModel ().getColumn (2));
		jXTreeTable1.getColumnModel ().removeColumn (jXTreeTable1.getColumnModel ().getColumn (1));
		
		jXTreeTable1.getSelectionModel ().setSelectionMode (ListSelectionModel.SINGLE_SELECTION);
		
		getRootPane ().setDefaultButton (okButton);
		
		cancelButton.getInputMap (JComponent.WHEN_IN_FOCUSED_WINDOW).put (KeyStroke.getKeyStroke ("ESCAPE"), "cancel");
		cancelButton.getActionMap ().put ("cancel", new javax.swing.AbstractAction ("cancel"){
			public void actionPerformed (ActionEvent ae){
				cancel ();
			}
		});
		
		pack ();
		setLocationRelativeTo (null);
		
		timeRangeChanged ();
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        filterPanel = new javax.swing.JPanel();
        presetsRadio = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        fromDateEditor = new org.jdesktop.swingx.JXDatePicker();
        toDateEditor = new org.jdesktop.swingx.JXDatePicker();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        presetRangesList = new org.jdesktop.swingx.JXList();
        taskTreePaneljPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jXTreeTable1 = new org.jdesktop.swingx.JXTreeTable();
        reportChoicePanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jComboBox2 = new JComboBox (ReportExportAction.values ());
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        jComboBox1 = new JComboBox (ReportChoice.values ());
        jPanel3 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res"); // NOI18N
        setTitle(bundle.getString("ReportDialog/Title/Reporting_panel")); // NOI18N
        getContentPane().setLayout(new java.awt.GridBagLayout());

        filterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ReportDialog/ChoiceGroupTitle/Time_range"))); // NOI18N
        filterPanel.setLayout(new java.awt.GridBagLayout());

        buttonGroup1.add(presetsRadio);
        presetsRadio.setFont(new java.awt.Font("Dialog", 0, 12));
        presetsRadio.setText(bundle.getString("ReportDialog/ChoiceControl/presets")); // NOI18N
        presetsRadio.setToolTipText("Use presets");
        presetsRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        presetsRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));
        presetsRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                presetsRadioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        filterPanel.add(presetsRadio, gridBagConstraints);

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 12));
        jRadioButton2.setText(bundle.getString("ReportDialog/ChoiceControl/custom")); // NOI18N
        jRadioButton2.setToolTipText("Set a custom time range");
        jRadioButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 4, 4, 4);
        filterPanel.add(jRadioButton2, gridBagConstraints);

        fromDateEditor.setDate(new Date ());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        filterPanel.add(fromDateEditor, gridBagConstraints);

        toDateEditor.setDate(new Date ());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        filterPanel.add(toDateEditor, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setText(bundle.getString("ReportDialog/CoiceControl/from")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(13, 13, 3, 4);
        filterPanel.add(jLabel1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setText(bundle.getString("ReportDialog/CoiceControl/to")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(12, 13, 3, 4);
        filterPanel.add(jLabel2, gridBagConstraints);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(22, 120));

        presetRangesList.setModel(new AbstractListModel () {
            final TimeRangePreset[] elements = TimeRangePreset.values ();

            public Object getElementAt (int index) {
                return elements[index];
            }
            public int getSize () {
                return elements.length;
            }
        });
        presetRangesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        presetRangesList.setToolTipText(bundle.getString("ReportDialog/ControlTooltip/Preset_list_tooltip")); // NOI18N
        presetRangesList.setFont(new java.awt.Font("Dialog", 0, 12));
        presetRangesList.setSelectedIndex(0);
        presetRangesList.setVisibleRowCount(6);
        presetRangesList.setSelectionMode (ListSelectionModel.SINGLE_SELECTION);
        presetRangesList.setVisibleRowCount (presetRangesList.getModel ().getSize ());
        presetRangesList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                presetRangesListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(presetRangesList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        filterPanel.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(filterPanel, gridBagConstraints);

        taskTreePaneljPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ReportDialog/ControlsGroup/ReportRootChoice"))); // NOI18N
        taskTreePaneljPanel3.setLayout(new java.awt.GridBagLayout());

        jXTreeTable1.setEditable(false);
        jXTreeTable1.setRootVisible(true);
        jXTreeTable1.setTreeTableModel(_context.getWindowManager ().getMainWindow ().getTaskTreeTableModel ());
        jScrollPane2.setViewportView(jXTreeTable1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        taskTreePaneljPanel3.add(jScrollPane2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(taskTreePaneljPanel3, gridBagConstraints);

        reportChoicePanel.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ReportsDialog/ActionGroup/ReportLaunchAction"))); // NOI18N
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jComboBox2.setFont(new java.awt.Font("Dialog", 0, 12));
        jComboBox2.setEnabled(false);
        jComboBox2.setMinimumSize(new java.awt.Dimension(60, 24));
        jComboBox2.setPreferredSize(new java.awt.Dimension(60, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 4, 4);
        jPanel1.add(jComboBox2, gridBagConstraints);

        buttonGroup2.add(jRadioButton1);
        jRadioButton1.setFont(new java.awt.Font("Dialog", 0, 12));
        jRadioButton1.setSelected(true);
        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("com/davidecavestro/timekeeper/gui/res"); // NOI18N
        jRadioButton1.setText(bundle1.getString("ReportDialog/LaunchActionGroup/PreviewAction")); // NOI18N
        jRadioButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 2, 3);
        jPanel1.add(jRadioButton1, gridBagConstraints);

        buttonGroup2.add(jRadioButton3);
        jRadioButton3.setFont(new java.awt.Font("Dialog", 0, 12));
        jRadioButton3.setText(bundle1.getString("ReportDialog/LaunchActionGroup/SaveAction")); // NOI18N
        jRadioButton3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButton3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioButton3StateChanged(evt);
            }
        });
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 3, 3);
        jPanel1.add(jRadioButton3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        reportChoicePanel.add(jPanel1, gridBagConstraints);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ReportDialog/ControlsGroup/ReportChoice"))); // NOI18N
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jComboBox1.setFont(new java.awt.Font("Dialog", 0, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        jPanel4.add(jComboBox1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        reportChoicePanel.add(jPanel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        getContentPane().add(reportChoicePanel, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        okButton.setFont(new java.awt.Font("Dialog", 0, 12));
        okButton.setText(bundle.getString("ReportDialog/LaunchButton/Confirm")); // NOI18N
        okButton.setMinimumSize(new java.awt.Dimension(80, 25));
        okButton.setPreferredSize(new java.awt.Dimension(80, 25));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(okButton, gridBagConstraints);

        cancelButton.setFont(new java.awt.Font("Dialog", 0, 12));
        cancelButton.setText(bundle.getString("ReportDialog/LaunchButton/Cancel")); // NOI18N
        cancelButton.setMinimumSize(new java.awt.Dimension(80, 25));
        cancelButton.setPreferredSize(new java.awt.Dimension(80, 25));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 7);
        jPanel3.add(cancelButton, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(jPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel3, gridBagConstraints);

        jPanel5.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jPanel5, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void jRadioButton1ActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
		reportActionChanged ();
	}//GEN-LAST:event_jRadioButton1ActionPerformed

	private void jRadioButton3ActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
		reportActionChanged ();
	}//GEN-LAST:event_jRadioButton3ActionPerformed

	private void jRadioButton3StateChanged (javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioButton3StateChanged
		
	}//GEN-LAST:event_jRadioButton3StateChanged

	private void presetRangesListMouseClicked (java.awt.event.MouseEvent evt) {//GEN-FIRST:event_presetRangesListMouseClicked
		if (evt.getClickCount ()>1) {
			confirm ();
		}
	}//GEN-LAST:event_presetRangesListMouseClicked
	
	private void jRadioButton2ActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
		timeRangeChanged ();
	}//GEN-LAST:event_jRadioButton2ActionPerformed
	
	private void presetsRadioActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_presetsRadioActionPerformed
		timeRangeChanged ();
	}//GEN-LAST:event_presetsRadioActionPerformed
	
	private void cancelButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
		cancel ();
	}//GEN-LAST:event_cancelButtonActionPerformed
	
	private void okButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
		confirm ();;
	}//GEN-LAST:event_okButtonActionPerformed
	
	
	
	public String getPersistenceKey () {
		return "report-dialog";
	}
	
	public void makePersistent (PersistenceStorage props) {
		PersistenceUtils.makeBoundsPersistent (props, getPersistenceKey (), this);
	}
	
	public boolean restorePersistent (PersistenceStorage props) {
		return PersistenceUtils.restorePersistentBounds (props, getPersistenceKey (), this);
	}
	
	public void addDialogListener (DialogListener l) {
		_dialogNotifier.addDialogListener (l);
	}
	
	public void removeDialogListener (DialogListener l) {
		_dialogNotifier.removeDialogListener (l);
	}
	
	/**
	 * Azione di CONFERMA sulla dialog
	 */
	private void confirm (){
		_dialogNotifier.fireDialogPerformed (new DialogEvent (this, new Date[] {/*@todo completare con le date di inizio e fine periodo*/}, JOptionPane.OK_OPTION));
//		hide ();
	}
	
	/**
	 * Azione di ANNULLA sulla dialog
	 */
	private void cancel (){
		hide ();
	}
	
	void showForRoot (Task reportRoot) {
		show ();
	}
	
	
	boolean _usePresets;
	private void timeRangeChanged () {
		_usePresets = presetsRadio.isSelected ();
		presetRangesList.setEnabled (_usePresets);
		fromDateEditor.setEnabled (!_usePresets);
		toDateEditor.setEnabled (!_usePresets);
	}
	
	private void reportRootChanged () {
	}
	
	private final static long MILLISECS_PER_DAY = 1000 * 60 * 60 * 24;
	
	void launchReport () {
		final ReportPreferences prefs = new ReportPreferences (null);
		
		final ReportChoice reportChoice = getReportChoice ();
		
		final JRBindings jrb = new JRBindings (getClass ().getResourceAsStream (reportChoice.getResource ()));
		
		final java.util.List filters = new ArrayList ();
//		if (fromDateEditor.isEnabled ()){
//			filters.add (new TargetedFilterContainer (SimpleProgresses.PROGRESS_FROM, new NegateFilter (new BeforeDateFilter (fromDateEditor.getDate ()))));
//		}
//		if (toDateEditor.isEnabled ()){
//			filters.add (new TargetedFilterContainer (SimpleProgresses.PROGRESS_FROM, new NegateFilter (new AfterDateFilter (toDateEditor.getDate ()))));
//		}
		
		Date startDate = null;
		int periodCount = 7;
		if (_usePresets) {
			final TimeRangePreset preset = (TimeRangePreset)presetRangesList.getSelectedValue ();
			startDate = preset.from ();
			periodCount = preset.length ()-1;
		} else {
			startDate = fromDateEditor.getDate ();
			final Calendar from = new GregorianCalendar ();
			from.setTime (fromDateEditor.getDate ());
			final Calendar to = new GregorianCalendar ();
			to.setTime (toDateEditor.getDate ());
			periodCount = Math.round ((to.getTimeInMillis () - from.getTimeInMillis ()) / MILLISECS_PER_DAY);
		}
//		System.out.println ("extracting data from "+CalendarUtils.toTSString (startDate)+" for "+periodCount+" days");

		
		Task t = _context.getModel ().getRoot ();
		
		final int selectedRow = jXTreeTable1.getSelectedRow ();
		if (selectedRow >= 0) {
			t = (Task) jXTreeTable1.getModel ().getValueAt (selectedRow, 0);
		}
		
		final GregorianCalendar finishDate = new GregorianCalendar ();
		finishDate.setTime (new Date (startDate.getTime ()));
		finishDate.add (Calendar.DAY_OF_YEAR, periodCount);
		
		prefs.addParameter ("REPORT_ROOT_NAME", t.getName ());
		prefs.addParameter ("START_DATE", CalendarUtils.getTimestamp (startDate, CustomizableFormat.SHORT_DATE.getValue (_context)));
		prefs.addParameter ("FINISH_DATE", CalendarUtils.getTimestamp (finishDate.getTime (), CustomizableFormat.SHORT_DATE.getValue (_context)));
		
		final DataExtractor extractor = reportChoice.getExtractor (_context,
			t,
			(TargetedFilterContainer[])filters.toArray (targetedFilterArray),
			startDate,
			1,
			periodCount);
		
		
		if (jRadioButton1.isSelected ()) {
			new ReportDataGenerator (_context).generate (extractor, prefs, jrb, new ReportLaunchAction () {
				public void execute (ApplicationContext context, Component parent, JasperPrint print) throws JRException {
					ReportViewer.viewReport (print);
				}
			});
		} else {
			new ReportDataGenerator (_context).generate (extractor, prefs, jrb, (ReportExportAction)jComboBox2.getModel ().getSelectedItem ());
		}
		
	}
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel filterPanel;
    private org.jdesktop.swingx.JXDatePicker fromDateEditor;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private org.jdesktop.swingx.JXTreeTable jXTreeTable1;
    private javax.swing.JButton okButton;
    private org.jdesktop.swingx.JXList presetRangesList;
    private javax.swing.JRadioButton presetsRadio;
    private javax.swing.JPanel reportChoicePanel;
    private javax.swing.JPanel taskTreePaneljPanel3;
    private org.jdesktop.swingx.JXDatePicker toDateEditor;
    // End of variables declaration//GEN-END:variables
	
	
	/**
	 * Intervalli temporali preimpostati per il report.
	 */
	private enum TimeRangePreset {
		THIS_WEEK {
			public String toString () {
				return java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("ReportDialog/ReportPresetName/This_week");
			}
			public int length () {
				return 7;
			}
			public Date from () {
				final GregorianCalendar c = new GregorianCalendar ();
				c.setTime (CalendarUtils.resetTimeCopy (new Date ()));
				c.set (Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek ());
				
				return c.getTime ();
			}
		},
		LAST_SEVEN_DAYS {
			public String toString () {
				return java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("ReportDialog/ReportPresetName/Last_seven_days");
			}
			public int length () {
				return 7;
			}
			public Date from () {
				final GregorianCalendar c = new GregorianCalendar ();
				c.setTime (CalendarUtils.resetTimeCopy (new Date ()));
				int day = c.get (Calendar.DAY_OF_YEAR);
				c.add (Calendar.DAY_OF_YEAR, -7);
				if (day < 7) {
					/*
					 * siamo nelle prima settimana dell'anno
					 *
					 * adegua il campo di anno, quando necessario, 
					 * dato  che non verrebbe decrementatoautomaticamente in quanto superiore almese
					 */
					c.roll (Calendar.YEAR, -1);
				}
				
				return c.getTime ();
			}
		},
		PREVIOUS_WEEK {
			public String toString () {
				return java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("ReportDialog/ReportPresetName/Last_week");
			}
			public int length () {
				return 7;
			}
			public Date from () {
				final GregorianCalendar c = new GregorianCalendar ();
				c.setTime (CalendarUtils.resetTimeCopy (new Date ()));
				final int week = c.get (Calendar.WEEK_OF_YEAR);
				c.roll (Calendar.WEEK_OF_YEAR, -1);
				c.set (Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek ());
				if (week==1) {
					/*
					 * siamo nelle prima settimana dell'anno
					 *
					 * adegua il campo di anno, quando necessario, 
					 * dato  che non verrebbe decrementatoautomaticamente in quanto superiore almese
					 */
					c.roll (Calendar.YEAR, -1);
				}
				
				return c.getTime ();
			}
		},
		PREVIOUS_TWO_WEEKS {
			public String toString () {
				return java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("ReportDialog/ReportPresetName/Last_2_weeks");
			}
			public int length () {
				return 14;
			}
			public Date from () {
				final GregorianCalendar c = new GregorianCalendar ();
				c.setTime (CalendarUtils.resetTimeCopy (new Date ()));
				final int week = c.get (Calendar.WEEK_OF_YEAR);
				
				c.roll (Calendar.WEEK_OF_YEAR, -2);
				if (week<=2) {
					/*
					 * siamo nelle prime  settimane dell'anno
					 *
					 * adegua il campo di anno, quando necessario, 
					 * dato  che non verrebbe decrementatoautomaticamente in quanto superiore almese
					 */
					c.roll (Calendar.YEAR, -1);
				}
				c.set (Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek ());
				return c.getTime ();
			}
		},
		THIS_MONTH {
			public String toString () {
				return java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("ReportDialog/ReportPresetName/This_month");
			}
			public int length () {
				return getDaysInMonth (new GregorianCalendar ());
			}
			public Date from () {
				final GregorianCalendar c = new GregorianCalendar ();
				c.setTime (CalendarUtils.resetTimeCopy (new Date ()));
				c.set (Calendar.DAY_OF_MONTH, 1);
				
				return c.getTime ();
			}
		},
		PREVIOUS_MONTH {
			public String toString () {
				return java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("ReportDialog/ReportPresetName/Previous_month");
			}
			public int length () {
				final GregorianCalendar c = new GregorianCalendar ();
				c.add (Calendar.MONTH, -1);
				
				return getDaysInMonth (c);
			}
			public Date from () {
				final GregorianCalendar c = new GregorianCalendar ();
				c.setTime (CalendarUtils.resetTimeCopy (new Date ()));
				c.set (Calendar.DAY_OF_MONTH, 1);
				final int month = c.get (Calendar.MONTH);
				c.roll (Calendar.MONTH, -1);
				if (month==Calendar.JANUARY) {
					/*
					 * siamo in gennaio
					 *
					 * adegua il campo di anno, quando necessario, 
					 * dato  che non verrebbe decrementato automaticamente in quanto superiore al mese
					 */
					c.roll (Calendar.YEAR, -1);
				}
				
				return c.getTime ();
			}
		};
		abstract int length ();
		abstract Date from ();
		public abstract String toString ();
	};
	
	

	private static int getDaysInMonth (final GregorianCalendar c) {
		int d = c.get (Calendar.DAY_OF_MONTH);
		int m = c.get (Calendar.MONTH);
		for (;;) {
			c.add (Calendar.DAY_OF_MONTH, 1);
			if (c.get (Calendar.MONTH)==m) {
				d++;
			} else {
				break;
			}
		}
		return d;
	}

	private ReportChoice getReportChoice () {
		return (ReportChoice)jComboBox1.getSelectedItem ();
	}

	private void reportActionChanged () {
		jComboBox2.setEnabled (jRadioButton3.isSelected ());
	}
	

		
		
	private enum ReportChoice {
		DAILY_ACTIONS_PER_TASK {
			public String getResource () {
				return "/dailyprogressesdetail.jasper";
			}
			public String toString () {
				return java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("ReportDialog/ReportChoice/DailyProgressesDetail");
			}
			
			public DataExtractor getExtractor (ApplicationContext context, Task t, TargetedFilterContainer[] targetedFilterContainer, Date startDate, int i, int periodCount) {
				return getCumulateLocalProgressesExtractor (context, t, targetedFilterContainer, startDate, i, periodCount);
			}
			
		},
		DAILY_ACTIONS_LIST {
			public String getResource () {
				return "/dailyactionslist.jasper";
			}
			public String toString () {
				return java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("ReportDialog/ReportChoice/DailyActionsList");
			}
			
			public DataExtractor getExtractor (ApplicationContext context, Task t, TargetedFilterContainer[] targetedFilterContainer, Date startDate, int i, int periodCount) {
				return getCumulateLocalProgressesExtractor (context, t, targetedFilterContainer, startDate, i, periodCount);
			}
			
		},
		TASKS_LIST {
			public String getResource () {
				return "/tasklist.jasper";
			}
			public String toString () {
				return java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("ReportDialog/ReportChoice/TaskList");
			}
			
			public DataExtractor getExtractor (ApplicationContext context, Task t, TargetedFilterContainer[] targetedFilterContainer, Date startDate, int i, int periodCount) {
				return new TaskListExtractor (
				context,
				t,
				targetedFilterContainer,
				startDate,
				i,
				periodCount);
			}
			
		};
		
		
		public abstract String getResource ();
		public abstract String toString ();

		public abstract DataExtractor getExtractor (ApplicationContext _context, Task t, TargetedFilterContainer[] targetedFilterContainer, Date startDate, int i, int periodCount);
		
		protected DataExtractor getCumulateLocalProgressesExtractor (ApplicationContext context, Task t, TargetedFilterContainer[] targetedFilterContainer, Date startDate, int i, int periodCount) {
			return new CumulateLocalProgresses (
			context,
			t,
			targetedFilterContainer,
			startDate,
			i,
			periodCount);
		}
		
	}

	private void initFormats () {
		CustomizableFormat.SHORT_DATE.addPropertyChangeListener (this);
		setFormats ();
	}
	
	private void setFormats () {
		final DateFormat[] formats = new DateFormat[] {new SimpleDateFormat (CustomizableFormat.SHORT_DATE.getValue (_context))};
		fromDateEditor.setFormats (formats);
		toDateEditor.setFormats (formats);
	}

	public void propertyChange (PropertyChangeEvent evt) {
		if (evt.getSource ()==CustomizableFormat.SHORT_DATE) {
			//codice commentatoa causa di problema con il popup di selezione giorno
			//setFormats ();
		}
	}

}
