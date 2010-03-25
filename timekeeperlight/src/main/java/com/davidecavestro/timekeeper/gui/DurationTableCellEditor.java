/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.davidecavestro.timekeeper.gui;

import com.ost.timekeeper.util.DurationUtils;
import com.ost.timekeeper.util.Duration;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

/**
 * Editor per celle di tabella contenenti durate.
 *
 * @author davide
 */
public class DurationTableCellEditor extends DefaultCellEditor implements TableCellEditor {
		public DurationTableCellEditor () {
			super (new DurationTextField ());
			getComponent ().setHorizontalAlignment (JTextField.TRAILING);
		}
		
		@Override
		public JFormattedTextField getComponent () {
			return (JFormattedTextField)super.getComponent ();
		}

		/**
		 * Editazione parte, in caso di evento del mouse, solo dopo doppio click
		 */
		@Override
			public boolean isCellEditable (EventObject eo) {
			if (eo instanceof MouseEvent) {
				return ((MouseEvent)eo).getClickCount ()>1;
			} else {
				return super.isCellEditable (eo);
			}
		}
		
	@Override
		public Object getCellEditorValue () {
			/*
			 * ritorna una durata
			 */
			return (Duration)getComponent ().getValue ();
		}
		
	@Override
		public Component getTableCellEditorComponent (final JTable table, final Object value, final boolean isSelected, final int row, final int column) {
			/*
			 * riceve un avanzamento
			 */
			getComponent ().setValue ((Duration)value);
			getComponent ().setText (DurationUtils.format ((Duration)value));
			getComponent ().setCaretPosition (0);
			return getComponent ();
		}
	
}
