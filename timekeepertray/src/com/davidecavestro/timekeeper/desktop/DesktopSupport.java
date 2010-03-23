/*
 * SystemTraySupport.java
 *
 * Created on November 7, 2007, 7:22 PM
 *
 */

package com.davidecavestro.timekeeper.desktop;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 * Fornisce metodi di utilit&agrave; per la gestione del supporto per il desktop (helper applications).
 * 
 * Rende trasparente il controllo di abilitazione del supporto al desktop da parte dell'ambiente grafico (potrebbe non supportarlo ).
 * Si occupa, inoltre, di disaccoppiare l'applicazione dalle librerie digestione del Desktop, disponibili solamente da Java 6.
 * In questo modo l'applicazione rimane compatibile com Java 5 (ma senza supporto al Desktop).
 *
 * @author Davide Cavestro
 */
public class DesktopSupport {
	
	/**
	 * Costruttore.
	 */
	public DesktopSupport () {
	}
	

	/**
	 * Apre un file con l'helper application associata, se disponibile.
	 * @param file
	 * @return
	 * @throws java.lang.ClassNotFoundException
	 * @throws java.lang.NoClassDefFoundError
	 * @throws java.io.IOException
	 */
	public boolean  open (final File file) throws ClassNotFoundException, NoClassDefFoundError, IOException {
		if (Desktop.isDesktopSupported()) {
			
			Desktop.getDesktop ().open(file);
			return true;
		}
		return false;
	}
	
	
}
