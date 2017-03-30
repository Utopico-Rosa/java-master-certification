/* 
 * DBFileTest
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

import java.io.File;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import org.hamcrest.Matchers;
import static suncertify.db.DBSchema.*;

/**
 * @author Filipe João Mendes Rosa
 */
public class DBFileTest {

    public DBFileTest() {
    }

    @Test
    public void testFileisSuitable() throws Exception {
        DBFile fileclass = new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db");
        File file = fileclass.getFile();
        assertTrue(file.canRead() && file.canWrite() && file.exists() && file.
                isFile()
                && file.length() != 0);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileisNotSuitable() throws Exception {
        DBFile fileclass = new DBFile("/home/utopico/javamaster/MyAssignment/");
    }

    @Test
    public void testHeaderCodedData() throws Exception {
        DBFile fileclass = new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db");
        fileclass.readHeader();
        assertTrue(fileclass.getMagicCookie() == MAGIC_COOKIE && fileclass.
                getRecordRowLength() == TOTAL_OVERALL_LENGTH_OF_EACH_RECORD
                && fileclass.getNumberFieldsRecord()
                == NUMBER_OF_FIELDS_IN_EACH_RECORD);
        Map<String, Short> fields = fileclass.getFields();
        assertThat(fields, Matchers.hasEntry(SUBCONTRACTOR_NAME,
                (short) SUBCONTRACTOR_NAME_LENGTH));
        assertThat(fields, Matchers.hasEntry(CITY,
                (short) CITY_LENGTH));
        assertThat(fields, Matchers.hasEntry(TYPES_OF_WORK_PERFORMED,
                (short) TYPES_OF_WORK_PERFORMED_LENGTH));
        assertThat(fields, Matchers.hasEntry(NUMBER_OF_STAFF_IN_ORGANIZATION,
                (short) NUMBER_OF_STAFF_IN_ORGANIZATION_LENGTH));
        assertThat(fields, Matchers.hasEntry(HOURLY_CHARGE,
                (short) HOURLY_CHARGE_LENGTH));
        assertThat(fields, Matchers.hasEntry(CUSTOMER_HOLDING_THIS_RECORD,
                (short) CUSTOMER_HOLDING_THIS_RECORD_LENGTH));
    }

}
