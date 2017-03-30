/* 
 * StandaloneConfigFrame
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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import suncertify.integration.DataLocal;
import suncertify.persistence.DBDML;
import suncertify.persistence.DBFile;
import suncertify.db.Data;

/**
 * Graphical user interface window that allows local non-distributed users, to
 * configure the location in the file system where the database file resides.
 *
 * @author Filipe João Mendes Rosa
 */
public class StandaloneConfigFrame extends JFrame {

    /**
     * Graphical widget that allows the user to browse and select a file in the
     * file system.
     */
    private final JButton browse;

    /**
     * Graphical widget that allows the user to confirm the setup configuration
     * is done.
     */
    private final JButton ok;

    /**
     * Graphical widget that allows the user to exit the setup stage, and hence
     * halt the execution of the program.
     */
    private final JButton exit;

    /**
     * Graphical widget that allows the user to input the location of the
     * database file, where the program will collect and persist its data.
     */
    private final JTextField fileName;

    /**
     * Allows the user to manage, store, and retrieve key-value pairs. Useful to
     * autocomplete the input graphical widget, when the configuration stage
     * doesn't frequently change.
     *
     * @see #fileName
     */
    private PropertyManager propertyManager;

    /**
     * Creates and displays the local client configuration window.
     * <p>
     * If there's a properties file in the file system current directory called
     * "suncertify.properties", the graphical input widget is prepopulated with
     * the database filename previously configured.
     *
     * @param title the title of the local client configuration window
     */
    public StandaloneConfigFrame(String title) {
        super(title);
        try {
            propertyManager = new PropertyManager(new File(
                    "suncertify.properties"));
        } catch (IOException ioexception) {
            JOptionPane.showMessageDialog(null, "Error while accessing"
                    + " the filesystem.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
        JLabel label = new JLabel("File", JLabel.TRAILING);
        label.setToolTipText("File name.");
        fileName = new JTextField(50);
        fileName.addActionListener(new OkActionListener());
        if (propertyManager != null) {
            fileName.setText(propertyManager.getProperty("FILE"));
        }
        fileName.setHorizontalAlignment(JTextField.LEADING);
        fileName.setToolTipText("Name your file here or press the button"
                + " on the right.");
        browse = new JButton("Browse");
        browse.addActionListener(new BrowseActionListener());
        browse.setMnemonic(KeyEvent.VK_B);
        browse.setToolTipText("Press Browse to search for a file.");
        browse.setBorder(new CompoundBorder(new EmptyBorder(0, 10, 0, 0),
                browse.getBorder()));
        FlowLayout flowlayout = new FlowLayout(FlowLayout.CENTER);
        JPanel panel = new JPanel(flowlayout);
        panel.add(label);
        panel.add(fileName);
        panel.add(browse);
        getContentPane().add(panel, BorderLayout.CENTER);
        ok = new JButton("Ok");
        ok.addActionListener(new OkActionListener());
        ok.setMnemonic(KeyEvent.VK_O);
        ok.setToolTipText("Press Ok to access the main window.");
        exit = new JButton("Exit");
        exit.addActionListener(new ExitActionListener());
        exit.setMnemonic(KeyEvent.VK_E);
        exit.setToolTipText("Press Exit to quit.");
        FlowLayout layout1 = new FlowLayout(FlowLayout.TRAILING);
        JPanel panel1 = new JPanel(layout1);
        panel1.add(ok);
        panel1.add(exit);
        getContentPane().add(panel1, BorderLayout.PAGE_END);
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.setToolTipText("Press File to see the options.");
        menu.setMnemonic(KeyEvent.VK_F);
        JMenuItem item = new JMenuItem("Ok", KeyEvent.VK_O);
        item.addActionListener(new OkActionListener());
        item.setToolTipText("Press Ok to access the main window.");
        JMenuItem item1 = new JMenuItem("Browse", KeyEvent.VK_B);
        item1.addActionListener(new BrowseActionListener());
        item1.setToolTipText("Press Browse to search for a file.");
        JMenuItem item2 = new JMenuItem("Exit", KeyEvent.VK_E);
        item2.addActionListener(new ExitActionListener());
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
     * Implementation of a graphical user interface triggering action, that
     * starts the local database server and client.
     */
    private class OkActionListener implements ActionListener {

        /**
         * Checks whether the input graphical widget is populated, checks to see
         * if the filename is a valid candidate database file (no analysis of
         * the file's data is performed here), saves the user input setup
         * configuration to a properties file, and initializes the singleton
         * cache with the database file data.
         * <p>
         * Closes the setup window, and opens a new one with tabular data from
         * the local server, and functionality to allow the user to query and
         * manipulate the data.
         *
         * @param e the event that triggered the action
         *
         * @see #fileName
         *
         * @see suncertify.db.Data
         *
         * @see suncertify.persistence.DBDML
         *
         * @see LocalClientFrame
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            String filename;
            File file;

            if (fileName.getText() == null || (filename = fileName.getText().
                    trim()).equals("")) {
                JOptionPane.showMessageDialog(StandaloneConfigFrame.this,
                        "You must insert a "
                        + "name file.", "Field Missing",
                        JOptionPane.WARNING_MESSAGE);
            } else if (!(file = new File(filename)).exists() || !file.canRead()
                    || !file.canWrite()
                    || !file.isFile() || !(file.length() != 0)) {
                JOptionPane.showMessageDialog(StandaloneConfigFrame.this,
                        "The filename"
                        + " you entered is invalid!", "Invalid File Name",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    propertyManager.addProperty("FILE", fileName.getText().
                            trim());
                    try {
                        propertyManager.saveProperties();
                    } catch (IOException iOException) {
                        JOptionPane.showMessageDialog(null,
                                "Error while accessing"
                                + " the filesystem.", "Warning",
                                JOptionPane.WARNING_MESSAGE);
                    }
                    DBDML dbdml = new DBDML(new DBFile(filename));
                    Data.setList(dbdml);
                    DataLocal data = new DataLocal(Data.getInstance());
                    StandaloneConfigFrame.this.dispose();
                    new LocalClientFrame("Database", data);
                } catch (FileNotFoundException fileexception) {
                    JOptionPane.showMessageDialog(StandaloneConfigFrame.this,
                            "The filename"
                            + " you entered is invalid.", "Invalid File Name",
                            JOptionPane.ERROR_MESSAGE);
                } catch (IOException ioexception) {
                    JOptionPane.showMessageDialog(StandaloneConfigFrame.this,
                            "Error while accessing the database. You must"
                            + " restart the program. Check you have the"
                            + " correct database file.",
                            "Database Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                } catch (Throwable throwable) {
                    JOptionPane.showMessageDialog(StandaloneConfigFrame.this,
                            "There was an unknown error. You must restart"
                            + " the program. Check you have the correct"
                            + " database file.",
                            "Severe Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }

            }
        }
    }

    /**
     * Implementation of a graphical user interface triggering action, that
     * allows the user to traverse and choose a file from the file system.
     */
    private class BrowseActionListener implements ActionListener {

        /**
         * Opens a file system graphical user interface window, where the user
         * can traverse the file system and choose a file.
         *
         * @param e the event that triggered the action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser filechooser = new JFileChooser(new File("."));
            int option = filechooser.showOpenDialog(StandaloneConfigFrame.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                String file = filechooser.getSelectedFile().getAbsolutePath();
                fileName.setText(file);
            }
        }

    }

    /**
     * Implementation of a graphical user interface triggering action, that
     * allow the user to exit the program.
     */
    private class ExitActionListener implements ActionListener {

        /**
         * Opens a graphical user interface window to give the user a last
         * chance to think he really wants to exit the program. If the user
         * presses the "yes" button the program exits, otherwise the user is
         * taken back to the local setup window.
         *
         * @param e the event that triggered the action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            int option = JOptionPane.showConfirmDialog(
                    StandaloneConfigFrame.this, "Are you sure you"
                    + " want to quit?", "Exit", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                StandaloneConfigFrame.this.dispose();
            }

        }

    }

}
