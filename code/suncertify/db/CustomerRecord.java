/* 
 * CustomerRecord
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

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static suncertify.db.DBSchema.*;

/**
 * Represents the object-oriented mapping of a customer record.
 *
 * @author Filipe João Mendes Rosa
 */
public class CustomerRecord implements Serializable {

    /**
     * Used during deserialization to verify that the sender and receiver of a
     * serialized object have loaded classes for that object that are compatible
     * with respect to serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Logger for <code>suncertify.db.CustomerRecord</code>. This <code>Logger
     * </code> uses a <code>ConsoleHandler</code>,<code>Level.INFO</code>, and a
     * <code>SimpleFormatter</code>, in production.
     *
     * @see java.util.logging.Logger
     *
     * @see java.util.logging.ConsoleHandler
     *
     * @see java.util.logging.Level#INFO
     *
     * @see java.util.logging.SimpleFormatter
     */
    private static final Logger LOG = Logger.getLogger(CustomerRecord.class.
            getName());

    /**
     * Represents the deleted state. It has one <code>byte</code> in size.
     * <code>"0"</code> implies valid record, <code>"1"</code> implies deleted
     * record.
     *
     * @see suncertify.db.DBSchema#VALID_RECORD
     *
     * @see suncertify.db.DBSchema#DELETED_RECORD
     */
    private String flag;

    /**
     * Represents the name of the client company. It has 32 bytes in length.
     *
     * @see suncertify.db.DBSchema#SUBCONTRACTOR_NAME_LENGTH
     */
    private String name;

    /**
     * Represents the geographical location of the client company. It has 64
     * bytes in length.
     *
     * @see suncertify.db.DBSchema#CITY_LENGTH
     */
    private String location;

    /**
     * Represents the type of home improvement craftsmanship the client company
     * provides. It has 64 bytes in length.
     *
     * @see suncertify.db.DBSchema#TYPES_OF_WORK_PERFORMED_LENGTH
     */
    private String specialties;

    /**
     * Represents the company size in workforce. It has 6 bytes in length.
     *
     * @see suncertify.db.DBSchema#NUMBER_OF_STAFF_IN_ORGANIZATION_LENGTH
     */
    private String size;

    /**
     * Represents the hourly charge rate of the client company. It has 8 bytes
     * in length.
     *
     * @see suncertify.db.DBSchema#HOURLY_CHARGE_LENGTH
     */
    private String rate;

    /**
     * Represents the identification of a customer that booked a contract with a
     * client company. It has 8 bytes in length. It has only numeric digits.
     *
     * @see suncertify.db.DBSchema#CUSTOMER_HOLDING_THIS_RECORD_LENGTH
     */
    private String owner;

    /**
     * Initializes <code>CustomerRecord</code> state.
     *
     * @param list the data structure containing the state input.
     */
    public CustomerRecord(List<String> list) {
        setFlag(list.get(0));
        setName(list.get(1));
        setLocation(list.get(2));
        setSpecialties(list.get(3));
        setSize(list.get(4));
        setRate(list.get(5));
        setOwner(list.get(6));
    }

    /**
     * Accesses the deleted state.
     *
     * @return the deleted state
     */
    public String getFlag() {
        return flag;
    }

