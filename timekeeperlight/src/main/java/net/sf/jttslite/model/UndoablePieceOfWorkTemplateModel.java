/*
 * UndoablePieceOfWorkTemplateModel.java
 *
 * Created on August 2, 2008, 5:05 PM
 *
 */
package net.sf.jttslite.model;

import net.sf.jttslite.core.model.PieceOfWorkTemplateBackup;
import net.sf.jttslite.core.model.impl.ProgressTemplate;
import net.sf.jttslite.core.util.Duration;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

/**
 * Estensione al modello con supporto all'UNDO.
 *
 * @author Davide Cavestro
 */
public class UndoablePieceOfWorkTemplateModel extends PieceOfWorkTemplateModelImpl {

	/**
	 * Costruttore.
	 */
	public UndoablePieceOfWorkTemplateModel () {
	}

	public void addUndoableEditListener (UndoableEditListener listener) {
		listenerList.add (UndoableEditListener.class, listener);
	}

	public void removeUndoableEditListener (UndoableEditListener listener) {
		listenerList.remove (UndoableEditListener.class, listener);
	}

	@Override
	public void add (final int index, final ProgressTemplate element) {
		super.add (index, element);

		fireUndoableEditEvent (new AbstractUndoableEdit () {

			private final PieceOfWorkTemplateBackup backup = element.backup ();
			
			@Override
			public String getPresentationName () {
				return java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("UndoablePieceOfWorkTemplateModel/UndoActionName/add_progresstemplate");
			}

			@Override
			public void undo () throws CannotUndoException {
				super.undo ();

				UndoablePieceOfWorkTemplateModel.super.removeElement (element);
			}

			@Override
			public void redo () throws CannotUndoException {
				super.redo ();

				/*
				 * @workaroud ripristina lo stato interno, annullato da JDO
				 */
				backup.restore ();
				
				UndoablePieceOfWorkTemplateModel.super.add (index, element);
			}
		});
	}

	@Override
	public void addElement (final ProgressTemplate obj) {
		super.addElement (obj);

		fireUndoableEditEvent (new AbstractUndoableEdit () {

			private final PieceOfWorkTemplateBackup backup = obj.backup ();
			
			@Override
			public String getPresentationName () {
				return java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("UndoablePieceOfWorkTemplateModel/UndoActionName/add_progresstemplate");
			}

			@Override
			public void undo () throws CannotUndoException {
				super.undo ();

				UndoablePieceOfWorkTemplateModel.super.removeElement (obj);
			}

			@Override
			public void redo () throws CannotUndoException {
				super.redo ();

				/*
				 * @workaroud ripristina lo stato interno, annullato da JDO
				 */
				backup.restore ();
				
				UndoablePieceOfWorkTemplateModel.super.addElement (obj);
			}
		});
	}

	@Override
	public void clear () {
		super.clear ();
	}

	@Override
	public void insertElementAt (final ProgressTemplate obj, final int index) {
		super.insertElementAt (obj, index);

		fireUndoableEditEvent (new AbstractUndoableEdit () {

			private final PieceOfWorkTemplateBackup backup = obj.backup ();
			
			@Override
			public String getPresentationName () {
				return java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("UndoablePieceOfWorkTemplateModel/UndoActionName/insert_progresstemplate");
			}

			@Override
			public void undo () throws CannotUndoException {
				super.undo ();

				UndoablePieceOfWorkTemplateModel.super.removeElement (obj);
			}

			@Override
			public void redo () throws CannotUndoException {
				super.redo ();

				/*
				 * @workaroud ripristina lo stato interno, annullato da JDO
				 */
				backup.restore ();
				
				UndoablePieceOfWorkTemplateModel.super.insertElementAt (obj, index);
			}
		});
	}

	@Override
	public ProgressTemplate remove (final int index) {
		final PieceOfWorkTemplateBackup backup = super.getElementAt (index).backup ();
			
		final ProgressTemplate retValue = super.remove (index);

		fireUndoableEditEvent (new AbstractUndoableEdit () {

			@Override
			public String getPresentationName () {
				return java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("UndoablePieceOfWorkTemplateModel/UndoActionName/remove_progresstemplate");
			}

			@Override
			public void undo () throws CannotUndoException {
				super.undo ();

				/*
				 * @workaroud ripristina lo stato interno, annullato da JDO
				 */
				backup.restore ();

				UndoablePieceOfWorkTemplateModel.super.insertElementAt (retValue, index);
			}

			@Override
			public void redo () throws CannotUndoException {
				super.redo ();

				UndoablePieceOfWorkTemplateModel.super.remove (index);
			}
		});

		return retValue;
	}

	@Override
	public void removeAllElements () {
		final ProgressTemplate[] elements = super.toArray ();
		final List<PieceOfWorkTemplateBackup> backups = new ArrayList<PieceOfWorkTemplateBackup> ();
		for (final ProgressTemplate template : elements) {
			backups.add (template.backup ());
		}
		super.removeAllElements ();
		
		fireUndoableEditEvent (new AbstractUndoableEdit () {

			@Override
			public String getPresentationName () {
				return java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("UndoablePieceOfWorkTemplateModel/UndoActionName/remove_progresstemplate");
			}

			@Override
			public void undo () throws CannotUndoException {
				super.undo ();
				
				for (int i = 0; i < elements.length; i++) {
					final ProgressTemplate template = elements[i];
					/*
					 * @workaroud ripristina lo stato interno, annullato da JDO
					 */
					backups.get (i).restore ();
					
					UndoablePieceOfWorkTemplateModel.super.addElement (template);
				}
			}

			@Override
			public void redo () throws CannotUndoException {
				super.redo ();

				UndoablePieceOfWorkTemplateModel.super.removeAllElements ();
			}
		});
		
	}

