/*
 * ReportDataGenerator.java
 *
 * Created on 30 gennaio 2005, 9.56
 */

package net.sf.jttslite.report;


import net.sf.jttslite.ApplicationContext;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Generatore di report.
 *
 * @author  davide
 */
public final class ReportDataGenerator {
	
	private final ApplicationContext _context;
	/** Costruttore. */
	public ReportDataGenerator (final ApplicationContext context) {
		_context = context;
	}
	
	/**
	 * Genera il report.
	 *
	 * @param extractor l'estrattore dei dati.
	 * @param prefs le preferenze di generazione del report.
	 * @param jasperBindings le impostazioni...
	 */
	public void generate ( final DataExtractor extractor, final ReportPreferences prefs, final JRBindings jasperBindings, final ReportLaunchAction rla){
		_context.getLogger ().log (Level.INFO ,java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Starting_report_generation_process"));
		
		final StringBuilder logBuffer = new StringBuilder ();
		logBuffer.append (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Generating_report_data.")).append ("\n");
		logBuffer.append (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("extractor:_")).append (extractor).append ("\n");
		logBuffer.append (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("preferences:_")).append (prefs).append ("\n");
		logBuffer.append (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("bindings:_")).append (jasperBindings);
		_context.getLogger ().log (Level.INFO, logBuffer.toString ());
		
		final Collection data = extractor.extract ();
		try {
			//			final File output = prefs.getOutput ();
			//@todo completare generazione report
			
			final InputStream reportDescriptor = jasperBindings.getReportDescriptor ();
			//			final String outFileName = output.getPath ();
			final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource (data);
			
			
			final JasperPrint print = JasperFillManager.fillReport (
				reportDescriptor,
				prefs.getParams (),
				dataSource);
			
			rla.execute (_context, _context.getWindowManager ().getMainWindow (), print);
		} catch (final Exception e){
			throw new RuntimeException (e);
		}
		_context.getLogger ().log (Level.INFO, java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Report_generation_process_successfully_completed"));
	}
	
	
}
