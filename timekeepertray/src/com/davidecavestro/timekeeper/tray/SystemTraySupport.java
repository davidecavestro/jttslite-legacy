/*
 * SystemTraySupport.java
 *
 * Created on November 7, 2007, 7:22 PM
 *
 */

package com.davidecavestro.timekeeper.tray;

import com.davidecavestro.timekeeper.model.PieceOfWork;
import com.davidecavestro.timekeeper.model.Task;
import com.ost.timekeeper.util.Duration;
import com.ost.timekeeper.util.DurationUtils;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 * Fornisce metodi di utilit&agrave; per la gestione della tray icon.
 * Rende trasparente il controllo di abilitazione del supporto alla tray icon da parte dell'ambiente grafico (potrebbe non supportarla ).
 * Si occupa, inoltre, di disaccoppiare l'applicazione dalle librerie digestione della SystemTray, disponibili solamente da Java 6.
 * In questo modo l'applicazione rimane compatibile com Java 5 (ma senza tray icon).
 *
 * @author Davide Cavestro
 */
public class SystemTraySupport {
	
	private PieceOfWork _advancingPOW;
	private Task _selectedTask;
	
	private final Object _context;
	
	/**
	 * Costruttore.
	 */
	public SystemTraySupport (final Object context) {
		_context = context;
	}
	
	
	Object trayIcon;
	
	final Image workImage = Toolkit.getDefaultToolkit ().getImage (getClass ().getResource ("/com/davidecavestro/timekeeper/gui/images/small/clock.png"));
	final Image pauseImage = Toolkit.getDefaultToolkit ().getImage (getClass ().getResource ("/com/davidecavestro/timekeeper/gui/images/small/clock_red.png"));
	
