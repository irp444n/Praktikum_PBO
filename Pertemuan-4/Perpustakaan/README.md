# 📚 Sistem Manajemen Perpustakaan Mini

Aplikasi konsol berbasis Java untuk mengelola koleksi buku, data anggota, transaksi peminjaman dan pengembalian, serta analisis sederhana terhadap aktivitas perpustakaan (buku terpopuler, anggota paling aktif, kategori terpopuler).

Project ini dibangun dengan menerapkan konsep Object Oriented Programming (OOP) secara eksplisit, yaitu pemisahan model, service, exception, dan main ke dalam package tersendiri, serta memanfaatkan beberapa fitur inti Java seperti custom exception, assertion, dan manipulasi String serta Character.

## 1. Fitur

1. Manajemen Data Buku, meliputi menambahkan buku baru dan melihat seluruh koleksi.
2. Pencarian dan Analisis Buku, yaitu mencari buku berdasarkan judul atau kategori tanpa memperhatikan huruf besar dan kecil, serta menghitung jumlah buku per kategori.
3. Sistem Anggota, meliputi pendaftaran anggota dan pelacakan buku yang sedang dipinjam.
4. Peminjaman dan Pengembalian, yang divalidasi dengan custom exception dan assertion.
5. Laporan dan Analisis Aktivitas, meliputi total transaksi, anggota paling aktif, kategori paling populer, dan buku paling sering dipinjam.
6. Menu interaktif berbasis Scanner di terminal.

## 2. Struktur Project

```
perpustakaan/
├── README.md
└── src/
    └── library/
        ├── model/
        │   ├── Book.java                      # Entitas buku
        │   └── Member.java                    # Entitas anggota
        ├── service/
        │   └── LibraryService.java             # Logika bisnis aplikasi
        ├── exception/
        │   ├── BookNotFoundException.java      # Buku atau anggota tidak ditemukan
        │   ├── BookAlreadyBorrowedException.java # Buku sedang dipinjam
        │   └── BorrowLimitExceededException.java # Melebihi batas pinjam (3 buku)
        └── main/
            └── MainApp.java                    # Entry point dan menu interaktif
```

Prinsip pemisahan package yang dipakai:

| Package | Tanggung jawab |
|---|---|
| `library.model` | Menyimpan data (state). Tidak berisi logika bisnis yang rumit. |
| `library.service` | Menyimpan logika dan proses. Tempat semua aturan bisnis diproses. |
| `library.exception` | Kumpulan exception kustom khusus domain perpustakaan. |
| `library.main` | Titik masuk aplikasi dan tempat interaksi dengan pengguna (I/O). |

Alur data secara singkat dapat dijelaskan sebagai berikut.

1. Pengguna memasukkan input melalui `Scanner` pada kelas `MainApp`.
2. `MainApp` membaca pilihan menu, lalu memanggil method yang sesuai pada `LibraryService`.
3. `LibraryService` memvalidasi data yang diterima, lalu memproses objek `Book` atau `Member` yang bersangkutan. Jika ditemukan kondisi yang tidak valid, method ini akan melempar salah satu custom exception.
4. Hasil proses, baik berupa data maupun pesan kesalahan, dikembalikan ke `MainApp` untuk ditampilkan kepada pengguna.

## 3. Penjelasan Detail Setiap File

### `library.model.Book`

Merepresentasikan satu buku dalam koleksi perpustakaan.

Atribut:

| Atribut | Tipe | Keterangan |
|---|---|---|
| `judul` | `String` (reference) | Judul buku |
| `penulis` | `String` (reference) | Nama penulis |
| `tahunTerbit` | `int` (primitive) | Tahun terbit |
| `kategori` | `String` (reference) | Kategori atau genre buku |
| `statusKetersediaan` | `boolean` (primitive) | `true` berarti tersedia, `false` berarti sedang dipinjam |

Constructor menginisialisasi seluruh atribut. Tersedia dua versi overload, satu secara otomatis mengeset status menjadi tersedia, dan satu lagi mengizinkan status ditentukan secara manual.

Method penting pada kelas ini:

1. `cocokBerdasarkanKataKunci(String kataKunci)` melakukan pencarian pada judul dan kategori tanpa memperhatikan huruf besar dan kecil, menggunakan `toLowerCase()` dan `contains()`.
2. `toString()` menampilkan data buku dalam format tabel yang rapi menggunakan `String.format()`.

### `library.model.Member`

Merepresentasikan satu anggota perpustakaan.

Atribut:

| Atribut | Tipe | Keterangan |
|---|---|---|
| `id` | `String` | ID unik anggota |
| `nama` | `String` | Nama anggota |
| `daftarPinjaman` | `ArrayList<Book>` | Buku yang sedang dipinjam anggota ini |

Method penting pada kelas ini adalah `tambahPinjaman()`, `hapusPinjaman()`, dan `jumlahBukuDipinjam()`. Ketiganya dipakai oleh `LibraryService` untuk memvalidasi batas peminjaman.

### `library.exception` (Custom Exception)

Tiga checked exception yang masing masing mewakili satu kondisi kegagalan transaksi:

| Exception | Dilempar ketika |
|---|---|
| `BookNotFoundException` | Judul buku atau ID anggota tidak ditemukan di sistem |
| `BookAlreadyBorrowedException` | Buku yang ingin dipinjam berstatus sedang dipinjam |
| `BorrowLimitExceededException` | Anggota sudah meminjam 3 buku, yaitu batas maksimal |

Ketiganya mewarisi kelas `Exception`, bukan `RuntimeException`, sehingga bersifat checked. Artinya, setiap method yang melemparnya wajib mendeklarasikan `throws`, dan pemanggilnya wajib menanganinya dengan blok `try` dan `catch`.

### `library.service.LibraryService`

Kelas inti yang menyimpan seluruh state aplikasi dan memproses seluruh logika bisnis.

Struktur data yang dipakai:

```java
private ArrayList<Book> koleksiBuku;              // seluruh buku di perpustakaan
private HashMap<String, Member> anggota;           // key = id anggota
private ArrayList<String> riwayatTransaksi;        // log setiap transaksi
private HashMap<String, Integer> frekuensiPinjamBuku; // penghitung buku terpopuler
```

Method utama pada kelas ini:

| Method | Fungsi |
|---|---|
| `tambahBuku(Book)` | Menambahkan buku baru ke koleksi |
| `cariBuku(String)` | Mencari buku berdasarkan judul atau kategori |
| `hitungJumlahPerKategori()` | Menghitung jumlah buku tiap kategori dengan `HashMap` |
| `pinjamBuku(idAnggota, judul)` | Memvalidasi dan memproses peminjaman, dapat melempar 3 jenis exception di atas |
| `kembalikanBuku(idAnggota, judul)` | Memvalidasi dan memproses pengembalian |
| `bukuPalingSeringDipinjam()` | Analisis buku dengan frekuensi pinjam tertinggi |
| `anggotaPalingAktif()` | Analisis anggota dengan jumlah transaksi terbanyak |
| `kategoriPalingPopuler()` | Analisis kategori dengan total peminjaman terbanyak |

Assertion diterapkan pada `pinjamBuku()` dan `kembalikanBuku()`. Sebelum transaksi diproses, dilakukan pengecekan berikut:

```java
assert idAnggota != null && !idAnggota.trim().isEmpty() : "ID anggota tidak boleh kosong";
assert member != null : "Anggota dengan id '" + idAnggota + "' tidak terdaftar di sistem";
```

Assertion ini menjaga invariant internal program, yaitu asumsi bahwa data anggota yang diproses memang valid. Hal ini berbeda dari exception yang menangani kegagalan yang memang bisa terjadi secara wajar akibat input pengguna. Assertion hanya aktif jika program dijalankan dengan flag `-ea`.

Manipulasi Character terdapat pada method privat `kapitalisasiSetiapKata()`, yang mengubah huruf pertama tiap kata menjadi huruf kapital menggunakan `Character.toUpperCase()`, `Character.isLetter()`, dan `Character.isWhitespace()`. Method ini dipakai untuk merapikan tampilan judul buku terpopuler pada laporan.

### `library.main.MainApp`

Titik masuk program yang memiliki method `public static void main`. Kelas ini bertanggung jawab menampilkan menu, membaca input lewat `Scanner`, dan mendelegasikan setiap aksi ke `LibraryService`.

