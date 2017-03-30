/* 
 * NetworkedConfigFrame
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
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import suncertify.integration.DBAllQueriesRemote;

/**
 * Graphical user interface window that allows remote, distributed users, to
 * configure the host and port of the target machine, where the remote server is
 * hosted.
 *
 * @author Filipe João Mendes Rosa
 */
public class NetworkedConfigFrame extends JFrame {

    /**
     * Timeout period to "ping" the remote machine for a reply.
     */
    private static final int PERIOD_LENGTH = 5000;

    /**
     * Maximum computer port range. Computer ports range between 0 and 65536.
     */
    private static final int MAX_PORT_RANGE = 65536;

    /**
     * Widget tag specifying that this area of the screen relates to the host of
     * the remote machine.
     */
    private final JLabel host;

    /**
     * Widget tag specifying that this area of the screen relates to the port of
     * the remote machine.
     */
    private final JLabel port;

    /**
     * Graphical widget that allows the user to input a remote DNS host or IP
     * number.
     */
    private final JTextField hostText;

    /**
     * Graphical widget that allows the user to input a remote computer port
     * number.
     */
    private final JTextField portText;

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
     * auto-complete the input graphical widgets, when the configuration stage
     * doesn't frequently change.
     *
     * @see #hostText
     *
     * @see #portText
     */
    private PropertyManager propertyManager;

