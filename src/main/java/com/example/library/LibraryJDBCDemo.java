package com.example.library;

import java.sql.*;

public class LibraryJDBCDemo {
    private static final String URL = "jdbc:mysql://localhost:3306/LibraryDB?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "123456789";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Connected to MySQL database using JDBC!");

            // Thêm sách mới (kiểm tra ISBN trước)
            String isbn = "12345";
            if (!isISBNExists(conn, isbn)) {
                String insertSQL = "INSERT INTO Books (Title, Author, ISBN, IsAvailable) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(insertSQL);
                pstmt.setString(1, "Java Programming");
                pstmt.setString(2, "John Doe");
                pstmt.setString(3, isbn);
                pstmt.setBoolean(4, true);
                pstmt.executeUpdate();
                System.out.println("Added new book using JDBC.");
            } else {
                System.out.println("Book with ISBN " + isbn + " already exists.");
            }

            // Kiểm tra BookID và MemberID trước khi mượn
            int bookID = 1;
            int memberID = 2;
            if (isBookAvailable(conn, bookID) && isMemberExists(conn, memberID)) {
                borrowBookTransaction(conn, bookID, memberID);
            } else {
                System.out.println("Cannot borrow: Book or Member does not exist or book is not available.");
            }

            // Hiển thị tất cả sách
            String selectSQL = "SELECT * FROM Books";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectSQL);
            while (rs.next()) {
                System.out.println("Book: " + rs.getString("Title") + ", Author: " + rs.getString("Author") +
                        ", ISBN: " + rs.getString("ISBN") + ", Available: " + rs.getBoolean("IsAvailable"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean isISBNExists(Connection conn, String isbn) throws SQLException {
        String checkSQL = "SELECT COUNT(*) FROM Books WHERE ISBN = ?";
        PreparedStatement pstmt = conn.prepareStatement(checkSQL);
        pstmt.setString(1, isbn);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    private static boolean isBookAvailable(Connection conn, int bookID) throws SQLException {
        String checkSQL = "SELECT IsAvailable FROM Books WHERE BookID = ?";
        PreparedStatement pstmt = conn.prepareStatement(checkSQL);
        pstmt.setInt(1, bookID);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getBoolean("IsAvailable");
        }
        return false; // Sách không tồn tại
    }

    private static boolean isMemberExists(Connection conn, int memberID) throws SQLException {
        String checkSQL = "SELECT COUNT(*) FROM Members WHERE MemberID = ?";
        PreparedStatement pstmt = conn.prepareStatement(checkSQL);
        pstmt.setInt(1, memberID);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    private static void borrowBookTransaction(Connection conn, int bookID, int memberID) throws SQLException {
        conn.setAutoCommit(false);
        try {
            // Cập nhật trạng thái sách
            String updateSQL = "UPDATE Books SET IsAvailable = FALSE WHERE BookID = ? AND IsAvailable = TRUE";
            PreparedStatement pstmt1 = conn.prepareStatement(updateSQL);
            pstmt1.setInt(1, bookID);
            int rowsAffected = pstmt1.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Book is not available or does not exist.");
            }

            // Thêm bản ghi mượn
            String insertSQL = "INSERT INTO BorrowRecords (BookID, MemberID, BorrowDate) VALUES (?, ?, ?)";
            PreparedStatement pstmt2 = conn.prepareStatement(insertSQL);
            pstmt2.setInt(1, bookID);
            pstmt2.setInt(2, memberID);
            pstmt2.setDate(3, Date.valueOf("2025-05-13"));
            pstmt2.executeUpdate();

            conn.commit(); // Commit giao dịch
            System.out.println("Book borrowed successfully.");
        } catch (SQLException e) {
            conn.rollback(); // Rollback nếu có lỗi
            System.out.println("Transaction failed: " + e.getMessage());
            throw e;
        } finally {
            conn.setAutoCommit(true); // Đặt lại chế độ tự động commit
        }
    }
}