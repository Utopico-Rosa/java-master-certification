/* 
 * NetworkedConfigFrameTest
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
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JButton;
import javax.swing.JTextField;
import org.junit.Test;
import static org.junit.Assert.*;
import suncertify.integration.DataRemote;
import suncertify.persistence.DBDML;
import suncertify.persistence.DBFile;
import suncertify.db.Data;

/**
 * @author Filipe João Mendes Rosa
 */
public class NetworkedConfigFrameTest {

    public NetworkedConfigFrameTest() {
    }

    @Test
    public void testIsVisible() {
        NetworkedConfigFrame networkedconfigframe = new NetworkedConfigFrame(
                "Remote Client Configuration");
        assertTrue(networkedconfigframe.isVisible());
    }

    @Test
    public void testOkActionListener() throws Exception {
        NetworkedConfigFrame networkedconfigframe = NetworkedConfigFrame.class.
                getConstructor(String.class).newInstance("Remote Client "
                + "Config");
        Field field = NetworkedConfigFrame.class.getDeclaredField("ok");
        field.setAccessible(true);
        Field field1 = NetworkedConfigFrame.class.getDeclaredField("hostText");
        field1.setAccessible(true);
        Field field2 = NetworkedConfigFrame.class.getDeclaredField("portText");
        field2.setAccessible(true);

        JButton button1 = (JButton) field.get(networkedconfigframe);
        JTextField textfield = (JTextField) field1.get(networkedconfigframe);
        JTextField textfield2 = (JTextField) field2.get(networkedconfigframe);

        textfield.setText("localhost");
        textfield2.setText("5014");
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        DataRemote dataremote = new DataRemote(Data.getInstance());
        Registry registry = LocateRegistry.createRegistry(5014);
        registry.rebind("SERVER", dataremote);

        button1.doClick();

        assertFalse(networkedconfigframe.isVisible());

    }

    @Test
    public void testOkActionListenerJtextFieldblank() throws Exception {
        NetworkedConfigFrame networkedconfigframe = NetworkedConfigFrame.class.
                getConstructor(String.class).newInstance("Remote Client "
                + "Config");
        Field field = NetworkedConfigFrame.class.getDeclaredField("ok");
        field.setAccessible(true);
        Field field1 = NetworkedConfigFrame.class.getDeclaredField("hostText");
        field1.setAccessible(true);
        Field field2 = NetworkedConfigFrame.class.getDeclaredField("portText");
        field2.setAccessible(true);

        JButton button1 = (JButton) field.get(networkedconfigframe);
        JTextField textfield = (JTextField) field1.get(networkedconfigframe);
        JTextField textfield2 = (JTextField) field2.get(networkedconfigframe);

        textfield.setText("");
        textfield.setText("");

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

        assertTrue(networkedconfigframe.isVisible());

    }

    @Test
    public void testActionListenerHostAddressIsNotReachable() throws Exception {
        NetworkedConfigFrame networkedconfigframe = NetworkedConfigFrame.class.
                getConstructor(String.class).newInstance("Remote Client "
                + "Config");
        Field field = NetworkedConfigFrame.class.getDeclaredField("ok");
        field.setAccessible(true);
        Field field1 = NetworkedConfigFrame.class.getDeclaredField("hostText");
        field1.setAccessible(true);
        Field field2 = NetworkedConfigFrame.class.getDeclaredField("portText");
        field2.setAccessible(true);

        JButton button1 = (JButton) field.get(networkedconfigframe);
        JTextField textfield = (JTextField) field1.get(networkedconfigframe);
        JTextField textfield2 = (JTextField) field2.get(networkedconfigframe);

        textfield.setText("localhost");
        textfield2.setText("5014");

        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        DataRemote dataremote = new DataRemote(Data.getInstance());
        Registry registry = LocateRegistry.createRegistry(5015);
        registry.rebind("SERVER", dataremote);

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

        assertTrue(networkedconfigframe.isVisible());
    }

    @Test
    public void testOkActionListenerPortNumberNotInValidRange()
            throws Exception {
        NetworkedConfigFrame networkedconfigframe = NetworkedConfigFrame.class.
                getConstructor(String.class).newInstance("Remote Client "
                + "Config");
        Field field = NetworkedConfigFrame.class.getDeclaredField("ok");
        field.setAccessible(true);
        Field field1 = NetworkedConfigFrame.class.getDeclaredField("hostText");
        field1.setAccessible(true);
        Field field2 = NetworkedConfigFrame.class.getDeclaredField("portText");
        field2.setAccessible(true);

        JButton button1 = (JButton) field.get(networkedconfigframe);
        JTextField textfield = (JTextField) field1.get(networkedconfigframe);
        JTextField textfield2 = (JTextField) field2.get(networkedconfigframe);

        textfield.setText("localhost");
        textfield2.setText("501555");

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

        assertTrue(networkedconfigframe.isVisible());
    }

    @Test
    public void testOkActionListenerPropertiesSaved() throws Exception {
        NetworkedConfigFrame networkedconfigframe = NetworkedConfigFrame.class.
                getConstructor(String.class).newInstance("Remote Client "
                + "Config");
        Field field = NetworkedConfigFrame.class.getDeclaredField("ok");
        field.setAccessible(true);
        Field field1 = NetworkedConfigFrame.class.getDeclaredField("hostText");
        field1.setAccessible(true);
        Field field2 = NetworkedConfigFrame.class.getDeclaredField("portText");
        field2.setAccessible(true);

        JButton button1 = (JButton) field.get(networkedconfigframe);
        JTextField textfield = (JTextField) field1.get(networkedconfigframe);
        JTextField textfield2 = (JTextField) field2.get(networkedconfigframe);

        textfield.setText("localhost");
        textfield2.setText("5033");
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        DataRemote dataremote = new DataRemote(Data.getInstance());
        Registry registry = LocateRegistry.createRegistry(5033);
        registry.rebind("SERVER", dataremote);

        button1.doClick();

        String host = textfield.getText();
        String port = textfield2.getText();
        PropertyManager propertymanager = propertymanager = new PropertyManager(
                new File("suncertify.properties"));

        assertEquals(host, propertymanager.getProperty("HOST"));
        assertEquals(port, propertymanager.getProperty("PORT"));
    }

    @Test
    public void testExitActionListener() throws Exception {
        NetworkedConfigFrame networkedconfigframe = NetworkedConfigFrame.class.
                getConstructor(String.class).newInstance("Remote Client "
                + "Config");
        Field field = NetworkedConfigFrame.class.getDeclaredField("exit");
        field.setAccessible(true);

        JButton button1 = (JButton) field.get(networkedconfigframe);

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

        assertFalse(networkedconfigframe.isVisible());

    }

}
