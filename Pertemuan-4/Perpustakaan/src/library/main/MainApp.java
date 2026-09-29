package library.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import library.model.Book;
import library.model.Member;
import library.service.LibraryService;
import library.exception.BookNotFoundException;
import library.exception.BookAlreadyBorrowedException;
import library.exception.BorrowLimitExceededException;

/**
 * MainApp adalah entry point aplikasi (method main).
 * Bertanggung jawab atas interaksi dengan pengguna melalui Scanner (console),
 * menampilkan menu, dan memanggil method-method di LibraryService.
 */
public class MainApp {

    private static Scanner scanner = new Scanner(System.in);
    private static LibraryService libraryService = new LibraryService();

    public static void main(String[] args) {
        // Data awal (dummy) supaya program langsung bisa dicoba tanpa input manual dulu
        inisialisasiDataAwal();

        boolean berjalan = true; // tipe data primitive boolean sebagai kondisi loop

        // Struktur kontrol looping: while, akan terus berjalan sampai user memilih "Keluar"
        while (berjalan) {
            tampilkanMenu();
            int pilihan = bacaPilihanMenu();

            // Struktur kontrol kondisional: switch-case untuk navigasi menu
            switch (pilihan) {
                case 1:
                    tambahBuku();
                    break;
                case 2:
                    tampilkanDaftarBuku();
                    break;
                case 3:
                    cariBuku();
                    break;
                case 4:
                    pinjamBuku();
                    break;
                case 5:
                    kembalikanBuku();
                    break;
                case 6:
                    tampilkanLaporan();
                    break;
                case 7:
                    berjalan = false;
                    System.out.println("Terima kasih telah menggunakan Sistem Perpustakaan Mini. Sampai jumpa!");
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan pilih angka 1-7.");
            }
            System.out.println();
        }

        scanner.close();
    }

    private static void tampilkanMenu() {
        System.out.println("==========================================");
        System.out.println("   SISTEM MANAJEMEN PERPUSTAKAAN MINI");
        System.out.println("==========================================");
        System.out.println("1. Tambah Buku");
        System.out.println("2. Daftar Buku");
        System.out.println("3. Cari Buku");
        System.out.println("4. Pinjam Buku");
        System.out.println("5. Kembalikan Buku");
        System.out.println("6. Laporan Perpustakaan");
        System.out.println("7. Keluar");
        System.out.print("Pilih menu (1-7): ");
    }

