/*
 * UndoableWorkSpaceModel.java
 *
 * Created on April 25, 2009, 7:45 PM
 *
 */
package net.sf.jttslite.model;

import net.sf.jttslite.core.model.WorkSpace;
import net.sf.jttslite.core.model.WorkSpaceBackup;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

/**
 * Estensione al modello con supporto all'UNDO.
 *
 *
 * @author Davide Cavestro
 * @deprecated al momento questa classe è affetta da bug gravi per cui non dev'essere usata
 *
 */
public class UndoableWorkSpaceModel extends WorkSpaceModelImpl {

    //TODO fissare i problemi di undo

    private WorkSpaceRemovalController removalController;
    
	/**
	 * Costruttore.
	 */
	public UndoableWorkSpaceModel (final WorkSpaceRemovalController removalController) {
        this.removalController = removalController;
	}

	public void addUndoableEditListener (UndoableEditListener listener) {
		listenerList.add (UndoableEditListener.class, listener);
	}

	public void removeUndoableEditListener (UndoableEditListener listener) {
		listenerList.remove (UndoableEditListener.class, listener);
	}

	@Override
	public void add (final int index, final WorkSpace element) {
		super.add (index, element);

		fireUndoableEditEvent (new AbstractUndoableEdit () {

			private final WorkSpaceBackup backup = element.backup ();
			
			@Override
			public String getPresentationName () {
				return java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("UndoableWorkSpaceModel/UndoActionName/add_workspace");
			}

			@Override
			public void undo () throws CannotUndoException {
				super.undo ();
                try {
                    UndoableWorkSpaceModel.super.removeElement (element);
                } catch (final CannotRemoveWorkSpaceException ex) {
                    throw new CannotUndoException ();
                }
			}

			@Override
			public void redo () throws CannotUndoException {
				super.redo ();

				/*
				 * @workaroud ripristina lo stato interno, annullato da JDO
				 */
				backup.restore ();
				
				UndoableWorkSpaceModel.super.add (index, element);
			}
		});
	}

	@Override
	public void addElement (final WorkSpace obj) throws DuplicatedWorkSpaceException {
		super.addElement (obj);

		fireUndoableEditEvent (new AbstractUndoableEdit () {

			private final WorkSpaceBackup backup = obj.backup ();
			
			@Override
			public String getPresentationName () {
				return java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("UndoableWorkSpaceModel/UndoActionName/add_workspace");
			}

			@Override
			public void undo () throws CannotUndoException {
				super.undo ();
                try {
                    UndoableWorkSpaceModel.super.removeElement (obj);
                } catch (final CannotRemoveWorkSpaceException ex) {
                    throw new CannotUndoException ();
                }
			}

			@Override
			public void redo () throws CannotUndoException {
				super.redo ();

				/*
				 * @workaroud ripristina lo stato interno, annullato da JDO
				 */
				backup.restore ();
                try {
                    UndoableWorkSpaceModel.super.addElement (obj);
                } catch (DuplicatedWorkSpaceException ex) {
                    throw new CannotUndoException ();
                }
			}
		});
	}

	@Override
	public void clear () {
		super.clear ();
	}

	@Override
	public void insertElementAt (final WorkSpace obj, final int index) {
		super.insertElementAt (obj, index);

		fireUndoableEditEvent (new AbstractUndoableEdit () {

			private final WorkSpaceBackup backup = obj.backup ();
			
			@Override
			public String getPresentationName () {
				return java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("UndoableWorkSpaceModel/UndoActionName/insert_workspace");
			}

			@Override
			public void undo () throws CannotUndoException {
				super.undo ();
                try {
                    UndoableWorkSpaceModel.super.removeElement (obj);
                } catch (CannotRemoveWorkSpaceException ex) {
                    throw new CannotUndoException ();
                }
			}

			@Override
			public void redo () throws CannotUndoException {
				super.redo ();

				/*
				 * @workaroud ripristina lo stato interno, annullato da JDO
				 */
				backup.restore ();
				
				UndoableWorkSpaceModel.super.insertElementAt (obj, index);
			}
		});
	}

