/*
 * TablePersistenceExt.java
 *
 * Created on November 3, 2007, 7:06 PM
 *
 */
package net.sf.jttslite.gui;

import net.sf.jttslite.common.util.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.sf.jttslite.ApplicationContext;
import net.sf.jttslite.prefs.PersistentComponent;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.SortOrder;
import org.jdesktop.swingx.table.TableColumnExt;

/**
 * Supporto alla persistenza dello stato di visualizzazione e ordinamento colonne per una tabella.
 *
 * @author Davide Cavestro
 */
public abstract class TablePersistenceExt implements PersistentComponent {

   private final JXTable _table;
   private final ApplicationContext _context;

   /**
    * Costruttore.
    *
	* @param table la tabella da estendere
	* @param context contesto applicativo
    */
   public TablePersistenceExt(final JXTable table, final ApplicationContext context) {
	  _table = table;
	  _context = context;
   }

   public void makePersistent() {
	  final List<String> s1 = new ArrayList<String> ();
	  final List<String> s2 = new ArrayList<String> ();

	  for (int i = 0; i < _table.getColumnCount (false); i++) {
		 final TableColumnExt tce = _table.getColumnExt (i);
		 final int columnIndex = _table.convertColumnIndexToModel (i);

		 if (tce.isVisible ()) {
			s1.add (Integer.toString (columnIndex));
		 }
		 final SortOrder so = _table.getSortOrder (i);
		 if (so != null && so != SortOrder.UNSORTED) {
			s2.add (Integer.toString (toColumnSortIndex (columnIndex, so)));
		 }
	  }
	  _context.getPreferenceManager ().getGuiPreferences ().getPreferences ().put (getVisibleColumnsPersistenceKey (), StringUtils.toCSV (s1.toArray ()));
	  _context.getPreferenceManager ().getGuiPreferences ().getPreferences ().put (getSortedColumnsPersistenceKey (), StringUtils.toCSV (s2.toArray ()));
	  _context.getPreferenceManager ().getGuiPreferences ().makeColumnWidthsPersistent (getWidthColumnsPersistenceKey (), _table);
   }

   public boolean restorePersistent() {
	  final String s1 = _context.getPreferenceManager ().getGuiPreferences ().getPreferences ().get (getVisibleColumnsPersistenceKey (), null);
	  final String s2 = _context.getPreferenceManager ().getGuiPreferences ().getPreferences ().get (getSortedColumnsPersistenceKey (), null);

	  final Set<String> values1 = new HashSet<String> ();
	  if (s1 != null) {
		 values1.addAll (Arrays.asList (s1.split (",")));
	  }

	  final Set<String> values2 = new HashSet<String> ();
	  if (s2 != null) {
		 values2.addAll (Arrays.asList (s2.split (",")));
	  }

	  /*
	   * Se non e' impostata alcuna colonna come visibile, le lascia tutte visualizzate
	   * altrimenti le abilita ad una ad una
	   */
	  if (!values1.isEmpty ()) {
		 for (int i = _table.getColumnCount (false) - 1; i >= 0; i--) {
			final TableColumnExt tce = _table.getColumnExt (i);
			tce.setVisible (values1.contains (Integer.toString (_table.convertColumnIndexToModel (i))));
		 }
	  }

	  for (final String s : values2) {
		 if (s != null && s.length () > 0) {
			setColumnSort (Integer.parseInt (s));
		 }
	  }

	  _context.getPreferenceManager ().getGuiPreferences ().restorePersistentColumnWidths (getWidthColumnsPersistenceKey (), _table);

	  return true;
   }

   private String getVisibleColumnsPersistenceKey() {
	  return getPersistenceKey () + ".visible.columns";
   }

   private String getSortedColumnsPersistenceKey() {
	  return getPersistenceKey () + ".sorted.columns";
   }

   private String getWidthColumnsPersistenceKey() {
	  return getPersistenceKey () + ".width.columns";
   }

   /*
    * Ritorna l'indice codificato relativo che specifica la posizione e l'ordine applicato sulla colonna.
    * Chiamare questo metodo solo per indici di modello relativi a colonne con ordinamento impostato
    */
   private int toColumnSortIndex(final int columnModelIndex, final SortOrder sortOrder) {
	  return (columnModelIndex + 1) * (sortOrder.isAscending () ? 1 : -1);
   }

   private void setColumnSort(final int columnSortIndex) {
	  //_table.getColumnExt (Math.abs (columnSortIndex) - 1).setSortable (true);
	  _table.setSortOrder (Math.abs (columnSortIndex) - 1, columnSortIndex > 0 ? SortOrder.ASCENDING : SortOrder.DESCENDING);
   }
}
