/* 
 * SchemaVerifier
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
package suncertify.db;

import suncertify.persistence.DBFile;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static suncertify.db.DBSchema.*;

/**
 * Auxiliary <code>class</code> that checks the mapped object-oriented primitive
 * and reference types decoded from the database file bytes, against a normative
 * database schema.
 * 
 * @author Filipe João Mendes Rosa
 * 
 * @see DBSchema
 */
public class SchemaVerifier {

    /**
     * Logger for <code>suncertify.db.SchemaVerifier</code>. This <code>
     * Logger</code> uses a <code>ConsoleHandler</code>,<code>Level.INFO</code>,
     * and a <code>SimpleFormatter</code>, in production.
     *
     * @see java.util.logging.Logger
     *
     * @see java.util.logging.ConsoleHandler
     *
     * @see java.util.logging.Level#INFO
     *
     * @see java.util.logging.SimpleFormatter
     */
    private static final Logger LOG = Logger.getLogger(SchemaVerifier.class.
            getName());
    
    /**
     * Mapping from database field names to field lengths.
     */
    Map<String, Short> fields;

    /**
     * Assists in reading and translating the database encoded header.
     *
     * @see suncertify.persistence.DBFile
     */    
    private final DBFile file;

    /**
     * Initializes this instance state with a delegate that assists in reading
     * and translating the database encoded header.
     * 
     * @param file assists in reading and translating the database encoded
     * header
     */
    public SchemaVerifier(DBFile file) {
        this.file = file;
    }

    /**
     * Asserts that the object-oriented primitive and reference types, mapped 
     * from the database file bytes, match a normative database schema. If
     * logging is enabled, and the operation concludes without an <code>
     * Exception</code> being thrown, a message is logged to the console.
     * <p>
     * This method is a "Facade" that confirms that the database encoded header 
     * was already read and translated, and if not performs that operation 
     * before proceeding with the verification assertions.
     * 
     * @return true if all the verification assertions pass, false otherwise
     * 
     * @throws IOException if there's an error while accessing or
     * reading the database file
     */
    public boolean verify() throws IOException {
        confirmHeaderRead();
        boolean asserted = assertMagicCookie() && assertRecordTotalLength()
                && assertNumberFieldsRecord() && assertFieldNames()
                && assertFieldLenghts();
        if (LOG.isLoggable(Level.FINE)) {
            LOG.log(Level.FINE, "Schema successfully asserted:{0}.", asserted);
        }
        return asserted;

    }
    
    /**
     * Confirms that the database encoded header was already read and 
     * translated, and if not performs that operation.
     * 
     * @throws IOException if there's an error while accessing or
     * reading the database file
     */
    private void confirmHeaderRead() throws IOException {
        if (file.isHeaderRead() == false) {
            file.readHeader();
        }
        fields = file.getFields();
    }
    
    /**
     * Asserts that the "magic cookie", decoded from the database file, is the
     * same as the one in the normative database schema.
     */
    private boolean assertMagicCookie() {
        return MAGIC_COOKIE == file.getMagicCookie();
    }
    
    /**
     * Asserts that the total overall length of each database record, minus the 
     * deleted state byte flag, in bytes, decoded from the database file, is the 
     * same as the one in the normative database schema.
     */
    private boolean assertRecordTotalLength() {
        return TOTAL_OVERALL_LENGTH_OF_EACH_RECORD == file.getRecordRowLength();
    }
    
    /**
     * Asserts that the number of fields in each record, decoded from the
     * database file, is the same as the one in the normative database schema.
     */
    private boolean assertNumberFieldsRecord() {
        return NUMBER_OF_FIELDS_IN_EACH_RECORD == file.getNumberFieldsRecord();
    }

    /**
     * Asserts that the field names, decoded from the database file, are the
     * same as the ones in the normative database schema.
     */
    private boolean assertFieldNames() {
        boolean a = fields.containsKey(SUBCONTRACTOR_NAME);
        boolean b = fields.containsKey(CITY);
        boolean c = fields.containsKey(TYPES_OF_WORK_PERFORMED);
        boolean d = fields.containsKey(NUMBER_OF_STAFF_IN_ORGANIZATION);
        boolean e = fields.containsKey(HOURLY_CHARGE);
        boolean f = fields.containsKey(CUSTOMER_HOLDING_THIS_RECORD);
        return a && b && c && d && e && f;
    }
    
    /**
     * Asserts that the fields' lengths in bytes, decoded from the database 
     * file, are the same as the ones in the normative database schema.
     */
    private boolean assertFieldLenghts() {
        boolean a = ((short) fields.get(SUBCONTRACTOR_NAME))
                == SUBCONTRACTOR_NAME_LENGTH;
        boolean b = ((short) fields.get(CITY)) == CITY_LENGTH;
        boolean c = ((short) fields.get(TYPES_OF_WORK_PERFORMED))
                == TYPES_OF_WORK_PERFORMED_LENGTH;
        boolean d = ((short) fields.get(NUMBER_OF_STAFF_IN_ORGANIZATION))
                == NUMBER_OF_STAFF_IN_ORGANIZATION_LENGTH;
        boolean e = ((short) fields.get(HOURLY_CHARGE)) == HOURLY_CHARGE_LENGTH;
        boolean f = ((short) fields.get(CUSTOMER_HOLDING_THIS_RECORD))
                == CUSTOMER_HOLDING_THIS_RECORD_LENGTH;
        return a && b && c && d && e && f;
    }
}
