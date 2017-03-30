/* 
 * ServerConfigFrame
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import suncertify.integration.DataRemote;
import suncertify.persistence.DBDML;
import suncertify.persistence.DBFile;
import suncertify.db.Data;

/**
 * Graphical user interface window that allows a user to run a setup
 * configuration for a remote distributed database server.
 *
 * @author Filipe João Mendes Rosa
 */
public class ServerConfigFrame extends JFrame {

    /**
     * Maximum computer port range. Computer ports range between 0 and 65536.
     */
    private final static int MAX_PORT_RANGE = 65536;

    /**
     * Widget tag specifying that this area of the screen relates to the port of
     * the server where the remote, distributed database program will be
     * running.
     */
    private final JLabel port;

    /**
     * Widget tag specifying that this area of the screen relates to the
     * location of the database file, where the remote distributed program, will
     * collect and persist its data.
     */
    private final JLabel file;

    /**
     * Graphical widget that allows the user to input the port of the server
     * where the remote, distributed database program will be running.
     */
    private final JTextField portText;

    /**
     * Graphical widget that allows the user to input the location of the
     * database file, where the remote distributed program, will collect and
     * persist its data.
     */
    private final JTextField fileText;

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
     * Allows the user to manage, store, and retrieve key-value pairs. Useful to
     * autocomplete the input graphical widgets, when the configuration stage
     * doesn't frequently change.
     *
     * @see #portText
     *
     * @see #fileText
     */
    private PropertyManager propertyManager;

