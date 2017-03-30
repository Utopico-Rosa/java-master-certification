/* 
 * DBFile
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides access to the database file and translates its header encoded
 * information into java primitive and reference data types.
 * <p>
 * <pre>
 * The format of data in the database file is as follows: Start of file 4 byte
 * numeric, magic cookie value. Identifies this as a data file 4 byte numeric,
 * total overall length in bytes of each record 2 byte numeric, number of fields
 * in each record
 *
 * Schema description section. Repeated for each field in a record: 2 byte
 * numeric, length in bytes of field name n bytes (defined by previous entry),
 * field name 2 byte numeric, field length in bytes end of repeating block
 *
 * Data section. Repeat to end of file: 1 byte "deleted" flag. 0 implies valid
 * record, 1 implies deleted record Record containing fields in order specified
 * in schema section, no separators between fields, each field fixed length at
 * maximum specified in schema information
 *
 * End of file
 *
 * All numeric values are stored in the header information use the formats of
 * the DataInputStream and DataOutputStream classes. All text values, and all
 * fields (which are text only), contain only 8 bit characters, null terminated
 * if less than the maximum length for the field. The character encoding is 8
 * bit US ASCII.
 * </pre>
 *
 * @author Filipe João Mendes Rosa
 */
public class DBFile {

    /**
     * Logger for <code>suncertify.persistence.DBFile</code>. This <code>
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
    private static final Logger LOG = Logger.getLogger(DBFile.class.getName());

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
     * Traverses, reads and writes to a file in a noncontiguous fashion.
     *
     * @see java.io.RandomAccessFile
     */
    private RandomAccessFile randomFile;

    /**
     * Magic cookie value. Identifies the file as a data file.
     */
    private int magicCookie;

    /**
     * Total overall length in bytes of each record.
     */
    private int recordRowLength;

    /**
     * Number of fields in each record.
     */
    private short numberFieldsRecord;

    /**
     * Mapping from the record fields names to their lengths.
     */
    private Map<String, Short> fields;

    /**
     * Total overall length in bytes of each record, plus the length of the
     * deleted state byte flag. Useful for positioning <code>java.io.
     * RandomAccessFile</code> in the beginning of each record.
     *
     * @see java.io.RandomAccessFile#seek(long)
     */
    private int recordRowLengthPlusByteFlag;

    /**
     * Zero based index of the first record. Useful for positioning <code>java.
     * io.RandomAccessFile</code> in the beginning of the first record, just
     * after the header coded data.
     *
     * @see java.io.RandomAccessFile#seek(long)
     */
    private int indexFirstRecord;

    /**
     * <code>Boolean</code> primitive flag, indicating if the byte encoded
     * header data has already been decoded to java primitive and reference
     * types.
     */
    private boolean headerRead;

    /**
     * Provides file system access to the database file.
     */
    private final File file;

    /**
     * Checks if the input filename is suitable to be of a file that contains
     * the database, else an <code>Exception</code> is thrown. Prepares the file
     * for access in read mode in a noncontiguous way.
     * <p>
     * If logging is enabled, the beginning and end of the operation is logged
     * to the console.
     *
     * @param fileName the name of the database file
     *
     * @throws FileNotFoundException if there is no such file in the file system
     *
     * @see java.io.RandomAccessFile
     */
    public DBFile(String fileName) throws FileNotFoundException {
        if (LOG.isLoggable(Level.FINER)) {
            LOG.entering("suncertify.persistence.DBFile",
                    "DBFile (String filename)",
                    new Object[]{fileName});
        }
        file = new File(fileName);
        if (!file.exists() || !file.canRead() || !file.canWrite() || !file.
                isFile() || !(file.length() != 0)) {
            throw new IllegalArgumentException(
                    "Incorrect filename for db access.");
        }
        randomFile = new RandomAccessFile(file, READ_MODE);
        if (LOG.isLoggable(Level.FINER)) {
            LOG.exiting("suncertify.persistence.DBFile",
                    "DBFile (String filename)");
        }
    }

