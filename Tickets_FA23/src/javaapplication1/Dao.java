/*
 * Author: Sufyan Khan and Mbargou Gueye
 * Date: 12/02/2023
 * File: Dao.java
 * Description: The IIT Help Desk application's database operations are managed by the {Dao} class.
 */
package javaapplication1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Dao {
    static Connection connect = null;
    Statement statement = null;

    // JDBC URL, database credentials
    static final String DB_URL = "jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false";
    static final String USER = "fp411", PASS = "411";

    // Establishes the database connection
    public Connection getConnection() {
        // Try to establish a connection to the database
        try {
            connect = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connect;
    }

    // Creates required tables if they don't exist
    public void createTables() {
        // SQL statements to create tables
        final String createTicketsTable = "CREATE TABLE IF NOT EXISTS SkhanMgueye_tickets(ticket_id INT AUTO_INCREMENT PRIMARY KEY, ticket_issuer VARCHAR(30), ticket_description VARCHAR(200), opened DATETIME, ticket_status VARCHAR(10))";
        final String createUsersTable = "CREATE TABLE IF NOT EXISTS SkhanMgueye_users(uid INT AUTO_INCREMENT PRIMARY KEY, uname VARCHAR(30), upass VARCHAR(30), admin INT)";
        final String createCommentsTable = "CREATE TABLE IF NOT EXISTS SkhanMgueye_comments(comment_id INT AUTO_INCREMENT PRIMARY KEY, ticket_id INT, comment_text VARCHAR(200), FOREIGN KEY (ticket_id) REFERENCES SkhanMgueye_tickets(ticket_id))";

        try {
            // Create tables in the database
            statement = getConnection().createStatement();
            statement.executeUpdate(createTicketsTable);
            statement.executeUpdate(createUsersTable);
            statement.executeUpdate(createCommentsTable);
            System.out.println("Created tables in the given database...");
            statement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        addUsers();
    }

    // Adds a column 'ticket_status' to the 'SkhanMgueye_tickets' table
    public void addColumnToTicketsTable() {
        // Try adding a new column to the tickets table
        try {
            // Alter table to add a column
            Connection connection = getConnection();
            String alterTableQuery = "ALTER TABLE SkhanMgueye_tickets ADD COLUMN ticket_status VARCHAR(10)";
            Statement statement = connection.createStatement();
            statement.executeUpdate(alterTableQuery);
            statement.close();
            connection.close();
            System.out.println("Column 'ticket_status' added to SkhanMgueye_tickets table successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding column 'ticket_status': " + e.getMessage());
        }
    }

    // Adds user data from a CSV file to the 'SkhanMgueye_users' table
    public void addUsers() {
        // Try adding users from a CSV file to the database
        String sql;
        Statement statement;
        BufferedReader br;
        List<List<String>> array = new ArrayList<>();

        try {
            // Read user data from CSV file
            br = new BufferedReader(new FileReader(new File("./userlist.csv")));
            String line;
            while ((line = br.readLine()) != null) {
                array.add(Arrays.asList(line.split(",")));
            }
        } catch (Exception e) {
            System.out.println("There was a problem loading the file");
        }

        try {
            // Insert user data into the database
            statement = getConnection().createStatement();
            for (List<String> rowData : array) {
                sql = "INSERT INTO SkhanMgueye_users(uname, upass, admin) VALUES('" + rowData.get(0) + "','" + rowData.get(1) + "','" + rowData.get(2) + "')";
                statement.executeUpdate(sql);
            }
            System.out.println("Inserts completed in the given database...");
            statement.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Inserts a new ticket record into the 'SkhanMgueye_tickets' table
    public int insertRecords(String ticketName, String ticketDesc) {
        // Try inserting a new ticket record into the database
        int id = 0;
        try {
            // Prepare and execute INSERT SQL query for a new ticket
            String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
            String query = "INSERT INTO SkhanMgueye_tickets (ticket_issuer, ticket_description, opened, ticket_status) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            // Set parameters for the new ticket
            preparedStatement.setString(1, ticketName);
            preparedStatement.setString(2, ticketDesc);
            preparedStatement.setString(3, timeStamp);
            preparedStatement.setString(4, "OPEN");

            // Execute the INSERT query and retrieve the generated ID
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    // Retrieves all ticket records from the 'SkhanMgueye_tickets' table
    public ResultSet readRecords() {
        // Try fetching all ticket records from the database
        ResultSet results = null;
        try {
            statement = connect.createStatement();
            results = statement.executeQuery("SELECT * FROM SkhanMgueye_tickets");
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return results;
    }

    // Selects ticket records by ID from the 'SkhanMgueye_tickets' table
    public ResultSet selectRecords(int tid) {
        // Try selecting ticket records by ID from the database
        ResultSet results = null;
        try {
            statement = connect.createStatement();
            results = statement.executeQuery("SELECT * FROM SkhanMgueye_tickets WHERE ticket_id = " + tid);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return results;
    }

    // Updates ticket details in the 'SkhanMgueye_tickets' table
    public void updateRecords(int ticketId, String newDescription, String status) {
        // Try updating ticket records in the database
        try {
            // Prepare SQL statements for updating ticket details
            PreparedStatement ps = connect.prepareStatement("SELECT ticket_description FROM SkhanMgueye_tickets WHERE ticket_id = ?");
            ps.setInt(1, ticketId);
            ResultSet rs = ps.executeQuery();
            String currentDescription = "";
            if (rs.next()) {
                currentDescription = rs.getString("ticket_description");
            }
            rs.close();
            ps.close();

            // Append the new description to the existing one
            String updatedDescription = currentDescription + "\nUpdate: " + newDescription;

            PreparedStatement updatePs = connect.prepareStatement("UPDATE SkhanMgueye_tickets SET ticket_description = ?, ticket_status = ? WHERE ticket_id = ?");
            updatePs.setString(1, updatedDescription);
            updatePs.setString(2, status);
            updatePs.setInt(3, ticketId);
            updatePs.executeUpdate();
            updatePs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Deletes a ticket record by ID from the 'SkhanMgueye_tickets' table
    public int deleteRecords(int tid) {
        // Try deleting a ticket record by ID from the database
        try {
            statement = connect.createStatement();
            String sql = "DELETE FROM SkhanMgueye_tickets WHERE ticket_id = " + tid;
            statement.executeUpdate(sql);
            System.out.println("Ticket ID : " + tid + " deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tid;
    }

    // Checks if a ticket with the given ID exists in the database
    public boolean checkTicketExists(int ticketId) {
        // Try checking if a ticket exists in the database
        boolean exists = false;
        try {
            // Prepare a SQL statement to check for the existence of the ticket ID
            PreparedStatement ps = connect.prepareStatement("SELECT * FROM SkhanMgueye_tickets WHERE ticket_id = ?");
            ps.setInt(1, ticketId);
            ResultSet rs = ps.executeQuery();
            exists = rs.next(); // If there is a next record, the ticket exists
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    // Updates the status of a ticket with the given ID
    public boolean updateTicketStatus(int ticketId, String newStatus) {
        // Try updating the status of a ticket in the database
        boolean updated = false;
        try {
            // Prepare a SQL statement to update the ticket status
            PreparedStatement ps = connect.prepareStatement("UPDATE SkhanMgueye_tickets SET ticket_status = ? WHERE ticket_id = ?");
            ps.setString(1, newStatus);
            ps.setInt(2, ticketId);
            int rowsAffected = ps.executeUpdate();
            updated = rowsAffected > 0; // If rows were affected, the update was successful
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updated;
    }

    
    public void updateTicket(int ticketIdToUpdate, String newTicketDescription) {
        // TODO: Implement updateTicket method
    }

    public void deleteTicket(int ticketIdToDelete) {
        // TODO: Implement deleteTicket method
    }

    public boolean updateTicketDescription(int ticketId, String newDescription) {
        boolean descriptionUpdated = false;
        try {
            // Prepare a SQL statement to update the ticket description
            PreparedStatement ps = connect.prepareStatement("UPDATE SkhanMgueye_tickets SET ticket_description = ? WHERE ticket_id = ?");
            ps.setString(1, newDescription);
            ps.setInt(2, ticketId);

            // Execute the update query
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                descriptionUpdated = true;
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return descriptionUpdated;
    }
}
