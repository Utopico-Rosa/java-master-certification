/* 
 * AllDbTest
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
package suncertify.db;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Filipe João Mendes Rosa
 */
@RunWith(Suite.class)
@SuiteClasses({CustomerRecordTest.class, DataTest.class, LockManagerTest.class,
    VerifySchemaTest.class})
public class AllDbTest {

    public AllDbTest() {
    }

}
