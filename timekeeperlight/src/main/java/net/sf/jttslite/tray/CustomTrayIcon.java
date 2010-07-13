/*
 * Created on November 12, 2007, 12:39 PM
 *
 */

package net.sf.jttslite.tray;

import net.sf.jttslite.core.model.PieceOfWork;
import net.sf.jttslite.core.util.DurationImpl;
import net.sf.jttslite.core.util.DurationUtils;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.TrayIcon;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.LogManager;

/**
 * Tray icon dedicata all'applicazione.
 *
 * @author Davide Cavestro
 */
public class CustomTrayIcon extends TrayIcon {
	
	private final SystemTraySupport _systemTraySupport;
	
	public CustomTrayIcon (final Image image, final String tooltip, final PopupMenu popup, final SystemTraySupport sts) {
		super (image, tooltip, popup);
		_systemTraySupport = sts;
	}

	@Override
	public String getToolTip () {
		final PieceOfWork pow =_systemTraySupport.getRunningAction ();
		if (pow!=null) {
			LogManager.getLogManager ().getLogger ("net.sf.jtts").warning ("Using pow: "+pow);
			LogManager.getLogManager ().getLogger ("net.sf.jtts").warning ("Using pow.getTask(): "+pow.getTask ());

			return MessageFormat.format (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("TrayIcon/Tooltip/Working"), pow.getTask ().getName (), DurationUtils.format (new DurationImpl (pow.getFrom (), new Date ())));
		} else {
			return java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("TrayIcon/Tooltip/Pause");
		}
	}
}
