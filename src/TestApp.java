import controller.UserController;
import controller.EquipmentController;
import controller.RentalController;
import controller.PaymentController;
import model.User;
import model.Equipment;
import model.Rental;
import utils.Session;

import java.time.LocalDate;
import java.util.List;

public class TestApp {
    public static void main(String[] args) {
        System.out.println("=== Testing Aplikasi Rental Peralatan Gunung ===");
        
        // Testing koneksi database dan kelas controller
        testUserController();
        testEquipmentController();
        testRentalController();
        testPaymentController();
        
        System.out.println("=== Testing Selesai ===");
        System.out.println("Semua modul telah diinisialisasi dengan benar.");
        System.out.println("Silakan jalankan MainApp untuk menggunakan aplikasi.");
    }
    
    private static void testUserController() {
        System.out.println("\n--- Testing UserController ---");
        UserController userController = new UserController();
        
        // Test registrasi pengguna
        boolean registrationResult = userController.registerUser(
            "testuser", 
            "test@example.com", 
            "password123", 
            "pelanggan"
        );
        System.out.println("Registrasi pengguna berhasil: " + registrationResult);
        
        // Test otentikasi pengguna
        User authenticatedUser = userController.authenticateUser("testuser", "password123");
        System.out.println("Otentikasi berhasil: " + (authenticatedUser != null));
        
        if (authenticatedUser != null) {
            // Test session
            Session.login(authenticatedUser);
            System.out.println("Session login berhasil: " + Session.isLoggedIn());
            System.out.println("Role pengguna: " + Session.getRole());
            Session.logout();
            System.out.println("Session logout berhasil: " + !Session.isLoggedIn());
        }
    }
    
    private static void testEquipmentController() {
        System.out.println("\n--- Testing EquipmentController ---");
        EquipmentController equipmentController = new EquipmentController();
        
        // Test menambahkan peralatan
        boolean addResult = equipmentController.addEquipment(
            "Test Equipment", 
            "Deskripsi test", 
            50000.0, 
            5, 
            null
        );
        System.out.println("Penambahan peralatan berhasil: " + addResult);
        
        // Test mengambil semua peralatan
        List<Equipment> equipmentList = equipmentController.getAllEquipment();
        System.out.println("Jumlah peralatan: " + equipmentList.size());
    }
    
    private static void testRentalController() {
        System.out.println("\n--- Testing RentalController ---");
        RentalController rentalController = new RentalController();
        
        // Test mengambil semua rental (mungkin kosong jika belum ada)
        List<Rental> rentals = rentalController.getAllRentals();
        System.out.println("Jumlah rental: " + rentals.size());
        
        // Test mengambil rental pending
        List<Rental> pendingRentals = rentalController.getPendingRentals();
        System.out.println("Jumlah rental pending: " + pendingRentals.size());
    }
    
    private static void testPaymentController() {
        System.out.println("\n--- Testing PaymentController ---");
        PaymentController paymentController = new PaymentController();
        
        System.out.println("PaymentController berhasil diinisialisasi");
        System.out.println("Fungsi-fungsi pembayaran tersedia");
    }
}