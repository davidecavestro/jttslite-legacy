package net.sf.jttslite.dbmigrator;

/**
 * Interfaccia base per un migratore
 * @author capitanfuturo
 */
public interface Migrator {

    /**
     * Effettua la migrazione e lancia un'eccezione in caso di errore
     * @throws MigrationException
     */
    public void migrate() throws MigrationException;
}
