package view;

import controller.EquipmentController;
import controller.RentalController;
import model.Equipment;
import model.Rental;
import utils.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OwnerView extends JFrame {
    private EquipmentController equipmentController;
    private RentalController rentalController;
    private JPanel rentalPanel;
    private JLabel welcomeLabel;
    private JButton logoutButton;
    private JButton refreshButton;
    private JButton addEquipmentButton;

    public OwnerView() {
        equipmentController = new EquipmentController();
        rentalController = new RentalController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadRentalData();
    }

    private void initializeComponents() {
        setTitle("Dashboard Owner - Rental Peralatan Gunung");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        rentalPanel = new JPanel();
        rentalPanel.setLayout(new BoxLayout(rentalPanel, BoxLayout.Y_AXIS));

        welcomeLabel = new JLabel("Selamat Datang, Owner " + Session.getCurrentUser().getUsername());
        logoutButton = new JButton("Logout");
        refreshButton = new JButton("Refresh Data");
        addEquipmentButton = new JButton("Tambah Peralatan");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.add(welcomeLabel);
        headerPanel.add(Box.createHorizontalStrut(10));
        headerPanel.add(refreshButton);
        headerPanel.add(addEquipmentButton);
        headerPanel.add(Box.createHorizontalStrut(10));
        headerPanel.add(logoutButton);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(headerPanel, BorderLayout.NORTH);

        // Main content panel
        JScrollPane scrollPane = new JScrollPane(rentalPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");

        JMenuItem equipmentManagementItem = new JMenuItem("Manajemen Peralatan");
        JMenuItem rentalManagementItem = new JMenuItem("Manajemen Peminjaman");

        menu.add(equipmentManagementItem);
        menu.add(rentalManagementItem);
        menuBar.add(menu);

        setJMenuBar(menuBar);

        // Aksi menu
        equipmentManagementItem.addActionListener(e -> showEquipmentManagementDialog());
        rentalManagementItem.addActionListener(e -> loadRentalData());
    }

    private void setupEventHandlers() {
        logoutButton.addActionListener(e -> {
            Session.logout();
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
            dispose();
        });

        refreshButton.addActionListener(e -> loadRentalData());

        addEquipmentButton.addActionListener(e -> showAddEquipmentDialog());
    }

    private void loadRentalData() {
        List<Rental> rentalList = rentalController.getAllRentals();
        rentalPanel.removeAll();

        for (Rental rental : rentalList) {
            JPanel rentalCard = createRentalCard(rental);
            rentalPanel.add(rentalCard);
            rentalPanel.add(Box.createVerticalStrut(10)); // Jarak antar card
        }

        rentalPanel.revalidate();
        rentalPanel.repaint();
    }

    private JPanel createRentalCard(Rental rental) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createTitledBorder("ID Peminjaman: " + rental.getId()));
        card.setPreferredSize(new Dimension(800, 150));

        // Panel kiri - informasi peminjaman
        JPanel leftPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        leftPanel.add(new JLabel("Tanggal Peminjaman: " + rental.getRentalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))), gbc);
        gbc.gridy++;
        leftPanel.add(new JLabel("Tanggal Pengembalian: " + rental.getReturnDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))), gbc);
        gbc.gridy++;
        leftPanel.add(new JLabel("Status: " + rental.getStatus()), gbc);
        gbc.gridy++;
        leftPanel.add(new JLabel("Pembayaran: " + (rental.isPaymentConfirmed() ? "Dikonfirmasi" : "Belum dikonfirmasi")), gbc);

        // Panel kanan - tombol konfirmasi pembayaran dan informasi pengguna
        JPanel rightPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0; gbc.gridy = 0;
        rightPanel.add(new JLabel("User ID: " + rental.getUserId()), gbc);
        gbc.gridy++;
        rightPanel.add(new JLabel("Equipment ID: " + rental.getEquipmentId()), gbc);
        gbc.gridy++;
        rightPanel.add(new JLabel("Total: Rp " + String.format("%.0f", rental.getTotalPrice())), gbc);
        gbc.gridy++;
        
        // Tombol konfirmasi pembayaran
        JButton confirmPaymentButton = new JButton("Konfirmasi Pembayaran");
        confirmPaymentButton.setEnabled(!rental.isPaymentConfirmed() && "pending".equals(rental.getStatus()));
        confirmPaymentButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, 
                "Konfirmasi pembayaran untuk peminjaman ID " + rental.getId() + "?", 
                "Konfirmasi Pembayaran", 
                JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                if (rentalController.confirmPayment(rental.getId())) {
                    JOptionPane.showMessageDialog(this, "Pembayaran berhasil dikonfirmasi!");
                    loadRentalData(); // Refresh data
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal mengkonfirmasi pembayaran!");
                }
            }
        });
        
        rightPanel.add(confirmPaymentButton, gbc);

        card.add(leftPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);

        return card;
    }

    private void showAddEquipmentDialog() {
        JDialog dialog = new JDialog(this, "Tambah Peralatan Baru", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Labels dan fields
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nama Peralatan:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Deskripsi:"), gbc);
        gbc.gridx = 1;
        JTextField descriptionField = new JTextField(20);
        panel.add(descriptionField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Harga per Hari:"), gbc);
        gbc.gridx = 1;
        JTextField priceField = new JTextField(20);
        panel.add(priceField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Jumlah Tersedia:"), gbc);
        gbc.gridx = 1;
        JTextField quantityField = new JTextField(20);
        panel.add(quantityField, gbc);

        // Tombol
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton confirmButton = new JButton("Tambahkan");
        JButton cancelButton = new JButton("Batal");

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, gbc);

        dialog.add(panel);

        // Event handlers
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String description = descriptionField.getText().trim();
                String priceText = priceField.getText().trim();
                String quantityText = quantityField.getText().trim();

                // Validasi input
                if (name.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                        "Nama, harga, dan jumlah harus diisi!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double price = Double.parseDouble(priceText);
                    int quantity = Integer.parseInt(quantityText);

                    if (price <= 0 || quantity <= 0) {
                        JOptionPane.showMessageDialog(dialog,
                            "Harga dan jumlah harus lebih besar dari 0!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Tambahkan peralatan
                    boolean success = equipmentController.addEquipment(name, description, price, quantity, null); // image null untuk sekarang
                    if (success) {
                        JOptionPane.showMessageDialog(dialog,
                            "Peralatan berhasil ditambahkan!",
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        loadRentalData(); // Refresh tampilan
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                            "Gagal menambahkan peralatan!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Harga dan jumlah harus berupa angka!",
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

    private void showEquipmentManagementDialog() {
        JDialog dialog = new JDialog(this, "Manajemen Peralatan", true);
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Panel untuk daftar peralatan
        JPanel equipmentPanel = new JPanel();
        equipmentPanel.setLayout(new BoxLayout(equipmentPanel, BoxLayout.Y_AXIS));

        // Ambil daftar peralatan
        List<Equipment> equipmentList = equipmentController.getAllEquipment();

        if (equipmentList.isEmpty()) {
            JLabel noEquipmentLabel = new JLabel("Belum ada peralatan yang tersedia.");
            noEquipmentLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            equipmentPanel.add(noEquipmentLabel);
        } else {
            for (Equipment equipment : equipmentList) {
                JPanel equipmentCard = createEquipmentCard(equipment);
                equipmentPanel.add(equipmentCard);
                equipmentPanel.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scrollPane = new JScrollPane(equipmentPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Tombol refresh
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            // Refresh dialog dengan data terbaru
            dialog.dispose();
            showEquipmentManagementDialog();
        });
        buttonPanel.add(refreshButton);

        JButton addButton = new JButton("Tambah Peralatan");
        addButton.addActionListener(e -> {
            // Tambah peralatan baru
            dialog.dispose();
            showAddEquipmentDialog();
        });
        buttonPanel.add(addButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private JPanel createEquipmentCard(Equipment equipment) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createTitledBorder(equipment.getName()));
        card.setPreferredSize(new Dimension(700, 100));

        // Panel kiri - informasi peralatan
        JPanel leftPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        leftPanel.add(new JLabel("Deskripsi: " + equipment.getDescription()), gbc);
        gbc.gridy++;
        leftPanel.add(new JLabel("Harga per hari: Rp " + String.format("%.0f", equipment.getPricePerDay())), gbc);
        gbc.gridy++;
        leftPanel.add(new JLabel("Jumlah tersedia: " + equipment.getQuantity()), gbc);
        gbc.gridy++;
        leftPanel.add(new JLabel("Status: " + (equipment.isAvailable() ? "Tersedia" : "Tidak Tersedia")), gbc);

        // Panel kanan - tombol edit/hapus
        JPanel rightPanel = new JPanel(new FlowLayout());
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Hapus");

        editButton.addActionListener(e -> showEditEquipmentDialog(equipment));
        deleteButton.addActionListener(e -> deleteEquipment(equipment));

        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(editButton);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(deleteButton);

        card.add(leftPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);

        return card;
    }

    private void showEditEquipmentDialog(Equipment equipment) {
        JDialog dialog = new JDialog(this, "Edit Peralatan - " + equipment.getName(), true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Labels dan fields
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nama Peralatan:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(equipment.getName(), 20);
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Deskripsi:"), gbc);
        gbc.gridx = 1;
        JTextField descriptionField = new JTextField(equipment.getDescription(), 20);
        panel.add(descriptionField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Harga per Hari:"), gbc);
        gbc.gridx = 1;
        JTextField priceField = new JTextField(String.valueOf(equipment.getPricePerDay()), 20);
        panel.add(priceField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Jumlah Tersedia:"), gbc);
        gbc.gridx = 1;
        JTextField quantityField = new JTextField(String.valueOf(equipment.getQuantity()), 20);
        panel.add(quantityField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Tersedia:"), gbc);
        gbc.gridx = 1;
        JCheckBox availableCheckBox = new JCheckBox();
        availableCheckBox.setSelected(equipment.isAvailable());
        panel.add(availableCheckBox, gbc);

        // Tombol
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton confirmButton = new JButton("Simpan");
        JButton cancelButton = new JButton("Batal");

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, gbc);

        dialog.add(panel);

        // Event handlers
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String description = descriptionField.getText().trim();
                String priceText = priceField.getText().trim();
                String quantityText = quantityField.getText().trim();

                // Validasi input
                if (name.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                        "Nama, harga, dan jumlah harus diisi!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double price = Double.parseDouble(priceText);
                    int quantity = Integer.parseInt(quantityText);

                    if (price <= 0 || quantity <= 0) {
                        JOptionPane.showMessageDialog(dialog,
                            "Harga dan jumlah harus lebih besar dari 0!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Update peralatan
                    boolean success = equipmentController.updateEquipment(
                        equipment.getId(), name, description, price, quantity, availableCheckBox.isSelected()
                    );
                    if (success) {
                        JOptionPane.showMessageDialog(dialog,
                            "Peralatan berhasil diperbarui!",
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        // Refresh dialog
                        SwingUtilities.getWindowAncestor(dialog).dispose();
                        showEquipmentManagementDialog();
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                            "Gagal memperbarui peralatan!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Harga dan jumlah harus berupa angka!",
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

    private void deleteEquipment(Equipment equipment) {
        int result = JOptionPane.showConfirmDialog(this,
            "Hapus peralatan '" + equipment.getName() + "'?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            boolean success = equipmentController.deleteEquipment(equipment.getId());
            if (success) {
                JOptionPane.showMessageDialog(this, "Peralatan berhasil dihapus!");
                // Refresh dialog
                SwingUtilities.getWindowAncestor(rentalPanel).dispose();
                showEquipmentManagementDialog();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus peralatan!");
            }
        }
    }
}