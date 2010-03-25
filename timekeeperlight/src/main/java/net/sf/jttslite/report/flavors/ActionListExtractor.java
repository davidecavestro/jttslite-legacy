package net.sf.jttslite.report.flavors;

import net.sf.jttslite.common.application.ApplicationData;
import net.sf.jttslite.ApplicationContext;
import net.sf.jttslite.core.model.Task;
import net.sf.jttslite.report.AbstractDataExtractor;
import net.sf.jttslite.report.filter.Target;
import net.sf.jttslite.report.filter.TargetedFilterContainer;
import net.sf.jttslite.core.model.impl.Progress;
import net.sf.jttslite.core.util.Duration;
import net.sf.jttslite.core.util.LocalizedPeriod;
import net.sf.jttslite.core.util.LocalizedPeriodImpl;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Estrae i deti per il report "lista piana azioni".
 *
 * @author  davide
 */
public final class ActionListExtractor extends AbstractDataExtractor {

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
	private final ApplicationContext _context;

	/**
	 * Costruttore.
	 * @param date la data di inizio del primo periodo
	 * @param periodLength la lunghezza (in giorni) del periodo
	 * @param periodCount il numero di periodi
	 * @param filters i filtri da applicare.
	 * @param subtreeRoot la radice del sottoalbero di interesse per il report.
	 */
	public ActionListExtractor(final ApplicationContext context, final Task subtreeRoot, final TargetedFilterContainer[] filters, final Date date, final int periodLength, final int periodCount) {
		super(filters);
		this._subtreeRoot = subtreeRoot;
		this._date = date;
		this._periodLength = periodLength;
		this._periodCount = periodCount;
		_context = context;
	}

	/**
	 * Ritorna la radice del sottoalbero di interesse per il report.
	 *
	 * @return la radice del sottoalbero di interesse per il report.
	 */
	public Task getSubtreeRoot() {
		return this._subtreeRoot;
	}

	/**
	 * Estrae e ritorna i dati per il report.
	 *
	 * @return i dati per il report.
	 */
	public Collection extract() {

		final Task root = this._subtreeRoot;

		final Collection<ActionListRowBean> data = new ArrayList<ActionListRowBean>();

		for (final Iterator it = root.getSubtreeProgresses().iterator(); it.hasNext();) {
			final Progress progress = (Progress) it.next();
			if (progress.isEndOpened()) {
				/*
				 * azione in corso, va esclusa
				 */
				continue;
			}

			if (match(Target.PROGRESS_FROM, progress.getFrom()) && match(Target.PROGRESS_TO, progress.getTo())) {

				/* filtri superati */
				final String taskName = progress.getTask().getName();
				/* Gerarchia */
				final StringBuilder hierarchyData = new StringBuilder();
				Task parent = progress.getTask().getParent();
				while (parent != null) {
					final StringBuilder ancestorData = new StringBuilder();
					ancestorData.append("/");
					final String code = parent.getCode();
					if (code != null && code.length() > 0) {
						ancestorData.append(code).append(" - ");
					}
					ancestorData.append(parent.getName());
					hierarchyData.insert(0, ancestorData);
					parent = parent.getParent();
				}

				final String taskHierarchy = hierarchyData.toString();

				final ActionListRowBean row = new ActionListRowBean();

				data.add(row);

				row.setDuration(progress.getDuration().getTime());
				row.setStartDate (new Timestamp (progress.getFrom ().getTime ()));
				row.setFinishDate (new Timestamp (progress.getTo ().getTime ()));

				row.setTaskHierarchy(taskHierarchy);
				row.setTaskName(taskName);
				row.setTaskDescription(progress.getTask().getDescription());

				if (progress.getDescription() != null && progress.getDescription().length() > 0) {
					row.setActionNotes((row.getActionNotes() != null ? row.getActionNotes() + "\n" : "") + progress.getDescription());
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
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("_subtree_root:_"));
		sb.append(this._subtreeRoot);
		return sb.toString();
	}

	private String getDurationLabel(Duration duration) {
		if (duration == null) {
			duration = Duration.ZERODURATION;
		}
		final StringBuilder sb = new StringBuilder();

		sb.append(durationNumberFormatter.format(duration.getTotalHours())).append(":").append(durationNumberFormatter.format(duration.getMinutes())).append(":").append(durationNumberFormatter.format(duration.getSeconds()));
		return sb.toString();
	}

	private final DurationNumberFormatter durationNumberFormatter = new DurationNumberFormatter();

	private static class DurationNumberFormatter extends DecimalFormat {

		public DurationNumberFormatter() {
			this.setMinimumIntegerDigits(2);
		}
	}

	private class IdentificatorAssigner {

		int periodCounter = 0;
		int taskCounter = 0;
		final Map<Task, Integer> taskIDMap = new HashMap<Task, Integer>();

		public int assignPeriodID() {
			return ++periodCounter;
		}

		public int currentPeriodID() {
			return periodCounter;
		}

		public int assignTaskID(final Task t) {
			taskIDMap.put(t, Integer.valueOf(taskCounter++));
			return taskCounter;
		}

		public int getTaskID(final Task t) {
			if (taskIDMap.containsKey(t)) {
				return taskIDMap.get(t);
			} else {
				return assignTaskID(t);
			}
		}
	}
}
