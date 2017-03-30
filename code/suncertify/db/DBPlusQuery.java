/* 
 * DBPlusQuery
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
 * Enhances <code>DB</code>'s use cases with another not originally
 * contemplated. <code>Data</code>, the singleton cache which implements the
 * business logic's use cases implements <code>DB</code> and <code>DBPlusQuery.
 * </code>
 * 
 * @see DB
 * 
 * @see Data
 * 
 * @author Filipe João Mendes Rosa
 */
public interface DBPlusQuery {

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
     */
    public int[] findByNameAndLocation(String[] criteria);
}