	@Override
	public WorkSpace remove (final int index) throws CannotRemoveWorkSpaceException {
		final WorkSpaceBackup backup = super.getElementAt (index).backup ();
			
		final WorkSpace retValue = super.remove (index);

		fireUndoableEditEvent (new AbstractUndoableEdit () {

			@Override
			public String getPresentationName () {
				return java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("UndoableWorkSpaceModel/UndoActionName/remove_workspace");
			}

			@Override
			public void undo () throws CannotUndoException {
				super.undo ();

				/*
				 * @workaroud ripristina lo stato interno, annullato da JDO
				 */
				backup.restore ();

				UndoableWorkSpaceModel.super.insertElementAt (retValue, index);
			}

			@Override
			public void redo () throws CannotRedoException {
				super.redo ();
                try {
                    UndoableWorkSpaceModel.super.remove (index);
                } catch (CannotRemoveWorkSpaceException ex) {
                    throw new CannotUndoException ();
                }
			}
		});

		return retValue;
	}

    /*
     * Questo metodo solleva sempre una CannotRemoveWorkSpaceException
     * in quanto al momento c'è sempre almeno un progetto in uso.
     */
	@Override
	public void removeAllElements () throws CannotRemoveWorkSpaceException {
        throw new CannotRemoveWorkSpaceException ();

//		final WorkSpace[] elements = super.toArray ();
//		final List<WorkSpaceBackup> backups = new ArrayList<WorkSpaceBackup> ();
//		for (final WorkSpace template : elements) {
//			backups.add (template.backup ());
//		}
//		super.removeAllElements ();
//
//		fireUndoableEditEvent (new AbstractUndoableEdit () {
//
//			@Override
//			public String getPresentationName () {
//				return java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("UndoableWorkSpaceModel/UndoActionName/remove_workspace");
//			}
//
//			@Override
//			public void undo () throws CannotUndoException {
//				super.undo ();
//
//				for (int i = 0; i < elements.length; i++) {
//					final WorkSpace template = elements[i];
//					/*
//					 * @workaroud ripristina lo stato interno, annullato da JDO
//					 */
//					backups.get (i).restore ();
//                    try {
//                        UndoableWorkSpaceModel.super.addElement (template);
//                    } catch (DuplicatedWorkSpaceException ex) {
//                        throw new CannotUndoException ();
//                    }
//				}
//			}
//
//			@Override
//			public void redo () throws CannotUndoException {
//				super.redo ();
//
//				UndoableWorkSpaceModel.super.removeAllElements ();
//			}
//		});
		
	}

	@Override
	public boolean removeElement (final WorkSpace obj) throws CannotRemoveWorkSpaceException {
		final int index = indexOf (obj);

		final WorkSpaceBackup backup = obj.backup ();
			
		final boolean retValue = super.removeElement (obj);

		fireUndoableEditEvent (new AbstractUndoableEdit () {

			@Override
			public String getPresentationName () {
				return java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("UndoableWorkSpaceModel/UndoActionName/remove_workspace");
			}

			@Override
			public void undo () throws CannotUndoException {
				super.undo ();
				if (index >= 0) {
					
					/*
					 * @workaroud ripristina lo stato interno, annullato da JDO
					 */
					backup.restore ();
					
					UndoableWorkSpaceModel.super.insertElementAt (obj, index);
				}
			}

			@Override
			public void redo () throws CannotUndoException {
				super.redo ();
                try {
                    UndoableWorkSpaceModel.super.removeElement (obj);
                } catch (CannotRemoveWorkSpaceException ex) {
                    throw new CannotUndoException ();
                }
			}
		});

		return retValue;
	}

