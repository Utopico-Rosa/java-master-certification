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

import java.util.List;
import org.junit.Test;
import suncertify.db.CustomerRecord;
import static org.junit.Assert.*;

/**
 * @author Filipe João Mendes Rosa
 */
public class DBDMLTest {

    public DBDMLTest() {
    }

    @Test
    public void testFetchPersistFetchComparison() throws Exception {
        DBDML dbdml = new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db"));
        List<CustomerRecord> list = dbdml.fetch();
        for (CustomerRecord record : list) {
            record.setFlag("0");
            record.setOwner("        ");
        }
        dbdml.persist(list);
        list = dbdml.fetch();
        for (CustomerRecord record : list) {
            assertTrue(record.getFlag().equals("0"));
            assertTrue(record.getOwner().equals(
                    "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000"));
        }
    }

}
