/* 
 * Data
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

import suncertify.persistence.DBDML;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import static suncertify.db.DBSchema.DELETED_RECORD;
import static suncertify.db.DBSchema.VALID_RECORD;

/**
 * Singleton that acts as a cache between the user(s) and the database file, and
 * implements the business logic use cases.
 * 
 * @author Filipe João Mendes Rosa
 */
public class Data implements DB, DBPlusQuery {

    /**
     * Thread-safe data structure that maps customer record numbers, to another
     * thread-safe data structure that maps "lock cookies" to object-oriented
     * mapped customer records.
     */
    private static ConcurrentMap<Integer,
            ConcurrentMap<Long, CustomerRecord>> map;

    /**
     * Logger for <code>suncertify.db.Data</code>. This <code>
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
    private static final Logger LOG = Logger.getLogger(Data.class.getName());
    
    /**
     * "Delegate" that reads and map bytes to object-oriented customer
     * records, and in the opposite direction maps and writes object-oriented 
     * customer records to bytes. 
     */
    private static DBDML dbDML;

    /**
     * Accesses the Singleton's object of this <code>class</code>. The creation
     * of the singleton is made in a thread-safe, lazy way.
     * 
     * @return the Singleton's object of this <code>class</code>
     */
    public static Data getInstance() {
        return SingletonHelper.INSTANCE;
    }

    /**
     * Accesses the object-oriented customer records stored in cache, filters
     * those marked as valid records, and returns them in a data structure.
     * 
     * @return a data structure containing valid, object-oriented customer 
     * records
     * 
     * @see DBSchema#VALID_RECORD
     */
    public static List<CustomerRecord> getList() {
        List<CustomerRecord> list = new ArrayList<>();
        first:
        for (ConcurrentMap<Long, CustomerRecord> e : map.values()) {
            second:
            for (CustomerRecord record : e.values()) {
                if (record.getFlag().equals(DELETED_RECORD)) {
                    continue second;
                } else {
                    list.add(record);
                }
            }
        }
        return list;
    }

    /**
     * Initializes the cache, in a thread-safe way, doing nothing when the 
     * cache is already populated.
     * <p>
     * If logging is enabled, and an attempt is made to re-populate the cache,
     * a warning message is logged to the console.
     * 
     * @param fileDML the delegate that maps from bytes to objects to bytes
     * 
     * @throws IOException if there's an error while accessing the customer
     * records bytes
     */
    public static synchronized void setList(DBDML fileDML) throws IOException {
        if (dbDML == null && map == null) {
            dbDML = fileDML;
            map = new ConcurrentSkipListMap<Integer, 
                    ConcurrentMap<Long, CustomerRecord>>();
            List<CustomerRecord> list = fileDML.fetch();
            int n = 0;
            for (CustomerRecord record : list) {
                map.put(++n, new ConcurrentHashMap<Long, CustomerRecord>());
                map.get(n).put((long) 0, record);
            }
        } else if (LOG.isLoggable(Level.WARNING)) {
            LOG.warning("An attempt was made to reset the state"
                    + " of the Singleton cache.");
        }

    }

    /**
     * Accesses the delegate that maps from bytes to objects to bytes. 
     * 
     * @return the delegate that maps from bytes to objects to bytes
     */
    public static DBDML getDbDML() {
        return dbDML;
    }
    
    /**
     * No argument constructor. As this class is implemented as a singleton, the
     * constructor is private.
     */
    private Data() {
    }

    /**
     * Reads a record from the file. Returns an array where each element is a 
     * record value. If logging is enabled and the operation succeeds, a message
     * is logged to the console.
     * <p>
     * A record is considered not found, if the record number doesn't exist, or
     * if it exists, but the matching record is deemed as deleted.
     * 
     * @param recNo the record number pertaining to a client company
     * 
     * @return a data structure containing the state of the client company
     * 
     * @throws RecordNotFoundException if there is no such record in memory
     * 
     * @see DBSchema#DELETED_RECORD 
     */ 
    @Override
    public String[] read(int recNo) throws RecordNotFoundException {
        ConcurrentMap<Long, CustomerRecord> maprecord = map.get(recNo);
        if (maprecord == null) {
            throw new RecordNotFoundException("Invalid record number:" + recNo
                    + ".");
        }
        Collection<CustomerRecord> records = maprecord.values();
        for (CustomerRecord record : records) {
            if (record.getFlag().equals(DELETED_RECORD)) {
                throw new RecordNotFoundException("Invalid record number:"
                        + recNo + ".");
            }
            String[] state = record.serializeAllState();
            if (LOG.isLoggable(Level.FINER)) {
                LOG.log(Level.FINER, "record {0} read at {1} by id:{2}.",
                        new Object[]{recNo, 
                            new Date(System.currentTimeMillis()),
                            Thread.currentThread().getId()});
            }
            return state;
        }
        throw new RecordNotFoundException("Invalid record number:" + recNo + 
                ".");
    }

