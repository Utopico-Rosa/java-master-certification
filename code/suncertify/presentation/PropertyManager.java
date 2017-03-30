/* 
 * PropertyManager
 * 
 * Filipe João Mendes Rosa
 * 
 * 27/02/2007
 * 
 * Sun Certified Developer for the Java 2 Platform: Application 
 * Submission (Version 2.1.1)
 * 
 * Java SE 6 Developer Certified Master Assignment 1Z0-855
 * 
 */
package suncertify.presentation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Manages key-value properties in memory, saves them to a file, and recovers
 * them from the file.
 *
 * @author Filipe João Mendes Rosa
 */
public class PropertyManager {

    /**
     * Key-value property list.
     */
    private final Properties properties;

    /**
     * File in the file system.
     */
    private final File file;

    /**
     * Initializes this instance state. If the passed in <code>file</code>
     * doesn't exist in the file system, it is created. Otherwise, its key-
     * value properties are read into memory.
     *
     * @param file where the key-value properties are stored
     *
     * @throws IOException if there's an error accessing, reading or writing to
     * the file in the file system
     */
    public PropertyManager(File file) throws IOException {
        this.file = file;
        this.properties = new Properties();
        readProperties();
    }

    /**
     * Saves the in-memory key-value properties to a file.
     *
     * @throws IOException if there's an error accessing, reading or writing to
     * the file in the file system
     */
    public void saveProperties() throws IOException {
        properties.store(new BufferedWriter(new FileWriter(file)),
                "User Default Configuration");
    }

    /**
     * Adds a key-value property to memory.
     *
     * @param key the <code>String</code> key
     * 
     * @param value the <code>String</code> value
     */
    public void addProperty(String key, String value) {
        properties.put(key, value);

    }

    /**
     * Accesses a value from a key-value property in memory.
     *
     * @param key the <code>String</code> key
     *
     * @return the <code>String</code> value
     */
    public String getProperty(String key) {
        return (String) properties.get(key);
    }

    /**
     * Reads the key-value properties from a file in the file system and stores
     * them in memory. If the file doesn't already exist it is created blank.
     * 
     * @throws IOException if there's an error accessing, reading or writing to
     * the file in the file system
     */
    private void readProperties() throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        properties.load(new BufferedReader(new InputStreamReader(
                new FileInputStream(file))));
    }

}
