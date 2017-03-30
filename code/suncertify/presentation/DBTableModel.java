/* 
 * DBTableModel
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

import javax.swing.table.AbstractTableModel;

/**
 * Concrete implementation of <code>javax.swing.table.AbstractTableModel</code>,
 * suited to the database schema of the database file. 
 * 
 * @author Filipe João Mendes Rosa
 * 
 * @see suncertify.persistence.DBFile
 */

public class DBTableModel extends AbstractTableModel {

    /**
     * Ordered names of the database columns.
     */
    String[] columns = {"name", "location", "specialties", "size", "rate",
        "owner"};
    
    /**
     * Ordered data of the rows of the database table.
     */
    private Object[][] data;

    /**
     * Initializes this instance state with the rows for the database table.
     * 
     * @param data the ordered rows of the database table
     */
    public DBTableModel(Object[][] data) {
        this.data = data;
    }
    
    /**
     * Gets the number of columns on the table.
     * 
     * @return the number of columns on the table
     */    
    @Override
    public int getColumnCount() {
        return columns.length;
    }
    
    /**
     * Gets the number of rows on the table.
     * 
     * @return the number of rows on the table
     */  
    @Override
    public int getRowCount() {
        return data.length;
    }
    
    /**
     * Gets the reference value of a cell, based upon the relation between rows
     * and columns.
     * 
     * @param rowIndex the zero-based index of the row
     * 
     * @param columnIndex the zero-based index of the column
     * 
     * @return the reference value of a cell
     */  
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }
    
    /**
     * Gets the name of a column.
     * 
     * @param column the zero-based index of the column
     *
     * @return the name of the column
     */
    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    /**
     * Accesses the ordered data of the rows of the database table.
     * 
     * @return the ordered data of the rows of the database table
     */
    public Object[][] getData() {
        return data;
    }

    /**
     * Changes the ordered data of the rows of the database table.
     * 
     * @param data the new ordered data of the rows of the database table
     */
    public void setData(Object[][] data) {
        this.data = data;
    }

}
