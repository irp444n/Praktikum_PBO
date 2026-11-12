# Program Pengelolaan Nilai Mahasiswa (Java OOP)

Program Java console berorientasi objek untuk mengelola data nilai mahasiswa. Program membaca input dari pengguna, memvalidasi data dengan **Assertion** dan **Custom Exception**, menyimpan objek dalam **ArrayList**, mengolah **String/Character**, lalu menyimpan rekapitulasi ke file teks `laporan_nilai.txt` menggunakan **File I/O**.

## Struktur Proyek

```
P7/
├── app/
│   └── Main.java                    # Kelas utama (input, statistik, tulis file)
└── model/
    ├── Mahasiswa.java               # Kelas data + validasi + grade
    └── NilaiInvalidException.java   # Custom exception untuk nilai tidak valid
```

## Persyaratan

- JDK 8 atau lebih baru (`javac` dan `java` tersedia di PATH)

## Cara Menjalankan

Dari folder `P7`:

```bash
# 1. Kompilasi
javac -d out model/*.java app/*.java

# 2. Jalankan (flag -ea mengaktifkan assertion)
java -ea -cp out app.Main
```

> **Penting:** `assert` di Java **nonaktif secara default**. Tanpa `-ea`, baris `assert` tidak dieksekusi. Karena itu `Mahasiswa` juga memiliki pengecekan `if` eksplisit agar validasi tetap berjalan tanpa `-ea`.
> Di IDE (IntelliJ/VS Code/Eclipse), tambahkan `-ea` pada *VM options*.

File `laporan_nilai.txt` dibuat di direktori tempat perintah `java` dijalankan.

## Alur Program

1. Pengguna memasukkan jumlah mahasiswa.
2. Untuk tiap mahasiswa, pengguna mengisi nama, NIM, dan nilai.
3. Jika input tidak valid, pesan error ditampilkan dan input diulang untuk mahasiswa yang sama.
4. Data valid disimpan ke `ArrayList<Mahasiswa>`.
5. Program menghitung rata-rata kelas, jumlah lulus, dan jumlah tidak lulus.
6. Rekap ditampilkan di console dan ditulis ke `laporan_nilai.txt`.

## Penjelasan Kelas

### `model.NilaiInvalidException`
Turunan `Exception` (checked). Dilempar ketika nilai kurang dari 0 atau lebih dari 100.

### `model.Mahasiswa`
| Anggota | Keterangan |
|---|---|
| `FINAL_PASSING_GRADE` | Konstanta `final` = `70.0` (batas kelulusan) |
| `nama`, `nim` (`String`) | Reference type |
| `nilai` (`double`) | Primitive type |
| Constructor | Merapikan input, memvalidasi, lalu menginisialisasi atribut |
| `getGrade()` | Memetakan nilai ke huruf dengan if-else |
| `isLulus()` | `true` jika `nilai >= FINAL_PASSING_GRADE` |
| `toString()` | Satu baris rekap berformat |

**Pemetaan grade:**

| Nilai | Grade |
|---|---|
| ≥ 85 | A |
| ≥ 75 | B |
| ≥ 70 | C |
| < 70 | D |

> Rentang di atas adalah asumsi. Sesuaikan dengan ketentuan mata kuliah jika berbeda.

**Validasi di constructor:**
- `trim()` dan `toUpperCase()` pada nama; `trim()` pada NIM.
- `assert !nimBersih.isEmpty() : "NIM tidak boleh kosong!"` (aktif dengan `-ea`).
- NIM kosong atau nama kosong melempar `IllegalArgumentException`.
- Setiap karakter NIM dicek dengan `Character.isDigit()`; selain angka melempar `IllegalArgumentException`.
- Nilai di luar 0–100 melempar `NilaiInvalidException`.

### `app.Main`
- `Scanner` untuk input console (try-with-resources).
- `ArrayList<Mahasiswa>` dengan `add()`, `size()`, dan for-each.
- Menangani `NumberFormatException`, `NilaiInvalidException`, `IllegalArgumentException`, dan `AssertionError` lalu meminta input ulang.
- Menulis laporan memakai `BufferedWriter` + `FileWriter` dalam try-with-resources, dengan `catch (IOException)`.

## Pemetaan Materi

| Pertemuan | Materi | Implementasi |
|---|---|---|
| 2 | Package, class, constructor, final | Package `model`/`app`, constructor `Mahasiswa`, `FINAL_PASSING_GRADE` |
| 3 | Kondisional, looping, primitive vs reference | if-else grade, for-each rata-rata, `double`/`boolean` vs `Mahasiswa`/`String` |
| 4 | Exception, assertion, String, Character | `NilaiInvalidException`, `assert`, `trim()`, `toUpperCase()`, `Character.isDigit()` |
| 5 | Collection | `ArrayList<Mahasiswa>` |
| 6 | Console I/O & File I/O | `Scanner`, `BufferedWriter`, `FileWriter`, try-with-resources |

## Contoh Input

```
Jumlah mahasiswa: 2

--- Mahasiswa ke-1 ---
Nama  : budi
NIM   : 123
Nilai : 80

--- Mahasiswa ke-2 ---
Nama  : ani
NIM   : 456
Nilai : 60
```

## Contoh Isi `laporan_nilai.txt`

```
===== LAPORAN NILAI MAHASISWA =====
NIM          NAMA                       NILAI  GR STATUS
123          BUDI                       80.00  B  LULUS
456          ANI                        60.00  D  TIDAK LULUS

Jumlah mahasiswa : 2
Rata-rata kelas  : 70.00
Jumlah lulus     : 1
Jumlah tdk lulus : 1
```

## Skenario Uji yang Disarankan

| Input | Hasil yang diharapkan |
|---|---|
| Nilai `150` atau `-5` | `NilaiInvalidException`, input diulang |
| Nilai `abc` | Pesan "Nilai harus berupa angka", input diulang |
| NIM `12a` | `IllegalArgumentException` (NIM hanya angka), input diulang |
| NIM kosong, dengan `-ea` | `AssertionError` ditangkap, input diulang |
| NIM kosong, tanpa `-ea` | `IllegalArgumentException`, input diulang |

## Catatan

- Mengandalkan `assert` untuk validasi input pengguna bukan praktik yang baik karena bisa dinonaktifkan. Di sini `assert` dipertahankan sesuai ketentuan tugas, ditambah pengecekan `if` sebagai pengaman.
- Menangkap `AssertionError` dilakukan agar program tidak berhenti saat demo dengan `-ea`.
