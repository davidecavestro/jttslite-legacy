/*
 * UndoableTaskTreeModel.java
 *
 * Created on December 4, 2006, 11:05 PM
 *
 */

package net.sf.jttslite.model;

import net.sf.jttslite.core.model.PieceOfWorkBackup;
import net.sf.jttslite.core.model.TaskBackup;
import net.sf.jttslite.core.model.WorkSpace;
import net.sf.jttslite.core.model.PieceOfWork;
import net.sf.jttslite.core.model.Task;
import net.sf.jttslite.conf.ApplicationOptions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class UndoableTaskTreeModel extends TaskTreeModelImpl {
	
	/**
	 * Costruttore.
	 * @param applicationOptions le opzioni di configurazione.
	 * @param name il nome.
	 * @param resources le risorse di localizzazione.
	 */
	public UndoableTaskTreeModel (final ApplicationOptions applicationOptions,  final TaskTreeModelExceptionHandler peh, final WorkSpace workSpace) {
		super (applicationOptions, peh, workSpace);
	}
	
	public void addUndoableEditListener (UndoableEditListener listener) {
		listenerList.add (UndoableEditListener.class, listener);
	}
	
	public void removeUndoableEditListener (UndoableEditListener listener) {
		listenerList.remove (UndoableEditListener.class, listener);
	}
	
	
	
	@Override
	public void removePieceOfWork (final PieceOfWork pow) {
		final PieceOfWorkBackup backup = pow.backup ();
			
		final Task t = pow.getTask ();
		final int pos = t.pieceOfWorkIndex (pow);
		super.removePieceOfWork (pow);
		
		fireUndoableEditEvent (new AbstractUndoableEdit () {
			@Override
			public String getPresentationName () {
				return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("delete_action");
			}
			
			
			@Override
			public void undo () throws CannotUndoException {
				super.undo ();
				
				/*
				 * @workaroud ripristina lo stato interno, annullato da JDO
				 */
				backup.restore ();
				
				UndoableTaskTreeModel.super.insertPieceOfWorkInto (pow, t, pos);
			}
			
			
			@Override
			public void redo () throws CannotUndoException {
				super.redo ();
				
				UndoableTaskTreeModel.super.removePieceOfWork (pow);
			}
		});
	}
	
	@Override
	public void insertPieceOfWorkInto (final PieceOfWork newPOW, final Task t, final int index) {
		final boolean startingPOW = newPOW.getTo ()==null;
		super.insertPieceOfWorkInto (newPOW, t, index);
		
		fireUndoableEditEvent (new AbstractUndoableEdit () {
			
			private final PieceOfWorkBackup backup = newPOW.backup ();
			
			@Override
			public String getPresentationName () {
				if (startingPOW) {
					return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("start_action");
				} else {
					return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("new_action");
				}
			}
			
			
			@Override
			public void undo () throws CannotUndoException {
				super.undo ();
				
				UndoableTaskTreeModel.super.removePieceOfWork (newPOW);
			}
			
			
			@Override
			public void redo () throws CannotUndoException {
				super.redo ();
				
				/*
				 * @workaroud ripristina lo stato interno, annullato da JDO
				 */
				backup.restore ();
				
				UndoableTaskTreeModel.super.insertPieceOfWorkInto (newPOW, t, index);
			}
		});
		
	}
	
	@Override
	public void removeNodeFromParent (final Task node) {
		final TaskBackup backup = backupSubTree (node);
		
		final Task p = node.getParent ();
		final int pos = p.childIndex (node);
		super.removeNodeFromParent (node);
		
		fireUndoableEditEvent (new AbstractUndoableEdit () {

			@Override
			public String getPresentationName () {
				return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("delete_task");
			}
			
			
			@Override
			public void undo () throws CannotUndoException {
				super.undo ();
				
				/*
				 * @workaroud ripristina lo stato interno, annullato da JDO
				 */
				backup.restore ();
				
				UndoableTaskTreeModel.super.insertNodeInto (node, p, pos);
			}
			
			
			@Override
			public void redo () throws CannotUndoException {
				super.redo ();
				
				UndoableTaskTreeModel.super.removeNodeFromParent (node);
			}
		});
	}
	
	@Override
	public void insertNodeInto (final Task newChild, final Task parent, final int index) {
		super.insertNodeInto (newChild, parent, index);
		
		fireUndoableEditEvent (new AbstractUndoableEdit () {
			
			private final TaskBackup backup = backupSubTree (newChild);
			
			@Override
			public String getPresentationName () {
				return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("new_task");
			}
			
			
			@Override
			public void undo () throws CannotUndoException {
				super.undo ();
				
				UndoableTaskTreeModel.super.removeNodeFromParent (newChild);
			}
			
			
			@Override
			public void redo () throws CannotUndoException {
				super.redo ();
				
				/*
				 * @workaroud ripristina lo stato interno, annullato da JDO
				 */
				backup.restore ();
				
				UndoableTaskTreeModel.super.insertNodeInto (newChild, parent, index);
			}
		});
	}
	
	@Override
	public void removeTasks (final Task parent, final List<Task> l) {
		final boolean onlyOne = l.size ()==1;
		
		final Map<Integer, TaskBackup> backupMap = new HashMap<Integer, TaskBackup> ();
		
		final Map<Integer, Task> m = new HashMap<Integer, Task> ();
		final List<Integer> positions = new ArrayList<Integer> ();
		for (final Task t : l) {
			final Integer pos = parent.childIndex (t);
			m.put (pos, t);
			backupMap.put (pos, backupSubTree (t));
			positions.add (pos);
		}
		
		super.removeTasks (parent, l);
		
		Collections.sort (positions);
		
		fireUndoableEditEvent (new AbstractUndoableEdit () {
			
			@Override
			public String getPresentationName () {
				if (onlyOne) {
					return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("delete_task");
				} else {
					return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("delete_tasks");
				}
			}
			
			
			@Override
			public void undo () throws CannotUndoException {
				super.undo ();
				
				for (final Integer i : positions) {
					final Task t = m.get (i);
					final TaskBackup backup = backupMap.get (i);
					/*
					 * @workaroud ripristina lo stato interno, annullato da JDO
					 */
					backup.restore ();
					UndoableTaskTreeModel.super.insertNodeInto (t, parent, i.intValue ());
				}
			}
			
			
			@Override
			public void redo () throws CannotUndoException {
				super.redo ();
				
				UndoableTaskTreeModel.super.removeTasks (parent, l);
			}
		});
	}
	
	@Override
	public void removePiecesOfWork (final Task t, final List<PieceOfWork> l) {
		final boolean onlyOne = l.size ()==1;
		final Map<Integer, PieceOfWorkBackup> backupMap = new HashMap<Integer, PieceOfWorkBackup> ();
		
		final Map<Integer, PieceOfWork> m = new HashMap<Integer, PieceOfWork> ();
		final List<Integer> positions = new ArrayList<Integer> ();
		for (final PieceOfWork pow : l) {
			final Integer pos = t.pieceOfWorkIndex (pow);
			m.put (pos, pow);
			backupMap.put (pos, pow.backup ());
			positions.add (pos);
		}
		
		super.removePiecesOfWork (t, l);
		
		Collections.sort (positions);
		
		fireUndoableEditEvent (new AbstractUndoableEdit () {
			
			@Override
			public String getPresentationName () {
				if (onlyOne) {
					return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("delete_action");
				} else {
					return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("delete_actions");
				}
			}
			
			
			@Override
			public void undo () throws CannotUndoException {
				super.undo ();
				
				for (final Integer i : positions) {
					final PieceOfWork pow = m.get (i);
					final PieceOfWorkBackup backup = backupMap.get (i);
					/*
					 * @workaroud ripristina lo stato interno, annullato da JDO
					 */
					backup.restore ();
					UndoableTaskTreeModel.super.insertPieceOfWorkInto (pow, t, i.intValue ());
				}
			}
			
			
			@Override
			public void redo () throws CannotUndoException {
				super.redo ();
				
				UndoableTaskTreeModel.super.removePiecesOfWork (t, l);
			}
		});
	}
	
	@Override
	public void moveTasksTo (final Task previousParent, final List<Task> l, final Task target, final int position) {
		final boolean onlyOne = l.size ()==1;
		final Map<Integer, Task> m = new HashMap<Integer, Task> ();
		final List<Integer> positions = new ArrayList<Integer> ();
		for (final Task t : l) {
			final Integer pos = previousParent.childIndex (t);
			m.put (pos, t);
			positions.add (pos);
		}
		
		super.moveTasksTo (previousParent, l, target, position);
		
		Collections.sort (positions);
		
		fireUndoableEditEvent (new AbstractUndoableEdit () {
			
			@Override
			public String getPresentationName () {
				if (onlyOne) {
					return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("move_task");
				} else {
					return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("move_tasks");
				}
			}
			
			
			@Override
			public void undo () throws CannotUndoException {
				super.undo ();
				
				for (final Integer i : positions) {
					final List<Task> l = new ArrayList<Task> ();
					l.add (m.get (i));
					UndoableTaskTreeModel.super.moveTasksTo (target, l, previousParent, i.intValue ());
				}
			}
			
			
			@Override
			public void redo () throws CannotUndoException {
				super.redo ();
				
				UndoableTaskTreeModel.super.moveTasksTo (previousParent, l, target, position);
			}
		});		
		
	}
	
	@Override
	public void movePiecesOfWorkTo (final Task previousTask, final List<PieceOfWork> l, final Task target, final int position) {
		final boolean onlyOne = l.size ()==1;
		final Map<Integer, PieceOfWork> m = new HashMap<Integer, PieceOfWork> ();
		final List<Integer> positions = new ArrayList<Integer> ();
		for (final PieceOfWork pow : l) {
			final Integer pos = previousTask.pieceOfWorkIndex (pow);
			m.put (pos, pow);
			positions.add (pos);
		}
		
		super.movePiecesOfWorkTo (previousTask, l, target, position);
		
		Collections.sort (positions);
		
		fireUndoableEditEvent (new AbstractUndoableEdit () {
			
			@Override
			public String getPresentationName () {
				if (onlyOne) {
					return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("move_action");
				} else {
					return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("move_actions");
				}
			}
			
			
			@Override
			public void undo () throws CannotUndoException {
				super.undo ();
				
				for (final Integer i : positions) {
					final List<PieceOfWork> l = new ArrayList ();
					l.add (m.get (i));
					UndoableTaskTreeModel.super.movePiecesOfWorkTo (target, l, previousTask, i.intValue ());
				}
			}
			
			
			@Override
			public void redo () throws CannotUndoException {
				super.redo ();
				
				UndoableTaskTreeModel.super.movePiecesOfWorkTo (previousTask, l, target, position);
			}
		});
	}
	
	@Override
	public void updateTask (final Task t, final String name, final String description) {
		final String oldName = t.getName ();
		super.updateTask (t, name, description);

		fireUndoableEditEvent (new AbstractUndoableEdit () {
			
			@Override
			public String getPresentationName () {
				return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("edit_task");
			}
			
			
			@Override
			public void undo () throws CannotUndoException {
				super.undo ();
				
				UndoableTaskTreeModel.super.updateTask (t, oldName, description);
			}
			
			
			@Override
			public void redo () throws CannotUndoException {
				super.redo ();
				
				UndoableTaskTreeModel.super.updateTask (t, name, description);
			}
		});
		
	}
	
	
	@Override
	public void updatePieceOfWork (final PieceOfWork pow, final Date from, final Date to, final String description) {
		final Date oldFrom = pow.getFrom ();
		final Date oldTo = pow.getTo ();
		final String oldDescr = pow.getDescription ();
		
		final boolean stoppingPOW = oldTo==null && to!=null;
		
		super.updatePieceOfWork (pow, from, to, description);

		fireUndoableEditEvent (new AbstractUndoableEdit () {
			
			@Override
			public String getPresentationName () {
				if (stoppingPOW) {
					return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("stop_action");
				} else {
					return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("edit_action");
				}
			}
			
			
			@Override
			public void undo () throws CannotUndoException {
				super.undo ();
				
				UndoableTaskTreeModel.super.updatePieceOfWork (pow, oldFrom, oldTo, oldDescr);
			}
			
			
			@Override
			public void redo () throws CannotUndoException {
				super.redo ();
				
				UndoableTaskTreeModel.super.updatePieceOfWork (pow, from, to, description);
			}
		});
		
	}	
	
	protected void fireUndoableEditEvent (final UndoableEdit undoableAction) {
		final UndoableEditEvent editEvent = new UndoableEditEvent (this, undoableAction);
		final UndoableEditListener[] listeners = (UndoableEditListener[])getListeners (UndoableEditListener.class);
		for (int i=0; i< listeners.length;i++){
			listeners[i].undoableEditHappened (editEvent);
		}
	}
	
	private final TaskBackup backupSubTree (final Task task) {
		final List<Task> l = new ArrayList<Task> ();
		l.add (task);
		return backupSubTree (l).get (0);
	}
	
	private final List<TaskBackup> backupSubTree (final List<Task> tasks) {
		final List<TaskBackup> backups = new ArrayList<TaskBackup> ();
		for (final Task t : tasks) {
			backups.add (t.backup ());
		}
		return backups;
	}
}
