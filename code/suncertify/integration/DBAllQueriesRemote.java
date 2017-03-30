/* 
 * DBAllQueriesRemote
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
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;

/**
 * Represents all operations a distributed client can perform on a network
 * server.
 * 
 * @author Filipe João Mendes Rosa
 */
public interface DBAllQueriesRemote extends Remote, DataMarker {

    /**
     * Reads a record from the file. Returns an array where each element is a 
     * record value.
     * 
     * @param recNo the record number pertaining to a client company
     * 
     * @return a data structure containing the state of the client company
     * 
     * @throws RecordNotFoundException if there is no such record in memory
     * 
     * @throws RemoteException if an unspecified error occurs on the server side
     */

    public String[] read(int recNo) throws RecordNotFoundException,
            RemoteException;

    /**
     * Modifies the fields of a record. The new value for field n appears in 
     * data[n].
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
     * @throws RemoteException if an unspecified error occurs on the server side
     */   
    public void update(int recNo, String[] data, long lockCookie)
            throws RecordNotFoundException, SecurityException, RemoteException;
    
    /**
     * Deletes a record, making the record number and associated disk storage 
     * available for reuse.
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
     * @throws RemoteException if an unspecified error occurs on the server side
     */
    public void delete(int recNo, long lockCookie)
            throws RecordNotFoundException, SecurityException, RemoteException;

    /**
     * Returns an array of record numbers that match the specified
     * criteria. Field n in the database file is described by
     * criteria[n]. A null value in criteria[n] matches any field
     * value. A non-null  value in criteria[n] matches any field
     * value that begins with criteria[n]. (For example, "Fred"
     * matches "Fred" or "Freddy".)
     * 
     * @param criteria the data structure that contains the search criteria
     * 
     * @return a data structure that contains the records numbers that match the
     * criteria
     * 
     * @throws RemoteException if an unspecified error occurs on the server side 
     */
    public int[] find(String[] criteria) throws RemoteException;

    /**
     * Searches for matching record numbers on the basis of name and location.
     * A null value in criteria[n] matches any field value. A non-null value in 
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
    public int[] findByNameAndLocation(String[] criteria) 
            throws RemoteException;
    
    /**
     * Creates a new record in the database (possibly reusing a
     * deleted entry). Inserts the given data, and returns the record
     * number of the new record.
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
    public int create(String[] data) throws DuplicateKeyException,
            RemoteException;

    /**
     * Locks a record so that it can only be updated or deleted by this client.
     * Returned value is a cookie that must be used when the record is unlocked,
     * updated, or deleted. If the specified record is already locked by a 
     * different client, the current thread gives up the CPU and consumes no 
     * CPU cycles until the record is unlocked.
     * 
     * @param recNo the record number to lock
     * 
     * @return an identification security token
     * 
     * @throws RecordNotFoundException if there is no such record in memory
     * 
     * @throws RemoteException if an unspecified error occurs on the server side
     */
    
    public long lock(int recNo) throws RecordNotFoundException, RemoteException;

    /**
     * Releases the lock on a record. Cookie must be the cookie returned when 
     * the record was locked; otherwise throws SecurityException.
     * 
     * @param recNo the record number to unlock
     * 
     * @param cookie the identification security token
     
     * @throws RecordNotFoundException if there is no such record in memory
     * 
     * @throws SecurityException if the record is locked with a cookie
     * other than <code>lockCookie</code>
     * 
     * @throws RemoteException if an unspecified error occurs on the server side
     */
    public void unlock(int recNo, long cookie)
            throws RecordNotFoundException, SecurityException, RemoteException;

    /**
     * Accesses all object-oriented mapped customer records listed in memory,
     * and returns them in a data structure.
     * 
     * @return a data structure containing all customer records listed in memory
     * 
     * @throws RemoteException if an unspecified error occurs on the server side
     */
    public List<CustomerRecord> getList() throws RemoteException;

}

