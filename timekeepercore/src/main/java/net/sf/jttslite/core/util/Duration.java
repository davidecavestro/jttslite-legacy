package net.sf.jttslite.core.util;

/**
 * Una durata temporale.
 * <p>
 * Fornisce la durata in termini di 
 * <ul>
 * <li>
 * numero totale di millisecondi (vedi {@link #getTime()}), oppure
 * <li>
 * con dati aggregati in termini di giorni, ore, minuti, secondi e millisecondi.
 * </ul>
 * Nella seconda modalità ogni campo rappresenta il tempo non incluso nel campo 
 * superiore (ovvero il modulo). Fa quindi eccezione il campo giorni, che
 * non avendo campi "superiori" non  è limitato in alcun modo.
 * <br>
 * E' inoltre possibile ottenere il numero di ore al posto dei giorni tramite il metodo
 * {@link #getTotalHours()}. In sostanza tale metodo è sostitutivo di
 * <code>getDays()</code> e <code>getHours()</code>.
 * <br>
 * Ad esempio per una durata di due giorni e tre ore, saranno vere le seguenti affermazioni
 *
 * <pre>
 * Assert.true (d.getDays()==2);
 * Assert.true (d.getHours()==3);
 *
 * Assert.true (d.getTotalHours()==51);//24*2+3
 *
 * </pre>
 * </p>
 *
 * @author davide
 */
public interface Duration {

	long HOURS_PER_DAY = 24;
	long MILLISECONDS_PER_DAY = DurationImpl.MILLISECONDS_PER_HOUR * DurationImpl.HOURS_PER_DAY;
	long MILLISECONDS_PER_HOUR = DurationImpl.MILLISECONDS_PER_MINUTE * DurationImpl.MINUTES_PER_HOUR;
	long MILLISECONDS_PER_MINUTE = DurationImpl.MILLISECONDS_PER_SECOND * DurationImpl.SECONDS_PER_MINUTE;
	long MILLISECONDS_PER_SECOND = 1000;
	long MINUTES_PER_HOUR = 60;
	long SECONDS_PER_MINUTE = 60;

	/**
	 * Ritorna i giorni di questa durata.
	 *
	 * @return i giorni di questa durata.
	 */
	long getDays ();

	/**
	 * Ritorna le ore di questa durata.
	 *
	 * @return le ore di questa durata.
	 */
	long getHours ();

	/**
	 * Ritorna i minuti di questa durata.
	 *
	 * @return i minuti di questa durata.
	 */
	long getMinutes ();

	/**
	 * Ritorna i secondi di questa durata.
	 *
	 * @return i secondi di questa durata.
	 */
	long getSeconds ();


	/**
	 * Ritorna i millisecondi di questa durata.
	 *
	 * @return i millisecondi di questa durata.
	 */
	long getMilliseconds ();

	/**
	 * Ritorna questa durata in millisecondi.
	 *
	 * @return questa durata in millisecondi.
	 */
	long getTime ();

	/**
	 * Ritorna il numero totale di ore di questa durata.
	 * <p>
	 * Si differenzia da {@link #getHours()} in quanto il valore ritornato da questo
	 * metodo include il valore rappresentato da {@link #getDays()}.
	 * </p>
	 *
	 * @return il numero totale di ore di questa durata.
	 */
	long getTotalHours ();

}
