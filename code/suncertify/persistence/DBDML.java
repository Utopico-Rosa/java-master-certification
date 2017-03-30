/* 
 * DBDML
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
package suncertify.persistence;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import suncertify.db.CustomerRecord;
import suncertify.db.SchemaVerifier;
import static suncertify.db.DBSchema.*;

/**
 * Provides the persistence and retrieval mechanism for the database file.
 *
 * @author Filipe João Mendes Rosa
 */
public class DBDML {

    /**
     * Read write mode for <code>java.io.RandomAccessFile</code>.
     *
     * @see java.io.RandomAccessFile
     */
    private static final String READ_WRITE_MODE = "rw";

    /**
     * Read mode for <code>java.io.RandomAccessFile</code>.
     *
     * @see java.io.RandomAccessFile
     */
    private static final String READ_MODE = "r";

    /**
     * <a href="http://www.asciitable.com/">ASCII encoding.</a>
     */
    private static final String US_ASCII = "US-ASCII";

    /**
     * Logger for <code>suncertify.persistence.DBMDL</code>. This <code>
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
    private static final Logger LOG = Logger.getLogger(DBDML.class.getName());

    /**
     * Assists in reading and translating the database encoded header.
     *
     * @see DBFile
     */
    private final DBFile file;

    /**
     * Traverses, reads and writes to a file in a noncontiguous fashion.
     *
     * @see java.io.RandomAccessFile
     */
    private RandomAccessFile randomFile;

    /**
     * Space character in decimal ASCII.
     */
    private static final int ASCII_SPACE = 32;

    /**
     * Initializes this instance state by providing access to the underlying
     * database file.
     *
     * @param file the underlying database file
     */
    public DBDML(DBFile file) {

        this.file = file;

    }

