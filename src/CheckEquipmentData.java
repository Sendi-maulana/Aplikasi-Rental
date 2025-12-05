import database.DatabaseConfig;
import java.sql.*;

public class CheckEquipmentData {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            System.out.println("Koneksi ke database berhasil!");
            
            // Check all equipment in the database
            String query = "SELECT * FROM equipment ORDER BY name";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                
                System.out.println("\nDaftar peralatan dalam database:");
                System.out.println("ID\tNama\t\t\tDeskripsi\t\t\t\tHarga\t\tJumlah\t\tTersedia");
                System.out.println("--\t----\t\t\t---------\t\t\t\t-----\t\t------\t\t--------");
                
                int count = 0;
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    double price = rs.getDouble("price_per_day");
                    int quantity = rs.getInt("quantity");
                    boolean isAvailable = rs.getBoolean("isAvailable");
                    
                    System.out.printf("%d\t%-15s\t%-30s\tRp %.0f\t\t%d\t\t%s%n", 
                        id, name, description, price, quantity, isAvailable ? "Ya" : "Tidak");
                    count++;
                }
                
                if (count == 0) {
                    System.out.println("Tidak ada data peralatan ditemukan.");
                } else {
                    System.out.println("\nTotal peralatan dalam database: " + count);
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error saat mengecek data peralatan:");
            e.printStackTrace();
        }
    }
}