/*
 * XMLImporter.java
 *
 * Created on January 2, 2007, 3:22 PM
 *
 */

package net.sf.jttslite.backup;

import net.sf.jttslite.common.util.CalendarUtils;
import net.sf.jttslite.ApplicationContext;
import net.sf.jttslite.model.DuplicatedWorkSpaceException;
import net.sf.jttslite.core.model.WorkSpace;
import net.sf.jttslite.model.WorkSpaceModelImpl;
import net.sf.jttslite.core.model.impl.Progress;
import net.sf.jttslite.core.model.impl.ProgressItem;
import net.sf.jttslite.core.model.impl.Project;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Document;
import org.jdom.Element;

/**
 * Effettua il restore da file dei dati relativi ad un progetto.
 *
 * @author Davide Cavestro
 */
public class XMLImporter {
	
	final ApplicationContext _context;
	
	/**
	 * Costruttore.
	 */
	public XMLImporter (final ApplicationContext context) {
		_context = context;
	}
	
	/**
	 * Effettua il restore da file dei dati relativi ad un progetto.
	 * Il file usato &egrave; in formato XML.
	 *
	 * @param project il progetto da salvare.
	 * @param document il documento XML da usare.
	 *
	 * @return <CODE>true</CODE> se il ripristino &egrave; andato a buon fine.
	 */
	public boolean restore (final Document document, final ImportConflictsResolver icr) {
		final ProjectElement projectElement = new ProjectElement (document.getRootElement ());
		final WorkSpace prj = projectElement.getProject ();
        try {
            return _restore (prj, icr);
        } catch (final DuplicatedWorkSpaceException ex) {
            icr.showErrorMessage (MessageFormat.format (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("XMLImporter/ErrorMessage/DuplicatedWorkSpaceException"), prj.getName ()));
        }
        return false;
	}

    /**
     * Rende persistente un progetto, eventualmente sostituendone un altro.
     *
     * @param prj il progetto da rendere persistente.
     * @param toRemove il progetto da sostituire.
     */
	private void makePersistent (final WorkSpace prj, final WorkSpace toRemove) throws DuplicatedWorkSpaceException {
		final WorkSpaceModelImpl wm = _context.getWorkSpaceModel ();
			if (toRemove!=null){
				wm.setElementAt (prj, wm.indexOf (toRemove));
			} else {
				wm.addElement (prj);
			}
	}
	
	
	private WorkSpace findConflicts (final WorkSpace prj) {
		for (final Iterator<WorkSpace> it = Arrays.asList (_context.getWorkSpaceModel ().toArray ()).iterator ();it.hasNext ();){
			final WorkSpace existing = it.next ();
			if (prj!=existing && existing.getName ()!=null && existing.getName ().equals (prj.getName ())){
				/*
				 * trovato conflitto
				 */
				return existing;
			}
		}
		return null;
	}
	
	private boolean _restore (final WorkSpace prj, final ImportConflictsResolver icr) throws DuplicatedWorkSpaceException{
		WorkSpace conflicting = null;
		ConflictResolution choice = null;
		do {
			conflicting = findConflicts (prj);

			if (conflicting==null) {
				break;
			}
			choice = icr.chooseResolution ();

			if (choice==ConflictResolution.RENAME) {
				/*
				 * rinomina
				 */
				icr.rename (prj);
			}

		} while (choice==ConflictResolution.RENAME);
			
			
		if (choice==ConflictResolution.CANCEL) {
			/*
			 * azione abbandonata
			 */
			return false;
		}
		
		return save (prj, icr, conflicting);
	}
	
	private boolean save (final WorkSpace prj, final ImportConflictsResolver icr, final WorkSpace conflicting) throws DuplicatedWorkSpaceException {
		final boolean overwritingCurrentWS = conflicting == _context.getModel ().getWorkSpace ();
		if (overwritingCurrentWS) {
			if (!icr.continueOverwritingCurrentWorkspace ()) {
				return false;
			}
		}
		makePersistent (prj, conflicting);
		if (overwritingCurrentWS) {
			/*
			 * sovrascrittura del progetto corrente.
			 */
			_context.getModel ().setWorkSpace (prj);
		}
		return true;
	}
	
	
	public enum ConflictResolution {
		OVERWRITE {
            @Override
			public String toString () {
				return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Overwrite");
			}
		},
		RENAME {
            @Override
			public String toString () {
				return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Rename");
			}
		},
		CANCEL {
            @Override
			public String toString () {
				return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Cancel");
			}
		}
		
	}
		
