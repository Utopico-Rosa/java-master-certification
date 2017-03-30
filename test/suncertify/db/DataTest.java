/* 
 * DataTest
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
import suncertify.persistence.DBDML;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import static org.hamcrest.Matchers.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Filipe João Mendes Rosa
 */
public class DataTest {

    public DataTest() {
    }

    @Test
    public void testSetList() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        assertNotNull(Data.getDbDML());
    }

    @Test
    public void testGetList() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        assertThat(Data.getList(), is(not(empty())));
    }

    @Test
    public void testRead() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        assertThat(Data.getList(), is(not(empty())));
        List<CustomerRecord> list = Data.getList();
        for (int i = 0; i < list.size(); ++i) {
            assertArrayEquals(list.get(i).serializeAllState(),
                    data.read(i + 1));
        }

    }

    @Test(expected = RecordNotFoundException.class)
    public void testReadThrowsRecordNotFoundException() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        List<CustomerRecord> list = Data.getList();
        for (CustomerRecord record : list) {
            record.setFlag("1");
        }
        try {
            data.read(5);
        } catch (RecordNotFoundException recordexception) {
            for (CustomerRecord record : list) {
                record.setFlag("0");
            }
            throw recordexception;
        }
    }

    @Test
    public void testLock() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        assertThat(Data.getList(), is(not(empty())));
        List<CustomerRecord> list = Data.getList();
        for (int i = 0; i < list.size(); ++i) {
            assertThat(data.lock(i + 1), greaterThan((long) 0));
        }
    }

    @Test(expected = RecordNotFoundException.class)
    public void testLockThrowsRecordNotFoundException() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        assertThat(Data.getList(), is(not(empty())));
        int listsize = Data.getList().size();
        data.lock(listsize + 1);
    }

    @Test
    public void testUnlock() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        assertThat(Data.getList(), is(not(empty())));
        List<CustomerRecord> list = Data.getList();
        long lockcookie;
        List<Long> cookielist = new ArrayList<>();
        for (int i = 0; i < list.size(); ++i) {
            lockcookie = data.lock(i + 1);
            cookielist.add(lockcookie);
        }
        for (int i = 0; i < list.size(); ++i) {
            data.unlock(i + 1, cookielist.get(i));
        }

    }

    @Test(expected = RecordNotFoundException.class)
    public void testUnlockthrowsRecordNotFOundException() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        assertThat(Data.getList(), is(not(empty())));
        List<CustomerRecord> list = Data.getList();
        int listsize = Data.getList().size();
        long lockcookie;
        List<Long> cookielist = new ArrayList<>();
        for (int i = 0; i < list.size(); ++i) {
            lockcookie = data.lock(i + 1);
            cookielist.add(lockcookie);
        }
        data.unlock(listsize + 1, 0);
    }

    @Test(expected = SecurityException.class)
    public void testUnlockThrowsSecurityException() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        assertThat(Data.getList(), is(not(empty())));
        List<CustomerRecord> list = Data.getList();
        int listsize = Data.getList().size();
        long lockcookie;
        List<Long> cookielist = new ArrayList<>();
        for (int i = 0; i < list.size(); ++i) {
            lockcookie = data.lock(i + 1);
            cookielist.add(lockcookie);
        }
        data.unlock(1, cookielist.get(0) + 1);
    }

    @Test
    public void TestUpdate() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        assertThat(Data.getList(), is(not(empty())));
        List<CustomerRecord> list = Data.getList();
        List<String[]> arrayslist = new ArrayList<>();
        for (CustomerRecord record : list) {
            arrayslist.add(record.serializeAllState());
        }
        for (String[] arrays : arrayslist) {
            arrays[5] = "55555555";
        }
        long lockcookie;
        List<Long> cookielist = new ArrayList<>();
        for (int i = 0; i < list.size(); ++i) {
            lockcookie = data.lock(i + 1);
            cookielist.add(lockcookie);
        }
        for (int i = 0; i < list.size(); ++i) {
            data.update(i + 1, arrayslist.get(i), cookielist.get(i));
        }
        for (CustomerRecord record : list) {
            assertTrue(record.getOwner().equals("55555555"));
        }
        for (CustomerRecord record : list) {
            record.setOwner("        ");
        }

    }

    @Test(expected = RecordNotFoundException.class)
    public void testUpdateThrowsRecordNotFoundException() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        assertThat(Data.getList(), is(not(empty())));
        List<CustomerRecord> list = Data.getList();
        List<String[]> arrayslist = new ArrayList<>();
        for (CustomerRecord record : list) {
            arrayslist.add(record.serializeAllState());
        }
        for (String[] arrays : arrayslist) {
            arrays[5] = "55555555";
        }
        long lockcookie;
        List<Long> cookielist = new ArrayList<>();
        for (int i = 0; i < list.size(); ++i) {
            lockcookie = data.lock(i + 1);
            cookielist.add(lockcookie);
        }
        for (int i = 0; i < list.size(); ++i) {
            data.update(list.size() + 1, arrayslist.get(i), cookielist.get(i));
        }

    }

    @Test(expected = BookingException.class)
    public void testUpdateThrowsBookingException() throws Exception {

        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        assertThat(Data.getList(), is(not(empty())));
        List<CustomerRecord> list = Data.getList();
        List<String[]> arrayslist = new ArrayList<>();
        for (CustomerRecord record : list) {
            arrayslist.add(record.serializeAllState());
        }
        for (String[] arrays : arrayslist) {
            arrays[5] = "55555555";
        }
        long lockcookie;
        List<Long> cookielist = new ArrayList<>();
        for (int i = 0; i < list.size(); ++i) {
            lockcookie = data.lock(i + 1);
            cookielist.add(lockcookie);
        }
        for (int i = 0; i < list.size(); ++i) {
            data.update(i + 1, arrayslist.get(i), cookielist.get(i));
            data.update(i + 1, arrayslist.get(i), cookielist.get(i));
        }

    }

    @Test(expected = SecurityException.class)
    public void testUpdateThrowsSecurityException() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        assertThat(Data.getList(), is(not(empty())));
        List<CustomerRecord> list = Data.getList();
        List<String[]> arrayslist = new ArrayList<>();
        for (CustomerRecord record : list) {
            arrayslist.add(record.serializeAllState());
        }
        for (String[] arrays : arrayslist) {
            arrays[5] = "55555555";
        }
        long lockcookie;
        List<Long> cookielist = new ArrayList<>();
        for (int i = 0; i < list.size(); ++i) {
            lockcookie = data.lock(i + 1);
            cookielist.add(lockcookie);
        }
        for (int i = 0; i < list.size(); ++i) {
            data.update(i + 1, arrayslist.get(i), cookielist.get(i) + 1);
        }
    }

    @Test
    public void testDelete() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        assertThat(Data.getList(), is(not(empty())));
        List<CustomerRecord> list = Data.getList();
        List<Long> cookielist = new ArrayList<>();
        long lockcookie;
        for (int i = 0; i < list.size(); ++i) {
            lockcookie = data.lock(i + 1);
            cookielist.add(lockcookie);
        }
        for (int i = 0; i < list.size(); ++i) {
            data.delete(i + 1, cookielist.get(i));
        }
        for (CustomerRecord record : list) {
            assertTrue(record.getFlag().equals("1"));
        }
        for (CustomerRecord record : list) {
            record.setFlag("0");
        }
    }

    @Test(expected = RecordNotFoundException.class)
    public void testDeleteThrowsRecordNotFoundException() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        assertThat(Data.getList(), is(not(empty())));
        List<CustomerRecord> list = Data.getList();
        List<Long> cookielist = new ArrayList<>();
        long lockcookie;
        for (int i = 0; i < list.size(); ++i) {
            lockcookie = data.lock(i + 1);
            cookielist.add(lockcookie);
        }
        for (int i = 0; i < list.size(); ++i) {
            data.delete(list.size() + 1, cookielist.get(i));
        }

    }

    @Test(expected = RecordNotFoundException.class)
    public void testDeleteThrowsRecordNotFoundException2() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        assertThat(Data.getList(), is(not(empty())));
        List<CustomerRecord> list = Data.getList();
        List<Long> cookielist = new ArrayList<>();
        long lockcookie;
        for (int i = 0; i < list.size(); ++i) {
            lockcookie = data.lock(i + 1);
            cookielist.add(lockcookie);
        }
        for (int i = 0; i < list.size(); ++i) {
            data.delete(i + 1, cookielist.get(i));
        }

        for (int i = 0; i < list.size(); ++i) {
            try {
                data.delete(i + 1, cookielist.get(i));
            } catch (RecordNotFoundException recordexception) {
                for (CustomerRecord record : list) {
                    record.setFlag("0");
                }
                throw recordexception;
            }
        }
    }

    @Test(expected = SecurityException.class)
    public void testDeleteThrowsSecurityException() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        assertThat(Data.getList(), is(not(empty())));
        List<CustomerRecord> list = Data.getList();
        List<Long> cookielist = new ArrayList<>();
        long lockcookie;
        for (int i = 0; i < list.size(); ++i) {
            lockcookie = data.lock(i + 1);
            cookielist.add(lockcookie);
        }
        for (int i = 0; i < list.size(); ++i) {
            data.delete(i + 1, cookielist.get(i) + 1);
        }
    }

    @Test
    public void testFind() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        assertThat(Data.getList(), is(not(empty())));
        String[] fourmatches = {"Buonarotti", null, null, null, null, null};
        int[] fourmatchesrecordnumbers = {1, 6, 19, 26};
        String[] twomatches = {null, "Small", null, null, null, null};
        int[] twomatchesrecordnumbers = {1, 2};
        String[] fourmatches_ = {null, null, "Air", null, null, null};
        int[] fourmatches_recordnumbers = {1, 3, 12, 18};
        String[] threematches = {null, null, null, "1", null, null};
        int[] threematchesrecordnumbers = {1, 12, 22};
        String[] twomatches_ = {null, null, null, null, "$90", null};
        int[] twomatches_recordnumbers = {4, 7};
        String[] twomatches__ = {null, "Pleasant", "Drywall", "5", null, null};
        int[] twomatches__recordnumbers = {7, 8};
        String[] onematch = {null, "Pleasant", "Drywall", "5", "$90", null};
        int[] onematchrecordnumber = {7};
        String[] twomatches___ = {null, "Digit", null, "3", "$75", null};
        int[] twomatches___recordnumbers = {10, 11};
        String[] twomatches____ = {null, "Lend", "Electrical", null, null,
            null};
        int[] twomatches____recordnumbers = {28, 29};
        assertTrue(data.find(fourmatches).length == 4);
        assertTrue(data.find(twomatches).length == 2);
        assertTrue(data.find(fourmatches_).length == 4);
        assertTrue(data.find(threematches).length == 3);
        assertTrue(data.find(twomatches_).length == 2);
        assertTrue(data.find(twomatches__).length == 2);
        assertTrue(data.find(onematch).length == 1);
        assertTrue(data.find(twomatches___).length == 2);
        assertTrue(data.find(twomatches____).length == 2);
        assertArrayEquals(data.find(fourmatches), fourmatchesrecordnumbers);
        assertArrayEquals(data.find(twomatches), twomatchesrecordnumbers);
        assertArrayEquals(data.find(fourmatches_), fourmatches_recordnumbers);
        assertArrayEquals(data.find(threematches), threematchesrecordnumbers);
        assertArrayEquals(data.find(twomatches_), twomatches_recordnumbers);
        assertArrayEquals(data.find(twomatches__), twomatches__recordnumbers);
        assertArrayEquals(data.find(onematch), onematchrecordnumber);
        assertArrayEquals(data.find(twomatches___), twomatches___recordnumbers);
        assertArrayEquals(data.find(twomatches____),
                twomatches____recordnumbers);
    }

    @Test
    public void testFindNoResults() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        assertThat(Data.getList(), is(not(empty())));
        List<CustomerRecord> list = Data.getList();
        List<Long> cookielist = new ArrayList<>();
        long lockcookie;
        for (int i = 0; i < list.size(); ++i) {
            lockcookie = data.lock(i + 1);
            cookielist.add(lockcookie);
        }
        for (int i = 0; i < list.size(); ++i) {
            data.delete(i + 1, cookielist.get(i));
        }
        String[] fourmatches = {"Buonarotti", null, null, null, null, null};
        String[] twomatches = {null, "Small", null, null, null, null};
        String[] fourmatches_ = {null, null, "Air", null, null, null};
        String[] threematches = {null, null, null, "1", null, null};
        String[] twomatches_ = {null, null, null, null, "$90", null};
        String[] twomatches__ = {null, "Pleasant", "Drywall", "5", null, null};
        String[] onematch = {null, "Pleasant", "Drywall", "5", "$90", null};
        String[] twomatches___ = {null, "Digit", null, "3", "$75", null};
        String[] twomatches____ = {null, "Lend", "Electrical", null, null,
            null};
        assertTrue(data.find(fourmatches).length == 0);
        assertTrue(data.find(twomatches).length == 0);
        assertTrue(data.find(fourmatches_).length == 0);
        assertTrue(data.find(threematches).length == 0);
        assertTrue(data.find(twomatches_).length == 0);
        assertTrue(data.find(twomatches__).length == 0);
        assertTrue(data.find(onematch).length == 0);
        assertTrue(data.find(twomatches___).length == 0);
        assertTrue(data.find(twomatches____).length == 0);
        for (CustomerRecord record : list) {
            record.setFlag("0");
        }
    }

    @Test
    public void testfindByNameAndLocation() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        assertThat(Data.getList(), is(not(empty())));
        String[] fourmatches = {"Buonarotti & Company", null, null, null, null,
            null};
        int[] fourmatchesrecordnumbers = {1, 6, 19, 26};
        String[] threematches = {"Swanders & Flaughn", null, null, null, null,
            null};
        int[] threematchesrecordnumbers = {2, 13, 23};
        String[] fifthmatches = {"Moore Power Tool Ya", null, null, null, null,
            null};
        int[] fifthmatchesrecordnumbers = {3, 12, 22, 24, 29};
        String[] twomatches = {"Hamner & Tong", null, null, null, null, null};
        int[] twomatchesrecordnumbers = {4, 7};
        String[] fifthmatches_ = {"Bitter Homes & Gardens", null, null, null,
            null, null};
        int[] fifthmatches_recordnumbers = {5, 8, 9, 18, 20,};
        String[] threematches_ = {"Fred & Nobby", null, null, null, null, null};
        int[] threematches_recordnumbers = {10, 16, 21};
        String[] threematches__ = {"Dogs With Tools", null, null, null, null,
            null};
        int[] threematches__recordnumbers = {11, 15, 27};
        String[] fourmatches_ = {"Philharmonic Remodeling", null, null, null,
            null, null};
        int[] fourmatches_recordnumbers = {14, 17, 25, 28};
        String[] twomatches_ = {null, "Smallville", null, null, null, null};
        int[] twomatches_recordnumbers = {1, 2};
        String[] twomatches__ = {null, "Whoville", null, null, null, null};
        int[] twomatches__recordnumbers = {3, 4};
        String[] twomatches___ = {null, "Metropolis", null, null, null, null};
        int[] twomatches___recordnumbers = {5, 6};
        String[] twomatches____ = {null, "Pleasantville", null, null, null,
            null};
        int[] twomatches____recordnumbers = {7, 8};
        String[] threematches___ = {null, "Digitopolis", null, null, null,
            null};
        int[] threematches___recordnumbers = {9, 10, 11};
        String[] threematches____ = {null, "Atlantis", null, null, null, null};
        int[] threematches____recordnumbers = {12, 13, 14};
        String[] threematches_____ = {null, "EmeraldCity", null, null, null,
            null};
        int[] threematches_____recordnumbers = {15, 16, 17};
        String[] twomatches_____ = {null, "Bali Hai", null, null, null, null};
        int[] twomatches_____recordnumbers = {18, 19};
        String[] threematches______ = {null, "Xanadu", null, null, null, null};
        int[] threematches______recordnumbers = {20, 21, 22};
        String[] twomatches______ = {null, "Paravel", null, null, null, null};
        int[] twomatches______recordnumbers = {23, 24};
        String[] twomatches_______ = {null, "Hobbiton", null, null, null, null};
        int[] twomatches_______recordnumbers = {25, 26};
        String[] threematches_______ = {null, "Lendmarch", null, null, null,
            null};
        int[] threematches_______recordnumbers = {27, 28, 29};
        String[] onematch = {"Bitter Homes & Gardens", "Metropolis", null, null,
            null, null};
        int[] onematchrecordnumbers = {5};

        assertTrue(data.findByNameAndLocation(fourmatches).length == 4);
        assertTrue(data.findByNameAndLocation(threematches).length == 3);
        assertTrue(data.findByNameAndLocation(fifthmatches).length == 5);
        assertTrue(data.findByNameAndLocation(twomatches).length == 2);
        assertTrue(data.findByNameAndLocation(fifthmatches_).length == 5);
        assertTrue(data.findByNameAndLocation(threematches_).length == 3);
        assertTrue(data.findByNameAndLocation(threematches__).length == 3);
        assertTrue(data.findByNameAndLocation(fourmatches_).length == 4);
        assertTrue(data.findByNameAndLocation(twomatches_).length == 2);
        assertTrue(data.findByNameAndLocation(twomatches__).length == 2);
        assertTrue(data.findByNameAndLocation(twomatches___).length == 2);
        assertTrue(data.findByNameAndLocation(twomatches____).length == 2);
        assertTrue(data.findByNameAndLocation(threematches___).length == 3);
        assertTrue(data.findByNameAndLocation(threematches____).length == 3);
        assertTrue(data.findByNameAndLocation(threematches_____).length == 3);
        assertTrue(data.findByNameAndLocation(twomatches_____).length == 2);
        assertTrue(data.findByNameAndLocation(threematches______).length == 3);
        assertTrue(data.findByNameAndLocation(twomatches______).length == 2);
        assertTrue(data.findByNameAndLocation(twomatches_______).length == 2);
        assertTrue(data.findByNameAndLocation(threematches_______).length == 3);
        assertTrue(data.findByNameAndLocation(onematch).length == 1);
        assertArrayEquals(data.findByNameAndLocation(fourmatches),
                fourmatchesrecordnumbers);
        assertArrayEquals(data.findByNameAndLocation(threematches),
                threematchesrecordnumbers);
        assertArrayEquals(data.findByNameAndLocation(fifthmatches),
                fifthmatchesrecordnumbers);
        assertArrayEquals(data.findByNameAndLocation(twomatches),
                twomatchesrecordnumbers);
        assertArrayEquals(data.findByNameAndLocation(fifthmatches_),
                fifthmatches_recordnumbers);
        assertArrayEquals(data.findByNameAndLocation(threematches_),
                threematches_recordnumbers);
        assertArrayEquals(data.findByNameAndLocation(threematches__),
                threematches__recordnumbers);
        assertArrayEquals(data.findByNameAndLocation(fourmatches_),
                fourmatches_recordnumbers);
        assertArrayEquals(data.findByNameAndLocation(twomatches_),
                twomatches_recordnumbers);
        assertArrayEquals(data.findByNameAndLocation(twomatches__),
                twomatches__recordnumbers);
        assertArrayEquals(data.findByNameAndLocation(twomatches___),
                twomatches___recordnumbers);
        assertArrayEquals(data.findByNameAndLocation(twomatches____),
                twomatches____recordnumbers);
        assertArrayEquals(data.findByNameAndLocation(threematches___),
                threematches___recordnumbers);
        assertArrayEquals(data.findByNameAndLocation(threematches____),
                threematches____recordnumbers);
        assertArrayEquals(data.findByNameAndLocation(threematches_____),
                threematches_____recordnumbers);
        assertArrayEquals(data.findByNameAndLocation(twomatches_____),
                twomatches_____recordnumbers);
        assertArrayEquals(data.findByNameAndLocation(threematches______),
                threematches______recordnumbers);
        assertArrayEquals(data.findByNameAndLocation(twomatches______),
                twomatches______recordnumbers);
        assertArrayEquals(data.findByNameAndLocation(twomatches_______),
                twomatches_______recordnumbers);
        assertArrayEquals(data.findByNameAndLocation(threematches_______),
                threematches_______recordnumbers);
        assertArrayEquals(data.findByNameAndLocation(onematch),
                onematchrecordnumbers);
    }

    @Test
    public void testfindByNameAndLocationNoResults() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        assertThat(Data.getList(), is(not(empty())));
        List<CustomerRecord> list = Data.getList();
        List<Long> cookielist = new ArrayList<>();
        long lockcookie;
        for (int i = 0; i < list.size(); ++i) {
            lockcookie = data.lock(i + 1);
            cookielist.add(lockcookie);
        }
        for (int i = 0; i < list.size(); ++i) {
            data.delete(i + 1, cookielist.get(i));
        }

        String[] fourmatches = {"Buonarotti & Company", null, null, null, null,
            null};
        String[] threematches = {"Swanders & Flaughn", null, null, null, null,
            null};
        String[] fifthmatches = {"Moore Power Tool Ya", null, null, null, null,
            null};
        String[] twomatches = {"Hamner & Tong", null, null, null, null, null};
        String[] fifthmatches_ = {"Bitter Homes & Gardens", null, null, null,
            null, null};
        String[] threematches_ = {"Fred & Nobby", null, null, null, null, null};
        String[] threematches__ = {"Dogs With Tools", null, null, null, null,
            null};
        String[] fourmatches_ = {"Philharmonic Remodeling", null, null, null,
            null, null};
        String[] twomatches_ = {null, "Smallville", null, null, null, null};
        String[] twomatches__ = {null, "Whoville", null, null, null, null};
        String[] twomatches___ = {null, "Metropolis", null, null, null, null};
        String[] twomatches____ = {null, "Pleasantville", null, null, null,
            null};
        String[] threematches___ = {null, "Digitopolis", null, null, null,
            null};
        String[] threematches____ = {null, "Atlantis", null, null, null, null};
        String[] threematches_____ = {null, "EmeraldCity", null, null, null,
            null};
        String[] twomatches_____ = {null, "Bali Hai", null, null, null, null};
        String[] threematches______ = {null, "Xanadu", null, null, null, null};
        String[] twomatches______ = {null, "Paravel", null, null, null, null};
        String[] twomatches_______ = {null, "Hobbiton", null, null, null, null};
        String[] threematches_______ = {null, "Lendmarch", null, null, null,
            null};
        String[] onematch = {"Bitter Homes & Gardens", "Metropolis", null, null,
            null, null};

        assertTrue(data.findByNameAndLocation(fourmatches).length == 0);
        assertTrue(data.findByNameAndLocation(threematches).length == 0);
        assertTrue(data.findByNameAndLocation(fifthmatches).length == 0);
        assertTrue(data.findByNameAndLocation(twomatches).length == 0);
        assertTrue(data.findByNameAndLocation(fifthmatches_).length == 0);
        assertTrue(data.findByNameAndLocation(threematches_).length == 0);
        assertTrue(data.findByNameAndLocation(threematches__).length == 0);
        assertTrue(data.findByNameAndLocation(fourmatches_).length == 0);
        assertTrue(data.findByNameAndLocation(twomatches_).length == 0);
        assertTrue(data.findByNameAndLocation(twomatches__).length == 0);
        assertTrue(data.findByNameAndLocation(twomatches___).length == 0);
        assertTrue(data.findByNameAndLocation(twomatches____).length == 0);
        assertTrue(data.findByNameAndLocation(threematches___).length == 0);
        assertTrue(data.findByNameAndLocation(threematches____).length == 0);
        assertTrue(data.findByNameAndLocation(threematches_____).length == 0);
        assertTrue(data.findByNameAndLocation(twomatches_____).length == 0);
        assertTrue(data.findByNameAndLocation(threematches______).length == 0);
        assertTrue(data.findByNameAndLocation(twomatches______).length == 0);
        assertTrue(data.findByNameAndLocation(twomatches_______).length == 0);
        assertTrue(data.findByNameAndLocation(threematches_______).length == 0);
        assertTrue(data.findByNameAndLocation(onematch).length == 0);
        for (CustomerRecord record : list) {
            record.setFlag("0");
        }
    }

    @Test
    public void testCreate() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        assertThat(Data.getList(), is(not(empty())));
        int listsize = Data.getList().size();
        byte[] namebytes = new byte[32];
        byte[] locationbytes = new byte[64];
        byte[] specialtiesbytes = new byte[64];
        byte[] sizebytes = new byte[6];
        byte[] ratebytes = new byte[8];
        byte[] ownerbytes = new byte[8];
        namebytes[0] = locationbytes[0] = specialtiesbytes[0] = sizebytes[0]
                = ratebytes[0] = ownerbytes[0] = 101;
        namebytes[1] = locationbytes[1] = specialtiesbytes[1] = sizebytes[1]
                = ratebytes[1] = ownerbytes[1] = 116;
        namebytes[2] = locationbytes[2] = specialtiesbytes[2] = sizebytes[2]
                = ratebytes[2] = ownerbytes[2] = 99;
        String name = new String(namebytes, StandardCharsets.US_ASCII);
        String location = new String(locationbytes, StandardCharsets.US_ASCII);
        String specialties = new String(specialtiesbytes,
                StandardCharsets.US_ASCII);
        String size = new String(sizebytes, StandardCharsets.US_ASCII);
        String rate = new String(ratebytes, StandardCharsets.US_ASCII);
        String owner = new String(ownerbytes, StandardCharsets.US_ASCII);
        String[] datafields = new String[6];
        datafields[0] = name;
        datafields[1] = location;
        datafields[2] = specialties;
        datafields[3] = size;
        datafields[4] = rate;
        datafields[5] = owner;
        int recordnumber = data.create(datafields);
        assertEquals(listsize + 1, recordnumber);
        
        Field mapfield = Data.class.getDeclaredField("map");
        mapfield.setAccessible(true);
        ConcurrentSkipListMap<Integer, ConcurrentMap<Long, CustomerRecord>> map
                = ((ConcurrentSkipListMap<Integer, 
                ConcurrentMap<Long, CustomerRecord>>) mapfield.get(null));
        map.remove(recordnumber);
    }

    @Test(expected = DuplicateKeyException.class)
    public void testCreatethrowsDuplicateKeyException() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        assertThat(Data.getList(), is(not(empty())));
        int listsize = Data.getList().size();
        byte[] namebytes = new byte[32];
        byte[] locationbytes = new byte[64];
        byte[] specialtiesbytes = new byte[64];
        byte[] sizebytes = new byte[6];
        byte[] ratebytes = new byte[8];
        byte[] ownerbytes = new byte[8];
        namebytes[0] = 70;
        namebytes[1] = 114;
        namebytes[2] = 101;
        namebytes[3] = 100;
        namebytes[4] = 32;
        namebytes[5] = 38;
        namebytes[6] = 32;
        namebytes[7] = 78;
        namebytes[8] = 111;
        namebytes[9] = 98;
        namebytes[10] = 98;
        namebytes[11] = 121;
        locationbytes[0] = 88;
        locationbytes[1] = 97;
        locationbytes[2] = 110;
        locationbytes[3] = 97;
        locationbytes[4] = 100;
        locationbytes[5] = 117;
        specialtiesbytes[0] = sizebytes[0] = ratebytes[0] = ownerbytes[0] = 101;
        specialtiesbytes[1] = sizebytes[1] = ratebytes[1] = ownerbytes[1] = 116;
        specialtiesbytes[2] = sizebytes[2] = ratebytes[2] = ownerbytes[2] = 99;
        String name = new String(namebytes, StandardCharsets.US_ASCII);
        String location = new String(locationbytes, StandardCharsets.US_ASCII);
        String specialties = new String(specialtiesbytes,
                StandardCharsets.US_ASCII);
        String size = new String(sizebytes, StandardCharsets.US_ASCII);
        String rate = new String(ratebytes, StandardCharsets.US_ASCII);
        String owner = new String(ownerbytes, StandardCharsets.US_ASCII);
        String[] datafields = new String[6];
        datafields[0] = name;
        datafields[1] = location;
        datafields[2] = specialties;
        datafields[3] = size;
        datafields[4] = rate;
        datafields[5] = owner;
        int recordnumber = data.create(datafields);
    }

    @Test
    public void createOnTopOfDeletedRecord() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));

        Field mapfield = Data.class.getDeclaredField("map");
        mapfield.setAccessible(true);
        ConcurrentSkipListMap<Integer, ConcurrentMap<Long, CustomerRecord>> map
                = ((ConcurrentSkipListMap<Integer, ConcurrentMap<Long, CustomerRecord>>) mapfield.
                get(null));
        Collection<ConcurrentMap<Long, CustomerRecord>> collection = map.
                values();
        ConcurrentMap<Long, CustomerRecord> first = map.get(1);
        String[] state = null;
        for (CustomerRecord record : first.values()) {
            state = record.serializeAllState();
        }
        List<String> statelist = new ArrayList<>();
        statelist.add("0");
        statelist.addAll(Arrays.asList(state));
        CustomerRecord clonedcustomer = new CustomerRecord(statelist);

        Data data = Data.getInstance();
        assertThat(Data.getList(), is(not(empty())));
        List<CustomerRecord> list = Data.getList();
        int listsize = list.size();
        List<Long> cookielist = new ArrayList<>();
        long lockcookie;
        for (int i = 0; i < list.size(); ++i) {
            lockcookie = data.lock(i + 1);
            cookielist.add(lockcookie);
        }
        for (int i = 0; i < list.size(); ++i) {
            data.delete(i + 1, cookielist.get(i));
        }
        byte[] namebytes = new byte[32];
        byte[] locationbytes = new byte[64];
        byte[] specialtiesbytes = new byte[64];
        byte[] sizebytes = new byte[6];
        byte[] ratebytes = new byte[8];
        byte[] ownerbytes = new byte[8];
        namebytes[0] = locationbytes[0] = specialtiesbytes[0] = sizebytes[0]
                = ratebytes[0] = ownerbytes[0] = 101;
        namebytes[1] = locationbytes[1] = specialtiesbytes[1] = sizebytes[1]
                = ratebytes[1] = ownerbytes[1] = 116;
        namebytes[2] = locationbytes[2] = specialtiesbytes[2] = sizebytes[2]
                = ratebytes[2] = ownerbytes[2] = 99;
        String name = new String(namebytes, StandardCharsets.US_ASCII);
        String location = new String(locationbytes, StandardCharsets.US_ASCII);
        String specialties = new String(specialtiesbytes,
                StandardCharsets.US_ASCII);
        String size = new String(sizebytes, StandardCharsets.US_ASCII);
        String rate = new String(ratebytes, StandardCharsets.US_ASCII);
        String owner = new String(ownerbytes, StandardCharsets.US_ASCII);
        String[] datafields = new String[6];
        datafields[0] = name;
        datafields[1] = location;
        datafields[2] = specialties;
        datafields[3] = size;
        datafields[4] = rate;
        datafields[5] = owner;
        int recordnumber = data.create(datafields);
        assertThat(recordnumber, allOf(greaterThanOrEqualTo(1),
                lessThanOrEqualTo(listsize)));

        first = map.get(1);
        first.clear();
        first.put((long) 0, clonedcustomer);
        for (ConcurrentMap<Long, CustomerRecord> recordmap : collection) {
            for (CustomerRecord record : recordmap.values()) {
                record.setFlag("0");
                record.setOwner("        ");
            }
        }

    }

}
