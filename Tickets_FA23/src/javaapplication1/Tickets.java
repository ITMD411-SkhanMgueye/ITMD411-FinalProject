/*
 * Author: Sufyan Khan and Mbargou Gueye
 * Date: 12/06/2023
 * File: Tickets.java
 */
package javaapplication1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class Tickets extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    Dao dao = new Dao();
    boolean chkIfAdmin = false;

    private JMenu mnuFile = new JMenu("File");
    private JMenu mnuAdmin = new JMenu("Admin");
    private JMenu mnuTickets = new JMenu("Tickets");

    JMenuItem mnuItemExit;
    JMenuItem mnuItemRefresh;
    JMenuItem mnuItemDelete;
    JMenuItem mnuItemOpenTicket;
    JMenuItem mnuItemSelectTicket;
    JMenuItem mnuItemAddTicket;
    JMenuItem mnuItemCloseTicket;
    JMenuItem mnuItemViewTicketStatus;
    JMenuItem mnuItemUpdateDescription;

    public Tickets(boolean isAdmin) {
        chkIfAdmin = isAdmin;
        createMenu();
        prepareGUI();

        if (chkIfAdmin) {
            openTickets();
        }
    }

    private void createMenu() {
        mnuItemExit = new JMenuItem("Exit");
        mnuFile.add(mnuItemExit);

        mnuItemRefresh = new JMenuItem("Refresh");
        mnuFile.add(mnuItemRefresh);

        if (chkIfAdmin) {
            mnuItemDelete = new JMenuItem("Delete Ticket");
            mnuAdmin.add(mnuItemDelete);

            mnuItemAddTicket = new JMenuItem("Add Ticket");
            mnuAdmin.add(mnuItemAddTicket);

            mnuItemCloseTicket = new JMenuItem("Close Ticket");
            mnuAdmin.add(mnuItemCloseTicket);
            mnuItemCloseTicket.addActionListener(this);

            mnuItemViewTicketStatus = new JMenuItem("View Ticket Status");
            mnuAdmin.add(mnuItemViewTicketStatus);
            mnuItemViewTicketStatus.addActionListener(this);

            mnuItemUpdateDescription = new JMenuItem("Update Ticket Description");
            mnuAdmin.add(mnuItemUpdateDescription);
            mnuItemUpdateDescription.addActionListener(this);
        }

        mnuItemOpenTicket = new JMenuItem("Open Ticket");
        mnuTickets.add(mnuItemOpenTicket);

        mnuItemSelectTicket = new JMenuItem("Select Ticket");
        mnuTickets.add(mnuItemSelectTicket);

        mnuItemExit.addActionListener(this);
        mnuItemRefresh.addActionListener(this);
        if (chkIfAdmin) {
            mnuItemDelete.addActionListener(this);
            mnuItemAddTicket.addActionListener(this);
        }
        mnuItemOpenTicket.addActionListener(this);
        mnuItemSelectTicket.addActionListener(this);
    }

    private void updateTicketDescription() {
        String ticketIdInput = JOptionPane.showInputDialog(null, "Enter the ticket ID to update the description");

        if (ticketIdInput == null || ticketIdInput.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ticket ID is empty.");
            System.out.println("Ticket ID is empty.");
            return;
        }

        int ticketId;
        try {
            ticketId = Integer.parseInt(ticketIdInput);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid ticket ID format.");
            System.out.println("Invalid ticket ID format.");
            return;
        }

        boolean ticketExists = dao.checkTicketExists(ticketId);

        if (!ticketExists) {
            System.out.println("Ticket ID: " + ticketId + " does not exist.");
            JOptionPane.showMessageDialog(null, "Ticket ID: " + ticketId + " does not exist.");
            return;
        }

        String newDescription = JOptionPane.showInputDialog(null, "Enter the new ticket description");

        if (newDescription == null || newDescription.isEmpty()) {
            JOptionPane.showMessageDialog(null, "New ticket description is empty.");
            System.out.println("New ticket description is empty.");
            return;
        }

        boolean descriptionUpdated = dao.updateTicketDescription(ticketId, newDescription);

        if (descriptionUpdated) {
            System.out.println("Ticket ID : " + ticketId + " description updated successfully.");
            JOptionPane.showMessageDialog(null, "Ticket ID: " + ticketId + " description updated successfully");
            refreshTicketView();
        } else {
            System.out.println("Failed to update ticket description.");
            JOptionPane.showMessageDialog(null, "Failed to update ticket description.");
        }
    }


    private void prepareGUI() {
        JMenuBar bar = new JMenuBar();
        bar.add(mnuFile);
        if (chkIfAdmin) {
            bar.add(mnuAdmin);
        }
        bar.add(mnuTickets);
        setJMenuBar(bar);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent wE) {
                System.exit(0);
            }
        });

        setSize(400, 400);
        getContentPane().setBackground(Color.GREEN);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mnuItemExit) {
            System.exit(0);
        } else if (e.getSource() == mnuItemRefresh) {
            refreshTicketView();
        } else if (e.getSource() == mnuItemSelectTicket) {
            selectTicket();
        } else if (e.getSource() == mnuItemAddTicket) {
            addTicket();
        } else if (e.getSource() == mnuItemDelete) {
            deleteTicket();
        } else if (e.getSource() == mnuItemOpenTicket) {
            openTickets();
        } else if (e.getSource() == mnuItemCloseTicket && chkIfAdmin) {
            closeTicketStatus();
        } else if (e.getSource() == mnuItemViewTicketStatus && chkIfAdmin) {
            openTicketStatus();
        } else if (e.getSource() == mnuItemUpdateDescription && chkIfAdmin) {
            updateTicketDescription();
        }
    }

    private void refreshTicketView() {
        try {
            JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
            jt.setBounds(30, 40, 200, 400);
            jt.setBackground(Color.green);
            jt.setForeground(Color.black);
            jt.getTableHeader().setBackground(Color.BLACK);
            jt.getTableHeader().setForeground(Color.white);
            JScrollPane sp = new JScrollPane(jt);
            getContentPane().removeAll();
            add(sp);
            revalidate();
            repaint();
        } catch (SQLException e1) {
            System.out.println("Ticket view refresh failed.");
            e1.printStackTrace();
        }
    }

    private void selectTicket() {
        JOptionPane.showMessageDialog(null, "Ticket selected!");
        // You can implement the functionality for selecting a ticket here
    }

    private void addTicket() {
        String ticketName = JOptionPane.showInputDialog(null, "Enter your name");
        String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");

        if (ticketName == null || (ticketName != null && ("".equals(ticketName))) ||
                ticketDesc == null || (ticketDesc != null && ("".equals(ticketDesc)))) {
            JOptionPane.showMessageDialog(null, "Ticket creation failed: empty name / description.");
            System.out.println("Ticket creation failed: empty name / description.");
        } else {
            int id = dao.insertRecords(ticketName, ticketDesc);

            if (id != 0) {
                System.out.println("Ticket ID : " + id + " created successfully.");
                JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created");

                try {
                    JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
                    jt.setBounds(30, 40, 200, 400);
                    jt.setBackground(Color.green);
                    jt.setForeground(Color.white);
                    jt.getTableHeader().setBackground(Color.BLACK);
                    jt.getTableHeader().setForeground(Color.white);
                    JScrollPane sp = new JScrollPane(jt);
                    getContentPane().removeAll();
                    add(sp);
                    revalidate();
                    repaint();
                } catch (SQLException e1) {
                    System.out.println("Ticket view refresh failed.");
                    e1.printStackTrace();
                }
            } else {
                System.out.println("Ticket creation failed.");
            }
        }
    }

    private void deleteTicket() {
        String ticketId = JOptionPane.showInputDialog(null, "Enter the ticket id to delete the ticket");

        if (ticketId == null || (ticketId != null && ("".equals(ticketId)))) {
            JOptionPane.showMessageDialog(null, "Ticket deletion failed: empty tid.");
            System.out.println("Ticket deletion failed: empty tid.");
        } else {
            int tid = Integer.parseInt(ticketId);
            int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete ticket " + tid + "?", "Warning!", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                int id = dao.deleteRecords(tid);
                if (id != 0) {
                    System.out.println("Ticket ID : " + id + " deleted successfully.");
                    JOptionPane.showMessageDialog(null, "Ticket id: " + id + " deleted");

                    try {
                        JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
                        jt.setBounds(30, 40, 200, 400);
                        jt.setBackground(Color.green);
                        jt.setForeground(Color.black);
                        jt.getTableHeader().setBackground(Color.BLACK);
                        jt.getTableHeader().setForeground(Color.white);
                        JScrollPane sp = new JScrollPane(jt);
                        getContentPane().removeAll();
                        add(sp);
                        revalidate();
                        repaint();
                    } catch (SQLException e1) {
                        System.out.println("Ticket view refresh failed.");
                        e1.printStackTrace();
                    }
                } else {
                    System.out.println("Ticket cannot be deleted!!!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ticket " + tid + " was not deleted.");
            }
        }
    }

    private void closeTicketStatus() {
        String ticketIdInput = JOptionPane.showInputDialog(null, "Enter the ticket ID to close the ticket");

        if (ticketIdInput == null || (ticketIdInput != null && ("".equals(ticketIdInput)))) {
            JOptionPane.showMessageDialog(null, "Ticket ID is empty.");
            System.out.println("Ticket ID is empty.");
        } else {
            int ticketId = Integer.parseInt(ticketIdInput);
            boolean ticketExists = dao.checkTicketExists(ticketId);

            if (ticketExists) {
                boolean statusUpdated = dao.updateTicketStatus(ticketId, "CLOSE");

                if (statusUpdated) {
                    System.out.println("Ticket ID : " + ticketId + " status updated successfully to 'CLOSE'.");
                    JOptionPane.showMessageDialog(null, "Ticket ID: " + ticketId + " status updated to 'CLOSE'");
                    refreshTicketView();
                } else {
                    System.out.println("Failed to update ticket status.");
                    JOptionPane.showMessageDialog(null, "Failed to update ticket status.");
                }
            } else {
                System.out.println("Ticket ID: " + ticketId + " does not exist.");
                JOptionPane.showMessageDialog(null, "Ticket ID: " + ticketId + " does not exist.");
            }
        }
    }

    private void openTicketStatus() {
        String ticketIdInput = JOptionPane.showInputDialog(null, "Enter the ticket ID to open the ticket");

        if (ticketIdInput == null || (ticketIdInput != null && ("".equals(ticketIdInput)))) {
            JOptionPane.showMessageDialog(null, "Ticket ID is empty.");
            System.out.println("Ticket ID is empty.");
        } else {
            int ticketId = Integer.parseInt(ticketIdInput);
            boolean ticketExists = dao.checkTicketExists(ticketId);

            if (ticketExists) {
                boolean statusUpdated = dao.updateTicketStatus(ticketId, "OPEN");

                if (statusUpdated) {
                    System.out.println("Ticket ID : " + ticketId + " status updated successfully to 'OPEN'.");
                    JOptionPane.showMessageDialog(null, "Ticket ID: " + ticketId + " status updated to 'OPEN'");
                    refreshTicketView();
                } else {
                    System.out.println("Failed to update ticket status.");
                    JOptionPane.showMessageDialog(null, "Failed to update ticket status.");
                }
            } else {
                System.out.println("Ticket ID: " + ticketId + " does not exist.");
                JOptionPane.showMessageDialog(null, "Ticket ID: " + ticketId + " does not exist.");
            }
        }
    }

    private void openTickets() {
        try {
            JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
            jt.setBounds(30, 40, 200, 400);
            jt.setBackground(Color.green);
            jt.setForeground(Color.black);
            jt.getTableHeader().setBackground(Color.BLACK);
            jt.getTableHeader().setForeground(Color.white);
            JScrollPane sp = new JScrollPane(jt);
            getContentPane().removeAll();
            add(sp);
            revalidate();
            repaint();
        } catch (SQLException e1) {
            System.out.println("Ticket view failed for admin.");
            e1.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Entry point - You may add relevant code if needed
    }
}
