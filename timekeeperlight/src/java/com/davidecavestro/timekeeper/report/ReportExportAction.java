/*
 * Created on April 8, 2008, 12:11 PM
 *
 */

package com.davidecavestro.timekeeper.report;

import com.davidecavestro.common.util.file.CustomFileFilter;
import com.davidecavestro.common.util.file.FileUtils;
import java.awt.Component;
import java.io.File;
import java.text.MessageFormat;
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
	
	public void execute (final Component parent, final JasperPrint print) throws JRException {
		initFileChooser ();
		final int result = _fileChooser.showSaveDialog (parent);
		if (result==JFileChooser.APPROVE_OPTION) {
			final File f = normalizeFile (_fileChooser.getSelectedFile ());
			if (f.exists ()) {
				if (JOptionPane.YES_OPTION!=JOptionPane.showConfirmDialog (
					parent, 
					MessageFormat.format (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("ReportDialog/JOptionPane/Confirm_overwrite_existing_file"), f.getPath ()),
					java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("ReportDialog/JOptionPane/Title/Confirm_overwrite_existing_file"), 
					JOptionPane.YES_NO_OPTION)) {
					return;
				}
			}
			export (print, f);
			
			JOptionPane.showMessageDialog (parent, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("ReportExportMessageDialog/Exporting_completed"));
			
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
