/*
 * Created on April 8, 2008, 3:15 PM
 *
 */

package com.davidecavestro.timekeeper.report;

import java.awt.Component;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Azione di lancio report.
 *
 * @author Davide Cavestro
 */
public interface ReportLaunchAction {
	void execute (final Component parent, final JasperPrint print) throws JRException;
}
