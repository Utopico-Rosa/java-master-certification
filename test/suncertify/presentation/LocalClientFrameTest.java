/* 
 * LocalClientFrameTest
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
import java.lang.reflect.Method;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import suncertify.integration.DataLocal;
import suncertify.integration.DataMarker;
import suncertify.persistence.DBDML;
import suncertify.persistence.DBFile;
import suncertify.db.Data;

/**
 * @author Filipe João Mendes Rosa
 */
public class LocalClientFrameTest {

    public LocalClientFrameTest() {
    }

    @Test
    public void testIsVisible() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        assertThat(Data.getList(), is(not(empty())));
        DataLocal datalocal = new DataLocal(data);
        LocalClientFrame localclienframe = new LocalClientFrame("Local Client",
                datalocal);
        assertTrue(localclienframe.isVisible());
    }

    @Test
    public void SearchActionListener() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        int size = Data.getList().size();
        assertThat(Data.getList(), is(not(empty())));
        DataLocal datalocal = new DataLocal(data);

        LocalClientFrame localclientframe = LocalClientFrame.class.
                getConstructor(String.class, DataMarker.class).newInstance(
                "Local Client", datalocal);
        Field field = ClientFrame.class.getDeclaredField("search");
        field.setAccessible(true);
        Field field1 = ClientFrame.class.getDeclaredField("name");
        field1.setAccessible(true);
        Field field2 = ClientFrame.class.getDeclaredField("location");
        field2.setAccessible(true);
        Field field3 = ClientFrame.class.getDeclaredField("table");
        field3.setAccessible(true);

        JButton button = (JButton) field.get(localclientframe);
        JTextField textfield = (JTextField) field1.get(localclientframe);
        JTextField textfield1 = (JTextField) field2.get(localclientframe);
        JTable table = (JTable) field3.get(localclientframe);

        textfield.setText("Fred & Nobby");
        textfield1.setText("Xanadu");
        button.doClick();

        assertTrue(((DBTableModel) table.getModel()).getRowCount() == 1);

        textfield.setText("");
        textfield1.setText("");
        button.doClick();

        assertTrue(((DBTableModel) table.getModel()).getRowCount() == size);

        textfield.setText("*****");
        textfield1.setText("*****");
        button.doClick();

        assertTrue(((DBTableModel) table.getModel()).getRowCount() == 0);

        textfield.setText("Fred & Nobby");
        textfield1.setText("");
        button.doClick();

        assertTrue(((DBTableModel) table.getModel()).getRowCount() == 3);

        textfield.setText("");
        textfield1.setText("Xanadu");
        button.doClick();

        assertTrue(((DBTableModel) table.getModel()).getRowCount() == 3);

    }

    @Test
    public void testBookActionListenerZeroRowsSelected() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        int size = Data.getList().size();
        assertThat(Data.getList(), is(not(empty())));
        DataLocal datalocal = new DataLocal(data);

        LocalClientFrame localclientframe = LocalClientFrame.class.
                getConstructor(String.class, DataMarker.class).newInstance(
                "Local Client", datalocal);
        Field field = ClientFrame.class.getDeclaredField("book");
        field.setAccessible(true);
        Field field1 = ClientFrame.class.getDeclaredField("table");
        field1.setAccessible(true);

        JButton button1 = (JButton) field.get(localclientframe);
        JTable table = (JTable) field1.get(localclientframe);

        table.getSelectionModel().clearSelection();

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

    }

    @Test
    public void testBookActionListenerInvalidNumberTyped() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        int size = Data.getList().size();
        assertThat(Data.getList(), is(not(empty())));
        DataLocal datalocal = new DataLocal(data);

        LocalClientFrame localclientframe = LocalClientFrame.class.
                getConstructor(String.class, DataMarker.class).newInstance(
                "Local Client", datalocal);
        Field field = ClientFrame.class.getDeclaredField("book");
        field.setAccessible(true);
        Field field1 = ClientFrame.class.getDeclaredField("table");
        field1.setAccessible(true);

        JButton button1 = (JButton) field.get(localclientframe);
        JTable table = (JTable) field1.get(localclientframe);

        table.getSelectionModel().setSelectionInterval(0, 0);

        new Thread() {
            public void run() {
                Robot robot = null;
                try {
                    robot = new Robot();
                } catch (Exception e) {
                    new RuntimeException(e);
                }
                robot.delay(1000);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
                robot.delay(1000);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
            }
        }.start();

        button1.doClick();

        new Thread() {
            public void run() {
                Robot robot = null;
                try {
                    robot = new Robot();
                } catch (Exception e) {
                    new RuntimeException(e);
                }
                robot.delay(1000);
                robot.keyPress(KeyEvent.VK_B);
                robot.keyRelease(KeyEvent.VK_B);
                robot.keyPress(KeyEvent.VK_B);
                robot.keyRelease(KeyEvent.VK_B);
                robot.keyPress(KeyEvent.VK_B);
                robot.keyRelease(KeyEvent.VK_B);
                robot.keyPress(KeyEvent.VK_B);
                robot.keyRelease(KeyEvent.VK_B);
                robot.keyPress(KeyEvent.VK_B);
                robot.keyRelease(KeyEvent.VK_B);
                robot.keyPress(KeyEvent.VK_B);
                robot.keyRelease(KeyEvent.VK_B);
                robot.keyPress(KeyEvent.VK_B);
                robot.keyRelease(KeyEvent.VK_B);
                robot.keyPress(KeyEvent.VK_B);
                robot.keyRelease(KeyEvent.VK_B);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
                robot.delay(1000);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
            }
        }.start();

        button1.doClick();

    }

    @Test
    public void testBookActionListenerValidNumberTyped() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        int size = Data.getList().size();
        assertThat(Data.getList(), is(not(empty())));
        DataLocal datalocal = new DataLocal(data);

        LocalClientFrame localclientframe = LocalClientFrame.class.
                getConstructor(String.class, DataMarker.class).newInstance(
                "Local Client", datalocal);
        Field field = ClientFrame.class.getDeclaredField("book");
        field.setAccessible(true);
        Field field1 = ClientFrame.class.getDeclaredField("table");
        field1.setAccessible(true);

        JButton button1 = (JButton) field.get(localclientframe);
        JTable table = (JTable) field1.get(localclientframe);

        table.getSelectionModel().setSelectionInterval(0, 0);

        Data.getList().get(0).setOwner("        ");

        new Thread() {
            public void run() {
                Robot robot = null;
                try {
                    robot = new Robot();
                } catch (Exception e) {
                    new RuntimeException(e);
                }
                robot.delay(1000);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
            }
        }.start();

        button1.doClick();

        assertEquals("55555555", ((DBTableModel) table.getModel()).getValueAt(0,
                5));

        Data.getList().get(0).setOwner("        ");

    }

    @Test
    public void testBookActionRecordNotFound() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        int size = Data.getList().size();
        assertThat(Data.getList(), is(not(empty())));
        DataLocal datalocal = new DataLocal(data);

        LocalClientFrame localclientframe = LocalClientFrame.class.
                getConstructor(String.class, DataMarker.class).newInstance(
                "Local Client", datalocal);
        Method method = LocalClientFrame.class.getDeclaredMethod("bookCustomer",
                int.class, String[].class);
        method.setAccessible(true);

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
                robot.delay(1000);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
            }
        }.start();

        method.invoke(localclientframe, size + 1, new String[]{null, null, null,
            null, null, null, "22222222"});

    }

    @Test
    public void testBookActionClientAlreadyBooked() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        int size = Data.getList().size();
        assertThat(Data.getList(), is(not(empty())));
        DataLocal datalocal = new DataLocal(data);

        LocalClientFrame localclientframe = LocalClientFrame.class.
                getConstructor(String.class, DataMarker.class).newInstance(
                "Local Client", datalocal);
        Field field = ClientFrame.class.getDeclaredField("search");
        field.setAccessible(true);
        Field field1 = ClientFrame.class.getDeclaredField("table");
        field1.setAccessible(true);

        JButton button1 = (JButton) field.get(localclientframe);
        JTable table = (JTable) field1.get(localclientframe);

        table.getSelectionModel().setSelectionInterval(0, 0);

        Data.getList().get(0).setOwner("        ");

        new Thread() {
            public void run() {
                Robot robot = null;
                try {
                    robot = new Robot();
                } catch (Exception e) {
                    new RuntimeException(e);
                }
                robot.delay(1000);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
            }
        }.start();

        button1.doClick();

        table.getSelectionModel().setSelectionInterval(0, 0);

        new Thread() {
            public void run() {
                Robot robot = null;
                try {
                    robot = new Robot();
                } catch (Exception e) {
                    new RuntimeException(e);
                }
                robot.delay(1000);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_5);
                robot.keyRelease(KeyEvent.VK_5);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
                robot.delay(1000);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
            }
        }.start();

        button1.doClick();

    }

    @Ignore("Must be run in isolation. Code under tests halts the JVM.")
    @Test
    public void testExitListener() throws Exception {
        Data.setList(new DBDML(new DBFile(
                "/home/utopico/javamaster/MyAssignment/db-2x1-teste.db")));
        Data data = Data.getInstance();
        int size = Data.getList().size();
        assertThat(Data.getList(), is(not(empty())));
        DataLocal datalocal = new DataLocal(data);

        LocalClientFrame localclientframe = LocalClientFrame.class.
                getConstructor(String.class, DataMarker.class).newInstance(
                "Local Client", datalocal);
        Field field = ClientFrame.class.getDeclaredField("button2");
        field.setAccessible(true);

        JButton button2 = (JButton) field.get(localclientframe);

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
                robot.keyRelease(KeyEvent.VK_ENTER);;
            }
        }.start();

        button2.doClick();

        assertFalse(localclientframe.isVisible());

    }

}
