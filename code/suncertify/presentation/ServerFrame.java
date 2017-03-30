/* 
 * ServerFrame
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
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import suncertify.db.Data;

/**
 * Graphical user interface window that displays the status of the running
 * remote database server.
 * 
 * @author Filipe João Mendes Rosa
 */
public class ServerFrame extends JFrame {
    
    /**
     * Widget tag specifying that this area of the screen relates to the status
     * of the running remote database server.
     */
    private final JLabel serverStatus;
    
    /**
     * Graphical widget that allows the user to stop the remote server and exit
     * the program.
     */
    private final JButton exit;

    /**
     * Creates and displays the remote server status window.
     * 
     * @param title the title of the remote server status window
     */
    public ServerFrame(String title) {
        super(title);
        serverStatus = new JLabel("Server Running", JLabel.CENTER);
        serverStatus.setToolTipText("Server is running...");
        FlowLayout flowlayout = new FlowLayout(FlowLayout.CENTER);
        JPanel panel = new JPanel(flowlayout, true);
        panel.add(serverStatus);
        exit = new JButton("Exit");
        exit.setMnemonic(KeyEvent.VK_E);
        exit.setToolTipText("Press Exit to shutdown the server and quit.");
        exit.addActionListener(new ExitActionListener());
        FlowLayout flowlayout1 = new FlowLayout(FlowLayout.TRAILING);
        JPanel panel1 = new JPanel(flowlayout1, true);
        panel1.add(exit);
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(panel1, BorderLayout.PAGE_END);
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menu.setToolTipText("Press File to see the options.");
        JMenuItem item = new JMenuItem("Exit", KeyEvent.VK_E);
        item.addActionListener(new ExitActionListener());
        item.setToolTipText("Press Exit to shutdown the server and quit.");
        menu.add(item);
        menubar.add(menu);
        setJMenuBar(menubar);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
    }

    /**
     * Implementation of a graphical user interface triggering action, that
     * allow the user to stop the remote server and exit the program.
     */
    private class ExitActionListener implements ActionListener {
        
        /**
         * Opens a graphical user interface window to give the user a last
         * chance to think he really wants to stop the remote server and exit 
         * the program. If the user presses the "yes" button, the singleton 
         * cache is persisted back to the database file, the remote server 
         * stops and the program exits, otherwise the user is taken back to the
         * remote server status window.
         * 
         * @param e the event that triggered the action
         * 
         * @see suncertify.db.Data
         * 
         * @see suncertify.persistence.DBDML
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            int option = JOptionPane.showConfirmDialog(ServerFrame.this,
                    "Are you sure you want to shutdown the server and quit?",
                    "Shutdown Server", JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        try {
                            Data.getDbDML().persist(Data.getList());
                        } catch (IOException ioexception) {
                            JOptionPane.showMessageDialog(null, "There "
                                    + "was an error while writing the data from"
                                    + " cache to the database file.", "Exit",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                System.exit(0);
            }

        }

    }

}
