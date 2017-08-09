/*
 * RingChartzPanel.java
 *
 * Created on 4 marzo 2005, 8.26
 */

package net.sf.jttslite.gui;

import JSci.awt.GraphLayout;
import net.sf.jttslite.common.charts.awt.DefaultSerieNode;
import net.sf.jttslite.common.charts.awt.DefaultTreeGraph2DModel;
import net.sf.jttslite.common.charts.awt.SerieNode;
import net.sf.jttslite.common.charts.awt.TreeGraph2DModel;
import net.sf.jttslite.gui.charts.JRingChart;
import net.sf.jttslite.core.model.impl.Progress;
import net.sf.jttslite.core.model.impl.ProgressItem;
import net.sf.jttslite.core.util.ResourceClass;
import net.sf.jttslite.core.util.ResourceSupplier;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Pannello contenente i controli ed il grafico ad anello.
 *
 * @author  davide
 */
public class RingChartPanel extends JPanel{
	
	private TreeGraph2DModel _treeModel;
	private ProgressItem _root;
	private JRingChart _pieChart;
	
	private String _chartTitle;
	private JLabel _chartTitleLabel;
	
	/**
	 * Costruttore.
	 * @param chartTitle il titolo del grafico.
	 */
	public RingChartPanel (final String chartTitle) {
		super ();
		this._chartTitle = chartTitle;
		initComponents ();
	}
	
	private void initComponents (){
		this.setLayout (new BorderLayout ());
		
		final JPanel chartContainer = new JPanel (new GraphLayout ());
		
		final JLabel xAxisLabel = new JLabel ();
		
		final Font titleFont = new Font ("Default",Font.BOLD,14);
		this._treeModel = createTreeData (null);
		this._pieChart = new JRingChart (this._treeModel, 5, xAxisLabel);
		this._chartTitleLabel = new JLabel ( this._chartTitle, JLabel.CENTER);
		this._chartTitleLabel.setFont (titleFont);
		
		chartContainer.add (this._chartTitleLabel,"Title");
		chartContainer.add (this._pieChart,"Graph");
		chartContainer.add (xAxisLabel,"X-axis");
		this._pieChart.setBackground (java.awt.Color.white);
		
		add (new JScrollPane (chartContainer), BorderLayout.CENTER);
//		final JPanel controlsContainer = new JPanel (new GridBagLayout ());
//		
//		final GridBagConstraints c = new GridBagConstraints ();
//		c.fill = GridBagConstraints.BOTH;
//		c.anchor = GridBagConstraints.FIRST_LINE_START;
//		c.insets = new Insets (3, 10, 3, 10);
//
////		c.weightx = 1;
//		
//		c.gridx = 0;
//		c.gridy = 0;
//		controlsContainer.add (new JLabel ("foo"), c);
//		c.gridx = 0;
//		c.gridy = 1;
//		controlsContainer.add (createControlPanel (), c);
//		c.gridx = 0;
//		c.gridy = 2;
//		controlsContainer.add (createButtonPanel (), c);
//		
//		/* etichetta vuota per riemire spazio */
//		c.weightx = 1.0;
//		c.weighty = 1.0;
//		c.gridx = 0;
//		c.gridy = 3;
//		c.gridwidth = 1;
//		controlsContainer.add (new JLabel (), c);
//		
//		add (controlsContainer, BorderLayout.SOUTH);
	}
	
	private static DefaultTreeGraph2DModel createTreeData (final ProgressItem root) {
		DefaultTreeGraph2DModel model=new DefaultTreeGraph2DModel (createSerieNode (null, root));
		return model;
	}
	
	private static SerieNode createSerieNode (final SerieNode parent, final ProgressItem progressItem) {
		if (null==progressItem){
			return new DefaultSerieNode ((SerieNode)null, null + " - " + null, 0d, new SerieNode [0], progressItem);
		}
		
		
		final DefaultSerieNode sn = new DefaultSerieNode (parent, (progressItem.getCode ()!=null?progressItem.getCode () + " - " :"")+ progressItem.getName (), 0, null, progressItem);		
		
		/*
		 * calcola durata complessiva sottoalbero.
		 */
		double totalDuration = 0;
		for (final Iterator it = progressItem.getPiecesOfWork ().iterator ();it.hasNext ();){
			final Progress period = (Progress)it.next ();
			totalDuration += period.getDuration ().getTime ();
		}
		
		/*
		 * Popola array serie nodi figli
		 */
		SerieNode[] childSeries = new SerieNode[progressItem.getChildren ().size ()];
		int i=0;
		for (final Iterator it = progressItem.getChildren ().iterator ();it.hasNext ();){
			childSeries[i] = createSerieNode (sn, (ProgressItem)it.next ());
			i++;
		}
		
		sn.init (totalDuration, childSeries);
		return sn;
	}

