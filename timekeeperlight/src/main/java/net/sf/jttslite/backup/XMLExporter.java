/*
 * XMLExportter.java
 *
 * Created on January 2, 2007, 3:22 PM
 *
 */

package net.sf.jttslite.backup;

import net.sf.jttslite.common.util.CalendarUtils;
import net.sf.jttslite.ApplicationContext;
import net.sf.jttslite.core.model.PieceOfWork;
import net.sf.jttslite.core.model.Task;
import net.sf.jttslite.core.model.WorkSpace;
import java.util.Iterator;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;

/**
 * Effettua il backup su file dei dati relativi ad un progetto.
 *
 * @author Davide Cavestro
 */
public class XMLExporter {
	
	final ApplicationContext _context;
	
	/**
	 * Costruttore.
	 */
	public XMLExporter (final ApplicationContext context) {
		_context = context;
	}
	
	/**
	 * Effettua il backup su file dei dati relativi ad un progetto.
	 * Il file generato &egrave; in formato XML.
	 * 
	 * @param project il progetto da salvare.
	 * @param document il documento XML da usare.
	 *
	 * @return <CODE>true</CODE> se il backup /egrave; andato a buon fine.
	 */
	public boolean backup (final WorkSpace project, final Document document) {
		final ProjectElement projectElement = new ProjectElement (project);
		document.setRootElement (projectElement);
		
		return true;
	}
	
	private final class ProgressItemElement extends Element {
		public ProgressItemElement (final Task progressItem){
			super (XMLResources.PROGRESSITEM_ELEMENT);
			
			addContent (new NullableSingleValueElement (XMLResources.CODE_PROPERTY, progressItem.getCode ()));
			addContent (new NullableSingleValueElement (XMLResources.NAME_PROPERTY, progressItem.getName ()));
			addContent (new NullableSingleValueElement (XMLResources.DESCRIPTION_PROPERTY, progressItem.getDescription ()));
			addContent (new NullableSingleValueElement (XMLResources.NOTES_PROPERTY, progressItem.getNotes ()));
			
			for (final Iterator it = progressItem.getChildren ().iterator ();it.hasNext ();){
				final Task child = (Task)it.next ();
				try {
					addContent (new ProgressItemElement (child));
				} catch (Exception e){_context.getLogger ().severe (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("error_exporting_node"));}
			}
			for (final Object progress : progressItem.getPiecesOfWork ()){
				addContent (new ProgressElement ((PieceOfWork)progress));
			}
		}
		
	}
	
	private final class ProjectElement extends Element {
		public ProjectElement (final WorkSpace project){
			super (XMLResources.PROJECT_ELEMENT);
			addContent (new Comment (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Generated_by_")+_context.getApplicationData ().getApplicationInternalName ()+ java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("_v.")+_context.getApplicationData ().getVersionNumber ()+ java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("_build_")+_context.getApplicationData ().getBuildNumber ()));
			
			addContent (new NullableSingleValueElement  (XMLResources.NAME_PROPERTY, project.getName ()));
			addContent (new NullableSingleValueElement  (XMLResources.DESCRIPTION_PROPERTY, project.getDescription ()));
			addContent (new NullableSingleValueElement (XMLResources.NOTES_PROPERTY, project.getNotes ()));
			
			addContent (new ProgressItemElement (project.getRoot ()));
		}
	}
	
	private final class ProgressElement extends Element {
		public ProgressElement (final PieceOfWork progress){
			super (XMLResources.PROGRESS_ELEMENT);
			try {
				addContent (new NullableSingleValueElement (XMLResources.FROM_PROPERTY, CalendarUtils.getTimestamp (progress.getFrom (), CalendarUtils.TIMESTAMP_FORMAT)));
			} catch (Exception e){_context.getLogger ().severe (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("error_exporting_action"));}
			try {
				addContent (new NullableSingleValueElement (XMLResources.TO_PROPERTY, CalendarUtils.getTimestamp (progress.getTo (), CalendarUtils.TIMESTAMP_FORMAT)));
			} catch (Exception e){_context.getLogger ().severe (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("error_exporting_action"));}
			try {
				addContent (new NullableSingleValueElement (XMLResources.DESCRIPTION_PROPERTY, progress.getDescription ()));
			} catch (Exception e){_context.getLogger ().severe (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("error_exporting_action"));}
			try {
				addContent (new NullableSingleValueElement (XMLResources.NOTES_PROPERTY, progress.getNotes ()));
			} catch (Exception e){_context.getLogger ().severe (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("error_exporting_action"));}
			
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