    /**
     * Modifies the fields of a record. The new value for field n appears in 
     * data[n]. If logging is enabled and the operation succeeds, a message is
     * logged to the console.
     * <p>
     * A record is considered not found, if the record number doesn't exist, or
     * if it exists, but the matching record is deemed as deleted.
     * <p>
     * If the input data structure contains a non-blank customer id, and the
     * customer record matching the input record number, already has a non-blank
     * customer id, a <code>BookingException</code> is thrown.
     * <p>
     * Before updating the customer record, it compares the input <code>
     * lockcookie</code> with the "lockcookie" stored in memory for the customer
     * record matching the input record number, and if they mismatch, a <code>
     * SecurityException</code> is thrown.
     * 
     * @param recNo the record number pertaining to a client company
     * 
     * @param data the new state of the client company
     * 
     * @param lockCookie the id security token that locked the record number
     * 
     * @throws RecordNotFoundException if there is no such record in memory
     * 
     * @throws SecurityException if the record is locked with a cookie
     * other than <code>lockCookie</code>
     * 
     * @throws BookingException if it tries to book a client when that client is
     * already booked
     * 
     * @see DBSchema#DELETED_RECORD
     * 
     * @see DBSchema#CUSTOMER_HOLDING_THIS_RECORD
     */ 
    @Override
    public void update(int recNo, String[] data, long lockCookie)
            throws RecordNotFoundException, SecurityException, 
            BookingException {
        if (map.get(recNo) == null) {
            throw new RecordNotFoundException("Invalid record number:" + recNo
                    + ".");
        }
        ConcurrentMap<Long, CustomerRecord> recordmap = map.get(recNo);
        Collection<CustomerRecord> records = recordmap.values();
        for (CustomerRecord record : records) {
            if (record.getFlag().equals(DELETED_RECORD)) {
                throw new RecordNotFoundException("Invalid record number:"
                        + recNo + ".");
            }
        }
        if (data[5] != null) {
            for (CustomerRecord record : records) {
                if (!(record.getOwner().trim().equals(""))) {
                    throw new BookingException(recNo + " alredy booked!");
                }
            }
        }
        LockManager.checkSecurity(lockCookie, recordmap);
        for (CustomerRecord record : records) {
            record.setState(data);
        }
        if (LOG.isLoggable(Level.FINER)) {
            LOG.log(Level.FINER, "record {0} updated at {1} by id:{2}.",
                    new Object[]{recNo, new Date(System.currentTimeMillis()),
                        Thread.currentThread().getId()});
        }
    }

    /**
     * Deletes a record, making the record number and associated disk storage 
     * available for reuse. If logging is enabled and the operation succeeds, a
     * message is logged to the console.
     * <p>
     * A record is considered not found, if the record number doesn't exist, or
     * if it exists, but the matching record is deemed as deleted.  
     * <p>
     * Before deleting the customer record, it compares the input <code>
     * lockcookie</code> with the "lockcookie" stored in memory for the customer
     * record matching the input record number, and if they mismatch, a <code>
     * SecurityException</code> is thrown.
     * 
     * @param recNo the record number pertaining to a client company
     * 
     * @param lockCookie the id security token that locked the record number
     * 
     * @throws RecordNotFoundException if there is no such record in memory
     * 
     * @throws SecurityException if the record is locked with a cookie
     * other than <code>lockCookie</code>
     * 
     * @see DBSchema#DELETED_RECORD
     */
    @Override
    public void delete(int recNo, long lockCookie)
            throws RecordNotFoundException, SecurityException {
        if (map.get(recNo) == null) {
            throw new RecordNotFoundException("Invalid record number:" + recNo
                    + ".");
        }
        ConcurrentMap<Long, CustomerRecord> recordmap = map.get(recNo);
        Collection<CustomerRecord> records = recordmap.values();
        for (CustomerRecord record : records) {
            if (record.getFlag().equals(DELETED_RECORD)) {
                throw new RecordNotFoundException("Invalid record number:"
                        + recNo + ".");
            }
        }
        LockManager.checkSecurity(lockCookie, map.get(recNo));
        for (CustomerRecord record : recordmap.values()) {
            record.setFlag(DELETED_RECORD);
        }
        if (LOG.isLoggable(Level.FINER)) {
            LOG.log(Level.FINER, "record {0} deleted at {1} by id:{2}.",
                    new Object[]{recNo, new Date(System.currentTimeMillis()),
                        Thread.currentThread().getId()});
        }
    }

