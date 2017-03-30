/* 
 * LockManager
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

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread-safe class responsible for locking and unlocking object-oriented
 * mapped customer records, managing and verifying the validity of security
 * identification tokens, also known as "lockcookies".
 * 
 * @author Filipe João Mendes Rosa
 */

public class LockManager {

    /**
     * Thread-safe security identification token.
     *<p>
     * Creates an anonymous <code>ThreadLocal&lt;AtomicLon&gt;><code> with the 
     * initial value set to 0.
     */ 
    private static final ThreadLocal< AtomicLong> LOCKCOOKIE
            = new ThreadLocal<AtomicLong>() {
        @Override
        protected AtomicLong initialValue() {
            return new AtomicLong(0);
        }
    };
    
    /**
     * Logger for <code>suncertify.db.LockManager<code>. This <code>
     * Logger<code> uses a <code>ConsoleHandler<code>,<code>Level.INFO<code>, 
     * and a <code>SimpleFormatter<code>, in production.
     * 
     * @see java.util.logging.Logger
     * 
     * @see java.util.logging.ConsoleHandler
     * 
     * @see java.util.logging.Level#INFO
     * 
     * @see java.util.logging.SimpleFormatter
     */
    private static final Logger LOG = Logger.getLogger(LockManager.class.
            getName());

    /**
     * Locks a record so that it can only be updated or deleted by this client.
     * Returned value is a cookie that must be used when the record is unlocked,
     * updated, or deleted. If the specified record is already locked by a 
     * different client, the current thread gives up the CPU and consumes no 
     * CPU cycles until the record is unlocked.
     * <p>
     * Besides returning the "lockcookie", it also passes the "lockcookie" by-
     * value to the the input data structure.
     * <p>
     * If logging is enabled, it logs the entering and exiting of the operation.
     * 
     * @param map data structure containing a mapping from the "lockcookie" to
     * an object-oriented mapped customer record
     * 
     * @return an identification security token, also known as a "lockcookie"
     */
    public static synchronized long lock(ConcurrentMap<Long, 
            CustomerRecord> map) {
        if (LOG.isLoggable(Level.FINER)) {
            LOG.entering("suncertify.db.LockManager",
                    "lock(ConcurrentMap<Long, CustomerRecord> map)",
                    new Object[]{map});
        }
        for (Long key : map.keySet()) {
            LOCKCOOKIE.get().set(key);
        }
        long cookie = LOCKCOOKIE.get().get();
        long id = Thread.currentThread().getId();
        while (cookie != 0 && cookie != id) {
            try {
                LockManager.class.wait();
            } catch (InterruptedException interrupted) {
                break;
            }
        }
        for (CustomerRecord record : map.values()) {
            map.clear();
            map.put(id, record);
        }
        LOCKCOOKIE.get().set(id);
        long lockcookie = LOCKCOOKIE.get().get();
        if (LOG.isLoggable(Level.FINER)) {
            LOG.exiting("suncertify.db.LockManager",
                    "lock(ConcurrentMap<Long, CustomerRecord> map)",
                    new Object[]{lockcookie});
        }
        return lockcookie;
    }

    /**
     * Releases the lock on a record. Cookie must be the cookie
     * returned when the record was locked; otherwise throws SecurityException.
     * Notifies other threads that are waiting to get a lock on this customer
     * record.
     * <p>
     * When the record lock is released the lockcookie in the input data
     * structure is reseted to 0.
     * <p>
     * If logging is enabled, it logs the entering and exiting of the operation.
     * 
     * @param cookie the security identification token
     * 
     * @param map data structure containing a mapping from the "lockcookie" to
     * an object-oriented mapped customer record
     * 
     * @throws SecurityException if the security identification tokens don't
     * match
     */
    public static synchronized void unlock(long cookie,
            ConcurrentMap<Long, CustomerRecord> map)
            throws SecurityException {
        if (LOG.isLoggable(Level.FINER)) {
            LOG.entering("suncertify.db.LockManager",
                    "unlock(long cookie,ConcurrentMap<Long, "
                    + "CustomerRecord> map)", new Object[]{cookie, map});
        }
        checkSecurity(cookie, map);
        for (CustomerRecord record : map.values()) {
            map.clear();
            map.put((long) 0, record);
            LOCKCOOKIE.get().set(0);
            LockManager.class.notifyAll();
            if (LOG.isLoggable(Level.FINER)) {
                LOG.exiting("suncertify.db.LockManager",
                        "unlock(long cookie,ConcurrentMap<Long, "
                         + "CustomerRecord> map)");
            }
        }
    }

    /**
     * Confirms that the security identification token passed for the operation 
     * on a particular customer record is the same as is attributed to that 
     * record in memory.
     * 
     * @param cookie the security identification token, also known as 
     * "lockcookie"
     * 
     * @param map data structure containing a mapping from the "lockcookie" to
     * an object-oriented mapped customer record
     * 
     * @throws SecurityException if the security identification tokens don't
     * match
     */
    public static void checkSecurity(long cookie,
            ConcurrentMap<Long, CustomerRecord> map)
            throws SecurityException {
        for (Long key : map.keySet()) {
            if (cookie != (long) key) {
                throw new SecurityException("Input Mismatch. The provided token"
                        + " for unlock wasn´t the correct one.");
            }
        }

    } 

}
