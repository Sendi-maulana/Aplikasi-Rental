# Cara Menjalankan Aplikasi Rental Peralatan Gunung

## Persyaratan Sistem
- Java 8+ terinstall
- MySQL 5.7+ terinstall dan berjalan
- Maven (opsional, untuk build otomatis)

## Instalasi dan Konfigurasi

### 1. Clone atau buat proyek
Proyek ini terdiri dari beberapa direktori:
- `src/` - Kode sumber Java
- `bin/` - Tempat file .class hasil kompilasi
- `database_setup.sql` - Skrip untuk membuat database
- `pom.xml` - File konfigurasi Maven (jika menggunakan Maven)

### 2. Setup Database
1. Buka command prompt atau terminal
2. Jalankan MySQL server
3. Buat database dengan menjalankan skrip:
   ```sql
   mysql -u root -p < database_setup.sql
   ```

### 3. Kompilasi dan Jalankan Aplikasi

#### Opsi 1: Menggunakan Command Prompt (tanpa Maven)
1. Download MySQL Connector/J dari: https://dev.mysql.com/downloads/connector/j/
2. Ekstrak file mysql-connector-java-x.x.x.jar
3. Kompilasi aplikasi:
   ```
   javac -d bin -cp ".;path/ke/mysql-connector-java-x.x.x.jar" src\*.java src\model\*.java src\view\*.java src\controller\*.java src\database\*.java src\utils\*.java
   ```
4. Jalankan aplikasi:
   ```
   java -cp "bin;path/ke/mysql-connector-java-x.x.x.jar" MainApp
   ```

#### Opsi 2: Menggunakan Maven
1. Install Maven jika belum terinstall
2. Download dan letakkan file mysql-connector-java-x.x.x.jar di lokal repository Maven
3. Jalankan perintah:
   ```
   mvn clean compile exec:java -Dexec.mainClass="MainApp"
   ```

## Konfigurasi Database
Ubah konfigurasi database pada file `src/database/DatabaseConfig.java` sesuai dengan pengaturan MySQL Anda:
```java
private static final String URL = "jdbc:mysql://127.0.0.1:3306/rental_alpinis?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta&allowLoadLocalInfile=true&connectTimeout=60000&socketTimeout=60000&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&initialTimeout=2";
private static final String USERNAME = "root";
private static final String PASSWORD = "";
```

## Akun Default
- Super Admin:
  - Username: superadmin
  - Password: superadmin123

## Struktur Aplikasi
- Model: User, Equipment, Rental
- View: LoginView, RegisterView, CustomerDashboardView, OwnerView, AdminView
- Controller: UserController, EquipmentController, RentalController, PaymentController
- Database: DatabaseConfig
- Utils: Session

## Testing
File TestApp.java disediakan untuk testing dasar fungsionalitas aplikasi.