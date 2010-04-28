/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.jttslite.report;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import net.sf.jttslite.report.flavors.CumulateLocalProgressesRowBean;

/**
 *
 * @author davide
 */
public class ReportDataSourceProvider {

	public static final List<CumulateLocalProgressesRowBean> forTest () {
		final CumulateLocalProgressesRowBean row = new CumulateLocalProgressesRowBean ();
		row.setPeriodName ("foo");
		row.setPeriodStart (new Timestamp (System.currentTimeMillis ()));
		row.setPeriodTotalEffort (Long.valueOf (4));
		row.setProgressDescription ("this is a progress");
		row.setProgressEnd (new Timestamp (System.currentTimeMillis ()));
		row.setTaskHierarchy ("/foo/foo1");
		row.setTaskName ("foo");
		row.setTaskID (1);
		row.setTaskTotalEffort (Long.valueOf (4));
		final List<CumulateLocalProgressesRowBean> data = new ArrayList<CumulateLocalProgressesRowBean> ();
		data.add (row);

		return data;
//		return new JRBeanCollectionDataSource (data);
	}
}
