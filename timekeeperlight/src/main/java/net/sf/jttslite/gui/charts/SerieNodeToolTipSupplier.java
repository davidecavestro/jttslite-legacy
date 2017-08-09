/*
 * SerieNodeTooltipSupplier.java
 *
 * Created on 4 marzo 2005, 9.26
 */

package net.sf.jttslite.gui.charts;

import net.sf.jttslite.common.charts.awt.SerieNode;
import net.sf.jttslite.common.charts.swing.tooltip.ToolTipGenerationPolicy;
import net.sf.jttslite.core.util.Duration;
import java.text.DecimalFormat;

/**
 * Genera il messaggio di ToolTip per un nodo della serie
 *
 * @author  davide
 */
public final class SerieNodeToolTipSupplier {
	private final static DefaultPolicy _defaultPolicy = new DefaultPolicy ();
	/** Costruttore privato, evita istanziazione. */
	private SerieNodeToolTipSupplier () {
	}
	
	/**
	 * Ritorna il messaggio di ToolTip per il nodo specificato.
	 *
	 * @param node il nodo.
	 * @return il messaggio di ToolTip per il nodo specificato.
	 */	
	public static String getToolTip (final SerieNode node){
		return _defaultPolicy.generate (node);
	}
	
	/**
	 * Ritorna il messaggio di ToolTip per il nodo specificato.
	 *
	 * @return il messaggio di ToolTip per il nodo specificato.
	 * @param policy la politica di generazione.
	 * @param node il nodo.
	 */	
	public static String getToolTip (final SerieNode node, ToolTipGenerationPolicy policy){
		return policy.generate (node);
	}
	
	/**
	 * Implementazione interna di default. Fornisce una stringa del tipo:
	 * [NODE NAME] - local HH:MM:SS - total HH:MM:SS
	 */
	private final static class DefaultPolicy implements ToolTipGenerationPolicy {

		private DefaultPolicy (){}
		
		private final DurationNumberFormatter durationNumberFormatter = new DurationNumberFormatter ();

		private static class DurationNumberFormatter extends DecimalFormat {
			public DurationNumberFormatter (){
				this.setMinimumIntegerDigits (2);
			}
		}
	
		
		public String generate (final SerieNode node){
			
			final long localPercentage = Math.round (node.getValue ()*100/node.getTotalValue ());
			final long childrenPercentage = Math.round (node.getChildrenValue ()*100/node.getTotalValue ());
			
			final double parentTotalValue = getParentTotalValue (node);
			
			final long totalPercentageOnParent = parentTotalValue!=0?Math.round (node.getTotalValue ()*100/parentTotalValue):100;
			final long localPercentageOnParent = parentTotalValue!=0?Math.round (node.getValue ()*100/parentTotalValue):100;
			final long childrenPercentageOnParent = parentTotalValue!=0?Math.round (node.getChildrenValue ()*100/parentTotalValue):100;
			
			
				
			final StringBuilder sb= new StringBuilder ();
			sb.append ("<HTML>")
			.append ("<TABLE>")
			
			.append ("<THEAD>")
			
			.append ("<TR>")
			.append ("<TD colspan='4' nowrap align=center>")
			.append ("<B><I>").append (node.getName ()).append ("</I></B>")
			.append ("</TD>")
			.append ("</TR>")
			
			.append ("<TR>")
			.append ("<TD colspan='3' nowrap align=center>")
			.append ("</TD>")
			.append ("<TD nowrap align=center>")
			.append ("<B>")
			.append (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("RingChart/Tooltip/parent_percentage"))
			.append ("</B>")
			.append ("</TD>")
			.append ("</TR>")
			
			.append ("</THEAD>")
			
			.append ("<TBODY>")
			
			.append ("<TR>")
			.append ("<TD align=right>")
			.append (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("RingChart/Tooltip/node_local_time")).append (": ")
			.append ("</TD>")
			.append ("<TD align=right>")
			.append ("<TT align=right>").append (formatDuration (new Duration ((long)node.getValue ()))).append ("<TT>")
			.append ("</TD>")
			.append ("<TD align=right>").append ("<TT>(").append (localPercentage).append ("%)").append ("</TT>").append ("</TD>")
			.append ("<TD align=right>").append ("<TT>").append (localPercentageOnParent).append ("%").append ("</TT>").append ("</TD>")
			.append ("</TR>")
			
			.append ("<TR>")
			.append ("<TD align=right>")
			.append (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("RingChart/Tooltip/node_subtree_time")).append (": ")
			.append ("</TD>")
			.append ("<TD align=right>")
			.append ("<TT>").append (formatDuration (new Duration ((long)node.getChildrenValue ()))).append ("<TT>")
			.append ("</TD>")
			.append ("<TD align=right>").append ("<TT>(").append (childrenPercentage).append ("%)").append ("</TT>").append ("</TD>")
			.append ("<TD align=right>").append ("<TT>").append (childrenPercentageOnParent).append ("%").append ("</TT>").append ("</TD>")
			.append ("</TR>")
			
			.append ("<TR>").append ("<TD colspan=4><HR></TD>").append ("</TR>")
			
			.append ("<TR>")
			.append ("<TD align=right>")
			.append ("<B>")
			.append (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("RingChart/Tooltip/node_total_time"))
			.append ("</B>")
			.append (": ")
			.append ("</TD>")
			.append ("<TD align=right>").append ("<B>").append ("<TT>").append (formatDuration (new Duration ((long)node.getTotalValue ()))).append ("</TT>").append ("</B>").append ("</TD>")
			.append ("<TD>").append ("</TD>")
			.append ("<TD align=right>").append ("<B><TT>").append (totalPercentageOnParent).append ("%").append ("</TT></B>").append ("</TD>")
			.append ("</TR>")
			
			.append ("</TBODY>")
			.append ("</HTML>");
			;
			return sb.toString ();
		}
		
		/**
		 * Ritorna la stringa formattata che rappresenta la durata specificata nelformato previsto.
		 *
		 * @param duration la durata.
		 * @return la stringa formattata che rappresenta una durata nelformato previsto.
		 */		
		private String formatDuration (final Duration duration){
			final StringBuilder sb = new StringBuilder ();
			
//			final long days = duration.getDays();
//
//			if (0==days){
//				sb.append ("__");
//			} else {
//				sb.append (durationNumberFormatter.format(duration.getDays()));
//			}
//			sb.append (" - ");
			
			sb.append (durationNumberFormatter.format(duration.getTotalHours()))
			.append (":")
			.append (durationNumberFormatter.format(duration.getMinutes()))
			.append (":")
			.append (durationNumberFormatter.format(duration.getSeconds()));
			return sb.toString ();
		}
	}
	
	private static double getParentTotalValue (final SerieNode node) {
		if (node.getParent ()!=null) {
			return node.getParent ().getTotalValue ();
		}
		return 0;
	}
	private static double getParentValue (final SerieNode node) {
		if (node.getParent ()!=null) {
			return node.getParent ().getValue ();
		}
		return 0;
	}
	private static double getParentChildrenValue (final SerieNode node) {
		if (node.getParent ()!=null) {
			return node.getParent ().getChildrenValue ();
		}
		return 0;
	}
}
