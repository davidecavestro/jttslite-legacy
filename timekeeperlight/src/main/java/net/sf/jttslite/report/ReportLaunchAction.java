/*
 * Created on April 8, 2008, 3:15 PM
 *
 */

package net.sf.jttslite.report;

import net.sf.jttslite.ApplicationContext;
import java.awt.Component;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Azione di lancio report.
 *
 * @author Davide Cavestro
 */
public interface ReportLaunchAction {
	void execute (final ApplicationContext context, final Component parent, final JasperPrint print) throws JRException;
}