    /**
     * Reads and translates, at once, the preliminary encoded information of the
     * database file's header and its encoded database schema.
     * <p>
     * If logging is enabled, and the operation completes successfully, a
     * confirmation message is logged to the console. If an error occurs, and an
     * <code>Exception</code> is thrown, an error message is logged to the
     * console.
     *
     * @throws IOException if there's an error accessing the database file, or
     * reading its bytes.
     */
    public void readHeader() throws IOException {
        try {
            readStartFile();
            readSchemaDescription();
            headerRead = true;
            if (LOG.isLoggable(Level.CONFIG)) {
                LOG.config("Database header read.");
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
            throw new IOException("File access error while reading the header.",
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
                                    getProperty("line separator"));
                        }
                        LOG.log(Level.SEVERE,
                                "Error while accessing the database file: "
                                + "{0}" + System.getProperty("line.separator")
                                + "{1}",
                                new Object[]{ioexception.getMessage(),
                                    builder});
                    }
                    throw new IOException(
                            "File access error while closing the file.",
                            ioexception);
                }

            }
        }
    }

    /**
     * Accesses the magic cookie value.
     *
     * @return the magic cookie value
     */
    public int getMagicCookie() {
        return magicCookie;
    }

    /**
     * Accesses the total overall length in bytes of each record
     *
     * @return the total overall length in bytes of each record
     */
    public int getRecordRowLength() {
        return recordRowLength;
    }

    /**
     * Accesses the number of fields in each record.
     *
     * @return the number of fields in each record
     */
    public short getNumberFieldsRecord() {
        return numberFieldsRecord;
    }

    /**
     * Accesses the mapping from the record fields names to their lengths.
     *
     * @return the mapping from the record fields names to their lengths
     */
    public Map getFields() {
        return fields;
    }
    
    /**
     * Accesses the total overall length in bytes of each record, plus the
     * length of the deleted state byte flag.
     *
     * @return the total overall length in bytes of each record, plus the length
     * of the deleted state byte flag
     */
    public int getRecordRowLengthPlusByteFlag() {
        return recordRowLengthPlusByteFlag = recordRowLength + 1;
    }

    /**
     * Accesses the zero based index of the first record.
     *
     * @return the zero based index of the first record
     */
    public int getIndexFirstRecord() {
        return indexFirstRecord;
    }

    /**
     * Accesses the <code>Boolean</code> primitive flag, indicating if the byte
     * encoded header data has already been decoded into java primitive and
     * reference types.
     *
     * @return the <code>boolean</code> flag
     */
    public boolean isHeaderRead() {
        return headerRead;
    }

    /**
     * Accesses the file system access to the database file.
     *
     * @return the file system access to the database file
     */
    public File getFile() {
        return file;
    }

    /**
     * Reads and translates to java primitive types, the byte encoded start of
     * the database file header.
     * <p>
     * Values read are:
     * <ul>
     * <li>4 byte numeric, magic cookie value
     * <li>4 byte numeric, total overall length in bytes of each record
     * <li>2 byte numeric, number of fields in each record
     * </ul>
     *
     * @throws java.io.IOException if there's an error accessing or reading the
     * database file
     */
    private void readStartFile() throws IOException {
        randomFile.seek(0);
        magicCookie = randomFile.readInt();
        recordRowLength = randomFile.readInt();
        numberFieldsRecord = randomFile.readShort();
    }

    /**
     * Reads and translates to java primitive and reference types, the byte
     * encoded database schema of the database file header.
     * <p>
     * <font size="10">Database schema:</font>
     * <strong>Field descriptive name</strong>
     * <ul>
     * <li>Subcontractor Name
     * <li>City
     * <li>Types of work performed
     * <li>Number of staff in organization
     * <li>Hourly charge
     * <li>Customer holding this record
     * </ul>
     * <strong>Database field name</strong>
     * <ul>
     * <li>name
     * <li>location
     * <li>specialties
     * <li>size
     * <li>rate
     * <li>owner
     * </ul>
     * <strong>Field length</strong>
     * <ul>
     * <li>32
     * <li>64
     * <li>64
     * <li>6
     * <li>8
     * <li>8
     * </ul>
     * <strong>Detailed description</strong>
     * <ul>
     * <li>The name of the subcontractor this record relates to
     * <li>The locality in which this contractor works
     * <li>Comma separated list of types of work this contractor can perform
     * <li>The number of workers available when this record is booked
     * <li>Charge per hour for the subcontractor. This field includes the
     * currency symbol</li>
     * <li>The customer id (an 8 digit number). Note that for this application,
     * you should assume that both the customers. and the CSRs know the customer
     * id. However the booking must always be made by CSR by entering the
     * customer id against the subcontractor details</li>
     * </ul>
     *
     * @throws java.io.IOException if there's an error accessing or reading the
     * database file
     */
    private void readSchemaDescription() throws IOException {
        short fieldnamelength;
        String fieldname;
        short fieldlength;
        byte[] fieldnamebytes;

        fields = new HashMap<>(numberFieldsRecord);
        for (int fieldnumber = 0; fieldnumber < numberFieldsRecord;
                fieldnumber++) {
            fieldnamelength = randomFile.readShort();
            fieldnamebytes = new byte[fieldnamelength];
            randomFile.readFully(fieldnamebytes);
            fieldname = new String(fieldnamebytes, US_ASCII);
            fieldlength = randomFile.readShort();
            fields.put(fieldname, fieldlength);
        }
        indexFirstRecord = (int) randomFile.getFilePointer();

    }

}
