/* 
 * PropertyManagerTest
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

import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Filipe João Mendes Rosa
 */
public class PropertyManagerTest {

    public PropertyManagerTest() {
    }

    @Test
    public void testCreatesNewFile() throws Exception {
        File file = new File("teste");
        assertFalse(file.exists());
        PropertyManager manager = new PropertyManager(new File("teste"));
        assertTrue(file.exists());
        file.delete();
    }

    @Test
    public void testGetSetProperty() throws Exception {
        File file = new File("teste");
        assertFalse(file.exists());
        PropertyManager manager = new PropertyManager(new File("teste"));
        assertTrue(file.exists());
        assertNotEquals("world", manager.getProperty("hello"));
        manager.addProperty("hello", "world");
        assertEquals("world", manager.getProperty("hello"));
        file.delete();
    }

    @Test
    public void testSaveProperties() throws Exception {
        File file = new File("teste");
        assertFalse(file.exists());
        PropertyManager manager = new PropertyManager(new File("teste"));
        assertTrue(file.exists());
        assertNotEquals("world", manager.getProperty("hello"));
        manager.addProperty("hello", "world");
        assertEquals("world", manager.getProperty("hello"));
        assertTrue(file.length() == 0);
        manager.saveProperties();
        assertTrue(file.length() > 0);
        file.delete();
    }

    @Test
    public void readPropertyFromFile() throws Exception {
        File file = new File("teste");
        assertFalse(file.exists());
        PropertyManager manager = new PropertyManager(new File("teste"));
        assertTrue(file.exists());
        assertNotEquals("world", manager.getProperty("hello"));
        manager.addProperty("hello", "world");
        assertEquals("world", manager.getProperty("hello"));
        assertTrue(file.length() == 0);
        manager.saveProperties();
        assertTrue(file.length() > 0);
        manager = new PropertyManager(file);
        assertEquals("world", manager.getProperty("hello"));
        file.delete();
    }

}
