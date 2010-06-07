/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.jttslite.dbmigrator;

/**
 * Eccezione base per le operazioni di migrazione
 * @author g-caliendo
 */
public class MigrationException extends Exception {

   private static final long serialVersionUID = 1L;

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