    /**
     * Reads all records' bytes from the database file, maps them to object-
     * oriented customer records and returns them in a data structure.
     * <p>
     * If the database file has space characters between the last character in a
     * field and the maximum length for the field, that remaining space
     * characters are transformed in null characters.
     * <p>
     * If logging is enabled and the operation succeeds, a message is logged to
     * the console. If an error occurs and an <code>Exception</code> is thrown,
     * an error message is logged to the console.
     *
     * @return a data structure containing object-oriented customer records
     *
     * @throws IOException if there's an error accessing or reading the database
     * file
     *
     * @see suncertify.db.CustomerRecord
     */
    public List<CustomerRecord> fetch() throws IOException {
        SchemaVerifier verifier = new SchemaVerifier(file);
        boolean asserted = verifier.verify();
        if (asserted) {
            try {
                randomFile = new RandomAccessFile(file.getFile(), READ_MODE);
                randomFile.seek(file.getIndexFirstRecord());
                int indexfirstrecord = file.getIndexFirstRecord();
                int filelength = (int) randomFile.length();
                int recordrowlengthplusbyteflag = file.
                        getRecordRowLengthPlusByteFlag();
                List<Integer> list = new ArrayList<>(
                        NUMBER_OF_FIELDS_IN_EACH_RECORD);
                list.add(SUBCONTRACTOR_NAME_LENGTH);
                list.add(CITY_LENGTH);
                list.add(TYPES_OF_WORK_PERFORMED_LENGTH);
                list.add(NUMBER_OF_STAFF_IN_ORGANIZATION_LENGTH);
                list.add(HOURLY_CHARGE_LENGTH);
                list.add(CUSTOMER_HOLDING_THIS_RECORD_LENGTH);
                byte[] fieldsdata;
                byte[] flagbyte;
                String fieldvalue;
                List<String> valuelist = new ArrayList<>(
                        NUMBER_OF_FIELDS_IN_EACH_RECORD + 1);
                CustomerRecord customer;
                List<CustomerRecord> customerlist = new ArrayList<>();
                StringBuilder builder;

                for (int i = indexfirstrecord; i < filelength; i = i
                        + recordrowlengthplusbyteflag) {
                    flagbyte = new byte[1];
                    randomFile.readFully(flagbyte);
                    fieldvalue = new String(flagbyte, US_ASCII);
                    valuelist.add(fieldvalue);
                    for (Integer fieldsize : list) {
                        fieldsdata = new byte[fieldsize];
                        randomFile.readFully(fieldsdata);
                        fieldvalue = new String(fieldsdata, US_ASCII);

                        if (Pattern.matches("^\\w+$", fieldvalue)) {
                            //do nothing          
                        } else if (Pattern.matches("^\\$\\d+\\.\\d+\u0020$",
                                fieldvalue)) {
                            fieldsdata[fieldsdata.length - 1] = 0;
                            fieldvalue = new String(fieldsdata, US_ASCII);
                        } else if (Pattern.matches("^\\w+\u0020$",
                                fieldvalue)) {
                            fieldsdata[fieldsdata.length - 1] = 0;
                            fieldvalue = new String(fieldsdata, US_ASCII);
                        } else {
                            int j;
                            for (j = 0; j < fieldsdata.length; ++j) {
                                if (fieldsdata[j] == ASCII_SPACE && fieldsdata[j
                                        + 1] == ASCII_SPACE) {
                                    break;
                                }
                            }
                            for (int k = j; k < fieldsdata.length; ++k) {
                                fieldsdata[k] = 0;
                            }

                            fieldvalue = new String(fieldsdata, US_ASCII);
                        }

                        valuelist.add(fieldvalue);
                    }

                    customer = new CustomerRecord(new ArrayList<>(valuelist));
                    customerlist.add(customer);
                    valuelist.clear();
                }
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.fine("Database records fetched from the database "
                            + "file.");
                }
                return customerlist;
            } catch (IOException ioexception) {

                if (LOG.isLoggable(Level.SEVERE)) {
                    StackTraceElement[] stacktrace = ioexception.
                            getStackTrace();
                    String line;
                    StringBuilder builder = new StringBuilder();
                    for (StackTraceElement element : stacktrace) {
                        line = element.toString();
                        builder.append(line).append(System.getProperty("line."
                                + "separator"));
                    }
                    LOG.log(Level.SEVERE,
                            "Error while accessing the database file: "
                            + "{0}" + System.getProperty("line.separator")
                            + "{1}",
                            new Object[]{ioexception.getMessage(), builder});
                }
                throw new IOException(
                        "File access error while reading the data.",
                        ioexception);

            } finally {
                if (randomFile != null) {
                    try {
                        randomFile.close();
                    } catch (IOException ioexception) {
                        if (LOG.isLoggable(Level.SEVERE)) {
                            StackTraceElement[] stacktrace = ioexception.
                                    getStackTrace();
                            String line;
                            StringBuilder builder = new StringBuilder();
                            for (StackTraceElement element : stacktrace) {
                                line = element.toString();
                                builder.append(line).append(System.
                                        getProperty("line.separator"));
                            }
                            LOG.log(Level.SEVERE,
                                    "Error while accessing the database file: "
                                    + "{0}" + System.getProperty(
                                            "line.separator") + "{1}",
                                    new Object[]{ioexception.getMessage(),
                                        builder});
                        }
                        throw new IOException(
                                "File access error while closing the file.",
                                ioexception);
                    }

                }
            }

        } else {
            if (LOG.isLoggable(Level.WARNING)) {
                LOG.warning("Schema mismatch. Didn´t retrieve the database "
                        + "records from the database file to memory.");
            }
            throw new RuntimeException("Schema mismatch.");
        }
    }

    /**
     * Takes the object-oriented customer records from the input data structure,
     * transforms them to bytes, and persists those bytes to the database file.
     * <p>
     * If logging is enabled and the operation succeeds, a message is logged to
     * the console. If an error occurs and an <code>Exception</code> is thrown,
     * an error message is logged to the console.
     *
     * @param list a data structure containing object-oriented customer records
     *
     * @throws IOException if there's an error accessing or writing to the
     * database file
     *
     * @see suncertify.db.CustomerRecord
     */
    public void persist(List<CustomerRecord> list) throws IOException {
        try {
            String flag;
            String name;
            String location;
            String specialties;
            String size;
            String rate;
            String owner;
            byte[] flagbytes;
            byte[] namebytes;
            byte[] locationbytes;
            byte[] specialtiesbytes;
            byte[] sizebytes;
            byte[] ratebytes;
            byte[] ownerbytes;

            randomFile = new RandomAccessFile(file.getFile(), READ_WRITE_MODE);
            randomFile.seek(file.getIndexFirstRecord());
            for (CustomerRecord record : list) {
                flag = record.getFlag();
                name = record.getName();
                location = record.getLocation();
                specialties = record.getSpecialties();
                size = record.getSize();
                rate = record.getRate();
                owner = record.getOwner();
                flagbytes = flag.getBytes(US_ASCII);
                namebytes = name.getBytes(US_ASCII);
                locationbytes = location.getBytes(US_ASCII);
                specialtiesbytes = specialties.getBytes(US_ASCII);
                sizebytes = size.getBytes(US_ASCII);
                ratebytes = rate.getBytes(US_ASCII);
                ownerbytes = owner.getBytes(US_ASCII);
                randomFile.write(flagbytes);
                randomFile.write(namebytes);
                randomFile.write(locationbytes);
                randomFile.write(specialtiesbytes);
                randomFile.write(sizebytes);
                randomFile.write(ratebytes);
                randomFile.write(ownerbytes);
            }
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("Database records written from memory to the database"
                        + " file.");
            }
        } catch (IOException ioexception) {
            if (LOG.isLoggable(Level.SEVERE)) {
                StackTraceElement[] stacktrace = ioexception.getStackTrace();
                String line;
                StringBuilder builder = new StringBuilder();
                for (StackTraceElement element : stacktrace) {
                    line = element.toString();
                    builder.append(line).append(System.getProperty("line."
                            + "separator"));
                }
                LOG.log(Level.SEVERE,
                        "Error while accessing the database file: "
                        + "{0}" + System.getProperty("line.separator") + "{1}",
                        new Object[]{ioexception.getMessage(), builder});
            }
            throw new IOException("File access error while saving data.",
                    ioexception);
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException ioexception) {
                    if (LOG.isLoggable(Level.SEVERE)) {
                        StackTraceElement[] stacktrace = ioexception.
                                getStackTrace();
                        String line;
                        StringBuilder builder = new StringBuilder();
                        for (StackTraceElement element : stacktrace) {
                            line = element.toString();
                            builder.append(line).append(System.
                                    getProperty("line.separator"));
                        }
                        LOG.log(Level.SEVERE,
                                "Error while accessing the database file: "
                                + "{0}" + System.getProperty("line.separator")
                                + "{1}", new Object[]{ioexception.getMessage(),
                                    builder});
                    }
                    throw new IOException("File access error while closing the "
                            + "file.", ioexception);
                }

            }
        }
    }

}
