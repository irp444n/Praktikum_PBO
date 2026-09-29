# Sistem Manajemen Perpustakaan Mini

Aplikasi konsol Java untuk mengelola koleksi buku, anggota, transaksi
peminjaman/pengembalian, serta analisis aktivitas perpustakaan.

## Struktur Direktori

```
perpustakaan/
└── src/
    └── library/
        ├── model/
        │   ├── Book.java
        │   └── Member.java
        ├── service/
        │   └── LibraryService.java
        ├── exception/
        │   ├── BookNotFoundException.java
        │   ├── BookAlreadyBorrowedException.java   (exception tambahan pelengkap 3 kondisi wajib)
        │   └── BorrowLimitExceededException.java
        └── main/
            └── MainApp.java
```

## Cara Compile

Dari dalam folder `perpustakaan/`, jalankan:

```bash
javac -d bin $(find src -name "*.java")
```

Ini akan meng-compile semua file `.java` dan meletakkan hasil `.class`
(termasuk struktur package-nya) ke dalam folder `bin/`.

Jika `find` tidak tersedia (mis. di Windows CMD), bisa juga:

```bash
javac -d bin src/library/model/*.java src/library/service/*.java src/library/exception/*.java src/library/main/*.java
```

## Cara Run (WAJIB dengan flag -ea untuk assertion)

Program ini menggunakan `assert` untuk memvalidasi data anggota sebelum
transaksi peminjaman/pengembalian. Secara default, JVM **mengabaikan**
statement `assert` kecuali dijalankan dengan flag `-ea` (enable assertions).

Jalankan dengan:

```bash
java -ea -cp bin library.main.MainApp
```

- `-ea` → mengaktifkan assertion. Tanpa flag ini, semua baris `assert ...`
  di dalam kode akan dilewati begitu saja (tidak dievaluasi).
- `-cp bin` → classpath diarahkan ke folder hasil compile.
- `library.main.MainApp` → nama class yang memiliki method `main`, ditulis
  lengkap dengan package-nya.

Untuk menjalankan TANPA assertion aktif (assertion akan dilewati):

```bash
java -cp bin library.main.MainApp
```

## Menu Aplikasi

1. Tambah Buku
2. Daftar Buku (sekaligus menampilkan jumlah buku per kategori)
3. Cari Buku (berdasarkan judul atau kategori, tidak case-sensitive)
4. Pinjam Buku
5. Kembalikan Buku
6. Laporan Perpustakaan (total pinjaman, anggota paling aktif, kategori
   paling populer, buku paling sering dipinjam)
7. Keluar

## Peta Konsep Wajib -> Lokasi di Kode

| Konsep                              | Lokasi                                                                 |
|--------------------------------------|-------------------------------------------------------------------------|
| Class, Object, Constructor           | `Book.java`, `Member.java` (constructor menginisialisasi field)         |
| Package                              | `library.model`, `library.service`, `library.exception`, `library.main` |
| Tipe primitive (int, boolean)        | `Book.tahunTerbit` (int), `Book.statusKetersediaan` (boolean)           |
| Tipe reference (String, ArrayList, HashMap) | `Book.judul` (String), `LibraryService.koleksiBuku` (ArrayList), `LibraryService.anggota` (HashMap) |
| Kondisional & Looping                | `LibraryService.cariBuku()`, `hitungJumlahPerKategori()`, `MainApp` (switch-case, while) |
| Custom Exception (3 buah)            | `BookNotFoundException`, `BookAlreadyBorrowedException`, `BorrowLimitExceededException` |
| Exception handling (try-catch)       | `MainApp.pinjamBuku()`, `MainApp.kembalikanBuku()`                      |
| Assertion (`assert`)                 | `LibraryService.pinjamBuku()` dan `kembalikanBuku()` — validasi anggota sebelum transaksi |
| Manipulasi String (toLowerCase, contains) | `Book.cocokBerdasarkanKataKunci()`                                 |
| Manipulasi String tambahan (format, split, repeat) | `Book.toString()`, `LibraryService.anggotaPalingAktif()`, `MainApp.tampilkanDaftarBuku()` |
| Manipulasi Character                 | `LibraryService.kapitalisasiSetiapKata()` (`Character.toUpperCase`, `Character.isLetter`, `Character.isWhitespace`) |
| Scanner (input konsol)               | `MainApp` (field `scanner`, dipakai di semua method menu)               |

## Catatan

- Program sudah diuji: berhasil di-compile dengan `javac` dan dijalankan
  dengan `java -ea` tanpa error, mencakup skenario pinjam, buku sudah
  dipinjam (exception), pengembalian, dan laporan.
- Data awal (5 buku, 2 anggota) sudah disediakan otomatis saat program
  dijalankan agar bisa langsung dicoba tanpa input manual dahulu.
