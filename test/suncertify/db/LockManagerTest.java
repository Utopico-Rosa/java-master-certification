/* 
 * LockManagerTest
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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.junit.Test;
import static org.junit.Assert.*;
import static suncertify.db.DBSchema.*;

/**
 * @author Filipe João Mendes Rosa
 */
public class LockManagerTest {

    public LockManagerTest() {
    }

    @Test(expected = SecurityException.class)
    public void testSecurityCheck() {
        long cookie = 2;
        byte[] flagbytes = new byte[1];
        byte[] namebytes = new byte[SUBCONTRACTOR_NAME_LENGTH];
        byte[] locationbytes = new byte[CITY_LENGTH];
        byte[] specialtiesbytes = new byte[TYPES_OF_WORK_PERFORMED_LENGTH];
        byte[] sizebytes = new byte[NUMBER_OF_STAFF_IN_ORGANIZATION_LENGTH];
        byte[] ratebytes = new byte[HOURLY_CHARGE_LENGTH];
        byte[] ownerbytes = new byte[CUSTOMER_HOLDING_THIS_RECORD_LENGTH];
        String flag = new String(flagbytes, StandardCharsets.US_ASCII);
        String name = new String(namebytes, StandardCharsets.US_ASCII);
        String location = new String(locationbytes, StandardCharsets.US_ASCII);
        String specialties = new String(specialtiesbytes,
                StandardCharsets.US_ASCII);
        String size = new String(sizebytes, StandardCharsets.US_ASCII);
        String rate = new String(ratebytes, StandardCharsets.US_ASCII);
        String owner = new String(ownerbytes, StandardCharsets.US_ASCII);
        List<String> list = new ArrayList<>();
        list.add(flag);
        list.add(name);
        list.add(location);
        list.add(specialties);
        list.add(size);
        list.add(rate);
        list.add(owner);
        CustomerRecord record = new CustomerRecord(list);
        ConcurrentMap<Long, CustomerRecord> map = new ConcurrentHashMap<>();
        map.put((long) 1, record);
        LockManager.checkSecurity(cookie, map);
    }

    @Test
    public void testLock() {
        byte[] flagbytes = new byte[1];
        byte[] namebytes = new byte[SUBCONTRACTOR_NAME_LENGTH];
        byte[] locationbytes = new byte[CITY_LENGTH];
        byte[] specialtiesbytes = new byte[TYPES_OF_WORK_PERFORMED_LENGTH];
        byte[] sizebytes = new byte[NUMBER_OF_STAFF_IN_ORGANIZATION_LENGTH];
        byte[] ratebytes = new byte[HOURLY_CHARGE_LENGTH];
        byte[] ownerbytes = new byte[CUSTOMER_HOLDING_THIS_RECORD_LENGTH];
        String flag = new String(flagbytes, StandardCharsets.US_ASCII);
        String name = new String(namebytes, StandardCharsets.US_ASCII);
        String location = new String(locationbytes, StandardCharsets.US_ASCII);
        String specialties = new String(specialtiesbytes,
                StandardCharsets.US_ASCII);
        String size = new String(sizebytes, StandardCharsets.US_ASCII);
        String rate = new String(ratebytes, StandardCharsets.US_ASCII);
        String owner = new String(ownerbytes, StandardCharsets.US_ASCII);
        List<String> list = new ArrayList<>();
        list.add(flag);
        list.add(name);
        list.add(location);
        list.add(specialties);
        list.add(size);
        list.add(rate);
        list.add(owner);
        CustomerRecord record = new CustomerRecord(list);
        ConcurrentMap<Long, CustomerRecord> map = new ConcurrentHashMap<>();
        map.put((long) 0, record);
        long cookie = LockManager.lock(map);
        assertTrue(cookie > 0);
    }

    @Test
    public void testUnlock() {
        byte[] flagbytes = new byte[1];
        byte[] namebytes = new byte[SUBCONTRACTOR_NAME_LENGTH];
        byte[] locationbytes = new byte[CITY_LENGTH];
        byte[] specialtiesbytes = new byte[TYPES_OF_WORK_PERFORMED_LENGTH];
        byte[] sizebytes = new byte[NUMBER_OF_STAFF_IN_ORGANIZATION_LENGTH];
        byte[] ratebytes = new byte[HOURLY_CHARGE_LENGTH];
        byte[] ownerbytes = new byte[CUSTOMER_HOLDING_THIS_RECORD_LENGTH];
        String flag = new String(flagbytes, StandardCharsets.US_ASCII);
        String name = new String(namebytes, StandardCharsets.US_ASCII);
        String location = new String(locationbytes, StandardCharsets.US_ASCII);
        String specialties = new String(specialtiesbytes,
                StandardCharsets.US_ASCII);
        String size = new String(sizebytes, StandardCharsets.US_ASCII);
        String rate = new String(ratebytes, StandardCharsets.US_ASCII);
        String owner = new String(ownerbytes, StandardCharsets.US_ASCII);
        List<String> list = new ArrayList<>();
        list.add(flag);
        list.add(name);
        list.add(location);
        list.add(specialties);
        list.add(size);
        list.add(rate);
        list.add(owner);
        CustomerRecord record = new CustomerRecord(list);
        ConcurrentMap<Long, CustomerRecord> map = new ConcurrentHashMap<>();
        map.put((long) 0, record);
        long cookie = LockManager.lock(map);
        assertTrue(map.get(cookie) != null);
        assertTrue(map.get((long) 0) == null);
        LockManager.unlock(cookie, map);
        assertTrue(map.get((long) 0) != null);
        assertTrue(map.get(cookie) == null);
    }

}
