/*
 * GenericUtils.java
 *
 * Created on July 20, 2008, 8:01 AM
 *
 */

package com.davidecavestro.common.util;

/**
 * A collector for generic utility methods.
 *
 * @author Davide Cavestro
 */
public class GenericUtils {
	
	/**
	 * A simple <tt>equals</tt> method invocation with the necessary <tt>null</tt> reference tests.
	 *
	 * @param o1 the first object to compare.
	 * @param o2 the second object to compare.
	 * @return a result according to the semantics of the <tt>equals</tt> method defined for the first parameter.
	 */
	public static boolean equals (final Object o1, final Object o2) {
		if (o1==o2) {
			return true;
		}
		if (o1!=null && o2!=null) {
			return o1.equals (o2);
		}
		
		return false;
	}
	
	/**
	 * A <tt>hashCode</tt> method invocation on the specified parameter, with the simple check of <tt>null</tt> reference.
	 */
	public static int hashCode (final Object obj, final int hashForNull) {
		if (obj==null) {
			return hashForNull;
		}
		return obj.hashCode ();
	}
}
