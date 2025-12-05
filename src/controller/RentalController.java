package controller;

import database.DatabaseConfig;
import model.Rental;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalController {
    
    public boolean createRental(int userId, int equipmentId, LocalDate rentalDate, LocalDate returnDate, double totalPrice) {
        String query = "INSERT INTO rentals (user_id, equipment_id, rental_date, return_date, total_price, status, paymentConfirmed) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, equipmentId);
            stmt.setDate(3, Date.valueOf(rentalDate));
            stmt.setDate(4, Date.valueOf(returnDate));
            stmt.setDouble(5, totalPrice);
            stmt.setString(6, "pending");
            stmt.setBoolean(7, false);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Rental> getRentalsByUserId(int userId) {
        List<Rental> rentals = new ArrayList<>();
        String query = "SELECT * FROM rentals WHERE user_id = ? ORDER BY rental_date DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

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

    public List<Rental> getAllRentals() {
        List<Rental> rentals = new ArrayList<>();
        String query = "SELECT r.*, u.username, e.name as equipment_name FROM rentals r " +
                      "JOIN users u ON r.user_id = u.id " +
                      "JOIN equipment e ON r.equipment_id = e.id " +
                      "ORDER BY r.rental_date DESC";

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
    
    public boolean updateRentalStatus(int rentalId, String status) {
        String query = "UPDATE rentals SET status = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, rentalId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean confirmPayment(int rentalId) {
        String query = "UPDATE rentals SET paymentConfirmed = true WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, rentalId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Rental> getPendingRentals() {
        List<Rental> rentals = new ArrayList<>();
        String query = "SELECT r.*, u.username, e.name as equipment_name FROM rentals r " +
                      "JOIN users u ON r.user_id = u.id " +
                      "JOIN equipment e ON r.equipment_id = e.id " +
                      "WHERE r.status = 'pending' " +
                      "ORDER BY r.rental_date DESC";

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
}