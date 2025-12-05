import view.LoginView;

public class MainApp {
    public static void main(String[] args) {
        // Menjalankan aplikasi dengan menampilkan halaman login
        java.awt.EventQueue.invokeLater(() -> {
            try {
                // Menggunakan look and feel sistem
                javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginView().setVisible(true);
        });
    }
}