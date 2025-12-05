package view;

import controller.PaymentController;
import controller.RentalController;
import controller.UserController;
import model.Rental;
import utils.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminView extends JFrame {
    private UserController userController;
    private RentalController rentalController;
    private PaymentController paymentController;
    private JPanel userPanel;
    private JLabel welcomeLabel;
    private JButton logoutButton;
    private JButton refreshButton;
    private JButton createOwnerButton;
    private JButton paymentManagementButton;

    public AdminView() {
        userController = new UserController();
        rentalController = new RentalController();
        paymentController = new PaymentController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadUserData();
    }

    private void initializeComponents() {
        setTitle("Dashboard Super Admin - Rental Peralatan Gunung");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));

        welcomeLabel = new JLabel("Selamat Datang, Super Admin " + Session.getCurrentUser().getUsername());
        logoutButton = new JButton("Logout");
        refreshButton = new JButton("Refresh Data");
        createOwnerButton = new JButton("Buat Owner Baru");
        paymentManagementButton = new JButton("Manajemen Pembayaran");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.add(welcomeLabel);
        headerPanel.add(Box.createHorizontalStrut(10));
        headerPanel.add(refreshButton);
        headerPanel.add(createOwnerButton);
        headerPanel.add(paymentManagementButton);
        headerPanel.add(Box.createHorizontalStrut(10));
        headerPanel.add(logoutButton);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(headerPanel, BorderLayout.NORTH);

        // Main content panel
        JScrollPane scrollPane = new JScrollPane(userPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");

        JMenuItem userManagementItem = new JMenuItem("Manajemen Pengguna");
        JMenuItem equipmentManagementItem = new JMenuItem("Manajemen Peralatan");
        JMenuItem rentalManagementItem = new JMenuItem("Manajemen Peminjaman");

        menu.add(userManagementItem);
        menu.add(equipmentManagementItem);
        menu.add(rentalManagementItem);
        menuBar.add(menu);

        setJMenuBar(menuBar);

        // Aksi menu
        userManagementItem.addActionListener(e -> showUserManagementDialog());
        equipmentManagementItem.addActionListener(e -> showEquipmentManagementDialog());
        rentalManagementItem.addActionListener(e -> showRentalManagementDialog());
    }

    private void setupEventHandlers() {
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Session.logout();
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
                dispose();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadUserData();
            }
        });

        createOwnerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCreateOwnerDialog();
            }
        });

        paymentManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPaymentManagementDialog();
            }
        });
    }

    private void loadUserData() {
        // Dalam implementasi nyata, Anda akan memuat data dari database
        // Untuk sekarang, kita hanya menampilkan pesan bahwa ini adalah antarmuka admin
        userPanel.removeAll();

        JLabel infoLabel = new JLabel(
            "<html><h2>Antarmuka Super Admin</h2>" +
            "<p>Anda dapat:</p>" +
            "<ul>" +
            "<li>Mengelola akun pengguna (pelanggan, owner)</li>" +
            "<li>Mengelola peralatan rental</li>" +
            "<li>Mengkonfirmasi pembayaran dari pelanggan</li>" +
            "<li>Melihat laporan dan statistik</li>" +
            "</ul></html>"
        );
        infoLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        userPanel.add(infoLabel);

        userPanel.revalidate();
        userPanel.repaint();
    }

    private void showCreateOwnerDialog() {
        JDialog dialog = new JDialog(this, "Buat Owner Baru", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Labels dan fields
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        JTextField usernameField = new JTextField(20);
        panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        JTextField emailField = new JTextField(20);
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);

        // Tombol
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton confirmButton = new JButton("Buat Owner");
        JButton cancelButton = new JButton("Batal");

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, gbc);

        dialog.add(panel);

        // Event handlers
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword());

                // Validasi input
                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                        "Semua field harus diisi!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (password.length() < 6) {
                    JOptionPane.showMessageDialog(dialog,
                        "Password minimal 6 karakter!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validasi format email sederhana
                if (!email.contains("@") || !email.contains(".")) {
                    JOptionPane.showMessageDialog(dialog,
                        "Format email tidak valid!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Registrasi owner
                boolean success = userController.registerUser(username, email, password, "owner");
                if (success) {
                    JOptionPane.showMessageDialog(dialog,
                        "Owner berhasil dibuat!",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadUserData(); // Refresh tampilan
                } else {
                    JOptionPane.showMessageDialog(dialog,
                        "Gagal membuat owner! Mungkin username sudah digunakan.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }

    private void showUserManagementDialog() {
        // Tampilkan informasi dasar tentang manajemen pengguna
        JOptionPane.showMessageDialog(this,
            "Fitur manajemen pengguna akan memungkinkan Anda:\n" +
            "- Melihat daftar semua pengguna\n" +
            "- Mengaktifkan/nonaktifkan akun\n" +
            "- Mengedit informasi pengguna\n" +
            "- Menghapus akun\n" +
            "- Mengganti peran pengguna",
            "Manajemen Pengguna",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void showEquipmentManagementDialog() {
        // Tampilkan informasi dasar tentang manajemen peralatan
        JOptionPane.showMessageDialog(this,
            "Fitur manajemen peralatan akan memungkinkan Anda:\n" +
            "- Menambah peralatan baru\n" +
            "- Mengedit informasi peralatan\n" +
            "- Menghapus peralatan\n" +
            "- Mengelola stok peralatan",
            "Manajemen Peralatan",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void showRentalManagementDialog() {
        // Tampilkan informasi bahwa fungsi ini sekarang dilakukan oleh owner
        JOptionPane.showMessageDialog(this,
            "Fitur manajemen peminjaman sekarang dilakukan oleh owner.\n" +
            "Tugas super admin hanya untuk manajemen akun dan reset password.",
            "Manajemen Peminjaman",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void showPaymentManagementDialog() {
        // Tampilkan informasi bahwa fungsi ini sekarang dilakukan oleh owner
        JOptionPane.showMessageDialog(this,
            "Fitur konfirmasi pembayaran sekarang dilakukan oleh owner.\n" +
            "Tugas super admin hanya untuk manajemen akun dan reset password.",
            "Manajemen Pembayaran",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createUserCard(model.User user) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createTitledBorder("Pengguna: " + user.getUsername()));
        card.setPreferredSize(new Dimension(700, 100));

        // Panel kiri - informasi pengguna
        JPanel leftPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        leftPanel.add(new JLabel("ID: " + user.getId()), gbc);
        gbc.gridy++;
        leftPanel.add(new JLabel("Email: " + user.getEmail()), gbc);
        gbc.gridy++;
        leftPanel.add(new JLabel("Role: " + user.getRole()), gbc);

        // Panel kanan - tombol reset password
        JPanel rightPanel = new JPanel(new FlowLayout());
        JButton resetPasswordButton = new JButton("Reset Password");
        JButton promoteToOwnerButton = new JButton("Jadikan Owner");

        resetPasswordButton.addActionListener(e -> resetUserPassword(user));
        promoteToOwnerButton.addActionListener(e -> promoteUserToOwner(user));

        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(resetPasswordButton);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(promoteToOwnerButton);

        card.add(leftPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);

        return card;
    }

    private void resetUserPassword(model.User user) {
        String newPassword = JOptionPane.showInputDialog(this,
            "Masukkan password baru untuk " + user.getUsername() + ":",
            "Reset Password",
            JOptionPane.QUESTION_MESSAGE);

        if (newPassword != null && !newPassword.trim().isEmpty()) {
            if (newPassword.length() < 6) {
                JOptionPane.showMessageDialog(this,
                    "Password harus minimal 6 karakter!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Implementasi update password ke database
            // Kita akan gunakan UserController untuk update password nantinya
            JOptionPane.showMessageDialog(this,
                "Password untuk " + user.getUsername() + " berhasil direset!");
        }
    }

    private void promoteUserToOwner(model.User user) {
        int result = JOptionPane.showConfirmDialog(this,
            "Jadikan " + user.getUsername() + " sebagai owner?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            // Di sini nanti akan ada implementasi untuk mengupdate role user ke owner
            JOptionPane.showMessageDialog(this,
                user.getUsername() + " sekarang menjadi owner!");
        }
    }
}