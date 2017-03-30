/* 
 * Runner
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
package suncertify.application;

import java.io.Console;
import javax.swing.SwingUtilities;
import suncertify.presentation.NetworkedConfigFrame;
import suncertify.presentation.ServerConfigFrame;
import suncertify.presentation.StandaloneConfigFrame;

/**
 * Starts the program by collecting user input and by setting visible one of
 * three configuration windows.
 * <a href="https://docs.oracle.com/javase/8/docs/api/javax/swing/JFrame.html"> 
 * See JFrame</a>.
 * <p>
 * In this windows the user can choose the options he wants, to afterwards
 * proceed to the core functionality of the program.
 *
 * @author Filipe João Mendes Rosa
 *
 * @see suncertify.presentation.NetworkedConfigFrame
 *
 * @see suncertify.presentation.ServerConfigFrame
 *
 * @see suncertify.presentation.StandaloneConfigFrame
 */
public class Runner {

    /**
     * Represents the option to start the networked server.
     */
    public static final String SERVER = "SERVER";

    /**
     * Represents the option to start the local client and server.
     */
    public static final String ALONE = "ALONE";

    /**
     * Represents the option to start the remote client.
     */
    public static final String REMOTE = "REMOTE";

    /**
     * Collects user input from the command line and starts one of three
     * configuration windows.
     * <p>
     * The user should use no command line arguments to start the networked
     * client option. He must use the command line argument "server", not
     * regarding case, to start the remote server option. And he must use the
     * command line option "alone", not regarding case, to start the local
     * client option.
     * <p>
     * If the user misspells the word on the command line argument or enters
     * more than one command line argument:
     * <ul>
     * <li>he is given a chance to correct his mistake if on the command line
     * <li>if not on the command line, the programs halts with an error message
     * </ul>
     *
     * @param args the choice the user took
     *
     * @see suncertify.presentation.NetworkedConfigFrame
     *
     * @see suncertify.presentation.ServerConfigFrame
     *
     * @see suncertify.presentation.StandaloneConfigFrame
     */
    public static void main(String... args) {
        while ((args.length == 1
                && (!args[0].trim().equalsIgnoreCase(ALONE) && !args[0].trim().
                equalsIgnoreCase(SERVER))) || args.length > 1) {
            Console console = System.console();
            if (console != null) {
                String line;
                do {
                    args[0] = line = console.readLine(
                            "Wrong arguments! You should type server for "
                            + "starting the server, or alone for starting the "
                            + "local client or simply press enter for starting "
                            + "the remote client, or press Control C to exit"
                            + " the application." + System.getProperty("line."
                                    + "separator"));
                    if (line.trim().equals("")) {
                        args = new String[0];
                    }
                } while (!line.trim().equals("") && !line.trim().
                        equalsIgnoreCase(SERVER)
                        && !line.trim().equalsIgnoreCase(ALONE));

            } else {
                System.err.println(
                        "Bad arguments. Application shutdown. To start the "
                        + "application you must type java -jar runme.jar mode,"
                        + " where mode must be replaced by server, for starting"
                        + " the server, by alone for starting the local client,"
                        + " or simply typing java -jar runme.jar for starting "
                        + "the remote client."
                );
                System.exit(1);
            }

        }
        if (args.length == 0) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new NetworkedConfigFrame("Remote Configuration");
                }
            });
        } else if (args[0].trim().equalsIgnoreCase(SERVER)) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ServerConfigFrame("Server Configuration");
                }
            });
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new StandaloneConfigFrame("Local Configuration");
                }
            });

        }

    }

}
