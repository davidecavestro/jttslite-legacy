/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.net.sf.jttslite.dbmigrator;

/**
 * Eccezione base per le operazioni di migrazione
 * @author g-caliendo
 */
public class MigrationException extends Exception {

   public MigrationException(String message, Throwable cause) {
	  super (message, cause);
   }

   public MigrationException(String message) {
	  super (message);
   }

   public MigrationException(Throwable cause){
	  super(cause);
   }
}
