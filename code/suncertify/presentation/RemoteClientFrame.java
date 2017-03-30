/* 
 * RemoteClientFrame
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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import suncertify.db.CustomerRecord;
import suncertify.integration.DBAllQueriesRemote;
import suncertify.integration.DataMarker;
import suncertify.db.BookingException;
import suncertify.db.RecordNotFoundException;

/**
 * Implements the remote distributed client fine-grained functionality, called 
 * by the parent <code>class</code> "template methods".
 * 
 * @author Filipe João Mendes Rosa
 */
public class RemoteClientFrame extends ClientFrame {

    /**
     * Initializes this instance, passing to the parent <code>class</code>, the
     * title of the graphical user interface window, and a marker interface,
     * whose concrete type implements remote, distributed functionality.
     * 
     * @param title the title of the graphical user interface window
     * 
     * @param data marker interface, whose concrete type implements remote,
     * distributed functionality
     */
    public RemoteClientFrame(String title, DataMarker data) {
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
     * @see suncertify.integration.DataRemote#getList() 
     * 
     * @see suncertify.db.CustomerRecord
     * 
     * @see suncertify.db.Data
     * 
     * @see suncertify.db.DBSchema#DELETED_RECORD
     */ 
    @Override
    protected String[][] getValues() {
        DBAllQueriesRemote dataremote = (DBAllQueriesRemote) data;
        List<String[]> statelist = new ArrayList<>();
        SwingWorker<List<CustomerRecord>, Void> worker
                = new SwingWorker<List<CustomerRecord>, Void>() {
            @Override
            public List<CustomerRecord> doInBackground() {
                List<CustomerRecord> list = new ArrayList<>();
                try {
                    list = dataremote.getList();
                } catch (RemoteException remote) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.
                                    showMessageDialog(RemoteClientFrame.this,
                                            "There was an uknown error: "
                                            + remote.getCause().getMessage()
                                            + " We´re working on it. Please"
                                            + " try again later.",
                                            "Unknown Error",
                                            JOptionPane.ERROR_MESSAGE);
                        }
                    });
                }
                return list;
            }
        };
        worker.execute();
        List<CustomerRecord> list = new ArrayList<>();
        try {
            list = worker.get();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(RemoteClientFrame.this,
                    "There was an uknown error: "
                    + e.getCause().getMessage() + " We´re working on it. Please"
                    + " try again later.", "Unknown Error",
                    JOptionPane.ERROR_MESSAGE);
        }
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
     * @return a data structure containing record numbers containing matching
     * results
     * 
     * @see suncertify.integration.DataRemote#findByNameAndLocation(String[]) 
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
        SwingWorker<int[], Void> worker = new SwingWorker<int[], Void>() {

            @Override
            public int[] doInBackground() {
                try {
                    return ((DBAllQueriesRemote) data).findByNameAndLocation(
                            query);
                } catch (RemoteException remote) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.
                                    showMessageDialog(RemoteClientFrame.this,
                                            "There was an uknown error: "
                                            + remote.getCause().getMessage()
                                            + " We´re working on it. Please"
                                            + " try again later.",
                                            "Unknown Error",
                                            JOptionPane.ERROR_MESSAGE);
                        }
                    });

                    return new int[0];
                }
            }
        };
        worker.execute();
        try {
            return worker.get();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(RemoteClientFrame.this,
                    "There was an error while communicating with the server. "
                            + "Try again later, please!", "Unkown Error",
                    JOptionPane.ERROR_MESSAGE);
            return new int[0];
        }
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
     * @see suncertify.integration.DataRemote#update(int, java.lang.String[], 
     * long)  
     * 
     * @see suncertify.db.Data
     */
    @Override
    protected void bookCustomer(int recno, String[] data) {

        new SwingWorker<Void, Void>() {
            long lockcookie;

            @Override
            public Void doInBackground() {
                try {
                    lockcookie
                            = ((DBAllQueriesRemote) 
                                    RemoteClientFrame.this.data).lock(recno);
                    ((DBAllQueriesRemote) RemoteClientFrame.this.data).update(
                            recno, data, lockcookie);
                    ((DBAllQueriesRemote) RemoteClientFrame.this.data).unlock(
                            recno, lockcookie);
                } catch (RecordNotFoundException recordexception) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.
                                    showMessageDialog(RemoteClientFrame.this,
                                            "Record not found!"
                                            + " Please consult the updated "
                                            + "table!", "Record Not Found!",
                                            JOptionPane.WARNING_MESSAGE);
                        }
                    });
                } catch (RemoteException remote) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.
                                    showMessageDialog(RemoteClientFrame.this,
                                            "There was an uknown error: "
                                            + remote.getCause().getMessage()
                                            + " We´re working on it. Please"
                                            + " try again later.",
                                            "Unknown Error",
                                            JOptionPane.ERROR_MESSAGE);
                        }
                    });
                } catch (SecurityException securityexception) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.
                                    showMessageDialog(RemoteClientFrame.this,
                                            "There was a security problem"
                                            + " with your request. Try again"
                                            + " later!", "Security Problem",
                                            JOptionPane.WARNING_MESSAGE);
                        }
                    });
                } catch (BookingException bookingexception) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.
                                    showMessageDialog(RemoteClientFrame.this,
                                            "Client already booked! Please "
                                            + "consult the updated table!", 
                                            "Client Already Booked!",
                                            JOptionPane.WARNING_MESSAGE);
                        }
                    });
                }
                return null;
            }

            @Override
            public void done() {
                name.setText(null);
                location.setText(null);
                search.doClick();
            }
        }.execute();

    }

    /**
     * Does nothing.
     */
    @Override
    protected void saveDataToFile() {
    }


}
