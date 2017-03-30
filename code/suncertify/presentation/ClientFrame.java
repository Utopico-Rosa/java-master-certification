/* 
 * ClientFrame
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

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import suncertify.integration.DataMarker;

/**
 * Shows the database data in a graphical user interface, and has the
 * functionality required by the requested use cases (search and book a client).
 * <p>
 * This <code>class</code> is abstract and has "template methods", whose fine-
 * grained methods are implemented by subclasses, whether the requested
 * operation is local and non-distributed, or networked remote and distributed.
 * 
 * @author Filipe João Mendes Rosa
 */
public abstract class ClientFrame extends JFrame {

    /**
     * Marker interface. It allows to pass the same type to this <code>class's
     * </code> constructor, so that a downcast can be made on this <code>class's
     * </code> child classes, whether the operation is local or remote.
     */
    protected final DataMarker data;

    /**
     * Database field "name" graphical user input. Allows queries to be sent
     * to the singleton cache.
     * 
     * @see suncertify.db.Data
     */
    protected final JTextField name;

    /**
     * Database field "location" graphical user input. Allows queries to be sent
     * to the singleton cache.
     * 
     * @see suncertify.db.Data
     */
    protected final JTextField location;

    /**
     * Trigger graphical user interface widget that aids in triggering a "search
     * client" use-case.
     */
    protected final JButton search;
    
    /**
     * Trigger graphical user interface widget that aids in triggering a "book 
     * customer" use-case.
     */
    private final JButton book;
    
    /**
     * Data tabular graphical user interface widget that lays the database data
     * in rows and columns.
     */
    private final JTable table;

