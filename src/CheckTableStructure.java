import database.DatabaseConfig;
import java.sql.*;

public class CheckTableStructure {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            System.out.println("Koneksi ke database berhasil!");
            
            // Check structure of users table
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, "users", null);
            
            System.out.println("\nStruktur tabel users:");
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String typeName = columns.getString("TYPE_NAME");
                int size = columns.getInt("COLUMN_SIZE");
                System.out.println("- " + columnName + " (" + typeName + ", size: " + size + ")");
            }
            
            // Also check equipment and rentals tables
            columns = metaData.getColumns(null, null, "equipment", null);
            System.out.println("\nStruktur tabel equipment:");
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String typeName = columns.getString("TYPE_NAME");
                int size = columns.getInt("COLUMN_SIZE");
                System.out.println("- " + columnName + " (" + typeName + ", size: " + size + ")");
            }
            
            columns = metaData.getColumns(null, null, "rentals", null);
            System.out.println("\nStruktur tabel rentals:");
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String typeName = columns.getString("TYPE_NAME");
                int size = columns.getInt("COLUMN_SIZE");
                System.out.println("- " + columnName + " (" + typeName + ", size: " + size + ")");
            }
            
        } catch (Exception e) {
            System.out.println("Error saat mengecek struktur tabel:");
            e.printStackTrace();
        }
    }
}