/* 
 * AllTest
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
package suncertify.all;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import suncertify.integration.AllIntegrationTest;
import suncertify.db.AllDbTest;
import suncertify.persistence.AllPersistenceTest;
import suncertify.presentation.AllPresentationTest;

/**
 * @author Filipe João Mendes Rosa
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({AllPersistenceTest.class, AllDbTest.class,
    AllIntegrationTest.class, AllPresentationTest.class})
public class AllTest {

    public AllTest() {
    }

}
