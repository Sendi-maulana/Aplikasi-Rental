import database.DatabaseConfig;
import java.sql.*;

public class CheckRentalData {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            System.out.println("Koneksi ke database berhasil!");
            
            // Check all rentals in the database
            String query = "SELECT * FROM rentals ORDER BY rental_date DESC LIMIT 20";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                
                System.out.println("\nDaftar 20 peminjaman terbaru di database:");
                System.out.println("ID\tUser ID\t\tEquipment ID\tTanggal Pinjam\t\tTanggal Kembali\t\tTotal\t\tStatus\t\tPayment Confirmed");
                System.out.println("--\t-------\t\t----------\t-------------\t\t---------------\t\t-----\t\t------\t\t---------------");
                
                int count = 0;
                while (rs.next() && count < 20) {
                    int id = rs.getInt("id");
                    int userId = rs.getInt("user_id");
                    int equipmentId = rs.getInt("equipment_id");
                    Date rentalDate = rs.getDate("rental_date");
                    Date returnDate = rs.getDate("return_date");
                    double totalPrice = rs.getDouble("total_price");
                    String status = rs.getString("status");
                    boolean paymentConfirmed = rs.getBoolean("paymentConfirmed");
                    
                    System.out.printf("%d\t%d\t\t%d\t\t%s\t%s\t\tRp %.0f\t%s\t\t%s%n", 
                        id, userId, equipmentId, rentalDate, returnDate, totalPrice, status, paymentConfirmed ? "Ya" : "Tidak");
                    count++;
                }
                
                if (count == 0) {
                    System.out.println("Tidak ada data peminjaman ditemukan.");
                } else {
                    System.out.println("\nTotal peminjaman dalam database: " + count);
                }
            }
            
            // Check rental count
            String countQuery = "SELECT COUNT(*) as total FROM rentals";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(countQuery)) {
                if (rs.next()) {
                    int totalRentals = rs.getInt("total");
                    System.out.println("Total seluruh peminjaman dalam database: " + totalRentals);
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error saat mengecek data peminjaman:");
            e.printStackTrace();
        }
    }
}