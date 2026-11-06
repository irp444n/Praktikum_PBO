# Praktik Lab Session 3B — Sistem Rental Kendaraan

## 1. Deskripsi Program

Program ini merupakan aplikasi konsol sederhana untuk menghitung biaya penyewaan kendaraan. Pengguna dapat melihat informasi kendaraan dan menghitung biaya sewa berdasarkan jumlah hari. Program menyediakan dua jenis kendaraan, yaitu mobil dan motor.

Studi kasus ini dipilih karena memiliki hubungan objek yang jelas: mobil dan motor sama-sama termasuk kendaraan, tetapi keduanya dapat memiliki informasi serta aturan perhitungan yang dikembangkan melalui class masing-masing.

## 2. Struktur File

| File | Fungsi |
|---|---|
| `Main.java` | Titik awal program, membuat objek, dan menjalankan contoh penggunaan |
| `Kendaraan.java` | Abstract superclass yang menyimpan data umum kendaraan dan aturan sewa |
| `Mobil.java` | Subclass untuk kendaraan jenis mobil |
| `Motor.java` | Subclass untuk kendaraan jenis motor |
| `DapatDisewa.java` | Interface yang menetapkan method informasi penyewaan |

## 3. Hubungan Antarclass

- `Kendaraan` merupakan abstract superclass.
- `Mobil` dan `Motor` merupakan subclass yang mewarisi `Kendaraan` menggunakan `extends`.
- `Kendaraan` mengimplementasikan interface `DapatDisewa` menggunakan `implements`.
- `Mobil` dan `Motor` mengisi implementasi method `tampilkanInfoSewa()` sesuai karakteristik masing-masing.
- `Main` membuat dan menggunakan empat objek: dua objek `Mobil` dan dua objek `Motor`.

Gambaran hubungan:

```text
             DapatDisewa (interface)
                      ^
                      |
             Kendaraan (abstract)
                 /       \\
                /         \\
             Mobil       Motor
                \\         /
                 \\       /
                   Main
```

## 4. Penerapan Konsep OOP

### A. Encapsulation

Atribut `namaKendaraan` dan `tarifHarian` pada class `Kendaraan` menggunakan access modifier `private`. Atribut `kapasitasPenumpang` pada `Mobil` dan `jenisMotor` pada `Motor` juga bersifat `private`.

Data tersebut diakses atau diubah melalui getter dan setter. Contohnya:

```java
mobil2.setTarifHarian(320000);
System.out.println(mobil2.getTarifHarian());
```

Setter juga melakukan validasi agar tarif tidak bernilai nol atau negatif.

### B. Inheritance

Class `Mobil` dan `Motor` mewarisi class `Kendaraan` dengan keyword `extends`.

Constructor subclass memanggil constructor superclass menggunakan `super()`:

```java
public Mobil(String namaKendaraan, double tarifHarian, int kapasitasPenumpang) {
    super(namaKendaraan, tarifHarian);
    setKapasitasPenumpang(kapasitasPenumpang);
}
```

Dengan pewarisan ini, kedua subclass dapat menggunakan atribut dan method umum yang disediakan `Kendaraan`.

### C. Polymorphism

**Method overriding** dilakukan ketika `Mobil` dan `Motor` menulis ulang method `hitungBiaya()` dan `tampilkanInfoSewa()` yang berasal dari superclass atau interface. Anotasi `@Override` digunakan untuk menandai method yang ditimpa.

**Method overloading** diterapkan pada method `sewa()` di class `Kendaraan`:

```java
sewa(int lamaHari)
sewa(int lamaHari, boolean menggunakanAsuransi)
```

Keduanya memiliki nama sama, tetapi jumlah parameternya berbeda.

**Runtime polymorphism** ditunjukkan melalui array bertipe `Kendaraan` yang menyimpan objek `Mobil` dan `Motor`:

```java
Kendaraan[] daftarKendaraan = { mobil1, mobil2, motor1, motor2 };

for (Kendaraan kendaraan : daftarKendaraan) {
    kendaraan.tampilkanInfoSewa(2);
}
```

Walaupun referensinya bertipe `Kendaraan`, method yang dijalankan mengikuti class objek sebenarnya, yaitu `Mobil` atau `Motor`.

### D. Abstract Class

Class `Kendaraan` ditetapkan sebagai `abstract`, sehingga tidak dibuat objek secara langsung. Class ini memiliki abstract method:

```java
public abstract double hitungBiaya(int lamaHari);
```

Method tersebut wajib diimplementasikan oleh subclass konkret. `Mobil` dan `Motor` menyediakan implementasinya menggunakan `@Override`.

### E. Interface

Interface `DapatDisewa` mendeklarasikan kontrak method:

```java
void tampilkanInfoSewa(int lamaHari);
```

Class `Kendaraan` menyatakan `implements DapatDisewa`. Karena `Kendaraan` merupakan abstract class, implementasi method interface dapat diselesaikan oleh subclass konkret. Class `Mobil` dan `Motor` masing-masing mengimplementasikan `tampilkanInfoSewa()`.

## 5. Cara Menjalankan Program

Pastikan Java Development Kit (JDK) sudah terpasang.

1. Simpan semua file `.java` dalam satu folder.
2. Buka terminal atau Command Prompt pada folder tersebut.
3. Kompilasi seluruh file:

```bash
javac *.java
```

4. Jalankan program:

```bash
java Main
```

## 6. Contoh Output

```text
=== SISTEM RENTAL KENDARAAN ===

1. Contoh pemanggilan method overloading
Biaya sewa Toyota Avanza selama 2 hari: Rp700000.0
Biaya sewa Honda Vario selama 3 hari dengan asuransi: Rp375000.0

2. Contoh runtime polymorphism
Jenis: Mobil | Kendaraan: Toyota Avanza | Kapasitas: 7 orang | Total: Rp700000.0
Jenis: Mobil | Kendaraan: Honda Brio | Kapasitas: 5 orang | Total: Rp600000.0
Jenis: Motor | Kendaraan: Honda Vario | Tipe: Matic | Total: Rp200000.0
Jenis: Motor | Kendaraan: Yamaha NMAX | Tipe: Matic | Total: Rp300000.0

3. Contoh encapsulation
Tarif awal Honda Brio: Rp300000.0
Tarif setelah diubah: Rp320000.0

Program selesai.
```

*Catatan: output di atas merupakan contoh berdasarkan data objek yang dibuat pada `Main.java`.*

## 7. Kesimpulan

Program Sistem Rental Kendaraan telah menerapkan konsep OOP yang diminta. Encapsulation digunakan untuk melindungi atribut melalui getter dan setter. Inheritance digunakan agar `Mobil` dan `Motor` mewarisi sifat umum dari `Kendaraan`. Polymorphism ditunjukkan melalui overriding, overloading, dan runtime polymorphism. Abstract class digunakan sebagai kerangka umum kendaraan, sedangkan interface `DapatDisewa` menetapkan method yang harus dipenuhi oleh class konkret.

Program memiliki lima class/interface yang relevan, yaitu `Main`, `Kendaraan`, `Mobil`, `Motor`, dan `DapatDisewa`, serta menggunakan empat objek kendaraan.
