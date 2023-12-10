/*
 * Author: Sufyan Khan and Mbargou Gueye
 * Date: 12/05/2023
 * File: TicketComment.java
*/
package javaapplication1;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TicketComment {
    Dao dao;

    // Constructor initializing Dao object
    public TicketComment() {
        dao = new Dao(); // Initialize Dao object
    }

    // Method to insert a comment for a specific ticket
    public void insertComment(int ticketId, String commentText) {
        try {
            String sql = "INSERT INTO SkhanMgueye_comments(ticket_id, comment_text) VALUES (?, ?)";
            PreparedStatement pstmt = dao.getConnection().prepareStatement(sql);
            pstmt.setInt(1, ticketId);
            pstmt.setString(2, commentText);
            pstmt.executeUpdate();
            System.out.println("Comment added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve comments for a specific ticket
    public ResultSet getCommentsForTicket(int ticketId) {
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM SkhanMgueye_comments WHERE ticket_id = ?";
            PreparedStatement pstmt = dao.getConnection().prepareStatement(query);
            pstmt.setInt(1, ticketId);
            resultSet = pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    // Method to update a comment based on its ID
    public void updateComment(int commentId, String newCommentText) {
        try {
            String sql = "UPDATE SkhanMgueye_comments SET comment_text = ? WHERE comment_id = ?";
            PreparedStatement pstmt = dao.getConnection().prepareStatement(sql);
            pstmt.setString(1, newCommentText);
            pstmt.setInt(2, commentId);
            pstmt.executeUpdate();
            System.out.println("Comment updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a comment based on its ID
    public void deleteComment(int commentId) {
        try {
            String sql = "DELETE FROM SkhanMgueye_comments WHERE comment_id = ?";
            PreparedStatement pstmt = dao.getConnection().prepareStatement(sql);
            pstmt.setInt(1, commentId);
            pstmt.executeUpdate();
            System.out.println("Comment deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve all comments
    public ResultSet getAllComments() {
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM SkhanMgueye_comments";
            PreparedStatement pstmt = dao.getConnection().prepareStatement(query);
            resultSet = pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    // Method to count the total number of comments for a specific ticket
    public int countCommentsForTicket(int ticketId) {
        int count = 0;
        try {
            String query = "SELECT COUNT(*) AS total FROM SkhanMgueye_comments WHERE ticket_id = ?";
            PreparedStatement pstmt = dao.getConnection().prepareStatement(query);
            pstmt.setInt(1, ticketId);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}