Struktur kontrol yang digunakan:

1. `while (berjalan)` merupakan loop utama menu yang berhenti saat pengguna memilih Keluar.
2. `switch (pilihan)` mengarahkan proses ke method yang sesuai dengan nomor menu yang dipilih.
3. Blok `try` dan `catch` menangani `BookNotFoundException`, `BookAlreadyBorrowedException`, `BorrowLimitExceededException`, dan `AssertionError` saat proses pinjam atau kembalikan buku, lalu menampilkan pesan yang sesuai kepada pengguna tanpa membuat program berhenti secara tiba tiba.

Menu yang tersedia:

```
1. Tambah Buku
2. Daftar Buku
3. Cari Buku
4. Pinjam Buku
5. Kembalikan Buku
6. Laporan Perpustakaan
7. Keluar
```

Program juga memuat beberapa data awal, yaitu 5 buku dan 2 anggota, lewat `inisialisasiDataAwal()` agar bisa langsung dicoba tanpa input manual.

## 4. Konsep yang Diterapkan dan Lokasinya di Kode

| Konsep | Lokasi |
|---|---|
| Class, Object, Constructor | `Book.java`, `Member.java` |
| Package | `library.model`, `library.service`, `library.exception`, `library.main` |
| Tipe primitive (`int`, `boolean`) | `Book.tahunTerbit`, `Book.statusKetersediaan` |
| Tipe reference (`String`, `ArrayList`, `HashMap`) | `Book.judul`, `LibraryService.koleksiBuku`, `LibraryService.anggota` |
| Kondisional dan Looping | `LibraryService.cariBuku()`, `hitungJumlahPerKategori()`, `MainApp` (`switch`, `while`) |
| Custom Exception | `BookNotFoundException`, `BookAlreadyBorrowedException`, `BorrowLimitExceededException` |
| Exception handling (`try`, `catch`) | `MainApp.pinjamBuku()`, `MainApp.kembalikanBuku()` |
| Assertion (`assert`) | `LibraryService.pinjamBuku()`, `kembalikanBuku()` |
| Manipulasi `String` (`toLowerCase`, `contains`) | `Book.cocokBerdasarkanKataKunci()` |
| Manipulasi `String` lainnya (`format`, `split`, `repeat`, `equalsIgnoreCase`) | `Book.toString()`, `LibraryService.anggotaPalingAktif()`, `MainApp.tampilkanDaftarBuku()` |
| Manipulasi `Character` | `LibraryService.kapitalisasiSetiapKata()` |
| `Scanner` (input konsol) | `MainApp` |

## 5. Instalasi dan Menjalankan

Prasyarat: JDK 8 atau versi yang lebih baru sudah terpasang. Bisa dicek dengan perintah `javac -version`.

Compile:

```bash
git clone <url_repo_project>
cd perpustakaan
javac -d bin $(find src -name "*.java")
```

Di Windows CMD, karena tidak tersedia perintah `find`, gunakan:

```bash
javac -d bin src\library\model\*.java src\library\service\*.java src\library\exception\*.java src\library\main\*.java
```

Jalankan program (wajib memakai flag `-ea` agar assertion aktif):

```bash
java -ea -cp bin library.main.MainApp
```

1. Flag `-ea` mengaktifkan `assert`. Tanpa flag ini, semua baris `assert` akan dilewati begitu saja oleh JVM.
2. Opsi `-cp bin` mengarahkan classpath ke folder hasil compile.

Menjalankan di NetBeans atau IntelliJ IDEA:

1. Buat project Java baru, lalu salin folder `src/library` ke dalam folder `src` project.
2. Atur Main Class menjadi `library.main.MainApp`.
3. Tambahkan `-ea` pada VM Options. Di NetBeans, klik kanan project, pilih Properties, lalu Run, lalu isi VM Options. Di IntelliJ, buka Edit Configurations, lalu isi VM options.
4. Jalankan seperti biasa menggunakan Run Project atau Shift F10.

## 6. Teknologi

Project ini menggunakan Java (JDK 8 ke atas) tanpa dependency atau library eksternal, dengan memanfaatkan `java.util.ArrayList`, `java.util.HashMap`, dan `java.util.Scanner` dari pustaka standar Java.