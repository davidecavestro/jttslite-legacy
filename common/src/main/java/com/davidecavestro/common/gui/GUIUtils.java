
package com.davidecavestro.common.gui;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

/**
 *  Utilities for GUI components.
 *
 * @author davide
 */
public class GUIUtils {

	
	/**
	 * Add a rollover effect to specified components. if they are instances of {@link javax.swing.JButton}.
	 * @param alltc
	 */
	public static void addBorderRollover (final Component[] alltc) {
		
		for (int i=0;i<alltc.length;i++){
			final Component c = alltc[i];
			if (c instanceof JButton){
				final JButton jb = (JButton)c;
				jb.setBorderPainted (false);
				jb.setOpaque (false);
				jb.addMouseListener (new MouseAdapter (){
					/**
					 * Invoked when the mouse enters a component.
					 */
					public void mouseEntered (final MouseEvent e) {
						jb.setBorderPainted (true);
					}
					
					/**
					 * Invoked when the mouse exits a component.
					 */
					public void mouseExited (final MouseEvent e) {
						jb.setBorderPainted (false);
					}
				});
			}
		}		
	}
}
