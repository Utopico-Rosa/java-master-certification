/* 
 * ServerConfigFrameTest
 * 
 * Filipe João Mendes Rosa
 * 
 * 27/02/2007
 * 
 * Sun Certified Developer for the Java 2 Platform: Application Submission 
 * (Version 2.1.1)
 * 
 * Java SE 6 Developer Certified Master Assignment 1Z0-855
 * 
 */
package suncertify.presentation;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JButton;
import javax.swing.JTextField;
import org.junit.Test;
import static org.junit.Assert.*;
import suncertify.integration.DBAllQueriesRemote;

/**
 * @author Filipe João Mendes Rosa
 */
public class ServerConfigFrameTest {

    public ServerConfigFrameTest() {
    }

    @Test
    public void testIsVisible() {
        ServerConfigFrame configframe = new ServerConfigFrame("Server Config");
        assertTrue(configframe.isVisible());
    }

    @Test
    public void testOkActionListenerServerBound() throws Exception {
        ServerConfigFrame configframe = ServerConfigFrame.class.getConstructor(
                String.class).newInstance("Server Config");
        Field field = ServerConfigFrame.class.getDeclaredField("ok");
        field.setAccessible(true);
        Field field1 = ServerConfigFrame.class.getDeclaredField("portText");
        field1.setAccessible(true);
        Field field2 = ServerConfigFrame.class.getDeclaredField("fileText");
        field2.setAccessible(true);

        JTextField jtextfield = (JTextField) field1.get(configframe);
        JTextField jtextfield2 = (JTextField) field2.get(configframe);
        JButton button1 = (JButton) field.get(configframe);

        jtextfield.setText("7000");
        jtextfield2.setText(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db");
        button1.doClick();

        Registry registry = LocateRegistry.getRegistry("localhost", 7000);
        DBAllQueriesRemote stub = (DBAllQueriesRemote) registry.
                lookup("SERVER");
        assertNotNull(stub);

    }

    @Test
    public void testOkActionListenerJtextFieldsblank() throws Exception {
        ServerConfigFrame configframe = ServerConfigFrame.class.getConstructor(
                String.class).newInstance("Server Config");
        Field field = ServerConfigFrame.class.getDeclaredField("ok");
        field.setAccessible(true);
        Field field1 = ServerConfigFrame.class.getDeclaredField("portText");
        field1.setAccessible(true);
        Field field2 = ServerConfigFrame.class.getDeclaredField("fileText");
        field2.setAccessible(true);

        JTextField jtextfield = (JTextField) field1.get(configframe);
        JTextField jtextfield2 = (JTextField) field2.get(configframe);
        JButton button1 = (JButton) field.get(configframe);

        jtextfield.setText("");
        jtextfield2.setText("");

        new Thread() {
            public void run() {
                Robot robot = null;
                try {
                    robot = new Robot();
                } catch (Exception e) {
                    new RuntimeException(e);
                }
                robot.delay(1000);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
            }
        }.start();

        button1.doClick();

        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry("localhost", 7000);
        } catch (RemoteException remote) {
            assertNull(registry);
        }

    }

    @Test
    public void testOkActionListenerFileNameNotSuitable() throws Exception {
        ServerConfigFrame configframe = ServerConfigFrame.class.getConstructor(
                String.class).newInstance("Server Config");
        Field field = ServerConfigFrame.class.getDeclaredField("ok");
        field.setAccessible(true);
        Field field1 = ServerConfigFrame.class.getDeclaredField("portText");
        field1.setAccessible(true);
        Field field2 = ServerConfigFrame.class.getDeclaredField("fileText");
        field2.setAccessible(true);

        JTextField jtextfield = (JTextField) field1.get(configframe);
        JTextField jtextfield2 = (JTextField) field2.get(configframe);
        JButton button1 = (JButton) field.get(configframe);

        jtextfield.setText("7000");
        jtextfield2.setText(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.dbb");

        new Thread() {
            public void run() {
                Robot robot = null;
                try {
                    robot = new Robot();
                } catch (Exception e) {
                    new RuntimeException(e);
                }
                robot.delay(1000);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
            }
        }.start();

        button1.doClick();

        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry("localhost", 7000);
        } catch (RemoteException remote) {
            assertNull(registry);
        }

    }

