/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.jttslite.dbmigrator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.spi.IniBuilder;

/**
 * Classe base per il test del migratore
 * @author capitanfuturo
 */
public class Test {

    public static void main(String[] arg) throws FileNotFoundException, InvalidFileFormatException, IOException, MigrationException {
        File old = new File("/home/capitanfuturo/.JTTS/etc/userprefs.properties");

        File migrated = new File("/home/capitanfuturo/.JTTS/etc/JTTS.ini");
        FileInputStream fip = new FileInputStream(migrated);
        Ini ini = new Ini(fip);
        IniBuilder builder = IniBuilder.newInstance(ini);
        builder.startIni();
        builder.startSection("JDO");
        builder.handleOption("jdousername", "");
        builder.handleOption("jdostoragedirpath", "");
        builder.handleOption("jdostoragename", "");
        builder.endSection();
        builder.endIni();
        PreferencesMigrator migrator = new PreferencesMigrator(Logger.getAnonymousLogger(), old, ini);
        migrator.migrate();
        ini.store(migrated);
    }
}
