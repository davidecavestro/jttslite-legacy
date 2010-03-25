/*
 * ToolTipGenerationPolicy.java
 *
 * Created on 4 marzo 2005, 9.31
 */

package net.sf.jttslite.common.charts.swing.tooltip;

import net.sf.jttslite.common.charts.awt.SerieNode;

/**
 * Politica di generazione del messaggio di tooltip per un nodo della serie.
 *
 * @author  davide
 */
public interface ToolTipGenerationPolicy {
	String generate (final SerieNode node);
}