	public static abstract class ImportConflictsResolver {
		
		public abstract ConflictResolution chooseResolution ();
		
		public void rename (final WorkSpace prj) {
			prj.setName (getNewName ());
			prj.getRoot ().setName (prj.getName ());
		}
		
		public abstract void showErrorMessage (String msg);

		public abstract String getNewName ();

		public abstract boolean continueOverwritingCurrentWorkspace ();
		
	}
	
	
	
	private final class ProgressItemElement extends Element {
		private ProgressItem _progressItem;
		public ProgressItemElement (final Element element, final Project project){
			this._progressItem = new ProgressItem ();
			
			this._progressItem.setCode (element.getChild (XMLResources.CODE_PROPERTY).getValue ());
			this._progressItem.setName (element.getChild (XMLResources.NAME_PROPERTY).getValue ());
			this._progressItem.setDescription (element.getChild (XMLResources.DESCRIPTION_PROPERTY).getValue ());
			this._progressItem.setNotes (element.getChild (XMLResources.NOTES_PROPERTY).getValue ());
			
			final List children = new ArrayList ();
			for (final Iterator it = element.getChildren (XMLResources.PROGRESSITEM_ELEMENT).iterator ();it.hasNext ();){
				try {
					final ProgressItem child = new ProgressItemElement ((Element)it.next (), project).getProgressItem ();
					child.setParent (this._progressItem);
					children.add (child);
				} catch (Exception e){
					_context.getLogger ().log (Level.WARNING, java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("error_importing_task_"),e);
				}
			}
			this._progressItem.setChildren (children);
			final List progresses = new ArrayList ();
			for (final Iterator it = element.getChildren (XMLResources.PROGRESS_ELEMENT).iterator ();it.hasNext ();){
				try {
					final Progress progress = new ProgressElement ((Element)it.next (), this._progressItem).getProgress ();
					progresses.add (progress);
				} catch (Exception e){
					_context.getLogger ().log (Level.WARNING, java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("error_importing_action_"),e);
				}
			}
			this._progressItem.setProgresses (progresses);
		}
		
		public ProgressItem getProgressItem (){
			return this._progressItem;
		}
		
	}

	private final class ProjectElement  {
		private Project _project;
		public ProjectElement (final Element element){
			this._project = new Project ();
			this._project.setName (element.getChild (XMLResources.NAME_PROPERTY).getValue ());
			this._project.setDescription (element.getChild (XMLResources.DESCRIPTION_PROPERTY).getValue ());
			this._project.setNotes (element.getChild (XMLResources.NOTES_PROPERTY).getValue ());
			
			
			this._project.setRoot (new ProgressItemElement (element.getChild (XMLResources.PROGRESSITEM_ELEMENT), this._project).getProgressItem ());
		}
		
		public Project getProject (){
			return this._project;
		}
	}
	
	private final class ProgressElement extends Element {
		private Progress _progress;
		public ProgressElement (final Element element, final ProgressItem progressItem){
			try {
				this._progress = new Progress (
					CalendarUtils.timestamp2Date (
					element.getChild (XMLResources.FROM_PROPERTY).getValue ()),
					CalendarUtils.timestamp2Date (
					element.getChild (XMLResources.TO_PROPERTY).getValue ()),
					progressItem);
			} catch (ParseException pe){
				throw new RuntimeException (pe);
			}
			this._progress.setDescription (element.getChild (XMLResources.DESCRIPTION_PROPERTY).getValue ());
			this._progress.setNotes (element.getChild (XMLResources.NOTES_PROPERTY).getValue ());
			
		}
		public Progress getProgress (){
			return this._progress;
		}
	}
	
	private final class NullableSingleValueElement extends Element {
		public NullableSingleValueElement (final String type, final String value){
			super (type);
			if (value!=null){
				setText (value);
			}
		}
	}	
}
