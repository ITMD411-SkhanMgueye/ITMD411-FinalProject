/*
 * Author: Sufyan Khan and Mbargou Gueye
 * Date: 12/05/2023
 * File: ticketsJTable.java
*/
package javaapplication1;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

// Class to handle building a table model for JTable from a ResultSet
public class ticketsJTable {

    // Method to create a DefaultTableModel from a ResultSet
    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // Store column names in a Vector
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // Store table data in a Vector of Vectors
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }
        // Return the data and column names as a DefaultTableModel for JTable
        return new DefaultTableModel(data, columnNames);
    }
}