    /**
     * Changes the deleted state. The state is only changed if the new state is
     * not null, neither is the wrong size in bytes.
     *
     * @param flag the new deleted state
     */
    public final void setFlag(String flag) {
        if (flag == null) {
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest("Null value passed as an input to a field.");
            }
        } else if (flag.length() != 1) {
            if (LOG.isLoggable(Level.WARNING)) {
                LOG.warning("Attempt to change a field in memory without"
                        + " the correct length.");
            }
        } else {
            this.flag = flag;
        }

    }

    /**
     * Accesses the name of the client company.
     *
     * @return the name of the client company
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the name of the client company. The state is only changed if the
     * new state is not null, neither is the wrong size in bytes.
     *
     * @param name the new name of the client company
     */
    public final void setName(String name) {
        if (name == null) {
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest("Null value passed as an input to a field.");
            }
        } else if (name.length() != SUBCONTRACTOR_NAME_LENGTH) {
            if (LOG.isLoggable(Level.WARNING)) {
                LOG.warning("Attempt to change a field in memory without"
                        + " the correct length.");
            }
        } else {
            this.name = name;
        }

    }

    /**
     * Accesses the geographical location of the client company.
     *
     * @return the geographical location of the client company
     */
    public String getLocation() {
        return location;
    }

    /**
     * Changes the geographical location of the client company. The state is
     * only changed if the new state is not null, neither is the wrong size in
     * bytes.
     *
     * @param location the new geographical location of the client company
     */
    public final void setLocation(String location) {
        if (location == null) {
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest("Null value passed as an input to a field.");
            }
        } else if (location.length() != CITY_LENGTH) {
            if (LOG.isLoggable(Level.WARNING)) {
                LOG.warning("Attempt to change a field in memory without"
                        + " the correct length.");
            }
        } else {
            this.location = location;
        }

    }

    /**
     * Accesses the type of home improvement craftsmanship the client company
     * provides.
     *
     * @return the type of home improvement craftsmanship the company provides
     */
    public String getSpecialties() {
        return specialties;
    }

    /**
     * Changes the type of home improvement craftsmanship the client company
     * provides. The state is only changed if the new state is not null, neither
     * is the wrong size in bytes.
     *
     * @param specialties the new type of home improvement craftsmanship
     */
    public final void setSpecialties(String specialties) {
        if (specialties == null) {
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest("Null value passed as an input to a field.");
            }
        } else if (specialties.length() != TYPES_OF_WORK_PERFORMED_LENGTH) {
            if (LOG.isLoggable(Level.WARNING)) {
                LOG.warning("Attempt to change a field in memory without"
                        + " the correct length.");
            }
        } else {
            this.specialties = specialties;
        }
    }

    /**
     * Accesses the company size in workforce.
     *
     * @return the company size in workforce
     */
    public String getSize() {
        return size;
    }

    /**
     * Changes the company size in workforce. The state is only changed if the
     * new state is not null, neither is the wrong size in bytes.
     *
     * @param size the new company size
     */
    public final void setSize(String size) {
        if (size == null) {
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest("Null value passed as an input to a field.");
            }
        } else if (size.length() != NUMBER_OF_STAFF_IN_ORGANIZATION_LENGTH) {
            if (LOG.isLoggable(Level.WARNING)) {
                LOG.warning("Attempt to change a field in memory without"
                        + " the correct length.");
            }
        } else {
            this.size = size;
        }
    }

    /**
     * Accesses the hourly charge rate of the client company.
     *
     * @return the hourly charge rate of the client company
     */
    public String getRate() {
        return rate;
    }

    /**
     * Changes the hourly charge rate of the client company. The state is only
     * changed if the new state is not null, neither is the wrong size in bytes.
     *
     * @param rate the new hourly charge rate of the client company
     */
    public final void setRate(String rate) {
        if (rate == null) {
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest("Null value passed as an input to a field.");
            }
        } else if (rate.length() != HOURLY_CHARGE_LENGTH) {
            if (LOG.isLoggable(Level.WARNING)) {
                LOG.warning("Attempt to change a field in memory without"
                        + " the correct length.");
            }
        } else {
            this.rate = rate;
        }
    }

    /**
     * Accesses the identification of the customer that booked a contract with
     * the client company. The result may be an empty eight <code>byte String
     * </code> ,which represents no booking.
     *
     * @return the identification of the customer
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Changes the identification of the customer that booked a contract with
     * the client company. The state is only changed if the new state is not
     * null, neither is the wrong size in bytes.
     *
     * @param owner the new identification of a customer
     */
    public final void setOwner(String owner) {
        if (owner == null) {
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest("Null value passed as an input to a field.");
            }
        } else if (owner.length() != CUSTOMER_HOLDING_THIS_RECORD_LENGTH) {
            if (LOG.isLoggable(Level.WARNING)) {
                LOG.warning("Attempt to change a field in memory without"
                        + " the correct length.");
            }
        } else {
            this.owner = owner;
        }
    }

    /**
     * Collects this instance state in a data structure, minus the deleted
     * state.
     *
     * @return the data structure with this instance state, minus the deleted
     * state
     */
    public String[] serializeAllState() {
        String[] state = new String[6];
        state[0] = getName();
        state[1] = getLocation();
        state[2] = getSpecialties();
        state[3] = getSize();
        state[4] = getRate();
        state[5] = getOwner();
        return state;
    }

    /**
     * Collects the name and geographical location of the client company in a
     * data structure.
     *
     * @return the data structure with the name and location of the client
     * company
     */
    public String[] serializeNameAndLocation() {
        String[] state = new String[6];
        state[0] = getName();
        state[1] = getLocation();
        state[2] = null;
        state[3] = null;
        state[4] = null;
        state[5] = null;
        return state;
    }

    /**
     * Changes all the instance state, minus the deleted record state. If the
     * data structure is &gt;6 in length an exception is thrown.
     *
     * @param update the data structure with the new state
     *
     * @throws IllegalArgumentException if the data structure &gt;6 in length
     */
    public void setState(String[] update) {
        for (int i = 0; i < update.length; ++i) {
            switch (i) {
                case 0:
                    setName(update[i]);
                    break;
                case 1:
                    setLocation(update[i]);
                    break;
                case 2:
                    setSpecialties(update[i]);
                    break;
                case 3:
                    setSize(update[i]);
                    break;
                case 4:
                    setRate(update[i]);
                    break;
                case 5:
                    setOwner(update[i]);
                    break;
                default:
                    throw new IllegalArgumentException("Wrong data"
                            + " configuration.");
            }
        }

    }

    /**
     * Returns the state of this instance as a <code>String</code>. It includes
     * the deleted state, the name of the client company, the geographical
     * location of the client company, the type of home improvement
     * craftsmanship the client company provides, the company size in workforce,
     * the hourly charge rate of the client company, and the identification of a
     * customer that booked a contract with the client company.
     *
     * @return the state of this instance as a <code>String</code>
     */
    @Override
    public String toString() {
        return flag + " " + name + " " + location + " " + specialties + " "
                + size + " " + rate + " " + owner
                + System.getProperty("line.separator");
    }
}
