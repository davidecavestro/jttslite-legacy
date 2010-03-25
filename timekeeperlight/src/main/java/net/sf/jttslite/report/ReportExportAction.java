/*
 * Created on April 8, 2008, 12:11 PM
 *
 */

package net.sf.jttslite.report;

import net.sf.jttslite.common.gui.SwingWorker;
import net.sf.jttslite.common.util.file.CustomFileFilter;
import net.sf.jttslite.common.util.file.FileUtils;
import net.sf.jttslite.ApplicationContext;
import java.awt.Component;
import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;


/**
 * Azione di esportazione report.
 *
 * @author Davide Cavestro
 */
public enum ReportExportAction implements ReportLaunchAction {
	
	CSV {
		public void export (final JasperPrint print, final File f) throws JRException {
			export (new JRCsvExporter (), print, f);
		}

		public String getExt () {
			return "CSV";
		}
		
		public void initFileChooser () {
			_fileChooser.resetChoosableFileFilters ();
			_fileChooser.addChoosableFileFilter (new CustomFileFilter (
				new String []{FileUtils.csv},
				new String []{"CSV files"}
			));
		}
	},
	HTML {
		public void export (final JasperPrint print, final File f) throws JRException {
			JasperExportManager.exportReportToHtmlFile (print, normalizeFile (f).getPath ());
		}

		public String getExt () {
			return "HTML";
		}
		
		public void initFileChooser () {
			_fileChooser.resetChoosableFileFilters ();
			_fileChooser.addChoosableFileFilter (new CustomFileFilter (
				new String []{FileUtils.html},
				new String []{"HTML files"}
			));
		}
	},
	XLS {
		public void export (final JasperPrint print, final File f) throws JRException {
			export (new JRXlsExporter (), print, f);
		}

		public String getExt () {
			return "XLS";
		}
		
		public void initFileChooser () {
			_fileChooser.resetChoosableFileFilters ();
			_fileChooser.addChoosableFileFilter (new CustomFileFilter (
				new String []{FileUtils.xls},
				new String []{"XLS files"}
			));
		}
	},
	PDF {
		public void export (final JasperPrint print, final File f) throws JRException {
			JasperExportManager.exportReportToPdfFile (print, normalizeFile (f).getPath ());
		}

		public String getExt () {
			return "PDF";
		}
		
		public void initFileChooser () {
			_fileChooser.resetChoosableFileFilters ();
			_fileChooser.addChoosableFileFilter (new CustomFileFilter (
				new String []{FileUtils.pdf},
				new String []{"PDF files"}
			));
		}
	}
	;

	
	
	
	
	/**
	 * Esegue l'azione.
	 */
	public abstract void export (JasperPrint print, final File f) throws JRException ;
	
	/** 
	 * Ritorna la stringa che rappresenta l'azione da eseguire.
	 */
	public String getUIName () {
		return getExt ();
	}
	
	/** 
	 * Ritorna l'estensione del file esportato.
	 */
	public abstract String getExt ();
	
	protected final static JFileChooser _fileChooser = new JFileChooser ();

	public String toString () {
		return getUIName ();
	}
	
	final Cursor waiting = new Cursor (Cursor.WAIT_CURSOR);
	
	public void execute (final ApplicationContext context, final Component parent, final JasperPrint print) throws JRException {
		File tempFile = null;
		if (context.getApplicationOptions ().isHelperApplicationIntegrationEnabled ()) {
			try {

				tempFile = File.createTempFile (print.getName(), "." + getExt ().toLowerCase ());
			} catch (IOException ex) {
				/*
				 * 
				 */
			}
			if (tempFile!=null) {
				final Cursor original = parent.getCursor ();
				parent.setCursor(waiting);
					try {

					final File fileToOpen = tempFile;
					final SwingWorker sw = new SwingWorker () {

						@Override
						public Object construct() {
							try {
								export (print, fileToOpen);
								
								/*
								 * @workaround disabilita temporaneamente, in modo che, se c'è un crash, al prossimo riavvio rimanga disabilitato
								 * 
								 */
								context.getUserSettings ().setHelperApplicationsEnabled (Boolean.FALSE);
								context.getUserSettings ().storeProperties ();
								try {
									return Boolean.valueOf (context.getWindowManager ().getDesktopSupport ().open (fileToOpen));
								} finally {
									/*
									 * riabilita 
									 */
									context.getUserSettings ().setHelperApplicationsEnabled (Boolean.TRUE);
								}
								
							} catch (final Exception e) {
								/*
								 * Problema con l'helper application
								 */
							} catch (final NoClassDefFoundError e) {
								/*
								 * Problema con l'helper application
								 */
							}
							return Boolean.FALSE;
						}
					};

					sw.start();
					
					final Boolean opened = (Boolean) sw.get ();
					if (opened!=null && opened.booleanValue ()) {
						return;
					}
				} finally {
					parent.setCursor (original);
				}
			}
		}


		initFileChooser();
		final int result = _fileChooser.showSaveDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			final File f = normalizeFile(_fileChooser.getSelectedFile());
			if (f.exists()) {
				if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(parent, MessageFormat.format(java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("ReportDialog/JOptionPane/Confirm_overwrite_existing_file"), f.getPath()), java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("ReportDialog/JOptionPane/Title/Confirm_overwrite_existing_file"), JOptionPane.YES_NO_OPTION)) {
					return;
				}
			}
			if (tempFile != null && tempFile.exists()) {
				tempFile.renameTo(f);
			} else {
				export(print, f);
			}

			JOptionPane.showMessageDialog(parent, java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("ReportExportMessageDialog/Exporting_completed"));
		}
	}

	protected abstract void initFileChooser ();
	
	
	protected void export (final JRExporter e, final JasperPrint print, final File f) throws JRException {
		e.setParameter (JRExporterParameter.OUTPUT_FILE_NAME, normalizeFile (f).getPath ());
		e.setParameter (JRExporterParameter.JASPER_PRINT, print);
		e.exportReport ();
	}
	
	/**
	 * Aggiunge l'estensione al file, se necessario.
	 */
	protected File normalizeFile (final File f) {
		if (f.getName ().toLowerCase ().endsWith (getExt ().toLowerCase ())) {
			return f;
		} else {
			return new File (f.getPath ()+"."+getExt ().toLowerCase ());
		}
	}
}