	@Override
	public void removeElementAt (final int index) throws CannotRemoveWorkSpaceException {
		final WorkSpace oldElem = get (index);

		final WorkSpaceBackup backup = oldElem.backup ();
			
		super.removeElementAt (index);

		fireUndoableEditEvent (new AbstractUndoableEdit () {

			@Override
			public String getPresentationName () {
				return java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("UndoableWorkSpaceModel/UndoActionName/remove_workspace");
			}

			@Override
			public void undo () throws CannotUndoException {
				super.undo ();

				/*
				 * @workaroud ripristina lo stato interno, annullato da JDO
				 */
				backup.restore ();
				
				UndoableWorkSpaceModel.super.insertElementAt (oldElem, index);
			}

			@Override
			public void redo () throws CannotRedoException {
				super.redo ();
                try {
                    UndoableWorkSpaceModel.super.removeElementAt (index);
                } catch (CannotRemoveWorkSpaceException ex) {
                    throw new CannotUndoException ();
                }
			}
		});

	}

	@Override
	public WorkSpace set (final int index, final WorkSpace element) throws CannotRemoveWorkSpaceException {

		final WorkSpace oldElem = get (index);

		final WorkSpaceBackup backupOld = oldElem.backup ();
			
		final WorkSpace retValue = super.set (index, element);

		fireUndoableEditEvent (new AbstractUndoableEdit () {

			private final WorkSpaceBackup backupNew = element.backup ();
			
			
			@Override
			public String getPresentationName () {
				return java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("UndoableWorkSpaceModel/UndoActionName/set_workspac");
			}

			@Override
			public void undo () throws CannotUndoException {
				super.undo ();

				/*
				 * @workaroud ripristina lo stato interno, annullato da JDO
				 */
				backupOld.restore ();
                try {
                    UndoableWorkSpaceModel.super.set (index, oldElem);
                    //UndoableWorkSpaceModel.super.removeElement (element);
                } catch (CannotRemoveWorkSpaceException ex) {
                    throw new CannotUndoException ();
                }
				//UndoableWorkSpaceModel.super.removeElement (element);
			}

			@Override
			public void redo () throws CannotUndoException {
				super.redo ();

				/*
				 * @workaroud ripristina lo stato interno, annullato da JDO
				 */
				backupNew.restore ();
                try {
                    UndoableWorkSpaceModel.super.set (index, element);
                } catch (CannotRemoveWorkSpaceException ex) {
                    throw new CannotUndoException ();
                }
			}
		});

		return retValue;
	}

	@Override
	public void setElementAt (final WorkSpace obj, final int index) {
		final WorkSpace oldElem = get (index);
		final WorkSpaceBackup backupOld = oldElem.backup ();
		
		super.setElementAt (obj, index);

		fireUndoableEditEvent (new AbstractUndoableEdit () {

			private final WorkSpaceBackup backupNew = obj.backup ();
			
			@Override
			public String getPresentationName () {
				return java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("UndoableWorkSpaceModel/UndoActionName/set_workspace");
			}

			@Override
			public void undo () throws CannotUndoException {
				super.undo ();

				/*
				 * @workaroud ripristina lo stato interno, annullato da JDO
				 */
				backupOld.restore ();
				
				UndoableWorkSpaceModel.super.setElementAt (oldElem, index);
				//UndoableWorkSpaceModel.super.removeElement (obj);
			}

			@Override
			public void redo () throws CannotUndoException {
				super.redo ();

				/*
				 * @workaroud ripristina lo stato interno, annullato da JDO
				 */
				backupNew.restore ();
				
				UndoableWorkSpaceModel.super.setElementAt (obj, index);
			}
		});
	}

	@Override
	public void updateElement (final WorkSpace ws, final String name, final String descr, final String notes) {
		final String oldName = ws.getName ();
		final String oldDescr = ws.getDescription ();
		final String oldNotes = ws.getNotes ();
		super.updateElement (ws, name, descr, notes);

		fireUndoableEditEvent (new AbstractUndoableEdit () {

			@Override
			public String getPresentationName () {
				return java.util.ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString ("UndoableWorkSpaceModel/UndoActionName/edit_workspace");
			}

			@Override
			public void undo () throws CannotUndoException {
				super.undo ();

				UndoableWorkSpaceModel.super.updateElement (ws, oldName, oldDescr, oldNotes);
			}

			@Override
			public void redo () throws CannotUndoException {
				super.redo ();

				UndoableWorkSpaceModel.super.updateElement (ws, name, descr, notes);
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
