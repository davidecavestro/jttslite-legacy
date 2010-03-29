/*
 * SystemTraySupport.java
 *
 * Created on November 7, 2007, 7:22 PM
 *
 */

package net.sf.jttslite.tray;

import net.sf.jttslite.core.model.PieceOfWork;
import net.sf.jttslite.core.model.Task;
import net.sf.jttslite.core.util.Duration;
import net.sf.jttslite.core.util.DurationUtils;
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
    Object trayIcon;
	final Image workImage = Toolkit.getDefaultToolkit ().getImage (getClass ().getResource ("/net/sf/jttslite/gui/images/trayicon_on_work.png"));
	final Image pauseImage = Toolkit.getDefaultToolkit ().getImage (getClass ().getResource ("/net/sf/jttslite/gui/images/trayicon_on_pause.png"));
	private final static Object[] voidObjectArray = new Object[0];
	private final static Class[] voidClassArray = new Class[0];
	private TrayIconConfigurationAccessor accessor;
	private boolean accessorInitializationFailed = true;
	
	/**
	 * Costruttore.
	 *
	 * @param context 
	 */
	public SystemTraySupport (final Object context) {
		_context = context;
	}
	
	/**
	 * Registra l'icona nella system tray.
	 *
	 *
	 * @return l'esito della registrazione.
	 * @param pow l'azione correntemente attiva. Pu&ograve; essere nulla.
	 * @param popup il menu da associare alla tray icon. Pu&ograve; essere nullo.
	 */
	public boolean register (final PopupMenu popup, final PieceOfWork pow){
		
		if (SystemTray.isSupported ()) {
			
			final SystemTray tray = SystemTray.getSystemTray ();
			
			trayIcon = new CustomTrayIcon (pauseImage, null, popup, this);
			
			setWorking (pow);
			
			getTrayIcon ().setImageAutoSize (true);
			
			try {
				tray.add (getTrayIcon ());
				getTrayIcon ().setToolTip ("foo");
			} catch (final AWTException e) {
				System.out.println ("TrayIcon is supported but is not working properly.");
				e.printStackTrace (System.out);
			}
			
			
			return true;
		} else {
			System.out.println ("TrayIcon is not supported.");
		}
		
		return false;
	}
	
    /**
     * Imposta un nuovo enu per la tray icon.
     *
     * @param popup ilnuovo menu.
     */
    public void setMenu(final PopupMenu popup) {
        if (getTrayIcon() == null) {
            return;
        }
        getTrayIcon().setPopupMenu(popup);
    }

    /**
     * Ritorna il menu associato alla tray icon.
     *
     * @return il menu associato alla tray icon.
     */
    public PopupMenu getMenu() {
        if (getTrayIcon() == null) {
            return null;
        }
        return getTrayIcon().getPopupMenu();
    }

    /**
     * Imposta un nuovo tooltip.
     * @param text il nuovo tooltip.
     */
    public void setTooltip(final String text) {
        if (getTrayIcon() == null) {
            return;
        }
        getTrayIcon().setToolTip(text);
    }
	
    /**
     * Imposta l'attivit&agrave; in corso. Il valore <TT>null</TT> va usato per indicare che non cisono attivit&agrave; in corso.
     * @param spow l'attivit&agrave; correntemente in fase di misurazione.
     */
    public void setWorking(final PieceOfWork spow) {
        if (getTrayIcon() == null) {
            return;
        }
        _advancingPOW = spow;
        if (spow != null) {
            getTrayIcon().setImage(workImage);
        } else {
            getTrayIcon().setImage(pauseImage);
        }

        refreshTooltip();
    }
	
	private void refreshTooltip () {
		SwingUtilities.invokeLater (new Runnable () {
			public void run () {
				/*
				 * @workaround invoca in modo asincrono la modifica tooltip
				 */
				final PieceOfWork pow = getRunningAction ();
				if (pow!=null) {
					setTooltip (MessageFormat.format (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("TrayIcon/Tooltip/Working"), pow.getTask ().getName (), DurationUtils.format (new Duration (pow.getFrom (), new Date ()))));
				} else {
					final Task selectedTask = getSelectedTask ();
					if (selectedTask!=null) {
						setTooltip (MessageFormat.format (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("TrayIcon/Tooltip/Pause_on_task"), selectedTask.getName ()));
					} else {
						setTooltip (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("TrayIcon/Tooltip/Pause"));
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
    public void showError(final String caption, final String message) {
        if (getTrayIcon() == null) {
            return;
        }
        getTrayIcon().displayMessage(caption, message, TrayIcon.MessageType.ERROR);
    }

    /**
     * Mostra un messaggio di info.
     *
     * @param caption il titolo delmessaggio.
     * @param message il testo del messaggio.
     */
    public void displayInfo(final String caption, final String message) {
        if (getTrayIcon() == null) {
            return;
        }
        getTrayIcon().displayMessage(caption, message, TrayIcon.MessageType.INFO);
    }

    /**
     * Mostra un messaggio di warning.
     *
     * @param caption il titolo delmessaggio.
     * @param message il testo del messaggio.
     */
    public void displayWarning(final String caption, final String message) {
        if (getTrayIcon() == null) {
            return;
        }
        getTrayIcon().displayMessage(caption, message, TrayIcon.MessageType.WARNING);
    }

    /**
     * Mostra un messaggio.
     *
     * @param caption il titolo delmessaggio.
     * @param message il testo del messaggio.
     */
    public void displayMessage(final String caption, final String message) {
        if (getTrayIcon() == null) {
            return;
        }
        getTrayIcon().displayMessage(caption, message, TrayIcon.MessageType.NONE);
    }

    /**
     * Rimuove la tray icon dalla System Tray.
     */
    public void release() {
        if (getTrayIcon() == null) {
            return;
        }
        final SystemTray tray = SystemTray.getSystemTray();

        tray.remove(getTrayIcon());
    }

    public void addMouseListener(final MouseListener l) {
        if (getTrayIcon() == null) {
            return;
        }
        getTrayIcon().addMouseListener(l);
    }

    public void removeMouseListener(final MouseListener l) {
        if (getTrayIcon() == null) {
            return;
        }
        getTrayIcon().removeMouseListener(l);
    }

    public void addMouseMotionListener(final MouseMotionListener l) {
        if (getTrayIcon() == null) {
            return;
        }
        getTrayIcon().addMouseMotionListener(l);
    }

    public void removeMouseMotionListener(final MouseMotionListener l) {
        if (getTrayIcon() == null) {
            return;
        }
        getTrayIcon().removeMouseMotionListener(l);
    }
	
	
	public PieceOfWork getRunningAction () {
		return _advancingPOW;
	}
	
	public Task getSelectedTask () {
		return _selectedTask;
	}
	
	/**
	 * Finche' si vuole supportare Java 5, e' necessario proteggere
         * qualsiasi accesso a TrayIcon con un
	 * 
	 */
	private TrayIcon getTrayIcon (){
		
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
	 * Imposta il task correntemente selezionato.
	 * Serve per poter mostrare nei tooltip il nome del task selezionato (quello su cui
	 * eventualmente si fa partire un'azione)
	 *
	 * @param t task correntemente selezionato
	 */
	public void setSelected (final Task t) {
		_selectedTask = t;
		refreshTooltip ();
	}
	
	/*
	 * @workaround usa la reflection per rimuovere dipendenze di compilazione
	 * verso ApplicationOptions
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