	/**
	 * Reimposta la radice del grafico, e lo aggiorna.
	 */
	public final void reloadChart (final ProgressItem root) {
		this._root = root;
		this._treeModel.setRoot (createSerieNode (null, root));
	}
	
	private JPanel createButtonPanel (){
		final JPanel thePanel = new JPanel ();
		
		final JButton rootSelectionButton = new JButton (ResourceSupplier.getString (ResourceClass.UI, "controls", "choose.root"));
		rootSelectionButton.addActionListener (new java.awt.event.ActionListener () {
			public void actionPerformed (java.awt.event.ActionEvent evt) {/*
				final ProgressItemTreeSelector.UserChoice choice = ProgressItemTreeSelector.supplyProgressItem (
				ResourceSupplier.getString (ResourceClass.UI, "controls", "progressitem.choice"),
				ResourceSupplier.getString (ResourceClass.UI, "controls", "choose.ringchart.root"), true, HelpResource.RINGCHART_ROOTSELECTION_DIALOG);
				if (choice.isConfirmed ()){
					RingChartPanel.this.reloadChart (choice.getChoice ());
				}*/
			}
		});
		thePanel.add (rootSelectionButton);
		
		final JButton updateButton = new JButton (ResourceSupplier.getString (ResourceClass.UI, "controls", "update"));
		updateButton.addActionListener (new java.awt.event.ActionListener () {
			public void actionPerformed (java.awt.event.ActionEvent evt) {
				RingChartPanel.this._treeModel.setRoot (createSerieNode (null, RingChartPanel.this._root));
			}
		});
		thePanel.add (updateButton);
		
		return thePanel;
	}
	
	private JPanel createControlPanel (){
		final JPanel thePanel = new JPanel (new GridBagLayout ());
		/**
		 * Componente editazione numero livelli visibili.
		 */
		final javax.swing.JSpinner visibleLevelsEditor = new javax.swing.JSpinner ();
		
		final JLabel levelEditorLabel = new JLabel (ResourceSupplier.getString (ResourceClass.UI, "controls", "visible.level.number"));
		levelEditorLabel.setLabelFor (visibleLevelsEditor);
		
		final JComponent editor = visibleLevelsEditor.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
			/* impoxsta il numero di colonne dell'editor */
            ((JSpinner.DefaultEditor)editor).getTextField().setColumns (2);
		}
		visibleLevelsEditor.setModel (
			new SpinnerNumberModel(this._pieChart.getDepth (), //initial value
				2, //min
				100, //max
				1)); //step
		
		visibleLevelsEditor.addChangeListener (new ChangeListener (){
			public void stateChanged (ChangeEvent e){
				final Integer value = (Integer)visibleLevelsEditor.getValue ();
				//UserSettings.getInstance ().setRingChartVisibleLevels (value);
				RingChartPanel.this._pieChart.setDepth (value.intValue ());
			}});
			
			final GridBagConstraints c = new GridBagConstraints ();
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			c.insets = new Insets (3, 10, 3, 10);
			
			c.gridx = 0;
			c.gridy = 0;
			thePanel.add (levelEditorLabel, c);
			c.gridx = 1;
			c.gridy = 0;
			thePanel.add (visibleLevelsEditor, c);
			
			
			return thePanel;
	}
	
	public void setDepth (final int d) {
		_pieChart.setDepth (d);
	}
	
	public void addRingChartRootChangeListener (final PropertyChangeListener l) {
		_pieChart.addRootChangeListener (l);
	}
	
	public void removeRingChartRootChangeListener (final PropertyChangeListener l) {
		_pieChart.removeRootChangeListener (l);
	}
}
