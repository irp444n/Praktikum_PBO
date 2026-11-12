# Program Pengelola Nilai Siswa (Java)

Program Java prosedural sederhana untuk mengelola data nilai siswa. Program membaca data dari konsol, menyimpannya ke file teks `laporan_nilai.txt`, lalu membaca kembali file tersebut dan menampilkannya di konsol sebagai bukti data tersimpan dengan benar.

## Fitur

- Input jumlah siswa, nama, dan nilai melalui keyboard (`Scanner`).
- Penentuan status kelulusan otomatis: nilai **≥ 70 → Lulus**, nilai **< 70 → Tidak Lulus**.
- Penulisan laporan ke `laporan_nilai.txt` memakai `FileWriter` dan `BufferedWriter`.
- Ringkasan di akhir file: rata-rata nilai kelas, jumlah siswa lulus, dan jumlah siswa tidak lulus.
- Pembacaan ulang file memakai `FileReader` dan `BufferedReader` (`readLine()` sampai `null`).
- Penanganan error dengan `try-with-resources` dan `catch (IOException)`.
- Validasi input: program mengulang pertanyaan jika input tidak valid.

## Struktur Proyek

```
.
├── LaporanNilai.java    # Source code utama
├── README.md            # Dokumentasi ini
└── laporan_nilai.txt    # Dibuat otomatis saat program dijalankan
```

## Persyaratan

- JDK 8 atau lebih baru (`try-with-resources` membutuhkan minimal Java 7).
- Terminal / command prompt, atau IDE seperti VS Code, IntelliJ IDEA, atau NetBeans.

Cek instalasi Java:

```bash
java -version
javac -version
```

## Cara Menjalankan

1. Buka terminal di folder yang berisi `LaporanNilai.java`.
2. Kompilasi:
   ```bash
   javac LaporanNilai.java
   ```
3. Jalankan:
   ```bash
   java LaporanNilai
   ```

> **Catatan:** `laporan_nilai.txt` dibuat di *working directory*, yaitu folder tempat perintah `java` dijalankan. Jika memakai IDE, lokasinya bisa berbeda dari folder source code.

## Contoh Penggunaan

**Input / interaksi di konsol:**

```
Masukkan jumlah siswa: 2

Siswa ke-1
Nama  : Budi
Nilai : 80

Siswa ke-2
Nama  : Sari
Nilai : 65

===== ISI FILE laporan_nilai.txt =====
=== LAPORAN NILAI SISWA ===
1. Budi | Nilai: 80.00 | Status: Lulus
2. Sari | Nilai: 65.00 | Status: Tidak Lulus
---------------------------
Rata-rata nilai kelas : 72.50
Jumlah siswa lulus    : 1
Jumlah tidak lulus    : 1
```

**Isi file `laporan_nilai.txt`:**

```
=== LAPORAN NILAI SISWA ===
1. Budi | Nilai: 80.00 | Status: Lulus
2. Sari | Nilai: 65.00 | Status: Tidak Lulus
---------------------------
Rata-rata nilai kelas : 72.50
Jumlah siswa lulus    : 1
Jumlah tidak lulus    : 1
```

## Alur Program

```
main()
 ├── bacaJumlah()      → minta jumlah siswa (harus > 0)
 ├── tulisLaporan()    → loop input tiap siswa, tulis ke file + ringkasan
 │     ├── bacaNama()
 │     ├── bacaNilai()
 │     └── tentukanStatus()
 └── bacaLaporan()     → baca file baris demi baris, tampilkan ke konsol
```

## Penjelasan Method

| Method | Fungsi |
|---|---|
| `bacaJumlah(Scanner, String)` | Membaca bilangan bulat > 0; mengulang jika input salah. |
| `bacaNama(Scanner, String)` | Membaca nama; tidak boleh kosong. |
| `bacaNilai(Scanner, String)` | Membaca nilai 0–100; menerima koma atau titik sebagai pemisah desimal. |
| `tentukanStatus(double)` | Mengembalikan `"Lulus"` jika nilai ≥ 70, selain itu `"Tidak Lulus"`. |
| `tulisLaporan(Scanner, int)` | Menulis data siswa dan ringkasan ke `laporan_nilai.txt`. |
| `bacaLaporan()` | Membaca `laporan_nilai.txt` dan mencetak isinya ke konsol. |
| `main(String[])` | Menjalankan seluruh alur dan menangani `IOException`. |

## Keputusan Desain

- **Semua input dibaca dengan `nextLine()`** lalu di-parse. Ini menghindari bug `Scanner` ketika `nextInt()` / `nextDouble()` meninggalkan karakter Enter dan membuat input nama berikutnya terlewat.
- **Validasi input** mencegah `NumberFormatException` dan pembagian `0/0` (rata-rata `NaN`) jika jumlah siswa 0.
- **`Locale.US` pada `String.format`** menjamin desimal selalu memakai titik, apa pun pengaturan bahasa sistem.
- **`try-with-resources`** menutup writer dan reader otomatis, termasuk saat terjadi error. Untuk writer, penutupan juga memastikan data benar-benar tertulis ke disk.
- **File dibaca setelah writer ditutup**, sehingga isi file sudah lengkap saat dibaca.

## Batasan

- Setiap kali program dijalankan, `laporan_nilai.txt` **ditimpa** (tidak di-append). Data dari eksekusi sebelumnya hilang.
- Nama siswa dengan karakter non-ASCII bergantung pada charset bawaan sistem karena memakai `FileWriter` / `FileReader`.
- Data hanya tersimpan sebagai teks; program tidak memuat ulang data lama untuk diedit.
- Batas kelulusan (70) ditetapkan sebagai konstanta `BATAS_LULUS` di source code, bukan input pengguna.

## Pengembangan Lanjutan (Opsional)

- Mode append agar data antar-eksekusi tidak hilang.
- Memakai `Files.newBufferedWriter(path, StandardCharsets.UTF_8)` agar encoding konsisten.
- Menampilkan nilai tertinggi dan terendah di ringkasan.
- Memisahkan data siswa ke class tersendiri (pendekatan OOP).