    /**
     * Returns an array of record numbers that match the specified
     * criteria. Field n in the database file is described by
     * criteria[n]. A null value in criteria[n] matches any field
     * value. A non-null  value in criteria[n] matches any field
     * value that begins with criteria[n]. (For example, "Fred"
     * matches "Fred" or "Freddy".) If logging is enabled and the operation 
     * succeeds, a message is logged to the console.
     * <p>
     * It excludes from the search those customer records that are deemed as 
     * deleted.
     * 
     * @param criteria the data structure that contains the search criteria
     * 
     * @return a data structure that contains the records numbers that match the
     * criteria
     * 
     * @see DBSchema#DELETED_RECORD
     */
    @Override
    public int[] find(String[] criteria) {
        int[] recordnumbers;
        String[] state;
        
        List<Integer> list = new ArrayList<>();
        int recordnumber = 0;
        boolean matches = true;  
        first:
        for (ConcurrentMap<Long, CustomerRecord> mapcontainer : map.values()) {
            second:
            for (CustomerRecord record : mapcontainer.values()) {
                ++recordnumber;
                if (record.getFlag().equals(DELETED_RECORD)) {
                    continue first;
                }
                state = record.serializeAllState();
                third:
                for (int i = 0; i < state.length; ++i) {
                    fourth:
                    for (int j = 0; j < criteria.length; ++j) {
                        if (criteria[j] == null) {
                            continue fourth;
                        } else if (i != j) {
                            continue fourth;

                        } else if (criteria[j].contains("$")) {
                            matches &= Pattern.matches("^" + "\\" + criteria[j]
                                    + ".*", state[i]);
                        } else {
                            matches &= Pattern.matches("^" + criteria[j] + ".*",
                                    state[i]);
                        }

                    }
                }
                if (matches == true) {

                    list.add(recordnumber);
                }
                matches = true;
            }

        }
        recordnumbers = new int[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            recordnumbers[i] = list.get(i);
        }
        if (LOG.isLoggable(Level.FINER)) {
            LOG.log(Level.FINER, "Query conducted at {0} by id:{1}.",
                    new Object[]{new Date(System.currentTimeMillis()),
                        Thread.currentThread().getId()});
        }
        return recordnumbers;
    }

    /**
     * Searches for matching record numbers on the basis of name and location.
     * A null value in criteria[n] matches any field value. A non-null value in 
     * criteria[n] matches any field value that has an exact match on 
     * criteria[n]. (For example, "Philarmonic Remodeling" matches "Philharmonic
     * Remodeling"). If logging is enabled and the operation succeeds, a 
     * message is logged to the console.
     * <p>
     * It excludes from the search those customer records that are deemed as 
     * deleted.
     * 
     * @param criteria the data structure that contains the search criteria
     * 
     * @return a data structure that contains the record numbers that match the
     * criteria
     * 
     * @see DBSchema#DELETED_RECORD
     */
    @Override
    public int[] findByNameAndLocation(String[] criteria) {
        int[] recordnumbers;
        String[] state;
        
        int recordnumber = 0;
        List<Integer> list = new ArrayList<>();
        boolean matches = true;
        first:
        for (ConcurrentMap<Long, CustomerRecord> mapcontainer : map.values()) {
            second:
            for (CustomerRecord record : mapcontainer.values()) {
                ++recordnumber;
                if (record.getFlag().equals(DELETED_RECORD)) {
                    continue first;
                }
                state = record.serializeNameAndLocation();
                third:
                for (int i = 0; i < state.length; ++i) {
                    if (state[i] == null) {
                        continue third;
                    }
                    fourth:
                    for (int j = 0; j < criteria.length; ++j) {
                        if (criteria[j] == null) {
                            continue fourth;
                        } else if (i != j) {
                            continue fourth;
                        } else {
                          matches &= state[i].trim().equals(criteria[j].trim());
                        }

                    }
                }
                if (matches == true) {

                    list.add(recordnumber);
                }
                matches = true;
            }

        }
        recordnumbers = new int[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            recordnumbers[i] = list.get(i);
        }
        if (LOG.isLoggable(Level.FINER)) {
            LOG.log(Level.FINER, "Query conducted at {0} by id:{1}.",
                    new Object[]{new Date(System.currentTimeMillis()),
                        Thread.currentThread().getId()});
        }
        return recordnumbers;
    }