	/**
	 * Registra l'icona nella system tray.
	 *
	 *
	 * @return l'esito della registrazione.
	 * @param pow l'azione correntemente attiva. Pu&ograve; essere nulla.
	 * @param popup il menu da associare alla tray icon. Pu&ograve; essere nullo.
	 * @throws java.lang.ClassNotFoundException in caso di utilizo con una versionedi Java anteriore alla 6
	 */
	public boolean register (final PopupMenu popup, final PieceOfWork pow) throws ClassNotFoundException, NoClassDefFoundError {
		
		if (SystemTray.isSupported ()) {
			
			final SystemTray tray = SystemTray.getSystemTray ();
			
			trayIcon = new CustomTrayIcon (pauseImage, null, popup, this);
			
			setWorking (pow);
			
			getTrayIcon ().setImageAutoSize (true);
			
			try {
				tray.add (getTrayIcon ());
				getTrayIcon ().setToolTip ("foo");
			} catch (final AWTException e) {
				System.out.println ("TrayIcon is not supported.");
				e.printStackTrace (System.out);
			}
			
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Imposta un nuovo enu per la tray icon.
	 *
	 * @param popup ilnuovo menu.
	 */
	public void setMenu (final PopupMenu popup) {
		try {
			if (getTrayIcon ()==null) {
				return;
			}
		} catch (final NoClassDefFoundError ncdfe) {
			/*
			 * Supporto java 5
			 */
			return;
		}
		getTrayIcon ().setPopupMenu (popup);
	}
	
	/**
	 * Ritorna il menu associato alla tray icon.
	 *
	 * @return il menu associato alla tray icon.
	 */
	public PopupMenu getMenu () {
		try {
			if (getTrayIcon ()==null) {
				return null;
			}
		} catch (final NoClassDefFoundError ncdfe) {
			/*
			 * SUpporto java 5
			 */
			return null;
		}
		return getTrayIcon ().getPopupMenu ();
	}
	
	/**
	 * Imposta un nuovo tooltip.
	 * @param text il nuovo tooltip.
	 */
	public void setTooltip (final String text) {
		try {
			if (getTrayIcon ()==null) {
				return;
			}
		} catch (final NoClassDefFoundError ncdfe) {
			/*
			 * SUpporto java 5
			 */
			return;
		}
		getTrayIcon ().setToolTip (text);
	}
	
	/**
	 * Imposta l'attivit&agrave; in corso. Il valore <TT>null</TT> va usato per indicare che non cisono attivit&agrave; in corso.
	 * @param t l'attivit&agrave; correntemente in fase di misurazione.
	 */
	public void setWorking (final PieceOfWork spow) {
		
		try {
			if (getTrayIcon ()==null) {
				return;
			}
		} catch (final NoClassDefFoundError ncdfe) {
			/*
			 * SUpporto java 5
			 */
			return;
		}
		_advancingPOW = spow;
		if (spow!=null) {
			getTrayIcon ().setImage (workImage);
		} else {
			getTrayIcon ().setImage (pauseImage);
		}
		
		refreshTooltip ();
	}
	
	private void refreshTooltip () {
		SwingUtilities.invokeLater (new Runnable () {
			public void run () {
				/*
				 * @workaround invoca in modo asincrono la modifica tooltip
				 */
				final PieceOfWork pow = getRunningAction ();
				if (pow!=null) {
					setTooltip (MessageFormat.format (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("TrayIcon/Tooltip/Working"), pow.getTask ().getName (), DurationUtils.format (new Duration (pow.getFrom (), new Date ()))));
				} else {
					final Task selectedTask = getSelectedTask ();
					if (selectedTask!=null) {
						setTooltip (MessageFormat.format (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("TrayIcon/Tooltip/Pause_on_task"), selectedTask.getName ()));
					} else {
						setTooltip (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("TrayIcon/Tooltip/Pause"));
					}
				}
			}
		});
	}
	
	
	/**
	 * Mostra un messaggio dierrore.
	 *
	 * @param caption il titolo delmessaggio.
	 * @param message il testo del messaggio.
	 */
	public void showError (final String caption, final String message) {
		try {
			if (getTrayIcon ()==null) {
				return;
			}
		} catch (final NoClassDefFoundError ncdfe) {
			/*
			 * SUpporto java 5
			 */
			return;
		}
		getTrayIcon ().displayMessage (caption, message, TrayIcon.MessageType.ERROR);
	}
	/**
	 * Mostra un messaggio di info.
	 *
	 * @param caption il titolo delmessaggio.
	 * @param message il testo del messaggio.
	 */
	public void displayInfo (final String caption, final String message) {
		try {
			if (getTrayIcon ()==null) {
				return;
			}
		} catch (final NoClassDefFoundError ncdfe) {
			/*
			 * SUpporto java 5
			 */
			return;
		}
		getTrayIcon ().displayMessage (caption, message, TrayIcon.MessageType.INFO);
	}
	/**
	 * Mostra un messaggio di warning.
	 *
	 * @param caption il titolo delmessaggio.
	 * @param message il testo del messaggio.
	 */
	public void displayWarning (final String caption, final String message) {
		try {
			if (getTrayIcon ()==null) {
				return;
			}
		} catch (final NoClassDefFoundError ncdfe) {
			/*
			 * SUpporto java 5
			 */
			return;
		}
		getTrayIcon ().displayMessage (caption, message, TrayIcon.MessageType.WARNING);
	}
	/**
	 * Mostra un messaggio.
	 *
	 * @param caption il titolo delmessaggio.
	 * @param message il testo del messaggio.
	 */
	public void displayMessage (final String caption, final String message) {
		try {
			if (getTrayIcon ()==null) {
				return;
			}
		} catch (final NoClassDefFoundError ncdfe) {
			/*
			 * SUpporto java 5
			 */
			return;
		}
		getTrayIcon ().displayMessage (caption, message, TrayIcon.MessageType.NONE);
	}
	
	
	/**
	 * Rimuove la tray icon dalla System Tray.
	 */
	public void release () {
		try {
			if (getTrayIcon ()==null) {
				return;
			}
		} catch (final NoClassDefFoundError ncdfe) {
			/*
			 * SUpporto java 5
			 */
			return;
		}
		final SystemTray tray = SystemTray.getSystemTray ();
		
		tray.remove (getTrayIcon ());
	}
	
	public void addMouseListener (final MouseListener l) {
		try {
			if (getTrayIcon ()==null) {
				return;
			}
		} catch (final NoClassDefFoundError ncdfe) {
			/*
			 * SUpporto java 5
			 */
			return;
		}
		getTrayIcon ().addMouseListener (l);
	}
	
	public void removeMouseListener (final MouseListener l) {
		try {
			if (getTrayIcon ()==null) {
				return;
			}
		} catch (final NoClassDefFoundError ncdfe) {
			/*
			 * SUpporto java 5
			 */
			return;
		}
		getTrayIcon ().removeMouseListener (l);
	}
	
	public void addMouseMotionListener (final MouseMotionListener l) {
		try {
			if (getTrayIcon ()==null) {
				return;
			}
		} catch (final NoClassDefFoundError ncdfe) {
			/*
			 * SUpporto java 5
			 */
			return;
		}
		getTrayIcon ().addMouseMotionListener (l);
	}
	
	public void removeMouseMotionListener (final MouseMotionListener l) {
		try {
			if (getTrayIcon ()==null) {
				return;
			}
		} catch (final NoClassDefFoundError ncdfe) {
			/*
			 * SUpporto java 5
			 */
			return;
		}
		getTrayIcon ().removeMouseMotionListener (l);
	}
	
	
	public PieceOfWork getRunningAction () {
		return _advancingPOW;
	}
	
	public Task getSelectedTask () {
		return _selectedTask;
	}
	
	private final static Object[] voidObjectArray = new Object[0];
	private final static Class[] voidClassArray = new Class[0];
	
	private TrayIconConfigurationAccessor accessor;
	private boolean accessorInitializationFailed = true;
	
	/**
	 * Finche' si vuole supportare Java 5, e' necessario proteggere qualsiasi accesso a TrayIcon con un 
	 * try {
	 * ...
	 * } catch (NoClassDefFoundError )
	 */
	private TrayIcon getTrayIcon () throws NoClassDefFoundError {
		
		boolean isTrayIconEnabled = true;
		
		try {
			if (accessor == null) {
				accessor = new TrayIconConfigurationAccessor ();
				accessorInitializationFailed = false;
			}
			if (!accessorInitializationFailed) {
				isTrayIconEnabled = accessor.getValue ();
			}
		} catch (final IllegalAccessException ex) {
			ex.printStackTrace (System.err);
		} catch (final IllegalArgumentException ex) {
			ex.printStackTrace (System.err);
		} catch (final InvocationTargetException ex) {
			ex.printStackTrace (System.err);
		} catch (final NoSuchMethodException ex) {
			ex.printStackTrace (System.err);
		} catch (final SecurityException ex) {
			ex.printStackTrace (System.err);
		}
		
		if (isTrayIconEnabled) {
			return (TrayIcon)trayIcon;
		}
		
		return null;
	}

	/**
	 * Imposta il correntemente selezionato.
	 * Serve per poter mostrare neitooltip il nome deltask selezionato (quello su cuieventualmente sifa partire un'azione)
	 */
	public void setSelected (final Task t) {
		_selectedTask = t;
		refreshTooltip ();
	}

//	public void showMenu () {
//		if (getTrayIcon ()==null) {
//			return;
//		}
////		getTrayIcon ().getPopupMenu ().dispatchEvent (new MouseEvent ());
//	}
	
	/*
	 * @workaround usa la reflection per rimuovere dipendenze di comilazione verso ApplicationOptions (progetto Java5 vs Java6)
	 */
	private class TrayIconConfigurationAccessor {
		
		private final Method applicationOptionsGetter;

		private final Object applicationOptions;

		private final Method trayIconEnabledGetter;
			
		public TrayIconConfigurationAccessor () throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, IllegalArgumentException, InvocationTargetException {
			
			applicationOptionsGetter = _context.getClass ().getMethod ("getApplicationOptions", voidClassArray);

			applicationOptions = applicationOptionsGetter.invoke (_context, voidObjectArray);

			trayIconEnabledGetter = applicationOptions.getClass ().getMethod ("isTrayIconEnabled", voidClassArray);
		}
		
			
		public boolean getValue () throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

			return ((Boolean)trayIconEnabledGetter.invoke (applicationOptions, voidObjectArray)).booleanValue ();
			
		}
	}
	
}
