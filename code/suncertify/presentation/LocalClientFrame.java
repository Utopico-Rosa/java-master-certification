/* 
 * LocalClientFrame
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
package suncertify.presentation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import suncertify.db.CustomerRecord;
import suncertify.integration.DataLocal;
import suncertify.integration.DataMarker;
import suncertify.db.BookingException;
import suncertify.db.Data;
import suncertify.db.RecordNotFoundException;

/**
 * Implements the local client fine-grained functionality, called by the parent
 * <code>class</code> "template methods".
 * 
 * @author Filipe João Mendes Rosa
 */
public class LocalClientFrame extends ClientFrame {

    /**
     * Initializes this instance, passing to the parent <code>class</code>, the
     * title of the graphical user interface window, and a marker interface,
     * whose concrete type implements local, non-distributed functionality.
     * 
     * @param title the title of the graphical user interface window
     * 
     * @param data marker interface, whose concrete type implements local, non-
     * distributed functionality
     */
    public LocalClientFrame(String title, DataMarker data) {
        super(title, data);
    }

    /**
     * Retrieves all object-oriented customer record from the singleton cache,
     * that are not deemed as deleted, and returns them in a multidimensional 
     * data structure.
     * <p>
     * This is a fine-grained operation that is called by template methods, and
     * implemented by subclasses.
     * 
     * @return a multidimensional data structure containing object-oriented
     * customer records
     * 
     * @see suncertify.integration.DataLocal#getList() 
     * 
     * @see suncertify.db.CustomerRecord
     * 
     * @see suncertify.db.Data
     * 
     * @see suncertify.db.DBSchema#DELETED_RECORD
     */ 
    @Override
    protected String[][] getValues() {
        DataLocal datalocal = (DataLocal) data;
        List<String[]> statelist = new ArrayList<>();
        List<CustomerRecord> list = datalocal.getList(); 
        for (CustomerRecord record : list) {
            statelist.add(record.serializeAllState());
        }
        return statelist.toArray(new String[0][]);
    }

    /**
     * Conducts a query, whose string of characters typed by the user on the
     * database field "name" graphical user input, and/or the database field 
     * "location" graphical user input is compared to the "name" and "location"
     * fields of all the object-oriented customer records in the singleton
     * cache, that are not deemed as deleted.
     * <p>
     * This is a fine-grained operation that is called by template methods, and
     * implemented by subclasses.
     * 
     * @param query data structure containing "name" and/or "location" search
     * criteria
     * 
     * @return a data structure containing record numbers with matching results
     * 
     * @see suncertify.integration.DataLocal#findByNameAndLocation(String[]) 
     * 
     * @see #name
     * 
     * @see #location
     * 
     * @see suncertify.db.DBSchema#SUBCONTRACTOR_NAME
     * 
     * @see suncertify.db.DBSchema#CITY
     * 
     * @see suncertify.db.CustomerRecord
     * 
     * @see suncertify.db.Data
     * 
     * @see suncertify.db.DBSchema#DELETED_RECORD
     */
    @Override
    protected int[] conductQuery(String[] query) {
        return ((DataLocal) data).findByNameAndLocation(query);
    }

    /**
     * Books a customer for home improvement craftsmanship, on a client company.
     * Updates the data shown on the graphical user interface window's table,
     * with the most recent data on the server's singleton cache.
     * <p>
     * This is a fine-grained operation that is called by template methods, and
     * implemented by subclasses.
     * 
     * @param recno the record number of the client company
     * 
     * @param data data structure containing the customer identification
     * 
     * @see suncertify.integration.DataLocal#update(int, java.lang.String[],
     * long) 
     * 
     * @see suncertify.db.Data
     */
    @Override
    protected void bookCustomer(int recno, String[] data) {
        long lockcookie = 0;       
        try {
            lockcookie = ((DataLocal) this.data).lock(recno);
            ((DataLocal) this.data).update(recno, data, lockcookie);
        } catch (RecordNotFoundException recordexception) {
            JOptionPane.showMessageDialog(LocalClientFrame.this,
                    "Record not found!"
                    + " Please consult the updated table!", "Record Not Found!",
                    JOptionPane.WARNING_MESSAGE);
        } catch (BookingException bookingexception) {
            JOptionPane.showMessageDialog(LocalClientFrame.this,
                    "Client already booked!"
                    + " Please consult the updated table!",
                    "Client Already Booked!", JOptionPane.WARNING_MESSAGE);
        } catch (SecurityException securityexception) {
            JOptionPane.showMessageDialog(LocalClientFrame.this,
                    "There was a security problem"
                    + " with your request. Try again later!", 
                    "Security Problem", JOptionPane.WARNING_MESSAGE);
        } catch (Throwable throwable) {
            JOptionPane.showMessageDialog(LocalClientFrame.this,
                    "There was an unknown error."
                    + " We´re working on it. Try again later, please!",
                    "Severe Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                ((DataLocal) this.data).unlock(recno, lockcookie);
            } catch (RecordNotFoundException recordexception) {
                JOptionPane.showMessageDialog(LocalClientFrame.this,
                        "Record not found!"
                        + " Please consult the updated table!",
                        "Record Not Found!", JOptionPane.WARNING_MESSAGE);
            } catch (SecurityException securityexception) {
                JOptionPane.showMessageDialog(LocalClientFrame.this,
                        "There was a security problem"
                        + " with your request. Try again later!",
                        "Security Problem", JOptionPane.WARNING_MESSAGE);
            } catch (Throwable throwable) {
                JOptionPane.showMessageDialog(LocalClientFrame.this,
                        "There was an unknown error."
                        + " We´re working on it. Try again later, please!",
                        "Severe Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        name.setText(null);
        location.setText(null);
        search.doClick();
    }

    /**
     * Orders the program to persist the data on the singleton cache, back to 
     * the database file, when and before the server is about to exit.
     * <p>
     * This is a fine-grained operation that is called by template methods, and
     * implemented by subclasses.
     * 
     * @see suncertify.db.Data
     * 
     * @see suncertify.persistence.DBDML
     * 
     * @see Runtime#addShutdownHook(java.lang.Thread) 
     */
    @Override
    protected void saveDataToFile() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    Data.getDbDML().persist(Data.getList());
                } catch (IOException ioexception) {
                    JOptionPane.showMessageDialog(null, "There "
                            + "was an error while writing the data from the"
                            + " cache to the database file.", "Error",
                            +JOptionPane.ERROR_MESSAGE);
                } catch (Throwable throwable) {
                    JOptionPane.showMessageDialog(LocalClientFrame.this,
                            "There was an unknown error."
                            + " We´re working on it. Try again later, please!",
                            "Severe Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

}