    /**
     * Creates and displays the remote setup configuration window.
     * <p>
     * If there's a properties file in the user's file system current directory
     * called "suncertify.properties", the graphical input widgets are pre-
     * populated with the host and port previously configured.
     *
     * @param title the title of the remote setup configuration window
     */
    public NetworkedConfigFrame(String title) {
        super(title);
        try {
            propertyManager = new PropertyManager(new File(
                    "suncertify.properties"));
        } catch (IOException ioexception) {
            JOptionPane.showMessageDialog(null, "Error while accessing"
                    + " the filesystem.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 0, 10, 0);
        constraints.anchor = GridBagConstraints.LINE_END;
        host = new JLabel("Host", JLabel.TRAILING);
        host.setToolTipText("Host name or IP number.");
        getContentPane().add(host, constraints);
        hostText = new JTextField(50);
        if (propertyManager != null) {
            hostText.setText(propertyManager.getProperty("HOST"));
        }
        hostText.setHorizontalAlignment(JTextField.LEADING);
        host.setLabelFor(hostText);
        hostText.setToolTipText("Insert a host or IP number.");
        hostText.addActionListener(new OkActionListener());
        constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.insets = new Insets(10, 0, 10, 0);
        getContentPane().add(hostText, constraints);
        port = new JLabel("Port", JLabel.TRAILING);
        port.setToolTipText("Port number.");
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.insets = new Insets(10, 0, 10, 0);
        getContentPane().add(port, constraints);
        portText = new JTextField(50);
        portText.addActionListener(new OkActionListener());
        if (propertyManager != null) {
            portText.setText(propertyManager.getProperty("PORT"));
        }
        portText.setHorizontalAlignment(JTextField.LEADING);
        portText.setToolTipText("Insert a number from 0 to 65536.");
        port.setLabelFor(portText);
        constraints = new GridBagConstraints();
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.insets = new Insets(10, 0, 10, 10);
        getContentPane().add(portText, constraints);
        JButton layoutHelper = new JButton();
        layoutHelper.setVisible(false);
        constraints = new GridBagConstraints();
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.insets = new Insets(10, 0, 10, 0);
        getContentPane().add(layoutHelper, constraints);
        ok = new JButton("Ok");
        ok.setMnemonic(KeyEvent.VK_O);
        ok.setToolTipText("Press Ok to access the main window.");
        ok.addActionListener(new OkActionListener());
        constraints = new GridBagConstraints();
        constraints.gridx = 3;
        constraints.gridy = 4;
        getContentPane().add(ok, constraints);
        exit = new JButton("Exit");
        exit.setHorizontalAlignment(JButton.TRAILING);
        exit.setMnemonic(KeyEvent.VK_E);
        exit.setToolTipText("Press Exit to quit.");
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
        okitem.setToolTipText("Press Ok to access the main window.");
        JMenuItem exititem = new JMenuItem("Exit", KeyEvent.VK_E);
        exititem.addActionListener(new ExitActionListener());
        exititem.setToolTipText("Press Exit to quit.");
        filemenu.add(okitem);
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
     * connects this program to a remote distributed database program.
     */
    private class OkActionListener implements ActionListener {

        /**
         * Checks whether the input graphical widgets are populated, "pings" the
         * remote host to see if it replies, checks that the port number is
         * between the permitted range (0-65536), saves the user input setup
         * configuration to a properties file, and connects to the remote,
         * distributed database server indicated in the setup by the user.
         * <p>
         * Closes the setup window, and opens a new one with tabular data from
         * the remote server, and functionality to allow the user to query and
         * manipulate the data.
         *
         * @param e the event that triggered the action
         *
         * @see #hostText
         *
         * @see #portText
         *
         * @see RemoteClientFrame
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            String host;
            String port;
            int portnumeric;

            try {
                if (hostText.getText() == null || (host = hostText.getText().
                        trim()).equals("")
                        || portText.getText() == null || (port = portText.
                        getText().trim()).equals("")) {
                    JOptionPane.showMessageDialog(NetworkedConfigFrame.this,
                            "You must insert a "
                            + "host and a port.", "Fields Missing",
                            JOptionPane.WARNING_MESSAGE);
                } else if (!InetAddress.getByName(host).isReachable(
                        PERIOD_LENGTH)) {
                    JOptionPane.showMessageDialog(NetworkedConfigFrame.this,
                            "The server"
                            + " doesn´t appear to be up!", "Server Error",
                            JOptionPane.ERROR_MESSAGE);

                } else if ((portnumeric = new Integer(port).intValue()) < 0
                        || portnumeric > MAX_PORT_RANGE) {
                    JOptionPane.showMessageDialog(NetworkedConfigFrame.this,
                            "The port you entered is invalid. Must be between 0"
                            + " and 65536.", "Invalid Port Number",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        propertyManager.addProperty("HOST", hostText.getText().
                                trim());
                        propertyManager.addProperty("PORT",
                                portText.getText().trim());
                        try {
                            propertyManager.saveProperties();
                        } catch (IOException iOException) {
                            JOptionPane.showMessageDialog(null,
                                    "Error while accessing the filesystem.",
                                    "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                        Registry registry = LocateRegistry.getRegistry(host,
                                portnumeric);
                        DBAllQueriesRemote data = (DBAllQueriesRemote) registry.
                                lookup("SERVER");
                        NetworkedConfigFrame.this.dispose();
                        new RemoteClientFrame("Database", data);
                    } catch (NotBoundException boundexception) {
                        JOptionPane.showMessageDialog(NetworkedConfigFrame.this,
                                "The server isn´t properly configurated",
                                "Server Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            } catch (NumberFormatException formatException) {
                JOptionPane.showMessageDialog(NetworkedConfigFrame.this,
                        "You must insert numeric digits in the range 0-65536.",
                        "Invalid Port", JOptionPane.WARNING_MESSAGE);
            } catch (UnknownHostException hostexception) {
                JOptionPane.showMessageDialog(NetworkedConfigFrame.this,
                        "The host or IP you typed is invalid!", "Invalid Host",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IOException ioexception) {
                JOptionPane.showMessageDialog(NetworkedConfigFrame.this, "There"
                        + " were problems with the communication. Check your "
                        + "internet connection!", "Network Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Throwable throwable) {
                JOptionPane.showMessageDialog(NetworkedConfigFrame.this,
                        "There was an unknown error."
                        + " We´re working on it. Try again later, please!",
                        "Severe Error", JOptionPane.ERROR_MESSAGE);
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
         * taken back to the remote setup window.
         *
         * @param e the event that triggered the action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            int option = JOptionPane.
                    showConfirmDialog(NetworkedConfigFrame.this,
                            "Are you sure you want to quit?", "Exit",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                NetworkedConfigFrame.this.dispose();
            }

        }

    }
}
