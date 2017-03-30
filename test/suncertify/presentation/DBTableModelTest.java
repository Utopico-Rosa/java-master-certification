/* 
 * DBTableModelTest
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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Filipe João Mendes Rosa
 */
public class DBTableModelTest {

    static String[][] data;
    static DBTableModel tableModel;
    static String[] columns;

    public DBTableModelTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        data = new String[][]{{"aaa", "bbb", "ccc", "ddd", "eee", "fff"},
        {"ggg", "hhh", "iii", "jjj", "kkk", "lll"},
        {"mmm", "nnn", "ooo", "ppp", "qqq", "rrr"}};
        tableModel = new DBTableModel(data);
        columns = new String[]{"name", "location", "specialties", "size",
            "rate", "owner"};
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetColumnCount() {
        assertEquals(6, tableModel.getColumnCount());
    }

    @Test
    public void testGetRowCount() {
        assertEquals(data.length, tableModel.getRowCount());
    }

    @Test
    public void testGetValueAt() {
        assertEquals("iii", tableModel.getValueAt(1, 2));
    }

    @Test
    public void getColumnName() {
        assertEquals("rate", tableModel.getColumnName(4));
    }

    @Test
    public void getData() {
        assertSame(data, tableModel.getData());
    }

    @Test
    public void setData() {
        String[][] data2 = new String[][]{{"a", "b", "c", "d", "e", "f"},
        {"g", "h", "i", "j", "k", "l"},
        {"m", "n", "o", "p", "q", "r"}};
        tableModel.setData(data2);
        assertSame(data2, tableModel.getData());
        assertNotSame(data, tableModel.getData());
    }
}
