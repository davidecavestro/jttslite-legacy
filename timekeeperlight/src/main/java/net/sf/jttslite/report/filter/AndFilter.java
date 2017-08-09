/*
 * AndFilter.java
 *
 * Created on 30 gennaio 2005, 10.23
 */

package net.sf.jttslite.report.filter;

/**
 * Implementa la concatenazione logica di due filtri tramite l'peratore di AND logico.
 *
 * @author  davide
 */
public final class AndFilter extends AbstractFilter{
	
	/**
	 * Il filtro sx.
	 */
	private Filter _left;
	
	/**
	 * Il filtro dx.
	 */
	private Filter _right;
	
	/** Costruttore. */
	public AndFilter (final Filter left, final Filter right) {
		this._left = left;
		this._right = right;
	}
	
	/**
	 * Ritorna il filtro di sinistra.
	 *
	 * @return il filtro di sinistra.
	 */	
	public Filter getLeft (){
		return this._left;
	}
	
	/**
	 * Ritorna il filtro di destra.
	 *
	 * @return il filtro di destra.
	 */	
	public Filter getRight (){
		return this._right;
	}
	
	/**
	 * Ritorna una rappresentazione in formato stringa di questo filtro.
	 *
	 * @return una stringa che rappresenta questo filtro.
	 */	
	public String toString (){
		final StringBuilder sb = new StringBuilder ();
		sb.append ("[ ");
		sb.append (this._left);
		sb.append (" AND ");
		sb.append (this._right);
		sb.append ("] ");
		return sb.toString ();
	}
	
	/**
	 * Ritorna <TT>true</TT> se l'oggetto specificato soddisfa la condizione di questo filtro.
	 *
	 * @param obj l'oggetto da filtrare.
	 * @return <TT>true</TT> se l'oggetto specificato soddisfa la condizione di questo filtro.
	 */	
	public boolean matches (Object obj) {
		return 
			this._left.matches (obj) 
			&& 
			this._right.matches (obj);
	}
	
	/**
	 * Ritorna <TT>true</TT> se questo filtro � uguale all'ogggetto specificato.
	 *
	 * @param obj l'oggetto da testare.
	 * @return <TT>true</TT> se questo filtro � uguale a quello specificato.
	 */	
	public boolean equals (Object obj){
		if (this==obj){
			/* identita' */
			return true;
		}
		if (obj instanceof AndFilter){
			final AndFilter af = (AndFilter)obj;
			return 
				(this._left==af._left
					|| this._left!=null && af._left!=null && this._left.equals (af._left))
				&&
				(this._right==af._right
					|| this._right!=null && af._right!=null && this._right.equals (af._right));
		}
		
		/* tipo incompatibile */
		return false;
	}
}
