package database;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConfig.getConnection();
            System.out.println("Koneksi ke database berhasil!");
            System.out.println("Database URL: " + DatabaseConfig.getURL());
            conn.close();
        } catch (Exception e) {
            System.out.println("Koneksi ke database gagal!");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}