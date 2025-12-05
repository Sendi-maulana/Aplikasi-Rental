import database.DatabaseConfig;
import java.sql.*;

public class CheckUsers {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            System.out.println("Koneksi ke database berhasil!");
            
            // Check all users in the database
            String query = "SELECT id, username, email, role, isActive FROM users";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                
                System.out.println("\nDaftar pengguna di database:");
                System.out.println("ID\tUsername\t\tEmail\t\t\tRole\t\tStatus");
                System.out.println("--\t--------\t\t-----\t\t\t----\t\t------");
                
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String username = rs.getString("username");
                    String email = rs.getString("email");
                    String role = rs.getString("role");
                    boolean isActive = rs.getBoolean("isActive");
                    
                    System.out.printf("%d\t%-15s\t%-20s\t%-10s\t%s%n", 
                        id, username, email, role, isActive ? "Aktif" : "Tidak Aktif");
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error saat mengecek pengguna:");
            e.printStackTrace();
        }
    }
}