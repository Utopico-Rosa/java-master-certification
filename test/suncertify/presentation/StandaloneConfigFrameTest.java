/* 
 * StandaloneConfigFrameTest
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
import javax.swing.JButton;
import javax.swing.JTextField;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Filipe João Mendes Rosa
 */
public class StandaloneConfigFrameTest {

    public StandaloneConfigFrameTest() {
    }

    @Test
    public void testIsVisible() {
        StandaloneConfigFrame standaloneconfigframe = new StandaloneConfigFrame(
                "Config Frame");
        assertTrue(standaloneconfigframe.isVisible());
    }

    @Test
    public void testOkActionListener() throws Exception {
        StandaloneConfigFrame standaloneconfigframe
                = StandaloneConfigFrame.class.getConstructor(String.class).
                newInstance("Local Client Config");
        Field field = StandaloneConfigFrame.class.getDeclaredField("ok");
        field.setAccessible(true);
        Field field1 = StandaloneConfigFrame.class.getDeclaredField("fileName");
        field1.setAccessible(true);

        JButton button1 = (JButton) field.get(standaloneconfigframe);
        JTextField textfield = (JTextField) field1.get(standaloneconfigframe);

        textfield.setText(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db");

        button1.doClick();

        assertFalse(standaloneconfigframe.isVisible());

    }

    @Test
    public void testOkActionListenerPropertiesSaved() throws Exception {
        StandaloneConfigFrame standaloneconfigframe
                = StandaloneConfigFrame.class.getConstructor(String.class).
                newInstance("Local Client Config");
        Field field = StandaloneConfigFrame.class.getDeclaredField("ok");
        field.setAccessible(true);
        Field field1 = StandaloneConfigFrame.class.getDeclaredField("fileName");
        field1.setAccessible(true);

        JButton button1 = (JButton) field.get(standaloneconfigframe);
        JTextField textfield = (JTextField) field1.get(standaloneconfigframe);

        textfield.setText(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db");
        button1.doClick();

        String filename = textfield.getText();
        PropertyManager propertymanager = propertymanager = new PropertyManager(
                new File("suncertify.properties"));

        assertEquals(filename, propertymanager.getProperty("FILE"));

    }

    @Test
    public void testOkActionListenerJtextFieldblank() throws Exception {
        StandaloneConfigFrame standaloneconfigframe
                = StandaloneConfigFrame.class.getConstructor(String.class).
                newInstance("Local Client Config");
        Field field = StandaloneConfigFrame.class.getDeclaredField("ok");
        field.setAccessible(true);
        Field field1 = StandaloneConfigFrame.class.getDeclaredField("fileName");
        field1.setAccessible(true);

        JButton button1 = (JButton) field.get(standaloneconfigframe);
        JTextField textfield = (JTextField) field1.get(standaloneconfigframe);

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

        assertTrue(standaloneconfigframe.isVisible());

    }

    @Test
    public void testOkActionListenerFileNameNotSuitable() throws Exception {
        StandaloneConfigFrame standaloneconfigframe
                = StandaloneConfigFrame.class
                .getConstructor(String.class).newInstance("Local Client "
                + "Config");
        Field field = StandaloneConfigFrame.class.getDeclaredField("ok");
        field.setAccessible(true);
        Field field1 = StandaloneConfigFrame.class.getDeclaredField("fileName");
        field1.setAccessible(true);

        JButton button1 = (JButton) field.get(standaloneconfigframe);
        JTextField textfield = (JTextField) field1.get(standaloneconfigframe);

        textfield.setText(
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

        assertTrue(standaloneconfigframe.isVisible());

    }

    @Test
    public void testBrowseActionListener() throws Exception {
        StandaloneConfigFrame standaloneconfigframe
                = StandaloneConfigFrame.class
                .getConstructor(String.class).newInstance("Local Client "
                + "Config");
        Field field = StandaloneConfigFrame.class.getDeclaredField("browse");
        field.setAccessible(true);
        Field field1 = StandaloneConfigFrame.class.getDeclaredField("fileName");
        field1.setAccessible(true);

        JButton button = (JButton) field.get(standaloneconfigframe);
        JTextField textfield = (JTextField) field1.get(standaloneconfigframe);

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

        assertTrue(textfield.getText().contains("etc"));

    }

    @Test
    public void testExitActionListener() throws Exception {
        StandaloneConfigFrame standaloneconfigframe
                = StandaloneConfigFrame.class
                .getConstructor(String.class).newInstance("Local Client "
                + "Config");
        Field field = StandaloneConfigFrame.class.getDeclaredField("exit");
        field.setAccessible(true);

        JButton button2 = (JButton) field.get(standaloneconfigframe);

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

        assertFalse(standaloneconfigframe.isVisible());

    }

}
