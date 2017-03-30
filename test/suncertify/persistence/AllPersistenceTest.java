/* 
 * DBDMLTest
 * 
 * Filipe João Mendes Rosa
 * 
 * 27/02/2007
 * 
 * Sun Certified Developer for the Java 2 Platform: Application Submission 
 * (Version 2.1.1)
 * 
 * Java SE 6 Developer Certified Master Assignment 1Z0-855
 * 
 */
package suncertify.persistence;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Filipe João Mendes Rosa
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({DBDMLTest.class, DBFileTest.class})
public class AllPersistenceTest {

    public AllPersistenceTest() {
    }

}
