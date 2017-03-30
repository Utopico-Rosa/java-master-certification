/* 
 * VerifySchemaTest
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

import suncertify.persistence.DBFile;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Filipe João Mendes Rosa
 */
public class VerifySchemaTest {

    public VerifySchemaTest() {

    }

    @Test
    public void testVerify() throws Exception {
        SchemaVerifier verifyschema = new SchemaVerifier(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db"));
        assertTrue(verifyschema.verify());
    }
}
