/* 
 * RecordNotFoundException
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

/**
 * Indicates that a customer record doesn't exist, or is deemed as deleted.
 *
 * @author Filipe João Mendes Rosa
 * 
 * @see DBSchema#DELETED_RECORD
 */

public class RecordNotFoundException extends Exception {
    
    /**
     * Used during deserialization to verify that the sender and receiver of a 
     * serialized object have loaded classes for that object that are compatible
     * with respect to serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates the <code>Exception</code> object.
     */
    public RecordNotFoundException() {
    }

    /**
     * Creates the <code>Exception</code> object, initialized with a detail
     * message.
     * 
     * @param msg the detail message
     */
    public RecordNotFoundException(String msg) {
        super(msg);
    }

    /**
     * Creates the <code>Exception</code> object, initialized with a previous 
     * cause, which is a <code>Throwable</code> thrown before this <code>
     * Exception</code>.
     * 
     * @param throwable a <code>Throwable</code> thrown before this Exception
     */
    public RecordNotFoundException(Throwable throwable) {
        super(throwable);
    }

    /**
     * Creates the <code>Exception</code> object, initialized with a detail 
     * message, and with a previous cause, which is a <code>Throwable</code> 
     * thrown before this <code>Exception</code>.
     * 
     * @param msg the detail message
     * 
     * @param throwable a <code>Throwable</code> thrown before this Exception
     */
    public RecordNotFoundException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
