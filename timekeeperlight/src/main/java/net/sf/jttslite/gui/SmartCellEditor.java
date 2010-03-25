package net.sf.jttslite.gui;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.EventObject;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

/**
 * Cell editor con gestione della cancellazione (lascia partire l'editazione alla pressione di qualsiasi tasto, tranne quelli di cancellazione
 * IN questo modo la tabella può essere facilmenbte editata, e la cancellazione  di una riga rimane veloce
 */
class SmartCellEditor implements TableCellEditor {

	private final TableCellEditor delegate;

	public SmartCellEditor (TableCellEditor delegate) {
		super ();
		this.delegate = delegate;
	}

	public Component getTableCellEditorComponent (JTable table, Object value, boolean isSelected, int row, int column) {
		return delegate.getTableCellEditorComponent (table, value, isSelected, row, column);
	}

	public Object getCellEditorValue () {
		return delegate.getCellEditorValue ();
	}

	public boolean isCellEditable (EventObject anEvent) {
		if (anEvent instanceof KeyEvent) {
			if (((KeyEvent) anEvent).getKeyCode () == KeyEvent.VK_DELETE || ((KeyEvent) anEvent).getKeyCode () == KeyEvent.VK_CANCEL) {
				//non accetta editazione con CANC  o DELETE
				return false;
			}
		}
		return delegate.isCellEditable (anEvent);
	}

	public boolean shouldSelectCell (EventObject anEvent) {
		return delegate.shouldSelectCell (anEvent);
	}

	public boolean stopCellEditing () {
		return delegate.stopCellEditing ();
	}

	public void cancelCellEditing () {
		delegate.cancelCellEditing ();
	}

	public void addCellEditorListener (CellEditorListener l) {
		delegate.addCellEditorListener (l);
	}

	public void removeCellEditorListener (CellEditorListener l) {
		delegate.removeCellEditorListener (l);
	}
}
