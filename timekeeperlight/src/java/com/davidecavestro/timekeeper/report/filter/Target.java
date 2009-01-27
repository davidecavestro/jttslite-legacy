/*
 * Target.java
 *
 * Created on 31 gennaio 2005, 23.38
 */

package com.davidecavestro.timekeeper.report.filter;

/**
 * Identificatore dell'obiettivo di un filtro.
 *
 * @author  davide
 */
public interface Target {
	
	/**
	 * Identificatore dell'attributo <TT>FROM</TT> in qualita' di obiettivo di un filtro.
	 */
	public final static Target PROGRESS_FROM = new Target (){};
	
	/**
	 * Identificatore dell'attributo <TT>TO</TT> in qualita' di obiettivo di un filtro.
	 */
	public final static Target PROGRESS_TO = new Target (){};	
}
