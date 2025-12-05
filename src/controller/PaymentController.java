package controller;

import database.DatabaseConfig;
import model.Rental;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentController {
    
    public boolean createPayment(int rentalId, double amount, String paymentMethod) {
        String query = "INSERT INTO payments (rental_id, amount, payment_method, payment_status) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, rentalId);
            stmt.setDouble(2, amount);
            stmt.setString(3, paymentMethod);
            stmt.setString(4, "pending");
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean confirmPayment(int rentalId) {
        // Update status pembayaran di tabel payments
        String paymentQuery = "UPDATE payments SET payment_status = 'confirmed' WHERE rental_id = ?";
        // Update status pembayaran dan status rental di tabel rentals
        String rentalQuery = "UPDATE rentals SET paymentConfirmed = true, status = 'confirmed' WHERE id = ?";

        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false); // Mulai transaksi

            // Update payments
            try (PreparedStatement paymentStmt = conn.prepareStatement(paymentQuery)) {
                paymentStmt.setInt(1, rentalId);
                paymentStmt.executeUpdate();
            }

            // Update rentals
            try (PreparedStatement rentalStmt = conn.prepareStatement(rentalQuery)) {
                rentalStmt.setInt(1, rentalId);
                rentalStmt.executeUpdate();
            }

            conn.commit(); // Commit transaksi
            conn.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback jika ada error
                    conn.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public boolean cancelPayment(int rentalId) {
        // Update status pembayaran di tabel payments
        String paymentQuery = "UPDATE payments SET payment_status = 'failed' WHERE rental_id = ?";
        // Update status rental di tabel rentals
        String rentalQuery = "UPDATE rentals SET paymentConfirmed = false, status = 'pending' WHERE id = ?";

        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false); // Mulai transaksi

            // Update payments
            try (PreparedStatement paymentStmt = conn.prepareStatement(paymentQuery)) {
                paymentStmt.setInt(1, rentalId);
                paymentStmt.executeUpdate();
            }

            // Update rentals
            try (PreparedStatement rentalStmt = conn.prepareStatement(rentalQuery)) {
                rentalStmt.setInt(1, rentalId);
                rentalStmt.executeUpdate();
            }

            conn.commit(); // Commit transaksi
            conn.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback jika ada error
                    conn.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public List<Rental> getPendingPayments() {
        List<Rental> rentals = new ArrayList<>();
        String query = "SELECT r.*, u.username, e.name as equipment_name FROM rentals r " +
                      "JOIN users u ON r.user_id = u.id " +
                      "JOIN equipment e ON r.equipment_id = e.id " +
                      "JOIN payments p ON r.id = p.rental_id " +
                      "WHERE p.payment_status = 'pending' OR r.paymentConfirmed = false " +
                      "ORDER BY r.created_at DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Rental rental = new Rental(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("equipment_id"),
                    rs.getDate("rental_date").toLocalDate(),
                    rs.getDate("return_date").toLocalDate(),
                    rs.getDouble("total_price"),
                    rs.getString("status"),
                    rs.getBoolean("paymentConfirmed")
                );
                rentals.add(rental);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rentals;
    }
    
    public boolean refundPayment(int rentalId) {
        // Update status pembayaran di tabel payments
        String paymentQuery = "UPDATE payments SET payment_status = 'failed' WHERE rental_id = ?";
        // Update status rental di tabel rentals
        String rentalQuery = "UPDATE rentals SET paymentConfirmed = false, status = 'returned' WHERE id = ?";

        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false); // Mulai transaksi

            // Update payments
            try (PreparedStatement paymentStmt = conn.prepareStatement(paymentQuery)) {
                paymentStmt.setInt(1, rentalId);
                paymentStmt.executeUpdate();
            }

            // Update rentals
            try (PreparedStatement rentalStmt = conn.prepareStatement(rentalQuery)) {
                rentalStmt.setInt(1, rentalId);
                rentalStmt.executeUpdate();
            }

            conn.commit(); // Commit transaksi
            conn.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback jika ada error
                    conn.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}