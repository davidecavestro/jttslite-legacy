/*
 * AdvancingTicPump.java
 *
 * Created on November 29, 2006, 11:25 PM
 *
 */

package net.sf.jttslite.gui;

import net.sf.jttslite.model.TaskTreeModelImpl;
import net.sf.jttslite.core.model.event.WorkAdvanceModelEvent;
import net.sf.jttslite.core.model.event.WorkAdvanceModelListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

/**
 * Sincronizza la notifica di avanzamento dei progress.
 *
 * @author Davide Cavestro
 */
public class AdvancingTicPump implements WorkAdvanceModelListener {
	
	private final  TaskTreeModelImpl _model;
	
	private Timer t;
		
	private final int _period;
	
	/**
	 * Costruttore.
	 * @param model il modello.
	 * @param period il periodo di notifica, in millisecondi.
	 */
	public AdvancingTicPump (final TaskTreeModelImpl model, final int period) {
		_model = model;
		_period = period;
		somethingHappened ();
	}

	public void elementsInserted (WorkAdvanceModelEvent e) {
		somethingHappened ();
	}

	public void elementsRemoved (WorkAdvanceModelEvent e) {
		somethingHappened ();
	}
	
	private void somethingHappened () {
		if (_model.getAdvancing ().isEmpty ()) {
			stop ();
		} else {
			start ();
		}
		
	}
	
	
	EventListenerList listenerList = new EventListenerList ();
	AdvancingTicEvent ticEvent = null;
	
	public void addAdvancingTicListener (AdvancingTicListener l) {
		listenerList.add (AdvancingTicListener.class, l);
	}
	
	public void removeAdvancingTicListener (AdvancingTicListener l) {
		listenerList.remove (AdvancingTicListener.class, l);
	}
	
	
// Notify all listeners that have registered interest for
// notification on this event type.  The event instance
// is lazily created using the parameters passed into
// the fire method.
	
	protected void fireAdvancingTic () {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList ();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==AdvancingTicListener.class) {
				// Lazily create the event:
				if (ticEvent == null) {
					ticEvent = new AdvancingTicEvent (_period);
				}
				((AdvancingTicListener)listeners[i+1]).advanceTic (ticEvent);
			}
		}
	}
 
	private void start () {
		t = new Timer ("advancing-tic-pump", true);
		t.schedule (
			new TimerTask () {
			    public void run() {
					SwingUtilities.invokeLater (new Runnable () {
						public void run () {
							/*
							 * @workaround invoca in modo asincrono la modifica tooltip
							 */
							fireAdvancingTic ();
						}});
				}
			}
			, 0
			, _period
		);		
	}
	
	private void stop () {
		if (t!=null) {
			t.cancel ();
			t = null;
		}
	}
	
	/**
	 * Ferma il timer di notifica. DOpo l'invocazione di questo metodo l'istanza non &egrave; pi&ugrave; utilizzabile.
	 */
	public void release () {
		stop ();
	}
}
