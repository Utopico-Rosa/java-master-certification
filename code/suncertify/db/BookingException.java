/* 
 * BookingException
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
 * Indicates that a customer record is already booked.
 * 
 * @author Filipe João Mendes Rosa
 */
public class BookingException extends RuntimeException {
    
    /**
     * Used during deserialization to verify that the sender and receiver of a 
     * serialized object have loaded classes for that object that are compatible
     * with respect to serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * No arguments constructor. Does nothing besides creating the object.
     */
    public BookingException() {
    }

    /**
     * Creates the <code>RuntimeException</code> with a detail message. 
     * 
     * @param msg the detail message
     */
    public BookingException(String msg) {
        super(msg);
    }

    /**
     * Creates the <code>RuntimeException</code> with an underlying cause, which
     * means than another <code>Throwable</code> was thrown before.
     * 
     * @param throwable the earlier <code>Throwable</code> that was thrown
     */
    public BookingException(Throwable throwable) {
        super(throwable);
    }

    /**
     * Creates the <code>RuntimeException</code> with a detail message, and 
     * an underlying cause, which means than another <code>Throwable</code> was 
     * thrown before.
     * 
     * @param msg the detail message
     * 
     * @param throwable the earlier <code>Throwable</code> that was thrown
     */
    public BookingException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
