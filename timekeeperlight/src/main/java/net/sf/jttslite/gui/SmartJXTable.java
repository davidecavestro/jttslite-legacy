package net.sf.jttslite.gui;

import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import javax.swing.table.TableModel;
import org.jdesktop.swingx.JXTable;

class SmartJXTable extends JXTable {

	/**
	 * Costruttore senza parametri.
	 */
	public SmartJXTable () {
	}
	
	/**
	 * Costruttore con modello.
	 * 
	 * @param templateTableModel il modello della tabella.
	 */
	public SmartJXTable (final TableModel templateTableModel) {
		super (templateTableModel);
	}

	/**
	 * Scavalca il metodo di JTable solo per poter invocare editCellAt passando l'evento
	 * in questo modo l'editor può decidere se far partire l'editazione oppure no, consentendo di escludere
	 * alcuni tasti, come il CANC, da quelli che fanno aprtire l'editazione.
	 * @workaround
	 * 
	 * @param ks
	 * @param e
	 * @param condition
	 * @param pressed
	 * @return
	 */
	@Override
//	protected boolean processKeyBinding (KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
//		boolean retValue = false;
//		if (!retValue && condition == WHEN_ANCESTOR_OF_FOCUSED_COMPONENT && isFocusOwner () && !Boolean.FALSE.equals ((Boolean) getClientProperty ("JTable.autoStartsEdit"))) {
//			Component editorComponent = getEditorComponent ();
//			if (editorComponent == null) {
//				// Only attempt to install the editor on a KEY_PRESSED,
//				if (e == null || e.getID () != KeyEvent.KEY_PRESSED) {
//					return false;
//				}
//				// Don't start when just a modifier is pressed
//				int code = e.getKeyCode ();
//				if (code == KeyEvent.VK_SHIFT || code == KeyEvent.VK_CONTROL || code == KeyEvent.VK_ALT) {
//					return false;
//				}
//				// Try to install the editor
//				int leadRow = getSelectionModel ().getLeadSelectionIndex ();
//				int leadColumn = getColumnModel ().getSelectionModel ().getLeadSelectionIndex ();
//				if (leadRow != -1 && leadColumn != -1 && !isEditing ()) {
//					//HACK
//					if (!editCellAt (leadRow, leadColumn, e)) {
//						return false;
//					}
//				}
//				editorComponent = getEditorComponent ();
//				if (editorComponent == null) {
//					return false;
//				}
//			}
//		}
////			return retValue;
//		return super.processKeyBinding (ks, e, condition, pressed);
//	}
	
	
    protected boolean processKeyBinding (KeyStroke ks, KeyEvent e,
		int condition, boolean pressed) {
		
//		System.err.println ("ks: "+ks);
//		if (!isEditing () && ks!=null) { 
//			final int code = ks.getKeyCode ();
//			if (code == KeyEvent.VK_CANCEL || code == KeyEvent.VK_DELETE) {
//				System.err.println ("cancel call");
//				return true;
//			}
//			if (code == KeyEvent.VK_SHIFT || code == KeyEvent.VK_CONTROL ||
//				code == KeyEvent.VK_ALT) {
//				System.err.println ("modifiers call");
//				return true;
//			}
//		}
		final boolean r = super.processKeyBinding (ks, e, condition, pressed);
		
//		if (r) {
//			System.err.println ("consuming...");
//			e.consume ();
//		}
		return r;

//		boolean retValue = false;
//
//		// Start editing when a key is typed. UI classes can disable this behavior
//		// by setting the client property JTable.autoStartsEdit to Boolean.FALSE.
//		if (!retValue && condition == WHEN_ANCESTOR_OF_FOCUSED_COMPONENT &&
//			isFocusOwner () &&
//			!Boolean.FALSE.equals ((Boolean) getClientProperty ("JTable.autoStartsEdit"))) {
//			// We do not have a binding for the event.
//			Component editorComponent = getEditorComponent ();
//			if (editorComponent == null) {
//				// Only attempt to install the editor on a KEY_PRESSED,
//				if (e == null || e.getID () != KeyEvent.KEY_PRESSED) {
//					return false;
//				}
//				// Don't start when just a modifier is pressed
//				int code = e.getKeyCode ();
//				if (code == KeyEvent.VK_SHIFT || code == KeyEvent.VK_CONTROL ||
//					code == KeyEvent.VK_ALT) {
//					return false;
//				}
//				if (code == KeyEvent.VK_CANCEL || code == KeyEvent.VK_DELETE) {
//					return false;
//				}
//				// Try to install the editor
//				int leadRow = getSelectionModel ().getLeadSelectionIndex ();
//				int leadColumn = getColumnModel ().getSelectionModel ().
//					getLeadSelectionIndex ();
//				if (leadRow != -1 && leadColumn != -1 && !isEditing ()) {
//					if (!editCellAt (leadRow, leadColumn)) {
//						return false;
//					}
//				}
//				editorComponent = getEditorComponent ();
//				if (editorComponent == null) {
//					return false;
//				}
//			}
////			// If the editorComponent is a JComponent, pass the event to it.
////			if (editorComponent instanceof JComponent) {
////				retValue = ((JComponent) editorComponent).processKeyBinding (ks, e, WHEN_FOCUSED, pressed);
////				// If we have started an editor as a result of the user
////				// pressing a key and the surrendersFocusOnKeystroke property
////				// is true, give the focus to the new editor.
////				if (getSurrendersFocusOnKeystroke ()) {
////					editorComponent.requestFocus ();
////				}
////			}
//		}
//		return super.processKeyBinding (ks, e, condition, pressed);
	}	
}
