/* 
 * CustomerRecordTest
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
import org.junit.Test;
import static org.junit.Assert.*;
import static suncertify.db.DBSchema.*;

/**
 * @author Filipe João Mendes Rosa
 */
public class CustomerRecordTest {

    public CustomerRecordTest() {
    }

    @Test
    public void testDataNotNull() {
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
        assertNotNull(record.getFlag());
        assertNotNull(record.getName());
        assertNotNull(record.getLocation());
        assertNotNull(record.getSpecialties());
        assertNotNull(record.getSize());
        assertNotNull(record.getRate());
        assertNotNull(record.getOwner());
    }

    @Test
    public void testDataEnteredisNUll() {
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
        flag = null;
        name = null;
        location = null;
        specialties = null;
        size = null;
        rate = null;
        owner = null;
        record.setFlag(flag);
        record.setName(name);
        record.setLocation(location);
        record.setSpecialties(specialties);
        record.setSize(size);
        record.setRate(rate);
        record.setOwner(owner);
        assertNotEquals(null, record.getFlag());
        assertNotEquals(null, record.getName());
        assertNotEquals(null, record.getLocation());
        assertNotEquals(null, record.getSpecialties());
        assertNotEquals(null, record.getSize());
        assertNotEquals(null, record.getRate());
        assertNotEquals(null, record.getOwner());
    }

    @Test
    public void TestDataEnteredIsOfIncorrectLength() {
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
        flagbytes = new byte[25];
        namebytes = new byte[25];
        locationbytes = new byte[25];
        specialtiesbytes = new byte[25];
        sizebytes = new byte[25];
        ratebytes = new byte[25];
        ownerbytes = new byte[25];
        flag = new String(flagbytes, StandardCharsets.US_ASCII);
        name = new String(namebytes, StandardCharsets.US_ASCII);
        location = new String(locationbytes, StandardCharsets.US_ASCII);
        specialties = new String(specialtiesbytes, StandardCharsets.US_ASCII);
        size = new String(sizebytes, StandardCharsets.US_ASCII);
        rate = new String(ratebytes, StandardCharsets.US_ASCII);
        owner = new String(ownerbytes, StandardCharsets.US_ASCII);
        record.setFlag(flag);
        record.setName(name);
        record.setLocation(location);
        record.setSpecialties(specialties);
        record.setSize(size);
        record.setRate(rate);
        record.setOwner(owner);
        assertNotEquals(flag.length(), record.getFlag().length());
        assertNotEquals(name.length(), record.getName().length());
        assertNotEquals(location.length(), record.getLocation().length());
        assertNotEquals(specialties.length(), record.getSpecialties().length());
        assertNotEquals(size.length(), record.getSize().length());
        assertNotEquals(rate.length(), record.getRate().length());
        assertNotEquals(owner.length(), record.getOwner().length());
    }

    @Test
    public void TestserializeAllState() {
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
        assertEquals(6, record.serializeAllState().length);
    }

    @Test
    public void testSerializeNameAndLocation() {
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
        String[] serializednamelocation = record.serializeNameAndLocation();
        assertNotNull(serializednamelocation[0]);
        assertNotNull(serializednamelocation[1]);
        assertNull(serializednamelocation[2]);
        assertNull(serializednamelocation[3]);
        assertNull(serializednamelocation[4]);
        assertNull(serializednamelocation[5]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setStatethrowsIllegalArgumentException() {
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
        record.setState(list.toArray(new String[7]));

    }

    @Test
    public void testToString() {
        byte[] flagbytes = new byte[1];
        byte[] namebytes = new byte[SUBCONTRACTOR_NAME_LENGTH];
        byte[] locationbytes = new byte[CITY_LENGTH];
        byte[] specialtiesbytes = new byte[TYPES_OF_WORK_PERFORMED_LENGTH];
        byte[] sizebytes = new byte[NUMBER_OF_STAFF_IN_ORGANIZATION_LENGTH];
        byte[] ratebytes = new byte[HOURLY_CHARGE_LENGTH];
        byte[] ownerbytes = new byte[CUSTOMER_HOLDING_THIS_RECORD_LENGTH];
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
        String objectString = record.toString();
        assertTrue(objectString.contains(flag));
        assertTrue(objectString.contains(name));
        assertTrue(objectString.contains(location));
        assertTrue(objectString.contains(specialties));
        assertTrue(objectString.contains(size));
        assertTrue(objectString.contains(rate));
        assertTrue(objectString.contains(owner));
    }

}
