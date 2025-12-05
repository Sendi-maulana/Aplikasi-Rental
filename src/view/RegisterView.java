package view;

import controller.UserController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterView extends JFrame {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private UserController userController;
    private JButton registerButton;
    private JButton backButton;

    public RegisterView() {
        userController = new UserController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    private void initializeComponents() {
        setTitle("Registrasi - Rental Peralatan Gunung");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        usernameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Panel utama
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Label dan field
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Konfirmasi Password:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(confirmPasswordField, gbc);

        // Tombol daftar
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        registerButton = new JButton("Daftar");
        registerButton.setPreferredSize(new Dimension(100, 30));
        mainPanel.add(registerButton, gbc);

        // Tombol kembali
        gbc.gridx = 0; gbc.gridy = 5;
        backButton = new JButton("Kembali");
        backButton.setPreferredSize(new Dimension(100, 30));
        mainPanel.add(backButton, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Panel bawah
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel("Registrasi Akun Pelanggan"));
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        // Aksi register
        ActionListener registerListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRegister();
            }
        };

        // Menangani event handler secara langsung melalui field/button yang kita buat
        registerButton.addActionListener(registerListener);

        // Aksi back
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLoginView();
            }
        });

        // Aksi enter di confirm password field
        confirmPasswordField.addActionListener(registerListener);
    }

    private void performRegister() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Validasi input
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Semua field harus diisi!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Password dan konfirmasi password tidak cocok!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, 
                "Password minimal 6 karakter!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validasi format email sederhana
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, 
                "Format email tidak valid!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Registrasi pengguna
        boolean success = userController.registerUser(username, email, password, "pelanggan");
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "Registrasi berhasil! Silakan login.", 
                "Sukses", 
                JOptionPane.INFORMATION_MESSAGE);
            openLoginView();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Gagal melakukan registrasi! Mungkin username sudah digunakan.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openLoginView() {
        LoginView loginView = new LoginView();
        loginView.setVisible(true);
        this.dispose();
    }
}