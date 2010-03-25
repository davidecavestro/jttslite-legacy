/*
 * ProgressVisitor.java
 *
 * Created on 4 aprile 2004, 12.50
 */

package net.sf.jttslite.core.model.impl;

/**
 *
 * @author  davide
 */
public interface ProgressVisitor {
	
	public void visit(final ProgressItem item);
	
}
