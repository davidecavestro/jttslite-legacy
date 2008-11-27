/*
 * TaskListExtractor.java
 *
 * Created on 25 marzo 2008, 19.41
 */

package com.davidecavestro.timekeeper.report.flavors;

import com.davidecavestro.common.application.ApplicationData;
import com.davidecavestro.common.util.CalendarUtils;
import com.davidecavestro.timekeeper.ApplicationContext;
import com.davidecavestro.timekeeper.gui.CustomizableFormat;
import com.davidecavestro.timekeeper.model.Task;
import com.davidecavestro.timekeeper.report.AbstractDataExtractor;
import com.davidecavestro.timekeeper.report.filter.Target;
import com.davidecavestro.timekeeper.report.filter.TargetedFilterContainer;
import com.ost.timekeeper.model.Progress;
import com.ost.timekeeper.util.Duration;
import com.ost.timekeeper.util.LocalizedPeriod;
import com.ost.timekeeper.util.LocalizedPeriodImpl;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Estrae i deti per il report "lista attivit&agrave;".
 *
 * @author  davide
 */
public final class TaskListExtractor extends AbstractDataExtractor {
	/**
	 * Identificatore dell'attributo <TT>FROM</TT> in qualita' di obiettivo di un filtro.
	 */
	public final static Target PROGRESS_FROM = new Target (){};
	
	/**
	 * Identificatore dell'attributo <TT>TO</TT> in qualita' di obiettivo di un filtro.
	 */
	public final static Target PROGRESS_TO = new Target (){};
	
	/**
	 * La radice del sottoalbero di interesse.
	 */
	private final Task _subtreeRoot;
	
	/**
	 * Il numero di giorni dilunghezza del periodo
	 */
	private final int _periodLength;
	
	/**
	 * Il numero di periodi di interesse
	 */
	private final int _periodCount;
	
	private final Date _date;

	private final ApplicationContext _context ;
	
	/**
	 * Costruttore.
	 * @param date la data di inizio del primo periodo
	 * @param periodLength la lunghezza (in giorni) del periodo
	 * @param periodCount il numero di periodi
	 * @param filters i filtri da applicare.
	 * @param subtreeRoot la radice del sottoalbero di interesse per il report.
	 */
	public TaskListExtractor (final ApplicationContext context, final Task subtreeRoot, final TargetedFilterContainer[] filters, final Date date, final int periodLength, final int periodCount) {
		super (filters);
		this._subtreeRoot = subtreeRoot;
		this._date=date;
		this._periodLength = periodLength;
		this._periodCount = periodCount;
		_context = context;
	}
	
	/**
	 * Ritorna la radice del sottoalbero di interesse per il report.
	 *
	 * @return la radice del sottoalbero di interesse per il report.
	 */
	public Task getSubtreeRoot (){
		return this._subtreeRoot;
	}
	
	/**
	 * Estrae e ritorna i dati per il report.
	 *
	 * @return i dati per il report.
	 */
	public Collection extract () {
		final ApplicationData applicationData = _context.getApplicationData ();
		
		final List localProgresses = new ArrayList ();
		final List subtreeProgresses = new ArrayList ();
		
		final Calendar now = new GregorianCalendar ();
		
		
		if (this._date!=null){
			now.setTime (this._date);
		}
		now.set (Calendar.HOUR_OF_DAY, 0);
		now.set (Calendar.MINUTE, 0);
		now.set (Calendar.SECOND, 0);
		now.set (Calendar.MILLISECOND, 0);
		//		now.roll (Calendar.DATE, 1);
		final Date periodStartDate = new Date (now.getTime ().getTime ());
		
		now.add (Calendar.DAY_OF_YEAR, 1*_periodLength*_periodCount);
		final Date periodFinishDate = new Date (now.getTime ().getTime ());
		
		final CumulationPeriod cumulationPeriod = new CumulationPeriod (periodStartDate, periodFinishDate);
		
		final Task root = this._subtreeRoot;
		
		for (final Iterator it = root.getSubtreeProgresses ().iterator ();it.hasNext ();){
			final Progress progress = (Progress)it.next ();
			
			if (match (PROGRESS_FROM, progress.getFrom ()) 
				&& match (PROGRESS_TO, progress.getTo ())){
				/* filtri superati */
				cumulationPeriod.computeProgress (progress);
			}
		}
		
		final Collection<TaskListRowBean> data = new ArrayList<TaskListRowBean> ();
			
		for (final Iterator<Task> nodeIterator = cumulationPeriod.iterateProgressItems ();nodeIterator.hasNext ();){
			final Task progressItem = nodeIterator.next ();
			final NodeProgresses detail = cumulationPeriod.getDetail (progressItem);

			final long taskTotalEffort = (long)detail.getDuration ();

			final String taskName = progressItem.getName ();
			/* Gerarchia */
			final StringBuilder hierarchyData = new StringBuilder ();
			Task parent = progressItem.getParent ();
			while (parent!=null) {
				final StringBuilder ancestorData = new StringBuilder ();
				ancestorData.append ("/");
				final String code = parent.getCode ();
				if (code!=null && code.length ()>0){
					ancestorData.append (code).append (" - ");
				}
				ancestorData.append (parent.getName ());
				hierarchyData.insert (0, ancestorData);
				parent = parent.getParent ();
			}

			final String taskHierarchy = hierarchyData.toString ();

			final TaskListRowBean row = new TaskListRowBean ();

			data.add (row);

			row.setDuration (taskTotalEffort);

			row.setTaskHierarchy (taskHierarchy);
			row.setTaskName (taskName);
			row.setTaskDescription (taskName);

			for (final Iterator<CumulationPeriodNodeProgress> detailIterator = detail.iterateProgresses ();detailIterator.hasNext ();){
				final CumulationPeriodNodeProgress nodeProgress = detailIterator.next ();
				final Progress progress = nodeProgress.getProgress ();

				if (progress.getDescription ()!=null && progress.getDescription ().length ()>0) {
					row.setActionsNotes ((row.getActionsNotes ()!=null?row.getActionsNotes () + "\n":"") + progress.getDescription ());
				}
			}
		}
		
		return data;
	}
	
