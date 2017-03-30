/* 
 * DataRemote
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
package suncertify.integration;

import suncertify.db.CustomerRecord;
import suncertify.db.Data;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;

/**
 * Provides the delegate that let a remote client use the program's
 * functionalities in a distributed fashion.
 *
 * @author Filipe João Mendes Rosa
 */
public class DataRemote extends UnicastRemoteObject implements
        DBAllQueriesRemote {

    /**
     * Logger for <code>suncertify.integration.DataRemote</code>. This <code>
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
    private static final Logger LOG = Logger.getLogger(DataRemote.class.
            getName());

    /**
     * The singleton cache.
     */
    private final Data cacheManager;

    /**
     * Initializes this instance state with the singleton cache.
     *
     * @param cacheManager the singleton cache
     *
     * @throws RemoteException if an unspecified error occurs on the server side
     */
    public DataRemote(Data cacheManager) throws RemoteException {
        this.cacheManager = cacheManager;
    }

    /**
     * Reads a record from the file. Returns an array where each element is a
     * record value. If logging is enabled and a <code>RecordNotFoundException
     * </code> is thrown, logs a message to the console.
     *
     * @param recNo the record number pertaining to a client company
     *
     * @return a data structure containing the state of the client company
     *
     * @throws RecordNotFoundException if there is no such record in memory
     *
     * @throws RemoteException if an unspecified error occurs on the server side
     */
    @Override
    public String[] read(int recNo) throws RecordNotFoundException,
            RemoteException {
        try {
            return cacheManager.read(recNo);
        } catch (RecordNotFoundException recordexception) {
            if (LOG.isLoggable(Level.FINE)) {
                StackTraceElement[] stacktrace = recordexception.
                        getStackTrace();
                String line;
                StringBuilder builder = new StringBuilder();
                for (StackTraceElement element : stacktrace) {
                    line = element.toString();
                    builder.append(line).append(System.getProperty("line."
                            + "separator"));
                }
                LOG.log(Level.FINE,
                        "Attempt to access an invalid record number: "
                        + "{0}" + System.getProperty("line.separator") + "{1}",
                        new Object[]{recordexception.getMessage(), builder});
            }
            throw new RecordNotFoundException("Wrong record number.",
                    recordexception);
        }
    }

    /**
     * Modifies the fields of a record. The new value for field n appears in
     * data[n]. If logging is enabled and a <code>RecordNotFoundException
     * </code> or <code>SecurityException</code> are thrown, logs a message to
     * the console.
     *
     * @param recNo the record number pertaining to a client company
     *
     * @param data the new state of the client company
     *
     * @param lockCookie the id security token that locked the record number
     *
     * @throws RecordNotFoundException if there is no such record in memory
     *
     * @throws SecurityException if the record is locked with a cookie other
     * than <code>lockCookie</code>
     *
     * @throws RemoteException if an unspecified error occurs on the server side
     */
    @Override
    public void update(int recNo, String[] data, long lockCookie)
            throws RecordNotFoundException, SecurityException, RemoteException {
        try {
            cacheManager.update(recNo, data, lockCookie);
        } catch (RecordNotFoundException recordexception) {
            if (LOG.isLoggable(Level.FINE)) {
                StackTraceElement[] stacktrace = recordexception
                        .getStackTrace();
                String line;
                StringBuilder builder = new StringBuilder();
                for (StackTraceElement element : stacktrace) {
                    line = element.toString();
                    builder.append(line).append(System.getProperty("line."
                            + "separator"));
                }
                LOG.log(Level.FINE,
                        "Attempt to access an invalid record number: "
                        + "{0}" + System.getProperty("line.separator") + "{1}",
                        new Object[]{recordexception.getMessage(), builder});
            }
            throw new RecordNotFoundException("Wrong record number.",
                    recordexception);
        } catch (SecurityException security) {
            if (LOG.isLoggable(Level.WARNING)) {
                StackTraceElement[] stacktrace = security.getStackTrace();
                String line;
                StringBuilder builder = new StringBuilder();
                for (StackTraceElement element : stacktrace) {
                    line = element.toString();
                    builder.append(line).append(System.getProperty("line."
                            + "separator"));
                }
                LOG.log(Level.WARNING, "Security breach. Token mismatch: "
                        + "{0}" + System.getProperty("line.separator") + "{1}",
                        new Object[]{security.getMessage(), builder});
            }
            throw new SecurityException("The user has no privileges for"
                    + " conducting this action.", security);
        }
    }

    /**
     * Deletes a record, making the record number and associated disk storage
     * available for reuse. If logging is enabled and a <code>
     * RecordNotFoundException</code> or <code>SecurityException</code> are
     * thrown, logs a message to the console.
     *
     * @param recNo the record number pertaining to a client company
     *
     * @param lockCookie the id security token that locked the record number
     *
     * @throws RecordNotFoundException if there is no such record in memory
     *
     * @throws SecurityException if the record is locked with a cookie other
     * than <code>lockCookie</code>
     *
     * @throws RemoteException if an unspecified error occurs on the server side
     */
    @Override
    public void delete(int recNo, long lockCookie)
            throws RecordNotFoundException, SecurityException, RemoteException {
        try {
            cacheManager.delete(recNo, lockCookie);
        } catch (RecordNotFoundException recordexception) {
            if (LOG.isLoggable(Level.FINE)) {
                StackTraceElement[] stacktrace = recordexception
                        .getStackTrace();
                String line;
                StringBuilder builder = new StringBuilder();
                for (StackTraceElement element : stacktrace) {
                    line = element.toString();
                    builder.append(line).append(System.getProperty("line."
                            + "separator"));
                }
                LOG.log(Level.FINE,
                        "Attempt to access an invalid record number: "
                        + "{0}" + System.getProperty("line.separator") + "{1}",
                        new Object[]{recordexception.getMessage(), builder});
            }
            throw new RecordNotFoundException("Wrong record number.",
                    recordexception);
        } catch (SecurityException security) {
            if (LOG.isLoggable(Level.WARNING)) {
                StackTraceElement[] stacktrace = security.getStackTrace();
                String line;
                StringBuilder builder = new StringBuilder();
                for (StackTraceElement element : stacktrace) {
                    line = element.toString();
                    builder.append(line).append(System.getProperty("line."
                            + "separator"));
                }
                LOG.log(Level.WARNING, "Security breach. Token mismatch: "
                        + "{0}" + System.getProperty("line.separator") + "{1}",
                        new Object[]{security.getMessage(), builder});
            }
            throw new SecurityException("The user has no privileges for"
                    + " conducting this action.", security);
        }
    }

    /**
     * Returns an array of record numbers that match the specified criteria.
     * Field n in the database file is described by criteria[n]. A null value in
     * criteria[n] matches any field value. A non-null value in criteria[n]
     * matches any field value that begins with criteria[n]. (For example,
     * "Fred" matches "Fred" or "Freddy".)
     *
     * @param criteria the data structure that contains the search criteria
     *
     * @return a data structure that contains the records numbers that match the
     * criteria
     *
     * @throws RemoteException if an unspecified error occurs on the server side
     */
    @Override
    public int[] find(String[] criteria) throws RemoteException {
        return cacheManager.find(criteria);
    }

    /**
     * Searches for matching record numbers on the basis of name and location. A
     * null value in criteria[n] matches any field value. A non-null value in
     * criteria[n] matches any field value that has an exact match on
     * criteria[n]. (For example, "Philarmonic Remodeling" matches "Philharmonic
     * Remodeling").
     *
     * @param criteria the data structure that contains the search criteria
     *
     * @return a data structure that contains the record numbers that match the
     * criteria
     *
     * @throws RemoteException if an unspecified error occurs on the server side
     */
    @Override
    public int[] findByNameAndLocation(String[] criteria)
            throws RemoteException {
        return cacheManager.findByNameAndLocation(criteria);
    }

    /**
     * Creates a new record in the database (possibly reusing a deleted entry).
     * Inserts the given data, and returns the record number of the new record.
     * If logging is enabled and a <code>DuplicateKeyException</code> is thrown,
     * logs a message to the console.
     *
     * @param data the data structure that contains the new record state
     *
     * @return the record number of the new record
     *
     * @throws DuplicateKeyException if the client company name and geographical
     * location already exists in memory
     *
     * @throws RemoteException if an unspecified error occurs on the server side
     */
    @Override
    public int create(String[] data) throws DuplicateKeyException,
            RemoteException {
        try {
            return cacheManager.create(data);
        } catch (DuplicateKeyException duplicate) {
            if (LOG.isLoggable(Level.WARNING)) {
                StackTraceElement[] stacktrace = duplicate.getStackTrace();
                String line;
                StringBuilder builder = new StringBuilder();
                for (StackTraceElement element : stacktrace) {
                    line = element.toString();
                    builder.append(line).append(System.getProperty("line."
                            + "separator"));
                }
                LOG.log(Level.FINE, "Attempt to input a repeated client: "
                        + "{0}" + System.getProperty("line.separator") + "{1}",
                        new Object[]{duplicate.getMessage(), builder});
            }
            throw new DuplicateKeyException("The client already exists!",
                    duplicate);

        }

    }

    /**
     * Locks a record so that it can only be updated or deleted by this client.
     * Returned value is a cookie that must be used when the record is unlocked,
     * updated, or deleted. If the specified record is already locked by a
     * different client, the current thread gives up the CPU and consumes no CPU
     * cycles until the record is unlocked. If logging is enabled and a
     * <code>DuplicateKeyException</code> is thrown, logs a message to the
     * console.
     *
     * @param recNo the record number to lock
     *
     * @return an identification security token
     *
     * @throws RecordNotFoundException if there is no such record in memory
     *
     * @throws RemoteException if an unspecified error occurs on the server side
     */
    @Override
    public long lock(int recNo)
            throws RecordNotFoundException, RemoteException {
        try {
            return cacheManager.lock(recNo);
        } catch (RecordNotFoundException recordexception) {
            if (LOG.isLoggable(Level.FINE)) {
                StackTraceElement[] stacktrace = recordexception.
                        getStackTrace();
                String line;
                StringBuilder builder = new StringBuilder();
                for (StackTraceElement element : stacktrace) {
                    line = element.toString();
                    builder.append(line).append(System.getProperty("line."
                            + "separator"));
                }
                LOG.log(Level.FINE,
                        "Attempt to access an invalid record number: "
                        + "{0}" + System.getProperty("line.separator") + "{1}",
                        new Object[]{recordexception.getMessage(), builder});
            }
            throw new RecordNotFoundException("Wrong record number.",
                    recordexception);

        }
    }

    /**
     * Releases the lock on a record. Cookie must be the cookie returned when
     * the record was locked; otherwise throws SecurityException. If logging is
     * enabled and a <code>RecordNotFoundException</code> or <code>
     * SecurityException</code> are thrown, logs a message to the console.
     *
     * @param recNo the record number to unlock
     *
     * @param cookie the identification security token
     *
     * @throws RecordNotFoundException if there is no such record in memory
     *
     * @throws SecurityException if the record is locked with a cookie other
     * than <code>lockCookie</code>
     *
     * @throws RemoteException if an unspecified error occurs on the server side
     */
    @Override
    public void unlock(int recNo, long cookie)
            throws RecordNotFoundException, SecurityException, RemoteException {
        try {
            cacheManager.unlock(recNo, cookie);
        } catch (RecordNotFoundException recordexception) {
            if (LOG.isLoggable(Level.FINE)) {
                StackTraceElement[] stacktrace = recordexception.
                        getStackTrace();
                String line;
                StringBuilder builder = new StringBuilder();
                for (StackTraceElement element : stacktrace) {
                    line = element.toString();
                    builder.append(line).append(System.getProperty("line."
                            + "separator"));
                }
                LOG.log(Level.FINE,
                        "Attempt to access an invalid record number: "
                        + "{0}" + System.getProperty("line.separator") + "{1}",
                        new Object[]{recordexception.getMessage(), builder});
            }
            throw new RecordNotFoundException("Wrong record number.",
                    recordexception);
        } catch (SecurityException security) {
            if (LOG.isLoggable(Level.WARNING)) {
                StackTraceElement[] stacktrace = security.getStackTrace();
                String line;
                StringBuilder builder = new StringBuilder();
                for (StackTraceElement element : stacktrace) {
                    line = element.toString();
                    builder.append(line).append(System.getProperty("line."
                            + "separator"));
                }
                LOG.log(Level.WARNING, "Security breach. Token mismatch: "
                        + "{0}" + System.getProperty("line.separator") + "{1}",
                        new Object[]{security.getMessage(), builder});
            }
            throw new SecurityException("The user has no privileges for"
                    + " conducting this action.",
                    security);
        }
    }

    /**
     * Accesses all object-oriented mapped customer records listed in memory,
     * and returns them in a data structure.
     *
     * @return a data structure containing all customer records listed in memory
     *
     * @throws RemoteException if an unspecified error occurs on the server side
     */
    @Override
    public List<CustomerRecord> getList() throws RemoteException {
        return Data.getList();
    }

}
