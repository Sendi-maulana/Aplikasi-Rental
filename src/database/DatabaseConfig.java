package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/rental_alpinis?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta&allowLoadLocalInfile=true&connectTimeout=60000&socketTimeout=60000&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&initialTimeout=2";
    private static final String USERNAME = "root";
    private static final String PASSWORD = ""; // Default password Laragon biasanya kosong

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver tidak ditemukan", e);
        }
    }

    public static String getURL() {
        return URL;
    }
}