    /**
     * Creates and displays the remote server configuration window.
     * <p>
     * If there's a properties file in the server's file system current
     * directory called "suncertify.properties", the graphical input widgets are
     * prepopulated with the port and database filename previously configured.
     *
     * @param title the title of the remote server configuration window
     */
    public ServerConfigFrame(String title) {
        super(title);
        try {
            propertyManager = new PropertyManager(new File(
                    "suncertify.properties"));
        } catch (IOException ioexception) {
            JOptionPane.showMessageDialog(null, "Error while accessing the "
                    + "filesystem.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 0, 10, 0);
        constraints.anchor = GridBagConstraints.LINE_END;
        port = new JLabel("Port", JLabel.TRAILING);
        port.setToolTipText("Port number.");
        getContentPane().add(port, constraints);
        portText = new JTextField(50);
        if (propertyManager != null) {
            portText.setText(propertyManager.getProperty("PORT"));
        }
        portText.setHorizontalAlignment(JTextField.LEADING);
        port.setLabelFor(portText);
        portText.setToolTipText(
                "Insert a number from 0 to 65536. Only use ports"
                + " between 0 to 1023 if you have Administrator privileges.");
        portText.addActionListener(new OkActionListener());
        constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.insets = new Insets(10, 0, 10, 0);
        getContentPane().add(portText, constraints);
        file = new JLabel("File", JLabel.TRAILING);
        file.setToolTipText("File name.");
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.insets = new Insets(10, 0, 10, 0);
        getContentPane().add(file, constraints);
        fileText = new JTextField(50);
        fileText.addActionListener(new OkActionListener());
        if (propertyManager != null) {
            fileText.setText(propertyManager.getProperty("FILE"));
        }
        fileText.setHorizontalAlignment(JTextField.LEADING);
        fileText.setToolTipText("Name your file here or press the button"
                + " on the right.");
        file.setLabelFor(fileText);
        portText.addActionListener(new OkActionListener());
        constraints = new GridBagConstraints();
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.insets = new Insets(10, 0, 10, 10);
        getContentPane().add(fileText, constraints);
        browse = new JButton("Browse");
        browse.setMnemonic(KeyEvent.VK_B);
        browse.setToolTipText("Press Browse to choose a file.");
        browse.addActionListener(new BrowseActionListener());
        constraints = new GridBagConstraints();
        constraints.gridy = 1;
        constraints.gridwidth = constraints.gridwidth
                = GridBagConstraints.REMAINDER;
        constraints.insets = new Insets(10, 0, 10, 0);
        getContentPane().add(browse, constraints);
        ok = new JButton("Ok");
        ok.setMnemonic(KeyEvent.VK_O);
        ok.setToolTipText("Press Ok to start the server.");
        ok.addActionListener(new OkActionListener());
        constraints = new GridBagConstraints();
        constraints.gridx = 3;
        constraints.gridy = 4;
        getContentPane().add(ok, constraints);
        exit = new JButton("Exit");
        exit.setMnemonic(KeyEvent.VK_E);
        exit.setToolTipText("Press Exit to quit the program.");
        exit.addActionListener(new ExitActionListener());
        constraints = new GridBagConstraints();
        constraints.gridy = 4;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        getContentPane().add(exit, constraints);
        JMenuBar menubar = new JMenuBar();
        JMenu filemenu = new JMenu("File");
        filemenu.setMnemonic(KeyEvent.VK_F);
        filemenu.setToolTipText("Press File to see the options.");
        JMenuItem okitem = new JMenuItem("Ok", KeyEvent.VK_O);
        okitem.addActionListener(new OkActionListener());
        okitem.setToolTipText("Press Ok to start the server.");
        JMenuItem browseitem = new JMenuItem("Browse", KeyEvent.VK_B);
        browseitem.addActionListener(new BrowseActionListener());
        browseitem.setToolTipText("Press Browse to choose a file.");
        JMenuItem exititem = new JMenuItem("Exit", KeyEvent.VK_E);
        exititem.addActionListener(new ExitActionListener());
        exititem.setToolTipText("Press exit to quit the program.");
        filemenu.add(okitem);
        filemenu.add(browseitem);
        filemenu.add(exititem);
        menubar.add(filemenu);
        setJMenuBar(menubar);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
    }

    /**
     * Implementation of a graphical user interface triggering action, that
     * starts the remote server.
     */
    private class OkActionListener implements ActionListener {

        /**
         * Checks whether the input graphical widgets are populated, checks to
         * see if the filename is a valid candidate database file (no analysis
         * of the file's data is performed here), checks that the port number is
         * between the permitted range (0-65536), saves the user input setup
         * configuration to a properties file, initializes the singleton cache
         * with the database file data, starts the remote server and a remote
         * discovery system, where the remote client can find and access the
         * remote database service.
         * <p>
         * An all encompassing test and warning message, was later added to
         * screen for corner cases where the user uses types a port number not
         * in the range -2147483648 to 2147483647, or when the user types a
         * non-numeric character in the port graphical input widget.
         * <p>
         * Closes the setup window, and opens a new one indicating the status of
         * the running database server.
         *
         * @param e the event that triggered the action
         *
         * @see #portText
         *
         * @see #fileText
         *
         * @see suncertify.db.Data
         *
         * @see suncertify.persistence.DBDML
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            String port;
            int portnumeric;
            String filename;
            File file;

            try {
                if ((portText.getText() == null) || (port = portText.getText().
                        trim()).equals("")
                        || fileText.getText() == null || (filename = fileText.
                        getText().trim()).equals("")) {
                    JOptionPane.showMessageDialog(ServerConfigFrame.this,
                            "You must insert a "
                            + "port number and a name file.", "Fields Missing",
                            JOptionPane.WARNING_MESSAGE);
                } else if (!(file = new File(filename)).exists() || !file.
                        canRead()
                        || !file.canWrite()
                        || !file.isFile() || !(file.length() != 0)) {
                    JOptionPane.showMessageDialog(ServerConfigFrame.this,
                            "The filename"
                            + " you entered is invalid!", "Invalid File Name",
                            JOptionPane.WARNING_MESSAGE);
                } else if ((portnumeric = new Integer(port).intValue()) < 0
                        || portnumeric > MAX_PORT_RANGE) {
                    JOptionPane.showMessageDialog(ServerConfigFrame.this,
                            "The port"
                            + " you entered is invalid. Must be betwenn 0 and "
                            + "65536.", "Invalid Port Number",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        propertyManager.addProperty("PORT", portText.getText().
                                trim());
                        propertyManager.addProperty("FILE", fileText.getText().
                                trim());
                        try {
                            propertyManager.saveProperties();
                        } catch (IOException iOException) {
                            JOptionPane.showMessageDialog(null,
                                    "Error while accessing the filesystem.",
                                    "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                        DBDML dbdml = new DBDML(new DBFile(filename));
                        Data.setList(dbdml);
                        DataRemote dataremote = null;
                        Registry registry = null;
                        try {
                            dataremote = new DataRemote(Data.getInstance());
                            registry = LocateRegistry.
                                    createRegistry(portnumeric);
                            registry.rebind("SERVER", dataremote);
                        } catch (RemoteException remote) {
                            JOptionPane.
                                    showMessageDialog(ServerConfigFrame.this,
                                            "There was a problem while "
                                            + "initializing the server. Try a "
                                            + "port number above 1023.",
                                            "Server"
                                            + " Error",
                                            JOptionPane.ERROR_MESSAGE);
                        }
                        try {
                            if (dataremote == null || registry == null
                                    || registry.
                                    lookup("SERVER") == null) {
                                //do nothing    
                            } else {
                                ServerConfigFrame.this.dispose();
                                new ServerFrame("Server");
                            }
                        } catch (Exception exception) {
                            JOptionPane.
                                    showMessageDialog(ServerConfigFrame.this,
                                            "There was an error while accessing"
                                            + " the server. Try later, please!",
                                            "Server Error",
                                            JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (FileNotFoundException fileexception) {
                        JOptionPane.showMessageDialog(ServerConfigFrame.this,
                                "The filename"
                                + " you entered is invalid.",
                                "Invalid File Name",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ioexception) {
                        JOptionPane.showMessageDialog(ServerConfigFrame.this,
                                "Error while accessing the database. You must"
                                + " restart the program. Check you have the"
                                + " correct database file.",
                                "Database Error", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    } catch (Throwable throwable) {
                        JOptionPane.showMessageDialog(ServerConfigFrame.this,
                                "There was an unknown error. You must restart"
                                + " the program. Check you have the correct"
                                + " database file.",
                                "Severe Error", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    }

                }
            } catch (NumberFormatException formatException) {
                JOptionPane.showMessageDialog(ServerConfigFrame.this,
                        "You must insert numeric digits in the range 0-65536.",
                        "Invalid Port", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Implementation of a graphical user interface triggering action, that
     * allows the user to traverse and choose a file from the file system.
     *
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
            int option = filechooser.showOpenDialog(ServerConfigFrame.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                String file = filechooser.getSelectedFile().
                        getAbsolutePath();
                fileText.setText(file);
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
         * taken back to the remote server setup window.
         *
         * @param e the event that triggered the action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            int option = JOptionPane.showConfirmDialog(
                    ServerConfigFrame.this,
                    "Are you sure you want to quit?", "Exit",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                ServerConfigFrame.this.dispose();
            }

        }

    }

}
