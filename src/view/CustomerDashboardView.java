package view;

import controller.EquipmentController;
import controller.PaymentController;
import controller.RentalController;
import model.Equipment;
import model.Rental;
import utils.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CustomerDashboardView extends JFrame {
    private EquipmentController equipmentController;
    private RentalController rentalController;
    private PaymentController paymentController;
    private JPanel equipmentPanel;
    private JLabel welcomeLabel;
    private JButton logoutButton;

    public CustomerDashboardView() {
        equipmentController = new EquipmentController();
        rentalController = new RentalController();
        paymentController = new PaymentController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadEquipmentData();
    }

    private void initializeComponents() {
        setTitle("Dashboard Pelanggan - Rental Peralatan Gunung");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        equipmentPanel = new JPanel();
        equipmentPanel.setLayout(new BoxLayout(equipmentPanel, BoxLayout.Y_AXIS));

        welcomeLabel = new JLabel("Selamat Datang, " + Session.getCurrentUser().getUsername());
        logoutButton = new JButton("Logout");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(headerPanel, BorderLayout.NORTH);

        // Main content panel
        JScrollPane scrollPane = new JScrollPane(equipmentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");

        JMenuItem profileItem = new JMenuItem("Profil Saya");
        JMenuItem historyItem = new JMenuItem("Riwayat Peminjaman");

        menu.add(profileItem);
        menu.add(historyItem);
        menuBar.add(menu);

        setJMenuBar(menuBar);

        // Aksi menu
        profileItem.addActionListener(e -> showProfileDialog());
        historyItem.addActionListener(e -> showRentalHistory());
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
    }

    private void loadEquipmentData() {
        List<Equipment> equipmentList = equipmentController.getAllEquipment();
        equipmentPanel.removeAll();

        for (Equipment equipment : equipmentList) {
            if (equipment.isAvailable()) {
                JPanel equipmentCard = createEquipmentCard(equipment);
                equipmentPanel.add(equipmentCard);
                equipmentPanel.add(Box.createVerticalStrut(10)); // Jarak antar card
            }
        }

        equipmentPanel.revalidate();
        equipmentPanel.repaint();
    }

    private JPanel createEquipmentCard(Equipment equipment) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createTitledBorder(equipment.getName()));
        card.setPreferredSize(new Dimension(800, 120));

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

        // Panel kanan - tombol rental
        JPanel rightPanel = new JPanel(new FlowLayout());
        JButton rentButton = new JButton("Sewa Sekarang");

        rentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRentalDialog(equipment);
            }
        });

        rightPanel.add(rentButton);

        card.add(leftPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);

        return card;
    }

    private void showRentalDialog(Equipment equipment) {
        JDialog dialog = new JDialog(this, "Formulir Peminjaman - " + equipment.getName(), true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Labels dan fields
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Tanggal Peminjaman:"), gbc);
        gbc.gridx = 1;
        JSpinner rentalDateSpinner = new JSpinner(new SpinnerDateModel(java.util.Date.from(LocalDate.now().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()), null, null, java.util.Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(rentalDateSpinner, "dd/MM/yyyy");
        rentalDateSpinner.setEditor(dateEditor);
        panel.add(rentalDateSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Tanggal Pengembalian:"), gbc);
        gbc.gridx = 1;
        JSpinner returnDateSpinner = new JSpinner(new SpinnerDateModel(java.util.Date.from(LocalDate.now().plusDays(1).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()), null, null, java.util.Calendar.DAY_OF_MONTH));
        returnDateSpinner.setEditor(new JSpinner.DateEditor(returnDateSpinner, "dd/MM/yyyy"));
        panel.add(returnDateSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Jumlah Peralatan:"), gbc);
        gbc.gridx = 1;
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, equipment.getQuantity(), 1));
        panel.add(quantitySpinner, gbc);

        // Tombol
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton confirmButton = new JButton("Konfirmasi");
        JButton cancelButton = new JButton("Batal");

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, gbc);

        dialog.add(panel);

        // Event handlers
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validasi tanggal
                LocalDate rentalDate = ((java.util.Date) rentalDateSpinner.getValue()).toInstant()
                        .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                LocalDate returnDate = ((java.util.Date) returnDateSpinner.getValue()).toInstant()
                        .atZone(java.time.ZoneId.systemDefault()).toLocalDate();

                if (rentalDate.isBefore(LocalDate.now())) {
                    JOptionPane.showMessageDialog(dialog, "Tanggal peminjaman tidak boleh kurang dari hari ini!");
                    return;
                }

                if (returnDate.isBefore(rentalDate)) {
                    JOptionPane.showMessageDialog(dialog, "Tanggal pengembalian harus setelah tanggal peminjaman!");
                    return;
                }

                int quantity = (Integer) quantitySpinner.getValue();
                if (quantity > equipment.getQuantity()) {
                    JOptionPane.showMessageDialog(dialog, "Jumlah peralatan yang diminta melebihi stok tersedia!");
                    return;
                }

                // Hitung total harga
                long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(rentalDate, returnDate) + 1;
                double totalPrice = daysBetween * equipment.getPricePerDay() * quantity;

                int confirm = JOptionPane.showConfirmDialog(dialog,
                        "Konfirmasi peminjaman:\n" +
                        "Peralatan: " + equipment.getName() + "\n" +
                        "Tanggal: " + rentalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                        " - " + returnDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
                        "Jumlah: " + quantity + "\n" +
                        "Total Harga: Rp " + String.format("%.0f", totalPrice) + "\n\n" +
                        "Lanjutkan?",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = rentalController.createRental(
                        Session.getCurrentUser().getId(),
                        equipment.getId(),
                        rentalDate,
                        returnDate,
                        totalPrice
                    );

                    if (success) {
                        int result = JOptionPane.showConfirmDialog(dialog,
                                "Peminjaman berhasil! Ingin melakukan pembayaran sekarang?",
                                "Pembayaran",
                                JOptionPane.YES_NO_OPTION);

                        if (result == JOptionPane.YES_OPTION) {
                            // Ambil rental terakhir yang dibuat untuk pengguna ini
                            List<Rental> userRentals = rentalController.getRentalsByUserId(Session.getCurrentUser().getId());
                            if (!userRentals.isEmpty()) {
                                Rental latestRental = userRentals.get(0); // Ambil rental terbaru
                                showPaymentDialog(latestRental);
                            }
                        }

                        loadEquipmentData(); // Refresh tampilan
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Gagal membuat peminjaman!");
                    }
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

    private void showProfileDialog() {
        model.User user = Session.getCurrentUser();
        JOptionPane.showMessageDialog(this,
            "Profil Pengguna\n\n" +
            "ID: " + user.getId() + "\n" +
            "Username: " + user.getUsername() + "\n" +
            "Email: " + user.getEmail() + "\n" +
            "Role: " + user.getRole(),
            "Profil Saya",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void showRentalHistory() {
        List<Rental> rentals = rentalController.getRentalsByUserId(Session.getCurrentUser().getId());

        if (rentals.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Belum ada riwayat peminjaman.", "Riwayat Peminjaman", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));

        for (Rental rental : rentals) {
            JPanel rentalCard = new JPanel(new BorderLayout());
            rentalCard.setBorder(BorderFactory.createTitledBorder("Rental ID: " + rental.getId()));
            rentalCard.setPreferredSize(new Dimension(600, 100));

            // Panel kiri - informasi rental
            JPanel leftPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.gridx = 0; gbc.gridy = 0;
            leftPanel.add(new JLabel("Tanggal: " + rental.getRentalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                          " - " + rental.getReturnDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))), gbc);
            gbc.gridy++;
            leftPanel.add(new JLabel("Total: Rp " + String.format("%.0f", rental.getTotalPrice())), gbc);
            gbc.gridy++;
            leftPanel.add(new JLabel("Status: " + rental.getStatus()), gbc);
            gbc.gridy++;
            leftPanel.add(new JLabel("Pembayaran: " + (rental.isPaymentConfirmed() ? "Dikonfirmasi" : "Belum dikonfirmasi")), gbc);

            // Panel kanan - tombol aksi
            JPanel rightPanel = new JPanel(new FlowLayout());
            JButton paymentButton = new JButton("Bayar Sekarang");
            paymentButton.setEnabled(!rental.isPaymentConfirmed() && "pending".equals(rental.getStatus()));

            paymentButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showPaymentDialog(rental);
                }
            });

            rightPanel.add(paymentButton);

            rentalCard.add(leftPanel, BorderLayout.CENTER);
            rentalCard.add(rightPanel, BorderLayout.EAST);

            historyPanel.add(rentalCard);
            historyPanel.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(historyPanel);
        scrollPane.setPreferredSize(new Dimension(700, 500));

        JOptionPane.showMessageDialog(this, scrollPane, "Riwayat Peminjaman", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showPaymentDialog(Rental rental) {
        JDialog dialog = new JDialog(this, "Formulir Pembayaran - Rental ID: " + rental.getId(), true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Informasi rental
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel infoLabel = new JLabel("<html><b>Informasi Peminjaman:</b><br/>" +
                "Tanggal: " + rental.getRentalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                " - " + rental.getReturnDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "<br/>" +
                "Total: Rp " + String.format("%.0f", rental.getTotalPrice()) + "</html>");
        panel.add(infoLabel, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Metode Pembayaran:"), gbc);
        gbc.gridx = 1;
        String[] paymentMethods = {"Transfer Bank", "E-Wallet", "COD (Bayar saat pengambilan)"};
        JComboBox<String> paymentMethodBox = new JComboBox<>(paymentMethods);
        panel.add(paymentMethodBox, gbc);

        // Tombol
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton payButton = new JButton("Proses Pembayaran");
        JButton cancelButton = new JButton("Batal");

        buttonPanel.add(payButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, gbc);

        dialog.add(panel);

        // Event handlers
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String method = (String) paymentMethodBox.getSelectedItem();

                int confirm = JOptionPane.showConfirmDialog(dialog,
                        "Konfirmasi pembayaran:\n" +
                        "Rental ID: " + rental.getId() + "\n" +
                        "Metode: " + method + "\n" +
                        "Jumlah: Rp " + String.format("%.0f", rental.getTotalPrice()) + "\n\n" +
                        "Lanjutkan?",
                        "Konfirmasi Pembayaran",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    // Buat entri pembayaran
                    boolean success = paymentController.createPayment(rental.getId(), rental.getTotalPrice(), method);

                    if (success) {
                        JOptionPane.showMessageDialog(dialog,
                            "Pembayaran berhasil diproses! Tunggu konfirmasi dari admin.",
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);

                        dialog.dispose();
                        // Refresh history
                        showRentalHistory();
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                            "Gagal memproses pembayaran!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
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
}