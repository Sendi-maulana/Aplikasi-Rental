# Aplikasi Rental Peralatan Gunung

Aplikasi ini dibuat menggunakan Java dan MySQL untuk mengelola rental peralatan gunung. Aplikasi ini memiliki tiga jenis pengguna: pelanggan, owner, dan super admin.

## Fitur
- Sistem login dan registrasi
- Manajemen peralatan rental
- Peminjaman peralatan
- Sistem pembayaran dan konfirmasi pembayaran
- Tiga level akses: pelanggan, owner, super admin

## Struktur Proyek
- `model/` - Kelas-kelas model untuk data
- `view/` - Antarmuka pengguna
- `controller/` - Logika bisnis
- `database/` - Konfigurasi database
- `utils/` - Kelas utilitas

## Persyaratan
- Java 8+
- MySQL 5.7+
- JDBC MySQL Connector

## Cara Menjalankan Aplikasi
1. Pastikan MySQL telah terinstall dan berjalan
2. Buat database dengan menjalankan skrip `database_setup.sql`
3. Impor proyek ke IDE Java (seperti IntelliJ IDEA atau Eclipse)
4. Tambahkan JDBC connector ke classpath
5. Jalankan kelas `MainApp`

## Konfigurasi Database
Ubah konfigurasi database pada file `src/database/DatabaseConfig.java` sesuai dengan pengaturan MySQL Anda:

```java
private static final String URL = "jdbc:mysql://127.0.0.1:3306/rental_alpinis?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta&allowLoadLocalInfile=true&connectTimeout=60000&socketTimeout=60000&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&initialTimeout=2";
private static final String USERNAME = "root";
private static final String PASSWORD = "";
```

## Akun Default
- Super Admin: username = `superadmin`, password = `superadmin123`

## Penjelasan Tugas
1. **Pelanggan**: Dapat login, registrasi, melihat peralatan, memesan dan membayar peralatan
2. **Owner**: Dapat melihat daftar peminjaman dan informasi pelanggan
3. **Super Admin**: Dapat mengelola akun, peralatan, dan mengkonfirmasi pembayaran

## Database Schema
1. `users`: Menyimpan informasi pengguna
2. `equipment`: Menyimpan informasi peralatan
3. `rentals`: Menyimpan informasi peminjaman
4. `payments`: Menyimpan informasi pembayaran
java -cp bin;bin/mysql-connector-j-8.0.33.jar MainApp