    /**
     * Creates a new record in the database (possibly reusing a
     * deleted entry). Inserts the given data, and returns the record
     * number of the new record. If logging is enabled and the operation
     * succeeds a message is logged to the console.
     * <p>
     * A <code>DuplicateKeyException</code> is thrown if one of the object-
     * oriented customer records in cache, already contains simultaneously 
     * a matching subcontractor name and city with those of the input data
     * structure, and that record is deemed as valid.
     * <p>
     * It excludes from the search those customer records that are deemed as 
     * deleted.
     * 
     * @param data the data structure that contains the new record state
     * 
     * @return the record number of the new record
     * 
     * @throws DuplicateKeyException if the client company name and geographical
     * location already exists in memory
     * 
     * @see DBSchema#DELETED_RECORD
     * 
     * @see DBSchema#VALID_RECORD
     * 
     * @see CustomerRecord
     * 
     * @see DBSchema#SUBCONTRACTOR_NAME
     * 
     * @see DBSchema#CITY
     */
    @Override
    public int create(String[] data) throws DuplicateKeyException {
        for (ConcurrentMap<Long, CustomerRecord> maprecords : map.values()) {
            for (CustomerRecord record : maprecords.values()) {
                if (record.getName().contains(data[0])
                        && record.getLocation().contains(data[1])
                        && record.getFlag().equals(VALID_RECORD)) {
                    throw new DuplicateKeyException(
                            "An attempt was made to insert"
                            + " a new record with preexisting name:'" + data[0]
                            + "' and location:'" + data[1] + "'.");
                }
            }
        }
        List<String> list = new ArrayList<>();
        list.add("0");
        list.addAll(Arrays.asList(data));
        CustomerRecord customer = new CustomerRecord(list);
        int recordnumber = 0;
        first:
        for (ConcurrentMap<Long, CustomerRecord> maprecords : map.values()) {
            ++recordnumber;
            second:
            for (CustomerRecord record : maprecords.values()) {
                if (record.getFlag().equals(DELETED_RECORD)) {
                    third:
                    for (Long cookie : maprecords.keySet()) {
                        maprecords.clear();
                        maprecords.put(cookie, customer);
                        if (LOG.isLoggable(Level.FINER)) {
                            LOG.log(Level.FINER,
                                    "Customer record created at {0} by id:{1}.",
                                    new Object[]{new Date(System.
                                                currentTimeMillis()),
                                        Thread.currentThread().getId()});
                        }
                        return recordnumber;
                    }
                }
            }

        }
        ConcurrentMap<Long, CustomerRecord> recordmap
                = new ConcurrentHashMap<>();
        recordmap.put((long) 0, customer);
        map.put(++recordnumber, recordmap);
        if (LOG.isLoggable(Level.FINER)) {
            LOG.log(Level.FINER, "Customer record created at {0} by id:{1}.",
                    new Object[]{new Date(System.currentTimeMillis()),
                        Thread.currentThread().getId()});
        }
        return recordnumber;
    }

    /**
     * Locks a record so that it can only be updated or deleted by this client.
     * Returned value is a cookie that must be used when the record is unlocked,
     * updated, or deleted. If the specified record is already locked by a 
     * different client, the current thread gives up the CPU and consumes no 
     * CPU cycles until the record is unlocked.
     * <p>
     * A <code>RecordNotFoundException</code> is thrown, if the record number 
     * doesn't exist.
     * 
     * @param recNo the record number to lock
     * 
     * @return an identification security token
     * 
     * @throws RecordNotFoundException if there is no such record in memory
     */
    @Override
    public long lock(int recNo) throws RecordNotFoundException {
        if (map.get(recNo) != null) {
            return LockManager.lock(map.get(recNo));
        } else {
            throw new RecordNotFoundException("Invalid record number:" + recNo
                    + ".");
        }
    }

    /**
     * Releases the lock on a record. Cookie must be the cookie returned when 
     * the record was locked; otherwise throws SecurityException.
     * <p>
     * A <code>RecordNotFoundException</code> is thrown, if the record number 
     * doesn't exist.
     * 
     * @param recNo the record number to unlock
     * 
     * @param cookie the identification security token
     
     * @throws RecordNotFoundException if there is no such record in memory
     * 
     * @throws SecurityException if the record is locked with a cookie
     * other than <code>lockCookie</code>
     */
    @Override
    public void unlock(int recNo, long cookie)
            throws RecordNotFoundException, SecurityException {

        if (map.get(recNo) != null) {
            LockManager.unlock(cookie, map.get(recNo));
        } else {
            throw new RecordNotFoundException("Invalid record number:" + recNo
                    + ".");
        }

    }
    
    /**
     * Provides a thread-safe lazy-loading of the singleton <code>Data</code> 
     */
    private static class SingletonHelper {
    
      /**
       * A reference to the outer <code>class Data</code>
       */
      private static final Data INSTANCE = new Data();
    }

}
