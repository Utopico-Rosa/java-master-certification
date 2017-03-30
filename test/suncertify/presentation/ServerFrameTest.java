/* 
 * ServerFrameTest
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
import java.lang.reflect.Field;
import javax.swing.JButton;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import suncertify.persistence.DBDML;
import suncertify.persistence.DBFile;
import suncertify.db.Data;

/**
 * @author Filipe João Mendes Rosa
 */
public class ServerFrameTest {

    public ServerFrameTest() {
    }

    @Test
    public void testIsVisible() {
        ServerFrame serverframe = new ServerFrame("SERVER");
        assertTrue(serverframe.isVisible());
    }
    
    @Ignore("Must be used in isolation. Code under tests halts the JVM.")
    @Test
    public void ExitActionListenerAddShutDownHookTest() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));

        ServerFrame serverframe
                = ServerFrame.class.getConstructor(String.class).newInstance(
                "SERVER");
        Field field = ServerFrame.class.getDeclaredField("button");
        field.setAccessible(true);

        JButton button = (JButton) field.get(serverframe);

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

        button.doClick();

        assertFalse(serverframe.isVisible());

    }

}
