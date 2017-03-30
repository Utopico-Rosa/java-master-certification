/* 
 * DuplicateKeyException
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
 * Indicates an attempt to insert a new customer record, with a repeated client 
 * company name and geographical location.
 *
 * @author Filipe João Mendes Rosa
 */

public class DuplicateKeyException extends Exception {
    
    /**
     * Used during deserialization to verify that the sender and receiver of a 
     * serialized object have loaded classes for that object that are compatible
     * with respect to serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates the <code>Exception</code> object.
     */
    public DuplicateKeyException() {
    }

    /**
     * Creates the <code>Exception</code> object, initialized with a detail
     * message.
     * 
     * @param msg the detail message
     */
    public DuplicateKeyException(String msg) {
        super(msg);
    }

    /**
     * Creates the <code>Exception</code> object, initialized with a previous 
     * cause, which is a <code>Throwable</code> thrown before this <code>
     * Exception</code>.
     * 
     * @param throwable a <code>Throwable</code> thrown before this Exception
     */
    public DuplicateKeyException(Throwable throwable) {
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
    public DuplicateKeyException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
