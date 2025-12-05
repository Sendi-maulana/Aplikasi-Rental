-- Skrip untuk memperbaiki nama kolom dari is_active menjadi isActive
ALTER TABLE users CHANGE COLUMN is_active isActive BOOLEAN DEFAULT TRUE;

-- Lakukan juga untuk tabel equipment - ubah is_available menjadi isAvailable
ALTER TABLE equipment CHANGE COLUMN is_available isAvailable BOOLEAN DEFAULT TRUE;

-- Dan untuk tabel rentals - ubah payment_confirmed menjadi paymentConfirmed
ALTER TABLE rentals CHANGE COLUMN payment_confirmed paymentConfirmed BOOLEAN DEFAULT FALSE;