-- Database: rental_alpinis
CREATE DATABASE IF NOT EXISTS rental_alpinis;
USE rental_alpinis;

-- Tabel users
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('pelanggan', 'owner', 'super_admin') NOT NULL DEFAULT 'pelanggan',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabel equipment
CREATE TABLE equipment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price_per_day DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    image VARCHAR(255),
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabel rentals
CREATE TABLE rentals (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    equipment_id INT NOT NULL,
    rental_date DATE NOT NULL,
    return_date DATE NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status ENUM('pending', 'confirmed', 'returned') DEFAULT 'pending',
    payment_confirmed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (equipment_id) REFERENCES equipment(id) ON DELETE CASCADE
);

-- Tabel pembayaran (opsional, untuk tracking pembayaran secara lebih detail)
CREATE TABLE payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rental_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(50),
    payment_status ENUM('pending', 'confirmed', 'failed') DEFAULT 'pending',
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (rental_id) REFERENCES rentals(id) ON DELETE CASCADE
);

-- Tambahkan super admin default
INSERT INTO users (username, email, password, role, is_active) 
VALUES ('superadmin', 'superadmin@rentalalpinis.com', 'superadmin123', 'super_admin', TRUE);

-- Tambahkan beberapa data peralatan contoh
INSERT INTO equipment (name, description, price_per_day, quantity, is_available) VALUES
('Tenda 4 Orang', 'Tenda yang kuat dan tahan air, cocok untuk pendakian', 50000.00, 5, TRUE),
('Carrier 60L', 'Ransel besar dengan sistem suspensi yang nyaman', 40000.00, 8, TRUE),
('Sleeping Bag', 'Kantong tidur yang hangat untuk suhu dingin', 25000.00, 10, TRUE),
('Matras Camping', 'Matras tebal untuk kenyamanan tidur di alam', 15000.00, 12, TRUE),
('Kompor Portable', 'Kompor gas mini untuk memasak di alam terbuka', 30000.00, 6, TRUE),
('Sepatu Gunung', 'Sepatu tahan air dengan sol anti-selip', 60000.00, 4, TRUE);