    @Test
    public void testOkActionListenerPortNumberNotInValidRange()
            throws Exception {
        ServerConfigFrame configframe = ServerConfigFrame.class.getConstructor(
                String.class).newInstance("Server Config");
        Field field = ServerConfigFrame.class.getDeclaredField("ok");
        field.setAccessible(true);
        Field field1 = ServerConfigFrame.class.getDeclaredField("portText");
        field1.setAccessible(true);
        Field field2 = ServerConfigFrame.class.getDeclaredField("fileText");
        field2.setAccessible(true);

        JTextField jtextfield = (JTextField) field1.get(configframe);
        JTextField jtextfield2 = (JTextField) field2.get(configframe);
        JButton button1 = (JButton) field.get(configframe);

        jtextfield.setText("70000");
        jtextfield2.setText(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db");

        new Thread() {
            public void run() {
                Robot robot = null;
                try {
                    robot = new Robot();
                } catch (Exception e) {
                    new RuntimeException(e);
                }
                robot.delay(1000);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
            }
        }.start();

        button1.doClick();

        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry("localhost", 7000);
        } catch (RemoteException remote) {
            assertNull(registry);
        }

    }

    @Test
    public void testOkActionListenerPropertiesSaved() throws Exception {
        ServerConfigFrame configframe = ServerConfigFrame.class.getConstructor(
                String.class).newInstance("Server Config");
        Field field = ServerConfigFrame.class.getDeclaredField("ok");
        field.setAccessible(true);
        Field field1 = ServerConfigFrame.class.getDeclaredField("portText");
        field1.setAccessible(true);
        Field field2 = ServerConfigFrame.class.getDeclaredField("fileText");
        field2.setAccessible(true);

        JTextField jtextfield = (JTextField) field1.get(configframe);
        JTextField jtextfield2 = (JTextField) field2.get(configframe);
        JButton button1 = (JButton) field.get(configframe);

        jtextfield.setText("9500");
        jtextfield2.setText(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db");
        button1.doClick();

        String portnumber = jtextfield.getText();
        String filename = jtextfield2.getText();
        PropertyManager propertymanager = propertymanager = new PropertyManager(
                new File("suncertify.properties"));

        assertEquals(portnumber, propertymanager.getProperty("PORT"));
        assertEquals(filename, propertymanager.getProperty("FILE"));

    }

    @Test
    public void testBrowseActionListener() throws Exception {
        ServerConfigFrame configframe = ServerConfigFrame.class.getConstructor(
                String.class).newInstance("Server Config");
        Field field = ServerConfigFrame.class.getDeclaredField("browse");
        field.setAccessible(true);
        Field field2 = ServerConfigFrame.class.getDeclaredField("fileText");
        field2.setAccessible(true);

        JButton button = (JButton) field.get(configframe);
        JTextField jtextfield2 = (JTextField) field2.get(configframe);

        new Thread() {
            public void run() {
                Robot robot = null;
                try {
                    robot = new Robot();
                } catch (Exception e) {
                    new RuntimeException(e);
                }
                robot.delay(1000);
                robot.keyPress(KeyEvent.VK_E);
                robot.keyRelease(KeyEvent.VK_E);
                robot.keyPress(KeyEvent.VK_T);
                robot.keyRelease(KeyEvent.VK_T);
                robot.keyPress(KeyEvent.VK_C);
                robot.keyRelease(KeyEvent.VK_C);
                robot.delay(1000);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
            }
        }.start();

        button.doClick();

        assertTrue(jtextfield2.getText().contains("etc"));

    }

    @Test
    public void testExitActionListener() throws Exception {
        ServerConfigFrame configframe = ServerConfigFrame.class.getConstructor(
                String.class).newInstance("Server Config");
        Field field = ServerConfigFrame.class.getDeclaredField("exit");
        field.setAccessible(true);

        JButton button2 = (JButton) field.get(configframe);

        new Thread() {
            public void run() {
                Robot robot = null;
                try {
                    robot = new Robot();
                } catch (Exception e) {
                    new RuntimeException(e);
                }
                robot.delay(1000);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
            }
        }.start();

        button2.doClick();

        assertFalse(configframe.isVisible());

    }

}