	@Override
	public boolean removeElement (final ProgressTemplate obj) {
		final int index = indexOf (obj);

		final PieceOfWorkTemplateBackup backup = obj.backup ();
			
		final boolean retValue = super.removeElement (obj);

		fireUndoableEditEvent (new AbstractUndoableEdit () {

			@Override
			public String getPresentationName () {
				return java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("UndoablePieceOfWorkTemplateModel/UndoActionName/remove_progresstemplate");
			}

			@Override
			public void undo () throws CannotUndoException {
				super.undo ();
				if (index >= 0) {
					
					/*
					 * @workaroud ripristina lo stato interno, annullato da JDO
					 */
					backup.restore ();
					
					UndoablePieceOfWorkTemplateModel.super.insertElementAt (obj, index);
				}
			}

			@Override
			public void redo () throws CannotUndoException {
				super.redo ();

				UndoablePieceOfWorkTemplateModel.super.removeElement (obj);
			}
		});

		return retValue;
	}

	@Override
	public void removeElementAt (final int index) {
		final ProgressTemplate oldElem = get (index);

		final PieceOfWorkTemplateBackup backup = oldElem.backup ();
			
		super.removeElementAt (index);

		fireUndoableEditEvent (new AbstractUndoableEdit () {

			@Override
			public String getPresentationName () {
				return java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("UndoablePieceOfWorkTemplateModel/UndoActionName/remove_progresstemplate");
			}

			@Override
			public void undo () throws CannotUndoException {
				super.undo ();

				/*
				 * @workaroud ripristina lo stato interno, annullato da JDO
				 */
				backup.restore ();
				
				UndoablePieceOfWorkTemplateModel.super.insertElementAt (oldElem, index);
			}

			@Override
			public void redo () throws CannotUndoException {
				super.redo ();

				UndoablePieceOfWorkTemplateModel.super.removeElementAt (index);
			}
		});

	}

	@Override
	public ProgressTemplate set (final int index, final ProgressTemplate element) {

		final ProgressTemplate oldElem = get (index);

		final PieceOfWorkTemplateBackup backupOld = oldElem.backup ();
			
		final ProgressTemplate retValue = super.set (index, element);

		fireUndoableEditEvent (new AbstractUndoableEdit () {

			private final PieceOfWorkTemplateBackup backupNew = element.backup ();
			
			
			@Override
			public String getPresentationName () {
				return java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("UndoablePieceOfWorkTemplateModel/UndoActionName/set_progresstemplateset_progresstemplate");
			}

			@Override
			public void undo () throws CannotUndoException {
				super.undo ();

				/*
				 * @workaroud ripristina lo stato interno, annullato da JDO
				 */
				backupOld.restore ();
				
				UndoablePieceOfWorkTemplateModel.super.set (index, oldElem);
				//UndoablePieceOfWorkTemplateModel.super.removeElement (element);
			}

			@Override
			public void redo () throws CannotUndoException {
				super.redo ();

				/*
				 * @workaroud ripristina lo stato interno, annullato da JDO
				 */
				backupNew.restore ();
				
				UndoablePieceOfWorkTemplateModel.super.set (index, element);
			}
		});

		return retValue;
	}

	@Override
	public void setElementAt (final ProgressTemplate obj, final int index) {
		final ProgressTemplate oldElem = get (index);
		final PieceOfWorkTemplateBackup backupOld = oldElem.backup ();
		
		super.setElementAt (obj, index);

		fireUndoableEditEvent (new AbstractUndoableEdit () {

			private final PieceOfWorkTemplateBackup backupNew = obj.backup ();
			
			@Override
			public String getPresentationName () {
				return java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("UndoablePieceOfWorkTemplateModel/UndoActionName/set_progresstemplateset_progresstemplate");
			}

			@Override
			public void undo () throws CannotUndoException {
				super.undo ();

				/*
				 * @workaroud ripristina lo stato interno, annullato da JDO
				 */
				backupOld.restore ();
				
				UndoablePieceOfWorkTemplateModel.super.setElementAt (oldElem, index);
				//UndoablePieceOfWorkTemplateModel.super.removeElement (obj);
			}

			@Override
			public void redo () throws CannotUndoException {
				super.redo ();

				/*
				 * @workaroud ripristina lo stato interno, annullato da JDO
				 */
				backupNew.restore ();
				
				UndoablePieceOfWorkTemplateModel.super.setElementAt (obj, index);
			}
		});
	}

	@Override
	public void updateElement (final ProgressTemplate t, final String name, final String notes, final Duration d) {
		final String oldName = t.getName ();
		final String oldNotes = t.getNotes ();
		final Duration oldDuration = t.getDuration ();
		super.updateElement (t, name, notes, d);

		fireUndoableEditEvent (new AbstractUndoableEdit () {

			@Override
			public String getPresentationName () {
				return java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("UndoablePieceOfWorkTemplateModel/UndoActionName/edit_progresstemplate");
			}

			@Override
			public void undo () throws CannotUndoException {
				super.undo ();

				UndoablePieceOfWorkTemplateModel.super.updateElement (t, oldName, oldNotes, oldDuration);
			}

			@Override
			public void redo () throws CannotUndoException {
				super.redo ();

				UndoablePieceOfWorkTemplateModel.super.updateElement (t, name, notes, d);
			}
		});

	}

	protected void fireUndoableEditEvent (final UndoableEdit undoableAction) {
		final UndoableEditEvent editEvent = new UndoableEditEvent (this, undoableAction);
		final UndoableEditListener[] listeners = (UndoableEditListener[]) getListeners (UndoableEditListener.class);
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].undoableEditHappened (editEvent);
		}
	}
}