    /**
     * Creates the graphical user interface window that let users see the
     * database data in a table, and has the widgets that allow the search and
     * booking of clients.
     * 
     * @param title the title of the window
     * 
     * @param data marker interface, whose concrete type will have local or
     * remote functionality
     * 
     * @see suncertify.integration.DBAllQueriesRemote
     * 
     * @see suncertify.integration.DataLocal
     */
    public ClientFrame(String title, DataMarker data) {
        super(title);
        this.data = data;
        String[][] values = getValues();
        table = new JTable(new DBTableModel(values));
        table.getSelectionModel().setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollpane = new JScrollPane(table);
        getContentPane().add(scrollpane, BorderLayout.CENTER);
        JPanel panel = new JPanel(new GridBagLayout(), true);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        JLabel label = new JLabel("Name");
        label.setToolTipText("Search by name.");
        panel.add(label, constraints);
        name = new JTextField(15);
        name.addActionListener(new SearchActionListener());
        label.setLabelFor(name);
        name.setToolTipText("Insert a name to look for.");
        constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 0, 5, 5);
        panel.add(name, constraints);
        JLabel label1 = new JLabel("Location");
        label1.setToolTipText("Search by location.");
        constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 0, 5, 5);
        panel.add(label1, constraints);
        location = new JTextField(15);
        location.addActionListener(new SearchActionListener());
        label1.setLabelFor(location);
        location.setToolTipText("Insert a location to look for.");
        constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 0, 5, 5);
        panel.add(location, constraints);
        search = new JButton("Search");
        search.addActionListener(new SearchActionListener());
        search.setToolTipText("Press Search to see the results.");
        search.setMnemonic(KeyEvent.VK_S);
        constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 0, 0, 0);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(search, constraints);
        book = new JButton("Book");
        book.addActionListener(new BookActionListener());
        book.setToolTipText(
                "Select a row and press Book to reserve a client.");
        book.setMnemonic(KeyEvent.VK_B);
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.insets = new Insets(0, 5, 0, 0);
        panel.add(book, constraints);
        JButton button2 = new JButton("Exit");
        button2.addActionListener(new ExitListener());
        button2.setToolTipText("Press Exit to quit.");
        button2.setMnemonic(KeyEvent.VK_E);
        constraints = new GridBagConstraints();
        constraints.gridx = 6;
        constraints.gridy = 6;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(button2, constraints);
        getContentPane().add(panel, BorderLayout.PAGE_END);
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menu.setToolTipText("Press File to see the options.");
        JMenuItem item = new JMenuItem("Search", KeyEvent.VK_S);
        item.addActionListener(new SearchActionListener());
        item.setToolTipText("Press Search to see the results.");
        JMenuItem item1 = new JMenuItem("Book", KeyEvent.VK_B);
        item1.addActionListener(new BookActionListener());
        item1.setToolTipText("Select a row and press Book to reserve a "
                + "client.");
        JMenuItem item2 = new JMenuItem("Exit", KeyEvent.VK_E);
        item2.addActionListener(new ExitListener());
        item2.setToolTipText("Press Exit to quit.");
        menu.add(item);
        menu.add(item1);
        menu.add(item2);
        menubar.add(menu);
        setJMenuBar(menubar);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
    }

    /**
     * Retrieves all object-oriented customer records from the singleton cache,
     * that are not deemed as deleted, and returns them in a multidimensional 
     * data structure.
     * <p>
     * This is a fine-grained operation that is called by template methods, and
     * implemented by subclasses.
     * 
     * @return a multidimensional data structure containing object-oriented
     * customer records
     * 
     * @see suncertify.db.CustomerRecord
     * 
     * @see suncertify.db.Data
     * 
     * @see suncertify.db.DBSchema#DELETED_RECORD
     */
    protected abstract String[][] getValues();

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
    protected abstract int[] conductQuery(String[] query);

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
     * @see suncertify.db.Data
     */
    protected abstract void bookCustomer(int recno, String[] data);

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
    protected abstract void saveDataToFile();

    
    /**
     * Checks if all characters are numeric. 
     *
     * @param id string of characters
     * 
     * @return true if all numeric, false otherwise
     */
    private boolean checkDigits(String id) {
        char[] stringchar = id.toCharArray();
        for (char letter : stringchar) {
            if (Character.isDigit(letter)) {
            } else {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Implementation of a graphical user interface triggering action, that
     * allows the user to conduct a search query.
     */
    private class SearchActionListener implements ActionListener {
        
        
        /**
         * Gathers user input from the database field "name" graphical user 
         * input and/or the database field "location" graphical user input,
         * conducts a search query on the singleton cache, retrieves
         * simultaneously the matching results and all the updated data,
         * compares the matching results with the updated data, and refreshes 
         * the data on the window.
         * 
         * @param e the event that triggered the action
         * 
         * @see #name
         * 
         * @see #location
         * 
         * @see #conductQuery(java.lang.String[]) 
         * 
         * @see DBTableModel
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = ClientFrame.this.name.getText().trim();
            String location = ClientFrame.this.location.getText().trim();
            String[] query = new String[6];
            for (int i = 0; i < query.length; ++i) {
                if (i == 0 && name.length() != 0) {
                    query[i] = name;
                } else if (i == 1 && location.length() != 0) {
                    query[i] = location;
                } else {
                    continue;
                }
            }
            int[] recordsfound = conductQuery(query);
            for (int i = 0; i < recordsfound.length; ++i) {
                recordsfound[i]--;
            }
            String[][] arrayvalues = getValues();
            DBTableModel tablemodel = (DBTableModel) table.getModel();
            Object[][] newdata = new Object[recordsfound.length][];
            for (int i = 0; i < recordsfound.length; i++) {
                newdata[i] = arrayvalues[recordsfound[i]];
            }
            tablemodel.setData(newdata);
            tablemodel.fireTableDataChanged();

        }
    }
    
    /**
     * Implementation of a graphical user interface triggering action, that
     * allows the user to book a customer with a client company.
     */
    private class BookActionListener implements ActionListener {
         
        /**
         * Checks that the user selected a row on the database data table, opens
         * a graphical user interface window where the user must type an eight-
         * digit number, and orders a booking with that customer eight-digit 
         * number, on the selected client company row.
         * 
         * @param e the event that triggered the action
         * 
         * @see javax.swing.JOptionPane#showInputDialog(java.awt.Component, 
         * java.lang.Object, java.lang.String, int)  
         *  
         * @see #bookCustomer(int, java.lang.String[]) 
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (table.getSelectedRowCount() == 0) {
                JOptionPane.showMessageDialog(ClientFrame.this,
                        "You must select a row before you book a client.", 
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String id = JOptionPane.showInputDialog(ClientFrame.this,
                    "Type an eight digit number.", "ID",
                    JOptionPane.PLAIN_MESSAGE);
            if ((id = id.trim()).length() != 8 || !checkDigits(id)) {
                JOptionPane.showMessageDialog(ClientFrame.this, "Your id didn´t"
                        + " have eight digits. Book the client again, please!",
                        "ID Invalid", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                int rowselected = table.getSelectedRow();
                rowselected++;
                String[] outputdata = new String[6];
                outputdata[5] = id;
                bookCustomer(rowselected, outputdata);
            }
        }

    }

    /**
     * Implementation of a graphical user interface triggering action, that
     * allow the user to exit the program.
     */
    private class ExitListener implements ActionListener {
        
        /**
         * Opens a graphical user interface window to give the user a last
         * chance to think he really wants to exit the program. If the user
         * presses the "yes" button, the program is instructed to save the data
         * on the singleton cache back to the database data file before it
         * leaves, otherwise the user is taken back to the main window.
         * 
         * @param e the event that triggered the action
         * 
         * @see javax.swing.JOptionPane#showConfirmDialog(java.awt.Component, 
         * java.lang.Object, java.lang.String, int, int)
         * 
         * @see #saveDataToFile() 
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            int option = JOptionPane.showConfirmDialog(ClientFrame.this,
                    "Are you sure you want to quit?",
                    "Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                saveDataToFile();
                System.exit(0);
            }

        }

    }

}