    /**
     * Membaca pilihan menu dari user dengan penanganan exception standar Java
     * (NumberFormatException) apabila user memasukkan input yang bukan angka.
     */
    private static int bacaPilihanMenu() {
        String input = scanner.nextLine();
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            // Exception bawaan Java: menangani input yang tidak berupa angka
            return -1; // nilai yang tidak akan cocok dengan case manapun -> masuk ke "default"
        }
    }

    // =========================================================
    // 1. TAMBAH BUKU
    // =========================================================
    private static void tambahBuku() {
        System.out.println("--- Tambah Buku Baru ---");
        System.out.print("Judul buku   : ");
        String judul = scanner.nextLine();
        System.out.print("Penulis      : ");
        String penulis = scanner.nextLine();

        int tahun;
        try {
            System.out.print("Tahun terbit : ");
            tahun = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Tahun tidak valid, menggunakan tahun 0 sebagai default.");
            tahun = 0;
        }

        System.out.print("Kategori     : ");
        String kategori = scanner.nextLine();

        Book bukuBaru = new Book(judul, penulis, tahun, kategori); // memanggil constructor Book
        libraryService.tambahBuku(bukuBaru);

        System.out.println("Buku \"" + judul + "\" berhasil ditambahkan.");
    }

    // =========================================================
    // 2. DAFTAR BUKU
    // =========================================================
    private static void tampilkanDaftarBuku() {
        System.out.println("--- Daftar Seluruh Buku ---");
        ArrayList<Book> semuaBuku = libraryService.getKoleksiBuku();

        if (semuaBuku.isEmpty()) {
            System.out.println("Belum ada buku dalam koleksi.");
            return;
        }

        System.out.println(String.format("%-30s | %-20s | %-4s | %-15s | %s",
                "Judul", "Penulis", "Thn", "Kategori", "Status"));
        System.out.println("-".repeat(95)); // manipulasi String: repeat()

        for (Book b : semuaBuku) { // looping
            System.out.println(b); // memanggil Book.toString()
        }

        System.out.println("\nJumlah buku per kategori:");
        HashMap<String, Integer> jumlahPerKategori = libraryService.hitungJumlahPerKategori();
        for (Map.Entry<String, Integer> entry : jumlahPerKategori.entrySet()) {
            System.out.println("- " + entry.getKey() + " : " + entry.getValue() + " buku");
        }
    }

    // =========================================================
    // 3. CARI BUKU
    // =========================================================
    private static void cariBuku() {
        System.out.print("Masukkan kata kunci (judul/kategori): ");
        String kataKunci = scanner.nextLine();

        ArrayList<Book> hasil = libraryService.cariBuku(kataKunci);

        if (hasil.isEmpty()) {
            System.out.println("Tidak ada buku yang cocok dengan kata kunci \"" + kataKunci + "\".");
        } else {
            System.out.println("Ditemukan " + hasil.size() + " buku:");
            for (Book b : hasil) {
                System.out.println(b);
            }
        }
    }

    // =========================================================
    // 4. PINJAM BUKU
    // =========================================================
    private static void pinjamBuku() {
        System.out.print("ID Anggota   : ");
        String idAnggota = scanner.nextLine().trim();

        // Jika anggota belum terdaftar, tawarkan pendaftaran cepat
        if (!libraryService.getAnggota().containsKey(idAnggota)) {
            System.out.print("Anggota belum terdaftar. Masukkan nama untuk mendaftar (kosongkan untuk batal): ");
            String nama = scanner.nextLine().trim();
            if (nama.isEmpty()) {
                System.out.println("Peminjaman dibatalkan.");
                return;
            }
            libraryService.tambahAnggota(new Member(idAnggota, nama));
            System.out.println("Anggota baru \"" + nama + "\" berhasil didaftarkan.");
        }

        System.out.print("Judul buku yang dipinjam: ");
        String judul = scanner.nextLine();

        // ===== Exception handling: menangani 3 kondisi wajib =====
        try {
            libraryService.pinjamBuku(idAnggota, judul);
            System.out.println("Buku \"" + judul + "\" berhasil dipinjam oleh anggota " + idAnggota + ".");
        } catch (BookNotFoundException e) {
            System.out.println("Gagal meminjam: " + e.getMessage());
        } catch (BookAlreadyBorrowedException e) {
            System.out.println("Gagal meminjam: " + e.getMessage());
        } catch (BorrowLimitExceededException e) {
            System.out.println("Gagal meminjam: " + e.getMessage());
        } catch (AssertionError e) {
            // Ditangkap agar program tidak crash total jika assertion gagal saat -ea aktif
            System.out.println("Data anggota tidak valid: " + e.getMessage());
        }
    }

    // =========================================================
    // 5. KEMBALIKAN BUKU
    // =========================================================
    private static void kembalikanBuku() {
        System.out.print("ID Anggota   : ");
        String idAnggota = scanner.nextLine().trim();
        System.out.print("Judul buku yang dikembalikan: ");
        String judul = scanner.nextLine();

        try {
            libraryService.kembalikanBuku(idAnggota, judul);
            System.out.println("Buku \"" + judul + "\" berhasil dikembalikan.");
        } catch (BookNotFoundException e) {
            System.out.println("Gagal mengembalikan: " + e.getMessage());
        } catch (AssertionError e) {
            System.out.println("Data anggota tidak valid: " + e.getMessage());
        }
    }

    // =========================================================
    // 6. LAPORAN PERPUSTAKAAN
    // =========================================================
    private static void tampilkanLaporan() {
        System.out.println("--- Laporan Perpustakaan ---");
        System.out.println("Jumlah total pinjaman   : " + libraryService.getTotalTransaksiPinjam());
        System.out.println("Anggota paling aktif    : " + libraryService.anggotaPalingAktif());
        System.out.println("Kategori paling populer : " + libraryService.kategoriPalingPopuler());
        System.out.println("Buku paling sering dipinjam: " + libraryService.bukuPalingSeringDipinjam());
    }

    // =========================================================
    // DATA AWAL (dummy) supaya aplikasi langsung bisa dicoba
    // =========================================================
    private static void inisialisasiDataAwal() {
        libraryService.tambahBuku(new Book("Laskar Pelangi", "Andrea Hirata", 2005, "Fiksi"));
        libraryService.tambahBuku(new Book("Bumi Manusia", "Pramoedya Ananta Toer", 1980, "Fiksi"));
        libraryService.tambahBuku(new Book("Filosofi Teras", "Henry Manampiring", 2018, "Non-Fiksi"));
        libraryService.tambahBuku(new Book("Algoritma dan Struktur Data", "Rinaldi Munir", 2016, "Teknologi"));
        libraryService.tambahBuku(new Book("Clean Code", "Robert C. Martin", 2008, "Teknologi"));

        libraryService.tambahAnggota(new Member("A001", "Sri Wulandari"));
        libraryService.tambahAnggota(new Member("A002", "Budi Santoso"));
    }
}
