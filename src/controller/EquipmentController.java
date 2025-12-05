package controller;

import database.DatabaseConfig;
import model.Equipment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipmentController {
    
    public List<Equipment> getAllEquipment() {
        List<Equipment> equipmentList = new ArrayList<>();
        String query = "SELECT * FROM equipment ORDER BY name";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Equipment equipment = new Equipment(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price_per_day"),
                    rs.getInt("quantity"),
                    rs.getString("image"),
                    rs.getBoolean("isAvailable")
                );
                equipmentList.add(equipment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return equipmentList;
    }

    public Equipment getEquipmentById(int equipmentId) {
        String query = "SELECT * FROM equipment WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, equipmentId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Equipment(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price_per_day"),
                    rs.getInt("quantity"),
                    rs.getString("image"),
                    rs.getBoolean("isAvailable")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean addEquipment(String name, String description, double pricePerDay, int quantity, String image) {
        String query = "INSERT INTO equipment (name, description, price_per_day, quantity, image, isAvailable) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setDouble(3, pricePerDay);
            stmt.setInt(4, quantity);
            stmt.setString(5, image);
            stmt.setBoolean(6, true);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateEquipment(int id, String name, String description, double pricePerDay, int quantity, boolean isAvailable) {
        String query = "UPDATE equipment SET name = ?, description = ?, price_per_day = ?, quantity = ?, isAvailable = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setDouble(3, pricePerDay);
            stmt.setInt(4, quantity);
            stmt.setBoolean(5, isAvailable);
            stmt.setInt(6, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteEquipment(int id) {
        String query = "DELETE FROM equipment WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}