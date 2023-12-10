/*
 * Author: Sufyan Khan and Mbargou Gueye
 * Date: 12/01/2023
 * File: Login.java
 * Description: This Java application provides a GUI-based login interface using Swing components.
 * It limits the number of login tries, validates user credentials against a database table, and, after a successful login, grants access to a help desk system.
 */
package javaapplication1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("serial")
public class Login extends JFrame {

    Dao conn;

    public Login() {
        super("IIT HELP DESK LOGIN");

        // Initialize the database connection
        conn = new Dao();
        conn.createTables(); // Create necessary tables if they don't exist in the database

        // Set the background color
        getContentPane().setBackground(Color.green);

        // Set the layout and size
        setLayout(new BorderLayout()); // Use BorderLayout for organized positioning
        setSize(400, 210); // Initial size
        setLocationRelativeTo(null); // Set window location to the center of the screen

        // Create title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.green);

        JLabel titleLabel = new JLabel("IIT Help Desk Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Set title font and size
        titleLabel.setForeground(Color.black);

        titlePanel.add(titleLabel);

        // Create input panel for username and password fields
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5)); // GridLayout for structured arrangement
        inputPanel.setBackground(Color.green);

        JLabel lblUsername = new JLabel("Username:", JLabel.LEFT);
        lblUsername.setFont(lblUsername.getFont().deriveFont(Font.BOLD)); // Set font style to bold
        JTextField txtUname = new JTextField(10); // Username text field
        JLabel lblPassword = new JLabel("Password:", JLabel.LEFT);
        lblPassword.setFont(lblPassword.getFont().deriveFont(Font.BOLD)); // Set font style to bold
        JPasswordField txtPassword = new JPasswordField(); // Password field
        JLabel lblStatus = new JLabel(" ", JLabel.CENTER); // Placeholder for status display
        lblStatus.setForeground(Color.red); // Set text color to red for status label

        inputPanel.add(lblUsername);
        inputPanel.add(txtUname);
        inputPanel.add(lblPassword);
        inputPanel.add(txtPassword);
        inputPanel.add(lblStatus); // Display status

        // Create button panel for Login and Exit buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.green);

        JButton btnExit = new JButton("Exit");
        btnExit.setBackground(Color.black); // Set button background color
        btnExit.setForeground(Color.white); // Set button text color
        JButton btn = new JButton("Log In");
        btn.setBackground(Color.black); // Set button background color
        btn.setForeground(Color.white); // Set button text color

        buttonPanel.add(btnExit);
        buttonPanel.add(btn);

        // Add components to the frame
        add(titlePanel, BorderLayout.NORTH); // Add title panel to the top
        add(inputPanel, BorderLayout.CENTER); // Add input fields panel in the center
        add(buttonPanel, BorderLayout.SOUTH); // Add button panel at the bottom

        // Button click actions
        btn.addActionListener(new ActionListener() {
            int count = 0; // Variable to track login attempts

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isAdmin = false;
                count = count + 1; // Increment attempt count on button click

                String query = "SELECT * FROM SkhanMgueye_users WHERE uname = ? AND upass = ?";
                try (PreparedStatement stmt = conn.getConnection().prepareStatement(query)) {
                    stmt.setString(1, txtUname.getText()); // Get username entered by user
                    stmt.setString(2, String.valueOf(txtPassword.getPassword())); // Get password entered by user
                    ResultSet rs = stmt.executeQuery(); // Execute the query
                    if (rs.next()) {
                        isAdmin = (rs.getInt("admin") == 1); // Check if user is an admin
                        System.out.println("Login success.");
                        new Tickets(isAdmin); // Create Tickets object with isAdmin flag
                        setVisible(false); // Hide the login window
                        dispose(); // Dispose of the login window resources
                    } else {
                        lblStatus.setText("Try again! " + (3 - count) + " / 3 attempts left"); // Display status message
                        System.out.println("Try again! " + (3 - count) + " / 3 attempts left");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace(); // Print SQL exception stack trace
                }
            }
        });

        btnExit.addActionListener(e -> System.exit(0)); // Exit the application when Exit button is clicked

        // Set visibility of the login window
        setVisible(true);
    }

    public static void main(String[] args) {
        new Login(); // Create an instance of the Login class to start the application
    }
}