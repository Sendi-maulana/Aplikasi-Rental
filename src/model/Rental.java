package model;

import java.time.LocalDate;

public class Rental {
    private int id;
    private int userId;
    private int equipmentId;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private double totalPrice;
    private String status; // pending, confirmed, returned
    private boolean paymentConfirmed;

    public Rental() {}

    public Rental(int id, int userId, int equipmentId, LocalDate rentalDate, LocalDate returnDate,
                  double totalPrice, String status, boolean paymentConfirmed) {
        this.id = id;
        this.userId = userId;
        this.equipmentId = equipmentId;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.paymentConfirmed = paymentConfirmed;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getEquipmentId() { return equipmentId; }
    public void setEquipmentId(int equipmentId) { this.equipmentId = equipmentId; }

    public LocalDate getRentalDate() { return rentalDate; }
    public void setRentalDate(LocalDate rentalDate) { this.rentalDate = rentalDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isPaymentConfirmed() { return paymentConfirmed; }
    public void setPaymentConfirmed(boolean paymentConfirmed) { this.paymentConfirmed = paymentConfirmed; }
}