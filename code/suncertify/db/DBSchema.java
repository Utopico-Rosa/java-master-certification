/* 
 * DBSchema
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
 * Provides access to the decoded data constants of the database file header. 
 *
 * @author Filipe João Mendes Rosa
 */
public class DBSchema {

    /**
     * The name of the subcontractor the record relates to.
     */
    public static final String SUBCONTRACTOR_NAME = "name";

    /**
     * The locality in which the contractor works.
     */
    public static final String CITY = "location";

    /**
     * Comma separated list of types of work the contractor can perform.
     */
    public static final String TYPES_OF_WORK_PERFORMED = "specialties";

    /**
     * The number of workers available when the record is booked.
     */
    public static final String NUMBER_OF_STAFF_IN_ORGANIZATION = "size";

    /**
     * Charge per hour for the subcontractor. This field includes the currency
     * symbol.
     */
    public static final String HOURLY_CHARGE = "rate";

    /**
     * The customer id (an 8 digit number).
     */
    public static final String CUSTOMER_HOLDING_THIS_RECORD = "owner";

    /**
     * The subcontractor's name length in bytes.
     */
    public static final int SUBCONTRACTOR_NAME_LENGTH = 32;

    /**
     * The city's length in bytes.
     */
    public static final int CITY_LENGTH = 64;

    /**
     * The types of work performed length, in bytes.
     */
    public static final int TYPES_OF_WORK_PERFORMED_LENGTH = 64;

    /**
     * The number of staff in organization length, in bytes.
     */
    public static final int NUMBER_OF_STAFF_IN_ORGANIZATION_LENGTH = 6;

    /**
     * The hourly charge length, in bytes.
     */
    public static final int HOURLY_CHARGE_LENGTH = 8;

    /**
     * The customer holding this record length, in bytes.
     */
    public static final int CUSTOMER_HOLDING_THIS_RECORD_LENGTH = 8;

    /**
     * Decimal value of the magic cookie, that identifies a file as a database
     * file.
     */
    public static final int MAGIC_COOKIE = 513;

    /**
     * Total overall length of each database record, minus the deleted state
     * byte flag, in bytes.
     */
    public static final int TOTAL_OVERALL_LENGTH_OF_EACH_RECORD
            = SUBCONTRACTOR_NAME_LENGTH
            + CITY_LENGTH
            + TYPES_OF_WORK_PERFORMED_LENGTH
            + NUMBER_OF_STAFF_IN_ORGANIZATION_LENGTH
            + HOURLY_CHARGE_LENGTH
            + CUSTOMER_HOLDING_THIS_RECORD_LENGTH;

    /**
     * Number of fields in each record.
     */
    public static final int NUMBER_OF_FIELDS_IN_EACH_RECORD = 6;

    /**
     * Value of the deleted state byte flag, indicating a valid record.
     */
    public static final String VALID_RECORD = "0";

    /**
     * Value of the deleted state byte flag, indicating a non-valid, deleted 
     * record.
     */
    public static final String DELETED_RECORD = "1";
}