	/**
	 * Ritorna una rappresentazion ein formato stringa di questo estrattore.
	 *
	 * @return una stringa che rappresenta questo estrattore di dati.
	 */
	public String toString (){
		final StringBuilder sb = new StringBuilder ();
		sb.append (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("_subtree_root:_"));
		sb.append (this._subtreeRoot);
		return sb.toString ();
	}
	
	private String getDurationLabel (Duration duration){
		if (duration==null){
			duration = Duration.ZERODURATION;
		}
		final StringBuilder sb = new StringBuilder ();

		sb.append (durationNumberFormatter.format (duration.getTotalHours ()))
		.append (":")
		.append (durationNumberFormatter.format (duration.getMinutes ()))
		.append (":")
		.append (durationNumberFormatter.format (duration.getSeconds ()));
		return sb.toString ();
	}
	
	
	/**
	 * Un periodo.
	 */
	private final class CumulationPeriod extends LocalizedPeriodImpl implements Comparable {
		private final Map<Task, NodeProgresses> _map;
		
		public CumulationPeriod (final Date from, final Date to){
			super (from, to);
			if (null==from){
				throw new IllegalArgumentException (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Invalid_'from'_value:_")+from);
			}
			if (null==to){
				throw new IllegalArgumentException (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Invalid_'to'_value:_")+to);
			}
			_map = new HashMap<Task, NodeProgresses> ();
		}
		
		public int compareTo (Object o) {
			return compareToStart ((CumulationPeriod)o);
		}
		
		public void computeProgress ( final Progress progress ){
			if (!this.intersects (progress)){
				return;
			}
			final LocalizedPeriod intersection = this.intersection (progress);
			final Task progressItem = progress.getTask ();
			final double duration = intersection.getDuration ().getTime ();
			
			NodeProgresses detail = _map.get (progressItem);
			if (detail==null){
				detail = new NodeProgresses ();
				_map.put (progressItem, detail);
			}
			detail.addProgress (progress, intersection);
		}
		
		/**
		 * Ritorna l'iteratore sui nodi implicati.
		 *
		 * @return l'iteratore sui nodi implicati.
		 */
		public Iterator<Task> iterateProgressItems (){
			final List<Task> l = new ArrayList<Task> ();
			l.addAll (_map.keySet ());
			Collections.sort (l, new java.util.Comparator<Task> () {
				public int compare (Task o1, Task o2) {
					return o1.getName ().compareToIgnoreCase (o2.getName ());
				}
				public boolean equals (Object object) {
					return this==object;
				}
			});
			return l.iterator ();
		}
		
		public NodeProgresses getDetail (final Task progressItem){
			return _map.get (progressItem);
		}
	}
	
	/**
	 * Avanzamenti di interesse (nel periodo) per un nodo.
	 */
	private final class NodeProgresses {

		private double _duration = 0;
		private final List<CumulationPeriodNodeProgress> _nodeProgresses = new ArrayList<CumulationPeriodNodeProgress> ();
		public NodeProgresses (){}
		public void addProgress (final Progress progress, final LocalizedPeriod periodOfInterest){
			final double duration = periodOfInterest.getDuration ().getTime ();
			_duration += duration;
			_nodeProgresses.add (new CumulationPeriodNodeProgress (progress, periodOfInterest));
		}
		
		public double getDuration (){
			return this._duration;
		}
		
		public Iterator<CumulationPeriodNodeProgress> iterateProgresses (){
			return _nodeProgresses.iterator ();
		}
	}
	
	/**
	 * Avanzamento con durata di interesse associata (nel periodo).
	 */
	private final class CumulationPeriodNodeProgress {
		private final Progress _progress;
		private final LocalizedPeriod _periodOfInterest;
		
		public CumulationPeriodNodeProgress (final Progress progress, final LocalizedPeriod periodOfInterest){
			this._progress = progress;
			this._periodOfInterest = periodOfInterest;
		}
		
		public Progress getProgress (){
			return this._progress;
		}
		
		public LocalizedPeriod getPeriodOfInterest (){
			return this._periodOfInterest;
		}
	}
	
	private final DurationNumberFormatter durationNumberFormatter = new DurationNumberFormatter ();
	private static class DurationNumberFormatter extends DecimalFormat {
		public DurationNumberFormatter (){
			this.setMinimumIntegerDigits (2);
		}
	}
	

	private class IdentificatorAssigner {
		int periodCounter = 0;
		int taskCounter = 0;
		
		final Map<Task, Integer> taskIDMap = new HashMap<Task, Integer> ();

		public int assignPeriodID () {
			return ++periodCounter;
		}
		
		public int currentPeriodID () {
			return periodCounter;
		}
		
		public int assignTaskID (final Task t){
			taskIDMap.put (t, Integer.valueOf (taskCounter++));
			return taskCounter;
		}
		
		public int getTaskID (final Task t){
			if (taskIDMap.containsKey (t)) {
				return taskIDMap.get (t);
			} else {
				return assignTaskID (t);
			}
		}
	}
	
}
