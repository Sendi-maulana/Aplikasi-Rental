package view;

import controller.UserController;
import model.User;
import utils.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserController userController;
    private JButton loginButton;
    private JButton registerButton;

    public LoginView() {
        userController = new UserController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    private void initializeComponents() {
        setTitle("Login - Rental Peralatan Gunung");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
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
        mainPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        // Tombol login
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 30));
        mainPanel.add(loginButton, gbc);

        // Tombol daftar
        gbc.gridx = 0; gbc.gridy = 3;
        registerButton = new JButton("Daftar");
        registerButton.setPreferredSize(new Dimension(100, 30));
        mainPanel.add(registerButton, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Panel bawah
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel("Rental Peralatan Gunung"));
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        // Aksi login
        ActionListener loginListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        };

        // Menangani event handler secara langsung melalui field/button yang kita buat
        loginButton.addActionListener(loginListener);

        // Aksi register
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegisterView();
            }
        });

        // Aksi enter di password field
        passwordField.addActionListener(loginListener);
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Username dan password tidak boleh kosong!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = userController.authenticateUser(username, password);
        if (user != null) {
            Session.login(user);
            JOptionPane.showMessageDialog(this, 
                "Login berhasil sebagai " + user.getRole() + "!", 
                "Sukses", 
                JOptionPane.INFORMATION_MESSAGE);
            
            openDashboard(user.getRole());
        } else {
            JOptionPane.showMessageDialog(this, 
                "Username atau password salah!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRegisterView() {
        RegisterView registerView = new RegisterView();
        registerView.setVisible(true);
        this.dispose();
    }

    private void openDashboard(String role) {
        switch (role) {
            case "pelanggan":
                CustomerDashboardView customerView = new CustomerDashboardView();
                customerView.setVisible(true);
                break;
            case "owner":
                OwnerView ownerView = new OwnerView();
                ownerView.setVisible(true);
                break;
            case "super_admin":
                AdminView adminView = new AdminView();
                adminView.setVisible(true);
                break;
        }
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginView().setVisible(true);
        });
    }
}