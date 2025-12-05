import database.DatabaseConfig;
import java.sql.Connection;
import java.sql.Statement;

public class UpdateDatabaseStructure {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            // Update tabel users - ganti nama kolom is_active menjadi isActive
            stmt.executeUpdate("ALTER TABLE users CHANGE COLUMN is_active isActive BOOLEAN DEFAULT TRUE");
            System.out.println("Berhasil memperbarui tabel users");

            // Update tabel equipment - ganti nama kolom is_available menjadi isAvailable
            stmt.executeUpdate("ALTER TABLE equipment CHANGE COLUMN is_available isAvailable BOOLEAN DEFAULT TRUE");
            System.out.println("Berhasil memperbarui tabel equipment");

            // Update tabel rentals - ganti nama kolom payment_confirmed menjadi paymentConfirmed
            stmt.executeUpdate("ALTER TABLE rentals CHANGE COLUMN payment_confirmed paymentConfirmed BOOLEAN DEFAULT FALSE");
            System.out.println("Berhasil memperbarui tabel rentals");

            System.out.println("Semua struktur database telah diperbarui!");

        } catch (Exception e) {
            System.out.println("Error saat memperbarui struktur database:");
            e.printStackTrace();
        }
    }
}